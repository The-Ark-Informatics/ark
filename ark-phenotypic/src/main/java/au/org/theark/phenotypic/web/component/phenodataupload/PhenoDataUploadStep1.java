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
package au.org.theark.phenotypic.web.component.phenodataupload;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.button.ArkDownloadTemplateButton;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodataupload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class PhenoDataUploadStep1 extends AbstractWizardStepPanel {
	private static final long				serialVersionUID		= -3267334731280446472L;
	public java.util.Collection<String>		validationMessages	= null;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;
	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE) 
	private IPhenotypicService					iPhenotypicService;
	private Form<UploadVO>						containerForm;
	private FileUploadField						fileUploadField;
	private DropDownChoice<DelimiterType>		delimiterTypeDdc;
	private WizardForm							wizardForm;
	private DropDownChoice<PhenoDataSetGroup>	questionnaireDdc;
	private TextField<String>					phenoCollectionDescription;
	private CheckBox							overwriteExistingCheckBox;
	private ArkDownloadTemplateButton 			downloadTemplateButton;
	private WebMarkupContainer 					templatePanel;
	private String[][]	PHENO_TEMPLATE_CELLS = {
		{ "", "SUBJECTUID", "RECORD_DATE_TIME", "YOUR_FIRST_DATASET_FIELD_NAME_HERE", "YOUR_SECOND_DATASET_FIELD_NAME_HERE", "AND SO ON"},
		{ "DESCRIPTION", "The unique identifier assigned for this subject.  This must match the subjectUID in the database", "The date this record was taken DD/MM/YYYY format", "Value for first dataset field","Value for second field", "AND SO ON" },
		{ "NOTE: Removed this first column, and replace row 1, (after SUBJECTUID field) with your actual dataset field names (the headers only appear once, row 2 will have your first subject and his/her values, row 2 your second subject, etc.", "", "" , "", "" , "" } 
		};
private String[] PHENO_COLUMN_NAMES;
	
private String[] PHENO_COLUMN_DESC;
	
private String[][]	PHENO_TEMPLATE_CELLS2;
	

	public PhenoDataUploadStep1(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 1/5: Select data file to upload", "Select the file containing data, the file type and the specified delimiter, click Next to continue.");

		this.containerForm = containerForm;
		this.setWizardForm(wizardForm);
		//Set the Step Out completed status
		containerForm.getModelObject().setPreviousStepOutCompleted(false);
		initialiseDetailForm();
		containerForm.getModelObject().getUpload().setUploadType(iArkCommonService.getCustomFieldDataUploadType());
	}

	@SuppressWarnings( { "unchecked" })
	private void initialiseDropDownChoices() {
		java.util.Collection<DelimiterType> delimiterTypeCollection = iArkCommonService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
		containerForm.getModelObject().getUpload().setDelimiterType(iArkCommonService.getDelimiterType(new Long(1)));
	}
	
	private void initQuestionnaireDdc() {
		Study study=null;
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null && sessionStudyId > 0) {
			study = iArkCommonService.getStudy(sessionStudyId);
		}
		else {
			log.error("\n\n\n can't have a null study and see pheno!");
		}
		// Get a list of questionnaires for the subject in context by default
		PhenoDataSetGroup pfgForStudyCriteria =new PhenoDataSetGroup();
		pfgForStudyCriteria.setStudy(study);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		pfgForStudyCriteria.setArkFunction(arkFunction);
		pfgForStudyCriteria.setPublished(true);	//make sure that we don't return non-published Questionnaires
		List<PhenoDataSetGroup> questionnaireList =iPhenotypicService.getPhenoDataSetGroups(pfgForStudyCriteria, 0, Integer.MAX_VALUE);
		ChoiceRenderer<PhenoDataSetGroup> choiceRenderer = new ChoiceRenderer<PhenoDataSetGroup>("name", "id");
		questionnaireDdc = new DropDownChoice<PhenoDataSetGroup>("phenoDataSetGroup", (List<PhenoDataSetGroup>) questionnaireList, choiceRenderer);
		questionnaireDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	List<PhenoDataSetField> phenoDataSetFields=iPhenotypicService.getAllPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(questionnaireDdc.getModelObject());
            	if(phenoDataSetFields.size()>0){
            		downloadTemplateButton.setEnabled(true);
	            	for (PhenoDataSetField phenoDataSetField : phenoDataSetFields) {
	            		PHENO_COLUMN_NAMES=(String[])ArrayUtils.add(PHENO_COLUMN_NAMES, phenoDataSetField.getName());
	            		PHENO_COLUMN_DESC=(String[])ArrayUtils.add(PHENO_COLUMN_DESC, phenoDataSetField.getFieldType().getName().toLowerCase());
	            	}
	            	PHENO_TEMPLATE_CELLS2=createPhenoTemplateCellArray(PHENO_COLUMN_NAMES, PHENO_COLUMN_DESC);
	            	PHENO_COLUMN_NAMES=null;
	            	PHENO_COLUMN_DESC=null;
	            	templatePanel.remove(downloadTemplateButton);
	            	initDownloadButton(PHENO_TEMPLATE_CELLS2,questionnaireDdc.getModelObject().getName());
	            	templatePanel.add(downloadTemplateButton);
	            	target.add(downloadTemplateButton);
	            	target.add(templatePanel);
            	}else{
            		error("The DataSet not having any fields specified.");
            		getWizardForm().onError(target,null);
            		downloadTemplateButton.setEnabled(false);
            		target.add(downloadTemplateButton);
	        	}
            }
            });
	}

	public void initialiseDetailForm() {
		fileUploadField = new FileUploadField(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);
		phenoCollectionDescription = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENOCOLLECTION_DESCRIPTION);
		initialiseDropDownChoices();
		initQuestionnaireDdc();
		initTemplatePanel();
		initDownloadButton(PHENO_TEMPLATE_CELLS,"");
		overwriteExistingCheckBox = new CheckBox("updateChkBox");
		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
		questionnaireDdc.setRequired(true).setLabel(new StringResourceModel("error.phenoDataSetGroup.required", this,  new Model<String>("CFG")));
	}

	private void addComponents() {
		add(fileUploadField);
		add(delimiterTypeDdc);
		add(questionnaireDdc);
		add(phenoCollectionDescription);
		add(overwriteExistingCheckBox);
		add(templatePanel);
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		saveFileInMemory(target);
	}

	public void setWizardForm(WizardForm wizardForm) {
		this.wizardForm = wizardForm;
	}

	public WizardForm getWizardForm() {
		return wizardForm;
	}

	private void saveFileInMemory(AjaxRequestTarget target) {
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		FileUpload fileUpload = fileUploadField.getFileUpload();
		containerForm.getModelObject().setFileUpload(fileUpload);
		Payload payload = iArkCommonService.createPayload(fileUpload.getBytes());

		containerForm.getModelObject().getUpload().setPayload(payload);
		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
		String fileFormatName = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		au.org.theark.core.model.study.entity.FileFormat fileFormat = new au.org.theark.core.model.study.entity.FileFormat();
		fileFormat = iArkCommonService.getFileFormatByName(fileFormatName);
		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
		
		containerForm.getModelObject().getUpload().setStudy(study);
		containerForm.getModelObject().getUpload().setFileFormat(fileFormat);
		containerForm.getModelObject().getUpload().setChecksum(checksum);
		containerForm.getModelObject().getUpload().setFilename(filename);
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		containerForm.getModelObject().getUpload().setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_FIELD_DATA_UPLOAD));
		wizardForm.setFileName(filename);

		containerForm.getModelObject().getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(Constants.UPLOAD_STATUS_COMPLETED));		
		//TODO analyse how many times this is saved and where it should be saved
		try {
			iArkCommonService.createUpload(containerForm.getModelObject().getUpload());
			containerForm.getModelObject().setPreviousStepOutCompleted(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			error("There is a problem during the upload process.");
			getWizardForm().onError(target, null);
			
		}
		
	}
	private void initTemplatePanel(){
		templatePanel=new WebMarkupContainer("templatePanel");
		templatePanel.setOutputMarkupId(true);
	}
	private void initDownloadButton(String [][] defaultArray,String datasetName){
		downloadTemplateButton = new ArkDownloadTemplateButton("downloadTemplate", "PhenoDataSetDataUpload-"+datasetName, defaultArray) {
			private static final long	serialVersionUID	= 1L;
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected Error: Could not proceed with download of the template.");
			}

		};
		downloadTemplateButton.setEnabled(!datasetName.isEmpty());
		templatePanel.add(downloadTemplateButton);
	}
	private String[][] createPhenoTemplateCellArray(String[] phenoColname,String[] phenoColDesc){
		if(phenoColname!=null && phenoColDesc!=null && phenoColname.length >0 && phenoColDesc.length >0){
			String[][] result = new String[3][phenoColname.length+3];
			result[0][0]=""; result[0][1]="SUBJECTUID";result[0][2]="RECORD_DATE_TIME";
			result[1][0]="DESCRIPTION"; result[1][1]="The unique identifier assigned for this subject.  This must match the subjectUID in the database";result[1][2]="The date this record was taken DD/MM/YYYY format";
			result[2][0]="NOTE: Removed this first column, and replace row 1, (after SUBJECTUID field) with your actual dataset field names (the headers only appear once, row 2 will have your first subject and his/her values, row 2 your second subject, etc.";
	        for(int i =3; i<phenoColname.length+3;i++){
	            result[0][i]=phenoColname[i-3];
	            result[1][i]=phenoColDesc[i-3];
	            result[2][i]="";
	        }
	        return result;
		}else{
			this.error("The DataSet not having any fields specified.");
			return null;
		}
	}
}
