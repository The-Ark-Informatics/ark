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
package au.org.theark.lims.web.component.biospecimen;

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

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biospecimen.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked" })
public class SearchResultListPanel extends Panel {
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService			iLimsService;

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
	 * @return the pageableListView of Biospecimen
	 */
	public PageableListView<Biospecimen> buildPageableListView(IModel iModel) {

		PageableListView<Biospecimen> sitePageableListView = new PageableListView<Biospecimen>("collectionList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<Biospecimen> item) {
				Biospecimen biospecimen = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				/* The ID */
				if (biospecimen.getId() != null) {
					// Add the id component here
					item.add(new Label("biospecimen.id", biospecimen.getId().toString()));
				}
				else {
					item.add(new Label("biospecimen.id", ""));
				}

				// Component Name Link
				item.add(buildLink(biospecimen));

				// TODO when displaying text escape any special characters
				if (biospecimen.getLinkSubjectStudy() != null) {
					item.add(new Label("biospecimen.linkSubjectStudy", biospecimen.getLinkSubjectStudy().getSubjectUID()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("biospecimen.linkSubjectStudy", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Sample Type
				if (biospecimen.getSampleType() != null) {
					item.add(new Label("biospecimen.sampleType", biospecimen.getSampleType().getName()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("biospecimen.sampleType", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// BioCollection
				if (biospecimen.getBioCollection() != null) {
					item.add(new Label("biospecimen.bioCollection", biospecimen.getBioCollection().getName()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("biospecimen.bioCollection", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Sample Date
				if (biospecimen.getSampleDate() != null) {
					item.add(new Label("biospecimen.sampleDate", simpleDateFormat.format(biospecimen.getSampleDate())));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("biospecimen.sampleDate", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				if (biospecimen.getQuantity() != null) {
					item.add(new Label("biospecimen.quantity", biospecimen.getQuantity().toString()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label("biospecimen.quantity", ""));// the ID here must match the ones in mark-up
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

	private AjaxLink buildLink(final Biospecimen biospecimen) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("biospecimen.biospecimenUid") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Biospecimen biospecimenFromBackend = new Biospecimen();
				try {
					biospecimenFromBackend = iLimsService.getBiospecimen(biospecimen.getId());
				}
				catch (EntityNotFoundException e) {
					this.error(e.getMessage());
				}

				// Sets the selected object into the model
				LimsVO limsVo = containerForm.getModelObject();
				limsVo.setBiospecimen(biospecimenFromBackend);
				limsVo.setLinkSubjectStudy(biospecimenFromBackend.getLinkSubjectStudy());
				limsVo.setBioCollection(biospecimenFromBackend.getBioCollection());

				containerForm.setModelObject(limsVo);

				detailsPanelContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);

				// Button containers
				// View Field, thus view container visible
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				editButtonContainer.setVisible(false);

				target.add(searchResultContainer);
				target.add(detailsPanelContainer);
				target.add(searchPanelContainer);
				target.add(viewButtonContainer);
				target.add(editButtonContainer);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", biospecimen.getBiospecimenUid());
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
