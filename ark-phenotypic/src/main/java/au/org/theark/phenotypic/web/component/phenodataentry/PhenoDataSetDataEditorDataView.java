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
package au.org.theark.phenotypic.web.component.phenodataentry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.study.entity.IPhenoDataSetFieldData;
import au.org.theark.core.web.component.customfield.dataentry.CheckGroupDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.DateDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.DropDownChoiceDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.EncodedValueVO;
import au.org.theark.core.web.component.customfield.dataentry.NumberDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.TextDataEntryPanel;

/**
 * PhenoDataSetDataEditorDataView is designed to assist in rendering a phenoDataSetField Data entry panel
 * through its re-usable implementation of populateItem(..).  Make sure the super.populateItem
 * is called if you override the populateItem(..) method.
 * 
 * @author elam
 *
 * @param <T> specifies the underlying Entity/VO that stores the phenoDataSetField data 
 */
public abstract class PhenoDataSetDataEditorDataView<T extends IPhenoDataSetFieldData> extends DataView<T> {

	private static final Logger				log					= LoggerFactory.getLogger(PhenoDataSetDataEditorDataView.class);

	private static final long	serialVersionUID	= 1L;

	protected PhenoDataSetDataEditorDataView(String id, IDataProvider<T> dataProvider) {
		super(id, dataProvider);
	}

	@Override
	protected void populateItem(Item<T> item) {
		IPhenoDataSetFieldData aCustomData = item.getModelObject();
		PhenoDataSetField pf = aCustomData.getPhenoDataSetFieldDisplay().getPhenoDataSetField();
		PhenoDataSetFieldDisplay pfd = aCustomData.getPhenoDataSetFieldDisplay();
		
		// Determine label of component, also used for error messages
		String labelModel = new String();
		if (pf.getFieldLabel() != null) {
			labelModel = pf.getFieldLabel();
		}
		else {
			// Defaults to name if no fieldLabel
			labelModel = pf.getName();
		}
		Label fieldLabelLbl = new Label("fieldLabel", labelModel);
		
		Panel dataValueEntryPanel;
		String fieldTypeName = pf.getFieldType().getName();
		String encodedValues = pf.getEncodedValues();
		Boolean requiredField = aCustomData.getPhenoDataSetFieldDisplay().getRequired();
		if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
			if(pf.getDefaultValue() != null && item.getModelObject().getDateDataValue() == null) {
				try {
					item.getModelObject().setDateDataValue(new SimpleDateFormat(Constants.DD_MM_YYYY).parse(pf.getDefaultValue()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			DateDataEntryPanel dateDataEntryPanel = new DateDataEntryPanel("dataValueEntryPanel", 
														new PropertyModel<Date>(item.getModel(), "dateDataValue"),
														new Model<String>(labelModel));
			dateDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
			dateDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "phenoDataSetFieldDisplay.phenoDataSetField.unitType.name"));

			if (pf.getMinValue() != null && !pf.getMinValue().isEmpty()) {
				IConverter<Date> dateConverter = dateDataEntryPanel.getDateConverter();
				try {
					Date minDate = (Date) dateConverter.convertToObject(pf.getMinValue(), getLocale());
					dateDataEntryPanel.addValidator(DateValidator.minimum(minDate, Constants.DD_MM_YYYY));
				}
				catch (ConversionException ce) {
					// This should not occur because it means the data is corrupted on the backend database
					getLog().error("Unexpected error: phenoDataSetField.minValue is not in the DD/MM/YYYY date format");
					this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
					getParentContainer().setEnabled(false);
				}
			}
			if (pf.getMaxValue() != null && !pf.getMaxValue().isEmpty()) {
				IConverter<Date> dateConverter = dateDataEntryPanel.getDateConverter();
				try {
					Date maxDate = (Date) dateConverter.convertToObject(pf.getMaxValue(), getLocale());
					dateDataEntryPanel.addValidator(DateValidator.maximum(maxDate, Constants.DD_MM_YYYY));
				}
				catch (ConversionException ce) {
					// This should not occur because it means the data is corrupted on the backend database
					getLog().error("Unexpected error: phenoDataSetField.maxValue is not in the DD/MM/YYYY date format");
					this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
					getParentContainer().setEnabled(false);
				}
			}
			if (requiredField != null && requiredField == true) {
				dateDataEntryPanel.setRequired(true);
			}
			dataValueEntryPanel = dateDataEntryPanel;
		}
		else {//ie if its not a date...
			if (encodedValues != null && !encodedValues.isEmpty()) {
				// The presence of encodedValues means it should be a DropDownChoice
				List<String> encodeKeyValueList = Arrays.asList(encodedValues.split(";"));
				ArrayList<EncodedValueVO> choiceList = new ArrayList<EncodedValueVO>();
				for (String keyValue : encodeKeyValueList) {
					// Only split for the first instance of the '=' (allows the '=' character in the actual value)
					String[] keyValueArray = keyValue.split("=", 2);
					EncodedValueVO encodedValueVo = new EncodedValueVO();
					encodedValueVo.setKey(keyValueArray[0]);
					encodedValueVo.setValue(keyValueArray[1]);
					choiceList.add(encodedValueVo);
				}				
				

				ChoiceRenderer<EncodedValueVO> choiceRenderer = new ChoiceRenderer<EncodedValueVO>("value", "key");
				
 				if(pfd.getAllowMultiselect()){

 					CheckGroupDataEntryPanel cgdePanel = new CheckGroupDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(item.getModel(), "textDataValue"), 
															new Model<String>(labelModel), choiceList, choiceRenderer); 
					
					cgdePanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					cgdePanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "phenoDataSetFieldDisplay.phenoDataSetField.unitType.name"));

					if (pf.getMissingValue() != null && !pf.getMissingValue().isEmpty()) {
						cgdePanel.setMissingValue(pf.getMissingValue());
					}
					if (requiredField != null && requiredField == true) {
						cgdePanel.setRequired(true);
					}
					
					dataValueEntryPanel = cgdePanel;

				}
				else{
					if(pf.getDefaultValue() != null && item.getModelObject().getTextDataValue() == null) {
						item.getModelObject().setTextDataValue(pf.getDefaultValue());
					}
					DropDownChoiceDataEntryPanel ddcPanel = 
								new DropDownChoiceDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(item.getModel(), "textDataValue"), 
																				new Model<String>(labelModel), choiceList, choiceRenderer);
					ddcPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					ddcPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "phenoDataSetFieldDisplay.phenoDataSetField.unitType.name"));
					
					if (pf.getMissingValue() != null && !pf.getMissingValue().isEmpty()) {
						ddcPanel.setMissingValue(pf.getMissingValue());
					}
					if (requiredField != null && requiredField == true) {
						ddcPanel.setRequired(true);
					}
					dataValueEntryPanel = ddcPanel;
				}
			}
			else {
				if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.CHARACTER_FIELD_TYPE_NAME)) {
					// Text data
					if(pf.getDefaultValue() != null && item.getModelObject().getTextDataValue() == null) {
						item.getModelObject().setTextDataValue(pf.getDefaultValue());
					}
					TextDataEntryPanel textDataEntryPanel = new TextDataEntryPanel("dataValueEntryPanel", 
																										new PropertyModel<String>(item.getModel(), "textDataValue"), 
																										new Model<String>(labelModel));
					textDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					textDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "phenoDataSetFieldDisplay.phenoDataSetField.unitType.name"));
					textDataEntryPanel.setTextFieldSize(60);
					
					if (requiredField != null && requiredField == true) {
						 textDataEntryPanel.setRequired(true);
					}
					dataValueEntryPanel = textDataEntryPanel;
				}
				else if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.NUMBER_FIELD_TYPE_NAME)) {
					// Number data
					if(pf.getDefaultValue() != null && item.getModelObject().getNumberDataValue() == null) {
						item.getModelObject().setNumberDataValue(Double.parseDouble(pf.getDefaultValue()));;
					}
					NumberDataEntryPanel numberDataEntryPanel = new NumberDataEntryPanel("dataValueEntryPanel", 
																						new PropertyModel<Double>(item.getModel(), "numberDataValue"), 
																						new Model<String>(labelModel));
					numberDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					numberDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "phenoDataSetFieldDisplay.phenoDataSetField.unitType.name"));
										
					if (pf.getMinValue() != null && !pf.getMinValue().isEmpty()) {
						IConverter<Double> doubleConverter = numberDataEntryPanel.getNumberConverter();
						try {
							Double minNumber = (Double) doubleConverter.convertToObject(pf.getMinValue(), getLocale());
							numberDataEntryPanel.addValidator(new MinimumValidator<Double>(minNumber));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							getLog().error("Unexpected error: phenoDataSetField.maxValue is not in a valid number format");
							this.error("An unexpected error occurred loading the field validators from database. Please contact your System Administrator.");
							getParentContainer().setEnabled(false);
						}
					}
					if (pf.getMaxValue() != null && !pf.getMaxValue().isEmpty()) {
						IConverter<Double> doubleConverter = numberDataEntryPanel.getNumberConverter();
						try {
							Double maxNumber = (Double) doubleConverter.convertToObject(pf.getMaxValue(), getLocale());
							numberDataEntryPanel.addValidator(new MaximumValidator<Double>(maxNumber));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							getLog().error("Unexpected error: phenoDataSetField.maxValue is not in a valid number format");
							this.error("An unexpected error occurred loading the field validators from database. Please contact your System Administrator.");
							getParentContainer().setEnabled(false);
						}
					}
					if (requiredField != null && requiredField == true) {
						numberDataEntryPanel.setRequired(true);
					}
					dataValueEntryPanel = numberDataEntryPanel;
				}
				else {
					// TODO: Unknown type should display an UnsupportedValueEntryPanel
					dataValueEntryPanel = new EmptyPanel("dataValueEntryPanel");
				}
			}
		}

		item.add(fieldLabelLbl);
		item.add(dataValueEntryPanel);
	}
	
	protected abstract WebMarkupContainer getParentContainer();
	
	protected abstract Logger getLog();
	
}