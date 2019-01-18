package au.org.theark.phenotypic.util;

import java.util.Date;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.study.entity.CustomField;

/**
 * @author nivedann
 *
 */
public class PhenoDataSetFieldValidationMessage {
	private static StringBuffer	stringBuffer	= null;

	/**
	 * Returns field not of the defined type error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataSubjectUidNotFound(String subjectUid) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Subject UID: ");
		stringBuffer.append(subjectUid);
		stringBuffer.append(" ");
		stringBuffer.append("was not found in the database. Please check and try again.");
		return (stringBuffer.toString());
	}

	/**
	 * Returns dateCollected not a valid date format error message
	 * 
	 * @param subkjecUid
	 * @param dateCollectedStr
	 * @return String
	 */
	public static String dateCollectedNotValidDate(String subjectUid, String dateCollectedStr) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("Subject UID: ");
		stringBuffer.append(subjectUid);
		stringBuffer.append(": ");
		stringBuffer.append(" with the date collected: ");
		stringBuffer.append(dateCollectedStr);
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
		return (stringBuffer.toString());
	}

	public static String fieldMinValueNotDefinedType(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" minimum value ");
		stringBuffer.append(field.getMinValue().toString());
		stringBuffer.append(" is not the defined format of: ");
		stringBuffer.append(field.getFieldType().getName());
		return (stringBuffer.toString());
	}

	public static String fieldMaxValueNotDefinedType(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" maximum value ");
		stringBuffer.append(field.getMinValue().toString());
		stringBuffer.append(" is not the defined format of: ");
		stringBuffer.append(field.getFieldType().getName());
		return (stringBuffer.toString());
	}

	public static String fieldMissingValueNotDefinedType(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" missing value ");
		stringBuffer.append(field.getMissingValue().toString());
		stringBuffer.append(" is not the defined format of: ");
		stringBuffer.append(field.getFieldType().getName());
		return (stringBuffer.toString());
	}

	public static String fieldDefinitionMinValueNotValidDate(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" minimum value ");
		stringBuffer.append(field.getMinValue().toString());
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
		return (stringBuffer.toString());
	}

	public static String fieldDefinitionMaxValueNotValidDate(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" minimum value ");
		stringBuffer.append(field.getMinValue().toString());
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
		return (stringBuffer.toString());
	}

	public static String fieldDefinitionMissingValueNotValidDate(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" missing value ");
		stringBuffer.append(field.getMinValue().toString());
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
		return (stringBuffer.toString());
	}

	public static String fieldTypeNotDefined(String fieldName, String fieldType) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" with field type of ");
		stringBuffer.append(fieldType);
		stringBuffer.append(" is not in the valid type of: ");
		stringBuffer.append(Constants.FIELD_TYPE_CHARACTER);
		stringBuffer.append(", ");
		stringBuffer.append(Constants.FIELD_TYPE_NUMBER);
		stringBuffer.append(", or ");
		stringBuffer.append(Constants.FIELD_TYPE_DATE);
		return (stringBuffer.toString());
	}

	public static String fieldTypeIsNotCharacterWithEncodedValue(String fieldName, String fieldType) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" with field type of ");
		stringBuffer.append(fieldType);
		stringBuffer.append(" should not have an associate ENCODED_VALUES");
		return (stringBuffer.toString());
	}

	public static String fieldNotFound(String fieldName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" was not found in the database. Please check the name and try again, or add the field to the Data Dictionary.");
		return (stringBuffer.toString());
	}

	public static String nonConformingEncodedValue(String fieldName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" does not conform with the required format. ");
		stringBuffer.append("Example format: internalCode_NoSpaces1=Displayed choice (spaces allowed) 1;internalCode_NoSpaces2=Displayed choice (spaces allowed) 2;\n");
		stringBuffer.append("*NB: Please remember to end each encoding with ';', i.e. even the last one");
		return (stringBuffer.toString());
	}
	
	public static String nonConformingAllowMultipleSelect(String fieldName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" requires a valid ENCODED_VALUES value. ");
		return (stringBuffer.toString());
	}

	public static String invalidOption(String fieldName, String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" has an invalid option for the column ");
		stringBuffer.append(columnName);
		return (stringBuffer.toString());
	}
	public static String invalidCustomFieldType(String fieldName, String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" has an invalid custom field type for the column ");
		stringBuffer.append(columnName);
		return (stringBuffer.toString());
	}
	public static String invalidCategory(String fieldName, String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" has an invalid category for the column.");
		stringBuffer.append(columnName+"\n");
		stringBuffer.append("Please check again category is under your entered custom field Type");
		return (stringBuffer.toString());
	}
	public static String invalidCustomFieldTypeForCategory(String categoryName, String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The category ");
		stringBuffer.append(categoryName);
		stringBuffer.append(" has an invalid custom field type for the column ");
		stringBuffer.append(columnName);
		return (stringBuffer.toString());
	}
	public static String invalidParentCategryForCategory(String categoryName, String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The category ");
		stringBuffer.append(categoryName);
		stringBuffer.append(" has an invalid parent Category for the column ");
		stringBuffer.append(columnName);
		return (stringBuffer.toString());
	}
	public static String invalidOrderNumberForCategory(String categoryName, String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The category ");
		stringBuffer.append(categoryName);
		stringBuffer.append(" has an invalid order number for the column ");
		stringBuffer.append(columnName);
		return (stringBuffer.toString());
	}
	public static String fieldDefaultValueNotDefinedType(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" default value ");
		stringBuffer.append(field.getDefaultValue().toString());
		stringBuffer.append(" is not the defined format of: ");
		stringBuffer.append(field.getFieldType().getName());
		return (stringBuffer.toString());
	}
	public static String fieldDefaultValueNotINEncodedLst(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" default value ");
		stringBuffer.append(field.getDefaultValue().toString());
		stringBuffer.append(" is not in the encorded list.");
		return (stringBuffer.toString());
	}
	public static String fieldDefaultValueInsideMinAndMaxRange(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" default value ");
		stringBuffer.append(field.getDefaultValue().toString());
		stringBuffer.append(" is not in side the range of min:"+field.getMinValue()+"and max"+field.getMaxValue());
		return (stringBuffer.toString());
	}
	public static String fieldDefaultDateInsideMinAndMaxRange(PhenoDataSetField field,Date defDate,Date minDate,Date maxDate) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" default date ");
		stringBuffer.append(defDate);
		stringBuffer.append(" is not in side the range of min date:"+minDate+"and max date"+maxDate);
		return (stringBuffer.toString());
	}
	public static String fieldDefinitionDefaultValueNotValidDate(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" default value ");
		stringBuffer.append(field.getDefaultValue().toString());
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
		return (stringBuffer.toString());
	}
	public static String fieldDefinitionDefaultValueValidatingNull(PhenoDataSetField field) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" default value ");
		stringBuffer.append(field.getDefaultValue().toString());
		stringBuffer.append(" have an issue when validating.");
		return (stringBuffer.toString());
	}
	public static String nonMultiLineAllowWithEncodedValues(String fieldName,String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field  ");
		stringBuffer.append(fieldName);
		stringBuffer.append(", If correct encoded values contains in the field ");
		stringBuffer.append(columnName);
		stringBuffer.append(" must not accept data when the field type is character type.");
		return (stringBuffer.toString());
	}
	public static String nonChatacterFieldTypeMultiLineNotAccepted(String fieldName,String columnName) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field  ");
		stringBuffer.append(fieldName);
		stringBuffer.append(", when field type is NOT a character type  ");
		stringBuffer.append(columnName);
		stringBuffer.append(" is not allowed any value.");
		return (stringBuffer.toString());
	}

}
