package au.org.theark.core.web.form;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.markup.html.WebMarkupContainer;

import au.org.theark.core.web.component.ArkErrorHighlightBehaviour;
import au.org.theark.core.web.component.ArkRequiredBorder;
import au.org.theark.core.web.component.ArkValidationMsgBehaviour;

public class ArkFormVisitor implements IVisitor, Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1613309540699904032L;
	Set	visited	= new HashSet();

	public Object component(Component c)
	{
		if (!visited.contains(c))
		{
			visited.add(c);
			c.setComponentBorder(new ArkRequiredBorder());
			c.add(new ArkValidationMsgBehaviour());
			c.add(new ArkErrorHighlightBehaviour());
		}
		return IVisitor.CONTINUE_TRAVERSAL;
	}
}
