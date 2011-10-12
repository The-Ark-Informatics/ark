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
package au.org.theark.geno.web.component.upload;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.geno.entity.DelimiterType;
import au.org.theark.core.model.geno.entity.FileFormat;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.UploadCollection;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.geno.model.vo.UploadCollectionVO;
import au.org.theark.geno.service.Constants;
import au.org.theark.geno.service.IGenoService;
import au.org.theark.geno.web.component.upload.DetailPanel.DetailForm;
import au.org.theark.geno.web.component.upload.form.ContainerForm;

public class SearchPanel extends Panel {

	private UploadContainerPanel fileUploadContainerPanel;
	private ContainerForm containerForm;

	public SearchPanel(String id, 
					UploadContainerPanel genoCollectionContainerPanel, 
					ContainerForm containerForm)
	{
		super(id);
		this.fileUploadContainerPanel = genoCollectionContainerPanel; 
		this.containerForm = containerForm;
	}

	/**
	 * NB: Call this after the a new SearchPanel, but not within its constructor
	 */
	public void initialisePanel()
	{
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, 
												(CompoundPropertyModel<UploadCollectionVO>) containerForm.getModel());
		searchForm.initialiseSearchForm();
		add(searchForm);
	}
	
	/*
	 * SearchForm inner class
	 */
	protected class SearchForm extends AbstractSearchForm<UploadCollectionVO> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		@SpringBean(name = au.org.theark.geno.service.Constants.GENO_SERVICE)
		private IGenoService genoService;

		@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
		private IArkCommonService arkCommonService;
		
		// Links to related resources
		private CompoundPropertyModel<UploadCollectionVO> cpmModel;

		// Define the components for the search form
		private TextField<String> uploadCollectionIdTxtFld;
		private TextField<String> uploadCollectionUploadFilenameTxtFld;
		private DropDownChoice<FileFormat> uploadFileformatDdc;
		private DropDownChoice<DelimiterType> uploadDelimiterTypeDdc;

		public SearchForm(String id,
				CompoundPropertyModel<UploadCollectionVO> model) {
			
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
			uploadCollectionIdTxtFld = new TextField<String>(Constants.UPLOADCOLLECTION_VO_ID);
			uploadCollectionUploadFilenameTxtFld = new TextField<String>(Constants.UPLOADCOLLECTION_VO_UPLOAD_FILENAME);

			initDropDownChoices();
			addFieldComponents();
			
			Long sessionGenoColId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.geno.web.Constants.SESSION_GENO_COLLECTION_ID);
//			if (sessionStudyId == null) {
//				this.error(au.org.theark.geno.web.Constants.MSG_NO_GENOCOLLECTION_CONTEXT);
//				setSearchButtonsEnabled(false);
//			} else {
//				setSearchButtonsEnabled(true);
//			}
			disableSearchForm(sessionGenoColId, au.org.theark.geno.web.Constants.MSG_NO_GENOCOLLECTION_CONTEXT);
		}
		
		//TODO
		private void initDropDownChoices()
		{
			CompoundPropertyModel<UploadCollectionVO> fileUploadCpm = cpmModel;
			PropertyModel<UploadCollectionVO> uploadCollectionPm = new PropertyModel<UploadCollectionVO>(fileUploadCpm, au.org.theark.geno.service.Constants.UPLOADCOLLECTION);

			java.util.Collection<FileFormat> fileFormatCollection = genoService.getFileFormatCollection();
			PropertyModel<FileFormat> fileFormatPm = new PropertyModel<FileFormat>(uploadCollectionPm, au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD_FILEFORMAT);
			ChoiceRenderer<FileFormat> fieldTypeRenderer = new ChoiceRenderer<FileFormat>(au.org.theark.geno.service.Constants.FILEFORMAT_NAME, 
																au.org.theark.geno.service.Constants.FILEFORMAT_ID);
			uploadFileformatDdc = new DropDownChoice<FileFormat>(au.org.theark.geno.service.Constants.FILEFORMAT, fileFormatPm, 
															(List) fileFormatCollection, fieldTypeRenderer);
			
			java.util.Collection<DelimiterType> delimiterTypeCollection = genoService.getDelimiterTypeCollection();
			PropertyModel<DelimiterType> delimiterTypePm = new PropertyModel<DelimiterType>(uploadCollectionPm, au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD_DELIMITERTYPE);
			ChoiceRenderer<DelimiterType> delimiterTypeRenderer = new ChoiceRenderer<DelimiterType>(au.org.theark.geno.service.Constants.DELIMITERTYPE_NAME, 
																au.org.theark.geno.service.Constants.DELIMITERTYPE_ID);
			uploadDelimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.geno.service.Constants.DELIMITERTYPE, delimiterTypePm, 
															(List) delimiterTypeCollection, delimiterTypeRenderer);
		}

		private void setSearchButtonsEnabled(boolean enabled) {
			searchButton.setEnabled(enabled);
			newButton.setEnabled(enabled);
			resetButton.setEnabled(enabled);			
		}

		private void addFieldComponents() {
			add(uploadCollectionIdTxtFld);
			add(uploadCollectionUploadFilenameTxtFld);
			add(uploadFileformatDdc);
			add(uploadDelimiterTypeDdc);
		}

		@Override
		public void onNew(AjaxRequestTarget target) {
			// Show the details panel name and description
			UploadCollectionVO fileUploadVo = new UploadCollectionVO();
//			genoCollectionVo.setMode(au.org.theark.core.Constants.MODE_NEW);

			// Set collection for the new upload
			Long genoColctnId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.geno.web.Constants.SESSION_GENO_COLLECTION_ID);
			GenoCollection genoCol = genoService.getCollection(genoColctnId);
//			fileUploadVo.setCollection(genoCol);
			//TODO: FIX above

			setModelObject(fileUploadVo);
			fileUploadContainerPanel.showNewDetail(target);
		}

		@Override
		public void onSearch(AjaxRequestTarget target) {
			// TODO Auto-generated method stub
			// Refresh the FB panel if there was an old message from previous search result
			fileUploadContainerPanel.refreshFeedback(target);

			// Set collection in context
			Long genoColctnId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.geno.web.Constants.SESSION_GENO_COLLECTION_ID);
			GenoCollection genoCol = genoService.getCollection(genoColctnId);
			// Get a list of all uploads for the collection in context
			UploadCollection uploadColCrit = containerForm.getModelObject().getUploadCollection();
			uploadColCrit.setCollection(genoCol);

			java.util.Collection<UploadCollection> uploadCollectionCol = genoService.searchUploadCollection(uploadColCrit);

			if (uploadCollectionCol == null && uploadCollectionCol.size() == 0)
			{
				this.info("Uplaod Collections with the specified criteria does not exist in the system.");
				fileUploadContainerPanel.refreshFeedback(target);
			}

			fileUploadContainerPanel.showSearchResults(uploadCollectionCol, target);
		}
		
	}

}
