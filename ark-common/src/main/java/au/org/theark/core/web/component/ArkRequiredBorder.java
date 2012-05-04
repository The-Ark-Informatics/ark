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

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.BorderBehavior;
import org.apache.wicket.markup.html.form.FormComponent;

public class ArkRequiredBorder extends BorderBehavior {

	private static final long	serialVersionUID	= -1568367562733328792L;

	@Override
	public void afterRender(Component component) {
		try {
			FormComponent<?> fc = (FormComponent<?>) component;
			if (fc.isRequired()) {
				super.afterRender(component);
			}
		}
		catch (ClassCastException cce) {
			// ignore non FormComponent Objects
		}
	}
}
