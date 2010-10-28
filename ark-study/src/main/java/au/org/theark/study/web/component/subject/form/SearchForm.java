/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import java.util.Collection;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.entity.GenderType;
import au.org.theark.study.model.entity.Person;
import au.org.theark.study.model.entity.SubjectStatus;
import au.org.theark.study.model.entity.VitalStatus;
import au.org.theark.study.model.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class SearchForm extends Form<SubjectVO>{


	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private TextField<String> subjectIdTxtFld;
	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	
	
	
	private AjaxButton searchButton;
	private AjaxButton newButton;
	private Button resetButton;
	
	private CompoundPropertyModel<SubjectVO> cpmModel;
	
	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<SubjectVO> model) {
		super(id);
		this.cpmModel = model; 
		// TODO Auto-generated constructor stub
		
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
				onSearch(target);
			}
			
		};
		
		resetButton = new Button(Constants.RESET){
			public void onSubmit(){
				onReset();
			}
		};
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId == null){
			newButton.setEnabled(false);
			searchButton.setEnabled(false);
			this.error("There is no study in context. You can only search or manage subjects based on a study.");
		}
		
		initVitalStatusDdc();
		initSubjectStatusDdc();
		initGenderTypeDdc();
		
	}
	
	private void initVitalStatusDdc(){
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<Person> personPm = new PropertyModel<Person>(subjectCpm,Constants.PERSON);
		
		PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm,Constants.VITAL_STATUS);
		Collection<VitalStatus> vitalStatusList = studyService.getVitalStatus();
		ChoiceRenderer vitalStatusRenderer = new ChoiceRenderer(Constants.STATUS_NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.VITAL_STATUS,vitalStatusPm,(List)vitalStatusList,vitalStatusRenderer);
	}
	
	private void initSubjectStatusDdc(){
		
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<SubjectStatus> subjectStatusPm = new PropertyModel<SubjectStatus>(subjectCpm,Constants.SUBJECT_STATUS);
		Collection<SubjectStatus> subjectStatusList = studyService.getSubjectStatus();
		ChoiceRenderer subjectStatusRenderer = new ChoiceRenderer(Constants.NAME,Constants.SUBJECT_STATUS_KEY);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,subjectStatusPm,(List)subjectStatusList,subjectStatusRenderer);
	}
	
	private void initGenderTypeDdc(){
		
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<Person> personPm = new PropertyModel<Person>(subjectCpm,Constants.PERSON);
		PropertyModel<GenderType> genderTypePm = new PropertyModel<GenderType>(personPm,Constants.GENDER_TYPE);
		Collection<GenderType> genderTypeList = studyService.getGenderType(); 
		ChoiceRenderer genderTypeRenderer = new ChoiceRenderer(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.GENDER_TYPE,genderTypePm, (List)genderTypeList,genderTypeRenderer);
	}
	
	protected void onNew(AjaxRequestTarget target){}

	protected void onSearch(AjaxRequestTarget target){}
	
	protected void onReset(){
		clearInput();
		updateFormComponentModels();
		
	}
	
	private void addComponents(){
		
		add(subjectIdTxtFld);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(vitalStatusDdc);
		add(subjectStatusDdc);
		add(genderTypeDdc);
		add(newButton);
		add(searchButton);
		add(resetButton.setDefaultFormProcessing(false));
		
	}
	public void initialiseForm(){
		subjectIdTxtFld = new TextField<String>(Constants.PERSON_KEY);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		
		addComponents();
	}

}
