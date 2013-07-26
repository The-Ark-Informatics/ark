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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.collections.ArrayListStack;
import org.apache.wicket.util.file.File;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IAuditDao;
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
import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkStudySubstudy;
import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
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
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.model.dao.IStudyDao;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.util.ConsentHistoryComparator;
import au.org.theark.study.util.DataUploader;
import au.org.theark.study.util.LinkSubjectStudyConsentHistoryComparator;
import au.org.theark.study.util.SubjectUploadValidator;
import au.org.theark.study.web.Constants;

@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService {

	private static Logger		log	= LoggerFactory.getLogger(StudyServiceImpl.class);

	private IArkCommonService	iArkCommonService;
	private IUserService			iUserService;
	private IStudyDao				iStudyDao;
	private IAuditDao				iAuditDao;

	public IArkCommonService getiArkCommonService() {
		return iArkCommonService;
	}

	@Autowired
	public void setiArkCommonService(IArkCommonService iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	/**
	 * @return the iUserService
	 */
	public IUserService getiUserService() {
		return iUserService;
	}

	/**
	 * @param iUserService
	 *           the iUserService to set
	 */
	@Autowired
	public void setiUserService(IUserService iUserService) {
		this.iUserService = iUserService;
	}

	/* To access Hibernate Study Dao */
	@Autowired
	public void setiStudyDao(IStudyDao iStudyDao) {
		this.iStudyDao = iStudyDao;
	}

	public IStudyDao getiStudyDao() {
		return iStudyDao;
	}

	/**
	 * @return the iAuditDao
	 */
	public IAuditDao getiAuditDao() {
		return iAuditDao;
	}

	/**
	 * @param iAuditDao
	 *           the iAuditDao to set
	 */
	@Autowired
	public void setiAuditDao(IAuditDao iAuditDao) {
		this.iAuditDao = iAuditDao;
	}

	public List<StudyStatus> getListOfStudyStatus() {
		return iStudyDao.getListOfStudyStatus();
	}

	public void createStudy(StudyModelVO studyModelVo) {
		// Create the study group in the LDAP for the selected applications and also add the roles to each of the application.
		iStudyDao.create(studyModelVo.getStudy(), studyModelVo.getSelectedArkModules(), studyModelVo.getStudy().getParentStudy());
		BiospecimenUidTemplate template = studyModelVo.getBiospecimenUidTemplate();
		if (template != null && template.getBiospecimenUidPadChar() != null && template.getBiospecimenUidPrefix() != null && template.getBiospecimenUidToken() != null) {
			template.setStudy(studyModelVo.getStudy());
			iArkCommonService.createBiospecimenUidTemplate(template);
		}

		BioCollectionUidTemplate bioCollectionUidTemplate = studyModelVo.getBioCollectionUidTemplate();
		if (bioCollectionUidTemplate != null && bioCollectionUidTemplate.getBioCollectionUidPadChar() != null && bioCollectionUidTemplate.getBioCollectionUidToken() != null) {
			bioCollectionUidTemplate.setStudy(studyModelVo.getStudy());
			iArkCommonService.createBioCollectionUidTemplate(bioCollectionUidTemplate);
		}

		Collection<SubjectVO> selectedSubjects = studyModelVo.getSelectedSubjects();
		if (selectedSubjects != null && selectedSubjects.size() > 0) {
			for (SubjectVO subjectVO : selectedSubjects) {
				LinkSubjectStudy lss = new LinkSubjectStudy();
				lss.setStudy(studyModelVo.getStudy());// Current Study
				lss.setPerson(subjectVO.getLinkSubjectStudy().getPerson());
				lss.setSubjectUID(subjectVO.getLinkSubjectStudy().getSubjectUID());
				lss.setSubjectStatus(subjectVO.getLinkSubjectStudy().getSubjectStatus());
				cloneSubjectForSubStudy(lss);
			}
		}

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Study " + studyModelVo.getStudy().getName());
		ah.setEntityId(studyModelVo.getStudy().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		iArkCommonService.createAuditHistory(ah);
	}

	public void createStudy(StudyModelVO studyModelVo, ArkUserVO arkUserVo) {
		// Create the study group in the LDAP for the selected applications and also add the roles to each of the application.
		iStudyDao.create(studyModelVo.getStudy(), arkUserVo, studyModelVo.getSelectedArkModules());
		BiospecimenUidTemplate template = studyModelVo.getBiospecimenUidTemplate();
		if (template != null && template.getBiospecimenUidPadChar() != null && template.getBiospecimenUidPrefix() != null && template.getBiospecimenUidToken() != null) {
			template.setStudy(studyModelVo.getStudy());
			iArkCommonService.createBiospecimenUidTemplate(template);
		}

		BioCollectionUidTemplate bioCollectionUidTemplate = studyModelVo.getBioCollectionUidTemplate();
		if (bioCollectionUidTemplate != null && bioCollectionUidTemplate.getBioCollectionUidPadChar() != null && bioCollectionUidTemplate.getBioCollectionUidToken() != null) {
			bioCollectionUidTemplate.setStudy(studyModelVo.getStudy());
			iArkCommonService.createBioCollectionUidTemplate(bioCollectionUidTemplate);
		}

		Collection<SubjectVO> selectedSubjects = studyModelVo.getSelectedSubjects();
		if (selectedSubjects != null && selectedSubjects.size() > 0) {
			for (SubjectVO subjectVO : selectedSubjects) {
				LinkSubjectStudy lss = new LinkSubjectStudy();
				lss.setStudy(studyModelVo.getStudy());// Current Study
				lss.setPerson(subjectVO.getLinkSubjectStudy().getPerson());
				lss.setSubjectUID(subjectVO.getLinkSubjectStudy().getSubjectUID());
				lss.setSubjectStatus(subjectVO.getLinkSubjectStudy().getSubjectStatus());
				cloneSubjectForSubStudy(lss);
			}
		}

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Study " + studyModelVo.getStudy().getName());
		ah.setEntityId(studyModelVo.getStudy().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateStudy(StudyModelVO studyModelVo) throws CannotRemoveArkModuleException {

		iStudyDao.updateStudy(studyModelVo.getStudy(), studyModelVo.getSelectedArkModules());

		if (!iArkCommonService.studyHasBiospecimen(studyModelVo.getStudy())) {
			// Defensive check to make sure no biospecimens are attached to the study
			BiospecimenUidTemplate template = studyModelVo.getBiospecimenUidTemplate();
			if (template != null && template.getBiospecimenUidPadChar() != null && template.getBiospecimenUidPrefix() != null && template.getBiospecimenUidToken() != null) {
				template.setStudy(studyModelVo.getStudy());
				iArkCommonService.updateBiospecimenUidTemplate(template);
			}
		}

		if (!iArkCommonService.studyHasBioCollection(studyModelVo.getStudy())) {
			BioCollectionUidTemplate bioCollectionUidTemplate = studyModelVo.getBioCollectionUidTemplate();
			if (bioCollectionUidTemplate != null && bioCollectionUidTemplate.getBioCollectionUidPadChar() != null && bioCollectionUidTemplate.getBioCollectionUidToken() != null) {
				bioCollectionUidTemplate.setStudy(studyModelVo.getStudy());
				iArkCommonService.updateBioCollectionUidTemplate(bioCollectionUidTemplate);
			}
		}

		Collection<SubjectVO> selectedSubjects = studyModelVo.getSelectedSubjects();
		for (SubjectVO subjectVO : selectedSubjects) {
			LinkSubjectStudy lss = new LinkSubjectStudy();
			lss.setStudy(studyModelVo.getStudy());// Current Study
			lss.setPerson(subjectVO.getLinkSubjectStudy().getPerson());
			lss.setSubjectUID(subjectVO.getLinkSubjectStudy().getSubjectUID());
			lss.setSubjectStatus(subjectVO.getLinkSubjectStudy().getSubjectStatus());
			cloneSubjectForSubStudy(lss);
		}

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Study " + studyModelVo.getStudy().getName());
		ah.setEntityId(studyModelVo.getStudy().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		iArkCommonService.createAuditHistory(ah);

	}

	/**
	 * This will mark the study as archived.
	 */
	public void archiveStudy(Study studyEntity) throws UnAuthorizedOperation, StatusNotAvailableException, ArkSystemException {
		// For archive, set the status to Archived and then issue an update
		StudyStatus status = iStudyDao.getStudyStatus(au.org.theark.study.service.Constants.STUDY_STATUS_ARCHIVE);
		studyEntity.setStudyStatus(status);
		iStudyDao.updateStudy(studyEntity);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Archived Study " + studyEntity.getName());
		ah.setEntityId(studyEntity.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		iArkCommonService.createAuditHistory(ah);
	}

	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) throws ArkSystemException {
		return iStudyDao.searchStudyComp(studyCompCriteria);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void create(StudyComp studyComponent) throws UnAuthorizedOperation, ArkSystemException, EntityExistsException {
		try {
			iStudyDao.create(studyComponent);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);

			ah.setComment("Created Study Component " + studyComponent.getName());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
			ah.setStudyStatus(studyComponent.getStudy().getStudyStatus());
			ah.setEntityId(studyComponent.getId());
			iArkCommonService.createAuditHistory(ah);
		}
		catch (ConstraintViolationException cvex) {
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
		try {
			iStudyDao.update(studyComponent);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);

			ah.setComment("Updated Study Component " + studyComponent.getName());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
			ah.setStudyStatus(studyComponent.getStudy().getStudyStatus());
			ah.setEntityId(studyComponent.getId());
			iArkCommonService.createAuditHistory(ah);
		}
		catch (ConstraintViolationException cvex) {
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
			iStudyDao.create(phone);
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
		iArkCommonService.createAuditHistory(ah);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Phone phone) throws ArkSystemException, ArkUniqueException {
		try {
			iStudyDao.update(phone);
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
		iArkCommonService.createAuditHistory(ah);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Phone phone) throws ArkSystemException {
		try {
			iStudyDao.delete(phone);
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
		iArkCommonService.createAuditHistory(ah);
	}

	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException, ArkSubjectInsertException {
		iStudyDao.createSubject(subjectVO);
		
		//Consent history record create only when user has update one of the consent history fields.
		if (subjectVO.getLinkSubjectStudy().getConsentToActiveContact() != null || 
				subjectVO.getLinkSubjectStudy().getConsentToPassiveDataGathering() != null || 
				subjectVO.getLinkSubjectStudy().getConsentToUseData() != null || 
				subjectVO.getLinkSubjectStudy().getConsentStatus() != null || 
				subjectVO.getLinkSubjectStudy().getConsentType() != null || 
				subjectVO.getLinkSubjectStudy().getConsentDate() != null || 
				subjectVO.getLinkSubjectStudy().getConsentDownloaded() != null) {
			createLssConsentHistory(subjectVO.getLinkSubjectStudy());
		}
		assignChildStudies(subjectVO);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Subject " + subjectVO.getLinkSubjectStudy().getSubjectUID());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT);
		ah.setEntityId(subjectVO.getLinkSubjectStudy().getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException, EntityNotFoundException{
		LinkSubjectStudy linkSubjectStudy = iStudyDao.getLinkSubjectStudy(subjectVO.getLinkSubjectStudy().getId());
			
		LinkSubjectStudyConsentHistoryComparator comparator=new LinkSubjectStudyConsentHistoryComparator();
		if(comparator.compare(linkSubjectStudy,subjectVO.getLinkSubjectStudy())!=0){
			updateLssConsentHistory(subjectVO.getLinkSubjectStudy());
		}
		
		iStudyDao.updateSubject(subjectVO);
		
		assignChildStudies(subjectVO);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Subject " + subjectVO.getLinkSubjectStudy().getSubjectUID());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT);
		ah.setEntityId(subjectVO.getLinkSubjectStudy().getId());
		iArkCommonService.createAuditHistory(ah);
	}

	private void createLssConsentHistory(LinkSubjectStudy newLinkSubjectStudy) {
		LssConsentHistory lssConsentHistory = new LssConsentHistory();
		lssConsentHistory.setLinkSubjectStudy(newLinkSubjectStudy);
		lssConsentHistory.setConsentToActiveContact(newLinkSubjectStudy.getConsentToActiveContact());
		lssConsentHistory.setConsentToPassiveDataGathering(newLinkSubjectStudy.getConsentToPassiveDataGathering());
		lssConsentHistory.setConsentToUseData(newLinkSubjectStudy.getConsentToUseData());
		lssConsentHistory.setConsentStatus(newLinkSubjectStudy.getConsentStatus());
		lssConsentHistory.setConsentType(newLinkSubjectStudy.getConsentType());
		lssConsentHistory.setConsentDate(newLinkSubjectStudy.getConsentDate());
		lssConsentHistory.setConsentDownloaded(newLinkSubjectStudy.getConsentDownloaded());
		iAuditDao.createLssConsentHistory(lssConsentHistory);
	}

	private void updateLssConsentHistory(LinkSubjectStudy newLinkSubjectStudy) {
		// Always simply add to the history table
		createLssConsentHistory(newLinkSubjectStudy);
	}

	/* TODO: This is updating twice on those that are selected...this is probably worth avoiding (particularly once we start auditing etc) */
	private void assignChildStudies(SubjectVO subjectVO) {
		// Archive LinkSubjectStudy for all unassigned child studies
		List<Study> availableChildStudies = null;
		availableChildStudies = getChildStudyListOfParent(subjectVO.getLinkSubjectStudy().getStudy());
		for (Study childStudy : availableChildStudies) {
			LinkSubjectStudy linkSubjectStudy;
			try {
				linkSubjectStudy = iArkCommonService.getSubject(subjectVO.getLinkSubjectStudy().getPerson().getId(), childStudy);
				linkSubjectStudy.setSubjectStatus(iArkCommonService.getSubjectStatus("Archive"));
				iStudyDao.update(linkSubjectStudy);
			}
			catch (EntityNotFoundException e) { // TODO :probably dont need an exception here
			//	log.error(e.getMessage());
			}
		}

		// Update ArkUser for all assigned child studies
		List<Study> childStudies = subjectVO.getSelectedChildStudies();
		for (Study childStudy : childStudies) {
			LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
			try {
				// Found a previous archived record
				linkSubjectStudy = iArkCommonService.getSubject(subjectVO.getLinkSubjectStudy().getPerson().getId(), childStudy);
				linkSubjectStudy.setSubjectStatus(iArkCommonService.getSubjectStatus("Subject"));
				iStudyDao.update(linkSubjectStudy);
			}
			catch (EntityNotFoundException e) {
				// Subject not assigned to child study, clone/assign accordingly
				log.info("Assigning LinkSubjectStudy to child Study: " + childStudy.getName());
				linkSubjectStudy = new LinkSubjectStudy();
				linkSubjectStudy.setStudy(childStudy);
				linkSubjectStudy.setPerson(subjectVO.getLinkSubjectStudy().getPerson());
				linkSubjectStudy.setSubjectUID(subjectVO.getLinkSubjectStudy().getSubjectUID());
				linkSubjectStudy.setSubjectStatus(subjectVO.getLinkSubjectStudy().getSubjectStatus());
				cloneSubjectForSubStudy(linkSubjectStudy);
			}
		}
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
		return iStudyDao.getPerson(personId);
	}

	/**
	 * Look up the phones connected with the person(subject or contact)
	 * 
	 * @param personId
	 * @return List<Phone> or empty list is none exist
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId) throws ArkSystemException {
		return iStudyDao.getPersonPhoneList(personId);
	}

	/**
	 * Looks up the phones linked to a person and applies any filter supplied with the phone object.Used in Search Phone functionality. One can look up
	 * base don area code, phone type, phone number
	 * 
	 * @param personId
	 * @param phone
	 * @return
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId, Phone phone) throws ArkSystemException {
		return iStudyDao.getPersonPhoneList(personId, phone);
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
	public List<Address> getPersonAddressList(Long personId, Address address) throws ArkSystemException {
		return iStudyDao.getPersonAddressList(personId, address);
	}

	public void create(Address address) throws ArkSystemException {
		iStudyDao.create(address);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Address " + address.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void update(Address address) throws ArkSystemException {
		iStudyDao.update(address);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Address " + address.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void delete(Address address) throws ArkSystemException {
		// Add business rules to check if this address is in use/active and referred elsewhere
		iStudyDao.delete(address);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Address " + address.getStreetAddress());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void create(Consent consent) throws ArkSystemException {
		iStudyDao.create(consent);
		createConsentHistory(consent);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException, ArkSystemException {
		return iStudyDao.searchConsent(consent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.study.service.IStudyService#update(au.org.theark.core.model.study.entity.Consent)
	 */
	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.update(consent);
		createConsentHistory(consent);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		iArkCommonService.createAuditHistory(ah);
	}
	
	public void update(Consent consent,boolean consentFile) throws ArkSystemException, EntityNotFoundException {

		ConsentHistoryComparator comparator=new ConsentHistoryComparator();
		if(consentFile ||
				comparator.compare(iStudyDao.getConsent(consent.getId()), consent)!=0){
			
			createConsentHistory(consent);
		}
		iStudyDao.update(consent);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	private void createConsentHistory(Consent newConsent) {
		ConsentHistory consentHistory = new ConsentHistory();
		consentHistory.setLinkSubjectStudy(newConsent.getLinkSubjectStudy());
		consentHistory.setStudyComp(newConsent.getStudyComp());
		consentHistory.setStudyComponentStatus(newConsent.getStudyComponentStatus());
		consentHistory.setConsentDate(newConsent.getConsentDate());
		consentHistory.setConsentedBy(newConsent.getConsentedBy());
		consentHistory.setConsentStatus(newConsent.getConsentStatus());
		consentHistory.setConsentType(newConsent.getConsentType());
		consentHistory.setComments(newConsent.getComments());
		consentHistory.setRequestedDate(newConsent.getRequestedDate());
		consentHistory.setReceivedDate(newConsent.getReceivedDate());
		consentHistory.setCompletedDate(newConsent.getCompletedDate());
		consentHistory.setConsentDownloaded(newConsent.getConsentDownloaded());
		iAuditDao.createConsentHistory(consentHistory);
	}

	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException, ArkSystemException {
		return iStudyDao.searchConsent(consentVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.study.service.IStudyService#delete(au.org.theark.core.model.study.entity.Consent)
	 */
	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.delete(consent);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public Consent getConsent(Long id) throws ArkSystemException {
		return iStudyDao.getConsent(id);
	}
	
	public ConsentOption getConsentOptionForBoolean(boolean trueForYesFalseForNo) throws ArkSystemException {
		return iStudyDao.getConsentOptionForBoolean(trueForYesFalseForNo);
	}

	/*** correspondence service functions ***/
	public void create(Correspondences correspondence) throws ArkSystemException {
		iStudyDao.create(correspondence);
	}

	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.update(correspondence);
	}

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.delete(correspondence);
	}

	public List<Correspondences> getPersonCorrespondenceList(Long personId, Correspondences correspondence) throws ArkSystemException {
		return iStudyDao.getPersonCorrespondenceList(personId, correspondence);
	}

	public List<CorrespondenceModeType> getCorrespondenceModeTypes() {
		return iStudyDao.getCorrespondenceModeTypes();
	}

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes() {
		return iStudyDao.getCorrespondenceDirectionTypes();
	}

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes() {
		return iStudyDao.getCorrespondenceOutcomeTypes();
	}

	public void create(ConsentFile consentFile) throws ArkSystemException {
		iStudyDao.create(consentFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void update(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.update(consentFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.delete(consentFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public List<ConsentFile> searchConsentFile(ConsentFile consentFile) throws EntityNotFoundException, ArkSystemException {
		return iStudyDao.searchConsentFile(consentFile);
	}

	/*public void createPersonLastnameHistory(Person person) {
		iStudyDao.createPersonLastnameHistory(person);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PersonLastnameHistory " + person.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY);
		ah.setEntityId(person.getId());
		iArkCommonService.createAuditHistory(ah);
	}*/
/*
	public void updatePersonLastnameHistory(Person person) {
		iStudyDao.updatePersonLastnameHistory(person);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Updated PersonLastnameHistory " + person.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY);
		ah.setEntityId(person.getId());
		iArkCommonService.createAuditHistory(ah);
	}
*/
	public List<PersonLastnameHistory> getLastnameHistory(Person person) {
		return iStudyDao.getLastnameHistory(person);
	}

	public String getPreviousLastname(Person person) {
		return iStudyDao.getPreviousLastname(person);
	}
/*
	public String getCurrentLastname(Person person) {
		return iStudyDao.getCurrentLastname(person);
	}
*/
	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory) {
		return iStudyDao.getPreviousSurnameHistory(personSurnameHistory);
	}

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId) {
		return iStudyDao.personHasPreferredMailingAddress(person, currentAddressId);
	}

	public void create(SubjectFile subjectFile) throws ArkSystemException {
		iStudyDao.create(subjectFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.update(subjectFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.delete(subjectFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException {
		return iStudyDao.searchSubjectFile(subjectFile);
	}

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved, UnAuthorizedOperation {
		iStudyDao.delete(studyComp);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);

		ah.setComment("Deleted Study Component " + studyComp.getName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
		ah.setStudyStatus(studyComp.getStudy().getStudyStatus());
		ah.setEntityId(studyComp.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public StringBuffer uploadAndReportMatrixSubjectFile(File file, String fileFormat, char delimChar, List<String> uidsToUpdate) {
		StringBuffer uploadReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		DataUploader subjectUploader = new DataUploader(study, iArkCommonService, this);

		try {
			InputStream is = new FileInputStream(file);

			log.debug("Importing and reporting Subject file");
			uploadReport = subjectUploader.uploadAndReportMatrixSubjectFile(is, file.length(), fileFormat, delimChar, uidsToUpdate);
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

	public SubjectUploadValidator validateSubjectFileFormat(File file, String fileFormat, char delimChar) {
		// java.util.Collection<String> validationMessages = null;
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);

		try {
			log.debug("Validating Subject file format");
			InputStream is = new FileInputStream(file);
			// validationMessages =
			subjectUploadValidator.validateSubjectMatrixFileFormat(is, file.length(), fileFormat, delimChar);
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
		// java.util.Collection<String> validationMessages = null;
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);

		try {
			// validationMessages =
			subjectUploadValidator.validateSubjectMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return subjectUploadValidator;
	}

	public StringBuffer uploadAndReportCustomDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId,  List<String> listOfUIDsToUpdate){
		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		DataUploader dataUploader = new DataUploader(study, iArkCommonService, this);
		try {
			//log.warn("uploadAndReportCustomDataFile list=" + listOfUIDsToUpdate);
			uploadReport = dataUploader.uploadAndReportCustomDataFile(inputStream, size, fileFormat, delimChar, listOfUIDsToUpdate);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return uploadReport;
	}

	public StringBuffer uploadAndReportSubjectConsentDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId){
		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		DataUploader dataUploader = new DataUploader(study, iArkCommonService, this);
		try {
			//log.warn("uploadAndReportCustomDataFile list=" + listOfUIDsToUpdate);
			uploadReport = dataUploader.uploadAndReportSubjectConsentDataFile(inputStream, size, fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return uploadReport;
	}
	
	public StringBuffer uploadAndReportPedigreeDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId) {
		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		DataUploader dataUploader = new DataUploader(study, iArkCommonService, this);
		try {
			//log.warn("uploadAndReportCustomDataFile list=" + listOfUIDsToUpdate);
			uploadReport = dataUploader.uploadAndReportPedigreeDataFile(inputStream, size, fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return uploadReport;
	}
	
	public SubjectUploadValidator validateSubjectFileData(InputStream inputStream, String fileFormat, char delimChar, List<String> uidsToUpdateReference) {
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);
		try {
			log.debug("Validating Subject file data");
			subjectUploadValidator.validateMatrixSubjectFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar, Long.MAX_VALUE, uidsToUpdateReference);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return subjectUploadValidator;
	}

	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId, List<String> listOfUIDsToUpdate) {
		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		DataUploader subjectUploader = new DataUploader(study, iArkCommonService, this);
		try {
			uploadReport = subjectUploader.uploadAndReportMatrixSubjectFile(inputStream, size, fileFormat, delimChar, listOfUIDsToUpdate);
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
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);
		subjectUploadValidator.validateSubjectFileFormat(uploadVo);
		return subjectUploadValidator;
	}

	public SubjectUploadValidator validateSubjectFileData(UploadVO uploadVo, List<String> uidsToUpdateReference) {
		SubjectUploadValidator subjectUploadValidator = new SubjectUploadValidator(iArkCommonService);
		subjectUploadValidator.validateSubjectFileData(uploadVo, uidsToUpdateReference);
		return subjectUploadValidator;
	}

	public void processBatch(List<LinkSubjectStudy> subjectListToInsert, Study study, List<LinkSubjectStudy> subjectsToUpdate) {
		iStudyDao.processBatch(subjectListToInsert, study, subjectsToUpdate);
	}

	public void processFieldsBatch(List<SubjectCustomFieldData> fieldsToUpdate, Study study, List<SubjectCustomFieldData> fieldsToInsert){
		iStudyDao.processFieldsBatch(fieldsToUpdate, study, fieldsToInsert);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void processSubjectConsentBatch(List<Consent> updateConsentList, List<Consent> insertConsentList) throws ArkSystemException,EntityNotFoundException{
		for(Consent updateConsent : updateConsentList){
			update(updateConsent,false);
		}
		for(Consent insertConsent : insertConsentList){
			create(insertConsent);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void processPedigreeBatch(List<LinkSubjectPedigree> insertPedigreeList) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.processPedigreeBatch(insertPedigreeList);
		
	}
	
	/*
	public void batchInsertSubjects(List<LinkSubjectStudy> subjectList, Study study) throws ArkUniqueException, ArkSubjectInsertException {
		iStudyDao.batchInsertSubjects(subjectList, study);
	}*/

	public Collection<ArkUser> lookupArkUser(Study study) {
		return iStudyDao.lookupArkUser(study);
	}

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException {
		return iStudyDao.getSubjectLinkedToStudy(personId, study);
	}

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction, int first, int count) {
		List<SubjectCustomFieldData> customfieldDataList = new ArrayList<SubjectCustomFieldData>();
		customfieldDataList = iStudyDao.getSubjectCustomFieldDataList(linkSubjectStudyCriteria, arkFunction, first, count);
		return customfieldDataList;
	}


	public long getSubjectCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction) {
		return iStudyDao.getSubjectCustomFieldDataCount(linkSubjectStudyCriteria, arkFunction);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<SubjectCustomFieldData> createOrUpdateSubjectCustomFieldData(List<SubjectCustomFieldData> subjectCustomFieldDataList) {

		List<SubjectCustomFieldData> listOfExceptions = new ArrayList<SubjectCustomFieldData>();
		/* Iterate the list and call DAO to persist each Item */
		for (SubjectCustomFieldData subjectCustomFieldData : subjectCustomFieldDataList) {

			try {
				/* Insert the Field if it does not have a ID and has the required fields */
				if (canInsert(subjectCustomFieldData)) {

					iStudyDao.createSubjectCustomFieldData(subjectCustomFieldData);
					Long id = subjectCustomFieldData.getCustomFieldDisplay().getCustomField().getId();

					CustomField customField = iArkCommonService.getCustomField(id);
					customField.setCustomFieldHasData(true);
					CustomFieldVO customFieldVO = new CustomFieldVO();
					customFieldVO.setCustomField(customField);

					iArkCommonService.updateCustomField(customFieldVO);

				}
				else if (canUpdate(subjectCustomFieldData)) {

					// If there was bad data uploaded and the user has now corrected it on the front end then set/blank out the error data value and
					// updated the record.
					if (subjectCustomFieldData.getErrorDataValue() != null) {
						subjectCustomFieldData.setErrorDataValue(null);
					}
					iStudyDao.updateSubjectCustomFieldData(subjectCustomFieldData);

				}
				else if (canDelete(subjectCustomFieldData)) {
					// Check if the CustomField is used by anyone else and if not set the customFieldHasData to false;
					Long count = iStudyDao.isCustomFieldUsed(subjectCustomFieldData);

					iStudyDao.deleteSubjectCustomFieldData(subjectCustomFieldData);
					if (count <= 1) {
						// Then update the CustomField's hasDataFlag to false;
						Long id = subjectCustomFieldData.getCustomFieldDisplay().getCustomField().getId();// Reload since the session was closed in the
						// front end and the child objects won't be lazy
						// loaded
						CustomField customField = iArkCommonService.getCustomField(id);
						customField.setCustomFieldHasData(false);
						CustomFieldVO customFieldVO = new CustomFieldVO();
						customFieldVO.setCustomField(customField);
						iArkCommonService.updateCustomField(customFieldVO); // Update it

					}
				}
			}
			catch (Exception someException) {
				listOfExceptions.add(subjectCustomFieldData);// Continue with rest of the list
			}
		}

		return listOfExceptions;
	}

	/**
	 * In order to delete it must satisfy the following conditions 1. SubjectCustomFieldData must be a persistent entity(with a valid primary key/ID)
	 * AND 2. SubjectCustomFieldData should have a valid Subject linked to it and must not be null AND 3. SubjectCustomFieldData.TextDataValue is NULL
	 * OR is EMPTY 4. SubjectCustomFieldData.NumberDataValue is NULL 5. SubjectCustomFieldData.DatewDataValue is NULL When these conditiosn are
	 * satisfied this method will return Boolean TRUE
	 * 
	 * @param subjectCustomFieldData
	 * @return
	 */
	private Boolean canDelete(SubjectCustomFieldData subjectCustomFieldData) {
		Boolean flag = false;

		if (subjectCustomFieldData.getId() != null
				&& subjectCustomFieldData.getLinkSubjectStudy() != null
				&& (subjectCustomFieldData.getTextDataValue() == null || subjectCustomFieldData.getTextDataValue().isEmpty() || subjectCustomFieldData.getNumberDataValue() == null || subjectCustomFieldData
						.getDateDataValue() == null)) {

			flag = true;

		}
		return flag;
	}

	/**
	 * In order to Update a SubjectCustomFieldData instance the following conditions must be met 1. SubjectCustomFieldData must be a persistent
	 * entity(with a valid primary key/ID) AND 2. SubjectCustomFieldData should have a valid Subject linked to it and must not be null AND 3.
	 * SubjectCustomFieldData.TextDataValue is NOT NULL AND NOT EMPTY OR 4. SubjectCustomFieldData.NumberDataValue is NOT NULL 5.
	 * SubjectCustomFieldData.DateDataValue is NOT NULL When these conditions are satisfied the method will return Boolean TRUE
	 * 
	 * @param subjectCustomFieldData
	 * @return
	 */
	private Boolean canUpdate(SubjectCustomFieldData subjectCustomFieldData) {
		Boolean flag = false;

		if (subjectCustomFieldData.getId() != null
				&& subjectCustomFieldData.getLinkSubjectStudy() != null
				&& ((subjectCustomFieldData.getTextDataValue() != null && !subjectCustomFieldData.getTextDataValue().isEmpty()) || subjectCustomFieldData.getDateDataValue() != null || subjectCustomFieldData
						.getNumberDataValue() != null)) {

			flag = true;

		}
		return flag;
	}

	/**
	 * In order to Insert a SubjectCustomFieldData instance the following conditions must be met. 1. SubjectCustomFieldData must be a transient
	 * entity(Not yet associated with an ID/PK) AND 2. SubjectCustomFieldData should have a valid Subject linked to it and must not be null AND 3.
	 * SubjectCustomFieldData.TextDataValue is NOT NULL OR 4. SubjectCustomFieldData.NumberDataValue is NOT NULL OR 5.
	 * SubjectCustomFieldData.DateDataValue is NOT NULL When these conditions are satisfied the method will return Boolean TRUE
	 * 
	 * @param subjectCustomFieldData
	 * @return
	 */
	private Boolean canInsert(SubjectCustomFieldData subjectCustomFieldData) {
		Boolean flag = false;

		if (subjectCustomFieldData.getId() == null && subjectCustomFieldData.getLinkSubjectStudy() != null
				&& (subjectCustomFieldData.getNumberDataValue() != null || subjectCustomFieldData.getTextDataValue() != null || subjectCustomFieldData.getDateDataValue() != null)) {

			flag = true;

		}
		return flag;
	}

	public boolean isStudyComponentHasAttachments(StudyComp studyComp) {
		return iStudyDao.isStudyComponentHasAttachments(studyComp);
	}

	public void cloneSubjectForSubStudy(LinkSubjectStudy linkSubjectStudy) {
		iStudyDao.cloneSubjectForSubStudy(linkSubjectStudy);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Cloned Subject " + linkSubjectStudy.getSubjectUID());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT);
		ah.setEntityId(linkSubjectStudy.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public LinkStudySubstudy isSubStudy(Study study) {
		return iStudyDao.isSubStudy(study);
	}

	public List<Study> getChildStudyListOfParent(Study study) {
		return iStudyDao.getChildStudyListOfParent(study);
	}

	public List<LssConsentHistory> getLssConsentHistoryList(LinkSubjectStudy linkSubjectStudy) {
		return iAuditDao.getLssConsentHistoryList(linkSubjectStudy);
	}

	public List<ConsentHistory> getConsentHistoryList(Consent consent) {
		return iAuditDao.getConsentHistoryList(consent);
	}


	public Upload getUpload(Long id) {
		return iStudyDao.getUpload(id);
	}

	public Upload refreshUpload(Upload upload) {
		return iStudyDao.refreshUpload(upload);
	}

	public GenderType getDefaultGenderType() {
		return iStudyDao.getDefaultGenderType();
	}

	public void setPreferredMailingAdressToFalse(Person person) {
		iStudyDao.setPreferredMailingAdressToFalse(person);	
	}
	
	public MaritalStatus getDefaultMaritalStatus() {
		return iStudyDao.getDefaultMaritalStatus();
	}

	public SubjectStatus getDefaultSubjectStatus() {
		 return iStudyDao.getDefaultSubjectStatus();
	}

	public TitleType getDefaultTitleType() {
		return iStudyDao.getDefaultTitleType();
	}

	public VitalStatus getDefaultVitalStatus() {
		return iStudyDao.getDefaultVitalStatus();
	}

	public ConsentStatus getConsentStatusByName(String name) {
		return iStudyDao.getConsentStatusByName(name);
	}

	public ConsentType getConsentTypeByName(String name) {
		return iStudyDao.getConsentTypeByName(name);
	}



	public AddressType getDefaultAddressType() {
		return iStudyDao.getDefaultAddressType();
	}

	public AddressStatus getDefaultAddressStatus() {
		return iStudyDao.getDefaultAddressStatus();
	}


	public PhoneType getDefaultPhoneType() {
		return iStudyDao.getDefaultPhoneType();
	}

	public PhoneStatus getDefaultPhoneStatus() {
		return iStudyDao.getDefaultPhoneStatus();
	}

	public EmailStatus getDefaultEmailStatus() {
		return iStudyDao.getDefaultEmailStatus();
	}

	public List<ConsentOption> getConsentOptions() {
		return iStudyDao.getConsentOptions();
	}

	public List<ConsentStatus> getConsentStatus() {
		return iStudyDao.getConsentStatus();
	}

	public List<ConsentType> getConsentType() {
		return iStudyDao.getConsentType();
	}

	public RelativeCapsule[] generateSubjectPedigree(final String subjectUID,final Long studyId){
		List<RelativeCapsule> relativeCapsules = new ArrayList<RelativeCapsule>();
		Queue<RelativeCapsule> relativeCapsuleQueue = new LinkedList<RelativeCapsule>();
		RelationshipVo probandRelationship = iStudyDao.getSubjectRelative(subjectUID, studyId);
		
		String familyUID=null;
		
		if(probandRelationship !=null){
			
			familyUID = "F"+probandRelationship.getIndividualId();
			
			RelativeCapsule proband = createSubjectRelativeCapsule(probandRelationship,familyUID);
			proband.setProband("Y");
			proband.setRelationship("Proband");
			relativeCapsuleQueue.add(proband);
			
			//Generate parent relationships
			RelativeCapsule relativeCapsule =null;
			while((relativeCapsule = relativeCapsuleQueue.poll())!=null ){
				List<RelationshipVo> relationships =  iStudyDao.getSubjectParentRelatives(relativeCapsule.getIndividualId(),studyId);
				for(RelationshipVo parentRelationshipVo : relationships){
					RelativeCapsule parentCapsule = createSubjectRelativeCapsule(parentRelationshipVo,familyUID); 
					if("M".equalsIgnoreCase(parentCapsule.getGender())){
						relativeCapsule.setFather(parentCapsule.getIndividualId());
					}
					else{
						relativeCapsule.setMother(parentCapsule.getIndividualId());
					}
					relativeCapsuleQueue.add(parentCapsule);
				}
				relativeCapsules.add(relativeCapsule);
			}

			relativeCapsuleQueue.addAll(relativeCapsules);

			//Generate the child relationships to parent relationships
			while((relativeCapsule = relativeCapsuleQueue.poll())!=null ){
				List<RelationshipVo> relationships =  iStudyDao.getSubjectChildRelatives(relativeCapsule.getIndividualId(),studyId);
				for(RelationshipVo childRelativeVo : relationships){
					RelativeCapsule childCapsule = createSubjectRelativeCapsule(childRelativeVo,familyUID);
					if(relativeCapsules.contains(childCapsule)){
						RelativeCapsule prevCapsule = relativeCapsules.get(relativeCapsules.indexOf(childCapsule));
						if("M".equalsIgnoreCase(relativeCapsule.getGender())){
							prevCapsule.setFather(relativeCapsule.getIndividualId());
						}
						else{
							prevCapsule.setMother(relativeCapsule.getIndividualId());
						}
					}
					else{
						if("M".equalsIgnoreCase(relativeCapsule.getGender())){
							childCapsule.setFather(relativeCapsule.getIndividualId());
						}
						else{
							childCapsule.setMother(relativeCapsule.getIndividualId());
						}
						relativeCapsuleQueue.add(childCapsule);
						relativeCapsules.add(childCapsule);
					}
				}
			}
		}
		
		return relativeCapsules.size() >2 ? relativeCapsules.toArray(new RelativeCapsule[relativeCapsules.size()]):new RelativeCapsule[0];
	}
	
	public List<RelationshipVo> generateSubjectPedigreeRelativeList(final String subjectUID,final Long studyId){
		List<RelationshipVo> relativeSubjects = new ArrayList<RelationshipVo>();
		Queue<RelationshipVo> relativeSubjectQueue = new LinkedList<RelationshipVo>();
		RelationshipVo probandRelationship = iStudyDao.getSubjectRelative(subjectUID, studyId);
		
		
		if(probandRelationship !=null){
			
			probandRelationship.setRelationship("Proband");
			relativeSubjectQueue.add(probandRelationship);
			
			//Generate parent relationships
			RelationshipVo relativeSubject =null;
			while((relativeSubject = relativeSubjectQueue.poll())!=null ){
				List<RelationshipVo> relationships =  iStudyDao.getSubjectParentRelatives(relativeSubject.getIndividualId(),studyId);
				for(RelationshipVo parentRelationshipVo : relationships){ 
					if("Male".equalsIgnoreCase(parentRelationshipVo.getGender())){
						relativeSubject.setFatherId(parentRelationshipVo.getIndividualId());
					}
					else{
						relativeSubject.setMotherId(parentRelationshipVo.getIndividualId());
					}
					relativeSubjectQueue.add(parentRelationshipVo);
				}
				relativeSubjects.add(relativeSubject);
			}

			relativeSubjectQueue.addAll(relativeSubjects);

			//Generate the child relationships to parent relationships
			while((relativeSubject = relativeSubjectQueue.poll())!=null ){
				List<RelationshipVo> relationships =  iStudyDao.getSubjectChildRelatives(relativeSubject.getIndividualId(),studyId);
				for(RelationshipVo childRelativeVo : relationships){
					if(relativeSubjects.contains(childRelativeVo)){
						RelationshipVo prevRelativeSubject = relativeSubjects.get(relativeSubjects.indexOf(childRelativeVo));
						if("Male".equalsIgnoreCase(relativeSubject.getGender())){
							prevRelativeSubject.setFatherId(relativeSubject.getIndividualId());
						}
						else{
							prevRelativeSubject.setMotherId(relativeSubject.getIndividualId());
						}
					}
					else{
						if("Male".equalsIgnoreCase(relativeSubject.getGender())){
							childRelativeVo.setFatherId(relativeSubject.getIndividualId());
						}
						else{
							childRelativeVo.setMotherId(relativeSubject.getIndividualId());
						}
						relativeSubjectQueue.add(childRelativeVo);
						relativeSubjects.add(childRelativeVo);
					}
				}
			}
			
			
			/**
			 * Building the relationships around proband 
			 */
			
			//Set parents
			
			RelationshipVo father=null;
			RelationshipVo mother=null;
			Queue<RelationshipVo> parentQueue = new LinkedList<RelationshipVo>();
			
			//Paternal relationships
			if(probandRelationship.getFatherId() != null){
				int fatherIndex = getRelativePosition(probandRelationship.getFatherId(), relativeSubjects);
				father = relativeSubjects.get(fatherIndex);
				father.setRelationship("Father");
					
				parentQueue.add(father);
				
				while((relativeSubject = parentQueue.poll())!=null ){					
					if(relativeSubject.getFatherId() !=null){
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getFatherId(), relativeSubjects);
						RelationshipVo grandFather=relativeSubjects.get(position);
						grandFather.setRelationship(createRelationship("Paternal",++relativeIndex,"GrandFather"));
						grandFather.setRelativeIndex(relativeIndex);
						parentQueue.add(grandFather);
					}
					if(relativeSubject.getMotherId() !=null){
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getMotherId(), relativeSubjects);
						RelationshipVo grandMother=relativeSubjects.get(position);
						grandMother.setRelationship(createRelationship("Paternal",++relativeIndex,"GrandMother"));
						grandMother.setRelativeIndex(relativeIndex);
						parentQueue.add(grandMother);
					}
				}
				
				
			}
			
			//Maternal Relationships
			if(probandRelationship.getMotherId() != null){
				int motherIndex = getRelativePosition(probandRelationship.getMotherId(), relativeSubjects);
				mother = relativeSubjects.get(motherIndex);
				mother.setRelationship("Mother");
				parentQueue.add(mother);
				
				while((relativeSubject = parentQueue.poll())!=null ){					
					if(relativeSubject.getFatherId() !=null){
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getFatherId(), relativeSubjects);
						RelationshipVo grandFather=relativeSubjects.get(position);
						grandFather.setRelationship(createRelationship("Maternal",++relativeIndex,"GrandFather"));
						grandFather.setRelativeIndex(relativeIndex);
						parentQueue.add(grandFather);
					}
					if(relativeSubject.getMotherId() !=null){
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getMotherId(), relativeSubjects);
						RelationshipVo grandMother=relativeSubjects.get(position);
						grandMother.setRelationship(createRelationship("Maternal",++relativeIndex,"GrandMother"));
						grandMother.setRelativeIndex(relativeIndex);
						parentQueue.add(grandMother);
					}
				}
				
			}
			
			//Siblings
			for(int i=0;i<relativeSubjects.size();++i){
				relativeSubject = relativeSubjects.get(i);
				String fatherId=relativeSubject.getFatherId();
				String motherId=relativeSubject.getMotherId();
				if(!"Proband".equals(relativeSubject.getRelationship())  
						&& ((father !=null && father.getIndividualId().equals(fatherId)) ||(mother !=null && mother.getIndividualId().equals(motherId)))){
					if("Male".equalsIgnoreCase(relativeSubject.getGender())){
						relativeSubject.setRelationship("Brother");
					}else if("Female".equalsIgnoreCase(relativeSubject.getGender())){
						relativeSubject.setRelationship("Sister");
					}
				}
			}
			
			
			//Children
			Queue<RelationshipVo> childrenQueue = new LinkedList<RelationshipVo>();
			
			for(int i=0;i<relativeSubjects.size();++i){
				relativeSubject = relativeSubjects.get(i);
				String fatherId=relativeSubject.getFatherId();
				String motherId=relativeSubject.getMotherId();
				if((father !=null && probandRelationship.getIndividualId().equals(fatherId)) ||(mother !=null && probandRelationship.getIndividualId().equals(motherId))){
					if("Male".equalsIgnoreCase(relativeSubject.getGender())){
						relativeSubject.setRelationship("Son");
						childrenQueue.add(relativeSubject);
					}else if("Female".equalsIgnoreCase(relativeSubject.getGender())){
						relativeSubject.setRelationship("Daughter");
						childrenQueue.add(relativeSubject);
					}
				}
			}
			
			//Grand Children
			while((relativeSubject = childrenQueue.poll())!=null ){
				for(RelationshipVo relative:relativeSubjects){
					if("Male".equals(relativeSubject.getGender()) && relativeSubject.getIndividualId().equals(relative.getFatherId())){
						int relativeIndex = relative.getRelativeIndex();
						relative.setRelationship(createRelationship("",++relativeIndex,"GrandSon"));
						relative.setRelativeIndex(relativeIndex);
						childrenQueue.add(relative);
					}
					if("Female".equals(relativeSubject.getGender()) && relativeSubject.getIndividualId().equals(relative.getMotherId())){
						int relativeIndex = relative.getRelativeIndex();
						relative.setRelationship(createRelationship("",++relativeIndex,"GrandDaughter"));
						relative.setRelativeIndex(relativeIndex);
						childrenQueue.add(relative);
					}
					
				}
			}
			
			//Remove proband from the list
			relativeSubjects.remove(0);
		}
		
		return relativeSubjects;
	}
	
	private String createRelationship(String prefix,int count,String suffix){
		String great=" ";
		for (int i =1;i<count;++i){
			great=great+"G ";
		}
		
		return prefix +great+suffix;
				
	}
	
	private int getRelativePosition(String individualUID, List<RelationshipVo> relationshipList){
		int index=-1;
		
		for(RelationshipVo obj:relationshipList ){
			++index;
			if(individualUID.equals(obj.getIndividualId())){
				break;
			}
		}
		return index;
	}

	
	private RelativeCapsule createSubjectRelativeCapsule(RelationshipVo relationshipVo,String familyUID){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
		RelativeCapsule relative=new RelativeCapsule();
		relative.setFamilyId(familyUID);
		relative.setIndividualId(relationshipVo.getIndividualId());
		if(relationshipVo.getGender() !=null && "Male".equalsIgnoreCase(relationshipVo.getGender())){
			relative.setGender("M");
		}else{
			relative.setGender("F");
		}
		
		try{
			String dob=sdf.format(relationshipVo.getDob());
			relative.setDob(dob);
		}catch(Exception e){
			
		}
		if(relationshipVo.getDeceased() !=null && "Deceased".equalsIgnoreCase(relationshipVo.getDeceased())){
			relative.setDeceased("Y");
		}
		return relative;
	}	 
	
	public void create(LinkSubjectPedigree pedigree){
		iStudyDao.create(pedigree);
	}
	
	public void deleteRelationship(final LinkSubjectPedigree relationship){
		iStudyDao.deleteRelationship(relationship);
	}

	public List<RelationshipVo> getSubjectPedigreeTwinList(String subjectUID, Long studyId) {
		
		System.out.println("-------------------------------------------------Get subject pedigree twin list-----------------------------------------------------------------------------");
		
		ArrayList<RelationshipVo> relationships = new ArrayList<RelationshipVo>();
		
		RelationshipVo r1=new RelationshipVo();
		r1.setIndividualId("A001");
		r1.setFirstName("Dean");
		r1.setLastName("Jones");
		r1.setDob(new Date());
		relationships.add(r1);
		
		RelationshipVo r2=new RelationshipVo();
		r2.setIndividualId("A002");
		r2.setFirstName("Dan");
		r2.setLastName("Palmer");
		r2.setDob(new Date());
		relationships.add(r2);
		
		RelationshipVo r3=new RelationshipVo();
		r3.setIndividualId("A003");
		r3.setFirstName("Sen");
		r3.setLastName("Nivans");
		r3.setDob(new Date());
		r3.setTwin("MZ");
		relationships.add(r3);
	
		RelationshipVo r4=new RelationshipVo();
		r4.setIndividualId("A004");
		r4.setFirstName("Ann");
		r4.setLastName("Pam");
		r4.setDob(new Date());
		r4.setTwin("DZ");
		relationships.add(r4);
		
		// TODO Auto-generated method stub
		return relationships;
	}

}