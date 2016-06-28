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
package au.org.theark.report.web.component.dataextraction.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.phenotypic.service.IPhenotypicService;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.web.form.AbstractSearchForm;

/**
 * @author nivedann
 * 
 */
public class SearchForm extends AbstractSearchForm<SearchVO> {


	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;
	@SpringBean(name = Constants.ARK_PHENO_DATA_SERVICE)
	private IPhenotypicService 				iPhenoService;
	
	private static final long				serialVersionUID	= 1L;
	private ArkCrudContainerVO				arkCrudContainerVO;
	private TextField<String>				searchIdTxtFld;
	private TextField<String>				searchNameTxtFld;
	private PageableListView<Search>		listView;

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<SearchVO> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<Search> listView) {

		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		//Add the study for model object not sure why ignores.................
		getModelObject().getSearch().setStudy(iArkCommonService.getStudy(sessionStudyId));
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a Study.");
	}

	protected void addSearchComponentsToForm() {
		add(searchIdTxtFld);
		add(searchNameTxtFld);
	}

	protected void initialiseSearchForm() {

		searchIdTxtFld = new TextField<String>(Constants.SEARCH_ID);
		searchNameTxtFld = new TextField<String>(Constants.SEARCH_NAME);
	//	descriptionTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_DESCRIPTION);
//		keywordTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_KEYWORD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
//		getModelObject().setMode(Constants.MODE_NEW);
//		getModelObject().getStudyComponent().setId(null);
		
		SearchVO searchVo = new SearchVO();// containerForm.getModelObject();
		//searchVo.setMode(Constants.MODE_EDIT);
		searchVo.setSearch(new Search());// Sets the selected object into the model
		
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId); 
		Search search = searchVo.getSearch();
		search.setStudy(study);
		
		Collection<DemographicField> availableDemographicFields = iArkCommonService.getAllDemographicFields();
		this.getModelObject().setAvailableDemographicFields(availableDemographicFields);
		Collection<DemographicField> selectedDemographicFields = new ArrayList<DemographicField>();//iArkCommonService.getSelectedDemographicFieldsForSearch(search);//, true);
		this.getModelObject().setSelectedDemographicFields(selectedDemographicFields);


		Collection<BiospecimenField> availableBiospecimenFields = iArkCommonService.getAllBiospecimenFields();
		this.getModelObject().setAvailableBiospecimenFields(availableBiospecimenFields);
		Collection<BiospecimenField> selectedBiospecimenFields = new ArrayList<BiospecimenField>();//iArkCommonService.getSelectedBiospecimenFieldsForSearch(search);//, true);
		this.getModelObject().setSelectedBiospecimenFields(selectedBiospecimenFields);
		
		Collection<BiocollectionField> availableBiocollectionFields = iArkCommonService.getAllBiocollectionFields();
		this.getModelObject().setAvailableBiocollectionFields(availableBiocollectionFields);
		Collection<BiocollectionField> selectedBiocollectionFields =new ArrayList<BiocollectionField>();//iArkCommonService.getSelectedBiocollectionFieldsForSearch(search);//, true);
		this.getModelObject().setSelectedBiocollectionFields(selectedBiocollectionFields);

		

		ArkFunction arkFunctionPheno = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		//ArkFunction arkFunctionBiocollection = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
		ArkFunction arkFunctionBiocollection = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
		ArkFunction arkFunctionBiospecimen = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
		ArkFunction arkFunctionSubject = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);

		Collection<PhenoDataSetFieldDisplay> availablePhenoDataSetFieldDisplays = iPhenoService.getPhenoFieldDisplaysIn(study, arkFunctionPheno);
		this.getModelObject().setAvailablePhenoDataSetFieldDisplays(availablePhenoDataSetFieldDisplays);
		Collection<PhenoDataSetFieldDisplay> selectedPhenoCustomFieldDisplays = new ArrayList<PhenoDataSetFieldDisplay>();//iArkCommonService.getSelectedPhenoCustomFieldDisplaysForSearch(search);//, true);
		this.getModelObject().setSelectedPhenoDataSetFieldDisplays(selectedPhenoCustomFieldDisplays);


		Collection<CustomFieldDisplay> availableSubjectCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysIn(study, arkFunctionSubject);
		this.getModelObject().setAvailableSubjectCustomFieldDisplays(availableSubjectCustomFieldDisplays);
		Collection<CustomFieldDisplay> selectedSubjectCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();//iArkCommonService.getSelectedSubjectCustomFieldDisplaysForSearch(search);//, true);
		this.getModelObject().setSelectedSubjectCustomFieldDisplays(selectedSubjectCustomFieldDisplays);

		Collection<CustomFieldDisplay> availableBiocollectionCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysIn(study, arkFunctionBiocollection);
		this.getModelObject().setAvailableBiocollectionCustomFieldDisplays(availableBiocollectionCustomFieldDisplays);
		Collection<CustomFieldDisplay> selectedBiocollectionCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();//iArkCommonService.getSelectedBiocollectionCustomFieldDisplaysForSearch(search);//, true);
		this.getModelObject().setSelectedBiocollectionCustomFieldDisplays(selectedBiocollectionCustomFieldDisplays);


		Collection<CustomFieldDisplay> availableBiospecimenCustomFieldDisplays = iArkCommonService.getCustomFieldDisplaysIn(study, arkFunctionBiospecimen);
		this.getModelObject().setAvailableBiospecimenCustomFieldDisplays(availableBiospecimenCustomFieldDisplays);
		Collection<CustomFieldDisplay> selectedBiospecimenCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();//iArkCommonService.getSelectedBiospecimenCustomFieldDisplaysForSearch(search);//, true);
		this.getModelObject().setSelectedBiospecimenCustomFieldDisplays(selectedBiospecimenCustomFieldDisplays);

		preProcessDetailPanel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {

		target.add(feedbackPanel);
	//	try {

			//List<Search> resultList = iArkCommonService.getSearchesForThisStudy(getModelObject().getSearch().getStudy());
		List<Search> resultList = iArkCommonService.getSearchesForSearch(getModelObject().getSearch());
			if (resultList != null && resultList.size() == 0) {
				this.info("Searches with the specified criteria does not exist in the system.");
				target.add(feedbackPanel);
			}
			getModelObject().setListOfSearchesForResultList(resultList);
			listView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	//	}
	//	catch (ArkSystemException arkEx) {
	//		this.error("A system error has occured. Please try after sometime.");
	//	}

	}

}
