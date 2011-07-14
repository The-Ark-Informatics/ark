package au.org.theark.core.web.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.Response;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.form.FormComponent;

public class ArkRequiredFieldHintBehavior extends AbstractBehavior
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2695428312191631705L;

	/**
	 * @see org.apache.wicket.behavior.AbstractBehavior#onRendered(org.apache.wicket.Component)
	 */
	@Override
	public void onRendered(Component component)
	{
		try
		{
			FormComponent<?> fc = (FormComponent<?>) component;
			if (fc.isRequired())
			{
				super.onRendered(component);
				// Append the span and img icon right after the rendering of the
				// component. Not as pretty as working with a panel etc, but works
				// for behaviors and is more efficient
				Response response = component.getResponse();
				response.write("<span class=\"requiredHint\">*</span>");
			}
		}
		catch (ClassCastException cce)
		{
			// ignore non FormComponent Objects
		}
	}
}
