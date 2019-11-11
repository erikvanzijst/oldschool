package nl.vu.ip;

public class BankPay
{
	public static void main(String[] argv) throws Exception
	{
		System.out.println("usage: java nl.vu.ip.BankPay <bankaddress> <bankport> <src> <dest> <amount>");
		int retval = BankClient.bank_pay(argv[0], Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Integer.parseInt(argv[3]), Float.parseFloat(argv[4]));
		System.out.println("Payment returned " + retval);
	}
}
