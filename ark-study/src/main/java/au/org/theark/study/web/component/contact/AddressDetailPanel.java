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
import au.org.theark.study.web.component.contact.form.AddressDetailForm;
import au.org.theark.study.web.component.contact.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class AddressDetailPanel extends Panel {

	private static final long	serialVersionUID	= -6903681043337009908L;
	private FeedbackPanel		feedBackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;

	private ContainerForm		containerForm;
	private AddressDetailForm	detailForm;
	private WebMarkupContainer	detailPanelFormContainer;
	private WebMarkupContainer	editButtonContainer;
	

	public AddressDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedBackPanel;
	}

	public void initialisePanel() {
		replacedExsistingWebMarkups();
		detailForm = new AddressDetailForm("addressDetailsForm", feedBackPanel, arkCrudContainerVO, containerForm);
		detailForm.initialiseDetailForm();
		add(detailForm);

	}

	/**
	 * This will replace the abstract level web mark ups to suit to address details form. 
	 */
	private void replacedExsistingWebMarkups() {
		// Contains the controls of the Detail form
		detailPanelFormContainer = new WebMarkupContainer("addressDetailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);	
		arkCrudContainerVO.setDetailPanelFormContainer(detailPanelFormContainer);
		// Web markup to hold the buttons visible when in "Edit" mode
		editButtonContainer = new WebMarkupContainer("addressEditButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);
		arkCrudContainerVO.setEditButtonContainer(editButtonContainer);
	}
}