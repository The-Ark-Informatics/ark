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
package au.org.theark.core.web.component.wizard;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;

@SuppressWarnings("serial")
public class ArkCommonWizard extends Wizard {
	private WebMarkupContainer		resultListContainer;
	private WebMarkupContainer		wizardPanelContainer;
	private WebMarkupContainer		wizardPanelFormContainer;
	private WebMarkupContainer		searchPanelContainer;
	private AjaxRequestTarget		target;
	private WizardStep[]				wizardSteps;
	private AjaxWizardButtonBar	buttonBar;

	/**
	 * Constructor with specified wizard home/index page
	 * 
	 * @param id
	 *           The component id
	 * @param wizardSteps
	 *           The custom wizard steps to be used
	 */
	public ArkCommonWizard(String id, WizardStep[] wizardSteps) {
		super(id);

		// create a model with the specified steps
		WizardModel model = new WizardModel();

		for (int i = 0; i < wizardSteps.length; i++) {
			model.add(wizardSteps[i]);
		}

		// initialize the wizard
		init(model);
	}

	/**
	 * Constructor with specified wizard home/index page
	 * 
	 * @param id
	 *           The component id
	 * @param wizardSteps
	 *           The custom wizard steps to be used
	 */
	public ArkCommonWizard(String id, WizardStep[] wizardSteps, WebMarkupContainer resultListContainer, WebMarkupContainer wizardPanelContainer, WebMarkupContainer wizardPanelFormContainer,
			WebMarkupContainer searchPanelContainer, AjaxRequestTarget target) {
		super(id);

		this.setResultListContainer(resultListContainer);
		this.setWizardPanelContainer(wizardPanelContainer);
		this.setWizardPanelFormContainer(wizardPanelFormContainer);
		this.setSearchPanelContainer(searchPanelContainer);
		this.setTarget(target);
		this.wizardSteps = new WizardStep[wizardSteps.length];

		// create a model with the specified steps
		WizardModel model = new WizardModel();

		for (int i = 0; i < wizardSteps.length; i++) {
			this.wizardSteps[i] = wizardSteps[i];
			model.add(wizardSteps[i]);
		}

		// initialize the wizard
		init(model);
	}

	@Override
	protected Component newButtonBar(java.lang.String id) {
		return new AjaxWizardButtonBar(id, this);
	}

	@Override
	protected Component newOverviewBar(java.lang.String id) {
		return new ArkWizardOverviewPanel(id);
	}

	/**
	 * @param resultListContainer
	 *           the resultListContainer to set
	 */
	public void setResultListContainer(WebMarkupContainer resultListContainer) {
		this.resultListContainer = resultListContainer;
	}

	/**
	 * @return the resultListContainer
	 */
	public WebMarkupContainer getResultListContainer() {
		return resultListContainer;
	}

	/**
	 * @param wizardPanelContainer
	 *           the wizardPanelContainer to set
	 */
	public void setWizardPanelContainer(WebMarkupContainer wizardPanelContainer) {
		this.wizardPanelContainer = wizardPanelContainer;
	}

	/**
	 * @return the wizardPanelContainer
	 */
	public WebMarkupContainer getWizardPanelContainer() {
		return wizardPanelContainer;
	}

	/**
	 * @param wizardPanelFormContainer
	 *           the wizardPanelFormContainer to set
	 */
	public void setWizardPanelFormContainer(WebMarkupContainer wizardPanelFormContainer) {
		this.wizardPanelFormContainer = wizardPanelFormContainer;
	}

	/**
	 * @return the wizardPanelFormContainer
	 */
	public WebMarkupContainer getWizardPanelFormContainer() {
		return wizardPanelFormContainer;
	}

	/**
	 * @param searchPanelContainer
	 *           the searchPanelContainer to set
	 */
	public void setSearchPanelContainer(WebMarkupContainer searchPanelContainer) {
		this.searchPanelContainer = searchPanelContainer;
	}

	/**
	 * @return the searchPanelContainer
	 */
	public WebMarkupContainer getSearchPanelContainer() {
		return searchPanelContainer;
	}

	/**
	 * @param target
	 *           the target to set
	 */
	public void setTarget(AjaxRequestTarget target) {
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public AjaxRequestTarget getTarget() {
		return target;
	}

	/**
	 * @param wizardSteps
	 *           the wizardSteps to set
	 */
	public void setWizardSteps(WizardStep[] wizardSteps) {
		this.wizardSteps = wizardSteps;
	}

	/**
	 * @return the wizardSteps
	 */
	public WizardStep[] getWizardSteps() {
		return wizardSteps;
	}

	/**
	 * @param buttonBar
	 *           the buttonBar to set
	 */
	public void setButtonBar(AjaxWizardButtonBar buttonBar) {
		this.buttonBar = buttonBar;
	}

	/**
	 * @return the buttonBar
	 */
	public AjaxWizardButtonBar getButtonBar() {
		return buttonBar;
	}
}
