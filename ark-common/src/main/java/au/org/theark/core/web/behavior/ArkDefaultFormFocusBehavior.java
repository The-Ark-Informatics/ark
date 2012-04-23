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
import org.apache.wicket.markup.html.IHeaderResponse;

public class ArkDefaultFormFocusBehavior extends Behavior {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8530926132995921828L;
	private Component				component;

	public void bind(Component component) {
		this.component = component;
		component.setOutputMarkupId(true);
	}

	public void renderHead(IHeaderResponse iHeaderResponse) {
		super.renderHead(component,iHeaderResponse);
		iHeaderResponse.renderOnLoadJavaScript("document.getElementById('" + component.getMarkupId() + "').focus();");
	}
}
