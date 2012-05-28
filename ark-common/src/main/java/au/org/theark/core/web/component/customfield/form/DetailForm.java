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
package au.org.theark.core.web.component.customfield.form;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.customfield.Constants;
import au.org.theark.core.web.component.customfield.dataentry.DateDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.NumberDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.StringDateModel;
import au.org.theark.core.web.component.customfield.dataentry.TextDataEntryPanel;
import au.org.theark.core.web.form.AbstractDetailForm;

/**
 * CustomField's DetailForm
 * 
 * Follows a slightly different abstraction model again trying to improve
 * on top of the existing CRUD abstraction classes.  Of note:
 * - Receive a (compound property) model instead of the container form, 
 * which should make it lighter than passing the container form in.
 * - We do not have to pass in the container form's model/VO, but instead
 * this can use an independent model for an independent VO.
 * 
 * @auther elam
 * 
 * Does not use the containerForm under the revised abstraction.
 */
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class DetailForm extends AbstractDetailForm<CustomFieldVO> {
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	private int										mode;

	private TextField<String>					fieldIdTxtFld;
	private TextField<String>					fieldNameTxtFld;
	private DropDownChoice<FieldType>		fieldTypeDdc;

	private TextArea<String>					fieldDescriptionTxtAreaFld;
	private DropDownChoice<UnitType>			fieldUnitTypeDdc;
//	private TextField<String>					fieldMinValueTxtFld;
//	private TextField<String>					fieldMaxValueTxtFld;
	private TextArea<String>					fieldEncodedValuesTxtFld;
	private TextField<String>					fieldMissingValueTxtFld;
	
	private TextArea<String>					fieldLabelTxtAreaFld;
	private CheckBox								fieldDisplayRequiredChkBox;
//	private TextArea<String>					fieldDisplayRequireMsgTxtAreaFld;
	private DropDownChoice<CustomFieldGroup>	fieldDisplayFieldGroupDdc;
	
	protected WebMarkupContainer 				customFieldDetailWMC;
	protected WebMarkupContainer				minMaxValueEntryWMC;
	protected WebMarkupContainer				customFieldDisplayDetailWMC;
	protected Panel			 					customFieldDisplayPositionPanel;
	protected Panel								minValueEntryPnl;
	protected Panel								maxValueEntryPnl;

	protected IModel<List<CustomFieldGroup>>	cfGroupDdcListModel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param cpModel
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, CompoundPropertyModel<CustomFieldVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		// Initialise the model to be empty for now
		cfGroupDdcListModel = new ListModel<CustomFieldGroup>();
		
		refreshEntityFromBackend();
	}
	
	protected void refreshEntityFromBackend() {
		// Refresh the entity from the backend
		if (getModelObject().getCustomField().getId() != null) {
			CustomField cfFromBackend = iArkCommonService.getCustomField(getModelObject().getCustomField().getId());
			getModelObject().setCustomField(cfFromBackend);
			
			CustomFieldDisplay cfdFromBackend;
			cfdFromBackend = iArkCommonService.getCustomFieldDisplayByCustomField(cfFromBackend);
			getModelObject().setCustomFieldDisplay(cfdFromBackend);
		}
		if (getModelObject().isUseCustomFieldDisplay() == true) {
			// Ensure the customFieldDisplay.require is never NULL
			if (getModelObject().getCustomFieldDisplay().getRequired() == null) {
				getModelObject().getCustomFieldDisplay().setRequired(false);
			}
		}
	}

	private void initFieldTypeDdc() {
		List<FieldType> fieldTypeCollection = iArkCommonService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(Constants.FIELDTYPE_NAME, Constants.FIELDTYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(Constants.FIELDVO_CUSTOMFIELD_FIELD_TYPE, fieldTypeCollection, fieldTypeRenderer) {
			@Override
			protected void onBeforeRender() {
				if(!isNew()) {
					boolean hasData = iArkCommonService.customFieldHasData(cpModel.getObject().getCustomField());
					// Disable fieldType if data exists for the field
					setEnabled(!hasData);
				}
				super.onBeforeRender();
			}
		};
		fieldTypeDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			protected void onUpdate(AjaxRequestTarget target) {
				updateEncodedValueFld();
				updateUnitTypeDdc();
				initMinMaxValuePnls();
				target.add(minMaxValueEntryWMC);
				target.add(fieldEncodedValuesTxtFld);
				target.add(fieldUnitTypeDdc);
			}
		});
	}

	private void updateEncodedValueFld() {
		FieldType fieldType = getModelObject().getCustomField().getFieldType();
		if (fieldType != null && fieldType.getName().equals(Constants.CHARACTER_FIELD_TYPE_NAME)) {
			// Only allowed to use encodedValues when fieldType == CHARACTER
			fieldEncodedValuesTxtFld.setEnabled(true);
		}
		else {
			fieldEncodedValuesTxtFld.setEnabled(false);
		}
	}
	
	private void updateUnitTypeDdc() {
		FieldType fieldType = getModelObject().getCustomField().getFieldType();
		if (fieldType != null && !fieldType.getName().equals(Constants.DATE_FIELD_TYPE_NAME)) {
			// Only allowed to use unitType when fieldType != DATE
			fieldUnitTypeDdc.setEnabled(true);
		}
		else {
			fieldUnitTypeDdc.setEnabled(false);
		}
	}

	private void initMinMaxValuePnls() {
		FieldType fieldType = getModelObject().getCustomField().getFieldType();
		if (fieldType == null || fieldType.getName().equals(Constants.CHARACTER_FIELD_TYPE_NAME)) {
			// Create disabled min and max value entry panels for fieldType = unspecified (null) / CHARACTER
			// dummyModel is required to ensure Wicket doesn't try to find the textDateValue in the CompoundPropertyModel
			IModel<String> dummyModel = new IModel<String>() {

				public String getObject() {
					return null;
				}

				public void setObject(String object) {
				}

				public void detach() {
				}
				
			};
			minValueEntryPnl = new TextDataEntryPanel("minValueEntryPanel", dummyModel, new Model<String>("MinValue"));
			minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
			minValueEntryPnl.setEnabled(false);
			maxValueEntryPnl = new TextDataEntryPanel("maxValueEntryPanel", dummyModel, new Model<String>("MinValue"));
			maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
			maxValueEntryPnl.setEnabled(false);
		}
//	Not supporting min and max value for CHARACTER fieldTypes 
//		else if (fieldType.getName().equals(Constants.CHARACTER_FIELD_TYPE_NAME)) {
//			IModel<String> minValueMdl = new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE);
//			IModel<String> maxValueMdl = new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE);
//			minValueEntryPnl = new TextDataEntryPanel("minValueEntryPanel", minValueMdl, new Model<String>("MinValue"));
//			minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
//			maxValueEntryPnl = new TextDataEntryPanel("maxValueEntryPanel", maxValueMdl, new Model<String>("MaxValue"));
//			maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
//		}
		else if (fieldType.getName().equals(Constants.NUMBER_FIELD_TYPE_NAME)) {
			// NUMBER fieldType
			IModel<Double> minValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE);
			IModel<Double> maxValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE);
			minValueEntryPnl = new NumberDataEntryPanel("minValueEntryPanel", minValueMdl, new Model<String>("MinValue"));
			minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
			maxValueEntryPnl = new NumberDataEntryPanel("maxValueEntryPanel", maxValueMdl, new Model<String>("MaxValue"));
			maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
		}
		else if (fieldType.getName().equals(Constants.DATE_FIELD_TYPE_NAME)) {
			// DATE fieldType
			IModel<Date> minValueMdl = new StringDateModel(
														new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE),
														au.org.theark.core.Constants.DD_MM_YYYY);
			IModel<Date> maxValueMdl = new StringDateModel(
														new PropertyModel<String>(getModelObject(), Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE),
														au.org.theark.core.Constants.DD_MM_YYYY);
			minValueEntryPnl = new DateDataEntryPanel("minValueEntryPanel", minValueMdl, new Model<String>("MinValue"));
			minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
			maxValueEntryPnl = new DateDataEntryPanel("maxValueEntryPanel", maxValueMdl, new Model<String>("MaxValue"));
			maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
		}
		minMaxValueEntryWMC.addOrReplace(minValueEntryPnl);
		minMaxValueEntryWMC.addOrReplace(maxValueEntryPnl);
	}
	
	private void initUnitTypeDdc() {
		UnitType unitTypeCriteria = new UnitType();
		unitTypeCriteria.setArkFunction(cpModel.getObject().getCustomField().getArkFunction());
		List<UnitType> unitTypeList = iArkCommonService.getUnitTypes(unitTypeCriteria);
		// assumes that if the unit.name will appear within the unit.description 
		
		ChoiceRenderer unitTypeRenderer = new ChoiceRenderer(Constants.UNITTYPE_DESCRIPTION, Constants.UNITTYPE_ID);
		//ChoiceRenderer unitTypeRenderer = new ChoiceRenderer(Constants.UNITTYPE_DESCRIPTION );
		fieldUnitTypeDdc = new DropDownChoice<UnitType>(Constants.FIELDVO_CUSTOMFIELD_UNIT_TYPE, unitTypeList, unitTypeRenderer);
		fieldUnitTypeDdc.setNullValid(true);	// null is ok for units
		fieldUnitTypeDdc.setOutputMarkupId(true);	// unitTypeDdc can be enabled/disabled
	}
	
	/**
	 * Call this after the constructor is finished
	 */
	public void initialiseDetailForm() {
		fieldIdTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_NAME);
		fieldNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		
		fieldDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_DESCRIPTION);
		fieldLabelTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_FIELD_LABEL);
//		fieldMinValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE);
//		fieldMaxValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE);
		
		fieldMissingValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MISSING_VALUE);
		fieldDisplayRequiredChkBox = new CheckBox(Constants.FIELDVO_CUSTOMFIELDDISPLAY_REQUIRED);
//		fieldDisplayRequireMsgTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELDDISPLAY_REQUIRED_MSG);
		
		if (getModelObject().isUseCustomFieldDisplay()) {
			//TODO: Have not implemented position support right now
			customFieldDisplayPositionPanel = new EmptyPanel("customFieldDisplayPositionPanel");
		}
		else {
			customFieldDisplayPositionPanel = new EmptyPanel("customFieldDisplayPositionPanel");		
		}
		
		// Initialise Drop Down Choices
		initFieldTypeDdc();
		initUnitTypeDdc();
		
		// Min and Max Value panels rely on fieldTypeDdc being already established
		minMaxValueEntryWMC = new WebMarkupContainer("minMaxValueEntryWMC");
		minMaxValueEntryWMC.setOutputMarkupPlaceholderTag(true);
		initMinMaxValuePnls();
		
		// unitType and encodedValues rely on fieldTypeDdc being already established
		fieldEncodedValuesTxtFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_ENCODED_VALUES);
		fieldEncodedValuesTxtFld.setOutputMarkupId(true);
		updateEncodedValueFld();
		updateUnitTypeDdc();

		// Have to Edit, before allowing delete
		deleteButton.setEnabled(false);

		attachValidators();
		addDetailFormComponents();
	}

	protected void attachValidators() {
		fieldNameTxtFld.setRequired(true);
		// Enforce particular characters for fieldName
		fieldNameTxtFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
		fieldTypeDdc.setRequired(true);
		
		fieldLabelTxtAreaFld.add(StringValidator.maximumLength(255));
		
		//TODO : perhaps some validation on min max etc
		
		// TODO: Add correct validator, possibly custom with better validation message
		fieldEncodedValuesTxtFld.add(new PatternValidator("(\\b[\\w]+=[^;]+;)*")).setLabel(
				new StringResourceModel("customField.encodedValues.validation", this, new Model<String>("Encoded Value definition")));
	}

	@Override
	protected void onSave(Form<CustomFieldVO> containerForm, AjaxRequestTarget target) {
		// NB: creating/updating the customFieldDisplay will be tied to the customField by the service
		if (getModelObject().getCustomField().getId() == null) {
			// Save the Field
			try {
				iArkCommonService.createCustomField(getModelObject());
				this.info(new StringResourceModel("info.createSuccessMsg", this, null, 
						new Object[] { getModelObject().getCustomField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, 
												new Object[] { getModelObject().getCustomField().getName() }).getString());
				e.printStackTrace();
			}
			processErrors(target);
		}
		else {
			// Update the Field
			try {
				iArkCommonService.updateCustomField(getModelObject());
				this.info(new StringResourceModel("info.updateSuccessMsg", this, null, 
												new Object[] { getModelObject().getCustomField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, 
												new Object[] { getModelObject().getCustomField().getName() }).getString());
				e.printStackTrace();
			}
			processErrors(target);
		}
	}

	protected void onCancel(AjaxRequestTarget target) {

	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}


	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			iArkCommonService.deleteCustomField(getModelObject());
			this.info("Field " + getModelObject().getCustomField().getName() + " was deleted successfully");
		}
		catch (ArkSystemException e) {
			this.error(e.getMessage());
		}
		catch (EntityCannotBeRemoved e) {
			this.error(e.getMessage());
		}

		// Display delete confirmation message
		target.add(feedBackPanel);
		// TODO Implement Exceptions in PhentoypicService
		// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// this.error("A System error occured, we will have someone contact you."); processFeedback(target); }

		// Move focus back to Search form
		editCancelProcess(target);	//this ends up calling onCancel(target)
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (getModelObject().getCustomField().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}
	
	// Allow the model for the CustomFieldGroups to be assessed (but not allow it be to be set)
	public IModel<List<CustomFieldGroup>> getCfGroupDdcListModel() {
		return cfGroupDdcListModel;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		customFieldDetailWMC = new WebMarkupContainer("customFieldDetailWMC");
		customFieldDetailWMC.setOutputMarkupPlaceholderTag(true);
		customFieldDetailWMC.add(fieldIdTxtFld.setEnabled(false));	// Disable ID field editing
		customFieldDetailWMC.add(fieldNameTxtFld);
		customFieldDetailWMC.add(fieldDescriptionTxtAreaFld);
		customFieldDetailWMC.add(fieldTypeDdc);
		customFieldDetailWMC.add(fieldUnitTypeDdc);
		customFieldDetailWMC.add(minMaxValueEntryWMC);
		customFieldDetailWMC.add(fieldEncodedValuesTxtFld);
		customFieldDetailWMC.add(fieldMissingValueTxtFld);
		customFieldDetailWMC.add(fieldLabelTxtAreaFld);
		
		customFieldDisplayDetailWMC = new WebMarkupContainer("customFieldDisplayDetailWMC");
		customFieldDisplayDetailWMC.add(customFieldDisplayPositionPanel);
		customFieldDisplayDetailWMC.add(fieldDisplayRequiredChkBox);
//		customFieldDisplayDetailWMC.add(fieldDisplayRequireMsgTxtAreaFld);
		// Only show these fields if necessary...
		if (getModelObject().isUseCustomFieldDisplay() == false) {
			customFieldDisplayDetailWMC.setVisible(false);
		}
		
		// TODO: This 'addOrReplace' (instead of just 'add') is a temporary workaround due to the 
		// detailPanelFormContainer being initialised only once at the top-level container panel. 
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldDetailWMC);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldDisplayDetailWMC);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
		
	}

}
