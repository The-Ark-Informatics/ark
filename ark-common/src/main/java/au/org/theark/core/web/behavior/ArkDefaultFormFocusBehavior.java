package au.org.theark.core.web.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class ArkDefaultFormFocusBehavior extends AbstractBehavior
{
    /**
	 * 
	 */
	private static final long	serialVersionUID	= 8530926132995921828L;
	private Component component;

    public void bind( Component component )
    {
        this.component = component;
        component.setOutputMarkupId(true);
    }

    public void renderHead( IHeaderResponse iHeaderResponse )
    {
        super.renderHead(iHeaderResponse);
        iHeaderResponse.renderOnLoadJavascript("document.getElementById('" + component.getMarkupId() + "').focus();");
    }
}