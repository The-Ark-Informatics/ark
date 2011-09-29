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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import au.org.theark.core.security.ArkPermissionHelper;

@SuppressWarnings({ "unchecked" })
/**
 * @author cellis
 *
 */
public abstract class AjaxDeleteButton extends IndicatingAjaxButton {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2845373897903023596L;
	private final IModel			confirm;

	public AjaxDeleteButton(String id, IModel confirm, IModel label) {
		super(id);
		this.setModel(label);
		this.confirm = confirm;
	}
	
	public AjaxDeleteButton(String id, IModel confirm) {
		super(id);
		this.confirm = confirm;
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
			private static final long	serialVersionUID	= 7495281332320552876L;

			@Override
			public CharSequence preDecorateScript(CharSequence script) {
				return "if(!confirm('" + confirm.getObject() + "'))" + "{ " + "	return false " + "} " + "else " + "{ " + "	this.disabled = true; " + "};" + script;
			}
		};
	}

	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);

	@Override
	public boolean isVisible() {
		return super.isVisible() && ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.DELETE);
	}
}
