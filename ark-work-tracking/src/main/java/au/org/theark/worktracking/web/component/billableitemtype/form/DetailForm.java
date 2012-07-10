package au.org.theark.worktracking.web.component.billableitemtype.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.BillableItemTypeVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;

public class DetailForm extends AbstractDetailForm<BillableItemTypeVo> {
	
	public static final long	serialVersionUID	= -8267651986631341353L;

	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private List<BillableItemTypeStatus> billableItemTypeStatusses;
	
	
	private TextField<String>				billableItemTypeIdTxtField;
	private TextField<String>				billableItemTypeItemNameTxtField;
	private TextField<String>				billableItemTypeQuantityPerUnitTxtField;
	private TextField<String>				billableItemTypeUnitPriceTxtField;
	private TextField<String>				billableItemTypeGstTxtField;
	
	private TextArea<String>				billableItemTypeDescriptionTxtArea;
	
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
		billableItemTypeStatusses= iWorkTrackingService.getBillableItemTypeStatuses();	
		billableItemTypeIdTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_ID); 
		billableItemTypeIdTxtField.setEnabled(false);
		billableItemTypeItemNameTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_ITEM_NAME);       
		billableItemTypeQuantityPerUnitTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_QUANTITY_PER_UNIT);
		billableItemTypeUnitPriceTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_UNIT_PRICE);      
		billableItemTypeGstTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_GST);            
		billableItemTypeDescriptionTxtArea=new TextArea<String>(Constants.BILLABLE_ITEM_TYPE_DESCRIPTION);
		
	
		
		addDetailFormComponents();
		attachValidators();
	}
	
	public void addDetailFormComponents() {	
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeIdTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeItemNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeQuantityPerUnitTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeUnitPriceTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeGstTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeIdTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeDescriptionTxtArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		billableItemTypeItemNameTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_TYPE_ITEM_NAME_REQUIRED, billableItemTypeItemNameTxtField, new Model<String>(Constants.BILLABLE_ITEM_TYPE_ITEM_NAME_TAG)));
		billableItemTypeItemNameTxtField.add(StringValidator.lengthBetween(1, 30)).setLabel(
				new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_TYPE_ITEM_NAME_LENGTH, billableItemTypeItemNameTxtField, new Model<String>(Constants.BILLABLE_ITEM_TYPE_ITEM_NAME_TAG)));
		
		billableItemTypeDescriptionTxtArea.add(StringValidator.lengthBetween(1, 255)).setLabel(
				new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_TYPE_DESCRIPTION_LENGTH, billableItemTypeDescriptionTxtArea, new Model<String>(Constants.BILLABLE_ITEM_TYPE_DESCRIPTION_TAG)));
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		BillableItemTypeVo billableItemTypeVo = new BillableItemTypeVo();
		containerForm.setModelObject(billableItemTypeVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<BillableItemTypeVo> containerForm, AjaxRequestTarget target) {

		target.add(arkCrudContainerVO.getDetailPanelContainer());
		try {
			
			if (containerForm.getModelObject().getBillableItemType().getId() == null) {
				
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().getBillableItemType().setStudyId(studyId);
				containerForm.getModelObject().getBillableItemType().setType(Constants.BILLABLE_ITEM_TYPE_CUSTOM);
				for(BillableItemTypeStatus status:billableItemTypeStatusses){
					if(Constants.BILLABLE_ITEM_TYPE_ACTIVE.equalsIgnoreCase(status.getName())){
						containerForm.getModelObject().getBillableItemType().setBillableItemTypeStatus(status);
						break;
					}
				}
				
				iWorkTrackingService.createBillableItemType(containerForm.getModelObject().getBillableItemType());
				this.info("Billable Item Type " + containerForm.getModelObject().getBillableItemType().getItemName()  + " was created successfully");
				processErrors(target);
			}
			else {
				iWorkTrackingService.updateBillableItemType(containerForm.getModelObject().getBillableItemType());
				this.info("Billable Item Type " + containerForm.getModelObject().getBillableItemType().getItemName() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);

		}
		catch (EntityExistsException e) {
			this.error("A Billa Item Type with the same name already exists for this study.");
			processErrors(target);
		}
		catch (ArkSystemException e) {
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
			
			for(BillableItemTypeStatus status:billableItemTypeStatusses){
				if(Constants.BILLABLE_ITEM_TYPE_INACTIVE.equalsIgnoreCase(status.getName())){
					containerForm.getModelObject().getBillableItemType().setBillableItemTypeStatus(status);
					break;
				}
			}
			
			iWorkTrackingService.updateBillableItemType(containerForm.getModelObject().getBillableItemType());
			containerForm.info("The Billable Item type was deleted successfully.");
			editCancelProcess(target);
		}
		catch (EntityExistsException entityExistsException) {
			processErrors(target);
		}
		catch (ArkSystemException e) {
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
		if (containerForm.getModelObject().getBillableItemType().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

}