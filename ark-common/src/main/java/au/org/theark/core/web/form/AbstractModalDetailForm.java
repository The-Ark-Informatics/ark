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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
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
	protected CompoundPropertyModel<T>	cpModel;

	protected AjaxButton				saveButton;
	protected AjaxButton				doneButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				deleteButton;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor		formVisitor			= new ArkFormVisitor();

	// Use this for the model where WebMarkupContainers are set inside this VO
	protected ArkCrudContainerVO	arkCrudContainerVo;									

	public AbstractModalDetailForm(String id, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVo, CompoundPropertyModel<T> cpModel)
	{
		super(id, cpModel);
		this.feedbackPanel = feedbackPanel;
		this.arkCrudContainerVo = arkCrudContainerVo;
		this.cpModel = cpModel;
		
		initialiseForm();
	}

	/**
	 * Initialise method that is specific to classes that follow the ArkCrudContainerVO Pattern. The code related to each function has been modularised
	 * into protected methods, this is to provide the subclasses to refer to the protected methods without having to re-create/duplicate them when they
	 * extend the classes.
	 */
	protected void initialiseForm()
	{
		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -423605230448635419L;

			@Override
			public boolean isVisible()
			{
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(target);
				target.addComponent(arkCrudContainerVo.getDetailPanelContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				saveOnErrorProcess(target);
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
				onCancel(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};
		
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onCancel(target);
			}

		};
		
		deleteButton = new AjaxDeleteButton(Constants.DELETE,	new StringResourceModel("confirmDelete", this, null),	new StringResourceModel(Constants.DELETE,	this, null))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6596207763260166508L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onDeleteConfirmed(target, form);
			}
				
			@Override
			public boolean isVisible()
			{
				return ArkPermissionHelper.isActionPermitted(Constants.DELETE);
			}
		};
		
		// Override default settings as set in ArkCrudContainerVO 
		arkCrudContainerVo.getDetailPanelContainer().setEnabled(true);
		arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVo.getEditButtonContainer().setEnabled(true);
		arkCrudContainerVo.getEditButtonContainer().setVisible(true);
		
		addComponentsToForm();
	}

	protected void addComponentsToForm()
	{
		arkCrudContainerVo.getEditButtonContainer().add(saveButton);
		arkCrudContainerVo.getEditButtonContainer().add(doneButton.setDefaultFormProcessing(false));
		arkCrudContainerVo.getEditButtonContainer().add(cancelButton.setDefaultFormProcessing(false));
		arkCrudContainerVo.getEditButtonContainer().add(deleteButton);

		add(arkCrudContainerVo.getDetailPanelFormContainer());
		add(arkCrudContainerVo.getEditButtonContainer());
	}

	@SuppressWarnings("unchecked")
	public void onBeforeRender()
	{
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	@SuppressWarnings("unchecked")
	protected void saveOnErrorProcess(AjaxRequestTarget target)
	{
		boolean setFocusError = false;
		WebMarkupContainer wmc = arkCrudContainerVo.getDetailPanelFormContainer();
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

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode.
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target)
	{
		arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVo.getEditButtonContainer().setVisible(true);

		target.addComponent(arkCrudContainerVo.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVo.getDetailPanelFormContainer());
		target.addComponent(arkCrudContainerVo.getEditButtonContainer());
	}

	protected void disableModalDetailForm(Long sessionId, String errorMessage)
	{
		if (sessionId == null)
		{
			arkCrudContainerVo.getDetailPanelContainer().setEnabled(false);
			this.error(errorMessage);
		}
		else
		{
			arkCrudContainerVo.getDetailPanelContainer().setEnabled(true);
		}
	}

	protected void disableModalDetailForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVo)
	{
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (!securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) && !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE)
				&& !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ) && !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE))
		{

			arkCrudContainerVo.getDetailPanelContainer().setEnabled(false);
			this.error("You do not have the required security privileges to work with this function. Please see your Administrator.");
		}
		else
		{

			if (sessionId == null)
			{
				arkCrudContainerVo.getDetailPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else
			{
				arkCrudContainerVo.getDetailPanelContainer().setEnabled(true);
			}
		}
	}
	
	abstract protected void attachValidators();

	abstract protected void onSave(AjaxRequestTarget target);
	
	abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form);

	abstract protected void processErrors(AjaxRequestTarget target);

	abstract protected boolean isNew();
}