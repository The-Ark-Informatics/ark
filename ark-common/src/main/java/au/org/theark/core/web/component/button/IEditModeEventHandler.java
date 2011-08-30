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
package au.org.theark.core.web.component.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * IEditModeEventHandler is an interface for use with the EditModeButtonsPanel
 * 
 * @author elam
 * @author cellis
 *
 */
public interface IEditModeEventHandler {
	/**
	 * Handle Save button's onSubmit
	 * @param target
	 * @param form
	 */
	void onEditSave(AjaxRequestTarget target, Form<?> form);
	
	/**
	 * Handle Cancel button's onSubmit
	 * @param target
	 * @param form
	 */
	void onEditCancel(AjaxRequestTarget target, Form<?> form);
	
	/**
	 * Handle Delete button's onSubmit
	 * @param target
	 * @param form
	 */
	void onEditDelete(AjaxRequestTarget target, Form<?> form);
	
	/**
	 * Handle Save button's onError
	 * @param target
	 * @param form
	 */
	void onEditSaveError(AjaxRequestTarget target, Form<?> form);
	
	/**
	 * Handle Cancel button's onError
	 * @param target
	 * @param form
	 */
	void onEditCancelError(AjaxRequestTarget target, Form<?> form);
	
	/**
	 * Handle Delete button's onError
	 * @param target
	 * @param form
	 */
	void onEditDeleteError(AjaxRequestTarget target, Form<?> form);
}