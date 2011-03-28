package au.org.theark.core.web.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.MarkupComponentBorder;
import org.apache.wicket.markup.html.form.FormComponent;

public class ArkRequiredBorder extends MarkupComponentBorder
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1568367562733328792L;

	public void renderAfter(Component component)
	{
		try
		{
			FormComponent<?> fc = (FormComponent<?>) component;
			if (fc.isRequired())
			{
				super.renderAfter(component);
			}
		}
		catch (ClassCastException cce)
		{
			// ignore non FormComponent Objects
		}
	}
}
