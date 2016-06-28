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
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.file.File;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IAuditDao;
import au.org.theark.core.dao.ICustomFieldDao;
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
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.ICustomFieldData;
import au.org.theark.core.model.study.entity.LinkStudySubstudy;
import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.LinkSubjectTwin;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyCalendar;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.study.model.capsule.ArkRelativeCapsule;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.model.dao.IStudyDao;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.model.vo.StudyCalendarVo;
import au.org.theark.study.util.ConsentHistoryComparator;
import au.org.theark.study.util.DataUploader;
import au.org.theark.study.util.LinkSubjectStudyConsentHistoryComparator;
import au.org.theark.study.util.PedigreeUploadValidator;
import au.org.theark.study.util.SubjectUploadValidator;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectUpload.UploadUtilities;

import com.csvreader.CsvReader;



@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService {

	private static Logger		log	= LoggerFactory.getLogger(StudyServiceImpl.class);

	private IArkCommonService		iArkCommonService;
	private IUserService			iUserService;
	private IStudyDao				iStudyDao;
	private IAuditDao				iAuditDao;
	private ICustomFieldDao 		iCustomFieldDao;

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
	public ICustomFieldDao getiCustomFieldDao() {
		return iCustomFieldDao;
	}
	@Autowired
	public void setiCustomFieldDao(ICustomFieldDao iCustomFieldDao) {
		this.iCustomFieldDao = iCustomFieldDao;
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

		// Consent history record create only when user has update one of the consent history fields.
		if (subjectVO.getLinkSubjectStudy().getConsentToActiveContact() != null || subjectVO.getLinkSubjectStudy().getConsentToPassiveDataGathering() != null
				|| subjectVO.getLinkSubjectStudy().getConsentToUseData() != null || subjectVO.getLinkSubjectStudy().getConsentStatus() != null || subjectVO.getLinkSubjectStudy().getConsentType() != null
				|| subjectVO.getLinkSubjectStudy().getConsentDate() != null || subjectVO.getLinkSubjectStudy().getConsentDownloaded() != null) {
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

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException, EntityNotFoundException {
		LinkSubjectStudy linkSubjectStudy = iStudyDao.getLinkSubjectStudy(subjectVO.getLinkSubjectStudy().getId());

		LinkSubjectStudyConsentHistoryComparator comparator = new LinkSubjectStudyConsentHistoryComparator();
		if (comparator.compare(linkSubjectStudy, subjectVO.getLinkSubjectStudy()) != 0) {
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
				// log.error(e.getMessage());
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

	public List<OtherID> getPersonOtherIDList(Long personID) throws ArkSystemException {
		return iStudyDao.getPersonOtherIDList(personID);
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

	public void update(Consent consent, boolean consentFile) throws ArkSystemException, EntityNotFoundException {

		ConsentHistoryComparator comparator = new ConsentHistoryComparator();
		if (consentFile || comparator.compare(iStudyDao.getConsent(consent.getId()), consent) != 0) {

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

		if (correspondence.getAttachmentPayload() != null) {
			Long studyId = correspondence.getLss().getStudy().getId();
			String subjectUID = correspondence.getLss().getSubjectUID();
			String fileName = correspondence.getAttachmentFilename();
			byte[] payload = correspondence.getAttachmentPayload();

			// Generate unique file id for given file name
			String fileId = iArkCommonService.generateArkFileId(fileName);

			// Set unique subject file id
			correspondence.setAttachmentFileId(fileId);

			// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
			iArkCommonService.saveArkFileAttachment(studyId, subjectUID, Constants.ARK_SUBJECT_CORRESPONDENCE_DIR, fileName, payload, fileId);

			// Remove the attachment
			correspondence.setAttachmentPayload(null);
		}
		iStudyDao.create(correspondence);
	}

	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.update(correspondence);
	}
	
	public void update(Correspondences correspondence, String checksum) throws ArkSystemException, EntityNotFoundException {
		Long studyId = correspondence.getLss().getStudy().getId();
		String subjectUID = correspondence.getLss().getSubjectUID();
		String fileName = correspondence.getAttachmentFilename();
		byte[] payload = correspondence.getAttachmentPayload();
		String prevChecksum = correspondence.getAttachmentChecksum();

		String fileId = null;
		if (correspondence.getAttachmentPayload() != null) {

			if (correspondence.getAttachmentFileId() != null) {

				// Get existing file Id
				fileId = correspondence.getAttachmentFileId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, subjectUID, fileId, Constants.ARK_SUBJECT_CORRESPONDENCE_DIR,prevChecksum);

				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				correspondence.setAttachmentFileId(fileId);

				// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, subjectUID, Constants.ARK_SUBJECT_CORRESPONDENCE_DIR, fileName, payload, fileId);
			}
			else {
				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				correspondence.setAttachmentFileId(fileId);

				// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, subjectUID, Constants.ARK_SUBJECT_CORRESPONDENCE_DIR, fileName, payload, fileId);
			}
			//Set new file checksum
			correspondence.setAttachmentChecksum(checksum);
		}
		else {
			if (correspondence.getAttachmentFileId() != null) {
				// Get existing file Id
				fileId = correspondence.getAttachmentFileId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, subjectUID, fileId, Constants.ARK_SUBJECT_CORRESPONDENCE_DIR,prevChecksum);
				
				//remove existing attachment file id and checksum
				correspondence.setAttachmentFileId(null);
				correspondence.setAttachmentChecksum(null);
			}
		}
		// Remove the attachment
		correspondence.setAttachmentPayload(null);
		iStudyDao.update(correspondence);
	}

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {

		if (correspondence.getAttachmentFileId() != null) {
			Long studyId = correspondence.getLss().getStudy().getId();
			String subjectUID = correspondence.getLss().getSubjectUID();
			String fileId =correspondence.getAttachmentFileId();
			String checksum = correspondence.getAttachmentChecksum();
			try {
					if (iArkCommonService.deleteArkFileAttachment(studyId, subjectUID, fileId, Constants.ARK_SUBJECT_CORRESPONDENCE_DIR, checksum)) {
						log.info("File deleted successfully - " + fileId);
					}
					else {
						log.error("Could not find the file -" + fileId);
					}
				
			}
			catch (Exception e) {
				throw new ArkSystemException(e.getMessage());
			}
			
		}
		iStudyDao.delete(correspondence);
	}

	public List<Correspondences> getCorrespondenceList(LinkSubjectStudy lss, Correspondences correspondence) throws ArkSystemException {
		return iStudyDao.getCorrespondenceList(lss, correspondence);
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

	/*
	 * public void createPersonLastnameHistory(Person person) { iStudyDao.createPersonLastnameHistory(person);
	 * 
	 * AuditHistory ah = new AuditHistory(); ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
	 * ah.setComment("Created PersonLastnameHistory " + person.getId());
	 * ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY); ah.setEntityId(person.getId());
	 * iArkCommonService.createAuditHistory(ah); }
	 */
	/*
	 * public void updatePersonLastnameHistory(Person person) { iStudyDao.updatePersonLastnameHistory(person);
	 * 
	 * AuditHistory ah = new AuditHistory(); ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
	 * ah.setComment("Updated PersonLastnameHistory " + person.getId());
	 * ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY); ah.setEntityId(person.getId());
	 * iArkCommonService.createAuditHistory(ah); }
	 */
	public List<PersonLastnameHistory> getLastnameHistory(Person person) {
		return iStudyDao.getLastnameHistory(person);
	}

	public String getPreviousLastname(Person person) {
		return iStudyDao.getPreviousLastname(person);
	}

	/*
	 * public String getCurrentLastname(Person person) { return iStudyDao.getCurrentLastname(person); }
	 */
	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory) {
		return iStudyDao.getPreviousSurnameHistory(personSurnameHistory);
	}

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId) {
		return iStudyDao.personHasPreferredMailingAddress(person, currentAddressId);
	}

	public void create(SubjectFile subjectFile) throws ArkSystemException {

		Long studyId = subjectFile.getLinkSubjectStudy().getStudy().getId();
		String subjectUID = subjectFile.getLinkSubjectStudy().getSubjectUID();
		String fileName = subjectFile.getFilename();
		byte[] payload = subjectFile.getPayload();

		// Generate unique file id for given file name
		String fileId = iArkCommonService.generateArkFileId(fileName);

		// Set unique subject file id
		subjectFile.setFileId(fileId);

		// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
		iArkCommonService.saveArkFileAttachment(studyId, subjectUID, Constants.ARK_SUBJECT_ATTACHEMENT_DIR, fileName, payload, fileId);

		// Remove the attachment
		subjectFile.setPayload(null);

		// Save attachment meta information in relational tables
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

	public void update(SubjectFile subjectFile, String checksum) throws ArkSystemException, EntityNotFoundException {
		Long studyId = subjectFile.getLinkSubjectStudy().getStudy().getId();
		String subjectUID = subjectFile.getLinkSubjectStudy().getSubjectUID();
		String fileName = subjectFile.getFilename();
		byte[] payload = subjectFile.getPayload();
		String fileId = subjectFile.getFileId();
		
		String preChecksum= subjectFile.getChecksum();

		// Delete existing attachment
		iArkCommonService.deleteArkFileAttachment(studyId, subjectUID, fileId, Constants.ARK_SUBJECT_ATTACHEMENT_DIR,preChecksum);

		fileId = iArkCommonService.generateArkFileId(fileName);

		// Set unique subject file id
		subjectFile.setFileId(fileId);

		// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
		iArkCommonService.saveArkFileAttachment(studyId, subjectUID, Constants.ARK_SUBJECT_ATTACHEMENT_DIR, fileName, payload, fileId);

		// Remove payload
		subjectFile.setPayload(null);

		// Update checksum
		subjectFile.setChecksum(checksum);

		iStudyDao.update(subjectFile);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		iArkCommonService.createAuditHistory(ah);
	}

	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException {

		Long studyId = subjectFile.getLinkSubjectStudy().getStudy().getId();
		String subjectUID = subjectFile.getLinkSubjectStudy().getSubjectUID();
		String fileId = subjectFile.getFileId();
		String checksum=subjectFile.getChecksum();
		
		try {
				if (iArkCommonService.deleteArkFileAttachment(studyId, subjectUID, fileId, Constants.ARK_SUBJECT_ATTACHEMENT_DIR, checksum)) {
					iStudyDao.delete(subjectFile);
					AuditHistory ah = new AuditHistory();
					ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
					ah.setComment("Deleted subjectFile " + subjectFile.getId());
					ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
					ah.setEntityId(subjectFile.getId());
					iArkCommonService.createAuditHistory(ah);
				}
				else {
					log.error("Could not find the file - "+fileId);
				}
		}
		catch (Exception e) {
			throw new ArkSystemException(e.getMessage());
		}
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
	/**
	 * 
	 */
	public StringBuffer uploadAndReportCustomDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId, List<String> listOfUIDsToUpdate) {
		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		DataUploader dataUploader = new DataUploader(study, iArkCommonService, this);
		try {
			String customFieldType=UploadUtilities.getUploadFileDataFileSubjectOrFamily(inputStream,delimChar);
			if(au.org.theark.core.Constants.SUBJECTUID.equals(customFieldType)){
					uploadReport = dataUploader.uploadAndReportSubjectCustomDataFile(inputStream, size, fileFormat, delimChar, listOfUIDsToUpdate);
			}else if(au.org.theark.core.Constants.FAMILYUID.equals(customFieldType)){
				uploadReport = dataUploader.uploadAndReportFamilyCustomDataFile(inputStream, size, fileFormat, delimChar, listOfUIDsToUpdate);
			}else{
				log.error(Constants.FILE_FORMAT_EXCEPTION);
				throw new FileFormatException(); 
			}
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return uploadReport;
	}

	public StringBuffer uploadAndReportSubjectConsentDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId) {
		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		DataUploader dataUploader = new DataUploader(study, iArkCommonService, this);
		try {
			// log.warn("uploadAndReportCustomDataFile list=" + listOfUIDsToUpdate);
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
			// log.warn("uploadAndReportCustomDataFile list=" + listOfUIDsToUpdate);
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
	
	public StringBuffer uploadAndReportSubjectAttachmentDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, long studyId, String user_id) {
		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		DataUploader dataUploader = new DataUploader(study, iArkCommonService, this);
		try {
			// log.warn("uploadAndReportCustomDataFile list=" + listOfUIDsToUpdate);
			uploadReport = dataUploader.uploadAndReportSubjectAttachmentDataFile(inputStream, size, fileFormat, delimChar, user_id);
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
	@Override
	public void processFieldsBatch(List<? extends ICustomFieldData> fieldsToUpdate, Study study, List<? extends ICustomFieldData> fieldsToInsert) {
		iStudyDao.processFieldsBatch(fieldsToUpdate, study, fieldsToInsert);
	}

	/**
	 * {@inheritDoc}
	 */
	public void processSubjectConsentBatch(List<Consent> updateConsentList, List<Consent> insertConsentList) throws ArkSystemException, EntityNotFoundException {
		for (Consent updateConsent : updateConsentList) {
			update(updateConsent, false);
		}
		for (Consent insertConsent : insertConsentList) {
			create(insertConsent);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void processPedigreeBatch(List<LinkSubjectPedigree> parentsToInsert, List<LinkSubjectTwin> twinsToInsert) throws ArkSystemException, EntityNotFoundException {
		iStudyDao.processPedigreeBatch(parentsToInsert, twinsToInsert);

	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public void processSubjectAttachmentBatch(List<SubjectFile> subjectFiles)throws ArkSystemException, EntityNotFoundException{
		iStudyDao.processSubjectAttachmentBatch(subjectFiles);
	}

	/*
	 * public void batchInsertSubjects(List<LinkSubjectStudy> subjectList, Study study) throws ArkUniqueException, ArkSubjectInsertException {
	 * iStudyDao.batchInsertSubjects(subjectList, study); }
	 */

	public Collection<ArkUser> lookupArkUser(Study study) {
		return iStudyDao.lookupArkUser(study);
	}

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException {
		return iStudyDao.getSubjectLinkedToStudy(personId, study);
	}

	/*public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction, int first, int count) {
		List<SubjectCustomFieldData> customfieldDataList = new ArrayList<SubjectCustomFieldData>();
		customfieldDataList = iStudyDao.getSubjectCustomFieldDataList(linkSubjectStudyCriteria, arkFunction, first, count);
		return customfieldDataList;
	}*/

	public long getSubjectCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction) {
		return iStudyDao.getSubjectCustomFieldDataCount(linkSubjectStudyCriteria, arkFunction);
	}
	
	public long getFamilyCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction){
		return iStudyDao.getFamilyCustomFieldDataCount(linkSubjectStudyCriteria, arkFunction);
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
					//CustomFieldVO customFieldVO = new CustomFieldVO();
					//customFieldVO.setCustomField(customField);
					//iArkCommonService.updateCustomField(customFieldVO);
					iCustomFieldDao.updateCustomField(customField);
					

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
						//CustomFieldVO customFieldVO = new CustomFieldVO();
						//customFieldVO.setCustomField(customField);
						//iArkCommonService.updateCustomField(customFieldVO); // Update it
						iCustomFieldDao.updateCustomField(customField);

					}
				}
			}
			catch (Exception someException) {
				listOfExceptions.add(subjectCustomFieldData);// Continue with rest of the list
			}
		}

		return listOfExceptions;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<FamilyCustomFieldData> createOrUpdateFamilyCustomFieldData(List<FamilyCustomFieldData> familyCustomFieldDataList) {

		List<FamilyCustomFieldData> listOfExceptions = new ArrayList<FamilyCustomFieldData>();
		/* Iterate the list and call DAO to persist each Item */
		for (FamilyCustomFieldData familyCustomFieldData : familyCustomFieldDataList) {
			
			try {
				/* Insert the Field if it does not have a ID and has the required fields */
				if (canInsert(familyCustomFieldData)) {

					iStudyDao.createOrUpdateFamilyCustomFieldData(familyCustomFieldData);
					Long id = familyCustomFieldData.getCustomFieldDisplay().getCustomField().getId();

					CustomField customField = iArkCommonService.getCustomField(id);
					customField.setCustomFieldHasData(true);
					//CustomFieldVO customFieldVO = new CustomFieldVO();
					//customFieldVO.setCustomField(customField);
					//iArkCommonService.updateCustomField(customFieldVO);
					iCustomFieldDao.updateCustomField(customField);

				}
				else if (canUpdate(familyCustomFieldData)) {

					// If there was bad data uploaded and the user has now corrected it on the front end then set/blank out the error data value and
					// updated the record.
					if (familyCustomFieldData.getErrorDataValue() != null) {
						familyCustomFieldData.setErrorDataValue(null);
					}
					iStudyDao.createOrUpdateFamilyCustomFieldData(familyCustomFieldData);

				}
				else if (canDelete(familyCustomFieldData)) {
					// Check if the CustomField is used by anyone else and if not set the customFieldHasData to false;
					Long count = iStudyDao.isFamilyCustomFieldUsed(familyCustomFieldData);

					iStudyDao.deleteFamilyCustomFieldData(familyCustomFieldData);
					if (count <= 1) {
						// Then update the CustomField's hasDataFlag to false;
						Long id = familyCustomFieldData.getCustomFieldDisplay().getCustomField().getId();// Reload since the session was closed in the
						// front end and the child objects won't be lazy
						// loaded
						CustomField customField = iArkCommonService.getCustomField(id);
						customField.setCustomFieldHasData(false);
						//CustomFieldVO customFieldVO = new CustomFieldVO();
						//customFieldVO.setCustomField(customField);
						//iArkCommonService.updateCustomField(customFieldVO); // Update it
						iCustomFieldDao.updateCustomField(customField);

					}
				}
			}
			catch (Exception someException) {
				listOfExceptions.add(familyCustomFieldData);// Continue with rest of the list
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
	
	private Boolean canDelete(FamilyCustomFieldData subjectCustomFieldData) {
		Boolean flag = false;

		if (subjectCustomFieldData.getId() != null
				&& subjectCustomFieldData.getFamilyUid() != null
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
	
	private Boolean canUpdate(FamilyCustomFieldData subjectCustomFieldData) {
		Boolean flag = false;

		if (subjectCustomFieldData.getId() != null
				&& subjectCustomFieldData.getFamilyUid() != null
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
	
	private Boolean canInsert(FamilyCustomFieldData familyCustomFieldData) {
		Boolean flag = false;

		if (familyCustomFieldData.getId() == null && familyCustomFieldData.getFamilyUid() != null
				&& (familyCustomFieldData.getNumberDataValue() != null || familyCustomFieldData.getTextDataValue() != null || familyCustomFieldData.getDateDataValue() != null)) {

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

	public RelativeCapsule[] generateSubjectPedigreeImageList(final String subjectUID, final Long studyId) {
		RelativeCapsule[] relativeCapsules = generateSubjectPedigree(subjectUID, studyId);
		return relativeCapsules.length > 2 ? relativeCapsules : new RelativeCapsule[0];
	}

	public RelativeCapsule[] generateSubjectPedigreeExportList(final String subjectUID, final Long studyId) {
		RelativeCapsule[] relativeCapsules = generateSubjectPedigree(subjectUID, studyId);
		return relativeCapsules;
	}

	public ArkRelativeCapsule[] generateSubjectArkPedigreeExportList(final String subjectUID, final Long studyId) {
		List<ArkRelativeCapsule> arkRelativeCapsules = new ArrayList<ArkRelativeCapsule>();
		RelativeCapsule[] relativeCapsules = generateSubjectPedigree(subjectUID, studyId);

		for (RelativeCapsule relativeCapsule : relativeCapsules) {
			ArkRelativeCapsule arkRelativeCapsule = new ArkRelativeCapsule();
			arkRelativeCapsule.setIndividualId(relativeCapsule.getIndividualId());
			arkRelativeCapsule.setFatherId(relativeCapsule.getFather());
			arkRelativeCapsule.setMotherId(relativeCapsule.getMother());

			if ("Y".equalsIgnoreCase(relativeCapsule.getMzTwin())) {
				arkRelativeCapsule.setTwinStatus("M");
			}
			else if ("Y".equalsIgnoreCase(relativeCapsule.getDzTwin())) {
				arkRelativeCapsule.setTwinStatus("D");
			}
			arkRelativeCapsules.add(arkRelativeCapsule);
		}

		for (ArkRelativeCapsule capsule1 : arkRelativeCapsules) {
			if (capsule1.getTwinStatus() != null) {
				for (ArkRelativeCapsule capsule2 : arkRelativeCapsules) {
					if (capsule2.getTwinStatus() != null) {
						if (!capsule1.getIndividualId().equals(capsule2.getIndividualId()) && capsule1.getFatherId() != null && capsule1.getFatherId().equals(capsule2.getFatherId())
								&& capsule2.getMotherId() != null && capsule1.getMotherId().equals(capsule2.getMotherId())) {
							capsule1.setTwinId(capsule2.getIndividualId());
						}
					}
				}
			}
		}

		return arkRelativeCapsules.toArray(new ArkRelativeCapsule[arkRelativeCapsules.size()]);
	}

	private RelativeCapsule[] generateSubjectPedigree(final String subjectUID, final Long studyId) {
		List<RelativeCapsule> relativeCapsules = new ArrayList<RelativeCapsule>();
		Queue<RelativeCapsule> relativeCapsuleQueue = new LinkedList<RelativeCapsule>();
		RelationshipVo probandRelationship = iStudyDao.getSubjectRelative(subjectUID, studyId);

		String familyUID = null;

		if (probandRelationship != null) {

			familyUID = "_F" + probandRelationship.getIndividualId();

			if (familyUID.length() > 11) {
				familyUID = familyUID.substring(0, 11);
			}

			RelativeCapsule proband = createSubjectRelativeCapsule(probandRelationship, familyUID);
			proband.setProband("Y");
			proband.setRelationship("Proband");
			relativeCapsuleQueue.add(proband);

			// Generate parent relationships
			RelativeCapsule relativeCapsule = null;
			while ((relativeCapsule = relativeCapsuleQueue.poll()) != null) {
				List<RelationshipVo> relationships = iStudyDao.getSubjectParentRelatives(relativeCapsule.getIndividualId(), studyId);
				for (RelationshipVo parentRelationshipVo : relationships) {
					RelativeCapsule parentCapsule = createSubjectRelativeCapsule(parentRelationshipVo, familyUID);
					if (!relativeCapsuleQueue.contains(parentCapsule) && !relativeCapsules.contains(parentCapsule)) {
						relativeCapsuleQueue.add(parentCapsule);
					}
				}
				relativeCapsules.add(relativeCapsule);
			}

			relativeCapsuleQueue.addAll(relativeCapsules);

			// Generate the child relationships to parent relationships
			while ((relativeCapsule = relativeCapsuleQueue.poll()) != null) {
				List<RelationshipVo> relationships = iStudyDao.getSubjectChildRelatives(relativeCapsule.getIndividualId(), studyId);
				for (RelationshipVo childRelativeVo : relationships) {
					RelativeCapsule childCapsule = createSubjectRelativeCapsule(childRelativeVo, familyUID);
					if (relativeCapsules.contains(childCapsule)) {
						childCapsule = relativeCapsules.get(relativeCapsules.indexOf(childCapsule));
					}
					else {
						relativeCapsuleQueue.add(childCapsule);
						relativeCapsules.add(childCapsule);
					}

					if ("M".equalsIgnoreCase(relativeCapsule.getGender())) {
						childCapsule.setFather(relativeCapsule.getIndividualId());
					}
					else {
						childCapsule.setMother(relativeCapsule.getIndividualId());
					}
				}
			}

			// Generate twin relationships

			processTwinRelationshipCapsules(relativeCapsules, studyId);

		}

		// return relativeCapsules.size() >2 ? relativeCapsules.toArray(new RelativeCapsule[relativeCapsules.size()]):new RelativeCapsule[0];
		return relativeCapsules.toArray(new RelativeCapsule[relativeCapsules.size()]);
	}

	public List<RelationshipVo> generateSubjectPedigreeRelativeList(final String subjectUID, final Long studyId) {
		List<RelationshipVo> relativeSubjects = new ArrayList<RelationshipVo>();
		Queue<RelationshipVo> relativeSubjectQueue = new LinkedList<RelationshipVo>();
		RelationshipVo probandRelationship = iStudyDao.getSubjectRelative(subjectUID, studyId);

		if (probandRelationship != null) {

			probandRelationship.setRelationship("Proband");
			relativeSubjectQueue.add(probandRelationship);

			// Generate parent relationships
			RelationshipVo relativeSubject = null;
			while ((relativeSubject = relativeSubjectQueue.poll()) != null) {
				List<RelationshipVo> relationships = iStudyDao.getSubjectParentRelatives(relativeSubject.getIndividualId(), studyId);
				for (RelationshipVo parentRelationshipVo : relationships) {
					if (!relativeSubjectQueue.contains(parentRelationshipVo) && !relativeSubjects.contains(parentRelationshipVo)) {
						relativeSubjectQueue.add(parentRelationshipVo);
					}
				}
				relativeSubjects.add(relativeSubject);
			}

			relativeSubjectQueue.addAll(relativeSubjects);

			// Generate the child relationships to parent relationships

			while ((relativeSubject = relativeSubjectQueue.poll()) != null) {
				List<RelationshipVo> relationships = iStudyDao.getSubjectChildRelatives(relativeSubject.getIndividualId(), studyId);
				for (RelationshipVo childRelativeVo : relationships) {
					if (relativeSubjects.contains(childRelativeVo)) {
						childRelativeVo = relativeSubjects.get(relativeSubjects.indexOf(childRelativeVo));
					}
					else {
						relativeSubjectQueue.add(childRelativeVo);
						relativeSubjects.add(childRelativeVo);
					}

					if ("Male".equalsIgnoreCase(relativeSubject.getGender())) {
						childRelativeVo.setFatherId(relativeSubject.getIndividualId());
					}
					else {
						childRelativeVo.setMotherId(relativeSubject.getIndividualId());
					}
				}
			}

			/**
			 * Building the relationships around proband
			 */

			// Set parents

			RelationshipVo father = null;
			RelationshipVo mother = null;
			RelationshipVo paternalGF = null;
			RelationshipVo paternalGM = null;
			RelationshipVo maternalGF = null;
			RelationshipVo maternalGM = null;
			Queue<RelationshipVo> parentQueue = new LinkedList<RelationshipVo>();

			// Paternal relationships
			if (probandRelationship.getFatherId() != null) {
				int fatherIndex = getRelativePosition(probandRelationship.getFatherId(), relativeSubjects);
				father = relativeSubjects.get(fatherIndex);
				father.setRelationship("Father");

				parentQueue.add(father);

				while ((relativeSubject = parentQueue.poll()) != null) {
					if (relativeSubject.getFatherId() != null) {
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getFatherId(), relativeSubjects);
						RelationshipVo grandFather = relativeSubjects.get(position);
						grandFather.setRelationship(createRelationship("Paternal", ++relativeIndex, "Grandfather"));
						grandFather.setRelativeIndex(relativeIndex);
						if ((relativeIndex - 1) == 0) {
							paternalGF = grandFather;
						}
						parentQueue.add(grandFather);
					}
					if (relativeSubject.getMotherId() != null) {
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getMotherId(), relativeSubjects);
						RelationshipVo grandMother = relativeSubjects.get(position);
						grandMother.setRelationship(createRelationship("Paternal", ++relativeIndex, "Grandmother"));
						grandMother.setRelativeIndex(relativeIndex);
						if ((relativeIndex - 1) == 0) {
							paternalGM = grandMother;
						}
						parentQueue.add(grandMother);
					}
				}

			}

			// Maternal Relationships

			if (probandRelationship.getMotherId() != null) {
				int motherIndex = getRelativePosition(probandRelationship.getMotherId(), relativeSubjects);
				mother = relativeSubjects.get(motherIndex);
				mother.setRelationship("Mother");
				parentQueue.add(mother);

				while ((relativeSubject = parentQueue.poll()) != null) {
					if (relativeSubject.getFatherId() != null) {
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getFatherId(), relativeSubjects);
						RelationshipVo grandFather = relativeSubjects.get(position);
						grandFather.setRelationship(createRelationship("Maternal", ++relativeIndex, "Grandfather"));
						grandFather.setRelativeIndex(relativeIndex);
						if ((relativeIndex - 1) == 0) {
							maternalGF = grandFather;
						}
						parentQueue.add(grandFather);
					}
					if (relativeSubject.getMotherId() != null) {
						int relativeIndex = relativeSubject.getRelativeIndex();
						int position = getRelativePosition(relativeSubject.getMotherId(), relativeSubjects);
						RelationshipVo grandMother = relativeSubjects.get(position);
						grandMother.setRelationship(createRelationship("Maternal", ++relativeIndex, "Grandmother"));
						grandMother.setRelativeIndex(relativeIndex);
						if ((relativeIndex - 1) == 0) {
							maternalGM = grandMother;
						}
						parentQueue.add(grandMother);
					}
				}

			}

			// Siblings
			List<RelationshipVo> brotherSisterList = new ArrayList<RelationshipVo>();
			for (int i = 0; i < relativeSubjects.size(); ++i) {
				relativeSubject = relativeSubjects.get(i);
				String fatherId = relativeSubject.getFatherId();
				String motherId = relativeSubject.getMotherId();
				if (!"Proband".equals(relativeSubject.getRelationship())) {

					if ((father != null && father.getIndividualId().equals(fatherId)) && (mother != null && mother.getIndividualId().equals(motherId))) {
						if ("Male".equalsIgnoreCase(relativeSubject.getGender())) {
							relativeSubject.setRelationship("Brother");
							brotherSisterList.add(relativeSubject);
						}
						else if ("Female".equalsIgnoreCase(relativeSubject.getGender())) {
							relativeSubject.setRelationship("Sister");
							brotherSisterList.add(relativeSubject);
						}
					}
					else {
						if ((father != null && father.getIndividualId().equals(fatherId)) || (mother != null && mother.getIndividualId().equals(motherId))) {
							if ("Male".equalsIgnoreCase(relativeSubject.getGender())) {
								relativeSubject.setRelationship("Half Brother");
								brotherSisterList.add(relativeSubject);
							}
							else if ("Female".equalsIgnoreCase(relativeSubject.getGender())) {
								relativeSubject.setRelationship("Half Sister");
								brotherSisterList.add(relativeSubject);
							}
						}
					}
				}
			}

			// Nieces and Nephews
			for (RelationshipVo brotherOrSister : brotherSisterList) {
				for (RelationshipVo existingRelationship : relativeSubjects) {
					if ("Male".equalsIgnoreCase(brotherOrSister.getGender()) && existingRelationship.getFatherId() != null && existingRelationship.getFatherId().equals(brotherOrSister.getIndividualId())) {
						if ("Male".equalsIgnoreCase(existingRelationship.getGender())) {
							existingRelationship.setRelationship("Nephew");
						}
						else if ("Female".equalsIgnoreCase(existingRelationship.getGender())) {
							existingRelationship.setRelationship("Niece");
						}

					}
					else if ("Female".equalsIgnoreCase(brotherOrSister.getGender()) && existingRelationship.getMotherId() != null
							&& existingRelationship.getMotherId().equals(brotherOrSister.getIndividualId())) {
						if ("Male".equalsIgnoreCase(existingRelationship.getGender())) {
							existingRelationship.setRelationship("Nephew");
						}
						else if ("Female".equalsIgnoreCase(existingRelationship.getGender())) {
							existingRelationship.setRelationship("Niece");
						}
					}
				}
			}

			// Children
			Queue<RelationshipVo> childrenQueue = new LinkedList<RelationshipVo>();

			for (int i = 0; i < relativeSubjects.size(); ++i) {
				relativeSubject = relativeSubjects.get(i);
				String fatherId = relativeSubject.getFatherId();
				String motherId = relativeSubject.getMotherId();
				if ((probandRelationship.getIndividualId().equals(fatherId)) || (probandRelationship.getIndividualId().equals(motherId))) {
					if ("Male".equalsIgnoreCase(relativeSubject.getGender())) {
						relativeSubject.setRelationship("Son");
						childrenQueue.add(relativeSubject);
					}
					else if ("Female".equalsIgnoreCase(relativeSubject.getGender())) {
						relativeSubject.setRelationship("Daughter");
						childrenQueue.add(relativeSubject);
					}
				}
			}

			// Grand Children
			while ((relativeSubject = childrenQueue.poll()) != null) {
				for (RelationshipVo relative : relativeSubjects) {
					if (relativeSubject.getIndividualId().equals(relative.getFatherId()) || relativeSubject.getIndividualId().equals(relative.getMotherId())) {

						int relativeIndex = relative.getRelativeIndex();
						if ("Male".equals(relative.getGender())) {
							relative.setRelationship(createRelationship("", ++relativeIndex, "Grandson"));
						}
						else if ("Female".equals(relative.getGender())) {
							relative.setRelationship(createRelationship("", ++relativeIndex, "Granddaughter"));
						}
						relative.setRelativeIndex(relativeIndex);
						childrenQueue.add(relative);

					}
				}
			}

			// Select uncles and aunts
			List<RelationshipVo> uncleAuntList = new ArrayList<RelationshipVo>();
			for (RelationshipVo existingRelationship : relativeSubjects) {
				// Paternal uncles and aunts
				if ((father != null && father.getIndividualId().equals(existingRelationship.getIndividualId()))
						|| (mother != null && mother.getIndividualId().equals(existingRelationship.getIndividualId()))) {
					continue;
				}

				if ((paternalGF != null && paternalGF.getIndividualId().equalsIgnoreCase(existingRelationship.getFatherId()))
						|| (paternalGM != null && paternalGM.getIndividualId().equalsIgnoreCase(existingRelationship.getMotherId()))) {
					if ("Male".equalsIgnoreCase(existingRelationship.getGender())) {
						existingRelationship.setRelationship("Paternal Uncle");
						uncleAuntList.add(existingRelationship);
					}
					else if ("Female".equalsIgnoreCase(existingRelationship.getGender())) {
						existingRelationship.setRelationship("Paternal Aunt");
						uncleAuntList.add(existingRelationship);
					}
				}
				if ((maternalGF != null && maternalGF.getIndividualId().equalsIgnoreCase(existingRelationship.getFatherId()))
						|| (maternalGM != null && maternalGM.getIndividualId().equalsIgnoreCase(existingRelationship.getMotherId()))) {
					if ("Male".equalsIgnoreCase(existingRelationship.getGender())) {
						existingRelationship.setRelationship("Maternal Uncle");
						uncleAuntList.add(existingRelationship);
					}
					else if ("Female".equalsIgnoreCase(existingRelationship.getGender())) {
						existingRelationship.setRelationship("Maternal Aunt");
						uncleAuntList.add(existingRelationship);
					}
				}
			}

			// First Cousins
			for (RelationshipVo uncleOrAunty : uncleAuntList) {
				for (RelationshipVo existingRelationship : relativeSubjects) {
					if ("Male".equalsIgnoreCase(uncleOrAunty.getGender()) && existingRelationship.getFatherId() != null && existingRelationship.getFatherId().equals(uncleOrAunty.getIndividualId())) {
						existingRelationship.setRelationship("First Cousin");

					}
					else if ("Female".equalsIgnoreCase(uncleOrAunty.getGender()) && existingRelationship.getMotherId() != null && existingRelationship.getMotherId().equals(uncleOrAunty.getIndividualId())) {
						existingRelationship.setRelationship("First Cousin");
					}
				}
			}

			processTwinRelationships(relativeSubjects, studyId);
			
			processInbreeds(relativeSubjects);

			// Remove proband from the list
			relativeSubjects.remove(0);
		}

		return relativeSubjects;
	}

	private String createRelationship(String prefix, int count, String suffix) {
		String great = " ";
		for (int i = 1; i < count; ++i) {
			great = great + "G/";
		}

		return prefix + great + suffix;

	}

	private int getRelativePosition(String individualUID, List<RelationshipVo> relationshipList) {
		int index = -1;

		for (RelationshipVo obj : relationshipList) {
			++index;
			if (individualUID.equals(obj.getIndividualId())) {
				break;
			}
		}
		return index;
	}

	private RelativeCapsule createSubjectRelativeCapsule(RelationshipVo relationshipVo, String familyUID) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		RelativeCapsule relative = new RelativeCapsule();
		relative.setFamilyId(familyUID);
		relative.setIndividualId(relationshipVo.getIndividualId());

		if ("Male".equalsIgnoreCase(relationshipVo.getGender())) {
			relative.setGender("M");
		}
		else if ("Female".equalsIgnoreCase(relationshipVo.getGender())) {
			relative.setGender("F");
		}
		else {
			relative.setGender("0");
		}

		try {
			String dob = sdf.format(relationshipVo.getDob());
			relative.setDob(dob);
		}
		catch (Exception e) {

		}

		if ("Deceased".equalsIgnoreCase(relationshipVo.getDeceased())) {
			relative.setDeceased("Y");
		}

		if ("0".equalsIgnoreCase(relationshipVo.getAffectedStatus())) {
			relative.setAffected("Y");
		}

		relative.setAge(calculateRelativeAge(relationshipVo));

		return relative;
	}

	private String calculateRelativeAge(final RelationshipVo relationshipVo) {
		String age = null;

		if ("Alive".equalsIgnoreCase(relationshipVo.getDeceased()) && relationshipVo.getDob() != null) {
			age = calculatePedigreeAge(relationshipVo.getDob(), null);
		}
		else if ("Deceased".equalsIgnoreCase(relationshipVo.getDeceased()) && relationshipVo.getDob() != null && relationshipVo.getDod() != null) {
			age = calculatePedigreeAge(relationshipVo.getDob(), relationshipVo.getDod());
		}
		return age;
	}

	private String calculatePedigreeAge(Date birthDate, Date selectDate) {
		String age = null;
		LocalDate oldDate = null;
		LocalDate newDate = null;

		oldDate = new LocalDate(birthDate);

		if (selectDate != null) {
			newDate = new LocalDate(selectDate);
		}
		else {
			newDate = new LocalDate();
		}

		Period period = new Period(oldDate, newDate, PeriodType.yearMonthDay());
		int years = period.getYears();
		age = "" + (years < 1 ? "&lt;1" : years);

		return age;
	}
	
	public List<RelationshipVo> getSubjectChildren(String subjectUID, long studyId){
		List<RelationshipVo> relativeSubjects = new ArrayList<RelationshipVo>();
		Queue<RelationshipVo> relativeSubjectQueue = new LinkedList<RelationshipVo>();
		RelationshipVo probandRelationship = iStudyDao.getSubjectRelative(subjectUID, studyId);
		
		if(probandRelationship!=null){
			relativeSubjectQueue.add(probandRelationship);
		}
		RelationshipVo relativeSubject = null;
		while ((relativeSubject = relativeSubjectQueue.poll()) != null) {
			List<RelationshipVo> relationships = iStudyDao.getSubjectChildRelatives(relativeSubject.getIndividualId(), studyId);
			for (RelationshipVo childRelativeVo : relationships) {
				if (relativeSubjects.contains(childRelativeVo)) {
					childRelativeVo = relativeSubjects.get(relativeSubjects.indexOf(childRelativeVo));
				}
				else {
					relativeSubjectQueue.add(childRelativeVo);
					relativeSubjects.add(childRelativeVo);
				}

				if ("Male".equalsIgnoreCase(relativeSubject.getGender())) {
					childRelativeVo.setFatherId(relativeSubject.getIndividualId());
				}
				else {
					childRelativeVo.setMotherId(relativeSubject.getIndividualId());
				}
			}
		}
		return relativeSubjects;
	}

	public void create(LinkSubjectPedigree pedigree) {
		iStudyDao.create(pedigree);
	}

	public void deleteRelationship(final LinkSubjectPedigree relationship) {
		iStudyDao.deleteRelationship(relationship);
	}

	public List<RelationshipVo> getSubjectPedigreeTwinList(String subjectUID, Long studyId) {

		List<RelationshipVo> twinList = new ArrayList<RelationshipVo>();

		Study study = iArkCommonService.getStudy(studyId);
		LinkSubjectStudy subject = null;
		try {
			subject = iArkCommonService.getSubjectByUID(subjectUID, study);
		}
		catch (Exception e) {
			e.printStackTrace();
			return twinList;
		}

		List<LinkSubjectPedigree> parentList = iStudyDao.getSubjectParentRelationshipList(subject);

		LinkSubjectStudy father = null;
		LinkSubjectStudy mother = null;

		if (parentList != null && parentList.size() == 2) {
			for (LinkSubjectPedigree subjectRelationship : parentList) {
				if (subjectRelationship.getRelationship() != null) {
					if ("father".equalsIgnoreCase(subjectRelationship.getRelationship().getName())) {
						father = subjectRelationship.getRelative();
					}
					else if ("mother".equalsIgnoreCase(subjectRelationship.getRelationship().getName())) {
						mother = subjectRelationship.getRelative();
					}
				}
			}

			List<LinkSubjectPedigree> fatherRelationList = iStudyDao.getParentNonSubjectRelationshipList(subject, father);
			List<LinkSubjectPedigree> motherRelationList = iStudyDao.getParentNonSubjectRelationshipList(subject, mother);

			HashMap<String, Integer> parentMap = new HashMap<String, Integer>();

			for (LinkSubjectPedigree fatherRelationship : fatherRelationList) {
				for (LinkSubjectPedigree motherRelationship : motherRelationList) {
					if (fatherRelationship.getSubject().getSubjectUID().equals(motherRelationship.getSubject().getSubjectUID())) {
						if (parentMap.containsKey(fatherRelationship.getSubject().getSubjectUID())) {
							int count = parentMap.get(fatherRelationship.getSubject().getSubjectUID());
							parentMap.put(fatherRelationship.getSubject().getSubjectUID(), ++count);
						}
						else {
							parentMap.put(fatherRelationship.getSubject().getSubjectUID(), 1);
						}
					}
				}
			}

			Set<String> siblingUids = parentMap.keySet();
			if (siblingUids != null && siblingUids.size() > 0) {
				twinList = iStudyDao.getSubjectTwins(subjectUID, siblingUids, studyId);
			}
		}

		return twinList;
	}

	public void processPedigreeTwinRelationship(final RelationshipVo relationshipVo, final String subjectUid, final Long studyId) {
		// if("NT".equalsIgnoreCase(relationshipVo.getTwin())){
		if (relationshipVo.getTwin() == null) {
			if (relationshipVo.getId() != null) {
				LinkSubjectTwin twin = new LinkSubjectTwin();
				twin.setId(relationshipVo.getId());
				iStudyDao.delete(twin);
			}
		}
		else {
			List<TwinType> twinTypes = iStudyDao.getTwinTypes();
			for (TwinType type : twinTypes) {
				if (relationshipVo.getTwin().equalsIgnoreCase(type.getName())) {
					LinkSubjectTwin twin = new LinkSubjectTwin();
					twin.setTwinType(type);

					Study study = iArkCommonService.getStudy(studyId);
					try {
						LinkSubjectStudy subject1 = iArkCommonService.getSubjectByUID(subjectUid, study);
						LinkSubjectStudy subject2 = iArkCommonService.getSubjectByUID(relationshipVo.getIndividualId(), study);

						twin.setFirstSubject(subject1);
						twin.setSecondSubject(subject2);
						twin.setId(relationshipVo.getId());
						if (twin.getId() != null) {
							iStudyDao.update(twin);
						}
						else {
							iStudyDao.create(twin);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					break;

				}
			}
		}
	}
	
	public void processInbreeds(List<RelationshipVo> existingRelatives){
		StringBuilder pedigree = PedigreeUploadValidator.generatePedigreeGraph(existingRelatives); 
		Set<String> circularUIDs = PedigreeUploadValidator.getCircularUIDs(pedigree);
		if (circularUIDs.size() > 0) {
			circularIdLoop:for (String uid : circularUIDs) {
					relativesLoop:for(RelationshipVo relative : existingRelatives){
						if(uid.equalsIgnoreCase(relative.getIndividualId())){
							relative.setInbreed(true);
							break relativesLoop;
						}
					}
			}
		}
	}

	public List<TwinType> getTwinTypes() {
		// TODO Auto-generated method stub
		return iStudyDao.getTwinTypes();
	}

	public long getRelationshipCount(final String subjectUID, final Long studyId) {
		return iStudyDao.getRelationshipCount(subjectUID, studyId);
	}

	private void processTwinRelationships(List<RelationshipVo> relatives, Long studyId) {
		Set<String> uidSet = new HashSet<String>();
		for (RelationshipVo relationship : relatives) {
			uidSet.add(relationship.getIndividualId());
		}
		List<LinkSubjectTwin> twins = getTwins(uidSet, studyId);
		for (RelationshipVo relationship : relatives) {
			String individualId = relationship.getIndividualId();
			for (LinkSubjectTwin twin : twins) {
				if (individualId.equals(twin.getFirstSubject().getSubjectUID()) || individualId.equals(twin.getSecondSubject().getSubjectUID())) {
					if ("MZ".equalsIgnoreCase(twin.getTwinType().getName())) {
						relationship.setMz("MZ");
					}
					else if ("DZ".equalsIgnoreCase(twin.getTwinType().getName())) {
						relationship.setDz("DZ");
					}
				}
			}
		}
	}

	private void processTwinRelationshipCapsules(List<RelativeCapsule> relatives, Long studyId) {
		Set<String> uidSet = new HashSet<String>();
		for (RelativeCapsule relationship : relatives) {
			uidSet.add(relationship.getIndividualId());
		}
		List<LinkSubjectTwin> twins = getTwins(uidSet, studyId);
		for (RelativeCapsule relationship : relatives) {
			String individualId = relationship.getIndividualId();
			for (LinkSubjectTwin twin : twins) {
				if (individualId.equals(twin.getFirstSubject().getSubjectUID()) || individualId.equals(twin.getSecondSubject().getSubjectUID())) {
					if ("MZ".equalsIgnoreCase(twin.getTwinType().getName())) {
						relationship.setMzTwin("Y");
					}
					else if ("DZ".equalsIgnoreCase(twin.getTwinType().getName())) {
						relationship.setDzTwin("Y");
					}
				}
			}
		}
	}

	public List<LinkSubjectTwin> getTwins(Set<String> subjectUids, Long studyId) {
		// TODO Auto-generated method stub
		return iStudyDao.getTwins(subjectUids, studyId);
	}

	public List<CustomField> getBinaryCustomFieldsForPedigreeRelativesList(Long studyId) {
		// TODO Auto-generated method stub
		return iStudyDao.getBinaryCustomFieldsForPedigreeRelativesList(studyId);
	}

	public StudyPedigreeConfiguration getStudyPedigreeConfiguration(Long studyId) {
		return iStudyDao.getStudyPedigreeConfiguration(studyId);
	}

	public void saveOrUpdateStudyPedigreeConfiguration(StudyPedigreeConfiguration config) {
		iStudyDao.saveOrUpdateStudyPedigreeConfiguration(config);
	}

	public List<Phone> pageablePersonPhoneList(Long personId, Phone phoneCriteria, int first, int count) {
		return iStudyDao.pageablePersonPhoneLst(personId,phoneCriteria, first, count);
	}
	public List<Address> pageablePersonAddressList(Long personId, Address adressCriteria, int first, int count) {
		return iStudyDao.pageablePersonAddressLst(personId,adressCriteria, first, count);
	}
	
	public List<CustomField> getFamilyUIdCustomFieldsForPedigreeRelativesList(Long studyId){
		return iStudyDao.getFamilyUIdCustomFieldsForPedigreeRelativesList(studyId);
	}
	
	public List<FamilyCustomFieldData> getFamilyCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction,CustomFieldCategory customFieldCategory,CustomFieldType customFieldType, int first, int count){
		return iStudyDao.getFamilyCustomFieldDataList(linkSubjectStudyCriteria, arkFunction, customFieldCategory, customFieldType, first, count);
	}
	public String getSubjectFamilyId(Long studyId, String subjectUID){
		return iStudyDao.getSubjectFamilyUId(studyId, subjectUID);
	}

	@Override
	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction,
			CustomFieldCategory customFieldCategory,CustomFieldType customFieldType, int first, int count) {
		return iStudyDao.getSubjectCustomFieldDataList(linkSubjectStudyCriteria, arkFunction, customFieldCategory,customFieldType, first, count);
	}

	@Override
	public void setPreferredPhoneNumberToFalse(Person person) {
		iStudyDao.setPreferredPhoneNumberToFalse(person);
	}

	@Override
	public void saveOrUpdate(StudyCalendar studyCalendar) {
		iStudyDao.saveOrUpdate(studyCalendar);
	}
	
	@Override
	public void saveOrUpdate(StudyCalendarVo studyCalendarVo) {
		iStudyDao.saveOrUpdate(studyCalendarVo);
	}

	@Override
	public void delete(StudyCalendar studyCalendar) {
		iStudyDao.delete(studyCalendar);
	}

	@Override
	public List<StudyCalendar> searchStudyCalenderList(StudyCalendar studyCalendar) {
		return iStudyDao.searchStudyCalenderList(studyCalendar);
	}
	
	public List<CustomField> getStudySubjectCustomFieldList(Long studyId){
		return iStudyDao.getStudySubjectCustomFieldList(studyId);
	}
	
	public List<CustomField> getSelectedCalendarCustomFieldList(StudyCalendar studyCalendar){
		return iStudyDao.getSelectedCalendarCustomFieldList(studyCalendar);
	}
	public void delete(OtherID otherID) {
		iStudyDao.delete(otherID);
	}
	@Override
	public boolean isStudyComponentBeingUsedInConsent(StudyComp studyComp) {
		
		return iStudyDao.isStudyComponentBeingUsedInConsent(studyComp);
	}
}