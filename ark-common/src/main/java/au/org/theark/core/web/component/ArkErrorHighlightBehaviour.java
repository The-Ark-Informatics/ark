package au.org.theark.core.web.component;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

public class ArkErrorHighlightBehaviour extends AbstractBehavior
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5544350240467988658L;

	public void onComponentTag(Component c, ComponentTag tag)
	{
		try
		{
			FormComponent<?> fc = (FormComponent<?>) c;
			if (!fc.isValid())
			{
				tag.put("class", "error");
			}
		}
		catch(ClassCastException cce)
		{
			// ignore non FormComponent Objects
		}
	}
}