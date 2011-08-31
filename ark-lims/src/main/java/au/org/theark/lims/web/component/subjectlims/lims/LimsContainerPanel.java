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
package au.org.theark.lims.web.component.subjectlims.lims;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.BioCollectionListPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenListPanel;
import au.org.theark.lims.web.component.subjectlims.lims.form.ContainerForm;

/**
 * @author elam
 * @author cellis
 * 
 */
public class LimsContainerPanel extends Panel {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= -1L;
	private static final Logger					log					= LoggerFactory.getLogger(LimsContainerPanel.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	protected LimsVO									limsVO				= new LimsVO();
	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected FeedbackPanel							feedbackPanel;
	protected WebMarkupContainer					arkContextMarkup;
	protected ContainerForm							containerForm;
	protected Panel									collectionListPanel;
	protected Panel									biospecimenListPanel;

	public LimsContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		initialisePanel();
	}

	public void initialisePanel() {
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());

		prerenderContextCheck();

		this.add(containerForm);
	}

	protected void prerenderContextCheck() {
		// Get the SubjectUID in context
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		if (sessionSubjectUID != null) {
			LinkSubjectStudy linkSubjectStudy = null;
			boolean contextLoaded = false;
			try {
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID);
				if (linkSubjectStudy != null) {
					contextLoaded = true;
				}
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}

			if (contextLoaded) {
				BioCollectionListPanel biocollectionListPanel = new BioCollectionListPanel("biocollectionListPanel", feedbackPanel, cpModel);
				collectionListPanel = biocollectionListPanel;
				containerForm.add(collectionListPanel);

				BiospecimenListPanel bioSpecimenListPanel = new BiospecimenListPanel("biospecimenListPanel", feedbackPanel, cpModel);
				biospecimenListPanel = bioSpecimenListPanel;
				containerForm.add(biospecimenListPanel);
			}
			else {
				containerForm.info("Could not load subject in context - record is invalid (e.g. deleted)");

				collectionListPanel = new EmptyPanel("biocollectionListPanel");
				collectionListPanel.setOutputMarkupId(true);
				containerForm.add(collectionListPanel);

				biospecimenListPanel = new EmptyPanel("biospecimenListPanel");
				biospecimenListPanel.setOutputMarkupId(true);
				containerForm.add(biospecimenListPanel);
			}
		}
	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
}