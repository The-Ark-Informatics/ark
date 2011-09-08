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
package au.org.theark.lims.web.component.biocollection.form;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biocollection.DetailPanel;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<LimsVO> {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 3935670037697869845L;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService								iLimsService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	private PageableListView<BioCollection>	listView;
	private CompoundPropertyModel<LimsVO>		cpmModel;
	private TextField<String>						idTxtFld;
	private TextField<String>						nameTxtFld;
	private DateTextField							collectionDateTxtFld;
	private DateTextField							surgeryDateTxtFld;
	private DetailPanel								detailPanel;
	private Long										sessionStudyId;
	private WebMarkupContainer						arkContextMarkup;
	private String										subjectUIDInContext;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<LimsVO> model, PageableListView<BioCollection> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer, WebMarkupContainer arkContextMarkup) {

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.setCpmModel(model);
		this.listView = listView;
		this.setDetailPanel(detailPanel);
		this.setArkContextMarkup(arkContextMarkup);
		initialiseFieldForm();

		// Override New button, disabling
		newButton = new ArkBusyAjaxButton(Constants.NEW) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 4695227309689500914L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				target.addComponent(feedbackPanel);
			}
		};
		addOrReplace(newButton);
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<LimsVO> compoundPropertyModel) {
		super(id, compoundPropertyModel);
		this.setCpmModel(compoundPropertyModel);
		initialiseFieldForm();
	}

	public void initialiseFieldForm() {
		idTxtFld = new TextField<String>("bioCollection.id");
		nameTxtFld = new TextField<String>("bioCollection.name");
		new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField("bioCollection.collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("bioCollection.surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);

		addFieldComponents();
	}

	private void addFieldComponents() {
		add(idTxtFld);
		add(nameTxtFld);
		add(collectionDateTxtFld);
		add(surgeryDateTxtFld);
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		LimsVO limsVo = getModelObject();
		limsVo.getBioCollection().setId(null); // must ensure Id is blank onNew

		// Set study for the new collection
		this.sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		limsVo.getBioCollection().setStudy(study);

		setModelObject(limsVo);
		preProcessDetailPanel(target);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		// Refresh the FB panel if there was an old message from previous search result
		target.addComponent(feedbackPanel);

		// Get a list of collections for the study/subject in context by default
		java.util.List<au.org.theark.core.model.lims.entity.BioCollection> bioCollectionList = new ArrayList<au.org.theark.core.model.lims.entity.BioCollection>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null && sessionStudyId > 0) {
			Study study = iArkCommonService.getStudy(sessionStudyId);
			BioCollection bioCollection = new BioCollection();
			bioCollection.setStudy(study);

			try {
				bioCollectionList = iLimsService.searchBioCollection(getModelObject().getBioCollection());
			}
			catch (ArkSystemException e) {
				this.error(e.getMessage());
			}
		}

		if (bioCollectionList != null && bioCollectionList.size() == 0) {
			this.info("Collections with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}
		getModelObject().setBioCollectionList(bioCollectionList);
		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);// For ajax this is required so
	}

	/**
	 * @param arkContextMarkup
	 *           the arkContextMarkup to set
	 */
	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup) {
		this.arkContextMarkup = arkContextMarkup;
	}

	/**
	 * @return the arkContextMarkup
	 */
	public WebMarkupContainer getArkContextMarkup() {
		return arkContextMarkup;
	}

	/**
	 * @param detailPanel
	 *           the detailPanel to set
	 */
	public void setDetailPanel(DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	/**
	 * @return the detailPanel
	 */
	public DetailPanel getDetailPanel() {
		return detailPanel;
	}

	/**
	 * @param cpmModel
	 *           the cpmModel to set
	 */
	public void setCpmModel(CompoundPropertyModel<LimsVO> cpmModel) {
		this.cpmModel = cpmModel;
	}

	/**
	 * @return the cpmModel
	 */
	public CompoundPropertyModel<LimsVO> getCpmModel() {
		return cpmModel;
	}
}
