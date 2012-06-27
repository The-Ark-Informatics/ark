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
package au.org.theark.lims.web.component.biospecimen;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.biospecimen.form.ContainerForm;

/**
 * 
 * @author cellis
 *
 */
public class BiospecimenContainerPanel extends Panel {

	private static final long						serialVersionUID	= -1L;
	private static final Logger					log					= LoggerFactory.getLogger(BiospecimenContainerPanel.class);
	
	protected LimsVO									limsVO				= new LimsVO();
	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected ArkCrudContainerVO					arkCrudContainerVO;

	protected FeedbackPanel							feedbackPanel;
	protected WebMarkupContainer					arkContextMarkup;
	protected ContainerForm							containerForm;
	protected Panel									biospecimenListPanel;

	public BiospecimenContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		arkCrudContainerVO = new ArkCrudContainerVO();
		
		initialisePanel();
	}

	public void initialisePanel() {
		containerForm = new ContainerForm("containerForm", cpModel);
				
		// Needed to handle for modalWindow
		containerForm.setMultiPart(true);
		
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseSearchResultPanel());
		this.add(containerForm);
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
	
	private WebMarkupContainer initialiseSearchPanel() {
		WebMarkupContainer searchPanelContainer = arkCrudContainerVO.getSearchPanelContainer();
		SearchPanel searchComponentPanel = new SearchPanel("searchPanel", feedbackPanel, containerForm, arkCrudContainerVO);
		searchComponentPanel.initialisePanel();
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
	
	private WebMarkupContainer initialiseSearchResultPanel() {
		WebMarkupContainer resultListContainer = arkCrudContainerVO.getSearchResultPanelContainer();
		resultListContainer.setOutputMarkupPlaceholderTag(true);
		
		BiospecimenListPanel biospecimenListPanel = new BiospecimenListPanel("biospecimenListPanel", feedbackPanel, cpModel);
		this.biospecimenListPanel = biospecimenListPanel;
		resultListContainer.add(biospecimenListPanel);
		return resultListContainer;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}
}