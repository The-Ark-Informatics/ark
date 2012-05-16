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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionUidPadChar;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BioCollectionUidToken;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
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
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
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
import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.UploadType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.util.CsvListReader;
import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 * @param <T>
 * 
 */
@Repository("commonStudyDao")
public class StudyDao<T> extends HibernateSessionDao implements IStudyDao {
	
	private static Logger	log	= LoggerFactory.getLogger(StudyDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.dao.IStudyDao#getStudy(au.org.theark.core.model.study.entity.Study)
	 */
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

	public SubjectStatus getSubjectStatus(String statusName) {

		SubjectStatus statusToReturn = null;

		SubjectStatus subjectStatus = new SubjectStatus();
		subjectStatus.setName(statusName);
		Example example = Example.create(subjectStatus);

		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);

		
		if (criteria != null){
			List<SubjectStatus> results = criteria.list();
			if(results != null && !results.isEmpty()) {
				statusToReturn = (SubjectStatus) results.get(0);
			}
		}

		return statusToReturn;
	}

	/**
	 * Given a status name will return the StudyStatus object.
	 */
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException {
		StudyStatus studyStatus = new StudyStatus();
		studyStatus.setName("Archive");
		Example studyStatusExample = Example.create(studyStatus);

		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatusExample);
		if (studyStatusCriteria != null){
			List<StudyStatus> results = studyStatusCriteria.list();
			if(results != null && results.size() > 0) {
				return (StudyStatus)results.get(0);
			}
		}

		log.error("Study Status Table maybe out of synch. Please check if it has an entry for Archive status.  Cannot locate a study status with " + statusName + " in the database");
		throw new StatusNotAvailableException();

	}

	public List<StudyStatus> getListOfStudyStatus() {
		Example studyStatus = Example.create(new StudyStatus());
		Criteria criteria = getSession().createCriteria(StudyStatus.class).add(studyStatus);
		return criteria.list();

	}

	public Study getStudy(Long id) {
		Study study = (Study) getSession().get(Study.class, id);
		return study;
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

	//TODO:  cache?
	public Collection<GenderType> getGenderTypes() {
		Example example = Example.create(new GenderType());
		Criteria criteria = getSession().createCriteria(GenderType.class).add(example);
		return criteria.list();
	}

	public List<PhoneType> getListOfPhoneType() {
		Example phoneTypeExample = Example.create(new PhoneType());
		Criteria criteria = getSession().createCriteria(PhoneType.class).add(phoneTypeExample);
		return criteria.list();
	}

	public List<SubjectStatus> getSubjectStatus() {

		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();
	}

	public Collection<MaritalStatus> getMaritalStatus() {
		Example example = Example.create(new MaritalStatus());
		Criteria criteria = getSession().createCriteria(MaritalStatus.class).add(example);
		return criteria.list();
	}

	/**
	 * Look up the Link Subject Study for subjects linked to a study
	 * 
	 * @param subjectVO
	 * @return
	 */
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
			// Place the LinkSubjectStudy instance into a SubjectVO and add the SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setLinkSubjectStudy(linkSubjectStudy);
			Person person = subject.getLinkSubjectStudy().getPerson();
			subject.setSubjectPreviousLastname(getPreviousLastname(person));
			subjectVOList.add(subject);
		}
		return subjectVOList;

	}

	public List<Phone> getPhonesForPerson(Person person) {

		Criteria personCriteria = getSession().createCriteria(Phone.class);
		personCriteria.add(Restrictions.eq("person", person));// Filter the phones linked to this personID/Key
		return personCriteria.list();
	}

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
	//	Query linkSubjectStudyCriteria = getSession().createQuery(" select subject from LinkSubjectStudy subject " +
	//			" where study =:study and subjectUID=:subjectUID " +
	//			" left join fetch subject.person ");
	//	linkSubjectStudyCriteria.setParameter("subjectUID", subjectUID);
	//	linkSubjectStudyCriteria.setParameter("study", study);
		log.warn("about to ask hibernate for list right now");
		return (LinkSubjectStudy)linkSubjectStudyCriteria.uniqueResult();
		//		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.uniqueResult();

//		if (listOfSubjects != null && listOfSubjects.size() > 0) {
//			return listOfSubjects.get(0);
//		}
//		return null;
	}

	/**
	 * Returns a list of Countries
	 */
	public List<Country> getCountries() {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	//TODO
	public Country getCountry(Long id) {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("id", id));
		return (Country) criteria.list().get(0);
	}

	//TODO
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

	@Override
	public void createAuditHistory(AuditHistory auditHistory, String userId, Study study) {
		Date date = new Date(System.currentTimeMillis());

		if(userId == null){//if not forcing a userID manually, get currentuser
			Subject currentUser = SecurityUtils.getSubject();
			auditHistory.setArkUserId((String) currentUser.getPrincipal());			
		}
		else{
			auditHistory.setArkUserId(userId);		
		}
		if(study==null){
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
		else{
			auditHistory.setStudyStatus(study.getStudyStatus());			
		}
		auditHistory.setDateTime(date);
		getSession().save(auditHistory);
		//getSession().flush();		
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
		if (criteria != null){//should it ever?
			List<PersonLastnameHistory> results = criteria.list();
			if(results != null && !results.isEmpty()) {
				personLastnameHistoryToReturn = (PersonLastnameHistory) results.get(0);
			}
		}
		return personLastnameHistoryToReturn;
	}

	public void createPersonLastnameHistory(Person person) {
		PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory();
		personLastNameHistory.setPerson(person);
		personLastNameHistory.setLastName(person.getLastName());

		getSession().save(personLastNameHistory);
	}

	public void updatePersonLastnameHistory(Person person) {
		PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory();
		personLastnameHistory.setPerson(person);
		personLastnameHistory.setLastName(person.getLastName());

		String currentLastName = getCurrentLastname(person);

		if (currentLastName == null || (currentLastName != null && !currentLastName.equalsIgnoreCase(person.getLastName())))
			getSession().save(personLastnameHistory);
	}

	public String getPreviousLastname(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}
		criteria.addOrder(Order.desc("id"));
		PersonLastnameHistory personLastameHistory = new PersonLastnameHistory();

		List<PersonLastnameHistory> results = criteria.list();
		if (results.size()>1) {
			
			//what this is saying is get the second-last last-name to display as "previous lastname"
			personLastameHistory = (PersonLastnameHistory) results.get(1);
		}//else it doesnt have a previous...only a current

		return personLastameHistory.getLastName();
	}

	public String getCurrentLastname(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}
		criteria.addOrder(Order.desc("id"));

		List<PersonLastnameHistory> results = criteria.list();
		PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory();
		if (!results.isEmpty()) {
			personLastnameHistory = (PersonLastnameHistory) results.get(0);
		}

		return personLastnameHistory.getLastName();
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
			titleType = (TitleType)results.get(0);
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

		//TODO analyse
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {

			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			// Place the LinkSubjectStudy instance into a SubjectVO and add the SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setLinkSubjectStudy(linkSubjectStudy);
			Person person = subject.getLinkSubjectStudy().getPerson();
			subject.setSubjectPreviousLastname(getPreviousLastname(person));
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

		criteria.add(Restrictions.eq("arkUser", arkUser));// Represents the user either who is logged in or one that is provided
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

	public UploadType getDefaultUploadType(){
		return (UploadType)(getSession().get(UploadType.class, 1L));//TODO:  maybe fix ALL such entities by adding isDefault boolean to table?
	}
	
	
	public List<StudyUpload> searchUploads(StudyUpload uploadCriteria) {
		Criteria criteria = getSession().createCriteria(StudyUpload.class);
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
		List<StudyUpload> resultsList = criteria.list();

		return resultsList;
	}

	public void createUpload(StudyUpload studyUpload) {
		Subject currentUser = SecurityUtils.getSubject();
		String userId = (String) currentUser.getPrincipal();
		studyUpload.setUserId(userId);
		getSession().save(studyUpload);
		//getSession().flush();
	}

	public void updateUpload(StudyUpload studyUpload) {
		//getSession().flush();
		Subject currentUser = SecurityUtils.getSubject();
		String userId = (String) currentUser.getPrincipal();
		studyUpload.setUserId(userId);
		getSession().update(studyUpload);
		//getSession().flush();
	}

	public String getDelimiterTypeNameByDelimiterChar(char delimiterCharacter) {
		String delimiterTypeName = null;
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		criteria.add(Restrictions.eq("delimiterCharacter", delimiterCharacter));
		criteria.setProjection(Projections.property("name"));
		delimiterTypeName = (String) criteria.uniqueResult();
		return delimiterTypeName;
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
			// Place the LinkSubjectStudy instance into a SubjectVO and add the SubjectVO into a List
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


	public long countNumberOfSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection subjectUids) {
		String queryString = "select count(*) " +
									"from LinkSubjectStudy subject " +
									"where study =:study " +
									"and subjectUID in  (:subjects) ";
		Query query =  getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);
		
		return (Long)query.uniqueResult();
	}

	@Override
	public List<String> getSubjectUIDsThatAlreadyExistWithTheseUIDs(Study study, Collection subjectUids) {
		String queryString = "select subject.subjectUID " +
		"from LinkSubjectStudy subject " +
		"where study =:study " +
		"and subjectUID in  (:subjects) ";
		Query query =  getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return query.list();
	}

	@Override
	public List<LinkSubjectStudy> getSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection subjectUids) {
		String queryString = "select subject " +
		"from LinkSubjectStudy subject " +
		"where study =:study " +
		"and subjectUID in  (:subjects) ";
		Query query =  getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return query.list();
	}

	public List<String> getAllSubjectUIDs(Study study) {
		String queryString = "select subject.subjectUID " +
		"from LinkSubjectStudy subject " +
		"where study =:study " +
		"order by subjectUID ";
		Query query =  getSession().createQuery(queryString);
		query.setParameter("study", study);

		return query.list();
	}
	
}
