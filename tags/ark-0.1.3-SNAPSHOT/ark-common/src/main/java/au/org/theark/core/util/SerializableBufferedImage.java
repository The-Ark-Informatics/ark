package au.org.theark.core.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class SerializableBufferedImage implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1990200903054294230L;
	private byte[]	byteImage	= null;
	private int width;
	private int height;

	public SerializableBufferedImage(BufferedImage bufferedImage)
	{
		this.byteImage = toByteArray(bufferedImage);
		this.setWidth(bufferedImage.getWidth());
		this.setHeight(bufferedImage.getHeight());
	}

	public BufferedImage getBufferedImage()
	{
		return fromByteArray(byteImage);
	}

	private BufferedImage fromByteArray(byte[] imagebytes)
	{
		try
		{
			if (imagebytes != null && (imagebytes.length > 0))
			{
				BufferedImage im = ImageIO.read(new ByteArrayInputStream(imagebytes));
				return im;
			}
			return null;
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException(e.toString());
		}
	}

	private byte[] toByteArray(BufferedImage bufferedImage)
	{
		if (bufferedImage != null)
		{
			BufferedImage image = bufferedImage;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try
			{
				ImageIO.write(image, "png", baos);
			}
			catch (IOException e)
			{
				throw new IllegalStateException(e.toString());
			}
			byte[] b = baos.toByteArray();
			return b;
		}
		return new byte[0];
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}
}
