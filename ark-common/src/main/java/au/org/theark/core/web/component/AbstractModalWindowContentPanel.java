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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.form.AbstractModalDetailForm;

public abstract class AbstractModalWindowContentPanel extends Panel {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -4604115589812210436L;
	protected Label								label;
	protected AbstractModalDetailForm<?>	form;
	protected AbstractDetailModalWindow		modalWindow;
	protected FeedbackPanel						feedbackPanel;

	public AbstractModalWindowContentPanel(String id) {
		super(id);
		initialise();
	}

	public AbstractModalWindowContentPanel(String id, AbstractModalDetailForm<?> form) {
		super(id);
		this.form = form;
		initialise();
	}

	public AbstractModalWindowContentPanel(String id, AbstractModalDetailForm<?> form, AbstractDetailModalWindow modalWindow) {
		super(id);
		this.form = form;
		this.modalWindow = modalWindow;
		initialise();
	}

	public AbstractModalWindowContentPanel(String id, AbstractModalDetailForm<?> form, AbstractDetailModalWindow modalWindow, FeedbackPanel feedbackPanel) {
		super(id);
		this.form = form;
		this.modalWindow = modalWindow;
		this.feedbackPanel = feedbackPanel;
		initialise();
	}

	protected void initialise() {
		setOutputMarkupId(true);
		if (form == null) {
			Form form = new Form("detailForm");
			add(form);
		}
		else {
			form.getId();
			addOrReplace(form);
		}
	}
}
