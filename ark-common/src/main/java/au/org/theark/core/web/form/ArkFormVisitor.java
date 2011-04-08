package au.org.theark.core.web.form;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.markup.html.form.FormComponent;

import au.org.theark.core.web.behavior.ArkRequiredFieldHintBehavior;

@SuppressWarnings("unchecked")
public class ArkFormVisitor implements IVisitor, Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1613309540699904032L;
	Set<Component>					visited				= new HashSet<Component>();

	public Object component(Component component)
	{
		if (!visited.contains(component))
		{
			visited.add(component);
			if (component instanceof FormComponent)
			{
				// Force id's in HTML to allow for target.focusCompont to focus on fieds in error
				component.setOutputMarkupId(true);
				// Add a "*" after the required fields
				component.add(new ArkRequiredFieldHintBehavior());
			}
		}
		return IVisitor.CONTINUE_TRAVERSAL;
	}
}