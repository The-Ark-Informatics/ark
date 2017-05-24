package au.org.theark.worktracking.web.component.billableitemtype.form;

import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.worktracking.model.vo.BillableItemTypeVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.util.NumberValidatable;
import au.org.theark.worktracking.util.ValidatableItemType;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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
import java.util.List;

public class DetailForm extends AbstractDetailForm<BillableItemTypeVo> {
	
	public static final long	serialVersionUID	= -8267651986631341353L;

	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private List<BillableItemTypeStatus> billableItemTypeStatusses;
	
	
	private TextField<String>				billableItemTypeIdTxtField;
	private TextField<String>				billableItemTypeItemNameTxtField;
	private TextField<String>				billableItemTypeQuantityPerUnitTxtField;
	private TextField<String>				billableItemTypeUnitPriceTxtField;
//	private TextField<String>				billableItemTypeGstTxtField;
	private TextField<String>				billableItemTypeQuantityTypeTxtField;
	
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
		billableItemTypeQuantityPerUnitTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_QUANTITY_PER_UNIT){
		private static final long serialVersionUID = 1L;
		@Override
		public <C> IConverter<C> getConverter(Class<C> type) {
					  	DoubleConverter converter = (DoubleConverter)DoubleConverter.INSTANCE;
						NumberFormat format = converter.getNumberFormat(getLocale());
						format.setMinimumFractionDigits(2);
						converter.setNumberFormat(getLocale(), format);
						return (IConverter<C>) converter; 
			}	
		};
		billableItemTypeUnitPriceTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_UNIT_PRICE){
		private static final long serialVersionUID = 1L;
		@Override
		public <C> IConverter<C> getConverter(Class<C> type) {
				  	DoubleConverter converter = (DoubleConverter)DoubleConverter.INSTANCE;
					NumberFormat format = converter.getNumberFormat(getLocale());
					format.setMinimumFractionDigits(2);
					converter.setNumberFormat(getLocale(), format);
					return (IConverter<C>) converter; 
			}
		};            
      
//		billableItemTypeGstTxtField=new TextField<String>(Constants.BILLABLE_ITEM_TYPE_GST){
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public <C> IConverter<C> getConverter(Class<C> type) {
//				  	DoubleConverter converter = (DoubleConverter)DoubleConverter.INSTANCE;
//					NumberFormat format = converter.getNumberFormat(getLocale());
//					format.setMinimumFractionDigits(2);
//					converter.setNumberFormat(getLocale(), format);
//					return (IConverter<C>) converter; 
//			}
//		};            
		billableItemTypeDescriptionTxtArea=new TextArea<String>(Constants.BILLABLE_ITEM_TYPE_DESCRIPTION);
		billableItemTypeQuantityTypeTxtField= new TextField<String>(Constants.BILLABLE_ITEM_TYPE_QUANTITY_TYPE);
				
		addDetailFormComponents();
		attachValidators();
	}
	
	public void addDetailFormComponents() {	
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeIdTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeItemNameTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeQuantityPerUnitTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeUnitPriceTxtField);
//		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeGstTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeIdTxtField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeDescriptionTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(billableItemTypeQuantityTypeTxtField);
	}
	
//	private enum BillablePriceType{
//		UNIT_PRICE,GST;
//	}

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
		billableItemTypeUnitPriceTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_TYPE_UNIT_PRICE_REQUIRED, billableItemTypeUnitPriceTxtField, new Model<String>(Constants.BILLABLE_ITEM_TYPE_UNIT_PRICE_TAG)));
		billableItemTypeQuantityPerUnitTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_TYPE_QUNATITY_PER_UNIT_REQUIRED, billableItemTypeUnitPriceTxtField, new Model<String>(Constants.BILLABLE_ITEM_TYPE_QUNATITY_PER_UNIT_TAG)));
//		billableItemTypeGstTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_TYPE_GST_REQUIRED,billableItemTypeGstTxtField,new Model<String>(Constants.BILLABLE_ITEM_TYPE_GST_TAG)));
		billableItemTypeUnitPriceTxtField.add(new PatternValidator(Constants.TWO_DECIMAL_PATTERN){
			private static final long serialVersionUID = 1L;
			@Override
			public void validate(IValidatable<String> validatable) {
				super.validate(new NumberValidatable(validatable,ValidatableItemType.UNIT_PRICE));
			}
		});
		billableItemTypeQuantityTypeTxtField.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_BILLABLE_ITEM_TYPE_QUNATITY_TYPE_REQUIRED, billableItemTypeQuantityTypeTxtField, new Model<String>(Constants.BILLABLE_ITEM_TYPE_QUNATITY_TYPE_TAG)));
		billableItemTypeQuantityPerUnitTxtField.add(new PatternValidator(Constants.NON_NEGATIVE_PATTERN){
			private static final long serialVersionUID = 1L;
			@Override
			public void validate(IValidatable<String> validatable) {
				super.validate(new NumberValidatable(validatable,ValidatableItemType.QUANTITY_PER_UNIT));
			}
		});
//		billableItemTypeGstTxtField.add(new PatternValidator(Constants.BILLABLE_ITEM_TYPE_TWO_DECIMAL_PATTERN){
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onValidate(IValidatable<String> validatable) {
//				super.onValidate(new DoubleValidatable(validatable,ValidatableItemType.GST));
//			}
//		});		
	}
	
	
	/**
	 * Convert Double input value to String and set the validate error messages.  
	 * @author thilina
	 * 
	 * @see IValidatable
	 * @see PatternValidator
	 *
	 */
//	private class DoubleValidatable implements IValidatable<String>{
//		
//		private IValidatable<String> validatable;
//		private BillablePriceType priceType;
//		
//		DoubleValidatable(IValidatable<String> validatable,BillablePriceType priceType){
//			this.validatable=validatable;
//			this.priceType=priceType;
//		}
//		
//		public String getValue() {
//			Object obj=this.validatable.getValue();
//			return obj.toString();
//		}
//
//		public void error(IValidationError error) {
//			ValidationError validationError = (ValidationError) error;
//			switch(priceType){
//				case UNIT_PRICE:
//					this.validatable.error(validationError.addMessageKey(Constants.ERROR_BILLABLE_ITEM_TYPE_UNIT_PRICE));
//					break;
//				case GST:
//					this.validatable.error(validationError.addMessageKey(Constants.ERROR_BILLABLE_ITEM_TYPE_GST));
//					break;
//			}
//		}
//
//		public boolean isValid() {
//			// TODO Auto-generated method stub
//			return this.validatable.isValid();
//		}
//
//		public IModel<String> getModel() {
//			// TODO Auto-generated method stub
//			return this.validatable.getModel();
//		}
//		
//	}
	
	
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
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			try {
				if (containerForm.getModelObject().getBillableItemType().getId() == null) {
					containerForm.getModelObject().getBillableItemType().setStudyId(studyId);
					containerForm.getModelObject().getBillableItemType().setType(Constants.BILLABLE_ITEM_TYPE_CUSTOM);
					for(BillableItemTypeStatus status:billableItemTypeStatusses){
						if(Constants.BILLABLE_ITEM_TYPE_ACTIVE.equalsIgnoreCase(status.getName())){
							containerForm.getModelObject().getBillableItemType().setBillableItemTypeStatus(status);
							break;
						}
					}
					//Check for billable item type for exsistance.
					if(iWorkTrackingService.isBillableItemTypeExsistForStudy(studyId, containerForm.getModelObject().getBillableItemType())){
						this.error("Billable Item Type " + containerForm.getModelObject().getBillableItemType().getItemName()  + " already exists in this study.");
					}else{	
						iWorkTrackingService.createBillableItemType(containerForm.getModelObject().getBillableItemType());
						this.saveInformation();
						//this.info("Billable Item Type " + containerForm.getModelObject().getBillableItemType().getItemName()  + " was created successfully");
					}
				}else {
					iWorkTrackingService.updateBillableItemType(containerForm.getModelObject().getBillableItemType());
					this.updateInformation();
					//this.info("Billable Item Type " + containerForm.getModelObject().getBillableItemType().getItemName() + " was updated successfully");
				}
				onSavePostProcess(target);
			}
			catch (Exception e) {
				if(e.getMessage().contains("Duplicate entry")){
					this.error("Billable Item Type " + containerForm.getModelObject().getBillableItemType().getItemName()  + " already exists in this study.");
				}else{
					this.error("A system error occurred. Please contact the system administrator.");
				}
			}
		processErrors(target);
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
			
		Long count = iWorkTrackingService.getBillableItemCount(containerForm.getModelObject().getBillableItemType());
		if(count == 0){
			
			for(BillableItemTypeStatus status:billableItemTypeStatusses){
				if(Constants.BILLABLE_ITEM_TYPE_INACTIVE.equalsIgnoreCase(status.getName())){
					containerForm.getModelObject().getBillableItemType().setBillableItemTypeStatus(status);
					break;
				}
			}
			
			iWorkTrackingService.updateBillableItemType(containerForm.getModelObject().getBillableItemType());
			this.deleteInformation();
			//containerForm.info("The Billable Item type was deleted successfully.");
			editCancelProcess(target);
		}else{
			containerForm.error("The billable item type could not be deleted. This billable item type is associated with existing billable items.");
			processErrors(target);
		}	
		}
		catch (Exception e) {
			containerForm.error("A system error has occurred. Please contact the system administrator.");
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