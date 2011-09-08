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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.PhoneContainerForm;
import au.org.theark.study.web.component.subject.form.PhoneForm;

/**
 * @author nivedann
 * 
 */
public class PhoneDetail extends Panel {

	private PhoneContainerForm			phoneContainerForm;
	private FeedbackPanel				feedBackPanel;
	private PhoneForm						phoneForm;
	private PageableListView<Phone>	pageableListView;
	private WebMarkupContainer			phoneListContainer;
	private WebMarkupContainer			phoneDetailPanelContainer;
	// Subject
	private ContainerForm				subjectContainerForm;

	@SpringBean(name = "studyService")
	private IStudyService				studyService;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param phoneDetailPanelContainer
	 * @param containerForm
	 * @param feedBackPanel
	 */

	public PhoneDetail(String id, PhoneContainerForm containerForm, PageableListView<Phone> pageableListView, WebMarkupContainer phoneListContainer, WebMarkupContainer phoneDetailPanelContainer,
			FeedbackPanel feedBackPanel) {

		super(id);
		phoneContainerForm = containerForm;
		this.feedBackPanel = feedBackPanel;
		this.pageableListView = pageableListView;
		this.phoneListContainer = phoneListContainer;
		this.phoneDetailPanelContainer = phoneDetailPanelContainer;
	}

	public PhoneDetail(String id, ContainerForm containerForm, PageableListView<Phone> pageableListView, WebMarkupContainer phoneListContainer, WebMarkupContainer phoneDetailPanelContainer,
			FeedbackPanel feedBackPanel) {

		super(id);
		subjectContainerForm = containerForm;
		this.feedBackPanel = feedBackPanel;
		this.pageableListView = pageableListView;
		this.phoneListContainer = phoneListContainer;
		this.phoneDetailPanelContainer = phoneDetailPanelContainer;
	}

	@SuppressWarnings("serial")
	public void initialisePanel() {

		// Changed from phoneContainerForm to subjectContainerForm
		phoneForm = new PhoneForm("phoneForm", subjectContainerForm, pageableListView, phoneListContainer, phoneDetailPanelContainer, feedBackPanel) {

			protected void onSave(SubjectVO subjectVO, AjaxRequestTarget target) {
				// Save or Update the Phone detail
				// if(subjectVO.getPhone().getId() == null){
				//
				// }else{
				// }
			}

			@SuppressWarnings("unused")
			protected void onCancel(AjaxRequestTarget target) {

			}

			@SuppressWarnings("unused")
			protected void onDelete(AjaxRequestTarget target) {

			}

			protected void processFeedback(AjaxRequestTarget target) {
				target.addComponent(feedBackPanel);
			}
		};

		phoneForm.initialiseForm();
		add(phoneForm);
	}
}
