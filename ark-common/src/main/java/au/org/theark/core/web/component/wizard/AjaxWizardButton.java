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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;

@SuppressWarnings("unchecked")
public abstract class AjaxWizardButton extends AjaxButton {

	private static final long	serialVersionUID	= 1L;
	private final IWizard		wizard;

	public AjaxWizardButton(String id, IWizard wizard, final Form form, String labelResourceKey) {
		super(id, form);
		this.setLabel(new ResourceModel(labelResourceKey));
		this.wizard = wizard;
	}

	public AjaxWizardButton(String id, IWizard wizard, String labelResourceKey) {
		this(id, wizard, null, labelResourceKey);
	}

	protected final IWizard getWizard() {
		return wizard;
	}

	protected final IWizardModel getWizardModel() {
		return getWizard().getWizardModel();
	}

	protected final void onSubmit(AjaxRequestTarget target, Form form) {
		onClick(target, form);
	}

	protected abstract void onClick(AjaxRequestTarget target, Form form);
}
