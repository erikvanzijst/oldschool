package com.marketxs.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <P>
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 08.jan.2004
 */
public class MessageInputStream
{
	// Note that these MUST be equal to the values in MessageOutputStream!
	private static final int EOM = 126;
	private static final int escapeByte = 125;
	private static final boolean useDelimiters = true;

	private InputStream in = null;
	
	public MessageInputStream(InputStream in)
	{
		this.in = in;
	}

	public void close() throws IOException
	{
		in.close();
	}
	
	public byte[] getNextMessage() throws IOException
	{
		if(useDelimiters)
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			boolean esc = false;
			int i;
		
			while( (i = in.read()) != -1 && i != EOM)
			{
				if(i == escapeByte)
				{
					if(esc)	// if previous was esc as well
					{
						bos.write(escapeByte);
						esc = false;	// reset
					}
					else 
					{
						esc = true;
					}
				}
				else
				{
					if(esc)	// if previous was a single escapeByte occurance
					{
						bos.write(EOM);
					}
					bos.write(i);
					esc = false;
				}
			}
			
			if(esc)
			{
				bos.write(i);
			}
		
			if(i == -1)
			{
				throw new IOException("Connection closed by peer.");
			}
		
			return bos.toByteArray();
		}
		else
		{
			byte[] buf = new byte[8192];
			int count = in.read(buf);
			if(count == -1)
			{
				throw new IOException("Connection closed by peer.");
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bos.write(buf, 0, count);
			return bos.toByteArray();
		}
	}
}
