package au.org.theark.core.web.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;

/**
 * <p>
 * Abstract class for Detail Form sub-classes. It provides some common functionality that sub-classes inherit.
 * Provides the skeleton methods for onSave,onDelete,onCancel etc.Defines the core buttons like save,delete,cancel, edit and editCancel.
 * Provides method to toggle the view from read only to edit mode which is usually a common behavior the sub-classes can re-use.
 * </p>
 * @author nivedann
 * @param <T>
 *
 */
public abstract class AbstractDetailForm<T> extends Form<T>{

	private static final long serialVersionUID = 1L;

	protected WebMarkupContainer  resultListContainer;
	protected WebMarkupContainer detailPanelContainer;
	protected WebMarkupContainer searchPanelContainer;
	protected WebMarkupContainer viewButtonContainer;
	protected WebMarkupContainer editButtonContainer;
	protected WebMarkupContainer detailPanelFormContainer;
	protected FeedbackPanel feedBackPanel;;
	protected Form<T> containerForm;
	
	protected AjaxButton saveButton;
	protected AjaxButton cancelButton;
	protected AjaxButton deleteButton;
	protected AjaxButton editButton;
	protected AjaxButton editCancelButton;

	protected ModalWindow selectModalWindow;
	
	/**
	 * Implement this to add all the form components/objects
	 */
	protected void addFormComponents(){
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
	}
	
	abstract protected void attachValidators();
	
	protected void onDelete(Form<T> containerForm, AjaxRequestTarget target){
		selectModalWindow.show(target);
		target.addComponent(selectModalWindow);
	}
	
	abstract protected void onCancel(AjaxRequestTarget target);
	
	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);
	
	abstract protected void processErrors(AjaxRequestTarget target);
	
	
	
	
	protected void onCancelPostProcess(AjaxRequestTarget target){
		
		resultListContainer.setVisible(false);
		detailPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(true);

		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(resultListContainer);
	}

	/**
	 * Constructor for AbstractDetailForm class
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param detailPanelContainer
	 * @param detailPanelFormContainer
	 * @param searchPanelContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param containerForm
	 */
	public AbstractDetailForm(	String id,
								FeedbackPanel feedBackPanel,
								WebMarkupContainer resultListContainer,
								WebMarkupContainer detailPanelContainer,
								WebMarkupContainer detailPanelFormContainer,
								WebMarkupContainer searchPanelContainer,
								WebMarkupContainer viewButtonContainer,
								WebMarkupContainer editButtonContainer,
								Form<T> containerForm) {
		super(id);
		this.resultListContainer = resultListContainer;
		this.detailPanelContainer = detailPanelContainer;
		this.feedBackPanel = feedBackPanel;
		this.searchPanelContainer = searchPanelContainer;
		this.editButtonContainer = editButtonContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.containerForm = containerForm;
		
		initialiseForm();
	}
	
	@SuppressWarnings("serial")
	protected void initialiseForm(){
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				resultListContainer.setVisible(false); //Hide the Search Result List Panel via the WebMarkupContainer
				detailPanelContainer.setVisible(false); //Hide the Detail Panle via the WebMarkupContainer
				target.addComponent(detailPanelContainer);//Attach the Detail WebMarkupContainer to be re-rendered using Ajax
				target.addComponent(resultListContainer);//Attach the resultListContainer WebMarkupContainer to be re-rendered using Ajax
				onCancel(target);//Invoke a onCancel() that the sub-class can use to build anything more specific
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(containerForm, target);
				target.addComponent(detailPanelContainer);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
		
		deleteButton = new AjaxButton(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(detailPanelContainer);
				onDelete(containerForm, target);
			
			}
		};
		
		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				deleteButton.setEnabled(true);
				deleteButton.setVisible(true);
				viewButtonContainer.setVisible(false);
				editButtonContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(true);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
				target.addComponent(detailPanelFormContainer);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
		
		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{	
				onCancel(target);
			}
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
		
		selectModalWindow = initialiseModalWindow();
		
		addComponentsToForm();
	}
	
	protected void addComponentsToForm(){
		
		detailPanelFormContainer.add(selectModalWindow);
		add(detailPanelFormContainer);

		editButtonContainer.add(saveButton);
		editButtonContainer.add(cancelButton.setDefaultFormProcessing(false));
		editButtonContainer.add(deleteButton);
		
		viewButtonContainer.add(editButton);
		viewButtonContainer.add(editCancelButton.setDefaultFormProcessing(false));
		
		add(editButtonContainer);
		add(viewButtonContainer);
		
	}
	
	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by
	 * sub-classes as part of the onSave() implementation.Once the user has pressed Save either to
	 * create a new entity or update, invoking this method will place the new/edited record panel in 
	 * View/Read only mode.
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target){
		
		detailPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		detailPanelFormContainer.setEnabled(false);
		resultListContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		editButtonContainer.setVisible(false);
		
		target.addComponent(resultListContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(detailPanelFormContainer);
		target.addComponent(searchPanelContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		
	}
	

	protected abstract void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow);
	
	protected  void onDeleteCancel(AjaxRequestTarget target, ModalWindow selectModalWindow){
		selectModalWindow.close(target);
        // Go back into Edit mode (and remove feedback, if straight after "New")
        detailPanelFormContainer.setEnabled(true);
		editButtonContainer.setVisible(true);
		viewButtonContainer.setVisible(false);
		deleteButton.setEnabled(true);
		deleteButton.setVisible(true);
		target.addComponent(detailPanelFormContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
	}
	
	protected ModalWindow initialiseModalWindow(){
	
		// The ModalWindow, showing some choices for the user to select.
		selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow"){

			protected void onSelect(AjaxRequestTarget target, String selection){
				onDeleteConfirmed(target,selection, selectModalWindow);
		    }
	
		    protected void onCancel(AjaxRequestTarget target){
		    	onDeleteCancel(target,selectModalWindow);
		    }
		};
		
		return selectModalWindow;

	}
}
