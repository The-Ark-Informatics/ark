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
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.model.IModel;

/**
 * Ark implementation of an AjaxButton that disables whole web page, and re-enables once processing completed, also has an pop-up loading/busy indicator.
 *
 * @author cellis
 *
 */
public abstract class ArkBusyAjaxButton extends AjaxButton {


	private static final long	serialVersionUID		= 6243098370244180405L;

	private String					setBusyIndicatorOn	= "";
																	//"var overlayDiv = document.getElementById('busyOverlay'); ";
																	//+ "overlayDiv.style.visibility = 'visible'; "
																	//+ "overlayDiv.style.display = ''; ";

	private String					setBusyIndicatorOff	= ""; 
																	//"var overlayDiv = document.getElementById('busyOverlay'); ";
																	//+ "overlayDiv.style.visibility = 'hidden'; " 
																	//+ "overlayDiv.style.display = 'none'; ";
	

	/**
	 * Ark implementation of an AjaxButton that disables whole web page, and re-enables once processing completed, also has an pop-up loading/busy indicator.
	 * @param id The non-null identifer of this component
	 */
	public ArkBusyAjaxButton(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
	}

	/**
	 * Ark implementation of an AjaxButton that disables whole web page, and re-enables once processing completed, also has an pop-up loading/busy indicator.
	 * @param id The non-null identifer of this component
	 * @param model The model of this component
	 */
	public ArkBusyAjaxButton(String id, IModel<String> model) {
		super(id, model, null);
		setOutputMarkupPlaceholderTag(true);
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
			private static final long	serialVersionUID	= 1L;

			@Override
			public CharSequence postDecorateScript(Component component, CharSequence script) {
				return script + setBusyIndicatorOn;
			}

			@Override
			public CharSequence postDecorateOnFailureScript(Component component, CharSequence script) {
				return script + setBusyIndicatorOff;
			}

			@Override
			public CharSequence postDecorateOnSuccessScript(Component component, CharSequence script) {
				return script + setBusyIndicatorOff;
			}
		};
	}
}
