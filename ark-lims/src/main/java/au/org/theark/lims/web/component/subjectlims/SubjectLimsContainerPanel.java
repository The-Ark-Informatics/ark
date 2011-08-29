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
package au.org.theark.lims.web.component.subjectlims;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.lims.web.component.subjectlims.lims.LimsContainerPanel;
import au.org.theark.lims.web.component.subjectlims.subject.SubjectContainerPanel;

/**
 * @author elam
 * 
 */
public class SubjectLimsContainerPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2890453994817235963L;

	private static final Logger	log					= LoggerFactory.getLogger(SubjectLimsContainerPanel.class);

	private WebMarkupContainer		arkContextMarkup;
	private SubjectContainerPanel	subjectContainerPanel;
	private WebMarkupContainer		limsContainerWMC;
	private Panel						limsContainerPanel;

	public SubjectLimsContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		initialisePanel();
	}

	public void initialisePanel() {
		subjectContainerPanel = new SubjectContainerPanel("subjectContainerPanel", arkContextMarkup);
		this.add(subjectContainerPanel);

		limsContainerWMC = new WebMarkupContainer("limsContainerWMC");
		limsContainerWMC.setOutputMarkupPlaceholderTag(true);

		limsContainerPanel = new EmptyPanel("limsContainerPanel");
		limsContainerWMC.add(limsContainerPanel);
		this.add(limsContainerWMC);

		subjectContainerPanel.setContextUpdateLimsWMC(limsContainerWMC);
	}

	@Override
	public void onBeforeRender() {
		// Get the Study and SubjectUID in Context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			if (limsContainerPanel instanceof EmptyPanel) {
				Panel limsContainerPanel = new LimsContainerPanel("limsContainerPanel", arkContextMarkup);
				limsContainerWMC.addOrReplace(limsContainerPanel);
			}
		}
		super.onBeforeRender();
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}

}
