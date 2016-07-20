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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.ArkUidGenerator;
import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.dao.IArkAuthorisation;
import au.org.theark.core.dao.IAuditDao;
import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.model.report.entity.SearchSubject;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
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
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.ICustomFieldData;
import au.org.theark.core.model.study.entity.LinkCalendarCustomField;
import au.org.theark.core.model.study.entity.LinkStudyArkModule;
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
import au.org.theark.core.model.study.entity.SubjectUidSequence;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.model.vo.StudyCalendarVo;
import au.org.theark.study.service.Constants;

@Repository("studyDao")
public class StudyDao extends HibernateSessionDao implements IStudyDao {

	private static Logger		log	= LoggerFactory.getLogger(StudyDao.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService	arkCommonService;
	@SuppressWarnings("unchecked")
	private IArkAuthorisation	iArkAuthorisationService;
	private Date					dateNow;
	private ArkUidGenerator		arkUidGenerator;
	private IAuditDao				iAuditDao;

	@Autowired
	public void setArkUidGenerator(ArkUidGenerator arkUidGenerator) {
		this.arkUidGenerator = arkUidGenerator;
	}

	@SuppressWarnings("unchecked")
	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}

	/**
	 * @param iArkAuthorisationService
	 *           the iuserService to set
	 */
	@Autowired
	public void setIArkAuthorisationService(IArkAuthorisation iArkAuthorisationService) {
		this.iArkAuthorisationService = iArkAuthorisationService;
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

	public void create(Study study) {
		getSession().save(study);
	}

	public void create(Study study, Collection<ArkModule> selectedModules, Study parentStudy) {
		Session session = getSession();
		session.save(study);
		linkStudyToArkModule(study, selectedModules, session, au.org.theark.core.Constants.MODE_NEW);

		// Update parent study accordingly
		if (parentStudy != null) {
			parentStudy.setParentStudy(parentStudy);
			session.update(parentStudy);
		}
	}

	/**
	 * Perform all inserts and updates as an atomic unit
	 * 
	 * @param subjectsToInsert
	 * @param study
	 * @param subjectsToUpdate
	 */
	/*public void processFieldsBatch(List<? extends ICustomFieldData> fieldsToUpdate, Study study, List<? extends ICustomFieldData> fieldsToInsert) {
		Session session = getSession();
		int count = 0;
		
		
		for (SubjectCustomFieldData dataToUpdate : fieldsToUpdate) {
			session.update(dataToUpdate);
			count++;
			// based on recommended hibernate practice of <prop key="hibernate.jdbc.batch_size">50</prop>
			if (count % 50 == 0) {
				log.info("\n\n\n\n\n\n\n\n\nflush!!!!!!!!!!!!!!"); // TODO Evaluate why batch not working. hints: may be identity/id generation related.
																					// Will revisit after all batch work done
				session.flush();
				session.clear();
			}
		}
		count = 0;
		for (SubjectCustomFieldData dataToInsert : fieldsToInsert) {
			session.save(dataToInsert);
			count++;
			// based on recommended hibernate practice of <prop key="hibernate.jdbc.batch_size">50</prop>
			if (count % 50 == 0) {
				log.info("\n\n\n\n\n\n\n\n\nflush!!!!!!!!!!!!!!");
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();
	}*/

	/**
	 * {@inheritDoc}
	 */
	public void processPedigreeBatch(List<LinkSubjectPedigree> parentsToInsert, List<LinkSubjectTwin> twinsToInsert) {
		Session session = getSession();
		int count = 0;
		for (LinkSubjectPedigree dataToInsert : parentsToInsert) {
			session.save(dataToInsert);
			count++;
			// based on recommended hibernate practice of <prop key="hibernate.jdbc.batch_size">50</prop>
			if (count % 50 == 0) {
				log.info("\n\n\n\n\n\n\n\n\nflush!!!!!!!!!!!!!!");
				session.flush();
				session.clear();
			}
		}
		count = 0;
		for (LinkSubjectTwin dataToInsert : twinsToInsert) {
			session.save(dataToInsert);
			count++;
			// based on recommended hibernate practice of <prop key="hibernate.jdbc.batch_size">50</prop>
			if (count % 50 == 0) {
				log.info("\n\n\n\n\n\n\n\n\nflush!!!!!!!!!!!!!!");
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void processSubjectAttachmentBatch(List<SubjectFile> subjectFiles) {
		Session session = getSession();
		int count = 0;
		for (SubjectFile subjectFile : subjectFiles) {
			session.save(subjectFile);
			count++;
			// based on recommended hibernate practice of <prop key="hibernate.jdbc.batch_size">50</prop>
			if (count % 50 == 0) {
				log.info("\n\n\n\n\n\n\n\n\nflush!!!!!!!!!!!!!!");
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();

	}

	
	

	public void create(Study study, ArkUserVO arkUserVo, Collection<ArkModule> selectedModules) {
		Session session = getSession();
		session.save(study);
		linkStudyToArkModule(study, selectedModules, session, au.org.theark.core.Constants.MODE_NEW);

		// Update parent study accordingly
		Study parentStudy = study.getParentStudy();
		if (parentStudy != null) {
			parentStudy.setParentStudy(parentStudy);
			session.update(parentStudy);
		}

		// Assign user to newly created study
		try {
			iArkAuthorisationService.updateArkUser(arkUserVo);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	private void linkStudyToArkModule(Study study, Collection<ArkModule> selectedModules, Session session, int mode) {
		for (ArkModule arkModule : selectedModules) {
			LinkStudyArkModule linkStudyArkModule = new LinkStudyArkModule();
			linkStudyArkModule.setStudy(study);
			linkStudyArkModule.setArkModule(arkModule);
			if (mode == au.org.theark.core.Constants.MODE_NEW) {
				session.save(linkStudyArkModule);
			}
			else {
				session.update(linkStudyArkModule);
			}
		}
	}

	public List<StudyStatus> getListOfStudyStatus() {
		Example studyStatus = Example.create(new StudyStatus());
		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatus);
		return studyStatusCriteria.list();
	}

	/**
	 * Given a status name will return the StudyStatus object.
	 */
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException {
		StudyStatus studyStatus = new StudyStatus();
		studyStatus.setName("Archive");
		Example studyStatusExample = Example.create(studyStatus);

		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatusExample);
		if (studyStatusCriteria != null && studyStatusCriteria.list() != null && studyStatusCriteria.list().size() > 0) {
			return (StudyStatus) studyStatusCriteria.list().get(0);
		}
		else {
			log.error("Study Status Table maybe out of synch. Please check if it has an entry for Archive status");
			System.out.println("Cannot locate a study status with " + statusName + " in the database");
			throw new StatusNotAvailableException();
		}
	}

	public Study getStudy(Long id) {
		Study study = (Study) getSession().get(Study.class, id);
		return study;
	}

	public void updateStudy(Study studyEntity) {
		// session.update and session.flush required as Blob read/writes are used, and InputStream may cause NullPointers when closed incorrectly
		Session session = getSession();
		session.update(studyEntity);
		// session.refresh(studyEntity);

		session.flush();
	}

	public void updateStudy(Study study, Collection<ArkModule> selectedApplications) throws CannotRemoveArkModuleException {
		Session session = getSession();

		// session.update(study);
		// execute merge method instead of update to avoid org.hibernate.NonUniqueObjectException
		session.merge(study);

		Collection<LinkStudyArkModule> linkStudyArkModulesToAdd = getModulesToAddList(study, selectedApplications);
		// Determine Removal List here
		Collection<LinkStudyArkModule> linkStudyArkModulesToRemove = getModulesToRemoveList(study, selectedApplications);

		// Process the Removal of Linked ArkModules for this study
		for (LinkStudyArkModule linkStudyArkModule : linkStudyArkModulesToRemove) {
			session.delete(linkStudyArkModule);
		}
		// Insert the new modules for the Study
		for (LinkStudyArkModule linkStudyArkModule : linkStudyArkModulesToAdd) {
			session.save(linkStudyArkModule);
		}

		// Flush must be the last thing to call. If there is any other code/logic to be added make sure session.flush() is invoked after that.
		session.flush();
	}

	/**
	 * Creates a list of ArkModule to which the Study should be linked to.
	 * 
	 * @param study
	 * @param selectedApplications
	 * @return
	 */
	private Collection<LinkStudyArkModule> getModulesToAddList(Study study, Collection<ArkModule> selectedApplications) {

		Collection<LinkStudyArkModule> modulesToLink = new ArrayList<LinkStudyArkModule>();
		// Existing List of ArkModules that were linked to this study
		Collection<ArkModule> arkModules = arkCommonService.getArkModulesLinkedWithStudy(study);
		for (ArkModule arkModule : selectedApplications) {
			if (!arkModules.contains(arkModule)) {
				LinkStudyArkModule linkStudyArkModule = new LinkStudyArkModule();
				linkStudyArkModule.setArkModule(arkModule);
				linkStudyArkModule.setStudy(study);
				modulesToLink.add(linkStudyArkModule);
			}
		}
		return modulesToLink;
	}

	/**
	 * Creates a list of ArkModules that have been requested to be removed for a given study. A business validation to check if there are any ArkUsers
	 * linked to this ArkModule must be determined before we finalise the list. If there was any conflicts this must be notified via a business
	 * exception and abort the update process.
	 * 
	 * @param study
	 * @param selectedApplications
	 * @throws CannotRemoveArkModuleException
	 */
	private Collection<LinkStudyArkModule> getModulesToRemoveList(Study study, Collection<ArkModule> selectedApplications) throws CannotRemoveArkModuleException {
		// If there are users linked to a study and the module then do not add the module for removal
		Collection<LinkStudyArkModule> linkStudyArkModuleCollection = arkCommonService.getLinkStudyArkModulesList(study);

		Collection<LinkStudyArkModule> modulesToRemove = new ArrayList<LinkStudyArkModule>();

		for (LinkStudyArkModule linkStudyArkModule : linkStudyArkModuleCollection) {

			if (!selectedApplications.contains(linkStudyArkModule.getArkModule())) {
				// Check if there are any ArkUsers linked to this module
				List<ArkUserRole> usersLinkedToModule = arkCommonService.getArkUserLinkedModule(study, linkStudyArkModule.getArkModule());
				if (usersLinkedToModule != null && usersLinkedToModule.size() > 0) {
					throw new CannotRemoveArkModuleException();
				}
				else {
					modulesToRemove.add(linkStudyArkModule);
				}

			}
		}

		return modulesToRemove;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.study.model.dao.IStudyDao#create(au.org.theark.study.model.entity.StudyComp)
	 */
	public void create(StudyComp studyComponent) throws ArkSystemException, EntityExistsException {
		getSession().save(studyComponent);

	}

	public void update(StudyComp studyComponent) throws ArkSystemException, EntityExistsException {
		getSession().update(studyComponent);
	}

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved {
		try {
			if (!isStudyComponentUsed(studyComp)) {
				getSession().delete(studyComp);
			}
			else {
				throw new EntityCannotBeRemoved("The Study component is used and cannot be removed.");
			}
		}
		catch (HibernateException hibException) {
			log.error("A hibernate exception occured. Cannot detele the study component ID: " + studyComp.getId() + " Cause " + hibException.getStackTrace());
			throw new ArkSystemException("Cannot update Study component due to system error");
		}

	}

	public boolean isStudyComponentUsed(StudyComp studyComp) {
		boolean flag = false;
		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("studyComp", studyComp));
		criteria.setProjection(Projections.rowCount());
		Long i = (Long) criteria.uniqueResult();
		if (i > 0L) {
			flag = true;
		}
		return flag;
	}

	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) {
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		if (studyCompCriteria.getId() != null) {
			//criteria.add(Restrictions.like(Constants.ID, studyCompCriteria.getId().toString(),MatchMode.ANYWHERE));
			//ARK-1558
			criteria.add(Restrictions.sqlRestriction(" id LIKE '%"+studyCompCriteria.getId()+"%' "));
		}
		if (studyCompCriteria.getName() != null) {
			criteria.add(Restrictions.ilike(Constants.STUDY_COMP_NAME, studyCompCriteria.getName(),MatchMode.ANYWHERE));
		}
		if (studyCompCriteria.getKeyword() != null) {
			criteria.add(Restrictions.ilike(Constants.STUDY_COMP_KEYWORD, studyCompCriteria.getKeyword(), MatchMode.ANYWHERE));
		}
		// Restrict the search for the study do not pull other study components
		criteria.add(Restrictions.eq("study", studyCompCriteria.getStudy()));

		List<StudyComp> list = criteria.list();
		return list;
	}

	public List<PhoneType> getListOfPhoneType() {
		Example phoneTypeExample = Example.create(new PhoneType());
		Criteria criteria = getSession().createCriteria(PhoneType.class).add(phoneTypeExample);
		return criteria.list();
	}

	public void create(Phone phone) {
		getSession().save(phone);
	}

	public void update(Phone phone) {
		getSession().update(phone);
	}

	public void delete(Phone phone) {
		getSession().delete(phone);
	}

	public Collection<TitleType> getTitleType() {
		Example example = Example.create(new TitleType());
		Criteria criteria = getSession().createCriteria(TitleType.class).add(example);
		return criteria.list();
	}

	public Collection<VitalStatus> getVitalStatus() {
		Example example = Example.create(new VitalStatus());
		Criteria criteria = getSession().createCriteria(VitalStatus.class).add(example);
		return criteria.list();
	}

	public Collection<GenderType> getGenderTypes() {
		Example example = Example.create(new GenderType());
		Criteria criteria = getSession().createCriteria(GenderType.class).add(example);
		return criteria.list();
	}

	public void createSubject(SubjectVO subjectVo) throws ArkUniqueException, ArkSubjectInsertException {
		Study study = subjectVo.getLinkSubjectStudy().getStudy();
		if (study.getAutoGenerateSubjectUid()) {
			String subjectUID = getNextGeneratedSubjectUID(study);

			if (isSubjectUIDUnique(subjectUID, study.getId(), "Insert")) {
				subjectVo.getLinkSubjectStudy().setSubjectUID(subjectUID);
			}
			else {// TODO : maybe a for loop to guard against a manual db insert, or just throw exception and holds someone up from further inserts until
					// investigated why?
				subjectUID = getNextGeneratedSubjectUID(study);
				subjectVo.getLinkSubjectStudy().setSubjectUID(subjectUID);
			}
		}
		else {
			if (!isSubjectUIDUnique(subjectVo.getLinkSubjectStudy().getSubjectUID(), subjectVo.getLinkSubjectStudy().getStudy().getId(), "Insert")) {
				throw new ArkUniqueException("Subject UID must be unique");
			}
		}

		// Set default foreign key reference
		if (subjectVo.getLinkSubjectStudy().getSubjectStatus() == null || StringUtils.isBlank(subjectVo.getLinkSubjectStudy().getSubjectStatus().getName())) {
			SubjectStatus subjectStatus = getSubjectStatusByName("Subject");
			subjectVo.getLinkSubjectStudy().setSubjectStatus(subjectStatus);
		}

		// Set default foreign key reference
		if (subjectVo.getLinkSubjectStudy().getPerson().getTitleType() == null || StringUtils.isBlank(subjectVo.getLinkSubjectStudy().getPerson().getTitleType().getName())) {
			TitleType titleType = getTitleType(new Long(0));
			subjectVo.getLinkSubjectStudy().getPerson().setTitleType(titleType);
		}

		// Set default foreign key reference
		if (subjectVo.getLinkSubjectStudy().getPerson().getGenderType() == null || StringUtils.isBlank(subjectVo.getLinkSubjectStudy().getPerson().getGenderType().getName())) {
			GenderType genderType = getGenderType(new Long(0));
			subjectVo.getLinkSubjectStudy().getPerson().setGenderType(genderType);
		}

		// Set default foreign key reference
		if (subjectVo.getLinkSubjectStudy().getPerson().getVitalStatus() == null || StringUtils.isBlank(subjectVo.getLinkSubjectStudy().getPerson().getVitalStatus().getName())) {
			VitalStatus vitalStatus = getVitalStatus(new Long(0));
			subjectVo.getLinkSubjectStudy().getPerson().setVitalStatus(vitalStatus);
		}

		//log.info("create subject otherIDs:  " + subjectVo.getLinkSubjectStudy().getPerson().getOtherIDs());

		if (subjectVo.getLinkSubjectStudy().getPerson().getOtherIDs() == null || subjectVo.getLinkSubjectStudy().getPerson().getOtherIDs().isEmpty()) { // get
																																																		// better
																																																		// values
																																																		// when
																																																		// implemented
																																																		// properly
			List<OtherID> otherIDs = new ArrayList<OtherID>();
			subjectVo.getLinkSubjectStudy().getPerson().setOtherIDs(otherIDs);
		}

		Session session = getSession();
		Person person = subjectVo.getLinkSubjectStudy().getPerson();

		if (person.getId() == null) {
			session.save(person);
			PersonLastnameHistory personLastNameHistory = null;

			// Previous LastName (if supplied on new Subject)
			if (subjectVo.getSubjectPreviousLastname() != null && !subjectVo.getSubjectPreviousLastname().isEmpty()) {
				personLastNameHistory = new PersonLastnameHistory();
				personLastNameHistory.setPerson(person);
				personLastNameHistory.setLastName(subjectVo.getSubjectPreviousLastname());
				session.save(personLastNameHistory);
			}

			/*
			 * only keeping previous names from now on // Current lastName if (person.getLastName() != null) { personLastNameHistory = new
			 * PersonLastnameHistory(); personLastNameHistory.setPerson(person); personLastNameHistory.setLastName(person.getLastName());
			 * session.save(personLastNameHistory); }
			 */
		}

		// Update subjectPreviousLastname TODO investigate
		subjectVo.setSubjectPreviousLastname(getPreviousLastname(person));

		LinkSubjectStudy linkSubjectStudy = subjectVo.getLinkSubjectStudy();
		session.save(linkSubjectStudy);// The hibernate session is the same. This should be automatically bound with Spring's

		autoConsentLinkSubjectStudy(subjectVo.getLinkSubjectStudy());

		// TODO EXCEPTIONHANDLING
	}

	private void autoConsentLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		// Auto consent Subject
		if (linkSubjectStudy.getStudy().getAutoConsent() && linkSubjectStudy.getSubjectStatus().getName().equalsIgnoreCase("Subject")) {
			linkSubjectStudy.setConsentDate(new Date());
			linkSubjectStudy.setConsentStatus(getConsentStatusByName("Consented"));
			linkSubjectStudy.setConsentType(getConsentTypeByName("Electronic"));

			ConsentOption defaultConsentOption = getConsentOption("Yes");
			linkSubjectStudy.setConsentToActiveContact(defaultConsentOption);
			linkSubjectStudy.setConsentToPassiveDataGathering(defaultConsentOption);
			linkSubjectStudy.setConsentToUseData(defaultConsentOption);
		}
	}

	public ConsentType getConsentTypeByName(String name) {
		ConsentType consentType = null;
		Criteria criteria = getSession().createCriteria(ConsentType.class);
		criteria.add(Restrictions.eq("name", name));

		if (criteria.list().size() > 0) {
			consentType = (ConsentType) criteria.list().get(0);
		}
		return consentType;
	}

	public ConsentStatus getConsentStatusByName(String name) {
		ConsentStatus consentStatus = null;
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		criteria.add(Restrictions.eq("name", name));

		if (criteria.list().size() > 0) {
			consentStatus = (ConsentStatus) criteria.list().get(0);
		}
		return consentStatus;
	}

	private VitalStatus getVitalStatus(Long id) {
		Criteria criteria = getSession().createCriteria(VitalStatus.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		return (VitalStatus) criteria.list().get(0);
	}

	public MaritalStatus getMaritalStatusNyName(String name) {
		Criteria criteria = getSession().createCriteria(MaritalStatus.class);

		if (name != null) {
			criteria.add(Restrictions.eq("name", name));
		}

		return (MaritalStatus) criteria.list().get(0);
	}

	public GenderType getGenderType(Long id) {
		Criteria criteria = getSession().createCriteria(GenderType.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		return (GenderType) criteria.list().get(0);
	}

	public TitleType getTitleType(Long id) {
		Criteria criteria = getSession().createCriteria(TitleType.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		return (TitleType) criteria.list().get(0);
	}

	public SubjectStatus getSubjectStatusByName(String name) {
		Criteria criteria = getSession().createCriteria(SubjectStatus.class);

		if (name != null) {
			criteria.add(Restrictions.eq("name", name));
		}

		List<SubjectStatus> subjectStatus = criteria.list();

		// TODO - this should just be not permitted at db level...code shouldnt be checking for poor data - particularly on something which is more enum
		// like than data like
		if (subjectStatus.size() > 0) {
			if (subjectStatus.size() > 1) {
				log.error("Backend database has non-unique Status names, returned the first one");
			}
			return (subjectStatus.get(0));
		}
		else
			return null;
	}

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException {
		Session session = getSession();

		Person person = subjectVO.getLinkSubjectStudy().getPerson();
		String currentLastNameFromDB = getCurrentLastnameFromDB(person);// may need to test this effect of a reget

		session.update(person);// Update Person and associated Phones

		PersonLastnameHistory personLastNameHistory = null;

		if (currentLastNameFromDB != null && !currentLastNameFromDB.isEmpty() && !currentLastNameFromDB.equalsIgnoreCase(person.getLastName())) {
			if (person.getLastName() != null) {
				personLastNameHistory = new PersonLastnameHistory();
				personLastNameHistory.setPerson(person);
				personLastNameHistory.setLastName(currentLastNameFromDB);
				session.save(personLastNameHistory);
			}
		}

		// Update subjectPreviousLastname
		if (personLastNameHistory != null) {
			subjectVO.setSubjectPreviousLastname(getPreviousLastname(person));
		}

		LinkSubjectStudy linkSubjectStudy = subjectVO.getLinkSubjectStudy();
		session.merge(linkSubjectStudy);
	}

	/**
	 * note the numbers coming in
	 * 
	 * @param nextSequenceNumber
	 *           - IMPORTANT this is the NEXT (ie already incremented and gotten from the DB, with the DB updated)
	 * @param subjectUidPrefix
	 * @param subjectUidToken
	 * @param padChar
	 * @param subjectUidPaddedIncrementor
	 * @param subjectUidPadChar
	 * @return
	 */
	// called padchar in db?
	public String getUIDGiven(long startingAtNumber, long nextSequenceNumber, String subjectUidPrefix, String subjectUidToken, int padThisManyChars) {
		// ABC-0000123
		// String subjectUidPrefix = new String(""); //ABC
		// String subjectUidToken = new String(""); // -
		// String subjectUidPadChar = //(how many chars to pad to...eg; "7" here 0000001 0000012 0234567
		// String nextIncrementedsubjectUid = new String(""); //124
		// String subjectUid = new String(""); //ABC-0000124
		String theCompletedUID = "";
		long incrementedValue = startingAtNumber + nextSequenceNumber;
		// log.warn("after convoluted analysis...sequenceuid = " + incrementedValue);
		String numberAsString = "" + incrementedValue;
		String subjectUidPaddedIncrementor = StringUtils.leftPad(numberAsString, (int) padThisManyChars, "0");
		theCompletedUID = subjectUidPrefix + subjectUidToken + subjectUidPaddedIncrementor;
		log.warn("completeUID = " + theCompletedUID);
		return theCompletedUID;
	}

	/**
	 * TODO : make this and batch use same mechanism
	 * 
	 * @param study
	 * @return
	 * @throws ArkSubjectInsertException
	 */
	protected String getNextGeneratedSubjectUID(Study study) throws ArkSubjectInsertException {
		String subjectUidPrefix = new String("");
		String subjectUidToken = new String("");
		String subjectUidPaddedIncrementor = new String("");
		String subjectUidPadChar = new String("0");
		String nextIncrementedsubjectUid = new String("");
		String subjectUid = new String("");

		if (study.getId() != null && study.getAutoGenerateSubjectUid() != null) {

			if (study.getSubjectUidPrefix() != null) {
				subjectUidPrefix = study.getSubjectUidPrefix();
			}

			if (study.getSubjectUidToken() != null && study.getSubjectUidToken().getName() != null) {
				subjectUidToken = study.getSubjectUidToken().getName();
			}

			if (study.getSubjectUidPadChar() != null && study.getSubjectUidPadChar().getName() != null) {
				subjectUidPadChar = study.getSubjectUidPadChar().getName().trim();
			}

			Long subjectUidStart = study.getSubjectUidStart();
			if (subjectUidStart == null) {
				subjectUidStart = new Long(1); // if null, then use: 1
				study.setSubjectUidStart(subjectUidStart);
			}
			Long incrementedValue = subjectUidStart + getNextUidSequence(study);
			nextIncrementedsubjectUid = incrementedValue.toString();
			log.warn("after convoluted analysis...uid = " + nextIncrementedsubjectUid);

			int size = Integer.parseInt(subjectUidPadChar);
			subjectUidPaddedIncrementor = StringUtils.leftPad(nextIncrementedsubjectUid, size, "0");
			subjectUid = subjectUidPrefix + subjectUidToken + subjectUidPaddedIncrementor;
		}
		else {
			subjectUid = null;
		}
		return subjectUid;
	}

	public Integer getNextUidSequence(Study study) throws ArkSubjectInsertException {

		Integer result;
		if (study == null) {
			log.error("Error in Subject insertion - Study was null");
			throw new ArkSubjectInsertException("Error in Subject insertion - Study not in context");
		}
		if (study.getName() == null) {
			log.error("Error in Subject insertion - Study name was null");
			throw new ArkSubjectInsertException("Error in Subject insertion - Empty study name");
		}
		//log.warn("Ark uid generator nnull??? " + (arkUidGenerator == null));
		// arkUidGenerator.

		result = (Integer) arkUidGenerator.getUidAndIncrement(study.getName());
		log.info("about to return new uid # of " + result + " for study " + study.getName());
		return result;
	}

	protected boolean getSubjectUidSequenceLock(Study study) {
		boolean lock = false;
		SubjectUidSequence subjUidSeq = getSubjectUidSequence(study);
		// log.warn("uid seq = " + subjUidSeq);
		if (subjUidSeq == null) {
			lock = false; // not locked if record doesn't exist
		}
		else {
			// log.warn("got subjectuid with a lock = " + subjUidSeq.getInsertLock());
			lock = subjUidSeq.getInsertLock();
		}
		return lock;
	}

	protected SubjectUidSequence getSubjectUidSequence(Study study) {
		// log.info("Getting uid seq entity for study " + study.getName());
		// Stateless sessions should be used to avoid locking the record for future update
		// by getSession(), which relies on the "open session filter" mechanism
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(SubjectUidSequence.class);
		criteria.add(Restrictions.eq(Constants.SUBJECTUIDSEQ_STUDYNAMEID, study.getName()));
		criteria.setMaxResults(1);
		SubjectUidSequence result = (SubjectUidSequence) criteria.uniqueResult();
		session.close();
		log.warn("and got entity with lock = " + result.getInsertLock() + " for study " + study.getName());
		return result;
	}

	/*
	 * protected void setSubjectUidSequenceLock(Study study, boolean lock) { log.info("***********************SETTING LOCK ON STUDY = " +
	 * study.getName() + " to " + lock); // Stateless sessions should be used to avoid locking the record for future update // by getSession(), which
	 * relies on the "open session filter" mechanism StatelessSession session = getStatelessSession(); Transaction tx = session.getTransaction();
	 * tx.begin(); SubjectUidSequence subjUidSeq = getSubjectUidSequence(study); if (subjUidSeq == null) { // create a new record if it doens't exist
	 * subjUidSeq = new SubjectUidSequence(); subjUidSeq.setStudyNameId(study.getName()); subjUidSeq.setUidSequence(new Integer(0));
	 * subjUidSeq.setInsertLock(lock); session.insert(subjUidSeq); } else { subjUidSeq.setInsertLock(lock); session.update(subjUidSeq); } tx.commit();
	 * session.close(); }
	 */
	public Long getSubjectCount(Study study) {
		Long subjectCount = new Long(0);
		if (study.getId() != null) {
			Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
			criteria.add(Restrictions.eq("study", study));

			List<LinkSubjectStudy> listOfSubjects = (List<LinkSubjectStudy>) criteria.list();
			subjectCount = new Long(listOfSubjects.size());
		}

		return subjectCount;
	}

	public Collection<SubjectStatus> getSubjectStatus() {

		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();

	}

	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException {

		Criteria linkSubjectStudyCriteria = getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq(Constants.ID, id));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if (listOfSubjects != null && listOfSubjects.size() > 0) {
			return listOfSubjects.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
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

		Criteria personCriteria = getSession().createCriteria(Person.class);
		personCriteria.add(Restrictions.eq(Constants.ID, personId));
		List<Person> listOfPerson = personCriteria.list();
		if (listOfPerson != null && listOfPerson.size() > 0) {
			return listOfPerson.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Phone> getPersonPhoneList(Long personId) throws ArkSystemException {
		Criteria phoneCriteria = getSession().createCriteria(Phone.class);
		phoneCriteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		List<Phone> personPhoneList = phoneCriteria.list();
		//log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());

		if (personPhoneList.isEmpty()) {
			log.error("this person has no phone;  " + personId);
			// throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
		}
		//log.info("Number of phone items retrieved for person Id " + personId + " " + personPhoneList.size());
		return personPhoneList;
	}

	@SuppressWarnings("unchecked")
	public List<Phone> getPersonPhoneList(Long personId, Phone phone) throws ArkSystemException {

		Criteria phoneCriteria = getSession().createCriteria(Phone.class);

		if (personId != null) {
			phoneCriteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}

		if (phone  != null) {

			if (phone.getId() != null) {
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_ID, phone.getId()));
			}

			if (phone.getPhoneNumber() != null) {
				phoneCriteria.add(Restrictions.ilike(Constants.PHONE_NUMBER, phone.getPhoneNumber()));
			}

			if (phone.getPhoneType() != null) {
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_TYPE, phone.getPhoneType()));
			}

			if (phone.getAreaCode() != null) {
				phoneCriteria.add(Restrictions.eq(Constants.AREA_CODE, phone.getAreaCode()));
			}
			phoneCriteria.setFetchMode("silentMode", FetchMode.JOIN);

		}
	
		List<Phone> personPhoneList = phoneCriteria.list();
		//log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());

		//if (personPhoneList.isEmpty()) {
			// throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
		//	log.info(" personId " + personId + " had no phones.  No drama");
	//	}
		return personPhoneList;
	}

	/**
	 * Looks up all the addresses for a person.
	 * 
	 * @param personId
	 * @param address
	 * @return List<Address>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	@SuppressWarnings("unchecked")
	public List<Address> getPersonAddressList(Long personId, Address address) throws ArkSystemException {

		Criteria criteria = getSession().createCriteria(Address.class);

		if (personId != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}

		if (address != null) {
			// Add criteria for address
			if (address.getStreetAddress() != null) {
				criteria.add(Restrictions.ilike(Constants.STREET_ADDRESS, address.getStreetAddress(), MatchMode.ANYWHERE));
			}

			if (address.getCountry() != null) {
				criteria.add(Restrictions.eq(Constants.COUNTRY_NAME, address.getCountry()));
			}

			if (address.getPostCode() != null) {
				criteria.add(Restrictions.eq(Constants.POST_CODE, address.getPostCode()));
			}

			if (address.getCity() != null) {
				criteria.add(Restrictions.ilike(Constants.CITY, address.getCity()));
			}

			if (address.getState() != null) {
				criteria.add(Restrictions.eq(Constants.STATE_NAME, address.getState()));
			}

			if (address.getAddressType() != null) {
				criteria.add(Restrictions.eq(Constants.ADDRESS_TYPE, address.getAddressType()));
			}
		}

		List<Address> personAddressList = criteria.list();

		// if (personAddressList.isEmpty()) {
		// throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
		// log.info("person " + personId + " does not have any addresses");
		// }
		return personAddressList;
	}

	public void create(Address address) throws ArkSystemException {
		Session session = getSession();
		session.save(address);
	}

	public void update(Address address) throws ArkSystemException {
		Session session = getSession();
		session.update(address);
	}

	public void delete(Address address) throws ArkSystemException {

		getSession().delete(address);
	}

	public void create(OtherID otherID) throws ArkSystemException {
		Session session = getSession();
		session.save(otherID);
	}

	public void create(Consent consent) throws ArkSystemException {
		try {
			Session session = getSession();
			session.save(consent);

		}
		catch (HibernateException hibException) {
			log.error("An exception occured while creating a consent " + hibException.getStackTrace());
			throw new ArkSystemException("Could not create the consent.");
		}
	}

	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException {
		try {
			Session session = getSession();
			session.merge(consent);
		}
		catch (HibernateException someHibernateException) {
			log.error("An Exception occured while trying to update this consent " + someHibernateException.getStackTrace());
		}
		catch (Exception e) {
			log.error("An Exception occured while trying to update this consent " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}

	}

	/**
	 * If a consent is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * 
	 * @param consent
	 * @throws ArkSystemException
	 */
	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException {
		try {
			Session session = getSession();
			consent = (Consent) session.get(Consent.class, consent.getId());
			if (consent != null) {
				getSession().delete(consent);
			}
			else {
				throw new EntityNotFoundException("The Consent record you tried to remove does not exist in the Ark System");
			}

		}
		catch (HibernateException someHibernateException) {
			log.error("An Exception occured while trying to delete this consent " + someHibernateException.getStackTrace());
		}
		catch (Exception e) {
			log.error("An Exception occured while trying to delete this consent " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}

	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException, ArkSystemException {
		Criteria criteria = getSession().createCriteria(Consent.class);
		if (consent != null) {
			criteria.add(Restrictions.eq("study.id", consent.getStudy().getId()));

			if (consent.getStudyComp() != null) {
				criteria.add(Restrictions.eq("studyComp", consent.getStudyComp()));
			}

			if (consent.getStudyComponentStatus() != null) {
				criteria.add(Restrictions.eq("studyComponentStatus", consent.getStudyComponentStatus()));
			}

			if (consent.getConsentedBy() != null) {
				criteria.add(Restrictions.ilike("consentedBy", consent.getConsentedBy(), MatchMode.ANYWHERE));
			}

			if (consent.getConsentStatus() != null) {
				criteria.add(Restrictions.eq("consentStatus", consent.getConsentStatus()));
			}

			if (consent.getConsentDate() != null) {
				criteria.add(Restrictions.eq("consentDate", consent.getConsentDate()));
			}

			if (consent.getConsentType() != null) {
				criteria.add(Restrictions.eq("consentType", consent.getConsentType()));
			}
		}

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException, ArkSystemException {

		Criteria criteria = getSession().createCriteria(Consent.class);
		if (consentVO != null) {
			criteria.add(Restrictions.eq("study.id", consentVO.getConsent().getStudy().getId()));

			// must only get consents for subject in context
			criteria.add(Restrictions.eq("linkSubjectStudy", consentVO.getConsent().getLinkSubjectStudy()));

			if (consentVO.getConsent().getStudyComp() != null) {
				criteria.add(Restrictions.eq("studyComp", consentVO.getConsent().getStudyComp()));
			}

			if (consentVO.getConsent().getStudyComponentStatus() != null) {
				criteria.add(Restrictions.eq("studyComponentStatus", consentVO.getConsent().getStudyComponentStatus()));
			}

			if (consentVO.getConsent().getConsentedBy() != null) {
				criteria.add(Restrictions.ilike("consentedBy", consentVO.getConsent().getConsentedBy(), MatchMode.ANYWHERE));
			}

			if (consentVO.getConsent().getConsentStatus() != null) {
				criteria.add(Restrictions.eq("consentStatus", consentVO.getConsent().getConsentStatus()));
			}

			if (consentVO.getConsent().getConsentDate() != null) {
				criteria.add(Restrictions.between("consentDate", consentVO.getConsent().getConsentDate(), consentVO.getConsentDateEnd()));
			}

			if (consentVO.getConsent().getConsentType() != null) {
				criteria.add(Restrictions.eq("consentType", consentVO.getConsent().getConsentType()));
			}

		}
		List<Consent> list = criteria.list();
		return list;
	}

	public void create(Correspondences correspondence) throws ArkSystemException {
		try {
			getSession().save(correspondence);
		}
		catch (HibernateException ex) {
			log.error("A Hibernate exception occurred when creating a correspondence record. Cause: " + ex.getStackTrace());
			throw new ArkSystemException("Unable to create a correspondence record.");
		}
	}

	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		try {
			getSession().update(correspondence);
		}
		catch (HibernateException ex) {
			log.error("A Hibernate exception occurred when updating a correspondence record. Cause: " + ex.getStackTrace());
			throw new ArkSystemException("Unable to update a correspondence record.");
		}
	}

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {

		try {
			getSession().delete(correspondence);
		}
		catch (HibernateException ex) {
			log.error("A Hibernate exception occurred when deleting a correspondence record. Cause: " + ex.getStackTrace());
			throw new ArkSystemException("Unable to delete a correspondence record.");
		}

	}

	public List<Correspondences> getCorrespondenceList(LinkSubjectStudy lss, Correspondences correspondence) throws ArkSystemException {

		Criteria criteria = getSession().createCriteria(Correspondences.class, "co");
		criteria.createAlias("lss", "lss", JoinType.LEFT_OUTER_JOIN);
		//criteria.createAlias("study", "st", JoinType.LEFT_OUTER_JOIN);
		//criteria.createAlias("st.parentStudy", "pstudy", JoinType.LEFT_OUTER_JOIN);

		if (lss != null) {
			criteria.add(Restrictions.eq("lss", lss));
		}

		if (correspondence != null) {

			// Check context study is match with correspondence study or it's parent study
			if (correspondence.getLss() != null && correspondence.getLss().getStudy() != null) {
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("lss.study", correspondence.getLss().getStudy())));//.add(Restrictions.eq("pstudy", correspondence.getLss().getStudy())));
			}
			if (correspondence.getCorrespondenceDirectionType() != null) {
				criteria.add(Restrictions.eq("co.correspondenceDirectionType", correspondence.getCorrespondenceDirectionType()));
			}
			if (correspondence.getCorrespondenceModeType() != null) {
				criteria.add(Restrictions.eq("co.correspondenceModeType", correspondence.getCorrespondenceModeType()));
			}
			if (correspondence.getCorrespondenceOutcomeType() != null) {
				criteria.add(Restrictions.eq("co.correspondenceOutcomeType", correspondence.getCorrespondenceOutcomeType()));
			}
			if (correspondence.getDate() != null) {
				criteria.add(Restrictions.eq("co.date", correspondence.getDate()));
			}
			if (correspondence.getTime() != null) {
				criteria.add(Restrictions.eq("co.time", correspondence.getTime()));
			}
			if (correspondence.getDetails() != null) {
				criteria.add(Restrictions.ilike("co.details", correspondence.getDetails(), MatchMode.ANYWHERE));
			}
			if (correspondence.getReason() != null) {
				criteria.add(Restrictions.ilike("co.reason", correspondence.getDetails(), MatchMode.ANYWHERE));
			}
			if (correspondence.getComments() != null) {
				criteria.add(Restrictions.ilike("co.comments", correspondence.getComments(), MatchMode.ANYWHERE));
			}
			if (correspondence.getOperator() != null) {
				criteria.add(Restrictions.eq("co.operator", correspondence.getOperator()));
			}
		}

		List<Correspondences> personCorrespondenceList = criteria.list();
		return personCorrespondenceList;
	}

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes() {
		Example directionTypeExample = Example.create(new CorrespondenceDirectionType());
		Criteria criteria = getSession().createCriteria(CorrespondenceDirectionType.class).add(directionTypeExample);
		return criteria.list();
	}

	public List<CorrespondenceModeType> getCorrespondenceModeTypes() {
		Example modeTypeExample = Example.create(new CorrespondenceModeType());
		Criteria criteria = getSession().createCriteria(CorrespondenceModeType.class).add(modeTypeExample);
		return criteria.list();
	}

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes() {
		Example outcomeTypeExample = Example.create(new CorrespondenceOutcomeType());
		Criteria criteria = getSession().createCriteria(CorrespondenceOutcomeType.class).add(outcomeTypeExample);
		return criteria.list();
	}

	public Consent getConsent(Long id) throws ArkSystemException {
		Session session = openSession();
		Consent consent = (Consent) session.get(Consent.class, id);
		return consent;
	}

	public void create(ConsentFile consentFile) {
		getSession().save(consentFile);
	}

	public void create(LinkSubjectPedigree pedigree) {
		getSession().save(pedigree);
	}

	public void update(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException {
		Session session = getSession();

		dateNow = new Date(System.currentTimeMillis());
		consentFile.setUpdateTime(dateNow);

		if ((ConsentFile) session.get(ConsentFile.class, consentFile.getId()) != null) {
			session.update(consentFile);
		}
		else {
			throw new EntityNotFoundException("The Consent file record you tried to update does not exist in the Ark System");
		}
	}

	/**
	 * If a consentFile is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * 
	 * @param consentFile
	 * @throws ArkSystemException
	 */
	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException {
		try {
			Session session = getSession();
			consentFile = (ConsentFile) session.get(ConsentFile.class, consentFile.getId());
			if (consentFile != null) {
				getSession().delete(consentFile);
			}
			else {
				throw new EntityNotFoundException("The Consent file record you tried to remove does not exist in the Ark System");
			}

		}
		catch (HibernateException someHibernateException) {
			log.error("An Exception occured while trying to delete this consent file " + someHibernateException.getStackTrace());
		}
		catch (Exception e) {
			log.error("An Exception occured while trying to delete this consent file " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}

	public List<ConsentFile> searchConsentFile(ConsentFile consentFile) throws EntityNotFoundException, ArkSystemException {
		Criteria criteria = getSession().createCriteria(ConsentFile.class);
		if (consentFile != null) {

			if (consentFile.getId() != null) {
				criteria.add(Restrictions.eq("id", consentFile.getId()));
			}

			if (consentFile.getConsent() != null) {
				criteria.add(Restrictions.eq("consent", consentFile.getConsent()));
			}

			if (consentFile.getFilename() != null) {
				criteria.add(Restrictions.ilike("filename", consentFile.getFilename(), MatchMode.ANYWHERE));
			}
		}
		criteria.addOrder(Order.desc("id"));

		@SuppressWarnings("unchecked")
		List<ConsentFile> list = criteria.list();
		return list;
	}

	private boolean isSubjectUIDUnique(String subjectUID, Long studyId, String action) {
		boolean isUnique = true;
		Session session = getSession();
		Criteria criteria = session.createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("subjectUID", subjectUID));
		criteria.add(Restrictions.eq("study.id", studyId));
		if (action.equalsIgnoreCase(au.org.theark.core.Constants.ACTION_INSERT)) {
			if (criteria.list().size() > 0) {
				isUnique = false;
			}
		}
		else if (action.equalsIgnoreCase(au.org.theark.core.Constants.ACTION_UPDATE)) {
			if (criteria.list().size() > 1) {
				isUnique = false;
			}
		}
		return isUnique;
	}

	/*
	 * private YesNo getYesNo(String value) { Criteria criteria = getSession().createCriteria(YesNo.class); criteria.add(Restrictions.ilike("name",
	 * value)); return (YesNo) criteria.list().get(0); }
	 */

	private ConsentOption getConsentOption(String value) {
		Criteria criteria = getSession().createCriteria(ConsentOption.class);
		criteria.add(Restrictions.ilike("name", value));
		return (ConsentOption) criteria.list().get(0);
	}

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId) {

		boolean hasPreferredMailing = false;

		Criteria criteria = getSession().createCriteria(Address.class);

		// YesNo yes = getYesNo("Yes");
		criteria.add(Restrictions.eq("person.id", person.getId()));
		criteria.add(Restrictions.eq("preferredMailingAddress", true));
		if (currentAddressId != null) {
			criteria.add(Restrictions.ne("id", currentAddressId));
		}

		List list = criteria.list();
		if (list.size() > 0) {
			hasPreferredMailing = true;
		}
		return hasPreferredMailing;
	}

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory) {
		PersonLastnameHistory personLastnameHistoryToReturn = null;

		Example example = Example.create(personSurnameHistory);

		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class).add(example);
		if (criteria != null && criteria.list() != null && criteria.list().size() > 0) {
			personLastnameHistoryToReturn = (PersonLastnameHistory) criteria.list().get(0);
		}

		return personLastnameHistoryToReturn;
	}

	public void createPersonLastnameHistory(Person person) {
		PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory();
		personLastNameHistory.setPerson(person);
		personLastNameHistory.setLastName(person.getLastName());

		getSession().save(personLastNameHistory);
	}

	/*
	 * public void updatePersonLastnameHistory(Person person) { PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory();
	 * personLastnameHistory.setPerson(person); personLastnameHistory.setLastName(person.getLastName());
	 * 
	 * String currentLastName = getCurrentLastname(person);
	 * 
	 * if (currentLastName == null || (currentLastName != null && !currentLastName.equalsIgnoreCase(person.getLastName())))
	 * getSession().save(personLastnameHistory); }
	 */
	public String getPreviousLastname(Person person) {
		PersonLastnameHistory personLastameHistory = new PersonLastnameHistory();

		// Only get previous lastname if person in context
		if (person.getId() != null && person.getLastName() != null) {
			Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);
			criteria.add(Restrictions.eq(au.org.theark.core.Constants.PERSON_SURNAME_HISTORY_PERSON, person));
			criteria.addOrder(Order.desc("id"));
			List pastNamesList = criteria.list();
			if (!pastNamesList.isEmpty()) {
				if (pastNamesList.size() > 0)
					personLastameHistory = (PersonLastnameHistory) pastNamesList.get((pastNamesList.size() - 1));
			}
		}

		return personLastameHistory.getLastName();
	}

	/**
	 * Ensures that the lastname in the db is returned
	 */
	public String getCurrentLastnameFromDB(Person person) {
		/*
		 * Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);
		 * 
		 * if (person.getId() != null) { criteria.add(Restrictions.eq(au.org.theark.core.Constants.PERSON_SURNAME_HISTORY_PERSON, person)); }
		 * criteria.addOrder(Order.desc("id")); PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory(); if
		 * (!criteria.list().isEmpty()) { personLastnameHistory = (PersonLastnameHistory) criteria.list().get(0); }
		 * 
		 * return personLastnameHistory.getLastName();
		 */

		if (person.getId() != null) {
			Query q = getSession().createQuery("Select person.lastName from Person person where person.id = :personId ");
			q.setParameter("personId", person.getId());

			List<String> results = q.list();
			if (!results.isEmpty()) {
				return (String) results.get(0);
			}
		}

		return null;
	}

	public List<PersonLastnameHistory> getLastnameHistory(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.core.Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}

		return criteria.list();
	}

	public void create(SubjectFile subjectFile) throws ArkSystemException {
		getSession().save(subjectFile);
	}

	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException {
		getSession().update(subjectFile);
	}

	/**
	 * If a subjectFile is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * 
	 * @param subjectFile
	 * @throws ArkSystemException
	 */
	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException {
		try {
			Session session = getSession();
			subjectFile = (SubjectFile) session.get(SubjectFile.class, subjectFile.getId());
			if (subjectFile != null) {
				getSession().delete(subjectFile);
			}
			else {
				throw new EntityNotFoundException("The Consent file record you tried to remove does not exist in the Ark System");
			}

		}
		catch (HibernateException someHibernateException) {
			log.error("An Exception occured while trying to delete this consent file " + someHibernateException.getStackTrace());
		}
		catch (Exception e) {
			log.error("An Exception occured while trying to delete this consent file " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException {
		Criteria criteria = getSession().createCriteria(SubjectFile.class);
		if (subjectFile != null) {

			if (subjectFile.getId() != null) {
				criteria.add(Restrictions.eq("id", subjectFile.getId()));
			}

			if (subjectFile.getLinkSubjectStudy() != null) {
				criteria.add(Restrictions.eq("linkSubjectStudy", subjectFile.getLinkSubjectStudy()));
			}

			if (subjectFile.getStudyComp() != null) {
				criteria.add(Restrictions.eq("studyComp", subjectFile.getStudyComp()));
			}

			if (subjectFile.getFilename() != null) {
				criteria.add(Restrictions.ilike("filename", subjectFile.getFilename(), MatchMode.ANYWHERE));
			}
		}
		criteria.addOrder(Order.desc("id"));

		@SuppressWarnings("unchecked")
		List<SubjectFile> list = criteria.list();
		return list;
	}

	public void processBatch(List<LinkSubjectStudy> subjectsToInsert, Study study, List<LinkSubjectStudy> subjectsToUpdate) {
		log.warn("about to process " + subjectsToInsert.size() + " inserts and " + subjectsToUpdate.size() + " updates!");
		// Transaction tx = null;
		// Session session = getSession();
		// StatelessSession session = getStatelessSession();
		try {
			// tx = getSession().beginTransaction();
			batchInsertSubjects(subjectsToInsert, study);
			for (LinkSubjectStudy subject : subjectsToInsert) {
				Person person = subject.getPerson();

				Set<Address> addresses = person.getAddresses();
				person.setAddresses(new HashSet<Address>(0));// this line seems like a hack to get around something that should be set up in our
																			// relationships TODO: fix
				Set<Phone> phones = person.getPhones();
				person.setPhones(new HashSet<Phone>(0));// this line seems like a hack to get around something that should be set up in our relationships
																		// TODO: fix
				getSession().save(person);

				for (Address address : addresses) {
					address.setPerson(person);
					getSession().save(address);
				}
				for (Phone phone : phones) {
					phone.setPerson(person);
					getSession().save(phone);
				}

				// Update Person and associated Phones - TODO test personlastnamehistory nonsense
				getSession().save(subject);

				// Create a new consent history by default
				saveConsentHistory(subject);
			}

			for (LinkSubjectStudy subject : subjectsToUpdate) {
				Person person = subject.getPerson();
				getSession().update(person);// Update Person and associated Phones and addresses - TODO test personlastnamehistory nonsense
				getSession().update(subject);

				// Only save history if indicated to do so (ie prevents save of history for every single update)
				if (subject.getUpdateConsent()) {
					saveConsentHistory(subject);
				}
			}
			// tx.commit();
		}
		catch (ArkUniqueException e) {
			log.error("Got a unique insertion error" + e);
			// if(tx!=null){
			// tx.rollback();
			// }
		}
		catch (HibernateException e) {
			log.error("Got an exception from hibernate " + e);
			// if(tx!=null){
			// tx.rollback();
			// }
		}
		catch (ArkSubjectInsertException e) {
			log.error("ArkSubjectInsertion Exception while performing batch" + e);
			// if(tx!=null){
			// tx.rollback();
			// }
		}
		catch (RuntimeException e) {
			log.error("Runtime Exception while performing batch" + e);
			// if(tx!=null){
			// tx.rollback();
			// }
		}
		catch (Exception e) {
			log.error("Generic unknown Exception while performing batch" + e);
			// if(tx!=null){
			// tx.rollback();
			// }
		}
		finally {
			// session.close();
		}
	}

	public void saveConsentHistory(LinkSubjectStudy subject) {
		if (subject.getConsentToActiveContact() != null || subject.getConsentToPassiveDataGathering() != null || subject.getConsentToUseData() != null || subject.getConsentStatus() != null
				|| subject.getConsentType() != null || subject.getConsentDate() != null || subject.getConsentDownloaded() != null) {

			LssConsentHistory lssConsentHistory = new LssConsentHistory();
			lssConsentHistory.setLinkSubjectStudy(subject);
			lssConsentHistory.setConsentToActiveContact(subject.getConsentToActiveContact());
			lssConsentHistory.setConsentToPassiveDataGathering(subject.getConsentToPassiveDataGathering());
			lssConsentHistory.setConsentToUseData(subject.getConsentToUseData());
			lssConsentHistory.setConsentStatus(subject.getConsentStatus());
			lssConsentHistory.setConsentType(subject.getConsentType());
			lssConsentHistory.setConsentDate(subject.getConsentDate());
			lssConsentHistory.setConsentDownloaded(subject.getConsentDownloaded());

			iAuditDao.createLssConsentHistory(lssConsentHistory);
		}
	}

	// TODO ASAP we need to handle excepions and ROLLBACK if something hits the fan.
	// We also need to discuss uid create and external references and user training
	public List<LinkSubjectStudy> batchInsertSubjects(List<LinkSubjectStudy> subjectsToInsert, Study study) throws ArkUniqueException, ArkSubjectInsertException {

		Integer nextSequenceNumber = null;
		long start = 1L;
		String prefix = null;
		String token = null;
		int howManyCharsToPad = 0;

		if (study.getAutoGenerateSubjectUid()) {
			nextSequenceNumber = arkUidGenerator.getUidAndIncrement(study.getName(), subjectsToInsert.size());
			start = study.getSubjectUidStart() == null ? 1L : study.getSubjectUidStart();
			prefix = study.getSubjectUidPrefix() == null ? "" : study.getSubjectUidPrefix();
			token = study.getSubjectUidToken() == null ? "" : study.getSubjectUidToken().getName();
			howManyCharsToPad = study.getSubjectUidPadChar() == null ? 0 : study.getSubjectUidPadChar().getId().intValue();
			log.warn("\n\nnextSequenceNumber = '" + nextSequenceNumber + "'" + "\tstart = '" + start + "'" + "\tprefix = '" + prefix + "'" + "\ttoken = '" + token + "'" + "\thowManyCharsToPad = '"
					+ howManyCharsToPad + "'");
		}

		try {
			for (LinkSubjectStudy subject : subjectsToInsert) {
				if (study.getAutoGenerateSubjectUid()) {
					String nextsubjectUID = getUIDGiven(start, nextSequenceNumber++, prefix, token, howManyCharsToPad);
					// log.warn("setting uid to " + nextsubjectUID);
					subject.setSubjectUID(nextsubjectUID);
				}
			}
		}
		catch (HibernateException e) {// TODO ASAP exception handling
			log.error("SQL Exception which we must intelligently handle/throw asap" + e);
		}
		catch (Exception e) {
			log.error("Generic Exception which we must intelligently handle asap" + e);
		}
		finally {
			// TODO ASAP handle and test success & failure, both expected and runtime
		}
		return subjectsToInsert;
	}

	/*
	 * public void batchUpdateSubjects(Collection<SubjectVO> subjectVoCollection) { StatelessSession session = getStatelessSession();
	 * 
	 * //Transaction tx = session.beginTransaction();
	 * 
	 * for (Iterator<SubjectVO> iterator = subjectVoCollection.iterator(); iterator.hasNext();) { SubjectVO subjectVo = (SubjectVO) iterator.next();
	 * 
	 * Person person = subjectVo.getLinkSubjectStudy().getPerson(); session.update(person);// Update Person and associated Phones
	 * 
	 * String currentLastName = getCurrentLastname(person);
	 * 
	 * if (currentLastName == null || (currentLastName != null && !currentLastName.equalsIgnoreCase(person.getLastName()))) { if (person.getLastName()
	 * != null) { PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory(); personLastNameHistory.setPerson(person);
	 * personLastNameHistory.setLastName(person.getLastName()); session.insert(personLastNameHistory); }
	 * 
	 * // Update subjectPreviousLastname subjectVo.setSubjectPreviousLastname(getPreviousLastname(person)); }
	 * 
	 * LinkSubjectStudy linkSubjectStudy = subjectVo.getLinkSubjectStudy(); session.update(linkSubjectStudy); } //tx.commit(); session.close(); }
	 * 
	 * public void batchUpdateSubjects(List<LinkSubjectStudy> subjectList) { for (LinkSubjectStudy subject : subjectList) { Person person =
	 * subject.getPerson(); getSession().update(person);// Update Person and associated Phones - TODO test personlastnamehistory nonsense
	 * getSession().update(subject); } }
	 */
	public Collection<ArkUser> lookupArkUser(Study study) {
		StringBuffer hqlQuery = new StringBuffer();
		hqlQuery.append("  select distinct arkUserObj from ArkUserRole as arkuserRole,ArkUser as arkUserObj ");
		hqlQuery.append("  where arkuserRole.study = ");
		hqlQuery.append(study.getId());
		hqlQuery.append(" and arkuserRole.arkUser.id = arkUserObj.id");
		org.hibernate.Query queryObject = getSession().createQuery(hqlQuery.toString());
		return queryObject.list();

	}

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		Person person = getPerson(personId);
		criteria.add(Restrictions.eq("person", person));
		criteria.add(Restrictions.eq("study", study));
		criteria.setMaxResults(1);
		return (LinkSubjectStudy) criteria.uniqueResult();
	}

	/**
	 * Determines if study component with a given name is already present for a Study.
	 * 
	 * @param studyComponentName
	 * @param study
	 * @return
	 */
	public boolean isStudyComponentPresent(String studyComponentName, Study study) {
		boolean isPresent = false;
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		criteria.add(Restrictions.eq("name", studyComponentName));
		criteria.add(Restrictions.eq("study", study));
		if (criteria.list() != null && criteria.list().size() > 0) {
			isPresent = true;
		}
		return isPresent;
	}

	public boolean isStudyCompUnique(String studyComponentName, Study study, StudyComp studyComponentToUpdate) {

		boolean isUnique = true;
		StatelessSession stateLessSession = getStatelessSession();
		Criteria criteria = stateLessSession.createCriteria(StudyComp.class);
		criteria.add(Restrictions.eq("name", studyComponentName));
		criteria.add(Restrictions.eq("study", study));
		criteria.setMaxResults(1);

		StudyComp existingComponent = (StudyComp) criteria.uniqueResult();

		if ((studyComponentToUpdate.getId() != null && studyComponentToUpdate.getId() > 0)) {

			if (existingComponent != null && !studyComponentToUpdate.getId().equals(existingComponent.getId())) {
				isUnique = false;
			}
		}
		else {
			if (existingComponent != null) {
				isUnique = false;
			}
		}
		stateLessSession.close();
		return isUnique;

	}

	/**
	 * The count can be based on CustomFieldDisplay only instead of a left join with it using SubjectCustomFieldData
	 */
	public long getSubjectCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.createAlias("customField", "cfield",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cfield.customFieldType", "cfieldType",JoinType.LEFT_OUTER_JOIN);		
		criteria.add(Restrictions.eq("cfield.study", linkSubjectStudyCriteria.getStudy()));
		criteria.add(Restrictions.eq("cfield.arkFunction", arkFunction));
		criteria.add(Restrictions.or(Restrictions.isNull("cfield.customFieldType"),Restrictions.eq("cfieldType.name", "SUBJECT")));
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	
	/**
	 * The count can be based on CustomFieldDisplay only instead of a left join with it using FamilyCustomFieldData
	 */
	public long getFamilyCustomFieldDataCount(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.createAlias("customField", "cfield",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cfield.customFieldType", "cfieldType",JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("cfield.study", linkSubjectStudyCriteria.getStudy()));
		criteria.add(Restrictions.eq("cfield.arkFunction", arkFunction));
		criteria.add(Restrictions.eq("cfieldType.name", "FAMILY"));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	/**
	 * Update the Study consent fo all LinkSubjectStudy's in the specified Study
	 * 
	 * @param study
	 */
	public void consentAllLinkSubjectsToStudy(Study study) {
		Session session = getSession();
		Set<LinkSubjectStudy> linkSubjectStudyList = study.getLinkSubjectStudies();
		for (Iterator<LinkSubjectStudy> iterator = linkSubjectStudyList.iterator(); iterator.hasNext();) {
			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			autoConsentLinkSubjectStudy(linkSubjectStudy);
			session.update(linkSubjectStudy);
		}
	}

	/**
	 * <p>
	 * Builds a HQL to Left Join wtih SubjectCustomFieldData and applies a condition using the WITH clause to get a sub-set for the given Subject and
	 * then applies the restrictions on study and module.
	 * </p>
	 */
	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction,CustomFieldCategory customFieldCategory,CustomFieldType customFieldType, int first, int count) {

		List<SubjectCustomFieldData> subjectCustomFieldDataList = new ArrayList<SubjectCustomFieldData>();

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cfd, fieldList");
		sb.append(" FROM  CustomFieldDisplay AS cfd ");
		sb.append("LEFT JOIN cfd.customField AS cf ");
		sb.append("LEFT JOIN cf.customFieldType AS cft ");
		sb.append("LEFT JOIN cfd.subjectCustomFieldData as fieldList ");
		sb.append(" with fieldList.linkSubjectStudy.id = :subjectId ");
		sb.append("  where cfd.customField.study.id = :studyId");
		sb.append(" and cfd.customField.arkFunction.id = :functionId");
		//Add new requirement for the category
		if(customFieldCategory!=null){
			sb.append(" and cfd.customField.customFieldCategory.id = :customFieldCategotyId");
		}
//		if(type == null || "SUBJECT".equalsIgnoreCase(type)){
		sb.append(" and (cft is null or cft.name = :type)");
//		}else{
//			sb.append(" and cft.name = :type");
//		}
		sb.append(" order by cfd.sequence");

		Query query = getSession().createQuery(sb.toString());
		query.setParameter("subjectId", linkSubjectStudyCriteria.getId());
		query.setParameter("studyId", linkSubjectStudyCriteria.getStudy().getId());
		query.setParameter("functionId", arkFunction.getId());
		//Add type and category
		if(customFieldCategory!=null){
			query.setParameter("customFieldCategotyId", customFieldCategory.getId());
		}
		query.setParameter("type", customFieldType.getName());
		query.setFirstResult(first);
		query.setMaxResults(count);

		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			CustomFieldDisplay cfd = new CustomFieldDisplay();
			SubjectCustomFieldData scfd = new SubjectCustomFieldData();
			if (objects.length > 0 && objects.length >= 1) {

				cfd = (CustomFieldDisplay) objects[0];
				if (objects[1] != null) {
					scfd = (SubjectCustomFieldData) objects[1];
				}
				else {
					scfd.setCustomFieldDisplay(cfd);
				}

				subjectCustomFieldDataList.add(scfd);
			}
		}
		return subjectCustomFieldDataList;
	}
	

	/**
	 * <p>
	 * Builds a HQL to Left Join wtih SubjectCustomFieldData and applies a condition using the WITH clause to get a sub-set for the given Subject and
	 * then applies the restrictions on study and module.
	 * </p>
	 */
	public List<FamilyCustomFieldData> getFamilyCustomFieldDataList(LinkSubjectStudy linkSubjectStudyCriteria, ArkFunction arkFunction,CustomFieldCategory customFieldCategory,CustomFieldType customFieldType, int first, int count) {

		List<FamilyCustomFieldData> familyCustomFieldDataList = new ArrayList<FamilyCustomFieldData>();

		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT cfd, fieldList");
		sb.append(" FROM  CustomFieldDisplay AS cfd ");
		sb.append("LEFT JOIN cfd.customField AS cf ");
		sb.append("LEFT JOIN cf.customFieldType AS cft ");
		sb.append("LEFT JOIN cfd.familyCustomFieldData as fieldList ");
		sb.append(" with fieldList.familyUid = :familyUid ");
		sb.append("  where cfd.customField.study.id = :studyId");
		sb.append(" and cfd.customField.arkFunction.id = :functionId");
		//Add new requirement for the category
		if(customFieldCategory!=null){
			sb.append(" and cfd.customField.customFieldCategory.id = :customFieldCategotyId");
		}
//		if(type == null || "SUBJECT".equalsIgnoreCase(type)){
//			sb.append(" and (cft is null or cft.name = :type)");
//		}else{
		sb.append(" and cft.name = :type");
//		}
		sb.append(" order by cfd.sequence");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("familyUid", getSubjectFamilyUId(linkSubjectStudyCriteria.getStudy().getId(),linkSubjectStudyCriteria.getSubjectUID()));
		query.setParameter("studyId", linkSubjectStudyCriteria.getStudy().getId());
		query.setParameter("functionId", arkFunction.getId());
		//Add type and category
		if(customFieldCategory!=null){
			query.setParameter("customFieldCategotyId", customFieldCategory.getId());
		}
		query.setParameter("type", customFieldType.getName());
		query.setFirstResult(first);
		query.setMaxResults(count);

		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			CustomFieldDisplay cfd = new CustomFieldDisplay();
			FamilyCustomFieldData fcfd = new FamilyCustomFieldData();
			if (objects.length > 0 && objects.length >= 1) {

				cfd = (CustomFieldDisplay) objects[0];
				if (objects[1] != null) {
					fcfd = (FamilyCustomFieldData) objects[1];
				}
				else {
					fcfd.setCustomFieldDisplay(cfd);
				}

				familyCustomFieldDataList.add(fcfd);
			}
		}
		return familyCustomFieldDataList;
	}
	
	@Override
	public String getSubjectFamilyUId(Long studyId, String subjectUID) {
		String result=null;
		
		StringBuffer sb= new StringBuffer("select scfd.TEXT_DATA_VALUE from "
				+ "study.link_subject_study lss "
				+ "left outer join study.study st on lss.STUDY_ID = st.ID "
				+ "left outer join study.study_pedigree_config spc on spc.study_id = st.ID "
				+ "left outer join study.custom_field cf on cf.ID = spc.family_id "
				+ "left outer join study.custom_field_display cfd on cfd.custom_field_id = cf.id "
				+ "left outer join study.subject_custom_field_data scfd on scfd.CUSTOM_FIELD_DISPLAY_ID = cfd.id "
				+ "and scfd.LINK_SUBJECT_STUDY_ID = lss.ID "
				+ "where st.ID= :studyId and lss.SUBJECT_UID = :subjectUID and spc.family_id is not null");
		
		Query query = getSession().createSQLQuery(sb.toString());
	    query.setParameter("studyId", studyId);
	    query.setParameter("subjectUID", subjectUID);
	    result = (String) query.uniqueResult();
	    return result;
	}
	
	
	/**
	 * Insert a new record of type SubjectCustomFieldData
	 */
	public void createSubjectCustomFieldData(SubjectCustomFieldData subjectCustomFieldData) {
		getSession().save(subjectCustomFieldData);
	}

	/**
	 * Update existing SubjectCustomFieldData
	 */
	public void updateSubjectCustomFieldData(SubjectCustomFieldData subjectCustomFieldData) {
		getSession().update(subjectCustomFieldData);
	}
	
	public void createOrUpdateFamilyCustomFieldData(FamilyCustomFieldData familyCustomFieldData){
		if(familyCustomFieldData.getId() ==null){
			getSession().save(familyCustomFieldData);
		}
		else{
			getSession().update(familyCustomFieldData);
		}
	}

	/**
	 * Remove/Delete the SubjectCustomFieldData
	 * 
	 * @param subjectCustomFieldData
	 */
	public void deleteSubjectCustomFieldData(SubjectCustomFieldData subjectCustomFieldData) {
		getSession().delete(subjectCustomFieldData);
	}
	
	public void deleteFamilyCustomFieldData(FamilyCustomFieldData familyCustomFieldData) {
		getSession().delete(familyCustomFieldData);
	}

	public Long isCustomFieldUsed(SubjectCustomFieldData subjectCustomFieldData) {
		Long count = new Long("0");
		CustomField customField = subjectCustomFieldData.getCustomFieldDisplay().getCustomField();

		Study study = customField.getStudy();
		ArkFunction arkFunction = customField.getArkFunction();

		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(" SELECT COUNT(*) FROM SubjectCustomFieldData AS scfd WHERE EXISTS ");
		stringBuffer.append(" ( ");
		stringBuffer.append(" SELECT cfd.id FROM  CustomFieldDisplay AS cfd  WHERE cfd.customField.study.id = :studyId");
		stringBuffer.append(" AND cfd.customField.arkFunction.id = :functionId AND scfd.customFieldDisplay.id = :customFieldDisplayId");
		stringBuffer.append(" )");

		String theHQLQuery = stringBuffer.toString();

		Query query = getSession().createQuery(theHQLQuery);
		query.setParameter("studyId", study.getId());
		query.setParameter("functionId", arkFunction.getId());
		query.setParameter("customFieldDisplayId", subjectCustomFieldData.getCustomFieldDisplay().getId());
		count = (Long) query.uniqueResult();

		return count;
	}
	
	public Long isFamilyCustomFieldUsed(FamilyCustomFieldData familyCustomFieldData) {
		Long count = new Long("0");
		CustomField customField = familyCustomFieldData.getCustomFieldDisplay().getCustomField();

		Study study = customField.getStudy();
		ArkFunction arkFunction = customField.getArkFunction();

		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(" SELECT COUNT(*) FROM FamilyCustomFieldData AS scfd WHERE EXISTS ");
		stringBuffer.append(" ( ");
		stringBuffer.append(" SELECT cfd.id FROM  CustomFieldDisplay AS cfd  WHERE cfd.customField.study.id = :studyId");
		stringBuffer.append(" AND cfd.customField.arkFunction.id = :functionId AND scfd.customFieldDisplay.id = :customFieldDisplayId");
		stringBuffer.append(" )");

		String theHQLQuery = stringBuffer.toString();

		Query query = getSession().createQuery(theHQLQuery);
		query.setParameter("studyId", study.getId());
		query.setParameter("functionId", arkFunction.getId());
		query.setParameter("customFieldDisplayId", familyCustomFieldData.getCustomFieldDisplay().getId());
		count = (Long) query.uniqueResult();

		return count;
	}

	public boolean isStudyComponentHasAttachments(StudyComp studyComp) {

		boolean isFlag = false;
		Criteria criteria = getStatelessSession().createCriteria(SubjectFile.class);
		criteria.add(Restrictions.eq("studyComp", studyComp));
		criteria.setProjection(Projections.rowCount());
		Long i = (Long) criteria.uniqueResult();
		if (i > 0L) {
			isFlag = true;
		}
		return isFlag;
	}

	/**
	 * This is a lean version of CreateSubject. Here we basically copy the details of the existing subject and link it to the new study. The person
	 * object will still remain same and is not duplicated.There is no generation of SubjectUID as it is inherited from the existing subject details of
	 * the main study.
	 */
	public void cloneSubjectForSubStudy(LinkSubjectStudy linkSubjectStudy) {
		// Add Business Validations here as well apart from UI validation
		if (!linkSubjectStudyExists(linkSubjectStudy)) {
			Session session = getSession();
			session.save(linkSubjectStudy);
			autoConsentLinkSubjectStudy(linkSubjectStudy);
		}
	}

	private boolean linkSubjectStudyExists(LinkSubjectStudy linkSubjectStudy) {
		Criteria criteria = getStatelessSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("subjectUID", linkSubjectStudy.getSubjectUID()));
		criteria.add(Restrictions.eq("study", linkSubjectStudy.getStudy()));
		return criteria.uniqueResult() != null;
	}

	public LinkStudySubstudy isSubStudy(Study study) {
		Criteria criteria = getStatelessSession().createCriteria(LinkStudySubstudy.class);
		criteria.add(Restrictions.eq("subStudy", study));
		LinkStudySubstudy linkedStudy = (LinkStudySubstudy) criteria.uniqueResult();
		return linkedStudy;
	}

	public List<Study> getChildStudyListOfParent(Study study) {
		Criteria criteria = getStatelessSession().createCriteria(Study.class);
		criteria.add(Restrictions.ne("id", study.getId()));
		criteria.add(Restrictions.eq("parentStudy", study));

		try {
			criteria.add(Restrictions.ne("studyStatus", getStudyStatus("Archive")));
		}
		catch (StatusNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		criteria.addOrder(Order.asc("name"));
		List<Study> childStudyList = (List<Study>) criteria.list();
		return childStudyList;
	}

	public void update(LinkSubjectStudy linkSubjectStudy) {
		getSession().update(linkSubjectStudy);
	}

	public void delete(LinkSubjectStudy linkSubjectStudy) {
		getSession().delete(linkSubjectStudy);
	}

	public Upload refreshUpload(Upload upload) {
		getSession().refresh(upload);
		return upload;
	}

	public Upload getUpload(Long id) {
		return (Upload) getSession().get(Upload.class, id);
	}

	public GenderType getDefaultGenderType() {
		return getGenderType(0L);// TODO meaningful use of constants perhaps or a default bool in db
	}

	// TODO ASAP - I see hardcoding everywhere, fix
	public MaritalStatus getDefaultMaritalStatus() {
		return getMaritalStatusNyName("Unknown");
	}

	public SubjectStatus getDefaultSubjectStatus() {
		return getSubjectStatusByName("Subject");
	}

	public TitleType getDefaultTitleType() {
		return getTitleType(new Long(0));
	}

	public VitalStatus getDefaultVitalStatus() {
		return getVitalStatus(new Long(0));
	}

	public AddressType getDefaultAddressType() {
		// return getAddressType(new Long(0)); TODO replace the logic in these methods with there list criteria with .get[0] to simple session gets with
		// the id
		return (AddressType) (getSession().get(AddressType.class, 1L));
	}

	// TODO fix hardcoding
	public AddressStatus getDefaultAddressStatus() {
		return (AddressStatus) (getSession().get(AddressStatus.class, 1L));
	}

	public PhoneType getDefaultPhoneType() {
		// return getPhoneType(new Long(0)); TODO replace the logic in these methods with there list criteria with .get[0] to simple session gets with
		// the id
		return (PhoneType) (getSession().get(PhoneType.class, 1L));
	}

	// TODO fix hardcoding
	public PhoneStatus getDefaultPhoneStatus() {
		return (PhoneStatus) (getSession().get(PhoneStatus.class, 1L));
	}

	public ConsentOption getConsentOptionForBoolean(boolean trueForYesFalseForNo) {
		if (trueForYesFalseForNo) {
			return getConsentOption("YES");
		}
		else {
			return getConsentOption("NO");
		}
	}

	public void setPreferredMailingAdressToFalse(Person person) {
		String queryString = "UPDATE Address SET preferredMailingAddress = 0 WHERE person = :person";
		Query query = getSession().createQuery(queryString);
		query.setParameter("person", person);
		query.executeUpdate();
	}

	public EmailStatus getDefaultEmailStatus() {
		return (EmailStatus) (getSession().get(EmailStatus.class, 0L));// TODO hardcode removal
	}

	public List<ConsentOption> getConsentOptions() {
		Criteria criteria = getSession().createCriteria(ConsentOption.class);
		return criteria.list();
	}

	public List<ConsentStatus> getConsentStatus() {
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		return criteria.list();
	}

	public List<ConsentType> getConsentType() {
		Criteria criteria = getSession().createCriteria(ConsentType.class);
		return criteria.list();
	}

	public RelationshipVo getSubjectRelative(final String subjectUID, final Long studyId) {
		List<RelationshipVo> relatives = new ArrayList<RelationshipVo>();
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class, "sub");
		criteria.createAlias("sub.study", "substudy", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("sub.person", "subPerson", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("subPerson.genderType", "subGender", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("subPerson.vitalStatus", "subVitStatus", JoinType.LEFT_OUTER_JOIN);

		criteria.createAlias("substudy.pedigreeConfiguration", "spc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("spc.customField", "cf", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cf.customFieldDisplay", "cfd", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("sub.subjectCustomFieldDataSet", "scfd", JoinType.LEFT_OUTER_JOIN,Restrictions.eqProperty("cfd.id", "scfd.customFieldDisplay.id"));
		
		criteria.add(Restrictions.eq("sub.subjectUID", subjectUID));
		criteria.add(Restrictions.eq("substudy.id", studyId));

		criteria.setFetchMode("subject", FetchMode.JOIN);
		criteria.setFetchMode("relative", FetchMode.JOIN);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sub.subjectUID"), "individualId");
		projectionList.add(Projections.property("subGender.name"), "gender");
		projectionList.add(Projections.property("subPerson.dateOfBirth"), "dob");
		projectionList.add(Projections.property("subPerson.dateOfDeath"), "dod");
		projectionList.add(Projections.property("subVitStatus.name"), "deceased");
		
		projectionList.add(Projections.property("scfd.textDataValue"), "affectedStatus");

		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(RelationshipVo.class));

		relatives = criteria.list();

		return relatives.size() > 0 ? relatives.get(0) : null;
	}

	public List<RelationshipVo> getSubjectParentRelatives(final String subjectUID, final Long studyId) {
		List<RelationshipVo> relatives = new ArrayList<RelationshipVo>();
		Criteria criteria = getSession().createCriteria(LinkSubjectPedigree.class, "lsp");
		criteria.createAlias("subject", "sub", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("sub.study", "substudy", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("relative", "rel", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("rel.study", "relstudy", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("relationship", "type", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("rel.person", "relPerson", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("relPerson.genderType", "relGender", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("relPerson.vitalStatus", "relVitStatus", JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("relstudy.pedigreeConfiguration", "spc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("spc.customField", "cf", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cf.customFieldDisplay", "cfd", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("rel.subjectCustomFieldDataSet", "scfd", JoinType.LEFT_OUTER_JOIN,Restrictions.eqProperty("cfd.id", "scfd.customFieldDisplay.id"));

		criteria.add(Restrictions.eq("sub.subjectUID", subjectUID));
		criteria.add(Restrictions.eq("substudy.id", studyId));
		criteria.add(Restrictions.eq("relstudy.id", studyId));

		criteria.setFetchMode("subject", FetchMode.JOIN);
		criteria.setFetchMode("relative", FetchMode.JOIN);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("lsp.id"), "id");
		projectionList.add(Projections.property("rel.subjectUID"), "individualId");
		projectionList.add(Projections.property("relGender.name"), "gender");
		projectionList.add(Projections.property("relPerson.dateOfBirth"), "dob");
		projectionList.add(Projections.property("relPerson.dateOfDeath"), "dod");
		projectionList.add(Projections.property("relPerson.firstName"), "firstName");
		projectionList.add(Projections.property("relPerson.lastName"), "lastName");
		projectionList.add(Projections.property("relVitStatus.name"), "deceased");
		
		projectionList.add(Projections.property("scfd.textDataValue"), "affectedStatus");

		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(RelationshipVo.class));

		relatives = criteria.list();
		return relatives;
	}

	public List<RelationshipVo> getSubjectChildRelatives(final String subjectUID, final Long studyId) {
		List<RelationshipVo> relatives = new ArrayList<RelationshipVo>();
		Criteria criteria = getSession().createCriteria(LinkSubjectPedigree.class, "lsp");
		criteria.createAlias("subject", "sub", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("sub.study", "substudy", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("relative", "rel", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("rel.study", "relstudy", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("relationship", "type", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("sub.person", "subPerson", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("subPerson.genderType", "subGender", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("subPerson.vitalStatus", "subVitStatus", JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("substudy.pedigreeConfiguration", "spc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("spc.customField", "cf", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cf.customFieldDisplay", "cfd", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("sub.subjectCustomFieldDataSet", "scfd", JoinType.LEFT_OUTER_JOIN,Restrictions.eqProperty("cfd.id", "scfd.customFieldDisplay.id"));
		
		criteria.add(Restrictions.eq("rel.subjectUID", subjectUID));
		criteria.add(Restrictions.eq("substudy.id", studyId));
		criteria.add(Restrictions.eq("relstudy.id", studyId));
		
		criteria.setFetchMode("subject", FetchMode.JOIN);
		criteria.setFetchMode("relative", FetchMode.JOIN);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("lsp.id"), "id");
		projectionList.add(Projections.property("sub.subjectUID"), "individualId");
		projectionList.add(Projections.property("subGender.name"), "gender");
		projectionList.add(Projections.property("subPerson.dateOfBirth"), "dob");
		projectionList.add(Projections.property("subPerson.dateOfDeath"), "dod");
		projectionList.add(Projections.property("subPerson.firstName"), "firstName");
		projectionList.add(Projections.property("subPerson.lastName"), "lastName");
		projectionList.add(Projections.property("subVitStatus.name"), "deceased");
		
		projectionList.add(Projections.property("scfd.textDataValue"), "affectedStatus");

		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(RelationshipVo.class));

		relatives = criteria.list();
		return relatives;
	}

	public void deleteRelationship(final LinkSubjectPedigree relationship) {
		getSession().delete(relationship);
	}

	public List<TwinType> getTwinTypes() {
		Example example = Example.create(new TwinType());
		Criteria criteria = getSession().createCriteria(TwinType.class).add(example);
		return criteria.list();
	}

	public void create(LinkSubjectTwin twin) {
		getSession().save(twin);
	}

	public void update(LinkSubjectTwin twin) {
		getSession().update(twin);
	}

	public void delete(LinkSubjectTwin twin) {
		getSession().delete(twin);
	}

	public Integer getSubjectParentCount(final LinkSubjectStudy subject) {
		Criteria criteria = getSession().createCriteria(LinkSubjectPedigree.class);
		criteria.add(Restrictions.eq("subject", subject));
		criteria.setProjection(Projections.rowCount());
		return ((Integer) criteria.list().get(0)).intValue();
	}

	public List<LinkSubjectPedigree> getSubjectParentRelationshipList(final LinkSubjectStudy subject) {
		Criteria criteria = getSession().createCriteria(LinkSubjectPedigree.class);
		criteria.add(Restrictions.eq("subject", subject));
		return criteria.list();
	}

	public List<LinkSubjectPedigree> getParentNonSubjectRelationshipList(final LinkSubjectStudy subject, final LinkSubjectStudy parentSubject) {
		Criteria criteria = getSession().createCriteria(LinkSubjectPedigree.class);
		criteria.add(Restrictions.eq("relative", parentSubject));
		criteria.add(Restrictions.ne("subject", subject));
		return criteria.list();
	}

	public List<RelationshipVo> getSubjectTwins(final String subjectUid, final Set<String> subjectUids, final Long studyId) {
		// StringBuffer sb = new
		// StringBuffer("select lss.subject_uid as individualId, p.FIRST_NAME as firstName,p.LAST_NAME as lastName,p.DATE_OF_BIRTH as dob,slt.id as id , IFNULL(tw.name,'NT') as twin ");
		StringBuffer sb = new StringBuffer("select lss.subject_uid as individualId, p.FIRST_NAME as firstName,p.LAST_NAME as lastName,p.DATE_OF_BIRTH as dob,slt.id as id , tw.name as twin ");
		sb.append(" From study.link_subject_study lss ");
		sb.append(" 	inner join study.study st on st.id=lss.study_id ");
		sb.append(" 	inner join study.person p on p.id=lss.person_id ");
		sb.append(" 	left outer join study.link_subject_twin slt on (slt.first_subject = lss.id and slt.second_Subject = (select a.id ");
		sb.append(" 		From study.link_subject_study a ");
		sb.append(" 			inner join study.study st on st.id=a.study_id ");
		sb.append(" 		where a.SUBJECT_UID in (:asubjectuid) ");
		sb.append(" 			and st.id= :astudyid )) or (slt.second_subject = lss.id and slt.first_Subject = (select b.id ");
		sb.append(" 				From study.link_subject_study b ");
		sb.append(" 					inner join study.study st on st.id=b.study_id ");
		sb.append(" 				where b.SUBJECT_UID in ( :bsubjectuid ) ");
		sb.append(" 				and st.id= :bstudyid )) ");
		sb.append(" 	left outer join study.twin_type tw on tw.id = slt.twin_type_id ");
		sb.append(" where lss.SUBJECT_UID in ( :subjectUids ) ");
		sb.append(" 	and st.id= :studyId ");

		List<RelationshipVo> twins = getSession().createSQLQuery(sb.toString()).addScalar("individualId").addScalar("id").addScalar("firstName").addScalar("lastName").addScalar("dob").addScalar("twin")
				.setParameter("asubjectuid", subjectUid).setParameter("astudyid", studyId).setParameter("bsubjectuid", subjectUid).setParameter("bstudyid", studyId)
				.setParameterList("subjectUids", subjectUids != null ? subjectUids : new HashSet<String>()).setParameter("studyId", studyId)
				.setResultTransformer(Transformers.aliasToBean(RelationshipVo.class)).list();

		return twins;

	}

	public List<LinkSubjectTwin> getTwins(final Set<String> subjectUids, final Long studyId) {
		List<LinkSubjectTwin> twins = new ArrayList<LinkSubjectTwin>();
		Criteria criteria = getSession().createCriteria(LinkSubjectTwin.class, "lst");
		criteria.createAlias("firstSubject", "lssa", JoinType.INNER_JOIN);
		criteria.createAlias("lssa.study", "sta", JoinType.INNER_JOIN);
		criteria.createAlias("secondSubject", "lssb", JoinType.INNER_JOIN);
		criteria.createAlias("lssb.study", "stb", JoinType.INNER_JOIN);

		criteria.setFetchMode("firstSubject", FetchMode.JOIN);
		criteria.setFetchMode("secondSubject", FetchMode.JOIN);
		criteria.setFetchMode("twinType", FetchMode.JOIN);

		criteria.add(Restrictions.eq("sta.id", studyId));
		criteria.add(Restrictions.eq("stb.id", studyId));
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.in("lssa.subjectUID", subjectUids));
		or.add(Restrictions.in("lssb.subjectUID", subjectUids));
		criteria.add(or);

		twins = criteria.list();

		return twins;

	}

	public long getRelationshipCount(final String subjectUID, final Long studyId) {
		long count = 0;
		Criteria criteria = getSession().createCriteria(LinkSubjectPedigree.class, "lsp");
		criteria.createAlias("subject", "sub", JoinType.INNER_JOIN);
		criteria.createAlias("relative", "rel", JoinType.INNER_JOIN);
		criteria.createAlias("sub.study", "substudy", JoinType.INNER_JOIN);
		criteria.createAlias("rel.study", "relstudy", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("substudy.id", studyId));
		criteria.add(Restrictions.eq("relstudy.id", studyId));
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.eq("sub.subjectUID", subjectUID));
		or.add(Restrictions.eq("rel.subjectUID", subjectUID));
		criteria.add(or);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.count("lsp.id"));
		criteria.setProjection(projList);
		List list = criteria.list();
		if (list.size() > 0) {
			count = Integer.parseInt(list.get(0).toString());
		}
		return count;
	}

	public List<OtherID> getPersonOtherIDList(Long personID) throws ArkSystemException {
		List<OtherID> personOtherIDList = getSession().createCriteria(OtherID.class).add(Restrictions.eq(Constants.PERSON_PERSON_ID, personID)).list();
		log.info("Number of otherIDs fetched " + personOtherIDList.size() + "Person ID " + personID.intValue());

		if (personOtherIDList.isEmpty()) {
			log.error("this person has no other IDs; " + personID);
		}
		log.info("Number of OtherID items retrieved for person ID " + personID + " " + personOtherIDList.size());
		return personOtherIDList;
	}

	public List<CustomField> getBinaryCustomFieldsForPedigreeRelativesList(Long studyId) {
		List<CustomField> pedigreeCustomFields = null;
		Criteria criteria = getSession().createCriteria(CustomField.class, "cf");
		criteria.createAlias("study", "st", JoinType.INNER_JOIN);
		criteria.createAlias("arkFunction", "af", JoinType.INNER_JOIN);
		criteria.createAlias("customFieldType", "cft", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fieldType", "ft", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("st.id", studyId));
		criteria.add(Restrictions.eq("af.name", au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD));
		criteria.add(Restrictions.eq("cf.encodedValues", "0=Yes;1=No;").ignoreCase());
		criteria.add(Restrictions.or(Restrictions.isNull("cft.id"),Restrictions.eq("cft.name", au.org.theark.core.Constants.SUBJECT)));
		pedigreeCustomFields = criteria.list();

		return pedigreeCustomFields;
	}
	@Override
	public List<CustomField> getFamilyUIdCustomFieldsForPedigreeRelativesList(Long studyId) {
		List<CustomField> pedigreeCustomFields = null;
		Criteria criteria = getSession().createCriteria(CustomField.class, "cf");
		criteria.createAlias("study", "st", JoinType.INNER_JOIN);
		criteria.createAlias("arkFunction", "af", JoinType.INNER_JOIN);
		criteria.createAlias("fieldType", "ft", JoinType.INNER_JOIN);
		criteria.createAlias("customFieldType", "cft", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("st.id", studyId));
		criteria.add(Restrictions.eq("af.name",  au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD));
		criteria.add(Restrictions.isNull("cf.encodedValues"));
		criteria.add(Restrictions.eq("ft.name",au.org.theark.core.Constants.FIELD_TYPE_CHARACTER));
		criteria.add(Restrictions.or(Restrictions.isNull("cft.id"),Restrictions.eq("cft.name", au.org.theark.core.Constants.SUBJECT)));
		
		pedigreeCustomFields = criteria.list();

		return pedigreeCustomFields;
	}


	public StudyPedigreeConfiguration getStudyPedigreeConfiguration(Long studyId) {

		StudyPedigreeConfiguration pedigreeConfig = null;
		
		Criteria criteria = getSession().createCriteria(StudyPedigreeConfiguration.class, "spc");
		criteria.createAlias("study", "st", JoinType.INNER_JOIN);
		criteria.setFetchMode("customField", FetchMode.JOIN);

		criteria.add(Restrictions.eq("st.id", studyId));

		List<StudyPedigreeConfiguration> list = criteria.list();
		pedigreeConfig = list.size() == 1 ? list.get(0) : new StudyPedigreeConfiguration();

		return pedigreeConfig;

	}

	public void saveOrUpdateStudyPedigreeConfiguration(StudyPedigreeConfiguration config) {
		if (config.getId() == null) {
			getSession().save(config);
		}
		else {
			getSession().update(config);
		}
	}
	/**
	 * Pageable person phone list.
	 */
	public List<Phone> pageablePersonPhoneLst(Long personId,Phone phoneCriteria, int first, int count) {
		Criteria criteria = buildGeneralPhoneCriteria(personId,phoneCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<Phone> personPhoneList = criteria.list();
		//log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());
		if (personPhoneList.isEmpty()) {
			// throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
			log.info(" personId " + personId + " had no phones.  No drama");
		}
		return personPhoneList;
	}
	/**
	 * Genenal Phone search.
	 * @param personId
	 * @param phone
	 * @return
	 */
	private Criteria buildGeneralPhoneCriteria(Long personId,Phone phone){
		Criteria phoneCriteria = getSession().createCriteria(Phone.class);

		if (personId != null) {
			phoneCriteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}

		if (phone != null) {

			if (phone.getId() != null) {
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_ID, phone.getId()));
			}

			if (phone.getPhoneNumber() != null) {
				phoneCriteria.add(Restrictions.ilike(Constants.PHONE_NUMBER, phone.getPhoneNumber()));
			}

			if (phone.getPhoneType() != null) {
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_TYPE, phone.getPhoneType()));
			}

			if (phone.getAreaCode() != null) {
				phoneCriteria.add(Restrictions.eq(Constants.AREA_CODE, phone.getAreaCode()));
			}
			phoneCriteria.setFetchMode("silentMode", FetchMode.JOIN);

		}
		return phoneCriteria;
	}

	public List<Address> pageablePersonAddressLst(Long personId, Address addressCriteria, int first, int count) {
		Criteria criteria = buildGeneralAddressCriteria(personId,addressCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<Address> personAddressList = criteria.list();
		//log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());
		if (personAddressList.isEmpty()) {
			// throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
			log.info(" personId " + personId + " had no addresses.  No drama");
		}
		return personAddressList;
	}
	/**
	 * General Address search.
	 * @param personId
	 * @param address
	 * @return
	 */
	private Criteria buildGeneralAddressCriteria(Long personId,Address address){
		Criteria criteria = getSession().createCriteria(Address.class);
		if (personId != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}

		if (address != null) {
			// Add criteria for address
			if (address.getStreetAddress() != null) {
				criteria.add(Restrictions.ilike(Constants.STREET_ADDRESS, address.getStreetAddress(), MatchMode.ANYWHERE));
			}

			if (address.getCountry() != null) {
				criteria.add(Restrictions.eq(Constants.COUNTRY_NAME, address.getCountry()));
			}

			if (address.getPostCode() != null) {
				criteria.add(Restrictions.eq(Constants.POST_CODE, address.getPostCode()));
			}

			if (address.getCity() != null) {
				criteria.add(Restrictions.ilike(Constants.CITY, address.getCity()));
			}

			if (address.getState() != null) {
				criteria.add(Restrictions.eq(Constants.STATE_NAME, address.getState()));
			}

			if (address.getAddressType() != null) {
				criteria.add(Restrictions.eq(Constants.ADDRESS_TYPE, address.getAddressType()));
			}
		}
		return criteria;
	}

	@Override
	public void processFieldsBatch(List<? extends ICustomFieldData> fieldsToUpdate, Study study,List<? extends ICustomFieldData> fieldsToInsert) {
		Session session = getSession();
		int count = 0;
		for (ICustomFieldData dataToUpdate : fieldsToUpdate) {
			session.update(dataToUpdate);
			count++;
			// based on recommended hibernate practice of <prop key="hibernate.jdbc.batch_size">50</prop>
			if (count % 50 == 0) {
				log.info("\n\n\n\n\n\n\n\n\nflush!!!!!!!!!!!!!!"); // TODO Evaluate why batch not working. hints: may be identity/id generation related.
																					// Will revisit after all batch work done
				session.flush();
				session.clear();
			}
		}
		count = 0;
		for (ICustomFieldData dataToInsert : fieldsToInsert) {
			session.save(dataToInsert);
			count++;
			// based on recommended hibernate practice of <prop key="hibernate.jdbc.batch_size">50</prop>
			if (count % 50 == 0) {
				log.info("\n\n\n\n\n\n\n\n\nflush!!!!!!!!!!!!!!");
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();
	}

	@Override
	public void setPreferredPhoneNumberToFalse(Person person) {
		String queryString = "UPDATE Phone SET preferredPhoneNumber = 0 WHERE person = :person";
		Query query = getSession().createQuery(queryString);
		query.setParameter("person", person);
		query.executeUpdate();
	}
	
	public void saveOrUpdate(StudyCalendar studyCalendar){
		if(studyCalendar.getId() == null || studyCalendar.getId() == 0){
			getSession().save(studyCalendar);
		}else{
			getSession().update(studyCalendar);
		}
	}
	
	@Override
	public void saveOrUpdate(StudyCalendarVo studyCalendarVo) {
		StudyCalendar calendar = studyCalendarVo.getStudyCalendar();
		saveOrUpdate(calendar);
		
		Criteria criteria = getSession().createCriteria(LinkCalendarCustomField.class);
		criteria.add(Restrictions.eq("studyCalendar", calendar));
		List<LinkCalendarCustomField> searchResults = criteria.list();
		for (LinkCalendarCustomField calendarCustomFields : searchResults) {
			deleteCalendarCustomField(calendarCustomFields);
		}
		
		for(CustomField customField : studyCalendarVo.getSelectedCustomFields()){
			LinkCalendarCustomField linkCalendarCustomField = new LinkCalendarCustomField();
			linkCalendarCustomField.setStudyCalendar(calendar);
			linkCalendarCustomField.setCustomField(customField);
			saveCalendarCustomField(linkCalendarCustomField);
		}
	}
	
	private void saveCalendarCustomField(LinkCalendarCustomField calendarCustomField){
		getSession().save(calendarCustomField);
	}
	
	private void deleteCalendarCustomField(LinkCalendarCustomField calendarCustomField){
		getSession().delete(calendarCustomField);
	}

	public void delete(StudyCalendar studyCalendar){
		getSession().delete(studyCalendar);
	}
	
	public List<StudyCalendar> searchStudyCalenderList(StudyCalendar studyCalendar){
		List<StudyCalendar> list = new ArrayList<StudyCalendar>();
		Criteria criteria = getSession().createCriteria(StudyCalendar.class);
		
		criteria.add(Restrictions.eq("study", studyCalendar.getStudy()));
		
		if(studyCalendar.getName() !=null){
			criteria.add(Restrictions.ilike("name", studyCalendar.getName(),MatchMode.ANYWHERE));
		}
		
		if(studyCalendar.getStartDate() !=null){
			criteria.add(Restrictions.ge("startDate", studyCalendar.getStartDate()));
		}
		
		if(studyCalendar.getEndDate() !=null){
			criteria.add(Restrictions.le("endDate", studyCalendar.getEndDate()));
		}
		
		criteria.setFetchMode("studyComp", FetchMode.JOIN);
		
		list = criteria.list();
		
		return list;
	}
	
	public List<CustomField> getStudySubjectCustomFieldList(Long studyId){
		List<CustomField> list = new ArrayList<CustomField>();
		Criteria criteria = getSession().createCriteria(CustomField.class);
		criteria.createAlias("study", "st", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("customFieldType", "cft", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("st.id", studyId));
		criteria.add(Restrictions.eq("cft.name", au.org.theark.core.Constants.SUBJECT));
		list=criteria.list();
		return list;
	}
	
	public List<CustomField> getSelectedCalendarCustomFieldList(StudyCalendar studyCalendar){
		List<CustomField> list = new ArrayList<CustomField>();
		Criteria criteria = getSession().createCriteria(CustomField.class);
		criteria.createAlias("linkCalendarCustomField", "lccf", JoinType.INNER_JOIN);
		criteria.createAlias("linkCalendarCustomField.studyCalendar", "sc", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("sc.id", studyCalendar.getId()));
		list=criteria.list();
		return list;
	}
	public void delete(OtherID otherID) {
		getSession().delete(otherID);
	}
	public boolean isStudyComponentBeingUsedInConsent(StudyComp studyComp){
		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("study", studyComp.getStudy()));
		criteria.add(Restrictions.eq("studyComp",studyComp));
		List<Consent> consents=criteria.list();
		return (consents.size() > 0);
		
	}
}
