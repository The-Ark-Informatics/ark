/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import au.org.theark.core.service.IMainTabProvider;
import au.org.theark.core.web.component.ArkMainTab;

/**
 * @author nivedann
 * 
 */
public class RegistryTabProviderImpl extends Panel implements IMainTabProvider {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5905065151035360540L;
	List<ITab>						moduleTabsList;

	/**
	 * @param id
	 */
	public RegistryTabProviderImpl(String id) {
		super(id);
		moduleTabsList = new ArrayList<ITab>();
	}

	public List<ITab> buildTabs() {
		ITab tab1 = createTab(au.org.theark.core.Constants.ARK_MODULE_REGISTRY);
		moduleTabsList.add(tab1);
		return moduleTabsList;
	}

	public ITab createTab(final String tabName) {

		return new ArkMainTab(new Model<String>(tabName)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4616700064526881402L;

			@Override
			public Panel getPanel(String panelId) {
				Panel panelToReturn = null;

				if (tabName.equals(au.org.theark.core.Constants.ARK_MODULE_REGISTRY)) {
					return new RegistrySubMenuTab(panelId);
				}
				return panelToReturn;
			}

			public boolean isAccessible() {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (sessionStudyId == null) {
					this.getPanel(au.org.theark.core.Constants.ARK_MODULE_REGISTRY).error(au.org.theark.core.Constants.NO_STUDY_IN_CONTEXT_MESSAGE);
					return false;
				}
				else
					return true;
			}

			public boolean isVisible() {
				return true;
			}
		};
	}

}
