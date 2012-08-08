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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base Abstract class that all custom data entry sub-classes extend.
 * @author elam
 * @author nivedan
 * @author cellis
 *
 * @param <T>
 */
public abstract class AbstractDataEntryPanel<T> extends Panel {


	private static final long		serialVersionUID							= 1L;

	private static final Logger	log											= LoggerFactory.getLogger(AbstractDataEntryPanel.class);

	protected IModel<T>				dataValueModel;
	protected IModel<String>		errorDataLabelModel;
	protected IModel<String>		fieldLabelModel;
	protected IModel<String>		unitsLabelModel;
	protected Label					errorValueLbl;
	protected Label					unitsLbl;

	private static final String	DATE_VALIDATION_ERROR_RSRC_KEY		= "validationError.dateType";
	private static final String	NUMBER_VALIDATION_ERROR_RSRC_KEY		= "validationError.numberType";
	private static final String	TEXT_VALIDATION_ERROR_RSRC_KEY		= "validationError.textType";
	private static final String	DROPDOWN_VALIDATION_ERROR_RSRC_KEY	= "validationError.dropDownType";
	private static final String	CHECKGROUP_VALIDATION_ERROR_RSRC_KEY	= "validationError.checkGroupType";

	public enum DataEntryType {
		TEXT, DROPDOWN, NUMBER, DATE, CHECKGROUP
	}

	public AbstractDataEntryPanel(String id, IModel<String> labelModel) {
		super(id);

		if (labelModel != null) {
			fieldLabelModel = labelModel;
		}
		else {
			fieldLabelModel = new Model<String>("(unspecified label for data field)");
		}

		errorDataLabelModel = new Model<String>("");
		errorValueLbl = new Label("errorValueLabel", errorDataLabelModel);
		errorValueLbl.setOutputMarkupId(true);

		this.add(errorValueLbl);

		unitsLabelModel = new Model<String>("");
		unitsLbl = new Label("unitsLabel", unitsLabelModel);
		unitsLbl.setOutputMarkupId(true);

		this.add(unitsLbl);
	}

	/**
	 * Sets the model on the errorValueLbl
	 * @param errorDataStringModel
	 */
	public void setErrorDataValueModel(IModel<String> errorDataStringModel) {
		if (errorDataStringModel != null && errorDataStringModel.getObject() != null && !errorDataStringModel.getObject().isEmpty()) {
			errorDataLabelModel = new StringResourceModel(getValidationErrorResourceKey(), this, null, new Object[] { errorDataStringModel.getObject() });
		}
		else {
			errorDataLabelModel = new Model<String>("");
		}
		errorValueLbl.setDefaultModel(errorDataLabelModel);
	}

	/**
	 * Sets the model on the unitsLbl
	 * @param unitsStringModel
	 */
	public void setUnitsLabelModel(IModel<String> unitsStringModel) {
		if (unitsStringModel != null) {
			unitsLabelModel = unitsStringModel;
		}
		else {
			unitsLabelModel = new Model<String>("");
		}
		unitsLbl.setDefaultModel(unitsLabelModel);
	}

	private String getValidationErrorResourceKey() {
		String typeRsrcKey = null;
		switch (getDataEntryType()) {
			case TEXT:
				typeRsrcKey = TEXT_VALIDATION_ERROR_RSRC_KEY;
				break;
			case NUMBER:
				typeRsrcKey = NUMBER_VALIDATION_ERROR_RSRC_KEY;
				break;
			case DATE:
				typeRsrcKey = DATE_VALIDATION_ERROR_RSRC_KEY;
				break;
			case DROPDOWN:
				typeRsrcKey = DROPDOWN_VALIDATION_ERROR_RSRC_KEY;
				break;
			case CHECKGROUP:
				typeRsrcKey = CHECKGROUP_VALIDATION_ERROR_RSRC_KEY;
				break;
			default:
				log.error("Internal error: Code did not map all DataEntryType enums to resource keys");
				typeRsrcKey = TEXT_VALIDATION_ERROR_RSRC_KEY; // use least restrictive type by default
				break;
		}
		return typeRsrcKey;
	}

	/**
	 * Override this method to determine if custom field is required
	 * @param required
	 */
	abstract public void setRequired(boolean required);

	/**
	 * Override this method to add validation to the custom field 
	 * @param aValidator
	 */
	abstract public void addValidator(IValidator<T> aValidator);

	/**
	 * Override this method to provide the error label with a type name to display
	 * @return the DataEntryType
	 */
	abstract protected DataEntryType getDataEntryType();
}