/**
 * This application runs 2 threads, that each print their own (sub)string. When
 * these threads run sumultaneously, it ensures that threads wait for each
 * other before printing their own string. This is similar to the Syn1
 * application. In addition to Syn1, this class also assigns every thread a
 * number, indicating its order number. This allows the threads to wait for one
 * another in the specified order. <BR>
 * Contrary to Syn1, this application will never print the same string twice,
 * but instead will always wait for its turn, then print its string and signal
 * the other threads when it's done. After all threads have been notified, the
 * thread with the next order number will run and so on.
 *
 * @author	Erik van Zijst - erik@marketxs.com - 21.feb.2003
 * @author	Sander van Loo - sander@marketxs.com - 21.feb.2003
 */
public class Syn2 extends Thread
{
	private static Object mutex = new Object();
	private static int current = 0;

	private int order = 0;
	private int members = 0;
	private char[] characters = null;

	public Syn2(String string, int order, int members)
	{
		this.order = order;
		this.members = members;
		this.characters = string.toCharArray();
	}

	public static void main(String[] argv)
	{
		Syn2 ab = new Syn2("ab", 0, 2);	// number '0', will run first
		Syn2 cd = new Syn2("cd\n", 1, 2);	// number '1', will run after '0'

		ab.start();
		cd.start();
	}

	public void run()
	{
		for(int nX = 0; nX < 10; nX++)
		{
			synchronized(mutex)
			{
				while(current != order)	// wait for my turn
				{
					try
					{
						mutex.wait();
					}
					catch(InterruptedException e)
					{
					}
				}

				for(int mX = 0; mX < characters.length; mX++)
				{
					System.out.print(characters[mX]);
					try
					{
						sleep(0, 100);	// maximize need for synchronization
					}
					catch(InterruptedException e)
					{
					}
				}

				// select the next thread and notify all
				current = ++current % members;
				mutex.notifyAll();
			}
		}
	}
}
