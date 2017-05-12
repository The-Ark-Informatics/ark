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
package au.org.theark.core.web.component.button;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.model.IModel;

/**
 * AjaxButton that disables onSubmit, and renables once processing completed, also has an AJAX indicator
 */
public abstract class ArkIndicatingAjaxButton extends IndicatingAjaxButton {


	private static final long	serialVersionUID	= 5799668393803636626L;

	public ArkIndicatingAjaxButton(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
	}

	public ArkIndicatingAjaxButton(String id, IModel<String> model) {
		super(id, model, null);
		setOutputMarkupPlaceholderTag(true);
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
			private static final long	serialVersionUID	= 1L;

			@Override
			public CharSequence postDecorateScript(Component component, CharSequence script) {
				return script + "document.getElementById('" + getMarkupId() + "').disabled = true;";
			}

			@Override
			public CharSequence postDecorateOnFailureScript(Component component, CharSequence script) {
				return script + "document.getElementById('" + getMarkupId() + "').disabled = false;";
			}

			@Override
			public CharSequence postDecorateOnSuccessScript(Component component, CharSequence script) {
				return script + "document.getElementById('" + getMarkupId() + "').disabled = false;";
			}
		};
	}
}
