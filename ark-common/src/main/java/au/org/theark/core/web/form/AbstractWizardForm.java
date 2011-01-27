package au.org.theark.core.web.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Abstract class for Wizard Form sub-classes. It provides some common functionality that sub-classes inherit. Provides the skeleton methods for
 * onSave,onDelete,onCancel etc.Defines the core buttons like save,delete,cancel, edit and editCancel. Provides method to toggle the view from read
 * only to edit mode which is usually a common behavior the sub-classes can re-use.
 * </p>
 * 
 * @author nivedann
 * @param <T>
 * 
 */
public abstract class AbstractWizardForm<T> extends Form<T>
{

	private static final long		serialVersionUID	= 1L;

	private static final Logger	log					= LoggerFactory.getLogger(AbstractWizardForm.class);

	protected WebMarkupContainer	resultListContainer;
	protected WebMarkupContainer	wizardPanelContainer;
	protected WebMarkupContainer	searchPanelContainer;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;
	protected WebMarkupContainer	wizardPanelFormContainer;
	protected FeedbackPanel			feedBackPanel;
	protected Form<T>					containerForm;

	protected AjaxButton				saveButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				deleteButton;
	protected AjaxButton				editButton;
	protected AjaxButton				editCancelButton;

	protected ModalWindow			selectModalWindow;

	protected IBehavior				buttonStyleBehavior;

	/**
	 * Indicates whether the action was cancelled or not. TODO: should probably go in an Action interface or abstract class of some sort
	 */
	private boolean					cancelled			= false;

	public AbstractWizardForm(String id)
	{
		this(id, null);
	}

	public AbstractWizardForm(String id, IModel model)
	{
		super(id, model);

		buttonStyleBehavior = new AttributeAppender("class", new Model("ui-corner-all"), " ");
		setOutputMarkupId(true);
		setMultiPart(true);
		initialiseForm();
	}

	/**
	 * Constructor for AbstractDetailForm class
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param wizardPanelContainer
	 * @param wizardPanelFormContainer
	 * @param searchPanelContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param containerForm
	 */
	public AbstractWizardForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer resultListContainer, WebMarkupContainer wizardPanelContainer, WebMarkupContainer wizardPanelFormContainer,
			WebMarkupContainer searchPanelContainer, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, Form<T> containerForm)
	{
		super(id);
		this.resultListContainer = resultListContainer;
		this.wizardPanelContainer = wizardPanelContainer;
		this.feedBackPanel = feedBackPanel;
		this.searchPanelContainer = searchPanelContainer;
		this.editButtonContainer = editButtonContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.wizardPanelFormContainer = wizardPanelFormContainer;
		this.containerForm = containerForm;

		buttonStyleBehavior = new AttributeAppender("class", new Model("ui-corner-all"), " ");
		setOutputMarkupId(true);
		setMultiPart(true);
		initialiseForm();
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

	// abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	abstract protected void processErrors(AjaxRequestTarget target);

	protected void onCancelPostProcess(AjaxRequestTarget target)
	{
		searchPanelContainer.setVisible(true);
		wizardPanelContainer.setVisible(false);
		resultListContainer.setVisible(true);

		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(wizardPanelContainer);
		target.addComponent(resultListContainer);
	}

	@SuppressWarnings( { "serial", "unchecked" })
	protected void initialiseForm()
	{
		// finish button
		AjaxButton finish = createFinish();
		finish.add(buttonStyleBehavior);
		finish.setVisible(false);
		finish.setOutputMarkupId(true);
		finish.setOutputMarkupPlaceholderTag(true);
		add(finish);

		// previous button
		AjaxLink link = createPrevious();
		link.setVisible(false);
		link.setOutputMarkupId(true);
		link.setOutputMarkupPlaceholderTag(true);
		link.add(buttonStyleBehavior);
		add(link);

		// next button
		AjaxButton button = createNext();
		button.setOutputMarkupId(true);
		button.setOutputMarkupPlaceholderTag(true);
		button.add(buttonStyleBehavior);
		add(button);

		// cancel button
		AjaxLink cancelLink = createCancel();
		cancelLink.add(buttonStyleBehavior);
		add(cancelLink);
	}

	private AjaxButton createNext()
	{
		AjaxButton button = new AjaxButton("nextLink",  new StringResourceModel("cancelKey", this, null))
		{
			private static final long	serialVersionUID	= 0L;


			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				onNextError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// TODO Auto-generated method stub
				onNextSubmit(target, form);
			}

		};
		button.add(new AttributeModifier("value", true, getLabelModel("Next")));

		return button;
	}

	private AjaxLink createPrevious()
	{
		AjaxLink link = new AjaxLink("previousLink")
		{
			private static final long	serialVersionUID	= 0L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onPreviousClick(target);
			}

		};
		link.add(new AttributeModifier("value", true, getLabelModel("Previous")));

		return link;
	}

	private AjaxButton createFinish()
	{
		AjaxButton finish = new AjaxButton("finish", this)
		{

			private static final long	serialVersionUID	= 0L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onFinishSubmit(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				onFinishError(target, form);
			}

		};
		finish.add(new AttributeModifier("value", true, getLabelModel("Finish")));

		return finish;
	}

	private AjaxLink createCancel()
	{
		AjaxLink link = new AjaxLink("cancelLink")
		{
			private static final long	serialVersionUID	= 0L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onCancelClick(target);
			}

		};
		link.add(new AttributeModifier("value", true, getLabelModel("Cancel")));

		return link;
	}

	@SuppressWarnings("unchecked")
	public LoadableDetachableModel getLabelModel(String label)
	{
		return new StringResourceModel(label, AbstractWizardForm.this, null);
	}

	//
	// Event form triggers
	// 

	protected void onFinishSubmit(AjaxRequestTarget target, Form<?> form)
	{
		log.debug("finish.onSubmit");
		onFinish(target, form);
	}

	protected void onFinishError(AjaxRequestTarget target, Form<?> form)
	{
		log.debug("finish.onError");
		// if (getFeedbackWindow() != null)
		// showFeedbackWindow(target);
		AbstractWizardForm.this.onError(target, form);
		//target.appendJavascript("Resizer.resizeWizard();");
	}

	protected void onPreviousClick(AjaxRequestTarget target)
	{
		HistoryAjaxBehavior historyAjaxBehavior = getHistoryAjaxBehavior();
		if (historyAjaxBehavior != null)
		{
			historyAjaxBehavior.registerAjaxEvent(target, this);
		}
		AbstractWizardForm.this.gotoPrevious(target);
	}

	protected void onNextSubmit(AjaxRequestTarget target, Form<?> form)
	{
		log.debug("next.onSubmit");
		HistoryAjaxBehavior historyAjaxBehavior = getHistoryAjaxBehavior();
		if (historyAjaxBehavior != null)
		{
			historyAjaxBehavior.registerAjaxEvent(target, this);
		}
		AbstractWizardForm.this.gotoNext(target);
	}

	protected void onNextError(AjaxRequestTarget target, Form<?> form)
	{
		log.debug("next.onError");
		// if (getFeedbackWindow() != null)
		// showFeedbackWindow(target);
		AbstractWizardForm.this.onError(target, form);
		AbstractWizardStepPanel currentStep = (AbstractWizardStepPanel) AbstractWizardForm.this.get("step");
		currentStep.onStepOutNextError(AbstractWizardForm.this, target);
		target.appendJavascript("Resizer.resizeWizard();");
	}

	protected void onCancelClick(AjaxRequestTarget target)
	{
		cancelled = true;
		onCancel(target);
	}

	/**
	 * Called after wizard form submission generates an error (on next or finish click).
	 * 
	 * @param target
	 * @param form
	 */
	public abstract void onError(AjaxRequestTarget target, Form form);

	/**
	 * Called when finish is clicked.
	 * 
	 * @param target
	 * @param form
	 */
	public abstract void onFinish(AjaxRequestTarget target, Form form);

	/**
	 * Called when cancel is clicked.
	 * 
	 * @param target
	 */
	public abstract void onCancel(AjaxRequestTarget target);

	/**
	 * Get the "next" component.
	 * 
	 * @return
	 */
	public Component getNextLink()
	{
		return get("nextLink");
	}

	/**
	 * Get the "previous" component.
	 * 
	 * @return
	 */
	public Component getPreviousLink()
	{
		return get("previousLink");
	}

	/**
	 * Get the "finish" component.
	 * 
	 * @return
	 */
	public Component getFinishLink()
	{
		return get("finish");
	}

	/**
	 * Get the "cancel" component.
	 * 
	 * @return
	 */
	public Component getCancelLink()
	{
		return get("cancelLink");
	}

	/**
	 * Warn the current step panel we are going out by next, and ask which is the next step.
	 * 
	 * @param target
	 */
	protected void gotoNext(AjaxRequestTarget target)
	{
		AbstractWizardStepPanel currentStep = (AbstractWizardStepPanel) get("step");
		log.debug("gotoNext.currentStep={}", currentStep.getClass().getName());
		currentStep.onStepOutNext(AbstractWizardForm.this, target);

		AbstractWizardStepPanel next = currentStep.getNextStep();
		if (next != null)
		{
			currentStep.replaceWith(next);
			next.onStepInNext(this, target);
			next.handleWizardState(this, target);
		}
		target.addComponent(this);
	}

	/**
	 * Warn the current step panel we are going out by previous, and ask which is the previous step.
	 * 
	 * @param target
	 */
	protected void gotoPrevious(AjaxRequestTarget target)
	{
		AbstractWizardStepPanel currentStep = (AbstractWizardStepPanel) get("step");
		log.debug("gotoPrevious.currentStep={}", currentStep.getClass().getName());
		currentStep.onStepOutPrevious(AbstractWizardForm.this, target);

		AbstractWizardStepPanel previous = currentStep.getPreviousStep();
		if (previous != null)
		{
			currentStep.replaceWith(previous);
			previous.onStepInPrevious(this, target);
			previous.handleWizardState(this, target);
		}
		target.addComponent(this);
	}

	public boolean isCancelled()
	{
		return cancelled;
	}

	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}

	@SuppressWarnings("unchecked")
	public HistoryAjaxBehavior getHistoryAjaxBehavior()
	{
		// Start here
		Component current = getParent();

		// Walk up containment hierarchy
		while (current != null)
		{
			// Is current an instance of this class?
			if (IHistoryAjaxBehaviorOwner.class.isInstance(current))
			{
				return ((IHistoryAjaxBehaviorOwner) current).getHistoryAjaxBehavior();
			}

			// Check parent
			current = current.getParent();
		}
		return null;
	}

	// protected void showFeedbackWindow(AjaxRequestTarget target)
	// {
	// getFeedbackWindow().setContent(new FeedbackPanel("content"));
	// getFeedbackWindow().show(target);
	// }
	//
	// /**
	// * Accessor to the feedback panel if any.
	// *
	// * @return null by default.
	// */
	// public FeedbackWindow getFeedbackWindow()
	// {
	// return null;
	// }

	public static String getStepId()
	{
		return "step";
	}

	@SuppressWarnings("unchecked")
	public void changeWizardFormStyle(String cssClassName)
	{
		add(new AttributeModifier("class", new Model(cssClassName)));
	}
}
