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
package au.org.theark.core.web.component.customfield.dataentry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.ICustomFieldData;

/**
 * CustomDataEditorDataView is designed to assist in rendering a customField Data entry panel
 * through its re-usable implementation of populateItem(..).  Make sure the super.populateItem
 * is called if you override the populateItem(..) method.
 * 
 * @author elam
 *
 * @param <T> specifies the underlying Entity/VO that stores the CustomField data 
 */
public abstract class CustomDataEditorDataView<T extends ICustomFieldData> extends DataView<T> {


	private static final long	serialVersionUID	= 1L;

	protected CustomDataEditorDataView(String id, IDataProvider<T> dataProvider) {
		super(id, dataProvider);
	}

	@Override
	protected void populateItem(Item<T> item) {
		ICustomFieldData aCustomData = item.getModelObject();
		CustomField cf = aCustomData.getCustomFieldDisplay().getCustomField();
		CustomFieldDisplay cfd = aCustomData.getCustomFieldDisplay();
		
		Label fieldLabelLbl; 
		if (cf.getFieldLabel() != null) {
			fieldLabelLbl = new Label("fieldLabel", cf.getFieldLabel());
		}
		else {
			// Defaults to name if no fieldLabel
			fieldLabelLbl = new Label("fieldLabel", cf.getName());
		}
		Panel dataValueEntryPanel;
		String fieldTypeName = cf.getFieldType().getName();
		String encodedValues = cf.getEncodedValues();
		Boolean requiredField = aCustomData.getCustomFieldDisplay().getRequired();
		if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
			DateDataEntryPanel dateDataEntryPanel = new DateDataEntryPanel("dataValueEntryPanel", 
														new PropertyModel<Date>(item.getModel(), "dateDataValue"),
														new Model<String>(cf.getFieldLabel()));
			dateDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
			dateDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "customFieldDisplay.customField.unitType.name"));

			if (cf.getMinValue() != null && !cf.getMinValue().isEmpty()) {
				IConverter<Date> dateConverter = dateDataEntryPanel.getDateConverter();
				try {
					Date minDate = (Date) dateConverter.convertToObject(cf.getMinValue(), getLocale());
					dateDataEntryPanel.addValidator(DateValidator.minimum(minDate, Constants.DD_MM_YYYY));
				}
				catch (ConversionException ce) {
					// This should not occur because it means the data is corrupted on the backend database
					getLog().error("Unexpected error: customfield.minValue is not in the DD/MM/YYYY date format");
					this.error("An unexpected error occurred loading the field validators from database.  Please contact your System Administrator.");
					getParentContainer().setEnabled(false);
				}
			}
			if (cf.getMaxValue() != null && !cf.getMaxValue().isEmpty()) {
				IConverter<Date> dateConverter = dateDataEntryPanel.getDateConverter();
				try {
					Date maxDate = (Date) dateConverter.convertToObject(cf.getMaxValue(), getLocale());
					dateDataEntryPanel.addValidator(DateValidator.maximum(maxDate, Constants.DD_MM_YYYY));
				}
				catch (ConversionException ce) {
					// This should not occur because it means the data is corrupted on the backend database
					getLog().error("Unexpected error: customfield.maxValue is not in the DD/MM/YYYY date format");
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
					String[] keyValueArray = keyValue.split("=");
					EncodedValueVO encodedValueVo = new EncodedValueVO();
					encodedValueVo.setKey(keyValueArray[0]);
					encodedValueVo.setValue(keyValueArray[1]);
					choiceList.add(encodedValueVo);
				}				
				

				ChoiceRenderer<EncodedValueVO> choiceRenderer = new ChoiceRenderer<EncodedValueVO>("value", "key");
				
 				if(cfd.getAllowMultiselect()){

 					CheckGroupDataEntryPanel cgdePanel = new CheckGroupDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(item.getModel(), "textDataValue"), 
															new Model<String>(cf.getFieldLabel()), choiceList, choiceRenderer); 
					
					cgdePanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					cgdePanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "customFieldDisplay.customField.unitType.name"));

					if (cf.getMissingValue() != null && !cf.getMissingValue().isEmpty()) {
						cgdePanel.setMissingValue(cf.getMissingValue());
					}
					if (requiredField != null && requiredField == true) {
						cgdePanel.setRequired(true);
					}
					
					dataValueEntryPanel = cgdePanel;

				}
				else{
					DropDownChoiceDataEntryPanel ddcPanel = 
								new DropDownChoiceDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(item.getModel(), "textDataValue"), 
																				new Model<String>(cf.getFieldLabel()), choiceList, choiceRenderer);
					ddcPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					ddcPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "customFieldDisplay.customField.unitType.name"));
					
					if (cf.getMissingValue() != null && !cf.getMissingValue().isEmpty()) {
						ddcPanel.setMissingValue(cf.getMissingValue());
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
					TextDataEntryPanel textDataEntryPanel = new TextDataEntryPanel("dataValueEntryPanel", 
																										new PropertyModel<String>(item.getModel(), "textDataValue"), 
																										new Model<String>(cf.getFieldLabel()));
					textDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					textDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "customFieldDisplay.customField.unitType.name"));
										
					if (requiredField != null && requiredField == true) {
						 textDataEntryPanel.setRequired(true);
					}
					dataValueEntryPanel = textDataEntryPanel;
				}
				else if (fieldTypeName.equals(au.org.theark.core.web.component.customfield.Constants.NUMBER_FIELD_TYPE_NAME)) {
					// Number data
					NumberDataEntryPanel numberDataEntryPanel = new NumberDataEntryPanel("dataValueEntryPanel", 
																						new PropertyModel<Double>(item.getModel(), "numberDataValue"), 
																						new Model<String>(cf.getFieldLabel()));
					numberDataEntryPanel.setErrorDataValueModel(new PropertyModel<String>(item.getModel(), "errorDataValue"));
					numberDataEntryPanel.setUnitsLabelModel(new PropertyModel<String>(item.getModel(), "customFieldDisplay.customField.unitType.name"));
										
					if (cf.getMinValue() != null && !cf.getMinValue().isEmpty()) {
						IConverter<Double> doubleConverter = numberDataEntryPanel.getNumberConverter();
						try {
							Double minNumber = (Double) doubleConverter.convertToObject(cf.getMinValue(), getLocale());
							numberDataEntryPanel.addValidator(new MinimumValidator<Double>(minNumber));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							getLog().error("Unexpected error: customfield.maxValue is not in a valid number format");
							this.error("An unexpected error occurred loading the field validators from database. Please contact your System Administrator.");
							getParentContainer().setEnabled(false);
						}
					}
					if (cf.getMaxValue() != null && !cf.getMaxValue().isEmpty()) {
						IConverter<Double> doubleConverter = numberDataEntryPanel.getNumberConverter();
						try {
							Double maxNumber = (Double) doubleConverter.convertToObject(cf.getMaxValue(), getLocale());
							numberDataEntryPanel.addValidator(new MaximumValidator<Double>(maxNumber));
						}
						catch (ConversionException ce) {
							// This should not occur because it means the data is corrupted on the backend database
							getLog().error("Unexpected error: customfield.maxValue is not in a valid number format");
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