package au.org.theark.phenotypic.web.component.phenoUpload2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.io.IOUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.model.entity.DelimiterType;
import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.entity.PhenoCollectionUpload;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoUpload2.form.ContainerForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep1 extends WizardStep
{
	/**
	 * 
	 */
	private static final long					serialVersionUID		= 4272918747277155957L;

	public java.util.Collection<String>		validationMessages	= null;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService					serviceInterface;

	private transient Logger					log						= LoggerFactory.getLogger(PhenoUploadContainer.class);

	private ContainerForm						containerForm;

	private int										mode;

	private TextField<String>					uploadIdTxtFld;
	private TextField<String>					uploadFilenameTxtFld;
	private DropDownChoice<FileFormat>		fileFormatDdc;
	private FileUploadField						fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType>	delimiterTypeDdc;

	/**
	 * Construct.
	 */
	public PhenoUploadStep1(ContainerForm containerForm, java.util.Collection<String>	validationMessages)
	{
		super("Step 1/5: Select data file to upload", "Select the file containing data, the file type and the specified delimiter, click Next to continue.");
		this.containerForm = containerForm;
		this.validationMessages = validationMessages;
		initialiseDetailForm();
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices()
	{
		// Initialise Drop Down Choices
		java.util.Collection<FileFormat> fieldFormatCollection = phenotypicService.getFileFormats();
		ChoiceRenderer fieldFormatRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FILE_FORMAT_NAME, au.org.theark.phenotypic.web.Constants.FILE_FORMAT_ID);
		fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, (List) fieldFormatCollection, fieldFormatRenderer);

		java.util.Collection<DelimiterType> delimiterTypeCollection = phenotypicService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
	}

	public void initialiseDetailForm()
	{
		// Set up field on form here
		uploadIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID);
		// uploadFilenameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// progress bar for upload
		// uploadProgressBar = new UploadProgressBar("progress", ajaxSimpleUploadForm);

		// fileUpload for payload
		fileUploadField = new FileUploadField(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		addComponents();
	}

	protected void attachValidators()
	{
		// Field validation here
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		fileFormatDdc.setRequired(true).setLabel(new StringResourceModel("error.fileFormat.required", this, new Model<String>("File Format")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	private void addComponents()
	{
		// Add components here
		add(uploadIdTxtFld.setEnabled(false));
		add(fileUploadField);
		add(fileFormatDdc);
		add(delimiterTypeDdc);
	}

	/**
	 * This is called when the next button that was clicked, I assume you want to use your for for data gathering.. Normally you’d save the object the
	 * the database Or just leave it in memory and save it in the Wizard onFinish()
	 */
	public void applyState()
	{
		log.info("Validating Pheno upload file!");

		writeOutFile();
		save();

		setComplete(true); // Set this step as done, you should put custom logic here
	}
	
	private void writeOutFile()
	{
		// TODO: Load the temporary directory from the application configuration instead
		FileUpload fileUpload = fileUploadField.getFileUpload();
		String tempDir = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString();
		createDirectoryIfNeeded(tempDir);
		String filePath = tempDir + File.separator + fileUpload.getClientFileName();
		File file = new File(filePath);
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file);
			IOUtils.copy(fileUpload.getInputStream(), fos);
			fos.close();
			System.out.println("Successfully stored a temporary file: " + filePath);
		}
		catch (IOException ioe)
		{
			System.out.println("Failed to save the uploaded file:" + ioe);
		}
		finally
		{
			if (fos != null)
			{
				fos = null;
			}
		}

		validationMessages = serviceInterface.validatePhenotypicDataFile(file);
		containerForm.getModelObject().setValidationMessages(validationMessages);
	}

	private void save()
	{
		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		// Get Collection in context
		Long sessionPhenoCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);

		// Retrieve file and store as Blob in databasse
		// TODO: AJAX-ified and asynchronous and hit database
		FileUpload fileUpload = fileUploadField.getFileUpload();

		try
		{
			// Copy file to BLOB object
			Blob payload = Hibernate.createBlob(fileUpload.getInputStream());
			containerForm.getModelObject().getUpload().setPayload(payload);
		}
		catch (IOException ioe)
		{
			System.out.println("Failed to save the uploaded file: " + ioe);
		}

		// Set details of Upload object
		containerForm.getModelObject().getUpload().setStudy(study);

		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
		containerForm.getModelObject().getUpload().setChecksum(checksum);
		containerForm.getModelObject().getUpload().setFilename(fileUpload.getClientFileName());

		containerForm.getModelObject().setPhenoCollection(phenotypicService.getPhenoCollection(sessionPhenoCollectionId));

		// Set details of link table object
		PhenoCollectionUpload phenoCollectionUpload = new PhenoCollectionUpload();
		phenoCollectionUpload.setCollection(phenotypicService.getPhenoCollection(sessionPhenoCollectionId));
		phenoCollectionUpload.setUpload(containerForm.getModelObject().getUpload());
		containerForm.getModelObject().setPhenoCollectionUpload(phenoCollectionUpload);

		// Save
		phenotypicService.createUpload(containerForm.getModelObject());

	}

	static final String	HEXES	= "0123456789ABCDEF";

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
}
