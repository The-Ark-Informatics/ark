package au.org.theark.report.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.report.web.Constants;

@SuppressWarnings("serial")
public class ReportTabProviderImpl extends Panel implements
		IMainTabProvider {

	private static final long serialVersionUID = 1L;
	private WebMarkupContainer arkContextPanelMarkup;
	private List<ITab> moduleTabsList;

	public ReportTabProviderImpl(String panelId) {
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs() {
		// Main tab
		ITab iTab = createTab(Constants.REPORT_MAIN_TAB);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public List<ITab> buildTabs(WebMarkupContainer arkContextPanelMarkup) {
		this.arkContextPanelMarkup = arkContextPanelMarkup;

		// Main tab
		ITab iTab = createTab(Constants.REPORT_MAIN_TAB);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(String tabName) {
		return new AbstractTab(new Model<String>(tabName)) {
			@Override
			public Panel getPanel(String pid) {
				// The sub menu(s) for Reporting
				return new ReportSubMenuTab(pid, arkContextPanelMarkup);
			}
		};
	}
}