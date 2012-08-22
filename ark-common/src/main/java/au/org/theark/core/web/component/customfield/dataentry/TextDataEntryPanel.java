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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;

/**
 * TextField based Data entry panel for text
 * @author elam
 * @author cellis
 */
public class TextDataEntryPanel extends AbstractDataEntryPanel<String> {

	private static final long		serialVersionUID	= 1L;
	protected TextField<String>	dataValueTxtFld;

	/**
	 * TextDataEntryPanel Constructor
	 * 
	 * @param id
	 *           - component id
	 * @param dataModel
	 *           - must be a model a String dataValue
	 * @param labelModel
	 *           - field-specific String label model to be used for feedback
	 */
	public TextDataEntryPanel(String id, IModel<String> dataModel, IModel<String> labelModel) {
		super(id, labelModel);
		dataValueModel = dataModel;

		dataValueTxtFld = new TextField<String>("textDataValue", dataValueModel);
		dataValueTxtFld.setLabel(fieldLabelModel); // set the ${label} for feedback messages
		this.add(dataValueTxtFld);
	}

	@Override
	public void setRequired(boolean required) {
		dataValueTxtFld.setRequired(required);
	}

	@Override
	public void addValidator(IValidator<String> aValidator) {
		dataValueTxtFld.add(aValidator);
	}

	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.TEXT;
	}
	
	/**
	 * Set the text field size
	 * @param size
	 */
	public void setTextFieldSize(final int size){
		this.dataValueTxtFld.add(new AttributeModifier("size",size));
	}
}