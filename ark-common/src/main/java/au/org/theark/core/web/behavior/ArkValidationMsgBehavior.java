package au.org.theark.core.web.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.form.FormComponent;

public class ArkValidationMsgBehavior extends AbstractBehavior
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2591693136424670884L;

	public void onRendered(Component c)
	{
		try
		{
			FormComponent<?> fc = (FormComponent<?>) c;
			if (!fc.isValid())
			{
				String error;
				if (fc.hasFeedbackMessage())
				{
					error = fc.getFeedbackMessage().getMessage().toString();
				}
				else
				{
					error = "Your input is invalid.";
				}
				fc.getResponse().write("<div class=\"validationMsg\">" + error + "</div>");
			}
		}
		catch (ClassCastException cce)
		{
			// ignore non FormComponent Objects
		}
	}
}
