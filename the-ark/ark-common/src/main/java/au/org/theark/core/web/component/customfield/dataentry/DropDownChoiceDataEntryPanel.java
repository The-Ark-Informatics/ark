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

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidator;

/**
 * TextField based Data entry panel for numbers
 * @author elam
 * 
 * NB: Important assumptions that apply to this class:
 * + If missingValue is set non-null, then the dataValueDdc will become required automatically 
 *    (if you wish to reverse this, then you must setRequired(false) manually).
 * + The dataValue should not be null when the missingValue has been set non-null.  So it is 
 * 	expected that the dataValue should default to match the missingValue for new records.
 * + Only the displayed choices (with their internal encoded "key" mapping) are needed, so it 
 *    relies an external conversion process for the CustomField's encodeValue attribute.
 */
public class DropDownChoiceDataEntryPanel extends AbstractDataEntryPanel<EncodedValueVO> {
	
	private static final long	serialVersionUID	= 1L;
	
	protected IModel<String> underlyingDataModel;
	protected DropDownChoice<EncodedValueVO>	dataValueDdc;
	protected EncodedValueVO missingValueVo;

	/**
	 * NumberDataEntryPanel Constructor
	 * @param id - component id
	 * @param dataModel - must be a model for a String dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 * @param choiceList - list of choices for the Dropdown
	 * @param renderer - ChoiceRenderer for the Dropdown
	 */
	public DropDownChoiceDataEntryPanel(String id, IModel<String> dataModel, IModel<String> labelModel, 
													List<EncodedValueVO> choiceList, ChoiceRenderer<EncodedValueVO> renderer) {
		super(id, labelModel);
		missingValueVo = null;
		underlyingDataModel = dataModel;
		// Slightly tricky mapping from the EncodedVO's key to the underlying dataValue (i.e. a String) 
		dataValueModel = new IModel<EncodedValueVO>() {

			public EncodedValueVO getObject() {
				EncodedValueVO object = null;
				if (underlyingDataModel.getObject() != null) {
					object = new EncodedValueVO();
					object.setKey(underlyingDataModel.getObject());
				}
				return object;
			}

			public void setObject(EncodedValueVO object) {
				if (object == null) {
					underlyingDataModel.setObject(null);
				}
				else {
					underlyingDataModel.setObject(object.getKey());
				}
			}

			public void detach() {
			}
			
		};
		dataValueDdc = new DropDownChoice<EncodedValueVO>("ddcDataValue", dataValueModel, choiceList, renderer);
		dataValueDdc.setNullValid(true);	// nullValid allows you to set the "(no value)" option
		dataValueDdc.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		this.add(dataValueDdc);
	}

	public String getMissingValue() {
		// can be used to determine if the missingValue was set ok by comparing getMissingValue() with
		// the attempted setMissingValue(..)
		return missingValueVo.getKey();
	}

	public void setMissingValue(String aMissingValue) {

		if (this.missingValueVo != null) {
			// try to remove the original missingValue from choices
			List<EncodedValueVO> choiceList = (List<EncodedValueVO>) dataValueDdc.getChoices();
			boolean removeSuccess = choiceList.remove(this.missingValueVo);
			if (removeSuccess) {
				this.missingValueVo = null;	// nullify the missingValue after removal
			}
		}
		if ((aMissingValue != null) && (this.missingValueVo == null)) {
			EncodedValueVO newMissingValueVo = new EncodedValueVO();
			newMissingValueVo.setKey(aMissingValue);
				
			// since arg is non-null, then add it to the choices (NB: it will not be able to
			// replace the previous missingValue if the removal failed) 
			List<EncodedValueVO> choiceList = (List<EncodedValueVO>) dataValueDdc.getChoices();
			choiceList.add(newMissingValueVo);	// add new missingValue to choices
			setRequired(true);	// make the field required and !nullValid
			this.missingValueVo = newMissingValueVo;
		}
	}
	
	@Override
	protected void onBeforeRender() {
		/*
		 * Can only call #getString(..) after adding the component to the page, otherwise it will cause 
		 * a warning regardless of StringResourceModel().getString() or getLocalizer().getString(). 
		 * Example warning is below:
		 *  WARN  - Localizer                  - Tried to retrieve a localized string for a component that has not 
		 *  yet been added to the page. This can sometimes lead to an invalid or no localized resource returned. 
		 *  Make sure you are not calling Component#getString() inside your Component's constructor. 
		 *  Offending component: [MarkupContainer [Component id = dataValueEntryPanel]]
		 */
		if (this.missingValueVo != null) {
			// Load the text for the option "missing value" from resource properties
			String option = getLocalizer().getStringIgnoreSettings(dataValueDdc.getId() + ".missingValue", this, null, null);
			if (Strings.isEmpty(option))
			{
				option = getLocalizer().getString("missingValue", this, "(missing value)");
			}
			this.missingValueVo.setValue(option);
		}
		super.onBeforeRender();
	}

	@Override
	public void setRequired(boolean required) {
		dataValueDdc.setRequired(required);
		dataValueDdc.setNullValid(!required);	// if required is true, then it can not be nullValid
	}
	
	@Override
	public void addValidator(IValidator<EncodedValueVO> aValidator) {
		dataValueDdc.add(aValidator);
	}

	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.DROPDOWN;
	}

}
