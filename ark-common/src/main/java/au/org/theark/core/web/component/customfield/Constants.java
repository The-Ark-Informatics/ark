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
package au.org.theark.core.web.component.customfield;

public class Constants {

	//TODO make all of these constants firstly look in common?
	public static final String		FUNCTION_KEY_VALUE_PHENO_COLLECTION							= "PHENO_COLLECTION";
	public static final String		FUNCTION_KEY_VALUE_DATA_DICTIONARY							= "DATA_DICTIONARY";
	
	


	public static final String FIELD_TYPE_NUMBER = "NUMBER";
	public static final String FIELD_TYPE_CHARACTER = "CHARACTER";
	public static final String FIELD_TYPE_DATE = "DATE";
	public static final String DISCRETE_RANGE_TOKEN = ",";
	public static final String ENCODED_VALUES_TOKEN = ";";
	public static final String ENCODED_VALUES_FROM_TELEFORMS_TOKEN_SPACE = " ";
	public static final String ENCODED_VALUES_SEPARATOR = "=";
	
	public static final String		CHARACTER_FIELD_TYPE_NAME									= "CHARACTER";
	public static final String		DATE_FIELD_TYPE_NAME											= "DATE";
	public static final String		NUMBER_FIELD_TYPE_NAME										= "NUMBER";

	public static final String		CUSTOMFIELD 													= "customField";
	public static final String		CUSTOMFIELD_ID													= "id";
	public static final String		CUSTOMFIELD_STUDY												= "study";
	public static final String		CUSTOMFIELD_FIELD_TYPE										= "fieldType";
	public static final String		CUSTOMFIELD_NAME												= "name";
	public static final String		CUSTOMFIELD_DESCRIPTION										= "description";
	public static final String		CUSTOMFIELD_FIELD_LABEL										= "fieldLabel";
	public static final String		CUSTOMFIELD_UNIT_TYPE										= "unitType";
	public static final String		CUSTOMFIELD_MIN_VALUE										= "minValue";
	public static final String		CUSTOMFIELD_MAX_VALUE										= "maxValue";
	public static final String		CUSTOMFIELD_HAS_DATA											= "customFieldHasData";
	
	public static final String		FIELDTYPE 														= "fieldType";
	public static final String		FIELDTYPE_ID 													= "id";
	public static final String		FIELDTYPE_NAME 												= "name";
	
	public static final String		UNITTYPE 														= "unitType";
	public static final String		UNITTYPE_ID 													= "id";
	public static final String		UNITTYPE_NAME 													= "name";
	public static final String		UNITTYPE_DESCRIPTION											= "description";
	public static final String		UNITTYPE_ARK_MODULE 											= "arkModule";
	
 	public static final String		CUSTOMFIELDDISPLAY											= "customFieldDisplay";
	public static final String		CUSTOMFIELDDISPLAY_ID										= "id";
	public static final String		CUSTOMFIELDDISPLAY_CUSTOM_FIELD							= "customField";
	public static final String		CUSTOMFIELDDISPLAY_CUSTOM_FIELD_GROUP					= "customFieldGroup";
 	public static final String		CUSTOMFIELDDISPLAY_SEQUENCE								= "sequence";
 	public static final String		CUSTOMFIELDDISPLAY_REQUIRED								= "required";
 	public static final String		CUSTOMFIELDDISPLAY_REQUIRED_MSG							= "requiredMessage";

 	public static final String		CUSTOMFIELDGROUP												= "customFieldGroup";
 	public static final String		CUSTOMFIELDGROUP_ID											= "id";
 	public static final String		CUSTOMFIELDGROUP_NAME										= "name";
 	public static final String		CUSTOMFIELDGROUP_DESCRIPTION								= "description";

	public static final String		FIELDVO_CUSTOMFIELD_ID										= "customField.id";
	public static final String		FIELDVO_CUSTOMFIELD_STUDY									= "customField.study";
	public static final String		FIELDVO_CUSTOMFIELD_FIELD_TYPE							= "customField.fieldType";
	public static final String		FIELDVO_CUSTOMFIELD_NAME									= "customField.name";
	public static final String		FIELDVO_CUSTOMFIELD_DESCRIPTION							= "customField.description";
	public static final String		FIELDVO_CUSTOMFIELD_FIELD_LABEL							= "customField.fieldLabel";
	public static final String		FIELDVO_CUSTOMFIELD_UNIT_TYPE								= "customField.unitType";
	public static final String		FIELDVO_CUSTOMFIELD_SEQ_NUM								= "customField.seqNum";
	public static final String		FIELDVO_CUSTOMFIELD_MIN_VALUE								= "customField.minValue";
	public static final String		FIELDVO_CUSTOMFIELD_MAX_VALUE								= "customField.maxValue";
	public static final String		FIELDVO_CUSTOMFIELD_ENCODED_VALUES						= "customField.encodedValues";
	public static final String		FIELDVO_CUSTOMFIELD_MISSING_VALUE						= "customField.missingValue";
	public static final String		FIELDVO_CUSTOMFIELD_ALLOW_MULTISELECT						= "customFieldDisplay.allowMultiselect";
	
	public static final String		FIELDVO_CUSTOMFIELDDISPLAY_ID								= "customFieldDisplay.id";
	public static final String		FIELDVO_CUSTOMFIELDDISPLAY_CUSTOM_FIELD				= "customFieldDisplay.customField";
	public static final String		FIELDVO_CUSTOMFIELDDISPLAY_SEQUENCE						= "customFieldDisplay.sequence";
	public static final String		FIELDVO_CUSTOMFIELDDISPLAY_REQUIRED						= "customFieldDisplay.required";
	public static final String		FIELDVO_CUSTOMFIELDDISPLAY_REQUIRED_MSG				= "customFieldDisplay.requiredMessage";
	public static final String		FIELDVO_CUSTOMFIELDDISPLAY_FIELD_GROUP					= "customFieldDisplay.customFieldGroup";
	

}
