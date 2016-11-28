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
package au.org.theark.study.web.component.manageuser;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserContainerPanel extends AbstractContainerPanel<ArkUserVO> {

	private static Logger log = LoggerFactory.getLogger(UserContainerPanel.class);

	private static final long				serialVersionUID	= 8852394393025448913L;
	private ContainerForm					containerForm;
	private DetailPanel						detailsPanel;
	private SearchPanel						searchPanel;
	private PageableListView<ArkUserVO>	pageableListView;

	/* Spring Beans to Access Service Layer */
	@SpringBean(name = "userService")
	private IUserService						iUserService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<?>				iArkCommonService;

	public UserContainerPanel(String id) {

		super(id);

		cpModel = new CompoundPropertyModel<ArkUserVO>(new ArkUserVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {

		SearchResultListPanel searchResultListPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm, feedBackPanel);
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				List<ArkUserVO> userResultList = new ArrayList<ArkUserVO>();
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				String sessionUserName	= (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_USERID);
				if (isActionPermitted()) {
					if (sessionStudyId != null && sessionStudyId > 0) {
						ArkUser arkUser = null;
						try {
							arkUser=iArkCommonService.getArkUser(sessionUserName);
						} catch (EntityNotFoundException e) {
							log.error("User not found: " + arkUser.getLdapUserName());
							e.printStackTrace();
						}
						for(ArkUser arkUserEntry : iArkCommonService.getArkUserListByStudy(arkUser, iArkCommonService.getStudy(sessionStudyId))) {
							ArkUserVO arkUserVO = null;
							try {
								arkUserVO = iUserService.getCurrentUser(arkUserEntry.getLdapUserName());
							} catch (EntityNotFoundException e) {
							    log.error("User not found: " + arkUserEntry.getLdapUserName());
								e.printStackTrace();
							}
							if(arkUserVO != null) {
								userResultList.add(arkUserVO);
							}
						}

						containerForm.getModelObject().setUserList(userResultList);
					}
					pageableListView.removeAll();
				}
				return userResultList;
			}
		};

		pageableListView = searchResultListPanel.buildPageableListView(iModel, arkCrudContainerVO.getSearchResultPanelContainer());
		pageableListView.setReuseItems(true);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", pageableListView);
		pageNavigator.setOutputMarkupId(true);
		searchResultListPanel.setOutputMarkupId(true);
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new DetailPanel("detailPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		detailsPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {

		ArkUserVO arkUserVO = new ArkUserVO();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<ArkUserVO> userResultList = new ArrayList<ArkUserVO>();

		try {
			if (sessionStudyId != null && sessionStudyId > 0) {
				userResultList = iUserService.searchUser(arkUserVO);
			}
		}
		catch (ArkSystemException arkException) {

		}

		containerForm.getModelObject().setUserList(userResultList);
		searchPanel = new SearchPanel("searchPanel", arkCrudContainerVO, feedBackPanel, containerForm, pageableListView);
		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}
}
