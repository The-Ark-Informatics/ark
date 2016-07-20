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
package au.org.theark.study.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import au.org.theark.core.model.study.entity.*;
import org.apache.wicket.util.file.File;

import com.csvreader.CsvReader;

import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.study.model.capsule.ArkRelativeCapsule;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.model.vo.StudyCalendarVo;
import au.org.theark.study.util.SubjectUploadValidator;

public interface IStudyService {

	
	//public long countNumberOfUniqueSubjects(Study study, List subjects);
	
	
	/**
	 * 
	 * @param studyModelVo
	 */
	public void createStudy(StudyModelVO studyModelVo);

	/**
	 * Create a new study and assign the specified user
	 * 
	 * @param studyModelVo
	 * @param arkUserVo
	 */
	public void createStudy(StudyModelVO studyModelVo, ArkUserVO arkUserVo);

	public void updateStudy(StudyModelVO studyModelVo) throws CannotRemoveArkModuleException;

	public void archiveStudy(Study studyEntity) throws UnAuthorizedOperation, StatusNotAvailableException, ArkSystemException;

	/**
	 * Search for Study components with a certain criteria.
	 * 
	 * @param studyCompCriteria
	 * @return
	 */
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) throws ArkSystemException;

	public void create(StudyComp sc) throws UnAuthorizedOperation, ArkSystemException, EntityExistsException;

	public void update(StudyComp studyComponent) throws UnAuthorizedOperation, ArkSystemException, EntityExistsException;

	public void create(Phone phone) throws ArkSystemException, ArkUniqueException;

	public void update(Phone phone) throws ArkSystemException, ArkUniqueException;

	public void delete(Phone phone) throws ArkSystemException;

	/**
	 * A method to create a Subject.
	 * 
	 * @param subjectVO
	 * @throws ArkUniqueException
	 *            , ArkSubjectInsertException
	 */
	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException, ArkUniqueException, ArkSubjectInsertException;

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException,EntityNotFoundException;

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
	 * Look up the phones connected with the person(subject or contact)
	 * 
	 * @param personId
	 * @return List<Phone>
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId) throws ArkSystemException;

	/**
	 * Looks up the phones linked to a person and applies any filter supplied with the phone object.Used in Search Phone functionality. One can look up
	 * base don area code, phone type, phone number
	 * 
	 * @param personId
	 * @param phone
	 * @return
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId, Phone phone) throws ArkSystemException;

	/**
	 * Looks up the addresses linked to a person and applies any filter supplied with the address object.Used in Search Address functionality.
	 * 
	 * @param personId
	 * @param address
	 * @return
	 * @throws ArkSystemException
	 */
	public List<Address> getPersonAddressList(Long personId, Address address) throws ArkSystemException;

	public void create(Address address) throws ArkSystemException;

	public void update(Address address) throws ArkSystemException;

	public void delete(Address address) throws ArkSystemException;

	public void create(Consent consent) throws ArkSystemException;

	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException;
	
	public void update(Consent consent,boolean consentFile) throws ArkSystemException, EntityNotFoundException;

	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException;

	public Consent getConsent(Long id) throws ArkSystemException;

	/**
	 * for simplification's sake.   true is yes, false is no.  todo...look at whether booleans or something else are more appropriate in many of the situations
	 * @param trueOrFalse
	 * @return
	 * @throws ArkSystemException
	 */
	public ConsentOption getConsentOptionForBoolean(boolean trueForYesOrFalseForNo) throws ArkSystemException;

	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException, ArkSystemException;

	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException, ArkSystemException;

	/**
	 * 
	 * @param consentFile
	 * @throws ArkSystemException
	 */
	public void create(ConsentFile consentFile) throws ArkSystemException;

	public void update(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;

	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;

	public List<ConsentFile> searchConsentFile(ConsentFile consentFile) throws EntityNotFoundException, ArkSystemException;

	public void create(Correspondences correspondence) throws ArkSystemException;

	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;
	
	public void update(Correspondences correspondence, String checksum) throws ArkSystemException, EntityNotFoundException;

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;

	/**
	 * 
	 * @param personId
	 * @param correspondence
	 * @return a list of correspondence for person. if empty, returns an empty list
	 * @throws ArkSystemException
	 */
	public List<Correspondences> getCorrespondenceList(LinkSubjectStudy lss, Correspondences correspondence) throws ArkSystemException;

	public List<CorrespondenceModeType> getCorrespondenceModeTypes();

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes();

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes();

//	public void createPersonLastnameHistory(Person person);

//	public void updatePersonLastnameHistory(Person person);

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory);

	public String getPreviousLastname(Person person);

//	public String getCurrentLastname(Person person);

	public List<PersonLastnameHistory> getLastnameHistory(Person person);

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId);

	/**
	 * 
	 * @param subjectFile
	 * @throws ArkSystemException
	 */

	public void create(SubjectFile subjectFile) throws ArkSystemException;
	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;
	
	public void update(SubjectFile subjectFile, String checksum) throws ArkSystemException, EntityNotFoundException;

	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException;

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved, UnAuthorizedOperation;

	public SubjectUploadValidator validateSubjectFileFormat(File file, String fileFormat, char delimChar);

	public SubjectUploadValidator validateSubjectFileFormat(InputStream inputStream, String fileFormat, char delimChar);

	public SubjectUploadValidator validateSubjectFileFormat(UploadVO uploadVo);

	public SubjectUploadValidator validateSubjectFileData(InputStream inputStream, String fileFormat, char delimChar, List<String> referenceOfListStringsToUpdate);

	public SubjectUploadValidator validateSubjectFileData(UploadVO uploadVo, List<String> referenceOfListStringsToUpdate);

	public StringBuffer uploadAndReportMatrixSubjectFile(File file, String fileFormat, char delimChar, List<String> listOfUIDsToUpdate);

	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId,  List<String> listOfUIDsToUpdate);

	public StringBuffer uploadAndReportCustomDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId,  List<String> listOfUIDsToUpdate);
	

//TODO Trav Deprecated	public void batchInsertSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException;
//	public void batchInsertSubjects(List<LinkSubjectStudy> subjectList, Study study) throws ArkUniqueException, ArkSubjectInsertException;

	//public void batchUpdateSubjects(List<LinkSubjectStudy> subjectList) throws ArkUniqueException, ArkSubjectInsertException;

	/**
	 * Returns a Collection of ArkUser entities from the backend for users who are linked to the passed in study.
	 * 
	 * @param study
	 * @return Collection<ArkUser>
	 */
	public Collection<ArkUser> lookupArkUser(Study study);

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException;

	//public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction, int first, int count);

	public long getSubjectCustomFieldDataCount(LinkSubjectStudy criteria, ArkFunction arkFunction);
	
	public long getFamilyCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction);

	/**
	 * Allows to Save(Insert) or Update SubjectCustomFieldData. If there are SubjectCustomFieldData with no data value then it will discard it from the
	 * save/update process.
	 * 
	 * @param subjectCustomFieldDataList
	 */
	public List<SubjectCustomFieldData> createOrUpdateSubjectCustomFieldData(List<SubjectCustomFieldData> subjectCustomFieldDataList);
	
	public List<FamilyCustomFieldData> createOrUpdateFamilyCustomFieldData(List<FamilyCustomFieldData> familyCustomFieldDataList);

	/**
	 * Checks if the given component(study) is or has attachments linked to it
	 * 
	 * @param studyComp
	 * @return
	 */
	public boolean isStudyComponentHasAttachments(StudyComp studyComp);

	public void cloneSubjectForSubStudy(LinkSubjectStudy linkSubjectStudy);

	public LinkStudySubstudy isSubStudy(Study study);

	/**
	 * Gets the list of child studies for the specifed parent Study
	 * 
	 * @param study
	 *           the parent study
	 * @return
	 */
	public List<Study> getChildStudyListOfParent(Study study);

	/**
	 * Return a list of LinkSubjectStudy ConsentHistory entities
	 * 
	 * @param linkSubjectStudy
	 * @return
	 */
	public List<LssConsentHistory> getLssConsentHistoryList(LinkSubjectStudy linkSubjectStudy);

	/**
	 * Return a list of ConsentHistory entities
	 * 
	 * @param consent
	 * @return
	 */
	public List<ConsentHistory> getConsentHistoryList(Consent consent);


	/**
	 * Return the upload for the given id
	 * 
	 * @param consent
	 * @return
	 */
	public Upload getUpload(Long id);
	
	/**
	 * Refresh this entity with latest from db
	 * 
	 * @param consent
	 * @return
	 */
	public Upload refreshUpload(Upload upload);

	public SubjectStatus getDefaultSubjectStatus();

	public TitleType getDefaultTitleType();

	public GenderType getDefaultGenderType();

	public VitalStatus getDefaultVitalStatus();

	public MaritalStatus getDefaultMaritalStatus();

	public ConsentStatus getConsentStatusByName(String string);

	public ConsentType getConsentTypeByName(String string);

	public void setPreferredMailingAdressToFalse(Person person);
	
	public void processBatch(List<LinkSubjectStudy> subjectsToInsert, Study study, List<LinkSubjectStudy> subjectsToUpdate);

	public void processFieldsBatch(List<? extends ICustomFieldData> fieldDataList, Study study, List<? extends ICustomFieldData> fieldDataToInsert);
	
	/**
	 * Process the given {@link Consent} lists separately and persist the changes to the table. 
	 * @param updateConsentList
	 * @param insertConsentList
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void processSubjectConsentBatch(List<Consent> updateConsentList, List<Consent> insertConsentList) throws ArkSystemException,EntityNotFoundException;
	
	/**
	 * Insert the given {@link LinkSubjectPedigree} list. 
	 * @param parentsToInsert
	 * @param twinsToInsert
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void processPedigreeBatch(List<LinkSubjectPedigree> parentsToInsert,List<LinkSubjectTwin> twinsToInsert) throws ArkSystemException,EntityNotFoundException;

	/**
	 * Insert the given {@link SubjectFile} list
	 * @param subjectFiles
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void processSubjectAttachmentBatch(List<SubjectFile> subjectFiles)throws ArkSystemException,EntityNotFoundException;
	
	public AddressType getDefaultAddressType();

	public AddressStatus getDefaultAddressStatus();
	
	public PhoneType getDefaultPhoneType();

	public PhoneStatus getDefaultPhoneStatus();

	public EmailStatus getDefaultEmailStatus();

	public List<ConsentOption> getConsentOptions();

	public List<ConsentStatus> getConsentStatus();

	public List<ConsentType> getConsentType();
	
	public StringBuffer uploadAndReportSubjectConsentDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId);
	
	public StringBuffer uploadAndReportPedigreeDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId);
	
	public StringBuffer uploadAndReportSubjectAttachmentDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId, String user_id);
	
	public RelativeCapsule[] generateSubjectPedigreeImageList(final String subjectUID,final Long studyId);
	
	public RelativeCapsule[] generateSubjectPedigreeExportList(final String subjectUID,final Long studyId);
	
	public List<RelationshipVo> generateSubjectPedigreeRelativeList(final String subjectUID,final Long studyId);
	
	public void deleteRelationship(final LinkSubjectPedigree relationship);
	
	public void create(LinkSubjectPedigree pedigree);
	
	public List<RelationshipVo> getSubjectPedigreeTwinList(final String subjectUID,final Long studyId);
	
	public void processPedigreeTwinRelationship(final RelationshipVo relationshipVo, final String subjectUid, final Long studyId);
	
	public List<TwinType> getTwinTypes();
	
	public long getRelationshipCount(final String subjectUID,final Long studyId);
	
	public List<LinkSubjectTwin> getTwins(final Set<String> subjectUids,final Long studyId);
	
	public ArkRelativeCapsule[] generateSubjectArkPedigreeExportList(final String subjectUID,final Long studyId);
	
	public List<CustomField> getBinaryCustomFieldsForPedigreeRelativesList(Long studyId);
	
	public StudyPedigreeConfiguration getStudyPedigreeConfiguration(Long studyId);
	
	public void saveOrUpdateStudyPedigreeConfiguration(StudyPedigreeConfiguration config);
	
	public List<Phone> pageablePersonPhoneList(Long personId,Phone phoneCriteria, int first, int count);
	
	public List<Address> pageablePersonAddressList(Long personId,Address addressCriteria, int first, int count);
	
	public List<CustomField> getFamilyUIdCustomFieldsForPedigreeRelativesList(Long studyId);
	
	public List<FamilyCustomFieldData> getFamilyCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction,CustomFieldCategory customFieldCategory,CustomFieldType customFieldType, int first, int count);

	public String getSubjectFamilyId(Long studyId, String subjectUID);
	
	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction,CustomFieldCategory customFieldCategory,CustomFieldType customFieldType, int first, int count);
	
	public void setPreferredPhoneNumberToFalse(Person person);

	public void saveOrUpdate(StudyCalendar studyCalendar);

	public void delete(StudyCalendar studyCalendar);
	
	public List<StudyCalendar> searchStudyCalenderList(StudyCalendar studyCalendar);
	
	public List<CustomField> getStudySubjectCustomFieldList(Long studyId);
	
	public void saveOrUpdate(StudyCalendarVo studyCalendarVo);
	
	public List<CustomField> getSelectedCalendarCustomFieldList(StudyCalendar studyCalendar);
	
	public List<RelationshipVo> getSubjectChildren(String subjectUID, long studyId);
	public void delete(OtherID otherID);
	public boolean isStudyComponentBeingUsedInConsent(StudyComp studyComp);
}
