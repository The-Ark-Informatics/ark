package au.org.theark.report.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

@SuppressWarnings("serial")
public class ReportTabProviderImpl extends Panel implements IMainTabProvider
{

	private static final long	serialVersionUID	= 1L;
	private List<ITab>			moduleTabsList;

	public ReportTabProviderImpl(String panelId)
	{
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs()
	{
		// Main tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_REPORTING);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(String tabName)
	{
		return new ArkMainTab(new Model<String>(tabName))
		{
			@Override
			public Panel getPanel(String pid)
			{
				// The sub menu(s) for Reporting
				return new ReportSubMenuTab(pid);
			}

			public boolean isAccessible()
			{
				return true;
			}

			public boolean isVisible()
			{
				return true;
			}
		};
	}
}