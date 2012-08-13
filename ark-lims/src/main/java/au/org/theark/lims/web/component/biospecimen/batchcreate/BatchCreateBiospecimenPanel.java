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
package au.org.theark.lims.web.component.biospecimen.batchcreate;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import au.org.theark.lims.model.vo.BatchBiospecimenVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.biospecimen.batchcreate.form.BatchCreateBiospecimenForm;

public class BatchCreateBiospecimenPanel extends Panel {

	private static final long						serialVersionUID	= 7224168117680252835L;

	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected FeedbackPanel							feedbackPanel;
	private BatchCreateBiospecimenForm					form;

	public BatchCreateBiospecimenPanel(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
		initialisePanel();
		setOutputMarkupPlaceholderTag(true);
	}

	public void initialisePanel() {
		form = new BatchCreateBiospecimenForm("batchCreateBiospecimenForm", feedbackPanel, cpModel, new Model<BatchBiospecimenVO>(new BatchBiospecimenVO()));
		form.initialiseForm();
		add(form);
	}

	/**
	 * @return the listDetailForm
	 */
	public BatchCreateBiospecimenForm getListDetailForm() {
		return form;
	}

	/**
	 * @param listDetailForm the listDetailForm to set
	 */
	public void setListDetailForm(BatchCreateBiospecimenForm listDetailForm) {
		this.form = listDetailForm;
	}
}
