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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkSubjectSessionVO;
import au.org.theark.core.vo.ArkUserVO;

public class SearchResultsPanel extends Panel {
	private static final long serialVersionUID = -2656326926648860189L;
	protected transient Logger logger = LoggerFactory.getLogger(SearchResultsPanel.class);
	private FeedbackPanel feedbackPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	public SearchResultsPanel(String id, FeedbackPanel feedbackPanel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.setFeedbackPanel(feedbackPanel);
	}

	@SuppressWarnings("unchecked")
	public PageableListView<ArkSubjectSessionVO> buildPageableListView(IModel iModel) {
		return new PageableListView<ArkSubjectSessionVO>("list", iModel, iArkCommonService.getRowsPerPage()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<ArkSubjectSessionVO> item) {
				final ArkSubjectSessionVO subject = item.getModelObject();
				final String sessionId = subject.getSessionId();
				final String userId = subject.getUserId();
				final String host = subject.getHost();
				final Date startTimestamp = subject.getStartTimestamp();
				final Date lastAccessTime = subject.getLastAccessTime();
				ArkUserVO arkUser = null;
				SimpleDateFormat sdf = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY_HH_MM_SS);
				try {
					arkUser = iArkCommonService.getUser(userId);
				} catch (InvalidSessionException | ArkSystemException | EntityNotFoundException e) {
					logger.error("Failed to fetch Ark User", e);
				}

				item.add(new Label(au.org.theark.core.Constants.ARK_SESSION_ID, sessionId));
				item.add(new Label(au.org.theark.core.Constants.ARK_USERID, userId));
				String userName = "";
				if (arkUser != null) {
					userName = arkUser.getFirstName() + " " + arkUser.getLastName();
				}
				item.add(new Label(au.org.theark.core.Constants.ARK_USER_NAME, userName));
				item.add(new Label(au.org.theark.core.Constants.ARK_HOST, host));
				item.add(new Label(au.org.theark.core.Constants.ARK_SESSION_START_TIMESTAMP, sdf.format(startTimestamp)));
				item.add(new Label(au.org.theark.core.Constants.ARK_SESSION_LAST_ACCESS_TIME, sdf.format(lastAccessTime)));

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1938679383897533820L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
	}

	public void setFeedbackPanel(FeedbackPanel feedbackPanel) {
		this.feedbackPanel = feedbackPanel;
	}

	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}
}