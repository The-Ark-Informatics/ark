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

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.StringResourceStream;

public class ByteDataResourceRequestHandler extends ByteArrayResource implements IRequestHandler{
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
	public ByteDataResourceRequestHandler(String mimeType, byte[] data, String fileName) {
		super(mimeType, data, fileName);
	
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestHandler#detach(org.apache.wicket.request.IRequestCycle)
	 */
	public void detach(IRequestCycle arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestHandler#respond(org.apache.wicket.request.IRequestCycle)
	 */
	public void respond(IRequestCycle requestCycle) {
		
		StringResourceStream stringResourceStream = new StringResourceStream( new String(this.getData(null)));
		ResourceStreamRequestHandler resourceStreamRequestHandler = new ResourceStreamRequestHandler(stringResourceStream);
		resourceStreamRequestHandler.setFileName(fileName);
		resourceStreamRequestHandler.setContentDisposition(ContentDisposition.ATTACHMENT);
		requestCycle.scheduleRequestHandlerAfterCurrent(resourceStreamRequestHandler);
	}

}
