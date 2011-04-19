package au.org.theark.study.web.component.correspondence.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

public class DetailForm extends AbstractDetailForm<CorrespondenceVO> {

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private DropDownChoice<CorrespondenceStatusType> statusTypeChoice;
	private TextField<String> studyManagerTxtFld;
	private DateTextField dateFld;
	private TextField<String> timeTxtFld;
	private TextField<String> reasonTxtFld;
	private DropDownChoice<CorrespondenceModeType> modeTypeChoice;
	private DropDownChoice<CorrespondenceDirectionType> directionTypeChoice;
	private DropDownChoice<CorrespondenceOutcomeType> outcomeTypeChoice;
	private TextField<String> detailsTxtFld;
	private TextField<String> commentsTxtFld;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel,
			WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			Form<CorrespondenceVO> containerForm) {
		super(id, feedBackPanel, resultListContainer, detailPanelContainer,
				detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, containerForm);

	}

	
	public void initialiseDetailForm() {
		
		initialiseStatusTypeDropDown();
		studyManagerTxtFld = new TextField<String>("correspondence.studyManager");
		studyManagerTxtFld.add(new ArkDefaultFormFocusBehavior());
		// create new DateTextField and assign date format
		dateFld = new DateTextField("correspondence.date", au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateFld);
		dateFld.add(datePicker);
				
		timeTxtFld = new TextField<String>("correspondence.time");
		reasonTxtFld = new TextField<String>("correspondence.reason");
		initialiseModeTypeDropDown();
		initialiseDirectionTypeDropDown();
		initialiseOutcomeTypeDropDown();
		detailsTxtFld = new TextField<String>("correspondence.details");
		commentsTxtFld = new TextField<String>("correspondence.comments");
	}
	
	private void initialiseStatusTypeDropDown() {
		
		List<CorrespondenceStatusType> list = studyService.getCorrespondenceStatusTypes();
		ChoiceRenderer<CorrespondenceStatusType> defaultRenderer = new ChoiceRenderer<CorrespondenceStatusType>("name", "id");
		statusTypeChoice = new DropDownChoice<CorrespondenceStatusType>("correspondence.correspondenceStatusType", list, defaultRenderer); 
	}

	private void initialiseModeTypeDropDown() {
		
		List<CorrespondenceModeType> list = studyService.getCorrespondenceModeTypes();
		ChoiceRenderer<CorrespondenceModeType> defaultRenderer = new ChoiceRenderer<CorrespondenceModeType>("name", "id");
		modeTypeChoice = new DropDownChoice<CorrespondenceModeType>("correspondence.correspondenceModeType", list, defaultRenderer);
	}
	
	private void initialiseDirectionTypeDropDown() {
		
		List<CorrespondenceDirectionType> list = studyService.getCorrespondenceDirectionTypes();
		ChoiceRenderer<CorrespondenceDirectionType> defaultRenderer = new ChoiceRenderer<CorrespondenceDirectionType>("name", "id");
		directionTypeChoice = new DropDownChoice<CorrespondenceDirectionType>("correspondence.correspondenceDirectionType", list, defaultRenderer);
	}
	
	private void initialiseOutcomeTypeDropDown() {
		
		List<CorrespondenceOutcomeType> list = studyService.getCorrespondenceOutcomeTypes();
		ChoiceRenderer<CorrespondenceOutcomeType> defaultRenderer = new ChoiceRenderer<CorrespondenceOutcomeType>("name", "id");
		outcomeTypeChoice = new DropDownChoice<CorrespondenceOutcomeType>("correspondence.correspondenceOutcomeType", list, defaultRenderer);
	}
	
	public void addDetailFormComponents() {
		
		detailPanelFormContainer.add(statusTypeChoice);
		detailPanelFormContainer.add(studyManagerTxtFld);
		detailPanelFormContainer.add(dateFld);
		detailPanelFormContainer.add(timeTxtFld);
		detailPanelFormContainer.add(reasonTxtFld);
		detailPanelFormContainer.add(modeTypeChoice);
		detailPanelFormContainer.add(directionTypeChoice);
		detailPanelFormContainer.add(outcomeTypeChoice);
		detailPanelFormContainer.add(detailsTxtFld);
		detailPanelFormContainer.add(commentsTxtFld);
	}
	
	@Override
	protected void attachValidators() {
		
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		
		CorrespondenceVO correspondenceVO = new CorrespondenceVO();
		containerForm.setModelObject(correspondenceVO);
		onCancelPostProcess(target);
	}
	
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,
			String selection, ModalWindow selectModalWindow) {
		
		try {
			studyService.delete(containerForm.getModelObject().getCorrespondence());
		}catch(Exception ex) {
			this.error("An error occurred while processing the correspondence delete operation.");
			ex.printStackTrace();
		}
		
		selectModalWindow.close(target);
		CorrespondenceVO correspondenceVO = new CorrespondenceVO();
		containerForm.setModelObject(correspondenceVO);
		onCancel(target);
	}
	
	
	@Override
	protected void onSave(Form<CorrespondenceVO> containerForm,
			AjaxRequestTarget target) {

		Long personSessionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		// get the person and set it on the correspondence object
		try {
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getCorrespondence().setPerson(person);
			if(containerForm.getModelObject().getCorrespondence().getId() == null) {
				studyService.create(containerForm.getModelObject().getCorrespondence());
				this.info("Correspondence was successfully added and linked to subject: " + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
			}else {
				studyService.update(containerForm.getModelObject().getCorrespondence());
				this.info("Correspondence was successfully updated and linked to subject: " + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
			}
			// invoke backend to persist the correspondence
			onSavePostProcess(target);
		}catch (EntityNotFoundException ex) {
			ex.printStackTrace();
		}catch (ArkSystemException ex) {
			ex.printStackTrace();
		}
	}

	
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}


	@Override
	protected boolean isNew() {
		
		if(containerForm.getModelObject().getCorrespondence().getId() == null) {
			return true;
		} else {
			return false;
		}
	}

}
