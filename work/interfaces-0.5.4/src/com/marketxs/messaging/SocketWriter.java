package com.marketxs.messaging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelperInterface;

/**
 * <P>
 * The SocketWriter class is used by the TCPInterface to handle outgoing
 * packets. Packets are buffered in the interface while this class
 * continuously dequeues packets and transmits them over the TCP connection to
 * the peer.
 * </P>
 * <P>
 * When the writer is started using start(), it spawns a daemon thread that
 * starts tranmitting each packet it dequeues. During operation, the
 * isFinished() method returns false. When stop() is invoked (usually done by
 * interface thread), the writer finishes the packet it was processing and
 * then returns, setting the isFinished() flag to true.
 * The writer can be re-used by invoking start() again.
 * </P>
 * 
 * <P>
 * <UL>
 * <LI>com.marketxs.messaging.SocketWriter.discardFile = /path/to/discarded-outbound-messages
 * </UL>
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 10.oct.2003
 */
public class SocketWriter implements Runnable
{
	private TCPInterface iface = null;	// the interface instance this writer is working for
	private MessageOutputStream out = null;
	private Thread thread = null;
	private int fifoSize = 20;
	private int peakSize = 0;
	private PrioritizedObjectFIFO prioritizedFIFO = null;
	private final MessageInterceptor lastInterceptor;
	private MessageInterceptor firstInterceptor = null;
	private Log logger = Log.instance(SocketWriter.class);
	private boolean running = false;
	private boolean stop = false;
	private boolean finished = false;
	private String filename = null;	// file used to dump discarded messages
	private FileWriter fw = null;
	private final ManageableSocketWriterMBean mbean = this.new ManageableSocketWriter();
	
	private long bytesTotal = 0;	// counts all bytes ever
	private long packetsTotal = 0;	// counts all packets ever received
	private long bytesSession = 0;	// only counts bytes for this connection
	private long packetsSession = 0;	// only counts packets for this connection
	private long packetsDropped = 0;	// counts dropped messages/packets
	
	public SocketWriter(TCPInterface iface)
	{
		try
		{
			fifoSize = Integer.parseInt(Configurator.getProperty("com.marketxs.messaqing.TCPInterface.writeBufferCapacity", String.valueOf(fifoSize)));
		}
		catch(Exception e)
		{
		}
		
		filename = Configurator.getProperty("com.marketxs.messaging.SocketWriter.discardFile");

		this.iface = iface;
		prioritizedFIFO = new PrioritizedObjectFIFO();
		lastInterceptor = this.new FinalInterceptor();	// the last child in the pipeline
		firstInterceptor = lastInterceptor;
	}
	
	private class FinalInterceptor extends MessageInterceptor
	{
		public FinalInterceptor()
		{
		}
		
		public void intercept(Message message)
		{
			if(running)
			{
				synchronized(prioritizedFIFO)
				{
					try
					{
						if(isFull())
						{
							prioritizedFIFO.removeLeastSignificant();
							packetsDropped++;
							logger.log("Write buffer of interface " + iface.getId() + " is full; discarding oldest pending message.", Log.SEVERITY_WARNING);
							
							if(fw != null)
							{
								try
								{
									fw.write((new Date()).toString() + ": " + message.toString() + "\n");
									fw.flush();
								}
								catch(IOException ie)
								{
									logger.log("Unable to log discarded outbound message: " + ie.getMessage(), Log.SEVERITY_WARNING);
								}
							}
						}
						prioritizedFIFO.add(message);
						peakSize = prioritizedFIFO.size() > peakSize ? prioritizedFIFO.size() : peakSize;
					}
					catch(InterruptedException ie)
					{
						logger.log("Interrupted while removing discarding the oldest message from the output buffer of interface " + iface.getId() + ". This should never happen. It is likely that a message was lost.", Log.SEVERITY_ERROR);
					}
				}
			}
			else
			{
				int expunged = prioritizedFIFO.size();
				prioritizedFIFO.removeAll();

				logger.log("Message was sent to a disconnected interface (" + iface.getId() + "), message discarded, " + expunged + " pending messages removed from the output buffer.", Log.SEVERITY_DEBUG);
			}
		}
	}
	
	public void addInterceptor(MessageInterceptor interceptor)
	{
		synchronized(lastInterceptor)
		{
			firstInterceptor.setParent(interceptor);
			interceptor.setChild(firstInterceptor);
			firstInterceptor = interceptor;
		}
	}
	
	public void setOutputStream(OutputStream out)
	{
		this.out = new MessageOutputStream(out);
	}
	
	public void run()
	{
		try
		{
			if(filename != null)
			{
				try
				{
					fw = new FileWriter(filename + "-" + iface.getId(), true);
					fw.write("\nFile opened at " + (new Date()).toString() + "\n\n");
					logger.log("Logfile for discarded (outbound) messages opened (" + filename + "-" + iface.getId() + ").", Log.SEVERITY_INFO);
				}
				catch(IOException e)
				{
					logger.log("File \"" + filename + "\" could not be opened or created. Discarded (outbound) messages will not be logged.", Log.SEVERITY_ERROR);
				}
			}

			while(!stop)
			{
				Message m = (Message)prioritizedFIFO.remove();
				
				try
				{
					m.serialize();
					byte[] raw = m.getRawData(); 
					out.writeMessage(raw);
					out.flush();
				
					// increment counters
					bytesTotal += raw.length;
					bytesSession += raw.length;
					packetsTotal++;
					packetsSession++;
				}
				catch(SerializeException se)
				{
					logger.log("Exception while serializing outbound message: " + LogUtil.getStackTrace(se), Log.SEVERITY_ERROR);
				}
			}
		}
		catch(InterruptedException e)
		{
			logger.log("Socket writer thread of interface " + iface.getId() + " was interrupted. Disconnecting from peer.", Log.SEVERITY_WARNING);
		}
		catch(IOException e)
		{
			logger.log("Network write error on interface " + iface.getId() + " (" + e.getMessage() + "); disconnecting.", Log.SEVERITY_WARNING);
		}
		finally
		{
			try
			{
				out.close();
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

			logger.log("Socket writer for interface " + iface.getId() + " stopped.", Log.SEVERITY_INFO);
			running = false;
		}
	}
	
	/**
	 * <P>
	 * Checks to see if the writer thread is finishing up. This flag is set
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
	 * Starts this socket-writing thread. If it was running already, this
	 * method does nothing. The thread can be started only once.
	 * </P>
	 */
	public synchronized void start()
	{
		if(!running)
		{
			stop = finished = false;
			bytesSession = packetsSession = 0;	// reset session counters
			thread = new Thread(this, "Socket writer for interface " + iface.getId());
			thread.setDaemon(true);
			thread.start();
			running = true;
			logger.log("Socket writer thread started for interface " + iface.getId(), Log.SEVERITY_INFO);
		}
	}
	
	/**
	 * <P>
	 * Interrupts the socket-writing thread. When this method returns successfully, the socket-writer
	 * will finish as soon as possible. If the writer had finished already (perhaps because of
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
				out.close();
			}
			catch(IOException e)
			{
			}
			thread.interrupt();
		}
	}
	
	/**
	 * <P>
	 * Waits for the writer thread to exit.
	 * </P>
	 * 
	 * @throws InterruptedException
	 */
	public void join() throws InterruptedException
	{
		thread.join();
	}
	
	public ManageableSocketWriterMBean getMBean()
	{
		return mbean;
	}
	
	public boolean isFull()
	{
		return prioritizedFIFO.size() >= fifoSize;
	}
	
	/**
	 * <P>
	 * Adds a message to the output buffer of the socket writer. This method
	 * never blocks. When the buffer is full, the oldest message will be
	 * discarded to make room for the new message (FIFO). Also, a warning will
	 * logged.
	 * </P>
	 * 
	 * @param message	the message to be transmitted.
	 */
	public void send(Message message)
	{
		firstInterceptor.intercept(message);
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
	 * The MBean interface of the ManageableSocketWriter inner class.
	 * </P>
	 * 
	 * @author Erik van Zijst - 10.Dec.2003
	 */
	public interface ManageableSocketWriterMBean
	{
		/**
		 * <P>
		 * Returns the capacity of the writer's output buffer. This is the
		 * maximum number of messages that can be pending before messages are
		 * dropped.
		 * </P>
		 * 
		 * @return
		 */
		public int getCapacity();
		
		/**
		 * <P>
		 * Returns the size of the message output buffer. This is the number
		 * of pending messages to be transferred. Delivery will be delayed
		 * when this number is > 0.
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
		 * Resets the writer's counters for peakSize and messagesDropped.
		 * </P>
		 */
		public void resetCounters();
	}
	
	public class ManageableSocketWriter implements ManageableSocketWriterMBean, ManagementHelperInterface
	{
		private String description = "The SocketWriter class is used by the TCPInterface to handle outgoing " +
			"packets. Packets are buffered in the outbound queue while this class " +
			"continuously dequeues packets and transmits them over the TCP connection to " +
			"the peer. When the writer is started, it spawns a daemon thread that tranmits each packet it dequeues. " +
			"The socket writer also implements the interceptor chain for inbound messages.";

		public ManageableSocketWriter()
		{
			attributeDescriptionMap.put("Capacity", "The capacity of the output buffer. When this is reached, new outgoing messages will cause data loss.");
			attributeDescriptionMap.put("MessagesDropped", "The number of messages that have been dropped due to buffer overflow, since the last counter-reset.");
			attributeDescriptionMap.put("PeakSize", "The largest number of messages that have been in the buffer simultanously, since the last counter-reset.");
			attributeDescriptionMap.put("PendingMessages", "Displays the actual messages currently in the buffer. For some messages the entire content may be displayed.");
			attributeDescriptionMap.put("Size", "The number of messages currently in the outbound buffer, waiting to be transferred. Delivery may be delayed when this number is > 0.");
		
			operationDescriptionMap.put("resetCounters", "Resets the MessagesDropped and PeakSize counters to zero.");
		}

		private Map operationParameterNameMap = new HashMap();
		private Map attributeDescriptionMap = new HashMap();
		private Map operationDescriptionMap = new HashMap();
		private Map operationParameterDescriptionMap = new HashMap();

		/**
		 * <P>
		 * Returns the capacity of the writer's output buffer. This is the
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
		 * Returns the size of the message output buffer. This is the number
		 * of pending messages to be transferred. Delivery will be delayed
		 * when this number is > 0.
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
			Object[] values = prioritizedFIFO.toArray();	// make a safe copy of the queue
			String[] messages = new String[values.length];
			
			for (int i = 0; i < messages.length; i++)
			{
				messages[i] = values[i].toString();
			}
			
			return messages;
		}
		
		/**
		 * <P>
		 * Resets the writer's counters for peakSize and messagesDropped.
		 * </P>
		 * 
		 */
		public void resetCounters()
		{
			packetsDropped = peakSize = 0;	// not properly synchronized, but not critical either
		}
	
		/**
		 * <P>
		 * Returns the description strings that are displayed in JMX.
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
