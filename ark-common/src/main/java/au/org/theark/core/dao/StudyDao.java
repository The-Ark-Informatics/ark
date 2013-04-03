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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BioCollectionUidPadChar;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BioCollectionUidToken;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiocollectionFieldSearch;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.BiospecimenFieldSearch;
import au.org.theark.core.model.report.entity.CustomFieldDisplaySearch;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.DemographicFieldSearch;
import au.org.theark.core.model.report.entity.Entity;
import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Operator;
import au.org.theark.core.model.report.entity.QueryFilter;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.report.entity.SearchPayload;
import au.org.theark.core.model.report.entity.SearchResult;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
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
import au.org.theark.core.util.CsvListReader;
import au.org.theark.core.vo.DataExtractionVO;
import au.org.theark.core.vo.ExtractionVO;
import au.org.theark.core.vo.QueryFilterVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 * @param <T>
 * 
 */
@Repository("commonStudyDao")
public class StudyDao<T> extends HibernateSessionDao implements IStudyDao {

	private static Logger	log	= LoggerFactory.getLogger(StudyDao.class);
	protected static final String			HEXES					= "0123456789ABCDEF";
	private IDataExtractionDao iDataExtractionDao;

	/**
	 * @return the iDataExtractionDao
	 */
	public IDataExtractionDao getiDataExtractionDao() {
		return iDataExtractionDao;
	}

	/**
	 * @param iDataExtractionDao the iDataExtractionDao to set
	 */
	@Autowired
	public void setiDataExtractionDao(IDataExtractionDao iDataExtractionDao) {
		this.iDataExtractionDao = iDataExtractionDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.dao.IStudyDao#getStudy(au.org.theark.core.model.study .entity.Study)
	 */
	@SuppressWarnings("unchecked")
	public List<Study> getStudy(Study study) {

		Criteria studyCriteria = getSession().createCriteria(Study.class);

		if (study.getId() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_KEY, study.getId()));
		}

		if (study.getName() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.STUDY_NAME, study.getName(), MatchMode.ANYWHERE));
		}

		if (study.getDateOfApplication() != null) {
			studyCriteria.add(Restrictions.eq(Constants.DATE_OF_APPLICATION, study.getDateOfApplication()));
		}

		if (study.getEstimatedYearOfCompletion() != null) {
			studyCriteria.add(Restrictions.eq(Constants.EST_YEAR_OF_COMPLETION, study.getEstimatedYearOfCompletion()));
		}

		if (study.getChiefInvestigator() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CHIEF_INVESTIGATOR, study.getChiefInvestigator(), MatchMode.ANYWHERE));
		}

		if (study.getContactPerson() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CONTACT_PERSON, study.getContactPerson(), MatchMode.ANYWHERE));
		}

		if (study.getStudyStatus() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_STATUS, study.getStudyStatus()));
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}
		}
		else {
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}

		}

		studyCriteria.addOrder(Order.asc(Constants.STUDY_NAME));
		List<Study> studyList = studyCriteria.list();

		return studyList;
	}

	@SuppressWarnings("unchecked")
	public SubjectStatus getSubjectStatus(String statusName) {

		SubjectStatus statusToReturn = null;

		SubjectStatus subjectStatus = new SubjectStatus();
		subjectStatus.setName(statusName);
		Example example = Example.create(subjectStatus);

		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);

		if (criteria != null) {
			List<SubjectStatus> results = criteria.list();
			if (results != null && !results.isEmpty()) {
				statusToReturn = (SubjectStatus) results.get(0);
			}
		}

		return statusToReturn;
	}

	/**
	 * Given a status name will return the StudyStatus object.
	 */
	@SuppressWarnings("unchecked")
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException {
		StudyStatus studyStatus = new StudyStatus();
		studyStatus.setName("Archive");
		Example studyStatusExample = Example.create(studyStatus);

		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatusExample);
		if (studyStatusCriteria != null) {
			List<StudyStatus> results = studyStatusCriteria.list();
			if (results != null && results.size() > 0) {
				return (StudyStatus) results.get(0);
			}
		}

		log.error("Study Status Table maybe out of synch. Please check if it has an entry for Archive status.  Cannot locate a study status with " + statusName + " in the database");
		throw new StatusNotAvailableException();

	}

	@SuppressWarnings("unchecked")
	public List<StudyStatus> getListOfStudyStatus() {
		Example studyStatus = Example.create(new StudyStatus());
		Criteria criteria = getSession().createCriteria(StudyStatus.class).add(studyStatus);
		return criteria.list();

	}

	public Study getStudy(Long id) {
		Study study = (Study) getSession().get(Study.class, id);
		return study;
	}

	@SuppressWarnings("unchecked")
	public Collection<TitleType> getTitleType() {
		Example example = Example.create(new TitleType());
		Criteria criteria = getSession().createCriteria(TitleType.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<VitalStatus> getVitalStatus() {
		Example example = Example.create(new VitalStatus());
		Criteria criteria = getSession().createCriteria(VitalStatus.class).add(example);
		return criteria.list();
	}

	// TODO: cache?
	@SuppressWarnings("unchecked")
	public Collection<GenderType> getGenderTypes() {
		Example example = Example.create(new GenderType());
		Criteria criteria = getSession().createCriteria(GenderType.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PhoneType> getListOfPhoneType() {
		Example phoneTypeExample = Example.create(new PhoneType());
		Criteria criteria = getSession().createCriteria(PhoneType.class).add(phoneTypeExample);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SubjectStatus> getSubjectStatus() {

		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<MaritalStatus> getMaritalStatus() {
		Example example = Example.create(new MaritalStatus());
		Criteria criteria = getSession().createCriteria(MaritalStatus.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<EmailStatus> getAllEmailStatuses() {
		Example example = Example.create(new EmailStatus());
		Criteria criteria = getSession().createCriteria(EmailStatus.class).add(example);
		return criteria.list();
	}

	/**
	 * Look up the Link Subject Study for subjects linked to a study
	 * 
	 * @param subjectVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("person", "p");
		criteria.add(Restrictions.eq("study.id", subjectVO.getLinkSubjectStudy().getStudy().getId()));

		if (subjectVO.getLinkSubjectStudy().getPerson() != null) {

			if (subjectVO.getLinkSubjectStudy().getPerson().getId() != null) {
				criteria.add(Restrictions.eq("p.id", subjectVO.getLinkSubjectStudy().getPerson().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getFirstName() != null) {
				criteria.add(Restrictions.ilike("p.firstName", subjectVO.getLinkSubjectStudy().getPerson().getFirstName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getMiddleName() != null) {
				criteria.add(Restrictions.ilike("p.middleName", subjectVO.getLinkSubjectStudy().getPerson().getMiddleName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getLastName() != null) {
				criteria.add(Restrictions.ilike("p.lastName", subjectVO.getLinkSubjectStudy().getPerson().getLastName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
				criteria.add(Restrictions.eq("p.dateOfBirth", subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getGenderType() != null) {
				criteria.add(Restrictions.eq("p.genderType.id", subjectVO.getLinkSubjectStudy().getPerson().getGenderType().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus() != null) {
				criteria.add(Restrictions.eq("p.vitalStatus.id", subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus().getId()));
			}

		}

		if (subjectVO.getLinkSubjectStudy().getSubjectUID() != null && subjectVO.getLinkSubjectStudy().getSubjectUID().length() > 0) {
			criteria.add(Restrictions.eq("subjectUID", subjectVO.getLinkSubjectStudy().getSubjectUID()));
		}

		if (subjectVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("subjectStatus", subjectVO.getLinkSubjectStudy().getSubjectStatus()));
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}
		else {
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}

		criteria.addOrder(Order.asc("subjectUID"));
		List<LinkSubjectStudy> list = criteria.list();

		Collection<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {

			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			// Place the LinkSubjectStudy instance into a SubjectVO and add the
			// SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setLinkSubjectStudy(linkSubjectStudy);
			Person person = subject.getLinkSubjectStudy().getPerson();
			subject.setSubjectPreviousLastname(getPreviousLastname(person));
			subjectVOList.add(subject);
		}
		return subjectVOList;

	}

	@SuppressWarnings("unchecked")
	public List<Phone> getPhonesForPerson(Person person) {
		Criteria personCriteria = getSession().createCriteria(Phone.class);
		personCriteria.add(Restrictions.eq("person", person));// Filter the
		// phones linked
		// to this
		// personID/Key
		return personCriteria.list();
	}

	@SuppressWarnings("unchecked")
	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException {

		Criteria linkSubjectStudyCriteria = getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("id", id));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if (listOfSubjects != null && listOfSubjects.size() > 0) {
			return listOfSubjects.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}
	}

	@SuppressWarnings("unchecked")
	public LinkSubjectStudy getSubjectByUID(String subjectUID, Study study) throws EntityNotFoundException {

		Criteria linkSubjectStudyCriteria = getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("subjectUID", subjectUID));
		linkSubjectStudyCriteria.add(Restrictions.eq("study", study));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if (listOfSubjects != null && listOfSubjects.size() > 0) {
			return listOfSubjects.get(0);
		}
		else {
			throw new EntityNotFoundException("There is no subject with the given UID " + subjectUID.toString());
		}
	}

	/**
	 * returns a the subject (linksubjectystudy) IF there is one, else returns null
	 * 
	 * Note this is actively fetching person
	 * 
	 * @param subjectUID
	 * @param study
	 * @return LinkSubjectStudy
	 */
	public LinkSubjectStudy getSubjectByUIDAndStudy(String subjectUID, Study study) {
		log.warn("about to create query right now");
		Criteria linkSubjectStudyCriteria = getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("subjectUID", subjectUID));
		linkSubjectStudyCriteria.add(Restrictions.eq("study", study));
		return (LinkSubjectStudy) linkSubjectStudyCriteria.uniqueResult();
	}

	/**
	 * Returns a list of Countries
	 */
	@SuppressWarnings("unchecked")
	public List<Country> getCountries() {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	// TODO
	public Country getCountry(Long id) {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("id", id));
		return (Country) criteria.list().get(0);
	}

	// TODO HARDCODING
	public Country getCountry(String countryCode) {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("countryCode", countryCode));
		return (Country) criteria.list().get(0);
	}

	public List<State> getStates(Country country) {

		if (country == null) {
			country = getCountry(Constants.DEFAULT_COUNTRY_CODE);
		}
		Criteria criteria = getSession().createCriteria(State.class);
		criteria.add(Restrictions.eq("country", country));
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	/**
	 * Gets a list of all Address Types
	 * 
	 * @return
	 */
	public List<AddressType> getAddressTypes() {
		Criteria criteria = getSession().createCriteria(AddressType.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	/**
	 * Gets a list of all Phone Statuses
	 * 
	 * @return
	 */
	public List<PhoneStatus> getPhoneStatuses() {
		Criteria criteria = getSession().createCriteria(PhoneStatus.class);
		return criteria.list();
	}

	/**
	 * Gets a list of all Phone Types
	 * 
	 * @return
	 */
	public List<PhoneType> getPhoneTypes() {
		Criteria criteria = getSession().createCriteria(PhoneType.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	/**
	 * Gets a list of all Address Statuses
	 * 
	 * @return
	 */
	public List<AddressStatus> getAddressStatuses() {
		Criteria criteria = getSession().createCriteria(AddressStatus.class);
		return criteria.list();
	}

	public List<ConsentStatus> getConsentStatus() {
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		return criteria.list();
	}

	public List<StudyCompStatus> getStudyComponentStatus() {
		Criteria criteria = getSession().createCriteria(StudyCompStatus.class);
		return criteria.list();
	}

	public List<StudyComp> getStudyComponentByStudy(Study study) {
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		criteria.add(Restrictions.eq("study", study));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public boolean isSubjectConsentedToComponent(StudyComp studyComponent, Person person, Study study) {
		boolean isConsented = false;
		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("studyComp", studyComponent));
		criteria.add(Restrictions.eq("study", study));
		criteria.createAlias("linkSubjectStudy", "lss");
		criteria.add(Restrictions.eq("lss.person", person));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			isConsented = true;
		}
		return isConsented;
	}

	/**
	 * Returns a list of Consent types hardcopy, electronic document etc.
	 * 
	 * @return
	 */
	public List<ConsentType> getConsentType() {
		Criteria criteria = getSession().createCriteria(ConsentType.class);
		return criteria.list();
	}

	public List<ConsentAnswer> getConsentAnswer() {
		Criteria criteria = getSession().createCriteria(ConsentAnswer.class);
		return criteria.list();
	}

	public List<YesNo> getYesNoList() {
		Criteria criteria = getSession().createCriteria(YesNo.class);
		return criteria.list();
	}

	public void createAuditHistory(AuditHistory auditHistory, String userId, Study study) {
		Date date = new Date(System.currentTimeMillis());

		if (userId == null) {// if not forcing a userID manually, get
			// currentuser
			Subject currentUser = SecurityUtils.getSubject();
			auditHistory.setArkUserId((String) currentUser.getPrincipal());
		}
		else {
			auditHistory.setArkUserId(userId);
		}
		if (study == null) {
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			if (sessionStudyId != null && auditHistory.getStudyStatus() == null) {
				auditHistory.setStudyStatus(getStudy(sessionStudyId).getStudyStatus());
			}
			else {

				if (auditHistory.getEntityType().equalsIgnoreCase(au.org.theark.core.Constants.ENTITY_TYPE_STUDY)) {
					Study studyFromDB = getStudy(auditHistory.getEntityId());
					if (studyFromDB != null) {
						auditHistory.setStudyStatus(studyFromDB.getStudyStatus());
					}
				}
			}
		}
		else {
			auditHistory.setStudyStatus(study.getStudyStatus());
		}
		auditHistory.setDateTime(date);
		getSession().save(auditHistory);
		// getSession().flush();
	}

	public void createAuditHistory(AuditHistory auditHistory) {
		createAuditHistory(auditHistory, null, null);
	}

	public List<PersonContactMethod> getPersonContactMethodList() {
		Criteria criteria = getSession().createCriteria(PersonContactMethod.class);
		return criteria.list();
	}

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory) {
		PersonLastnameHistory personLastnameHistoryToReturn = null;

		Example example = Example.create(personSurnameHistory);

		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class).add(example);
		if (criteria != null) {// should it ever?
			List<PersonLastnameHistory> results = criteria.list();
			if (results != null && !results.isEmpty()) {
				personLastnameHistoryToReturn = (PersonLastnameHistory) results.get(0);
			}
		}
		return personLastnameHistoryToReturn;
	}

	public String getPreviousLastname(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}
		criteria.addOrder(Order.asc("id"));
		PersonLastnameHistory personLastameHistory = new PersonLastnameHistory();

		List<PersonLastnameHistory> results = criteria.list();
		if (results.size() > 0) {

			// what this is saying is get the second-last last-name to display
			// as "previous lastname"
			personLastameHistory = (PersonLastnameHistory) results.get(results.size() - 1);
		}// else it doesnt have a previous...only a current

		return personLastameHistory.getLastName();
	}

	public List<PersonLastnameHistory> getLastnameHistory(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}

		return criteria.list();
	}

	public LinkSubjectStudy getSubject(Long personId, Study study) throws EntityNotFoundException {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("person.id", personId));
		criteria.add(Restrictions.eq("study", study));
		LinkSubjectStudy subject = (LinkSubjectStudy) criteria.uniqueResult();
		if (subject == null) {
			throw new EntityNotFoundException("The Subject does not exist in the system");
		}
		return subject;
	}

	public List<SubjectUidPadChar> getListOfSubjectUidPadChar() {
		Example subjectUidPadChar = Example.create(new SubjectUidPadChar());
		Criteria studyStatusCriteria = getSession().createCriteria(SubjectUidPadChar.class).add(subjectUidPadChar);
		return studyStatusCriteria.list();
	}

	public String getSubjectUidExample(Study study) {
		String subjectUidPrefix = new String("");
		String subjectUidToken = new String("");
		String subjectUidPaddedIncrementor = new String("");
		String subjectUidPadChar = new String("0");
		String subjectUidStart = new String("");
		String subjectUidExample = new String("");

		if (study.getId() != null && study.getAutoGenerateSubjectUid() != null) {
			if (study.getSubjectUidPrefix() != null)
				subjectUidPrefix = study.getSubjectUidPrefix();

			if (study.getSubjectUidToken() != null)
				subjectUidToken = study.getSubjectUidToken().getName();

			if (study.getSubjectUidPadChar() != null) {
				subjectUidPadChar = study.getSubjectUidPadChar().getName().trim();
			}

			if (study.getSubjectUidStart() != null)
				subjectUidStart = study.getSubjectUidStart().toString();

			int size = Integer.parseInt(subjectUidPadChar);
			subjectUidPaddedIncrementor = StringUtils.leftPad(subjectUidStart, size, "0");
			subjectUidExample = subjectUidPrefix + subjectUidToken + subjectUidPaddedIncrementor;
		}
		else {
			subjectUidPrefix = "";
			subjectUidToken = "";
			subjectUidPadChar = "";
			subjectUidPaddedIncrementor = "";
			subjectUidExample = null;
		}
		return subjectUidExample;
	}

	public List<SubjectUidToken> getListOfSubjectUidToken() {
		Example subjectUidToken = Example.create(new SubjectUidToken());
		Criteria studyStatusCriteria = getSession().createCriteria(SubjectUidToken.class).add(subjectUidToken);
		return studyStatusCriteria.list();
	}

	public GenderType getGenderType(String name) {
		Criteria criteria = getSession().createCriteria(GenderType.class);
		criteria.add(Restrictions.eq("name", name));
		GenderType genderType = new GenderType();
		List<GenderType> results = criteria.list();
		if (!results.isEmpty()) {
			genderType = (GenderType) results.get(0);
		}
		return genderType;
	}

	public VitalStatus getVitalStatus(String name) {
		Criteria criteria = getSession().createCriteria(VitalStatus.class);
		criteria.add(Restrictions.eq("name", name));
		VitalStatus vitalStatus = new VitalStatus();
		List<VitalStatus> results = criteria.list();

		if (!results.isEmpty()) {
			vitalStatus = (VitalStatus) results.get(0);
		}
		return vitalStatus;
	}

	public TitleType getTitleType(String name) {
		Criteria criteria = getSession().createCriteria(TitleType.class);
		criteria.add(Restrictions.eq("name", name));
		TitleType titleType = new TitleType();
		List<TitleType> results = criteria.list();
		if (!results.isEmpty()) {
			titleType = (TitleType) results.get(0);
		}
		return titleType;
	}

	public MaritalStatus getMaritalStatus(String name) {
		Criteria criteria = getSession().createCriteria(MaritalStatus.class);
		criteria.add(Restrictions.eq("name", name));
		MaritalStatus maritalStatus = new MaritalStatus();
		List<MaritalStatus> results = criteria.list();
		if (!results.isEmpty()) {
			maritalStatus = (MaritalStatus) results.get(0);
		}
		return maritalStatus;
	}

	public PersonContactMethod getPersonContactMethod(String name) {
		Criteria criteria = getSession().createCriteria(PersonContactMethod.class);
		criteria.add(Restrictions.eq("name", name));
		PersonContactMethod personContactMethod = new PersonContactMethod();
		List<PersonContactMethod> results = criteria.list();
		if (!results.isEmpty()) {
			personContactMethod = (PersonContactMethod) results.get(0);
		}
		return personContactMethod;
	}

	public long getStudySubjectCount(SubjectVO subjectVO) {
		// Handle for study not in context
		if (subjectVO.getLinkSubjectStudy().getStudy() == null) {
			return 0;
		}

		Criteria criteria = buildGeneralSubjectCriteria(subjectVO);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount.intValue();
	}

	private Criteria buildGeneralSubjectCriteria(SubjectVO subjectVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("person", "p");
		criteria.add(Restrictions.eq("study.id", subjectVO.getLinkSubjectStudy().getStudy().getId()));

		if (subjectVO.getLinkSubjectStudy().getPerson() != null) {

			if (subjectVO.getLinkSubjectStudy().getPerson().getId() != null) {
				criteria.add(Restrictions.eq("p.id", subjectVO.getLinkSubjectStudy().getPerson().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getFirstName() != null) {
				criteria.add(Restrictions.ilike("p.firstName", subjectVO.getLinkSubjectStudy().getPerson().getFirstName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getMiddleName() != null) {
				criteria.add(Restrictions.ilike("p.middleName", subjectVO.getLinkSubjectStudy().getPerson().getMiddleName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getLastName() != null) {
				criteria.add(Restrictions.ilike("p.lastName", subjectVO.getLinkSubjectStudy().getPerson().getLastName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
				criteria.add(Restrictions.eq("p.dateOfBirth", subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getGenderType() != null) {
				criteria.add(Restrictions.eq("p.genderType.id", subjectVO.getLinkSubjectStudy().getPerson().getGenderType().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus() != null) {
				criteria.add(Restrictions.eq("p.vitalStatus.id", subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus().getId()));
			}

		}

		if (subjectVO.getLinkSubjectStudy().getSubjectUID() != null && subjectVO.getLinkSubjectStudy().getSubjectUID().length() > 0) {
			criteria.add(Restrictions.eq("subjectUID", subjectVO.getLinkSubjectStudy().getSubjectUID()));
		}

		if (subjectVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("subjectStatus", subjectVO.getLinkSubjectStudy().getSubjectStatus()));
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}
		else {
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}

		criteria.addOrder(Order.asc("subjectUID"));
		return criteria;
	}

	public List<SubjectVO> searchPageableSubjects(SubjectVO subjectVoCriteria, int first, int count) {
		Criteria criteria = buildGeneralSubjectCriteria(subjectVoCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<LinkSubjectStudy> list = criteria.list();
		List<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();

		// TODO analyse
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {

			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			// Place the LinkSubjectStudy instance into a SubjectVO and add the
			// SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setLinkSubjectStudy(linkSubjectStudy);
			// Person person = subject.getLinkSubjectStudy().getPerson();
			subject.setSubjectUID(linkSubjectStudy.getSubjectUID());
			subjectVOList.add(subject);
		}
		return subjectVOList;
	}

	public List<ConsentStatus> getRecordableConsentStatus() {
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		criteria.add(Restrictions.not(Restrictions.ilike("name", "Not Consented", MatchMode.ANYWHERE)));
		return criteria.list();
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
		personCriteria.add(Restrictions.eq("id", personId));
		List<Person> listOfPerson = personCriteria.list();
		if (listOfPerson != null && listOfPerson.size() > 0) {
			return listOfPerson.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
		}
	}

	public ArkFunctionType getArkFunctionType(String reportType) {
		Criteria criteria = getSession().createCriteria(ArkFunctionType.class);
		criteria.add(Restrictions.eq("name", reportType));
		criteria.setMaxResults(1);
		return (ArkFunctionType) criteria.uniqueResult();
	}

	public List<ArkFunction> getModuleFunction(ArkModule arkModule) {

		ArkFunctionType arkFunctionType = getArkFunctionType(Constants.ARK_FUNCTION_TYPE_NON_REPORT);

		Criteria criteria = getSession().createCriteria(ArkModuleFunction.class);
		criteria.createAlias("arkFunction", "aliasArkFunction");
		criteria.add(Restrictions.eq("arkModule", arkModule));
		// Pass in an instance that represents arkFunctionType non-report
		criteria.add(Restrictions.eq("aliasArkFunction.arkFunctionType", arkFunctionType));
		criteria.addOrder(Order.asc("functionSequence"));
		List<ArkModuleFunction> listOfArkModuleFunction = criteria.list();
		List<ArkFunction> arkFunctionList = new ArrayList<ArkFunction>();
		for (ArkModuleFunction arkModuleFunction : listOfArkModuleFunction) {
			arkFunctionList.add(arkModuleFunction.getArkFunction());
		}

		return arkFunctionList;
	}

	public List<PhoneStatus> getPhoneStatus() {
		Criteria criteria = getSession().createCriteria(PhoneStatus.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	public Boolean studyHasSubjects(Study study) {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		session.close();
		return totalCount.intValue() > 0;
	}

	public List<Study> getStudiesForUser(ArkUser arkUser, Study study) {

		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkStudy", "arkStudy");

		criteria.add(Restrictions.eq("arkUser", arkUser));// Represents the user
		// either who is
		// logged in or one
		// that is provided
		if (study.getId() != null) {
			criteria.add(Restrictions.eq("arkStudy.id", study.getId()));
		}

		if (study.getName() != null) {
			criteria.add(Restrictions.ilike("arkStudy.name", study.getName(), MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.distinct(Projections.property("study")));
		List<Study> studies = (List<Study>) criteria.list();
		return studies;

	}

	public long getCountOfStudies() {
		int total = 0;
		Long longTotal = ((Long) getSession().createQuery("select count(*) from Study").iterate().next());
		total = longTotal.intValue();
		return total;
	}

	public FileFormat getFileFormatByName(String name) {
		FileFormat fileFormat = null;
		Criteria criteria = getSession().createCriteria(FileFormat.class);
		criteria.add(Restrictions.eq("name", name));

		List<FileFormat> results = criteria.list();
		if (results.size() > 0) {
			fileFormat = (FileFormat) results.get(0);
		}
		return fileFormat;
	}

	public Collection<FileFormat> getFileFormats() {
		Criteria criteria = getSession().createCriteria(FileFormat.class);
		java.util.Collection<FileFormat> fileFormatCollection = criteria.list();
		return fileFormatCollection;
	}

	public DelimiterType getDelimiterType(Long id) {
		DelimiterType delimiterType = (DelimiterType) getSession().get(DelimiterType.class, id);
		return delimiterType;
	}

	public Collection<DelimiterType> getDelimiterTypes() {
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		java.util.Collection<DelimiterType> delimiterTypeCollection = criteria.list();
		return delimiterTypeCollection;
	}

	public Collection<UploadType> getUploadTypes() {
		Criteria criteria = getSession().createCriteria(UploadType.class);
		java.util.Collection<UploadType> delimiterTypeCollection = criteria.list();
		return delimiterTypeCollection;
	}

	public UploadType getDefaultUploadType() {
		return (UploadType) (getSession().get(UploadType.class, 1L));// TODO: maybe// fix// ALL// such// entities// by// adding// isDefault// boolean// to// table?
	}

	public UploadType getDefaultUploadTypeForLims() {
		return (UploadType) (getSession().get(UploadType.class, 4L));// TODO: maybe// fix// ALL// such// entities// by// adding// isDefault// boolean// to// table?
	}

	public UploadType getCustomFieldDataUploadType() {
		return (UploadType) (getSession().get(UploadType.class, 3L));// TODO: maybe// fix// ALL// such// entities// by// adding// isDefault// boolean// to// table?
	}

	public List<Upload> searchUploads(Upload uploadCriteria) {
		Criteria criteria = getSession().createCriteria(Upload.class);
		// Must be constrained on the arkFunction
		criteria.add(Restrictions.eq("arkFunction", uploadCriteria.getArkFunction()));

		if (uploadCriteria.getId() != null) {
			criteria.add(Restrictions.eq("id", uploadCriteria.getId()));
		}

		if (uploadCriteria.getStudy() != null) {
			criteria.add(Restrictions.eq("study", uploadCriteria.getStudy()));
		}

		if (uploadCriteria.getFileFormat() != null) {
			criteria.add(Restrictions.ilike("fileFormat", uploadCriteria.getFileFormat()));
		}

		if (uploadCriteria.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike("delimiterType", uploadCriteria.getDelimiterType()));
		}

		if (uploadCriteria.getFilename() != null) {
			criteria.add(Restrictions.ilike("filename", uploadCriteria.getFilename()));
		}

		criteria.addOrder(Order.desc("id"));
		List<Upload> resultsList = criteria.list();

		return resultsList;
	}

	public ArkFunction getArkFunctionByName(String functionName) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("name", functionName));
		criteria.setMaxResults(1);
		ArkFunction arkFunction = (ArkFunction) criteria.uniqueResult();
		return arkFunction;
	}

	public List<Upload> searchUploadsForBio(Upload uploadCriteria) {
		Criteria criteria = getSession().createCriteria(Upload.class);
		// - due to nature of table design...we need to specify it like this
		// ideally we might want to just have arkmodule in the upload table?
		// criteria.add(Restrictions.eq("arkFunction",
		// uploadCriteria.getArkFunction()));

		ArkFunction biospecArkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
		ArkFunction biocollArkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);

		List<ArkFunction> arkFunctionsForBio = new ArrayList<ArkFunction>();
		arkFunctionsForBio.add(biospecArkFunction);
		arkFunctionsForBio.add(biocollArkFunction);

		criteria.add(Restrictions.in("arkFunction", arkFunctionsForBio));

		if (uploadCriteria.getId() != null) {
			criteria.add(Restrictions.eq("id", uploadCriteria.getId()));
		}

		if (uploadCriteria.getStudy() != null) {
			criteria.add(Restrictions.eq("study", uploadCriteria.getStudy()));
		}

		if (uploadCriteria.getFileFormat() != null) {
			criteria.add(Restrictions.ilike("fileFormat", uploadCriteria.getFileFormat()));
		}

		if (uploadCriteria.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike("delimiterType", uploadCriteria.getDelimiterType()));
		}

		if (uploadCriteria.getFilename() != null) {
			criteria.add(Restrictions.ilike("filename", uploadCriteria.getFilename()));
		}

		criteria.addOrder(Order.desc("id"));
		List<Upload> resultsList = criteria.list();

		return resultsList;
	}

	public List<Upload> searchUploadsForBiospecimen(Upload uploadCriteria, List studyListForUser) {
		Criteria criteria = getSession().createCriteria(Upload.class);
		// - due to nature of table design...we need to specify it like this
		// ideally we might want to just have arkmodule in the upload table?
		// criteria.add(Restrictions.eq("arkFunction",
		// uploadCriteria.getArkFunction()));

		ArkFunction biospecArkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);

		List<ArkFunction> arkFunctionsForBio = new ArrayList<ArkFunction>();
		arkFunctionsForBio.add(biospecArkFunction);

		criteria.add(Restrictions.eq("arkFunction", uploadCriteria.getArkFunction()));

		if (uploadCriteria.getId() != null) {
			criteria.add(Restrictions.eq("id", uploadCriteria.getId()));
		}

		if (!studyListForUser.isEmpty()) {
			criteria.add(Restrictions.in("study", studyListForUser));
		}

		if (uploadCriteria.getFileFormat() != null) {
			criteria.add(Restrictions.ilike("fileFormat", uploadCriteria.getFileFormat()));
		}

		if (uploadCriteria.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike("delimiterType", uploadCriteria.getDelimiterType()));
		}

		if (uploadCriteria.getFilename() != null) {
			criteria.add(Restrictions.ilike("filename", uploadCriteria.getFilename()));
		}

		criteria.addOrder(Order.desc("id"));
		List<Upload> resultsList = criteria.list();

		return resultsList;
	}

	public void createUpload(Upload studyUpload) {
		if (studyUpload.getUploadStatus() == null) {
			studyUpload.setUploadStatus(getUploadStatusForUndefined());
		}
		Subject currentUser = SecurityUtils.getSubject();
		String userId = (String) currentUser.getPrincipal();
		studyUpload.setUserId(userId);
		getSession().save(studyUpload);
		// getSession().flush();
	}

	public void updateUpload(Upload studyUpload) {
		getSession().update(studyUpload);
	}

	public String getDelimiterTypeNameByDelimiterChar(char delimiterCharacter) {
		String delimiterTypeName = null;
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		criteria.add(Restrictions.eq("delimiterCharacter", delimiterCharacter));
		criteria.setProjection(Projections.property("name"));
		delimiterTypeName = (String) criteria.uniqueResult();
		return delimiterTypeName;
	}
	
	public DelimiterType getDelimiterTypeByDelimiterChar(char delimiterCharacter) {
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		criteria.add(Restrictions.eq("delimiterCharacter", delimiterCharacter));
		return (DelimiterType) criteria.uniqueResult();
	}

	public void createCustomFieldUpload(CustomFieldUpload cfUpload) {
		getSession().save(cfUpload);
	}

	public List<BiospecimenUidToken> getBiospecimenUidTokens() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidToken.class);
		return criteria.list();
	}

	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidPadChar.class);
		return criteria.list();
	}

	public List<Study> getStudyListAssignedToBiospecimenUidTemplate() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("study")));
		return criteria.list();
	}

	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		getSession().save(biospecimenUidTemplate);
	}

	public List<BioCollectionUidToken> getBioCollectionUidToken() {
		Example token = Example.create(new BioCollectionUidToken());
		Criteria criteria = getSession().createCriteria(BioCollectionUidToken.class).add(token);
		return criteria.list();
	}

	public List<BioCollectionUidPadChar> getBioCollectionUidPadChar() {
		Criteria criteria = getSession().createCriteria(BioCollectionUidPadChar.class);
		return criteria.list();
	}

	public void createBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate) {
		getSession().save(bioCollectionUidTemplate);
	}

	public Boolean studyHasBiospecimen(Study study) {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		session.close();
		return totalCount.intValue() > 0;
	}

	public Boolean studyHasBioCollection(Study study) {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(BioCollection.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		session.close();
		return totalCount.intValue() > 0;
	}

	public BiospecimenUidTemplate getBiospecimentUidTemplate(Study study) {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		criteria.add(Restrictions.eq("study", study));
		return (BiospecimenUidTemplate) criteria.uniqueResult();
	}

	public BioCollectionUidTemplate getBioCollectionUidTemplate(Study study) {
		Criteria criteria = getSession().createCriteria(BioCollectionUidTemplate.class);
		criteria.add(Restrictions.eq("study", study));
		return (BioCollectionUidTemplate) criteria.uniqueResult();
	}

	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		getSession().saveOrUpdate(biospecimenUidTemplate);
	}

	public void updateBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate) {
		getSession().saveOrUpdate(bioCollectionUidTemplate);
	}

	public long getCountOfSubjects(Study study) {
		int total = 0;
		total = ((Long) getSession().createQuery("select count(*) from LinkSubjectStudy where study = :study").setParameter("study", study).iterate().next()).intValue();
		return total;
	}

	public List<SubjectVO> matchSubjectsFromInputFile(FileUpload subjectFileUpload, Study study) {
		List<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();
		List<String> subjectUidList = new ArrayList<String>(0);

		try {
			subjectUidList = CsvListReader.readColumnIntoList(subjectFileUpload.getInputStream());
		}
		catch (IOException e) {
			log.error("Error in Subject list file");
			return subjectVOList;
		}

		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.in("subjectUID", subjectUidList));
		List<LinkSubjectStudy> subjectList = criteria.list();

		for (Iterator<LinkSubjectStudy> iterator = subjectList.iterator(); iterator.hasNext();) {
			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			// Place the LinkSubjectStudy instance into a SubjectVO and add the
			// SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setSubjectUID(linkSubjectStudy.getSubjectUID());
			subject.setLinkSubjectStudy(linkSubjectStudy);
			Person person = subject.getLinkSubjectStudy().getPerson();
			subject.setSubjectPreviousLastname(getPreviousLastname(person));
			subjectVOList.add(subject);
		}
		return subjectVOList;
	}

	public List<Study> getAssignedChildStudyListForPerson(Study study, Person person) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("study", "s");
		criteria.add(Restrictions.eq("person", person));
		criteria.add(Restrictions.eq("s.parentStudy", study));
		criteria.add(Restrictions.ne("s.id", study.getId()));
		criteria.add(Restrictions.ne("subjectStatus", getSubjectStatus("Archive")));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("study"), "study");
		criteria.setProjection(projectionList);

		return criteria.list();
	}

	public List<ConsentOption> getConsentOptionList() {
		Criteria criteria = getSession().createCriteria(ConsentOption.class);
		return criteria.list();
	}

	public boolean customFieldHasData(CustomField customField) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT count(*) FROM ");
		sb.append(" CustomField cf, ");
		sb.append(" CustomFieldDisplay cfd, ");
		sb.append(" PhenoData pd ");
		sb.append("WHERE cf.study.id = :studyId ");
		sb.append(" AND cf.arkFunction.id = :arkFunctionId ");
		sb.append(" AND cf.id = cfd.customField.id ");
		sb.append(" AND pd.customFieldDisplay.id = cfd.id");

		Query query = getSession().createQuery(sb.toString());
		query.setParameter("studyId", customField.getStudy().getId());
		query.setParameter("arkFunctionId", customField.getArkFunction().getId());
		return ((Number) query.iterate().next()).intValue() > 0;
	}

	public long countNumberOfSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection<String> subjectUids) {
		String queryString = "select count(*) " + "from LinkSubjectStudy subject " + "where study =:study " + "and subjectUID in  (:subjects) ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return (Long) query.uniqueResult();
	}

	public List<String> getSubjectUIDsThatAlreadyExistWithTheseUIDs(Study study, Collection<String> subjectUids) {
		String queryString = "select subject.subjectUID " + "from LinkSubjectStudy subject " + "where study =:study " + "and subjectUID in  (:subjects) ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return query.list();
	}

	/**
	 * based on sql concept of;4 select id from custom_field_display where custom_field_id in (SELECT id FROM custom_field where name='AGE' and
	 * study_id = 1 and ark_function_id = 5)
	 * 
	 * @param fieldNameCollection
	 * @param study
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List<String> fieldNameCollection, Study study, ArkFunction arkFunction) {
		/*
		 * log.warn("fieldnamecollection size=" + fieldNameCollection.size() +4 "\nstudy=" + study.getName() + " with id=" + study.getId() +
		 * "\narkFunctionid=" + arkFunction.getId());
		 */

		if (fieldNameCollection == null || fieldNameCollection.isEmpty()) {
			return new ArrayList<CustomFieldDisplay>();
		}
		else {
			List<String> lowerCaseNames = new ArrayList<String>();
			for (String name : fieldNameCollection) {
				lowerCaseNames.add(name.toLowerCase());
			}
			String queryString = "select cfd " + "from CustomFieldDisplay cfd " + "where customField.id in ( " + " SELECT id from CustomField cf " + " where cf.study =:study "
					+ " and lower(cf.name) in (:names) " + " and cf.arkFunction =:arkFunction )";
			Query query = getSession().createQuery(queryString);
			query.setParameter("study", study);
			// query.setParameterList("names", fieldNameCollection);
			query.setParameterList("names", lowerCaseNames);
			query.setParameter("arkFunction", arkFunction);
			return query.list();
		}
	}

	@SuppressWarnings("unchecked")
	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(Study study, ArkFunction arkFunction) {

		String queryString = "select cfd " + " from CustomFieldDisplay cfd " + " where customField.id in ( " + " SELECT id from CustomField cf " + " where cf.study =:study "
				+ " and cf.arkFunction =:arkFunction )";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameter("arkFunction", arkFunction);
		return query.list();

	}

	/**
	 * based on sql concept of; select id from custom_field_display where custom_field_id in (SELECT id FROM custom_field where name='AGE' and study_id
	 * = 1 and ark_function_id = 5)
	 * 
	 * @param fieldNameCollection
	 * @param study
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List<String> fieldNameCollection, Study study, ArkFunction arkFunction, CustomFieldGroup customFieldGroup) {

		if (fieldNameCollection == null || fieldNameCollection.isEmpty()) {
			return new ArrayList<CustomFieldDisplay>();
		}
		else {
			List<String> lowerCaseNames = new ArrayList<String>();
			for (String name : fieldNameCollection) {
				lowerCaseNames.add(name.toLowerCase());
			}
			String queryString = "select cfd from CustomFieldDisplay cfd " + 
				" where cfd.customFieldGroup =:customFieldGroup and customField.id in ( " + 
					" SELECT id from CustomField cf " + 
					" where cf.study =:study " + " and lower(cf.name) in (:names) " + " and cf.arkFunction =:arkFunction )";
			Query query = getSession().createQuery(queryString); 
			query.setParameter("study", study);
			// query.setParameterList("names", fieldNameCollection);
			query.setParameterList("names", lowerCaseNames);
			query.setParameter("arkFunction", arkFunction);
			query.setParameter("customFieldGroup", customFieldGroup);
			return query.list();
		}
	}

	@SuppressWarnings("unchecked")
	public List<LinkSubjectStudy> getSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection subjectUids) {
		String queryString = "select subject " + "from LinkSubjectStudy subject " + "where study =:study " + "and subjectUID in  (:subjects) ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllSubjectUIDs(Study study) {
		String queryString = "select subject.subjectUID " + "from LinkSubjectStudy subject " + "where study =:study " + "order by subjectUID ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);	

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<SubjectCustomFieldData> getCustomFieldDataFor(Collection customFieldDisplaysThatWeNeed, List subjectUIDsToBeIncluded) {
		if (customFieldDisplaysThatWeNeed == null || customFieldDisplaysThatWeNeed.isEmpty() || subjectUIDsToBeIncluded == null || subjectUIDsToBeIncluded.isEmpty()) {
			return new ArrayList<SubjectCustomFieldData>();
		}
		else {
			String queryString = "select scfd " + " from SubjectCustomFieldData scfd " + " where scfd.linkSubjectStudy in (:subjectUIDsToBeIncluded) "
					+ " and scfd.customFieldDisplay in (:customFieldDisplaysThatWeNeed) ";
			Query query = getSession().createQuery(queryString);
			query.setParameterList("subjectUIDsToBeIncluded", subjectUIDsToBeIncluded);
			query.setParameterList("customFieldDisplaysThatWeNeed", customFieldDisplaysThatWeNeed);
			return query.list();
		}
	}

	public Payload createPayload(byte[] bytes) {
		Payload payload = new Payload(bytes);
		getSession().save(payload);
		getSession().flush();
		getSession().refresh(payload);
		return payload;
	}

	public Payload getPayloadForUpload(Upload upload) {
		getSession().refresh(upload);// bit paranoid but the code calling this
		// may be from wicket and not be attached?
		return upload.getPayload();
	}

	public UploadStatus getUploadStatusForUploaded() {
		Criteria criteria = getSession().createCriteria(UploadStatus.class);
		criteria.add(Restrictions.eq("name", "COMPLETED"));
		return (UploadStatus) criteria.uniqueResult();
	}

	// TODO Constants?
	public UploadStatus getUploadStatusForUndefined() {
		Criteria criteria = getSession().createCriteria(UploadStatus.class);
		criteria.add(Restrictions.eq("name", "STATUS_NOT_DEFINED"));
		return (UploadStatus) criteria.uniqueResult();
	}

	public UploadStatus getUploadStatusForAwaitingValidation() {
		Criteria criteria = getSession().createCriteria(UploadStatus.class);
		criteria.add(Restrictions.eq("name", "AWAITING_VALIDATION"));
		return (UploadStatus) criteria.uniqueResult();
	}

	public UploadStatus getUploadStatusFor(String statusFromConstant) {
		Criteria criteria = getSession().createCriteria(UploadStatus.class);
		criteria.add(Restrictions.eq("name", statusFromConstant));
		return (UploadStatus) criteria.uniqueResult();
	}

	public UploadStatus getUploadStatusForValidated() {
		Criteria criteria = getSession().createCriteria(UploadStatus.class);
		criteria.add(Restrictions.eq("name", "VALIDATED"));
		return (UploadStatus) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public Collection<UploadType> getUploadTypesForSubject() {
		Criteria criteria = getSession().createCriteria(UploadType.class);
		criteria.add(Restrictions.eq("arkModule", getArkModuleForSubject()));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<UploadType> getUploadTypesForLims() {
		Criteria criteria = getSession().createCriteria(UploadType.class);
		criteria.add(Restrictions.eq("arkModule", getArkModuleForLims()));
		return criteria.list();
	}

	public ArkModule getArkModuleForSubject() {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("name", "Subject"));
		return (ArkModule) criteria.uniqueResult();
	}

	public ArkModule getArkModuleForLims() {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("name", "LIMS"));
		return (ArkModule) criteria.uniqueResult();
	}

	public YesNo getYes() {
		Criteria criteria = getSession().createCriteria(YesNo.class);
		criteria.add(Restrictions.eq("name", "Yes"));
		return (YesNo) criteria.uniqueResult();
	}

	public YesNo getNo() {
		Criteria criteria = getSession().createCriteria(YesNo.class);
		criteria.add(Restrictions.eq("name", "No"));
		return (YesNo) criteria.uniqueResult();
	}

	// TODO maybe a query/report/etc dao?
	// TODO ASAP
	public List<Search> getSearchesForThisStudy(Study study) {
		Criteria criteria = getSession().createCriteria(Search.class);
		criteria.add(Restrictions.eq("study", study));
		List<Search> searchList = criteria.list();
		for (Search search : searchList) {
			log.info(search.getName() + search.getId());
		}
		return searchList;
	}

	public boolean create(Search search) throws EntityExistsException {
		boolean success = true;
		if (isSearchNameTaken(search.getName(), search.getStudy(), null)) {
			throw new EntityExistsException("Search name '" + search.getName() + "' is already taken.  Please select a unique name");
		}
		getSession().save(search);
		return success;
	}

	public boolean update(Search search) throws EntityExistsException {
		boolean success = true;
		if (isSearchNameTaken(search.getName(), search.getStudy(), search.getId())) {
			throw new EntityExistsException("Search name '" + search.getName() + "' is already taken.  Please select a unique name");
		}
		getSession().update(search);
		return success;
	}

	public boolean create(SearchVO searchVO) throws EntityExistsException {
		boolean success = true;
		if (isSearchNameTaken(searchVO.getSearch().getName(), searchVO.getSearch().getStudy(), null)) {
			throw new EntityExistsException("Search name '" + searchVO.getSearch().getName() + "' is already taken.  Please select a unique name");
		}
		getSession().save(searchVO.getSearch());
		getSession().refresh(searchVO.getSearch());

		for (DemographicField field : searchVO.getSelectedDemographicFields()) {
			DemographicFieldSearch dfs = new DemographicFieldSearch(field, searchVO.getSearch());
			getSession().save(dfs);
		}

		return success;
	}

	public boolean update(SearchVO searchVO) throws EntityExistsException {
		//start save basic search info
		boolean success = true;
		Search search = searchVO.getSearch();
		log.info("search name" + search.getName());
		if (isSearchNameTaken(search.getName(), search.getStudy(), search.getId())) {
			throw new EntityExistsException("Search name '" + search.getName() + "' is already taken.  Please select a unique name");
		}
		getSession().update(search);
		getSession().flush();
		getSession().refresh(search);
//end save basic search info
	
		

		
//start save demographic fields
		Collection<DemographicField> listOfDemographicFieldsFromVO = searchVO.getSelectedDemographicFields();
		List<DemographicFieldSearch> nonPoppableDFS = new ArrayList<DemographicFieldSearch>();
		nonPoppableDFS.addAll(search.getDemographicFieldsToReturn());
		List<DemographicField> nonPoppableDemographicFieldsFromVO = new ArrayList<DemographicField>();
		nonPoppableDemographicFieldsFromVO.addAll(listOfDemographicFieldsFromVO);
		for (DemographicFieldSearch dfs : nonPoppableDFS) {
			log.info("fields to return=" + search.getDemographicFieldsToReturn().size());
			boolean toBeDeleted = true; // if we find no match along the way,
			// conclude that it has been deleted.

			for (DemographicField field : nonPoppableDemographicFieldsFromVO) {
				if (dfs.getDemographicField().getId().equals(field.getId())) {
					toBeDeleted = false;
					log.info("listOfDemographicFieldsFromVO.size()" + listOfDemographicFieldsFromVO.size());
					listOfDemographicFieldsFromVO.remove(field);
					// we found it, therefore  remove it  from the list that will ultimately be added as DFS's
					log.info("after removal listOfDemographicFieldsFromVO.size()" + listOfDemographicFieldsFromVO.size());
				}
			}
			if (toBeDeleted) {
				log.info("before delete");
				search.getDemographicFieldsToReturn().remove(dfs);
				getSession().update(search);
				getSession().delete(dfs);
				// setDemographicFieldsToReturn(getDemographicFieldsToReturn());
				getSession().flush();
			 	getSession().refresh(search);
				log.info("after delete" + search.getDemographicFieldsToReturn().size());
			}
		}

		for (DemographicField field : listOfDemographicFieldsFromVO) {
			DemographicFieldSearch dfs = new DemographicFieldSearch(field, search);
			getSession().save(dfs);
		} 
		searchVO.setSelectedDemographicFields(nonPoppableDemographicFieldsFromVO);
//end save demographic fields




//start save biospecimen fields
		Collection<BiospecimenField> listOfBiospecimenFieldsFromVO = searchVO.getSelectedBiospecimenFields();
		List<BiospecimenFieldSearch> nonPoppableBiospecimenFS = new ArrayList<BiospecimenFieldSearch>();
		nonPoppableBiospecimenFS.addAll(search.getBiospecimenFieldsToReturn());
		List<BiospecimenField> nonPoppableBiospecimenFieldsFromVO = new ArrayList<BiospecimenField>();
		nonPoppableBiospecimenFieldsFromVO.addAll(listOfBiospecimenFieldsFromVO);
		for (BiospecimenFieldSearch bfs : nonPoppableBiospecimenFS) {
			log.info("fields to return=" + search.getBiospecimenFieldsToReturn().size());
			boolean toBeDeleted = true; // if we find no match along the way,
			// conclude that it has been deleted.

			for (BiospecimenField field : nonPoppableBiospecimenFieldsFromVO) {
				if (bfs.getBiospecimenField().getId().equals(field.getId())) {
					toBeDeleted = false;
					log.info("listOfBiospecimenFieldsFromVO.size()" + listOfBiospecimenFieldsFromVO.size());
					listOfBiospecimenFieldsFromVO.remove(field);
					// we found it, therefore  remove it  from the list that will ultimately be added as DFS's
					log.info("after removal listOfBiospecimenFieldsFromVO.size()" + listOfBiospecimenFieldsFromVO.size());
				}
			}
			if (toBeDeleted) {
				log.info("before delete");
				search.getBiospecimenFieldsToReturn().remove(bfs);
				getSession().update(search);
				getSession().delete(bfs);
				// setBiospecimenFieldsToReturn(getBiospecimenFieldsToReturn());
				getSession().flush();
				getSession().refresh(search);
				log.info("after delete" + search.getBiospecimenFieldsToReturn().size());
			}
		}

		for (BiospecimenField field : listOfBiospecimenFieldsFromVO) {
			BiospecimenFieldSearch bfs = new BiospecimenFieldSearch(field, search);
			getSession().save(bfs);
		}
		searchVO.setSelectedBiospecimenFields(nonPoppableBiospecimenFieldsFromVO);
//end save biospecimen fields
		

//start save biocollection fields
		Collection<BiocollectionField> listOfBiocollectionFieldsFromVO = searchVO.getSelectedBiocollectionFields();
		List<BiocollectionFieldSearch> nonPoppableBiocollectionFS = new ArrayList<BiocollectionFieldSearch>();
		nonPoppableBiocollectionFS.addAll(search.getBiocollectionFieldsToReturn());
		List<BiocollectionField> nonPoppableBiocollectionFieldsFromVO = new ArrayList<BiocollectionField>();
		nonPoppableBiocollectionFieldsFromVO.addAll(listOfBiocollectionFieldsFromVO);
		for (BiocollectionFieldSearch bfs : nonPoppableBiocollectionFS) {
			log.info("fields to return=" + search.getBiocollectionFieldsToReturn().size());
			boolean toBeDeleted = true; // if we find no match along the way,
			// conclude that it has been deleted.

			for (BiocollectionField field : nonPoppableBiocollectionFieldsFromVO) {
				if (bfs.getBiocollectionField().getId().equals(field.getId())) {
					toBeDeleted = false;
					log.info("listOfBiocollectionFieldsFromVO.size()" + listOfBiocollectionFieldsFromVO.size());
					listOfBiocollectionFieldsFromVO.remove(field);
					// we found it, therefore  remove it  from the list that will ultimately be added as DFS's
					log.info("after removal listOfBiocollectionFieldsFromVO.size()" + listOfBiocollectionFieldsFromVO.size());
				}
			}
			if (toBeDeleted) {
				log.info("before delete");
				search.getBiocollectionFieldsToReturn().remove(bfs);
				getSession().update(search);
				getSession().delete(bfs);
				// setBiocollectionFieldsToReturn(getBiocollectionFieldsToReturn());
				getSession().flush();
				getSession().refresh(search);
				log.info("after delete" + search.getBiocollectionFieldsToReturn().size());
			}
		}

		for (BiocollectionField field : listOfBiocollectionFieldsFromVO) {
			BiocollectionFieldSearch bfs = new BiocollectionFieldSearch(field, search);
			getSession().save(bfs);
		}
		searchVO.setSelectedBiocollectionFields(nonPoppableBiocollectionFieldsFromVO);
//end save biocollection fields
						
						
						
//start saving all custom display fields		
		Collection<CustomFieldDisplay> listOfPhenoCustomFieldDisplaysFromVO = searchVO.getSelectedPhenoCustomFieldDisplays();
		Collection<CustomFieldDisplay> listOfSubjectCustomFieldDisplaysFromVO = searchVO.getSelectedSubjectCustomFieldDisplays();
		Collection<CustomFieldDisplay> listOfBiospecimenCustomFieldDisplaysFromVO = searchVO.getSelectedBiospecimenCustomFieldDisplays();
		Collection<CustomFieldDisplay> listOfBiocollectionCustomFieldDisplaysFromVO = searchVO.getSelectedBiocollectionCustomFieldDisplays();// we
																																															// really
																																															// can
		// add them all here and add to one collections
		List<CustomFieldDisplaySearch> nonPoppablePhenoCFDs = new ArrayList<CustomFieldDisplaySearch>();
		// List<CustomFieldDisplaySearch> nonPoppableSubjectCFDs = new ArrayList<CustomFieldDisplaySearch>();
		nonPoppablePhenoCFDs.addAll(search.getCustomFieldsToReturn());
		List<CustomFieldDisplay> nonPoppableCustomFieldsFromVO = new ArrayList<CustomFieldDisplay>();
		nonPoppableCustomFieldsFromVO.addAll(listOfPhenoCustomFieldDisplaysFromVO);
		nonPoppableCustomFieldsFromVO.addAll(listOfSubjectCustomFieldDisplaysFromVO);
		nonPoppableCustomFieldsFromVO.addAll(listOfBiospecimenCustomFieldDisplaysFromVO);
		nonPoppableCustomFieldsFromVO.addAll(listOfBiocollectionCustomFieldDisplaysFromVO);

		List<CustomFieldDisplay> poppableCustomFieldsFromVO = new ArrayList<CustomFieldDisplay>();
		poppableCustomFieldsFromVO.addAll(listOfPhenoCustomFieldDisplaysFromVO);
		poppableCustomFieldsFromVO.addAll(listOfSubjectCustomFieldDisplaysFromVO);
		poppableCustomFieldsFromVO.addAll(listOfBiospecimenCustomFieldDisplaysFromVO);
		poppableCustomFieldsFromVO.addAll(listOfBiocollectionCustomFieldDisplaysFromVO);

		for (CustomFieldDisplaySearch cfds : nonPoppablePhenoCFDs) {
			log.info("fields to return=" + search.getCustomFieldsToReturn().size());
			boolean toBeDeleted = true; // if we find no match along the way,
			// conclude that it has been deleted.

			for (CustomFieldDisplay field : nonPoppableCustomFieldsFromVO) {
				if (cfds.getCustomFieldDisplay().getId().equals(field.getId())) {
					toBeDeleted = false;
					/* log.info("listOfCustomFieldDisplaysFromVO.size()" + listOfPhenoCustomFieldDisplaysFromVO.size());
					 * listOfPhenoCustomFieldDisplaysFromVO.remove(field);//we found it, therefore remove it from the list that will ultimately be added as
					 * DFS's log.info( "after removal listOfCustomFieldDisplaysFromVO.size()" + listOfPhenoCustomFieldDisplaysFromVO.size());
					 */
					log.info("poppableCustomFieldsFromVO.size()" + poppableCustomFieldsFromVO.size());
					poppableCustomFieldsFromVO.remove(field);// we found it,
					// therefore remove it from the list that will ultimately be added as DFS's
					log.info("after removal poppableCustomFieldsFromVO.size()" + poppableCustomFieldsFromVO.size());
				}
			}
			if (toBeDeleted) {
				log.info("before delete");
				search.getCustomFieldsToReturn().remove(cfds);
				getSession().update(search);
				getSession().delete(cfds);
				// setCustomFieldDisplaysToReturn(getCustomFieldDisplaysToReturn());
				getSession().flush();
				getSession().refresh(search);
				log.info("after delete" + search.getCustomFieldsToReturn().size());
			}
		}

		for (CustomFieldDisplay field : poppableCustomFieldsFromVO) { // listOfPhenoCustomFieldDisplaysFromVO){
			CustomFieldDisplaySearch cfds = new CustomFieldDisplaySearch(field, search);
			getSession().save(cfds);
		}
		// is all of this necessary now...investigate
		// searchVO.setSelectedPhenoCustomFieldDisplays(nonPoppableCustomFieldsFromVO);
//end save all custom field displays
		
		return success;
	}

	/*
	 * private List<DemographicFieldSearch> getCurrentDemographicFieldSearches(Search search) {/* Criteria criteria =
	 * getSession().createCriteria(Search.class); criteria.add(Restrictions.eq("name", searchName));
	 * 
	 * if(anIdToExcludeFromResults != null){ criteria.add(Restrictions.ne("id", anIdToExcludeFromResults)); } return (criteria.list().size() > 0);*
	 * 
	 * String queryString = "select dfs " + " from DemographicFieldSearch dfs " + " where dfs.search=:search "; Query query =
	 * getSession().createQuery(queryString); query.setParameter("search", search); return query.list(); }
	 */
	/**
	 * 
	 * @param searchName
	 * @param anIdToExcludeFromResults
	 *           : This is if you want to exclude id, the most obvious case being where we want to exclude the current search itself in the case of an
	 *           update
	 * @return
	 */
	public boolean isSearchNameTaken(String searchName, Study study, Long anIdToExcludeFromResults) {
		Criteria criteria = getSession().createCriteria(Search.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("name", searchName));

		if (anIdToExcludeFromResults != null) {
			criteria.add(Restrictions.ne("id", anIdToExcludeFromResults));
		}
		return (criteria.list().size() > 0);
	}

	public Collection<DemographicField> getAllDemographicFields() {
		Criteria criteria = getSession().createCriteria(DemographicField.class);
		return criteria.list();
	}

	public Collection<BiospecimenField> getAllBiospecimenFields() {
		Criteria criteria = getSession().createCriteria(BiospecimenField.class);
		return criteria.list();
	}

	public Collection<BiocollectionField> getAllBiocollectionFields() {
		Criteria criteria = getSession().createCriteria(BiocollectionField.class);
		return criteria.list();
	}

	public List<DemographicField> getSelectedDemographicFieldsForSearch(Search search) {
		String queryString = "select dfs.demographicField " + " from DemographicFieldSearch dfs " + " where dfs.search=:search " + " order by dfs.demographicField.entity ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	public List<DemographicField> getSelectedDemographicFieldsForSearch(Search search, Entity entityEnumToRestrictOn) {
		String queryString = "select dfs.demographicField from DemographicFieldSearch dfs where dfs.search=:search " + " and dfs.demographicField.entity=:entityEnumToRestrictOn ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("entityEnumToRestrictOn", entityEnumToRestrictOn);
		return query.list();
	}

	public List<BiospecimenField> getSelectedBiospecimenFieldsForSearch(Search search) {
		String queryString = "select bsfs.biospecimenField from BiospecimenFieldSearch bsfs " + 
						" where bsfs.search=:search ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	public List<BiocollectionField> getSelectedBiocollectionFieldsForSearch(Search search) {
		String queryString = "select bcfs.biocollectionField " + " from BiocollectionFieldSearch bcfs " + " where bcfs.search=:search ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	/*
	 * @param search
	 * @param explicitReadOnly    - if true, will try to set to readonly ELSE false
	 * @return
	 *         public Collection<DemographicField> getSelectedDemographicFieldsForSearch(Search search, boolean explicitReadOnly) {
	 *         String queryString = "select dfs.demographicField " + " from DemographicFieldSearch dfs " + " where dfs.search=:search "; Query query =
	 *         getSession().createQuery(queryString); query.setParameter("search", search); query.setReadOnly(explicitReadOnly);
	 *         return query.list(); }
	 */
	/*
	 * @param search
	 * @param explicitReadOnly- if true, will try to set to readonly ELSE false
	 * @return
	 *         public Collection<BiospecimenField> getSelectedBiospecimenFieldsForSearch(Search search, boolean explicitReadOnly) {
	 *         String queryString = "select dfs.BiospecimenField " + " from BiospecimenFieldSearch dfs " + " where dfs.search=:search "; Query query =
	 *         getSession().createQuery(queryString); query.setParameter("search", search); query.setReadOnly(explicitReadOnly);
	 *         return query.list(); }
	 */
	/*
	 * @param search
	 * @param explicitReadOnly - if true, will try to set to readonly ELSE false
	 * @return
	 *         public Collection<BiocollectionField> getSelectedBiocollectionFieldsForSearch(Search search, boolean explicitReadOnly) {
	 *         String queryString = "select dfs.BiocollectionField " + " from BiocollectionFieldSearch dfs " + " where dfs.search=:search "; Query
	 *         query = getSession().createQuery(queryString); query.setParameter("search", search); query.setReadOnly(explicitReadOnly);
	 *         return query.list(); }
	 */


	public Collection<CustomFieldDisplay> getSelectedPhenoCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction ";// +
		// " order by cfds.customFieldDisplay.customFieldGroup.name ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));

		return query.list();
	}

	public Collection<CustomFieldDisplay> getSelectedSubjectCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD));
		return query.list();
	}

	public Collection<CustomFieldDisplay> getSelectedBiospecimenCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN));
		return query.list();
	}

	public Collection<CustomFieldDisplay> getAllSelectedCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	public Collection<CustomFieldDisplay> getSelectedBiocollectionCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION));
		return query.list();
	}

	public void runSearch(Long searchId) {
		DataExtractionVO allTheData = new DataExtractionVO();
		Search search = (Search) getSession().get(Search.class, searchId);
		if (search == null) {
			// TODO errors and reports
		}
		else {
			/* do i need fields or just run a mass query? */
			Collection<DemographicField> dfs = getSelectedDemographicFieldsForSearch(search);
			Collection<DemographicField> addressDFs = getSelectedDemographicFieldsForSearch(search, Entity.Address);
			Collection<DemographicField> lssDFs = getSelectedDemographicFieldsForSearch(search, Entity.LinkSubjectStudy);
			Collection<DemographicField> personDFs = getSelectedDemographicFieldsForSearch(search, Entity.Person);
			Collection<DemographicField> phoneDFs = getSelectedDemographicFieldsForSearch(search, Entity.Phone);
			Collection<BiospecimenField> bsfs = getSelectedBiospecimenFieldsForSearch(search);
			Collection<BiocollectionField> bcfs = getSelectedBiocollectionFieldsForSearch(search);
			 //Collection<CustomFieldDisplay> cfds = getAllSelectedCustomFieldDisplaysForSearch(search);
			Collection<CustomFieldDisplay> bccfds = getSelectedBiocollectionCustomFieldDisplaysForSearch(search);
			Collection<CustomFieldDisplay> bscfds = getSelectedBiospecimenCustomFieldDisplaysForSearch(search);
			Collection<CustomFieldDisplay> scfds = getSelectedSubjectCustomFieldDisplaysForSearch(search);
			// save PHENO for later Collection<CustomFieldDisplay> pcfds = getSelectedPhenoCustomFieldDisplaysForSearch(search);
			/* SAVE FILTERS FOR LATER 
			 * Making this stuff into an xml document THEN converting it generically to xls/csv/pdf/etc might be an option
			 */

			/***
			 * some of the options 1 get each of these and apply a filter every time 2 a megaquery to get EVERYTHING FOR EVERYONE into our
			 * "report object/model" 3 use the filters to create a set of subjectUIDs and maybe apply that, though may also needs a set of pheno_data_id, subj_custom_ids, etc
			 */

			/*get demographic data if(hasPersonFields(dfs)){ Query personQuery = getSession().createQuery("Select person from Person person ");
			 * //then get some fields and put it in our "report model" } if(hasLSSFields(dfs)){ //or a query that forces the join of person and lss if
			 * they both exist Query lssQuery = getSession().createQuery ("Select lss from LinkSubjectStudy lss "); //then get some fields and put it in
			 * our "report model" }
			 */
			// if(hasDemographicFieldsOrFilters(dfs)){ //i guess it always
			// should as there is always a representation of WHO
			// constructDemographicQuery(dfs);
			// DemographicExtractionVO
			// }

			addDataFromMegaDemographicQuery(allTheData, personDFs, lssDFs, addressDFs, phoneDFs, scfds, search);
			List<Long> uidsafterFiltering = applyDemographicFilters(search);
			/*for(Long uid : uidsFromDemographic){				log.info("got " + uid);	}			*/

			log.info("uidsafterFilteringdemo=" + uidsafterFiltering.size());
			//TODO ASAP need a differenciating between needing filters and needing to select fields independantly
			addDataFromMegaBiospecimenQuery(allTheData, bsfs, bscfds, search);
			uidsafterFiltering = applyBiospecimenFilters(allTheData, search, uidsafterFiltering);	//change will be applied to referenced object
			log.info("uidsafterFilteringbiospec=" + uidsafterFiltering.size());
			//TODO wipe the old data which doesn't still match the ID list
			addDataFromMegaBiocollectionQuery(allTheData, bcfs, bccfds, search);
			log.info("uidsafterFiltering doing the construction of megaobject=" + uidsafterFiltering.size());
			uidsafterFiltering = applyBiocollectionFilters(allTheData, search, uidsafterFiltering);	//change will be applied to referenced object
			log.info("uidsafterFiltering biocol=" + uidsafterFiltering.size());
			//TODO wipe the old data which doesn't still match the ID list
			uidsafterFiltering = applySubjectCustomFilters(allTheData, search, uidsafterFiltering);	//change will be applied to referenced object
			log.info("uidsafterFiltering SUBJECT cust=" + uidsafterFiltering.size());
			//TODO wipe the old data which doesn't still match the ID list
			uidsafterFiltering = applyBiospecimenCustomFilters(allTheData, search, uidsafterFiltering);	//change will be applied to referenced object
			log.info("uidsafterFiltering=Biospec cust" + uidsafterFiltering.size());
			//TODO wipe the old data which doesn't still match the ID list
			uidsafterFiltering = applyBiocollectionCustomFilters(allTheData, search, uidsafterFiltering);	//change will be applied to referenced object
			log.info("uidsafterFiltering biocol cust=" + uidsafterFiltering.size());
			//TODO wipe the old data which doesn't still match the ID list
			
			uidsafterFiltering = applyPhenoCustomFilters(allTheData, search, uidsafterFiltering);	//change will be applied to referenced object
			log.info("uidsafterFiltering pheno cust=" + uidsafterFiltering.size());
			//TODO wipe the old data which doesn't still match the ID list
			

			
			//now filter previous data from the further filtering steps each time.  First time not necessary just assign uids
			// CREATE CSVs
			createSearchResult(search, iDataExtractionDao.createSubjectDemographicCSV(search, allTheData.getDemographicData(), FieldCategory.DEMOGRAPHIC_FIELD));
			createSearchResult(search, iDataExtractionDao.createBiospecimenCSV(search, allTheData.getBiospecimenData(), FieldCategory.BIOSPECIMEN_FIELD));
			createSearchResult(search, iDataExtractionDao.createBiocollectionCSV(search, allTheData.getBiocollectionData(), FieldCategory.BIOCOLLECTION_FIELD));
			createSearchResult(search, iDataExtractionDao.createBiospecimenDataCustomCSV(search, allTheData.getBiospecimenCustomData(), FieldCategory.BIOSPECIMEN_CFD));
		}
	}

	private List<Long> applyDemographicFilters(Search search){
		List subjectUIDs = new ArrayList<Long>();
		//TODO ASAP  Apply external list of subjectUID as a restriction
		String personFilters = getPersonFilters(search, null);
		String lssAndPersonFilters = getLSSFilters(search, personFilters);
		
		/**
		 * note that this is ID not subject_uid being selected
		 */
		String queryString = "select distinct lss.id from LinkSubjectStudy lss " 
				//TODO also add filters for phone and address 
				+ " where lss.study.id = " + search.getStudy().getId()
				+ lssAndPersonFilters + " ";
		subjectUIDs = getSession().createQuery(queryString).list();
		log.info("size=" + subjectUIDs.size());

		return subjectUIDs;
	}

	/**
	 * @param allTheData
	 * @param search
	 * @param uidsToInclude
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applyBiospecimenFilters(DataExtractionVO allTheData, Search search, List<Long> uidsToInclude){
		//Set updatedListOfSubjectUIDs = new LinkedHashSet<Long>(); //rather than add each uid from the biospecimen.getlss.getid...just get it back as one query...otherwise hibernate will fetch each row
		String biospecimenFilters = getBiospecimenFilters(search);

		//only bother with the query and data IF biospecimen fields are needed
		if (!getSelectedBiospecimenFieldsForSearch(search).isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){
			String queryString = "select biospecimen from Biospecimen biospecimen " 
								+ " where biospecimen.study.id = " + search.getStudy().getId()
								+ biospecimenFilters
								+ " and  biospecimen.linkSubjectStudy.id in (:uidList) ";
			Query query = getSession().createQuery(queryString);
			query.setParameterList("uidList", uidsToInclude);
			//query.list(); 	
			List<Biospecimen> biospecimens = query.list(); 	
			
			//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list.
			//allTheData.setBiospecimens(biospecimens);

			log.info("sizeofbiospecs=" + biospecimens.size());
			for(Biospecimen b : biospecimens){
				log.info("biospecimen = " + b.getBiospecimenUid() + "     belongs to " + b.getLinkSubjectStudy().getSubjectUID());
			}
		}

		//only bother with restricting IF biospecimen filters exist
		if(!biospecimenFilters.isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){

			String queryString2 = "select distinct biospecimen.linkSubjectStudy.id from Biospecimen biospecimen " 
								+ " where biospecimen.study.id = " + search.getStudy().getId()
								+ biospecimenFilters  
								+ " and biospecimen.linkSubjectStudy.id in (:uidList) ";
			Query query2 = getSession().createQuery(queryString2);
			query2.setParameterList("uidList", uidsToInclude);
			List<Long> updatedListOfSubjectUIDs = query2.list(); 	
			
			//TODO ASAP in addtion to this it is now time to wipe the old data which is no longer to be included due to latest filter
			// if we don't want to garauntee order of wiping data, etc do this outside this method, in the calling method
			

			log.info("updated size of UIDs=" + updatedListOfSubjectUIDs.size());
			return updatedListOfSubjectUIDs;
		}
		else{
			return uidsToInclude;
		}
	}


	/**
	 * @param allTheDataz
	 * @param search
	 * @param uidsToInclude
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applyBiocollectionFilters(DataExtractionVO allTheData, Search search, List<Long> uidsToInclude){
		//Set updatedListOfSubjectUIDs = new LinkedHashSet<Long>(); //rather than add each uid from the biocollection.getlss.getid...just get it back as one query...otherwise hibernate will fetch each row
		String biocollectionFilters = getBiocollectionFilters(search);

		//only bother with the query and data IF biocollection fields are needed
		if (!getSelectedBiocollectionFieldsForSearch(search).isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){
			String queryString = "select biocollection from BioCollection biocollection " 
								+ " where biocollection.study.id = " + search.getStudy().getId()
								+ biocollectionFilters
								+ " and  biocollection.linkSubjectStudy.id in (:uidList) ";
			Query query = getSession().createQuery(queryString);
			query.setParameterList("uidList", uidsToInclude);
			//query.list(); 	
			List<BioCollection> biocollections = query.list(); 	
			
			//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list.
			//allTheData.setBiocollections(biocollections);

			log.info("after applying biocollection filters : sizeofbioCOLs=" + biocollections.size());
			for(BioCollection b : biocollections){
				log.info("biocollection = " + b.getBiocollectionUid() + "     belongs to " + b.getLinkSubjectStudy().getSubjectUID());
			}
		}

		//only bother with restricting IF biocollection filters exist
		if(!biocollectionFilters.isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){

			String queryString2 = "select distinct biocollection.linkSubjectStudy.id from BioCollection biocollection " 
								+ " where biocollection.study.id = " + search.getStudy().getId()
								+ biocollectionFilters  
								+ " and biocollection.linkSubjectStudy.id in (:uidList) ";
			Query query2 = getSession().createQuery(queryString2);
			query2.setParameterList("uidList", uidsToInclude);
			List<Long> updatedListOfSubjectUIDs = query2.list(); 	
			
			//TODO ASAP in addtion to this it is now time to wipe the old data which is no longer to be included due to latest filter
			// if we don't want to garauntee order of wiping data, etc do this outside this method, in the calling method
			

			log.info("updated size of UIDs=" + updatedListOfSubjectUIDs.size());
			return updatedListOfSubjectUIDs;
		}
		else{
			return uidsToInclude;
		}
	}


	/**
	 * @param allTheData - reference to the object containing our data collected so far, this is to be updated as we continue our refinement.
	 * @param search 
	 * @param idsToInclude - the constantly refined list of ID's passed from the previous extraction step
	 * 
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applySubjectCustomFilters(DataExtractionVO allTheData, Search search, List<Long> idsToInclude){
		if(idsToInclude!=null && !idsToInclude.isEmpty()){
			String queryToFilterSubjectIDs = getSubjectCustomFieldQuery(search);
			Collection<CustomFieldDisplay> cfdsToReturn = getSelectedSubjectCustomFieldDisplaysForSearch(search);
			
			log.info("about to APPLY subjectcustom filters.  UIDs size =" + idsToInclude.size() + " query string = " + queryToFilterSubjectIDs + " cfd to return size = " + cfdsToReturn.size());
			if(!queryToFilterSubjectIDs.isEmpty()){
				Query query = getSession().createQuery(queryToFilterSubjectIDs);
				query.setParameterList("idList", idsToInclude);
				idsToInclude = query.list(); 	
				log.info("rows returned = " + idsToInclude.size());
			}
			else{
				log.info("there were no subject custom data filters, therefore don't run filter query");
			}
		}
		else{
			log.info("there are no id's to filter.  therefore won't run filtering query");
		}

		Collection<CustomFieldDisplay> customFieldToGet = getSelectedSubjectCustomFieldDisplaysForSearch(search);
		
		/* We have the list of subjects, and therefore the list of subjectcustomdata - now bring back all the custom data rows IF they have any data they need */
		if(idsToInclude!=null && !idsToInclude.isEmpty() && !customFieldToGet.isEmpty()){
			String queryString = "select data from SubjectCustomFieldData data  " +
					" left join fetch data.linkSubjectStudy "  +
					" left join fetch data.customFieldDisplay custFieldDisplay "  +
					" left join fetch custFieldDisplay.customField custField "  +
					" where data.linkSubjectStudy.id in (:idList)" +
					" and data.customFieldDisplay in (:customFieldsList)" + 
					" order by data.linkSubjectStudy " ;
			Query query2 = getSession().createQuery(queryString);
			query2.setParameterList("idList", idsToInclude);
			query2.setParameterList("customFieldsList", customFieldToGet);
		
			List<SubjectCustomFieldData> scfData = query2.list();
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirSubjectCustomData = allTheData.getSubjectCustomData();

			ExtractionVO valuesForThisLss = new ExtractionVO();
			HashMap<String, String> map = null;
			LinkSubjectStudy previousLss = null;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (SubjectCustomFieldData data : scfData) {
				
				if(previousLss==null){
					map = new HashMap<String, String>();
					previousLss = data.getLinkSubjectStudy();
				}
				else if(data.getLinkSubjectStudy().getId().equals(previousLss.getId())){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisLss.setKeyValues(map);
					previousLss = data.getLinkSubjectStudy();
					hashOfSubjectsWithTheirSubjectCustomData.put(previousLss.getSubjectUID(), valuesForThisLss);	
					map = new HashMap<String, String>();//reset
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}
				}			
			}
			
			//finalize the last entered key value sets/extraction VOs
			if(map!=null && previousLss!=null){
				valuesForThisLss.setKeyValues(map);
				hashOfSubjectsWithTheirSubjectCustomData.put(previousLss.getSubjectUID(), valuesForThisLss);
			}
			
			//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list or parts of it.
			allTheData.setSubjectCustomData(hashOfSubjectsWithTheirSubjectCustomData);
		}		
		return idsToInclude;
	}	
	
	
	



	/**
	 * @param allTheData - reference to the object containing our data collected so far, this is to be updated as we continue our refinement.
	 * @param search 
	 * @param biospecimenIdsToInclude - the constantly refined list of ID's passed from the previous extraction step
	 * 
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applyBiospecimenCustomFilters(DataExtractionVO allTheData, Search search, List<Long> idsToInclude){
		List<Long> biospecimenIdsToInclude = new ArrayList<Long>();
		if(idsToInclude!=null && !idsToInclude.isEmpty()){
			String queryToFilterBiospecimenIDs = getBiospecimenDataCustomFieldIdQuery(search);
			//Collection<CustomFieldDisplay> cfdsToReturn = getSelectedBiospecimenCustomFieldDisplaysForSearch(search);
			//log.info("about to APPLY subject  filters.  UIDs size =" + idsToInclude.size() + " query string = " + queryToFilterSubjectIDs + " cfd to return size = " + cfdsToReturn.size());
			if(!queryToFilterBiospecimenIDs.isEmpty()){
				Query query = getSession().createQuery(queryToFilterBiospecimenIDs);
				query.setParameterList("idList", idsToInclude);
				biospecimenIdsToInclude = query.list(); 	
				log.info("rows returned = " + biospecimenIdsToInclude.size());
				idsToInclude = getSubjectIdsForBiospecimenIds(biospecimenIdsToInclude);	
			}
			else{
				log.info("there were no subject custom data filters, therefore don't run filter query");
			}
		}
		else{
			log.info("there are no id's to filter.  therefore won't run filtering query");
		}

		Collection<CustomFieldDisplay> customFieldToGet = getSelectedBiospecimenCustomFieldDisplaysForSearch(search);
		/* We have the list of biospecimens, and therefore the list of biospecimen custom data - now bring back all the custom data rows IF they have any data they need */
		if(biospecimenIdsToInclude!=null && !biospecimenIdsToInclude.isEmpty() && !customFieldToGet.isEmpty()){
			String queryString = "select data from BiospecimenCustomFieldData data  " +
					" left join fetch data.biospecimen "  +
					" left join fetch data.customFieldDisplay custFieldDisplay "  +
					" left join fetch custFieldDisplay.customField custField "  +
					" where data.biospecimen.id in (:biospecimenIdsToInclude)" +
					" and data.customFieldDisplay in (:customFieldsList)" + 
					" order by data.biospecimen.id " ;
			Query query2 = getSession().createQuery(queryString);
			query2.setParameterList("biospecimenIdsToInclude", biospecimenIdsToInclude);
			query2.setParameterList("customFieldsList", customFieldToGet);
		
			List<BiospecimenCustomFieldData> scfData = query2.list();
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirBiospecimenCustomData = allTheData.getBiospecimenCustomData();

			ExtractionVO valuesForThisLss = new ExtractionVO();
			HashMap<String, String> map = null;
			Long previousBiospecimenId = null;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (BiospecimenCustomFieldData data : scfData) {
				
				if(previousBiospecimenId==null){
					map = new HashMap<String, String>();
					previousBiospecimenId = data.getBiospecimen().getId();
				}
				else if(data.getBiospecimen().getId().equals(previousBiospecimenId)){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisLss.setKeyValues(map);
					previousBiospecimenId = data.getBiospecimen().getId();
					hashOfSubjectsWithTheirBiospecimenCustomData.put(previousBiospecimenId.toString(), valuesForThisLss);	
					map = new HashMap<String, String>();//reset
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}
				}			
			}
			
			//finalize the last entered key value sets/extraction VOs
			if(map!=null && previousBiospecimenId!=null){
				valuesForThisLss.setKeyValues(map);
				hashOfSubjectsWithTheirBiospecimenCustomData.put(previousBiospecimenId.toString(), valuesForThisLss);
			}
			
			//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list or parts of it.
			allTheData.setBiospecimenCustomData(hashOfSubjectsWithTheirBiospecimenCustomData);
		}		
		return idsToInclude;
	}	
	
	
	
	
	/**
	 * @param allTheDataz
	 * @param search
	 * @param uidsToInclude
	 * @return the updated list of uids that are still left after the filtering.
	 *
	private List<Long> applyBiospecimenCustomFilters(DataExtractionVO allTheData, Search search, List<Long> uidsToInclude){

		String dataFilters = getBiospecimenCustomFieldFilters(search);

		//only bother with the query and data IF data fields are needed
		if (!getSelectedBiospecimenCustomFieldDisplaysForSearch(search).isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()
				){
			String queryString = "select data from BiospecimenCustomFieldData data " 
			//					+ " where data.study.id = " + search.getStudy().getId()
								+ " where "
								+ (dataFilters.isEmpty()?"":(dataFilters + " and ")) +
								" data.biospecimen.linkSubjectStudy.id in (:uidList) "; //TODO group and order by biospecimen (uid?)!
			Query query = getSession().createQuery(queryString);
			query.setParameterList("uidList", uidsToInclude);
			List<BiospecimenCustomFieldData> scfData = query.list(); 	
			
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirBiospecimenCustomData = allTheData.getBiospecimenCustomData();

			ExtractionVO valuesForThisLss = new ExtractionVO();
			HashMap<String, String> map = null;
			String previousBiospecimenUID = null;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (BiospecimenCustomFieldData data : scfData) {
				
				if(previousBiospecimenUID==null){
					map = new HashMap<String, String>();
					previousBiospecimenUID = data.getBiospecimen().getBiospecimenUid();
					//given it is first time ensure map has a record of the subjectUID
					map.put("subjectUID", data.getBiospecimen().getLinkSubjectStudy().getSubjectUID());
				}
				else if(data.getBiospecimen().getBiospecimenUid().equals(previousBiospecimenUID)){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisLss.setKeyValues(map);
					previousBiospecimenUID = data.getBiospecimen().getBiospecimenUid();
					hashOfSubjectsWithTheirBiospecimenCustomData.put(previousBiospecimenUID, valuesForThisLss);	

					map = new HashMap<String, String>();//reset
					
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}
				}

			}
			//finalize the last entered key value sets/extraction VOs
			if(map!=null && previousBiospecimenUID!=null){
				valuesForThisLss.setKeyValues(map);
				hashOfSubjectsWithTheirBiospecimenCustomData.put(previousBiospecimenUID, valuesForThisLss);
				//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list.
				allTheData.setBiospecimenCustomData(hashOfSubjectsWithTheirBiospecimenCustomData);
			}
			//else no data, leave the set of biospecimen custom data as it was

		}

		//only bother with restricting IF data filters exist
		if(!dataFilters.isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){
			String queryString2 = "select data.biospecimen.linkSubjectStudy.id from BiospecimenCustomFieldData data " 
					//					+ " where data.study.id = " + search.getStudy().getId()
										+ " where "
										+ (dataFilters.isEmpty()?"":(dataFilters + " and ")) +
										" data.biospecimen.linkSubjectStudy.id in (:uidList) ";
			Query query2 = getSession().createQuery(queryString2);
			query2.setParameterList("uidList", uidsToInclude);
			List<Long> updatedListOfSubjectUIDs = query2.list(); 	
			
			//TODO ASAP in addtion to this it is now time to wipe the old data which is no longer to be included due to latest filter
				// if we don't want to garauntee order of wiping data, etc do this outside this method, in the calling method
			
			log.info("updated size of UIDs=" + updatedListOfSubjectUIDs.size());
			return updatedListOfSubjectUIDs;
		}
		else{
			return uidsToInclude;
		}
	}	
	
	*/
	
	

	
	private List<Long> getSubjectIdsForBiospecimenIds(List<Long> biospecimenIdsToInclude) {
		String queryString = "select bio.linkSubjectStudy.id from Biospecimen bio " 
							+ " where bio.id in (:biospecimenIdsToInclude) ";
		Query query = getSession().createQuery(queryString);
		query.setParameterList("biospecimenIdsToInclude", biospecimenIdsToInclude);
		return query.list();
	}

	/**
	 * @param allTheDataz
	 * @param search
	 * @param uidsToInclude
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applyBiocollectionCustomFilters(DataExtractionVO allTheData, Search search, List<Long> uidsToInclude){

		String dataFilters = getBiocollectionCustomFieldFilters(search);

		//only bother with the query and data IF data fields are needed
		if (!getSelectedBiocollectionCustomFieldDisplaysForSearch(search).isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){
			String queryString = "select data from BioCollectionCustomFieldData data " 
			//					+ " where data.study.id = " + search.getStudy().getId()
								+ " where "
								+ (dataFilters.isEmpty()?"":(dataFilters + " and ")) +
								" data.bioCollection.linkSubjectStudy.id in (:uidList) "; //TODO group and order by biocollection (uid?)!
			Query query = getSession().createQuery(queryString);
			query.setParameterList("uidList", uidsToInclude);
			List<BioCollectionCustomFieldData> scfData = query.list(); 	
			
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirBiocollectionCustomData = allTheData.getBiocollectionCustomData();

			ExtractionVO valuesForThisLss = new ExtractionVO();
			HashMap<String, String> map = null;
			String previousBiocollectionUID = null;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (BioCollectionCustomFieldData data : scfData) {
				
				if(previousBiocollectionUID==null){
					map = new HashMap<String, String>();
					previousBiocollectionUID = data.getBioCollection().getBiocollectionUid();
					//given it is first time ensure map has a record of the subjectUID
					map.put("subjectUID", data.getBioCollection().getLinkSubjectStudy().getSubjectUID());
				}
				else if(data.getBioCollection().getBiocollectionUid().equals(previousBiocollectionUID)){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisLss.setKeyValues(map);
					previousBiocollectionUID = data.getBioCollection().getBiocollectionUid();
					hashOfSubjectsWithTheirBiocollectionCustomData.put(previousBiocollectionUID, valuesForThisLss);	

					map = new HashMap<String, String>();//reset
					
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}
				}
			
			}
			//finalize the last entered key value sets/extraction VOs
			if(map!=null && previousBiocollectionUID!=null){
				valuesForThisLss.setKeyValues(map);
				hashOfSubjectsWithTheirBiocollectionCustomData.put(previousBiocollectionUID, valuesForThisLss);
				//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list.
				allTheData.setBiocollectionCustomData(hashOfSubjectsWithTheirBiocollectionCustomData);
			}
			//else no data, leave the set of biocollection custom data as it was

		}

		//only bother with restricting IF data filters exist
		if(!dataFilters.isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){
			String queryString2 = "select data.bioCollection.linkSubjectStudy.id from BioCollectionCustomFieldData data " 
					//					+ " where data.study.id = " + search.getStudy().getId()
										+ " where "
										+ (dataFilters.isEmpty()?"":(dataFilters + " and ")) +
										" data.bioCollection.linkSubjectStudy.id in (:uidList) ";
			Query query2 = getSession().createQuery(queryString2);
			query2.setParameterList("uidList", uidsToInclude);
			List<Long> updatedListOfSubjectUIDs = query2.list(); 	
			
			//TODO ASAP in addtion to this it is now time to wipe the old data which is no longer to be included due to latest filter
				// if we don't want to garauntee order of wiping data, etc do this outside this method, in the calling method
			
			log.info("updated size of UIDs=" + updatedListOfSubjectUIDs.size());
			return updatedListOfSubjectUIDs;
		}
		else{
			return uidsToInclude;
		}
	}	
	
	
		
	

	
	/**
	 * @param allTheDataz
	 * @param search
	 * @param uidsToInclude
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applyPhenoCustomFilters(DataExtractionVO allTheData, Search search, List<Long> uidsToInclude){

		String dataFilters = getPhenoFilters(search);

		//only bother with the query and data IF data fields are needed
		if (!getSelectedPhenoCustomFieldDisplaysForSearch(search).isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){
			String queryString = "select data from PhenoData data " 
			//					+ " where data.study.id = " + search.getStudy().getId()
								+ " where "
								+ (dataFilters.isEmpty()?"":(dataFilters + " and ")) +
								" data.phenoCollection.linkSubjectStudy.id in (:uidList) "; //TODO group and order by phenocollection (uid?)!
			Query query = getSession().createQuery(queryString);
			query.setParameterList("uidList", uidsToInclude);
			List<PhenoData> scfData = query.list(); 	
			
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirPhenoCustomData = allTheData.getPhenoCustomData();

			ExtractionVO valuesForThisLss = new ExtractionVO();
			HashMap<String, String> map = null;
			long phenoCollectionId = -1L;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (PhenoData data : scfData) {
				
				if(phenoCollectionId==-1){
					map = new HashMap<String, String>();
					phenoCollectionId = data.getPhenoCollection().getId();
					//given it is first time ensure map has a record of the subjectUID
					map.put("subjectUID", data.getPhenoCollection().getLinkSubjectStudy().getSubjectUID());
				}
				else if(data.getPhenoCollection().getId().equals(phenoCollectionId)){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisLss.setKeyValues(map);
					phenoCollectionId = data.getPhenoCollection().getId();
					hashOfSubjectsWithTheirPhenoCustomData.put(("" + phenoCollectionId), valuesForThisLss);	

					map = new HashMap<String, String>();//reset
					
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}
				}
			
			
			}
			//finalize the last entered key value sets/extraction VOs
			if(map!=null && phenoCollectionId==-1){
				valuesForThisLss.setKeyValues(map);
				hashOfSubjectsWithTheirPhenoCustomData.put(("" + phenoCollectionId), valuesForThisLss);
				//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list.
				allTheData.setPhenoCustomData(hashOfSubjectsWithTheirPhenoCustomData);
			}
			//else no data, leave the set of biocollection custom data as it was

		}

		//only bother with restricting IF data filters exist
		if(!dataFilters.isEmpty() &&
				uidsToInclude != null && !uidsToInclude.isEmpty()){
			String queryString2 = "select data.phenoCollection.linkSubjectStudy.id from PhenoData data " 
					//					+ " where data.study.id = " + search.getStudy().getId()
										+ " where "
										+ (dataFilters.isEmpty()?"":(dataFilters + " and ")) +
										" data.phenoCollection.linkSubjectStudy.id in (:uidList) ";
			Query query2 = getSession().createQuery(queryString2);
			query2.setParameterList("uidList", uidsToInclude);
			List<Long> updatedListOfSubjectUIDs = query2.list(); 	
			
			//TODO ASAP in addtion to this it is now time to wipe the old data which is no longer to be included due to latest filter
				// if we don't want to garauntee order of wiping data, etc do this outside this method, in the calling method
			
			log.info("updated size of UIDs=" + updatedListOfSubjectUIDs.size());
			return updatedListOfSubjectUIDs;
		}
		else{
			return uidsToInclude;
		}
	}	
	
	
	
	
	/**
	 * TODO : Chris, please note that we have to complete the hardcoding below after Thileana finishes his insert statements for demographic field.
	 * Alternatively you have reflection to deal with which may be a bit of a nightmare but less lines of code...but completely your call.
	 * 
	 * @param lss
	 * @param personFields
	 * @param lssFields
	 * @param addressFields
	 * @param phoneFields
	 * @return
	 */
	private HashMap<String, String> constructKeyValueHashmap(LinkSubjectStudy lss, Collection<DemographicField> personFields, Collection<DemographicField> lssFields,
			Collection<DemographicField> addressFields, Collection<DemographicField> phoneFields) {
		HashMap map = new HashMap<String, String>();
		for (DemographicField field : personFields) {
			// TODO: Analyse performance cost of using reflection instead...would be CLEANER code...maybe dangerous/slow
			if (field.getFieldName().equalsIgnoreCase("firstName")) {
				if(lss.getPerson().getFirstName()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getFirstName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("middleName")) {
				if(lss.getPerson().getMiddleName()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getMiddleName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("lastName")) {
				if(lss.getPerson().getLastName()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getLastName());
				}	
			}
			else if (field.getFieldName().equalsIgnoreCase("preferredName")) {
				if(lss.getPerson().getPreferredName()!=null)
					map.put(field.getPublicFieldName(), lss.getPerson().getPreferredName());
			}
			else if (field.getFieldName().equalsIgnoreCase("dateOfBirth")) {
				if(lss.getPerson().getDateOfBirth()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getDateOfBirth().toString());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("dateOfDeath")) {
				if(lss.getPerson().getDateOfDeath()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getDateOfDeath().toString());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("causeOfDeath")) {
				if(lss.getPerson().getCauseOfDeath()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getCauseOfDeath());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("preferredEmail")) {
				if(lss.getPerson().getPreferredEmail()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getPreferredEmail());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("preferredEmailStatus")) {
				if(lss.getPerson().getPreferredEmailStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getPreferredEmailStatus().toString());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("otherEmail")) {
				if(lss.getPerson().getOtherEmail()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getOtherEmail());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("otherEmailStatus")) {
				if(lss.getPerson().getOtherEmailStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getOtherEmailStatus().toString());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("dateLastKnownAlive")) {
				if(lss.getPerson().getDateLastKnownAlive()!=null){
			 		map.put(field.getPublicFieldName(), lss.getPerson().getDateLastKnownAlive().toString());					
				}
			}
			/*	private Long id;
			 * private String firstName;
			 * private String middleName;
				private String lastName;
				private String preferredName;
			private GenderType genderType;
			private VitalStatus vitalStatus;
			private TitleType titleType;
			private MaritalStatus maritalStatus;
				private Date dateOfBirth;
				private Date dateOfDeath;
				private String causeOfDeath;
			private PersonContactMethod personContactMethod;
				private String preferredEmail;
				private String otherEmail;
				private EmailStatus preferredEmailStatus;
				private EmailStatus otherEmailStatus;
				private Date dateLastKnownAlive;*/
		}
		for (DemographicField field : lssFields) {
			if (field.getFieldName().equalsIgnoreCase("consentDate")) {
				if(lss.getConsentDate()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentDate() == null ? "" : lss.getConsentDate().toString());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("subjectUID")) {
				if(lss.getSubjectCustomFieldDataSet()==null){
					map.put(field.getPublicFieldName(), lss.getSubjectUID());
				}
			}
			// etc
		}
		for (DemographicField field : addressFields) {
			int count = 0;
			// assume they have filtered type/status in hql sql statement
			for (Address a : lss.getPerson().getAddresses()) {
				count++;
				if (field.getFieldName().equalsIgnoreCase("postCode")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getPostCode());
				}
				//etc

			}
		}
		for (DemographicField field : phoneFields) {
			int count = 0;
			// assume they have filtered type/status in hql sql statement
			for (Phone phone : lss.getPerson().getPhones()) {
				count++;
				if (field.getFieldName().equalsIgnoreCase("phoneNumber")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getPhoneNumber());
				}
				// etc
			}
		}
		return map;
	}
	
	
	private HashMap<String, String> constructKeyValueHashmap(BioCollection bioCollection, Collection<BiocollectionField> bioCollectionFields) {
		HashMap<String,String> map = new HashMap<String, String>();
				
		for (BiocollectionField field : bioCollectionFields) {
			// TODO: Analyse performance cost of using reflection instead...would be CLEANER code...maybe dangerous/slow
			if (field.getFieldName().equalsIgnoreCase("name")) {
				if(bioCollection.getName() !=null){
					map.put(field.getPublicFieldName(), bioCollection.getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("collectionDate")) {
				if(bioCollection.getCollectionDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getCollectionDate()!=null?bioCollection.getCollectionDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("deleted")) {
				if(bioCollection.getDeleted()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDeleted()!=null?bioCollection.getDeleted().toString():"");
				}
			}			
			else if (field.getFieldName().equalsIgnoreCase("timestamp")) {
				if(bioCollection.getTimestamp()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getTimestamp());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("comments")) {
				if(bioCollection.getComments()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getComments());
				}
			}			
			else if (field.getFieldName().equalsIgnoreCase("hospital")) {
				if(bioCollection.getHospital()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getHospital());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("surgeryDate")) {
				if(bioCollection.getSurgeryDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getSurgeryDate()!=null?bioCollection.getSurgeryDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("diagCategory")) {
				if(bioCollection.getDiagCategory()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDiagCategory());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("refDoctor")) {
				if(bioCollection.getRefDoctor()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getRefDoctor());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("patientage")) {
				if(bioCollection.getPatientage()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getPatientage()!=null?bioCollection.getPatientage().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("dischargeDate")) {
				if(bioCollection.getDischargeDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDischargeDate()!=null?bioCollection.getDischargeDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("hospitalUr")) {
				if(bioCollection.getHospitalUr()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getHospitalUr());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("diagDate")) {
				if(bioCollection.getDiagDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDiagDate()!=null?bioCollection.getDiagDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("collectiongroupId")) {
				if(bioCollection.getCollectiongroupId()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getCollectiongroupId()!=null?bioCollection.getCollectiongroupId().toString():"");
				}
			}			
			else if (field.getFieldName().equalsIgnoreCase("episodeNum")) {
				if(bioCollection.getEpisodeNum()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getEpisodeNum());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("episodeDesc")) {
				if(bioCollection.getEpisodeDesc()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getEpisodeDesc());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("collectiongroup")) {
				if(bioCollection.getCollectiongroup()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getCollectiongroup());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("tissuetype")) {
				if(bioCollection.getTissuetype()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getTissuetype());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("tissueclass")) {
				if(bioCollection.getTissueclass()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getTissueclass());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("pathlabno")) {
				if(bioCollection.getPathlabno()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getPathlabno());
				}
			}
			
		}
		
		
		return map;
		
	}
		
	private HashMap<String, String> constructKeyValueHashmap(Biospecimen biospecimen, Collection<BiospecimenField> biospecimenFields) {
		HashMap<String,String> map = new HashMap<String, String>();
		
		for (BiospecimenField field : biospecimenFields) {
			if (field.getFieldName().equalsIgnoreCase("sampleType")) {
				if(biospecimen.getSampleType() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getSampleType().getName());
				}
			}  //TODO : this is really going to need to be expanded out.
			else if (field.getFieldName().equalsIgnoreCase("invCell")) {
				if(biospecimen.getInvCell()!=null){
					map.put(field.getPublicFieldName(), "Col: "+biospecimen.getInvCell().getColno()+" Row: "+biospecimen.getInvCell().getRowno());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("parentUid")) {
				if(biospecimen.getParentUid() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getParentUid());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("oldId")) {
				if(biospecimen.getOldId() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getOldId()!=null?biospecimen.getOldId().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("deleted")) {
				if(biospecimen.getDeleted() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getDeleted()!=null?biospecimen.getDeleted().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("timestamp")) {
				if(biospecimen.getTimestamp() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getTimestamp());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("otherid")) {
				if(biospecimen.getOtherid() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getOtherid());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("storedIn")) {
				if(biospecimen.getStoredIn() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getStoredIn().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("depth")) {
				if(biospecimen.getDepth() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getDepth()!=null ?biospecimen.getDepth().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("grade")) {
				if(biospecimen.getGrade() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getGrade().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("sampleDate")) {
				if(biospecimen.getSampleDate() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getSampleDate()!=null?biospecimen.getSampleDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("sampleTime")) {
				if(biospecimen.getSampleTime() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getSampleTime()!=null?biospecimen.getSampleTime().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("processedDate")) {
				if(biospecimen.getProcessedDate() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getProcessedDate()!=null?biospecimen.getProcessedDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("processedTime")) {
				if(biospecimen.getProcessedTime() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getProcessedTime()!=null?biospecimen.getProcessedTime().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("species")) {
				if(biospecimen.getSpecies() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getSpecies().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("qtyCollected")) {
				if(biospecimen.getQuantity() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getQuantity()!=null ?biospecimen.getQuantity().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("qtyCollected")) {
				if(biospecimen.getQtyCollected() != null){
					map.put(field.getPublicFieldName(), biospecimen.getQtyCollected()!=null?biospecimen.getQtyCollected().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("qtyRemoved")) {
				if(biospecimen.getQtyRemoved() != null){
					map.put(field.getPublicFieldName(), biospecimen.getQtyRemoved()!=null?biospecimen.getQtyRemoved().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("comments")) {
				if(biospecimen.getComments() != null){
					map.put(field.getPublicFieldName(), biospecimen.getComments());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("quantity")) {
				if(biospecimen.getQuantity() != null){
					map.put(field.getPublicFieldName(), biospecimen.getQuantity()!=null?biospecimen.getQuantity().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("unit")) {
				if(biospecimen.getUnit() != null){
					map.put(field.getPublicFieldName(), biospecimen.getUnit().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("treatmentType")) {
				if(biospecimen.getTreatmentType() != null){
					map.put(field.getPublicFieldName(), biospecimen.getTreatmentType().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("barcoded")) {
				if(biospecimen.getBarcoded() != null){
					map.put(field.getPublicFieldName(), biospecimen.getBarcoded()!=null?biospecimen.getBarcoded().toString():"" );
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("quality")) {
				if(biospecimen.getQuality() != null){
					map.put(field.getPublicFieldName(), biospecimen.getQuality().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("anticoag")) {
				if(biospecimen.getAnticoag() != null){
					map.put(field.getPublicFieldName(), biospecimen.getAnticoag().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("status")) {
				if(biospecimen.getStatus() != null){
					map.put(field.getPublicFieldName(), biospecimen.getStatus().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("concentration")) {
				if(biospecimen.getConcentration() != null){
					map.put(field.getPublicFieldName(), biospecimen.getConcentration()!=null ? biospecimen.getConcentration().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("amount")) {
				if(biospecimen.getAmount() != null){
					map.put(field.getPublicFieldName(), biospecimen.getAmount()!=null?biospecimen.getAmount().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("biospecimenProtocol")) {
				if(biospecimen.getBiospecimenProtocol() != null){
					map.put(field.getPublicFieldName(), biospecimen.getBiospecimenProtocol().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("purity")) {
				if(biospecimen.getPurity() != null){
					map.put(field.getPublicFieldName(), biospecimen.getPurity()!=null?biospecimen.getPurity().toString():"");
				}
			}
		}
		return map;
	}
	
	private String getPersonFilters(Search search, String filterThusFar) {
		String filterClause = filterThusFar;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			DemographicField demoField = filter.getDemographicField();
			if ((demoField != null)) {
				if (demoField.getEntity() != null && demoField.getEntity().equals(Entity.Person)) {
					String nextFilterLine = (demoField.getFieldName() + getHQLForOperator(filter.getOperator()) + "'" + filter.getValue() + "' ");
					if (filter.getOperator().equals(Operator.BETWEEN)) {
						nextFilterLine += (" AND " + "'" + filter.getSecondValue() + "' ");
					}
					if (filterClause == null || filterClause.isEmpty()) {
						filterClause = " and lss.person." + nextFilterLine;
					}
					else {
						filterClause = filterClause + " and lss.person." + nextFilterLine;
					}
				}
			}
		}
		log.info("filterClause = " + filterClause);
		return (filterClause == null ? "" : filterClause);
	}

	private String getLSSFilters(Search search, String personFilters) {
		String filterClause = personFilters;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			DemographicField demoField = filter.getDemographicField();
			if ((demoField != null)) {
				if (demoField.getEntity() != null && demoField.getEntity().equals(Entity.LinkSubjectStudy)) {
					String nextFilterLine = (demoField.getFieldName() + getHQLForOperator(filter.getOperator()) + "'" + filter.getValue() + "' ");
					if (filter.getOperator().equals(Operator.BETWEEN)) {
						nextFilterLine += (" AND " + "'" + filter.getSecondValue() + "' ");
					}
					
					filterClause = filterClause + " and lss." + nextFilterLine; // there will always be something before it so the and part is ok (study restrictions etc)
				}
			}
		}
		log.info(" filterClauseAfterLSS FILTERS = " + filterClause);
		return (filterClause == null ? "" : filterClause);
	}

	private String getBiospecimenFilters(Search search){//, String filterThusFar) {
		String filterClause = "";// filterThusFar;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			BiospecimenField biospecimenField = filter.getBiospecimenField();
			if ((biospecimenField != null)) {
				if (biospecimenField.getEntity() != null && biospecimenField.getEntity().equals(Entity.Biospecimen)) {
					String nextFilterLine = (biospecimenField.getFieldName() + getHQLForOperator(filter.getOperator()) + "'" + filter.getValue() + "' ");
					if (filter.getOperator().equals(Operator.BETWEEN)) {
						nextFilterLine += (" AND " + "'" + filter.getSecondValue() + "' ");
					}
					filterClause = " and biospecimen." + nextFilterLine;
				}
			}
		}
		log.info("biospecimen filterClause = " + filterClause);
		return filterClause;
	}


	private String getBiocollectionFilters(Search search){//, String filterThusFar) {
		String filterClause = "";// filterThusFar;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			BiocollectionField biocollectionField = filter.getBiocollectionField();
			if ((biocollectionField != null)) {
				if (biocollectionField.getEntity() != null && biocollectionField.getEntity().equals(Entity.BioCollection)) {
					String nextFilterLine = (biocollectionField.getFieldName() + getHQLForOperator(filter.getOperator()) + "'" + filter.getValue() + "' ");
					if (filter.getOperator().equals(Operator.BETWEEN)) {
						nextFilterLine += (" AND " + "'" + filter.getSecondValue() + "' ");
					}
					filterClause = " and biocollection." + nextFilterLine;
				}
			}
		}
		log.info("biocollection filterClause = " + filterClause);
		return filterClause;
	}

	/**
	 * @param search
	 * @param idsToInclude 
	 * @return a list of subject id's which match the filter we put together.
	 */
	private String getSubjectCustomFieldQuery(Search search) {

		int count = 0;
		String selectComponent = " Select data0.linkSubjectStudy.id ";
		String fromComponent = " from SubjectCustomFieldData data0 ";
		String whereClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
				
				String tablePrefix = "data" + count++;
				log.info("what is this SUBJECT CUSTOM filter? " + filter.getId() + "     for data row? " + tablePrefix );
				
				String nextFilterLine =  "";

				// Determine field type and assign key value accordingly
				// ( data.customFieldDisplay.id=99 AND data.numberDataValue  >  0  )  and ( ( data.customFieldDisplay.id=112 AND data.numberDataValue  >=  0 ) ) 

				//TODO evaluate date entry/validation
				if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else{
					count--;
				}
				//TODO ASAP i think all of these might need to start thinking about is null or is not null?
				if (filter.getOperator().equals(Operator.BETWEEN)) {
					nextFilterLine += (" AND " + filter.getSecondValue());
				}

				if(whereClause.isEmpty()){
					whereClause = " where " + nextFilterLine + " ) ";
				}
				else{
					fromComponent += ",  SubjectCustomFieldData " + tablePrefix ;
					whereClause = whereClause + " and " + nextFilterLine + " )  " +
							" and data0.linkSubjectStudy.id = " + tablePrefix +  ".linkSubjectStudy.id ";
				}
			}
		}
		whereClause += " and data0.linkSubjectStudy.id in (:idList) ";//count>0?"":
		log.info("filterClauseAfterSubjectCustomField FILTERS = " + whereClause);

		if(count>0){
			return selectComponent + fromComponent + whereClause;
		}
		else{
			return "";
		}
	}


	/**
	 * @param search
	 * @param idsToInclude 
	 * @return a query string to attain the updated list of bioscpecimens.
	 */
	private String getBiospecimenDataCustomFieldIdQuery(Search search) {

		int count = 0;
		String selectComponent = " Select data0.linkSubjectStudy.id ";
		String fromComponent = " from BiospecimenCustomFieldData data0 ";
		String whereClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
				
				String tablePrefix = "data" + count++;
				log.info("what is this BIOSPECIMEN CUSTOM filter? " + filter.getId() + "     for data row? " + tablePrefix );
				
				String nextFilterLine =  "";

				// Determine field type and assign key value accordingly
				// ( data.customFieldDisplay.id=99 AND data.numberDataValue  >  0  )  and ( ( data.customFieldDisplay.id=112 AND data.numberDataValue  >=  0 ) ) 

				//TODO evaluate date entry/validation
				if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else{
					count--;
				}
				//TODO ASAP i think all of these might need to start thinking about is null or is not null?
				if (filter.getOperator().equals(Operator.BETWEEN)) {
					nextFilterLine += (" AND " + filter.getSecondValue());
				}

				if(whereClause.isEmpty()){
					whereClause = " where " + nextFilterLine + " ) ";
				}
				else{
					fromComponent += ",  BiospecimenCustomFieldData " + tablePrefix ;
					whereClause = whereClause + " and " + nextFilterLine + " )  " +
							" and data0.linkSubjectStudy.id = " + tablePrefix +  ".linkSubjectStudy.id ";
				}
			}
		}
		whereClause += " and data0.linkSubjectStudy.id in (:idList) ";//count>0?"":
		log.info("filterClauseAfterBiospecimenCustomField FILTERS = " + whereClause);

		if(count>0){
			return selectComponent + fromComponent + whereClause;
		}
		else{
			return "";
		}
	}



	
	private String getBiospecimenCustomFieldFilters(Search search) {

		String filterClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN)) {
				
				log.info("what is this filter? " + filter.getId());
				String nextFilterLine = new String();

				// Determine field type and assign key value accordingly
				//TODO evaluate date entry/validation
				if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				
				//TODO ASAP i think all of these might need to start thinking about is null or is not null?
				if (filter.getOperator().equals(Operator.BETWEEN)) {
					nextFilterLine += (" AND " + filter.getSecondValue());
				}
				if(filterClause.isEmpty()){
					filterClause =  nextFilterLine ;
				}
				else{
					filterClause = filterClause + " and (" + nextFilterLine + ")";
				}
			}
		}
		log.info("filterClauseAfterBiospecimenCustomField FILTERS = " + filterClause);

		return filterClause;
	}


	
	private String getBiocollectionCustomFieldFilters(Search search) {

		String filterClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION)) {
				
				log.info("what is this filter? " + filter.getId());
				
				String nextFilterLine = new String();

				// Determine field type and assign key value accordingly
				//TODO evaluate date entry/validation
				if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				
				//TODO ASAP i think all of these might need to start thinking about is null or is not null?
				if (filter.getOperator().equals(Operator.BETWEEN)) {
					nextFilterLine += (" AND " + filter.getSecondValue());
				}
				if(filterClause.isEmpty()){
					filterClause =  nextFilterLine ;
				}
				else{
					filterClause = filterClause + " and (" + nextFilterLine + ")";
				}
			}
		}
		log.info("filterClauseAfterBiocollectionCustomField FILTERS = " + filterClause);

		return filterClause;
	}

	
	private String getPhenoFilters(Search search) {

		String filterClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION)) {
				
				log.info("what is this filter? " + filter.getId());
				
				String nextFilterLine = new String();

				// Determine field type and assign key value accordingly
				//TODO evaluate date entry/validation
				if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					nextFilterLine = (" data.customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND data.textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				
				//TODO ASAP i think all of these might need to start thinking about is null or is not null?
				if (filter.getOperator().equals(Operator.BETWEEN)) {
					nextFilterLine += (" AND " + filter.getSecondValue());
				}
				if(filterClause.isEmpty()){
					filterClause =  nextFilterLine ;
				}
				else{
					filterClause = filterClause + " and (" + nextFilterLine + ")";
				}
			}
		}
		log.info("filterClauseAfterPheno FILTERS = " + filterClause);

		return filterClause;
	}

	/**
	 * 
	 * @param operator
	 * @return the string representing that operator in HQL WITH some white space surrounding it
	 */
	private String getHQLForOperator(Operator operator) {
		switch (operator) {

			case BETWEEN: {
				return " BETWEEN ";
			}
			case EQUAL: {
				return " = ";
			}
			case GREATER_THAN: {
				return " > ";
			}
			case GREATER_THAN_OR_EQUAL: {
				return " >= ";
			}
			case LESS_THAN: {
				return " < ";
			}
			case LESS_THAN_OR_EQUAL: {
				return " > ";
			}
			case LIKE: {
				return " like ";
			}
			case NOT_EQUAL: {
				return " <> ";
			}
		}
		return " = ";
	}

	@SuppressWarnings("unchecked")
	public void createQueryFilters(List filterList) throws ArkSystemException {
		List<QueryFilter> queryFilterList = (List<QueryFilter>) filterList;

		if (validateQueryFilters(queryFilterList)) {

			for (QueryFilter filter : queryFilterList) {
				getSession().saveOrUpdate(filter);
			}
		}
	}

	public boolean validateQueryFilters(List<QueryFilter> queryFilterList) {
		for (QueryFilter filter : queryFilterList) {
			// TODO ASAP validate type, operator and value are compatible
			// if(filter.getValue()==null) i guess null or empty is valid for
			// string
		}
		return true;
	}

	public List<QueryFilterVO> getQueryFilterVOs(Search search) {

		Criteria criteria = getSession().createCriteria(QueryFilter.class);
		criteria.add(Restrictions.eq("search", search));
		List<QueryFilter> filters = criteria.list();
		List<QueryFilterVO> filterVOs = new ArrayList<QueryFilterVO>();
		for (QueryFilter filter : filters) {
			QueryFilterVO filterVO = new QueryFilterVO();
			filterVO.setQueryFilter(filter);
			if (filter.getDemographicField() != null) {
				filterVO.setFieldCategory(FieldCategory.DEMOGRAPHIC_FIELD);
			}
			else if (filter.getBiocollectionField() != null) {
				filterVO.setFieldCategory(FieldCategory.BIOCOLLECTION_FIELD);
			}
			else if (filter.getBiospecimenField() != null) {
				filterVO.setFieldCategory(FieldCategory.BIOSPECIMEN_FIELD);
			}
			else if (filter.getCustomFieldDisplay() != null) {
				filterVO.setFieldCategory(getFieldCategoryFor(filter.getCustomFieldDisplay().getCustomField().getArkFunction()));
			}
			filterVOs.add(filterVO);
		}
		return filterVOs;
	}

	private FieldCategory getFieldCategoryFor(ArkFunction arkFunction) {
		if (arkFunction.getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
			return FieldCategory.SUBJECT_CFD;
		}
		else if (arkFunction.getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION)) {
			return FieldCategory.PHENO_CFD;
		}
		else if (arkFunction.getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION)) {
			return FieldCategory.BIOCOLLECTION_CFD;
		}
		else {// should really have a default! TODO
			return FieldCategory.BIOSPECIMEN_CFD;
		}
	}

	@Override
	public void deleteQueryFilter(QueryFilter queryFilter) {
		if(queryFilter != null) {
			getSession().delete(queryFilter);
		}
	}

	@Override
	public List<Study> getParentAndChildStudies(Long id) {
		Criteria studyCriteria = getSession().createCriteria(Study.class);
		Study study = getStudy(id);

		if (study.getStudyStatus() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_STATUS, study.getStudyStatus()));
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}
		}
		else {
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}

		}
		
		if(study.getParentStudy() != null && !study.getParentStudy().equals(study)) {
			studyCriteria.add(Restrictions.or(Restrictions.idEq(id), Restrictions.eq("parentStudy", study.getParentStudy())));
		}
		else {
			studyCriteria.add(Restrictions.or(Restrictions.idEq(id), Restrictions.eq("parentStudy", study)));
		}
		
		studyCriteria.addOrder(Order.asc("id"));	
		studyCriteria.addOrder(Order.asc(Constants.STUDY_NAME));
		return studyCriteria.list();
	}	
	
	
	
	private void addDataFromMegaDemographicQuery(DataExtractionVO allTheData, Collection<DemographicField> personFields, Collection<DemographicField> lssFields,
			Collection<DemographicField> addressFields, Collection<DemographicField> phoneFields, Collection<CustomFieldDisplay> subjectCFDs, Search search) {
		log.info("in addDataFromMegaDemographicQuery");
		if (!lssFields.isEmpty() || !personFields.isEmpty() || !addressFields.isEmpty() || !phoneFields.isEmpty() || !subjectCFDs.isEmpty()) { // hasEmailFields(dfs)
			// ||
			// TODO
			// Also  needs  to  consider  filtering??
			String personFilters = getPersonFilters(search, null);
			String lssAndPersonFilters = getLSSFilters(search, personFilters);
//			String subjectCustomFieldFilters = getSubjectCustomFieldFilters(search, lssAndPersonFilters);
			
			String queryString = "select distinct lss "
					+ // , address, lss, email " +
					" from LinkSubjectStudy lss " 
					+ ((!personFields.isEmpty()) ? " left join fetch lss.person person " : "") 
					+ ((!addressFields.isEmpty()) ? " left join lss.person.addresses a " : "")
					+ ((!phoneFields.isEmpty()) ? " left join lss.person.phones p " : "")
//					+ ((!subjectCFDs.isEmpty()) ? " left join fetch lss.subjectCustomFieldDataSet scfd " : "") 
					// Force restriction on Study of search
					+ " where lss.study.id = " + search.getStudy().getId()
					+ lssAndPersonFilters + " ";
//					+ subjectCustomFieldFilters ;
			// TODO : getLSSFilters
			// TODO : getAddress
			// TODO : getPhone
			// TODO : getBiospecCustomFilters
			// TODO : getBiocollectionCustomFilters
			// TODO : getSubjectCustomFilters
			// TODO : getPhenoCustomFilters

			List<LinkSubjectStudy> subjects = getSession().createQuery(queryString).list();
			log.info("size=" + subjects.size());

			// DataExtractionVO devo; = new DataExtractionVO();
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirDemographicData = allTheData.getDemographicData();

			/**
			 * this is putting the data we extracted into a generic kind of VO doc that will be converted to an appopriate format later (such as
			 * csv/xls/pdf/xml/etc)
			 */
			for (LinkSubjectStudy lss : subjects) {
				ExtractionVO sev = new ExtractionVO();
				sev.setKeyValues(constructKeyValueHashmap(lss, personFields, lssFields, addressFields, phoneFields));
				hashOfSubjectsWithTheirDemographicData.put(lss.getSubjectUID(), sev);
			}

			/**
			 * 
			 * 
			 * TODO : CHris, I am doing an example get off subject custom data here, but do it however and WHERE you see most efficient or for that
			 * matter start with where its easiest to get working...then do more efficient after that
			 * 
			 */

			// TODO TAKES TOO LONG!!!
			//List<SubjectCustomFieldData> scfData = getCustomFieldDataFor(subjectCFDs, subjects); // todo add orderby SUBJECT... or alterative method with
																
			List<SubjectCustomFieldData> scfData = new ArrayList<SubjectCustomFieldData>(0);
			// order by to help us keeping track of subjects
			//log.info("we got " + scfData.size());
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirSubjectCustomData = allTheData.getSubjectCustomData();

			for (LinkSubjectStudy lss : subjects) {
				ExtractionVO sev = new ExtractionVO();
				HashMap<String, String> map = new HashMap<String, String>();
				for (SubjectCustomFieldData data : scfData) {
					
					if(data.getLinkSubjectStudy().getId().equals(lss.getId())){
						
						// if any error value, then just use that
						if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
							map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
						}
						else {
							// Determine field type and assign key value accordingly
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
							}
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
							}
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
							}
						}
						sev.setKeyValues(map);
					}
				}
				hashOfSubjectsWithTheirSubjectCustomData.put(lss.getSubjectUID(), sev);
			}

			/**
			 * this is just logging to see if things work
			 */
			prettyLoggingOfWhatIsInOurMegaObject(hashOfSubjectsWithTheirDemographicData, FieldCategory.DEMOGRAPHIC_FIELD);
			prettyLoggingOfWhatIsInOurMegaObject(hashOfSubjectsWithTheirSubjectCustomData, FieldCategory.SUBJECT_CFD);
			
			//iDataExtractionDao.createSubjectDemographicCSV(search, hashOfSubjectsWithTheirDemographicData, FieldCategory.DEMOGRAPHIC_FIELD);

		}
		/*
		 * if(hasLSSFields(dfs) && hasPersonFields(dfs) && hasAddressFields(dfs) && hasAddressFields(dfs)){//TODO Also needs to consider filtering???
		 * String queryString = "select distinct lss " + //, address, lss, email " + " from LinkSubjectStudy lss" +
		 * " left join fetch lss.person person " + " left join fetch person.addresses a " + //TODO FIX //" where lss.person.firstName like 'Travis%' " +
		 * //, link_subject_study lss " + getPersonFilters(search); //TODO ADD THE REST //final ResultTransformer trans;// = new
		 * DistinctRootEntityResultTransformer(); //qry.setResultTransformer(trans); Query query = getSession().createQuery(queryString);
		 * List<LinkSubjectStudy> subjects = query.list(); log.info("size=" + subjects.size()); for(LinkSubjectStudy lss : subjects){
		 * log.info(" person " + lss.getPerson().getId() + lss.getSubjectUID() + lss.getPerson().getFirstName()); log.info(" addresses size " +
		 * lss.getPerson().getAddresses().size()); } }
		 */
	}
	
	
	private void addDataFromMegaBiocollectionQuery(DataExtractionVO allTheData,Collection<BiocollectionField> biocollectionFields,Collection<CustomFieldDisplay> collectionCFDs, Search search ){
		if(!biocollectionFields.isEmpty() || !collectionCFDs.isEmpty()){
			
			Criteria criteria = getSession().createCriteria(BioCollection.class);
			criteria.add(Restrictions.eq("study", search.getStudy()));
			
			Collection<BioCollection> bioCollectionList=criteria.list();
			
			HashMap<String, ExtractionVO> hashOfBiocollectionData = allTheData.getBiocollectionData();
			
			for (BioCollection bioCollection : bioCollectionList) {
				ExtractionVO sev = new ExtractionVO();
				sev.setKeyValues(constructKeyValueHashmap(bioCollection,biocollectionFields));
				hashOfBiocollectionData.put(bioCollection.getBiocollectionUid(), sev);
			}
			
			
			//TODO Seems we are not printing the custom fields to the csv
			List<BioCollectionCustomFieldData> bccfData = new ArrayList<BioCollectionCustomFieldData>(0);

			HashMap<String, ExtractionVO> hashOfBioCollectionCustomData = allTheData.getBiocollectionCustomData();

			for (BioCollection bioCollection : bioCollectionList) {
				ExtractionVO sev = new ExtractionVO();
				HashMap<String, String> map = new HashMap<String, String>();
				for (BioCollectionCustomFieldData data : bccfData) {
					
					if(data.getBioCollection().getId().equals(bioCollection.getId())){						
						// if any error value, then just use that
						if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
							map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
						}
						else {
							// Determine field type and assign key value accordingly
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
							}
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
							}
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
							}
						}
						sev.setKeyValues(map);
					}
				}
				hashOfBioCollectionCustomData.put(bioCollection.getBiocollectionUid(), sev);
			}
			
			prettyLoggingOfWhatIsInOurMegaObject(hashOfBiocollectionData, FieldCategory.BIOCOLLECTION_FIELD);
			prettyLoggingOfWhatIsInOurMegaObject(hashOfBioCollectionCustomData, FieldCategory.BIOCOLLECTION_CFD);
			
			iDataExtractionDao.createBiocollectionCSV(search, hashOfBiocollectionData, FieldCategory.BIOCOLLECTION_FIELD);
		}
	}


	private void addDataFromMegaBiospecimenQuery(DataExtractionVO allTheData,Collection<BiospecimenField> biospecimenFields,Collection<CustomFieldDisplay> specimenCFDs, Search search ){
		log.info("in addDataFromMegaBiospecimenQuery");
		
		if(!biospecimenFields.isEmpty() || !specimenCFDs.isEmpty()){
									
			StringBuffer queryBuffer =new StringBuffer("select distinct biospecimen ");
			queryBuffer.append("from Biospecimen biospecimen " );
			queryBuffer.append(	" 	left join fetch biospecimen.sampleType sampleType ");
			queryBuffer.append(	"	left join fetch biospecimen.invCell invCell " );
			queryBuffer.append(	"	left join fetch biospecimen.storedIn storedIn " );
			queryBuffer.append(	"	left join fetch biospecimen.grade grade " );
			queryBuffer.append(	"	left join fetch biospecimen.species species " );
			queryBuffer.append(	"	left join fetch biospecimen.unit unit " );
			queryBuffer.append(	"	left join fetch biospecimen.treatmentType treatmentType ");
			queryBuffer.append(	"	left join fetch biospecimen.quality quality ");
			queryBuffer.append(	"	left join fetch biospecimen.anticoag anticoag ");
			queryBuffer.append(	"	left join fetch biospecimen.status status " );
			queryBuffer.append(	"	left join fetch biospecimen.biospecimenProtocol biospecimenProtocol ");
			queryBuffer.append(	" where biospecimen.study.id = " + search.getStudy().getId());

			Collection<Biospecimen> biospecimenList=getSession().createQuery(queryBuffer.toString()).list();
			
			HashMap<String, ExtractionVO> hashOfBiospecimenData = allTheData.getBiospecimenData();
			
			for (Biospecimen biospecimen : biospecimenList) {
				ExtractionVO sev = new ExtractionVO();
				sev.setKeyValues(constructKeyValueHashmap(biospecimen,biospecimenFields));
				hashOfBiospecimenData.put(biospecimen.getBiospecimenUid(), sev);
			}			
			
			//TODO Seems we are not printing the custom fields to the csv
			List<BiospecimenCustomFieldData> bscfData = new ArrayList<BiospecimenCustomFieldData>(0);

			HashMap<String, ExtractionVO> hashOfBiospecimenCustomData = allTheData.getBiocollectionCustomData();

			for (Biospecimen biospecimen : biospecimenList) {
				ExtractionVO sev = new ExtractionVO();
				HashMap<String, String> map = new HashMap<String, String>();
				for (BiospecimenCustomFieldData data : bscfData) {
					
					if(data.getBiospecimen().getId().equals(biospecimen.getId())){						
						// if any error value, then just use that
						if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
							map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
						}
						else {
							// Determine field type and assign key value accordingly
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
							}
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
							}
							if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
								map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
							}
						}
						sev.setKeyValues(map);
					}
				}
				hashOfBiospecimenCustomData.put(biospecimen.getBiospecimenUid(), sev);
			}
			
			prettyLoggingOfWhatIsInOurMegaObject(hashOfBiospecimenData, FieldCategory.BIOSPECIMEN_FIELD);
			prettyLoggingOfWhatIsInOurMegaObject(hashOfBiospecimenCustomData, FieldCategory.BIOSPECIMEN_CFD);
			
			//iDataExtractionDao.createSubjectDemographicCSV(search, allTheData.getDemographicData(), FieldCategory.DEMOGRAPHIC_FIELD);
			//iDataExtractionDao.createBiospecimenCSV(search, allTheData.getBiospecimenData(), FieldCategory.BIOSPECIMEN_FIELD);
			//iDataExtractionDao.createBiocollectionCSV(search, allTheData.getBiocollectionData(), FieldCategory.BIOCOLLECTION_FIELD);
			//iDataExtractionDao.createBiospecimenDataCustomCSV(search, hashOfBiospecimenCustomData, FieldCategory.BIOSPECIMEN_CFD);
		}
	}

	
	private void prettyLoggingOfWhatIsInOurMegaObject(HashMap<String, ExtractionVO> hashOfSubjectsWithData, FieldCategory fieldCategory) {
		log.info(" we have " + hashOfSubjectsWithData.size() + " entries for category '" + fieldCategory + "'");
		for (String subjectUID : hashOfSubjectsWithData.keySet()) {
			HashMap<String, String> keyValues = hashOfSubjectsWithData.get(subjectUID).getKeyValues();
			log.info(subjectUID + " has " + keyValues.size() + "demo fields"); 
			// remove(subjectUID).getKeyValues().size() + "demo fields");
			for (String key : keyValues.keySet()) {
				log.info("     key=" + key + "\t   value=" + keyValues.get(key));
			}
		}
	}


	/*
	 * private boolean hasEmailFields(Collection<DemographicField> demographicFields) { for(DemographicField demographicField : demographicFields){
	 * if(demographicField.getEntity()!=null && demographicField.getEntity().equals(Entity.Email)){ return true; } } return false; } private boolean
	 * hasPhoneFields(Collection<DemographicField> demographicFields) { for(DemographicField demographicField : demographicFields){
	 * if(demographicField.getEntity()!=null && demographicField.getEntity().equals(Entity.Phone)){ return true; } } return false; } private boolean
	 * hasAddressFields(Collection<DemographicField> demographicFields) { for(DemographicField demographicField : demographicFields){
	 * if(demographicField.getEntity()!=null && demographicField.getEntity().equals(Entity.Address)){ return true; } } return false; } private void
	 * addAddressData(DataExtractionVO allTheData, Collection<DemographicField> dfs){ //consent etc etc for(DemographicField field : dfs){
	 * if(field.getEntity().equals(Entity.Address)){ // addressFieldsString.append("address." + field.getFieldName()); // "AS \"ADDRESS_\" +
	 * getFieldName() // addressFieldsString.append(field.getFieldName()); break; } } } private void addLSSData(DataExtractionVO allTheData,
	 * Collection<DemographicField> dfs){ for(DemographicField field : dfs){ if(field.getEntity().equals(Entity.Address)){ //
	 * addressFieldsString.append("address." + field.getFieldName()); // "AS \"ADDRESS_\" + getFieldName() //
	 * addressFieldsString.append(field.getFieldName()); break; } LinkSubjectStudy lss = new LinkSubjectStudy(); //Field fieldFromDB =
	 * lss.getClass().getField(field.getFieldName()); didn't know if using reflection was best I fear I may be stuck with hardcoding } } private
	 * boolean hasDemographicFieldsOrFilters( Collection<DemographicField> dfs) { // TODO Auto-generated method stub return false; } private boolean
	 * hasLSSFields(Collection<DemographicField> demographicFields) { for(DemographicField demographicField : demographicFields){
	 * if(demographicField.getEntity()!=null && demographicField.getEntity().equals(Entity.LinkSubjectStudy)){ return true; } } return false; } private
	 * boolean hasPersonFields(Collection<DemographicField> demographicFields) { for(DemographicField demographicField : demographicFields){
	 * if(demographicField.getEntity()!=null && demographicField.getEntity().equals(Entity.Person)){ return true; } } return false; }
	 */
	/*
	 * private String constructDemographicQuery(Collection<DemographicField> dfs){ StringBuffer sb = new StringBuffer(); StringBuffer
	 * personFieldsString = new StringBuffer(); StringBuffer lssFieldsString = new StringBuffer(); StringBuffer emailFieldsString = new StringBuffer();
	 * StringBuffer addressFieldsString = new StringBuffer(); StringBuffer personFiltersString = new StringBuffer(); StringBuffer lssFiltersString =
	 * new StringBuffer(); StringBuffer emailFiltersString = new StringBuffer(); StringBuffer addressFiltersString = new StringBuffer(); //consent etc
	 * etc
	 * 
	 * for(DemographicField field : dfs){ switch(field.getEntity()){ case Person:{ personFieldsString.append("person." + field.getFieldName());
	 * personFieldsString.append(field.getFieldName()); personFieldsString.append(", "); break; } case LinkSubjectStudy:{ lssFieldsString.append("lss."
	 * + field.getFieldName()); lssFieldsString.append(field.getFieldName()); lssFieldsString.append(", "); break; } case Email:{
	 * emailFieldsString.append("email." + field.getFieldName()); emailFieldsString.append(field.getFieldName()); emailFieldsString.append(", ");
	 * break; } case Address:{ addressFieldsString.append("address." + field.getFieldName()); // "AS \"ADDRESS_\" + getFieldName()
	 * addressFieldsString.append(field.getFieldName()); addressFieldsString.append(", "); break; } /*case Entity.Person:{
	 * personFieldsString.append("person." + field.getFieldName()); personFieldsString.append(field.getFieldName()); personFieldsString.append(", ");
	 * break; }* default: { log.error("NEVER SHOULD HAVE A ENTITY WE DONT KNOW ABOUT!!!!!!!!!!!!!!!!" ); //TODO asap enums and constraints to ensure }
	 * } if(!lssFieldsString.toString().isEmpty()){ } /** TODO: NOW RUN CONSTRAINTS RELATED TO DEMOGRAPHICS FIELDS TOO * } return ""; }
	 */

	/*
	 * 	private void prettyLoggingOfWhatIsInOurMegaObject(HashMap<String, ExtractionVO> hashOfSubjectsWithData, FieldCategory fieldCategory) {
		log.info(" we have " + hashOfSubjectsWithData.size() + " entries for category '" + fieldCategory + "'");
		for (String subjectUID : hashOfSubjectsWithData.keySet()) {
			HashMap<String, String> keyValues = hashOfSubjectsWithData.get(subjectUID).getKeyValues();
			log.info(subjectUID + " has " + keyValues.size() + "demo fields"); 
			// remove(subjectUID).getKeyValues().size() + "demo fields");
			for (String key : keyValues.keySet()) {
				log.info("     key=" + key + "\t   value=" + keyValues.get(key));
			}
		}
	}
	 */
	public SearchPayload createSearchPayload(byte[] bytes) {
		SearchPayload payload = new SearchPayload(bytes);
		getSession().save(payload);
		getSession().flush();
		getSession().refresh(payload);
		return payload;
	}
	
	public SearchPayload getSearchPayloadForSearchResult(SearchResult searchResult) {
		getSession().refresh(searchResult);
		return searchResult.getSearchPayload();
	}

	public List<SearchResult> getSearchResultList(Long searchResultId) {
		Criteria criteria = getSession().createCriteria(SearchResult.class);
		criteria.add(Restrictions.eq("search.id",searchResultId));
		return criteria.list();
	}
	
	public void createSearchResult(SearchResult searchResult) {
		getSession().saveOrUpdate(searchResult);
	}
	
	public void createSearchResult(Search search, File file) {
		try {
			SearchResult sr = new SearchResult();
			sr.setSearch(search);
			sr.setFilename(file.getName());
			String fileFormatName = file.getName().substring(file.getName().lastIndexOf('.') + 1).toUpperCase();
			sr.setFileFormat(getFileFormatByName(fileFormatName));
			sr.setStartTime(new Date(System.currentTimeMillis()));
			sr.setDelimiterType(getDelimiterTypeByDelimiterChar(','));
			byte[] bytes = org.apache.commons.io.FileUtils.readFileToByteArray(file);
			sr.setChecksum(DigestUtils.md5DigestAsHex(bytes));
			sr.setSearchPayload(createSearchPayload(bytes));
			sr.setUserId(SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal().toString());
			createSearchResult(sr);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSearchResult(SearchResult searchResult) {
		getSession().delete(searchResult);
	}
	
	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
}