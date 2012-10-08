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
package au.org.theark.lims.web.component.biospecimen.batchaliquot;


import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.lims.model.vo.BatchBiospecimenAliquotsVO;
import au.org.theark.lims.web.component.biospecimen.batchaliquot.form.BatchAliquotBiospecimenForm;

public class BatchAliquotBiospecimenPanel extends Panel {

	private static final long						serialVersionUID	= 7224168117680252835L;
	protected FeedbackPanel							feedbackPanel;
	private BatchAliquotBiospecimenForm					form;

	public BatchAliquotBiospecimenPanel(String id, FeedbackPanel feedbackPanel, IModel<BatchBiospecimenAliquotsVO> model, ModalWindow modalWindow) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		initialisePanel(model, modalWindow);
		setOutputMarkupPlaceholderTag(true);
	}

	public void initialisePanel(IModel<BatchBiospecimenAliquotsVO> model, ModalWindow modalWindow) {
		form = new BatchAliquotBiospecimenForm("batchAliquotBiospecimenForm", model, modalWindow);
		form.initialiseForm();
		add(form);
	}

	/**
	 * @return the listDetailForm
	 */
	public BatchAliquotBiospecimenForm getListDetailForm() {
		return form;
	}

	/**
	 * @param listDetailForm the listDetailForm to set
	 */
	public void setListDetailForm(BatchAliquotBiospecimenForm listDetailForm) {
		this.form = listDetailForm;
	}
}
