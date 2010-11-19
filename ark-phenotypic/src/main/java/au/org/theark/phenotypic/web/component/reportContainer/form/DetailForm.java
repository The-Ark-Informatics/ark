/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.reportContainer.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.reportContainer.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class DetailForm extends AbstractDetailForm<PhenoCollectionVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			phenotypicService;
	
	private ContainerForm					fieldContainerForm;

	private int								mode;

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

	private void initialiseDropDownChoice()
	{
		// Initialise Drop Down Choices
	}

	public void initialiseDetailForm()
	{
		// Set up field on form here

		// Initialise Drop Down Choices
		initialiseDropDownChoice();

		attachValidators();
		addComponents();
	}

	protected void attachValidators()
	{
		// Field validation here
	}

	private void addComponents()
	{
		// Add components here eg:
		//detailPanelFormContainer.add(idTxtFld);
		//detailPanelFormContainer.add(nameTxtFld);
		//add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<PhenoCollectionVO> containerForm, AjaxRequestTarget target)
	{
		/* Implement Save/Update
		if (containerForm.getModelObject().getPhenoCollection().getId() == null)
		{
			// Save
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
		 * 
		 */
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		// Implement Cancel
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
		//phenotypicService.deleteCollection(containerForm.getModelObject().getPhenoCollection());
   	//this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was deleted successfully");
   		
   	// Display delete confirmation message
   	//target.addComponent(feedBackPanel);
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