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

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;

/**
 * TextField based Data entry panel for numbers
 * @author elam
 * @author cellis
 */
public class NumberDataEntryPanel extends AbstractDataEntryPanel<Double> {
	
	private static final long	serialVersionUID	= 1L;
	protected TextField<Double> dataValueTxtFld;

	/**
	 * NumberDataEntryPanel Constructor
	 * @param id - component id
	 * @param dataModel - must be a model Double dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 */
	public NumberDataEntryPanel(String id, IModel<Double> dataModel, IModel<String> labelModel) {
		super(id, labelModel);
		dataValueModel = dataModel;
				
		dataValueTxtFld = new TextField<Double>("numberDataValue", dataValueModel, Double.class);
		dataValueTxtFld.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		this.add(dataValueTxtFld);
	}
	
	public IConverter getNumberConverter() {
		return dataValueTxtFld.getConverter(dataValueTxtFld.getType());
	}

	@Override
	public void setRequired(boolean required) {
		dataValueTxtFld.setRequired(required);
	}
	
	@Override
	public void addValidator(IValidator<Double> aValidator) {
		dataValueTxtFld.add(aValidator);
	}

	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.NUMBER;
	}
}