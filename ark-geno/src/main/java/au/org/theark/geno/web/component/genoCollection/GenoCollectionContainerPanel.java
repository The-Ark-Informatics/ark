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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.geno.model.vo.GenoCollectionVO;
import au.org.theark.geno.service.IGenoService;
import au.org.theark.geno.web.component.genoCollection.form.ContainerForm;

/**
 * @author elam
 *
 */
public class GenoCollectionContainerPanel extends AbstractContainerPanel<GenoCollectionVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = au.org.theark.geno.service.Constants.GENO_SERVICE)
	private IGenoService genoService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService arkCommonService;

	private ContainerForm containerForm;
	
	// Panels
	private DetailPanel detailPanel;
	private SearchPanel searchComponentPanel;
	private SearchResultListPanel searchResultPanel;
	private WebMarkupContainer arkContextMarkup = null;

	// Buttons
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	private AjaxButton deleteButton;
	private AjaxButton editButton;
	private AjaxButton editCancelButton;
	
	// Pageable-List view
	private PageableListView<GenoCollection> pageableListView;
	
	public GenoCollectionContainerPanel(String id) {
		super(id);
		// Set up the CompoundPropertyModel (CPM) to map to the GenoCollectionVO
		cpModel = new CompoundPropertyModel<GenoCollectionVO>(new GenoCollectionVO());
	}
	
	/**
	 * NB: Call this after the a new ContainerPanel, but not within its constructor
	 */
	public void initialisePanel() {
		// Bind the CPM to the Form
		containerForm = new ContainerForm("containerForm", cpModel);
		// Add additional panels to the container form (NB: order is important)
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		// Attach containerForm to this container panel
		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", this, containerForm);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultPanel = new SearchResultListPanel("searchResults", this, containerForm);

		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				return containerForm.getModelObject().getGenoCollectionCollection();
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// TODO Auto-generated method stub
		// Get a collection of fields for the study in context by default
		
		// Get a collection of fields for the study in context by default
		Collection<GenoCollection> genoCollectionCol = new ArrayList<GenoCollection>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null && sessionStudyId > 0)
		{
			Study study = arkCommonService.getStudy(sessionStudyId);
			containerForm.getModelObject().getGenoCollection().setStudy(study);
			genoCollectionCol = genoService.searchGenoCollection(containerForm.getModelObject().getGenoCollection());
		}

		containerForm.getModelObject().setGenoCollectionCollection(genoCollectionCol);

		searchComponentPanel = new SearchPanel("searchPanel", this, containerForm);
		searchComponentPanel.initialisePanel();
		
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}

	/* 
	 * Setup the various references that should be supplied by the DetailPanel
	 */
	
	public void setDetailPanelFormContainer(
			WebMarkupContainer detailPanelFormContainer) {
		this.detailPanelFormContainer = detailPanelFormContainer;
	}

	public void setViewButtonContainer(WebMarkupContainer viewButtonContainer) {
		this.viewButtonContainer = viewButtonContainer;
	}

	public void setEditButtonContainer(WebMarkupContainer editButtonContainer) {
		this.editButtonContainer = editButtonContainer;
	}

	public void setDetailPanelButtons(AjaxButton saveButton,
			AjaxButton cancelButton,
			AjaxButton deleteButton,
			AjaxButton editButton,
			AjaxButton editCancelButton) {
		this.saveButton = saveButton;
		this.cancelButton = cancelButton;
		this.deleteButton = deleteButton;
		this.editButton = editButton;
		this.editCancelButton = editCancelButton;
	}

	/*
	 *  The following methods are for moving between the different "screens"
	 */

	public void showSearchResults(Collection<GenoCollection> genoCollectionCol, AjaxRequestTarget target) {
		containerForm.getModelObject().setGenoCollectionCollection(genoCollectionCol);
		pageableListView.removeAll();
		searchResultPanelContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(searchResultPanelContainer);// For ajax this is required so
	}
	
	public void refreshFeedback(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}
	
	public void showSearch(AjaxRequestTarget target) {
		// old code for onCancel
//		searchResultPanelContainer.setVisible(false); //Hide the Search Result List Panel via the WebMarkupContainer
//		detailPanelContainer.setVisible(false); //Hide the Detail Panle via the WebMarkupContainer
//		target.addComponent(detailPanelContainer);//Attach the Detail WebMarkupContainer to be re-rendered using Ajax
//		target.addComponent(searchResultPanelContainer);//Attach the resultListContainer WebMarkupContainer to be re-rendered using Ajax
		
		// merged from both the onCancel and onEditCancel 
		searchResultPanelContainer.setVisible(false);
		detailPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(true);

		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(searchResultPanelContainer);
	}

	public void refreshDetail(AjaxRequestTarget target) {
		target.addComponent(detailPanelContainer);
	}

	public void showNewDetail(AjaxRequestTarget target) {

		deleteButton.setEnabled(false);	//disable the delete button in new mode
//		deleteButton.setVisible(false);

		detailPanelContainer.setVisible(true);
		searchResultPanelContainer.setVisible(false);
		editButtonContainer.setVisible(true);
		viewButtonContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		detailPanelFormContainer.setEnabled(true);

		target.addComponent(detailPanelContainer);
		target.addComponent(searchResultPanelContainer);
		target.addComponent(searchPanelContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		target.addComponent(detailPanelFormContainer);		
	}
	
	public void showEditDetail(AjaxRequestTarget target) {
		deleteButton.setEnabled(true);	//enable the delete button in view mode
		deleteButton.setVisible(true);
		viewButtonContainer.setVisible(false);
		editButtonContainer.setVisible(true);
		detailPanelFormContainer.setEnabled(true);

		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		target.addComponent(detailPanelFormContainer);
	}
	
	protected void showViewDetail(AjaxRequestTarget target){
		
		detailPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		detailPanelFormContainer.setEnabled(false);
		searchResultPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		editButtonContainer.setVisible(false);
		
		target.addComponent(searchResultPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(detailPanelFormContainer);
		target.addComponent(searchPanelContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		
	}

	public void updateContext(AjaxRequestTarget target, GenoCollection genoCollection) {
		// Acquire the contextMarkupContainer after this component has been added to the page
		if (arkContextMarkup == null) {			
			arkContextMarkup = (WebMarkupContainer) this.getPage().get("contextMarkupContainer");		
		}

		ContextHelper contextHelper = new ContextHelper();
		contextHelper.setStudyContextLabel(target, genoCollection.getStudy().getName(), arkContextMarkup);
		contextHelper.setGenoContextLabel(target, genoCollection.getName(), arkContextMarkup);
	}
	
	@Deprecated
	public void showDetail(AjaxRequestTarget target) {
		detailPanelContainer.setVisible(true);
		detailPanelFormContainer.setEnabled(false);
		searchResultPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		
		// Button containers
		// View Field, thus view container visible
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		editButtonContainer.setVisible(false);
		
		// Have to Edit, before allowing delete
		//Instead of: detailPanel.getDetailForm().getDeleteButton().setEnabled(false);
//		AjaxButton detailDeleteBtn = (AjaxButton)detailsFormContainer.get(Constants.DETAILS_DELETE_BTN);
//		detailDeleteBtn.setEnabled(false);

		target.addComponent(searchResultPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(searchPanelContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
	}

}
