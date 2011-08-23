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

import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Base Abstract Wizard class using panels for each step
 * 
 * @author cellis
 */
@SuppressWarnings("serial")
public class ArkWizardWithPanels extends Wizard {
	private Class<ArkWizardIndex>	wizardIndexClass;
	private WebMarkupContainer		wizardBasePanel;

	protected WebMarkupContainer	resultListContainer;
	protected WebMarkupContainer	wizardPanelContainer;
	protected WebMarkupContainer	searchPanelContainer;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;
	protected WebMarkupContainer	wizardPanelFormContainer;
	protected FeedbackPanel			feedBackPanel;

	/**
	 * The first step of this wizard.
	 */
	private static final class Step1 extends WizardStep {
		/**
		 * Construct.
		 */
		public Step1() {
			super("One", "The first step");
		}
	}

	/**
	 * The second step of this wizard.
	 */
	private static final class Step2 extends WizardStep {
		/**
		 * Construct.
		 */
		public Step2() {
			super("Two", "The second step");
		}
	}

	/**
	 * The third step of this wizard.
	 */
	private static final class Step3 extends WizardStep {
		/**
		 * Construct.
		 */
		public Step3() {
			super("Three", "The third step");
		}
	}

	/**
	 * The fourth step of this wizard.
	 */
	private static final class Step4 extends WizardStep {
		/**
		 * Construct.
		 */
		public Step4() {
			super("Four", "The fourth step");
		}
	}

	/**
	 * The fifth step of this wizard.
	 */
	private static final class Step5 extends WizardStep {
		/**
		 * Construct.
		 */
		public Step5() {
			super("Fifth", "The fifth step");
		}
	}

	/**
	 * Default Constructor
	 * 
	 * @param id
	 *           The component id
	 */
	public ArkWizardWithPanels(String id) {
		super(id);
		wizardIndexClass = ArkWizardIndex.class;

		// create a model with a couple of custom panels
		// still not that spectacular, but at least it
		// will give you a hint of how nice it is to
		// be able to work with custom panels
		WizardModel model = new WizardModel();
		model.add(new Step1());
		model.add(new Step2());
		model.add(new Step3());
		model.add(new Step4());
		model.add(new Step5());

		// initialize the wizard
		init(model);
	}

	/**
	 * Constructor with specified wizard home/index page
	 * 
	 * @param id
	 *           The component id
	 * @param wizardIndexClass
	 *           The home/index page the wizard will go to on finish
	 */
	public ArkWizardWithPanels(String id, Class<ArkWizardIndex> wizardIndexClass) {
		super(id);

		// create a model with a couple of custom panels
		// still not that spectacular, but at least it
		// will give you a hint of how nice it is to
		// be able to work with custom panels
		WizardModel model = new WizardModel();
		model.add(new Step1());
		model.add(new Step2());
		model.add(new Step3());
		model.add(new Step4());
		model.add(new Step5());

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
	public ArkWizardWithPanels(String id, WizardStep[] wizardSteps) {
		// FeedbackPanel feedBackPanel, WebMarkupContainer resultListContainer, WebMarkupContainer wizardPanelContainer, WebMarkupContainer
		// wizardPanelFormContainer,WebMarkupContainer searchPanelContainer
		super(id);
		// this.feedBackPanel = feedBackPanel;
		// this.resultListContainer = resultListContainer;
		// this.wizardPanelContainer = wizardPanelContainer;
		// this.wizardPanelFormContainer = wizardPanelFormContainer;
		// this.searchPanelContainer = searchPanelContainer;

		// create a model with the specified steps
		WizardModel model = new WizardModel();

		for (int i = 0; i < wizardSteps.length; i++) {
			model.add(wizardSteps[i]);
		}

		// initialize the wizard
		init(model);
	}

	// /**
	// * @see org.apache.wicket.extensions.wizard.Wizard#onCancel()
	// */
	// @Override
	// public void onCancel()
	// {
	// setResponsePage(getWizardIndexClass());
	// }
	//
	// /**
	// * @see org.apache.wicket.extensions.wizard.Wizard#onFinish()
	// */
	// @Override
	// public void onFinish()
	// {
	// setResponsePage(getWizardIndexClass());
	// }

	/**
	 * @param arkWizardIndexClass
	 *           the wizardIndexClass to set
	 */
	public void setWizardIndexClass(Class<ArkWizardIndex> arkWizardIndexClass) {
		this.wizardIndexClass = arkWizardIndexClass;
	}

	/**
	 * @return the wizardIndexClass
	 */
	public Class<ArkWizardIndex> getWizardIndexClass() {
		return wizardIndexClass;
	}

	/**
	 * @param wizardBasePanel
	 *           the wizardBasePanel to set
	 */
	public void setWizardBasePanel(WebMarkupContainer wizardBasePanel) {
		this.wizardBasePanel = wizardBasePanel;
	}

	/**
	 * @return the wizardBasePanel
	 */
	public WebMarkupContainer getWizardBasePanel() {
		return wizardBasePanel;
	}
}
