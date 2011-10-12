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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

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

	protected LimsVO									limsVO				= new LimsVO();
	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected FeedbackPanel							feedbackPanel;
	protected WebMarkupContainer					arkContextMarkup;
	protected ContainerForm							containerForm;
	protected Panel									collectionListPanel;
	protected Panel									biospecimenListPanel;

	public LimsContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		initialisePanel();
	}
	
	public LimsContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		initialisePanel();
	}

	public void initialisePanel() {
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());

		containerForm.setMultiPart(true);
		
		BioCollectionListPanel biocollectionListPanel = new BioCollectionListPanel("biocollectionListPanel", feedbackPanel, cpModel);
		collectionListPanel = biocollectionListPanel;
		containerForm.add(collectionListPanel);

		BiospecimenListPanel bioSpecimenListPanel = new BiospecimenListPanel("biospecimenListPanel", feedbackPanel, cpModel);
		biospecimenListPanel = bioSpecimenListPanel;
		containerForm.add(biospecimenListPanel);
		
		this.add(containerForm);
	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
}