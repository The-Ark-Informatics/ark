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
package au.org.theark.study.web.component.manageuser.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

public class SearchForm extends AbstractSearchForm<ArkUserVO> {

	private CompoundPropertyModel<ArkUserVO>	cpmModel;
	private ContainerForm							containerForm;
	private ArkCrudContainerVO						arkCrudContainerVO;
	private FeedbackPanel							feedbackPanel;

	// Form Fields
	private TextField<String>						userNameTxtField	= new TextField<String>(Constants.USER_NAME);
	private TextField<String>						firstNameTxtField	= new TextField<String>(Constants.FIRST_NAME);
	private TextField<String>						lastNameTxtField	= new TextField<String>(Constants.LAST_NAME);
	private TextField<String>						emailTxtField		= new TextField<String>(Constants.EMAIL);
	protected DropDownChoice<YesNo>				usersLinkedToStudyOnlyChoice;

	@SpringBean(name = "userService")
	private IUserService								userService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	private PageableListView<ArkUserVO>			pageableListView;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param cpmModel
	 * @param containerForm
	 */
	public SearchForm(String id, CompoundPropertyModel<ArkUserVO> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedbackPanel, ContainerForm containerForm,
			PageableListView<ArkUserVO> pageableListView) {

		super(id, cpmModel, feedbackPanel, arkCrudContainerVO);
		this.pageableListView = pageableListView;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedbackPanel = feedbackPanel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study.", arkCrudContainerVO);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		try {
			// Look up a Ark User's based on the filter specified by the user.
			List<ArkUserVO> userResultList = userService.searchUser(containerForm.getModelObject());

			if (userResultList != null && userResultList.size() == 0) {
				this.info("User(s) with the specified criteria does not exist in the system. Please refine your search filter.");
				target.addComponent(feedbackPanel);
			}

			pageableListView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		}
		catch (ArkSystemException e) {
			this.error("A System Error has occured. Please contact support");
		}
	}

	private void prePopulateArkUserRoleList() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		Collection<ArkModuleVO> listArkModuleVO = iArkCommonService.getArkModulesAndRolesLinkedToStudy(study);

		for (ArkModuleVO arkModuleVO : listArkModuleVO) {
			ArkUserRole arkUserRole = new ArkUserRole();
			arkUserRole.setStudy(study);
			arkUserRole.setArkModule(arkModuleVO.getArkModule());
			containerForm.getModelObject().getArkUserRoleList().add(arkUserRole);
		}
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		containerForm.getModelObject().setMode(Constants.MODE_NEW);
		prePopulateArkUserRoleList();
		arkCrudContainerVO.getWmcForarkUserAccountPanel().setVisible(true);
		preProcessDetailPanel(target, arkCrudContainerVO);
		target.addComponent(arkCrudContainerVO.getWmcForarkUserAccountPanel());// This should re-render the list again
	}

	protected void initialiseSearchForm() {

		userNameTxtField = new TextField<String>(Constants.USER_NAME);
		firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
		lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
		emailTxtField = new TextField<String>(Constants.EMAIL);
	}


	private void addSearchComponentsToForm() {
		add(emailTxtField);
		add(firstNameTxtField);
		add(lastNameTxtField);
		add(userNameTxtField);
	}

}
