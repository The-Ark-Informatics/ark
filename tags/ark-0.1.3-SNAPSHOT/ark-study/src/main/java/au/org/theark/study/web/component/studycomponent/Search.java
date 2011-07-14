package au.org.theark.study.web.component.studycomponent;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.studycomponent.form.ContainerForm;
import au.org.theark.study.web.component.studycomponent.form.SearchForm;

public class Search extends Panel{

	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer searchMarkupContainer;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer detailFormContainer;
	private PageableListView<StudyComp> listView;
	
	
	/*Constructor*/
	public Search(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer,
					PageableListView<StudyComp> listView,  
					WebMarkupContainer resultListContainer, 
					WebMarkupContainer detailPanelContainer,
					WebMarkupContainer detailFormContainer,
					WebMarkupContainer viewButtonContainer,
					WebMarkupContainer editButtonContainer,	
					Details detailPanel,
					ContainerForm studyCompContainerForm) {
		
		super(id);
		this.searchMarkupContainer =  searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.listContainer = resultListContainer;
		this.detailsContainer = detailPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailFormContainer = detailFormContainer;
	}
	
	
	
	public void initialisePanel(CompoundPropertyModel<StudyCompVo> studyCompCpm){
		
		
		SearchForm searchStudyCompForm = new SearchForm(Constants.SEARCH_FORM, 
														studyCompCpm, 
														listView,
														feedBackPanel,
														listContainer,
														searchMarkupContainer,
														detailsContainer,
														detailFormContainer,
														viewButtonContainer,
														editButtonContainer);
		add(searchStudyCompForm);
		
	}

}
