package au.org.theark.study.web.component.subjectFile;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectFile.form.ContainerForm;

public class SubjectFileContainerPanel extends AbstractContainerPanel<SubjectVO>
{

	private static final long					serialVersionUID	= 1L;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	// Container Form
	private ContainerForm						containerForm;
	// Panels
	// private SearchResultListPanel searchResultListPanel;
	private SearchPanel							searchPanel;
	private DetailPanel							detailPanel;
	private PageableListView<SubjectFile>	pageableListView;

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public SubjectFileContainerPanel(String id)
	{

		super(id);
		cpModel = new CompoundPropertyModel<SubjectVO>(new SubjectVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	public SubjectFileContainerPanel(String id, SubjectVO subjectVo)
	{
		super(id);
		// Use consentVo in context from consent page
		cpModel = new CompoundPropertyModel<SubjectVO>(subjectVo);
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel().setVisible(true));
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel().setVisible(false));
		add(containerForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel()
	{

		detailPanel = new DetailPanel("detailsPanel", feedBackPanel, searchResultPanelContainer, detailPanelContainer, detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, containerForm);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel()
	{
		// Get the Person in Context and determine the Person Type
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		Collection<SubjectFile> subjectFileList = new ArrayList<SubjectFile>();

		// All the subject file items related to the subject if one found in session or an empty list
		cpModel.getObject().setSubjectFileList(subjectFileList);
		searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, searchPanelContainer, pageableListView, searchResultPanelContainer, detailPanelContainer, detailPanel, containerForm,
				viewButtonContainer, editButtonContainer, detailPanelFormContainer);
		searchPanel.initialisePanel(cpModel);
		searchPanelContainer.add(searchPanel);

		return searchPanelContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults()
	{

		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults", detailPanelContainer, detailPanelFormContainer, searchPanelContainer, searchResultPanelContainer,
				viewButtonContainer, editButtonContainer, containerForm);

		iModel = new LoadableDetachableModel<Object>()
		{

			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				// Get the PersonId from session and get the subjectFileList from back end
				Collection<SubjectFile> subjectFileList = new ArrayList<SubjectFile>();
				try
				{
					if(isActionPermitted()){
						
						SubjectFile subjectFile = containerForm.getModelObject().getSubjectFile();
						Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
						LinkSubjectStudy linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId);
						subjectFile.setLinkSubjectStudy(linkSubjectStudy);
						subjectFileList = studyService.searchSubjectFile(subjectFile);
					}
				}
				catch (EntityNotFoundException e)
				{
					containerForm.error("The person ID/Subject in context does not exist in the system.");
				}
				catch (ArkSystemException e)
				{
					containerForm.error("A System error has occured. Please contact Support");
				}

				pageableListView.removeAll();
				return subjectFileList;
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
}