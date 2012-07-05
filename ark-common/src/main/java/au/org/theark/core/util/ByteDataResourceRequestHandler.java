/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

public class ByteDataResourceRequestHandler extends ByteArrayResource implements IRequestHandler {

	private static final long	serialVersionUID	= 1L;

	/**
	 * the name of the file (because the ByteArrayResource's fileName is private)
	 */
	private String					fileName;

	/**
	 * main constructor
	 * 
	 * @param mimeType
	 * @param data
	 * @param fileName
	 */
	public ByteDataResourceRequestHandler(String mimeType, byte[] data, String fileName) {
		super(mimeType, data, fileName);
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.request.IRequestHandler#detach(org.apache.wicket.request.IRequestCycle)
	 */
	public void detach(IRequestCycle arg0) {
		// nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.request.IRequestHandler#respond(org.apache.wicket.request.IRequestCycle)
	 */
	public void respond(IRequestCycle requestCycle) {
		// Write out data as a file in temporary directory
		final String tempDir = System.getProperty("java.io.tmpdir");
		final java.io.File file = new File(tempDir, fileName);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(this.getData(null));
			fos.flush();
		}
		catch (IOException fe) {
			fe.printStackTrace();
		}
		finally {
			try {
				if (fos != null) {
					fos.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Send file back as attachment, and remove after download
		IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
		requestCycle.scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
			@Override
			public void respond(IRequestCycle requestCycle) {
				super.respond(requestCycle);
				Files.remove(file);
			}
		}.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT));
	}
}