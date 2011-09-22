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

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

public class ByteDataRequestTarget extends ByteArrayResource implements IRequestTarget {
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
	public ByteDataRequestTarget(String mimeType, byte[] data, String fileName) {
		super(mimeType, data, fileName);
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.IRequestTarget#detach(org.apache.wicket.RequestCycle)
	 */
	public void detach(RequestCycle arg0) {
		// do nothing
	}

	/**
	 * streams the file
	 */
	public void respond(RequestCycle requestCycle) {
		requestCycle.setRequestTarget(new ResourceStreamRequestTarget(this.getResourceStream()) {
			public String getFileName() {
				return fileName;
			}
		});
	}

}
