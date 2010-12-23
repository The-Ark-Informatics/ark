package au.org.theark.phenotypic.web.component.phenoUpload.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
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
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;

import au.org.theark.core.web.component.upload.UploadPage;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoUpload.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class DetailForm extends AbstractDetailForm<UploadVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			phenotypicService;
	
	private ContainerForm					fieldContainerForm;

	private int								mode;
	
	private TextField<String>									uploadIdTxtFld;
	private TextField<String>									uploadFilenameTxtFld;
	private DropDownChoice<FileFormat>						fileFormatDdc;
	private FileUploadField fileUploadField;
	private UploadProgressBar uploadProgressBar;
	
	// Form for Wicket upload item
	final FileUploadForm ajaxSimpleUploadForm = new FileUploadForm("ajax-simpleUpload");

	/**
	 * Constructor
	 * @param id
	 * @param feedBackPanel
	 * @param detailPanel
	 * @param listContainer
	 * @param detailsContainer
	 * @param containerForm
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param detailFormContainer
	 * @param searchPanelContainer
	 */
	public DetailForm(	String id,
						FeedbackPanel feedBackPanel, 
						DetailPanel detailPanel, 
						WebMarkupContainer listContainer, 
						WebMarkupContainer detailsContainer, 
						Form<UploadVO> containerForm,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						WebMarkupContainer detailFormContainer,
						WebMarkupContainer searchPanelContainer)
	{
		
		super(	id,
				feedBackPanel, 
				listContainer,
				detailsContainer,
				detailFormContainer,
				searchPanelContainer,
				viewButtonContainer,
				editButtonContainer,
				containerForm);
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoice()
	{
		// Initialise Drop Down Choices
		java.util.Collection<FileFormat> fieldFormatCollection = phenotypicService.getFileFormats();
		ChoiceRenderer fieldFormatRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FILE_FORMAT_NAME, au.org.theark.phenotypic.web.Constants.FILE_FORMAT_ID);
		fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, (List) fieldFormatCollection, fieldFormatRenderer);
	}

	public void initialiseDetailForm()
	{
		// Set up field on form here
		uploadIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID);
		uploadFilenameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);
		
		// progress bar for upload
		uploadProgressBar = new UploadProgressBar("progress", ajaxSimpleUploadForm);
		
		// Initialise Drop Down Choices
		//initialiseDropDownChoice();

		attachValidators();
		addComponents();
	}

	protected void attachValidators()
	{
		// Field validation here
		uploadFilenameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
	}

	private void addComponents()
	{
		// Add components here eg:
		detailPanelFormContainer.add(uploadIdTxtFld);
		detailPanelFormContainer.add(uploadFilenameTxtFld);
		ajaxSimpleUploadForm.add(new UploadProgressBar("progress", ajaxSimpleUploadForm));
		add(ajaxSimpleUploadForm);
		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<UploadVO> containerForm, AjaxRequestTarget target)
	{
		// Implement Save/Update
		if (containerForm.getModelObject().getUpload().getId() == null)
		{
			// Save
			phenotypicService.createUpload(containerForm.getModelObject().getUpload());
			this.info("Phenotypic collection " + containerForm.getModelObject().getUpload().getFilename() + " was created successfully");
			processErrors(target);
		}
		else
		{
			// Update the Field
			phenotypicService.updateUpload(containerForm.getModelObject().getUpload());
			this.info("Phenotypic collection " + containerForm.getModelObject().getUpload().getFilename() + " was updated successfully");
			processErrors(target);
		}
		
		onSavePostProcess(target);
		/*
		 * TODO:(CE) To handle Business and System Exceptions here
		 * 
		 */
	}

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
		
	public AjaxButton getDeleteButton()
	{
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton)
	{
		this.deleteButton = deleteButton;
	}
	
	/**
	 * 
	 */
	protected  void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow){
		//TODO:(CE) To handle Business and System Exceptions here
		phenotypicService.deleteUpload(containerForm.getModelObject().getUpload());
   	this.info("Upload file " + containerForm.getModelObject().getUpload().getFilename() + " was deleted successfully");
   		
   	// Display delete confirmation message
   	target.addComponent(feedBackPanel);
   	//TODO Implement Exceptions in PhentoypicService
		//  } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		//  study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		//  this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
     
		// Close the confirm modal window
   	selectModalWindow.close(target);
   	// Move focus back to Search form
		UploadVO uploadVo = new UploadVO();
		setModelObject(uploadVo);
		onCancel(target);
	}
	
	/**
    * Form for uploads.
    */
   private class FileUploadForm extends Form<Void>
   {
       private FileUploadField fileUploadField;

       /**
        * Construct.
        * 
        * @param name
        *            Component name
        */
       public FileUploadForm(String name)
       {
           super(name);

           // set this form to multipart mode (allways needed for uploads!)
           setMultiPart(true);

           // Add one file input field
           add(fileUploadField = new FileUploadField(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_PAYLOAD));

           // Set maximum size to 10mb for demo purposes
           setMaxSize(Bytes.megabytes(10));
       }

       /**
        * @see org.apache.wicket.markup.html.form.Form#onSubmit()
        */
       @Override
       protected void onSubmit()
       {
           final FileUpload upload = fileUploadField.getFileUpload();
           if (upload != null)
           {
               // Create a new file
               File newFile = new File(getUploadFolder(), upload.getClientFileName());

               // Check new file, delete if it allready existed
               checkFileExists(newFile);
               try
               {
                   // Save to new file
                   newFile.createNewFile();
                   upload.writeTo(newFile);

                   this.info("saved file: " + upload.getClientFileName());
               }
               catch (Exception e)
               {
                   throw new IllegalStateException("Unable to write file");
               }
           }
       }
       
       /**
        * Check whether the file allready exists, and if so, try to delete it.
        * 
        * @param newFile
        *            the file to check
        */
       private void checkFileExists(File newFile)
       {
           if (newFile.exists())
           {
               // Try to delete the file
               if (!Files.remove(newFile))
               {
                   throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
               }
           }
       }

       private Folder getUploadFolder()
       {
           return new Folder("/tmp");
       }
   }
}