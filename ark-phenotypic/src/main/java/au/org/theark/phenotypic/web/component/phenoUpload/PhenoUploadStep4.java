package au.org.theark.phenotypic.web.component.phenoUpload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoUploadReport;
import au.org.theark.phenotypic.web.component.phenoUpload.form.WizardForm;

/**
 * The 4th step of this wizard.
 */
public class PhenoUploadStep4 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2788948560672351760L;
	private Form<UploadVO>						containerForm;
	private WizardForm wizardForm;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService iPhenotypicService;
	
	/**
	 * Construct.
	 */
	public PhenoUploadStep4(String id, Form<UploadVO> containerForm, WizardForm wizardForm)
	{
		super(id, "Step 4/5: Confirm Upload", "Data will now be written to the database, click Next to continue, otherwise click Cancel.");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}
	
	private void initialiseDetailForm() 
	{
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
	}
	
	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		form.getArkExcelWorkSheetAsGrid().setVisible(false);
		target.addComponent(form.getArkExcelWorkSheetAsGrid());
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		// Filename seems to be lost from model when moving between steps in wizard
		containerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());
		
		// Perform actual import of data
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		StringBuffer uploadReport = null;
		
		uploadReport = iPhenotypicService.uploadAndReportPhenotypicDataFile(containerForm.getModelObject());
		
//		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
//		char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
//		
//		try 
//		{
//			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
//			inputStream.reset();
//			uploadReport = iPhenotypicService.uploadAndReportPhenotypicDataFile(inputStream, fileFormat, delimiterChar);
//			
//		} 
//		catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
		
		// Update the report
		updateUploadReport(uploadReport.toString());
		
		// Save all objects to the database
		save();
	}
	
	public void updateUploadReport(String importReport)
	{
		// Set Upload report
		PhenoUploadReport phenoUploadReport = new PhenoUploadReport();
		phenoUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		phenoUploadReport.append(importReport);
		byte[] bytes = phenoUploadReport.getReport().toString().getBytes();
		Blob uploadReportBlob = Hibernate.createBlob(bytes);
		containerForm.getModelObject().getUpload().setUploadReport(uploadReportBlob);
	}
	
	private void save()
	{
		containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
		containerForm.getModelObject().getUpload().setUploadType("FIELD_DATA");
		iPhenotypicService.createUpload(containerForm.getModelObject());
	}
}