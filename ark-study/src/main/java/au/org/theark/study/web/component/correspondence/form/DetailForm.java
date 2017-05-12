/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.web.component.correspondence.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.BillableItemCostCalculator;

public class DetailForm extends AbstractDetailForm<CorrespondenceVO> {

	private static final long										serialVersionUID	= 2900999695563378447L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>							iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService											studyService;

	@SpringBean(name = au.org.theark.worktracking.util.Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService									iWorkTrackingService;

	private DropDownChoice<ArkUser>								operatorChoice;
	private DateTextField											dateFld;
	private TextField<String>										timeTxtFld;
	private DropDownChoice<CorrespondenceModeType>			modeTypeChoice;
	private DropDownChoice<CorrespondenceDirectionType>	directionTypeChoice;
	private DropDownChoice<CorrespondenceOutcomeType>		outcomeTypeChoice;
	private TextArea<String>										reasonTxtArea;
	private TextArea<String>										detailsTxtArea;
	private TextArea<String>										commentsTxtArea;
	private FileUploadField											fileUploadField;

	private DropDownChoice<WorkRequest>							billableItemWorkRequests;
	private DropDownChoice<BillableItemType>					billableItemItemTypes;

	private Label														billableItemLbl;
	private Label														studyNameLbl;
	private AjaxButton												clearButton;
	private AjaxButton												deleteButton;
	private Label														fileNameLbl;

	private WebMarkupContainer										workTrackingContainer;
	private HistoryButtonPanel										historyButtonPanel;
	private  WebMarkupContainer										categoryPanelDirectionType;
	private  WebMarkupContainer										categoryPanelOutCome;


	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		setMultiPart(true);
	}

	@Override
	public void onBeforeRender() {
		historyButtonPanel.setVisible(!isNew());
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		initialiseOperatorDropDown();
		// create new DateTextField and assign date format
		dateFld = new DateTextField("correspondence.date", new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateFld);
		dateFld.add(datePicker);

		timeTxtFld = new TextField<String>("correspondence.time");
		initCategoryPanels();
		initialiseModeTypeDropDown();
		initialiseDirectionTypeDropDown();
		initialiseOutcomeTypeDropDown();
		reasonTxtArea = new TextArea<String>("correspondence.reason");
		detailsTxtArea = new TextArea<String>("correspondence.details");
		commentsTxtArea = new TextArea<String>("correspondence.comments");

		billableItemLbl = new Label("billableItemDescription");
		billableItemLbl.setOutputMarkupId(true);
		billableItemLbl.setVisible(true);

		studyNameLbl = new Label("studyName");
		studyNameLbl.setOutputMarkupId(true);
		studyNameLbl.setVisible(true);

		// fileUploadField = new FileUploadField("correspondence.attachmentFilename", new Model<List<FileUpload>>());
		fileUploadField = new FileUploadField("file");
		setMaxSize(Bytes.kilobytes(2048));

		fileNameLbl = new Label("correspondence.attachmentFilename");
		fileNameLbl.setOutputMarkupId(true);
		
		clearButton = new AjaxButton("clearButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}
		};
		clearButton.add(new AttributeModifier("title", new Model<String>("Clear Attachment")));
		
		deleteButton = new AjaxButton("deleteButton") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getCorrespondence().setAttachmentPayload(null);
				containerForm.getModelObject().getCorrespondence().setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getCorrespondence().setAttachmentPayload(null);
				containerForm.getModelObject().getCorrespondence().setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}
			
			@Override
			public boolean isVisible() {
				return (containerForm.getModelObject().getCorrespondence().getAttachmentFilename() != null) && !containerForm.getModelObject().getCorrespondence().getAttachmentFilename().isEmpty();
			}
		};
		deleteButton.add(new AttributeModifier("title", new Model<String>("Delete Attachment")));
		deleteButton.setOutputMarkupId(true);
		
		workTrackingContainer = new WebMarkupContainer("worktrackingcontainer");
		workTrackingContainer.setVisible(false);

		initBillableItemTypeDropDown();
		initWorkRequestDropDown();

		addDetailFormComponents();
		attachValidators();

		historyButtonPanel = new HistoryButtonPanel(containerForm, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer());
	}

	private void initialiseOperatorDropDown() {

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);

		Collection<ArkUser> coll = studyService.lookupArkUser(study);
		List<ArkUser> list = new ArrayList<ArkUser>(coll);
		ChoiceRenderer<ArkUser> defaultRenderer = new ChoiceRenderer<ArkUser>("ldapUserName", "id");
		operatorChoice = new DropDownChoice<ArkUser>("correspondence.operator", list, defaultRenderer);
	}

	private void initialiseModeTypeDropDown() {
		List<CorrespondenceModeType> list = studyService.getCorrespondenceModeTypes();
		ChoiceRenderer<CorrespondenceModeType> defaultRenderer = new ChoiceRenderer<CorrespondenceModeType>("name", "id");
		modeTypeChoice = new DropDownChoice<CorrespondenceModeType>("correspondence.correspondenceModeType", list, defaultRenderer);
		modeTypeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
            	List<CorrespondenceDirectionType> list = studyService.getCorrespondenceDirectionForMode(modeTypeChoice.getModelObject());
            	categoryPanelDirectionType.remove(directionTypeChoice);
            	ChoiceRenderer<CorrespondenceDirectionType> defaultRenderer = new ChoiceRenderer<CorrespondenceDirectionType>("name", "id");
            	directionTypeChoice = new DropDownChoice<CorrespondenceDirectionType>("correspondence.correspondenceDirectionType", list, defaultRenderer);
            	directionTypeChoice.setOutputMarkupId(true);
            	directionTypeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    protected void onUpdate(AjaxRequestTarget target) {
                    	List<CorrespondenceOutcomeType> list = studyService.getCorrespondenceOutcomeTypesForModeAndDirection(modeTypeChoice.getModelObject(),directionTypeChoice.getModelObject() );
                    	categoryPanelOutCome.remove(outcomeTypeChoice);
                    	ChoiceRenderer<CorrespondenceOutcomeType> defaultRenderer = new ChoiceRenderer<CorrespondenceOutcomeType>("name", "id");
                    	outcomeTypeChoice = new DropDownChoice<CorrespondenceOutcomeType>("correspondence.correspondenceOutcomeType", list, defaultRenderer);
                		outcomeTypeChoice.setOutputMarkupId(true);
                		categoryPanelOutCome.addOrReplace(outcomeTypeChoice);
        		    	target.add(outcomeTypeChoice);
        		    	target.add(categoryPanelOutCome);
                    }
                    });
            	categoryPanelDirectionType.addOrReplace(directionTypeChoice);
		    	target.add(directionTypeChoice);
		    	target.add(categoryPanelDirectionType);
            }
            });
	}

	private void initialiseDirectionTypeDropDown() {
		List<CorrespondenceDirectionType> list = studyService.getCorrespondenceDirectionTypes();
		ChoiceRenderer<CorrespondenceDirectionType> defaultRenderer = new ChoiceRenderer<CorrespondenceDirectionType>("name", "id");
		directionTypeChoice = new DropDownChoice<CorrespondenceDirectionType>("correspondence.correspondenceDirectionType", list, defaultRenderer);
		directionTypeChoice.setOutputMarkupId(true);
	}

	private void initialiseOutcomeTypeDropDown() {
		List<CorrespondenceOutcomeType> list = studyService.getCorrespondenceOutcomeTypes();
		ChoiceRenderer<CorrespondenceOutcomeType> defaultRenderer = new ChoiceRenderer<CorrespondenceOutcomeType>("name", "id");
		outcomeTypeChoice = new DropDownChoice<CorrespondenceOutcomeType>("correspondence.correspondenceOutcomeType", list, defaultRenderer);
		outcomeTypeChoice.setOutputMarkupId(true);
	}

	private void initBillableItemTypeDropDown() {

		BillableItemType billableItemType = new BillableItemType();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		billableItemType.setStudyId(studyId);
		List<BillableItemTypeStatus> billableItemTypeStatusses = iWorkTrackingService.getBillableItemTypeStatuses();

		for (BillableItemTypeStatus status : billableItemTypeStatusses) {
			if (au.org.theark.worktracking.util.Constants.BILLABLE_ITEM_TYPE_ACTIVE.equalsIgnoreCase(status.getName())) {
				billableItemType.setBillableItemTypeStatus(status);
				break;
			}
		}

		List<BillableItemType> billableItemTypeList = iWorkTrackingService.searchBillableItemType(billableItemType);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(au.org.theark.worktracking.util.Constants.BIT_ITEM_NAME, Constants.ID);
		billableItemItemTypes = new DropDownChoice("billableItemType", billableItemTypeList, defaultChoiceRenderer);
	}

	private void initWorkRequestDropDown() {
		WorkRequest workRequest = new WorkRequest();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		workRequest.setStudyId(studyId);
		List<WorkRequestStatus> requestStatusList = iWorkTrackingService.getWorkRequestStatuses();
		for (WorkRequestStatus status : requestStatusList) {
			if ("Commenced".equalsIgnoreCase(status.getName())) {
				workRequest.setRequestStatus(status);
				break;
			}
		}
		List<WorkRequest> workRequestList = iWorkTrackingService.searchWorkRequest(workRequest);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(au.org.theark.worktracking.util.Constants.NAME, Constants.ID);
		billableItemWorkRequests = new DropDownChoice("workRequest", workRequestList, defaultChoiceRenderer);
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(operatorChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(timeTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(modeTypeChoice);
		//--panel adding for dynamic behavior.
		categoryPanelDirectionType.add(directionTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(categoryPanelDirectionType);
		categoryPanelOutCome.add(outcomeTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(categoryPanelOutCome);
		//---
		arkCrudContainerVO.getDetailPanelFormContainer().add(reasonTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(detailsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyNameLbl);

		workTrackingContainer.add(billableItemWorkRequests);
		workTrackingContainer.add(billableItemItemTypes);

		arkCrudContainerVO.getDetailPanelFormContainer().add(workTrackingContainer);
		arkCrudContainerVO.getDetailPanelFormContainer().add(clearButton);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(deleteButton);
	}

	protected void attachValidators() {
		//dateFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("correspondence.date", this, null));
		dateFld.setLabel(new StringResourceModel("correspondence.date", this, null));
		dateFld.setRequired(true);
		reasonTxtArea.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255));
		detailsTxtArea.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255));  
		commentsTxtArea.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_COMMENTS_MAX_LENGTH_500)); 
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {

		CorrespondenceVO correspondenceVO = new CorrespondenceVO();
		containerForm.setModelObject(correspondenceVO);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {

		try {
			studyService.delete(containerForm.getModelObject().getCorrespondence());
			containerForm.info("The correspondence has been deleted successfully.");
			editCancelProcess(target);
		}
		catch (Exception ex) {
			this.error("An error occurred while processing the correspondence delete operation.");
			ex.printStackTrace();
		}
		onCancel(target);
	}

	@Override
	protected void onSave(Form<CorrespondenceVO> containerForm, AjaxRequestTarget target) {

		CorrespondenceVO correspondenceVO = containerForm.getModelObject();

		if (correspondenceVO.getCorrespondence().getId() == null) {

			if ((correspondenceVO.getWorkRequest() == null && correspondenceVO.getBillableItemType() != null)) {
				this.error("Work request must be set if a billable item type is specified");
				processErrors(target);
				return;
			}

			if (correspondenceVO.getWorkRequest() != null && correspondenceVO.getBillableItemType() == null) {
				this.error("Billable item type must be set if a work request is specified");
				processErrors(target);
				return;
			}
		}

		if (containerForm.getModelObject().getWorkRequest() != null && containerForm.getModelObject().getBillableItemType() != null) {
			BillableItem billableItem = createAutomatedBillableItem();
			containerForm.getModelObject().getCorrespondence().setBillableItem(billableItem);
		}

		Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		// get the person and set it on the correspondence object
		try {
			// set the Person in context
			//Person person = studyService.getPerson(personSessionId);

			// set the Study in context
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			LinkSubjectStudy lss = studyService.getSubjectLinkedToStudy(personSessionId, study);
			containerForm.getModelObject().getCorrespondence().setLss(lss);
			//containerForm.getModelObject().getCorrespondence().setStudy(study);

			if (containerForm.getModelObject().getCorrespondence().getId() == null) {
				// store correspondence file attachment
				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					FileUpload fileUpload = fileUploadField.getFileUpload();
					
					byte[] byteArray = fileUpload.getMD5();
					String checksum = getHex(byteArray);
					
					containerForm.getModelObject().getCorrespondence().setAttachmentPayload(fileUpload.getBytes());
					containerForm.getModelObject().getCorrespondence().setAttachmentFilename(fileUpload.getClientFileName());
					containerForm.getModelObject().getCorrespondence().setAttachmentChecksum(checksum);
				}

				// save
				studyService.create(containerForm.getModelObject().getCorrespondence());
				this.info("Correspondence was successfully added and linked to subject: " + lss.getSubjectUID());
				processErrors(target);
			}
			else {
				// store correspondence file attachment
				String checksum=null;
				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					// retrieve file and store as Blob in database
					FileUpload fileUpload = fileUploadField.getFileUpload();
					
					byte[] byteArray = fileUpload.getMD5();
					checksum = getHex(byteArray);
					
					containerForm.getModelObject().getCorrespondence().setAttachmentPayload(fileUpload.getBytes());
					containerForm.getModelObject().getCorrespondence().setAttachmentFilename(fileUpload.getClientFileName());
//					containerForm.getModelObject().getCorrespondence().setAttachementChecksum(checksum);
				}

				try {
					studyService.update(containerForm.getModelObject().getCorrespondence(),checksum);
				} catch (ArkFileNotFoundException e) {
					this.error("Couldn't find the file.");;
				}
				this.info("Correspondence was successfully updated and linked to subject: " + lss.getSubjectUID());
				processErrors(target);
			}
			// invoke backend to persist the correspondence

			workTrackingContainer.setVisible(false);
			onSavePostProcess(target);
		}
		catch (EntityNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (ArkSystemException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onSavePostProcess(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		super.onSavePostProcess(target);
		if (containerForm.getModelObject().getCorrespondence().getBillableItem() != null) {
			AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
			ajaxButton.setEnabled(false);
		}
	}

	/**
	 * Create the automated billable item when user has selected the work request and billable item type.
	 */
	private BillableItem createAutomatedBillableItem() {
		// Create new billable Item
		BillableItem billableItem = new BillableItem();
		billableItem.setType(au.org.theark.worktracking.util.Constants.BILLABLE_ITEM_AUTOMATED);
		billableItem.setWorkRequest(containerForm.getModelObject().getWorkRequest());
		billableItem.setBillableItemType(containerForm.getModelObject().getBillableItemType());
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		billableItem.setStudyId(studyId);
		billableItem.setQuantity(1d);
		billableItem.setItemCost(containerForm.getModelObject().getBillableItemType().getUnitPrice());
		billableItem.setTotalCost(BillableItemCostCalculator.calculateItemCost(billableItem));

		String subjectId = SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID).toString();
		billableItem.setDescription("Automated - Subject Id " + subjectId);
		billableItem.setInvoice(au.org.theark.worktracking.util.Constants.N);
		billableItem.setCommenceDate(containerForm.getModelObject().getCorrespondence().getDate());
		// Save newly created object
		iWorkTrackingService.createBillableItem(billableItem);

		// reset workrequest and billable item type
		containerForm.getModelObject().setWorkRequest(null);
		containerForm.getModelObject().setBillableItemType(null);

		return billableItem;
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	@Override
	protected boolean isNew() {

		if (containerForm.getModelObject().getCorrespondence().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
	private void initCategoryPanels(){
		categoryPanelDirectionType=new WebMarkupContainer("categoryPanelDirectionType");
		categoryPanelDirectionType.setOutputMarkupId(true);
		categoryPanelOutCome=new WebMarkupContainer("categoryPanelOutCome");
		categoryPanelOutCome.setOutputMarkupId(true);
		
	}
	
}
