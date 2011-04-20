package au.org.theark.study.web.component.managestudy.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.Details;
import au.org.theark.study.web.component.managestudy.StudyCrudContainerVO;
import au.org.theark.study.web.component.managestudy.StudyHelper;

public class SearchForm extends AbstractSearchForm<StudyModelVO>{

	
	private static final long serialVersionUID = -5468677674413992897L;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	/* The Input Components that will be part of the Search Form */
	private TextField<String> studyIdTxtFld; 
	private TextField<String> studyNameTxtFld;
	private DateTextField dateOfApplicationDp;
	private TextField<String> principalContactTxtFld;
	private DropDownChoice<StudyStatus> studyStatusDpChoices;
	private List<StudyStatus>  studyStatusList;
	private CompoundPropertyModel<StudyModelVO> cpmModel;
	
	private StudyCrudContainerVO studyCrudContainerVO;
	private Container containerForm;
	private FeedbackPanel feedbackPanel;
	
	/**
	 * Constructor
	 * @param id
	 * @param model
	 * @param studyCrudContainerVO
	 * @param containerForm
	 */
	public SearchForm(	String id, 
						CompoundPropertyModel<StudyModelVO> studyModelVOCpm, 
						StudyCrudContainerVO studyCrudContainerVO,
						FeedbackPanel feedbackPanel, 
						Container containerForm){
		
		super(id,studyModelVOCpm,feedbackPanel,studyCrudContainerVO);
		
		this.containerForm = containerForm;
		this.studyCrudContainerVO = studyCrudContainerVO;
		this.feedbackPanel = feedbackPanel;
		setMultiPart(true);
		
		cpmModel = studyModelVOCpm;
		initialiseSearchForm();
		addSearchComponentsToForm();
	}
	
	protected void initialiseSearchForm(){
		studyIdTxtFld =new TextField<String>(Constants.STUDY_SEARCH_KEY);
		
		studyNameTxtFld = new TextField<String>(Constants.STUDY_SEARCH_NAME);
		dateOfApplicationDp = new DateTextField(Constants.STUDY_SEARCH_DOA, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateOfApplicationDp);
		dateOfApplicationDp.add(datePicker);
		
		principalContactTxtFld = new TextField<String>(Constants.STUDY_SEARCH_CONTACT);
		this.studyStatusList = iArkCommonService.getListOfStudyStatus();
		
		CompoundPropertyModel<StudyModelVO> studyCmpModel = (CompoundPropertyModel<StudyModelVO>)cpmModel;
		//Create a propertyModel to bind the components of this form, the root which is StudyContainer
		PropertyModel<Study> pm = new PropertyModel<Study>(studyCmpModel,"study");
		//Another PropertyModel for rendering the DropDowns and pass in the Property Model instance of type Study
		PropertyModel<StudyStatus> pmStudyStatus = new PropertyModel<StudyStatus>(pm,"studyStatus");
		initStudyStatusDropDown(pmStudyStatus);
	}
	

	protected void onSearch(AjaxRequestTarget target){
		List<Study> studyResultList  = iArkCommonService.getStudy(containerForm.getModelObject().getStudy());
		if(studyResultList != null && studyResultList.size() == 0){
			containerForm.getModelObject().setStudyList(studyResultList);
			this.info("There are no records that matched your query. Please modify your filter");
			target.addComponent(feedbackPanel);
		}

		containerForm.getModelObject().setStudyList(studyResultList);
		studyCrudContainerVO.getPageableListView().removeAll();
		studyCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.addComponent(studyCrudContainerVO.getSearchResultPanelContainer());
	}

	private void addSearchComponentsToForm(){
		add(studyIdTxtFld);
		add(studyNameTxtFld);
		add(dateOfApplicationDp);
		add(principalContactTxtFld);
		add(studyStatusDpChoices);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initStudyStatusDropDown(PropertyModel<StudyStatus> pmStudyStatus){
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_DROP_DOWN_CHOICE,pmStudyStatus,studyStatusList,defaultChoiceRenderer);
	}

	@Override
	protected boolean isSecure(String actionType) {
		boolean flag = false;
		if(actionType.equalsIgnoreCase(Constants.NEW)){
			SecurityManager securityManager =  ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();		
			
			if(		
					securityManager.hasRole(currentUser.getPrincipals(),RoleConstants.ARK_ROLE_ADMINISTATOR)||
					securityManager.hasRole(currentUser.getPrincipals(),RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN) ||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
				flag = true;
			}else{
				flag=false;
			}	
		}else if(actionType.equalsIgnoreCase(Constants.SEARCH)){
			flag= true;
		}else{
			
			flag=true;
		}
		
		//if it is a Super or Study admin then make the new available
		return flag;
	}
	

	
	protected void onNew(AjaxRequestTarget target){
		
		containerForm.setModelObject(new StudyModelVO());

		List<ModuleVO> modules = new ArrayList<ModuleVO>();
		
		try {
			modules = userService.getModules(true);//source this from a static list or on application startup 
			containerForm.getModelObject().setModulesAvailable(modules);
			
			// Hide Summary details on new
			studyCrudContainerVO.getSummaryContainer().setVisible(false);
			target.addComponent(studyCrudContainerVO.getSummaryContainer());
			
			// Show upload item for new Study
			studyCrudContainerVO.getStudyLogoMarkup().setVisible(true);
			studyCrudContainerVO.getStudyLogoImageContainer().setVisible(true);
			studyCrudContainerVO.getStudyLogoUploadContainer().setVisible(true);
			
			StudyHelper studyHelper = new StudyHelper();
			studyHelper.setStudyLogo(containerForm.getModelObject().getStudy(),target,studyCrudContainerVO.getStudyNameMarkup(), studyCrudContainerVO.getStudyLogoMarkup());
			studyHelper.setStudyLogoImage(containerForm.getModelObject().getStudy(), "study.studyLogoImage", studyCrudContainerVO.getStudyLogoImageContainer());
			
			target.addComponent(studyCrudContainerVO.getStudyLogoMarkup());
			target.addComponent(studyCrudContainerVO.getStudyLogoUploadContainer());
			target.addComponent(studyCrudContainerVO.getStudyLogoImageContainer());
			
			// Clear context items
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.resetContextLabel(target, studyCrudContainerVO.getArkContextMarkup());
			studyNameTxtFld.setEnabled(true);
			
			// Default boolean selections
			containerForm.getModelObject().getStudy().setAutoGenerateSubjectUid(false);
			containerForm.getModelObject().getStudy().setAutoConsent(false);
					
			// Disable SubjectUID pattern fields by default for New study
			WebMarkupContainer wmc = (WebMarkupContainer) studyCrudContainerVO.getDetailPanelContainer();
			Details detailsPanel = (Details) wmc.get("detailsPanel");
			DetailForm detailForm = (DetailForm) detailsPanel.get("detailForm");
			WebMarkupContainer autoSubjectUidcontainer = detailForm.getAutoSubjectUidContainer();
			WebMarkupContainer subjectUidcontainer = detailForm.getSubjectUidContainer();
			
			// Example auto-generated SubjectUID to "AAA-0000000001" on new
			containerForm.getModelObject().setSubjectUidExample(Constants.SUBJECTUID_EXAMPLE);
			Label subjectUidExampleLbl = detailForm.getSubjectUidExampleLbl();
			subjectUidExampleLbl.setDefaultModelObject(containerForm.getModelObject().getSubjectUidExample());
			target.addComponent(subjectUidExampleLbl);
			
			autoSubjectUidcontainer.setEnabled(true);
			subjectUidcontainer.setEnabled(false);
			target.addComponent(subjectUidcontainer);
			
			preProcessDetailPanel(target,studyCrudContainerVO);
			
		} catch (ArkSystemException e) {
			//log the error message and notify sys admin to take appropriate action
			this.error("A system error has occured. Please try after some time.");
		}
	
	}
}