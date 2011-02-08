package au.org.theark.phenotypic.web.component.phenoUpload.form;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.hibernate.Hibernate;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.wizard.AjaxWizardButtonBar;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.phenotypic.model.entity.DelimiterType;
import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.entity.PhenoCollectionUpload;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoUpload.WizardPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class WizardForm extends AbstractWizardForm<UploadVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private ContainerForm						fieldContainerForm;

	private AjaxWizardButtonBar				buttonBar;
	
	private static final String	HEXES	= "0123456789ABCDEF";

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param wizardStep
	 * @param listContainer
	 * @param wizardContainer
	 * @param containerForm
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param wizardFormContainer
	 * @param searchPanelContainer
	 */
	public WizardForm(String id, FeedbackPanel feedBackPanel, WizardPanel wizardPanel, WebMarkupContainer listContainer, WebMarkupContainer wizardContainer, Form<UploadVO> containerForm,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer wizardFormContainer, WebMarkupContainer searchPanelContainer,
			AjaxWizardButtonBar buttonBar)
	{

		super(id, feedBackPanel, listContainer, wizardContainer, wizardFormContainer, searchPanelContainer, viewButtonContainer, editButtonContainer, containerForm);
		this.buttonBar = buttonBar;
	}


	public void initialiseDetailForm()
	{
		attachValidators();
		addComponents();
	}

	private void addComponents()
	{
		// Add components here eg:
//		wizardPanelFormContainer.add(uploadIdTxtFld.setEnabled(false));
//		wizardPanelFormContainer.add(fileUploadField);
//		wizardPanelFormContainer.add(fileFormatDdc);
//		wizardPanelFormContainer.add(delimiterTypeDdc);

		// TODO: AJAXify the form to show progress bar
		// ajaxSimpleUploadForm.add(new UploadProgressBar("progress", ajaxSimpleUploadForm));
		// add(ajaxSimpleUploadForm);

		add(wizardPanelFormContainer);
	}

	private void createDirectoryIfNeeded(String directoryName)
	{
		File theDir = new File(directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists())
		{
			System.out.println("creating directory: " + directoryName);
			theDir.mkdir();
		}
	}
	
	

	public static String getHex(byte[] raw)
	{
		if (raw == null)
		{
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw)
		{
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	@Override
	public void onFinish(AjaxRequestTarget target, Form form)
	{

	}

	private void onSavePostProcess(AjaxRequestTarget target)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCancel(AjaxRequestTarget target)
	{
		// Implement Cancel
		UploadVO uploadVO = new UploadVO();
		containerForm.setModelObject(uploadVO);
		onCancelPostProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	@Override
	public void onError(AjaxRequestTarget target, Form form)
	{
		processErrors(target);	
	}


	@Override
	protected void attachValidators()
	{
		// TODO Auto-generated method stub
		
	}	
}