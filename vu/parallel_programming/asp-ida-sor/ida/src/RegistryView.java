import java.rmi.Naming;

public class RegistryView
{
	public static void main(String[] argv) throws Exception
	{
		String url = "rmi://localhost";
		if(argv.length > 0)
			url = argv[0];

		String[] names = Naming.list(url);

		for(int nX = 0; nX < names.length; nX++)
			System.out.println(names[nX]);
	}
}
