package au.org.theark.study.web.component.managestudy;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
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
import au.org.theark.study.web.component.managestudy.form.SearchForm;

import au.org.theark.study.web.form.ModuleVo;

public class Search extends Panel{

	/* Search Results returned based on the search criteria */
	private CompoundPropertyModel<StudyModel> cpm;
	
	private List<Study> resultList;
	private List<StudyStatus> studyStatusList;
	
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
					List<StudyStatus> studyStatusList, 
					CompoundPropertyModel<StudyModel> cpm, 
					PageableListView<Study> pageableListView, 
					WebMarkupContainer resultListContainer,
					WebMarkupContainer searchMarkupContainer, 
					WebMarkupContainer detailsContainer, 
					Details detailsPanel,
					WebMarkupContainer saveArchBtnContainer,
					WebMarkupContainer editBtnContainer,
					WebMarkupContainer detailFormCompContainer) {
		
		super(id);
		this.studyStatusList = studyStatusList;
		this.cpm = cpm;
		pageListView = pageableListView;
		listContainer = resultListContainer;
		searchWebMarkupContainer = searchMarkupContainer;
		detailsWebMarkupContainer = detailsContainer;
		details = detailsPanel;
		saveArchivebuttonContainer = saveArchBtnContainer;
		editButtonContainer = editBtnContainer;
		detailFormContainer = detailFormCompContainer;
	}
	
	public void initialisePanel(){
		
		SearchForm sform = new SearchForm("searchForm",cpm, studyStatusList){
			
			/*Event handler for user's search request*/
			protected  void onSearch(AjaxRequestTarget target){
				
				resultList = studyService.getStudy(cpm.getObject().getStudy());
				cpm.getObject().setStudyList(resultList);//Place the results into the model
				pageListView.removeAll();
				listContainer.setVisible(true);
				searchWebMarkupContainer.setVisible(false);
				target.addComponent(listContainer);
			}
			
			protected  void onNew(AjaxRequestTarget target){
				cpm.setObject(new StudyModel());
				List<ModuleVO> modules;
				List<ModuleVo> moduleVoList = new ArrayList<ModuleVo>();
				try {
					modules = userService.getModules(true);//source this from a static list or on application startup 
					for (ModuleVO moduleVO : modules) {
						ModuleVo moduleVo = new ModuleVo();
						moduleVo.setModuleName(moduleVO.getModule());
						moduleVoList.add(moduleVo);
					}
				} catch (ArkSystemException e) {
					//log the error message and notify sys admin to take appropriate action
					this.error("A system error has occured. Please try after some time.");
				}
				cpm.getObject().setModulesAvailable(moduleVoList);
				details.setCpm(cpm);
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
