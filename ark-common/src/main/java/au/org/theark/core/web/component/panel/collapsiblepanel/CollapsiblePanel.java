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
package au.org.theark.core.web.component.panel.collapsiblepanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Wicket panel that can be opened and closed (based on example by Jonny Wray)
 * 
 * @author cellis
 */
public abstract class CollapsiblePanel extends Panel {


	private static final long								serialVersionUID	= -7323878216936160622L;
	private static final PackageResourceReference	closed				= new PackageResourceReference(CollapsiblePanel.class, "bullet_toggle_plus.png");
	private static final PackageResourceReference	open					= new PackageResourceReference(CollapsiblePanel.class, "bullet_toggle_minus.png");
	private boolean											visible				= false;
	protected Panel											innerPanel;

	/**
	 * Construct the panel
	 * 
	 * @param id
	 *           Panel ID
	 * @param titleModel
	 *           Model used to get the panel title
	 * @param defaultOpen
	 *           Is the default state open
	 */
	public CollapsiblePanel(String id, IModel<String> titleModel, boolean defaultOpen) {
		super(id);
		this.visible = defaultOpen;
		innerPanel = getInnerPanel("innerPanel");
		innerPanel.setVisible(visible);
		innerPanel.setOutputMarkupId(true);
		innerPanel.setOutputMarkupPlaceholderTag(true);
		add(innerPanel);

		final Image showHideImage = new Image("showHideIcon") {
			private static final long	serialVersionUID	= 8638737301579767296L;

			@Override
			public ResourceReference getImageResourceReference() {
				return visible ? open : closed;
			}
		};
		showHideImage.setOutputMarkupId(true);
		IndicatingAjaxLink<String> showHideLink = new IndicatingAjaxLink<String>("showHideLink") {
			private static final long	serialVersionUID	= -1929927616508773911L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				visible = !visible;
				innerPanel.setVisible(visible);
				target.add(innerPanel);
				target.add(showHideImage);
			}
		};
		showHideLink.add(showHideImage);
		add(new Label("titlePanel", titleModel));
		add(showHideLink);
	}

	/**
	 * Construct the panel contained within the collapsible panel
	 * 
	 * @param markupId
	 *           ID that should be used for the inner panel
	 * @return The inner panel displayed when collapsible is open
	 */
	protected abstract Panel getInnerPanel(String markupId);
}
