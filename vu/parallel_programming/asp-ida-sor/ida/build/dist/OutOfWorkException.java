package nl.vu.pp.distributed;

/**
 * This RuntimeException is thrown by the central JobManager's
 * <code>get()</code> method when both the manager's internal job queue is
 * empty and closed for new input.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.jan.2003
 */
public class OutOfWorkException extends RuntimeException
{
	/**
	 * Default Constructor.
	 */
	public OutOfWorkException()
	{
		super();
	}

	/**
	 * Constructs a exception with detailed error message.
	 *
	 * @param   s   message that can be retrieved by the
	 *              <CODE>getMessage()</CODE> method.
	 */
	public OutOfWorkException(String s)
	{
		super(s);
	}
}
