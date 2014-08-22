package au.org.theark.web.menu;

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

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

public class DiseaseTabProviderImpl extends Panel implements IMainTabProvider {

	private static final long serialVersionUID = 1L;

	private WebMarkupContainer			arkContextPanelMarkup;
	private List<ITab>					moduleTabsList;
	boolean visible;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	private transient static Logger log = LoggerFactory.getLogger(DiseaseTabProviderImpl.class);

	public DiseaseTabProviderImpl(String id) {
		super(id);
		moduleTabsList = new ArrayList<ITab>();
	}

	@Override
	public boolean isVisible() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		return sessionStudyId != null;
	}

	public List<ITab> buildTabs(WebMarkupContainer arkContextPanelMarkup) {
		this.arkContextPanelMarkup = arkContextPanelMarkup;

		// Forms the Main Top level Tab
		ITab iTab = createTab(au.org.theark.core.Constants.ARK_MODULE_DISEASE);
		moduleTabsList.add(iTab);

		return moduleTabsList;
	}

	public ITab createTab(final String tabName) {
		return new ArkMainTab(new Model<String>(tabName)) {

			private static final long	serialVersionUID	= 1L;

			@Override
			public Panel getPanel(String pid) {
				// The sub menu(s)
				return new DiseaseSubMenuTab(pid, arkContextPanelMarkup);
			}

			public boolean isAccessible() {
				return true;
			}

			public boolean isVisible() {
				return ArkPermissionHelper.isModuleAccessPermitted(au.org.theark.core.Constants.ARK_MODULE_LIMS);
			}
		};
	}

	/**
	 * @param log
	 *           the log to set
	 */
	public static void setLog(Logger log) {
		DiseaseTabProviderImpl.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}
}
