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
package au.org.theark.study.web.component.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.vo.SiteModelVO;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.ContainerForm;
import au.org.theark.study.web.component.site.form.SearchSiteForm;

@SuppressWarnings("serial")
public class Search extends Panel {

	private FeedbackPanel				fbPanel;

	private PageableListView<SiteVO>	listView;

	private WebMarkupContainer			searchMarkupContainer;
	// The container to wrap the Search Result List
	private WebMarkupContainer			listContainer;
	// The Container to wrap the details panel
	private WebMarkupContainer			detailsContainer;

	private ContainerForm				containerForm;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService				studyService;

	/* Constructor */
	public Search(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer, PageableListView<SiteVO> listView, WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer, ContainerForm siteContainerForm) {
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		fbPanel = feedBackPanel;
		listContainer = resultListContainer;
		detailsContainer = detailPanelContainer;
		containerForm = siteContainerForm;
	}

	@SuppressWarnings("serial")
	public void initialisePanel(CompoundPropertyModel<SiteModelVO> siteModelCpm) {

		// Get the study id from the session and get the study
		// Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<Person> availablePersons = new ArrayList<Person>();

		SearchSiteForm searchSiteForm = new SearchSiteForm(Constants.SEARCH_FORM, siteModelCpm, availablePersons) {

			protected void onSearch(AjaxRequestTarget target) {

				// Refresh the FB panel if there was an old message from previous search result
				target.addComponent(fbPanel);
				List<SiteVO> resultList = new ArrayList<SiteVO>();
				if (resultList != null && resultList.size() == 0) {
					this.info("Site with the specified criteria does not exist in the system.");
					target.addComponent(fbPanel);
				}
				else {
					containerForm.getModelObject().setSiteVoList(resultList);
					listView.removeAll();
					listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
					target.addComponent(listContainer);// For ajax this is required so
				}
			}

			protected void onNew(AjaxRequestTarget target) {
				// Show the details panel name and description
				SiteModelVO siteModel = new SiteModelVO();
				siteModel.setMode(Constants.MODE_NEW);
				containerForm.setModelObject(siteModel);
				processDetail(target, Constants.MODE_NEW);
			}
		};

		add(searchSiteForm);
	}

	public void processDetail(AjaxRequestTarget target, int mode) {

		detailsContainer.setVisible(true);
		listContainer.setVisible(false);
		searchMarkupContainer.setVisible(false);
		target.addComponent(detailsContainer);
		target.addComponent(listContainer);
		target.addComponent(searchMarkupContainer);
	}

}
