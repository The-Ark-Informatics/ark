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
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
import au.org.theark.core.web.component.export.ExportToolbar;
import au.org.theark.core.web.component.export.ExportableTextColumn;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.lims.model.vo.BatchBiospecimenAliquotsVO;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biolocation.BioLocationPanel;
import au.org.theark.lims.web.component.biospecimen.batchaliquot.BatchAliquotBiospecimenPanel;
import au.org.theark.lims.web.component.biospecimen.batchcreate.AutoGenBatchCreateBiospecimenPanel;
import au.org.theark.lims.web.component.biospecimen.batchcreate.ManualBatchCreateBiospecimenPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenModalDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class BiospecimenListForm extends Form<LimsVO> {

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
	private Label												studyLblFld;
	private Label												sampleTypeLblFld;
	private Label												collectionLblFld;
	private Label												quantityLblFld;
	private Label												unitsLblFld;
	private Label												commentsLblFld;
	private Label												locationLbl;
	private ArkBusyAjaxLink									locationLink;

	private Panel												modalContentPanel;
	protected ArkBusyAjaxButton							newButton;
	protected ArkBusyAjaxButton							batchCreateButton;

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
		initialiseBatchCreateButton();

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
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
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

			private static final long	serialVersionUID	= 1L;

			public int size() {
				return (int) iLimsService.getBiospecimenCount(criteriaModel.getObject());
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

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(dataViewListWMC);
			}
		};

		dataViewListWMC.add(pageNavigator);
		dataViewListWMC.add(dataView);

		List<IColumn<Biospecimen>> columns = new ArrayList<IColumn<Biospecimen>>();
		columns.add(new ExportableTextColumn<Biospecimen>(Model.of("BiospecimenUID"), "biospecimenUid"));
		columns.add(new ExportableTextColumn<Biospecimen>(Model.of("Study"), "study.name"));
		columns.add(new ExportableTextColumn<Biospecimen>(Model.of("SubjectUID"), "linkSubjectStudy.subjectUID"));
		columns.add(new ExportableTextColumn<Biospecimen>(Model.of("Collection"), "bioCollection.name"));
		columns.add(new ExportableTextColumn<Biospecimen>(Model.of("Sample Type"), "sampleType.name"));
		columns.add(new ExportableTextColumn<Biospecimen>(Model.of("Quantity"), "quantity"));
		
		DataTable table = new DataTable("datatable", columns, dataView.getDataProvider(), au.org.theark.core.Constants.ROWS_PER_PAGE);
		List<String> headers = new ArrayList<String>(0);
		headers.add("BiospecimenUID");
		headers.add("Study");
		headers.add("SubjectUID");
		headers.add("Collection");
		headers.add("Sample Type");
		headers.add("Quantity");
		
		String filename = "biospecimens";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		dataViewListWMC.add(toolbars);
		
		add(dataViewListWMC);
	}

	private void initialiseNewButton() {
		newButton = new ArkBusyAjaxButton(Constants.NEW) {

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
	
	private void initialiseBatchCreateButton() {
		batchCreateButton = new ArkBusyAjaxButton("batchCreate") {

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
				onBatchCreate(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to proceed with Batch Crreate Biospecimen");
			}
		};
		batchCreateButton.setDefaultFormProcessing(false);

		add(batchCreateButton);
	}
	
	private void onBatchCreate(AjaxRequestTarget target) {
	// Needs CREATE permission AND a BioCollection to select from
		boolean hasBioCollections = false;

		// Get session data (used for subject search)
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
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

		hasBioCollections = iLimsService.hasBioCollections(cpModel.getObject().getLinkSubjectStudy());

		if (hasBioCollections) {
			// Set new Biospecimen into model, then show modalWindow to save
			CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
			newModel.getObject().setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
			newModel.getObject().setStudy(cpModel.getObject().getStudy());
			newModel.getObject().getBiospecimen().setLinkSubjectStudy(getModelObject().getLinkSubjectStudy());
			newModel.getObject().getBiospecimen().setStudy(getModelObject().getLinkSubjectStudy().getStudy());

			// Allow for autogen or manual biospecimenUid entry
			boolean isAutoBiospecimen = getModelObject().getLinkSubjectStudy().getStudy().getAutoGenerateBiospecimenUid();
			if(isAutoBiospecimen) {
				modalContentPanel = new AutoGenBatchCreateBiospecimenPanel("content", feedbackPanel, newModel, modalWindow);
			}
			else {
				modalContentPanel = new ManualBatchCreateBiospecimenPanel("content", feedbackPanel, newModel, modalWindow);
			}
			
			// Set the modalWindow title and content
			modalWindow.setTitle("Batch Create Biospecimens");
			modalWindow.setContent(modalContentPanel);
			modalWindow.show(target);
		}
		else {
			this.error("No Biospecimen Collections exist. Please create at least one Collection.");
		}
		// refresh the feedback messages
		target.add(feedbackPanel);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of BioCollection
	 */
	public DataView<Biospecimen> buildDataView(ArkDataProvider2<LimsVO, Biospecimen> biospecimenProvider) {

		DataView<Biospecimen> biospecimenDataView = new DataView<Biospecimen>("biospecimenList", biospecimenProvider) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<Biospecimen> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final Biospecimen biospecimen = item.getModelObject();

				WebMarkupContainer rowDetailsWMC = new WebMarkupContainer("rowDetailsWMC", item.getModel());
				ArkBusyAjaxLink listDetailsLink = new ArkBusyAjaxLink("listDetailsLink") {

					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
						try {
							Biospecimen biospecimenFromDB = iLimsService.getBiospecimen(biospecimen.getId());
							newModel.getObject().setBiospecimen(biospecimenFromDB);
							newModel.getObject().setTreeModel(cpModel.getObject().getTreeModel());
							showModalWindow(target, newModel);
						}
						catch (EntityNotFoundException e) {
							log.error(e.getMessage());
						}
					}
				};

				idLblFld = new Label("biospecimen.id", String.valueOf(biospecimen.getId()));

				nameLblFld = new Label("biospecimen.biospecimenUid", biospecimen.getBiospecimenUid());
				listDetailsLink.add(nameLblFld);
				rowDetailsWMC.add(listDetailsLink);

				studyLblFld = new Label("biospecimen.study.name", biospecimen.getStudy().getName());
				sampleTypeLblFld = new Label("biospecimen.sampleType.name", biospecimen.getSampleType().getName());
				collectionLblFld = new Label("biospecimen.bioCollection.biocollectionUid", biospecimen.getBioCollection().getBiocollectionUid());
				commentsLblFld = new Label("biospecimen.comments", biospecimen.getComments());

				biospecimen.setQuantity(iLimsService.getQuantityAvailable(biospecimen));
				if (biospecimen.getQuantity() == null) {
					quantityLblFld = new Label("biospecimen.quantity", "0");
				}
				else {
					quantityLblFld = new Label("biospecimen.quantity", biospecimen.getQuantity().toString());
				}
				if (biospecimen.getUnit() == null) {
					unitsLblFld = new Label("biospecimen.unit", "");
				}
				else {
					unitsLblFld = new Label("biospecimen.unit", biospecimen.getUnit().getName());
				}

				try {
					locationLbl = new Label("biospecimen.location", "view");
					locationLink = new ArkBusyAjaxLink("biospecimen.location.link") {

						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
							newModel.getObject().getBiospecimen().setId(biospecimen.getId());
							BiospecimenLocationVO biospecimenLocationVo;
							try {
								biospecimenLocationVo = iInventoryService.getBiospecimenLocation(biospecimen);
								newModel.getObject().setBiospecimenLocationVO(biospecimenLocationVo);
								BioLocationPanel bioLocationPanel = new BioLocationPanel("content", newModel) {
									/**
									 * 
									 */
									private static final long	serialVersionUID	= 1L;

									@Override
									public void refreshParentPanel(AjaxRequestTarget target) {
									}
								};
								bioLocationPanel.getUnallocateButton().setVisible(false);
								bioLocationPanel.add(new AttributeModifier("class", "detailsPanelBorder"));
								// Set the modalWindow title and content
								modalWindow.setTitle("Biospecimen Location Detail");
								modalWindow.setContent(bioLocationPanel);
								modalWindow.show(target);
							}
							catch (ArkSystemException e) {
								log.error(e.getMessage());
							}
						}
					};

					locationLink.add(locationLbl);

					BiospecimenLocationVO biospecimenLocationVo = iInventoryService.getBiospecimenLocation(biospecimen);
					if (!biospecimenLocationVo.getIsAllocated()) {
						locationLink.setVisible(false);
					}
				}
				catch (ArkSystemException e) {
					log.error(e.getMessage());
				}

				item.add(idLblFld);
				item.add(rowDetailsWMC);
				item.add(studyLblFld);
				item.add(sampleTypeLblFld);
				item.add(collectionLblFld);
				//item.add(commentsLblFld);
				item.add(quantityLblFld);
				item.add(unitsLblFld);
				item.add(locationLink);

				if (biospecimen.getBarcoded()) {
					item.addOrReplace(new ContextImage("biospecimen.barcoded", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.addOrReplace(new ContextImage("biospecimen.barcoded", new Model<String>("images/icons/cross.png")));
				}
				
				item.add(new AjaxButton("batchAliquot"){

					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;
					
					protected void onSubmit(AjaxRequestTarget target, org.apache.wicket.markup.html.form.Form<?> form) {
						onBatchAliquot(target, item.getModel());
						target.add(feedbackPanel);
					};
					
					protected void onError(AjaxRequestTarget target, org.apache.wicket.markup.html.form.Form<?> form) {
						target.add(feedbackPanel);
					};
					
					public boolean isVisible() {
						SecurityManager securityManager = ThreadContext.getSecurityManager();
						Subject currentUser = SecurityUtils.getSubject();
						boolean canCreate = ArkPermissionHelper.hasNewPermission(securityManager, currentUser);
						boolean validQuantity = (item.getModelObject().getQuantity() != null && item.getModelObject().getQuantity() > 0);
						return (canCreate && validQuantity);
					};
					
				}.setDefaultFormProcessing(false));

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {

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

		// Get session data (used for subject search)
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = null;
			boolean contextLoaded = false;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
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

		hasBioCollections = iLimsService.hasBioCollections(cpModel.getObject().getLinkSubjectStudy());

		if (hasBioCollections) {
			// Set new Biospecimen into model, then show modalWindow to save
			CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
			newModel.getObject().setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
			newModel.getObject().setStudy(cpModel.getObject().getStudy());
			newModel.getObject().getBiospecimen().setLinkSubjectStudy(getModelObject().getLinkSubjectStudy());
			newModel.getObject().getBiospecimen().setStudy(getModelObject().getLinkSubjectStudy().getStudy());
			//TODO ASAP handle this newModel.getObject().getBiospecimen().setBiospecimenUid(Constants.AUTO_GENERATED);
			if(getModelObject().getLinkSubjectStudy().getStudy().getAutoGenerateBiospecimenUid()){
				newModel.getObject().getBiospecimen().setBiospecimenUid(Constants.AUTO_GENERATED);
			}
			else{
				newModel.getObject().getBiospecimen().setBiospecimenUid("");
			}
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
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public void onClose(AjaxRequestTarget target)
         {
				// Refresh the list form whenever the modalwindow is closed
         	target.add(BiospecimenListForm.this);	
         }
		});
	}
	
	private void onBatchAliquot(AjaxRequestTarget target, IModel<Biospecimen> iModel) {
			IModel model = new Model<BatchBiospecimenAliquotsVO>(new BatchBiospecimenAliquotsVO(iModel.getObject()));
			// handles for auto-gen biospecimenUid or manual entry
			modalContentPanel = new BatchAliquotBiospecimenPanel("content", feedbackPanel, model, modalWindow);
				
			// Set the modalWindow title and content
			modalWindow.setTitle("Batch Aliquot Biospecimens");
			modalWindow.setContent(modalContentPanel);
			modalWindow.show(target);
			// refresh the feedback messages
			target.add(feedbackPanel);
		}

	/**
	 * @return the newButton
	 */
	public ArkBusyAjaxButton getNewButton() {
		return newButton;
	}

	/**
	 * @param newButton
	 *           the newButton to set
	 */
	public void setNewButton(ArkBusyAjaxButton newButton) {
		this.newButton = newButton;
	}
}