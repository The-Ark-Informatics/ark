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

import java.awt.image.BufferedImage;

import org.apache.wicket.request.resource.DynamicImageResource;

public class ImageResource extends DynamicImageResource {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	// has to save this or get the image another way!
	private byte[]					image;

	public ImageResource(byte[] image, String format) {
		this.image = image;
		setFormat(format);
	}

	public ImageResource(BufferedImage image) {
		this.image = toImageData(image);
	}

	/**
	 * 1 day!
	 */
	protected int getCacheDuration() {

		return 3600 * 24;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.DynamicImageResource#getImageData(org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected byte[] getImageData(Attributes attributes) {
		
		if (image != null) {
			return image;
		}
		else {
			return new byte[0];
		}
	}

}
