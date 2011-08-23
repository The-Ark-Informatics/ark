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
package au.org.theark.core.web.component.chart;

import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.jfree.chart.JFreeChart;

public class JFreeChartImage extends Image {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1383706328089971627L;
	private int						width;
	private int						height;

	public JFreeChartImage(String id, JFreeChart chart, int width, int height) {
		super(id, new Model<JFreeChart>(chart));
		this.width = width;
		this.height = height;
	}

	@Override
	protected Resource getImageResource() {
		return new DynamicImageResource() {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6764144907661389644L;

			@Override
			protected byte[] getImageData() {
				JFreeChart chart = (JFreeChart) getDefaultModelObject();
				return toImageData(chart.createBufferedImage(width, height));
			}

			@Override
			protected void setHeaders(WebResponse response) {
				if (isCacheable()) {
					super.setHeaders(response);
				}
				else {
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);
				}
			}
		};
	}
}
