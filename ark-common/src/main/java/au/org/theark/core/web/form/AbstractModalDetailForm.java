package au.org.theark.core.web.form;

import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AjaxDeleteButton;

/**
 * @author cellis
 * @param <T>
 * 
 */
public abstract class AbstractModalDetailForm<T> extends Form<T>
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -4135522738458228329L;

	protected FeedbackPanel			feedbackPanel;
	protected Form<T>					containerForm;

	protected AjaxButton				saveButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				deleteButton;
	protected AjaxButton				editButton;
	protected AjaxButton				doneButton;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor		formVisitor			= new ArkFormVisitor();

	protected ArkCrudContainerVO	arkCrudContainerVO;									// Use this for the model where WebMarkupContainers are set inside this
																										// VO

	public AbstractModalDetailForm(String id, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVO, Form<T> containerForm)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		initialiseForm(true);
	}

	/**
	 * Initialise method that is specific to classes that follow the CrudContainerVO Pattern. The code related to each function has been modularised
	 * into protected methods, this is to provide the subclasses to refer to the protected methods without having to re-create/duplicate them when they
	 * extend the classes.
	 * 
	 * @param isArkCrudContainerVOPattern
	 */
	protected void initialiseForm(Boolean isArkCrudContainerVOPattern)
	{

		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if (isNew())
				{
					editCancelProcess(target, true);
				}
				else
				{
					editCancelProcessForUpdate(target);
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

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				saveOnErrorProcess(target);
			}
		};

		deleteButton = new AjaxDeleteButton(Constants.DELETE, new StringResourceModel("confirmDelete", this, null), new StringResourceModel(Constants.DELETE, this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4929802987078142352L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onDeleteConfirmed(target);
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
				editButtonProcess(target);
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

		doneButton = new AjaxButton("done", new StringResourceModel("doneKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				editCancelProcess(target, true);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};
		arkCrudContainerVO.getDetailPanelContainer().setEnabled(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		addComponentsToForm(true);
	}

	/**
	 * Overloaded for VO pattern
	 * 
	 * @param isArkCrudContainerVoPattern
	 */
	protected void addComponentsToForm(boolean isArkCrudContainerVoPattern)
	{
		arkCrudContainerVO.getEditButtonContainer().add(saveButton);
		arkCrudContainerVO.getEditButtonContainer().add(cancelButton.setDefaultFormProcessing(false));
		arkCrudContainerVO.getEditButtonContainer().add(deleteButton.setDefaultFormProcessing(false));

		add(arkCrudContainerVO.getDetailPanelFormContainer());
		add(arkCrudContainerVO.getEditButtonContainer());
	}

	protected void editCancelProcessForUpdate(AjaxRequestTarget target)
	{
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		onCancelPostProcess(target, true);
	}

	@SuppressWarnings("unchecked")
	protected void saveOnErrorProcess(AjaxRequestTarget target)
	{

		boolean setFocusError = false;
		WebMarkupContainer wmc = arkCrudContainerVO.getDetailPanelFormContainer();
		for (Iterator iterator = wmc.iterator(); iterator.hasNext();)
		{
			Component component = (Component) iterator.next();
			if (component instanceof FormComponent)
			{
				FormComponent formComponent = (FormComponent) component;

				if (!formComponent.isValid())
				{
					if (!setFocusError)
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

	protected void editButtonProcess(AjaxRequestTarget target)
	{

		deleteButton.setEnabled(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
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

	protected boolean isActionPermitted(String actionType)
	{

		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (actionType.equalsIgnoreCase(Constants.SAVE))
		{

			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE) || securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE))
			{

				flag = true;
			}
			else
			{
				flag = false;
			}

		}
		else if (actionType.equalsIgnoreCase(Constants.EDIT))
		{

			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE))
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}
		else if (actionType.equalsIgnoreCase(Constants.DELETE))
		{
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE))
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}
		return flag;
	}

	protected void onCancelPostProcess(AjaxRequestTarget target)
	{
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(false);

		target.addComponent(feedbackPanel);
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
	}

	/**
	 * Overloaded onCancelPostProcess. Use this when you use the ArkCrudContainerVO to manage the WebMarkupContainers.
	 * 
	 * @param target
	 * @param isArkCrudContainerVOPattern
	 */
	protected void onCancelPostProcess(AjaxRequestTarget target, Boolean isArkCrudContainerVOPattern)
	{
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(false);

		target.addComponent(feedbackPanel);
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
	}

	protected void editCancelProcess(AjaxRequestTarget target)
	{
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);

		target.addComponent(feedbackPanel);
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		onCancel(target);
	}

	protected void editCancelProcess(AjaxRequestTarget target, boolean isArkCrudContainerVoPattern)
	{
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);

		target.addComponent(feedbackPanel);
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
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
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);

		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
	}

	protected void onSavePostProcess(AjaxRequestTarget target, ArkCrudContainerVO arkCrudContainerVO)
	{
		// Visibility
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);

		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		target.addComponent(feedbackPanel);
	}

	protected void disableDetailForm(Long sessionId, String errorMessage)
	{
		if (sessionId == null)
		{
			arkCrudContainerVO.getDetailPanelContainer().setEnabled(false);
			this.error(errorMessage);
		}
		else
		{
			arkCrudContainerVO.getDetailPanelContainer().setEnabled(true);
		}
	}

	protected void disableModalDetailForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVO)
	{
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (!securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) && !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE)
				&& !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ) && !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE))
		{

			arkCrudContainerVO.getDetailPanelContainer().setEnabled(false);
			this.error("You do not have the required security privileges to work with this function.Please see your Administrator.");

		}
		else
		{

			if (sessionId == null)
			{
				arkCrudContainerVO.getDetailPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else
			{
				arkCrudContainerVO.getDetailPanelContainer().setEnabled(true);
			}
		}
	}

	abstract protected void attachValidators();

	abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	abstract protected void onDeleteConfirmed(AjaxRequestTarget target);

	abstract protected void processErrors(AjaxRequestTarget target);

	abstract protected boolean isNew();
}