package au.org.theark.worktracking.web.component.workrequest.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.WorkRequestVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;

public class DetailForm extends AbstractDetailForm<WorkRequestVo> {
	
	public static final long	serialVersionUID	= -8267651986631341353L;

	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private TextField<String>				workRequestIdTxtField;
	private TextField<String>				workRequestItemNameTxtField;
	
	private TextArea<String>				workRequestDescriptionTxtArea;
	
	private DateTextField					workRequestRequestedDateDp;
	private DateTextField					workRequestCommencedDateDp;
	private DateTextField					workRequestCompletedDateDp;
	
	private DropDownChoice<WorkRequestStatus>		 workRequestStatuses;
	private DropDownChoice<Researcher>		 workRequestResearchers;
	
	private List<WorkRequestStatus> workRequestStatusList;
	private List<Researcher> researcherList;
	
	private FeedbackPanel		feedBackPanel;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {

		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		workRequestIdTxtField=new TextField<String>(Constants.WORK_REQUEST_ID);   
		workRequestIdTxtField.setEnabled(false);
		workRequestItemNameTxtField=new TextField<String>(Constants.WORK_REQUEST_ITEM_NAME);       
		workRequestRequestedDateDp=new DateTextField(Constants.WORK_REQUEST_REQUESTED_DATE,au.org.theark.core.Constants.DD_MM_YYYY);
		initDateTextField(workRequestRequestedDateDp);
		workRequestCommencedDateDp=new DateTextField(Constants.WORK_REQUEST_COMMENCED_DATE,au.org.theark.core.Constants.DD_MM_YYYY);      
		initDateTextField(workRequestCommencedDateDp);
		workRequestCompletedDateDp=new DateTextField(Constants.WORK_REQUEST_COMPLETED_DATE,au.org.theark.core.Constants.DD_MM_YYYY);
		initDateTextField(workRequestCompletedDateDp);
		initWorkRequestStatusDropDown();
		initResearcherDropDown();
		
		workRequestDescriptionTxtArea=new TextArea<String>(Constants.WORK_REQUEST_DESCRIPTION); 
		
		addDetailFormComponents();
		attachValidators();
	}
	
	private void initResearcherDropDown() {
		Researcher researcher=new Researcher();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		researcher.setStudyId(studyId);
		this.researcherList=iWorkTrackingService.searchResearcher(researcher);
		IChoiceRenderer customChoiceRenderer = new IChoiceRenderer<Researcher>() {

			public Object getDisplayValue(Researcher researcher) {
				return researcher.getFirstName()+" "+researcher.getLastName();
			}

			public String getIdValue(Researcher researcher, int index) {
				return researcher.getId().toString();
			}
			
		};
		workRequestResearchers = new DropDownChoice(Constants.WORK_REQUEST_RESEARCHER,  this.researcherList, customChoiceRenderer);
	}

		


	private void initWorkRequestStatusDropDown() {
		this.workRequestStatusList=iWorkTrackingService.getWorkRequestStatuses();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		workRequestStatuses = new DropDownChoice(Constants.WORK_REQUEST_REQUEST_STATUS,  this.workRequestStatusList, defaultChoiceRenderer);
	}
	
	public void addDetailFormComponents() {	
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestIdTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestItemNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestRequestedDateDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestCommencedDateDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestCompletedDateDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestStatuses);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestDescriptionTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(workRequestResearchers);
	}
	
	private void initDateTextField(DateTextField dateTextField){
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateTextField);
		dateTextField.add(datePicker);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		workRequestItemNameTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_ITEM_NAME_REQUIRED, workRequestItemNameTxtField, new Model<String>(Constants.WORK_REQUEST_ITEM_NAME_TAG)));
		workRequestItemNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_REQUEST_ITEM_NAME_LENGTH, workRequestItemNameTxtField, new Model<String>(Constants.WORK_REQUEST_ITEM_NAME_TAG)));
		workRequestDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel(Constants.ERROR_WORK_REQUEST_DESCRIPTION_LENGTH, workRequestDescriptionTxtArea, new Model<String>(Constants.WORK_REQUEST_DESCRIPTION_TAG)));
		workRequestRequestedDateDp.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_REQUESTED_DATE_REQUIRED, workRequestRequestedDateDp, new Model<String>(Constants.WORK_REQUEST_REQUESTED_DATE_TAG)));
		workRequestStatuses.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_STATUS_REQUIRED, workRequestStatuses, new Model<String>(Constants.WORK_REQUEST_STATUS_TAG)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		WorkRequestVo workRequestVo = new WorkRequestVo();
		containerForm.setModelObject(workRequestVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<WorkRequestVo> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());
		try {
			
			if (containerForm.getModelObject().getWorkRequest().getId() == null) {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().getWorkRequest().setStudyId(studyId);
				
				
				iWorkTrackingService.createWorkRequest(containerForm.getModelObject().getWorkRequest());
				this.info("Work Request " + containerForm.getModelObject().getWorkRequest().getName()  + " was created successfully");
				processErrors(target);
			}
			else {
				iWorkTrackingService.updateWorkRequest(containerForm.getModelObject().getWorkRequest());
				this.info("Work Request " +  containerForm.getModelObject().getWorkRequest().getName() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);

		}
		catch (Exception e) {
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			Long count = iWorkTrackingService.getBillableItemCount(containerForm.getModelObject().getWorkRequest());
			if(count == 0){
				iWorkTrackingService.deleteWorkRequest(containerForm.getModelObject().getWorkRequest());
				containerForm.info("The Work Request was deleted successfully.");
				editCancelProcess(target);
			}else{
				containerForm.error("Cannot Delete this Work Request Component. This Work Request is associated with existing Billable Items ");
				processErrors(target);
			}
		}
		catch (Exception e) {
			containerForm.error("A System Error has occured please contact support.");
			processErrors(target);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getWorkRequest().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

}