package au.org.theark.worktracking.web.component.workrequest.form;

import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.WorkRequestBillableItemVo;
import au.org.theark.worktracking.model.vo.WorkRequestVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.util.NumberValidatable;
import au.org.theark.worktracking.util.ValidatableItemType;
import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class DetailForm extends AbstractDetailForm<WorkRequestVo> {

	public static final long serialVersionUID = -8267651986631341353L;

	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;

	private TextField<String> workRequestIdTxtField;
	private TextField<String> workRequestItemNameTxtField;

	private TextArea<String> workRequestDescriptionTxtArea;

	private DateTextField workRequestRequestedDateDp;
	private DateTextField workRequestCommencedDateDp;
	private DateTextField workRequestCompletedDateDp;

	private DropDownChoice<WorkRequestStatus> workRequestStatuses;
	private DropDownChoice<Researcher> workRequestResearchers;

	private List<WorkRequestStatus> workRequestStatusList;
	private List<Researcher> researcherList;

	private TextField<String> billableItemGstTxtField;
	private CheckBox billableItemGstAllowCheckBox;

	private FeedbackPanel feedBackPanel;

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
		workRequestIdTxtField = new TextField<String>(Constants.WORK_REQUEST_ID);
		workRequestIdTxtField.setEnabled(false);
		workRequestItemNameTxtField = new TextField<String>(Constants.WORK_REQUEST_ITEM_NAME);
		workRequestRequestedDateDp = new DateTextField(Constants.WORK_REQUEST_REQUESTED_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		initDateTextField(workRequestRequestedDateDp);
		workRequestCommencedDateDp = new DateTextField(Constants.WORK_REQUEST_COMMENCED_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		initDateTextField(workRequestCommencedDateDp);
		workRequestCompletedDateDp = new DateTextField(Constants.WORK_REQUEST_COMPLETED_DATE, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		initDateTextField(workRequestCompletedDateDp);
		initWorkRequestStatusDropDown();
		initResearcherDropDown();

		workRequestDescriptionTxtArea = new TextArea<String>(Constants.WORK_REQUEST_DESCRIPTION);

		billableItemGstAllowCheckBox = new CheckBox(Constants.WORK_REQUEST_GST_ALLOW);

		billableItemGstAllowCheckBox.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (billableItemGstAllowCheckBox.getModelObject()) {
					billableItemGstTxtField.setEnabled(true);
				} else {
					billableItemGstTxtField.setEnabled(false);
					containerForm.getModelObject().getWorkRequest().setGst(null);
				}
				target.add(billableItemGstTxtField);
			}
		});

		billableItemGstTxtField = new TextField<String>(Constants.WORK_REQUEST_GST) {
			private static final long serialVersionUID = 1L;

			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
				NumberFormat format = converter.getNumberFormat(getLocale());
				format.setMinimumFractionDigits(2);
				converter.setNumberFormat(getLocale(), format);
				return (IConverter<C>) converter;
			}
		};

		addDetailFormComponents();
		attachValidators();
	}

	private void initResearcherDropDown() {
		Researcher researcher = new Researcher();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		researcher.setStudyId(studyId);
		this.researcherList = iWorkTrackingService.searchResearcher(researcher);
		IChoiceRenderer customChoiceRenderer = new IChoiceRenderer<Researcher>() {
			private static final long serialVersionUID = 1L;

			public Object getDisplayValue(Researcher researcher) {
				return researcher.getFirstName() + " " + researcher.getLastName();
			}

			public String getIdValue(Researcher researcher, int index) {
				return researcher.getId().toString();
			}

		};
		workRequestResearchers = new DropDownChoice(Constants.WORK_REQUEST_RESEARCHER, this.researcherList, customChoiceRenderer);
	}

	private void initWorkRequestStatusDropDown() {
		this.workRequestStatusList = iWorkTrackingService.getWorkRequestStatuses();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		workRequestStatuses = new DropDownChoice(Constants.WORK_REQUEST_REQUEST_STATUS, this.workRequestStatusList, defaultChoiceRenderer);
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

		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemGstAllowCheckBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemGstTxtField);
	}

	private void initDateTextField(DateTextField dateTextField) {
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
		workRequestItemNameTxtField.add(StringValidator.lengthBetween(1, 50)).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_ITEM_NAME_LENGTH, workRequestItemNameTxtField, new Model<String>(Constants.WORK_REQUEST_ITEM_NAME_TAG)));
		workRequestDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_DESCRIPTION_LENGTH, workRequestDescriptionTxtArea, new Model<String>(Constants.WORK_REQUEST_DESCRIPTION_TAG)));
		workRequestRequestedDateDp.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_REQUESTED_DATE_REQUIRED, workRequestRequestedDateDp, new Model<String>(Constants.WORK_REQUEST_REQUESTED_DATE_TAG)));
		workRequestStatuses.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_STATUS_REQUIRED, workRequestStatuses, new Model<String>(Constants.WORK_REQUEST_STATUS_TAG)));
		workRequestResearchers.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_WORK_REQUEST_RESEARCHER_REQUIRED, workRequestResearchers, new Model<String>(Constants.WORK_REQUEST_RESEARCHER_TAG)));
		billableItemGstTxtField.add(new PatternValidator(Constants.TWO_DECIMAL_PATTERN) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<String> validatable) {
				super.validate(new NumberValidatable(validatable, ValidatableItemType.GST));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket
	 * .ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		WorkRequestVo workRequestVo = new WorkRequestVo();
		containerForm.setModelObject(workRequestVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket
	 * .markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<WorkRequestVo> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());
		if (isWorkRequestHasvalidDateValues(target)) {
			WorkRequest workRequest = containerForm.getModelObject().getWorkRequest();
			try {
				if (workRequest.getId() == null) {
					Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					workRequest.setStudyId(studyId);
					//Check for work request for exsistance.
					if(iWorkTrackingService.isWorkRequestExsistForStudy(studyId,workRequest )){
						this.error("Work request " + workRequest.getName()  + " already exists in this study.");
					}else{	
						iWorkTrackingService.createWorkRequest(workRequest);
						this.saveInformation();
						//this.info("Work Request " + workRequest.getName() + " was created successfully");
					}
					processErrors(target);
				} else {
					WorkRequestBillableItemVo workBillableItemVo = iWorkTrackingService.getWorkRequestBillableItem(workRequest);
					if (workBillableItemVo != null && workBillableItemVo.getBillableItemCount() > 0 && (!ObjectUtils.equals(workBillableItemVo.getGstAllow(), workRequest.getGstAllow()) || !ObjectUtils.equals(workBillableItemVo.getGst(), workRequest.getGst()))) {
						this.error("Cannot change GST value because a billable item has been recorded for this work request.");
						processErrors(target);
					}
					iWorkTrackingService.updateWorkRequest(containerForm.getModelObject().getWorkRequest());
					this.updateInformation();
					//this.info("Work Request " + containerForm.getModelObject().getWorkRequest().getName() + " was updated successfully");
					processErrors(target);
				}
				onSavePostProcess(target);
			} catch (Exception e) {
				if(e.getMessage().contains("Duplicate entry")){
					this.error("Work request " + workRequest.getName()  + " already exists in this study.");
				}else{
					this.error("A system error occured. Please contact the system administrator.");
				}
				processErrors(target);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache
	 * .wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	private boolean isWorkRequestHasvalidDateValues(final AjaxRequestTarget target) {
		boolean result = true;
		WorkRequest workRequest = containerForm.getModelObject().getWorkRequest();

		Date requestedDate = workRequest.getRequestedDate();
		Date commenceDate = workRequest.getCommencedDate();
		Date completedDate = workRequest.getCompletedDate();

		WorkRequestStatus status = workRequest.getRequestStatus();

		if ("Not Commenced".equalsIgnoreCase(status.getName()) && requestedDate == null) {
			this.error("Request date is mandatory when status is not commenced");
			processErrors(target);
			return false;
		} else if ("Commenced".equalsIgnoreCase(status.getName()) && (requestedDate == null || commenceDate == null)) {
			this.error("Request date and commence date are mandatory when status is commenced");
			processErrors(target);
			return false;
		} else if ("Completed".equalsIgnoreCase(status.getName()) && (requestedDate == null || commenceDate == null || completedDate == null)) {
			this.error("Request date, commence date and completed date are mandatory when status is completed");
			processErrors(target);
			return false;
		}

		if (commenceDate != null || completedDate != null) {
			if (commenceDate == null && completedDate != null) {
				this.error("Update the commence date before update the complete date");
				processErrors(target);
				return false;
			} else if (commenceDate != null && commenceDate.compareTo(requestedDate) < 0) {
				this.error("Commence date should be same or later than the request date");
				processErrors(target);
				return false;
			} else if (completedDate != null && completedDate.compareTo(commenceDate) < 0) {
				this.error("Complete date should be same or later than the commence date");
				processErrors(target);
				return false;
			}

		}
		return true;
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			Long count = iWorkTrackingService.getBillableItemCount(containerForm.getModelObject().getWorkRequest());
			if (count == 0) {
				iWorkTrackingService.deleteWorkRequest(containerForm.getModelObject().getWorkRequest());
				this.deleteInformation();
				//containerForm.info("The Work Request was deleted successfully.");
				editCancelProcess(target);
			} else {
				containerForm.error("Cannot Delete this work request component. This work request is associated with existing billable items.");
				processErrors(target);
			}
		} catch (Exception e) {
			containerForm.error("A system error has occured. Please contact the system administrator.");
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
		} else {
			return false;
		}

	}

}