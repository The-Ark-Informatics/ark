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
package au.org.theark.core.web.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 * Ark implementation of an AjaxLink that shows the Loading... "busy" indicator when clicked
 *
 * @author cellis
 *
 * @param <T>
 *            type of model object
 */
public abstract class ArkBusyAjaxLink<T> extends AjaxLink<T> {
	/**
	 * 
	 */
	private static final long	serialVersionUID		= 4139168862106185766L;

	private String					setBusyIndicatorOn	= "overlay = document.getElementById('busyOverlay'); "
																	+ "overlay.style.visibility = 'visible';";

	private String					setBusyIndicatorOff	= "overlay = document.getElementById('busyOverlay'); "
																	+ "overlay.style.visibility = 'hidden';";

	/**
	 * Ark implementation of an AjaxLink that shows the Loading... "busy" indicator when clicked
	 * @param id The non-null id of this component
	 */
	public ArkBusyAjaxLink(String id) {
		super(id);
		setOutputMarkupId(true);
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
