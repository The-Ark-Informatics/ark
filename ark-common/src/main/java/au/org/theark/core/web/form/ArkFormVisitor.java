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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import au.org.theark.core.web.behavior.ArkRequiredFieldHintBehavior;

@SuppressWarnings("unchecked")
public class ArkFormVisitor implements IVisitor<Component,Void>, Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1613309540699904032L;
	
	Set<Component>					visited				= new HashSet<Component>();


	public void component(final Component component, final IVisit<Void> visit) {
				
		if (!visited.contains(component)) {
			visited.add(component);
			if (component instanceof FormComponent) {
				// Force id's in HTML to allow for target.focusCompont to focus on fieds in error
				component.setOutputMarkupId(true);
				// Add a "*" after the required fields
				component.add(new ArkRequiredFieldHintBehavior());
			}
		}
	}
}
