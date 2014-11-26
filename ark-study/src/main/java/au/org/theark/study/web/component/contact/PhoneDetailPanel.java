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
package au.org.theark.study.web.component.contact;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.contact.form.ContainerForm;
import au.org.theark.study.web.component.contact.form.PhoneDetailForm;

/**
 * @author nivedann
 * 
 */
public class PhoneDetailPanel extends Panel {

	private static final long	serialVersionUID	= 6698569647044327113L;
	private PhoneDetailForm			detailForm;
	private FeedbackPanel		feedBackPanel;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private WebMarkupContainer	detailPanelFormContainer;
	private WebMarkupContainer	editButtonContainer;
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public PhoneDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
	}

	public void initialisePanel() {
		replacedExsistingWebMarkUps();
		detailForm = new PhoneDetailForm("phoneDetailsForm", feedBackPanel, arkCrudContainerVO, containerForm);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	/**
	 * This will replace the abstract level web mark ups to suit to phone details form. 
	 */
	private void replacedExsistingWebMarkUps() {
		// Contains the controls of the Detail form
		detailPanelFormContainer = new WebMarkupContainer("phoneDetailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		arkCrudContainerVO.setDetailPanelFormContainer(detailPanelFormContainer);
		// Web markup to hold the buttons visible when in "Edit" mode
		editButtonContainer = new WebMarkupContainer("phoneEditButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);
		arkCrudContainerVO.setEditButtonContainer(editButtonContainer);
	}
	
}
