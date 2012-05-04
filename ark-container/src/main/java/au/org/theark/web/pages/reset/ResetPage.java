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
package au.org.theark.web.pages.reset;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import au.org.theark.web.pages.Constants;

/**
 * <p>
 * The <code>ResetPage</code> class that extends the {@link org.apache.wicket.markup.html.WebPage WebPage} class. It provides the implementation of the
 * reset username page of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class ResetPage<T> extends WebPage {

	private static final long	serialVersionUID	= -8767984428141993995L;
	private FeedbackPanel		feedbackPanel;
	private ContextImage			hostedByImage;
	private ContextImage			productImage;
	private ResetForm				resetForm;

	public ResetPage() {
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		
		hostedByImage = new ContextImage("hostedByImage", new Model<String>("images/" + Constants.HOSTED_BY_IMAGE));
		productImage = new ContextImage("productImage", new Model<String>("images/" + Constants.PRODUCT_IMAGE));
		resetForm = new ResetForm("resetForm", feedbackPanel);
		addComponents();
	}

	private void addComponents() {
		add(feedbackPanel);
		add(hostedByImage);
		add(productImage);
		add(resetForm);
	}
}
