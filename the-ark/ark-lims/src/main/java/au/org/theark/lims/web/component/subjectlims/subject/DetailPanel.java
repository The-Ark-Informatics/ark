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
package au.org.theark.lims.web.component.subjectlims.subject;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.web.component.subjectlims.lims.LimsContainerPanel;
import au.org.theark.lims.web.component.subjectlims.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectlims.subject.form.DetailForm;

/**
 * @author cellis
 * 
 */
public class DetailPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 858762052753650329L;
	private DetailForm			detailsForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	arkContextContainer;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private WebMarkupContainer	limsContainerWMC;
	private Panel					limsContainerPanel;

	public DetailPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkContextContainer = arkContextContainer;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void initialisePanel() {
		detailsForm = new DetailForm("detailsForm", feedBackPanel, arkContextContainer, containerForm, arkCrudContainerVO);

		detailsForm.initialiseDetailForm();
		add(detailsForm);

		limsContainerWMC = new WebMarkupContainer("limsContainerWMC");
		limsContainerWMC.setOutputMarkupPlaceholderTag(true);

		limsContainerPanel = new LimsContainerPanel("limsContainerPanel");
		limsContainerWMC.add(limsContainerPanel);
		this.add(limsContainerWMC);
	}

	public DetailForm getDetailsForm() {
		return detailsForm;
	}

	public void setDetailsForm(DetailForm detailsForm) {
		this.detailsForm = detailsForm;
	}
}
