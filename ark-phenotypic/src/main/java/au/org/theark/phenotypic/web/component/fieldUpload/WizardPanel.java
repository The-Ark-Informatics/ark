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
package au.org.theark.phenotypic.web.component.fieldUpload;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.phenotypic.web.component.fieldUpload.form.ContainerForm;
import au.org.theark.phenotypic.web.component.fieldUpload.form.WizardForm;

@SuppressWarnings("serial")
public class WizardPanel extends Panel {
	private WizardForm				wizardForm;
	private FeedbackPanel			feedBackPanel;
	private WebMarkupContainer		listContainer;
	private WebMarkupContainer		searchPanelContainer;
	protected WebMarkupContainer	resultListContainer;
	protected WebMarkupContainer	wizardPanelContainer;
	protected WebMarkupContainer	wizardPanelFormContainer;
	private WebMarkupContainer		wizardButtonContainer;
	private ContainerForm			containerForm;

	public WizardPanel(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer wizardPanelContainer, WebMarkupContainer searchPanelContainer,
			WebMarkupContainer wizardPanelFormContainer, ContainerForm containerForm) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.wizardPanelContainer = wizardPanelContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.wizardPanelFormContainer = wizardPanelFormContainer;
		this.containerForm = containerForm;
	}

	public void initialisePanel() {
		wizardForm = new WizardForm("wizardForm", feedBackPanel, this, listContainer, wizardPanelContainer, containerForm, wizardButtonContainer, wizardPanelFormContainer, searchPanelContainer);
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
