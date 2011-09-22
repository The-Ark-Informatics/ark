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
package au.org.theark.study.web.component.subject.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class PhoneForm extends Form {

	// private PhoneContainerForm phoneContainerForm;
	private ContainerForm					subjectContainerForm;
	private TextField<String>				areaCodeTxtFld;
	private TextField<String>				phoneNumberTxtFld;
	private TextField<String>				phoneIdTxtFld;
	private DropDownChoice<PhoneType>	phoneTypeChoice;

	private WebMarkupContainer				phoneListContainer;
	private WebMarkupContainer				detailPanelContainer;

	private AjaxButton						deleteButton;
	private AjaxButton						saveButton;
	private AjaxButton						cancelButton;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService					studyService;

	private PageableListView<Phone>		pageableListView;
	private FeedbackPanel					feedBackPanel;

	/**
	 * @param id
	 */
	public PhoneForm(String id, ContainerForm containerForm,
	// PhoneContainerForm containerForm,
			PageableListView<Phone> pageableListView, WebMarkupContainer phoneListContainer, WebMarkupContainer detailPanelContainer, FeedbackPanel feedbackPanel) {
		super(id);
		this.subjectContainerForm = containerForm;
		this.pageableListView = pageableListView;
		this.phoneListContainer = phoneListContainer;
		this.detailPanelContainer = detailPanelContainer;
		this.feedBackPanel = feedbackPanel;
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onPhoneAddCancel(subjectContainerForm.getModelObject(), target);

			}
		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onPhoneAdd(subjectContainerForm.getModelObject(), target);
				// Hide the Detail Panel for Phone and make the List display with the new result
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				// Display the error message and allow user to correct it or cancel
				processFeedback(target);
			}
		};

		deleteButton = new AjaxButton(Constants.DELETE, new StringResourceModel("deleteKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

			}
		};

	}

	protected void onPhoneAddCancel(SubjectVO subjectVO, AjaxRequestTarget target) {
		// subjectContainerForm.getModelObject().setPhone(new Phone());
		phoneListContainer.setVisible(true);
		detailPanelContainer.setVisible(false);
		target.addComponent(phoneListContainer);
		target.addComponent(detailPanelContainer);
	}

	protected void onPhoneAdd(SubjectVO subjectVO, AjaxRequestTarget target) {
		// Phone phone = subjectVO.getPhone();
		// if(phone.getId() == null){
		// subjectContainerForm.getModelObject().getPhoneList().add(phone);
		// }
		pageableListView.removeAll();
		phoneListContainer.setVisible(true);
		detailPanelContainer.setVisible(false);
		target.addComponent(phoneListContainer);
		target.addComponent(detailPanelContainer);
		// Add the item into a list
		// Close the the detail panel
		// Refresh the List view
	}

	protected void onPhoneModify(SubjectVO subjectVO, AjaxRequestTarget target) {

	}

	protected void processFeedback(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}

	public void initialiseForm() {

		phoneIdTxtFld = new TextField<String>("phone.phoneKey");
		areaCodeTxtFld = new TextField<String>("phone.areaCode");
		phoneNumberTxtFld = new TextField<String>("phone.phoneNumber");

		// Initalise the dropdown choice with a list
		List<PhoneType> phoneTypeList = iArkCommonService.getListOfPhoneType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.PHONE_TYPE_ID);
		phoneTypeChoice = new DropDownChoice("phone.phoneType", phoneTypeList, defaultChoiceRenderer);
		addComponents();
	}

	private void addComponents() {

		add(phoneIdTxtFld.setEnabled(false));
		add(areaCodeTxtFld);
		add(phoneNumberTxtFld);
		add(phoneTypeChoice);
		add(saveButton);
		add(cancelButton);
		add(deleteButton);
	}

	private void attachValidators() {

	}

}
