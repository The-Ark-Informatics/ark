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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;

public class ArkAjaxTabbedPanel extends AjaxTabbedPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private transient Logger	log = LoggerFactory.getLogger(ArkAjaxTabbedPanel.class);
	private int numberOfTabs = 0;
	private boolean requireStudyInSession = true;
	
	protected String setBusyIndicatorOn = "document.getElementById('busyIndicator').style.display ='inline'; " +
	"overlay = document.getElementById('overlay'); " +
	"overlay.style.visibility = 'visible';";

	protected String setBusyIndicatorOff = "document.getElementById('busyIndicator').style.display ='none'; " +
	 "overlay = document.getElementById('overlay'); " +
	 "overlay.style.visibility = 'hidden';";
	
	public ArkAjaxTabbedPanel(String id, List<ITab> tabs)
	{
		super(id, tabs);
		this.numberOfTabs = tabs.size();
	}
	
	public ArkAjaxTabbedPanel(String id, List<ITab> tabs, boolean requireStudyInSession)
	{
		super(id, tabs);
		this.requireStudyInSession = requireStudyInSession;
		this.numberOfTabs = tabs.size()-1;
		
		if(requireStudyInSession)
			log.info("The tab with id:" + id + " require a Study in session.");
		else
			log.info("The tab with id:" + id + " does not require a Study in session.");
	}
	
	@SuppressWarnings("rawtypes")
	protected WebMarkupContainer newLink(final String linkId, final int index)
	{
		return new AjaxFallbackLink(linkId)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				int indexOfTab = index;
				int numOfTabs = numberOfTabs;
				
				//TODO: Amend to be more generalised (ie work out how to set whether study required or not from parent tab)
				// Study required for tabs except first (Study) and last (Reporting)
				if(sessionStudyId != null || indexOfTab == 0 || indexOfTab+1 == numOfTabs)
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

	/**
	 * @param requireStudyInSession the requireStudyInSession to set
	 */
	public void setRequireStudyInSession(boolean requireStudyInSession)
	{
		this.requireStudyInSession = requireStudyInSession;
	}

	/**
	 * @return the requireStudyInSession
	 */
	public boolean isRequireStudyInSession()
	{
		return requireStudyInSession;
	}
}
