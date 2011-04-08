package au.org.theark.core.web.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

public class ArkVisualErrorBehavior extends AbstractBehavior
{

	/**
	* 
	*/
	private static final long	serialVersionUID	= 3099650037766785119L;

   public void onComponentTag(Component component, ComponentTag tag) {
       super.onComponentTag(component, tag);
       if (component instanceof FormComponent) {
           if (!((FormComponent) component).isValid()) {                                           
               tag.put("style", "border: 1px solid #CC2200;");                                           
           }
       }
   }

}
