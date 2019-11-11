package nl.vu.ip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetAddress;

/**
 * This class is the interface to the bank server and uses sockets and the
 * bank's own protocol to do money transfers and to verify earlier transfers.
 * <BR> The protocol uses structure-like messages to communicate between client
 * and bank. The protocol's wire format is documented in a separate file.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class BankClient
{
	/**
	 * This method is used to transfer money from one bank account to the
	 * other. It takes the location information of the bank server, as well
	 * as the accounts and amount to transfer and sends a serializes
	 * message to the bank using TCP sockets. <BR>
	 * The protocol's wire format is documented in a separate file. <BR>
	 *
	 * @param	bankAddress the hostname or IP address of the bank
	 * 	server.
	 * @param	bankPort the remote port the bank server is listening
	 * 	to.
	 * @param	src the bank account to withdraw the money from.
	 * @param	dest the destination bank account.
	 * @param	amount the amount of money to transfer.
	 * @throws	IOException if there was a communication problem.
	 * @return	the certificate of the money transfer.
	 */
	public static int bank_pay(String bankAddress, int bankPort, int src, int dest, float amount) throws IOException
	{
		// prepare a protocol message
		Message m = new Message();
		m.command = Message.COMMAND_BANK_PAY;
		m.src = src;
		m.dest = dest;
		m.amount = amount;

		Socket socket = null;

		try
		{
			// connect to the bank and send the pay-command
			socket = new Socket(InetAddress.getByName(bankAddress), bankPort);
			OutputStream out = socket.getOutputStream();
			out.write(m.serialize().getBytes());

			// receive the bank's response
			InputStream in = socket.getInputStream();
			StringBuffer sb = new StringBuffer();
			char _char;

			// read until End Of Stream or End Of Text
			while( (_char = (char)in.read()) != (char)Message.ETX)
			{
				if((int)_char == -1)
					break;	// end of stream

				sb.append(_char);
			}

			m.deserialize(sb.toString());
		}
		catch(Exception e)
		{
			if(e instanceof IOException)
				throw (IOException)e;

			System.out.println("Error doing payment.");
			e.printStackTrace();
			m.retval = -1;
		}
		finally
		{
			try
			{
				// never forget to close the connection
				socket.close();
			}
			catch(Exception e)
			{
			}
		}

		// return the retval of the returned protocol message
		return m.retval;
	}

	/**
	 * This method is used to verify an earlier money transfer. Given all
	 * the transaction's properties such as bank accounts and amount,
	 * together with the transaction certificate that the bank generated,
	 * this method contacts the bank and lets the bank verify whether the
	 * described transaction was indeed executed earlier. <BR>
	 * The protocol's wire format is documented in a separate file. <BR>
	 *
	 * @param	bankAddress the hostname or IP address of the bank
	 * 	server.
	 * @param	bankPort the remote port the bank server is listening
	 * 	to.
	 * @param	src the bank account the money was withdrawn from.
	 * @param	dest the destination bank account.
	 * @param	amount the amount of money transferred.
	 * @param	certificate the certificate that was generated at time
	 * 	of the transaction.
	 * @throws	IOException if there was a communication problem.
	 * @return	0 if the described transaction is indeed a vaild one,
	 * 	-1 otherwise.
	 */
	public static int bank_check(String bankAddress, int bankPort, int src, int dest, float amount, int certificate) throws IOException
	{
		// prepare a protocol message
		Message m = new Message();
		m.command = Message.COMMAND_BANK_CHECK;
		m.src = src;
		m.dest = dest;
		m.amount = amount;
		m.certificate = certificate;

		Socket socket = null;

		System.out.println("Verifying the payment of " + amount + " from account " + src + " to account " + dest + " with certificate " + certificate);
		try
		{
			// connect to the bank and send the check-command
			socket = new Socket(InetAddress.getByName(bankAddress), bankPort);
			OutputStream out = socket.getOutputStream();
			out.write(m.serialize().getBytes());

			// receive the bank's response
			InputStream in = socket.getInputStream();
			StringBuffer sb = new StringBuffer();
			char _char;

			// read until End Of Stream or End Of Text
			while( (_char = (char)in.read()) != (char)Message.ETX)
			{
				if((int)_char == -1)
					break;	// end of stream

				sb.append(_char);
			}

			m.deserialize(sb.toString());
		}
		catch(Exception e)
		{
			if(e instanceof IOException)
				throw (IOException)e;

			System.out.println("Error checking payment.");
			e.printStackTrace();
			m.retval = -1;
		}
		finally
		{
			try
			{
				// never forget to close the connection
				socket.close();
			}
			catch(Exception e)
			{
			}
		}

		// return the retval of the returned protocol message
		return m.retval;
	}
}
