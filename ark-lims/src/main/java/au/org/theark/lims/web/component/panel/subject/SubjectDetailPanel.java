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
package au.org.theark.lims.web.component.panel.subject;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;


/**
 * @author cellis
 * 
 */
public class SubjectDetailPanel extends Panel {

	private static final long	serialVersionUID	= 858762052753650329L;
	private SubjectDetailForm			subjectDetailForm;
	protected Model<LinkSubjectStudy> model;

	public SubjectDetailPanel(final String id, final IModel<LinkSubjectStudy> model) {
		super(id, model);
		this.model = (Model<LinkSubjectStudy>) model;
	}

	public void initialisePanel() {
		subjectDetailForm = new SubjectDetailForm("subjectDetailForm", model);
		subjectDetailForm.initialiseDetailForm();
		add(subjectDetailForm);
	}

	public SubjectDetailForm getDetailsForm() {
		return subjectDetailForm;
	}

	public void setDetailsForm(SubjectDetailForm detailsForm) {
		this.subjectDetailForm = detailsForm;
	}
}
