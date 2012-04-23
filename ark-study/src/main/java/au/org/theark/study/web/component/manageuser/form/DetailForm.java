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
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
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
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractUserDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

public class DetailForm extends AbstractUserDetailForm<ArkUserVO> {
	/**
	 * 
	 */
	private static final long		serialVersionUID			= -2380685360085526939L;

	@SpringBean(name = "userService")
	private IUserService				iUserService;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService			iStudyService;

	protected TextField<String>	userNameTxtField			= new TextField<String>(Constants.USER_NAME);
	protected TextField<String>	firstNameTxtField			= new TextField<String>(Constants.FIRST_NAME);
	protected TextField<String>	lastNameTxtField			= new TextField<String>(Constants.LAST_NAME);
	protected TextField<String>	emailTxtField				= new TextField<String>(Constants.EMAIL);
	protected PasswordTextField	userPasswordField			= new PasswordTextField(Constants.PASSWORD);
	protected PasswordTextField	confirmPasswordField		= new PasswordTextField(Constants.CONFIRM_PASSWORD);
	protected PasswordTextField	oldPasswordField			= new PasswordTextField(Constants.OLD_PASSWORD);
	protected WebMarkupContainer	groupPasswordContainer	= new WebMarkupContainer("groupPasswordContainer");
	protected Label					assignedChildStudiesLabel;
	protected ArkPalette<Study>	assignedChildStudiesPalette;
	protected Label					assignedChildStudiesNote;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, arkCrudContainerVO, containerForm);
		initialiseRemoveButton();
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

		ListView listView = new ListView("arkUserRoleList", iModel) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(ListItem item) {
				// Each item will be ArkModuleVO use that to build the Module name and the drop down
				ArkUserRole arkUserRole = (ArkUserRole) item.getModelObject();
				ArkModule arkModule = arkUserRole.getArkModule();
				// Acts as the data source for ArkRoles
				ArrayList<ArkRole> arkRoleSourceList = iArkCommonService.getArkRoleLinkedToModule(arkModule);
				if (arkUserRole.getArkUser() == null && arkModule.getName().equalsIgnoreCase(au.org.theark.core.Constants.ARK_MODULE_STUDY)) {
					// If the ArkUserRole is not assigned and the module is Study then set the default Role
					ArkRole arkRole = iArkCommonService.getArkRoleByName(au.org.theark.core.Constants.ARK_STUDY_DEFAULT_ROLE);
					arkUserRole.setArkRole(arkRole);
				}

				PropertyModel arkUserRolePm = new PropertyModel(arkUserRole, "arkRole");
				ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>(Constants.NAME, "id");

				DropDownChoice<ArkRole> ddc = new DropDownChoice<ArkRole>("arkRole", arkUserRolePm, arkRoleSourceList, defaultChoiceRenderer);

				item.add(new Label("moduleName", arkModule.getName()));// arkModule within ArkUserRole
				item.add(ddc);

			}
		};

		listView.setReuseItems(true);
		arkCrudContainerVO.getWmcForarkUserAccountPanel().add(listView);

		initChildStudyPalette();

		attachValidators();
		addDetailFormComponents();

	}

	@SuppressWarnings("unchecked")
	private void initChildStudyPalette() {
		assignedChildStudiesLabel = new Label("assignedChildStudiesLabel", "Assigned Child Studies:");
		CompoundPropertyModel<ArkUserVO> sm = (CompoundPropertyModel<ArkUserVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
		PropertyModel<List<Study>> availableChildStudiesPm = new PropertyModel<List<Study>>(sm, "availableChildStudies");
		PropertyModel<List<Study>> selectedChildStudiesPm = new PropertyModel<List<Study>>(sm, "selectedChildStudies");
		assignedChildStudiesPalette = new ArkPalette("assignedChildStudiesPalette", selectedChildStudiesPm, availableChildStudiesPm, renderer, au.org.theark.study.web.Constants.PALETTE_ROWS, false);
		assignedChildStudiesNote = new Label("assignedChildStudiesNote", "Note: Child studies will have the same roles as the parent study");
	}

	@Override
	public void onBeforeRender() {
		if (!isNew()) {
			userNameTxtField.setEnabled(false);
			boolean hasChildStudies = (!containerForm.getModelObject().getAvailableChildStudies().isEmpty());
			assignedChildStudiesLabel.setVisible(hasChildStudies);
			assignedChildStudiesPalette.setVisible(hasChildStudies);
			assignedChildStudiesNote.setVisible(hasChildStudies);
		}
		else {
			userNameTxtField.setEnabled(true);
		}
		super.onBeforeRender();
	}

	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(userNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(firstNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(lastNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(emailTxtField);

		// We use this markup to hide unhide the password fields during edit. i.e. if the user selects edit password then make it visible/enabled.

		groupPasswordContainer.add(userPasswordField);
		groupPasswordContainer.add(confirmPasswordField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(groupPasswordContainer);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkCrudContainerVO.getWmcForarkUserAccountPanel());

		// Child study assigning
		arkCrudContainerVO.getDetailPanelFormContainer().add(assignedChildStudiesLabel);
		arkCrudContainerVO.getDetailPanelFormContainer().add(assignedChildStudiesPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().add(assignedChildStudiesNote);

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
				iUserService.createArkUser(containerForm.getModelObject());
				containerForm.getModelObject().setArkUserPresentInDatabase(true);
				containerForm.getModelObject().setMode(Constants.MODE_EDIT);
				userNameTxtField.setEnabled(false);
				onSavePostProcess(target);
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
				// Update ArkUser for study in context
				iUserService.updateArkUser(containerForm.getModelObject());

				// Delete ArkUser for all unassigned child studies
				List<Study> availableChildStudies = null;
				availableChildStudies = iStudyService.getChildStudyListOfParent(study);
				for (Study childStudy : availableChildStudies) {
					iUserService.deleteArkUserRolesForStudy(childStudy, containerForm.getModelObject().getArkUserEntity());
				}

				// Update ArkUser for all assigned child studies
				List<Study> childStudies = containerForm.getModelObject().getSelectedChildStudies();
				for (Study childStudy : childStudies) {
					for (ArkUserRole parentArkUserRole : containerForm.getModelObject().getArkUserRoleList()) {
						// Only create roles where selected
						if(parentArkUserRole.getArkRole() != null) {
							ArkUserRole arkUserRole = new ArkUserRole();
							arkUserRole.setArkUser(containerForm.getModelObject().getArkUserEntity());
							arkUserRole.setArkRole(parentArkUserRole.getArkRole());
							arkUserRole.setArkModule(parentArkUserRole.getArkModule());
							arkUserRole.setStudy(childStudy);
							iUserService.createArkUserRole(arkUserRole);
						}
					}
				}

				containerForm.getModelObject().setArkUserPresentInDatabase(true);
				this.info(new StringResourceModel("user.updated", this, null).getString());
				onSavePostProcess(target);

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
			iUserService.deleteArkUser(containerForm.getModelObject());
			this.info(new StringResourceModel("user.removed", this, null).getString());
			editCancelProcess(target);
		}
		catch (EntityNotFoundException e) {
			this.error(new StringResourceModel("user.notFound", this, null).getString());
		}
		catch (ArkSystemException e) {
			this.error(new StringResourceModel("ark.system.error", this, null).getString());
		}
		onCancel(target);
	}

	public void enableOrDisableRemoveButton() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		try {
			ArkUserVO arkUserVO = iUserService.lookupArkUser(containerForm.getModelObject().getUserName(), study);
			if (!arkUserVO.isArkUserPresentInDatabase()) {
				deleteButton.setEnabled(false);
			}
			else {
				deleteButton.setEnabled(true);
			}
		}
		catch (ArkSystemException e) {

			e.printStackTrace();
		}
	}
}