package au.org.theark.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

public class GenomicsTabProviderImpl extends Panel implements IMainTabProvider {

	private static final long	serialVersionUID	= 1L;
	private List<ITab>			moduleTabsList;

	public GenomicsTabProviderImpl(String panelId) {
		super(panelId);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs() {
		// Main tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_GENOMICS);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(String tabName) {
		return new ArkMainTab(new Model<String>(tabName)) {
			@Override
			public Panel getPanel(String pid) {
				// The sub menu(s) for Reporting
				return new GenomicsSubMenuTab(pid);
			}

			public boolean isAccessible() {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (sessionStudyId == null) {
					this.getPanel(au.org.theark.core.Constants.ARK_MODULE_GENOMICS).error(au.org.theark.core.Constants.NO_STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}

			public boolean isVisible() {
				return ArkPermissionHelper.isModuleAccessPermitted(au.org.theark.core.Constants.ARK_MODULE_GENOMICS);
			}
		};
	}

}
