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
package au.org.theark.core.service;

import org.apache.wicket.extensions.markup.html.tabs.ITab;

/**
 * Main interface for all Main Tabs in the application. All class implemtations must implement createTab()
 * 
 * @author nivedann
 * 
 */
public interface IMainTabProvider {

	/**
	 * Create the main menu tab.
	 * 
	 * @param tabName
	 *           The id/name of the tab
	 * @return the main tab
	 */
	public ITab createTab(String tabName);
}
