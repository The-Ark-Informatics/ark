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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.core.web.component.panel.table.DataTablePanel;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoDataSetCategoryOrderingHelper;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoClinicalDataValueEntryModalDetailPanel;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoCollectionDataEntryContainerPanel;
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
	private IPhenotypicService											iPhenotypicService;
	protected CompoundPropertyModel<PhenoDataCollectionVO>				cpModel;
	protected FeedbackPanel												feedbackPanel;
	protected AbstractDetailModalWindow									modalWindowDataSetDetail;
	private Label														idLbl;
	private Label														questionnaireLbl;
	private Label														descriptionLbl;
	private Label														recordDateLbl;
	private Label														reviewedDateLbl;
	private Label														statusLbl;
	private Panel														modalContentPanel;
	protected ArkBusyAjaxButton 										newButton;
	protected AjaxButton												getDataButton;
	protected WebMarkupContainer 										dataViewListWMC;
	private DataView<PhenoDataSetCollection>							dataView;
	private ArkDataProvider2<PhenoDataCollectionVO, PhenoDataSetCollection>	PhenoCollectionProvider;
	private DropDownChoice<PhenoDataSetGroup>							phenoDataSetFieldGroupDdc;
	private WebMarkupContainer categoryPanel;
	private DropDownChoice<PickedPhenoDataSetCategory>                  pickedPhenoDataSetCategoryDdc;
	private WebMarkupContainer											phenoDataView;
	protected AbstractDetailModalWindow									modalWindowClinicalDataSetValues;

	public PhenoCollectionListForm(String id, FeedbackPanel feedbackPanel,AbstractDetailModalWindow detailModalWindow, AbstractDetailModalWindow datasetValueModalWindow, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		this.modalWindowDataSetDetail = detailModalWindow;
		this.modalWindowClinicalDataSetValues=datasetValueModalWindow;
	}
	
	public void initialiseForm() {
		// Random exceptions occuring, Wicket suggests to implicitly set this, as it tries to auto-detect, but "some situations it cannot" 
		setMultiPart(true);
		modalContentPanel = new EmptyPanel("content");
		ArkFunction associatedPrimaryFn = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		CompoundPropertyModel<PhenoDataCollectionVO> phenoDataCPM = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
		phenoDataCPM.getObject().setArkFunction(associatedPrimaryFn);
		//modalContentPanel = new PhenoDataEntryContainerPanel("content", phenoDataCPM).initialisePanel();
		modalContentPanel=new PhenoCollectionDataEntryContainerPanel("content");
		initialiseDataView();
		initialiseNewButton();
		initPhenoFieldGroupDdc();
		initPhenoDataSetFieldCategoryDdc();
		phenoDataView = new EmptyPanel("phenoDataView");
		phenoDataView.setOutputMarkupPlaceholderTag(true);
		add(phenoDataView);
		initialiseGetDataButton();
		add(modalWindowDataSetDetail);	
		add(modalWindowClinicalDataSetValues);
	}

	private void initPhenoFieldGroupDdc() {
		LinkSubjectStudy linkSubjectStudy = cpModel.getObject().getPhenoDataSetCollection().getLinkSubjectStudy();
		List<PhenoDataSetGroup> pheDataSetGroupLst = iPhenotypicService.getPhenoDataSetGroupsByLinkSubjectStudy(linkSubjectStudy);
		ChoiceRenderer<PhenoDataSetGroup> renderer = new ChoiceRenderer<PhenoDataSetGroup>("name", "id");
		phenoDataSetFieldGroupDdc = new DropDownChoice<PhenoDataSetGroup>("pheDataSetGroupSelected", pheDataSetGroupLst);
		phenoDataSetFieldGroupDdc.setChoiceRenderer(renderer);
		phenoDataSetFieldGroupDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
		private static final long	serialVersionUID	= 1L;
		protected void onUpdate(AjaxRequestTarget target) {
			categoryPanel.remove(pickedPhenoDataSetCategoryDdc);
			//Create list of PickedPhenoDataSetCategories here for the hierarchy view of the category.
			List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=sortLst(remeoveDuplicates(populatePickedPhenoDataSetCategoriesFromdisplayListForPhenoDataSetGroup(phenoDataSetFieldGroupDdc.getModelObject())));
			List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategoriesHierachical=PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(pickedPhenoDataSetCategories);
			ChoiceRenderer renderer = new ChoiceRenderer("phenoDataSetCategory.name", "phenoDataSetCategory.id"){
				@Override
				public Object getDisplayValue(Object object) {
				PickedPhenoDataSetCategory pickedCat=(PickedPhenoDataSetCategory)object;
					return PhenoDataSetCategoryOrderingHelper.getInstance().preTextDecider(pickedCat)+ super.getDisplayValue(object);
				}
			};
			pickedPhenoDataSetCategoryDdc = new DropDownChoice<PickedPhenoDataSetCategory>("pickedPhenoDataSetCategory", pickedPhenoDataSetCategoriesHierachical,renderer);
			pickedPhenoDataSetCategoryDdc.setOutputMarkupId(true);
			categoryPanel.add(pickedPhenoDataSetCategoryDdc);
			target.add(pickedPhenoDataSetCategoryDdc);
			target.add(categoryPanel);
	         // Enable getData button when customFieldGroup actually selected
             getDataButton.setEnabled(phenoDataSetFieldGroupDdc.getValue() != null && !phenoDataSetFieldGroupDdc.getValue().isEmpty() &&
            		 pickedPhenoDataSetCategoryDdc.getValue() != null && !pickedPhenoDataSetCategoryDdc.getValue().isEmpty());
             target.add(getDataButton);
             target.add(feedbackPanel);
         }
     });
		addOrReplace(phenoDataSetFieldGroupDdc);
	}
	
	private void initPhenoDataSetFieldCategoryDdc(){
		categoryPanel = new WebMarkupContainer("categoryPanel");
		categoryPanel.setOutputMarkupId(true);
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=sortLst(remeoveDuplicates(populatePickedPhenoDataSetCategoriesFromdisplayListForPhenoDataSetGroup(cpModel.getObject().getPhenoDataSetCollection().getQuestionnaire())));
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategoriesHierachical=PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(pickedPhenoDataSetCategories);
		ChoiceRenderer renderer = new ChoiceRenderer("phenoDataSetCategory.name", "phenoDataSetCategory.id"){
			@Override
			public Object getDisplayValue(Object object) {
			PickedPhenoDataSetCategory pickedCat=(PickedPhenoDataSetCategory)object;
				return PhenoDataSetCategoryOrderingHelper.getInstance().preTextDecider(pickedCat)+ super.getDisplayValue(object);
			}
		};
		pickedPhenoDataSetCategoryDdc = new DropDownChoice<PickedPhenoDataSetCategory>("pickedPhenoDataSetCategory", pickedPhenoDataSetCategoriesHierachical);
		pickedPhenoDataSetCategoryDdc.setOutputMarkupId(true);
		pickedPhenoDataSetCategoryDdc.setChoiceRenderer(renderer);
		categoryPanel.add(pickedPhenoDataSetCategoryDdc);
		add(categoryPanel);
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
				cpModel.getObject().getPhenoDataSetGroup().setStudy(study);
				cpModel.getObject().getPhenoDataSetCollection().setLinkSubjectStudy(linkSubjectStudy);
			}
		}

		super.onBeforeRender();
	}

	private void initialiseDataView() {
		dataViewListWMC = new WebMarkupContainer("dataViewListWMC");
		dataViewListWMC.setOutputMarkupId(true);
		// Data provider to paginate resultList
		PhenoCollectionProvider = new ArkDataProvider2<PhenoDataCollectionVO, PhenoDataSetCollection>() {

			private static final long	serialVersionUID	= 1L;

			public int size() {
				return (int)iPhenotypicService.getPhenoCollectionCount(criteriaModel.getObject());
			}

			public Iterator<PhenoDataSetCollection> iterator(int first, int count) {
				List<PhenoDataSetCollection> phenoCollectionList = new ArrayList<PhenoDataSetCollection>();
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
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());

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
				if(phenoDataSetFieldGroupDdc.getValue().isEmpty() || pickedPhenoDataSetCategoryDdc.getValue().isEmpty()) {
					error("Please select the Dataset and Category first");	
				}
				else {
					LinkSubjectStudy linkSubjectStudy = cpModel.getObject().getPhenoDataSetCollection().getLinkSubjectStudy();
					List<PhenoDataSetGroup> phenoDataSetGroupList = new ArrayList<PhenoDataSetGroup>(0);
					PhenoDataSetGroup pdsg = iPhenotypicService.getPhenoFieldGroupById(new Long(phenoDataSetFieldGroupDdc.getValue()));
					phenoDataSetGroupList.add(pdsg);
					PhenoDataSetCategory phenoDataSetCategory =iPhenotypicService.getPhenoDataSetCategoryById(new Long(pickedPhenoDataSetCategoryDdc.getValue()));
					List<PhenoDataSetField> phenoSetFields =iPhenotypicService.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroupAndPhenoDataSetCategory(pdsg,phenoDataSetCategory);
					List<String> subjectUids = new ArrayList<String>(0);
					subjectUids.add(linkSubjectStudy.getSubjectUID());
					List<List<String>> dataSet = iPhenotypicService.getPhenoDataAsMatrix(linkSubjectStudy.getStudy(), subjectUids, phenoSetFields, phenoDataSetGroupList,phenoDataSetCategory);
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
	public DataView<PhenoDataSetCollection> buildDataView(ArkDataProvider2<PhenoDataCollectionVO, PhenoDataSetCollection> PhenoCollectionProvider) {

		DataView<PhenoDataSetCollection> PhenoCollectionDataView = new DataView<PhenoDataSetCollection>("PhenoDataSetCollectionList", PhenoCollectionProvider) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<PhenoDataSetCollection> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final PhenoDataSetCollection PhenoCollection = item.getModelObject();

				idLbl = new Label("phenoDataSetCollection.id", String.valueOf(PhenoCollection.getId()));
				questionnaireLbl = new Label("phenoDataSetCollection.questionnaire", PhenoCollection.getQuestionnaire().getName());
				ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {

					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						PhenoDataSetCollection phenoDataSetCollection= (PhenoDataSetCollection) (getParent().getDefaultModelObject());
						CompoundPropertyModel<PhenoDataCollectionVO> newModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
						newModel.getObject().setPhenoDataSetCollection(phenoDataSetCollection);
						showModalWindowDatasetDetail(target, newModel);
					}
				};
				link.add(questionnaireLbl);
				
//				nameLbl = new Label("PhenoCollection.name", PhenoCollection.getName());
				descriptionLbl = new Label("phenoDataSetCollection.description", PhenoCollection.getDescription());
				statusLbl = new Label("phenoDataSetCollection.status", PhenoCollection.getStatus().getName());
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				recordDateLbl = new Label("phenoDataSetCollection.recordDate", simpleDateFormat.format(PhenoCollection.getRecordDate()));
				
				if (PhenoCollection.getReviewedDate() == null){
					reviewedDateLbl = new Label("phenoDataSetCollection.reviewedDate", "");
				}
				else {
					reviewedDateLbl = new Label("phenoDataSetCollection.reviewedDate", simpleDateFormat.format(PhenoCollection.getReviewedDate()));	
				}

				item.add(idLbl);
				item.add(link);
				item.add(descriptionLbl);
				item.add(statusLbl);
				item.add(recordDateLbl);
				item.add(reviewedDateLbl);
				item.add(buildmodalWindowClinicalDataSetValuesButton());
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
		
		boolean hasQuestionnaires = false;
		hasQuestionnaires = (iPhenotypicService.getPhenoFieldGroupCount(cpModel.getObject().getPhenoDataSetCollection().getLinkSubjectStudy().getStudy(),
				cpModel.getObject().getArkFunction(),true) > 0);
		if (hasQuestionnaires) {
			// Set new Biospecimen into model, then show modalWindow to save
			CompoundPropertyModel<PhenoDataCollectionVO> newModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
			newModel.getObject().getPhenoDataSetCollection().setLinkSubjectStudy(cpModel.getObject().getPhenoDataSetCollection().getLinkSubjectStudy());
			showModalWindowDatasetDetail(target, newModel);
		}
		else {
			this.error("No published Questionnaires exist. Please create and publish at least one Questionnaire for the study.");
		}
		// refresh the feedback messages
		target.add(feedbackPanel);
	}

	protected void showModalWindowDatasetDetail(AjaxRequestTarget target, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		cpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));
		modalContentPanel = new PhenoDataEntryModalDetailPanel("content", modalWindowDataSetDetail, cpModel);
		// Set the modalWindow title and content
		modalWindowDataSetDetail.setTitle("Dataset Details");
		modalWindowDataSetDetail.setContent(modalContentPanel);
		modalWindowDataSetDetail.repaintComponent(getDataButton);
		// 2015-09-29 set windows call back
		modalWindowDataSetDetail.setWindowClosedCallback(new WindowClosedCallback() {
			private static final long serialVersionUID = 1L; 
                @Override 
                public void onClose(AjaxRequestTarget target) 
                { 
                	initPhenoFieldGroupDdc();
                    target.add(phenoDataSetFieldGroupDdc); 
                } 
        });
		modalWindowDataSetDetail.show(target);
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
	private List<PickedPhenoDataSetCategory> populatePickedPhenoDataSetCategoriesFromdisplayListForPhenoDataSetGroup(PhenoDataSetGroup phenoDataSetGroup){
		List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplays=iPhenotypicService.getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroupOrderByPhenoDataSetCategory(phenoDataSetFieldGroupDdc.getModelObject());
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=new ArrayList<PickedPhenoDataSetCategory>();
		for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplays) {
			PickedPhenoDataSetCategory pickedPhenoDataSetCategory=new PickedPhenoDataSetCategory();
			pickedPhenoDataSetCategory.setArkFunction(phenoDataSetFieldDisplay.getPhenoDataSetGroup().getArkFunction());
			pickedPhenoDataSetCategory.setStudy(phenoDataSetFieldDisplay.getPhenoDataSetGroup().getStudy());
			pickedPhenoDataSetCategory.setPhenoDataSetCategory(phenoDataSetFieldDisplay.getPhenoDataSetCategory());
			if(phenoDataSetFieldDisplay.getParentPhenoDataSetCategory()!=null){
				pickedPhenoDataSetCategory.setParentPickedPhenoDataSetCategory(findPickedPhenoDataSetCategoryFromSameList(pickedPhenoDataSetCategories, phenoDataSetFieldDisplay.getParentPhenoDataSetCategory()));
			}
			pickedPhenoDataSetCategory.setOrderNumber(phenoDataSetFieldDisplay.getPhenoDataSetCategoryOrderNumber());
			pickedPhenoDataSetCategories.add(pickedPhenoDataSetCategory);
		}
		return pickedPhenoDataSetCategories;
	}
	private PickedPhenoDataSetCategory findPickedPhenoDataSetCategoryFromSameList(List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories,PhenoDataSetCategory phenoDataSetCategoryToBefind){
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedPhenoDataSetCategories) {
			if (pickedPhenoDataSetCategory.getPhenoDataSetCategory().equals(phenoDataSetCategoryToBefind)){
				return pickedPhenoDataSetCategory;
			}
		}
		return null;
	}
	
	/**
	 * Sort custom field list according to the order number.
	 * @param customFieldLst
	 * @return
	 */
	private  List<PickedPhenoDataSetCategory> sortLst(List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories){
		//sort by order number.
		Collections.sort(pickedPhenoDataSetCategories, new Comparator<PickedPhenoDataSetCategory>(){
		    public int compare(PickedPhenoDataSetCategory custFieldCategory1, PickedPhenoDataSetCategory custFieldCatCategory2) {
		        return custFieldCategory1.getOrderNumber().compareTo(custFieldCatCategory2.getOrderNumber());
		    }
		});
				return pickedPhenoDataSetCategories;
	}
	/**
	 * Remove duplicates from list
	 * @param customFieldLst
	 * @return
	 */
	private  List<PickedPhenoDataSetCategory> remeoveDuplicates(List<PickedPhenoDataSetCategory> phenoDataSetCategories){
		Set<PickedPhenoDataSetCategory> phenoDataSetCategoriesSet=new HashSet<PickedPhenoDataSetCategory>();
		List<PickedPhenoDataSetCategory> phenoDataSetCategoriesNew=new ArrayList<PickedPhenoDataSetCategory>();
		phenoDataSetCategoriesSet.addAll(phenoDataSetCategories);
		phenoDataSetCategoriesNew.addAll(phenoDataSetCategoriesSet);
				return phenoDataSetCategoriesNew;
	}
	
	/**
	 * 
	 * @param upload
	 * @return
	 */
	private AjaxButton buildmodalWindowClinicalDataSetValuesButton(){
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.core.Constants.EDIT){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PhenoDataSetCollection phenoDataSetCollection= (PhenoDataSetCollection) (getParent().getDefaultModelObject());
				CompoundPropertyModel<PhenoDataCollectionVO> newModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
				newModel.getObject().setPhenoDataSetCollection(phenoDataSetCollection);
				showModalWindowClinicalDatasetValueDetail(target, newModel);
				modalWindowClinicalDataSetValues.show(target);
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("onError called when clinical data set value button pressed");
			};
		};
		ajaxButton.setDefaultFormProcessing(false);
		return ajaxButton;
	}
	protected void showModalWindowClinicalDatasetValueDetail(AjaxRequestTarget target, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		cpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));
		modalContentPanel = new PhenoClinicalDataValueEntryModalDetailPanel("content", modalWindowClinicalDataSetValues, cpModel);
		// Set the modalWindow title and content
		modalWindowClinicalDataSetValues.setTitle("Clinical Dataset Values");
		modalWindowClinicalDataSetValues.setContent(modalContentPanel);
		modalWindowClinicalDataSetValues.repaintComponent(getDataButton);
		// 2015-09-29 set windows call back
		modalWindowClinicalDataSetValues.setWindowClosedCallback(new WindowClosedCallback() {
			private static final long serialVersionUID = 1L; 
                @Override 
                public void onClose(AjaxRequestTarget target) 
                { 
                	initPhenoFieldGroupDdc();
                    target.add(phenoDataSetFieldGroupDdc); 
                } 
        });
		modalWindowClinicalDataSetValues.show(target);
	}
}