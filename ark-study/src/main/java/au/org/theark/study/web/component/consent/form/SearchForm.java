package au.org.theark.study.web.component.consent.form;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consent.ConsentContainerPanel;
import au.org.theark.study.web.component.phone.DetailPanel;

/**
 * @author Nivedan
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<ConsentVO>
{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	protected IStudyService studyService;

	protected DetailPanel detailPanel;
	protected PageableListView<Consent> pageableListView;
	protected CompoundPropertyModel<ConsentVO> cpmModel;
	
	/**
	 * Form Components
	 */
	protected TextField<String> consentedBy;
	protected DatePicker<Date> consentedDatePicker;
	protected DropDownChoice<StudyComp> studyComponentChoice;
	protected DropDownChoice<StudyCompStatus> studyComponentStatusChoice;
	protected DropDownChoice<ConsentStatus> consentStatusChoice;
	protected DropDownChoice<ConsentType> consentTypeChoice;
	
	
	
	/**
	 * @param id
	 */
	public SearchForm(String id,
						CompoundPropertyModel<ConsentVO> model, 
						PageableListView<Consent> listView, 
						FeedbackPanel feedBackPanel, 
						WebMarkupContainer listContainer,
						WebMarkupContainer searchMarkupContainer, 
						WebMarkupContainer detailContainer, 
						WebMarkupContainer detailPanelFormContainer, 
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.pageableListView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchButtons(sessionPersonId, "There is no subject or contact in context. Please select a Subject or Contact.");
	}

	protected void initialiseSearchForm(){
		consentedBy = new TextField<String>(Constants.CONSENT_CONSENTED_BY);
		consentedDatePicker = new DatePicker<Date>(Constants.CONSENT_CONSENT_DATE);
		initialiseConsentTypeChoice();
		initialiseConsentStatusChoice();
		initialiseComponentChoice();
		initialiseComponentStatusChoice();
	}
	
	/**
	 * Initialise the Consent Type Drop Down Choice Control
	 */
	protected void initialiseConsentTypeChoice(){
		List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		consentTypeChoice = new DropDownChoice(Constants.CONSENT_CONSENT_TYPE,consentTypeList,defaultChoiceRenderer);
	}
	
	/**
	 * Initialise the Consent Status Drop Down Choice Control
	 */
	protected void initialiseConsentStatusChoice(){
		List<ConsentStatus> consentStatusList = iArkCommonService.getConsentStatus();
		ChoiceRenderer<ConsentType> defaultChoiceRenderer = new ChoiceRenderer<ConsentType>(Constants.NAME, Constants.ID);
		consentStatusChoice  = new DropDownChoice(Constants.CONSENT_CONSENT_STATUS, consentStatusList,defaultChoiceRenderer);
	}
	
	/**
	 * Initialise the Consent StudyComp Drop Down Choice Control
	 */
	protected void initialiseComponentChoice(){
		
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponent();
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		studyComponentChoice  = new DropDownChoice(Constants.CONSENT_STUDY_COMP, studyCompList,defaultChoiceRenderer);
	
	}
	
	protected void initialiseComponentStatusChoice(){
		
		List<StudyCompStatus> studyCompList = iArkCommonService.getStudyComponentStatus();
		ChoiceRenderer<StudyCompStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyCompStatus>(Constants.NAME, Constants.ID);
		studyComponentStatusChoice  = new DropDownChoice(Constants.CONSENT_STUDY_COMP_STATUS, studyCompList,defaultChoiceRenderer);
	
	}

	protected void addSearchComponentsToForm(){
		add(consentTypeChoice);
		add(consentStatusChoice);
		add(studyComponentChoice);
		add(studyComponentStatusChoice);
		add(consentedDatePicker);
		add(consentedBy);
	}
	
	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
//		target.addComponent(feedbackPanel);
//		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
//		String sessionPersonType = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);//Subject or Contact: Denotes if it was a subject or contact placed in session
//		try{
//			
//			Phone phone = getModelObject().getPhone();
//			phone.setPerson(studyService.getPerson(sessionPersonId));
//
////			if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)){
////				
////			}else if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT)){
////				
////			}
//			Collection<Phone> phones = studyService.getPersonPhoneList(sessionPersonId, getModelObject().getPhone());
//			if (phones != null && phones.size() == 0)
//			{
//				this.info("Fields with the specified criteria does not exist in the system.");
//				target.addComponent(feedbackPanel);
//			}
//			
//			getModelObject().setPhoneList(phones);
//			pageableListView.removeAll();
//			listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
//			target.addComponent(listContainer);
//
//			
//		}catch(EntityNotFoundException entityNotFoundException){
//			this.warn("There are no phone items available for the specified criteria.");
//			target.addComponent(feedbackPanel);
//			
//		}catch(ArkSystemException arkException){
//			this.error("The Ark Application has encountered a system error.");
//			target.addComponent(feedbackPanel);
//		}
		
	}
	
	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		setModelObject(new ConsentVO());
		preProcessDetailPanel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#isSecure(java.lang.String)
	 */
	@Override
	protected boolean isSecure(String actionType) {
		// TODO Auto-generated method stub
		return true;
	}
}