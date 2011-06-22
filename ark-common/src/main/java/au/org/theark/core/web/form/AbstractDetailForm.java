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
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AjaxDeleteButton;
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
	//private transient Logger log = LoggerFactory.getLogger(AbstractDetailForm.class);
	
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
	protected ArkFormVisitor formVisitor = new ArkFormVisitor();
	
	private ArkCrudContainerVO arkCrudContainerVO;//Use this for the model where WebMarkupContainers are set inside this VO
	
	
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
	
	public AbstractDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,Form<T> containerForm){
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedBackPanel;
		
		initialiseForm(true);
	}
	
	
	protected void initialiseForm(Boolean isArkCrudContainerVOPattern){
		
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if(isNew()){
					editCancelProcess(target,true);
				}else{
					arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
					arkCrudContainerVO.getDetailPanelContainer().setVisible(false);
					target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
					target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
					onCancelPostProcess(target,true);
				}
			}
		
		};
		
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -423605230448635419L;

			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.SAVE);
			}
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(containerForm, target);
				target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
			}

			@SuppressWarnings("unchecked")
			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				boolean setFocusError = false;
				WebMarkupContainer wmc = arkCrudContainerVO.getDetailPanelContainer();
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
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -2430231894703055744L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// target.addComponent(detailPanelContainer);
				onDelete(containerForm, target);

			}
			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.DELETE);
			}
		};
		
		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6282464357368710796L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				deleteButton.setEnabled(true);
				// The visibility of the delete button should not be changed from 
				// any of the abstract classes.  This allows the implementation
				// to control the visibility of the delete button. 
				// NB: SearchForm onNew has the Delete button's setEnabled(false)
//				deleteButton.setVisible(true);
				arkCrudContainerVO.getViewButtonContainer().setVisible(false);
				arkCrudContainerVO.getEditButtonContainer().setVisible(true);
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
				target.addComponent(arkCrudContainerVO.getViewButtonContainer());
				target.addComponent(arkCrudContainerVO.getEditButtonContainer());
				target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
			
			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.EDIT);
			}
		};
		
		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				editCancelProcess(target,true);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
			
			
		};
		
		selectModalWindow = initialiseModalWindow();

		addComponentsToForm(true);
		
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
			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.SAVE);
			}
			
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(containerForm, target);
				target.addComponent(detailPanelContainer);
			}

			@SuppressWarnings("unchecked")
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

		/*
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
		*/
		deleteButton = new AjaxDeleteButton(Constants.DELETE,	new StringResourceModel("confirmDelete", this, null),	new StringResourceModel(Constants.DELETE,	this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onDeleteConfirmed(target, null, selectModalWindow);
			}
				
			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.DELETE);
				
			}
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
			
			@Override
			public boolean isVisible()
			{
				return isActionPermitted(Constants.EDIT);
			}
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
	 * Overloaded for VO pattern
	 * @param isArkCrudContainerVoPattern
	 */
	protected void addComponentsToForm(boolean isArkCrudContainerVoPattern)
	{
		arkCrudContainerVO.getEditButtonContainer().add(saveButton);
		arkCrudContainerVO.getEditButtonContainer().add(cancelButton.setDefaultFormProcessing(false));
		arkCrudContainerVO.getEditButtonContainer().add(deleteButton.setDefaultFormProcessing(false));
		
		arkCrudContainerVO.getViewButtonContainer().add(editButton);
		arkCrudContainerVO.getViewButtonContainer().add(editCancelButton.setDefaultFormProcessing(false));
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(selectModalWindow);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
		add(arkCrudContainerVO.getViewButtonContainer());
		add(arkCrudContainerVO.getEditButtonContainer());

	}

	
	@SuppressWarnings("unchecked")
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
	 * Overloaded onCancelPostProcess. Use this when you use the ArkCrudContainerVO to manage the
	 * WebMarkupContainers.
	 * @param target
	 * @param isArkCrudContianerVOPattern
	 */
	protected void onCancelPostProcess(AjaxRequestTarget target, Boolean isArkCrudContianerVOPattern){
			
			arkCrudContainerVO.getViewButtonContainer().setVisible(true);
			arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
			arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
			arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
			arkCrudContainerVO.getEditButtonContainer().setVisible(false);
			
			target.addComponent(feedBackPanel);
			target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
			target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
			target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
			target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());

			target.addComponent(arkCrudContainerVO.getViewButtonContainer());
			target.addComponent(arkCrudContainerVO.getEditButtonContainer());
		
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
	
	
	protected void editCancelProcess(AjaxRequestTarget target,boolean isArkCrudContainerVoPattern){
		
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(true);
		

		target.addComponent(feedBackPanel);
		target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		onCancel(target);
		
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
	
	protected void onSavePostProcess(AjaxRequestTarget target,ArkCrudContainerVO crudVO){
		//Visibility
		crudVO.getDetailPanelContainer().setVisible(true);
		crudVO.getViewButtonContainer().setVisible(true);
		crudVO.getSearchResultPanelContainer().setVisible(false);
		crudVO.getSearchPanelContainer().setVisible(false);
		crudVO.getEditButtonContainer().setVisible(false);
		
		//Enable
		crudVO.getDetailPanelFormContainer().setEnabled(false);
		crudVO.getViewButtonContainer().setEnabled(true);
	
		target.addComponent(crudVO.getSearchResultPanelContainer());
		target.addComponent(crudVO.getDetailPanelContainer());
		target.addComponent(crudVO.getDetailPanelFormContainer());
		target.addComponent(crudVO.getSearchPanelContainer());
		target.addComponent(crudVO.getViewButtonContainer());
		target.addComponent(crudVO.getEditButtonContainer());
		
		target.addComponent(feedBackPanel);
	}

	/**
	 * A helper method that handles the press of the Delete button, thus displaying a modal pop-up that required user selection
	 * 
	 * @param target
	 */
	protected void onDelete(Form<T> containerForm, AjaxRequestTarget target)
	{
		selectModalWindow.show(target);
		target.addComponent(selectModalWindow);
	}

	/**
	 * A helper method that handles the press of the Cancel button within the modal pop-up. ie Closes the modal pop-up
	 * 
	 * @param target
	 * @param selectModalWindow
	 */
	protected void onDeleteCancel(AjaxRequestTarget target, ModalWindow selectModalWindow)
	{
		selectModalWindow.close(target);
	}

	/**
	 * A helper method that initialises the modal window for delete confirmation
	 */
	protected ModalWindow initialiseModalWindow()
	{
		// The ModalWindow, showing some choices for the user to select.
		selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow")
		{

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1116985092871743122L;

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
	
	protected void disableDetailForm(Long sessionId, String errorMessage){	
		if (sessionId == null)
		{		
			detailPanelContainer.setEnabled(false);
			this.error(errorMessage);
		}
		else
		{
			detailPanelContainer.setEnabled(true);
		}

	}
	
	protected void disableDetailForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVO){	
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if(	!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.CREATE) &&
			!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.UPDATE) &&
			!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.READ)  &&
			!securityManager.isPermitted(currentUser.getPrincipals(),  PermissionConstants.UPDATE)){
			
			arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
			this.error("You do not have the required security privileges to work with this function.Please see your Administrator.");
			
		}else{

			if (sessionId == null){
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}else{	
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(true);
			}
		}
	}
	
	
	protected abstract void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow);
	
	abstract protected void attachValidators();

	abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	abstract protected void processErrors(AjaxRequestTarget target);
	
	abstract protected boolean isNew();
}
