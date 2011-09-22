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
package au.org.theark.phenotypic.web.component.fieldData.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldData.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("unused")
public class SearchForm extends AbstractSearchForm<PhenoCollectionVO> {
	/**
	 * 
	 */
	private static final long									serialVersionUID	= 4602216854250133940L;
	protected transient Logger									log = LoggerFactory.getLogger(SearchForm.class);

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService									iPhenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>							iArkCommonService;

	private PageableListView<FieldData>						listView;
	private CompoundPropertyModel<PhenoCollectionVO>	cpmModel;
	private TextField<String>									fieldDataIdTxtFld;
	private DropDownChoice<PhenoCollection>				fieldDataCollectionDdc;
	private DropDownChoice<PhenoCollection>				fieldDataFieldDdc;
	private TextField<String>									fieldDataSubjectUIDTxtFld;

	private DetailPanel											detailPanel;
	private Long													sessionStudyId;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> model, PageableListView<FieldData> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel,
			WebMarkupContainer listContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer) {

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();

		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null && sessionStudyId > 0) {
			getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId));
		}
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");

		// hide New button for FieldData
		newButton = new AjaxButton(au.org.theark.core.Constants.NEW) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 6539600487179555764L;

			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("onError when newButton pressed");
			}
		};
		addOrReplace(newButton);
	}

	@SuppressWarnings("unchecked")
	private void initFieldDataCollectionDdc() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = new Study();
		java.util.Collection<PhenoCollection> fieldDataCollection = null;
		PhenoCollection phenotypicCollection = new PhenoCollection();

		if (sessionStudyId != null && sessionStudyId > 0) {
			study = iArkCommonService.getStudy(sessionStudyId);
			fieldDataCollection = iPhenotypicService.getPhenoCollectionByStudy(study);
		}
		else {
			fieldDataCollection = iPhenotypicService.searchPhenotypicCollection(phenotypicCollection);
		}

		ChoiceRenderer fieldDataCollRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_NAME, au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_ID);
		fieldDataCollectionDdc = new DropDownChoice<PhenoCollection>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, (List) fieldDataCollection, fieldDataCollRenderer);
	}

	@SuppressWarnings("unchecked")
	private void initFieldDataFieldDdc() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = new Study();
		Field field = new Field();

		if (sessionStudyId != null && sessionStudyId > 0) {
			study = iArkCommonService.getStudy(sessionStudyId);
			field.setStudy(study);
		}

		java.util.Collection<Field> fieldDataField = iPhenotypicService.searchField(field);
		ChoiceRenderer fieldDataFieldRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FIELD_NAME, au.org.theark.phenotypic.web.Constants.FIELD_ID);
		fieldDataFieldDdc = new DropDownChoice<PhenoCollection>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, (List) fieldDataField, fieldDataFieldRenderer);
	}

	public void initialiseFieldForm() {
		fieldDataIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_ID);
		fieldDataSubjectUIDTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID);
		initFieldDataCollectionDdc();
		initFieldDataFieldDdc();
		addFieldComponents();
	}

	private void addFieldComponents() {
		add(fieldDataIdTxtFld);
		add(fieldDataCollectionDdc);
		add(fieldDataFieldDdc);
		add(fieldDataSubjectUIDTxtFld);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		FieldData searchFieldData = getModelObject().getFieldData();
		target.add(feedbackPanel);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId));

		int count = iPhenotypicService.getStudyFieldDataCount(getModelObject());
		if (count == 0) {
			this.info("There are no field data records with the specified criteria.");
			target.add(feedbackPanel);
		}
		listContainer.setVisible(true);
		target.add(listContainer);
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// Should not be possible to get here (not allowed to create a new FieldData via GUI)
	}

	protected boolean isSecure(String actionType) {
		boolean flag = false;
		if (actionType.equalsIgnoreCase(au.org.theark.core.Constants.NEW)) {
			flag = false;
		}
		else {
			flag = true;
		}
		return flag;
	}
}
