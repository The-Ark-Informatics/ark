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
package au.org.theark.study.web.component.managestudy;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.apache.wicket.util.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.util.ImageResource;

public class StudyHelper implements Serializable {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 8485137084667518625L;
	private static final Logger	log					= LoggerFactory.getLogger(StudyHelper.class);
	private NonCachingImage			studyLogoImage;
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

	@SuppressWarnings("unchecked")
	public void setStudyLogoImage(Study study, String id, WebMarkupContainer studyLogoImageContainer) {
		// Set the study logo
		try {
			if (study != null && study.getStudyLogoBlob() != null) {
				// Get the Study logo Blob as an array of bytes
				java.sql.Blob studyLogoBlob = study.getStudyLogoBlob();

				if (studyLogoBlob != null) {
					InputStream in = studyLogoBlob.getBinaryStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Streams.copy(in, out);
					// Get the Study logo Blob as an array of bytes
					final byte[] data = out.toByteArray(); // studyLogoBlob.getBytes(1, (int) studyLogoBlob.length());

					studyLogoImage = new NonCachingImage(id, new AbstractReadOnlyModel() {
						/**
						 * 
						 */
						private static final long	serialVersionUID	= 1L;

						@Override
						public Object getObject() {
							return new ImageResource(data, "gif");
						}
					});

					studyLogoImageContainer.replace(studyLogoImage);
				}
			}
			else {
				noStudyLogoImage = new ContextImage("study.studyLogoImage", new Model<String>("images/no_study_logo.gif"));
				studyLogoImageContainer.replace(noStudyLogoImage);
			}
		}
		catch (SQLException sqle) {
			log.error(sqle.getMessage());
		}
		catch (IOException ioe) {
			log.error(ioe.getMessage());
		}
	}

	public void setStudyContextLabel(AjaxRequestTarget target, String studyName, WebMarkupContainer arkContextMarkup) {
		studyLabel = new Label("studyLabel", new Model<String>("Study: " + studyName));
		arkContextMarkup.addOrReplace(studyLabel);
		target.add(arkContextMarkup);
	}
}
