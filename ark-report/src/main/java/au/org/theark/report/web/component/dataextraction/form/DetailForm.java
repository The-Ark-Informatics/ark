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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkCheckSumNotSameException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.ConsentStatusField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.report.entity.SearchFile;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.util.AJAXDownload;
import au.org.theark.core.util.ArkFileExtensionValidator;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.QueryFilterListVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.report.service.IReportService;
import au.org.theark.report.web.component.dataextraction.filter.QueryFilterPanel;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<SearchVO> {

	private static Logger log = LoggerFactory.getLogger(DetailForm.class);

	private static final long serialVersionUID = -8267651986631341353L;
	public static final int PALETTE_ROWS = 5;
	private TextField<String> searchIdTxtFld;
	private TextField<String> searchNameTxtFld;
	private CheckBox includeGenoChkBox;

	private FeedbackPanel feedBackPanel;
	private Panel modalContentPanel;
	protected AbstractDetailModalWindow modalWindow;

	private Palette<DemographicField> demographicFieldsToReturnPalette;
	private Palette<BiospecimenField> biospecimenFieldsToReturnPalette;
	private Palette<BiocollectionField> biocollectionFieldsToReturnPalette;
	private Palette<PhenoDataSetFieldDisplay> phenoDatasetFieldDisplaysToReturnPalette;
	private Palette<CustomFieldDisplay> subjectCustomFieldDisplaysToReturnPalette;
	private Palette<CustomFieldDisplay> biospecimenCustomFieldDisplaysToReturnPalette;
	private Palette<CustomFieldDisplay> biocollectionCustomFieldDisplaysToReturnPalette;

	private Palette<ConsentStatusField> consentStatusFieldsToReturnPalette;

	private FileUploadField fileUploadField;
	private Label			fileNameLbl;
	private ArkBusyAjaxLink<String>  				fileNameLnk;
	private AJAXDownload                            ajaxDownload;
	private AjaxButton clearButton;

	@SpringBean(name = Constants.ARK_PHENO_DATA_SERVICE)
	private IPhenotypicService iPhenoService;
	
	@SpringBean(name = au.org.theark.report.service.Constants.REPORT_SERVICE)
	private IReportService							reportService;
	
	

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 * @param modalWindow
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm, AbstractDetailModalWindow modalWindow) {
		// super()
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
		this.modalWindow = modalWindow;

		fileUploadField = new FileUploadField("subjectFileUpload");
		
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		SearchVO searchVO = containerForm.getModelObject();
		Search search = searchVO.getSearch();
		// studyComponent = containerForm.getModelObject();
		// StudyComp component = studyComponent.getStudyComponent();
		// ;
		if (search != null && search.getId() != null) {
			// deleteButton.setEnabled(false);
		}
		// If the given component is attached to a file/consents then disable
		// the delete button

	}

	private void initIncludeGeno() {
		includeGenoChkBox = new CheckBox(Constants.SEARCH_INCLUDE_GENO);
		includeGenoChkBox.setVisible(false);
	}

	public void initialiseDetailForm() {

		arkCrudContainerVO.getDetailPanelFormContainer().add(modalWindow);
		searchIdTxtFld = new TextField<String>(Constants.SEARCH_ID);
		searchIdTxtFld.setEnabled(false);
		searchNameTxtFld = new TextField<String>(Constants.SEARCH_NAME);
		searchNameTxtFld.add(new ArkDefaultFormFocusBehavior());

		modalContentPanel = new EmptyPanel("content");
		initIncludeGeno();
		initDemographicFieldsModulePalette();
		initBiospecimenFieldsModulePalette();
		initBiocollectionFieldsModulePalette();
		initPhenoDataSetFieldDisplaysModulePalette();
		initSubjectCustomFieldDisplaysModulePalette();
		initBiospecimenCustomFieldDisplaysModulePalette();
		initBiocollectionCustomFieldDisplaysModulePalette();
		initConsentStatusFieldsModulePalette();
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		
		fileNameLbl = new Label("searchFile.filename");
		fileNameLbl.setOutputMarkupId(true);
		ajaxDownload = new AJAXDownload()
		{
			 @Override
	            protected IResourceStream getResourceStream()
	            {     
				 	SearchFile searchFile=containerForm.getModelObject().getSearchFile();
					Long studyId =searchFile.getStudy().getId();
					Long searchId=searchFile.getSearch().getId();
					String fileId = searchFile.getFileId();
					String checksum = searchFile.getChecksum();
					File file = null;
					IResourceStream resStream =null;
						try {
							file=iArkCommonService.retriveArkFileAttachmentAsFile(studyId,searchId.toString(),au.org.theark.report.web.Constants.REPORT_DATA_EXTRACTION_SUBJECT_UID_RESTRICT_FILE,fileId,checksum);
							resStream = new FileResourceStream(file);
							if(resStream==null){
								containerForm.error("An unexpected error occurred. Download request could not be fulfilled.");
							}
						} catch (ArkSystemException e) {
							containerForm.error("An unexpected error occurred. Download request could not be fulfilled.");
							log.error(e.getMessage());
						} catch (ArkFileNotFoundException e) {
							containerForm.error("File not found:"+e.getMessage());
							log.error(e.getMessage());
						} catch (ArkCheckSumNotSameException e) {
							containerForm.error("Check sum error:"+e.getMessage());
							log.error(e.getMessage());
						}
						return resStream;
		        }
	            @Override
	            protected String getFileName() {
	                return containerForm.getModelObject().getSearchFile().getFilename();
	            }
		};
		fileNameLnk=new ArkBusyAjaxLink<String>(au.org.theark.report.web.Constants.SUBJECT_FILE_FILENAMELINK) {
		@Override
		public void onClick(AjaxRequestTarget target) {
			ajaxDownload.initiate(target);
			processErrors(target);
		}
			
		};
		fileNameLnk.add(fileNameLbl);
		clearButton = new AjaxButton("clearButton") {			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}
		};
		clearButton.add(new AttributeModifier("title", new Model<String>("Clear Attachment")));
		deleteButton = new AjaxButton("deleteButton") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			try {
				reportService.delete(containerForm.getModelObject().getSearchFile(),au.org.theark.report.web.Constants.REPORT_DATA_EXTRACTION_SUBJECT_UID_RESTRICT_FILE);
				containerForm.info("The file has been successfully deleted.");
				containerForm.getModelObject().getSearchFile().setFilename(null);
			 }catch (EntityNotFoundException e) {
					containerForm.error("The subject consent attachment no longer exists in the system.Please re-do the operation.");
			 }catch (ArkSystemException e) {
					containerForm.error("System error occure:"+e.getMessage());
			 } catch (ArkFileNotFoundException e) {
				 containerForm.error("File not found:"+e.getMessage());
			}finally{
				 //containerForm.getModelObject().getSubjectFile().setFilename(null);
				this.setVisible(false);
				target.add(fileNameLnk);
				target.add(this);
				processErrors(target);
			 }
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getSearchFile().setPayload(null);
				containerForm.getModelObject().getSearchFile().setFilename(null);
				this.setVisible(false);
				target.add(fileNameLnk);
				target.add(this);
				containerForm.error("Error occurred during the file deletion process.");
				processErrors(target);
			}
			
			@Override
			public boolean isVisible() {
				return (containerForm.getModelObject().getSearchFile()!=null && containerForm.getModelObject().getSearchFile().getFilename() != null) && !containerForm.getModelObject().getSearchFile().getFilename().isEmpty();
			}
		};
		deleteButton.add(new AttributeModifier("title", new Model<String>("Delete Attachment")));
		deleteButton.setOutputMarkupId(true);
		addDetailFormComponents();
		attachValidators();
	}

	private void onCreateFilters(AjaxRequestTarget target, SearchVO searchVO) {
		IModel model = new Model<QueryFilterListVO>(new QueryFilterListVO(searchVO));

		// handles for auto-gen biospecimenUid or manual entry
		modalContentPanel = new QueryFilterPanel("content", feedBackPanel, model, modalWindow,arkCrudContainerVO);

		// Set the modalWindow title and content
		modalWindow.setTitle("Create Basic Filters");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
		// refresh the feedback messages
		target.add(feedBackPanel);
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(new AjaxButton("createFilters") {

			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, org.apache.wicket.markup.html.form.Form<?> form) {
				onCreateFilters(target, containerForm.getModelObject());
				target.add(feedBackPanel);
				target.add(this);
				
			};

			protected void onError(AjaxRequestTarget target, org.apache.wicket.markup.html.form.Form<?> form) {
				target.add(feedBackPanel);
			};

			public boolean isVisible() {
				return true;
			};

			public boolean isEnabled() {
				return containerForm.getModelObject().getSearch().getId() != null;
			};

		}.setDefaultFormProcessing(false).add(new AttributeModifier("value", new Model<String>("Create Filters"))));

		/*
		 * item.add(new AttributeModifier(Constants.CLASS, new
		 * AbstractReadOnlyModel() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public String getObject() { return (item.getIndex() % 2 ==
		 * 1) ? Constants.EVEN : Constants.ODD; } }));
		 */
		arkCrudContainerVO.getDetailPanelFormContainer().add(searchIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(searchNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(includeGenoChkBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(demographicFieldsToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biocollectionFieldsToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenFieldsToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phenoDatasetFieldDisplaysToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectCustomFieldDisplaysToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenCustomFieldDisplaysToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biocollectionCustomFieldDisplaysToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(consentStatusFieldsToReturnPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(clearButton);
		//arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(deleteButton);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLnk);
		arkCrudContainerVO.getDetailPanelFormContainer().add(ajaxDownload);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		searchNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.search.name.required", searchNameTxtFld, new Model<String>("Search Name")));
		searchNameTxtFld.add(StringValidator.lengthBetween(1, 255)).setLabel(new StringResourceModel("error.search.name.length", searchNameTxtFld, new Model<String>("Search Name")));
		fileUploadField.add(new ArkFileExtensionValidator(".csv","valid.file.extension"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket
	 * .ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {

		SearchVO searchVO = new SearchVO();
		containerForm.setModelObject(searchVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket
	 * .markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<SearchVO> containerForm, AjaxRequestTarget target) {

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		try {

			containerForm.getModelObject().getSearch().setStudy(study);
			containerForm.getModelObject().getSearch().setStatus("READY TO RUN");
			containerForm.getModelObject().getSearch().setFinishTime(null);
			//Add to fix bug  ARK-1581 
			containerForm.getModelObject().getSearch().setIncludeGeno(false);

			FileUpload subjectFileUpload = fileUploadField.getFileUpload();
			List<SubjectVO> selectedSubjects = iArkCommonService.matchSubjectsFromInputFile(subjectFileUpload, study);

			if (containerForm.getModelObject().getSearch().getId() == null) {
				iArkCommonService.create(containerForm.getModelObject());
				iArkCommonService.createSearchSubjects(containerForm.getModelObject().getSearch(), selectedSubjects);
				createSearchFile();
				this.saveInformation();
				//this.info("Search " + containerForm.getModelObject().getSearch().getName() + " was created successfully");
				processErrors(target);

			} else {
				iArkCommonService.update(containerForm.getModelObject());
				iArkCommonService.createSearchSubjects(containerForm.getModelObject().getSearch(), selectedSubjects);
				createSearchFile();
				this.updateInformation();
				//this.info("Search " + containerForm.getModelObject().getSearch().getName() + " was updated successfully");
				processErrors(target);

			}

			onSavePostProcess(target);

		} catch (EntityExistsException e) {
			this.error("A search with the same name already exists for this study.");
			processErrors(target);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache
	 * .wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		iArkCommonService.delete(containerForm.getModelObject().getSearch());
		this.deleteInformation();
		processErrors(target);
		editCancelProcess(target);
	}

	@Override
	protected boolean isNew() {
		return (containerForm.getModelObject().getSearch().getId() == null);
	}

	@SuppressWarnings("unchecked")
	private void initDemographicFieldsModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<DemographicField>> selectedDemographicFieldsPm = new PropertyModel<Collection<DemographicField>>(searchCPM, "selectedDemographicFields");// "selectedDemographicFields");

		Collection<DemographicField> availableDemographicFields = iArkCommonService.getAllDemographicFields();
		containerForm.getModelObject().setAvailableDemographicFields(availableDemographicFields);

		PropertyModel<Collection<DemographicField>> availableDemographicFieldsPm = new PropertyModel<Collection<DemographicField>>(searchCPM, "availableDemographicFields");
		demographicFieldsToReturnPalette = new ArkPalette("selectedDemographicFields", selectedDemographicFieldsPm, availableDemographicFieldsPm, renderer, PALETTE_ROWS, false);
		demographicFieldsToReturnPalette.setOutputMarkupId(true);
	}

	@SuppressWarnings("unchecked")
	private void initBiocollectionFieldsModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<BiocollectionField>> selectedBiocollectionFieldsPm = new PropertyModel<Collection<BiocollectionField>>(searchCPM, "selectedBiocollectionFields");// "selectedBiocollectionFields");

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
		PropertyModel<Collection<BiospecimenField>> selectedBiospecimenFieldsPm = new PropertyModel<Collection<BiospecimenField>>(searchCPM, "selectedBiospecimenFields");// "selectedBiospecimenFields");

		Collection<BiospecimenField> availableBiospecimenFields = iArkCommonService.getAllBiospecimenFields();
		containerForm.getModelObject().setAvailableBiospecimenFields(availableBiospecimenFields);

		PropertyModel<Collection<BiospecimenField>> availableBiospecimenFieldsPm = new PropertyModel<Collection<BiospecimenField>>(searchCPM, "availableBiospecimenFields");
		biospecimenFieldsToReturnPalette = new ArkPalette("selectedBiospecimenFields", selectedBiospecimenFieldsPm, availableBiospecimenFieldsPm, renderer, PALETTE_ROWS, false);
		biospecimenFieldsToReturnPalette.setOutputMarkupId(true);
	}

	@SuppressWarnings("unchecked")
	private void initPhenoDataSetFieldDisplaysModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("descriptiveNameIncludingCFGName", "id");

		PropertyModel<Collection<PhenoDataSetFieldDisplay>> selectedPhenoDataSetFieldDisplaysPm = new PropertyModel<Collection<PhenoDataSetFieldDisplay>>(searchCPM, "selectedPhenoDataSetFieldDisplays");// "selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId); // Long arkFunctionId
															// = (Long)
															// SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);

		Collection<PhenoDataSetFieldDisplay> availablePhenoDataSetFieldDisplays = iPhenoService.getPhenoFieldDisplaysIn(study, arkFunction); //iArkCommonService.getCustomFieldDisplaysIn(study, arkFunction);
		containerForm.getModelObject().setAvailablePhenoDataSetFieldDisplays(availablePhenoDataSetFieldDisplays);

		PropertyModel<Collection<PhenoDataSetFieldDisplay>> availablePhenoDataSetFieldDisplaysPm = new PropertyModel<Collection<PhenoDataSetFieldDisplay>>(searchCPM, "availablePhenoDataSetFieldDisplays");
		phenoDatasetFieldDisplaysToReturnPalette = new ArkPalette("selectedPhenoDataSetFieldDisplays", selectedPhenoDataSetFieldDisplaysPm, availablePhenoDataSetFieldDisplaysPm, renderer, PALETTE_ROWS, false);
		phenoDatasetFieldDisplaysToReturnPalette.setOutputMarkupId(true);
	}

	@SuppressWarnings("unchecked")
	private void initSubjectCustomFieldDisplaysModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("customField.name", "id");

		PropertyModel<Collection<CustomFieldDisplay>> selectedSubjectCustomFieldDisplaysPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "selectedSubjectCustomFieldDisplays");// "selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId); // Long arkFunctionId
															// = (Long)
															// SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
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

		PropertyModel<Collection<CustomFieldDisplay>> selectedBiospecimenCustomFieldDisplaysPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "selectedBiospecimenCustomFieldDisplays");// "selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId); 
		
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
		
		CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.BIOSPECIMEN);

		Collection<CustomFieldDisplay> availableBiospecimenCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysInLIMS(study, arkFunction,customFieldType);
		containerForm.getModelObject().setAvailableBiospecimenCustomFieldDisplays(availableBiospecimenCustomFieldDisplays);

		PropertyModel<Collection<CustomFieldDisplay>> availableBiospecimenCustomFieldDisplayPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "availableBiospecimenCustomFieldDisplays");
		biospecimenCustomFieldDisplaysToReturnPalette = new ArkPalette("selectedBiospecimenCustomFieldDisplays", selectedBiospecimenCustomFieldDisplaysPm, availableBiospecimenCustomFieldDisplayPm, renderer, PALETTE_ROWS, false);
		biospecimenCustomFieldDisplaysToReturnPalette.setOutputMarkupId(true);

	}

	@SuppressWarnings("unchecked")
	private void initBiocollectionCustomFieldDisplaysModulePalette() {
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("customField.name", "id");

		PropertyModel<Collection<CustomFieldDisplay>> selectedBiocollectionCustomFieldDisplaysPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "selectedBiocollectionCustomFieldDisplays");// "selectedDemographicFields");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
		
		CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.BIOCOLLECTION);

		Collection<CustomFieldDisplay> availableBiocollectionCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysInLIMS(study, arkFunction,customFieldType);
		containerForm.getModelObject().setAvailableBiocollectionCustomFieldDisplays(availableBiocollectionCustomFieldDisplays);

		PropertyModel<Collection<CustomFieldDisplay>> availableBiocollectionCustomFieldDisplayPm = new PropertyModel<Collection<CustomFieldDisplay>>(searchCPM, "availableBiocollectionCustomFieldDisplays");
		biocollectionCustomFieldDisplaysToReturnPalette = new ArkPalette("selectedBiocollectionCustomFieldDisplays", selectedBiocollectionCustomFieldDisplaysPm, availableBiocollectionCustomFieldDisplayPm, renderer, PALETTE_ROWS, false);
		biocollectionCustomFieldDisplaysToReturnPalette.setOutputMarkupId(true);

	}

	@SuppressWarnings("unchecked")
	private void initConsentStatusFieldsModulePalette() {
		log.info("INITCONSENTSTATUSFIELDSMODULEPALETTE CALLED ##########################");
		CompoundPropertyModel<SearchVO> searchCPM = (CompoundPropertyModel<SearchVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("publicFieldName", "id");
		PropertyModel<Collection<ConsentStatusField>> selectedConsentStatusFieldsPm = new PropertyModel<Collection<ConsentStatusField>>(searchCPM, "selectedConsentStatusFields");

		Collection<ConsentStatusField> availableConsentStatusFields = iArkCommonService.getAllConsentStatusFields();
		containerForm.getModelObject().setAvailableConsentStatusFields(availableConsentStatusFields);

		PropertyModel<Collection<ConsentStatusField>> availableConsentStatusFieldsPm = new PropertyModel<Collection<ConsentStatusField>>(searchCPM, "availableConsentStatusFields");
		consentStatusFieldsToReturnPalette = new ArkPalette("selectedConsentStatusFields", selectedConsentStatusFieldsPm, availableConsentStatusFieldsPm, renderer, PALETTE_ROWS, false);
		consentStatusFieldsToReturnPalette.setOutputMarkupId(true);
	}
	
	private void createSearchFile(){
	
		FileUpload fileSubjectFile = fileUploadField.getFileUpload();
		if(fileSubjectFile != null) {
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			try {
				if(containerForm.getModelObject().getSearchFile()==null){
					containerForm.getModelObject().setSearchFile(new SearchFile());
				}
				// store search file attachment
				if (fileSubjectFile != null) {
					byte[] byteArray = fileSubjectFile.getMD5();
					String checksum = getHex(byteArray);
					containerForm.getModelObject().getSearchFile().setStudy(study);
					containerForm.getModelObject().getSearchFile().setSearch(containerForm.getModelObject().getSearch());
					containerForm.getModelObject().getSearchFile().setPayload(IOUtils.toByteArray(fileSubjectFile.getInputStream()));
					containerForm.getModelObject().getSearchFile().setFilename(fileSubjectFile.getClientFileName());
					containerForm.getModelObject().getSearchFile().setChecksum(checksum);
					containerForm.getModelObject().getSearchFile().setFileId(iArkCommonService.generateArkFileId(fileSubjectFile.getClientFileName()));
					containerForm.getModelObject().getSearchFile().setUserId(SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal().toString());
				}
				// Save
				reportService.create(containerForm.getModelObject().getSearchFile(),au.org.theark.report.web.Constants.REPORT_DATA_EXTRACTION_SUBJECT_UID_RESTRICT_FILE);
				
			}
			catch (IOException ioe) {
				log.error("Failed to save the uploaded file: " + ioe);
			}
			catch (ArkSystemException e) {
				log.error("Failed to save the uploaded file: " + e);
			}
		}
		
	}

}