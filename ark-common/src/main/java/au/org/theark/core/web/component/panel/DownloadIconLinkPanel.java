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

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.resource.ResourceReference;

public abstract class DownloadIconLinkPanel<T> extends IconLabelPanel<T> {


	private static final long	serialVersionUID	= 1L;

	protected Link<T> link;

	/**
	 * Constructor that uses no download link label (i.e. icon only)
	 * @param id
	 */
	public DownloadIconLinkPanel(String id, IModel<T> innerModel) {
		this(id, new Model<String>(""), innerModel);
	}

	/**
	 * Constructor that allows the a download link label to be passed in.
	 * If linkLabelModel is null, then default "Download" label is used.
	 * @param id
	 * @param linkLabelModel
	 */
	public DownloadIconLinkPanel(String id, IModel<String> linkLabelModel, IModel<T> innerModel) {
		super(id, linkLabelModel, innerModel);	// this will automatically call addComponents(..)
		this.setOutputMarkupPlaceholderTag(true);
	}

	@Override
	protected void addComponents(IModel<?> labelModel) {
		link = buildLink(innerModel);
		link.setOutputMarkupPlaceholderTag(true);
		link.add(newImageComponent("iconImage"));
		link.add(newLabelComponent("label", labelModel));
    	this.add(link);

		// tack on the AjaxDownloadBehaviour using "this" as the IDownloadRequestHandlerProvider
//		download = new AjaxDownloadBehaviour(this);
//		this.add(download);
	}
	
	protected Link<T> buildLink(IModel<T> innerModel) {
		return new Link<T>("arkLink", innerModel) {

			private static final long	serialVersionUID	= 1L;
	
			@Override
			public void onClick() {
				getRequestCycle().scheduleRequestHandlerAfterCurrent(getDownloadRequestHandler());
			}
		};
	}

//	/**
//	 * You may override this method to perform additional updates to components when the link is clicked.
//	 * This method is called before the download is triggered.
//	 * 
//	 * @param target
//	 */
//	protected void onLinkClick(AjaxRequestTarget target) {
//	}

//	protected AjaxLink<T> buildLink(IModel<T> innerModel)  {
//		
//		
//		return new AjaxLink<T>("arkLink", innerModel) {
//			/**
//			 * 
//			 */
//			private static final long	serialVersionUID	= 1L;
//	
//			AjaxDownloadBehaviour download;
//			
//			@Override
//			protected void onBeforeRender() {
//				download = new AjaxDownloadBehaviour(DownloadIconLinkPanel.this);
//				this.add(download);
//				super.onBeforeRender();
//			}
//			
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//				onLinkClick(target);
//				download.initiate(target);
//			}
//		};
//	}

	@Override
	protected ResourceReference getImageResourceReference() {
		return getResourceDownloadIcon();	//set the download icon
	}
	
	/**
	 * Implement the file download request handler here.
	 * @return
	 */
	abstract protected IRequestHandler getDownloadRequestHandler();

}
