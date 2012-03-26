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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.extensions.wizard.CancelButton;
import org.apache.wicket.extensions.wizard.FinishButton;
import org.apache.wicket.extensions.wizard.IDefaultButtonProvider;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.LastButton;
import org.apache.wicket.extensions.wizard.NextButton;
import org.apache.wicket.extensions.wizard.PreviousButton;
import org.apache.wicket.extensions.wizard.WizardButton;
import org.apache.wicket.extensions.wizard.WizardButtonBar;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * A bar of buttons for wizards utilizing {@link AjaxFormSubmitBehavior}.
 * 
 * @see WizardButtonBar
 */
public class AjaxWizardButtonBar extends Panel implements IDefaultButtonProvider {
	private static final long	serialVersionUID	= 1L;

	private ArkCommonWizard		wizard;
	private PreviousButton		previousButton;
	private NextButton			nextButton;
	private LastButton			lastButton;
	private CancelButton			cancelButton;
	private FinishButton			finishButton;

	/**
	 * Construct.
	 * 
	 * @param id
	 *           The component id
	 * @param wizard
	 *           The containing wizard
	 */
	public AjaxWizardButtonBar(String id, ArkCommonWizard wizard) {
		super(id);

		this.wizard = wizard;
		wizard.setOutputMarkupId(true);

		previousButton = new PreviousButton("previous", wizard);
		addAjax(previousButton);

		nextButton = new NextButton("next", wizard);
		addAjax(nextButton);

		lastButton = new LastButton("last", wizard);
		addAjax(lastButton);

		cancelButton = new CancelButton("cancel", wizard);
		addAjaxCancel(cancelButton);

		finishButton = new FinishButton("finish", wizard);
		addAjaxFinish(finishButton);
	}

	private void addAjax(final WizardButton button) {
		button.add(new AjaxFormSubmitBehavior("onclick") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected CharSequence getEventHandler() {
				AppendingStringBuffer handler = new AppendingStringBuffer();
				handler.append(super.getEventHandler());
				handler.append("; return false;");
				return handler;
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return AjaxWizardButtonBar.this.getAjaxCallDecorator();
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(wizard);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				target.add(wizard);
			}
		});

		add(button);
	}

	private void addAjaxCancel(final WizardButton button) {
		button.add(new AjaxFormSubmitBehavior("onclick") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected CharSequence getEventHandler() {
				AppendingStringBuffer handler = new AppendingStringBuffer();
				handler.append(super.getEventHandler());
				handler.append("; return false;");
				return handler;
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return AjaxWizardButtonBar.this.getAjaxCallDecorator();
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				wizard.getWizardModel().cancel();
				wizard = new ArkCommonWizard(wizard.getId(), wizard.getWizardSteps(), wizard.getResultListContainer(), wizard.getWizardPanelContainer(), wizard.getWizardPanelFormContainer(), wizard
						.getSearchPanelContainer(), wizard.getTarget());

				wizard.getSearchPanelContainer().setVisible(true);
				wizard.getResultListContainer().setVisible(true);
				wizard.getWizardPanelContainer().setVisible(true);

				cancelWizard(target);

				target.add(wizard.getSearchPanelContainer());
				target.add(wizard.getResultListContainer());
				target.add(wizard.getWizardPanelContainer());
				target.add(wizard);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				target.add(wizard);
			}
		});

		add(button);
	}

	private void addAjaxFinish(final WizardButton button) {
		button.add(new AjaxFormSubmitBehavior("onclick") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected CharSequence getEventHandler() {
				AppendingStringBuffer handler = new AppendingStringBuffer();
				handler.append(super.getEventHandler());
				handler.append("; return false;");
				return handler;
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return AjaxWizardButtonBar.this.getAjaxCallDecorator();
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				wizard.getSearchPanelContainer().setVisible(true);
				wizard.getResultListContainer().setVisible(true);
				wizard.getWizardPanelContainer().setVisible(true);

				wizard.getWizardModel().finish();

				target.add(wizard.getSearchPanelContainer());
				target.add(wizard.getResultListContainer());
				target.add(wizard.getWizardPanelContainer());
				target.add(wizard);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				target.add(wizard);
			}
		});

		add(button);
	}

	/**
	 * @see org.apache.wicket.extensions.wizard.IDefaultButtonProvider#getDefaultButton(org.apache.wicket.extensions.wizard.IWizardModel)
	 */
	public IFormSubmittingComponent getDefaultButton(IWizardModel model) {
		if (model.isNextAvailable()) {
			return (Button) get("next");
		}
		else if (model.isLastAvailable()) {
			return (Button) get("last");
		}
		else if (model.isLastStep(model.getActiveStep())) {
			return (Button) get("finish");
		}
		return null;
	}

	/**
	 * 
	 * @return call decorator to use or null if none
	 */
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return null;
	}

	public void finishWizard(AjaxRequestTarget target) {

	}

	public void cancelWizard(AjaxRequestTarget target) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param previousButton
	 *           the previousButton to set
	 */
	public void setPreviousButton(PreviousButton previousButton) {
		this.previousButton = previousButton;
	}

	/**
	 * @return the previousButton
	 */
	public PreviousButton getPreviousButton() {
		return previousButton;
	}

	/**
	 * @return the nextButton
	 */
	public NextButton getNextButton() {
		return nextButton;
	}

	/**
	 * @param nextButton
	 *           the nextButton to set
	 */
	public void setNextButton(NextButton nextButton) {
		this.nextButton = nextButton;
	}

	/**
	 * @param lastButton
	 *           the lastButton to set
	 */
	public void setLastButton(LastButton lastButton) {
		this.lastButton = lastButton;
	}

	/**
	 * @return the lastButton
	 */
	public LastButton getLastButton() {
		return lastButton;
	}

	/**
	 * @param cancelButton
	 *           the cancelButton to set
	 */
	public void setCancelButton(CancelButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * @return the cancelButton
	 */
	public CancelButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * @param finishButton
	 *           the finishButton to set
	 */
	public void setFinishButton(FinishButton finishButton) {
		this.finishButton = finishButton;
	}

	/**
	 * @return the finishButton
	 */
	public FinishButton getFinishButton() {
		return finishButton;
	}

}
