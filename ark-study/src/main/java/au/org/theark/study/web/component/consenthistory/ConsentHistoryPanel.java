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
package au.org.theark.study.web.component.consenthistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

public class ConsentHistoryPanel extends Panel {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService							iStudyService;
	private PageableListView<ConsentHistory>	listView;
	private PagingNavigator							pageNavigator;
	private SimpleDateFormat						simpleDateFormat	= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private SimpleDateFormat						simpleDateTimeFormat	= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY_HH_MM_SS);

	/**
	 * Construct a ConsentHistoryPanel for the linkSubjectStudy in question
	 * 
	 * @param id
	 * @param linkSubjectStudy
	 */
	public ConsentHistoryPanel(String id, IModel<?> model) {
		super(id, model);
		initConsentHistoryPanel();
		addComponents();
	}

	private void initConsentHistoryPanel() {
		IModel<Object> model = new LoadableDetachableModel<Object>() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				List<ConsentHistory> consentHistoryList = new ArrayList<ConsentHistory>();
				LinkSubjectStudy linkSubjectStudy = null;
				try {
					String subjectUid = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
					Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUid, iArkCommonService.getStudy(sessionStudyId));
					consentHistoryList = iStudyService.getConsentHistoryList(linkSubjectStudy);
				}
				catch (EntityNotFoundException e) {
					e.printStackTrace();
				}
				
				listView.removeAll();
				return consentHistoryList;
			}
		};
		listView = buildListView(model);
		pageNavigator = new PagingNavigator("navigator", listView);
	}

	@SuppressWarnings("unchecked")
	public PageableListView<ConsentHistory> buildListView(IModel iModel) {
		PageableListView<ConsentHistory> listView = new PageableListView<ConsentHistory>("resultList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<ConsentHistory> item) {
				ConsentHistory consentHistory = item.getModelObject();

				if(consentHistory.getTimestamp() != null ) {
					item.add(new Label("timestamp", simpleDateFormat.format(consentHistory.getTimestamp())));
				}
				else {
					item.add(new Label("timestamp"));
				}
				
				if (consentHistory.getStudyComponentStatus() != null) {
					item.add(new Label("studyComponentStatus", consentHistory.getStudyComponentStatus().getName()));
				}
				else {
					item.add(new Label("studyComponentStatus"));
				}
				
				if(consentHistory.getConsentStatus() != null) {
					item.add(new Label("consentStatus", consentHistory.getConsentStatus().getName()));
				}
				else {
					item.add(new Label("consentStatus"));
				}
				
				if (consentHistory.getConsentDate() != null) {
					item.add(new Label("consentDate", simpleDateFormat.format(consentHistory.getConsentDate())));
				}
				else {
					item.add(new Label("consentDate"));
				}

				if (consentHistory.getConsentType() != null) {
					item.add(new Label("consentType", consentHistory.getConsentType().getName()));
				}
				else {
					item.add(new Label("consentType"));
				}
				
				if (consentHistory.getConsentDownloaded() != null) {
					item.add(new Label("consentDownloaded", consentHistory.getConsentDownloaded().getName()));
				}
				else {
					item.add(new Label("consentDownloaded"));
				}
				
				if (consentHistory.getRequestedDate() != null) {
					item.add(new Label("requestedDate", simpleDateFormat.format(consentHistory.getRequestedDate())));
				}
				else {
					item.add(new Label("requestedDate"));
				}
				
				if (consentHistory.getReceivedDate() != null) {
					item.add(new Label("receivedDate", simpleDateFormat.format(consentHistory.getReceivedDate())));
				}
				else {
					item.add(new Label("receivedDate"));
				}
				
				if (consentHistory.getCompletedDate() != null) {
					item.add(new Label("completedDate", simpleDateFormat.format(consentHistory.getCompletedDate())));
				}
				else {
					item.add(new Label("completedDate"));
				}

				if (consentHistory.getConsentedBy() != null) {
					item.add(new Label("consentedBy", consentHistory.getConsentedBy()));
				}
				else {
					item.add(new Label("consentedBy"));
				}

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return listView;
	}

	private void addComponents() {
		add(listView);
		add(pageNavigator);
	}
}
