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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class AbstractWizardHeaderPanel extends Panel {

	private static final long	serialVersionUID	= -626989967032928525L;

	/**
	 * A summary of this step, or some usage advice.
	 */
	private String					summary;

	/**
	 * The title of this step.
	 */
	private String					title;

	public AbstractWizardHeaderPanel(String id) {
		super(id);

		add(new Label("title", new AbstractReadOnlyModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public String getObject() {
				return getTitle();
			}
		}).setEscapeModelStrings(false));
		add(new Label("summary", new AbstractReadOnlyModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public String getObject() {
				return getSummary();
			}
		}).setEscapeModelStrings(false));
	}

	public AbstractWizardHeaderPanel(String id, String summary, String title) {
		super(id);

		this.title = title;
		this.summary = summary;

		add(new Label("title", new AbstractReadOnlyModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public String getObject() {
				return getTitle();
			}
		}).setEscapeModelStrings(false));
		add(new Label("summary", new AbstractReadOnlyModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public String getObject() {
				return getSummary();
			}
		}).setEscapeModelStrings(false));
	}

	/**
	 * Gets the summary of this step. This will be displayed in the title of the wizard while this step is active. The summary is typically an overview
	 * of the step or some usage guidelines for the user.
	 * 
	 * @return the summary of this step.
	 */
	public String getSummary() {
		return (summary != null) ? summary : null;
	}

	/**
	 * Gets the title of this step.
	 * 
	 * @return the title of this step.
	 */
	public String getTitle() {
		return (title != null) ? title : null;
	}

}
