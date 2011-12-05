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

import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * <p>
 * Abstract class for the SubContainer panels that may contain any number of panels
 * </p>
 * 
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractSubContainerPanel<T> extends Panel {

	@SpringBean(name = "arkLdapRealm")
	private ArkLdapRealm						realm;

	protected FeedbackPanel					feedbackPanel;

	protected ArkCrudContainerVO			arkCrudContainerVO;

	protected IModel<Object>				iModel;
	protected CompoundPropertyModel<T>	cpModel;

	protected PageableListView<T>			myListView;

	/**
	 * @param id
	 */
	public AbstractSubContainerPanel(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		Subject currentUser = SecurityUtils.getSubject();
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
		currentUser.hasRole("Administrator");
	}

	public AbstractSubContainerPanel(String id, boolean flag) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		Subject currentUser = SecurityUtils.getSubject();
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());// TODO(NN) Uncomment after the User management usecase is complete
	}

	public void initCrudContainerVO() {
		arkCrudContainerVO = new ArkCrudContainerVO();
	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}

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
