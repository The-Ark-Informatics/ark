package au.org.theark.study.web.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.study.Details;
import au.org.theark.study.web.component.study.StudyModel;

public class SearchStudyForm extends Form<Study>{
	
	TextField<String> studyIdTxtFld =new TextField<String>(Constants.STUDY_SEARCH_KEY);
	TextField<String> studyNameTxtFld = new TextField<String>(Constants.STUDY_SEARCH_NAME);
	DatePicker<Date> dateOfApplicationDp = new DatePicker<Date>(Constants.STUDY_SEARCH_DOA);
	TextField<String> principalContactTxtFld = new TextField<String>(Constants.STUDY_SEARCH_CONTACT);
	DropDownChoice<StudyStatus> studyStatusDpChoices;
	Button searchButton;
	Button newButton;
	Button resetButton;
	List<StudyStatus>  studyStatusList;
	AjaxButton refresh;
	/**
	 * Constructor
	 * @param id
	 * @param study
	 * @param panelId
	 */
	public SearchStudyForm(String id, Study study, String panelId, List<StudyStatus>  studyStatusList, final Details detailsPanel ){

		super(id, new CompoundPropertyModel<Study>(study));
		this.studyStatusList = studyStatusList;
		

		refresh = new AjaxButton("refresh") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onNew(new Study());
				target.addComponent(detailsPanel);
			}
		};
		
		
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
				
				onNew(new Study());
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
	
	protected void onNew(Study study){
		clearInput();
		updateFormComponentModels();
	}
	
	protected void onReset(){}

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
		
		add(refresh);
	}
	
	
	@SuppressWarnings("unchecked")
	private void initStudyStatusDropDown(Study study){
		
		//List<StudyStatus>  studyStatusList = studyService.getListOfStudyStatus();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		PropertyModel propertyModel = new PropertyModel(study,Constants.STUDY_STATUS);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_DROP_DOWN_CHOICE,propertyModel,studyStatusList,defaultChoiceRenderer);
	}

}
