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

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.customfield.CustomFieldContainerPanel;

public abstract class AbstractDataEntryPanel<T> extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private static final Logger log = LoggerFactory.getLogger(AbstractDataEntryPanel.class);

	protected IModel<T> dataValueModel;
	protected IModel<String> errorDataLabelModel;
	protected IModel<String> fieldLabelModel;
	protected Label errorValueLbl;
	
	private static final String DATE_VALIDATION_ERROR_RSRC_KEY = "validationError.dateType";
	private static final String NUMBER_VALIDATION_ERROR_RSRC_KEY = "validationError.numberType";
	private static final String TEXT_VALIDATION_ERROR_RSRC_KEY = "validationError.textType";
	private static final String DROPDOWN_VALIDATION_ERROR_RSRC_KEY = "validationError.dropDownType";
	
	public enum DataEntryType {
		TEXT, DROPDOWN, NUMBER, DATE
	}

	public AbstractDataEntryPanel(String id, IModel<String> labelModel) {
		super(id);

		if (labelModel != null) {
			fieldLabelModel = labelModel;
		}
		else {
			fieldLabelModel = new Model<String>("(unspecified label for date field)");
		}
		
		errorDataLabelModel = new Model<String>("");
		errorValueLbl = new Label("errorValueLabel", errorDataLabelModel);
		errorValueLbl.setOutputMarkupId(true);
		
		this.add(errorValueLbl);
	}

	public void setErrorDataValueModel(IModel<String> errorDataStringModel) {
		if (errorDataStringModel != null 
				&& errorDataStringModel.getObject() != null && !errorDataStringModel.getObject().isEmpty()) {
			errorDataLabelModel = new StringResourceModel(getValidationErrorResourceKey(), this, 
																		null, new Object[] { errorDataStringModel.getObject() });
		}
		else {
			errorDataLabelModel = new Model<String>("");
		}
		errorValueLbl.setDefaultModel(errorDataLabelModel);
	}

	private String getValidationErrorResourceKey() {
		String typeRsrcKey = null;
		switch(getDataEntryType()) {
			case TEXT : typeRsrcKey = TEXT_VALIDATION_ERROR_RSRC_KEY;
							break;
			case NUMBER : typeRsrcKey = NUMBER_VALIDATION_ERROR_RSRC_KEY;
							break;
			case DATE : typeRsrcKey = DATE_VALIDATION_ERROR_RSRC_KEY;
							break;
			case DROPDOWN : typeRsrcKey = DROPDOWN_VALIDATION_ERROR_RSRC_KEY;
							break;
			default : log.error("Internal error: Code did not map all DataEntryType enums to resource keys");
							typeRsrcKey = TEXT_VALIDATION_ERROR_RSRC_KEY;	//use least restrictive type by default
							break;
		}
		return typeRsrcKey;
	}
	
	abstract public void setRequired(boolean required);

	abstract public void addValidator(IValidator<T> aValidator);
	
	/**
	 * Override this to provide the error label with a type name to display
	 */
	abstract protected DataEntryType getDataEntryType();
	
}

