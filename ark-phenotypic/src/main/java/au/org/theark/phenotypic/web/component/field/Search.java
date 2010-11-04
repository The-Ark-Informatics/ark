/**
 * 
 */
package au.org.theark.phenotypic.web.component.field;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.Constants;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.web.component.field.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class Search extends Panel
{
	private FeedbackPanel				feedBackPanel;
	private WebMarkupContainer			searchMarkupContainer;
	private WebMarkupContainer			listContainer;
	private WebMarkupContainer			detailsContainer;
	private PageableListView<Field>	listView;

	/* Constructor */
	public Search(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer, PageableListView<Field> listView, WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer)
	{
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		listContainer = resultListContainer;
	}
	
	
	public void initialisePanel(CompoundPropertyModel<FieldVO> fieldCpm){
		
		//Get the study id from the session and get the study
		//Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, fieldCpm){
			
			protected  void onSearch(AjaxRequestTarget target){
				
				//Refresh the FB panel if there was an old message from previous search result
				target.addComponent(feedBackPanel);
				
				//Get a list of Fields with the given criteria
				//try{
					
					List<Field> resultList = new ArrayList<Field>(); 
						//studyService.searchStudyComp(containerForm.getModelObject().getStudyComponent());
					
					if(resultList != null && resultList.size() == 0){
						this.info("Fields with the specified criteria does not exist in the system.");
						target.addComponent(feedBackPanel);
					}
					//containerForm.getModelObject().setStudyCompList(resultList);
					listView.removeAll();
					listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
					target.addComponent(listContainer);//For ajax this is required so 
					
					//processDetail(target);
				//}catch(ArkSystemException arkEx){
				//	this.error("A system error has occured. Please try after sometime.");
				//}
				
			}
			
			protected void onNew(AjaxRequestTarget target){
				/*// Show the details panel name and description
				StudyCompVo studyCompVo = new StudyCompVo();
				studyCompVo.setMode(Constants.MODE_NEW);
				containerForm.setModelObject(studyCompVo);
				processDetail(target);*/
			}
		};
		add(searchForm);
	}

}
