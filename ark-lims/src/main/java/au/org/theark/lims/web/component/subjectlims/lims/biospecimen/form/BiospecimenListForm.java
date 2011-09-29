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
package au.org.theark.lims.web.component.subjectlims.lims.biospecimen.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.UniqueIdGenerator;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biolocation.BioLocationPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenModalDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class BiospecimenListForm extends Form<LimsVO> {
	/**
	 * 
	 */
	private static final long								serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(BiospecimenListForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService										iLimsService;
	
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService								iInventoryService;

	protected CompoundPropertyModel<LimsVO>			cpModel;
	protected FeedbackPanel									feedbackPanel;
	protected AbstractDetailModalWindow					modalWindow;

	private Label												idLblFld;
	private Label												nameLblFld;
	private Label												sampleTypeLblFld;
	private Label												collectionLblFld;
	private Label												quantityLblFld;
	private Label												unitsLblFld;
	private Label												commentsLblFld;
	private Label												locationLbl;
	private ArkBusyAjaxLink									locationLink;

	private Panel												modalContentPanel;
	protected ArkBusyAjaxButton							newButton;

	protected WebMarkupContainer							dataViewListWMC;
	private DataView<Biospecimen>							dataView;
	private ArkDataProvider2<LimsVO, Biospecimen>	biospecimenProvider;

	public BiospecimenListForm(String id, FeedbackPanel feedbackPanel, AbstractDetailModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
	}

	public void initialiseForm() {
		modalContentPanel = new EmptyPanel("content");

		initialiseDataView();
		initialiseNewButton();

		add(modalWindow);
	}

	@Override
	public void onBeforeRender() {
		// Get session data (used for subject search)
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID);
				if (study != null && linkSubjectStudy != null) {
					contextLoaded = true;
				}
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}

			if (contextLoaded) {
				// Successfully loaded from backend
				cpModel.getObject().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);
				cpModel.getObject().getBiospecimen().setStudy(study);
			}
		}

		super.onBeforeRender();
	}

	private void initialiseDataView() {
		dataViewListWMC = new WebMarkupContainer("dataViewListWMC");
		dataViewListWMC.setOutputMarkupId(true);
		// Data provider to paginate resultList
		biospecimenProvider = new ArkDataProvider2<LimsVO, Biospecimen>() {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public int size() {
				return iLimsService.getBiospecimenCount(criteriaModel.getObject());
			}

			public Iterator<Biospecimen> iterator(int first, int count) {
				List<Biospecimen> biospecimenList = new ArrayList<Biospecimen>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					biospecimenList = iLimsService.searchPageableBiospecimens(criteriaModel.getObject(), first, count);
				}
				return biospecimenList.iterator();
			}
		};
		// Set the criteria into the data provider's model
		biospecimenProvider.setCriteriaModel(cpModel);

		dataView = buildDataView(biospecimenProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(dataViewListWMC);
			}
		};
		dataViewListWMC.add(pageNavigator);
		dataViewListWMC.add(dataView);
		add(dataViewListWMC);

	}

	private void initialiseNewButton() {
		newButton = new ArkBusyAjaxButton("listNewButton", new StringResourceModel("listNewKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				boolean isVisible = true;

				String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				isVisible = (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW) && sessionSubjectUID != null);

				return isVisible;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNew(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to proceed with New Biospecimen");
			}
		};
		newButton.setDefaultFormProcessing(false);

		add(newButton);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of BioCollection
	 */
	public DataView<Biospecimen> buildDataView(ArkDataProvider2<LimsVO, Biospecimen> biospecimenProvider) {

		DataView<Biospecimen> biospecimenDataView = new DataView<Biospecimen>("biospecimenList", biospecimenProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<Biospecimen> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final Biospecimen biospecimen = item.getModelObject();

				WebMarkupContainer rowDetailsWMC = new WebMarkupContainer("rowDetailsWMC", item.getModel());
				ArkBusyAjaxLink listDetailsLink = new ArkBusyAjaxLink("listDetailsLink") {

					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Biospecimen biospecimen = (Biospecimen) (getParent().getDefaultModelObject());
						CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
						newModel.getObject().getBiospecimen().setId(biospecimen.getId());
						showModalWindow(target, newModel);
					}
				};

				idLblFld = new Label("biospecimen.id", String.valueOf(biospecimen.getId()));

				nameLblFld = new Label("biospecimen.biospecimenUid", biospecimen.getBiospecimenUid());
				listDetailsLink.add(nameLblFld);
				rowDetailsWMC.add(listDetailsLink);				
				
				sampleTypeLblFld = new Label("biospecimen.sampleType.name", biospecimen.getSampleType().getName());
				collectionLblFld = new Label("biospecimen.bioCollection.name", biospecimen.getBioCollection().getName());
				commentsLblFld = new Label("biospecimen.comments", biospecimen.getComments());
				
				biospecimen.setQuantity(iLimsService.getQuantityAvailable(biospecimen));
				if (biospecimen.getQuantity() == null) {
					quantityLblFld = new Label("biospecimen.quantity", "0");
				}
				else {
					quantityLblFld = new Label("biospecimen.quantity", biospecimen.getQuantity().toString());
				}
				if (biospecimen.getUnit() == null){
					unitsLblFld = new Label("biospecimen.unit", "");
				}
				else {
					unitsLblFld = new Label("biospecimen.unit", biospecimen.getUnit().getName());	
				}
				
				try {
					locationLbl = new Label("biospecimen.location", "view");
					locationLink = new ArkBusyAjaxLink("biospecimen.location.link"){

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							Biospecimen biospecimen = (Biospecimen) (getParent().getDefaultModelObject());
							CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
							newModel.getObject().getBiospecimen().setId(biospecimen.getId());
							BiospecimenLocationVO biospecimenLocationVo;
							try {
								biospecimenLocationVo = iInventoryService.locateBiospecimen(biospecimen);
								newModel.getObject().setBiospecimenLocationVO(biospecimenLocationVo);
								modalContentPanel = new BioLocationPanel("content", newModel);
								modalContentPanel.add(new AttributeModifier("class", "detailsPanelBorder"));
								// Set the modalWindow title and content
								modalWindow.setTitle("Biospecimen Location Detail");
								modalWindow.setContent(modalContentPanel);
								modalWindow.show(target);
							}
							catch (ArkSystemException e) {
								log.error(e.getMessage());
							}
						}
					};
					
					locationLink.add(locationLbl);
					
					BiospecimenLocationVO biospecimenLocationVo = iInventoryService.locateBiospecimen(biospecimen);
					if(!biospecimenLocationVo.getIsAllocated()) {
						locationLink.setVisible(false);
					}
				}
				catch (ArkSystemException e) {
					log.error(e.getMessage());
				}

				item.add(idLblFld);
				item.add(rowDetailsWMC);
				item.add(sampleTypeLblFld);
				item.add(collectionLblFld);
				item.add(commentsLblFld);
				item.add(quantityLblFld);
				item.add(unitsLblFld);
				item.add(locationLink);

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {

					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}

		};

		return biospecimenDataView;
	}

	protected void onNew(AjaxRequestTarget target) {
		// Needs CREATE permission AND a BioCollection to select from
		boolean hasBioCollections = false;

		hasBioCollections = iLimsService.hasBioCollections(cpModel.getObject().getLinkSubjectStudy());

		if (hasBioCollections) {
			// Set new Biospecimen into model, then show modalWindow to save
			CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
			newModel.getObject().getBiospecimen().setLinkSubjectStudy(getModelObject().getLinkSubjectStudy());
			newModel.getObject().getBiospecimen().setStudy(getModelObject().getLinkSubjectStudy().getStudy());

			// Create new BiospecimenUID
			newModel.getObject().getBiospecimen().setBiospecimenUid(UniqueIdGenerator.generateUniqueId());

			showModalWindow(target, newModel); // listDetailsForm);
		}
		else {
			this.error("No Biospecimen Collections exist. Please create at least one Collection.");
		}
		// refresh the feedback messages
		target.add(feedbackPanel);
	}

	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<LimsVO> cpModel) {
		modalContentPanel = new BiospecimenModalDetailPanel("content", modalWindow, cpModel);

		// Set the modalWindow title and content
		modalWindow.setTitle("Biospecimen Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
	}

	/**
	 * @return the newButton
	 */
	public ArkBusyAjaxButton getNewButton() {
		return newButton;
	}

	/**
	 * @param newButton the newButton to set
	 */
	public void setNewButton(ArkBusyAjaxButton newButton) {
		this.newButton = newButton;
	}
}