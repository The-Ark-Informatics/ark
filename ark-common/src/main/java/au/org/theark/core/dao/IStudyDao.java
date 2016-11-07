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

import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.config.entity.ConfigField;
import au.org.theark.core.model.config.entity.UserConfig;
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
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategoryUpload;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
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
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.model.study.entity.UploadStatus;
import au.org.theark.core.model.study.entity.UploadType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.vo.QueryFilterVO;
import au.org.theark.core.vo.SearchVO;
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
	public Collection<GenderType> getGenderTypes();

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
	 * @param study 
	 * 
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubject(Long personId, Study study) throws EntityNotFoundException;

	/**
	 * Look up a list of subjects linked to the current study by passing in a primary key of the LinkSubjectStudy.
	 * 
	 * @param id
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getLinkSubjectStudy(Long linkSubjectStudyId) throws EntityNotFoundException;

	/**
	 * Return a Subject based on a Unique Identifier and study (note: SubjectUID is unique only within a particular study). 
	 * In the event it does not locate the specified subject by the Id it will throw a EntityNotFoundException
	 * 
	 * @param subjectUID
	 * @param study
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubjectByUID(String subjectUID, Study study) throws EntityNotFoundException;

	public LinkSubjectStudy getSubjectRefreshed(LinkSubjectStudy subject);
	
	
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
	public List<State> getStates(Country country);

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
	 * Returns a list of Phone Types
	 * 
	 * @return
	 */
	public List<PhoneType> getPhoneTypes();

	/**
	 * Returns a list of Phone Statuses
	 * 
	 * @return
	 */
	public List<PhoneStatus> getPhoneStatuses();


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
	 * Create an AuditHistory
	 * @param auditHistory
	 */
	public void createAuditHistory(AuditHistory auditHistory, String userId, Study study);

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
	 * Update the Persons LastNameHistory
	 * @param person
	 *
	public void updatePersonLastnameHistory(Person person);
*/
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
	//public String getCurrentLastname(Person person);

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
	public long getStudySubjectCount(SubjectVO subjectVoCriteria);

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
	 * Get the total count of Studies 
	 * @return the total count of Studies 
	 */
	public long getCountOfStudies();

	/**
	 * Get a FileFormat based on the name
	 * @param fileFormatName
	 * @return
	 */
	public FileFormat getFileFormatByName(String fileFormatName);

	/**
	 * Get a List of FileFormats
	 * @return
	 */
	public Collection<FileFormat> getFileFormats();

	/**
	 * Get a DelimiterType based on the id
	 * @param id
	 * @return
	 */
	public DelimiterType getDelimiterType(Long id);

	/**
	 * Get a List of DelimiterTypes
	 * @return
	 */
	public Collection<DelimiterType> getDelimiterTypes();


	/**
	 * Get a List of UploadTypes
	 * @return
	 */
	public Collection<UploadType> getUploadTypes();


	public UploadType getDefaultUploadType();
	
	public UploadType getDefaultUploadTypeForLims();
	
	public UploadType getCustomFieldDataUploadType();
	
	/**
	 * Retrieves a list of Upload for the given criteria
	 * @param uploadCriteria
	 * @return
	 */
	public List<Upload> searchUploads(Upload uploadCriteria);

	/**
	 * Create a new Upload 
	 * @param studyUpload
	 */
	public void createUpload(Upload studyUpload) throws Exception;

	/**
	 * Update a new Upload 
	 * @param studyUpload
	 */
	public void updateUpload(Upload studyUpload);
	
	/**
	 * Get the delimiter name by the character
	 * @param delimiterCharacter
	 * @return delimiter name
	 */
	public String getDelimiterTypeNameByDelimiterChar(char delimiterCharacter);

	/**
	 * Create a mew CustomFieldUpload
	 * @param cfUpload
	 */
	public void createCustomFieldUpload(CustomFieldUpload cfUpload);
	
	public List<BiospecimenUidToken> getBiospecimenUidTokens();
	
	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars();
	
	public List<Study> getStudyListAssignedToBiospecimenUidTemplate();
	
	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);
	
	public List<BioCollectionUidToken> getBioCollectionUidToken();
	
	public List<BioCollectionUidPadChar> getBioCollectionUidPadChar();
	
	public void createBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate);
	
	public Boolean studyHasBiospecimen(Study study);
	
	public Boolean studyHasBioCollection(Study study);
	
	public BiospecimenUidTemplate getBiospecimentUidTemplate(Study study);
	
	public BioCollectionUidTemplate getBioCollectionUidTemplate(Study study);
	
	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);
	
	public void updateBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate);

	/**
	 * returns a the subject (linksubjectystudy) IF there is one, else returns null
	 * @param subjectUID
	 * @param study
	 * @return LinkSubjectStudy
	 */
	public LinkSubjectStudy getSubjectByUIDAndStudy(String subjectUID, Study study);
	
	public long getCountOfSubjects(Study study);
	
	/**
	 * Match a file containing a list of SubjectUid's for a given study, and return the matched list
	 * @param subjectFileUpload
	 * @param study
	 * @return
	 */
	public List<SubjectVO> matchSubjectsFromInputFile(FileUpload subjectFileUpload, Study study);

	public List<Study> getAssignedChildStudyListForPerson(Study study, Person person);

	public List<ConsentOption> getConsentOptionList();

	public boolean customFieldHasData(CustomField customField);

	public long countNumberOfSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection<String> subjectUids);

	//TODO evaluate performance vs return list of strings as needed for large comparisons
	public List<String> getAllSubjectUIDs(Study study);

	public List<String> getSubjectUIDsThatAlreadyExistWithTheseUIDs(Study study, Collection<String> subjectUids);
	
	public List<LinkSubjectStudy> getSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection<String> subjectUids);
	
	@SuppressWarnings("unchecked")
	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List<String> fieldNameCollection, Study study, ArkFunction arkFunction);

	@SuppressWarnings("unchecked")
	public List<SubjectCustomFieldData> getSubjectCustomFieldDataFor(Collection customFieldDisplaysThatWeNeed, List subjectUIDsToBeIncluded);

	public Payload createPayload(byte[] bytes);

	public Payload getPayloadForUpload(Upload upload);

	//public UploadStatus getUploadStatusForUploaded();

	//public UploadStatus getUploadStatusForValidated();
	
	//public UploadStatus getUploadStatusForAwaitingValidation();

	public Collection<UploadType> getUploadTypesForSubject(Study study);

	public Collection<UploadType> getUploadTypesForLims();

	public List<Upload> searchUploadsForBio(Upload uploadCriteria);

	public UploadStatus getUploadStatusFor(String uploadStatusConstant);

	public YesNo getYes();

	public YesNo getNo();

	public List<EmailStatus> getAllEmailStatuses();
	
	public List<Upload> searchUploadsForBiospecimen(Upload uploadCriteria, List studyListForUser);

	public List<Search> getSearchesForThisStudy(Study study);	

	public boolean create(Search search) throws EntityExistsException;

	public boolean update(Search search) throws EntityExistsException;
	
	public boolean create(SearchVO search) throws EntityExistsException;

	public boolean update(SearchVO search) throws EntityExistsException;
	
	public Collection<ConsentStatusField> getAllConsentStatusFields();
	
	public Collection<DemographicField> getAllDemographicFields();

	public Collection<BiocollectionField> getAllBiocollectionFields();

	public Collection<BiospecimenField> getAllBiospecimenFields();

	public Collection<ConsentStatusField> getSelectedConsentStatusFieldsForSearch(Search search);

	public List<BiocollectionField> getSelectedBiocollectionFieldsForSearch(Search search);

	public List<BiospecimenField> getSelectedBiospecimenFieldsForSearch(Search search);


	public List<DemographicField> getSelectedDemographicFieldsForSearch(Search search);

	@Deprecated
	public Collection<CustomFieldDisplay> getSelectedPhenoCustomFieldDisplaysForSearch(Search search);

	public Collection<PhenoDataSetFieldDisplay> getSelectedPhenoDataSetFieldDisplaysForSearch(Search search);

	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(Study study, ArkFunction arkFunction);

	public Collection<CustomFieldDisplay> getSelectedSubjectCustomFieldDisplaysForSearch(Search search);

	public Collection<CustomFieldDisplay> getSelectedBiospecimenCustomFieldDisplaysForSearch(Search search);

	public Collection<CustomFieldDisplay> getSelectedBiocollectionCustomFieldDisplaysForSearch(Search search);

	public void runSearch(Long searchId);

	@SuppressWarnings("unchecked")
	public void createQueryFilters(List filterList) throws ArkSystemException;

	public List<QueryFilterVO> getQueryFilterVOs(Search search);

	public void deleteQueryFilter(QueryFilter queryFilter);

	public List getParentAndChildStudies(Long id);

	public SearchPayload getSearchPayloadForSearchResult(SearchResult searchResult);

	public List<SearchResult> getSearchResultList(Long searchResultId);
	
	public SearchPayload createSearchPayload(byte[] bytes);

	public List<Relationship> getFamilyRelationships();
	
	public List<SearchSubject> getSearchSubjects();
	
	public void createSearchSubjects(Search search, List<SearchSubject> searchSubjects);

	public void runSearch(Long searchId, String currentUser);

	public void delete(Search search);

	public void delete(SearchResult result);

	public ConsentStatus getConsentStatusByName(String name);
	
	public GenderType getSubjectGenderType(final String subjectUID,final Long studyId);

	public List<OtherID> getOtherIDs(Person person);

	public Collection<PersonLastnameHistory> getPersonLastnameHistory(Person person);

	public void createUserConfigs(List<UserConfig> userConfigList) throws ArkSystemException;
	
	public List<ConfigField> getAllConfigFields();
	
	public List<UserConfig> getUserConfigs(ArkUser arkUser);
	
	public void deleteUserConfig(UserConfig uc);
	
	public List<Study> getChildStudiesForStudy(Study study);
	
	public void createCustomFieldCategoryUpload(CustomFieldCategoryUpload cfcUpload);
	
	public List<String> getAllFamilyUIDs(Study study);
	
	public List<FamilyCustomFieldData> getfamilyCustomFieldDataFor(Study study,Collection customFieldDisplaysThatWeNeed, List familyUIDsToBeIncluded);
	
	public List<CustomFieldDisplay> getCustomFieldDisplaysInWithCustomFieldType(List<String>fieldNameCollection,Study study,ArkFunction arkFunction,CustomFieldType customFieldType);

	public UserConfig getUserConfig(ArkUser arkUser, ConfigField configField);

	public ConfigField getConfigFieldByName(String configName);
	
	public UploadType getUploadTypeByModuleAndName(ArkModule arkModule,String name);
	
	public List<Search> getSearchesForSearch(Search search);
	
	public List<StudyComp> getStudyComponentsNeverUsedInThisSubject(Study study,LinkSubjectStudy linkSubjectStudy);
	
	public List<StudyComp> getDifferentStudyComponentsInConsentForSubject(Study study,LinkSubjectStudy linkSubjectStudy);
	
	public List<StudyCompStatus> getConsentStudyComponentStatusForStudyAndStudyComp(Study study, StudyComp studyComp);
	
	public List<ConsentStatus> getConsentStatusForStudyStudyCompAndStudyCompStatus(Study study, StudyComp studyComp,StudyCompStatus studyCompStatus);
	
	public List<Address> getPersonAddressList(Long personId, Address address) throws ArkSystemException;
	
	public void deleteUpload(final Upload upload);
}
