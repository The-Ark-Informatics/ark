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
package au.org.theark.core.web.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;

public class ArkValidationMsgBehavior extends Behavior {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2591693136424670884L;

	public void onRendered(Component c) {
		try {
			FormComponent<?> fc = (FormComponent<?>) c;
			if (!fc.isValid()) {
				String error;
				if (fc.hasFeedbackMessage()) {
					error = fc.getFeedbackMessage().getMessage().toString();
				}
				else {
					error = "Your input is invalid.";
				}
				fc.getResponse().write("<div class=\"validationMsg\">" + error + "</div>");
			}
		}
		catch (ClassCastException cce) {
			// ignore non FormComponent Objects
		}
	}
}
