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
package au.org.theark.core.web.component.hideshow;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class HideShowPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 678875273789181319L;
	private Panel					panel					= new Panel("panel");
	private AjaxLink<Void>		showHideLink;
	private Image					showHideImage;
	private ResourceReference	showPanelImage		= new ResourceReference(HideShowPanel.class, "showPanelImage.png");
	private ResourceReference	hidePanelImage		= new ResourceReference(HideShowPanel.class, "hidePanelImage.png");
	private IModel<String>		titleModel;

	public HideShowPanel(String id, IModel<String> titleModel, Panel panel, boolean visible) {
		super(id);
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
		this.titleModel = titleModel;
		this.panel = panel;
		this.panel.setVisible(visible);
		initialisePanel();
		addComponents();
	}

	public void initialisePanel() {
		showHideLink = new AjaxLink<Void>("showHideLink") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2650687855479265673L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				panel.setVisible(!panel.isVisible());
				target.addComponent(panel);
				target.addComponent(showHideImage);
			}

		};

		showHideImage = new Image("showHideImage") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3966113343164803942L;

			@Override
			public ResourceReference getImageResourceReference() {
				return panel.isVisible() ? hidePanelImage : showPanelImage;
			}
		};
		
		showHideImage.setOutputMarkupId(true);
		showHideImage.setOutputMarkupPlaceholderTag(true);

		panel.setOutputMarkupId(true);
		panel.setOutputMarkupPlaceholderTag(true);
	}

	public void addComponents() {
		add(showHideLink);
		showHideLink.add(showHideImage);
		add(new Label("titlePanel", titleModel));
		addOrReplace(panel);
	}
}
