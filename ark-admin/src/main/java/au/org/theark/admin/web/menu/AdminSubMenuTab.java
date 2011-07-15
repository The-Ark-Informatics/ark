package au.org.theark.admin.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.web.component.function.FunctionContainerPanel;
import au.org.theark.admin.web.component.module.ModuleContainerPanel;
import au.org.theark.admin.web.component.rolePolicy.RolePolicyContainerPanel;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkAjaxTabbedPanel;
import au.org.theark.core.web.component.menu.AbstractArkTabPanel;

public class AdminSubMenuTab extends AbstractArkTabPanel {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 2808674930679468072L;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	private List<ITab>					moduleSubTabsList	= new ArrayList<ITab>();

	public AdminSubMenuTab(String id) {
		super(id);
		buildTabs();
	}

	public void buildTabs() {
		ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_ADMIN);
		List<ArkFunction> arkFunctionList = iArkCommonService.getModuleFunction(arkModule);

		// TODO: Shoud admin sub-menus really access the database to determine their visibility?
		for (final ArkFunction arkFunction : arkFunctionList) {
			AbstractTab tab = new AbstractTab(new StringResourceModel(arkFunction.getResourceKey(), this, null)) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= 5972079308171290188L;

				@Override
				public Panel getPanel(final String panelId) {
					return panelToReturn(arkFunction, panelId);
				}
			};
			moduleSubTabsList.add(tab);
		}

		ArkAjaxTabbedPanel moduleTabbedPanel = new ArkAjaxTabbedPanel("adminSubMenus", moduleSubTabsList);
		add(moduleTabbedPanel);
	}

	protected Panel panelToReturn(ArkFunction arkFunction, String panelId) {
		Panel panelToReturn = null;

		// Clear cache to determine permissions
		processAuthorizationCache(au.org.theark.core.Constants.ARK_MODULE_ADMIN, arkFunction);

		if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_MODULE)) {
			ModuleContainerPanel containerPanel = new ModuleContainerPanel(panelId);
			panelToReturn = containerPanel;
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FUNCTION)) {
			FunctionContainerPanel containerPanel = new FunctionContainerPanel(panelId);
			panelToReturn = containerPanel;
		}
		else if (arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_ROLE_POLICY_TEMPLATE)) {
			RolePolicyContainerPanel containerPanel = new RolePolicyContainerPanel(panelId);
			panelToReturn = containerPanel;
		}

		return panelToReturn;
	}
}
