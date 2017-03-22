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
package au.org.theark.core.web.component.link;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

/**
 * @author elam
 *
 */
public abstract class AjaxConfirmLink<T> extends AjaxLink<T> {

	private static final long	serialVersionUID	= 8138160131842627124L;
	private IModel<String>					confirm;
	
	public AjaxConfirmLink(String id, IModel<String> confirm, IModel<T> itemModel) {
		super(id, itemModel);
		this.confirm = confirm;
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.getAjaxCallListeners().add(new AjaxCallListener(){

			@Override
			public CharSequence getSuccessHandler(Component component) {
				return "if(!confirm('" + confirm.getObject() + "'))" + "{ " + "	return false " + "}; ";
			}

		});
	}
}
