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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public abstract class AjaxListDeleteButton extends AjaxEditorButton {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8812772472624903905L;

	@SuppressWarnings("unchecked")
	public AjaxListDeleteButton(String id, IModel confirm, IModel label) {
		super(id, confirm, label);
		setDefaultFormProcessing(false);
	}

	@Override
	public boolean isEnabled() {
		return getEditor().checkRemove(getItem());
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		int idx = getItem().getIndex();

		for (int i = idx + 1; i < getItem().getParent().size(); i++) {
			ListItem<?> item = (ListItem<?>) getItem().getParent().get(i);
			item.setIndex(item.getIndex() - 1);
		}

		getList().remove(idx);
		getEditor().remove(getItem());

		// only repaint ListDetailForm
		target.add(form);
		onDeleteConfirmed(target, form);
	}

	protected abstract void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form);
}
