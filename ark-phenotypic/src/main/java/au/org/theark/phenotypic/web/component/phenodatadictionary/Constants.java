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
package au.org.theark.phenotypic.web.component.phenodatadictionary;

public class Constants {
	public static final String		FUNCTION_KEY_VALUE_DATA_DICTIONARY							= "DATA_DICTIONARY";
	public static final String 		FIELD_TYPE_NUMBER 											= "Number";
	public static final String 		FIELD_TYPE_CHARACTER 										= "Character";
	public static final String 		FIELD_TYPE_DATE 											= "Date";
	public static final String 		DISCRETE_RANGE_TOKEN 										= ",";
	public static final String 		ENCODED_VALUES_TOKEN 										= ";";
	public static final String 		ENCODED_VALUES_FROM_TELEFORMS_TOKEN_SPACE 					= " ";
	public static final String 		ENCODED_VALUES_SEPARATOR 									= "=";
	public static final String		LOOKUP_FIELD_TYPE_NAME										= "LookUp";
	public static final String		PHENODATASET 												= "phenoDataSet";
	public static final String		PHENODATASET_ID												= "id";
	public static final String		PHENODATASET_STUDY											= "study";
	public static final String		PHENODATASET_FIELD_TYPE										= "fieldType";
	public static final String		PHENODATASET_NAME											= "name";
	public static final String		PHENODATASET_DESCRIPTION									= "description";
	public static final String		PHENODATASET_FIELD_LABEL									= "fieldLabel";
	public static final String		PHENODATASET_CUSTOME_FIELD_TYPE								= "phenoDataSetType";
	public static final String		PHENODATASET_UNIT_TYPE										= "unitType";
	public static final String		PHENODATASET_DEFAULT_VALUE									= "defaultValue";
	public static final String		PHENODATASET_MISSING_VALUE									= "missingValue";
	public static final String		PHENODATASET_MIN_VALUE										= "minValue";
	public static final String		PHENODATASET_MAX_VALUE										= "maxValue";
	public static final String		PHENODATASET_HAS_DATA										= "phenoDataSetHasData";
	public static final String		PHENODATASET_REQUIRED										= "required";
	public static final String		PHENODATASET_ALLOW_MULTIPLE_SELECTION						= "allowMultiselect";
	public static final String		FIELDTYPE 													= "fieldType";
	public static final String		FIELDTYPE_ID 												= "id";
	public static final String		FIELDTYPE_NAME 												= "name";
	public static final String		UNITTYPE 													= "unitType";
	public static final String		UNITTYPE_ID 												= "id";
	public static final String		UNITTYPE_NAME 												= "name";
	public static final String		UNITTYPE_DESCRIPTION										= "description";
	public static final String		UNITTYPE_ARK_MODULE 										= "arkModule";
	public static final String		PHENODATASETDISPLAY											= "phenoDataSetDisplay";
	public static final String		PHENODATASETDISPLAY_ID										= "id";
	public static final String		PHENODATASETDISPLAY_CUSTOM_FIELD							= "phenoDataSet";
	public static final String		PHENODATASETDISPLAY_CUSTOM_FIELD_GROUP				  		= "phenoDataSetGroup";
 	public static final String		PHENODATASETDISPLAY_SEQUENCE								= "sequence";
 	public static final String		PHENODATASETDISPLAY_REQUIRED								= "required";
 	public static final String		PHENODATASETDISPLAY_REQUIRED_MSG							= "requiredMessage";
 	public static final String		PHENODATASETGROUP											= "phenoDataSetGroup";
 	public static final String		PHENODATASETGROUP_ID										= "id";
 	public static final String		PHENODATASETGROUP_NAME										= "name";
 	public static final String		PHENODATASETGROUP_DESCRIPTION								= "description";
	public static final String		FIELDVO_PHENODATASET_ID										= "phenoDataSetField.id";
	public static final String		FIELDVO_PHENODATASET_STUDY									= "phenoDataSetField.study";
	public static final String		FIELDVO_PHENODATASET_FIELD_TYPE								= "phenoDataSetField.fieldType";
	public static final String		FIELDVO_PHENODATASET_NAME									= "phenoDataSetField.name";
	public static final String		FIELDVO_PHENODATASET_DESCRIPTION							= "phenoDataSetField.description";
	public static final String		FIELDVO_PHENODATASET_FIELD_LABEL							= "phenoDataSetField.fieldLabel";
	public static final String		FIELDVO_PHENODATASET_UNIT_TYPE								= "phenoDataSetField.unitType";
	public static final String		FIELDVO_PHENODATASET_REQUIRED								= "phenoDataSetField.required";
	public static final String		FIELDVO_PHENODATASET_ALLOW_MULTIPLE_SELECTION				= "phenoDataSetField.allowMultiselect";
	public static final String		FIELDVO_PHENODATASET_DEFAULT_VALUE							= "phenoDataSetField.defaultValue";
	//Add unit type as text
	public static final String		FIELDVO_PHENODATASET_UNIT_TYPE_TXT						= "phenoDataSetField.unitTypeInText";
	public static final String		FIELDVO_PHENODATASET_SEQ_NUM							= "phenoDataSetField.seqNum";
	public static final String		FIELDVO_PHENODATASET_MIN_VALUE							= "phenoDataSetField.minValue";
	public static final String		FIELDVO_PHENODATASET_MAX_VALUE							= "phenoDataSetField.maxValue";
	public static final String		FIELDVO_PHENODATASET_ENCODED_VALUES						= "phenoDataSetField.encodedValues";
	public static final String		FIELDVO_PHENODATASET_MISSING_VALUE						= "phenoDataSetField.missingValue";
	
	public static final String		FIELDVO_PHENODATASETDISPLAY_ID							= "phenoDataSetFieldDisplay.id";
	public static final String		FIELDVO_PHENODATASETDISPLAY_CUSTOM_FIELD				= "phenoDataSetFieldDisplay.phenoDataSetField";
	public static final String		FIELDVO_PHENODATASETDISPLAY_SEQUENCE					= "phenoDataSetFieldDisplay.sequence";
	public static final String		FIELDVO_PHENODATASETDISPLAY_REQUIRED					= "phenoDataSetFieldDisplay.required";
	public static final String		FIELDVO_PHENODATASETDISPLAY_REQUIRED_MSG				= "phenoDataSetFieldDisplay.requiredMessage";
	public static final String		FIELDVO_PHENODATASETDISPLAY_FIELD_GROUP					= "phenoDataSetFieldDisplay.phenoDataSetFieldGroup";
	//Add new variables-2015-07-20
	public static final String      CUSTOM_FIELD_TYPE_NAME									="name";
	public static final String		CUSTOM_FIELD_TYPE_ID									="id";
	public static final String 		PHENODATASETCATEGORY_NAME								="name";
	public static final String 		PHENODATASETCATEGORY_ID									="id";
	public static final int 		PHENO_CATEGORY_PALETTE_ROWS 							= 10;
	// Services
	public static final String	PHENOTYPIC_SERVICE				= "phenotypicService";

}
