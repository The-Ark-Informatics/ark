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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import au.org.theark.core.Constants;

public abstract class SelectModalWindow extends ModalWindow {
	public SelectModalWindow(String id) {
		super(id);

		// Set sizes of this ModalWindow. You can also do this from the HomePage
		// but its not a bad idea to set some good default values.
		setInitialWidth(300);
		setInitialHeight(150);

		setTitle(Constants.DELETE_CONFIRM_TITLE);

		// Set the content panel, implementing the abstract methods
		setContent(new SelectContentPanel(this.getContentId()) {
			void onCancel(AjaxRequestTarget target) {
				SelectModalWindow.this.onCancel(target);
			}

			void onSelect(AjaxRequestTarget target, String selection) {
				SelectModalWindow.this.onSelect(target, selection);
			}
		});
	}

	protected abstract void onCancel(AjaxRequestTarget target);

	protected abstract void onSelect(AjaxRequestTarget target, String selection);

}
