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
package au.org.theark.web.pages.mydetails;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.AbstractDetailModalWindow;

/**
 * @author cellis
 *
 */
public class MyDetailModalWindow extends AbstractDetailModalWindow {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4739679880093551297L;
	private Panel					panel;
	private Form<?>				form;

	public MyDetailModalWindow(String id) {
		super(id);
	}

	protected void onCloseModalWindow(AjaxRequestTarget target) {
		target.add(form);
		target.add(panel);
	}

	/**
	 * @return the panel
	 */
	public Panel getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	/**
	 * @return the form
	 */
	public Form<?> getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(Form<?> form) {
		this.form = form;
	}
}
