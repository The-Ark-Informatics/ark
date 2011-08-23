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
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.Constants;

@SuppressWarnings({ "serial", "unchecked" })
public abstract class SelectContentPanel extends Panel {
	Label	deleteMessage;

	public SelectContentPanel(String id) {
		super(id);

		// Create the form, to use later for the buttons
		Form form = new Form("confirmForm");
		add(form);

		deleteMessage = new Label("deleteMessage", Constants.DELETE_CONFIRM_MESSAGE);
		form.add(deleteMessage);

		form.add(new AjaxButton(Constants.OK) {
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				onSelect(target, new String("OK pressed"));
			}
		});

		// Add a cancel / close button.
		form.add(new AjaxButton(Constants.CANCEL) {
			public void onSubmit(AjaxRequestTarget target, Form form) {
				onCancel(target);
			}
		});

	}

	abstract void onCancel(AjaxRequestTarget target);

	abstract void onSelect(AjaxRequestTarget target, String selection);
}
