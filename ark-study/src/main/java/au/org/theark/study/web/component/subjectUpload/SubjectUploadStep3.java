package au.org.theark.study.web.component.subjectUpload;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.util.ArkSheetMetaData;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

import com.csvreader.CsvReader;

/**
 * The first step of this wizard.
 */
public class SubjectUploadStep3 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long				serialVersionUID		= 2987959815074138750L;
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;
	private WizardForm						wizardForm;
	private CheckBox							updateChkBox;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService					studyService;

	transient Sheet							sheet;													// an instance of an Excel WorkSheet
	private ArkSheetMetaData				meta;

	/**
	 * Construct.
	 */
	public SubjectUploadStep3(String id, Form<UploadVO> containerForm, WizardForm wizardForm)
	{
		super(id, "Step 3/5: Data Validation", "The data in the file is now validated, correct any errors and try again, otherwise, click Next to continue.");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}

	private void initialiseDetailForm()
	{
		meta = new ArkSheetMetaData(); // init Sheet Meta Data
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));

		updateChkBox = new CheckBox("updateChkBox");
		updateChkBox.setVisible(true);

		updateChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4514605801401294450L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (containerForm.getModelObject().getUpdateChkBox())
				{
					wizardForm.getNextButton().setEnabled(true);
				}
				else
				{
					wizardForm.getNextButton().setEnabled(false);
				}
				target.addComponent(wizardForm.getWizardButtonContainer());
			}
		});

		add(updateChkBox);
	}

	/**
	 * @param validationMessages
	 *           the validationMessages to set
	 */
	public void setValidationMessage(String validationMessage)
	{
		this.validationMessage = validationMessage;
	}

	/**
	 * @return the validationMessages
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
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		if (containerForm.getModelObject().getUpdateChkBox())
			containerForm.getModelObject().setValidationMessages(null);
	}

	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
		char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
		validationMessages = studyService.validateSubjectFileData(this.wizardForm.getFile(), fileFormat, delimiterChar);
		this.containerForm.getModelObject().setValidationMessages(validationMessages);
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

		if (validationMessage != null && validationMessage.length() > 0)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
	}

	public void showWorkbook()
	{
		try
		{
			if(wizardForm.getFile().getExtension().equalsIgnoreCase("XLS"))
			{
				// Streams directly from File upload inputStream into Workbook.getWorkbook(Inputstream)
				Workbook wkb = Workbook.getWorkbook(containerForm.getModelObject().getUpload().getPayload().getBinaryStream());
				sheet = wkb.getSheet(0); // get First Work Sheet
			}
			else
			{
				// Convert from csv or text
				InputStream input = containerForm.getModelObject().getUpload().getPayload().getBinaryStream();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(input);
				char delimiterType = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
				CsvReader csvReader = new CsvReader(inputStreamReader, delimiterType);
				WritableWorkbook wwkb = Workbook.createWorkbook(output);
				jxl.write.WritableSheet wsheet = wwkb.createSheet("First Sheet", 0);
				int row = 0;
				// Loop through all rows in file
				while (csvReader.readRecord())
				{
					String[] stringArray = csvReader.getValues();
					for (int col = 0; col < stringArray.length; col++)
					{
						jxl.write.Label label = new jxl.write.Label(col, row, stringArray[col]);
						wsheet.addCell(label); 
					}
					row++;
				}
				
				// All sheets and cells added. Now write out the workbook
				wwkb.write();
				
				sheet = wwkb.getSheet(0); // get First Work Sheet to display in webpage	
				
				wwkb.close();
				output.flush();
	    		input.close();
	    		output.close();
			}
			
			/*
			 * Sets Sheet meta data. The HTML table creation needs this object to know about the rows and columns
			 */
			meta.setRows(sheet.getRows());
			meta.setCols(sheet.getColumns());
		}
		catch (Exception ex)
		{
			validationMessage = "Error with creating workbook for display";
		}
	}

	/**
	 * @param updateChkBox
	 *           the updateChkBox to set
	 */
	public void setUpdateChkBox(CheckBox updateChkBox)
	{
		this.updateChkBox = updateChkBox;
	}

	/**
	 * @return the updateChkBox
	 */
	public CheckBox getUpdateChkBox()
	{
		return updateChkBox;
	}
}
