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
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * Abstract class that extends AbstractDetailForm, for specific implementation for the UserDetailForm
 * 
 * @author nivedann
 * @param <T>
 * 
 */
public abstract class AbstractUserDetailForm<T> extends AbstractDetailForm<T> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1768344112045735740L;

	/**
	 * Default constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public AbstractUserDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, Form<T> containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
	}

	protected void initialiseForm(Boolean isArkCrudContainerVOPattern) {

		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null)) {
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (isNew()) {
					editCancelProcess(target, true);
				}
				else {
					editCancelProcessForUpdate(target);
				}
			}
		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -423605230448635419L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(containerForm, target);
				target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				saveOnErrorProcess(target);
			}
		};

		deleteButton = new AjaxButton(Constants.DELETE, new StringResourceModel("deleteKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -2430231894703055744L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// target.addComponent(detailPanelContainer);
				onDelete(containerForm, target);
			}

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.DELETE);
			}
		};

		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6282464357368710796L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				editButtonProcess(target);
				// Add the sub-class functionality
				onEditButtonClick();
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}
		};

		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				editCancelProcess(target, true);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};

		selectModalWindow = initialiseModalWindow();

		addComponentsToForm(true);
	}

	/**
	 * Abstract method that allows sub-classes to implement specific functionality for onEditButtonClick event.
	 */
	public abstract void onEditButtonClick();
}
