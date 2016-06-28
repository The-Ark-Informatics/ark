package au.org.theark.core.dao;

import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.model.study.entity.UploadLevel;
import au.org.theark.core.vo.CustomFieldCategoryVO;

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
	public long getCustomFieldCount(CustomField customFieldCriteria);

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

	public long getCustomFieldGroupCount(CustomFieldGroup customFieldGroup);

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
	 * Search for CustomFields based on the criteria provided, limiting to the pageable amounts first and count
	 *  BUT WILL FORCE PHENO_ARK_MODULE so that it only gets pheno fields, workaround until an agreed method of sorting
	 * @param customFieldCriteria
	 * @param first
	 * @param count
	 * @return
	 */
	public List<CustomField> searchPageableCustomFieldsForPheno(CustomField customFieldCriteria, int first, int count);

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
	
	//public CustomField getCustomFieldByNameStudyCFG(String customFieldName, Study study, ArkFunction arkFunction, CustomFieldGroup customFieldGroup);
	
	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria, CustomFieldGroup customFieldGroup);
	
	public List<CustomField> matchCustomFieldsFromInputFile(FileUpload fileUpload, Study study, ArkFunction arkFunction);

	public void convertLimsBiocollectionCustomDataValuesToKeysForThisStudy(Study study);

	public void convertLimsBiospecimenCustomDataValuesToKeysForThisStudy(Study study);

	public List<CustomField> getCustomFieldsNotInList(List<CustomField> customFieldsFromData, ArkFunction arkFunction, Study study);
	
	
	public long getCustomFieldCategoryCount(CustomFieldCategory customFieldCategoryCriteria);
	
	/**
	 * Create customFieldCategory
	 * @param customFieldCategory
	 * @throws ArkSystemException
	 */
	public void createCustomFieldCategory(CustomFieldCategory customFieldCategory) throws  ArkSystemException;
	
	/**
	 * Update a CustomFieldCategory
	 * @param customFieldCategory
	 * @throws ArkSystemException
	 */
	public void updateCustomFieldCategory(CustomFieldCategory customFieldCategory) throws  ArkSystemException;
	
	/**
	 * Delete customFieldCategory
	 * @param customFieldCategory
	 * @throws ArkSystemException
	 */
	public void deleteCustomFieldCategory(CustomFieldCategory customFieldCategory) throws ArkSystemException;
	
	/**
	 * Determine if the CustomField is unique, based on the name, study and CustomField to update
	 * @param customFieldName
	 * @param study
	 * @param customFieldToUpdate
	 * @return true if the CustomField is unique
	 */
	public boolean isCustomFieldCategoryUnqiue(String customFieldCategoryName, Study study, CustomFieldCategory customFieldCategoryToUpdate);
	/**
	 * 
	 * Get custom field category.
	 * @param id
	 * @return
	 */
	public CustomFieldCategory getCustomFieldCategory(Long id);
	
	/**
	 * Search for CustomFields based on the criteria provided, limiting to the pageable amounts first and count
	 * @param customFieldCategoryCriteria
	 * @param first
	 * @param count
	 * @return
	 */
	public List<CustomFieldCategory> searchPageableCustomFieldCategories(CustomFieldCategory customFieldCategoryCriteria, int first, int count);
	
	/**
	 * Category list By custom field Type.
	 * 
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 * @throws ArkSystemException
	 */
	public List<CustomFieldCategory> getParentCategoryListByCustomFieldType(Study study,ArkFunction arkFunction,CustomFieldType customFieldType) throws ArkSystemException;
	
	/**
	 * check this customCategory is a parentcategory of any other category.
	 * 
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 */
	public boolean isThisCustomCategoryWasAParentCategoryOfAnother(CustomFieldCategory customFieldCategory);
	
	/**
	 * List of all available category list for update.
	 * 
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 */
	public List<CustomFieldCategory> getAvailableAllCategoryListByCustomFieldTypeExceptThis(Study study,ArkFunction arkFunction,CustomFieldType customFieldType,CustomFieldCategory thisCustomFieldCategory) throws ArkSystemException;
	/**
	 * List of all available category list by custom field type..
	 * 
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 * @throws ArkSystemException
	 */
	public List<CustomFieldCategory> getAvailableAllCategoryListByCustomFieldType(Study study, ArkFunction arkFunction,CustomFieldType customFieldType) throws ArkSystemException;
	/**
	 * List of all available categories in custom fileds by .
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 * @throws ArkSystemException
	 */
	public List<CustomFieldCategory> getCategoriesListInCustomFieldsByCustomFieldType(Study study, ArkFunction arkFunction,CustomFieldType customFieldType) throws ArkSystemException;
	/**
	 * List of all available categories for study.
	 * @param study
	 * @param customFieldType
	 * @return
	 * @throws ArkSystemException
	 */
	public List<CustomFieldCategory> getAvailableAllCategoryListInStudyByCustomFieldType(Study study,ArkFunction arkFunction,CustomFieldType customFieldType) throws ArkSystemException;
	/**
	 * Get Custom Field Type by name.
	 * @param name
	 * @return
	 */
	public CustomFieldType getCustomFieldTypeByName(String name);
	/**
	 * Get custom field types for a module.
	 * @param arkModule
	 * @return
	 */
	public List<CustomFieldType> getCustomFieldTypes(ArkModule arkModule);
	/**
	 * Return All Upload levels
	 * @return
	 */
	public List<UploadLevel> getAllUploadLevels();
	
	/**
	 * Get custom field categories by study and custom field type.
	 * @param study
	 * @param customFieldType
	 * @return
	 */
	public List<CustomFieldCategory> getCustomFieldCategoryByCustomFieldTypeAndStudy(Study study,CustomFieldType customFieldType);
	/**
	 * Get custom field category by name
	 * @param name
	 * @return
	 */
	public CustomFieldCategory getCustomFieldCategotyByName(String name);
	
	/**
	 * Get a upload levels by name
	 * @return
	 */
	public UploadLevel getUploadLevelByName(String name);
	/**
	 * Get custom field category by name study and function.
	 * @param name
	 * @param study
	 * @param arkFunction
	 */
	public CustomFieldCategory getCustomFieldCategoryByNameStudyAndArkFunction(String name,Study study,ArkFunction arkFunction);
	
	/**
	 * Check for the custom field being used for categorise custom field.
	 * @param customFieldCategory
	 * @return
	 */
	public boolean isCustomFieldCategoryBeingUsed(CustomFieldCategory customFieldCategory);
	/**
	 * Get children of custom field categories.
	 * @param study
	 * @param arkFunction
	 * @param parentcustomFieldCategory
	 * @return
	 */
	public List<CustomFieldCategory> getAllSubCategoriesOfThisCategory(Study study,ArkFunction arkFunction,CustomFieldType customFieldType,CustomFieldCategory parentcustomFieldCategory);
	/**
	 * Get siblings of custom field categories.
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @param customFieldCategory
	 * @return
	 */
	public List<CustomFieldCategory> getSiblingList(Study study,ArkFunction arkFunction,CustomFieldType customFieldType,CustomFieldCategory customFieldCategory);
	/**
	 * 
	 * @param name
	 * @param customFieldType
	 * @return
	 */
	public CustomFieldCategory getCustomFieldCategotyByNameAndCustomFieldType(String name,CustomFieldType customFieldType);
	
}
