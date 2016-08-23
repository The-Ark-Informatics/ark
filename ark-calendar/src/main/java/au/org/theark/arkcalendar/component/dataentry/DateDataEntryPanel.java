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
package au.org.theark.arkcalendar.component.dataentry;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import com.googlecode.wicket.kendo.ui.form.datetime.DateTimePicker;


/**
 * DateTextField based Data entry panel for dates
 * 
 * @author elam
 * @author cellis
 */
public class DateDataEntryPanel extends AbstractDataEntryPanel<Date> {

	private static final long	serialVersionUID	= 1L;
	
	public static final String DD_MM_YYYY = "dd/MM/yyyy";

	protected DateTextField		dataValueDateFld;

	/**
	 * DateDataEntryPanel Constructor
	 * 
	 * @param id
	 *           - component id
	 * @param dateDataModel
	 *           - must be a model for a Date dataValue
	 * @param labelModel
	 *           - field-specific String label model to be used for feedback
	 */

	public DateDataEntryPanel(String id, IModel<Date> dateDataModel, IModel<String> labelModel) {
		super(id, labelModel);
		dataValueModel = dateDataModel;

//		dataValueDateFld = new DateTextField("dateDataValue", dataValueModel,DateDataEntryPanel.DD_MM_YYYY);
//		DatePicker datePicker = new DatePicker();
//		datePicker.bind(dataValueDateFld);
//		dataValueDateFld.setLabel(fieldLabelModel); // set the ${label} for feedback messages
//		dataValueDateFld.add(datePicker);
		
		
//		DateTimePicker dp = new DateTimePicker("dateDataValue", dateDataModel);
//		dp.setTimePickerEnabled(false);
//		
//		this.add(dp);
		
//		DatePicker datePicker = new DatePicker("dateDataValue",dataValueModel,DateDataEntryPanel.DD_MM_YYYY, new Options());
		DatePicker datePicker = new DatePicker("dateDataValue",dataValueModel);
		
		this.add(datePicker);
		
//		this.add(dataValueDateFld);
	}

	public DateTextField getDataValueDateFld() {
		return dataValueDateFld;
	}

	public IConverter<Date> getDateConverter() {
		return dataValueDateFld.getConverter(dataValueDateFld.getType());
	}

	@Override
	public void setRequired(boolean required) {
		dataValueDateFld.setRequired(required);
	}

	@Override
	public void addValidator(IValidator<Date> aValidator) {
		dataValueDateFld.add(aValidator);
	}

	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.DATE;
	}
	
}