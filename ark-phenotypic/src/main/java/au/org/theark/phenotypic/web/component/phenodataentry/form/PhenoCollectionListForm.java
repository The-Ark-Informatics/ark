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
package au.org.theark.phenotypic.web.component.phenodataentry.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.core.web.component.panel.table.DataTablePanel;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoDataEntryModalDetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unchecked" })
public class PhenoCollectionListForm extends Form<PhenoDataCollectionVO> {

	private static final long								serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(PhenoCollectionListForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService								iPhenotypicService;

	protected CompoundPropertyModel<PhenoDataCollectionVO>			cpModel;
	protected FeedbackPanel									feedbackPanel;
	protected AbstractDetailModalWindow					modalWindow;

	private Label												idLbl;
	private Label												questionnaireLbl;
//	private Label												nameLbl;
	private Label												descriptionLbl;
	private Label												recordDateLbl;
	private Label												reviewedDateLbl;
	private Label												statusLbl;

	private Panel												modalContentPanel;
	protected ArkBusyAjaxButton							newButton;
	protected AjaxButton									getDataButton;

	protected WebMarkupContainer							dataViewListWMC;
	private DataView<PhenoCollection>				dataView;
	private ArkDataProvider2<PhenoDataCollectionVO, PhenoCollection>	PhenoCollectionProvider;
	private DropDownChoice<PhenoDataSetGroup>			phenoDataSetFieldGroupDdc;
	private WebMarkupContainer								phenoDataView;

	public PhenoCollectionListForm(String id, FeedbackPanel feedbackPanel, AbstractDetailModalWindow modalWindow, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
	}
	
	public void initialiseForm() {
		// Random exceptions occuring, Wicket suggests to implicitly set this, as it tries to auto-detect, but "some situations it cannot" 
		setMultiPart(true);
		
		modalContentPanel = new EmptyPanel("content");
//		ArkFunction associatedPrimaryFn = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
//		CompoundPropertyModel<PhenoDataCollectionVO> phenoDataCPM = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
//		phenoDataCPM.getObject().setArkFunction(associatedPrimaryFn);
//		modalContentPanel = new PhenoDataEntryContainerPanel("content", phenoDataCPM).initialisePanel();
		
		initialiseDataView();
		initialiseNewButton();
		initCustomFieldGroupDdc();
		
		phenoDataView = new EmptyPanel("phenoDataView");
		phenoDataView.setOutputMarkupPlaceholderTag(true);
		add(phenoDataView);
		
		initialiseGetDataButton();

		add(modalWindow);		
	}

	private void initCustomFieldGroupDdc() {
		LinkSubjectStudy linkSubjectStudy = cpModel.getObject().getPhenoCollection().getLinkSubjectStudy();
		List<PhenoDataSetGroup> pheDataSetGroupLst = iPhenotypicService.getPhenoDataSetGroupsByLinkSubjectStudy(linkSubjectStudy);
		
		ChoiceRenderer<PhenoDataSetGroup> renderer = new ChoiceRenderer<PhenoDataSetGroup>("name", "id");
		phenoDataSetFieldGroupDdc = new DropDownChoice<PhenoDataSetGroup>("customFieldGroupSelected", pheDataSetGroupLst);
		phenoDataSetFieldGroupDdc.setChoiceRenderer(renderer);
		
		phenoDataSetFieldGroupDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			private static final long	serialVersionUID	= 1L;

			protected void onUpdate(AjaxRequestTarget target) {
             // Enable getData button when customFieldGroup actually selected
             getDataButton.setEnabled(phenoDataSetFieldGroupDdc.getValue() != null && !phenoDataSetFieldGroupDdc.getValue().isEmpty());
             target.add(getDataButton);
             target.add(feedbackPanel);
         }
     });
		
		addOrReplace(phenoDataSetFieldGroupDdc);
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
				cpModel.getObject().getCustomFieldGroup().setStudy(study);
				cpModel.getObject().getPhenoCollection().setLinkSubjectStudy(linkSubjectStudy);
			}
		}

		super.onBeforeRender();
	}

	private void initialiseDataView() {
		dataViewListWMC = new WebMarkupContainer("dataViewListWMC");
		dataViewListWMC.setOutputMarkupId(true);
		// Data provider to paginate resultList
		PhenoCollectionProvider = new ArkDataProvider2<PhenoDataCollectionVO, PhenoCollection>() {

			private static final long	serialVersionUID	= 1L;

			public int size() {
				return (int)iPhenotypicService.getPhenoCollectionCount(criteriaModel.getObject());
			}

			public Iterator<PhenoCollection> iterator(int first, int count) {
				List<PhenoCollection> phenoCollectionList = new ArrayList<PhenoCollection>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					criteriaModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));
					phenoCollectionList = iPhenotypicService.searchPageablePhenoCollections(criteriaModel.getObject(), first, count);
				}
				return phenoCollectionList.iterator();
			}
		};
		// Set the criteria into the data provider's model
		PhenoCollectionProvider.setCriteriaModel(cpModel);

		dataView = buildDataView(PhenoCollectionProvider);
		dataView.setItemsPerPage(iArkCommonService.getUserConfig(Constants.CONFIG_ROWS_PER_PAGE).getIntValue());

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {

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
				this.error("Unexpected error: Unable to proceed with New");
			}
		};
		newButton.setDefaultFormProcessing(false);

		add(newButton);
	}
	
	private void initialiseGetDataButton() {
		getDataButton = new AjaxButton("getData") {

			private static final long	serialVersionUID	= 1L;
			
			@Override
			public boolean isEnabled() {
				return (true);
			}

			@Override
			public boolean isVisible() {
				boolean isVisible = true;

				String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				isVisible = (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH) && sessionSubjectUID != null);

				return isVisible;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if(phenoDataSetFieldGroupDdc.getValue() == null || phenoDataSetFieldGroupDdc.getValue().isEmpty()) {
					error("Please select a Data Set");	
				}
				else {
					LinkSubjectStudy linkSubjectStudy = cpModel.getObject().getPhenoCollection().getLinkSubjectStudy();
					
					List<PhenoDataSetGroup> phenoDataSetGroupList = new ArrayList<PhenoDataSetGroup>(0);
					PhenoDataSetGroup pdsg = iPhenotypicService.getPhenoFieldGroupById(new Long(phenoDataSetFieldGroupDdc.getValue()));
					phenoDataSetGroupList.add(pdsg);
					
					//List<CustomField> customFields = iPhenotypicService.getCustomFieldsLinkedToCustomFieldGroup(cfg);
					List<PhenoDataSetField> phenoSetFields =iPhenotypicService.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(pdsg);
					List<String> subjectUids = new ArrayList<String>(0);
					subjectUids.add(linkSubjectStudy.getSubjectUID());
					List<List<String>> dataSet = iPhenotypicService.getPhenoDataAsMatrix(linkSubjectStudy.getStudy(), subjectUids, phenoSetFields, phenoDataSetGroupList);
					
					phenoDataView = new DataTablePanel("phenoDataView", dataSet);
					PhenoCollectionListForm.this.addOrReplace(phenoDataView);
					target.add(phenoDataView);
				}
				target.add(feedbackPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to proceed with New");
				target.add(feedbackPanel);
			}
		};
		getDataButton.setDefaultFormProcessing(false);
		add(getDataButton);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of PhenoCollection
	 */
	public DataView<PhenoCollection> buildDataView(ArkDataProvider2<PhenoDataCollectionVO, PhenoCollection> PhenoCollectionProvider) {

		DataView<PhenoCollection> PhenoCollectionDataView = new DataView<PhenoCollection>("PhenoCollectionList", PhenoCollectionProvider) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<PhenoCollection> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final PhenoCollection PhenoCollection = item.getModelObject();

				idLbl = new Label("PhenoCollection.id", String.valueOf(PhenoCollection.getId()));
				questionnaireLbl = new Label("PhenoCollection.questionnaire", PhenoCollection.getQuestionnaire().getName());
				ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {

					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						PhenoCollection PhenoCollection = (PhenoCollection) (getParent().getDefaultModelObject());
						CompoundPropertyModel<PhenoDataCollectionVO> newModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
						newModel.getObject().getPhenoCollection().setId(PhenoCollection.getId());
						newModel.getObject().setCustomFieldGroup(cpModel.getObject().getCustomFieldGroup());
						showModalWindow(target, newModel);
					}
				};
				link.add(questionnaireLbl);
				
//				nameLbl = new Label("PhenoCollection.name", PhenoCollection.getName());
				descriptionLbl = new Label("PhenoCollection.description", PhenoCollection.getDescription());
				statusLbl = new Label("PhenoCollection.status", PhenoCollection.getStatus().getName());
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				recordDateLbl = new Label("PhenoCollection.recordDate", simpleDateFormat.format(PhenoCollection.getRecordDate()));
				
				if (PhenoCollection.getReviewedDate() == null){
					reviewedDateLbl = new Label("PhenoCollection.reviewedDate", "");
				}
				else {
					reviewedDateLbl = new Label("PhenoCollection.reviewedDate", simpleDateFormat.format(PhenoCollection.getReviewedDate()));	
				}

				item.add(idLbl);
				item.add(link);
//				item.add(nameLbl);
				item.add(descriptionLbl);
				item.add(statusLbl);
				item.add(recordDateLbl);
				item.add(reviewedDateLbl);
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel() {

					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}

		};

		return PhenoCollectionDataView;
	}

	protected void onNew(AjaxRequestTarget target) {
		// Needs CREATE permission AND at least one published CustomFieldGroup (Questionnaire) to select from
		boolean hasQuestionnaires = false;

		CustomFieldGroup questionnaire = new CustomFieldGroup();
		questionnaire.setArkFunction(cpModel.getObject().getArkFunction());
		questionnaire.setStudy(cpModel.getObject().getPhenoCollection().getLinkSubjectStudy().getStudy());
		questionnaire.setPublished(true);
		hasQuestionnaires = (iArkCommonService.getCustomFieldGroupCount(questionnaire) > 0);

		if (hasQuestionnaires) {
			// Set new Biospecimen into model, then show modalWindow to save
			CompoundPropertyModel<PhenoDataCollectionVO> newModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
			newModel.getObject().getPhenoCollection().setLinkSubjectStudy(getModelObject().getPhenoCollection().getLinkSubjectStudy());
			// the following should be replaced by a real "Questionnaire" selection when the new form is presented
			newModel.getObject().getPhenoCollection().setQuestionnaire(questionnaire);
			newModel.getObject().setCustomFieldGroup(cpModel.getObject().getCustomFieldGroup());

			showModalWindow(target, newModel); // listDetailsForm);
		}
		else {
			this.error("No published Questionnaires exist. Please create and publish at least one Questionnaire for the study.");
		}
		// refresh the feedback messages
		target.add(feedbackPanel);
	}

	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		cpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));
		modalContentPanel = new PhenoDataEntryModalDetailPanel("content", modalWindow, cpModel);

		// Set the modalWindow title and content
		modalWindow.setTitle("Subject Dataset Details");
		modalWindow.setContent(modalContentPanel);
		modalWindow.repaintComponent(getDataButton);
		// 2015-09-29 set windows call back
		modalWindow.setWindowClosedCallback(new WindowClosedCallback() {
			private static final long serialVersionUID = 1L; 
                @Override 
                public void onClose(AjaxRequestTarget target) 
                { 
                	initCustomFieldGroupDdc();
                    target.add(phenoDataSetFieldGroupDdc); 
                } 
        });
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