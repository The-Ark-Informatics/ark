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
package au.org.theark.geno.web.component.genoCollection;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.Status;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.geno.model.vo.GenoCollectionVO;
import au.org.theark.geno.service.Constants;
import au.org.theark.geno.service.IGenoService;
import au.org.theark.geno.web.component.genoCollection.DetailPanel.DetailForm;
import au.org.theark.geno.web.component.genoCollection.form.ContainerForm;

public class SearchPanel extends Panel {

	private GenoCollectionContainerPanel genoCollectionContainerPanel;
	private ContainerForm containerForm;

	public SearchPanel(String id, 
					GenoCollectionContainerPanel genoCollectionContainerPanel, 
					ContainerForm containerForm)
	{
		super(id);
		this.genoCollectionContainerPanel = genoCollectionContainerPanel; 
		this.containerForm = containerForm;
	}

	/**
	 * NB: Call this after the a new SearchPanel, but not within its constructor
	 */
	public void initialisePanel()
	{
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, 
												(CompoundPropertyModel<GenoCollectionVO>) containerForm.getModel());
		searchForm.initialiseSearchForm();
		add(searchForm);
	}
	
	/*
	 * SearchForm inner class
	 */
	protected class SearchForm extends AbstractSearchForm<GenoCollectionVO> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		@SpringBean(name = au.org.theark.geno.service.Constants.GENO_SERVICE)
		private IGenoService genoService;

		@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
		private IArkCommonService arkCommonService;
		
		// Links to related resources
		private CompoundPropertyModel<GenoCollectionVO> cpmModel;

		// Define the components for the search form
		private TextField<String> genoCollectionIdTxtFld;
		private TextField<String> genoCollectionNameTxtFld;
		private TextArea<String> genoCollectionDescriptionTxtAreaFld;
		private DropDownChoice<Status> genoStatusDdc;
		private DateTextField genoCollectionStartDateFld;
		private DateTextField genoCollectionExpiryDateFld;

		public SearchForm(String id,
				CompoundPropertyModel<GenoCollectionVO> model) {
			
			super(id, model);
			this.cpmModel = model;
			// TODO: Fix ARK-122:: To fix a NullPointer exception must add this hack, 
			// because Geno doesn't follow the AbstractSearchForm perfectly.
			// Remove this when switching over to the new Abstract classes.
			feedbackPanel = (FeedbackPanel) containerForm.get("feedbackMessage");
			WebMarkupContainer detailContainer = (WebMarkupContainer) containerForm.get("detailContainer");
			Panel detailPanel = (Panel) detailContainer.get("detailPanel");
			DetailForm detailForm = (DetailForm) detailPanel.get("detailForm");
			editButtonContainer = (WebMarkupContainer) detailForm.get("editButtonContainer");
		}
		
		/**
		 * NB: Call this after the a new SearchForm, but not within its constructor
		 */
		public void initialiseSearchForm()
		{
			genoCollectionIdTxtFld = new TextField<String>(Constants.GENO_COLLECTION_VO_ID);
			genoCollectionNameTxtFld = new TextField<String>(Constants.GENO_COLLECTION_VO_NAME);
			genoCollectionDescriptionTxtAreaFld = new TextArea<String>(Constants.GENO_COLLECTION_VO_DESCRIPTION);
			// Create new DateTextField and assign date format
			genoCollectionStartDateFld = new DateTextField(Constants.GENO_COLLECTION_VO_START_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
			ArkDatePicker datePicker = new ArkDatePicker();
			datePicker.bind(genoCollectionStartDateFld);
			genoCollectionStartDateFld.add(datePicker);
						
			genoCollectionExpiryDateFld = new DateTextField(Constants.GENO_COLLECTION_VO_EXPIRY_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
			ArkDatePicker datePicker2 = new ArkDatePicker();
			datePicker2.bind(genoCollectionExpiryDateFld);
			genoCollectionExpiryDateFld.add(datePicker2);

			initStatusDdc();
			addFieldComponents();
			
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//			if (sessionStudyId == null) {
//				this.error(au.org.theark.geno.web.Constants.MSG_NO_STUDY_CONTEXT);
//				setSearchButtonsEnabled(false);
//			} else {
//				setSearchButtonsEnabled(true);
//			}
			disableSearchForm(sessionStudyId, au.org.theark.geno.web.Constants.MSG_NO_STUDY_CONTEXT);

		}
		
		private void initStatusDdc()
		{
			java.util.Collection<Status> statusCollection = genoService.getStatusCollection();
			CompoundPropertyModel<GenoCollectionVO> genoCollectionCpm = cpmModel;
			PropertyModel<GenoCollection> genoCollectionPm = new PropertyModel<GenoCollection>(genoCollectionCpm, au.org.theark.geno.service.Constants.GENOCOLLECTION);
			PropertyModel<Status> statusPm = new PropertyModel<Status>(genoCollectionPm, au.org.theark.geno.service.Constants.STATUS);
			ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(au.org.theark.geno.service.Constants.STATUS_NAME, au.org.theark.geno.service.Constants.STATUS_ID);
			genoStatusDdc = new DropDownChoice<Status>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_STATUS, statusPm, (List) statusCollection, fieldTypeRenderer);
		}

		private void setSearchButtonsEnabled(boolean enabled) {
			searchButton.setEnabled(enabled);
			newButton.setEnabled(enabled);
			resetButton.setEnabled(enabled);			
		}

		private void addFieldComponents() {
			add(genoCollectionIdTxtFld);
			add(genoCollectionNameTxtFld);
			add(genoCollectionDescriptionTxtAreaFld);
			add(genoStatusDdc);
			add(genoCollectionStartDateFld);
			add(genoCollectionExpiryDateFld);
		}

		@Override
		public void onNew(AjaxRequestTarget target) {
			// Show the details panel name and description
			GenoCollectionVO genoCollectionVo = new GenoCollectionVO();
//			genoCollectionVo.setMode(au.org.theark.core.Constants.MODE_NEW);

			// Set study for the new collection
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = arkCommonService.getStudy(studyId);
			genoCollectionVo.getGenoCollection().setStudy(study);

			setModelObject(genoCollectionVo);
			genoCollectionContainerPanel.showNewDetail(target);
		}

		@Override
		public void onSearch(AjaxRequestTarget target) {
			// TODO Auto-generated method stub
			// Refresh the FB panel if there was an old message from previous search result
//			target.addComponent(feedBackPanel);
			genoCollectionContainerPanel.refreshFeedback(target);

			// Set study in context
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			// Get a list of all Fields for the Study in context
			Study study = arkCommonService.getStudy(studyId);

			GenoCollection genoCollection = containerForm.getModelObject().getGenoCollection();
			genoCollection.setStudy(study);

			java.util.Collection<GenoCollection> genoCollectionCol = genoService.searchGenoCollection(genoCollection);

			if (genoCollectionCol == null && genoCollectionCol.size() == 0)
			{
				this.info("Geno Collections with the specified criteria does not exist in the system.");
//				target.addComponent(feedBackPanel);
				genoCollectionContainerPanel.refreshFeedback(target);
			}
			// Moved to showSearchResults
//			containerForm.getModelObject().setGenoCollectionCollection(genoCollectionCol);
//			listView.removeAll();
//			searchResultPanelContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
//			target.addComponent(searchResultPanelContainer);// For ajax this is required so
			genoCollectionContainerPanel.showSearchResults(genoCollectionCol, target);
		}
		
	}

}
