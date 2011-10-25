package au.org.theark.core.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;

public interface ICustomFieldDao {

	CustomField getFieldByNameAndStudyAndFunction(String fieldName, Study study, ArkFunction arkFunction) throws EntityNotFoundException;

	FieldType getFieldTypeByName(String typeName) throws EntityNotFoundException;

	/**
	 * Create a CustomField
	 * @param customField
	 * @throws ArkSystemException
	 */
	public void createCustomField(CustomField customField) throws  ArkSystemException;

	/**
	 * Create a CustomFieldDisplay
	 * @param customFieldDisplay
	 * @throws ArkSystemException
	 */
	public void createCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) throws  ArkSystemException;

	/**
	 * Delete a CustomFieldDisplay
	 * @param customFieldDisplay
	 * @throws ArkSystemException
	 */
	public void deleteCustomDisplayField(CustomFieldDisplay customFieldDisplay) throws ArkSystemException;

	/**
	 * Delete a CustomField
	 * @param customField
	 * @throws ArkSystemException
	 */
	public void deleteCustomField(CustomField customField) throws ArkSystemException;

	/**
	 * Get a CustomField based on it's id
	 * @param id
	 * @return
	 */
	public CustomField getCustomField(Long id );

	/**
	 * Gets the count of CustomFields based on the criteria
	 * @param customFieldCriteria
	 * @return
	 */
	public int getCustomFieldCount(CustomField customFieldCriteria);

	/**
	 * Get a CustomFieldDisplay based on it's id
	 * @param id
	 * @return
	 */
	public CustomFieldDisplay getCustomFieldDisplay(Long id);

	/**
	 * Get a CustomFieldDisplay based on the criteria provided
	 * @param cfCriteria
	 * @return
	 */
	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria);

	public int getCustomFieldGroupCount(CustomFieldGroup customFieldGroup);

	/**
	 * Look up and return a list of CustomFieldGroup entities that match the search criteria. The mandatory parameters
	 * for the search are Study and ArkFunction. This must be set in the CustomFieldGroup instance that is passed to the service.
	 * @param customFieldGroup
	 * @return
	 */
	public List<CustomFieldGroup> getCustomFieldGroups(CustomFieldGroup customFieldGroup,int first, int count);

	/**
	 * Filters the CustomFields linked to 
	 * 1. A Study and
	 * 2. ArkFunction
	 * The Criteria is specified in the CustomField parameter. The study and ArkFunction must be set in it.
	 * @return List<CustomField>
	 */
	public List<CustomField> getCustomFieldList(CustomField customFieldCriteria);

	/**
	 * Get a FieldType based on it's id
	 * @param fieldTpeId
	 * @return
	 */
	public FieldType getFieldTypeById(Long id);

	/**
	 * Get a List of FieldTypes
	 * @return
	 */
	public List<FieldType> getFieldTypes();

	/**
	 * Get a List of UnitType.name(s), limited to the maxResults
	 * @param unitTypeCriteria
	 * @param maxResults
	 * @return
	 */
	public List<String> getUnitTypeNames(UnitType unitTypeCriteria, int maxResults);

	/**
	 * Get a list of UnitType(s) based on the criteria provided
	 * @param unitTypeCriteria
	 * @return
	 */
	public List<UnitType> getUnitTypes(UnitType unitTypeCriteria);

	/**
	 * Get a UnitType based on name and arkFunction
	 * @param name
	 * @param arkFunction
	 * @return
	 */
	public UnitType getUnitTypeByNameAndArkFunction(String name, ArkFunction arkFunction);
	
	/**
	 * Determine if the CustomField is unique, based on the name, study and CustomField to update
	 * @param customFieldName
	 * @param study
	 * @param customFieldToUpdate
	 * @return true if the CustomField is unique
	 */
	public boolean isCustomFieldUnqiue(String customFieldName, Study study, CustomField customFieldToUpdate);

	/**
	 * Search for CustomFields based on the criteria provided, limiting to the pageable amounts first and count
	 * @param customFieldCriteria
	 * @param first
	 * @param count
	 * @return
	 */
	public List<CustomField> searchPageableCustomFields(CustomField customFieldCriteria, int first, int count);

	/**
	 * Update a CustomField
	 * @param customField
	 * @throws ArkSystemException
	 */
	public void updateCustomField(CustomField customField) throws  ArkSystemException;

	/**
	 * Update a CustomFieldDisplay
	 * @param customFieldDisplay
	 * @throws ArkSystemException
	 */
	public void updateCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) throws  ArkSystemException;

	/**
	 * Get a CustomField based on name, study and arkFunction
	 * @param customFieldName
	 * @param study
	 * @param arkFunction
	 * @return
	 */
	public CustomField getCustomFieldByNameStudyArkFunction(String customFieldName, Study study, ArkFunction arkFunction);

}
