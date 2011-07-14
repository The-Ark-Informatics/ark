package au.org.theark.study.web.component.managestudy;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.web.component.managestudy.form.Container;

public class StudyContainer extends AbstractContainerPanel<StudyModelVO>
{

	private static final long			serialVersionUID	= 1L;
	private Container						containerForm;
	private Details						detailsPanel;
	private SearchResults				searchResultsPanel;
	private Search							searchStudyPanel;
	private TabbedPanel					moduleTabbedPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	private IModel<Object>				iModel;
	private StudyCrudContainerVO		studyCrudContainerVO;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param studyNameMarkup
	 * @param studyLogoMarkup
	 * @param arkContextMarkup
	 */
	public StudyContainer(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup)
	{

		super(id, true);
		cpModel = new CompoundPropertyModel<StudyModelVO>(new StudyModelVO());
		// Create the form that will hold the other controls
		containerForm = new Container("containerForm", cpModel);

		/* Initialise the study crud container vo that has all the WebMarkups */
		studyCrudContainerVO = new StudyCrudContainerVO();
		// Set the Markups that was passed in into the VO
		studyCrudContainerVO.setStudyNameMarkup(studyNameMarkup);
		studyCrudContainerVO.setStudyLogoMarkup(studyLogoMarkup);
		studyCrudContainerVO.setArkContextMarkup(arkContextMarkup);

		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param studyNameMarkup
	 * @param studyLogoMarkup
	 * @param arkContextMarkup
	 * @param moduleTabbedPanel
	 */
	public StudyContainer(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup, TabbedPanel moduleTabbedPanel)
	{
		super(id, true);
		this.moduleTabbedPanel = moduleTabbedPanel;

		cpModel = new CompoundPropertyModel<StudyModelVO>(new StudyModelVO());
		// Create the form that will hold the other controls
		containerForm = new Container("containerForm", cpModel);

		/* Initialise the study crud container vo that has all the WebMarkups */
		studyCrudContainerVO = new StudyCrudContainerVO();
		// Set the Markups that was passed in into the VO
		studyCrudContainerVO.setStudyNameMarkup(studyNameMarkup);
		studyCrudContainerVO.setStudyLogoMarkup(studyLogoMarkup);
		studyCrudContainerVO.setArkContextMarkup(arkContextMarkup);

		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchPanel()
	{

		searchStudyPanel = new Search("searchStudyPanel", studyCrudContainerVO, feedBackPanel, containerForm);
		searchStudyPanel.initialisePanel(cpModel);
		studyCrudContainerVO.getSearchPanelContainer().add(searchStudyPanel);
		return studyCrudContainerVO.getSearchPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults()
	{

		searchResultsPanel = new SearchResults("searchResults", studyCrudContainerVO, containerForm, moduleTabbedPanel);
		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				List<Study> studyList = new ArrayList<Study>();
				studyList = iArkCommonService.getStudy(containerForm.getModelObject().getStudy());
				studyCrudContainerVO.getPageableListView().removeAll();
				return studyList;
			}
		};

		studyCrudContainerVO.setPageableListView(searchResultsPanel.buildPageableListView(iModel, studyCrudContainerVO.getSearchResultPanelContainer()));
		studyCrudContainerVO.getPageableListView().setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", studyCrudContainerVO.getPageableListView());
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(studyCrudContainerVO.getPageableListView());
		studyCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return studyCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel()
	{
		detailsPanel = new Details("detailsPanel", feedBackPanel, studyCrudContainerVO, containerForm);
		detailsPanel.initialisePanel();
		studyCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return studyCrudContainerVO.getDetailPanelContainer();
	}

	/**
	 * @param moduleTabbedPanel
	 *           the moduleTabbedPanel to set
	 */
	public void setModuleTabbedPanel(TabbedPanel moduleTabbedPanel)
	{
		this.moduleTabbedPanel = moduleTabbedPanel;
	}

	/**
	 * @return the moduleTabbedPanel
	 */
	public TabbedPanel getModuleTabbedPanel()
	{
		return moduleTabbedPanel;
	}
}