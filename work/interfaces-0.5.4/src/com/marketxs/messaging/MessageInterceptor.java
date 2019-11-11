package com.marketxs.messaging;

/**
 * 
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public abstract class MessageInterceptor
{
	private MessageInterceptor parent = null;
	private MessageInterceptor child = null;
	
	/**
	 * The default implementation of this method directly calls the delegate()
	 * method, passing the message on to the next interceptor in the pipeline.
	 * When overriding this message in a subclass, it is important not to
	 * forget to pass the message to your child interceptor using delegate().
	 * 
	 * @param message
	 */
	public void intercept(Message message)
	{
		delegate(message);
	}
	
	/**
	 * Passes the messages directly to the child interceptor (if present).
	 * This method is normally called at the very end of the intercept()
	 * method.
	 * 
	 * @param message
	 */
	protected final void delegate(Message message)
	{
		if(child != null)
			child.intercept(message);
	}
	
	public final void setParent(MessageInterceptor interceptor)
	{
		this.parent = interceptor;
	}
	
	public final MessageInterceptor getParent()
	{
		return parent;
	}
	
	public final void setChild(MessageInterceptor interceptor)
	{
		this.child = interceptor;
	}
	
	public final MessageInterceptor getChild()
	{
		return child;
	}
}
