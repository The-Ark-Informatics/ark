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
package au.org.theark.phenotypic.web.component.phenoCollection;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoCollection.form.ContainerForm;

public class SearchResultListPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7289946551399368096L;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private ContainerForm		containerForm;
	
	public SearchResultListPanel(String id, ContainerForm containerForm, WebMarkupContainer arkContextMarkup, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of PhenoCollection
	 */
	@SuppressWarnings("unchecked")
	public PageableListView<PhenoCollection> buildPageableListView(IModel iModel) {

		PageableListView<PhenoCollection> sitePageableListView = new PageableListView<PhenoCollection>("collectionList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<PhenoCollection> item) {
				PhenoCollection phenoCollection = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);

				/* The phenoCollection ID */
				if (phenoCollection.getId() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_ID, phenoCollection.getId().toString()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_ID, ""));
				}

				// Component Name Link
				item.add(buildLink(phenoCollection));

				// TODO when displaying text escape any special characters
				// PhenoCollection status
				if (phenoCollection.getStatus() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_STATUS, phenoCollection.getStatus().getName()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_STATUS, ""));// the ID here must match
																																									// the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Description
				if (phenoCollection.getDescription() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION, phenoCollection.getDescription()));// the
																																																					// ID
																																																					// here
																																																					// must
																																																					// match
					// the ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION, ""));// the ID here must
																																											// match the ones in
																																											// mark-up
				}

				// TODO when displaying text escape any special characters
				// Start Date
				if (phenoCollection.getStartDate() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE, simpleDateFormat.format(phenoCollection.getStartDate())));// the
																																																												// ID
																																																												// here
																																																												// must
																																																												// match
					// the ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE, ""));// the ID here must
																																										// match the ones in
																																										// mark-up
				}

				// TODO when displaying text escape any special characters
				// Expiry Date
				if (phenoCollection.getEndDate() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_END_DATE, simpleDateFormat.format(phenoCollection.getEndDate())));// the
																																																											// ID
																																																											// here
																																																											// must
																																																											// match
					// the ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_END_DATE, ""));// the ID here must match
																																										// the ones in mark-up
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	private AjaxLink<String> buildLink(final PhenoCollection phenoCollection) {
		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("phenoCollection.name"){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				PhenoCollectionVO collectionVo = containerForm.getModelObject();
				collectionVo = iPhenotypicService.getPhenoCollectionAndFields(phenoCollection.getId());
				Field field = new Field();
				field.setStudy(phenoCollection.getStudy());
				collectionVo.setFieldsAvailable(iPhenotypicService.searchField(field));
				containerForm.setModelObject(collectionVo);

				arkCrudContainerVO.getDetailPanelContainer().setVisible(true);  
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVO.getSearchPanelContainer().setVisible(false);

				// Button containers
				// View Field, thus view container visible
				arkCrudContainerVO.getViewButtonContainer().setVisible(true);
				arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVO.getEditButtonContainer().setVisible(false);

				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getDetailPanelContainer());
				target.add(arkCrudContainerVO.getSearchPanelContainer());
				target.add(arkCrudContainerVO.getViewButtonContainer());
				target.add(arkCrudContainerVO.getEditButtonContainer());
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", phenoCollection.getName());
		link.add(nameLinkLabel);
		return link;
	}

}
