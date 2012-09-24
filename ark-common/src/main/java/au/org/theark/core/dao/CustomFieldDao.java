package au.org.theark.core.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.util.CsvListReader;
import au.org.theark.core.web.component.customfield.Constants;

@Repository("customFieldDao")
public class CustomFieldDao extends HibernateSessionDao implements ICustomFieldDao {

	static Logger		log	= LoggerFactory.getLogger(CustomFieldDao.class);

	public CustomField getFieldByNameAndStudyAndFunction(String fieldName, Study study, ArkFunction arkFunction) throws EntityNotFoundException {
		CustomField field = null;
		Criteria criteria = getSession().createCriteria(CustomField.class);
		if (fieldName != null && study != null && arkFunction != null) {
			criteria.add(Restrictions.eq("name", fieldName));
			criteria.add(Restrictions.eq("study", study));
			criteria.add(Restrictions.eq("arkFunction", arkFunction));
		}
		
		field = (CustomField) criteria.uniqueResult();	// should not have more than on field called the same name
		if (field == null) {
			throw new EntityNotFoundException();
		}

		return field;
	}

	public FieldType getFieldTypeByName(String typeName) throws EntityNotFoundException {
		FieldType fieldType = null;
		Criteria criteria = getSession().createCriteria(FieldType.class);
		criteria.add(Restrictions.eq("name", typeName!=null?typeName.toUpperCase():typeName));

		fieldType = (FieldType) criteria.uniqueResult();	// should not have more than on field called the same name
		if (fieldType == null) {
			log.error("Field Type Table maybe out of synch. Please check if it has an entry for " + typeName + " status");
			log.error("Cannot locate a field type with " + typeName + " in the database");
			throw new EntityNotFoundException();
		}
		return fieldType;
	}

	protected Criteria buildGeneralCustomFieldCritera(CustomField customField) {
		Criteria criteria = getSession().createCriteria(CustomField.class);
		// Must be constrained on study and function
		criteria.add(Restrictions.eq("study", customField.getStudy()));
		
		criteria.add(Restrictions.eq("arkFunction", customField.getArkFunction()));
		
		if (customField.getId() != null) {
			criteria.add(Restrictions.eq("id", customField.getId()));
		}
	
		if (customField.getName() != null) {
			criteria.add(Restrictions.ilike("name", customField.getName(), MatchMode.ANYWHERE));
		}
		
		if (customField.getFieldType() != null) {
			criteria.add(Restrictions.eq("fieldType", customField.getFieldType()));
		}
	
		if (customField.getDescription() != null) {
			criteria.add(Restrictions.ilike("description", customField.getDescription(), MatchMode.ANYWHERE));
		}
	
		if (customField.getUnitType() != null && customField.getUnitType().getName() != null) {
			criteria.createAlias("unitType", "ut");
			criteria.add(Restrictions.ilike("ut.name", customField.getUnitType().getName(), MatchMode.ANYWHERE));
		}
	
		if (customField.getMinValue() != null) {
			criteria.add(Restrictions.ilike("minValue", customField.getMinValue(), MatchMode.ANYWHERE));
		}
	
		if (customField.getMaxValue() != null) {
			criteria.add(Restrictions.ilike("maxValue", customField.getMaxValue(), MatchMode.ANYWHERE));
		}
	
		return criteria;
	}

	protected Criteria buildGeneralUnitTypeCriteria(UnitType unitTypeCriteria) {
		
		Criteria criteria = getSession().createCriteria(UnitType.class);
		// Bring back units that are module-specific as well as the global unit types (null FK)
		if (unitTypeCriteria.getArkFunction() != null) {
			criteria.add(Restrictions.or(Restrictions.isNull("arkFunction"), Restrictions.eq("arkFunction", unitTypeCriteria.getArkFunction())));
		}
		else {
			criteria.add(Restrictions.isNull("arkFunction"));
		}
		if (unitTypeCriteria.getName() != null) {
			criteria.add(Restrictions.ilike("name", unitTypeCriteria.getName(), MatchMode.START));
		}
		criteria.addOrder(Order.asc("measurementType.id")); //is hardcoding all of this as I see throughout the code the best way?  I guess a DAO is as close to db code as we allow at least
		criteria.addOrder(Order.asc("displayOrder")); //is hardcoding all of this as I see throughout the code the best way?  I guess a DAO is as close to db code as we allow at least
		return criteria;
	}

	private Criteria buildGenericCustomFieldGroupCriteria(CustomFieldGroup customFieldGroup){
		
		Criteria criteria = getSession().createCriteria(CustomFieldGroup.class);
		
		criteria.add(Restrictions.eq("study", customFieldGroup.getStudy()));
		criteria.add(Restrictions.eq("arkFunction", customFieldGroup.getArkFunction()));
		
		if (customFieldGroup.getName() != null) {
			criteria.add(Restrictions.ilike("name", customFieldGroup.getName(), MatchMode.ANYWHERE));
		}
		
		if(customFieldGroup.getPublished() != null){
			criteria.add(Restrictions.eq("published", customFieldGroup.getPublished()));
		}
		return criteria;
		
	}

	public void createCustomField(CustomField customField) throws  ArkSystemException{
		if(customField.getFieldType().getName().equals(Constants.NUMBER_FIELD_TYPE_NAME)) {
			if(customField.getMinValue()!=null){
				customField.setMinValue(customField.getMinValue().replace(",",""));				
			}
			if(customField.getMaxValue()!=null){
				customField.setMaxValue(customField.getMaxValue().replace(",",""));
			}
		}
		getSession().save(customField);
	}

	public void createCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) throws  ArkSystemException{
		getSession().save(customFieldDisplay);
	}

	public void deleteCustomDisplayField(CustomFieldDisplay customFieldDisplay) throws ArkSystemException{
		getSession().delete(customFieldDisplay);
	}

	public void deleteCustomField(CustomField customField) throws ArkSystemException{
		getSession().delete(customField);
	}

	public CustomField getCustomField(Long id ){
		Criteria criteria = getSession().createCriteria(CustomField.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.setMaxResults(1);
		return (CustomField)criteria.uniqueResult();
	}

	public long getCustomFieldCount(CustomField customFieldCriteria) {
		// Handle for study or function not in context
		if (customFieldCriteria.getStudy() == null || customFieldCriteria.getArkFunction() == null) {
			return 0;
		}
		Criteria criteria = buildGeneralCustomFieldCritera(customFieldCriteria);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}

	public CustomFieldDisplay getCustomFieldDisplay(Long id ){
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.setMaxResults(1);
		return (CustomFieldDisplay)criteria.uniqueResult();
	}

	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria){
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.add(Restrictions.eq("customField.id", cfCriteria.getId()));
		criteria.setMaxResults(1);
		return (CustomFieldDisplay)criteria.uniqueResult();
	}

	public long getCustomFieldGroupCount(CustomFieldGroup customFieldGroup) {
		// Handle for study or function not in context
		if (customFieldGroup.getStudy() == null || customFieldGroup.getArkFunction() == null) {
			return 0L;
		}
		Criteria criteria = buildGenericCustomFieldGroupCriteria(customFieldGroup);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}

	@SuppressWarnings("unchecked")
	public List<CustomFieldGroup> getCustomFieldGroups(CustomFieldGroup customFieldGroup, int first, int count){
	
		Criteria criteria = buildGenericCustomFieldGroupCriteria(customFieldGroup);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<CustomFieldGroup> list = (List<CustomFieldGroup>)criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CustomField> getCustomFieldList(CustomField customFieldCriteria){
		Criteria criteria = buildGeneralCustomFieldCritera(customFieldCriteria);
		// Return fields ordered alphabetically
		criteria.addOrder(Order.asc("name"));
		List<CustomField> customFieldList = (List<CustomField>) criteria.list();
		//log.warn("custom field criteria (just using name got a list of size " + customFieldList.size());
		return customFieldList;
	}

	public FieldType getFieldTypeById(Long id){
		Criteria criteria = getSession().createCriteria(FieldType.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.setMaxResults(1);
		return (FieldType)criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<FieldType> getFieldTypes() {
		Criteria criteria = getSession().createCriteria(FieldType.class);
		List<FieldType> customFieldTypeList = (List<FieldType>) criteria.list();
		return customFieldTypeList;
	}

	@SuppressWarnings("unchecked")
	public List<String> getUnitTypeNames(UnitType unitTypeCriteria, int maxResults) {
		Criteria criteria = buildGeneralUnitTypeCriteria(unitTypeCriteria);
		if (maxResults > 0) {
			// Only restrict results if the parameter is greater than 0 (i.e. can use 0 for unconstrained).
			criteria.setMaxResults(maxResults);
		}
		criteria.setProjection(Projections.property("name"));
		List<String> customFieldUnitTypeNames = criteria.list();
		return customFieldUnitTypeNames;
	}

	@SuppressWarnings("unchecked")
	public List<UnitType> getUnitTypes(UnitType unitTypeCriteria) {
		Criteria criteria = buildGeneralUnitTypeCriteria(unitTypeCriteria);
		List<UnitType> results = criteria.list();
		return results;
	}

	public boolean isCustomFieldUnqiue(String customFieldName, Study study, CustomField customFieldToUpdate){
		boolean isUnique = true;
		StatelessSession stateLessSession = getStatelessSession();
		Criteria criteria = stateLessSession.createCriteria(CustomField.class);
		criteria.add(Restrictions.eq("name", customFieldName));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", customFieldToUpdate.getArkFunction()));
		criteria.setMaxResults(1);
		
		CustomField existingField = (CustomField) criteria.uniqueResult();
		
		if( (customFieldToUpdate.getId() != null && customFieldToUpdate.getId() > 0)){
			
			if(existingField != null && !customFieldToUpdate.getId().equals(existingField.getId())){
				isUnique = false;
			}
		}else{
			if(existingField != null){
				isUnique = false;
			}
		}
		
		stateLessSession.close();
		return isUnique;
	}

	@SuppressWarnings("unchecked")
	public List<CustomField> searchPageableCustomFields(CustomField customFieldCriteria, int first, int count) {
		Criteria criteria = buildGeneralCustomFieldCritera(customFieldCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		// Return fields ordered alphabetically
		criteria.addOrder(Order.asc("name"));
		List<CustomField> customFieldList = (List<CustomField>) criteria.list();
		return customFieldList;
	}


	public ArkFunction getArkFunctionByName(String functionName) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("name", functionName));
		criteria.setMaxResults(1);
		ArkFunction arkFunction = (ArkFunction) criteria.uniqueResult();
		return arkFunction;
	}
	
	public List<CustomField> searchPageableCustomFieldsForPheno(CustomField customFieldCriteria, int first, int count) {
		customFieldCriteria.setArkFunction(getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));
		return searchPageableCustomFields(customFieldCriteria, first, count);
	}

	public void updateCustomField(CustomField customField) throws  ArkSystemException{
		if(customField.getFieldType().getName().equals(Constants.NUMBER_FIELD_TYPE_NAME)) {
			if(customField.getMinValue()!=null){
				customField.setMinValue(customField.getMinValue().replace(",",""));				
			}
			if(customField.getMaxValue()!=null){
				customField.setMaxValue(customField.getMaxValue().replace(",",""));
			}
		}
		getSession().update(customField);
	}

	public void updateCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) throws  ArkSystemException{
		if(!customFieldDisplay.getRequired()){
			customFieldDisplay.setRequiredMessage(null);
		}
		getSession().update(customFieldDisplay);
	}

	public CustomField getCustomFieldByNameStudyArkFunction(String customFieldName, Study study, ArkFunction arkFunction) {
		log.info("name" +  "\nstudy" + study.getId() + "\narkFunc="+ arkFunction.getId());
		Criteria criteria = getSession().createCriteria(CustomField.class);
		criteria.add(Restrictions.eq("name", customFieldName));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		CustomField result = (CustomField) criteria.uniqueResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public CustomField getCustomFieldByNameStudyCFG(String customFieldName, Study study, ArkFunction arkFunction, CustomFieldGroup customFieldGroup){

		Query q = getSession().createQuery("Select customField from CustomField customField " +
											" where customField.name =:customFieldName " +
											" and lower(customField.study) =lower(:study) " +
											" and customField.arkFunction =:arkFunction " +
											" and exists (" +
											"				from CustomFieldDisplay as customFieldDisplay " +
											"				where customFieldDisplay.customField = customField " +
											"				and customFieldDisplay.customFieldGroup =:customFieldGroup ) ");
		q.setParameter("customFieldName", customFieldName);
		q.setParameter("study", study);
		q.setParameter("arkFunction", arkFunction);
		q.setParameter("customFieldGroup", customFieldGroup);
		
		List<CustomField> results = q.list();
		if(results.size()>0){
			return (CustomField)results.get(0);
		}
		return null;
	}
	
	public UnitType getUnitTypeByNameAndArkFunction(String name, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(UnitType.class);
		// UnitType name should be sufficient to return only 1 row (i.e. uniqueness at the global and arkFunction-specific levels)
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.or(Restrictions.isNull("arkFunction"), Restrictions.eq("arkFunction", arkFunction)));
		UnitType result = (UnitType) criteria.uniqueResult();
		return result;
	}
	
	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria, CustomFieldGroup customFieldGroup){
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.add(Restrictions.eq("customField.id", cfCriteria.getId()));
		criteria.add(Restrictions.eq("customFieldGroup",customFieldGroup));
		criteria.setMaxResults(1);
		return (CustomFieldDisplay)criteria.uniqueResult();
	}
	
	public List<CustomField> matchCustomFieldsFromInputFile(FileUpload fileUpload, Study study, ArkFunction arkFunction) {
		List<CustomField> customFieldList = new ArrayList<CustomField>();
		List<String> customFieldNameList = new ArrayList<String>(0);

		try {
			customFieldNameList = CsvListReader.readColumnIntoList(fileUpload.getInputStream());
		}
		catch (IOException e) {
			log.error("Error in Custom Field list file");
			return customFieldList;
		}

		// Loop through list to get order
		for (String name : customFieldNameList) {
			Criteria criteria = getSession().createCriteria(CustomField.class);
			criteria.add(Restrictions.eq("study", study));
			criteria.add(Restrictions.eq("name", name));
			criteria.add(Restrictions.eq("arkFunction", arkFunction));
			customFieldList.add((CustomField) criteria.uniqueResult());
		}
		
		return customFieldList;
	}

	@Override
	public void convertLimsBiocollectionCustomDataValuesToKeysForThisStudy(Study study) {
		/*StringBu
		stringBuffer2.append(" SELECT cfd FROM  CustomFieldDisplay cfd  " );
		stringBuffer2.append(" WHERE " );
			//	"cfd.customField.study = :study ");
		//stringBuffer.append(" AND ");
		stringBuffer2.append(" cfd.customField.encodedValues is not null and cfd.customField.encodedValues != :emptyString ");
		*/		StringBuffer stringBuffer = new StringBuffer();
				
				stringBuffer.append(" SELECT bscfd FROM BioCollectionCustomFieldData bscfd ");
				stringBuffer.append(" WHERE bscfd.customFieldDisplay in ");
				stringBuffer.append(" ( ");               
					stringBuffer.append(" SELECT cfd FROM  CustomFieldDisplay cfd  " );
					stringBuffer.append(" WHERE cfd.customField.study = :study ");
					stringBuffer.append(" AND ");
					stringBuffer.append(" cfd.customField.encodedValues is not null and cfd.customField.encodedValues != :emptyString ");
					stringBuffer.append(" )");
				
				String theHQLQuery = stringBuffer.toString(); 
				
				Query query = getSession().createQuery(theHQLQuery);
				query.setParameter("emptyString", "");
				query.setParameter("study", study);
				List<BioCollectionCustomFieldData> bscfds = query.list();
				log.info("\n\n\n\n\n\n\n\n\n\nbiocol size = " + bscfds.size());
				for (BioCollectionCustomFieldData bscfd : bscfds){
					//bscfd.getCustomFieldDisplay().getCustomField().getEncodedValues();
					log.info("text =" +bscfd.getTextDataValue());
					if(!isInEncodedValues(bscfd.getCustomFieldDisplay().getCustomField(), bscfd.getTextDataValue())){
						log.info( bscfd.getTextDataValue() + " not in enc values " + bscfd.getCustomFieldDisplay().getCustomField().getEncodedValues());
					}
					else{
						log.info( bscfd.getTextDataValue() + " is in enc values " + bscfd.getCustomFieldDisplay().getCustomField().getEncodedValues());
					}
				}
	}
	
	public static boolean isInEncodedValues(CustomField customField, String value) {
		if(customField.getMissingValue()!=null && value!=null && value.trim().equalsIgnoreCase(customField.getMissingValue().trim())) {
			return true;
		}

		// Validate if encoded values is definedisInEncodedValues, and not a DATE fieldType
		if (customField != null && !customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {

			try {
				StringTokenizer stringTokenizer = new StringTokenizer(customField.getEncodedValues(), Constants.ENCODED_VALUES_TOKEN);

				// Iterate through all discrete defined values and compare to field data value
				while (stringTokenizer.hasMoreTokens()) {
					String encodedValueToken = stringTokenizer.nextToken();
					StringTokenizer encodedValueSeparator = new StringTokenizer(encodedValueToken, Constants.ENCODED_VALUES_SEPARATOR);
					String encodedValue = encodedValueSeparator.nextToken().trim();

					if (encodedValue.equalsIgnoreCase(value)) {
						return true;
					}
				}

			}
			catch (NullPointerException npe) {
				log.error("Field data null format exception " + npe.getMessage());
				return false;
			}

		}
		return false;
	}


	@Override
	public void convertLimsBiospecimenCustomDataValuesToKeysForThisStudy(Study study) {
		/*StringBu
		stringBuffer2.append(" SELECT cfd FROM  CustomFieldDisplay cfd  " );
		stringBuffer2.append(" WHERE " );
			//	"cfd.customField.study = :study ");
		//stringBuffer.append(" AND ");
		stringBuffer2.append(" cfd.customField.encodedValues is not null and cfd.customField.encodedValues != :emptyString ");
	*/		StringBuffer stringBuffer = new StringBuffer();
			
			stringBuffer.append(" SELECT bscfd FROM BiospecimenCustomFieldData bscfd ");
			stringBuffer.append(" WHERE bscfd.customFieldDisplay in ");
			stringBuffer.append(" ( ");               
				stringBuffer.append(" SELECT cfd FROM  CustomFieldDisplay cfd  " );
				stringBuffer.append(" WHERE cfd.customField.study = :study ");
				stringBuffer.append(" AND ");
				stringBuffer.append(" cfd.customField.encodedValues is not null and cfd.customField.encodedValues != :emptyString ");
				stringBuffer.append(" )");
			
			String theHQLQuery = stringBuffer.toString(); 
			
			Query query = getSession().createQuery(theHQLQuery);
			query.setParameter("emptyString", "");
			query.setParameter("study", study);
			List<BiospecimenCustomFieldData> bscfds = query.list();
			log.info("\n\n\n\n\n\n\n\n\n\nbiospec size = " + bscfds.size());
			for (BiospecimenCustomFieldData bscfd : bscfds){

				log.info("text =" +bscfd.getTextDataValue());
				if(!isInEncodedValues(bscfd.getCustomFieldDisplay().getCustomField(), bscfd.getTextDataValue())){
					log.info( bscfd.getTextDataValue() + " not in enc values " + bscfd.getCustomFieldDisplay().getCustomField().getEncodedValues());
				}
				else{
					log.info( bscfd.getTextDataValue() + " is in enc values " + bscfd.getCustomFieldDisplay().getCustomField().getEncodedValues());
				}
			}
	}
	
}
