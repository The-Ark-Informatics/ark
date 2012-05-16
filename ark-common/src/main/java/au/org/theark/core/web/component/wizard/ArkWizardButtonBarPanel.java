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

import org.apache.wicket.extensions.wizard.CancelButton;
import org.apache.wicket.extensions.wizard.FinishButton;
import org.apache.wicket.extensions.wizard.NextButton;
import org.apache.wicket.extensions.wizard.PreviousButton;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.Constants;

@SuppressWarnings("serial")
public class ArkWizardButtonBarPanel extends Panel {
	public ArkWizardButtonBarPanel(String id, Wizard wizard) {
		super(id);
		add(new PreviousButton(Constants.PREVIOUS, wizard));
		add(new NextButton(Constants.NEXT, wizard));
		add(new ArkWizardLastButton(Constants.LAST, wizard));
		add(new CancelButton(Constants.CANCEL, wizard));
		add(new FinishButton(Constants.FINISH, wizard));
	}
}
