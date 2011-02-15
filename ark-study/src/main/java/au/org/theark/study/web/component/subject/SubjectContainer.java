
package au.org.theark.study.web.component.subject;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class SubjectContainer extends AbstractContainerPanel<SubjectVO>{


	private Search searchPanel;
	private SearchResults searchResultsPanel;
	private Details detailsPanel;
	private PageableListView<SubjectVO> pageableListView;
	private ContainerForm containerForm;
	
	private WebMarkupContainer arkContextMarkup;
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	

	/**
	 * @param id
	 */
	public SubjectContainer(String id,WebMarkupContainer arkContextMarkup) {
		
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		/*Initialise the CPM */
		cpModel = new CompoundPropertyModel<SubjectVO>(new SubjectVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
		
	}
	
	protected WebMarkupContainer initialiseSearchPanel(){
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		Collection<SubjectVO> subjectVOCollection = new ArrayList<SubjectVO>();
		
		if(sessionStudyId != null && sessionStudyId > 0){
			
			containerForm.getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId));
			subjectVOCollection = iArkCommonService.getSubject(containerForm.getModelObject());	
		}
		
		cpModel.getObject().setSubjectList(subjectVOCollection);
		
		searchPanel = new Search("searchComponentPanel",
									feedBackPanel,
									searchPanelContainer,
									pageableListView,
									searchResultPanelContainer,
									detailPanelContainer,
									detailPanelFormContainer,
									viewButtonContainer,
									editButtonContainer,
									detailsPanel,
									containerForm);

		searchPanel.initialisePanel(cpModel);
		searchPanelContainer.add(searchPanel);
		return searchPanelContainer;
	}
	
	
	protected WebMarkupContainer initialiseDetailPanel(){
		
		detailsPanel = new Details("detailsPanel",feedBackPanel,searchResultPanelContainer,detailPanelContainer,detailPanelFormContainer,searchPanelContainer,viewButtonContainer,editButtonContainer,arkContextMarkup,containerForm);
		detailsPanel.initialisePanel();
		detailPanelContainer.add(detailsPanel);
		return detailPanelContainer;
	}
	
	protected WebMarkupContainer initialiseSearchResults(){
		
		searchResultsPanel = new SearchResults("searchResults",detailPanelContainer,detailPanelFormContainer,searchPanelContainer,searchResultPanelContainer,viewButtonContainer,editButtonContainer,arkContextMarkup,containerForm);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Object load() {
				Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId));
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(containerForm.getModelObject());
				containerForm.getModelObject().setSubjectList(subjects);
				return subjects;
			}
		};

		pageableListView  = searchResultsPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultsPanel);
		return searchResultPanelContainer;
	}

}
