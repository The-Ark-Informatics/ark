package au.org.theark.phenotypic.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import au.org.theark.core.Constants;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PhenotypicValidator provides support for validating phenotypic data with the defined data dictionary
 * 
 * @author cellis
 */
@SuppressWarnings("unused")
public class PhenotypicValidator
{
	static Logger				log						= LoggerFactory.getLogger(PhenotypicValidator.class);
	
	/**
	 * PhenotypicValidator constructor
	 * 
	 */
	public PhenotypicValidator()
	{
	}
	
	/**
	 * Returns true of the fiald data value is a valid format, either NUMBER, CHARACTER or DATE as specified in the data dictionary
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isValidFieldData(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		Field field = (Field)fieldData.getField();
		//errorMessages.add("Validating field data: " + field.getName().toString() + "\t" + fieldData.getValue().toString());
		
		// Number field type
		if(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)){
			try{
				Float floatFieldValue = Float.parseFloat(fieldData.getValue());
				return true;
			}
			catch(NumberFormatException nfe){
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotDefinedType(field, fieldData));
				log.error("Field data number format exception " + nfe.getMessage());
				return false;
			}
			catch(NullPointerException npe){
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
		}
		
		// Character field type
		if(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)){
			try{
				String stringFieldValue = fieldData.getValue();
				return true;
			}
			catch(NullPointerException npe){
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
		}
		
		// Date field type
		if(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)){
			try{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFieldValue = dateFormat.parse(fieldData.getValue());
				return true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotDefinedType(field, fieldData));
				log.error("Field data date parse exception " + pe.getMessage());
				return false;
			}
			catch(NullPointerException npe){
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Returns true if field data value is within the defined range as specified in the data dictionary
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInValidRange(FieldData fieldData, java.util.Collection<String> errorMessages){
		Field field = (Field)fieldData.getField();
		
		if(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)){
			try{
				Float floatMinValue = Float.parseFloat(field.getMinValue());
				Float floatMaxValue = Float.parseFloat(field.getMaxValue());
				Float floatFieldValue = Float.parseFloat(fieldData.getValue());
				
				if((floatFieldValue > floatMaxValue) || (floatFieldValue < floatMinValue)){
					if((floatFieldValue > floatMaxValue)){
						errorMessages.add(PhenotypicValidationMessage.fieldDataGreaterThanMaxValue(field, fieldData));
					}
					if((floatFieldValue < floatMinValue)){
						errorMessages.add(PhenotypicValidationMessage.fieldDataLessThanMaxValue(field, fieldData));
					}
					return false;
				}
				return true;
			}
			catch(NumberFormatException nfe){
				log.error("Field data number format exception " + nfe.getMessage());
				return false;
			}
			catch(NullPointerException npe){
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
			
		}
		return false;
	}
	
	/**
	 * Returns true if the field data value is within the discrete range as defined in the data dictionary
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInDiscreteValues(FieldData fieldData, java.util.Collection<String> errorMessages){
		boolean inDiscreteValues = false;
		
		Field field = (Field)fieldData.getField();
		
		// Validate if discrete values is defined
		if(field.getDiscreteValues() != null){
			try{
				StringTokenizer stringTokenizer = new StringTokenizer(field.getDiscreteValues(), Constants.DISCRETE_RANGE_TOKEN); 
				
				// Iterate through all discrete defined values and compare to field data value
				while(stringTokenizer.hasMoreTokens()) {
					String stringToken = stringTokenizer.nextToken();
					if(stringToken.equalsIgnoreCase(fieldData.getValue())){
						inDiscreteValues = true;
						break;
					}
					else{
						inDiscreteValues = false;
					}
				} 
				
				if(!inDiscreteValues){
					errorMessages.add(PhenotypicValidationMessage.fieldDataNotInDiscreteValues(field, fieldData));
				}
				
			}
			catch(NullPointerException npe){
				log.error("Field data null format exception " + npe.getMessage());
				inDiscreteValues = false;
			}
			
		}
		return inDiscreteValues;
	}
	
	public static void validateFieldData(FieldData fieldData, java.util.Collection<String> errorMessages){
		// Validate the field data
		if(isValidFieldData(fieldData,errorMessages)){
			//log.info("Field data valid");
		}
		
		if(isInDiscreteValues(fieldData,errorMessages)){
			//log.info("Field data in discrete values");
		}
		
		// Validate the field data within discrete values
		if(isInValidRange(fieldData,errorMessages)){
			//log.info("Field data in valid range");
		}
	}
}
