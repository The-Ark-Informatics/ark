package au.org.theark.study.web.component.calendar;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyCalendar;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.model.vo.StudyCalendarVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.calendar.form.ContainerForm;

public class CalendarContainerPanel extends AbstractContainerPanel<StudyCalendarVo> {

	private static final long serialVersionUID = 1L;

	// Panels
	private SearchPanel searchComponentPanel;
	private SearchResultListPanel searchResultPanel;
	private DetailPanel detailsPanel;

	private PageableListView<StudyCalendar> pageableListView;

	private ContainerForm containerForm;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	public CalendarContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<StudyCalendarVo>(new StudyCalendarVo());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());

		containerForm.add(initialiseDetailPanel());

		containerForm.add(initialiseSearchResults());

		containerForm.add(initialiseSearchPanel());

		add(containerForm);

	}

	protected WebMarkupContainer initialiseSearchResults() {

		searchResultPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				Long studySessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (isActionPermitted() && studySessionId != null) {
					Study studyInContext = iArkCommonService.getStudy(studySessionId);
					containerForm.getModelObject().getStudyCalendar().setStudy(studyInContext);
					containerForm.getModelObject().setStudyCalendarList(studyService.searchStudyCalenderList(containerForm.getModelObject().getStudyCalendar()));
				}

				pageableListView.removeAll();
				return containerForm.getModelObject().getStudyCalendarList();
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailsPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		StudyCalendarVo studyCalendarVo = new StudyCalendarVo();

		// Get a result-set by default
		List<StudyCalendar> resultList = new ArrayList<StudyCalendar>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null && sessionStudyId > 0) {
			Study studyInContext = iArkCommonService.getStudy(sessionStudyId);
			studyCalendarVo.getStudyCalendar().setStudy(studyInContext);
			resultList = studyService.searchStudyCalenderList(studyCalendarVo.getStudyCalendar());
		}

		cpModel.getObject().setStudyCalendarList(resultList);
		searchComponentPanel = new SearchPanel("searchComponentPanel", arkCrudContainerVO, feedBackPanel, containerForm, pageableListView);
		searchComponentPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchComponentPanel);
		return arkCrudContainerVO.getSearchPanelContainer();

	}

}