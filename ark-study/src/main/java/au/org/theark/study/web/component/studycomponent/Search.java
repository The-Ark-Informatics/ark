package au.org.theark.study.web.component.studycomponent;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;
import au.org.theark.study.web.component.studycomponent.form.SearchForm;

public class Search extends Panel{

	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer searchMarkupContainer;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private Details detailPanel;
	private ContainerForm containerForm;
	private PageableListView<StudyComp> listView;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	

	
	
	/*Constructor*/
	public Search(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer,
					PageableListView<StudyComp> listView,  
					WebMarkupContainer resultListContainer, 
					WebMarkupContainer detailPanelContainer,
					Details detailPanel,
					ContainerForm studyCompContainerForm) {
		
		super(id);
		this.searchMarkupContainer =  searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		listContainer = resultListContainer;
		detailsContainer = detailPanelContainer;
		this.detailPanel = detailPanel;
		containerForm = studyCompContainerForm;
	}
	
	
	
	public void initialisePanel(CompoundPropertyModel<StudyCompVo> studyCompCpm){
		
		
		SearchForm searchStudyCompForm = new SearchForm(Constants.SEARCH_FORM, studyCompCpm){
			
			protected  void onSearch(AjaxRequestTarget target){
				
				//Refresh the FB panel if there was an old message from previous search result
				target.addComponent(feedBackPanel);
				
				//Get a list of StudyComponents with the given criteria
				try{
					
					List<StudyComp> resultList = studyService.searchStudyComp(containerForm.getModelObject().getStudyComponent());
					
					if(resultList != null && resultList.size() == 0){
						this.info("Study Component with the specified criteria does not exist in the system.");
						target.addComponent(feedBackPanel);
					}
					containerForm.getModelObject().setStudyCompList(resultList);
					listView.removeAll();
					listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
					target.addComponent(listContainer);//For ajax this is required so 
				}catch(ArkSystemException arkEx){
					this.error("A system error has occured. Please try after sometime.");
				}
				
			}
			
			protected void onNew(AjaxRequestTarget target){
				// Show the details panel name and description
				StudyCompVo studyCompVo = new StudyCompVo();
				studyCompVo.setMode(Constants.MODE_NEW);
				containerForm.setModelObject(studyCompVo);
				processDetail(target);
			}
		};
		add(searchStudyCompForm);
	}
	
	public void processDetail(AjaxRequestTarget target){
		//Hide the Search Panel before the Details Panel is made visible via the detailsContainer
		searchMarkupContainer.setVisible(false);
		//Hide the Search Result List 
		listContainer.setVisible(false);
		//Un-hide the Details Panel visible 
		detailsContainer.setVisible(true);
		detailPanel.getDetailsForm().getComponentIdTxtFld().setEnabled(false);
		//Attach the containers that need to be re-painted
		target.addComponent(searchMarkupContainer);
		target.addComponent(detailsContainer);
		target.addComponent(listContainer);
	}

}
