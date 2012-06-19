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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.button.AjaxDeleteButton;

/**
 * Abstract class that extends AbstractDetailForm, for specific implementation for the UserDetailForm
 * 
 * @author nivedann
 * @param <T>
 * 
 */
public abstract class AbstractUserDetailForm<T> extends AbstractDetailForm<T> {

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

	protected void initialiseRemoveButton() {


		deleteButton = new AjaxDeleteButton(Constants.REMOVE, new StringResourceModel("confirmRemove", this, null), new StringResourceModel("removeUserKey", this, null)) {

			private static final long	serialVersionUID	= -2430231894703055744L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onDeleteConfirmed(target, null); 
			}

			@Override
			public boolean isVisible() {
				boolean flag = false;
				ArkUserVO arkUserVo = (ArkUserVO) containerForm.getModelObject();
				flag = arkUserVo.getStudy().getId() != null;
				return (flag && ArkPermissionHelper.isActionPermitted(Constants.DELETE));
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		arkCrudContainerVO.getEditButtonContainer().remove(Constants.DELETE);
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(deleteButton.setDefaultFormProcessing(false));
		
	}
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		
	}
	
	protected void editButtonProcess(AjaxRequestTarget target) {
		//Set the deleteButton to enabled true only if the user is attached to the study.
		enableOrDisableRemoveButton();
		// The visibility of the delete button should not be changed from
		// any of the abstract classes. This allows the implementation
		// to control the visibility of the delete button.
		// NB: SearchForm onNew has the Delete button's setEnabled(false)
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
	
		target.add(arkCrudContainerVO.getEditButtonContainer());
		target.add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	public abstract void enableOrDisableRemoveButton();

	/**
	 * Abstract method that allows sub-classes to implement specific functionality for onEditButtonClick event.
	 */
	//public abstract void onEditButtonClick();
}
