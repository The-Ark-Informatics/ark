/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.web.component;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.security.AAFRealm;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * <p>
 * Abstract class for the Container panels that contains Search,SearchResult and Detail panels. Defines the WebMarkupContainers and initialises
 * them.It also defines the Model CompoundPropertyModel and provides methods that the sub-classes must implement such as
 * initialiseSearchResults,initialiseDetailPanel and initialiseSearchPanel()
 * </p>
 * 
 * @author nivedann
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractContainerPanel<T> extends Panel {

	@SpringBean(name = "arkLdapRealm")
	protected ArkLdapRealm					arkLdapRealm;

	@SpringBean(name = "aafRealm")
	protected AAFRealm						aafRealm;

	protected FeedbackPanel					feedBackPanel;
	protected ArkCrudContainerVO			arkCrudContainerVO;
	protected IModel<Object>				iModel;
	protected CompoundPropertyModel<T>	cpModel;

	protected PageableListView<T>			myListView;

	/**
	 * 
	 * @param id
	 */
	public AbstractContainerPanel(String id) {
		super(id);
		Subject currentUser = SecurityUtils.getSubject();
		arkLdapRealm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
		aafRealm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
		initCrudContainerVO();
	}

	public void initCrudContainerVO() {
		arkCrudContainerVO = new ArkCrudContainerVO();

	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedBackPanel = new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}

	protected abstract WebMarkupContainer initialiseSearchResults();

	protected abstract WebMarkupContainer initialiseDetailPanel();

	// protected abstract WebMarkupContainer initialiseWizardPanel();

	protected abstract WebMarkupContainer initialiseSearchPanel();

	protected boolean isActionPermitted() {
		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ)) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}

}
