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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.file.File;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.CorrespondenceAttachment;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.study.model.dao.IStudyDao;
import au.org.theark.study.util.SubjectUploadValidator;
import au.org.theark.study.util.SubjectUploader;
import au.org.theark.study.web.Constants;

@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService {

	private static Logger		log	= LoggerFactory.getLogger(StudyServiceImpl.class);

	private IArkCommonService	arkCommonService;
	private IStudyDao				studyDao;

	/* To access Hibernate Study Dao */
	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}

	public IArkCommonService getArkCommonService() {
		return arkCommonService;
	}

	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}

	public List<StudyStatus> getListOfStudyStatus() {
		return studyDao.getListOfStudyStatus();
	}

	public void createStudy(StudyModelVO studyModelVo) {
		// Create the study group in the LDAP for the selected applications and also add the roles to each of the application.
		studyDao.create(studyModelVo.getStudy(), studyModelVo.getSelectedArkModules());
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Study " + studyModelVo.getStudy().getName());
		ah.setEntityId(studyModelVo.getStudy().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		arkCommonService.createAuditHistory(ah);
	}

	public void updateStudy(StudyModelVO studyModelVo) throws CannotRemoveArkModuleException {

		studyDao.updateStudy(studyModelVo.getStudy(), studyModelVo.getSelectedArkModules());

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Study " + studyModelVo.getStudy().getName());
		ah.setEntityId(studyModelVo.getStudy().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		arkCommonService.createAuditHistory(ah);

	}

	/**
	 * This will mark the study as archived.
	 */
	public void archiveStudy(Study studyEntity) throws UnAuthorizedOperation, StatusNotAvailableException, ArkSystemException {
		// For archive, set the status to Archived and then issue an update
		StudyStatus status = studyDao.getStudyStatus(au.org.theark.study.service.Constants.STUDY_STATUS_ARCHIVE);
		studyEntity.setStudyStatus(status);
		studyDao.updateStudy(studyEntity);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Archived Study " + studyEntity.getName());
		ah.setEntityId(studyEntity.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		arkCommonService.createAuditHistory(ah);
	}

	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) throws ArkSystemException {
		return studyDao.searchStudyComp(studyCompCriteria);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void create(StudyComp studyComponent) throws UnAuthorizedOperation, ArkSystemException, EntityExistsException {
		try{
			studyDao.create(studyComponent);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);

			ah.setComment("Created Study Component " + studyComponent.getName());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
			ah.setStudyStatus(studyComponent.getStudy().getStudyStatus());
			ah.setEntityId(studyComponent.getId());
			arkCommonService.createAuditHistory(ah);
		}catch (ConstraintViolationException cvex) {
			log.error("Study Component already exists.: " + cvex);
			throw new EntityExistsException("A Study Component already exits.");
		}
		catch (Exception ex) {
			log.error("Problem creating Study Component: " + ex);
			throw new ArkSystemException("Problem creating Study Component: " + ex.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(StudyComp studyComponent) throws UnAuthorizedOperation, ArkSystemException, EntityExistsException {
		try{
			studyDao.update(studyComponent);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
	
			ah.setComment("Updated Study Component " + studyComponent.getName());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
			ah.setStudyStatus(studyComponent.getStudy().getStudyStatus());
			ah.setEntityId(studyComponent.getId());
			arkCommonService.createAuditHistory(ah);
		}catch (ConstraintViolationException cvex) {
			log.error("Study Component already exists.: " + cvex);
			throw new EntityExistsException("A Study Component already exists.");
		}
		catch (Exception ex) {
			log.error("Problem updating Study Component: " + ex);
			throw new ArkSystemException("Problem updating Study Component: " + ex.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void create(Phone phone) throws ArkUniqueException, ArkSystemException {

		try {
			studyDao.create(phone);
		}
		catch (ConstraintViolationException cvex) {
			log.error("Problem creating phone record: " + cvex);
			// the following ArkUniqueException message will be shown to the user
			throw new ArkUniqueException("Failed saving: New phone number is not unique for this person");
		}
		catch (Exception ex) {
			log.error("Problem creating phone record: " + ex);
			throw new ArkSystemException("Problem creating phone record: " + ex.getMessage());
		}

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Phone " + phone.getPhoneNumber());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHONE);
		ah.setEntityId(phone.getId());
		arkCommonService.createAuditHistory(ah);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Phone phone) throws ArkSystemException, ArkUniqueException {
		try {
			studyDao.update(phone);
		}
		catch (ConstraintViolationException cvex) {
			log.error("Problem updating phone record: " + cvex);
			// the following ArkUniqueException message will be shown to the user
			throw new ArkUniqueException("Failed saving: Phone number already exists for this person");
		}
		catch (Exception ex) {
			log.error("Problem updating phone record: " + ex);
			throw new ArkSystemException("Problem updating phone record: " + ex.getMessage());
		}

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);

		ah.setComment("Updated Phone " + phone.getPhoneNumber());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHONE);
		ah.setEntityId(phone.getId());
		arkCommonService.createAuditHistory(ah);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Phone phone) throws ArkSystemException {
		try {
			studyDao.delete(phone);
		}
		catch (Exception ex) {
			log.error("An Exception occured while trying to delete this Phone record. Cause: " + ex);
			throw new ArkSystemException("Unable to delete a Phone record.");
		}

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Phone " + phone.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHONE);
		ah.setEntityId(phone.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException, ArkSubjectInsertException {

		studyDao.createSubject(subjectVO);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Subject " + subjectVO.getLinkSubjectStudy().getSubjectUID());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT);
		ah.setEntityId(subjectVO.getLinkSubjectStudy().getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException {

		studyDao.updateSubject(subjectVO);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Subject " + subjectVO.getLinkSubjectStudy().getSubjectUID());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT);
		ah.setEntityId(subjectVO.getLinkSubjectStudy().getId());
		arkCommonService.createAuditHistory(ah);
	}

	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can
	 * represent a subject or contact.
	 * 
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException {
		return studyDao.getPerson(personId);
	}

	/**
	 * Look up the phones connected with the person(subject or contact)
	 * 
	 * @param personId
	 * @return List<Phone>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId) throws EntityNotFoundException, ArkSystemException {
		return studyDao.getPersonPhoneList(personId);
	}

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
	public List<Phone> getPersonPhoneList(Long personId, Phone phone) throws EntityNotFoundException, ArkSystemException {
		return studyDao.getPersonPhoneList(personId, phone);
	}

	/**
	 * Looks up the addresses linked to a person and applies any filter supplied with the address object.Used in Search Address functionality.
	 * 
	 * @param personId
	 * @param address
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Address> getPersonAddressList(Long personId, Address address) throws EntityNotFoundException, ArkSystemException {
		return studyDao.getPersonAddressList(personId, address);
	}

	public void create(Address address) throws ArkSystemException {
		studyDao.create(address);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Address " + address.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void update(Address address) throws ArkSystemException {
		studyDao.update(address);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Address " + address.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void delete(Address address) throws ArkSystemException {
		// Add business rules to check if this address is in use/active and referred elsewhere
		studyDao.delete(address);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Address " + address.getStreetAddress());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void create(Consent consent) throws ArkSystemException {
		studyDao.create(consent);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException, ArkSystemException {
		return studyDao.searchConsent(consent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.study.service.IStudyService#update(au.org.theark.core.model.study.entity.Consent)
	 */
	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException {
		studyDao.update(consent);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException, ArkSystemException {
		return studyDao.searchConsent(consentVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.study.service.IStudyService#delete(au.org.theark.core.model.study.entity.Consent)
	 */
	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException {
		studyDao.delete(consent);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public Consent getConsent(Long id) throws ArkSystemException {
		return studyDao.getConsent(id);
	}


	/*** correspondence service functions ***/
	public void create(Correspondences correspondence) throws ArkSystemException {
		studyDao.create(correspondence);
	}

	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		studyDao.update(correspondence);
	}

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		studyDao.delete(correspondence);
	}

	public List<Correspondences> getPersonCorrespondenceList(Long personId, Correspondences correspondence) throws EntityNotFoundException, ArkSystemException {
		return studyDao.getPersonCorrespondenceList(personId, correspondence);
	}

	public void create(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException {
		studyDao.create(correspondenceAttachment);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created CorrespondenceAttachment " + correspondenceAttachment.getId());
		ah.setEntityType("Correspondence Attachment");
		ah.setEntityId(correspondenceAttachment.getId());
		arkCommonService.createAuditHistory(ah);

	}

	public void update(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException {
		studyDao.update(correspondenceAttachment);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated CorrespondenceAttachment " + correspondenceAttachment.getId());
		ah.setEntityType("Correspondence Attachment");
		ah.setEntityId(correspondenceAttachment.getId());
		arkCommonService.createAuditHistory(ah);

	}

	public void delete(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException {
		studyDao.delete(correspondenceAttachment);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted CorrespondenceAttachment " + correspondenceAttachment.getId());
		ah.setEntityType("Correspondence Attachment");
		ah.setEntityId(correspondenceAttachment.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<CorrespondenceAttachment> searchCorrespondenceAttachment(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException {

		return studyDao.searchCorrespondenceAttachment(correspondenceAttachment);
	}

	public List<CorrespondenceStatusType> getCorrespondenceStatusTypes() {
		return studyDao.getCorrespondenceStatusTypes();
	}

	public List<CorrespondenceModeType> getCorrespondenceModeTypes() {
		return studyDao.getCorrespondenceModeTypes();
	}

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes() {
		return studyDao.getCorrespondenceDirectionTypes();
	}

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes() {
		return studyDao.getCorrespondenceOutcomeTypes();
	}

	public void create(ConsentFile consentFile) throws ArkSystemException {
		studyDao.create(consentFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void update(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException {
		studyDao.update(consentFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException {
		studyDao.delete(consentFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<ConsentFile> searchConsentFile(ConsentFile consentFile) throws EntityNotFoundException, ArkSystemException {
		return studyDao.searchConsentFile(consentFile);
	}

	public void createPersonLastnameHistory(Person person) {
		studyDao.createPersonLastnameHistory(person);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PersonLastnameHistory " + person.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY);
		ah.setEntityId(person.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void updatePersonLastnameHistory(Person person) {
		studyDao.updatePersonLastnameHistory(person);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Updated PersonLastnameHistory " + person.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY);
		ah.setEntityId(person.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<PersonLastnameHistory> getLastnameHistory(Person person) {
		return studyDao.getLastnameHistory(person);
	}

	public String getPreviousLastname(Person person) {
		return studyDao.getPreviousLastname(person);
	}

	public String getCurrentLastname(Person person) {
		return studyDao.getCurrentLastname(person);
	}

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory) {
		return studyDao.getPreviousSurnameHistory(personSurnameHistory);
	}

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId) {
		return studyDao.personHasPreferredMailingAddress(person, currentAddressId);
	}

	public void create(SubjectFile subjectFile) throws ArkSystemException {
		studyDao.create(subjectFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException {
		studyDao.update(subjectFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException {
		studyDao.delete(subjectFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException {
		return studyDao.searchSubjectFile(subjectFile);
	}

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved, UnAuthorizedOperation {
		studyDao.delete(studyComp);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);

		ah.setComment("Deleted Study Component " + studyComp.getName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
		ah.setStudyStatus(studyComp.getStudy().getStudyStatus());
		ah.setEntityId(studyComp.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public Collection<StudyUpload> searchUpload(StudyUpload searchUpload) {
		return studyDao.searchUpload(searchUpload);
	}

	public void createUpload(StudyUpload studyUpload) {
		studyDao.createUpload(studyUpload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created studyUpload " + studyUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_UPLOAD);
		ah.setEntityId(studyUpload.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void deleteUpload(StudyUpload studyUpload) {
		studyDao.deleteUpload(studyUpload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted studyUpload " + studyUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_UPLOAD);
		ah.setEntityId(studyUpload.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void updateUpload(StudyUpload studyUpload) {
		studyDao.updateUpload(studyUpload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated studyUpload " + studyUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_UPLOAD);
		ah.setEntityId(studyUpload.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public StringBuffer uploadAndReportMatrixSubjectFile(File file, String fileFormat, char delimChar) {
		StringBuffer uploadReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = arkCommonService.getStudy(studyId);

		SubjectUploader subjectUploader = new SubjectUploader(study, arkCommonService, this);

		try {
			InputStream is = new FileInputStream(file);

			log.debug("Importing and reporting Subject file");
			uploadReport = subjectUploader.uploadAndReportMatrixSubjectFile(is, file.length(), fileFormat, delimChar);
		}
		catch (IOException ioe) {
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return uploadReport;
	}

	public SubjectUploadValidator validateSubjectFileData(File file, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(arkCommonService);

		try {
			log.debug("Validating Subject file data");
			InputStream is = new FileInputStream(file);
			validationMessages = subjectUploadValidator.validateMatrixSubjectFileData(is, file.length(), fileFormat, delimChar);
		}
		catch (IOException ioe) {
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return subjectUploadValidator;
	}

	public SubjectUploadValidator validateSubjectFileFormat(File file, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(arkCommonService);

		try {
			log.debug("Validating Subject file format");
			InputStream is = new FileInputStream(file);
			validationMessages = subjectUploadValidator.validateSubjectMatrixFileFormat(is, file.length(), fileFormat, delimChar);
		}
		catch (IOException ioe) {
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return subjectUploadValidator;
	}

	public SubjectUploadValidator validateSubjectFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(arkCommonService);

		try {
			validationMessages = subjectUploadValidator.validateSubjectMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return subjectUploadValidator;
	}

	public SubjectUploadValidator validateSubjectFileData(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = arkCommonService.getStudy(studyId);
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(arkCommonService);

		try {
			log.debug("Validating Subject file data");
			validationMessages = subjectUploadValidator.validateMatrixSubjectFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return subjectUploadValidator;
	}

	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream inputStream, long size, String fileFormat, char delimChar) {
		StringBuffer uploadReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = arkCommonService.getStudy(studyId);

		SubjectUploader subjectUploader = new SubjectUploader(study, arkCommonService, this);

		try {
			log.debug("Importing and reporting Subject file");
			uploadReport = subjectUploader.uploadAndReportMatrixSubjectFile(inputStream, size, fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return uploadReport;
	}

	public SubjectUploadValidator validateSubjectFileFormat(UploadVO uploadVo) {
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(arkCommonService);
		subjectUploadValidator.validateSubjectFileFormat(uploadVo);
		return subjectUploadValidator;
	}

	public SubjectUploadValidator validateSubjectFileData(UploadVO uploadVo) {
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(arkCommonService);
		subjectUploadValidator.validateSubjectFileData(uploadVo);
		return subjectUploadValidator;
	}

	public void batchInsertSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException {
		studyDao.batchInsertSubjects(subjectVoCollection);
	}

	public void batchUpdateSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException {
		studyDao.batchUpdateSubjects(subjectVoCollection);
	}

	public Collection<ArkUser> lookupArkUser(Study study) {
		return studyDao.lookupArkUser(study);
	}

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException {
		return studyDao.getSubjectLinkedToStudy(personId, study);
	}

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction, int first, int count){
		List<SubjectCustomFieldData> customfieldDataList = new ArrayList<SubjectCustomFieldData>();
		customfieldDataList  = studyDao.getSubjectCustomFieldDataList(linkSubjectStudyCriteria, arkFunction, first, count);
		return customfieldDataList;
	}
	
	/**
	 * 
	 */
	public int getSubjectCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction) {
		return   studyDao.getSubjectCustomFieldDataCount(linkSubjectStudyCriteria, arkFunction);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<SubjectCustomFieldData> createOrUpdateSubjectCustomFieldData(List<SubjectCustomFieldData> subjectCustomFieldDataList){
		
		List<SubjectCustomFieldData> listOfExceptions = new ArrayList<SubjectCustomFieldData>();
		/* Iterate the list and call DAO to persist each Item */
		for (SubjectCustomFieldData subjectCustomFieldData : subjectCustomFieldDataList) {
			
			
			try{
			/* Insert the Field if it does not have a  ID and has the required fields */
				if( canInsert(subjectCustomFieldData)) {
		
					studyDao.createSubjectCustomFieldData(subjectCustomFieldData);
					Long id = subjectCustomFieldData.getCustomFieldDisplay().getCustomField().getId();
					
					CustomField customField = arkCommonService.getCustomField(id);
					customField.setCustomFieldHasData(true);
					CustomFieldVO customFieldVO = new CustomFieldVO();
					customFieldVO.setCustomField(customField);
					
					arkCommonService.updateCustomField(customFieldVO);

				}else if(canUpdate(subjectCustomFieldData)){
					
					//If there was bad data uploaded and the user has now corrected it on the front end then set/blank out the error data value and updated the record.
					if(subjectCustomFieldData.getErrorDataValue() != null){
						subjectCustomFieldData.setErrorDataValue(null);
					} 
					studyDao.updateSubjectCustomFieldData(subjectCustomFieldData);
				
				}else if(canDelete(subjectCustomFieldData)){
					//Check if the CustomField is used by anyone else and if not set the customFieldHasData to false;
					Long count  = studyDao.isCustomFieldUsed(subjectCustomFieldData);
					
					studyDao.deleteSubjectCustomFieldData(subjectCustomFieldData);
					if(count <= 1){
						//Then update the CustomField's hasDataFlag to false;
						Long id = subjectCustomFieldData.getCustomFieldDisplay().getCustomField().getId();//Reload since the session was closed in the front end and the child objects won't be lazy loaded
						CustomField customField = arkCommonService.getCustomField(id);
						customField.setCustomFieldHasData(false);
						CustomFieldVO customFieldVO = new CustomFieldVO();
						customFieldVO.setCustomField(customField);
						arkCommonService.updateCustomField(customFieldVO); //Update it
						
					}
				}
			}catch(Exception someException){
				listOfExceptions.add(subjectCustomFieldData);//Continue with rest of the list
			}
		}
		
		return listOfExceptions;
	}
	
	/**
	 * In order to delete it must satisfy the following conditions
	 * 1. SubjectCustomFieldData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. SubjectCustomFieldData should have a valid Subject linked to it and must not be null AND
	 * 3. SubjectCustomFieldData.TextDataValue is NULL OR is EMPTY
	 * 4. SubjectCustomFieldData.NumberDataValue is NULL
	 * 5. SubjectCustomFieldData.DatewDataValue is NULL
	 * When these conditiosn are satisfied this method will return Boolean TRUE
	 * @param subjectCustomFieldData
	 * @return
	 */
	private Boolean canDelete(SubjectCustomFieldData subjectCustomFieldData){
		Boolean flag = false;
		
		if(subjectCustomFieldData.getId() != null &&  subjectCustomFieldData.getLinkSubjectStudy() != null && 
				( subjectCustomFieldData.getTextDataValue() == null  	||		
				  subjectCustomFieldData.getTextDataValue().isEmpty()  	|| 
				  subjectCustomFieldData.getNumberDataValue() == null 	||
				  subjectCustomFieldData.getDateDataValue() == null ) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Update a SubjectCustomFieldData instance the following conditions must be met
	 * 1. SubjectCustomFieldData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. SubjectCustomFieldData should have a valid Subject linked to it and must not be null AND
	 * 3. SubjectCustomFieldData.TextDataValue is NOT NULL AND NOT EMPTY OR
	 * 4. SubjectCustomFieldData.NumberDataValue is NOT NULL
	 * 5. SubjectCustomFieldData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param subjectCustomFieldData
	 * @return
	 */
	private Boolean canUpdate(SubjectCustomFieldData subjectCustomFieldData){
		Boolean flag = false;
		
		if(subjectCustomFieldData.getId() != null && subjectCustomFieldData.getLinkSubjectStudy() != null && 
				(( subjectCustomFieldData.getTextDataValue() != null 	&& 
				   !subjectCustomFieldData.getTextDataValue().isEmpty()) || 
				   subjectCustomFieldData.getDateDataValue() != null  	|| 
				   subjectCustomFieldData.getNumberDataValue() != null) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Insert a SubjectCustomFieldData instance the following conditions must be met.
	 * 1. SubjectCustomFieldData must be a transient entity(Not yet associated with an ID/PK) AND
	 * 2. SubjectCustomFieldData should have a valid Subject linked to it and must not be null AND
	 * 3. SubjectCustomFieldData.TextDataValue is NOT NULL  OR
	 * 4. SubjectCustomFieldData.NumberDataValue is NOT NULL OR
	 * 5. SubjectCustomFieldData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param subjectCustomFieldData
	 * @return
	 */
	private Boolean canInsert(SubjectCustomFieldData subjectCustomFieldData){
		Boolean flag = false;
		
		if(subjectCustomFieldData.getId() == null &&  subjectCustomFieldData.getLinkSubjectStudy() != null && 
				(		subjectCustomFieldData.getNumberDataValue() != null || 
						subjectCustomFieldData.getTextDataValue() != null 	|| 
						subjectCustomFieldData.getDateDataValue() != null )){
			
			flag=true;
			
		}
		return flag;
	}


}
