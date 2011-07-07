package au.org.theark.study.web.component.studycomponent;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;


public class StudyComponentContainerPanel extends AbstractContainerPanel<StudyCompVo>{

	private static final long serialVersionUID = 1L;

	
	//Panels
	private Search searchComponentPanel;
	private SearchResultList searchResultPanel;
	private Details detailsPanel;

	private PageableListView<StudyComp> pageableListView;

	private ContainerForm containerForm;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	
	public StudyComponentContainerPanel(String id) {
		super(id);
		
		/*Initialise the CPM */
		cpModel = new CompoundPropertyModel<StudyCompVo>(new StudyCompVo());
		
		/*Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		
		containerForm.add(initialiseFeedBackPanel());
	
		containerForm.add(initialiseDetailPanel());
	
		containerForm.add(initialiseSearchResults());
		
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
		
	}


	protected WebMarkupContainer initialiseSearchResults(){
		
		searchResultPanel = new SearchResultList("searchResults",
												detailPanelContainer,
												detailPanelFormContainer,
												searchPanelContainer,
												searchResultPanelContainer,
												viewButtonContainer,
												editButtonContainer,
												containerForm	);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				
				 try {
					 Long studySessionId  = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					if(isActionPermitted() && studySessionId!= null){
						 Study studyInContext = iArkCommonService.getStudy(studySessionId);
						 containerForm.getModelObject().getStudyComponent().setStudy(studyInContext);
						 containerForm.getModelObject().setStudyCompList(studyService.searchStudyComp(containerForm.getModelObject().getStudyComponent())); 
					 }
					 
				} catch (ArkSystemException e) {
					containerForm.error("A System Exception has occured please contact Support");
				}
				pageableListView.removeAll();
				return containerForm.getModelObject().getStudyCompList();
			}
		};

		pageableListView  = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;
	}
	
	
	protected WebMarkupContainer initialiseDetailPanel(){
		
		detailsPanel = new Details("detailsPanel",
									feedBackPanel,
									searchResultPanelContainer, 
									detailPanelContainer,
									detailPanelFormContainer,
									searchPanelContainer,
									viewButtonContainer,
									editButtonContainer,
									containerForm);
		detailsPanel.initialisePanel();
		detailPanelContainer.add(detailsPanel);
		return detailPanelContainer;
		
	}
	
	protected WebMarkupContainer initialiseSearchPanel(){
		
		StudyCompVo studyCompVo = new StudyCompVo();
		
		//Get a result-set by default
		List<StudyComp> resultList = new ArrayList<StudyComp>();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		try {
			if(sessionStudyId != null && sessionStudyId > 0){
				resultList = studyService.searchStudyComp(studyCompVo.getStudyComponent());	
			}
			
		} catch (ArkSystemException e) {
			this.error("A System error occured  while initializing Search Panel");
		}
		
		cpModel.getObject().setStudyCompList(resultList);
		
		searchComponentPanel = new Search(	"searchComponentPanel", 
											feedBackPanel, 
											searchPanelContainer, 
											pageableListView,
											searchResultPanelContainer,
											detailPanelContainer,
											detailPanelFormContainer,
											viewButtonContainer,
											editButtonContainer,
											detailsPanel,
											containerForm
										);
		
		searchComponentPanel.initialisePanel(cpModel);
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
	

}
