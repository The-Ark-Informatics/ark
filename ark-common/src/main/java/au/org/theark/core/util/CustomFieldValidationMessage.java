package au.org.theark.core.util;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.model.study.entity.CustomField;

/**
 * @author nivedann
 *
 */
public class CustomFieldValidationMessage {
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
	 * Returns field not of the defined type error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataNotDefinedType(CustomField field, FieldData fieldData) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Subject UID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is not the defined field type: ");
		stringBuffer.append(field.getFieldType().getName());
		return (stringBuffer.toString());
	}

	/**
	 * Returns field greater than defined max value error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataGreaterThanMaxValue(CustomField field, FieldData fieldData) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Subject UID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is greater than the defined maximum value: ");
		stringBuffer.append(field.getMaxValue());
		return (stringBuffer.toString());
	}

	/**
	 * Returns field less than defined min value error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataLessThanMinValue(CustomField field, FieldData fieldData) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Subject UID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is less than the defined minimum value: ");
		stringBuffer.append(field.getMinValue());
		return (stringBuffer.toString());
	}

	/**
	 * Returns field not within the defined encoded values error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataNotInEncodedValues(CustomField field, FieldData fieldData) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Subject UID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is not in the encoded values: ");
		stringBuffer.append(field.getEncodedValues().replace('\n', ' '));
		return (stringBuffer.toString());
	}

	/**
	 * Returns field not a valid date format error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataNotValidDate(CustomField field, FieldData fieldData) {
		stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("Subject UID: ");
		stringBuffer.append(fieldData.getLinkSubjectStudy().getSubjectUID());
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(fieldData.getValue().toString());
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
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

	public static String fieldMinValueNotDefinedType(CustomField field) {
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

	public static String fieldMaxValueNotDefinedType(CustomField field) {
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

	public static String fieldMissingValueNotDefinedType(CustomField field) {
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

	public static String fieldDefinitionMinValueNotValidDate(CustomField field) {
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

	public static String fieldDefinitionMaxValueNotValidDate(CustomField field) {
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

	public static String fieldDefinitionMissingValueNotValidDate(CustomField field) {
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

}
