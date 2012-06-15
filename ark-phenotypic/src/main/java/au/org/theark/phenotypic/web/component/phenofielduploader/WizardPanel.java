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
package au.org.theark.phenotypic.web.component.phenofielduploader;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.phenotypic.web.component.phenofielduploader.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenofielduploader.form.WizardForm;

public class WizardPanel extends Panel {

	private static final long	serialVersionUID	= -4110431686558145782L;
	private WizardForm				wizardForm;
	private FeedbackPanel			feedBackPanel;
	private ContainerForm			containerForm;
	private ArkCrudContainerVO		arkCrudContainerVO;
	
	public WizardPanel(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}
	
	public void initialisePanel() {
		wizardForm = new WizardForm("wizardForm", feedBackPanel, containerForm, arkCrudContainerVO);
		wizardForm.initialiseDetailForm();
		add(wizardForm);
		setOutputMarkupPlaceholderTag(true);
	}

	public WizardForm getWizardForm() {
		return wizardForm;
	}

	public void setWizardForm(WizardForm wizardForm) {
		this.wizardForm = wizardForm;
	}
}
