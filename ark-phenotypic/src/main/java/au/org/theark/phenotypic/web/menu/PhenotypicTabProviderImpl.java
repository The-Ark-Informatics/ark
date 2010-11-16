package au.org.theark.phenotypic.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.phenotypic.web.Constants;

@SuppressWarnings("serial")
public class PhenotypicTabProviderImpl extends Panel implements IMainTabProvider
{

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(PhenotypicTabProviderImpl.class);

	List<ITab>						moduleTabsList;

	public PhenotypicTabProviderImpl(String panelId)
	{
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs()
	{
		// Main tab
		ITab iTab = createTab(Constants.PHENOTYPIC_MAIN_TAB);
		moduleTabsList.add(iTab);
		
		// Hide tab if study not selected
		try
		{
			Subject currentUser = SecurityUtils.getSubject();
			Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

			// TODO: Disable main tab if studyId not selected (in context)
			if (studyId != null)
			{
				log.info("Study selected, tab should be visible");
			}
			else
			{
				log.info("Study NOT selected, tab should NOT be visible");
			}
		}
		catch (Exception ex)
		{
			log.error("PhenotypicSubMenuTab: General exception when determing tab visibility. " + ex.getMessage());
		}
		
		return moduleTabsList;
	}

	public ITab createTab(String tabName)
	{
		return new AbstractTab(new Model<String>(tabName))
		{
			@Override
			public Panel getPanel(String pid)
			{
				// The sub menu(s) for Phenotypic
				return new PhenotypicSubMenuTab(pid);
			}
		};
	}
}