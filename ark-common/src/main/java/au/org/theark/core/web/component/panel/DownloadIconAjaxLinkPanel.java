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
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.resource.ResourceReference;

import au.org.theark.core.web.component.link.AjaxDownloadBehaviour;

public abstract class DownloadIconAjaxLinkPanel extends IconLabelPanel implements IDownloadRequestHandlerProvider {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	protected AjaxDownloadBehaviour download;
	protected AjaxLink<String> link;

	/**
	 * Constructor that uses no download link label (i.e. icon only)
	 * @param id
	 */
	public DownloadIconAjaxLinkPanel(String id) {
		this(id, new Model<String>(""));
	}

	/**
	 * Constructor that allows the a download link label to be passed in.
	 * If linkLabelModel is null, then default "Download" label is used.
	 * @param id
	 * @param linkLabelModel
	 */
	public DownloadIconAjaxLinkPanel(String id, IModel<String> linkLabelModel) {
		super(id, linkLabelModel);	// this will automatically call addComponents(..)
		this.setOutputMarkupPlaceholderTag(true);
	}

	@Override
	protected void addComponents(IModel<?> labelModel) {
		link = buildLink();
		link.add(newImageComponent("iconImage"));
		link.add(newLabelComponent("label", labelModel));
    	this.add(link);

		// tack on the AjaxDownloadBehaviour using "this" as the IDownloadRequestHandlerProvider
		download = new AjaxDownloadBehaviour(this);
		this.add(download);
	}
	
	/**
	 * You may override this method to perform additional updates to components when the link is clicked.
	 * This method is called before the download is triggered.
	 * 
	 * @param target
	 */
	protected void onLinkClick(AjaxRequestTarget target) {
	}

	protected AjaxLink<String> buildLink()  {
		
		return new AjaxLink<String>("arkLink") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;
	
			@Override
			public void onClick(AjaxRequestTarget target) {
				onLinkClick(target);
				download.initiate(target);
			}
			
		};
	}

	@Override
	protected ResourceReference getImageResourceReference() {
		return getResourceDownloadIcon();	//set the download icon
	}
	
	/**
	 * Implement the file download request handler here.
	 * @return
	 */
	public abstract IRequestHandler getDownloadRequestHandler();

}
