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
package au.org.theark.phenotypic.web.component.phenoCollection.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.Status;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoCollection.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<PhenoCollectionVO> {
	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService									phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService									iArkCommonService;

	private PageableListView<PhenoCollection>				listView;
	private CompoundPropertyModel<PhenoCollectionVO>	cpmModel;
	private TextField<String>									phenoCollectionIdTxtFld;
	private TextField<String>									phenoCollectionNameTxtFld;
	private TextArea<String>									phenoCollectionDescriptionTxtAreaFld;
	private DropDownChoice<Status>							statusDdc;
	private DateTextField										phenoCollectionStartDateFld;
	private DateTextField										phenoCollectionEndDateFld;
	private DetailPanel											detailPanel;
	private Long													sessionStudyId;
	private WebMarkupContainer									arkContextMarkup;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> model, PageableListView<PhenoCollection> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel,
			WebMarkupContainer listContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer arkContextMarkup) {

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		this.setArkContextMarkup(arkContextMarkup);
		initialiseFieldForm();

		this.sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> compoundPropertyModel) {
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
	}

	private void initStatusDdc() {
		java.util.Collection<Status> statusCollection = phenotypicService.getStatus();
		CompoundPropertyModel<PhenoCollectionVO> phenoCollectionCpm = cpmModel;
		PropertyModel<PhenoCollection> phenoCollectionPm = new PropertyModel<PhenoCollection>(phenoCollectionCpm, au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION);
		PropertyModel<Status> statusPm = new PropertyModel<Status>(phenoCollectionPm, au.org.theark.phenotypic.web.Constants.STATUS);
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.STATUS_NAME, au.org.theark.phenotypic.web.Constants.STATUS_ID);
		statusDdc = new DropDownChoice<Status>(au.org.theark.phenotypic.web.Constants.STATUS, statusPm, (List) statusCollection, fieldTypeRenderer);
	}

	public void initialiseFieldForm() {
		phenoCollectionIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_ID);
		phenoCollectionNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_NAME);
		phenoCollectionDescriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION);
		phenoCollectionStartDateFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE, Constants.DD_MM_YYYY);
		phenoCollectionEndDateFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_END_DATE, Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(phenoCollectionStartDateFld);
		phenoCollectionStartDateFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(phenoCollectionEndDateFld);
		phenoCollectionEndDateFld.add(endDatePicker);

		initStatusDdc();
		addFieldComponents();
	}

	private void addFieldComponents() {
		add(phenoCollectionIdTxtFld);
		add(phenoCollectionNameTxtFld);
		add(statusDdc);
		add(phenoCollectionDescriptionTxtAreaFld);
		add(phenoCollectionStartDateFld);
		add(phenoCollectionEndDateFld);
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// Due to ARK-108 :: No longer reset the VO onNew(..)
		// Show the details panel name and description
		PhenoCollectionVO phenoCollectionVo = getModelObject();
		phenoCollectionVo.setMode(au.org.theark.core.Constants.MODE_NEW);
		phenoCollectionVo.getPhenoCollection().setId(null); // must ensure Id is blank onNew

		// Set study for the new collection and fields available
		this.sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		phenoCollectionVo.getPhenoCollection().setStudy(study);
		phenoCollectionVo.getField().setStudy(study);

		phenoCollectionVo.setFieldsAvailable(phenotypicService.searchField(phenoCollectionVo.getField()));

		setModelObject(phenoCollectionVo);
		preProcessDetailPanel(target);

	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		// Refresh the FB panel if there was an old message from previous search result
		target.add(feedbackPanel);

		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(studyId);

		PhenoCollection phenoCollection = getModelObject().getPhenoCollection();
		phenoCollection.setStudy(study);

		java.util.Collection<PhenoCollection> phenoCollectionCollection = phenotypicService.searchPhenotypicCollection(phenoCollection);

		if (phenoCollectionCollection != null && phenoCollectionCollection.size() == 0) {
			this.info("Phenotypic Collections with the specified criteria does not exist in the system.");
			target.add(feedbackPanel);
		}
		getModelObject().setPhenoCollectionCollection(phenoCollectionCollection);
		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.add(listContainer);// For ajax this is required so
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
}
