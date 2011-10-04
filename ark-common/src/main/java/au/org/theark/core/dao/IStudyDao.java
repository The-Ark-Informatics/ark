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
package au.org.theark.core.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.vo.SubjectVO;

/**
 * Interface that provides CRUD and accessor methods to Study entities 
 * @author nivedann
 * @author cellis
 */
public interface IStudyDao {

	/**
	 * Looks up a Study based on the filter supplied as part of the Study.
	 * 
	 * @param study
	 * @return
	 */
	public List<Study> getStudy(Study study);

	/**
	 * Interface to get a list of Study Status reference data from the backend. These study status' are no associated with a study as such but can be
	 * used for displaying a list of options for a particular study.
	 * 
	 * @return
	 */
	public List<StudyStatus> getListOfStudyStatus();

	/**
	 * Fetch a Study entity by providing the primary key of the study.
	 * 
	 * @param id
	 * @return
	 */
	public Study getStudy(Long id);

	/**
	 * Gets a StudyStatus entity by providing the status name.
	 * 
	 * @param statusName
	 * @return
	 * @throws StatusNotAvailableException
	 */
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException;

	/**
	 * A look up that returns a list of All Phone Types. Mobile, Land etc In the event that there is a database/runtime error it is wrapped into a
	 * ArkSystemException and returned
	 * 
	 * @return List<PhoneType>
	 */
	public List<PhoneType> getListOfPhoneType();

	/**
	 * An interface fetch a list of Title types.
	 * 
	 * @return
	 */
	public Collection<TitleType> getTitleType();

	/**
	 * An interace to fetch a list of Vital Status reference types.
	 * 
	 * @return
	 */
	public Collection<VitalStatus> getVitalStatus();

	/**
	 * An interface to fetch a list of GenderType references.
	 * 
	 * @return
	 */
	public Collection<GenderType> getGenderType();

	/**
	 * An interface to return a list of Subject Status types
	 * 
	 * @return
	 */
	public List<SubjectStatus> getSubjectStatus();

	/**
	 * A look-up method that searches based on the Filter provided in the incoming SubjectVO value object.
	 * 
	 * @param subjectVO
	 * @return
	 */
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO);

	/**
	 * Returns a LinkSubjectStudy entity by comparing the Long personId with LinkSubjectStudy entity. The Long id passed in represents the Person
	 * primary key or personId.
	 * 
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubject(Long personId) throws EntityNotFoundException;

	/**
	 * Look up a list of subjects linked to the current study by passing in a primary key of the LinkSubjectStudy.
	 * 
	 * @param id
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getLinkSubjectStudy(Long linkSubjectStudyId) throws EntityNotFoundException;

	/**
	 * Return a Subject based on a Unique Identifier. In the event it does not locate the specified subject by the Id it will throw a
	 * EntityNotFoundException
	 * 
	 * @param subjectUID
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubjectByUID(String subjectUID) throws EntityNotFoundException;

	/**
	 * Get a list of Marital Status
	 * 
	 * @return
	 */
	public Collection<MaritalStatus> getMaritalStatus();

	/**
	 * Returns a list of all countries.
	 * 
	 * @return
	 */
	public List<Country> getCountries();

	/**
	 * Get a Country based on a countryCode
	 * @param countryCode
	 * @return
	 */
	public Country getCountry(String countryCode);

	/**
	 * Given a country retrieves a list of States linked to it
	 * 
	 * @param country
	 * @return
	 */
	public List<CountryState> getStates(Country country);

	/**
	 * Returns a list of Address Types
	 * 
	 * @return
	 */
	public List<AddressType> getAddressTypes();

	/**
	 * Returns a list of Address Statuses
	 * 
	 * @return
	 */
	public List<AddressStatus> getAddressStatuses();

	/**
	 * Returns a Collection of Consent Status
	 * 
	 * @return
	 */
	public List<ConsentStatus> getConsentStatus();

	/**
	 * 
	 * @return
	 */
	public List<StudyCompStatus> getStudyComponentStatus();

	/**
	 * Get the Study components for the specified Study
	 * 
	 * @return A List of Study components for the specified Study
	 */
	public List<StudyComp> getStudyComponentByStudy(Study study);

	/**
	 * Returns a list of Consent types hardcopy, electronic document etc.
	 * 
	 * @return
	 */
	public List<ConsentType> getConsentType();

	/**
	 * Get a List of ConsentAnswer(s)
	 * @return
	 */
	public List<ConsentAnswer> getConsentAnswer();

	/**
	 * Get a List of YesNo(s)
	 * @return
	 */
	public List<YesNo> getYesNoList();

	/**
	 * Create an AuditHistory
	 * @param auditHistory
	 */
	public void createAuditHistory(AuditHistory auditHistory);

	/**
	 * Get a List of PersonContactMethod(s)
	 * @return
	 */
	public List<PersonContactMethod> getPersonContactMethodList();

	/**
	 * Determine if the Person is consented to the studyComponent in the study
	 * @param studyComponent
	 * @param subject
	 * @param study
	 * @return
	 */
	public boolean isSubjectConsentedToComponent(StudyComp studyComponent, Person subject, Study study);

	/**
	 * Create a new LastNameHistory for the Person
	 * @param person
	 */
	public void createPersonLastnameHistory(Person person);

	/**
	 * Update the Persons LastNameHistory
	 * @param person
	 */
	public void updatePersonLastnameHistory(Person person);

	/**
	 * Returns previousSurnameHistory
	 * 
	 * @return
	 */
	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory);

	/**
	 * Returns previous surname
	 * 
	 * @return
	 */
	public String getPreviousLastname(Person person);

	/**
	 * Returns current surname
	 * 
	 * @return
	 */
	public String getCurrentLastname(Person person);

	/**
	 * Returns a list of PersonSurnameHistory
	 * 
	 * @return
	 */
	public List<PersonLastnameHistory> getLastnameHistory(Person person);

	/**
	 * Get a List of SubjectUidPadChar(s)
	 * @return
	 */
	public List<SubjectUidPadChar> getListOfSubjectUidPadChar();

	/**
	 * Get an example of the SubjectUid for the study
	 * @param study
	 * @return
	 */
	public String getSubjectUidExample(Study study);

	/**
	 * Get a List of SubjectUidToken(s)
	 * @return
	 */
	public List<SubjectUidToken> getListOfSubjectUidToken();

	/**
	 * Get a GenderType based on a name
	 * @param name
	 * @return
	 */
	public GenderType getGenderType(String name);

	/**
	 * Get a VitalStatus based on a name
	 * @param name
	 * @return
	 */
	public VitalStatus getVitalStatus(String name);

	/**
	 * Get a TitleType based on a name
	 * @param name
	 * @return
	 */
	public TitleType getTitleType(String name);

	/**
	 * Get a MaritalStatus based on a name
	 * @param name
	 * @return
	 */
	public MaritalStatus getMaritalStatus(String name);

	/**
	 * Get a PersonContactMethod based on a name
	 * @param name
	 * @return
	 */
	public PersonContactMethod getPersonContactMethod(String name);

	/**
	 * Get a SubjectStatus based on a name
	 * @param name
	 * @return
	 */
	public SubjectStatus getSubjectStatus(String name);

	/**
	 * A generic interface that will return a list SubjectVO specified by a particular criteria, and a pagingated reference point
	 * 
	 * @return Collection of SubjectVO
	 */
	public List<SubjectVO> searchPageableSubjects(SubjectVO subjectVoCriteria, int first, int count);

	/**
	 * A generic interface that will return count of the subjects in the study
	 * 
	 * @return int
	 */
	public int getStudySubjectCount(SubjectVO subjectVoCriteria);

	/**
	 * Returns a list of consent status options permissible for creating/updating a record in the system. At the moment this means it is a list without
	 * the following: - "Not Consented" (because it should be null and/or no record in the Consent table)
	 * 
	 * @return List of ConsentStatus
	 */
	public List<ConsentStatus> getRecordableConsentStatus();

	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can
	 * represent a subject or contact.
	 * 
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Retrieves a List of ArkFunction for a given ArkModule. This can be used to determine what functions/use cases are tied to a particular module.
	 * 
	 * @param arkModule
	 * @return List<ArkFunction>
	 */
	public List<ArkFunction> getModuleFunction(ArkModule arkModule);

	/**
	 * Get a List of PhoneStatus(s)
	 * @return
	 */
	public List<PhoneStatus> getPhoneStatus();

	/**
	 * Returns true if the specified study has Subjects assigned
	 * @param study
	 * @return
	 */
	public Boolean studyHasSubjects(Study study);
	
	/**
	 * Gets a List of Study's for the specified arkUser (also retricts on study.name)
	 * @param arkUser
	 * @param study
	 * @return
	 */
	public List<Study> getStudiesForUser(ArkUser arkUser, Study study);
	
	/**
	 * Gets the count of CustomFields based on the criteria
	 * @param customFieldCriteria
	 * @return
	 */
	public int getCustomFieldCount(CustomField customFieldCriteria);

	/**
	 * Search a on CustomField based on the criteria provided, limiting to the pageable amounts first and count
	 * @param customFieldCriteria
	 * @param first
	 * @param count
	 * @return
	 */
	public List<CustomField> searchPageableCustomFields(CustomField customFieldCriteria, int first, int count);

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
	 * Get a FieldType based on it's id
	 * @param fieldTpeId
	 * @return
	 */
	public FieldType getFieldTypeById(Long id);
	
	/**
	 * Get a CustomField based on it's id
	 * @param id
	 * @return
	 */
	public CustomField getCustomField(Long id );
	
	/**
	 * Get a CustomFieldDisplay based on the criteria provided
	 * @param cfCriteria
	 * @return
	 */
	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria);
	
	/**
	 * Get a CustomFieldDisplay based on it's id
	 * @param id
	 * @return
	 */
	public CustomFieldDisplay getCustomFieldDisplay(Long id);
	
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
	 * Delete a CustomField
	 * @param customField
	 * @throws ArkSystemException
	 */
	public void deleteCustomField(CustomField customField) throws ArkSystemException;
	
	/**
	 * Delete a CustomFieldDisplay
	 * @param customFieldDisplay
	 * @throws ArkSystemException
	 */
	public void deleteCustomDisplayField(CustomFieldDisplay customFieldDisplay) throws ArkSystemException;
	
	/**
	 * Determine if the CustomField is unique, based on the name, study and CustomField to update
	 * @param customFieldName
	 * @param study
	 * @param customFieldToUpdate
	 * @return true if the CustomField is unique
	 */
	public boolean isCustomFieldUnqiue(String customFieldName, Study study, CustomField customFieldToUpdate);

	/**
	 * Get the total count of Studies 
	 * @return the total count of Studies 
	 */
	public int getCountOfStudies();
	
	/**
	 * Look up and return a list of CustomFieldGroup entities that match the search criteria. The mandatory parameters
	 * for the search are Study and ArkFunction. This must be set in the CustomFieldGroup instance that is passed to the service.
	 * @param customFieldGroup
	 * @return
	 */
	public List<CustomFieldGroup> getCustomFieldGroups(CustomFieldGroup customFieldGroup,int first, int count);
	
	
	public int getCustomFieldGroupCount(CustomFieldGroup customFieldGroup);
	
	/**
	 * Filters the CustomFields linked to 
	 * 1. A Study and
	 * 2. ArkFunction
	 * The Criteria is specified in the CustomField parameter. The study and ArkFunction must be set in it.
	 * @return List<CustomField>
	 */
	public List<CustomField> getCustomFieldList(CustomField customFieldCriteria);
}
