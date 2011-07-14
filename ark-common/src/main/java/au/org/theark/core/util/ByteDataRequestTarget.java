package au.org.theark.core.util;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

public class ByteDataRequestTarget extends ByteArrayResource implements IRequestTarget
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * the name of the file
	 */
	private String					fileName;

	/**
	 * main constructor
	 * 
	 * @param mimeType
	 * @param data
	 * @param fileName
	 */
	public ByteDataRequestTarget(String mimeType, byte[] data, String fileName)
	{
		super(mimeType, data, fileName);
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.IRequestTarget#detach(org.apache.wicket.RequestCycle)
	 */
	public void detach(RequestCycle arg0)
	{
		// do nothing
	}

	/**
	 * streams the file
	 */
	public void respond(RequestCycle requestCycle)
	{
		requestCycle.setRequestTarget(new ResourceStreamRequestTarget(this.getResourceStream())
		{
			public String getFileName()
			{
				return fileName;
			}
		});
	}

}