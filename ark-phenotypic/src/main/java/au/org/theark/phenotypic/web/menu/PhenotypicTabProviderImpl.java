package au.org.theark.phenotypic.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IMainTabProvider;

@SuppressWarnings("unchecked")
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
		ITab tab1 = createTab("Phenotypic");// Forms the Main Top level Tab
		moduleTabsList.add(tab1);
		return moduleTabsList;
	}

	public ITab createTab(String tabName)
	{
		// TODO Auto-generated method stub
		return new AbstractTab(new Model(tabName))
		{

			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible()
			{
				return true;
				// ArkSecurityManager asm = ArkSecurityManager.getInstance();
				// return asm.subjectHasRole(RoleConstants.ARK_SUPER_ADMIN);
			}

			@Override
			public Panel getPanel(String pid)
			{
				log.info("Panel ID in getPanel " + pid);
				return new PhenotypicSubMenuTab(pid);// The sub menus Study
			}

		};
	}
}
