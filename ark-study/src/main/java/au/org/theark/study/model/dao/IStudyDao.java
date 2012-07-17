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
package au.org.theark.study.model.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkStudySubstudy;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SubjectVO;

public interface IStudyDao {
	
	/**
	 * Perform all inserts and updates as an atomic unit
	 * @param subjectsToInsert
	 * @param study
	 * @param subjectsToUpdate
	 */
	public void processBatch(List<LinkSubjectStudy> subjectsToInsert, Study study, List<LinkSubjectStudy> subjectsToUpdate);

	
	/**
	 * Perform all inserts and updates as an atomic unit
	 * @param subjectsToInsert
	 * @param study
	 * @param subjectsToUpdate
	 */
	public void processFieldsBatch(List<SubjectCustomFieldData> fieldsToUpdate, Study study, List<SubjectCustomFieldData> fieldsToInsert);

	/**
	 * This will take a list of detached LinkSubjectStudies (whcih will contain associated Persons, etc and insert them.
	 * @param subjectsToInsert
	 * @throws ArkUniqueException
	 * @throws ArkSubjectInsertException
	 */
	public List<LinkSubjectStudy> batchInsertSubjects(List<LinkSubjectStudy> subjectsToInsert, Study study) throws ArkUniqueException, ArkSubjectInsertException;
	
	/**
	 * Create a new study
	 * 
	 * @param study
	 */
	public void create(Study study);

	/**
	 * Create a new child study, and assign it's parent study accordingly
	 * 
	 * @param study
	 * @param selectedApplications
	 * @param parentStudy
	 */
	public void create(Study study, Collection<ArkModule> selectedApplications, Study parentStudy);

	/**
	 * Create a new study, and assign the user accordingly
	 * 
	 * @param study
	 * @param arkUserVo
	 * @param selectedModules
	 */
	public void create(Study study, ArkUserVO arkUserVo, Collection<ArkModule> selectedModules);

	public void updateStudy(Study study, Collection<ArkModule> selectedModules) throws CannotRemoveArkModuleException;

	public void create(StudyComp studyComponent) throws ArkSystemException, EntityExistsException;

	public void update(StudyComp studyComponent) throws ArkSystemException, EntityExistsException;

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved;

	/**
	 * Interface to get a list of Study Status reference data from the backend. These study status' are no associated with a study as such but can be
	 * used for displaying a list of options for a particular study.
	 * 
	 * @return
	 */
	public List<StudyStatus> getListOfStudyStatus();

	public Study getStudy(Long id);

	public void updateStudy(Study study);

	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException;

	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria);

	/**
	 * A look up that returns a list of All Phone Types. Mobile, Land etc In the event that there is a database/runtime error it is wrapped into a
	 * ArkSystemException and returned
	 * 
	 * @return List<PhoneType>
	 */
	public List<PhoneType> getListOfPhoneType();

	public void create(Phone phone);

	public void update(Phone phone);

	public void delete(Phone phone);

	public Collection<TitleType> getTitleType();

	public Collection<VitalStatus> getVitalStatus();

	public Collection<GenderType> getGenderTypes();

	public Collection<SubjectStatus> getSubjectStatus();

	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException, ArkSubjectInsertException;

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException;

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
	 * @throws EntityNotFoundException
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

	/**
	 * 
	 * @param address
	 * @throws ArkSystemException
	 */
	public void create(Address address) throws ArkSystemException;

	/**
	 * 
	 * @param address
	 * @throws ArkSystemException
	 */
	public void update(Address address) throws ArkSystemException;

	/**
	 * 
	 * @param address
	 * @throws ArkSystemException
	 */
	public void delete(Address address) throws ArkSystemException;

	/**
	 * 
	 * @param consent
	 * @throws ArkSystemException
	 */
	public void create(Consent consent) throws ArkSystemException;

	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException;

	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException;

	public Consent getConsent(Long id) throws ArkSystemException;

	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException, ArkSystemException;

	public List<Consent> searchConsent(ConsentVO consentVo) throws EntityNotFoundException, ArkSystemException;

	/**
	 * 
	 * @param consentFile
	 * @throws ArkSystemException
	 */
	public void create(ConsentFile consentFile) throws ArkSystemException;

	public void update(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;

	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;

	public List<ConsentFile> searchConsentFile(ConsentFile consentFile) throws EntityNotFoundException, ArkSystemException;

	public void createPersonLastnameHistory(Person person);

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

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId);

	/**
	 * Returns a list of PersonSurnameHistory
	 * 
	 * @return
	 */
	public List<PersonLastnameHistory> getLastnameHistory(Person person);

	/**
	 * 
	 * @param subjectFile
	 * @throws ArkSystemException
	 */
	public void create(SubjectFile subjectFile) throws ArkSystemException;

	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;

	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException;

	public void create(Correspondences correspondence) throws ArkSystemException;

	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;

	public List<Correspondences> getPersonCorrespondenceList(Long id, Correspondences correspondence) throws ArkSystemException;

	public List<CorrespondenceStatusType> getCorrespondenceStatusTypes();

	public List<CorrespondenceModeType> getCorrespondenceModeTypes();

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes();

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes();

// TODO trav might make this deprecated	public void batchInsertSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException;

//	public void batchUpdateSubjects(List<LinkSubjectStudy> subjectList) throws ArkUniqueException, ArkSubjectInsertException;

	/**
	 * Returns a Collection of ArkUser entities who are linked to a particular study.
	 * 
	 * @param study
	 * @return Collection<ArkUser>
	 */
	public Collection<ArkUser> lookupArkUser(Study study);

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException;

	public long getSubjectCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction);

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction, int first, int count);

	/**
	 * Create a single record of type SubjectCustomFieldData
	 * 
	 * @param subjectCustomFieldData
	 */
	public void createSubjectCustomFieldData(SubjectCustomFieldData subjectCustomFieldData);

	public void updateSubjectCustomFieldData(SubjectCustomFieldData subjectCustomFieldData);

	public void deleteSubjectCustomFieldData(SubjectCustomFieldData subjectCustomFieldData);

	public Long isCustomFieldUsed(SubjectCustomFieldData subjectCustomFieldData);

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

	public void update(LinkSubjectStudy linkSubjectStudy);

	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException;

	public Upload refreshUpload(Upload upload);

	public Upload getUpload(Long id);
	
	public GenderType getGenderType(Long id);
	
	public SubjectStatus getSubjectStatusByName(String name);
	
	public SubjectStatus getDefaultSubjectStatus();

	public TitleType getDefaultTitleType();

	public GenderType getDefaultGenderType();

	public VitalStatus getDefaultVitalStatus();

	public MaritalStatus getDefaultMaritalStatus();

	public ConsentOption getConsentOptionForBoolean(boolean trueForYesFalseForNo);
	public ConsentType getConsentTypeByName(String name);
	public ConsentStatus getConsentStatusByName(String name);

	public void setPreferredMailingAdressToFalse(Person person);

	public AddressType getDefaultAddressType();
	public AddressStatus getDefaultAddressStatus();
	public PhoneType getDefaultPhoneType();
	public PhoneStatus getDefaultPhoneStatus();
}
