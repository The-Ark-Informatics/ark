package au.org.theark.lims.web.component.bioCollection.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
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

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.Status;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.AjaxDeleteButton;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.bioCollection.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class DetailForm extends AbstractDetailForm<LimsVO>
{
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService			iLimsService;
	
	private ContainerForm					fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			collectionIdTxtFld;
	private TextArea<String>			commentsTxtAreaFld;
	private DateTextField			collectionDateTxtFld;
	private DateTextField			surgeryDateTxtFld;
	
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
						AbstractContainerForm<LimsVO> containerForm,
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


	public void initialiseDetailForm()
	{
		idTxtFld = new TextField<String>("id");
		collectionIdTxtFld = new TextField<String>("collectionId");
		collectionIdTxtFld.add(new ArkDefaultFormFocusBehavior());
		commentsTxtAreaFld = new TextArea<String>("comments");
		collectionDateTxtFld = new DateTextField("collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);		 

		ArkDatePicker startDatePicker = new ArkDatePicker(); 
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);
		
		ArkDatePicker endDatePicker = new ArkDatePicker(); 
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);
		
		attachValidators();
		addComponents();
	}
	
	protected void attachValidators()
	{
		collectionIdTxtFld.setRequired(true).setLabel(new StringResourceModel("error.limsCollection.collectionId.required", this, new Model<String>("Name")));
	}

	private void addComponents()
	{
		detailPanelFormContainer.add(idTxtFld.setEnabled(false));
		detailPanelFormContainer.add(collectionIdTxtFld);
		detailPanelFormContainer.add(commentsTxtAreaFld);
		detailPanelFormContainer.add(collectionDateTxtFld);
		detailPanelFormContainer.add(surgeryDateTxtFld);
		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target)
	{

		if (containerForm.getModelObject().getBioCollection().getId() == null)
		{
			// Save
			iLimsService.createBioCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getBioCollection().getName() + " was created successfully");
			processErrors(target);
		}
		else
		{
			// Update
			iLimsService.updateBioCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getBioCollection().getName() + " was updated successfully");
			processErrors(target);
		}
		
		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);

		java.util.List<au.org.theark.core.model.lims.entity.BioCollection> bioCollectionList = new ArrayList<BioCollection>(0);
		try
		{
			bioCollectionList = iLimsService.searchBioCollection(limsVo.getBioCollection());
		}
		catch (ArkSystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		containerForm.getModelObject().setLimsCollectionList(bioCollectionList);
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
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow)
	{
		//TODO:(CE) To handle Business and System Exceptions here
		iLimsService.deleteBioCollection(containerForm.getModelObject());
		this.info("LIMS collection " + containerForm.getModelObject().getBioCollection().getName() + " was deleted successfully");
   		
   	// Display delete confirmation message
   	target.addComponent(feedBackPanel);
	   //TODO Implement Exceptions in PhentoypicService
		//  } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		//  study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		//  this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
     
		// Close the confirm modal window
	   selectModalWindow.close(target);
	   // Move focus back to Search form
		LimsVO phenoCollectionVo = new LimsVO();
		containerForm.setModelObject(phenoCollectionVo);
		editCancelProcess(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if(containerForm.getModelObject().getBioCollection().getId() == null){
			return true;
		}else{
			return false;
		}
	}
}