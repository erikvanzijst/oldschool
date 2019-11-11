package nl.vu.ip;

import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

/**
 * This class represents a single ad. <BR>
 * An ad is really an image file in either GIF or JPG format. This class is
 * used to transfer the images files. When serialized over the wire, the object
 * preserves the original filename, so that the ad can later be saved by a
 * different client or server using the same filename.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class Ad implements Serializable
{
	private String filename = null;
	private byte[] image;
	
	/**
	 * Creates a new ad with the specified relative filename.
	 *
	 * @param	filename the image file's original, relative filename
	 */
	public Ad(String filename)
	{
		this.filename = filename;
	}

	/**
	 * The ad's filename is supposed to be a relative name only. Because
	 * the ad is transferred to and from different machines, the absolute
	 * path is irrelevant.
	 *
	 * @param	filename the image file's original, relative filename.
	 */
	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	/**
	 * Returns the ad's original filename. <BR>
	 * The ad's filename is supposed to be a relative name only. Because
	 * the ad is transferred to and from different machines, the absolute
	 * path is irrelevant.
	 *
	 * @return	the ad's original filename.
	 */
	public String getFilename()
	{
		return filename;
	}

	/**
	 * Load's the image data for this ad into memory. <BR>
	 * The InputStream is typically a FileInputStream, connected to a GIF
	 * or JPG file.
	 *
	 * @param	in the InputStream that contains the binary image data.
	 * @throws	IOException if there was a problem reading the
	 * 	InputStream.
	 */
	public void load(InputStream in) throws IOException
	{
		int count;
		byte[] buf = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		while( (count = in.read(buf, 0, buf.length)) != -1)
			out.write(buf, 0, count);

		image = out.toByteArray();
	}
	
	/**
	 * Stores the ad's image data in the supplied OutputStream.
	 *
	 * @param	out the OutputStream to which the binary image data is
	 * 	written.
	 * @throws	IOException if there was a problem writing to the
	 * 	OutputStream.
	 */
	public void store(OutputStream out) throws IOException
	{
		out.write(image);
	}
}
