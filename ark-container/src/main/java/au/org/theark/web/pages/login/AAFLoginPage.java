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
package au.org.theark.web.pages.login;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;

import au.org.theark.web.pages.Constants;

/**
 * <p>
 * The <code>LoginPage</code> class that extends the {@link org.apache.wicket.markup.html.WebPage WebPage} class. It provides the implementation of the
 * login page of The Ark application.
 * 
 * @author nivedann
 * @author cellis
 * 
 * @param <T>
 */
public class AAFLoginPage<T> extends WebPage {

	private static final long			serialVersionUID	= -985615571643703296L;
	
	
	/**
	 * LoginPage Constructor
	 */
	public AAFLoginPage() {
		AAFLoginForm form = new AAFLoginForm("loginForm");
		this.add(form);

		ContextImage hostedByImage = new ContextImage("hostedByImage", new Model<String>("images/" + Constants.HOSTED_BY_IMAGE));
		ContextImage productImage = new ContextImage("productImage", new Model<String>("images/" + Constants.PRODUCT_IMAGE));

		// Add images
		add(hostedByImage);
		add(productImage);
	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren();
	}

	@Override
	protected void configureResponse(final org.apache.wicket.request.http.WebResponse webResponse) {
		super.configureResponse((org.apache.wicket.request.http.WebResponse) RequestCycle.get().getResponse());
		org.apache.wicket.request.http.WebResponse response = (org.apache.wicket.request.http.WebResponse) RequestCycle.get().getResponse();
		response.setHeader("Cache-Control", "no-cache, max-age=0,must-revalidate, no-store");
		response.setHeader("Expires", "-1");
		response.setHeader("Pragma", "no-cache");
	}
}
