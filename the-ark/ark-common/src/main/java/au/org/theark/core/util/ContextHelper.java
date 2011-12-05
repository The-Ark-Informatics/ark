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
package au.org.theark.core.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class ContextHelper {
	Label	studyLabel		= null;
	Label	subjectLabel	= null;
	Label	phenoLabel		= null;
	Label	genoLabel		= null;

	@SuppressWarnings("unchecked")
	public void resetContextLabel(AjaxRequestTarget target, WebMarkupContainer arkContextMarkup) {
		studyLabel = new Label("studyLabel", new Model(""));
		arkContextMarkup.addOrReplace(studyLabel);
		subjectLabel = new Label("subjectLabel", new Model(""));
		arkContextMarkup.addOrReplace(subjectLabel);
		phenoLabel = new Label("phenoLabel", new Model(""));
		arkContextMarkup.addOrReplace(phenoLabel);
		genoLabel = new Label("genoLabel", new Model(""));
		arkContextMarkup.addOrReplace(genoLabel);
		target.add(arkContextMarkup);
	}

	@SuppressWarnings("unchecked")
	public void setStudyContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup) {
		studyLabel = new Label("studyLabel", new Model("Study: " + label));
		arkContextMarkup.addOrReplace(studyLabel);
		target.add(arkContextMarkup);
	}
	
	/**
	 * Overloaded method that does not use a AjaxRequestTarget
	 * @param label
	 * @param arkContextMarkup
	 */
	@SuppressWarnings("unchecked")
	public void setStudyContextLabel( String label, WebMarkupContainer arkContextMarkup) {
		studyLabel = new Label("studyLabel", new Model("Study: " + label));
		arkContextMarkup.addOrReplace(studyLabel);
	}

	@SuppressWarnings("unchecked")
	public void setSubjectContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup) {
		subjectLabel = new Label("subjectLabel", new Model("Subject UID: " + label));
		arkContextMarkup.addOrReplace(subjectLabel);
		target.add(arkContextMarkup);
	}
	
	@SuppressWarnings("unchecked")
	public void setSubjectContextLabel( String label, WebMarkupContainer arkContextMarkup) {
		subjectLabel = new Label("subjectLabel", new Model("Subject UID: " + label));
		arkContextMarkup.addOrReplace(subjectLabel);
	}

	@SuppressWarnings("unchecked")
	public void setPhenoContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup) {
		phenoLabel = new Label("phenoLabel", new Model("Pheno Col.: " + label));
		arkContextMarkup.addOrReplace(phenoLabel);
		target.add(arkContextMarkup);
	}

	@SuppressWarnings("unchecked")
	public void setGenoContextLabel(AjaxRequestTarget target, String label, WebMarkupContainer arkContextMarkup) {
		genoLabel = new Label("genoLabel", new Model("Geno Col.: " + label));
		arkContextMarkup.addOrReplace(genoLabel);
		target.add(arkContextMarkup);
	}

	@SuppressWarnings("unchecked")
	public void setContextLabel(AjaxRequestTarget target, Label label, WebMarkupContainer arkContextMarkup) {
		if (label.getId() == "phenoLabel") {
			phenoLabel = new Label("phenoLabel", new Model("Pheno Col.: " + label));
			arkContextMarkup.addOrReplace(phenoLabel);
		}

		target.add(arkContextMarkup);
	}
}
