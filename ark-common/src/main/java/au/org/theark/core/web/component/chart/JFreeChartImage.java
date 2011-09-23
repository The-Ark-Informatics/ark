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

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.WebResponse.CacheScope;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.time.Duration;
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
    protected IResource getImageResource() {
        DynamicImageResource resource = new DynamicImageResource() {

            @Override
            protected byte[] getImageData(final Attributes attributes) {
                JFreeChart chart = (JFreeChart) getDefaultModelObject();
                return toImageData(chart.createBufferedImage(width, height));
            }

            @Override
            protected void configureResponse(final ResourceResponse response, final Attributes attributes) {
                super.configureResponse(response, attributes);
               
                //if (isCacheable() == false) {TODO Resolve this.
                    response.setCacheDuration(Duration.NONE);
                    response.setCacheScope(CacheScope.PRIVATE);
                //}
            }

        };

        return resource;
    }

	
}
