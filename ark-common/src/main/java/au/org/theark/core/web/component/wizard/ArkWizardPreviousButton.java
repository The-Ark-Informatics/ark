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

import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.WizardButton;

public class ArkWizardPreviousButton extends WizardButton {
	private static final long	serialVersionUID	= 1L;

	public ArkWizardPreviousButton(String id, IWizard wizard) {
		super(id, wizard, "org.apache.wicket.extensions.wizard.previous");
	}

	public boolean isEnabled() {
		return getWizardModel().isLastAvailable();
	}

	public boolean isVisible() {
		return getWizardModel().isLastVisible();
	}

	public void onClick() {
		IWizardModel wizardModel = getWizardModel();
		wizardModel.getActiveStep().applyState();
		wizardModel.previous();
	}
}
