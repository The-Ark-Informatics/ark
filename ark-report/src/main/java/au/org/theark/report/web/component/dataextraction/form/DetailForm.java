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
package au.org.theark.report.web.component.dataextraction.form;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.shiro.*;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractDetailForm;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<SearchVO> {

	private static final long	serialVersionUID	= -8267651986631341353L;
	public static final int	PALETTE_ROWS		= 5;
	private TextField<String>	searchIdTxtFld;
	private TextField<String>	searchNameTxtFld;
	private FeedbackPanel		feedBackPanel;

	private Palette<DemographicField>	demographicFieldsToReturnPalette;
	private Palette<BiospecimenField>	biospecimenFieldsToReturnPalette;
	private Palette<BiocollectionField>	biocollectionFieldsToReturnPalette;
	private Palette<CustomFieldDisplay>	phenoCustomFieldDisplaysToReturnPalette;
	private Palette<CustomFieldDisplay>	subjectCustomFieldDisplaysToReturnPalette;
	private Palette<CustomFieldDisplay>	biospecimenCustomFieldDisplaysToReturnPalette;
	private Palette<CustomFieldDisplay>	biocollectionCustomFieldDisplaysToReturnPalette;
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		//super()
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		SearchVO searchVO = containerForm.getModelObject();
		Search search = searchVO.getSearch();
//  studyComponent = containerForm.getModelObject();
		//StudyComp component = studyComponent.getStudyComponent();
		//;
		if (search != null && search.getId() != null ) {
			deleteButton.setEnabled(false);
		}
		// If the given component is attached to a file/consents then disable the delete button

	}

	public void initialiseDetailForm() {

		searchIdTxtFld = new TextField<String>(Constants.SEARCH_ID);
		searchIdTxtFld.setEnabled(false);
		searchNameTxtFld = new TextField<String>(Constants.SEARCH_NAME);
		searchNameTxtFld.add(new ArkDefaultFormFocusBehavior());

		initDemographicFieldsModulePalette();
		initBiospecimenFieldsModulePalette();
		initBiocollectionFieldsModulePalette();
		initPhenoCustomFieldDisplaysModulePalette();
		initSubjectCustomFieldDisplaysModulePalette();
		initBiospecimenCustomFieldDisplaysModulePalette();
		initBiocollectionCustomFieldDisplaysModulePalette();
		
		addDetailFormComponents();
		attachValidators();
	}
																																																																																																																									
	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(searchIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(searchNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(demographicFieldsToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biocollectionFieldsToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenFieldsToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phenoCustomFieldDisplaysToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectCustomFieldDisplaysToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenCustomFieldDisplaysToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biocollectionCustomFieldDisplaysToReturnPalette																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		searchNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.search.name.required", searchNameTxtFld, new Model<String>("Search Name")));
		searchNameTxtFld.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel("error.search.name.length", searchNameTxtFld, new Model<String>("Search Name")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {

		SearchVO searchVO = new SearchVO();
		containerForm.setModelObject(searchVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<SearchVO> containerForm, AjaxRequestTarget target) {

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		
		try {

			containerForm.getModelObject().getSearch().setStudy(study);

			if (containerForm.getModelObject().getSearch().getId() == null) {

				iArkCommonService.create(containerForm.getModelObject());
				this.info("Search " + containerForm.getModelObject().getSearch().getName() +
						" was created successfully");
				processErrors(target);

			}
			else {

				iArkCommonService.update(containerForm.getModelObject());
				this.info("Search " + containerForm.getModelObject().getSearch().getName() + " was updated successfully");
				processErrors(target);

			}

			onSavePostProcess(target);

		}
		catch (EntityExistsException e) {
			this.error("A Study Component with the same name already exists for this study.");
			processErrors(target);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		/*
		try {
	//		iStudyService.delete(containerForm.getModelObject().getStudyComponent());
			SearchVO studyCompVo = new SearchVO();
			containerForm.setModelObject(studyCompVo);
			containerForm.info("The Study Component was deleted successfully.");
			editCancelProcess(target);
		}
		catch (UnAuthorizedOperation unAuthorisedexception) {
			containerForm.error("You are not authorised to delete this study component.");
			processErrors(target);
		}
		catch (EntityCannotBeRemoved cannotRemoveException) {
			containerForm.error("Cannot Delete this Study Component. This component is associated with a Subject");
			processErrors(target);
		}
		catch (ArkSystemException e) {
			containerForm.error("A System Error has occured please contact support.");
			processErrors(target);
		}*/
		
	}

	@Override
	protected boolean isNew() {
		return (containerForm.getModelObject().getSearch().getId()==null);
	}
	
	@SuppressWarnings("unchecked")
	private void initDemographicFieldsModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<DemographicField>> selectedDemographicFieldsPm = new PropertyModel<Collection<DemographicField>>(searchCPM, "selectedDemographicFields");//"selectedDemographicFields");

		Collection<DemographicField> availableDemographicFields = iArkCommonService.getAllDemographicFields();
		containerForm.getModelObject().setAvailableDemographicFields(availableDemographicFields);
		
		PropertyModel<Collection<DemographicField>> availableDemographicFieldsPm = new PropertyModel<Collection<DemographicField>>(searchCPM, "availableDemographicFields");
		demographicFieldsToReturnPalette = new ArkPalette("selectedDemographicFields", selectedDemographicFieldsPm, availableDemographicFieldsPm, renderer, PALETTE_ROWS, false);
		demographicFieldsToReturnPalette.setOutputMarkupId(true);
	}

	/*
	@SuppressWarnings("unchecked")
	private void initBiocollectionFieldsModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<BiocollectionField>> selectedBiocollectionFieldsPm = new PropertyModel<Collection<BiocollectionField>>(searchCPM, "selectedBiocollectionFields");//"selectedBiocollectionFields");

		Collection<BiocollectionField> availableBiocollectionFields = iArkCommonService.getAllBiocollectionFields();
		containerForm.getModelObject().setAvailableBiocollectionFields(availableBiocollectionFields);
		
		PropertyModel<Collection<BiocollectionField>> availableBiocollectionFieldsPm = new PropertyModel<Collection<BiocollectionField>>(searchCPM, "availableBiocollectionFields");
		biocollectionFieldsToReturnPalette = new ArkPalette("selectedBiocollectionFields", selectedBiocollectionFieldsPm, availableBiocollectionFieldsPm, renderer, PALETTE_ROWS, false);
		biocollectionFieldsToReturnPalette.setOutputMarkupId(true);
	}

	
	@SuppressWarnings("unchecked")
	private void initBiospecimenFieldsModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<BiospecimenField>> selectedBiospecimenFieldsPm = new PropertyModel<Collection<BiospecimenField>>(searchCPM, "selectedBiospecimenFields");//"selectedBiospecimenFields");

		Collection<BiospecimenField> availableBiospecimenFields = iArkCommonService.getAllBiospecimenFields();
		containerForm.getModelObject().setAvailableBiospecimenFields(availableBiospecimenFields);
		
		PropertyModel<Collection<BiospecimenField>> availableBiospecimenFieldsPm = new PropertyModel<Collection<BiospecimenField>>(searchCPM, "availableBiospecimenFields");
		biospecimenFieldsToReturnPalette = new ArkPalette("selectedBiospecimenFields", selectedBiospecimenFieldsPm, availableBiospecimenFieldsPm, renderer, PALETTE_ROWS, false);
		biospecimenFieldsToReturnPalette.setOutputMarkupId(true);
	}
	*/
	@SuppressWarnings("unchecked")
	private void initBiocollectionFieldsModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<BiocollectionField>> selectedBiocollectionFieldsPm = new PropertyModel<Collection<BiocollectionField>>(searchCPM, "selectedBiocollectionFields");//"selectedBiocollectionFields");

		Collection<BiocollectionField> availableBiocollectionFields = iArkCommonService.getAllBiocollectionFields();
		containerForm.getModelObject().setAvailableBiocollectionFields(availableBiocollectionFields);
		
		PropertyModel<Collection<BiocollectionField>> availableBiocollectionFieldsPm = new PropertyModel<Collection<BiocollectionField>>(searchCPM, "availableBiocollectionFields");
		biocollectionFieldsToReturnPalette = new ArkPalette("selectedBiocollectionFields", selectedBiocollectionFieldsPm, availableBiocollectionFieldsPm, renderer, PALETTE_ROWS, false);
		biocollectionFieldsToReturnPalette.setOutputMarkupId(true);
	}
	
	@SuppressWarnings("unchecked")
	private void initBiospecimenFieldsModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<BiospecimenField>> selectedBiospecimenFieldsPm = new PropertyModel<Collection<BiospecimenField>>(searchCPM, "selectedBiospecimenFields");//"selectedBiospecimenFields");

		Collection<BiospecimenField> availableBiospecimenFields = iArkCommonService.getAllBiospecimenFields();
		containerForm.getModelObject().setAvailableBiospecimenFields(availableBiospecimenFields);
		
		PropertyModel<Collection<BiospecimenField>> availableBiospecimenFieldsPm = new PropertyModel<Collection<BiospecimenField>>(searchCPM, "availableBiospecimenFields");
		biospecimenFieldsToReturnPalette = new ArkPalette("selectedBiospecimenFields", selectedBiospecimenFieldsPm, availableBiospecimenFieldsPm, renderer, PALETTE_ROWS, false);
		biospecimenFieldsToReturnPalette.setOutputMarkupId(true);
	}
	
	@SuppressWarnings("unchecked")
	private void initPhenoCustomFieldDisplaysModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
	//	IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("customField.name", "id");
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("descriptiveNameIncludingCFGName", "id");
		
		PropertyModel<Collection<CustomFieldDisplay>> selectedPhenoCustomFieldDisplaysPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "selectedPhenoCustomFieldDisplays");//"selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);   //	Long arkFunctionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		
		Collection<CustomFieldDisplay> availablePhenoCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysIn(study, arkFunction);
		containerForm.getModelObject().setAvailablePhenoCustomFieldDisplays(availablePhenoCustomFieldDisplays);
		
		PropertyModel<Collection<CustomFieldDisplay>> availablePhenoCustomFieldDisplayPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "availablePhenoCustomFieldDisplays");
		phenoCustomFieldDisplaysToReturnPalette = new ArkPalette("selectedPhenoCustomFieldDisplays", selectedPhenoCustomFieldDisplaysPm, availablePhenoCustomFieldDisplayPm, renderer, PALETTE_ROWS, false);
		phenoCustomFieldDisplaysToReturnPalette.setOutputMarkupId(true);

	}

	
	@SuppressWarnings("unchecked")
	private void initSubjectCustomFieldDisplaysModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
	//	IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("customField.name", "id");
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("customField.name", "id");
		
		PropertyModel<Collection<CustomFieldDisplay>> selectedSubjectCustomFieldDisplaysPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "selectedSubjectCustomFieldDisplays");//"selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);   //	Long arkFunctionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
		
		Collection<CustomFieldDisplay> availableSubjectCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysIn(study, arkFunction);
		containerForm.getModelObject().setAvailableSubjectCustomFieldDisplays(availableSubjectCustomFieldDisplays);
		
		PropertyModel<Collection<CustomFieldDisplay>> availableSubjectCustomFieldDisplayPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "availableSubjectCustomFieldDisplays");
		subjectCustomFieldDisplaysToReturnPalette = new ArkPalette("selectedSubjectCustomFieldDisplays", selectedSubjectCustomFieldDisplaysPm, availableSubjectCustomFieldDisplayPm, renderer, PALETTE_ROWS, false);
		subjectCustomFieldDisplaysToReturnPalette.setOutputMarkupId(true);

	}

	
	@SuppressWarnings("unchecked")
	private void initBiospecimenCustomFieldDisplaysModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("customField.name", "id");
		
		PropertyModel<Collection<CustomFieldDisplay>> selectedBiospecimenCustomFieldDisplaysPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "selectedBiospecimenCustomFieldDisplays");//"selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);   //	Long arkFunctionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
		
		Collection<CustomFieldDisplay> availableBiospecimenCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysIn(study, arkFunction);
		containerForm.getModelObject().setAvailableBiospecimenCustomFieldDisplays(availableBiospecimenCustomFieldDisplays);
		
		PropertyModel<Collection<CustomFieldDisplay>> availableBiospecimenCustomFieldDisplayPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "availableBiospecimenCustomFieldDisplays");
		biospecimenCustomFieldDisplaysToReturnPalette = new ArkPalette("selectedBiospecimenCustomFieldDisplays", selectedBiospecimenCustomFieldDisplaysPm, availableBiospecimenCustomFieldDisplayPm, renderer, PALETTE_ROWS, false);
		biospecimenCustomFieldDisplaysToReturnPalette.setOutputMarkupId(true);

	}

	
	@SuppressWarnings("unchecked")
	private void initBiocollectionCustomFieldDisplaysModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("customField.name", "id");
	//	IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("descriptiveNameIncludingCFGName", "id");
		
		PropertyModel<Collection<CustomFieldDisplay>> selectedBiocollectionCustomFieldDisplaysPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "selectedBiocollectionCustomFieldDisplays");//"selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);   //	Long arkFunctionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
		
		Collection<CustomFieldDisplay> availableBiocollectionCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysIn(study, arkFunction);
		containerForm.getModelObject().setAvailableBiocollectionCustomFieldDisplays(availableBiocollectionCustomFieldDisplays);
		
		PropertyModel<Collection<CustomFieldDisplay>> availableBiocollectionCustomFieldDisplayPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "availableBiocollectionCustomFieldDisplays");
		biocollectionCustomFieldDisplaysToReturnPalette = new ArkPalette("selectedBiocollectionCustomFieldDisplays", selectedBiocollectionCustomFieldDisplaysPm, availableBiocollectionCustomFieldDisplayPm, renderer, PALETTE_ROWS, false);
		biocollectionCustomFieldDisplaysToReturnPalette.setOutputMarkupId(true);

	}

	

}
