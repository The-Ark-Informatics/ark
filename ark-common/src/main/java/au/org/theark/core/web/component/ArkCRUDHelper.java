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
package au.org.theark.core.web.component;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * @author nivedann
 * 
 */
public class ArkCRUDHelper {
	
	/**
	 * While navigating from a result list(Search result list panel) into a Detail Panel this method must be invoked
	 * by or within the onClick event handler.
	 * @param target
	 * @param arkCrudContainerVO
	 */
	public static void preProcessDetaiPanelOnSearchResults(AjaxRequestTarget target, ArkCrudContainerVO arkCrudContainerVO){
		// this check is required to make the delete button enabled if the user has Delete permissions. If the user had navigated from New to Detail Form first, 
		// The delete button will be disabled. When he cancels out and then clicks on an existing record, even if the user had permissions the delete button will be disabled.
		//The earlier code had two containers to manage this so it was possible. Now this has to be done through this method.
		if(ArkPermissionHelper.isActionPermitted(Constants.DELETE)){
			AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
			if (ajaxButton != null) {
				ajaxButton.setEnabled(true);
				target.add(ajaxButton);
			}
		}
		
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		target.add(arkCrudContainerVO.getSearchPanelContainer());
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelContainer());
	
	}

	
	/**
	 * This method should be invoked while the detail form's onBeforeRender is invoked on the AbstractDetailForm class.
	 * Based on the security it can call this method 
	 * @param arkCrudContainerVO
	 */
	public static void onBeforeRenderWithCRDPermissions(ArkCrudContainerVO arkCrudContainerVO){
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getEditButtonContainer().setEnabled(true);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
	}
	
	/**
	 * For cases where the Detail Panel must be disabled. E.g Lims Subject
	 * @param arkCrudContainerVO
	 * @param arkFunction
	 */
	public static void onBeforeRenderWithCRDPermissions(ArkCrudContainerVO arkCrudContainerVO, ArkFunction arkFunction){
		
		if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_SUBJECT)){
			
			AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
			deleteButton.setVisible(false);
			
			AjaxButton saveButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("save");
			saveButton.setVisible(false);
			
			arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
			arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
			arkCrudContainerVO.getEditButtonContainer().setVisible(true);
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		}
	}
	
	/**
	 * A method that can be invoked onBeforeRender of the DetailFomr's abstract class when a user has only Read permissions.
	 * @param arkCrudContainerVO
	 */
	public static void onBeforeRenderWithReadPermission(ArkCrudContainerVO arkCrudContainerVO){
		
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		
	}
}
