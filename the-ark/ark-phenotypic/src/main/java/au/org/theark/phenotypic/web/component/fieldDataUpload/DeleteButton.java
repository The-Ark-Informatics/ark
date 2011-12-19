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
package au.org.theark.phenotypic.web.component.fieldDataUpload;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.core.web.component.button.AjaxDeleteButton;

public class DeleteButton extends AjaxDeleteButton {
	private static final long	serialVersionUID	= 4966354164332401574L;
	private transient Logger	log					= LoggerFactory.getLogger(DeleteButton.class);

	DeleteButton(final PhenoUpload upload, Component component) {
		// Properties contains:
		// confirmDelete=Are you sure you want to delete?
		// delete=Delete
		super(au.org.theark.phenotypic.web.Constants.DELETE_FILE, new StringResourceModel("confirmDelete", component, null), new StringResourceModel(au.org.theark.phenotypic.web.Constants.DELETE,
				component, null));
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		log.error("onError called when DeleteButton pressed");
	}
}
