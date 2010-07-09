package au.org.theark.study.web.component.study;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.StudyForm;

@SuppressWarnings("serial")
public class Search extends Panel {
	
	private SearchResultList searchResults;
	private Details detailsPanel;
	private FeedbackPanel feedBackPanel = new FeedbackPanel("feedbackMessage");
	public SearchResultList getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(SearchResultList searchResults) {
		this.searchResults = searchResults;
	}
	
	public void setDetailsPanelVisible(boolean isVisible){
		detailsPanel.setVisible(isVisible);
	}
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	
	public Search(String id) {
		
		super(id);
		//Create a new instance of the details panel
		detailsPanel = new Details("detailsPanel", new Study(), this);
		//Hide it since we have not looked up as yet
		setDetailsPanelVisible(false);
		
		// Uses an entirely new VO for the search so each time the search panel is loaded. The values provided will be refreshed.
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, new Study(), id){
			/*When user has clicked on the Search Button*/
			protected  void onSearch(Study study){
				setDetailsPanelVisible(false);//Set the User Details panel to false/hide it
				List<Study> resultList = studyService.getStudy(study);
				if(resultList != null && resultList.size() == 0){
					this.info("Study with the specified criteria does not exist in the system.");	
				}
				
				//Render the Search Results
				//TODO NN Removing the panel is not efficient, I need to use another technique (Ajax)
				remove(searchResults);//Since we already have the panel.We need to partially update the list the panel uses  rather than action it at the panel level
				searchResults = new SearchResultList("searchResults", resultList,detailsPanel);
				add(searchResults);
			}
		};

		//Add the Form to the Panel. The Form object that will contain the child or UI components that will be part of the search or be affected by the search.
		add(searchForm);
		add(feedBackPanel); //Add feedback panel
		searchForm.add(detailsPanel);
		//Just an empty list
		List<Study> studyList = new ArrayList<Study>();
		Study study = new Study();
		studyList.add(study);
		searchResults = new SearchResultList("searchResults",studyList,detailsPanel);
		searchForm.add(searchResults);
	
	}
	
	public class SearchForm extends Form<Study>{

		TextField<String> studyIdTxtFld =new TextField<String>(Constants.STUDY_KEY);
		TextField<String> studyNameTxtFld = new TextField<String>(Constants.STUDY_NAME);
		DatePicker<Date> dateOfApplicationDp = new DatePicker<Date>("dateOfApplication");
		TextField<String> principalContactTxtFld = new TextField<String>(Constants.STUDY_CONTACT);
		DropDownChoice<StudyStatus> studyStatusDpChoices;
		Button searchButton;
		Button newButton;
		Button resetButton;
		
		private void decorateComponents(){
			ThemeUiHelper.componentRounded(studyNameTxtFld);
			ThemeUiHelper.componentRounded(studyIdTxtFld);
			ThemeUiHelper.componentRounded(dateOfApplicationDp);
			ThemeUiHelper.componentRounded(principalContactTxtFld);
			ThemeUiHelper.buttonRoundedFocused(searchButton);
			ThemeUiHelper.buttonRounded(newButton);
			ThemeUiHelper.buttonRounded(resetButton);
			ThemeUiHelper.componentRounded(studyStatusDpChoices);
			
		}
		
		private void addComponentsToForm(){
			add(studyIdTxtFld);
			add(studyNameTxtFld);
			add(dateOfApplicationDp);
			add(principalContactTxtFld);
			add(studyStatusDpChoices);
			add(searchButton);
			add(newButton);
			add(resetButton.setDefaultFormProcessing(false));
		}
		
		
		@SuppressWarnings("unchecked")
		private void initStudyStatusDropDown(Study study){
			
			List<StudyStatus>  studyStatusList = studyService.getListOfStudyStatus();
			ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer("name", "studyStatusKey");
			PropertyModel propertyModel = new PropertyModel(study, "studyStatus");
			studyStatusDpChoices = new DropDownChoice("studyChoice",propertyModel,studyStatusList,defaultChoiceRenderer);
		}
		
		public SearchForm(String id, Study study, String panelId){

			super(id, new CompoundPropertyModel<Study>(study));

			
			searchButton  = new Button(Constants.SEARCH, new StringResourceModel("page.search", this, null))
			{
				public void onSubmit()
				{
					
					onSearch((Study) getForm().getModelObject());
				}
			};

			newButton =  new Button(Constants.NEW, new StringResourceModel("page.new", this, null))
			{
				public void onSubmit()
				{
					
					onNew();
				}
				@Override
				public boolean isVisible(){
					
					SecurityManager securityManager =  ThreadContext.getSecurityManager();
					Subject currentUser = SecurityUtils.getSubject();		
					boolean flag = false;
					if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
							securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
						flag = true;
					}
					//if it is a Super or Study admin then make the new available
					return flag;
				}
			};
			
			resetButton = new Button("reset", new StringResourceModel("page.form.reset.button", this, null) ){
				public void onSubmit(){
					clearInput();
					updateFormComponentModels();
				}
			};
			
			initStudyStatusDropDown(study);
			decorateComponents();
			addComponentsToForm();
		}
		
		protected void onSearch(Study Study){
		}
		
		protected void onNew(){
			StudyForm form = detailsPanel.getStudyForm();
			Study study = (Study)form.getModelObject();
			
			if(study != null  && study.getStudyKey() != null && study.getStudyKey().longValue() > 0){
				study = null;
				study = new Study();
				form.setModelObject(study);
				form.clearInput();
			}
			
			//StudyForm studyForm = new StudyForm("studyForm", new Study());
			form.getStudyIdTxtFld().setEnabled(false);
			detailsPanel.setStudyForm(form);
			detailsPanel.setVisible(true);
			searchResults.setVisible(false);
		}
		
		protected void onReset(){}
	}

}
