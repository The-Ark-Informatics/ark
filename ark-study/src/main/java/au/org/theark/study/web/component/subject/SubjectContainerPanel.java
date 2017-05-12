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
package au.org.theark.study.web.component.subject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.export.ExportToolbar;
import au.org.theark.core.web.component.export.ExportableDateColumn;
import au.org.theark.core.web.component.export.ExportableTextColumn;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class SubjectContainerPanel extends AbstractContainerPanel<SubjectVO> {

	private static final long serialVersionUID = 2166285054533611912L;
	private static final Logger log = LoggerFactory.getLogger(SubjectContainerPanel.class);
	private SearchPanel searchPanel;
	private SearchResultListPanel searchResultsPanel;
	private DetailPanel detailPanel;
	private PageableListView<SubjectVO> pageableListView;
	private ContainerForm containerForm;

	private WebMarkupContainer arkContextMarkup;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService iStudyService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	private DataView<SubjectVO> dataView;
	private ArkDataProvider<SubjectVO, IArkCommonService> subjectProvider;
	private Long sessionStudyId;
	private Study study = new Study();
	protected WebMarkupContainer studyNameMarkup;
	protected WebMarkupContainer studyLogoMarkup;
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");

	/**
	 * @param id
	 * @param studyLogoMarkup
	 * @param studyNameMarkup
	 */
	public SubjectContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<SubjectVO>(new SubjectVO());

		// Restrict to subjects in current study in session
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
			List<Study> studyListForUser = iArkCommonService.getParentAndChildStudies(sessionStudyId);
			cpModel.getObject().setStudyList(studyListForUser);
		}

		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		prerenderContextCheck();

		add(containerForm);
	}

	/**
	 * Re-use in pedigree panel
	 * 
	 * @param id
	 * @param studyLogoMarkup
	 * @param studyNameMarkup
	 * @param modalWindow
	 */
	public SubjectContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, AbstractDetailModalWindow modalWindow, String gender, List<RelationshipVo> relatives) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;

		/* Initialise the CPM */

		SubjectVO subjectVO = new SubjectVO();
		subjectVO.setEnableNewButton(false);
		cpModel = new CompoundPropertyModel<SubjectVO>(subjectVO);

		// Restrict to subjects in current study in session
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
			List<Study> studyListForUser = iArkCommonService.getParentAndChildStudies(sessionStudyId);
			cpModel.getObject().setStudyList(studyListForUser);
		}

		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchResults(modalWindow, gender, relatives));
		containerForm.add(initialiseSearchPanel());

		arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("genderType").setEnabled(false);
		arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("study").setEnabled(false);

		add(containerForm);
	}

	@SuppressWarnings("unchecked")
	protected void prerenderContextCheck() {
		// Get the Person in Context and determine the Person Type
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		if ((sessionStudyId != null) && (sessionPersonId != null)) {
			String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);
			if (sessionPersonType.equals(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)) {
				Person person;
				boolean contextLoaded = false;
				try {
					person = studyService.getPerson(sessionPersonId);
					SubjectVO subjectVO = new SubjectVO();
					subjectVO.getLinkSubjectStudy().setPerson(person); // must
																		// have
																		// Person
					subjectVO.getLinkSubjectStudy().setStudy(study); // must
																		// have
																		// Study
					List<SubjectVO> subjectList = (List<SubjectVO>) iArkCommonService.getSubject(subjectVO);
					subjectList.get(0).setStudyList(cpModel.getObject().getStudyList());
					containerForm.setModelObject(subjectList.get(0));
					contextLoaded = true;
				} catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				} catch (ArkSystemException e) {
					log.error(e.getMessage());
				}

				if (contextLoaded) {
					// Available/assigned child studies
					List<Study> availableChildStudies = new ArrayList<Study>(0);
					List<Study> selectedChildStudies = new ArrayList<Study>(0);

					if (containerForm.getModelObject().getLinkSubjectStudy().getStudy().getParentStudy() != null) {
						availableChildStudies = iStudyService.getChildStudyListOfParent(containerForm.getModelObject().getLinkSubjectStudy().getStudy());
						selectedChildStudies = iArkCommonService.getAssignedChildStudyListForPerson(containerForm.getModelObject().getLinkSubjectStudy().getStudy(), containerForm.getModelObject().getLinkSubjectStudy().getPerson());
					}

					containerForm.getModelObject().setAvailableChildStudies(availableChildStudies);
					containerForm.getModelObject().setSelectedChildStudies(selectedChildStudies);

					// Put into Detail View mode
					arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
					arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
					arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
					arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
					arkCrudContainerVO.getEditButtonContainer().setVisible(false);
				}
			}
		}
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		containerForm.getModelObject().getLinkSubjectStudy().setStudy(study);

		searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, pageableListView, arkCrudContainerVO, containerForm, arkContextMarkup, studyNameMarkup, studyLogoMarkup);

		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, arkContextMarkup, containerForm, arkCrudContainerVO, studyNameMarkup, studyLogoMarkup);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@SuppressWarnings("unchecked")
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultListPanel("searchResults", arkContextMarkup, containerForm, arkCrudContainerVO, studyNameMarkup, studyLogoMarkup);

		if (sessionStudyId != null) {
			LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
			linkSubjectStudy.setStudy(study);
			// containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
		}

		// Data providor to paginate resultList
		subjectProvider = new ArkDataProvider<SubjectVO, IArkCommonService>(iArkCommonService) {

			private static final long serialVersionUID = 1L;

			public int size() {
				return (int) service.getStudySubjectCount(model.getObject());
			}

			public Iterator<SubjectVO> iterator(int first, int count) {
				List<SubjectVO> listSubjects = new ArrayList<SubjectVO>();
				if (isActionPermitted()) {
					listSubjects = iArkCommonService.searchPageableSubjects(model.getObject(), first, count);
				}
				return listSubjects.iterator();
			}
		};

		TextField<OtherID> txtFld = ((TextField<OtherID>) containerForm.get("searchContainer:searchComponentPanel:searchForm:otherID"));
		String otherIDSearch = txtFld != null ? txtFld.getValue() : null;
		if (otherIDSearch != null) {
			OtherID o;
			o = new OtherID();
			o.setOtherID(otherIDSearch);
//			List<OtherID> otherIDs = new ArrayList<OtherID>();
//			otherIDs.add(o);
			cpModel.getObject().getLinkSubjectStudy().getPerson().getOtherIDs().clear();//setOtherIDs(otherIDs);
			cpModel.getObject().getLinkSubjectStudy().getPerson().getOtherIDs().add(o);
		}

		subjectProvider.setModel(this.cpModel);

		dataView = searchResultsPanel.buildDataView(subjectProvider);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());

		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		resultsWmc.add(pageNavigator);

		List<IColumn<SubjectVO>> columns = new ArrayList<IColumn<SubjectVO>>();
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("SubjectUID"), "subjectUID"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Full Name"), "subjectFullName"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Preferred Name"), "linkSubjectStudy.person.preferredName"));
		columns.add(new ExportableDateColumn<SubjectVO>(Model.of("Date Of Birth"), "linkSubjectStudy.person.dateOfBirth", au.org.theark.core.Constants.DD_MM_YYYY));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Vital Status"), "linkSubjectStudy.person.vitalStatus.name"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Gender"), "linkSubjectStudy.person.genderType.name"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Subject Status"), "linkSubjectStudy.subjectStatus.name"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Consent Status"), "linkSubjectStudy.consentStatus.name"));

		DataTable table = new DataTable("datatable", columns, dataView.getDataProvider(), iArkCommonService.getRowsPerPage());
		List<String> headers = new ArrayList<String>(0);
		headers.add("SubjectUID");
		headers.add("Full Name");
		headers.add("Preferred Name");
		headers.add("Date of Birth");
		headers.add("Vital Status");
		headers.add("Gender");
		headers.add("Subject Status");
		headers.add("Consent Status");

		String filename = study.getName() + "_subjects";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		resultsWmc.add(toolbars);

		resultsWmc.add(dataView);
		searchResultsPanel.add(resultsWmc);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@SuppressWarnings("unchecked")
	protected WebMarkupContainer initialiseSearchResults(AbstractDetailModalWindow modalWindow, final String gender, final List<RelationshipVo> relatives) {
		searchResultsPanel = new SearchResultListPanel("searchResults", arkContextMarkup, containerForm, arkCrudContainerVO, studyNameMarkup, studyLogoMarkup);
		searchResultsPanel.setOutputMarkupId(true);

		if (sessionStudyId != null) {
			LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
			linkSubjectStudy.setStudy(study);
			// containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
		}

		// Data providor to paginate resultList
		subjectProvider = new ArkDataProvider<SubjectVO, IArkCommonService>(iArkCommonService) {

			private static final long serialVersionUID = 1L;

			private GenderType genderType;

			{
				Collection<GenderType> genderTypes = service.getGenderTypes();
				for (GenderType type : genderTypes) {
					if (gender.equalsIgnoreCase(type.getName())) {
						this.genderType = type;
						break;
					}
				}
			}

			public int size() {
				String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				model.getObject().getRelativeUIDs().add(subjectUID);
				// TODO comment this block to check inbred relatives
				Boolean inbreedAllowed = (Boolean)SecurityUtils.getSubject().getSession().getAttribute(Constants.INBREED_ALLOWED);
				if (BooleanUtils.isNotTrue(inbreedAllowed)) {
					for (RelationshipVo relationshipVo : relatives) {
						model.getObject().getRelativeUIDs().add(relationshipVo.getIndividualId());
					}
				}else{
					 List<RelationshipVo> childRelatives= iStudyService.getSubjectChildren(subjectUID, sessionStudyId);
					 for (RelationshipVo relationshipVo : childRelatives) {
						model.getObject().getRelativeUIDs().add(relationshipVo.getIndividualId());
					}
				}
				model.getObject().getLinkSubjectStudy().getPerson().setGenderType(genderType);
				return (int) service.getStudySubjectCount(model.getObject());
			}

			public Iterator<SubjectVO> iterator(int first, int count) {
				List<SubjectVO> listSubjects = new ArrayList<SubjectVO>();
				if (isActionPermitted()) {
					model.getObject().getLinkSubjectStudy().getPerson().setGenderType(genderType);
					String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
					model.getObject().getRelativeUIDs().add(subjectUID);
					// TODO comment this block to check inbred relatives
					Boolean inbreedAllowed = (Boolean)SecurityUtils.getSubject().getSession().getAttribute(Constants.INBREED_ALLOWED);
					if (BooleanUtils.isNotTrue(inbreedAllowed)) {
						for (RelationshipVo relationshipVo : relatives) {
							model.getObject().getRelativeUIDs().add(relationshipVo.getIndividualId());
						}
					}else{
						 List<RelationshipVo> childRelatives= iStudyService.getSubjectChildren(subjectUID, sessionStudyId);
						 for (RelationshipVo relationshipVo : childRelatives) {
							model.getObject().getRelativeUIDs().add(relationshipVo.getIndividualId());
						}
					}
					listSubjects = iArkCommonService.searchPageableSubjects(model.getObject(), first, count);
				}
				return listSubjects.iterator();
			}
		};
		subjectProvider.setModel(this.cpModel);

		dataView = searchResultsPanel.buildDataView(subjectProvider, modalWindow, relatives, feedBackPanel);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(searchResultsPanel);
			}
		};
		resultsWmc.add(pageNavigator);

		List<IColumn<SubjectVO>> columns = new ArrayList<IColumn<SubjectVO>>();
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("SubjectUID"), "subjectUID"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Full Name"), "subjectFullName"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Preferred Name"), "linkSubjectStudy.person.preferredName"));
		columns.add(new ExportableDateColumn<SubjectVO>(Model.of("Date Of Birth"), "linkSubjectStudy.person.dateOfBirth", au.org.theark.core.Constants.DD_MM_YYYY));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Vital Status"), "linkSubjectStudy.person.vitalStatus.name"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Gender"), "linkSubjectStudy.person.genderType.name"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Subject Status"), "linkSubjectStudy.subjectStatus.name"));
		columns.add(new ExportableTextColumn<SubjectVO>(Model.of("Consent Status"), "linkSubjectStudy.consentStatus.name"));

		DataTable table = new DataTable("datatable", columns, dataView.getDataProvider(), iArkCommonService.getRowsPerPage());
		List<String> headers = new ArrayList<String>(0);
		headers.add("SubjectUID");
		headers.add("Full Name");
		headers.add("Preferred Name");
		headers.add("Date of Birth");
		headers.add("Vital Status");
		headers.add("Gender");
		headers.add("Subject Status");
		headers.add("Consent Status");

		String filename = study.getName() + "_subjects";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		resultsWmc.add(toolbars);

		resultsWmc.add(dataView);
		searchResultsPanel.add(resultsWmc);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

}
