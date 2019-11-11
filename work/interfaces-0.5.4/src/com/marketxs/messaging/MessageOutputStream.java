package com.marketxs.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <P>
 * This implementation uses an escape character to separate messages. It uses
 * byte stuffing to escape the natural occurences of the escape character in
 * the original data.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 08.jan.2004
 */
public class MessageOutputStream
{
	// Note that these MUST be equal to the values in MessageInputStream!
	private static final int EOM = 126;
	private static final int escapeByte = 125;
	private static final boolean useDelimiters = true;

	private OutputStream out = null;
	
	public MessageOutputStream(OutputStream out)
	{
		this.out = out;
	}
	
	public void flush() throws IOException
	{
		out.flush();
	}
	
	/**
	 * 
	 * @param b
	 * @throws IOException
	 */
	public void writeMessage(byte[] b) throws IOException
	{
		if(useDelimiters)
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
			for(int nX = 0; nX < b.length; nX++)
			{
				if(b[nX] == EOM)
				{
					bos.write(escapeByte);
				}
				else if(b[nX] == escapeByte)
				{
					bos.write(new byte[]{escapeByte, escapeByte});
				}
				else
				{
					bos.write(b[nX]);
				}
			}
			bos.write(EOM);
		
			out.write(bos.toByteArray());
		}
		else
		{
			out.write(b);
		}
	}
	
	public void close() throws IOException
	{
		out.close();
	}
}
