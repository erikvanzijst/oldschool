package com.marketxs.messaging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelperInterface;

/**
 * <P>
 * The <code>SocketReader</code> takes care of reading the TCP socket of an
 * interface.
 * </P>
 * <P>
 * <UL>
 * <LI>com.marketxs.messaging.SocketReader.discardFile = /path/to/discarded-inbound-messages
 * </UL>
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 10.oct.2003
 */
public class SocketReader implements Runnable
{
	private TCPInterface iface = null;	// the interface instance this reader is working for
	private MessageInputStream in = null;
	private Thread thread = null;
	private int fifoSize = 20;	// default
	private int peakSize = 0;
	private PrioritizedObjectFIFO prioritizedFIFO = null;
	private Log logger = Log.instance(SocketReader.class);
	private final MessageInterceptor lastInterceptor;
	private MessageInterceptor firstInterceptor = null;
	private boolean running = false;
	private boolean stop = false;
	private boolean finished = false;
	private String filename = null;	// used to store discarded inbound messages
	private FileWriter fw = null;
	private final ManageableSocketReaderMBean mbean = this.new ManageableSocketReader();

	private long bytesTotal = 0;	// counts all bytes ever
	private long packetsTotal = 0;	// counts all packets ever received
	private long bytesSession = 0;	// only counts bytes for this connection
	private long packetsSession = 0;	// only counts packets for this connection
	private long corruptedMessages = 0;
	private long packetsDropped = 0;	// incoming messages dropped due to slow router core
		
	public SocketReader(TCPInterface iface)
	{
		try
		{
			fifoSize = Integer.parseInt(Configurator.getProperty("com.marketxs.messaqing.TCPInterface.readBufferCapacity", String.valueOf(fifoSize)));
		}
		catch(Exception e)
		{
		}
		
		filename = Configurator.getProperty("com.marketxs.messaging.SocketReader.discardFile");
		
		this.iface = iface;
		prioritizedFIFO = new PrioritizedObjectFIFO();
		lastInterceptor = this.new FinalInterceptor();	// the last child in the pipeline
		firstInterceptor = new DummyInterceptor();
		lastInterceptor.setParent(firstInterceptor);
		firstInterceptor.setChild(lastInterceptor);
		
		addInterceptor(this.new TtlDecrementor());
	}
	
	/**
	 * <P>
	 * The FinalInterceptor is the last interceptor in the chain that receives
	 * incoming messages. Its task is to enqueue the message in the reader's
	 * input buffer. When a new interceptor is added to the chain, it is
	 * chained right under the FinalInterceptor, so it will be the last
	 * interceptor to receive new messages before the FinalInterceptor.
	 * </P>
	 * 
	 * @author Erik van Zijst - erik@marketxs.com
	 */
	private class FinalInterceptor extends MessageInterceptor
	{
		public FinalInterceptor()
		{
		}
		
		public void intercept(Message message)
		{
			try
			{
				synchronized(prioritizedFIFO)
				{
					if(prioritizedFIFO.size() >= fifoSize)
					{
						prioritizedFIFO.removeLeastSignificant();
						packetsDropped++;
						logger.log("Receive buffer for interface " + iface.getId() + " full, dropping oldest pending message.", Log.SEVERITY_WARNING);
						
						if(fw != null)
						{
							try
							{
								fw.write((new Date()).toString() + ": " + message.toString() + "\n");
								fw.flush();
							}
							catch(Exception ie)
							{
								logger.log("Unable to log discarded inbound message: " + ie.getMessage(), Log.SEVERITY_WARNING);
							}
						}
					}
					prioritizedFIFO.add(message);
					peakSize = prioritizedFIFO.size() > peakSize ? prioritizedFIFO.size() : peakSize;
				}
				iface.statusChange();	// let the interface class notify all its observers
			}
			catch(InterruptedException ie)
			{
			}
		}
	}

	/**
	 * <P>
	 * This interceptor decrements the Time-To-Live field of every Routable
	 * message that passes by. It is added to the interceptor chain
	 * automatically.
	 * </P>
	 * 
	 * @author Erik van Zijst - erik@marketxs.com - 12.nov.2003
	 */
	private class TtlDecrementor extends MessageInterceptor
	{
		public void intercept(Message message)
		{
			if(message instanceof Routable)
			{
				Routable ud = (Routable)message;
				ud.setTtl(ud.getTtl() - 1);
			}
			super.intercept(message);
		}
	}
	
	/**
	 * <P>
	 * Simply adds this interceptor to the end of the chain of interceptors.
	 * The end means that incoming messages will be passed to the oldest
	 * interceptor. It is then delegated to the more recently added
	 * interceptors until it reaches the end of the pipeline. When the message
	 * still exists at the end of the pipeline (interceptors may decide not to
	 * forward a message to their child, effectively dropping it), it is
	 * automatically enqueued in the receive buffer.
	 * Effectively, the last interceptor to receive the message is the
	 * interceptor that was added most recently.
	 * </P>
	 * 
	 * @param interceptor
	 */
	public void addInterceptor(MessageInterceptor interceptor)
	{
		MessageInterceptor grandParent = lastInterceptor.getParent();
		lastInterceptor.setParent(interceptor);
		
		interceptor.setParent(grandParent);
		interceptor.setChild(lastInterceptor);
		
		grandParent.setChild(interceptor);
	}

	public void setInputStream(InputStream in)
	{
		this.in = new MessageInputStream(in);
	}

	/**
	 * <P>
	 * Main program loop for the socket reader. It continuously reads a packet
	 * from the socket's InputStream and stores it in the input buffer until
	 * an IO error occurs.
	 * </P>
	 */
	public void run()
	{
		byte[] buf;

		try
		{
			if(filename != null)
			{
				try
				{
					fw = new FileWriter(filename + "-" + iface.getId(), true);
					fw.write("\nFile opened at " + (new Date()).toString() + "\n\n");
					logger.log("Logfile for discarded (inbound) messages opened (" + filename + "-" + iface.getId() + ").", Log.SEVERITY_INFO);
				}
				catch(IOException e)
				{
					logger.log("File \"" + filename + "\" could not be opened or created. Discarded (inbound) messages will not be logged.", Log.SEVERITY_ERROR);
				}
			}

			while(!stop)
			{
				buf = in.getNextMessage();
				
				try
				{
					Message m = Message.deserialize(buf);
					logger.log("Interface " + iface.getId() + " received a " + m.length + " byte message of type " + m.getClass().getName() + ".", Log.SEVERITY_DEBUG);
					firstInterceptor.intercept(m); // run it through the interceptor pipeline

					// increment counters
					bytesTotal += buf.length;
					bytesSession += buf.length;
					packetsTotal++;
					packetsSession++;
					
				}
				catch(DeserializeException de)
				{
					logger.log("Corrupted message received on interface " + iface.getId() + ": " + LogUtil.getStackTrace(de), Log.SEVERITY_WARNING);
					logger.log("Corrupted message:\n" + TestClass.hexView(buf), Log.SEVERITY_WARNING);
					corruptedMessages++;
				}
			}
		}
		catch(IOException e)
		{
			logger.log("Network read error on interface " + iface.getId() + " (" + e.getMessage() + "); disconnecting.", Log.SEVERITY_WARNING);
		}
		finally
		{
			try
			{
				in.close();
			}
			catch(IOException e)
			{
			}

			if(filename != null)
			{
				try
				{
					fw.close();
				}
				catch(IOException ioe)
				{
				}
			}

			synchronized(iface.getMonitor())
			{
				// notify the interface thread that we're exiting
				finished = true;
				iface.getMonitor().notify();
			}

			logger.log("Socket reader for interface " + iface.getId() + " stopped.", Log.SEVERITY_INFO);
			running = false;
		}
	}
	
	/**
	 * <P>
	 * Checks to see if the reader thread is finishing up. This flag is set
	 * just before the sleeping interface thread is woken up. The interface
	 * thread uses this method to see which reader or writer it was that woke
	 * it. The interface then invokes join() on that reader or writer.
	 * </P>
	 * 
	 * @return	true if this thread has just woken the interface thread and is
	 * 	on the verge of terminating.
	 */
	public boolean isFinished()
	{
		return finished;
	}
	
	/**
	 * <P>
	 * Starts this socket-reading thread. If it was running already, this
	 * method does nothing. The thread can be started only once.
	 * </P>
	 */
	public synchronized void start()
	{
		if(!running)
		{
			stop = finished = false;
			bytesSession = packetsSession = 0;	// reset session counters
			thread = new Thread(this, "Socket reader for interface " + iface.getId());
			thread.setDaemon(true);
			thread.start();
			running = true;
			logger.log("Socket reader thread started for interface " + iface.getId(), Log.SEVERITY_INFO);
		}
	}
	
	/**
	 * <P>
	 * Interrupts the socket-reading thread. When this method returns successfully, the socket-reader
	 * will finish as soon as possible. If the reader had finished already (perhaps because of
	 * socket read error or remote socket close), the method returns
	 * immediately.
	 * </P>
	 */
	public synchronized void stop()
	{
		if(running)
		{
			stop = true;
			try
			{
				in.close();
			}
			catch(IOException e)
			{
			}
			thread.interrupt();
		}
	}
	
	/**
	 * <P>
	 * Waits for the reader thread to exit.
	 * </P>
	 * 
	 * @throws InterruptedException
	 */
	public void join() throws InterruptedException
	{
		thread.join();
	}
	
	public ManageableSocketReaderMBean getMBean()
	{
		return mbean;
	}

	/**
	 * <P>
	 * Returns true when the socket reader has at least one pending message.
	 * </P>
	 * 
	 * @return	true when the socket reader has at least one pending message.
	 */
	public boolean isReady()
	{
		return !prioritizedFIFO.isEmpty();
	}
	
	/**
	 * <P>
	 * Blocks until the socket reader has at least one pending message.
	 * </P>
	 * 
	 * @return	the oldest message (with highest priority)
	 * @throws InterruptedException
	 */
	public Message read() throws InterruptedException
	{
		return (Message)prioritizedFIFO.remove();
	}
	
	public long getBytesTotal()
	{
		return bytesTotal;
	}
	
	public long getBytesSession()
	{
		return bytesSession;
	}
	
	public long getPacketsTotal()
	{
		return packetsTotal;
	}
	
	public long getPacketsSession()
	{
		return packetsSession;
	}
	
	/**
	 * <P>
	 * The MBean interface of the ManageableSocketReader inner class.
	 * </P>
	 * 
	 * @author Erik van Zijst - 10.Dec.2003
	 */
	public interface ManageableSocketReaderMBean
	{
		/**
		 * <P>
		 * Returns the capacity of the reader's input buffer. This is the
		 * maximum number of messages that can be pending before messages are
		 * dropped.
		 * </P>
		 * 
		 * @return	the reader's capacity.
		 */
		public int getCapacity();
		
		/**
		 * <P>
		 * Returns the size of the message input buffer. This is the number
		 * of pending messages to be processed. Delivery will be delayed
		 * when this number is > 0.
		 * </P>
		 * <P>
		 * Generally, messages will only queue up in the input buffer if the
		 * router has insufficient cpu resources to keep up.
		 * </P>
		 * 
		 * @return
		 */
		public int getSize();
		
		public int getPeakSize();
		
		public long getMessagesDropped();
		
		public String[] getPendingMessages();

		/**
		 * <P>
		 * Resets the reader's counters for peakSize and messagesDropped.
		 * </P>
		 */
		public void resetCounters();
	}
	
	public class ManageableSocketReader implements ManageableSocketReaderMBean, ManagementHelperInterface
	{
		private String description = "The socket reader is the component that reads data from the established TCP " +
			"connection. Together with a SocketReader instance it does the actual data transfer of packets for an " +
			"interface. It uses an internal buffer to store newly received messages until the router core processes them. " +
			"The socket reader also implements the interceptor chain for inbound messages.";

		private Map operationParameterNameMap = new HashMap();
		private Map attributeDescriptionMap = new HashMap();
		private Map operationDescriptionMap = new HashMap();
		private Map operationParameterDescriptionMap = new HashMap();

		public ManageableSocketReader()
		{
			attributeDescriptionMap.put("Capacity", "The capacity of the input buffer. When this is reached, new incoming messages will cause data loss.");
			attributeDescriptionMap.put("MessagesDropped", "The number of messages that have been dropped due to buffer overflow, since the last counter-reset.");
			attributeDescriptionMap.put("PeakSize", "The largest number of messages that have been in the buffer simultanously, since the last counter-reset.");
			attributeDescriptionMap.put("PendingMessages", "Displays the actual messages currently in the buffer. For some messages the entire content may be displayed.");
			attributeDescriptionMap.put("Size", "The number received messages currently in the input buffer that are waiting to be processed. Delivery may be delayed when this number is > 0.");
		
			operationDescriptionMap.put("resetCounters", "Resets the MessagesDropped and PeakSize counters to zero.");
		}

		/**
		 * <P>
		 * Returns the capacity of the reader's input buffer. This is the
		 * maximum number of messages that can be pending before messages are
		 * dropped.
		 * </P>
		 * 
		 * @return
		 */
		public int getCapacity()
		{
			return fifoSize;
		}
		
		/**
		 * <P>
		 * Returns the size of the message input buffer. This is the number
		 * of pending messages to be processed. Delivery will be delayed
		 * when this number is > 0.
		 * </P>
		 * <P>
		 * Generally, messages will only queue up in the input buffer if the
		 * router has insufficient cpu resources to keep up.
		 * </P>
		 * 
		 * @return
		 */
		public int getSize()
		{
			return prioritizedFIFO.size();
		}
		
		public int getPeakSize()
		{
			return peakSize;
		}
		
		public long getMessagesDropped()
		{
			return packetsDropped;
		}
		
		public String[] getPendingMessages()
		{
			Object[] values = prioritizedFIFO.toArray();
			String[] messages = new String[values.length];
			
			for (int i = 0; i < messages.length; i++)
			{
				messages[i] = values[i].toString();
			}
			
			return messages;
		}
		
		/**
		 * <P>
		 * Resets the reader's counters for peakSize and messagesDropped.
		 * </P>
		 */
		public void resetCounters()
		{
			packetsDropped = peakSize = 0;	// not properly synchronized, but not critical either
		}
	
		/**
		 * <P>
		 * Returns the description string that is displayed in JMX.
		 * </P>
		 */
		public String getDescription()
		{
			return description;
		}
	
		public Map getOperationParameterNameMap()
		{
			return operationParameterNameMap;
		}
	
		public Map getAttributeDescriptionMap()
		{
			return attributeDescriptionMap;
		}
	
		public Map getOperationDescriptionMap()
		{
			return operationDescriptionMap;
		}
	
		public Map getOperationParameterDescriptionMap()
		{
			return operationParameterDescriptionMap;
		}
	}
}
