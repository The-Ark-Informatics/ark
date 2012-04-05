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
package au.org.theark.study.web.component.mydetails.form;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;

public class MyDetailsForm extends Form<ArkUserVO> {

	/**
	 * 
	 */
	private static final long			serialVersionUID			= 2381693804874240001L;
	private transient static Logger	log							= LoggerFactory.getLogger(MyDetailsForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	@SpringBean(name = "userService")
	private IUserService					iUserService;
	protected TextField<String>		userNameTxtField			= new TextField<String>(Constants.USER_NAME);
	protected TextField<String>		firstNameTxtField			= new TextField<String>(Constants.FIRST_NAME);
	protected TextField<String>		lastNameTxtField			= new TextField<String>(Constants.LAST_NAME);
	protected TextField<String>		emailTxtField				= new TextField<String>(Constants.EMAIL);
	protected TextField<String>		phoneNumberTxtField		= new TextField<String>(Constants.PHONE_NUMBER);
	protected PasswordTextField		userPasswordField			= new PasswordTextField(Constants.PASSWORD);
	protected PasswordTextField		confirmPasswordField		= new PasswordTextField(Constants.CONFIRM_PASSWORD);
	protected WebMarkupContainer		groupPasswordContainer	= new WebMarkupContainer("groupPasswordContainer");
	private AjaxButton					saveButton;
	private AjaxButton					closeButton;
	private FeedbackPanel				feedbackPanel;
	private ModalWindow					modalWindow;
	@SuppressWarnings("unchecked")
	private PageableListView			pageableListView;
	// Add a visitor class for required field marking/validation/highlighting
	ArkFormVisitor							formVisitor					= new ArkFormVisitor();

	public MyDetailsForm(String id, CompoundPropertyModel<ArkUserVO> model, final FeedbackPanel feedbackPanel, ModalWindow modalWindow) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		this.modalWindow = modalWindow;
	}

	@SuppressWarnings( { "unchecked" })
	public void initialiseForm() {
		ArkUserVO arkUserVOFromBackend = new ArkUserVO();
		try {
			arkUserVOFromBackend = iUserService.lookupArkUser(getModelObject().getUserName(), getModelObject().getStudy());
			List<ArkUserRole> arkUserRoleList = iArkCommonService.getArkRoleListByUser(arkUserVOFromBackend);
			arkUserVOFromBackend.setArkUserRoleList(arkUserRoleList);
			getModelObject().setArkUserRoleList(arkUserRoleList);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8737230044711628981L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processFeedback(target, feedbackPanel);
			}
		};

		closeButton = new AjaxButton(Constants.CLOSE, new StringResourceModel("closeKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.close(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processFeedback(target, feedbackPanel);
			}
		};
		closeButton.setDefaultFormProcessing(false);

		emailTxtField.add(EmailAddressValidator.getInstance());

		IModel<List<ArkUserRole>> iModel = new LoadableDetachableModel() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				return getModelObject().getArkUserRoleList();
			}
		};

		// TODO: Amend hard-coded 50 row limit, pageableListView didn't work within a ModalWindow
		pageableListView = new PageableListView("arkUserRoleList", iModel, 50) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3557668722549243826L;

			@Override
			protected void populateItem(final ListItem item) {
				ArkUserRole arkUserRole = (ArkUserRole) item.getModelObject();

				if (arkUserRole.getStudy() != null) {
					item.addOrReplace(new Label("studyName", arkUserRole.getStudy().getName()));
				}
				else {
					item.addOrReplace(new Label("studyName", "[All Study Access]"));
				}

				item.addOrReplace(new Label("moduleName", arkUserRole.getArkModule().getName()));
				item.addOrReplace(new Label("roleName", arkUserRole.getArkRole().getName()));

				try {
					Collection<String> rolePermissions = iArkCommonService.getArkRolePermission(arkUserRole.getArkRole().getName());
					if (rolePermissions.contains("CREATE")) {
						item.addOrReplace(new ContextImage("arkCreatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkCreatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (rolePermissions.contains("READ")) {
						item.addOrReplace(new ContextImage("arkReadPermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkReadPermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (rolePermissions.contains("UPDATE")) {
						item.addOrReplace(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkUpdatePermission", new Model<String>("images/icons/cross.png")));
					}

					// the ID here must match the ones in mark-up
					if (rolePermissions.contains("DELETE")) {
						item.addOrReplace(new ContextImage("arkDeletePermission", new Model<String>("images/icons/tick.png")));
					}
					else {
						item.addOrReplace(new ContextImage("arkDeletePermission", new Model<String>("images/icons/cross.png")));
					}
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}

				item.setEnabled(false);

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= -8887455455175404701L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		pageableListView.setReuseItems(true);

		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		add(pageNavigator);

		attachValidators();
		addComponents();
	}

	private void attachValidators() {
		userNameTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("userName.incorrect.format", this, null));
		userNameTxtField.setRequired(true).setLabel(new StringResourceModel("userName", this, null));
		userNameTxtField.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("userName", this, null));

		firstNameTxtField.setRequired(true).setLabel(new StringResourceModel("firstName", this, null));
		firstNameTxtField.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("firstName", this, null));

		emailTxtField.add(EmailAddressValidator.getInstance()).setLabel(new StringResourceModel("email.incorrect.format", this, null));
		emailTxtField.setRequired(true).setLabel(new StringResourceModel("email", this, null));

		lastNameTxtField.add(StringValidator.lengthBetween(3, 50)).setLabel(new StringResourceModel("lastNameLength", this, null));
		lastNameTxtField.setRequired(true).setLabel(new StringResourceModel("lastName", this, null));

		userPasswordField.setRequired(false);
		confirmPasswordField.setRequired(false);

		userPasswordField.setLabel(Model.of("Password"));
		userPasswordField.add(new PatternValidator(Constants.PASSWORD_PATTERN));
		confirmPasswordField.setLabel(Model.of("Confirm Password"));
	}

	private void addComponents() {
		add(userNameTxtField);
		add(firstNameTxtField);
		add(lastNameTxtField);
		add(emailTxtField);
		groupPasswordContainer.add(userPasswordField);
		groupPasswordContainer.add(confirmPasswordField);
		add(groupPasswordContainer);
		add(new EqualPasswordInputValidator(userPasswordField, confirmPasswordField));
		add(saveButton);
		add(closeButton);
		add(pageableListView);
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	protected void processFeedback(AjaxRequestTarget target, FeedbackPanel fb) {

	}

	protected void onSave(AjaxRequestTarget target) {

	}

	protected void onCancel(AjaxRequestTarget target) {

	}

	public TextField<String> getUserNameTxtField() {
		return userNameTxtField;
	}
}
