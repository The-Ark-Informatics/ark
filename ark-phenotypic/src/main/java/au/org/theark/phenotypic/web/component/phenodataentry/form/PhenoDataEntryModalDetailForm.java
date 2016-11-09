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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.org.theark.core.model.pheno.entity.*;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButtons;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.widget.dialog.MessageDialog;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoDataSetCategoryOrderingHelper;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoCollectionDataEntryContainerPanel;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoDataDataViewPanel;

/**
 * Detail form for Phenotypic Collection, as displayed within a modal window
 * 
 * @author elam
 */
public class PhenoDataEntryModalDetailForm extends AbstractModalDetailForm<PhenoDataCollectionVO> {

	private static final long					serialVersionUID	= 2727419197330261916L;
	private static final Logger				log					= LoggerFactory.getLogger(PhenoDataEntryModalDetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
		private IPhenotypicService								     iPhenotypicService;               
		private TextField<String>						             idTxtFld;                         
	//	private TextField<String>								   	 nameTxtFld;                           
		private DropDownChoice<PhenoDataSetGroup>		             questionnaireDdc;                 
		private DropDownChoice<QuestionnaireStatus>		             statusDdc;                        
		private TextArea<String>						             descriptionTxtAreaFld;            
		private DateTextField							             recordDateTxtFld;                 
		private DropDownChoice<ArkUser>					             reviewedByDdc;                    
		private DateTextField							             reviewedDateTxtFld;               
		private Panel									             phenoCollectionDataEntryPanel;    
		private ModalWindow								             modalWindow;                      
		private AjaxPagingNavigator						             dataEntryNavigator;               
		private WebMarkupContainer						             dataEntryWMC;                     
		protected Label									             jQueryLabel;                      
		private DropDownChoice<PickedPhenoDataSetCategory>           pickedPhenoDataSetCategoryDdc;
		private WebMarkupContainer 									 categoryPanel;

	private List<PhenoDataSetData> state;
	private int currentPage = 0;
	private int toPage;

	/**
	 * Constructor
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param cpModel
	 * @param jQueryLabel
	 */
	public PhenoDataEntryModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<PhenoDataCollectionVO> cpModel, Label jQueryLabel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
		refreshEntityFromBackend();
		this.jQueryLabel = jQueryLabel;
	}

	protected void refreshEntityFromBackend() {
		// Get the Biospecimen entity fresh from backend
		PhenoDataSetCollection pc = cpModel.getObject().getPhenoDataSetCollection();
		if (pc.getId() != null) {
			pc = iPhenotypicService.getPhenoCollection(pc.getId());
			cpModel.getObject().setPhenoDataSetCollection(pc);
			if (pc == null) {
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
			}
		}
	}
	private void initPhenoDataSetFieldCategoryDdc(PhenoDataSetGroup phenoDataSetGroup){
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=populatePickedPhenoDataSetCategoriesFromdisplayListForPhenoDataSetGroup(phenoDataSetGroup);
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategoriesHierachical=PhenoDataSetCategoryOrderingHelper.getInstance().orderHierarchicalyphenoDatasetCategories(pickedPhenoDataSetCategories);
		ChoiceRenderer renderer = new ChoiceRenderer("phenoDataSetCategory.name", "phenoDataSetCategory.id"){
			@Override
			public Object getDisplayValue(Object object) {
			PickedPhenoDataSetCategory pickedCat=(PickedPhenoDataSetCategory)object;
				return PhenoDataSetCategoryOrderingHelper.getInstance().preTextDecider(pickedCat)+ super.getDisplayValue(object);
			}
		};
		pickedPhenoDataSetCategoryDdc = new DropDownChoice<PickedPhenoDataSetCategory>("pickedPhenoDataSetCategory", new PropertyModel<PickedPhenoDataSetCategory>(cpModel, "pickedPhenoDataSetCategory"), pickedPhenoDataSetCategoriesHierachical,renderer);
		pickedPhenoDataSetCategoryDdc.setOutputMarkupId(true);
		pickedPhenoDataSetCategoryDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				//Remove
				dataEntryWMC.remove(phenoCollectionDataEntryPanel);
				dataEntryWMC.remove(dataEntryNavigator);
				arkCrudContainerVo.getDetailPanelFormContainer().remove(dataEntryWMC);
				//Create
				if(pickedPhenoDataSetCategoryDdc.getModelObject()!=null){
					initialisePhenoCollectionDataEntry(pickedPhenoDataSetCategoryDdc.getModelObject().getPhenoDataSetCategory());
				}
				//Add
				dataEntryWMC.add(phenoCollectionDataEntryPanel);
				dataEntryWMC.add(dataEntryNavigator);
				arkCrudContainerVo.getDetailPanelFormContainer().add(dataEntryWMC);
				//target
				target.add(phenoCollectionDataEntryPanel);
				target.add(dataEntryNavigator);
				target.add(dataEntryWMC);
			}
		});
	}
	private List<PickedPhenoDataSetCategory> populatePickedPhenoDataSetCategoriesFromdisplayListForPhenoDataSetGroup(PhenoDataSetGroup phenoDataSetGroup){
		List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplays=iPhenotypicService.getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroupOrderByPhenoDataSetCategory(phenoDataSetGroup);
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

	private boolean initialisePhenoCollectionDataEntry(PhenoDataSetCategory phenoDataSetCategory) {

		AbstractDefaultAjaxBehavior confirmBehaviour = new AbstractDefaultAjaxBehavior() {
			@Override
			protected void respond(AjaxRequestTarget target) {
				System.out.println("onConfirm");
				System.out.println(dataEntryNavigator.getPageable().getCurrentPage());
				dataEntryNavigator.getPageable().setCurrentPage(toPage);
				System.out.println(dataEntryNavigator.getPageable().getCurrentPage());
				currentPage = toPage;
				target.add(dataEntryWMC);
			}
		};

		add(confirmBehaviour);

		boolean replacePanel = false;
		//if (!(phenoCollectionDataEntryPanel instanceof PhenoDataDataViewPanel)) {
			CompoundPropertyModel<PhenoDataCollectionVO> phenoDataCpModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
			phenoDataCpModel.getObject().setPhenoDataSetCollection(cpModel.getObject().getPhenoDataSetCollection());
			phenoDataCpModel.getObject().getPhenoDataSetCollection().setQuestionnaire(cpModel.getObject().getPhenoDataSetCollection().getQuestionnaire());
			phenoDataCpModel.getObject().setArkFunction(cpModel.getObject().getArkFunction());

			PhenoDataDataViewPanel phenoCFDataEntryPanel;
			if(phenoDataSetCategory!=null){
				phenoCFDataEntryPanel = new PhenoDataDataViewPanel("phenoCFDataEntryPanel", phenoDataCpModel) {
					@Override
					protected void onBeforeRender() {
						super.onBeforeRender();
						System.out.println("Items on load:");
                        if(state == null) {
							state = new ArrayList<PhenoDataSetData>();
						} else {
							state.clear();
						}
						Iterator<Item<PhenoDataSetData>> itemsIterator = this.getDataView().getItems();
						while(itemsIterator.hasNext()) {
							Item<PhenoDataSetData> item = itemsIterator.next();
							System.out.println(item.getModelObject().getId());
							switch (item.getModelObject().getPhenoDataSetFieldDisplay().getPhenoDataSetField().getFieldType().getName()) {
								case "CHARACTER":
									System.out.println(item.getModelObject().getTextDataValue());
									break;
								case "NUMBER":
									System.out.println(item.getModelObject().getNumberDataValue());
									break;
								case "DATE":
									System.out.println(item.getModelObject().getDateDataValue());
									break;
							}
							if (item.getModelObject() != null) {
								state.add(item.getModelObject().deepCopy());
							} else {
								state.add(null);
							}
						}
					}
				}.initialisePanel(iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_CUSTOM_FIELDS_PER_PAGE).getIntValue(),phenoDataSetCategory);
			}else{
				 phenoCFDataEntryPanel = new PhenoDataDataViewPanel("phenoCFDataEntryPanel", phenoDataCpModel)
				.initialisePanel(iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_CUSTOM_FIELDS_PER_PAGE).getIntValue(),null);
			}
			dataEntryNavigator = new AjaxPagingNavigator("dataEntryNavigator", phenoCFDataEntryPanel.getDataView()) {
				private static final long	serialVersionUID	= 1L;

				@Override
				protected void onAjaxEvent(AjaxRequestTarget target) {

					List<PhenoDataSetData> stateCheck = new ArrayList<PhenoDataSetData>();

					System.out.println("Changed Page: ");
					System.out.println(dataEntryNavigator.getPageable().getCurrentPage());
                    System.out.println("Pheno Data on this page:");
                    Iterator<Item<PhenoDataSetData>> itemsIterator = phenoCFDataEntryPanel.getDataView().getItems();
                    while(itemsIterator.hasNext()) {
						Item<PhenoDataSetData> item = itemsIterator.next();
						System.out.println(item.getModelObject().getId());
						stateCheck.add(item.getModelObject());
					}

					boolean unequalFlag = false;
					System.out.println("Comparing contents before page change");
					for(int i = 0; i < state.size(); i++) {
						PhenoDataSetData stateP = state.get(i);
                        PhenoDataSetData stateC = stateCheck.get(i);
						if(stateC.getPhenoDataSetFieldDisplay() != null) {
							switch (stateC.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getFieldType().getName()) {
								case "CHARACTER":
									System.out.println(stateP.getTextDataValue() + " " + stateC.getTextDataValue() + " " + stateP.equals(stateC));
									break;
								case "NUMBER":
									System.out.println(stateP.getNumberDataValue() + " " + stateC.getNumberDataValue() + " " + stateP.equals(stateC));
									break;
								case "DATE":
									System.out.println(stateP.getDateDataValue() + " " + stateC.getDateDataValue() + " " + stateP.equals(stateC));
									break;
							}
						}
						if(!stateP.equals(stateC)) { //if the states aren't equal
							unequalFlag = true;
							break;
						}
					}
					if(unequalFlag) {
						toPage = dataEntryNavigator.getPageable().getCurrentPage();
						dataEntryNavigator.getPageable().setCurrentPage(currentPage);
						target.appendJavaScript("if(confirm('You have unsaved data. If you change page, your unsaved changes will be lost. Are you sure you want to switch page?')) { $.ajax('" + confirmBehaviour.getCallbackUrl() + "') } else { };");
					} else {
						target.add(dataEntryWMC);
						currentPage = dataEntryNavigator.getPageable().getCurrentPage();
					}
				}
			};
			//dataEntryNavigator = new ArkAjaxPagingNavigator("dataEntryNavigator", phenoCFDataEntryPanel.getDataView(), dataEntryWMC, jQueryLabel);
			phenoCollectionDataEntryPanel = phenoCFDataEntryPanel;
			//replacePanel = true;
		//}
		return replacePanel;
	}
	
	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("phenoDataSetCollection.id");
		idTxtFld.setEnabled(false);	// automatically generated
		
//		nameTxtFld = new TextField<String>("PhenoCollection.name");
		descriptionTxtAreaFld = new TextArea<String>("phenoDataSetCollection.description");
		recordDateTxtFld = new DateTextField("phenoDataSetCollection.recordDate", au.org.theark.core.Constants.DD_MM_YYYY);
		reviewedDateTxtFld = new DateTextField("phenoDataSetCollection.reviewedDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker recordDatePicker = new ArkDatePicker();
		recordDatePicker.bind(recordDateTxtFld);
		recordDateTxtFld.add(recordDatePicker);

		ArkDatePicker reviewedDatePicker = new ArkDatePicker();
		reviewedDatePicker.bind(reviewedDateTxtFld);
		reviewedDateTxtFld.add(reviewedDatePicker);
		
		//Add category panel
		categoryPanel=new WebMarkupContainer("categoryPanel");
		categoryPanel.setOutputMarkupId(true);
		
		initQuestionnaireDdc();
		initStatusDdc();
		initReviewedByDdc();
		dataEntryWMC = new WebMarkupContainer("dataEntryWMC");
		dataEntryWMC.setOutputMarkupId(true);
		initPhenoDataSetFieldCategoryDdc(cpModel.getObject().getPhenoDataSetCollection().getQuestionnaire());

		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=populatePickedPhenoDataSetCategoriesFromdisplayListForPhenoDataSetGroup(cpModel.getObject().getPhenoDataSetCollection().getQuestionnaire());
		PhenoDataSetCategory phenoDataSetCategory = null;
		if(pickedPhenoDataSetCategories.size() == 1) {
			PickedPhenoDataSetCategory pickedPhenoDataSetCategory = pickedPhenoDataSetCategories.get(0);
			phenoDataSetCategory = pickedPhenoDataSetCategory.getPhenoDataSetCategory();
			pickedPhenoDataSetCategoryDdc.setModelObject(pickedPhenoDataSetCategory);
		}

		initialisePhenoCollectionDataEntry(phenoDataSetCategory);
		attachValidators();
		addComponents();

		// Focus on Questionnaire
		questionnaireDdc.add(new ArkDefaultFormFocusBehavior());
		
	}

	private void initQuestionnaireDdc() {
		// Get a list of questionnaires for the subject in context by default
		PhenoDataSetGroup pfgForStudyCriteria =cpModel.getObject().getPhenoDataSetGroup();
		pfgForStudyCriteria.setStudy(cpModel.getObject().getPhenoDataSetCollection().getLinkSubjectStudy().getStudy());
		// NB: Assumes that CustomFieldGroup will always be used for criteria (not a true entity)
		pfgForStudyCriteria.setArkFunction(cpModel.getObject().getArkFunction());
		pfgForStudyCriteria.setPublished(true);	//make sure that we don't return non-published Questionnaires
		
		List<PhenoDataSetGroup> questionnaireList =iPhenotypicService.getPhenoDataSetGroups(pfgForStudyCriteria, 0, Integer.MAX_VALUE);
		//List<CustomFieldGroup> questionnaireList = iArkCommonService.getCustomFieldGroups(pfgForStudyCriteria, 0, Integer.MAX_VALUE);
		ChoiceRenderer<PhenoDataSetGroup> choiceRenderer = new ChoiceRenderer<PhenoDataSetGroup>(Constants.PHENO_COLLECTION_NAME, Constants.PHENO_COLLECTION_ID);
		questionnaireDdc = new DropDownChoice<PhenoDataSetGroup>("phenoDataSetCollection.questionnaire", (List<PhenoDataSetGroup>) questionnaireList, choiceRenderer);
		questionnaireDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				categoryPanel.remove(pickedPhenoDataSetCategoryDdc);
				initPhenoDataSetFieldCategoryDdc(questionnaireDdc.getModelObject());
				categoryPanel.add(pickedPhenoDataSetCategoryDdc);
		    	target.add(pickedPhenoDataSetCategoryDdc);
		    	target.add(categoryPanel);
			}
		});
		if (!isNew()) {
			questionnaireDdc.setEnabled(false);	//can't change questionnaire after creating the phenoCollection
			
		}
		
	}

	private void initStatusDdc() {
		// Get a list of status
		List<QuestionnaireStatus> statusList = iPhenotypicService.getPhenoCollectionStatusList();
		ChoiceRenderer<QuestionnaireStatus> choiceRenderer = new ChoiceRenderer<QuestionnaireStatus>("name", "id");
		statusDdc = new DropDownChoice<QuestionnaireStatus>("phenoDataSetCollection.status", statusList, choiceRenderer);
		statusDdc.setNullValid(false);
	}

	private void initReviewedByDdc() {
		String sessionUserName	= (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_USERID);
		ArkUser arkUser = null;
		try {
			 arkUser=iArkCommonService.getArkUser(sessionUserName);
		} catch (EntityNotFoundException e1) {
			e1.printStackTrace();
		}
		List<ArkUser> arkUserList = new ArrayList<ArkUser>(0);
		arkUserList = iArkCommonService.getArkUserListByStudy(arkUser,cpModel.getObject().getPhenoDataSetCollection().getLinkSubjectStudy().getStudy());
		ChoiceRenderer<ArkUser> choiceRenderer = new ChoiceRenderer<ArkUser>("ldapUserName", "id");
		reviewedByDdc = new DropDownChoice<ArkUser>("phenoDataSetCollection.reviewedBy", arkUserList, choiceRenderer);
	}

	protected void attachValidators() {
		questionnaireDdc.setRequired(true);
		recordDateTxtFld.setRequired(true);
		statusDdc.setRequired(true);
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(questionnaireDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(recordDateTxtFld);
//		arkCrudContainerVo.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(descriptionTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(statusDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(reviewedByDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(reviewedDateTxtFld);
		categoryPanel.add(pickedPhenoDataSetCategoryDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(categoryPanel);
		dataEntryWMC.add(phenoCollectionDataEntryPanel);
		dataEntryWMC.add(dataEntryNavigator);
		arkCrudContainerVo.getDetailPanelFormContainer().add(dataEntryWMC);

		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		if (cpModel.getObject().getPhenoDataSetCollection().getId() == null) {
			// Save
			iPhenotypicService.createCollection(cpModel.getObject().getPhenoDataSetCollection());
			this.info("Subject Dataset " + cpModel.getObject().getPhenoDataSetCollection().getId() + " was created successfully");
			processErrors(target);

		}
		else {
			// Update
			iPhenotypicService.updateCollection(cpModel.getObject().getPhenoDataSetCollection());
			this.info("Subject Dataset " + cpModel.getObject().getPhenoDataSetCollection().getId() + " was updated successfully");
			processErrors(target);
			
		}
		// Allow the PheotyocCollection data to be saved any time save is performed
		if (phenoCollectionDataEntryPanel instanceof PhenoDataDataViewPanel) {
			((PhenoDataDataViewPanel) phenoCollectionDataEntryPanel).savePhenoData();
		}
		// refresh the CF data entry panel (if necessary)
		//if (initialisePhenoCollectionDataEntry() == true) {
			dataEntryWMC.addOrReplace(phenoCollectionDataEntryPanel);
			dataEntryWMC.addOrReplace(dataEntryNavigator);
		//}

		onSavePostProcess(target);
	}

	@Override
	protected void onClose(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		iPhenotypicService.deletePhenoCollection(cpModel.getObject().getPhenoDataSetCollection());

		// Base containerForm for pheno data entry unfortunately way up the chain...thus a lot of getParent() calls. Not the neatest method by any means		
		PhenoCollectionDataEntryContainerPanel containerPanel = (PhenoCollectionDataEntryContainerPanel) this.getParent().getParent().getParent().getParent().getParent().getParent();
		containerPanel.info("Subject Dataset " + cpModel.getObject().getPhenoDataSetCollection().getQuestionnaire().getName() + " was deleted successfully");
		target.add(containerPanel.getFeedbackPanel());
		onClose(target);
		processErrors(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		return cpModel.getObject().getPhenoDataSetCollection().getId() == null;
	}
}