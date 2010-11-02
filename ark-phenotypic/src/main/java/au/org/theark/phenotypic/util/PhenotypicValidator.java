package au.org.theark.phenotypic.util;

import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.entity.Collection;
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
	
	public boolean isValidFieldData(FieldData fieldData)
	{
		Field field = (Field)fieldData.getField();
		log.info("Validating field data:" + field.getName().toString() + "\t" + fieldData.getValue().toString());
		
		if(field.getfieldType().getName().equalsIgnoreCase("NUMBER")){
			log.info("Field is a NUMBER");
			log.info("Field Min value: [" + field.getMinValue().toString() + "]");
			log.info("Field Max value: [" + field.getMaxValue().toString() + "]");
			
			Float floatMinValue = Float.parseFloat(field.getMinValue());
			Float floatMaxValue = Float.parseFloat(field.getMaxValue());
			Float floatFieldValue = Float.parseFloat(fieldData.getValue());
			
			if(floatFieldValue > floatMaxValue){
				log.error("Field data greater than defined max range:" + fieldData.getField().getMaxValue().toString() + "\t" + fieldData.getValue().toString());
			}
			if(floatFieldValue < floatMinValue){
				log.error("Field data less than defined min range:" + fieldData.getField().getMinValue().toString() + "\t" + fieldData.getValue().toString());
			}
		}
		return true;
	}
}
