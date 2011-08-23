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
package au.org.theark.study.web.component.subject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.DetailsForm;
import au.org.theark.study.web.component.subject.form.PhoneContainerForm;

/**
 * The container class for Subject related List Items. The lists like Phone/Address or Email will be listed using this container.
 * 
 * @author nivedann
 * 
 */
public class PhoneListContainer extends Panel {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService			iArkCommonService;

	private PhoneDetail					phoneDetailPanel;
	private PhoneList						phoneListPanel;

	private WebMarkupContainer			phoneListPanelContainer;
	private WebMarkupContainer			phoneDetailPanelContainer;

	private IModel<Object>				iModel;
	private PageableListView<Phone>	pageableListView;
	private PhoneContainerForm			phoneContainerForm;
	private DetailsForm					subjectForm;
	private AjaxButton					addPhoneButton;
	private FeedbackPanel				feedBackPanel;

	private ContainerForm				subjectContainerForm;

	private void onAddPhone(SubjectVO subjectVO, AjaxRequestTarget target) {
		System.out.println("onAddPhone Invoked");
		phoneDetailPanelContainer.setVisible(true);
		target.addComponent(phoneDetailPanelContainer);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param containerForm
	 * @param subjectDetailForm
	 */
	public PhoneListContainer(String id, ContainerForm containerForm, FeedbackPanel feedbackPanel, WebMarkupContainer phoneListPanelContainer, WebMarkupContainer phoneDetailPanelContainer) {
		super(id);
		this.feedBackPanel = feedbackPanel;
		this.phoneListPanelContainer = phoneListPanelContainer;
		this.phoneDetailPanelContainer = phoneDetailPanelContainer;
		this.subjectContainerForm = containerForm;

		phoneContainerForm = new PhoneContainerForm("phoneContainerForm");

		addPhoneButton = new AjaxButton(Constants.ADD_PHONE, new StringResourceModel("addPhoneKey", this, null)) {

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {

				// phoneContainerForm.getModelObject().setPhone(new Phone());
				// subjectContainerForm.getModelObject().getSubjectStudy().setPhone(new Phone());
				// onAddPhone(phoneContainerForm.getModelObject(), target);
				onAddPhone(subjectContainerForm.getModelObject(), target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				// processErrors(target);
			}
		};

		phoneListPanelContainer.add(addPhoneButton.setDefaultFormProcessing(false));

		phoneContainerForm.add(initialisePhoneList());
		phoneContainerForm.add(initialiseDetailPanel(phoneListPanelContainer, feedBackPanel));
		add(phoneContainerForm);
	}

	@SuppressWarnings("serial")
	public WebMarkupContainer initialisePhoneList() {

		phoneListPanel = new PhoneList("phoneListPanel", subjectContainerForm, phoneListPanelContainer, phoneDetailPanelContainer);
		iModel = new LoadableDetachableModel<Object>() {
			@Override
			protected Object load() {

				// TODO: Need to use the back-end to get the next list of Phone items in order to keep the data fresh and avoid LIE
				// Collection<SubjectVO> subjects = iArkCommonService.getSubject(subjectContainerForm.getModelObject());
				//
				// SubjectVO subjectVOBackend = new SubjectVO();
				// for (SubjectVO subjectVO : subjects) {
				// subjectVOBackend = subjectVO;
				// }
				// return subjectVOBackend.getPhoneList();//We are not updating the Model object Person
				return subjectContainerForm.getModelObject().getPhoneList();
			}
		};
		pageableListView = phoneListPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("phoneNavigator", pageableListView);
		phoneListPanel.add(pageNavigator);
		phoneListPanel.add(pageableListView);
		phoneListPanelContainer.add(phoneListPanel);

		return phoneListPanelContainer;
	}

	private WebMarkupContainer initialiseDetailPanel(WebMarkupContainer phoneListPanelContainer, FeedbackPanel feedbackPanel) {
		// phoneDetailPanel = new PhoneDetail("phoneDetailPanel",phoneContainerForm, pageableListView,
		// phoneListPanelContainer,phoneDetailPanelContainer, feedBackPanel);
		phoneDetailPanel = new PhoneDetail("phoneDetailPanel", subjectContainerForm, pageableListView, phoneListPanelContainer, phoneDetailPanelContainer, feedBackPanel);
		phoneDetailPanel.initialisePanel();
		phoneDetailPanelContainer.add(phoneDetailPanel);
		return phoneDetailPanelContainer;
	}

}
