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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;

/**
 * <p>
 * An Abstract Form class for Search. This class contains common behaviour that the sub-classes can inherit. The sub-classes themselves can override
 * the behaviour of the abstract but can also add more specific implementation if needed. As part of this class we have defined the New,Search and
 * Reset button and their behaviour which will be common for all search functions.
 * </p>
 * 
 * @author nivedann
 * @param <T>
 * 
 */
public abstract class AbstractSearchForm<T> extends Form<T> {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -408051334961302312L;
	protected AjaxButton				searchButton;
	protected AjaxButton				newButton;
	protected AjaxButton				resetButton;
	protected FeedbackPanel			feedbackPanel;
	protected ArkCrudContainerVO arkCrudContainerVO;

	/**
	 * @param id
	 * @param cpmModel
	 */
	public AbstractSearchForm(String id, IModel<T> cpmModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpmModel);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseForm();
	}

	
	protected void onReset(AjaxRequestTarget target, Form<?> form) {
		clearInput();
		updateFormComponentModels();
		target.add(form);
	}

	/**
	 * Initialise the form, utilising the common ArkCrudContainerVO object
	 * 
	 * @param arkCrudContainerVO
	 */
	protected void initialiseForm() {
		searchButton = new ArkBusyAjaxButton(Constants.SEARCH) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8096410123770458109L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make the details panel visible
				onSearch(target);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};

		resetButton = new AjaxButton(Constants.RESET) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5818909400695185935L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onReset(target, form);				
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO 
				
			}
		};

		newButton = new ArkBusyAjaxButton(Constants.NEW) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1666656098281624401L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make the details panel visible, disabling delete button (if found)
				// AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
				if (ajaxButton != null) {
					ajaxButton.setEnabled(false);
					target.add(ajaxButton);
				}
				// Call abstract method
				onNew(target);
			}

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.NEW);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};

		addComponentsToForm();
	}

	protected void addComponentsToForm() {
		add(searchButton);
		add(resetButton.setDefaultFormProcessing(false));
		add(newButton);
	}

	/**
	 * Overloaded Method that uses the VO to set the WMC's
	 * 
	 * @param target
	 * @param flag
	 */
	protected void preProcessDetailPanel(AjaxRequestTarget target) {
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);

		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getViewButtonContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);

		target.add(arkCrudContainerVO.getDetailPanelFormContainer());
		target.add(arkCrudContainerVO.getDetailPanelContainer());

		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		target.add(arkCrudContainerVO.getSearchPanelContainer());
		target.add(arkCrudContainerVO.getViewButtonContainer());
		target.add(arkCrudContainerVO.getEditButtonContainer());
	}


	protected void disableSearchForm(Long sessionId, String errorMessage) {
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			if (sessionId == null) {
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else {
				arkCrudContainerVO.getSearchPanelContainer().setEnabled(true);
			}
		}
		else {
			arkCrudContainerVO.getSearchPanelContainer().setEnabled(false);
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	abstract protected void onSearch(AjaxRequestTarget target);

	abstract protected void onNew(AjaxRequestTarget target);
}
