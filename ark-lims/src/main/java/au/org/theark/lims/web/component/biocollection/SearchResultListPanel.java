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
package au.org.theark.lims.web.component.biocollection;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
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

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.biocollection.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked" })
public class SearchResultListPanel extends Panel {
	private WebMarkupContainer	detailsPanelContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private ContainerForm		containerForm;
	private DetailPanel			detailPanel;
	private WebMarkupContainer	detailPanelFormContainer;
	private WebMarkupContainer	viewButtonContainer;
	private WebMarkupContainer	editButtonContainer;
	private WebMarkupContainer	arkContextMarkup;

	public SearchResultListPanel(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm studyCompContainerForm,
			WebMarkupContainer searchResultContainer, DetailPanel detail, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer arkContextMarkup) {
		super(id);
		this.detailsPanelContainer = detailPanelContainer;
		this.containerForm = studyCompContainerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.setArkContextMarkup(arkContextMarkup);
		this.setDetailPanel(detail);

	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of BioCollection
	 */
	public PageableListView<BioCollection> buildPageableListView(IModel iModel) {
		PageableListView<BioCollection> sitePageableListView = new PageableListView<BioCollection>("collectionList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<BioCollection> item) {
				BioCollection bioCollection = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				/* The ID */
				if (bioCollection.getId() != null) {
					// Add the id component here
					item.add(new Label("bioCollection.id", bioCollection.getId().toString()));
				}
				else {
					item.add(new Label("bioCollection.id", ""));
				}

				// Component Name Link
				item.add(buildLink(bioCollection));

				// TODO when displaying text escape any special characters
				if (bioCollection.getLinkSubjectStudy() != null) {
					item.add(new Label("bioCollection.linkSubjectStudy", bioCollection.getLinkSubjectStudy().getSubjectUID()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("bioCollection.linkSubjectStudy", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Start Date
				if (bioCollection.getCollectionDate() != null) {
					item.add(new Label("bioCollection.collectionDate", simpleDateFormat.format(bioCollection.getCollectionDate())));// the ID here must
																																											// match
					// the ones in mark-up
				}
				else {
					item.add(new Label("bioCollection.collectionDate", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Expiry Date
				if (bioCollection.getSurgeryDate() != null) {
					item.add(new Label("bioCollection.surgeryDate", simpleDateFormat.format(bioCollection.getSurgeryDate())));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("bioCollection.surgeryDate", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				if (bioCollection.getComments() != null) {
					item.add(new Label("bioCollection.comments", bioCollection.getComments()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("bioCollection.comments", ""));// the ID here must match the ones in mark-up
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	private AjaxLink buildLink(final BioCollection bioCollection) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("bioCollection.name") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				LimsVO limsVo = containerForm.getModelObject();
				limsVo.setBioCollection(bioCollection);
				containerForm.setModelObject(limsVo);

				// Place the selected collection in session context for the user
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.lims.web.Constants.BIO_COLLECTION, bioCollection.getId());

				detailsPanelContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);

				// Button containers
				// View Field, thus view container visible
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				editButtonContainer.setVisible(false);

				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", bioCollection.getName());
		link.add(nameLinkLabel);
		return link;
	}

	/**
	 * @param detailPanel
	 *           the detailPanel to set
	 */
	public void setDetailPanel(DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	/**
	 * @return the detailPanel
	 */
	public DetailPanel getDetailPanel() {
		return detailPanel;
	}

	/**
	 * @param arkContextMarkup
	 *           the arkContextMarkup to set
	 */
	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup) {
		this.arkContextMarkup = arkContextMarkup;
	}

	/**
	 * @return the arkContextMarkup
	 */
	public WebMarkupContainer getArkContextMarkup() {
		return arkContextMarkup;
	}
}
