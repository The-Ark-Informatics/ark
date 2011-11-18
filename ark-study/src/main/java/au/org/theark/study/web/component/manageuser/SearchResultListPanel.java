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

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;

@SuppressWarnings("unchecked")
public class SearchResultListPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8708032624256041305L;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private ContainerForm		containerForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	@SpringBean(name = "userService")
	private IUserService			iUserService;
	private FeedbackPanel		feedbackPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm, FeedbackPanel feedbackPanel) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedbackPanel = feedbackPanel;
	}

	public PageableListView<ArkUserVO> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer) {
		// This has to be populated earlier

		PageableListView<ArkUserVO> pageableListView = new PageableListView<ArkUserVO>("userList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<ArkUserVO> item) {

				ArkUserVO arkUserVO = item.getModelObject();
				item.add(buildLink(arkUserVO, searchResultsContainer));
				item.add(new Label("lastName", arkUserVO.getLastName()));// the ID here must match the ones in mark-up
				item.add(new Label("firstName", arkUserVO.getFirstName()));
				item.add(new Label("email", arkUserVO.getEmail()));

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		return pageableListView;
	}

	private AjaxLink buildLink(final ArkUserVO arkUserVo, final WebMarkupContainer searchResultsContainer) {

		AjaxLink link = new AjaxLink("userName") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {

					// Fetch the user and related details from backend
					Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					Study study = iArkCommonService.getStudy(sessionStudyId);

					ArkUserVO arkUserVOFromBackend = iUserService.lookupArkUser(arkUserVo.getUserName(), study);
					if (!arkUserVOFromBackend.isArkUserPresentInDatabase()) {

						containerForm.info(new StringResourceModel("user.not.linked.to.study", this, null).getString());
						target.add(feedbackPanel);
						arkUserVOFromBackend.setChangePassword(true);
						arkUserVOFromBackend.getArkUserEntity().setLdapUserName(arkUserVo.getUserName());
						prePopulateForNewUser(arkUserVOFromBackend);
					}
					else {
						prePopulateArkUserRoleList(arkUserVOFromBackend);
					}

					containerForm.getModelObject().setArkUserRoleList(arkUserVOFromBackend.getArkUserRoleList());

					containerForm.setModelObject(arkUserVOFromBackend);

					// This triggers the call to populateItem() of the ListView
					ListView listView = (ListView) arkCrudContainerVO.getWmcForarkUserAccountPanel().get("arkUserRoleList");
					if (listView != null) {
						listView.removeAll();
					}

					/*
					// Render the UI
					arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
					arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
					arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
					arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
					arkCrudContainerVO.getViewButtonContainer().setVisible(true);// saveBtn
				
					try {
						
						String userName = containerForm.getModelObject().getArkUserEntity().getLdapUserName();
						if(iArkCommonService.isSuperAdministrator(userName)){
							AjaxButton editButton  = (AjaxButton) arkCrudContainerVO.getViewButtonContainer().get("edit");
							editButton.setEnabled(false);
						}
						else{
							AjaxButton editButton  = (AjaxButton) arkCrudContainerVO.getViewButtonContainer().get("edit");
							editButton.setEnabled(true);
						}
					} catch (EntityNotFoundException e) {
						e.printStackTrace();
					}
					
					
					arkCrudContainerVO.getEditButtonContainer().setVisible(false);
					*/
					arkCrudContainerVO.getWmcForarkUserAccountPanel().setVisible(true);
					/*
					target.add(arkCrudContainerVO.getWmcForarkUserAccountPanel());// This should re-render the list again
					target.add(arkCrudContainerVO.getSearchPanelContainer());
					target.add(arkCrudContainerVO.getDetailPanelContainer());
					target.add(arkCrudContainerVO.getSearchResultPanelContainer());
					target.add(arkCrudContainerVO.getViewButtonContainer());
					target.add(arkCrudContainerVO.getEditButtonContainer());
					target.add(arkCrudContainerVO.getDetailPanelFormContainer());
					*/
					ArkCRUDHelper.preProcessDetaiPanelOnSearchResults(target, arkCrudContainerVO);
					target.add(feedbackPanel);
					// Set the MODE here.Since the User Details are from LDAP we don't have a entity that we can use to check for a mode
					containerForm.getModelObject().setMode(Constants.MODE_EDIT);
				}
				catch (ArkSystemException e) {
					containerForm.error(new StringResourceModel("ark.system.error", this, null).getString());
					target.add(feedbackPanel);
				}
			}
		};
		// Add the label for the link
		Label userNameLinkLabel = new Label("userNameLink", arkUserVo.getUserName());
		link.add(userNameLinkLabel);
		return link;
	}

	private void prePopulateForNewUser(ArkUserVO arkUserVOFromBackend) {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		Collection<ArkModuleVO> listArkModuleVO = iArkCommonService.getArkModulesAndRolesLinkedToStudy(study);

		for (ArkModuleVO arkModuleVO : listArkModuleVO) {
			ArkUserRole arkUserRole = new ArkUserRole();
			arkUserRole.setStudy(study);
			arkUserRole.setArkModule(arkModuleVO.getArkModule());
			arkUserVOFromBackend.getArkUserRoleList().add(arkUserRole);
		}

	}

	private void prePopulateArkUserRoleList(ArkUserVO arkUserVOFromBackend) {

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		// Get a List of ArkModules and associated ArkRoles linked to the study
		Collection<ArkModuleVO> listOfModulesAndRolesForStudy = iArkCommonService.getArkModulesAndRolesLinkedToStudy(study);
		// Note:Ideally using a Hibernate Criteria we should be able to get a List of Modules
		for (ArkModuleVO arkModuleVO : listOfModulesAndRolesForStudy) {

			boolean moduleFoundFlag = false;
			ArkModule module = arkModuleVO.getArkModule();
			for (ArkUserRole arkUserRole : arkUserVOFromBackend.getArkUserRoleList()) {
				ArkModule arkModule = arkUserRole.getArkModule();
				if (module.equals(arkModule)) {
					moduleFoundFlag = true;
					break;
				}
			}

			if (!moduleFoundFlag) {
				ArkUserRole userRole = new ArkUserRole();
				userRole.setStudy(study);
				userRole.setArkModule(module);
				arkUserVOFromBackend.getArkUserRoleList().add(userRole);
			}
		}
	}
}
