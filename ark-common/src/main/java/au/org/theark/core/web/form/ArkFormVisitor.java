package au.org.theark.core.web.form;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;

import au.org.theark.core.web.component.ArkErrorHighlightBehaviour;
import au.org.theark.core.web.component.ArkRequiredBorder;

@SuppressWarnings("rawtypes")
public class ArkFormVisitor implements IVisitor, Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1613309540699904032L;
	Set	visited	= new HashSet();

	@SuppressWarnings({ "unchecked", "deprecation" })
	public Object component(Component c)
	{
		if (!visited.contains(c))
		{
			visited.add(c);
			//TODO: Implement using non deprecated method
			c.setComponentBorder(new ArkRequiredBorder());
			//TODO: Implement message validation neater
			//c.add(new ArkValidationMsgBehaviour());
			//TODO: Implement error highlighting
			//c.add(new ArkErrorHighlightBehaviour());
		}
		return IVisitor.CONTINUE_TRAVERSAL;
	}
}