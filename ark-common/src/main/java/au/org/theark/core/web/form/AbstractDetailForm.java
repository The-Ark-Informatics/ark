package au.org.theark.core.web.form;

import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.web.component.ArkBusyAjaxButton;

/**
 * <p>
 * Abstract class for Detail Form sub-classes. It provides some common functionality that sub-classes inherit. Provides the skeleton methods for
 * onSave,onDelete,onCancel etc.Defines the core buttons like save,delete,cancel, edit and editCancel. Provides method to toggle the view from read
 * only to edit mode which is usually a common behavior the sub-classes can re-use.
 * </p>
 * 
 * @author nivedann
 * @param <T>
 * 
 */
public abstract class AbstractDetailForm<T> extends Form<T>
{

	private static final long		serialVersionUID	= 1L;

	protected WebMarkupContainer	resultListContainer;
	protected WebMarkupContainer	detailPanelContainer;
	protected WebMarkupContainer	searchPanelContainer;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;
	protected WebMarkupContainer	detailPanelFormContainer;
	protected FeedbackPanel			feedBackPanel;
	protected Form<T>					containerForm;

	protected AjaxButton				saveButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				deleteButton;
	protected AjaxButton				editButton;
	protected AjaxButton				editCancelButton;

	protected ModalWindow			selectModalWindow;
	
	// Add a visitor class for required field marking/validation/highlighting
	ArkFormVisitor formVisitor = new ArkFormVisitor();
	public void onBeforeRender()
	{
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	/**
	 * Implement this to add all the form components/objects
	 */
	protected void addFormComponents()
	{
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
	}

	abstract protected void attachValidators();

	protected void onDelete(Form<T> containerForm, AjaxRequestTarget target)
	{
		selectModalWindow.show(target);
		target.addComponent(selectModalWindow);
	}

	abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	abstract protected void processErrors(AjaxRequestTarget target);
	
	abstract protected boolean isNew();
	
	protected boolean isActionPermitted(String actionType){
		
		boolean flag = false;
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		if(actionType.equalsIgnoreCase(Constants.SAVE)){
			
			if( securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.UPDATE) ||
				securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.CREATE)){
					
				flag = true;
			}else{
				flag = false;
			}
			
		}
		else if(actionType.equalsIgnoreCase(Constants.EDIT)){
			
			if( securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.UPDATE)){
				flag = true;
			}else{
				flag = false;
			}
		}
		else if(actionType.equalsIgnoreCase(Constants.DELETE)){
			if( securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.DELETE)){
				flag = true;
			}else{
				flag = false;
			}
		}
		
		return flag;
	}

	protected void onCancelPostProcess(AjaxRequestTarget target)
	{

		detailPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		detailPanelFormContainer.setEnabled(false);
		resultListContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		editButtonContainer.setVisible(false);
		
		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(resultListContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(detailPanelFormContainer);
		target.addComponent(editButtonContainer);
	}

	/**
	 * Constructor for AbstractDetailForm class
	 * 
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
	public AbstractDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer resultListContainer, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, Form<T> containerForm)
	{
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
	protected void initialiseForm()
	{

		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if(isNew()){
					editCancelProcess(target);
				}else{
					resultListContainer.setVisible(false); // Hide the Search Result List Panel via the WebMarkupContainer
					detailPanelContainer.setVisible(false); // Hide the Detail Panle via the WebMarkupContainer
					target.addComponent(detailPanelContainer);// Attach the Detail WebMarkupContainer to be re-rendered using Ajax
					target.addComponent(resultListContainer);// Attach the resultListContainer WebMarkupContainer to be re-rendered using Ajax
					onCancelPostProcess(target);
				}
			}
		};

		saveButton = new ArkBusyAjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
//TODO NN Uncomment after User Management UI is completed			
//			@Override
//			public boolean isVisible()
//			{
//				return isActionPermitted(Constants.SAVE);
//			}
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(containerForm, target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				boolean setFocusError = false;
				WebMarkupContainer wmc = (WebMarkupContainer) form.get("detailFormContainer");
				for (Iterator iterator = wmc.iterator(); iterator.hasNext();)
				{
					Component component = (Component) iterator.next();
					if (component instanceof FormComponent)
					{
						FormComponent formComponent = (FormComponent) component;
						
						if(!formComponent.isValid())
						{
							if(!setFocusError)
							{
								// Place focus on field in error (for the first field in error)
								target.focusComponent(formComponent);
				            setFocusError = true;	
							}
						}
					}
				}
				
				processErrors(target);
			}
		};

		deleteButton = new AjaxButton(Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// target.addComponent(detailPanelContainer);
				onDelete(containerForm, target);

			}
			
			//TODO NN Uncomment after User Management UI is completed	
//			@Override
//			public boolean isVisible()
//			{
//				return isActionPermitted(Constants.DELETE);
//			}
		};

		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				deleteButton.setEnabled(true);
				// The visibility of the delete button should not be changed from 
				// any of the abstract classes.  This allows the implementation
				// to control the visibility of the delete button. 
				// NB: SearchForm onNew has the Delete button's setEnabled(false)
//				deleteButton.setVisible(true);
				viewButtonContainer.setVisible(false);
				editButtonContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(true);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
				target.addComponent(detailPanelFormContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
			
			//TODO NN Uncomment after User Management UI is completed	
//			@Override
//			public boolean isVisible()
//			{
//				return isActionPermitted(Constants.EDIT);
//			}
		};

		editCancelButton = new ArkBusyAjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				editCancelProcess(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};

		selectModalWindow = initialiseModalWindow();

		addComponentsToForm();
	}

	
	protected void editCancelProcess(AjaxRequestTarget target){
		resultListContainer.setVisible(true);
		detailPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(true);

		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(resultListContainer);
		onCancel(target);
		
	}
	protected void addComponentsToForm()
	{

		detailPanelFormContainer.add(selectModalWindow);
		add(detailPanelFormContainer);

		editButtonContainer.add(saveButton);
		editButtonContainer.add(cancelButton.setDefaultFormProcessing(false));
		editButtonContainer.add(deleteButton.setDefaultFormProcessing(false));

		viewButtonContainer.add(editButton);
		viewButtonContainer.add(editCancelButton.setDefaultFormProcessing(false));

		add(editButtonContainer);
		add(viewButtonContainer);

	}

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode.
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target)
	{

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

	protected void onDeleteCancel(AjaxRequestTarget target, ModalWindow selectModalWindow)
	{
		selectModalWindow.close(target);
	}

	protected ModalWindow initialiseModalWindow()
	{

		// The ModalWindow, showing some choices for the user to select.
		selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow")
		{

			protected void onSelect(AjaxRequestTarget target, String selection)
			{
				onDeleteConfirmed(target, selection, selectModalWindow);
			}

			protected void onCancel(AjaxRequestTarget target)
			{
				onDeleteCancel(target, selectModalWindow);
			}
		};

		return selectModalWindow;

	}
}
