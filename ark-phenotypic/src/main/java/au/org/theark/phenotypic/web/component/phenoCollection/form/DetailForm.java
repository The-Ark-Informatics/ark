package au.org.theark.phenotypic.web.component.phenoCollection.form;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import mx4j.log.Log;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoCollection.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DetailForm extends AbstractDetailForm<PhenoCollectionVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			phenotypicService;
	
	private ContainerForm					fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private DropDownChoice<Status>	statusDdc;
	private TextArea<String>			descriptionTxtAreaFld;
	private DatePicker<Date>			startDateTxtFld;
	private DatePicker<Date>			expiryDateTxtFld;

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
						Form<PhenoCollectionVO> containerForm,
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

	private void initStatusDdc()
	{
		java.util.Collection<Status> statusCollection = phenotypicService.getStatus();
		ChoiceRenderer statusRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.STATUS_NAME, au.org.theark.phenotypic.web.Constants.STATUS_ID);
		statusDdc = new DropDownChoice<Status>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_STATUS, (List) statusCollection, statusRenderer);
	}

	public void initialiseDetailForm()
	{
		idTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_ID);
		nameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_NAME);
		descriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION);
		startDateTxtFld = new DatePicker<Date>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE);
		expiryDateTxtFld = new DatePicker<Date>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_EXPIRY_DATE);

		startDateTxtFld.setDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
		expiryDateTxtFld.setDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

		// Initialise Drop Down Choices
		initStatusDdc();

		attachValidators();
		addComponents();
	}

	protected void attachValidators()
	{
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.name.required", this, new Model<String>("Name")));
		statusDdc.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.status.required", this, new Model<String>("Status")));
	}

	private void addComponents()
	{
		detailPanelFormContainer.add(idTxtFld.setEnabled(false));
		detailPanelFormContainer.add(nameTxtFld);
		detailPanelFormContainer.add(descriptionTxtAreaFld);
		detailPanelFormContainer.add(statusDdc);
		detailPanelFormContainer.add(startDateTxtFld);
		detailPanelFormContainer.add(expiryDateTxtFld);

		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<PhenoCollectionVO> containerForm, AjaxRequestTarget target)
	{

		if (containerForm.getModelObject().getPhenoCollection().getId() == null)
		{
			// Save the Field
			phenotypicService.createCollection(containerForm.getModelObject().getPhenoCollection());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was created successfully");
			processErrors(target);
		}
		else
		{
			// Update the Field
			phenotypicService.updateField(containerForm.getModelObject().getField());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was updated successfully");
			processErrors(target);
		}
		
		onSavePostProcess(target);
		//TODO:(CE) To handle Business and System Exceptions here
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		PhenoCollectionVO phenoCollectionVO = new PhenoCollectionVO();
		containerForm.setModelObject(phenoCollectionVO);
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
		phenotypicService.deleteCollection(containerForm.getModelObject().getPhenoCollection());
   	this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was deleted successfully");
   		
   	// Display delete confirmation message
   	target.addComponent(feedBackPanel);
   	//TODO Implement Exceptions in PhentoypicService
		//  } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		//  study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		//  this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
     
		// Close the confirm modal window
   	selectModalWindow.close(target);
   	// Move focus back to Search form
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		setModelObject(phenoCollectionVo);
		onCancel(target);
	}
}