package au.org.theark.core.web.component;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;

public abstract class ArkBusyAjaxLink<T> extends AjaxLink<T>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4139168862106185766L;
	private String setBusyIndicatorOn = "document.getElementById('busyIndicator').style.display ='inline'; " +
	"overlay = document.getElementById('overlay'); " +
	"overlay.style.visibility = 'visible';";

	private String setBusyIndicatorOff = "document.getElementById('busyIndicator').style.display ='none'; " +
	 "overlay = document.getElementById('overlay'); " +
	 "overlay.style.visibility = 'hidden';";

	public ArkBusyAjaxLink(String id)
	{
		super(id);
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
	}
	
	@Override
   protected IAjaxCallDecorator getAjaxCallDecorator() {
       return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
           private static final long serialVersionUID = 1L;

           @Override
           public CharSequence postDecorateScript(CharSequence script) {
           		return script + setBusyIndicatorOn;
           }
           
           @Override
           public CharSequence postDecorateOnFailureScript(CharSequence script) {
           		return script + setBusyIndicatorOff;
           }
           
           @Override
           public CharSequence postDecorateOnSuccessScript(CharSequence script) {
           	return script + setBusyIndicatorOff;
           }
       };
   }
}
