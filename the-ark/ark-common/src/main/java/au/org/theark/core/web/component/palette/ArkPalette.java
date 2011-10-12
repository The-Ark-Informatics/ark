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
package au.org.theark.core.web.component.palette;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Implementation of Wicket Palette class, overriding the default CSS
 * 
 * @author cellis
 */
public class ArkPalette<T> extends Palette<T> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2955612388183249517L;

	/**
	 * @param id
	 *           Component id
	 * @param choicesModel
	 *           Model representing collection of all available choices
	 * @param choiceRenderer
	 *           Render used to render choices. This must use unique IDs for the objects, not the index.
	 * @param rows
	 *           Number of choices to be visible on the screen with out scrolling
	 * @param allowOrder
	 *           Allow user to move selections up and down
	 */
	public ArkPalette(final String id, final IModel<? extends Collection<? extends T>> choicesModel, final IChoiceRenderer<T> choiceRenderer, final int rows, final boolean allowOrder) {
		super(id, choicesModel, choiceRenderer, rows, allowOrder);
	}

	/**
	 * @param id
	 *           Component id
	 * @param model
	 *           Model representing collection of user's selections
	 * @param choicesModel
	 *           Model representing collection of all available choices
	 * @param choiceRenderer
	 *           Render used to render choices. This must use unique IDs for the objects, not the index.
	 * @param rows
	 *           Number of choices to be visible on the screen with out scrolling
	 * @param allowOrder
	 *           Allow user to move selections up and down
	 */
	public ArkPalette(final String id, final IModel<? extends List<? extends T>> model, final IModel<? extends Collection<? extends T>> choicesModel, final IChoiceRenderer<T> choiceRenderer,
			final int rows, final boolean allowOrder) {
		super(id, model, choicesModel, choiceRenderer, rows, allowOrder);
	}

	@Override
	protected ResourceReference getCSS() {
		return new PackageResourceReference(ArkPalette.class, "arkPalette.css");
	}
}