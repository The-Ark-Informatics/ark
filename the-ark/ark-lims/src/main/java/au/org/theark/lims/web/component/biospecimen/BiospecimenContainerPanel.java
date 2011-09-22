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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.biospecimen.form.ContainerForm;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenListPanel;

/**
 * 
 * @author cellis
 *
 */
public class BiospecimenContainerPanel extends Panel {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= -1L;
	private static final Logger					log					= LoggerFactory.getLogger(BiospecimenContainerPanel.class);
	
	protected LimsVO									limsVO				= new LimsVO();
	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected FeedbackPanel							feedbackPanel;
	protected WebMarkupContainer					arkContextMarkup;
	protected ContainerForm							containerForm;
	protected WebMarkupContainer					resultListContainer;
	protected Panel									biospecimenListPanel;

	public BiospecimenContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		initialisePanel();
	}

	public void initialisePanel() {
		containerForm = new ContainerForm("containerForm", cpModel);
		
		resultListContainer = initialiseSearchResultPanel();
		
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(resultListContainer);
		this.add(containerForm);
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
	
	private WebMarkupContainer initialiseSearchPanel() {
		WebMarkupContainer searchPanelContainer = new WebMarkupContainer("searchPanelContainer");
		SearchPanel searchComponentPanel = new SearchPanel("searchPanel", feedbackPanel, resultListContainer, arkContextMarkup, containerForm);
		searchComponentPanel.initialisePanel();
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
	
	private WebMarkupContainer initialiseSearchResultPanel() {
		WebMarkupContainer resultListContainer = new WebMarkupContainer("resultListContainer");
		resultListContainer.setOutputMarkupPlaceholderTag(true);
		
		BiospecimenListPanel biospecimenListPanel = new BiospecimenListPanel("biospecimenListPanel", feedbackPanel, cpModel);
		this.biospecimenListPanel = biospecimenListPanel;
		
		// Hide New button in listPanel
		ArkBusyAjaxButton newButton = new ArkBusyAjaxButton("listNewButton", new StringResourceModel("listNewKey", this, null)) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {	
			}
			
			@Override
			public boolean isVisible() {
				return false;
			}
			
		};
		biospecimenListPanel.getListDetailForm().setNewButton(newButton);
		biospecimenListPanel.getListDetailForm().addOrReplace(biospecimenListPanel.getListDetailForm().getNewButton());
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