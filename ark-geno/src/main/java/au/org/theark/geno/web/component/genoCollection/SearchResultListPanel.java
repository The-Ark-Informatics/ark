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
package au.org.theark.geno.web.component.genoCollection;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.geno.model.vo.GenoCollectionVO;
import au.org.theark.geno.web.component.genoCollection.form.ContainerForm;

/**
 * @author elam
 *
 */
public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GenoCollectionContainerPanel genoCollectionContainerPanel;
	private ContainerForm containerForm;

	public SearchResultListPanel(String id,
			GenoCollectionContainerPanel genoCollectionContainerPanel,
			ContainerForm containerForm) {
		super(id);
		this.genoCollectionContainerPanel = genoCollectionContainerPanel; 
		this.containerForm = containerForm;
	}

	public PageableListView<GenoCollection> buildPageableListView(IModel iModel) {
		
		PageableListView<GenoCollection> sitePageableListView = new PageableListView<GenoCollection>("collectionList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE)
		{
			@Override
			protected void populateItem(final ListItem<GenoCollection> item)
			{
				GenoCollection genoCollection = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);

				/* The phenoCollection ID */
				if (genoCollection.getId() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_ID, genoCollection.getId().toString()));
				}
				else
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_ID, ""));
				}

				// Component Name Link
				item.add(buildLink(genoCollection));

				// TODO when displaying text escape any special characters
				// GenoCollection status
				if (genoCollection.getStatus() != null)
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_STATUS, genoCollection.getStatus().getName()));
				}
				else
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_STATUS, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Description
				if (genoCollection.getDescription() != null)
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_DESCRIPTION, genoCollection.getDescription()));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_DESCRIPTION, ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Start Date
				if (genoCollection.getStartDate() != null)
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_START_DATE, simpleDateFormat.format(genoCollection.getStartDate())));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_START_DATE, ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Expiry Date
				if (genoCollection.getExpiryDate() != null)
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_EXPIRY_DATE, simpleDateFormat.format(genoCollection.getExpiryDate())));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_EXPIRY_DATE, ""));// the ID here must match the ones in mark-up
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel()
				{
					@Override
					public String getObject()
					{
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	private AjaxLink buildLink(final GenoCollection genoCollection)
	{
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_NAME)
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Sets the selected object into the model
				GenoCollectionVO collectionVo = containerForm.getModelObject();
				collectionVo.setGenoCollection(genoCollection);
				
				// Place the selected collection in session context for the user (passes to Shiro)
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.geno.web.Constants.SESSION_GENO_COLLECTION_ID, genoCollection.getId());
		
				genoCollectionContainerPanel.updateContext(target, genoCollection);
/* Moved to TransitionToDetail
 				detailPanelContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultPanelContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				
				// Button containers
				// View Field, thus view container visible
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				editButtonContainer.setVisible(false);
				
				// Have to Edit, before allowing delete
//				WebMarkupContainer detailsFormContainer = detailPanelContainer.get(Constants.DETAILS_FORM);
//
//				//Instead of: detailPanel.getDetailForm().getDeleteButton().setEnabled(false);
//				AjaxButton detailDeleteBtn = (AjaxButton)detailsFormContainer.get(Constants.DETAILS_DELETE_BTN);
//				detailDeleteBtn.setEnabled(false);

				target.addComponent(searchResultPanelContainer);
				target.addComponent(detailPanelContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
*/
				genoCollectionContainerPanel.showViewDetail(target);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", genoCollection.getName());
		link.add(nameLinkLabel);
		return link;
	}

}
