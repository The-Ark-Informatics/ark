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
package au.org.theark.registry.web.component.invoice;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.registry.web.component.invoice.form.ContainerForm;
import au.org.theark.registry.web.component.invoice.form.ProcessDetailForm;

/**
 * @author nivedann
 * 
 */
public class ProcessDetailPanel extends Panel {

	private static final long	serialVersionUID	= -6903681043337009908L;
	private FeedbackPanel		feedBackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;

	private Form<Process>		containerForm;
	private ProcessDetailForm			detailForm;

	public ProcessDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, Form<Process> containerForm2) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm2;
		this.feedBackPanel = feedBackPanel;
	}

	public ProcessDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO ) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
	}

	public void initialisePanel() {
		detailForm = new ProcessDetailForm("detailsForm", feedBackPanel, arkCrudContainerVO, containerForm);
		detailForm.initialiseDetailForm();
		detailForm.addOrReplace(arkCrudContainerVO.getDetailPanelFormContainer());
		add(detailForm);
	}

	public ProcessDetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(ProcessDetailForm detailForm) {
		this.detailForm = detailForm;
	}
}