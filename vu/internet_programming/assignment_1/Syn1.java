public class Syn1 extends Thread
{
	private char[] characters = null;

	public Syn1(String string)
	{
		this.characters = string.toCharArray();
	}

	public static void main(String[] argv)
	{
		Syn1 helloWorld = new Syn1("Hello world\n");
		Syn1 bonjourMonde = new Syn1("Bonjour monde\n");

		helloWorld.start();
		bonjourMonde.start();
	}
	
	public void run()
	{
		for(int nX = 0; nX < 10; nX++)
		{
			synchronized(Syn1.class)
			{
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
			}
		}
	}
}

