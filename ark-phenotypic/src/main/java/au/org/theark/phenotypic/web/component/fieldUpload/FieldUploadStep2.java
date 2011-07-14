package au.org.theark.phenotypic.web.component.fieldUpload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenotypicValidator;
import au.org.theark.phenotypic.web.component.fieldUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class FieldUploadStep2 extends AbstractWizardStepPanel
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6923277221441497110L;
	static Logger	log	= LoggerFactory.getLogger(FieldUploadStep2.class);
	private Form<UploadVO>						containerForm;
	private String	validationMessage;
	public java.util.Collection<String> validationMessages = null;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService iPhenotypicService;
	
	private ArkDownloadAjaxButton downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", null, null, "txt");
	
	
	public FieldUploadStep2(String id) {
		super(id);
		initialiseDetailForm();
	}
	
	public FieldUploadStep2(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 2/5: File Format Validation", "The file format has been validated. If there are no errors, click Next to continue.");

		this.containerForm = containerForm;
		initialiseDetailForm();
	}
	
	private void initialiseDetailForm() 
	{
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
		add(downloadValMsgButton);
	}

	/**
	 * @param validationMessage the validationMessages to set
	 */
	public void setValidationMessage(String validationMessage)
	{
		this.validationMessage = validationMessage;
	}

	/**
	 * @return the validationMessage
	 */
	public String getValidationMessage()
	{
		return validationMessage;
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) 
	{
	
	}
	
	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		try
		{
			String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
			String fileFormat = filename.substring(filename.lastIndexOf('.')+1).toUpperCase();
			char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
			InputStream inputStream;
			
			// Uploading Fields of the Data Dictionary
			containerForm.getModelObject().getUpload().setUploadType("FIELD");
			
			// Only allow csv, txt or xls
			if(!(fileFormat.equalsIgnoreCase("CSV") || fileFormat.equalsIgnoreCase("TXT") || fileFormat.equalsIgnoreCase("XLS")))
			{
				throw new FileFormatException();
			}
			
			PhenotypicValidator phenotypicValidator = new PhenotypicValidator(iArkCommonService, iPhenotypicService, containerForm.getModelObject());
			inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			validationMessages = phenotypicValidator.validateMatrixPhenoFileFormat(inputStream, fileFormat, delimChar);
			
			containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			
			if(validationMessage != null && validationMessage.length() > 0)
			{
				form.getNextButton().setEnabled(false);
				target.addComponent(form.getWizardButtonContainer());
				downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage, "txt");
				addOrReplace(downloadValMsgButton);
				target.addComponent(downloadValMsgButton);
			}
			
			// Show file data
			inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			FileUpload fileUpload = containerForm.getModelObject().getFileUpload(); 
			inputStream.reset();
			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, fileUpload);
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);
			target.addComponent(form.getWizardPanelFormContainer());
			
		}
		catch (NullPointerException npe)
		{
			validationMessage = "Error attempting to display the file. Please check the file and try again.";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
		catch (IOException e)
		{
			validationMessage = "Error attempting to display the file. Please check the file and try again.";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
		catch (FileFormatException ffe)
		{
			validationMessage = "Error uploading file. You can only upload files of type: CSV (comma separated values), TXT (text), or XLS (Microsoft Excel file)";
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
	}
}