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
package au.org.theark.core.web.component.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.ResourceReference;

import au.org.theark.core.web.component.link.AjaxConfirmLink;

public abstract class DeleteIconAjaxLinkPanel<T> extends IconLabelPanel<T> {


	private static final long	serialVersionUID	= 1L;
	
	protected AjaxLink<String> link;

	/**
	 * Constructor that uses no link label (i.e. icon only)
	 * @param id
	 */
	public DeleteIconAjaxLinkPanel(String id, IModel<T> innerModel) {
		this(id, new Model<String>(""), innerModel);
	}

	/**
	 * Constructor that allows the a link label to be passed in.
	 * If linkLabelModel is null, then default label is used.
	 * @param id
	 * @param linkLabelModel
	 */
	public DeleteIconAjaxLinkPanel(String id, IModel<String> linkLabelModel, IModel<T> innerModel) {
		super(id, linkLabelModel, innerModel);	// this will automatically call addComponents(..)
		this.setOutputMarkupPlaceholderTag(true);
	}
	
	@Override
	protected void addComponents(IModel<?> labelModel)
	{
		link = buildLink(innerModel);
		link.add(newImageComponent("iconImage"));
		link.add(newLabelComponent("label", labelModel));
    	this.add(link);
	}
	
	protected AjaxLink<String> buildLink(IModel<T> innerModel)  {
		
		return new AjaxConfirmLink<String>("arkLink", new StringResourceModel("deleteIconLinkPanel.confirm", this, null), null) {

			private static final long	serialVersionUID	= 1L;
	
			@Override
			public void onClick(AjaxRequestTarget target) {
				onLinkClick(target);
			}
			
		};
	}

	@Override
	protected ResourceReference getImageResourceReference() {
		return getResourceDeleteIcon();	//set the delete icon
	}

	/**
	 * This method will perform the required process when the link is clicked.
	 * 
	 * @param target
	 */
	abstract protected void onLinkClick(AjaxRequestTarget target);

}
