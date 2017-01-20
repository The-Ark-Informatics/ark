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
package au.org.theark.phenotypic.web.component.phenodatadictionary.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CharactorDefaultMissingAndEncodedValueValidator;
import au.org.theark.core.util.DateFromToValidator;
import au.org.theark.core.util.DefaultMissingValueDateRangeValidator;
import au.org.theark.core.util.DefaultMissingValueDoubleRangeValidator;
import au.org.theark.core.util.DoubleMinimumToMaximumValidator;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetFieldVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.component.customfield.dataentry.DateDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.NumberDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.StringDateModel;
import au.org.theark.core.web.component.customfield.dataentry.TextDataEntryPanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatadictionary.Constants;

//import com.googlecode.wicket.jquery.ui.interaction.droppable.Droppable;


/**
 * CustomField's DetailForm
 * 
 * Follows a slightly different abstraction model again trying to improve on top of the existing CRUD abstraction classes. Of note: - Receive a
 * (compound property) model instead of the container form, which should make it lighter than passing the container form in. - We do not have to pass
 * in the container form's model/VO, but instead this can use an independent model for an independent VO.
 * 
 * @auther elam
 * 
 *         Does not use the containerForm under the revised abstraction.
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DetailForm extends AbstractDetailForm<PhenoDataSetFieldVO> {
	private static final Logger				log	= LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	private int											mode;

	private TextField<String>						fieldIdTxtFld;
	private TextField<String>						fieldNameTxtFld;
	private DropDownChoice<FieldType>				fieldTypeDdc;
	private TextArea<String>						fieldDescriptionTxtAreaFld;
	private DropDownChoice<UnitType>				fieldUnitTypeDdc;
	private TextField<String>						fieldUnitTypeTxtFld;
	private TextArea<String>						fieldEncodedValuesTxtFld;

	private TextArea<String>						fieldLabelTxtAreaFld;
	private CheckBox								fieldDisplayRequiredChkBox;
	private CheckBox								fieldAllowMultiselectChkBox;
	private DropDownChoice<PhenoDataSetGroup>		fieldDisplayFieldGroupDdc;

	protected WebMarkupContainer					phenoDataSetDetailWMC;
	protected WebMarkupContainer					minMaxValueEntryWMC;
	protected WebMarkupContainer					missingValueEntryWMC;
	protected WebMarkupContainer					defaultValueEntryWMC;
	
	protected WebMarkupContainer					phenoDataSetDisplayDetailWMC;
	protected Panel									phenoDataSetDisplayPositionPanel;
	protected Panel									minValueEntryPnl;
	protected Panel									maxValueEntryPnl;
	protected Panel									missingValueEntryPnl;
	protected Panel 								defaultValueEntryPnl;

	//private TextArea<String>						defaultValueTextArea;
	
	protected IModel<List<PhenoDataSetGroup>>		cfGroupDdcListModel;
	
	//New two webMarkupContainers to hold different unit types DropDown and Text.
	private WebMarkupContainer  					panelCustomUnitTypeDropDown;
	private WebMarkupContainer  					panelCustomUnitTypeText;
	
	private HistoryButtonPanel 						historyButtonPanel;
	//private HistoryCustomPhenoFieldButtonPanel historyCustomPhenoFieldButtonPanel;
	//private TextField<Long>							phenoDataSetCategoryOrderNoTxtFld;
	private ArkModule 								arkModule;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;
	
	private Study study;
	//private ArkFunction arkFunction;
	
	
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param cpModel
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, CompoundPropertyModel<PhenoDataSetFieldVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO){
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		cfGroupDdcListModel = new ListModel<PhenoDataSetGroup>();
		refreshEntityFromBackend();
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		study =cpModel.getObject().getPhenoDataSetField().getStudy();
		//arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
	}

	protected void refreshEntityFromBackend() {
		// Refresh the entity from the backend
		if (getModelObject().getPhenoDataSetField().getId() != null) {
			PhenoDataSetField cfFromBackend = iPhenotypicService.getPhenoDataSetField(getModelObject().getPhenoDataSetField().getId());
			getModelObject().setPhenoDataSetField(cfFromBackend);

			/*PhenoDataSetFieldDisplay pdsdFromBackend;
			pdsdFromBackend = iPhenotypicService.getPhenoDataSetFieldDisplayByPhenoDataSetFieldAndGroup(getModelObject().getPhenoDataSetField(),null);
			getModelObject().setPhenoDataSetFieldDisplay(pdsdFromBackend);*/
		}
		/*if (getModelObject().isUsePhenoDataSetFieldDisplay() == true) {
			// Ensure the phenoDataSetFieldDisplay.require is never NULL
			if (getModelObject().getPhenoDataSetFieldDisplay().getRequired() == null) {
				getModelObject().getPhenoDataSetFieldDisplay().setRequired(false);
			}
		}*/
	}
	/**
	 * initialise Field Types.
	 */
	private void initFieldTypeDdc() {
		List<FieldType> fieldTypeCollection = iArkCommonService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(Constants.FIELDTYPE_NAME, Constants.FIELDTYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(Constants.FIELDVO_PHENODATASET_FIELD_TYPE, fieldTypeCollection, fieldTypeRenderer) {
			@Override
			protected void onBeforeRender() {
				if (!isNew()) {
					// Disable fieldType if data exists for the field
					setEnabled(!cpModel.getObject().getPhenoDataSetField().getPhenoFieldHasData());
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
				target.add(missingValueEntryWMC);
				target.add(defaultValueEntryWMC);
				target.add(fieldEncodedValuesTxtFld);
				target.add(fieldUnitTypeDdc);
				target.add(fieldUnitTypeTxtFld);
				target.add(fieldAllowMultiselectChkBox);
			}
		});
	}
	
	
	
	private void updateEncodedValueFld() {
		FieldType fieldType = getModelObject().getPhenoDataSetField().getFieldType();
		if (fieldType != null && fieldType.getName().equals(Constants.FIELD_TYPE_CHARACTER)) {
			// Only allowed to use encodedValues when fieldType == CHARACTER
			fieldEncodedValuesTxtFld.setEnabled(true);
			fieldAllowMultiselectChkBox.setEnabled(true);
		}
		else {
			// Forcibly reset encoded values and allow multi when NUMBER or DATE
			String encVals = fieldEncodedValuesTxtFld.getDefaultModelObjectAsString();
			if (!encVals.isEmpty()) {
				fieldEncodedValuesTxtFld.setDefaultModelObject(null);
			}
			fieldEncodedValuesTxtFld.setEnabled(false);
			if(fieldAllowMultiselectChkBox.getDefaultModelObject() != null) {
				fieldAllowMultiselectChkBox.setDefaultModelObject(false);
			}
			fieldAllowMultiselectChkBox.setEnabled(false);
		}
	}

	private void updateUnitTypeDdc() {
		FieldType fieldType = getModelObject().getPhenoDataSetField().getFieldType();
		if (fieldType != null && !fieldType.getName().equals(Constants.FIELD_TYPE_DATE)) {
			// Only allowed to use unitType when fieldType != DATE
			fieldUnitTypeDdc.setEnabled(true);
			fieldUnitTypeTxtFld.setEnabled(true);
		}
		else {
			fieldUnitTypeDdc.setEnabled(false);
			fieldUnitTypeTxtFld.setEnabled(false);
		}
	}
	
	/**
	 * initialise max and min values.
	 */
	private void initMinMaxValuePnls() {
		FieldType fieldType = getModelObject().getPhenoDataSetField().getFieldType();
		IModel<String> missingValueMdl = new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_MISSING_VALUE);
		IModel<String> defaultValueMdl = new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_DEFAULT_VALUE);
		
			if (fieldType == null || fieldType.getName().equals(Constants.FIELD_TYPE_CHARACTER) 
					|| fieldType.getName().equals(Constants.LOOKUP_FIELD_TYPE_NAME)) {
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
				missingValueEntryPnl = new TextDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				missingValueEntryPnl.setEnabled(true);
				
				defaultValueEntryPnl = new TextDataEntryPanel("defaultValueEntryPnl", defaultValueMdl, new Model<String>("DefaultValue"));
				defaultValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				defaultValueEntryPnl.setEnabled(true);
				
				TextField<?> missing= ((TextDataEntryPanel)missingValueEntryPnl).getDataValueTxtFld();
				TextField<?> defaultVal= ((TextDataEntryPanel)defaultValueEntryPnl).getDataValueTxtFld();
				this.add(new CharactorDefaultMissingAndEncodedValueValidator(fieldEncodedValuesTxtFld, missing,defaultVal, "Encoded Values","Missing Value","Default Value"));
				
			}
			// Not supporting min and max value for CHARACTER fieldTypes
			else if (fieldType.getName().equals(Constants.FIELD_TYPE_NUMBER)) {
				// NUMBER fieldType
				IModel<Double> minValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_PHENODATASET_MIN_VALUE);
				IModel<Double> maxValueMdl = new PropertyModel<Double>(getModelObject(), Constants.FIELDVO_PHENODATASET_MAX_VALUE);
				minValueEntryPnl = new NumberDataEntryPanel("minValueEntryPanel", minValueMdl, new Model<String>("MinValue"));
				minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				maxValueEntryPnl = new NumberDataEntryPanel("maxValueEntryPanel", maxValueMdl, new Model<String>("MaxValue"));
				maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				//missingValueEntryPnl = new NumberDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl = new TextDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				
				defaultValueEntryPnl = new TextDataEntryPanel("defaultValueEntryPnl", defaultValueMdl, new Model<String>("DefaultValue"));
				defaultValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				
				TextField<?> min= ((NumberDataEntryPanel)minValueEntryPnl).getDataValueTxtFld();
				TextField<?> max= ((NumberDataEntryPanel)maxValueEntryPnl).getDataValueTxtFld();
				TextField<?> missingText= ((TextDataEntryPanel)missingValueEntryPnl).getDataValueTxtFld();
				TextField<?> defaultVal= ((TextDataEntryPanel)defaultValueEntryPnl).getDataValueTxtFld();
				this.add(new DoubleMinimumToMaximumValidator(min, max, "Minimum Value", "Maximum Value"));
				this.add(new DefaultMissingValueDoubleRangeValidator(min,max,missingText,defaultVal,"Minimum Value","Maximum Value","Missing Value","Default Value"));
				
			}
			else if (fieldType.getName().equals(Constants.FIELD_TYPE_DATE)) {
				// DATE fieldType
				IModel<Date> minValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_MIN_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				IModel<Date> maxValueMdl = new StringDateModel(new PropertyModel<String>(getModelObject(), Constants.FIELDVO_PHENODATASET_MAX_VALUE), au.org.theark.core.Constants.DD_MM_YYYY);
				minValueEntryPnl = new DateDataEntryPanel("minValueEntryPanel", minValueMdl, new Model<String>("MinValue"));
				minValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				maxValueEntryPnl = new DateDataEntryPanel("maxValueEntryPanel", maxValueMdl, new Model<String>("MaxValue"));
				maxValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				//missingValueEntryPnl = new DateDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl = new TextDataEntryPanel("missingValueEntryPanel", missingValueMdl, new Model<String>("MissingValue"));
				missingValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				
				defaultValueEntryPnl = new TextDataEntryPanel("defaultValueEntryPnl", defaultValueMdl, new Model<String>("DefaultValue"));
				defaultValueEntryPnl.setOutputMarkupPlaceholderTag(true);
				
				DateTextField fromDate= ((DateDataEntryPanel)minValueEntryPnl).getDataValueDateFld();
				DateTextField toDate= ((DateDataEntryPanel)maxValueEntryPnl).getDataValueDateFld();
				//DateTextField missingDate= ((DateDataEntryPanel)missingValueEntryPnl).getDataValueDateFld();
				TextField<?> missingDate= ((TextDataEntryPanel)missingValueEntryPnl).getDataValueTxtFld();
				TextField<?> defaultVal= ((TextDataEntryPanel)defaultValueEntryPnl).getDataValueTxtFld();
				
				this.add(new DateFromToValidator(fromDate,toDate,"Minimum Date","Maximum Date"));
				this.add(new DefaultMissingValueDateRangeValidator(fromDate,toDate,missingDate,defaultVal,"Minimum Date","Maximum Date","Missing Date","Default Date"));
			}
			minMaxValueEntryWMC.addOrReplace(minValueEntryPnl);
			minMaxValueEntryWMC.addOrReplace(maxValueEntryPnl);
			missingValueEntryWMC.addOrReplace(missingValueEntryPnl);
			defaultValueEntryWMC.addOrReplace(defaultValueEntryPnl);
		
	}
	/**
	 * initialise unit types.
	 */
	private void initUnitTypeDdc() {
		UnitType unitTypeCriteria = new UnitType();
		unitTypeCriteria.setArkFunction(cpModel.getObject().getPhenoDataSetField().getArkFunction());
		List<UnitType> unitTypeList = iArkCommonService.getUnitTypes(unitTypeCriteria);
		// assumes that if the unit.name will appear within the unit.description

		ChoiceRenderer unitTypeRenderer = new ChoiceRenderer(Constants.UNITTYPE_DESCRIPTION, Constants.UNITTYPE_ID);
		fieldUnitTypeDdc = new DropDownChoice<UnitType>(Constants.FIELDVO_PHENODATASET_UNIT_TYPE, unitTypeList, unitTypeRenderer);
		fieldUnitTypeDdc.setNullValid(true); // null is ok for units
		fieldUnitTypeDdc.setOutputMarkupId(true); // unitTypeDdc can be enabled/disabled
		
		//Add the Unit type text.
		fieldUnitTypeTxtFld=new TextField<String>(Constants.FIELDVO_PHENODATASET_UNIT_TYPE_TXT);
		fieldUnitTypeTxtFld.setOutputMarkupId(true);
		
	}

	/**
	 * Call this after the constructor is finished in detail panel.
	 */
	public void initialiseDetailForm() {
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		arkModule=iArkCommonService.getArkModuleById(sessionModuleId);
		fieldIdTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELDVO_PHENODATASET_NAME);
		fieldNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		fieldDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_PHENODATASET_DESCRIPTION);
		fieldLabelTxtAreaFld = new TextArea<String>(Constants.FIELDVO_PHENODATASET_FIELD_LABEL);
		//fieldMissingValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MISSING_VALUE);
		fieldDisplayRequiredChkBox = new CheckBox(Constants.FIELDVO_PHENODATASET_REQUIRED);
		fieldAllowMultiselectChkBox = new CheckBox(Constants.FIELDVO_PHENODATASET_ALLOW_MULTIPLE_SELECTION) {
		private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isEnabled() {
				return (fieldEncodedValuesTxtFld.getModelObject() != null);
			}
		};

		/*if (getModelObject().isUsePhenoDataSetFieldDisplay()) {
			// TODO: Have not implemented position support right now
			phenoDataSetDisplayPositionPanel = new EmptyPanel("phenoDataSetFieldDisplayPositionPanel");
		}*/
	//	else {
			phenoDataSetDisplayPositionPanel = new EmptyPanel("phenoDataSetFieldDisplayPositionPanel");
		//}
		
		// Initialise Drop Down Choices
		initFieldTypeDdc();
		initUnitTypeDdc();

		// Min and Max Value panels rely on fieldTypeDdc being already established
		minMaxValueEntryWMC = new WebMarkupContainer("minMaxValueEntryWMC");
		minMaxValueEntryWMC.setOutputMarkupPlaceholderTag(true);
		missingValueEntryWMC= new WebMarkupContainer("missingValueEntryWMC");
		missingValueEntryWMC.setOutputMarkupPlaceholderTag(true);
		
		defaultValueEntryWMC= new WebMarkupContainer("defaultValueEntryWMC");
		defaultValueEntryWMC.setOutputMarkupPlaceholderTag(true);

		// unitType and encodedValues rely on fieldTypeDdc being already established
		fieldEncodedValuesTxtFld = new TextArea<String>(Constants.FIELDVO_PHENODATASET_ENCODED_VALUES);
		fieldEncodedValuesTxtFld.setOutputMarkupId(true);
		fieldEncodedValuesTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(fieldAllowMultiselectChkBox);
			}
		});

		updateEncodedValueFld();
		updateUnitTypeDdc();

		// Have to Edit, before allowing delete
		deleteButton.setEnabled(false);

		//defaultValueTextArea = new TextArea<String>(Constants.FIELDVO_PHENODATASET_DEFAULT_VALUE);
		
		addDetailFormComponents();
		attachValidators();

		initMinMaxValuePnls();
		
		historyButtonPanel = new HistoryButtonPanel(this, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer());
		//historyCustomPhenoFieldButtonPanel = new HistoryCustomPhenoFieldButtonPanel(this, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer());
		
	}
	
	protected void attachValidators() {
		fieldNameTxtFld.setRequired(true).setLabel((new StringResourceModel("phenoDataSetField.name.required", this, new Model<String>("Custom Field Name"))));
		// Enforce particular characters for fieldName
		fieldNameTxtFld.add(new PatternValidator("[a-zA-Z0-9_-]+"));
		fieldNameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
		fieldDescriptionTxtAreaFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255));
		fieldTypeDdc.setRequired(true).setLabel((new StringResourceModel("phenoDataSetField.fieldType.required", this, new Model<String>("Custom Field Type"))));
		fieldLabelTxtAreaFld.add(StringValidator.maximumLength(255));
		fieldUnitTypeTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
		//defaultValueTextArea.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255));
		// TODO: Add correct validator, possibly phenoDataSet with better validation message
		fieldEncodedValuesTxtFld.add(new PatternValidator(au.org.theark.core.Constants.ENCODED_VALUES_PATTERN)).setLabel(
				new StringResourceModel("phenoDataSetField.encodedValues.validation", this, new Model<String>("Encoded Value definition")));
		
	}

	@Override
	protected void onSave(Form<PhenoDataSetFieldVO> containerForm, AjaxRequestTarget target) {
		// NB: creating/updating the phenoDataSetFieldDisplay will be tied to the phenoDataSetField by the service
		if (getModelObject().getPhenoDataSetField().getId() == null) {
			// Save the Field
			try {
				iPhenotypicService.createPhenoDataSetField(getModelObject());
				this.info(new StringResourceModel("info.createSuccessMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
				e.printStackTrace();
			}
			processErrors(target);
		}
		else {
			// Update the Field
			try {
				iPhenotypicService.updatePhenoDataSetField(getModelObject());
				this.info(new StringResourceModel("info.updateSuccessMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
				onSavePostProcess(target);
			}
			catch (ArkSystemException e) {
				this.error(new StringResourceModel("error.internalErrorMsg", this, null).getString());
				e.printStackTrace();
			}
			catch (ArkUniqueException e) {
				this.error(new StringResourceModel("error.nonUniqueCFMsg", this, null, new Object[] { getModelObject().getPhenoDataSetField().getName() }).getString());
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
			iPhenotypicService.deletePhenoDataSetField(getModelObject());
			this.info("Field " + getModelObject().getPhenoDataSetField().getName() + " was deleted successfully");
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
		editCancelProcess(target); // this ends up calling onCancel(target)
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (getModelObject().getPhenoDataSetField().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	// Allow the model for the CustomFieldGroups to be assessed (but not allow it be to be set)
	public IModel<List<PhenoDataSetGroup>> getCfGroupDdcListModel() {
		return cfGroupDdcListModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		phenoDataSetDetailWMC = new WebMarkupContainer("phenoDataSetFieldDetailWMC");
		//Add new panels to handle the Unit Type changes.
		panelCustomUnitTypeDropDown=new WebMarkupContainer("panelCustomUnitTypeDropDown");
		panelCustomUnitTypeDropDown.setOutputMarkupId(true);
		panelCustomUnitTypeText=new WebMarkupContainer("panelCustomUnitTypeText");
		panelCustomUnitTypeText.setOutputMarkupId(true);
		if(arkModule.getName().equals(au.org.theark.core.Constants.ARK_MODULE_PHENOTYPIC)){
			panelCustomUnitTypeDropDown.setVisible(false);
			panelCustomUnitTypeText.setVisible(true);
		}else{
			panelCustomUnitTypeDropDown.setVisible(true);
			panelCustomUnitTypeText.setVisible(false);
		}
		phenoDataSetDetailWMC.setOutputMarkupPlaceholderTag(true);
		phenoDataSetDetailWMC.add(fieldIdTxtFld.setEnabled(false)); // Disable ID field editing
		phenoDataSetDetailWMC.add(fieldNameTxtFld);
		phenoDataSetDetailWMC.add(fieldDescriptionTxtAreaFld);
		phenoDataSetDetailWMC.add(fieldTypeDdc);
		
		//Unit type changes
		panelCustomUnitTypeDropDown.add(fieldUnitTypeDdc);
		phenoDataSetDetailWMC.add(panelCustomUnitTypeDropDown);
		panelCustomUnitTypeText.add(fieldUnitTypeTxtFld);
		phenoDataSetDetailWMC.add(panelCustomUnitTypeText);
		//End of Unit type changes.
		phenoDataSetDetailWMC.add(minMaxValueEntryWMC);
		phenoDataSetDetailWMC.add(fieldEncodedValuesTxtFld);
		//phenoDataSetFieldDetailWMC.add(fieldMissingValueTxtFld);
		phenoDataSetDetailWMC.add(missingValueEntryWMC);
		phenoDataSetDetailWMC.add(fieldLabelTxtAreaFld);
		//phenoDataSetDetailWMC.add(defaultValueTextArea);
		phenoDataSetDetailWMC.add(defaultValueEntryWMC);
		phenoDataSetDisplayDetailWMC = new WebMarkupContainer("phenoDataSetFieldDisplayDetailWMC");
		phenoDataSetDisplayDetailWMC.add(phenoDataSetDisplayPositionPanel);
		phenoDataSetDisplayDetailWMC.add(fieldDisplayRequiredChkBox);
		phenoDataSetDisplayDetailWMC.add(fieldAllowMultiselectChkBox);
		
		// phenoDataSetFieldDisplayDetailWMC.add(fieldDisplayRequireMsgTxtAreaFld);
		// Only show these fields if necessary...
		/*if (getModelObject().isUsePhenoDataSetFieldDisplay() == false) {
			phenoDataSetDisplayDetailWMC.setVisible(false);
		}*/
		
		// TODO: This 'addOrReplace' (instead of just 'add') is a temporary workaround due to the
		// detailPanelFormContainer being initialised only once at the top-level container panel.
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetDetailWMC);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetDisplayDetailWMC);
		//arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(phenoDataSetCategoryDetailWMC);
		
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	
		
	

}
