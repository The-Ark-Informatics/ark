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
package au.org.theark.admin.web.component.activitymonitor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.link.AjaxConfirmLink;

public class SearchResultsPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2656326926648860189L;
	protected transient Logger		log					= LoggerFactory.getLogger(SearchResultsPanel.class);
	private FeedbackPanel feedbackPanel;

	public SearchResultsPanel(String id, FeedbackPanel feedbackPanel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.feedbackPanel = feedbackPanel;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<Subject> buildPageableListView(IModel iModel) {
		PageableListView<Subject> pageableListView = new PageableListView<Subject>("list", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Subject> item) {
				final Subject subject  = (Subject) item.getModelObject();
				String sessionId = subject.getSession().getId().toString();
				String userId = subject.getPrincipal().toString();
				String host = subject.getSession().getHost();
				
				item.add(new Label(au.org.theark.core.Constants.ARK_SESSION_ID, sessionId));
				item.add(new Label(au.org.theark.core.Constants.ARK_USERID, userId));
				item.add(new Label(au.org.theark.core.Constants.ARK_HOST, host));
			
				AjaxConfirmLink<Subject> logoutLink = new AjaxConfirmLink<Subject>("logoutLink", new StringResourceModel("confirmLogout", this, item.getModel(), new Object[] {userId}), item.getModel()) {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						String sessionId = subject.getSession().getId().toString();
						String userId = subject.getPrincipal().toString();
						this.info("Successfully logged out UserID: " + userId);
												
						// Trigger an event, then log off selected user 
						SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_LOGOFF_SESSION_ID, sessionId);
						SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.ARK_LOGOFF_SESSION_ID);
						subject.logout();
						
						target.add(feedbackPanel);
						target.add(SearchResultsPanel.this);
					}

				};
				logoutLink.setOutputMarkupPlaceholderTag(true);
				
				// hide logout link for actual current user
				if(SecurityUtils.getSubject().getSession().getId().equals(subject.getSession().getId())) {
					logoutLink.setVisible(false);
				}
				item.add(logoutLink);
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1938679383897533820L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return pageableListView;
	}
}
