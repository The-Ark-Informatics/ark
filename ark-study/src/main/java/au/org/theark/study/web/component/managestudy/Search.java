package au.org.theark.study.web.component.managestudy;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.model.vo.StudyModel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.SearchForm;

public class Search extends Panel{

	/* Search Results returned based on the search criteria */
	
	private Container containerForm;
	
	private List<Study> resultList;
	private List<StudyStatus> studyStatusList;
	private FeedbackPanel fbPanel;
	PageableListView<Study> pageListView;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer searchWebMarkupContainer;
	private WebMarkupContainer detailsWebMarkupContainer;
	private WebMarkupContainer saveArchivebuttonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer detailFormContainer;
	private Details details;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	public List<Study> getResultList() {
		return resultList;
	}
	
	public void setResultList(List<Study> resultList) {
		this.resultList = resultList;
	}
	
	/* Constructor for Search */
	public Search(	String id,
					FeedbackPanel feedbackpanel,
					List<StudyStatus> studyStatusList,
					WebMarkupContainer searchMarkupContainer,
					PageableListView<Study> pageableListView,
					WebMarkupContainer resultListContainer,
					WebMarkupContainer detailsContainer, 
					Details detailsPanel,
					WebMarkupContainer saveArchBtnContainer,
					WebMarkupContainer editBtnContainer,
					WebMarkupContainer detailFormCompContainer,
					Container containerForm
					) {
		
		super(id);
		this.studyStatusList = studyStatusList;
		pageListView = pageableListView;
		listContainer = resultListContainer;
		searchWebMarkupContainer = searchMarkupContainer;
		detailsWebMarkupContainer = detailsContainer;
		details = detailsPanel;
		saveArchivebuttonContainer = saveArchBtnContainer;
		editButtonContainer = editBtnContainer;
		detailFormContainer = detailFormCompContainer;
		fbPanel = feedbackpanel;
		this.containerForm = containerForm;
	}
	
	public void initialisePanel(){
		
		SearchForm sform = new SearchForm("searchForm",(CompoundPropertyModel<StudyModel>)containerForm.getModel(), studyStatusList){
			
			/*Event handler for user's search request*/
			protected  void onSearch(AjaxRequestTarget target){

				target.addComponent(fbPanel);
				
				resultList = studyService.getStudy(containerForm.getModelObject().getStudy());
				if(resultList != null && resultList.size() == 0){
					containerForm.getModelObject().setStudyList(resultList);
					this.info("There are no records that matched your query. Please modify your filter");
					target.addComponent(fbPanel);
				}

				containerForm.getModelObject().setStudyList(resultList);
				pageListView.removeAll();
				listContainer.setVisible(true);
				target.addComponent(listContainer);
								
			}
			
			protected  void onNew(AjaxRequestTarget target){
				
				containerForm.setModelObject(new StudyModel());

				List<ModuleVO> modules = new ArrayList<ModuleVO>();
				
				try {
					modules = userService.getModules(true);//source this from a static list or on application startup 
				} catch (ArkSystemException e) {
					//log the error message and notify sys admin to take appropriate action
					this.error("A system error has occured. Please try after some time.");
				}
				
				containerForm.getModelObject().setModulesAvailable(modules);
				//If the selected side has items then its re-using the first object
				processDetail(target, Constants.MODE_NEW);
			}
			
		};
		
		//Add the SearchForm 
		add(sform);
	}
	
	public void processSearch(){
		
	}
	
	public void processDetail(AjaxRequestTarget target, int mode){
		if(mode == Constants.MODE_NEW){
			//save archive container must be visible and edit must be disabled
		}
		details.getStudyForm().getStudyNameTxtFld().setEnabled(true);
		detailsWebMarkupContainer.setVisible(true);
		listContainer.setVisible(false);
		saveArchivebuttonContainer.setVisible(true);
		editButtonContainer.setVisible(false);
		searchWebMarkupContainer.setVisible(false);
		detailFormContainer.setEnabled(true);
		target.addComponent(detailsWebMarkupContainer);
		target.addComponent(listContainer);
		target.addComponent(searchWebMarkupContainer);
		target.addComponent(saveArchivebuttonContainer);
		target.addComponent(editButtonContainer);
		target.addComponent(detailFormContainer);
	}

}
