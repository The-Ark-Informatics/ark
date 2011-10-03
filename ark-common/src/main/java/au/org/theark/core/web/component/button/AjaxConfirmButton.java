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
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

@SuppressWarnings( { "unchecked" })
/**
 * @author cellis
 *
 */
public abstract class AjaxConfirmButton extends AjaxButton {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8138160131842627124L;
	private IModel					confirm				= new StringResourceModel("confirm", this, null);

	public AjaxConfirmButton(String id, IModel label) {
		super(id);
		this.setModel(label);
	}
	
	public AjaxConfirmButton(String id, IModel confirm, IModel label) {
		super(id);
		this.setModel(label);
		this.confirm = confirm;
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
			private static final long	serialVersionUID	= 7495281332320552876L;

			@Override
			public CharSequence preDecorateScript(CharSequence script) {
				return "if(!confirm('" + confirm.getObject() + "'))" + "{ " + "	return false " + "};" + script;
				// should call return if the rest of scripting required for Wicket to callback the onSubmit...
				// (patched the code to remove the "else" statement because it shouldn't return true)
			}
		};
	}

	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);
}
