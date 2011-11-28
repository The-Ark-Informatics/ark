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
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoDataDataViewPanel;

/**
 * Detail form for Phenotypic Collection, as displayed within a modal window
 * 
 * @author elam
 */
public class PhenotypicCollectionModalDetailForm extends AbstractModalDetailForm<PhenoDataCollectionVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 2727419197330261916L;
	private static final Logger				log					= LoggerFactory.getLogger(PhenotypicCollectionModalDetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					iPhenotypicService;

	private TextField<String>					idTxtFld;
	private TextField<String>					nameTxtFld;
	private DropDownChoice<CustomFieldGroup>	questionnaireDdc;
	private DropDownChoice<QuestionnaireStatus>	statusDdc;
	private TextArea<String>					descriptionTxtAreaFld;
	private DateTextField						recordDateTxtFld;
	private DropDownChoice<ArkUser>			reviewedByDdc;
	private DateTextField						reviewedDateTxtFld;

	private Panel									phenoCollectionDataEntryPanel;
	private ModalWindow							modalWindow;
	private AjaxPagingNavigator				dataEntryNavigator;
	private WebMarkupContainer					dataEntryWMC;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param listDetailPanel
	 */
	public PhenotypicCollectionModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
		refreshEntityFromBackend();
	}

	protected void refreshEntityFromBackend() {
		// Get the Biospecimen entity fresh from backend
		PhenotypicCollection pc = cpModel.getObject().getPhenotypicCollection();

		if (pc.getId() != null) {
			pc = iPhenotypicService.getPhenotypicCollection(pc.getId());
			cpModel.getObject().setPhenotypicCollection(pc);
			if (pc == null) {
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
			}
		}
	}

	private boolean initialisePhenotypicCollectionDataEntry() {
		boolean replacePanel = false;
		PhenotypicCollection pc = cpModel.getObject().getPhenotypicCollection();
		if (!(phenoCollectionDataEntryPanel instanceof PhenoDataDataViewPanel)) {
			CompoundPropertyModel<PhenoDataCollectionVO> phenoDataCpModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
			phenoDataCpModel.getObject().setPhenotypicCollection(pc);
			phenoDataCpModel.getObject().setArkFunction(cpModel.getObject().getArkFunction());
			PhenoDataDataViewPanel phenoCFDataEntryPanel = new PhenoDataDataViewPanel("phenoCFDataEntryPanel", phenoDataCpModel).initialisePanel(au.org.theark.core.Constants.ROWS_PER_PAGE);
			dataEntryNavigator = new AjaxPagingNavigator("dataEntryNavigator", phenoCFDataEntryPanel.getDataView()) {
				@Override
				protected void onAjaxEvent(AjaxRequestTarget target) {
					target.add(dataEntryWMC);
				}
			};
			phenoCollectionDataEntryPanel = phenoCFDataEntryPanel;
			replacePanel = true;
		}
		return replacePanel;
	}
	
	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("phenotypicCollection.id");
		idTxtFld.setEnabled(false);	// automatically generated
		
		nameTxtFld = new TextField<String>("phenotypicCollection.name");
		descriptionTxtAreaFld = new TextArea<String>("phenotypicCollection.description");
		recordDateTxtFld = new DateTextField("phenotypicCollection.recordDate", au.org.theark.core.Constants.DD_MM_YYYY);
		reviewedDateTxtFld = new DateTextField("phenotypicCollection.reviewedDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker recordDatePicker = new ArkDatePicker();
		recordDatePicker.bind(recordDateTxtFld);
		recordDateTxtFld.add(recordDatePicker);

		ArkDatePicker reviewedDatePicker = new ArkDatePicker();
		reviewedDatePicker.bind(reviewedDateTxtFld);
		reviewedDateTxtFld.add(reviewedDatePicker);
		
		initQuestionnaireDdc();
		initStatusDdc();
		initReviewedByDdc();
		
		dataEntryWMC = new WebMarkupContainer("dataEntryWMC");
		dataEntryWMC.setOutputMarkupId(true);
		initialisePhenotypicCollectionDataEntry();

		attachValidators();
		addComponents();

		// Focus on Questionnaire
		questionnaireDdc.add(new ArkDefaultFormFocusBehavior());
	}

	private void initQuestionnaireDdc() {
		// Get a list of questionnaires for the subject in context by default
		CustomFieldGroup cfgForStudyCriteria = cpModel.getObject().getCustomFieldGroup();
		// NB: Assumes that CustomFieldGroup will always be used for criteria (not a true entity)
		cfgForStudyCriteria.setArkFunction(cpModel.getObject().getArkFunction());
		cfgForStudyCriteria.setPublished(true);	//make sure that we don't return non-published Questionnaires
		
		List<CustomFieldGroup> questionnaireList = iArkCommonService.getCustomFieldGroups(cfgForStudyCriteria, 0, Integer.MAX_VALUE);
		ChoiceRenderer<CustomFieldGroup> choiceRenderer = new ChoiceRenderer<CustomFieldGroup>(Constants.PHENO_COLLECTION_NAME, Constants.PHENO_COLLECTION_ID);
		questionnaireDdc = new DropDownChoice<CustomFieldGroup>("phenotypicCollection.questionnaire", (List<CustomFieldGroup>) questionnaireList, choiceRenderer);
		questionnaireDdc.setNullValid(false);
		if (!isNew()) {
			questionnaireDdc.setEnabled(false);	//can't change questionnaire after crating the phenoCollection
		}
	}

	private void initStatusDdc() {
		// Get a list of status
		List<QuestionnaireStatus> statusList = iPhenotypicService.getPhenotypicCollectionStatusList();
		ChoiceRenderer<QuestionnaireStatus> choiceRenderer = new ChoiceRenderer<QuestionnaireStatus>("name", "id");
		statusDdc = new DropDownChoice<QuestionnaireStatus>("phenotypicCollection.status", statusList, choiceRenderer);
		statusDdc.setNullValid(false);
	}

	private void initReviewedByDdc() {
		List<ArkUser> userList = new ArrayList<ArkUser>(0);
		ChoiceRenderer<ArkUser> choiceRenderer = new ChoiceRenderer<ArkUser>("name", "id");
		reviewedByDdc = new DropDownChoice<ArkUser>("phenotypicCollection.reviewedBy", userList, choiceRenderer);
		reviewedByDdc.setEnabled(false);	//TODO: temporarily disabled reviewedBy capability for now
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
		arkCrudContainerVo.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(descriptionTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(statusDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(reviewedByDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(reviewedDateTxtFld);

		dataEntryWMC.add(phenoCollectionDataEntryPanel);
		dataEntryWMC.add(dataEntryNavigator);
		arkCrudContainerVo.getDetailPanelFormContainer().add(dataEntryWMC);

		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		if (cpModel.getObject().getPhenotypicCollection().getId() == null) {
			// Save

			// Inital transaction detail
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
			iPhenotypicService.createPhenotypicCollection(cpModel.getObject().getPhenotypicCollection());
			this.info("Phenotypic Collection " + cpModel.getObject().getPhenotypicCollection().getId() + " was created successfully");
			processErrors(target);

		}
		else {
			// Update
			iPhenotypicService.updatePhenotypicCollection(cpModel.getObject().getPhenotypicCollection());
			this.info("Phenotypic Collection " + cpModel.getObject().getPhenotypicCollection().getId() + " was updated successfully");
			processErrors(target);
			
		}
		// Allow the PheotyocCollection data to be saved any time save is performed
		if (phenoCollectionDataEntryPanel instanceof PhenoDataDataViewPanel) {
			((PhenoDataDataViewPanel) phenoCollectionDataEntryPanel).saveCustomData();
		}
		// refresh the CF data entry panel (if necessary)
		if (initialisePhenotypicCollectionDataEntry() == true) {
			dataEntryWMC.addOrReplace(phenoCollectionDataEntryPanel);
			dataEntryWMC.addOrReplace(dataEntryNavigator);
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
		iPhenotypicService.deletePhenotypicCollection(cpModel.getObject().getPhenotypicCollection());
		this.info("Phenotypic collection " + cpModel.getObject().getPhenotypicCollection().getId() + " was deleted successfully");
		processErrors(target);

		onClose(target);
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
		if (cpModel.getObject().getPhenotypicCollection().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}