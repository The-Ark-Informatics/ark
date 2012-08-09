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
import java.util.List;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidator;

/**
 * Data entry panel for multiplecheckboxes
 * @author travis
 * 
 * NB: Important assumptions that apply to this class:
 * + If missingValue is set non-null, then the dataValueDdc will become required automatically 
 *    (if you wish to reverse this, then you must setRequired(false) manually).
 * + The dataValue should not be null when the missingValue has been set non-null.  So it is 
 * 	expected that the dataValue should default to match the missingValue for new records.
 * + Only the displayed choices (with their internal encoded "key" mapping) are needed, so it 
 *    relies an external conversion process for the CustomField's encodeValue attribute.
 */
public class CheckGroupDataEntryPanel extends AbstractDataEntryPanel<ArrayList<EncodedValueVO>> {
	
	private static final long	serialVersionUID	= 1L;
	
	protected IModel<String> underlyingDataModel;
	protected CheckBoxMultipleChoice<EncodedValueVO>	checkBoxMultipleChoice;
	protected EncodedValueVO missingValueVo;

	/**
	 * NumberDataEntryPanel Constructor
	 * @param id - component id
	 * @param dataModel - must be a model for a String dataValue
	 * @param labelModel - field-specific String label model to be used for feedback
	 * @param allChoicesList - list of choices for the Dropdown
	 */
	public CheckGroupDataEntryPanel(String id, IModel<String> dataModel, IModel<String> labelModel, 
													final ArrayList<EncodedValueVO> allChoicesList, ChoiceRenderer<EncodedValueVO> renderer) {
		super(id, labelModel);
		missingValueVo = null;
		underlyingDataModel = dataModel;
//		dataValueModel = new Model<ArrayList<EncodedValueVO>>(selectedList); 
		dataValueModel = new Model<ArrayList<EncodedValueVO>>(){
			private static final long serialVersionUID = 1L;
			
			public ArrayList<EncodedValueVO> getObject() {
				
				String textDataValue = underlyingDataModel.getObject();
				ArrayList selectedEvvos = new ArrayList<EncodedValueVO>();
				if(textDataValue != null && !textDataValue.isEmpty()){
					List<String> encodeKeyValueList = Arrays.asList(textDataValue.split(";"));
					for (String keyValue : encodeKeyValueList) {
							//				if(key is in the list of our keys in allPossibilities)) 		then add it to out list to return			
						for(EncodedValueVO evvo : allChoicesList){
							if(keyValue.equalsIgnoreCase(evvo.getKey())){
								selectedEvvos.add(evvo);
							}
						}
					}		
				}
				return selectedEvvos;
			}
			
			public void setObject(ArrayList<EncodedValueVO> object) {
				String keyConcat = "";
				if (object == null || object.isEmpty()) {
					underlyingDataModel.setObject(null);
				}
				else {
					for(EncodedValueVO evvo : object){
						keyConcat += evvo.getKey() + ";"; 
					}
					underlyingDataModel.setObject(keyConcat);
				}
			}

		};
		
		checkBoxMultipleChoice = new CheckBoxMultipleChoice("checkGroupDataValue", dataValueModel, allChoicesList, renderer);
		//checkBoxMultipleChoice.setNullValid(true);	// nullValid allows you to set the "Choose One" option
		checkBoxMultipleChoice.setLabel(fieldLabelModel);	// set the ${label} for feedback messages
		this.add(checkBoxMultipleChoice);
	}

	public String getMissingValue() {
		// can be used to determine if the missingValue was set ok by comparing getMissingValue() with
		// the attempted setMissingValue(..)
		return missingValueVo.getKey();
	}

	//TODO ASAP Look into this
	@SuppressWarnings("unchecked")
	public void setMissingValue(String aMissingValue) {
		if (this.missingValueVo != null) {
			// try to remove the original missingValue from choices
			List<EncodedValueVO> choiceList = (List<EncodedValueVO>) checkBoxMultipleChoice.getChoices();
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
			List<EncodedValueVO> choiceList = (List<EncodedValueVO>) checkBoxMultipleChoice.getChoices();
			choiceList.add(newMissingValueVo);	// add new missingValue to choices
			
			// Is field really required when missing vlaue not-null? 
			//setRequired(true);	// make the field required and !nullValid
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
		/*if (this.missingValueVo != null) {
			// Load the text for the option "missing value" from resource properties
			String option = getLocalizer().getStringIgnoreSettings(checkBoxMultipleChoice.getId() + ".missingValue", this, null, null);
			if (Strings.isEmpty(option))
			{
				option = getLocalizer().getString("missingValue", this, "(missing value)");
			}
			this.missingValueVo.setValue(option);
		}*/
		super.onBeforeRender();
	}

	@Override
	public void setRequired(boolean required) {
		checkBoxMultipleChoice.setRequired(required);
		//dataValueDdc.setNullValid(!required);	// if required is true, then it can not be nullValid
	}
	
	@Override
	public void addValidator(IValidator<ArrayList<EncodedValueVO>> aValidator) {
		//log.info("we dont have a validator");
		//checkBoxMultipleChoice.add(aValidator);
	}
 
	@Override
	protected DataEntryType getDataEntryType() {
		return DataEntryType.CHECKGROUP;
	}


}
