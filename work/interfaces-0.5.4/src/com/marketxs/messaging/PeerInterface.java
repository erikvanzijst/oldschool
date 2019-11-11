package com.marketxs.messaging;

import java.net.InetAddress;
import java.util.Observable;

/**
 * <P>
 * This interface defines the functionality of an interface object. The
 * default implementation for it is the TCPInterface that uses a single TCP
 * connection to do all communication with the remote peer.
 * </P>
 * 
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public interface PeerInterface
{
	/**
	 * <P>
	 * Every interface has an identifier. It is unique among the interfaces of
	 * the local router.
	 * </P>
	 * 
	 * @return
	 */
	public String getId();
	
	public void setPeerAddress(Address peer);
	
	/**
	 * <P>
	 * Returns the overlay address of the peer interface.
	 * </P>
	 * 
	 * @return	the overlay address of the peer interface.
	 */
	public Address getPeerAddress();
	
	public void setRemoteHost(InetAddress addr);
	
	public InetAddress getRemoteHost();
	
	public void setPort(int port);
	
	public int getPort();
	
	/**
	 * <P>
	 * Configures this interface instance. The interface will automatically be
	 * started; i.e. it will connect to its peer. When a connection is
	 * established, the isConnected() method will return true.
	 * If the interface had already been configured previously and was running
	 * when setPeer() was invoked, the interface will first be shut down and
	 * then restarted.
	 * </P>
	 * 
	 * @param peerAddress	the overlay address of the remote host to connect
	 * 	to.
	 * @param host	the IP address of the remote host.
	 * @param port	the TCP port the remote host listens to.
	 * @throws InterruptedException	when the interface was already running,
	 * 	but this method was unable to shut it down. In this case, the
	 *	configuration will not be changed.
	 */
	public void setPeer(Address peerAddress, InetAddress host, int port) throws InterruptedException;

	/**
	 * <P>
	 * Starts the interface. This means that it will try to establish a
	 * connection to its peer. When a connection is established, the
	 * isConnected() method will return true.
	 * </P>
	 */
	public void enable();

	public void disable();

	/**
	 * <P>
	 * When a connection is established, the isConnected() method will return
	 * true.
	 * </P>
	 * 
	 * @return
	 */
	public boolean isConnected();
	
	public InterfaceState getState();
	
	public String getStatusString();

	/**
	 * <P>
	 * Returns the cost metric for this interface. A special value
	 * DistanceVector.UNREACHABLE is defined to identify an infinite cost.
	 * Whenever the cost metric changes, the Observable that can be retrieved
	 * with the getObservable() method sends a notification.
	 * </P>
	 * 
	 * @return the current cost metric that is an indication of the quality of
	 * 	the link to the peer. It ranges from 0 to DistanceVector.UNREACHABLE.
	 */
	public int getCostMetric();
	
	/**
	 * <P>
	 * Returns the Observable instance on which Observer classes can
	 * "subscribe". They will then be notified whenever the interface's status
	 * changes. This can be new available messages that can be read from the
	 * input buffer or a link cost metric change to respond to. When the
	 * Observable sends a notification, the interface's isReadyForReading()
	 * method should be checked as well as the getCostMetric() method to see
	 * if it has changed.
	 * </P>
	 * 
	 * @return
	 */
	public Observable getObservable();
	
	/**
	 * <P>
	 * Queues a message for transmission.
	 * </P>
	 * 
	 * @param message
	 */
	public void send(Message message);
	
	/**
	 * <P>
	 * Returns true if this interface has messages waiting in its input
	 * buffer. When this method returns true, the read() method will always
	 * return immediately with the oldest pending message. When the ready flag
	 * is false, the read method will block until at least one message is
	 * received over the network.
	 * </P>
	 * 
	 * @return
	 */
	public boolean isReadyForReading();
	
	/**
	 * <P>
	 * Returns true is the send buffer of this interface is not full. This
	 * means the send() method can safely be invoked without risk of message
	 * loss.
	 * </P>
	 * 
	 * @return
	 */
	public boolean isReadyForWriting();

	/**
	 * <P>
	 * Read a message from the input buffer. When nothing is available this
	 * method blocks until at least one message has arrived. This method is
	 * only guaranteed to return without blocking if isReadyForReading()
	 * returns true.
	 * </P>
	 * 
	 * @return	the oldest pending message.
	 * @throws InterruptedException	if the calling thread was interrupted
	 * 	while waiting for an incoming message.
	 */
	public Message read() throws InterruptedException;
	
	public long getTotalBytesIn();
	
	public long getTotalBytesOut();
	
	public long getTotalPacketsIn();
	
	public long getTotalPacketsOut();
}
