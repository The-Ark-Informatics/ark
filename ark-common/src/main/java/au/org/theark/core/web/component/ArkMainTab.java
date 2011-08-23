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

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Abstract class for implementation of the main tab/modules at the "top" level of the application
 * 
 * @author cellis
 * 
 */
public abstract class ArkMainTab extends AbstractTab {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3346358837428172502L;
	private Panel					panel;

	/**
	 * Creates a new ArkMainTab object, extending AbstractTab
	 * 
	 * @param id
	 *           The id of the tab that Ajax will reference
	 */
	public ArkMainTab(IModel<String> id) {
		super(id);
	}

	/**
	 * Method that determines whether or not tab is clickable or not
	 */
	public abstract boolean isAccessible();

	/**
	 * Method that determines whether or not tab is visible or not
	 */
	public abstract boolean isVisible();

	@Override
	public Panel getPanel(String panelId) {
		if (panel == null) {
			// Lazily create the panel
			panel = createPanel();
			if (!TabbedPanel.TAB_PANEL_ID.equals(panel.getId())) {
				throw new IllegalArgumentException("Panel id must be TabbedPanel.TAB_PANEL_ID");
			}
			panel.setOutputMarkupId(true);
		}
		return panel;
	}

	protected Panel createPanel() {
		throw new IllegalArgumentException("Must provide a panel");
	}
}
