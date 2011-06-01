package au.org.theark.core.web.component;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class ArkAjaxTabbedPanel extends AjaxTabbedPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	protected String setBusyIndicatorOn = "document.getElementById('busyIndicator').style.display ='inline'; " +
	"overlay = document.getElementById('overlay'); " +
	"overlay.style.visibility = 'visible';";

	protected String setBusyIndicatorOff = "document.getElementById('busyIndicator').style.display ='none'; " +
	 "overlay = document.getElementById('overlay'); " +
	 "overlay.style.visibility = 'hidden';";
	
	public ArkAjaxTabbedPanel(String id, List<ITab> tabs)
	{
		super(id, tabs);
	}
	
	protected WebMarkupContainer newLink(String linkId, final int index)
	{
		return new AjaxFallbackLink(linkId)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				
				if(sessionStudyId != null)
				{
					// move tabs...
					setSelectedTab(index);	
				}
				else
				{
					// require a study...
					this.error("There is no study in context. Please select a study");
				}
				
				if (target != null)
				{
					target.addComponent(ArkAjaxTabbedPanel.this);
				}
				onAjaxUpdate(target);
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
		};
	}
}
