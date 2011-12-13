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

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.ActivityMonitorVO;
import au.org.theark.core.session.SessionAttributeListener;
import au.org.theark.core.vo.ArkSubjectVO;

/**
 * @author cellis
 * 
 */
public class ActivityMonitorContainerPanel extends Panel {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -7123740564278141980L;
	protected transient Logger					log					= LoggerFactory.getLogger(ActivityMonitorContainerPanel.class);
	protected FeedbackPanel						feedBackPanel;
	private Form<ActivityMonitorVO>			form;
	private SearchResultsPanel					searchResultsPanel;
	private PageableListView<ArkSubjectVO>	listView;
	private Button									refresh;

	/**
	 * @param id
	 */
	public ActivityMonitorContainerPanel(String id) {
		super(id);
		form = new Form<ActivityMonitorVO>("form", new CompoundPropertyModel<ActivityMonitorVO>(new ActivityMonitorVO()));
		form.add(initialiseFeedBackPanel());
		form.add(initialiseSearchResults());

		refresh = new AjaxButton("refresh") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("Error on refresh click");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.add(searchResultsPanel);
			}
		};
		refresh.setDefaultFormProcessing(false);
		form.add(refresh);
		add(form);
	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedBackPanel = new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}

	@SuppressWarnings("unchecked")
	protected WebMarkupContainer initialiseSearchResults() {
		List<ArkSubjectVO> activeUsers = SessionAttributeListener.getActiveUsers();
		form.getModelObject().setActiveUsers(activeUsers);
		searchResultsPanel = new SearchResultsPanel("searchResultsPanel", feedBackPanel);
		listView = searchResultsPanel.buildPageableListView(new PropertyModel(form.getModelObject(), "activeUsers"));
		searchResultsPanel.add(listView);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultsPanel.add(pageNavigator);
		return searchResultsPanel;
	}
}
