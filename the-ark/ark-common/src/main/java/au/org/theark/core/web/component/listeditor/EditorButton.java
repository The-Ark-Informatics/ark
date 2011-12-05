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
package au.org.theark.core.web.component.listeditor;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;

public abstract class EditorButton extends Button {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 2239994447099355647L;
	private transient ListItem<?>	parent;

	public EditorButton(String id) {
		super(id);
	}

	protected final ListItem<?> getItem() {
		if (parent == null) {
			parent = findParent(ListItem.class);
		}
		return parent;
	}

	protected final List<?> getList() {
		return getEditor().items;
	}

	protected final AbstractListEditor<?> getEditor() {
		return (AbstractListEditor<?>) getItem().getParent();
	}

	@Override
	protected void onDetach() {
		parent = null;
		super.onDetach();
	}

}
