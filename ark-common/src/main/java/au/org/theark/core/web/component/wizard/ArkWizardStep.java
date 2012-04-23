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
package au.org.theark.core.web.component.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings({ "unchecked", "serial" })
public class ArkWizardStep extends WizardStep {
	public ArkWizardStep(Object object1) {
		setDefaultModel(new CompoundPropertyModel(object1));
		add(new Label("elementlabel"));
		add(new Label("elementdescription").setEscapeModelStrings(false));
		TextField theField = new TextField("elementvalue", new PropertyModel(object1, "elementvalue"));
		theField.setRequired(true);
		add(theField);
	}

	/**
	 * This is called when the next button that was clicked, I assume you want to use your for for data gathering.. Normally you’d save the object the
	 * the database Or just leave it in memory and save it in the Wizard onFinish()
	 */
	public void applyState() {
		System.err.println("Applying state!");
		setComplete(true); // Set this step as done, you should put custom logic here
	}
}
