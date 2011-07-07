package au.org.theark.lims.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;
import au.org.theark.lims.web.Constants;

public class LimsTabProviderImpl extends Panel implements IMainTabProvider
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2064073261192985087L;
	private transient static Logger	log	= LoggerFactory.getLogger(LimsTabProviderImpl.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	private WebMarkupContainer			arkContextPanelMarkup;
	private List<ITab>					moduleTabsList;

	public LimsTabProviderImpl(String panelId)
	{
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs()
	{
		log.info("Creating main tab: " + au.org.theark.core.Constants.ARK_MODULE_LIMS);
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_LIMS);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public List<ITab> buildTabs(WebMarkupContainer arkContextPanelMarkup)
	{
		this.arkContextPanelMarkup = arkContextPanelMarkup;

		log.info("Creating main tab: " + au.org.theark.core.Constants.ARK_MODULE_LIMS);
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
				// The sub menu(s) for Phenotypic
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
				ArkModule arkModule = iArkCommonService.getArkModuleByName(tabName);
				return ArkPermissionHelper.isModuleAccessPermitted(arkModule);
			}
		};
	}
}