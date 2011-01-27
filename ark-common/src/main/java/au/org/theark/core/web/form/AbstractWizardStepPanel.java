package au.org.theark.core.web.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class AbstractWizardStepPanel extends Panel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2982993968381162494L;

	protected AbstractWizardStepPanel	previous;

	protected AbstractWizardStepPanel	next;

	public AbstractWizardStepPanel(String id)
	{
		super(id);
	}

	public AbstractWizardStepPanel(String id, IModel model)
	{
		super(id, model);
	}

	public void setNextStep(AbstractWizardStepPanel next)
	{
		this.next = next;
	}

	public AbstractWizardStepPanel getNextStep()
	{
		return next;
	}

	public void setPreviousStep(AbstractWizardStepPanel previous)
	{
		this.previous = previous;
	}

	public AbstractWizardStepPanel getPreviousStep()
	{
		return previous;
	}

	protected void setContent(AjaxRequestTarget target, Component content)
	{
		if (!content.getId().equals(getContentId()))
			throw new IllegalArgumentException("Expected content id is " + getContentId() + " but " + content.getId() + " was found.");

		Component current = get(getContentId());
		if (current == null)
		{
			add(content);
		}
		else
		{
			current.replaceWith(content);
			if (target != null)
			{
				target.addComponent(get(getContentId()));
			}
		}

	}

	/**
	 * Called when "next" button submit the current step form to go to next step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{

	}

	/**
	 * Called when "previous" button was pressed to leave this step by going to previous step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepOutPrevious(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
	}

	/**
	 * Called when "next" button was pressed to go to this step coming from previous step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		onPageStep(target);
	}

	/**
	 * Called when "previous" button was pressed to go to this step coming from next step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepInPrevious(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		onPageStep(target);
	}

	/**
	 * Called when "next" button submit the current step form to go to next step, and it fails.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepOutNextError(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
	}

	/**
	 * Call this after page step previous / next occured.
	 * 
	 * @param target
	 */
	protected void onPageStep(AjaxRequestTarget target)
	{
		if (target != null)
		{
			target.appendJavascript("Resizer.resizeWizard();");
		}
	}

	public abstract void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target);

	public static String getContentId()
	{
		return "panel";
	}

	public static String getTitleId()
	{
		return "title";
	}

}
