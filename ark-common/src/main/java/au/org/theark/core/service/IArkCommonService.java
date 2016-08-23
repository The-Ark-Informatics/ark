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
package au.org.theark.core.service;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import org.apache.velocity.exception.VelocityException;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;

import au.org.theark.core.dao.ReCaptchaContextSource;
import au.org.theark.core.exception.ArkAlreadyBeingUsedException;
import au.org.theark.core.exception.ArkNotAllowedToUpdateException;
import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.config.entity.ConfigField;
import au.org.theark.core.model.config.entity.UserConfig;
import au.org.theark.core.model.geno.entity.Command;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.model.geno.entity.ProcessInput;
import au.org.theark.core.model.geno.entity.ProcessOutput;
import au.org.theark.core.model.lims.entity.BioCollectionUidPadChar;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BioCollectionUidToken;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.ConsentStatusField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.QueryFilter;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.report.entity.SearchPayload;
import au.org.theark.core.model.report.entity.SearchResult;
import au.org.theark.core.model.report.entity.SearchSubject;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldCategoryUpload;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkStudyArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.State;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.model.study.entity.UploadLevel;
import au.org.theark.core.model.study.entity.UploadStatus;
import au.org.theark.core.model.study.entity.UploadType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.QueryFilterVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.vo.SubjectVO;

public interface IArkCommonService<T> {

	// Place here any common services that must be visible to sub-applications
	// Get reference data etc.get study maybe required but sub-applications
	// access a study via ETA Study module
	public ArkUserVO getUser(String name) throws ArkSystemException, EntityNotFoundException;

	public List<Study> getStudy(Study study);

	public List<StudyStatus> getListOfStudyStatus();

	public Study getStudy(Long id);

	public Collection<SubjectVO> getSubject(SubjectVO subjectVO);

	public List<SubjectStatus> getSubjectStatus();

	/**
	 * 
	 * Something of a hack for data that has value rather than key set for
	 * encoded data.
	 * 
	 */
	public void convertLimsBiospecimenCustomDataValuesToKeysForThisStudy(Study study);

	public void convertLimsBiocollectionCustomDataValuesToKeysForThisStudy(Study study);

	public Collection<TitleType> getTitleType();

	public Collection<VitalStatus> getVitalStatus();

	public Collection<GenderType> getGenderTypes();

	public List<PhoneType> getListOfPhoneType();

	/**
	 * <p>
	 * An interface that will return a LinkSubjectStudy instance which
	 * represents a Subject when provided with a Subject Unique Identifier.In
	 * the event that the system does not find a subject with the given
	 * identifier the interface will throw an EntityNotFoundException.
	 * </p>
	 * 
	 * @param subjectUID
	 * @param study
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubjectByUID(String subjectUID, Study study) throws EntityNotFoundException;

	public LinkSubjectStudy getSubjectRefreshed(LinkSubjectStudy subject);

	public Collection<MaritalStatus> getMaritalStatus();

	public Collection<EmailStatus> getAllEmailStatuses();

	public List<Country> getCountries();

	public Country getCountry(String countryCode);

	public List<State> getStates(Country country);

	/**
	 * Looks up all address types
	 * 
	 * @return
	 */
	public List<AddressType> getAddressTypes();

	/**
	 * Looks up all address statuses
	 * 
	 * @return
	 */
	public List<AddressStatus> getAddressStatuses();

	/**
	 * Looks up all address types
	 * 
	 * @return
	 */
	public List<PhoneType> getPhoneTypes();

	/**
	 * Looks up all address statuses
	 * 
	 * @return
	 */
	public List<PhoneStatus> getPhoneStatuses();

	/**
	 * 
	 * @return a list of Consent Status
	 */
	public List<ConsentStatus> getConsentStatus();

	/**
	 * Returns a list of consent status options permissible for
	 * creating/updating a record in the system. At the moment this means it is
	 * a list without the following: - "Not Consented" (because it should be
	 * null and/or no record in the Consent table)
	 * 
	 * @return List of ConsentStatus
	 */
	public List<ConsentStatus> getRecordableConsentStatus();

	/**
	 * Get the Study components for the specified Study
	 * 
	 * @return A List of Study components for the specified Study
	 */
	public List<StudyComp> getStudyComponentByStudy(Study study);

	public List<ConsentType> getConsentType();

	public List<StudyCompStatus> getStudyComponentStatus();

	public List<ConsentAnswer> getConsentAnswer();

	public List<YesNo> getYesNoList();

	public YesNo getYes();

	public YesNo getNo();

	public void createAuditHistory(AuditHistory auditHistory);

	public List<PersonContactMethod> getPersonContactMethodList();

	public boolean isSubjectConsentedToComponent(StudyComp studyComponent, Person subject, Study study);

	/**
	 * Returns a LinkSubjectStudy via the personId represented by the Long
	 * 
	 * @param personId
	 * @param study
	 * @return
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubject(Long personId, Study study) throws EntityNotFoundException;

	public List<SubjectUidPadChar> getListOfSubjectUidPadChar();

	public String getSubjectUidExample(Study study);

	public List<SubjectUidToken> getListOfSubjectUidToken();

	/* Access User Roles from Backend */

	/**
	 * This interface checks if the user is a Super Administrator
	 */
	public boolean isSuperAdministrator(String userName) throws EntityNotFoundException;

	/**
	 * This interface checks if the user is a Administrator in the Ark System.
	 * 
	 * @param userName
	 * @return
	 */
	public boolean isAdministator(String userName) throws EntityNotFoundException;

	/**
	 * Returns a Collection User Admin Roles. Super Administrator or
	 * Administator
	 * 
	 * @param ldapUserName
	 * @return
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException;

	/**
	 * For a Given study and Ldap User name get the Role he has been assigned.
	 * 
	 * @param ldapUserName
	 * @param study
	 * @return String that represents the Role for this study
	 * @throws EntityNotFoundException
	 */
	public String getUserRoleForStudy(String ldapUserName, Study study) throws EntityNotFoundException;

	/**
	 * Returns an instance of GenderType for a given String that represents a
	 * gender type name. The name should match the name in the database Gender
	 * Type.
	 * 
	 * @param name
	 * @return
	 */
	public GenderType getGenderType(String name);

	/**
	 * Returns an instance of VitalStatus for a given String that represents a
	 * Vital Status. The name should match the name in the database table
	 * vital_status.
	 * 
	 * @param name
	 * @return
	 */
	public VitalStatus getVitalStatus(String name);

	/**
	 * Returns an instance of Title Type for a given String that represents a
	 * gender type name. The name should match the name in the database
	 * title_type
	 * 
	 * @param name
	 * @return
	 */
	public TitleType getTitleType(String name);

	/**
	 * Returns an instance of Marital Status for a given String that represents
	 * a gender type name. The name should match the name in the database
	 * marital_status
	 * 
	 * @param name
	 * @return
	 */

	public MaritalStatus getMaritalStatus(String name);

	/**
	 * Returns an instance of PersonContactMethod for a given String that
	 * represents a Contact method. The name should match the name in the
	 * database person_contact_method
	 * 
	 * @param name
	 * @return
	 */
	public PersonContactMethod getPersonContactMethod(String name);

	/**
	 * Returns an instance of Subject Status for a given String that represents
	 * a gender type name. The name should match the name in the database
	 * subject_status.
	 * 
	 * @param name
	 * @return
	 */

	public SubjectStatus getSubjectStatus(String name);

	/**
	 * Returns a ArkUsecase instance for a given String that represents a
	 * usecase name.The name should match the name in the database table
	 * ark_usecase.
	 * 
	 * @return ArkUsecase
	 */
	public ArkFunction getArkFunctionByName(String arkFunctionName);

	/**
	 * Returns a ArkModule instance for a given String that represents a module
	 * name.The name should match the name in the database table ark_module
	 * 
	 * @return
	 */
	public ArkModule getArkModuleByName(String moduleName);

	/**
	 * Returns a String that represents a Role for a given Ldap User Name,
	 * ArkUsecase, ArkModule and Study. The LdapUserName,ArkUsecase are
	 * mandatory for a successful retrieval of a role.
	 * 
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param arkModule
	 * @param study
	 * @return String
	 * @throws EntityNotFoundException
	 */
	public String getUserRole(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule, Study study) throws EntityNotFoundException;

	/**
	 * Returns a ArkUsecase instance when provided a Long id that represents a
	 * valid use case id.
	 * 
	 * @param usecaseId
	 * @return
	 */
	public ArkFunction getArkFunctionById(Long arkFunctionId);

	/**
	 * Returns a ArkModule instance when provided a Long id that represents a
	 * valid module id.
	 * 
	 * @param usecaseId
	 * @return
	 */
	public ArkModule getArkModuleById(Long moduleId);

	/**
	 * For a given ArkRole,ArkFunction and ArkModule return a list of
	 * Permissions. This interface does not require the user(ArkUser)
	 * information.The ArkRole for the current user should be pre-determined
	 * before invoking this method.To get the user's role call getUserRole and
	 * then call this method. This method will use ArkRolePolicyTemplate table
	 * to return the list of Permissions for the given parameters.
	 * 
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param userRole
	 * @param arkModule
	 * @param study
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getArkRolePermission(ArkFunction arkFunction, String userRole, ArkModule arkModule) throws EntityNotFoundException;

	/**
	 * This overloaded interface can be used when we only want all their
	 * Permissions for a Given Role. It is applicable for Super Administator
	 * role where we don't need to specify the ArkModule or ArkRole. If this
	 * method is invoked for any other role it will return all the permissions
	 * for each role, the permissions will be duplicated. So avoid invoking this
	 * method for Non SuperAdministator type roles.
	 * 
	 * @param userRole
	 * @return
	 */
	public Collection<String> getArkRolePermission(String userRole) throws EntityNotFoundException;

	/**
	 * Returns All Permissions as collection of Strings
	 * 
	 * @return Collection<String> that represents ArkPermission
	 */
	public Collection<String> getArkPermission();

	/**
	 * 
	 * @param ldapUserName
	 * @param useCaseId
	 * @param moduleId
	 * @return
	 * @throws EntityNotFoundException
	 */
	public boolean isSuperAdministator(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException;

	public ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException;

	/**
	 * A generic interface that will return a list Entities specified by the
	 * Type of class
	 * 
	 * @return Collection of Class of type T .eg if the class passed in was
	 *         ArkModule.class the return Collection will be
	 *         Collection<ArkModule>
	 */
	public Collection<Class<T>> getEntityList(Class<T> aClass);

	/**
	 * A generic interface that will return a list SubjectVO specified by a
	 * particular criteria, and a pagingated reference point
	 * 
	 * @return Collection of SubjectVO
	 */
	public List<SubjectVO> searchPageableSubjects(SubjectVO subjectVoCriteria, int first, int count);

	/**
	 * A generic interface that will return count of the subjects in the study
	 * 
	 * @return int
	 */
	public long getStudySubjectCount(SubjectVO subjectVoCriteria);

	public Collection<ArkModuleRole> getArkModuleAndLinkedRoles();

	public Collection<ArkModuleVO> getArkModulesAndRolesLinkedToStudy(Study study);

	public ArrayList<ArkRole> getArkRoleLinkedToModule(ArkModule arkModule);

	public Collection<ArkModule> getArkModulesLinkedWithStudy(Study study);

	/**
	 * Get a collection of Functions associated with the given module.
	 * 
	 * @param arkModule
	 * @return
	 */
	public List<ArkFunction> getModuleFunction(ArkModule arkModule);

	public List<ArkUserRole> getArkUserLinkedModule(Study study, ArkModule arkModule);

	public List<LinkStudyArkModule> getLinkStudyArkModulesList(Study study);

	public List<PhoneStatus> getPhoneStatus();

	public Boolean studyHasSubjects(Study study);

	/**
	 * Get a list of studies that the given ark user is associated with.
	 * 
	 * @param arkUser
	 * @param study
	 * @return
	 */
	public List<Study> getStudiesForUser(ArkUser arkUser, Study study);

	/**
	 * A generic interface that will return count of the subjects in the study
	 * 
	 * @param customFieldCriteria
	 * @return int
	 */
	public long getCustomFieldCount(CustomField customFieldCriteria);

	/**
	 * A generic interface that will return a list CustomFields specified by a
	 * particular criteria, and a paginated reference point
	 * 
	 * @param customFieldCriteria
	 * @param first
	 *            - index to the first item (for that page)
	 * @param count
	 *            - number of items to return (for that page)
	 * @return Collection of Custom Fields
	 */
	public List<CustomField> searchPageableCustomFields(CustomField customFieldCriteria, int first, int count);

	/**
	 * A less generic interface that will return a list CustomFields specified
	 * by a particular criteria, and a paginated reference point
	 * 
	 * @param customFieldCriteria
	 * @param first
	 *            - index to the first item (for that page)
	 * @param count
	 *            - number of items to return (for that page)
	 * @return Collection of Custom Fields
	 */
	public List<CustomField> searchPageableCustomFieldsForPheno(CustomField customFieldCriteria, int first, int count);

	/**
	 * A generic interface that will return a list Custom Field FieldTypes
	 * 
	 * @return Collection of Custom Field FieldTypes
	 */
	public List<FieldType> getFieldTypes();

	/**
	 * A generic interface that will return a list Custom Field UnitType names
	 * for AutoCompletion
	 * 
	 * @param customFieldCriteria
	 *            - criteria for the query on UnitType
	 * @param maxResults
	 *            - applies a maximum number of results to return if greater
	 *            than 0 (otherwise, unconstrained)
	 * @return List of Custom Field UnitTypes
	 */
	public List<String> getUnitTypeNames(UnitType unitTypeCriteria, int maxResults);

	public List<UnitType> getUnitTypes(UnitType unitTypeCriteria);

	public CustomField getCustomField(Long id);

	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria);

	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria, CustomFieldGroup customFieldGroup);

	/**
	 * 
	 * @param customFieldVO
	 * @throws ArkUniqueException
	 * @throws ArkSystemException
	 */
	public void createCustomField(CustomFieldVO customFieldVO) throws ArkSystemException, ArkUniqueException;

	public void updateCustomField(CustomFieldVO customFieldVO) throws ArkSystemException, ArkUniqueException,ArkNotAllowedToUpdateException;

	public void deleteCustomField(CustomFieldVO customFieldVO) throws ArkSystemException, EntityCannotBeRemoved;

	public List<Study> getStudyListForUser(ArkUserVO arkUserVo);

	public List<ArkUserRole> getArkRoleListByUser(ArkUserVO arkUserVo);

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplate(ArkRole arkRole, ArkModule arkModule);

	public List<ArkUserRole> getArkRoleListByUserAndStudy(ArkUserVO arkUserVo, Study study);

	public List<Study> getStudyListForUserAndModule(ArkUserVO arkUserVo, ArkModule arkModule);

	public boolean arkUserHasModuleAccess(ArkUser arkUser, ArkModule arkModule);

	public List<ArkModule> getArkModuleListByArkUser(ArkUser arkUser);

	public long getCountOfStudies();

	public Boolean isArkUserLinkedToStudies(ArkUser arkUser);

	public List<CustomFieldGroup> getCustomFieldGroups(CustomFieldGroup customFieldGroup, int first, int count);

	/**
	 * Returns a count of CustomFieldGroup that matches a specific criteria this
	 * is used by Data View
	 * 
	 * @param customFieldGroup
	 * @return
	 */
	public CustomField getFieldByNameAndStudyAndFunction(String fieldName, Study study, ArkFunction arkFunction) throws EntityNotFoundException;

	public FieldType getFieldTypeByName(String typeName) throws EntityNotFoundException;

	public ArkRole getArkRoleByName(String roleName);

	/**
	 * Filters the CustomFields linked to 1. A Study and 2. ArkFunction The
	 * Criteria is specified in the CustomField parameter. The study and
	 * ArkFunction must be set in it.
	 * 
	 * @return List<CustomField>
	 */
	public List<CustomField> getCustomFieldList(CustomField customFieldCriteria);

	public ReCaptchaContextSource getRecaptchaContextSource();

	/**
	 * Send an email to the specified address, using the given fields
	 * 
	 * @param simpleMailMessage
	 * @throws MailSendException
	 * @throws VelocityException
	 */
	public void sendEmail(final SimpleMailMessage simpleMailMessage) throws MailSendException, VelocityException;

	/**
	 * Sets up the reset password message body using a Velocity template
	 * 
	 * @param fullName
	 * @param password
	 * @throws VelocityException
	 * @return the reset password message
	 */
	public String setResetPasswordMessage(final String fullName, final String password) throws VelocityException;

	/**
	 * 
	 * @param customFieldDisplay
	 * @throws ArkSystemException
	 */
	public void updateCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) throws ArkSystemException;

	public CustomFieldDisplay getCustomFieldDisplay(Long id);

	public FileFormat getFileFormatByName(String fileFormatName);

	public Collection<FileFormat> getFileFormats();

	public DelimiterType getDelimiterType(Long id);

	public UploadType getDefaultUploadType();

	public UploadType getCustomFieldDataUploadType();

	public Collection<DelimiterType> getDelimiterTypes();

	public Collection<UploadType> getUploadTypes();

	public Collection<UploadType> getUploadTypesForSubject(Study study);

	public Collection<UploadType> getUploadTypesForLims();

	public List<Upload> searchUploads(Upload uploadCriteria);

	public List<Upload> searchUploadsForBio(Upload uploadCriteria);

	public CustomField getCustomFieldByNameStudyArkFunction(String customFieldName, Study study, ArkFunction arkFunction);

	//public CustomField getCustomFieldByNameStudyCFG(String customFieldName, Study study, ArkFunction arkFunction, CustomFieldGroup customFieldGroup);

	public UnitType getUnitTypeByNameAndArkFunction(String string, ArkFunction arkFunction);

	public void createUpload(Upload studyUpload) throws Exception;

	public void updateUpload(Upload studyUpload);

	public Blob createBlob(byte[] bytes);

	public String getDelimiterTypeNameByDelimiterChar(char delimiterCharacter);

	public void createCustomFieldUpload(CustomFieldUpload cfUpload);

	public List<BiospecimenUidToken> getBiospecimenUidTokens();

	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars();

	public List<Study> getStudyListAssignedToBiospecimenUidTemplate();

	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);

	public List<BioCollectionUidToken> getBioCollectionUidToken();

	public List<BioCollectionUidPadChar> getBioCollectionUidPadChar();

	public void createBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate);

	/**
	 * Checks and returns true if the given study has Biospecimens linked to it.
	 * 
	 * @param study
	 * @return
	 */
	public Boolean studyHasBiospecimen(Study study);

	public Boolean studyHasBioCollection(Study study);

	public long getCountOfSubjects(Study study);

	// public long countNumberOfUniqueSubjects(Study study, List subjects);
	public long countNumberOfUniqueSubjectsWithTheseUIDs(Study study, List<String> subjectUIDs);

	public BiospecimenUidTemplate getBiospecimenUidTemplate(Study study);

	public BioCollectionUidTemplate getBioCollectionUidTemplate(Study study);

	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);

	public void updateBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate);

	public List<ArkUser> getArkUserListByStudy(ArkUser arkUser,Study study);

	public List<Study> getParentStudyList();

	/**
	 * Sets up an ArkUserVO with the default administrator roles for the
	 * specified study and List of ArkModule names
	 * 
	 * @param userName
	 *            the user to assign
	 * @param study
	 *            the study to assign to
	 * @param moduleList
	 *            the list of module names
	 * @return
	 */
	public ArkUserVO getDefaultAdministratorRoles(String userName, Study study, Set<String> moduleList);

	public List<Study> getAssignedChildStudyListForUser(ArkUserVO arkUserVo);

	public void deleteArkUserRole(ArkUserRole arkUserRole);

	/**
	 * Match a file containing a list of SubjectUid's for a given study, and
	 * return the matched list
	 * 
	 * @param subjectFileUpload
	 * @param study
	 * @return
	 */
	public List<SubjectVO> matchSubjectsFromInputFile(FileUpload subjectFileUpload, Study study);

	public List<Study> getAssignedChildStudyListForPerson(Study study, Person person);

	public List<ConsentOption> getConsentOptionList();

	/**
	 * returns a the subject (linksubjectystudy) IF there is one, else returns
	 * null
	 * 
	 * @param subjectUID
	 * @param study
	 * @return LinkSubjectStudy
	 */
	public LinkSubjectStudy getSubjectByUIDAndStudy(String subjectUID, Study study);

	public boolean customFieldHasData(CustomField customField);

	public List<String> getUniqueSubjectUIDsWithTheseUIDs(Study study, Collection<String> subjectUIDs);

	public List<LinkSubjectStudy> getUniqueSubjectsWithTheseUIDs(Study study, Collection<String> subjectUIDs);

	public List<String> getAllSubjectUIDs(Study study);

	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List fieldNameCollection, Study study, ArkFunction arkFunction);

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataFor(List customFieldDisplaysThatWeNeed, List<String> subjectUIDsToBeIncluded);

	public Payload createPayload(byte[] bytes);

	public Payload getPayloadForUpload(Upload upload);

	public UploadStatus getUploadStatusFor(String uploadStatusConstant);

	public List<CustomField> matchCustomFieldsFromInputFile(FileUpload fileUpload, Study study, ArkFunction arkFunction);

	public String getPreviousLastname(Person person);

	public List<OtherID> getOtherIDs(Person person);

	public List<Upload> searchUploadsForBiospecimen(Upload studyUpload, List<Study> studyListForUser);

	public List<Search> getSearchesForThisStudy(Study study);

	public boolean create(Search search) throws EntityExistsException;

	public boolean update(Search search) throws EntityExistsException;

	public boolean create(SearchVO search) throws EntityExistsException;

	public boolean update(SearchVO search) throws EntityExistsException;

	public Collection<ConsentStatusField> getAllConsentStatusFields();

	public Collection<DemographicField> getAllDemographicFields();

	public Collection<BiospecimenField> getAllBiospecimenFields();

	public Collection<BiocollectionField> getAllBiocollectionFields();

	public Collection<ConsentStatusField> getSelectedConsentStatusFieldsForSearch(Search search);

	public Collection<DemographicField> getSelectedDemographicFieldsForSearch(Search search);

	public Collection<BiospecimenField> getSelectedBiospecimenFieldsForSearch(Search search);

	public Collection<BiocollectionField> getSelectedBiocollectionFieldsForSearch(Search search);

	// public Collection<DemographicField>
	// getSelectedDemographicFieldsForSearch(Search search, boolean readOnly);

	public Collection<CustomFieldDisplay> getSelectedPhenoCustomFieldDisplaysForSearch(Search search);

	public Collection<PhenoDataSetFieldDisplay> getSelectedPhenoDataSetFieldDisplaysForSearch(Search search);

	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(Study study, ArkFunction arkFunction);

	public Collection<CustomFieldDisplay> getSelectedSubjectCustomFieldDisplaysForSearch(Search search);

	public Collection<CustomFieldDisplay> getSelectedBiospecimenCustomFieldDisplaysForSearch(Search search);

	public Collection<CustomFieldDisplay> getSelectedBiocollectionCustomFieldDisplaysForSearch(Search search);

	public void runSearch(Long searchId);

	public void createQueryFilters(List filterList) throws ArkSystemException;

	public List<QueryFilterVO> getQueryFilterVOs(Search search);

	public void deleteQueryFilter(QueryFilter queryFilter);

	public List<Study> getParentAndChildStudies(Long id);

	public SearchPayload getSearchPayloadForSearchResult(SearchResult searchResult);

	public List<SearchResult> getSearchResultList(Long searchResultId);

	public List<Relationship> getFamilyRelationships();

	public List<SearchSubject> getSearchSubjects();

	public void createSearchSubjects(Search search, List<SubjectVO> subjectVos);

	public void runSearch(Long searchId, String currentUser);

	public void delete(Search search);

	// public List<OtherID> getPersonOtherIDList(Long personID);

	public Collection<PersonLastnameHistory> getPersonLastNameHistory(Person person);

	public ConsentStatus getConsentStatusByName(String string);

	public void createPipeline(Pipeline p);

	public void updatePipeline(Pipeline p);

	public void deletePipeline(Pipeline p);

	public GenderType getSubjectGenderType(final String subjectUID, final Long studyId);

	public int getPipelineCount(Pipeline p);

	public List<Pipeline> searchPageablePipelines(Pipeline object, int first, int count);

	public long getPipelineCount(Study study);

	public int getProcessCount(Process object);

	public List<Process> searchPageableProcesses(Process object, int first, int count);

	public void createProcess(Process modelObject);

	public void updateProcess(Process modelObject);

	public void deleteProcess(Process modelObject);

	public List<Command> getCommands();

	public List<ProcessInput> getProcessInputsForProcess(Process process);

	public List<ProcessOutput> getProcessOutputsForProcess(Process process);

	public List<ConfigField> getAllConfigFields();

	public List<UserConfig> getUserConfigs(ArkUser arkUser);

	public UserConfig getUserConfig(ArkUser arkUser, ConfigField configField);
	
	public UserConfig getUserConfig(String configName);
	
	public void createUserConfigs(List userConfigList) throws ArkSystemException;

	public void deleteUserConfig(UserConfig userConfig);

	public List<CustomField> getCustomFieldsNotInList(List<CustomField> customFieldsFromData, ArkFunction function, Study study);

	/**
	 * Save the Ark file attachment to pre-configured directory location in the
	 * server.
	 * 
	 * @param studyId
	 * @param subjectUID
	 * @param directoryType
	 * @param fileName
	 * @param payload
	 * @param checksum
	 * @param fileId
	 */
	public void saveArkFileAttachment(final Long studyId, final String subjectUID, final String directoryType, final String fileName, final byte[] payload, final String fileId) throws ArkSystemException;

	/**
	 * Create a new directory
	 * @param directoryName Directory Name
	 */
	public void createArkFileAttachmentDirectoy(String directoryName) throws ArkSystemException;
	
	/**
	 * Generate directory name according to attachment type
	 * 
	 * @param studyId
	 * @param subjectUID
	 * @param directoryType
	 * @return
	 */
	public String getArkFileDirName(final Long studyId, final String subjectUID, final String directoryType);

	/**
	 * Generate a unique file id per attachment
	 * 
	 * @param fileName
	 * @return
	 */
	public String generateArkFileId(String fileName);

	/**
	 * Retrieve the attachment byte array
	 * 
	 * @param studyId
	 * @param subjectUID
	 * @param directoryType
	 * @param fileId
	 * @param checksum
	 * @return
	 */
	public byte[] retriveArkFileAttachmentByteArray(final Long studyId, final String subjectUID, final String directoryType, final String fileId, String checksum) throws ArkSystemException;

	/**
	 * Delete the Ark file attachment
	 * 
	 * @param studyId
	 * @param subjectUID
	 * @param checksum
	 * @param fileId
	 * @param attachmentType
	 * @return isDeleteSuccess
	 * @throws ArkSystemException
	 */
	public boolean deleteArkFileAttachment(Long studyId, String subjectUID, String fileId, String attachmentType, String checksum) throws ArkSystemException;
	
	/**
	 * Copy Large file attachments from one location to another
	 * 
	 * @param sourceFilePath Source file location
	 * @param destinationFilePath Destination file location
	 */
	public void copyArkLargeFileAttachments(String sourceFilePath, String destinationFilePath)throws IOException;
	
	/**
	 * Generate hash string for specified algorithm
	 * 
	 * @param file
	 * @param algorithm
	 * @return
	 * @throws ArkSystemException
	 */
	public String generateArkFileChecksum(File file, String algorithm) throws ArkSystemException;
		
	/**
	 * After introducing the CustomFieldCategory following methods add to the class.
	 * *****************************************************************************
	 * A generic interface that will return count of the subjects in the study
	 * 
	 * @param customFieldCriteria
	 * @return int
	 */
	public long getCustomFieldCategoryCount(CustomFieldCategory customFieldCategoryCriteria);
	
	/**
	 * Create custom field category
	 * @param CustomFieldCategoryVO
	 * @throws ArkSystemException
	 * @throws ArkUniqueException
	 */
	public void createCustomFieldCategory(CustomFieldCategoryVO CustomFieldCategoryVO) throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException;
	/**
	 * Update custom field category
	 * 
	 * @param CustomFieldCategoryVO
	 * @throws ArkSystemException
	 * @throws ArkUniqueException
	 */
	public void updateCustomFieldCategory(CustomFieldCategoryVO CustomFieldCategoryVO) throws ArkSystemException, ArkUniqueException, ArkAlreadyBeingUsedException,ArkNotAllowedToUpdateException;

	/**
	 * Delete custom fied category.
	 * 
	 * @param CustomFieldCategoryVO
	 * @throws ArkSystemException
	 * @throws EntityCannotBeRemoved
	 */
	public void deleteCustomFieldCategory(CustomFieldCategoryVO CustomFieldCategoryVO) throws ArkSystemException, EntityCannotBeRemoved;
	
	/**
	 * Get custom field category by id.
	 * @param id
	 * @return
	 */
	public CustomFieldCategory getCustomFieldCategory(Long id) throws EntityNotFoundException;
	/**
	 * Search pageable custom filed categories.
	 * @param customFieldCategoryCriteria
	 * @param first
	 * @param count
	 * @return
	 */
	public List<CustomFieldCategory> searchPageableCustomFieldCategories(CustomFieldCategory customFieldCategoryCriteria, int first, int count);
	
	/**
	 * Category list By custom field Type.
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 * @throws ArkSystemException
	 */
	public List<CustomFieldCategory> getParentCategoryListByCustomFieldType(Study study,ArkFunction arkFunction,CustomFieldType customFieldType) throws ArkSystemException;
	/**
	 * List of all available category list for update.
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @param thisCustomFieldCategory
	 * @return
	 * @throws ArkSystemException
	 */
	public List<CustomFieldCategory> getAvailableAllCategoryListByCustomFieldTypeExceptThis(Study study,ArkFunction arkFunction,CustomFieldType customFieldType,CustomFieldCategory thisCustomFieldCategory) throws ArkSystemException;
	/**
	 * List of all available category list for new
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 * @throws ArkSystemException
	 */
	public List<CustomFieldCategory> getAvailableAllCategoryListByCustomFieldType(Study study,ArkFunction arkFunction,CustomFieldType customFieldType) throws ArkSystemException;
	
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
	 * Get custom field Type by name.
	 * @param name
	 * @return
	 */
	public CustomFieldType getCustomFieldTypeByName(String name);
	
	/**
	 * Return custom field types by ark module.
	 * @param arkModule
	 * @return
	 */
	public List<CustomFieldType> getCustomFieldTypes(ArkModule arkModule);
	
	/**
	 * Get all the upload levels
	 * @return
	 */
	public List<UploadLevel> getAllUploadLevels();
	
	/**
	 * Get custom field categories by custom field type and study.
	 * @param study
	 * @param customFieldType
	 * @return
	 */
	public List<CustomFieldCategory> getCustomFieldCategoryByCustomFieldTypeAndStudy(Study study,CustomFieldType customFieldType);
	/**
	 * Get custom field category by name.
	 * @param name
	 * @return
	 */
	public CustomFieldCategory getCustomFieldCategotyByName(String name);
	
	/**
	 * Get a upload levels by id
	 * @return
	 */
	public UploadLevel getUploadLevelByName(String name);
	/**
	 * Get custom field category by name study and function.
	 * @param name
	 * @param study
	 * @param arkFunction
	 * @return
	 */
	public CustomFieldCategory getCustomFieldCategoryByNameStudyAndArkFunction(String name,Study study,ArkFunction arkFunction);
	
	/**Check for the custom field being used for categorise custom field.
	 * 
	 * @param customFieldCategory
	 * @return
	 */
	public boolean isCustomFieldCategoryBeingUsed(CustomFieldCategory customFieldCategory);
	/**
	 * Create the custom field category.
	 * @param cfcUpload
	 */
	public void createCustomFieldCategoryUpload(CustomFieldCategoryUpload cfcUpload);
	/**
	 * Get all family UID's for Subject
	 * @param study
	 * @return
	 */
	public List<String> getAllFamilyUIDs(Study study);
	/**
	 * Get the family customFielddataFor
	 * @param customFieldDisplaysThatWeNeed
	 * @param subjectUIDsToBeIncluded
	 * @return
	 */
	public List<SubjectCustomFieldData> getFamilyCustomFieldDataFor(Study study,List customFieldDisplaysThatWeNeed, List<String> familyUIDsToBeIncluded);
	/**
	 * Get customFieldDisplaysIn  for a custom field type.
	 * @param fieldNameCollection
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @return
	 */
	public List<CustomFieldDisplay> getCustomFieldDisplaysInWithCustomFieldType(List<?> fieldNameCollection, Study study, ArkFunction arkFunction,CustomFieldType customFieldType);
	/**
	 *  Get all children
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @param parentCategory
	 * @param allChilrenLst
	 * @return
	 */
	public List<CustomFieldCategory> getAllChildrenCategoriedBelongToThisParent(Study study, ArkFunction arkFunction,CustomFieldType customFieldType,CustomFieldCategory parentCategory,List<CustomFieldCategory> allChildrenLst);
	/**
	 * Get sibling of a custom field category.
	 * @param study
	 * @param arkFunction
	 * @param customFieldType
	 * @param customFieldCategory
	 * @return
	 */
	public List<CustomFieldCategory> getSiblingList(Study study,ArkFunction arkFunction,CustomFieldType customFieldType,CustomFieldCategory customFieldCategory);
	
	/**
	 * Get all the function and permission list for user.
	 * @param arkUserRole
	 * @return
	 */
	public List<ArkRolePolicyTemplate> getArkRolePolicytemplateList(ArkUserVO arkUserVO);
	/**
	 * 
	 * @param arkUserRole
	 * @param arkFunction
	 * @return
	 */
	public List<ArkPermission> getArkPremissionListForRoleAndModule(ArkRolePolicyTemplate arkRolePolicyTemplate);
	/**
	 * 
	 * @param arkModule
	 * @param name
	 * @return
	 */
	public UploadType getUploadTypeByModuleAndName(ArkModule arkModule,String name);
	
	/**
	 * 
	 * @param customFieldCategory
	 * @return
	 */
	public boolean isThisCustomCategoryWasAParentCategoryOfAnother(CustomFieldCategory customFieldCategory);
	/**
	 * 
	 * @param name
	 * @param customFieldType
	 * @return
	 */
	public CustomFieldCategory getCustomFieldCategotyByNameAndCustomFieldType(String name, CustomFieldType customFieldType);
	/**
	 * 
	 * @param search
	 * @return
	 */
	public List<Search> getSearchesForSearch(Search search);
	/**
	 * Get study component never used in the subject.
	 * 
	 * @param study
	 * @param linkSubjectStudy
	 * @return
	 */
	public List<StudyComp> getStudyComponentsNotInThisSubject(Study study,LinkSubjectStudy linkSubjectStudy);
	/**
	 * 
	 * @param study
	 * @param linkSubjectStudy
	 * @return
	 */
	public List<StudyComp> getDifferentStudyComponentsInConsentForSubject(Study study, LinkSubjectStudy linkSubjectStudy);

}
