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
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.DateValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.component.ArkDatePicker;
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
	private IWorkTrackingService iWorkTrackingService;

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
	
	private DropDownChoice<WorkRequest>		 				billableItemWorkRequests;
	private DropDownChoice<BillableItemType>		 		billableItemItemTypes;
	
	private WebMarkupContainer workTrackingContainer;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		setMultiPart(true);
	}

	public void initialiseDetailForm() {
		initialiseOperatorDropDown();
		// create new DateTextField and assign date format
		dateFld = new DateTextField("correspondence.date", au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateFld);
		dateFld.add(datePicker);

		timeTxtFld = new TextField<String>("correspondence.time");
		initialiseModeTypeDropDown();
		initialiseDirectionTypeDropDown();
		initialiseOutcomeTypeDropDown();
		reasonTxtArea = new TextArea<String>("correspondence.reason");
		detailsTxtArea = new TextArea<String>("correspondence.details");
		commentsTxtArea = new TextArea<String>("correspondence.comments");

		// fileUploadField = new FileUploadField("correspondence.attachmentFilename", new Model<List<FileUpload>>());
		fileUploadField = new FileUploadField("correspondence.attachmentFilename");
		setMaxSize(Bytes.kilobytes(2048));
		
		workTrackingContainer = new WebMarkupContainer("worktrackingcontainer");
//		setOutputMarkupPlaceholderTag(true);
//		workTrackingContainer.setOutputMarkupPlaceholderTag(true);
		workTrackingContainer.setVisible(false);
		
		initBillableItemTypeDropDown();
		initWorkRequestDropDown();

		addDetailFormComponents();
		attachValidators();
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
	
	private void initBillableItemTypeDropDown() {
		
		BillableItemType billableItemType=new BillableItemType();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		billableItemType.setStudyId(studyId);
		List<BillableItemTypeStatus> billableItemTypeStatusses = iWorkTrackingService.getBillableItemTypeStatuses();
		
		for(BillableItemTypeStatus status:billableItemTypeStatusses){
			if(au.org.theark.worktracking.util.Constants.BILLABLE_ITEM_TYPE_ACTIVE.equalsIgnoreCase(status.getName())){
				billableItemType.setBillableItemTypeStatus(status);
				break;
			}
		}
		
		List<BillableItemType>				billableItemTypeList = iWorkTrackingService.searchBillableItemType(billableItemType);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(au.org.theark.worktracking.util.Constants.BIT_ITEM_NAME, Constants.ID);
		billableItemItemTypes = new DropDownChoice("billableItemType",  billableItemTypeList, defaultChoiceRenderer);
	}

	private void initWorkRequestDropDown() {
		WorkRequest workRequest =new WorkRequest();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		workRequest.setStudyId(studyId);
		List<WorkRequest>								workRequestList = iWorkTrackingService.searchWorkRequest(workRequest);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(au.org.theark.worktracking.util.Constants.NAME, Constants.ID);
		billableItemWorkRequests = new DropDownChoice("workRequest",  workRequestList, defaultChoiceRenderer);
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(operatorChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(timeTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(modeTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(directionTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(outcomeTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(reasonTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(detailsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		
		workTrackingContainer.add(billableItemWorkRequests);
		workTrackingContainer.add(billableItemItemTypes);
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(workTrackingContainer);
//		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemWorkRequests);
//		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemItemTypes);	
	}

	protected void attachValidators() {
		dateFld.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("correspondence.date", this, null));
		dateFld.setRequired(true);
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

		Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		// get the person and set it on the correspondence object
		try {
			// set the Person in context
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getCorrespondence().setPerson(person);
			// set the Study in context
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			containerForm.getModelObject().getCorrespondence().setStudy(study);

			if (containerForm.getModelObject().getCorrespondence().getId() == null) {
				// store correspondence file attachment
				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					FileUpload fileUpload = fileUploadField.getFileUpload();
					containerForm.getModelObject().getCorrespondence().setAttachmentPayload(fileUpload.getBytes());
					containerForm.getModelObject().getCorrespondence().setAttachmentFilename(fileUpload.getClientFileName());
				}

				// save
				studyService.create(containerForm.getModelObject().getCorrespondence());				
				this.info("Correspondence was successfully added and linked to subject: " + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
			}
			else {
				// store correspondence file attachment
				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					// retrieve file and store as Blob in database
					FileUpload fileUpload = fileUploadField.getFileUpload();
					containerForm.getModelObject().getCorrespondence().setAttachmentPayload(fileUpload.getBytes());
					containerForm.getModelObject().getCorrespondence().setAttachmentFilename(fileUpload.getClientFileName());
				}

				studyService.update(containerForm.getModelObject().getCorrespondence());
				this.info("Correspondence was successfully updated and linked to subject: " + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
			}
			// invoke backend to persist the correspondence
			
			if(containerForm.getModelObject().getWorkRequest()!=null &&
					containerForm.getModelObject().getBillableItemType()!=null){
				createAutomatedBillableItem();
			}
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
	
	/**
	 * Create the automated billable item when user has selected the work request and billable item type. 
	 */
	private void createAutomatedBillableItem(){
		//Create new billable Item
		BillableItem billableItem =new BillableItem();
		billableItem.setType(au.org.theark.worktracking.util.Constants.BILLABLE_ITEM_AUTOMATED);
		billableItem.setWorkRequest(containerForm.getModelObject().getWorkRequest());
		billableItem.setBillableItemType(containerForm.getModelObject().getBillableItemType());
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		billableItem.setStudyId(studyId);
		billableItem.setQuantity(1d);
		billableItem.setItemCost(containerForm.getModelObject().getBillableItemType().getUnitPrice());
		billableItem.setTotalCost(BillableItemCostCalculator.calculateItemCost(billableItem));
		
		String subjectId= SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID).toString();		
		billableItem.setDescription("Automated - Subject Id "+subjectId);
		billableItem.setInvoice(au.org.theark.worktracking.util.Constants.N);
		billableItem.setCommenceDate(containerForm.getModelObject().getCorrespondence().getDate());
		//Save newly created object
		iWorkTrackingService.createBillableItem(billableItem);
		
		//reset workrequest and billable item type
		containerForm.getModelObject().setWorkRequest(null);
		containerForm.getModelObject().setBillableItemType(null);	
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
}
