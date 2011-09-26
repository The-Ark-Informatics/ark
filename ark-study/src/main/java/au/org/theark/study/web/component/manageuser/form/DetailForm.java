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

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractUserDetailForm;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

@SuppressWarnings("serial")
public class DetailForm extends AbstractUserDetailForm<ArkUserVO> {

	@SpringBean(name = "userService")
	private IUserService				userService;

	@SuppressWarnings("rawtypes")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;

	protected TextField<String>	userNameTxtField			= new TextField<String>(Constants.USER_NAME);
	protected TextField<String>	firstNameTxtField			= new TextField<String>(Constants.FIRST_NAME);
	protected TextField<String>	lastNameTxtField			= new TextField<String>(Constants.LAST_NAME);
	protected TextField<String>	emailTxtField				= new TextField<String>(Constants.EMAIL);
	protected PasswordTextField	userPasswordField			= new PasswordTextField(Constants.PASSWORD);
	protected PasswordTextField	confirmPasswordField		= new PasswordTextField(Constants.CONFIRM_PASSWORD);
	protected PasswordTextField	oldPasswordField			= new PasswordTextField(Constants.OLD_PASSWORD);
	protected WebMarkupContainer	groupPasswordContainer	= new WebMarkupContainer("groupPasswordContainer");
	private ArkCrudContainerVO		arkCrudContainerVO;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, arkCrudContainerVO, containerForm);
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {
		userNameTxtField = new TextField<String>(Constants.USER_NAME);
		firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
		lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
		emailTxtField = new TextField<String>(Constants.EMAIL);
		userPasswordField = new PasswordTextField(Constants.PASSWORD);
		confirmPasswordField = new PasswordTextField(Constants.CONFIRM_PASSWORD);
		oldPasswordField = new PasswordTextField(Constants.OLD_PASSWORD);
		groupPasswordContainer = new WebMarkupContainer("groupPasswordContainer");

		IModel<List<ArkUserRole>> iModel = new LoadableDetachableModel() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {

				return containerForm.getModelObject().getArkUserRoleList();

			}
		};

		@SuppressWarnings("rawtypes")
		ListView listView = new ListView("arkUserRoleList", iModel) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(ListItem item) {
				// Each item will be ArkModuleVO use that to build the Module name and the drop down
				ArkUserRole arkUserRole = (ArkUserRole) item.getModelObject();
				ArkModule arkModule = arkUserRole.getArkModule();
				// Acts as the data source for ArkRoles
				ArrayList<ArkRole> arkRoleSourceList = iArkCommonService.getArkRoleLinkedToModule(arkModule);

				PropertyModel arkUserRolePm = new PropertyModel(arkUserRole, "arkRole");
				ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>(Constants.NAME, "id");

				DropDownChoice<ArkRole> ddc = new DropDownChoice<ArkRole>("arkRole", arkUserRolePm, arkRoleSourceList, defaultChoiceRenderer);

				item.add(new Label("moduleName", arkModule.getName()));// arkModule within ArkUserRole
				item.add(ddc);

			}
		};

		listView.setReuseItems(true);
		arkCrudContainerVO.getWmcForarkUserAccountPanel().add(listView);
		attachValidators();
		addDetailFormComponents();

	}

	private void addDetailFormComponents() {

		arkCrudContainerVO.getDetailPanelFormContainer().add(userNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(firstNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(lastNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailTxtField);

		// We use this markup to hide unhide the password fields during edit. i.e. if the user selects edit password then make it visible/enabled.

		groupPasswordContainer.add(userPasswordField);
		groupPasswordContainer.add(confirmPasswordField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(groupPasswordContainer);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkCrudContainerVO.getWmcForarkUserAccountPanel());

		add(new EqualPasswordInputValidator(userPasswordField, confirmPasswordField));
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	@Override
	protected void attachValidators() {

		lastNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("lastName", this, null));
		lastNameTxtField.setRequired(true);

		userNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("userName", this, null));
		userNameTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("userName.incorrectPattern", this, null));
		userNameTxtField.setRequired(true).setLabel(new StringResourceModel("userName", this, null));

		firstNameTxtField.setRequired(true).setLabel(new StringResourceModel("firstName", this, null));
		firstNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("firstName", this, null));

		emailTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel("email", this, null));
		emailTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("email.incorrectpattern", this, null));
		emailTxtField.setRequired(true).setLabel(new StringResourceModel("email", this, null));

		userPasswordField.setRequired(false);
		confirmPasswordField.setRequired(false);

		// Set the confirm password with a password pattern
		userPasswordField.setLabel(Model.of("Password"));
		userPasswordField.add(new PatternValidator(Constants.PASSWORD_PATTERN));
		confirmPasswordField.setLabel(Model.of("Confirm Password"));

	}

	protected void onCancel(AjaxRequestTarget target) {
		userNameTxtField.setEnabled(true);
		ArkUserVO arkUserVO = new ArkUserVO();
		containerForm.setModelObject(arkUserVO);
	}

	@Override
	protected void onSave(Form<ArkUserVO> containerForm, AjaxRequestTarget target) {

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		containerForm.getModelObject().setStudy(study);

		if (containerForm.getModelObject().getMode() == Constants.MODE_NEW) {

			try {

				userService.createArkUser(containerForm.getModelObject());
				containerForm.getModelObject().setArkUserPresentInDatabase(true);
				containerForm.getModelObject().setMode(Constants.MODE_EDIT);
				userNameTxtField.setEnabled(false);
				onSavePostProcess(target, arkCrudContainerVO);
				this.info(new StringResourceModel("user.saved", this, null).getString());
				target.add(feedBackPanel);

			}
			catch (UserNameExistsException e) {
				this.error(new StringResourceModel("user.exists", this, null).getString());
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("ark.system.error", this, null).getString());
			}
			catch (Exception e) {
				this.error(new StringResourceModel("severe.system.error", this, null).getString());
			}

		}
		else if (containerForm.getModelObject().getMode() == Constants.MODE_EDIT) {

			try {

				userService.updateArkUser(containerForm.getModelObject());
				containerForm.getModelObject().setArkUserPresentInDatabase(true);
				this.info(new StringResourceModel("user.updated", this, null).getString());
				onSavePostProcess(target, arkCrudContainerVO);

			}
			catch (EntityNotFoundException e) {
				this.error(new StringResourceModel("user.notFound", this, null).getString());
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("ark.system.error", this, null).getString());
			}
		}

		target.add(feedBackPanel);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getMode() == Constants.MODE_NEW) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {

		// Remove the Ark User from the Ark Database and his roles.
		try {
			userService.deleteArkUser(containerForm.getModelObject());
			this.info(new StringResourceModel("user.removed", this, null).getString());
			editCancelProcess(target, true);
		}
		catch (EntityNotFoundException e) {
			this.error(new StringResourceModel("user.notFound", this, null).getString());
		}
		catch (ArkSystemException e) {
			this.error(new StringResourceModel("ark.system.error", this, null).getString());
		}
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractUserDetailForm#onEditButtonClick()
	 */
	@Override
	public void onEditButtonClick() {
		userNameTxtField.setEnabled(false);
	}

}
