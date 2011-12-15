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
package au.org.theark.core.web;

import java.io.Serializable;
import java.sql.Blob;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.model.Model;

import au.org.theark.core.model.study.entity.Study;

public class StudyHelper implements Serializable {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 8485137084667518625L;
	private Image						studyLogoImage;
	private ContextImage				noStudyLogoImage;
	private Label						studyNameLabel		= null;
	private Label						studyLabel			= null;

	public void setStudyLogo(Study study, AjaxRequestTarget target, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		// Set the study logo
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null && study.getStudyLogoBlob() != null) {
			setStudyLogoImage(study, "studyLogoImage", studyLogoMarkup);
			studyNameMarkup.setVisible(false);
			studyLogoMarkup.setVisible(true);
		}
		else {
			// Only show study name, no logo
			studyNameLabel = new Label("studyNameLabel", new Model<String>(study.getName()));
			studyNameMarkup.replace(studyNameLabel);
			studyNameMarkup.setVisible(true);
			studyLogoMarkup.setVisible(false);
		}

		target.add(studyNameMarkup);
		target.add(studyLogoMarkup);
	}
	
	public void setStudyLogoImage(final Study study, String id, WebMarkupContainer studyLogoImageContainer) {
		// Set the study logo
		if (study != null && study.getStudyLogoBlob() != null) {
			final java.sql.Blob studyLogoBlob = study.getStudyLogoBlob();

			if (studyLogoBlob != null) {
				BlobImageResource blobImageResource = new BlobImageResource() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					protected Blob getBlob() {
						return studyLogoBlob;
					}
				};
				
				Image	studyLogoImage = new Image(id, blobImageResource);
				studyLogoImageContainer.addOrReplace(studyLogoImage);
			}
		}
		else {
			noStudyLogoImage = new ContextImage("study.studyLogoImage", new Model<String>("images/no_study_logo.gif"));
			studyLogoImageContainer.addOrReplace(noStudyLogoImage);
		}
	}

	public void setStudyContextLabel(AjaxRequestTarget target, String studyName, WebMarkupContainer arkContextMarkup) {
		studyLabel = new Label("studyLabel", new Model<String>("Study: " + studyName));
		arkContextMarkup.addOrReplace(studyLabel);
		target.add(arkContextMarkup);
	}
}
