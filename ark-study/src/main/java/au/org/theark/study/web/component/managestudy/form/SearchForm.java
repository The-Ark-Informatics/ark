package au.org.theark.study.web.component.managestudy.form;

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
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.model.vo.StudyModel;
import au.org.theark.study.web.Constants;

public class SearchForm extends Form<StudyModel>{

	/* The Input Components that will be part of the Search Form */
	private TextField<String> studyIdTxtFld; 
	private TextField<String> studyNameTxtFld;
	private DatePicker<Date> dateOfApplicationDp;
	private TextField<String> principalContactTxtFld;
	private DropDownChoice<StudyStatus> studyStatusDpChoices;
	private AjaxButton searchButton;
	private AjaxButton newButton;
	private Button resetButton;
	private List<StudyStatus>  studyStatusList;
	private  CompoundPropertyModel<StudyModel> cpmModel;
	/* Constructor */
	public SearchForm(String id, CompoundPropertyModel<StudyModel> model, List<StudyStatus>  statusList) {
		
		super(id);
		cpmModel = model;
		studyIdTxtFld =new TextField<String>(Constants.STUDY_SEARCH_KEY);
		studyNameTxtFld = new TextField<String>(Constants.STUDY_SEARCH_NAME);
		dateOfApplicationDp = new DatePicker<Date>(Constants.STUDY_SEARCH_DOA);
		principalContactTxtFld = new TextField<String>(Constants.STUDY_SEARCH_CONTACT);
		this.studyStatusList = statusList;
		
		newButton = new AjaxButton(Constants.NEW){
		
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onNew(target);
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
		
		searchButton = new AjaxButton(Constants.SEARCH){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onSearch(target);
			}
		};
		
		resetButton = new Button(Constants.RESET){
			public void onSubmit(){
				onReset();
			}
		};
		
		
		CompoundPropertyModel<StudyModel> studyCmpModel = (CompoundPropertyModel<StudyModel>)cpmModel;
		//Create a propertyModel to bind the components of this form, the root which is StudyContainer
		PropertyModel<Study> pm = new PropertyModel<Study>(studyCmpModel,"study");
		//Another PropertyModel for rendering the DropDowns and pass in the Property Model instance of type Study
		PropertyModel<StudyStatus> pmStudyStatus = new PropertyModel<StudyStatus>(pm,"studyStatus");
		initStudyStatusDropDown(pmStudyStatus);
		decorateComponents();
		addComponentsToForm();

	}
	
	
	protected void onSearch(AjaxRequestTarget target){}
	
	protected void onNew(AjaxRequestTarget target){}
	
	// A non-ajax function
	protected void onReset(){
		clearInput();
		updateFormComponentModels();
		
	}

	private void decorateComponents(){
		ThemeUiHelper.componentRounded(studyNameTxtFld);
		ThemeUiHelper.componentRounded(studyIdTxtFld);
		ThemeUiHelper.componentRounded(dateOfApplicationDp);
		ThemeUiHelper.componentRounded(principalContactTxtFld);
		ThemeUiHelper.buttonRounded(searchButton);
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
	private void initStudyStatusDropDown(PropertyModel<StudyStatus> pmStudyStatus){
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_DROP_DOWN_CHOICE,pmStudyStatus,studyStatusList,defaultChoiceRenderer);
	}

}
