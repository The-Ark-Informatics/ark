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
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.export.ExportToolbar;
import au.org.theark.core.web.component.export.ExportablePropertyColumn;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class SubjectContainerPanel extends AbstractContainerPanel<SubjectVO> {

	private static final long										serialVersionUID	= 2166285054533611912L;
	private static final Logger									log					= LoggerFactory.getLogger(SubjectContainerPanel.class);
	private SearchPanel												searchPanel;
	private SearchResultListPanel									searchResultsPanel;
	private DetailPanel												detailPanel;
	private PageableListView<SubjectVO>							pageableListView;
	private ContainerForm											containerForm;

	private WebMarkupContainer										arkContextMarkup;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService											iStudyService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService											studyService;

	private DataView<SubjectVO>									dataView;
	private ArkDataProvider<SubjectVO, IArkCommonService>	subjectProvider;
	private Long														sessionStudyId;
	private Study														study = new Study();

	/**
	 * @param id
	 */
	public SubjectContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		
		// Restrict to subjects in current study in session
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
		}
		
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<SubjectVO>(new SubjectVO());
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		prerenderContextCheck();

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
					subjectVO.getLinkSubjectStudy().setPerson(person); // must have Person
					subjectVO.getLinkSubjectStudy().setStudy(study); // must have Study
					List<SubjectVO> subjectList = (List<SubjectVO>) iArkCommonService.getSubject(subjectVO);
					containerForm.setModelObject(subjectList.get(0));
					contextLoaded = true;
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
				catch (ArkSystemException e) {
					log.error(e.getMessage());
				}

				if (contextLoaded) {
					// Available/assigned child studies
					List<Study> availableChildStudies = new ArrayList<Study>(0);
					List<Study> selectedChildStudies = new ArrayList<Study>(0);

					if (containerForm.getModelObject().getLinkSubjectStudy().getStudy().getParentStudy() != null) {
						availableChildStudies = iStudyService.getChildStudyListOfParent(containerForm.getModelObject().getLinkSubjectStudy().getStudy());
						selectedChildStudies = iArkCommonService.getAssignedChildStudyListForPerson(containerForm.getModelObject().getLinkSubjectStudy().getStudy(), containerForm.getModelObject()
								.getLinkSubjectStudy().getPerson());
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

		searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, pageableListView, arkCrudContainerVO, containerForm);

		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, arkContextMarkup, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@SuppressWarnings("unchecked")
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultListPanel("searchResults", arkContextMarkup, containerForm, arkCrudContainerVO);

		if (sessionStudyId != null) {
			LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
			linkSubjectStudy.setStudy(study);
			containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
		}

		// Data providor to paginate resultList
		subjectProvider = new ArkDataProvider<SubjectVO, IArkCommonService>(iArkCommonService) {

			private static final long	serialVersionUID	= 1L;

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
		subjectProvider.setModel(this.cpModel);

		dataView = searchResultsPanel.buildDataView(subjectProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		searchResultsPanel.add(pageNavigator);

		List<IColumn<SubjectVO>> columns = new ArrayList<IColumn<SubjectVO>>();
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("SubjectUID"), "subjectUID"));
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("Full Name"), "subjectFullName"));
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("Preferred Name"), "linkSubjectStudy.person.preferredName"));
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("Date Of Birth"), "linkSubjectStudy.person.dateOfBirth"));
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("Vital Status"), "linkSubjectStudy.person.vitalStatus.name"));
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("Gender"), "linkSubjectStudy.person.genderType.name"));
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("Subject Status"), "linkSubjectStudy.subjectStatus.name"));
		columns.add(new ExportablePropertyColumn<SubjectVO>(Model.of("Consent Status"), "linkSubjectStudy.consentStatus.name"));

		DataTable table = new DataTable("datatable", columns, dataView.getDataProvider(), au.org.theark.core.Constants.ROWS_PER_PAGE);
		List<String> headers = new ArrayList<String>(0);
		headers.add("SubjectUID");
		headers.add("Full Name");
		headers.add("Preferred Name");
		headers.add("Date of Birth");
		headers.add("Vital Status");
		headers.add("Gender");
		headers.add("Subject Status");
		headers.add("Consent Status");

		String filename = study.getName() +"_subjects";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		searchResultsPanel.add(toolbars);

		searchResultsPanel.add(dataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
}
