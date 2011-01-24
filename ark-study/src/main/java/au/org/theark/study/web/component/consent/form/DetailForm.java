/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.consent.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class DetailForm  extends AbstractDetailForm<ConsentVO>{

	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	protected IStudyService studyService;
	
	/**
	 * Form Components
	 */
	protected TextField<String> consentedBy;
	protected DatePicker<Date> consentedDatePicker;
	protected DropDownChoice<StudyComp> studyComponentChoice;
	protected DropDownChoice<StudyCompStatus> studyComponentStatusChoice;
	protected DropDownChoice<ConsentStatus> consentStatusChoice;
	protected DropDownChoice<ConsentType> consentTypeChoice;
	protected TextArea<String> commentTxtArea;
	
	
	/**
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param detailPanelContainer
	 * @param detailPanelFormContainer
	 * @param searchPanelContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel,
			WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			Form<ConsentVO> containerForm) {
		super(id, feedBackPanel, resultListContainer, detailPanelContainer,
				detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, containerForm);
		
	}
	
	public void initialiseDetailForm(){
		consentedBy = new TextField<String>(Constants.CONSENT_CONSENTED_BY);
		consentedDatePicker = new DatePicker<Date>(Constants.CONSENT_CONSENT_DATE);
		commentTxtArea = new TextArea<String>(Constants.CONSENT_CONSENT_COMMENT);
		initialiseConsentTypeChoice();
		initialiseConsentStatusChoice();
		initialiseComponentChoice();
		initialiseComponentStatusChoice();
		addDetailFormComponents();
		attachValidators();
	}
	
	public void addDetailFormComponents(){
		detailPanelFormContainer.add(consentedBy);
		detailPanelFormContainer.add(consentedDatePicker);
		detailPanelFormContainer.add(studyComponentChoice);
		detailPanelFormContainer.add(studyComponentStatusChoice);
		detailPanelFormContainer.add(consentStatusChoice);
		detailPanelFormContainer.add(consentTypeChoice);
		detailPanelFormContainer.add(commentTxtArea);
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


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
	
		consentedBy.setRequired(true);
		studyComponentChoice.setRequired(true);
		consentStatusChoice.setRequired(true);
		consentTypeChoice.setRequired(true);
		consentedDatePicker.setRequired(true);
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		ConsentVO consentVO = new ConsentVO();
		containerForm.setModelObject(consentVO);
		onCancelPostProcess(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		
		try {
			studyService.delete(containerForm.getModelObject().getConsent());
		
		}catch(EntityNotFoundException entityNotFoundException){
			this.error("The consent you tried to delete does not exist");
		}
		catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			//Report this to the user
			this.error("A system exception has occured during delete operation of the Consent");
		}
		
		selectModalWindow.close(target);
		ConsentVO consentVO = new ConsentVO();
		containerForm.setModelObject(consentVO);
		onCancel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<ConsentVO> containerForm,	AjaxRequestTarget target) {
		Long personSessionId =(Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		//save new
		try {
			
			//Subject in Context
			Person subject = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getConsent().setSubject(subject);
			//Study in Context
			Long studyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study =	iArkCommonService.getStudy(studyId);
			containerForm.getModelObject().getConsent().setStudy(study);
			
			if(containerForm.getModelObject().getConsent().getId() == null){
				
				studyService.create(containerForm.getModelObject().getConsent());
				this.info("Consent was successfuly created for the Subject ");
				processErrors(target);
			}else{
				studyService.update(containerForm.getModelObject().getConsent());
				this.info("Consent was successfuly updated for the Subject ");
				processErrors(target);
			}
			
		} catch (EntityNotFoundException e) {
			this.error("The Consent record you tried to update is no longer available in the system");
			processErrors(target);
		} catch (ArkSystemException e) {
			this.error(e.getMessage());
			processErrors(target);
		}finally{
			onSavePostProcess(target);
		}
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}

}
