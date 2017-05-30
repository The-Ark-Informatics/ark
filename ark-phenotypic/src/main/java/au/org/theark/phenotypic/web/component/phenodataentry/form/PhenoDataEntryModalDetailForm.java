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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoCollectionDataEntryContainerPanel;

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
		private DropDownChoice<PhenoDataSetGroup>		             questionnaireDdc;                 
		private DropDownChoice<QuestionnaireStatus>		             statusDdc;                        
		private TextArea<String>						             descriptionTxtAreaFld;            
		private DateTextField							             recordDateTxtFld;                 
		private DropDownChoice<ArkUser>					             reviewedByDdc;                    
		private DateTextField							             reviewedDateTxtFld;               
		private ModalWindow								             modalWindow;                      
		protected Label									             jQueryLabel;                      

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
				this.error("Cannot edit this record - it has been invalidated (e.g. deleted)");
			}
		}
	}
	private List<PickedPhenoDataSetCategory> populatePickedPhenoDataSetCategoriesFromdisplayListForPhenoDataSetGroup(PhenoDataSetGroup phenoDataSetGroup){
		List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplays=iPhenotypicService.getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroupOrderByPhenoDataSetCategory(phenoDataSetGroup);
		
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=new ArrayList<PickedPhenoDataSetCategory>();
		for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplays) {
			//Add only the categories if they only have the custom fields.Otherwise no point of showing them.   
			if(phenoDataSetFieldDisplay.getPhenoDataSetField()!=null){
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
			//
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
	
	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("phenoDataSetCollection.id");
		idTxtFld.setEnabled(false);	// automatically generated

		descriptionTxtAreaFld = new TextArea<String>("phenoDataSetCollection.description");
		recordDateTxtFld = new DateTextField("phenoDataSetCollection.recordDate", new PatternDateConverter( au.org.theark.core.Constants.DD_MM_YYYY, false));
		reviewedDateTxtFld = new DateTextField("phenoDataSetCollection.reviewedDate", new PatternDateConverter( au.org.theark.core.Constants.DD_MM_YYYY, false));

		ArkDatePicker recordDatePicker = new ArkDatePicker();
		recordDatePicker.bind(recordDateTxtFld);
		recordDateTxtFld.add(recordDatePicker);

		ArkDatePicker reviewedDatePicker = new ArkDatePicker();
		reviewedDatePicker.bind(reviewedDateTxtFld);
		reviewedDateTxtFld.add(reviewedDatePicker);
		initQuestionnaireDdc();
		initStatusDdc();
		initReviewedByDdc();
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
		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		if (cpModel.getObject().getPhenoDataSetCollection().getId() == null) {
			// Save
			iPhenotypicService.createCollection(cpModel.getObject().getPhenoDataSetCollection());
			this.info("Subject Dataset " + cpModel.getObject().getPhenoDataSetCollection().getId() + " was successfully created.");
			processErrors(target);

		}
		else {
			// Update
			iPhenotypicService.updateCollection(cpModel.getObject().getPhenoDataSetCollection());
			this.info("Subject Dataset " + cpModel.getObject().getPhenoDataSetCollection().getId() + " was successfully updated.");
			processErrors(target);
			
		}

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
		containerPanel.info("Subject Dataset with dataset values" + cpModel.getObject().getPhenoDataSetCollection().getQuestionnaire().getName() + " was successfully deleted.");
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