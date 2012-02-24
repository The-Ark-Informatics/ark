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
package au.org.theark.core.web.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * An Abstract class for a top level Container Form, which will house the Search, Results and Detail Panels and their child objects.
 * 
 * @author nivedann
 * @author cellis
 * @param <T>
 * 
 */
public abstract class AbstractContainerForm<T> extends Form<T> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8511781901281993808L;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param cpmModel
	 */
	public AbstractContainerForm(String id, CompoundPropertyModel<T> cpmModel) {
		super(id, cpmModel);
		setOutputMarkupPlaceholderTag(true);
	}
}