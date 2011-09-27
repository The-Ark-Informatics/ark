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
package au.org.theark.core.web.form;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.component.worksheet.ArkExcelWorkSheetAsGrid;

/**
 * <p>
 * Abstract class for Wizard Form sub-classes. It provides some common functionality that sub-classes inherit. Defines the core buttons like:
 * Previous,Next,Cancel, and Finish. Provides methods to access buttons for sub-class definition of use (e.g. disable Previous) Provides method to
 * toggle the view from read only to edit mode which is usually a common behaviour the sub-classes can re-use.
 * </p>
 * 
 * @author cellis
 * @param <T>
 * 
 */
@SuppressWarnings({ "unchecked" })
// TODO: Fix the AbstractWizardForm to use ArkCrudContainerVO
public abstract class AbstractWizardForm<T> extends Form<T> {

	private static final long			serialVersionUID	= 1L;

	private static final Logger		log					= LoggerFactory.getLogger(AbstractWizardForm.class);

	protected Form<T>						containerForm;
	protected FeedbackPanel				feedBackPanel;
	protected WebMarkupContainer		resultListContainer;
	protected WebMarkupContainer		searchPanelContainer;
	protected WebMarkupContainer		wizardPanelContainer;
	protected WebMarkupContainer		wizardPanelFormContainer;
	protected WebMarkupContainer		wizardButtonContainer;

	private ArkExcelWorkSheetAsGrid	arkExcelWorkSheetAsGrid;
	private AjaxButton					nextButton;
	private AjaxLink						previousLink;
	private AjaxLink						cancelLink;
	private AjaxButton					finishButton;

	private boolean						cancelled			= false;

	// Add a visitor class for required field marking/validation/highlighting
	ArkFormVisitor							formVisitor			= new ArkFormVisitor();

	protected ArkCrudContainerVO		arkCrudContainerVO;																		// Use this for the model where

	// WebMarkupContainers are set inside this
	// VO

	public AbstractWizardForm(String id) {
		this(id, null);
	}

	public AbstractWizardForm(String id, IModel model) {
		super(id, model);
		setOutputMarkupId(true);
		setMultiPart(true);
		initialiseForm();
	}

	/**
	 * Constructor for AbstractWizardForm class
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param wizardPanelContainer
	 * @param wizardPanelFormContainer
	 * @param searchPanelContainer
	 * @param wizardButtonContainer
	 * @param containerForm
	 */
	public AbstractWizardForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer resultListContainer, WebMarkupContainer wizardPanelContainer, WebMarkupContainer wizardPanelFormContainer,
			WebMarkupContainer searchPanelContainer, Form<T> containerForm) {
		super(id);
		this.resultListContainer = resultListContainer;
		this.wizardPanelContainer = wizardPanelContainer;
		this.feedBackPanel = feedBackPanel;
		this.searchPanelContainer = searchPanelContainer;
		this.wizardPanelFormContainer = wizardPanelFormContainer;
		this.containerForm = containerForm;
	
		setOutputMarkupId(true);
		setMultiPart(true);
		initialiseForm();
		initialiseGridView();
		addFormComponents();

		disableWizardForm();
	}

	protected void initialiseForm() {
		// previous button
		previousLink = createPrevious();
		previousLink.setVisible(false);
		previousLink.setEnabled(false);
		previousLink.setOutputMarkupId(true);
		previousLink.setOutputMarkupPlaceholderTag(true);
		// next button
		nextButton = createNext();
		nextButton.setOutputMarkupId(true);
		nextButton.setOutputMarkupPlaceholderTag(true);
		// cancel button
		cancelLink = createCancel();
		// finish button
		finishButton = createFinish();
		finishButton.setVisible(true);
		finishButton.setEnabled(false);
		finishButton.setOutputMarkupId(true);
		finishButton.setOutputMarkupPlaceholderTag(true);
	}

	/**
	 * Implement this to add all the form components/objects
	 */
	protected void addFormComponents() {
		// Web mark up for buttons
		wizardButtonContainer = new WebMarkupContainer("wizardButtonContainer");
		wizardButtonContainer.setOutputMarkupPlaceholderTag(true);
		wizardButtonContainer.setVisible(true);

		// Add buttons
		wizardButtonContainer.add(finishButton);
		wizardButtonContainer.add(previousLink);
		wizardButtonContainer.add(nextButton);
		wizardButtonContainer.add(cancelLink);
		add(wizardButtonContainer);
	}

	private void initialiseGridView() {
		arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView");
		arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
		arkExcelWorkSheetAsGrid.setVisible(false);
		wizardPanelFormContainer.addOrReplace(arkExcelWorkSheetAsGrid);
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	public WebMarkupContainer getWizardButtonContainer() {
		return wizardButtonContainer;
	}

	public void setWizardButtonContainer(WebMarkupContainer wizardButtonContainer) {
		this.wizardButtonContainer = wizardButtonContainer;
	}

	public WebMarkupContainer getWizardPanelFormContainer() {
		return wizardPanelFormContainer;
	}

	public void setWizardPanelFormContainer(WebMarkupContainer wizardPanelFormContainer) {
		this.wizardPanelFormContainer = wizardPanelFormContainer;
	}

	public AjaxButton getNextButton() {
		return nextButton;
	}

	public void setNextButton(AjaxButton nextButton) {
		this.nextButton = nextButton;
	}

	public void setPreviousLink(AjaxLink previousLink) {
		this.previousLink = previousLink;
	}

	public ArkExcelWorkSheetAsGrid getArkExcelWorkSheetAsGrid() {
		return arkExcelWorkSheetAsGrid;
	}

	public void setArkExcelWorkSheetAsGrid(ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid) {
		this.arkExcelWorkSheetAsGrid = arkExcelWorkSheetAsGrid;
	}

	protected void onCancelPostProcess(AjaxRequestTarget target) {
		initialiseGridView();
		wizardPanelContainer.setVisible(true);
		resultListContainer.setVisible(true);

		target.add(feedBackPanel);
		target.add(wizardPanelContainer);
		target.add(wizardPanelFormContainer);
		target.add(resultListContainer);
	}

	private AjaxButton createNext() {
		nextButton = new ArkBusyAjaxButton("next", new StringResourceModel("wizardNextKey", this, null)) {
			private static final long	serialVersionUID	= 0L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				onNextError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNextSubmit(target, form);
			}
		};

		return nextButton;
	}

	private AjaxLink createPrevious() {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("previous") {
			private static final long	serialVersionUID	= 0L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onPreviousClick(target);
			}
		};

		return link;
	}

	private AjaxButton createFinish() {
		finishButton = new AjaxButton("finish", new StringResourceModel("wizardFinishKey", this, null)) {

			private static final long	serialVersionUID	= 0L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onFinishSubmit(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				onFinishError(target, form);
			}
		};

		return finishButton;
	}

	private AjaxLink createCancel() {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("cancel") {
			private static final long	serialVersionUID	= 0L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onCancelClick(target);
			}
		};

		return link;
	}

	public LoadableDetachableModel getLabelModel(String label) {
		return new StringResourceModel(label, AbstractWizardForm.this, null);
	}

	//
	// Event form triggers
	//

	protected void onFinishSubmit(AjaxRequestTarget target, Form<?> form) {
		log.debug("finish.onSubmit");
		previousLink.setVisible(false);
		nextButton.setVisible(true);
		nextButton.setEnabled(true);
		cancelLink.setVisible(true);
		cancelLink.setEnabled(true);
		finishButton.setVisible(true);
		finishButton.setEnabled(false);

		searchPanelContainer.setVisible(true);
		target.add(wizardButtonContainer);
		target.add(resultListContainer);
		onFinish(target, form);
	}

	protected void onFinishError(AjaxRequestTarget target, Form<?> form) {
		log.debug("finish.onError");
		AbstractWizardForm.this.onError(target, form);
	}

	protected void onPreviousClick(AjaxRequestTarget target) {
		HistoryAjaxBehavior historyAjaxBehavior = getHistoryAjaxBehavior();
		if (historyAjaxBehavior != null) {
			historyAjaxBehavior.registerAjaxEvent(target, this);
		}
		AbstractWizardForm.this.gotoPrevious(target);
	}

	protected void onNextSubmit(AjaxRequestTarget target, Form<?> form) {
		log.debug("next.onSubmit");
		HistoryAjaxBehavior historyAjaxBehavior = getHistoryAjaxBehavior();
		if (historyAjaxBehavior != null) {
			historyAjaxBehavior.registerAjaxEvent(target, this);
		}

		// Make search results hidden until finish or cancel
		resultListContainer.setVisible(false);
		target.add(resultListContainer);

		AbstractWizardForm.this.gotoNext(target);
	}

	protected void onNextError(AjaxRequestTarget target, Form<?> form) {
		log.debug("next.onError");
		// Wizard steps are contained within a WebMarkupContainer
		WebMarkupContainer wmc = (WebMarkupContainer) get("wizardFormContainer");
		AbstractWizardStepPanel currentStep = (AbstractWizardStepPanel) wmc.get("step");
		currentStep.onStepOutNextError(AbstractWizardForm.this, target);
	}

	protected void onCancelClick(AjaxRequestTarget target) {
		cancelled = true;

		previousLink.setVisible(false);
		previousLink.setEnabled(false);
		nextButton.setVisible(true);
		nextButton.setEnabled(true);
		finishButton.setVisible(true);
		finishButton.setEnabled(false);
		resultListContainer.setVisible(true);

		target.add(wizardPanelFormContainer);
		target.add(wizardButtonContainer);
		target.add(resultListContainer);

		onCancel(target);
	}

	/**
	 * Warn the current step panel we are going out by next, and ask which is the next step.
	 * 
	 * @param target
	 */
	protected void gotoNext(AjaxRequestTarget target) {
		// Wizard steps are contained within a WebMarkupContainer
		WebMarkupContainer wmc = (WebMarkupContainer) get("wizardFormContainer");
		AbstractWizardStepPanel currentStep = (AbstractWizardStepPanel) wmc.get("step");

		log.debug("gotoNext.currentStep={}", currentStep.getClass().getName());

		// Handle wizard step state on Next press
		currentStep.onStepOutNext(AbstractWizardForm.this, target);
		currentStep.handleWizardState(this, target);

		AbstractWizardStepPanel next = currentStep.getNextStep();
		if (next != null) {
			next.onStepInNext(AbstractWizardForm.this, target);
			currentStep.replaceWith(next);

			// If no more steps, on final step
			if (next.getNextStep() == null) {
				nextButton.setEnabled(false);
				cancelLink.setEnabled(false);
				finishButton.setEnabled(true);
				arkExcelWorkSheetAsGrid.setEnabled(false);
				target.add(arkExcelWorkSheetAsGrid);
			}

			target.add(wizardButtonContainer);
		}
		target.add(wmc);
		target.add(feedBackPanel);

	}

	/**
	 * Warn the current step panel we are going out by previous, and ask which is the previous step.
	 * 
	 * @param target
	 */
	protected void gotoPrevious(AjaxRequestTarget target) {
		WebMarkupContainer wmc = (WebMarkupContainer) get("wizardFormContainer");
		AbstractWizardStepPanel currentStep = (AbstractWizardStepPanel) wmc.get("step");
		log.debug("gotoPrevious.currentStep={}", currentStep.getClass().getName());
		currentStep.onStepOutPrevious(AbstractWizardForm.this, target);

		AbstractWizardStepPanel previous = currentStep.getPreviousStep();
		if (previous != null) {
			currentStep.replaceWith(previous);
			previous.onStepInPrevious(this, target);
			previous.handleWizardState(this, target);
		}
		target.add(wmc);
		target.add(feedBackPanel);
	}

	public HistoryAjaxBehavior getHistoryAjaxBehavior() {
		// Start here
		Component current = getParent();

		// Walk up containment hierarchy
		while (current != null) {
			// Is current an instance of this class?
			if (IHistoryAjaxBehaviorOwner.class.isInstance(current)) {
				return ((IHistoryAjaxBehaviorOwner) current).getHistoryAjaxBehavior();
			}

			// Check parent
			current = current.getParent();
		}
		return null;
	}

	protected boolean isActionPermitted(String actionType) {
		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (actionType.equalsIgnoreCase(Constants.SAVE)) {
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE) || securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE)) {

				flag = true;
			}
			else {
				flag = false;
			}
		}
		else if (actionType.equalsIgnoreCase(Constants.EDIT)) {

			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE)) {
				flag = true;
			}
			else {
				flag = false;
			}
		}
		else if (actionType.equalsIgnoreCase(Constants.DELETE)) {
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE)) {
				flag = true;
			}
			else {
				flag = false;
			}
		}

		return flag;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Get the "next" component.
	 * 
	 * @return
	 */
	public Component getNextLink() {
		return get("nextLink");
	}

	/**
	 * Get the "previous" component.
	 * 
	 * @return
	 */
	public Component getPreviousLink() {
		return get("previousLink");
	}

	/**
	 * Get the "finish" component.
	 * 
	 * @return
	 */
	public Component getFinishLink() {
		return get("finish");
	}

	/**
	 * Get the "cancel" component.
	 * 
	 * @return
	 */
	public Component getCancelLink() {
		return get("cancelLink");
	}

	/**
	 * Get the id of the step.
	 * 
	 * @return
	 */
	public static String getStepId() {
		return "step";
	}

	/**
	 * Called to change/overrride the css of the Wizard.
	 * 
	 * @param target
	 */
	public void changeWizardFormStyle(String cssClassName) {
		add(new AttributeModifier("class", new Model(cssClassName)));
	}

	/**
	 * Called after wizard form submission generates an error (on next or finish click).
	 * 
	 * @param target
	 * @param form
	 */
	public abstract void onError(AjaxRequestTarget target, Form form);

	/**
	 * Called when finish is clicked.
	 * 
	 * @param target
	 * @param form
	 */
	public abstract void onFinish(AjaxRequestTarget target, Form form);

	/**
	 * Called when cancel is clicked.
	 * 
	 * @param target
	 */
	protected abstract void onCancel(AjaxRequestTarget target);

	/**
	 * Called to handle errors.
	 * 
	 * @param target
	 */
	protected abstract void processErrors(AjaxRequestTarget target);

	/**
	 * Called to disable entire WizardForm, and display reason.
	 */
	protected void disableWizardForm() {
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW)) {
			this.setEnabled(true);
		}
		else {
			this.setEnabled(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	/**
	 * Called to disable entire WizardForm, and display reason.
	 * 
	 * @param sessionId
	 * @param errorMessage
	 */
	protected void disableWizardForm(Long sessionId, String errorMessage) {
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW)) {
			if (sessionId == null) {
				this.setEnabled(false);
				this.error(errorMessage);
			}
			else {
				this.setEnabled(true);
			}
		}
		else {
			this.setEnabled(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	/**
	 * Called to disable entire WizardForm, and display reason.
	 * 
	 * @param sessionId
	 * @param errorMessage
	 * @param arkCrudContainserVO
	 */
	protected void disableWizardForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVO) {
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW)) {
			if (sessionId == null) {
				arkCrudContainerVO.getWizardPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else {
				arkCrudContainerVO.getWizardPanelContainer().setEnabled(true);
			}
		}
		else {
			arkCrudContainerVO.getWizardPanelContainer().setEnabled(false);
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}
}
