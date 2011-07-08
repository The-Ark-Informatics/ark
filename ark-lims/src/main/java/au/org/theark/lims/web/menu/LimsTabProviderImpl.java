package au.org.theark.lims.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

public class LimsTabProviderImpl extends Panel implements IMainTabProvider
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2064073261192985087L;
	private transient static Logger	log	= LoggerFactory.getLogger(LimsTabProviderImpl.class);
	private WebMarkupContainer			arkContextPanelMarkup;
	private List<ITab>					moduleTabsList;

	public LimsTabProviderImpl(String panelId)
	{
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}
	
	public List<ITab> buildTabs(WebMarkupContainer arkContextPanelMarkup)
	{
		this.arkContextPanelMarkup = arkContextPanelMarkup;

		// Forms the Main Top level Tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_LIMS);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(final String tabName)
	{
		return new ArkMainTab(new Model<String>(tabName))
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 4461043265879777714L;

			@Override
			public Panel getPanel(String pid)
			{
				// The sub menu(s)
				return new LimsSubMenuTab(pid, arkContextPanelMarkup);
			}

			public boolean isAccessible()
			{
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (sessionStudyId == null)
				{
					this.getPanel(au.org.theark.core.Constants.ARK_MODULE_LIMS).error(au.org.theark.core.Constants.NO_STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}

			public boolean isVisible()
			{
				return ArkPermissionHelper.isModuleAccessPermitted(au.org.theark.core.Constants.ARK_MODULE_LIMS);
			}
		};
	}

	/**
	 * @param log the log to set
	 */
	public static void setLog(Logger log)
	{
		LimsTabProviderImpl.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return log;
	}
}