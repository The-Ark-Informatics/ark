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

import org.apache.wicket.util.file.File;

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
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.CorrespondenceAttachment;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.LinkStudySubstudy;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.study.util.SubjectUploadValidator;

public interface IStudyService {

	/**
	 * 
	 * @param studyModelVo
	 */
	public void createStudy(StudyModelVO studyModelVo);
	
	/**
	 * Create a new study and assign the specified user
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
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId, Phone phone) throws  ArkSystemException;

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

	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException;

	public Consent getConsent(Long id) throws ArkSystemException;

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

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;

	/**
	 * 
	 * @param personId
	 * @param correspondence
	 * @return a list of correspondence for person.  if empty, returns an empty list
	 * @throws ArkSystemException
	 */
	public List<Correspondences> getPersonCorrespondenceList(Long personId, Correspondences correspondence) throws ArkSystemException;

	public void create(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException;

	public void update(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException;

	public void delete(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException;

	public List<CorrespondenceAttachment> searchCorrespondenceAttachment(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException;

	public List<CorrespondenceStatusType> getCorrespondenceStatusTypes();

	public List<CorrespondenceModeType> getCorrespondenceModeTypes();

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes();

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes();

	public void createPersonLastnameHistory(Person person);

	public void updatePersonLastnameHistory(Person person);

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory);

	public String getPreviousLastname(Person person);

	public String getCurrentLastname(Person person);

	public List<PersonLastnameHistory> getLastnameHistory(Person person);

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId);

	/**
	 * 
	 * @param subjectFile
	 * @throws ArkSystemException
	 */
	public void create(SubjectFile subjectFile) throws ArkSystemException;

	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;

	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException;

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved, UnAuthorizedOperation;

	public SubjectUploadValidator validateSubjectFileFormat(File file, String fileFormat, char delimChar);

	public SubjectUploadValidator validateSubjectFileFormat(InputStream inputStream, String fileFormat, char delimChar);

	public SubjectUploadValidator validateSubjectFileFormat(UploadVO uploadVo);

	public SubjectUploadValidator validateSubjectFileData(File file, String fileFormat, char delimChar);

	public SubjectUploadValidator validateSubjectFileData(InputStream inputStream, String fileFormat, char delimChar);

	public SubjectUploadValidator validateSubjectFileData(UploadVO uploadVo);

	public StringBuffer uploadAndReportMatrixSubjectFile(File file, String fileFormat, char delimChar);

	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream inputStream, long size, String fileFormat, char delimChar);

	public void batchInsertSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException;

	public void batchUpdateSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException;

	/**
	 * Returns a Collection of ArkUser entities from the backend for users who are linked to the passed in study.
	 * 
	 * @param study
	 * @return Collection<ArkUser>
	 */
	public Collection<ArkUser> lookupArkUser(Study study);

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException;

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction, int first, int count);
	
	public int getSubjectCustomFieldDataCount(LinkSubjectStudy criteria, ArkFunction arkFunction);
	/**
	 * Allows to Save(Insert) or Update  SubjectCustomFieldData. If there are SubjectCustomFieldData
	 * with no data value then it will discard it from the save/update process.
	 * @param subjectCustomFieldDataList
	 */
	public List<SubjectCustomFieldData>  createOrUpdateSubjectCustomFieldData(List<SubjectCustomFieldData> subjectCustomFieldDataList);
	
	/**
	 * Checks if the given component(study) is or has attachments linked to it
	 * @param studyComp
	 * @return
	 */
	public boolean isStudyComponentHasAttachments(StudyComp studyComp);
	
	public void cloneSubjectForSubStudy(LinkSubjectStudy linkSubjectStudy);
	
	public LinkStudySubstudy isSubStudy(Study study);
			
	/**
	 * Gets the list of child studies for the specifed parent Study
	 * @param study the parent study
	 * @return
	 */
	public List<Study> getChildStudyListOfParent(Study study);

	/**
	 * Return a list of LinkSubjectStudy ConsentHistory entities
	 * @param linkSubjectStudy
	 * @return
	 */
	public List<LssConsentHistory> getLssConsentHistoryList(LinkSubjectStudy linkSubjectStudy);
	
	/**
	 * Return a list of ConsentHistory entities
	 * @param linkSubjectStudy
	 * @return
	 */
	public List<ConsentHistory> getConsentHistoryList(LinkSubjectStudy linkSubjectStudy);
}
