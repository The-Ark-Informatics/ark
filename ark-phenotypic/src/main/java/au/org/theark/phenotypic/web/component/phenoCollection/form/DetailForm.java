package au.org.theark.phenotypic.web.component.phenoCollection.form;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import mx4j.log.Log;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
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
	private DateTextField			startDateTxtFld;
	private DateTextField			expiryDateTxtFld;
	
	// Field selection Palette
	private Palette	fieldPalette;
	
	private WebMarkupContainer arkContextMarkup;

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
						WebMarkupContainer searchPanelContainer,
						WebMarkupContainer arkContextMarkup)
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
		
		this.arkContextMarkup = arkContextMarkup;
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
		startDateTxtFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		expiryDateTxtFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_EXPIRY_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		 
		DatePicker startDatePicker = new DatePicker()
		{ 
			@Override 
			protected boolean enableMonthYearSelection() 
			{ 
			return true; 
			} 
		}; 
		startDatePicker.bind(startDateTxtFld);
		startDateTxtFld.add(startDatePicker);
		
		DatePicker endDatePicker = new DatePicker()
		{ 
			@Override 
			protected boolean enableMonthYearSelection() 
			{ 
				return true; 
			} 
		}; 
		endDatePicker.bind(expiryDateTxtFld);
		expiryDateTxtFld.add(endDatePicker);

		// Initialise Drop Down Choices
		initStatusDdc();
		
		// Initialise Field Palette
		initFieldPalette();

		attachValidators();
		addComponents();
	}
	
	private void initFieldPalette()
	{	
		CompoundPropertyModel<PhenoCollectionVO> cpm  = (CompoundPropertyModel<PhenoCollectionVO> )containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "id");
		PropertyModel<Collection<Field>> selectedModPm = new PropertyModel<Collection<Field>>(cpm,"fieldsSelected");
		PropertyModel<Collection<Field>> availableModPm = new PropertyModel<Collection<Field>>(cpm,"fieldsAvailable");
		
		fieldPalette = new Palette(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_FIELD_PALETTE, selectedModPm, availableModPm, renderer, 10, true){
			@Override
			public ResourceReference getCSS(){ 
		      return null;
			}
		};
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
		detailPanelFormContainer.add(fieldPalette);

		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<PhenoCollectionVO> containerForm, AjaxRequestTarget target)
	{

		if (containerForm.getModelObject().getPhenoCollection().getId() == null)
		{
			// Save
			phenotypicService.createCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was created successfully");
			processErrors(target);
		}
		else
		{
			// Update
			phenotypicService.updateCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was updated successfully");
			processErrors(target);
		}
		
		// Reset context item
		ContextHelper contextHelper = new ContextHelper();
		contextHelper.setPhenoContextLabel(target, containerForm.getModelObject().getPhenoCollection().getName(), arkContextMarkup);
		
		onSavePostProcess(target);
		//TODO:(CE) To handle Business and System Exceptions here
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		PhenoCollectionVO phenoCollectionVO = new PhenoCollectionVO();
		containerForm.setModelObject(phenoCollectionVO);

		java.util.Collection<PhenoCollection> phenoCollectionCollection = phenotypicService.searchPhenotypicCollection(phenoCollectionVO.getPhenoCollection());
		containerForm.getModelObject().setPhenoCollectionCollection(phenoCollectionCollection);
		
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
	protected  void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow)
	{
		//TODO:(CE) To handle Business and System Exceptions here
		phenotypicService.deleteCollection(containerForm.getModelObject());
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