package au.org.theark.study.web.component.managestudy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.DetailForm;

public class StudyContainer extends AbstractContainerPanel<StudyModelVO>{


	private static final long serialVersionUID = 1L;
	private Container containerForm;
	private Details detailsPanel;
	private SearchResults searchResultsPanel;
	private Search searchStudyPanel;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	private IModel<Object> iModel;
	private StudyCrudContainerVO studyCrudContainerVO;
	
	
	
	/**
	 * Constructor
	 * @param id
	 * @param studyNameMarkup
	 * @param studyLogoMarkup
	 * @param arkContextMarkup
	 */
	public StudyContainer(String id, 
							WebMarkupContainer studyNameMarkup,
							WebMarkupContainer studyLogoMarkup,
							WebMarkupContainer arkContextMarkup) {
		
		super(id, true);
		cpModel = new CompoundPropertyModel<StudyModelVO>(new StudyModelVO());
		//Create the form that will hold the other controls
		containerForm = new Container("containerForm",cpModel);
		
		/* Initialise the study crud container vo that has all the WebMarkups */
		studyCrudContainerVO = new StudyCrudContainerVO();
		//Set the Markups that was passed in into the VO
		studyCrudContainerVO.setStudyNameMarkup(studyNameMarkup);
		studyCrudContainerVO.setStudyLogoMarkup(studyLogoMarkup);
		studyCrudContainerVO.setArkContextMarkup(arkContextMarkup);
		
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());		
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		//prerenderContextCheck();
		add(containerForm);
		
	}
	
	/**
	 * TODO: To be hooked when a solution is found for AjaxRequestTarget. We need this to render the subjectUidContainer fields.
	 * We do not have a reference to the Target instance here.
	 */
	protected void prerenderContextCheck() {	
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId != null){
			Study study = iArkCommonService.getStudy(sessionStudyId);
			String subjectUidExample = iArkCommonService.getSubjectUidExample(study);
			containerForm.getModelObject().setStudy(study);
			containerForm.getModelObject().setSubjectUidExample(subjectUidExample);
			
			DetailForm detailForm = (DetailForm) detailsPanel.get("detailForm");
			
			// All SubjectUID generator fields grouped within a container(s)
			WebMarkupContainer autoSubjectUidcontainer = detailForm.getAutoSubjectUidContainer();
			WebMarkupContainer subjectUidcontainer = detailForm.getSubjectUidContainer();
			
			// Disable all SubjectUID generation fields is subjects exist
			if(iArkCommonService.getSubjectCount(study) > 0)
			{
				autoSubjectUidcontainer.setEnabled(false);
				subjectUidcontainer.setEnabled(false);
				//target.addComponent(subjectUidcontainer);
			}
		
			// Example auto-generated SubjectUID
			Label subjectUidExampleLbl = detailForm.getSubjectUidExampleLbl();
			subjectUidExampleLbl.setDefaultModelObject(containerForm.getModelObject().getSubjectUidExample());
			//target.addComponent(subjectUidExampleLbl);
			
			List<ModuleVO> modules = new ArrayList<ModuleVO>();
			Collection<ModuleVO> modulesLinkedToStudy = new  ArrayList<ModuleVO>();
			try {
				modules = userService.getModules(true);//source this from a static list or on application startup 
				modulesLinkedToStudy  = userService.getModulesLinkedToStudy(study.getName(), true);
				containerForm.getModelObject().setModulesSelected(modulesLinkedToStudy);
				containerForm.getModelObject().setModulesAvailable(modules);
				
			} catch (ArkSystemException e) {
				//log the error message and notify sys admin to take appropriate action
				this.error("A system error has occured. Please try after some time.");
			}
			
			
			studyCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
			studyCrudContainerVO.getSearchPanelContainer().setVisible(false);
			studyCrudContainerVO.getDetailPanelContainer().setVisible(true);
			studyCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
			
			studyCrudContainerVO.getViewButtonContainer().setVisible(true);//saveBtn
			studyCrudContainerVO.getViewButtonContainer().setEnabled(true);//saveBtn
			
			studyCrudContainerVO.getEditButtonContainer().setVisible(false);
			studyCrudContainerVO.getSummaryContainer().setVisible(true);

		}
	}

	protected WebMarkupContainer initialiseSearchPanel(){
		
		searchStudyPanel = new Search("searchStudyPanel",studyCrudContainerVO,feedBackPanel,containerForm);
		searchStudyPanel.initialisePanel(cpModel);
		studyCrudContainerVO.getSearchPanelContainer().add(searchStudyPanel);							
		return studyCrudContainerVO.getSearchPanelContainer();
	}


	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		Study study = new Study();
		containerForm.getModelObject().setStudyList(iArkCommonService.getStudy(study));
		
		searchResultsPanel = new SearchResults("searchResults", studyCrudContainerVO, containerForm);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				studyCrudContainerVO.getPageableListView().removeAll();
				return iArkCommonService.getStudy(containerForm.getModelObject().getStudy()); 
			}
		};
		
		studyCrudContainerVO.setPageableListView(searchResultsPanel.buildPageableListView(iModel,studyCrudContainerVO.getSearchResultPanelContainer()));
		studyCrudContainerVO.getPageableListView().setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", studyCrudContainerVO.getPageableListView());
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(studyCrudContainerVO.getPageableListView());
		studyCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return studyCrudContainerVO.getSearchResultPanelContainer();
	}


	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new Details("detailsPanel",feedBackPanel,studyCrudContainerVO,containerForm);
		detailsPanel.initialisePanel();
		studyCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return studyCrudContainerVO.getDetailPanelContainer();
	}
	
	
}
