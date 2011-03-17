/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.LinkSubjectStudycomp;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 * @param <T>
 *
 */
@Repository("commonStudyDao")
public class StudyDao<T>  extends HibernateSessionDao implements IStudyDao{

	private static Logger log = LoggerFactory.getLogger(StudyDao.class);
	/* (non-Javadoc)
	 * @see au.org.theark.core.dao.IStudyDao#getStudy(au.org.theark.core.model.study.entity.Study)
	 */
	public List<Study> getStudy(Study study)
	{
		
		Criteria studyCriteria =  getSession().createCriteria(Study.class);
		
		if(study.getId() != null){
			studyCriteria.add(Restrictions.eq(Constants.STUDY_KEY,study.getId()));	
		}
		
		if(study.getName() != null){
			studyCriteria.add(Restrictions.ilike(Constants.STUDY_NAME, study.getName(), MatchMode.ANYWHERE));	
		}
		
		if(study.getDateOfApplication() != null){
			studyCriteria.add(Restrictions.eq(Constants.DATE_OF_APPLICATION, study.getDateOfApplication()));
		}
		
		if(study.getEstimatedYearOfCompletion() != null){
			studyCriteria.add(Restrictions.eq(Constants.EST_YEAR_OF_COMPLETION, study.getEstimatedYearOfCompletion()));
		}

		if(study.getChiefInvestigator() != null){
			studyCriteria.add(Restrictions.ilike(Constants.CHIEF_INVESTIGATOR, study.getChiefInvestigator(),MatchMode.ANYWHERE));
		}

		if(study.getContactPerson() != null){
			studyCriteria.add(Restrictions.ilike(Constants.CONTACT_PERSON, study.getContactPerson(), MatchMode.ANYWHERE));
		}
		
		if(study.getStudyStatus() != null){
			studyCriteria.add(Restrictions.eq(Constants.STUDY_STATUS, study.getStudyStatus()));
			try{
				StudyStatus status  = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}catch(StatusNotAvailableException notAvailable){
				log.error("Cannot look up and filter on archive status.Reference data could be missing");
			}
		}else{
			try{
				StudyStatus status  = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}catch(StatusNotAvailableException notAvailable){
				log.error("Cannot look up and filter on archive status.Reference data could be missing");
			}

		}
		
		

		studyCriteria.addOrder(Order.asc(Constants.STUDY_NAME));
		List<Study> studyList  = studyCriteria.list();
		
		return studyList;
	}
	
	
	public  SubjectStatus getSubjectStatus(String statusName){
		
		SubjectStatus statusToReturn = null;
		
		SubjectStatus subjectStatus = new SubjectStatus();
		subjectStatus.setName("Archive");
		Example example = Example.create(subjectStatus);
		
		
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		if(criteria != null && criteria.list() != null && criteria.list().size() > 0){
			statusToReturn =  (SubjectStatus)criteria.list().get(0);	
		}
		
		return statusToReturn;
	}
	
	/**
	 * Given a status name will return the StudyStatus object.
	 */
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException{
		StudyStatus studyStatus = new StudyStatus();
		studyStatus.setName("Archive");
		Example studyStatusExample = Example.create(studyStatus);
		
		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatusExample);
		if(studyStatusCriteria != null && studyStatusCriteria.list() != null && studyStatusCriteria.list().size() > 0){
			return (StudyStatus)studyStatusCriteria.list().get(0);	
		}else{
			log.error("Study Status Table maybe out of synch. Please check if it has an entry for Archive status");
			System.out.println("Cannot locate a study status with " + statusName + " in the database");
			throw new StatusNotAvailableException();
		}
		
	}
	

	public List<StudyStatus> getListOfStudyStatus() {

		Example studyStatus = Example.create(new StudyStatus());
		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatus);
		return   studyStatusCriteria.list();
		
	}
	
	public Study getStudy(Long id){
		Study study =  (Study)getSession().get(Study.class, id);
		return study;
	}
	
	public Collection<TitleType> getTitleType(){
		Example example = Example.create(new TitleType());
		Criteria criteria = getSession().createCriteria(TitleType.class).add(example);
		return criteria.list();
	}
	public Collection<VitalStatus> getVitalStatus(){
		Example example = Example.create(new VitalStatus());
		Criteria criteria = getSession().createCriteria(VitalStatus.class).add(example);
		return criteria.list();
	}
	
	public Collection<GenderType> getGenderType(){
		Example example = Example.create(new GenderType());
		Criteria criteria = getSession().createCriteria(GenderType.class).add(example);
		return criteria.list();
	}
	
	public List<PhoneType> getListOfPhoneType() {
		Example phoneTypeExample = Example.create(new PhoneType());
		Criteria criteria = getSession().createCriteria(PhoneType.class).add(phoneTypeExample);
		return   criteria.list();
	}


	public Collection<SubjectStatus> getSubjectStatus(){
		
		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();
	}
	
	public Collection<MaritalStatus> getMaritalStatus(){
		Example example = Example.create(new MaritalStatus());
		Criteria criteria = getSession().createCriteria(MaritalStatus.class).add(example);
		return criteria.list();		
	}
	
	/**
	 * Look up the Link Subject Study for subjects linked to a study
	 * @param subjectVO
	 * @return
	 */
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO){
		Criteria criteria =  getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("person", "p");
		criteria.add(Restrictions.eq("study.id",subjectVO.getSubjectStudy().getStudy().getId()));	
		
		if(subjectVO.getSubjectStudy().getPerson() != null){
		
			if(subjectVO.getSubjectStudy().getPerson().getId() != null){
				criteria.add(Restrictions.eq("p.id",subjectVO.getSubjectStudy().getPerson().getId() ));	
			}

			if(subjectVO.getSubjectStudy().getPerson().getFirstName() != null){
				criteria.add(Restrictions.ilike("p.firstName",subjectVO.getSubjectStudy().getPerson().getFirstName(),MatchMode.ANYWHERE));
			}
			
			if(subjectVO.getSubjectStudy().getPerson().getMiddleName() != null){
				criteria.add(Restrictions.ilike("p.middleName",subjectVO.getSubjectStudy().getPerson().getMiddleName(),MatchMode.ANYWHERE));
			}
		
			if(subjectVO.getSubjectStudy().getPerson().getLastName() != null){
				criteria.add(Restrictions.ilike("p.lastName",subjectVO.getSubjectStudy().getPerson().getLastName(),MatchMode.ANYWHERE));
			}
			
			if(subjectVO.getSubjectStudy().getPerson().getGenderType() != null){
				criteria.add(Restrictions.eq("p.genderType.id",subjectVO.getSubjectStudy().getPerson().getGenderType().getId()));
			}
			
			if(subjectVO.getSubjectStudy().getPerson().getVitalStatus() != null){
				criteria.add(Restrictions.eq("p.vitalStatus.id",subjectVO.getSubjectStudy().getPerson().getVitalStatus().getId()));
			}
			
		}
		
		if( subjectVO.getSubjectStudy().getSubjectUID()!= null && subjectVO.getSubjectStudy().getSubjectUID().length() > 0){
			criteria.add(Restrictions.eq("subjectUID",subjectVO.getSubjectStudy().getSubjectUID()));
		}
		
		if(subjectVO.getSubjectStudy().getSubjectStatus() != null){
			criteria.add(Restrictions.eq("subjectStatus",subjectVO.getSubjectStudy().getSubjectStatus()));
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if(subjectStatus != null){
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));	
			}
		}else{
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if(subjectStatus != null){
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));	
			}
		}
		
		criteria.addOrder(Order.asc("subjectUID"));
		List<LinkSubjectStudy> list = criteria.list();
		
		Collection<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			
			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			//Place the LinkSubjectStudy instance into a SubjectVO and add the SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setSubjectStudy(linkSubjectStudy);
			Person person = subject.getSubjectStudy().getPerson();
			subject.setSubjectPreviousLastname(getPreviousLastname(person));
			subjectVOList.add(subject);
		}
		return subjectVOList;

	}
	
	
	public List<Phone> getPhonesForPerson(Person person){
		
		Criteria personCriteria  = getSession().createCriteria(Phone.class);
		personCriteria.add(Restrictions.eq("person", person));//Filter the phones linked to this personID/Key
		return personCriteria.list();
	}
	
	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException{
		
		Criteria linkSubjectStudyCriteria =  getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("id",id));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if(listOfSubjects != null && listOfSubjects.size() > 0){
			return listOfSubjects.get(0);
		}else{
			throw new EntityNotFoundException("The entity with id" + id.toString() +" cannot be found.");
		}
	}
	
	public LinkSubjectStudy getSubjectByUID(String subjectUID) throws EntityNotFoundException{
		
		Criteria linkSubjectStudyCriteria =  getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("subjectUID",subjectUID));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if(listOfSubjects != null && listOfSubjects.size() > 0){
			return listOfSubjects.get(0);
		}else{
			throw new EntityNotFoundException("There is no subject with the given UID " + subjectUID.toString() );
		}
	}
	
	/**
	 * Returns a list of Countries
	 */
	public List<Country> getCountries(){
		Criteria criteria  = getSession().createCriteria(Country.class);
		return criteria.list();
	}
	
	public Country getCountry(Long id){
		Criteria criteria  = getSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("id",id));
		return (Country) criteria.list().get(0);
	}
	
	public Country getCountry(String countryName){
		Criteria criteria  = getSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("name",countryName));
		return (Country) criteria.list().get(0);
	}
	
	public List<CountryState>  getStates(Country country){
		
		if(country == null ){
			//Default it to local one like australia, this can be based on locale
			//Get the default Country from backend and then use that to fetch the state
			country = getCountry(Constants.DEFAULT_COUNTRY);
		}
		StringBuffer hqlString =	new StringBuffer();
		hqlString.append(" from CountryState as cs ");
		hqlString.append(" where cs.country.id = ");
		hqlString.append( country.getId());
		
		Query query = getSession().createQuery(hqlString.toString());
		List<CountryState> list =  (List<CountryState>) query.list();
		return list;
	}
	
	/**
	 * Gets  a list of all Address Types
	 * @return
	 */
	public List<AddressType> getAddressTypes(){
		Criteria criteria  = getSession().createCriteria(AddressType.class);
		return criteria.list();
	}
	
	/**
	 * Gets  a list of all Address Statuses
	 * @return
	 */
	public List<AddressStatus> getAddressStatuses() {
		Criteria criteria = getSession().createCriteria(AddressStatus.class);
		return criteria.list();
	}

	public List<ConsentStatus> getConsentStatus(){
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		return criteria.list();
	}
	
	public List<StudyCompStatus> getStudyComponentStatus(){
		Criteria criteria = getSession().createCriteria(StudyCompStatus.class);
		return criteria.list();
	}
	
	public List<StudyComp> getStudyComponent(){
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		List<StudyComp> studyCompList = criteria.list();
		return criteria.list();
	}
	
	public boolean  isSubjectConsentedToComponent(StudyComp studyComponent, Person subject, Study study){
		boolean isConsented = false;
		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("studyComp.id",studyComponent.getId()));
		criteria.add(Restrictions.eq("study.id", study.getId()));
		criteria.add(Restrictions.eq("subject.id", subject.getId()));
		List list  = criteria.list();
		if (list != null && criteria.list().size() > 0){
			isConsented = true;
		}
		return isConsented;
	}
	
	/**
	 * Returns a list of Consent types hardcopy, electronic document etc.
	 * @return
	 */
	public List<ConsentType> getConsentType(){
		Criteria criteria = getSession().createCriteria(ConsentType.class);
		return criteria.list();
	}
	
	public List<ConsentAnswer> getConsentAnswer(){
		Criteria criteria = getSession().createCriteria(ConsentAnswer.class);
		return criteria.list();
	}
	
	public List<YesNo> getYesNoList(){
		Criteria criteria = getSession().createCriteria(YesNo.class);
		return criteria.list();
	}
	
	public void createAuditHistory(AuditHistory auditHistory){
	    Date date = new Date(System.currentTimeMillis());
	    Subject currentUser = SecurityUtils.getSubject();
	    auditHistory.setArkUserId((String) currentUser.getPrincipal());
	    Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
	    if(sessionStudyId != null && auditHistory.getStudyStatus() == null ){
	   	 auditHistory.setStudyStatus(getStudy(sessionStudyId).getStudyStatus());
	    }else{
	    	
	    	if( auditHistory.getEntityType().equalsIgnoreCase(au.org.theark.core.Constants.ENTITY_TYPE_STUDY)){
	    		Study study = getStudy(auditHistory.getEntityId());
		    	if(study != null){
		    		auditHistory.setStudyStatus(study.getStudyStatus());	
		    	}
	    	}

	    }
	    
	    auditHistory.setDateTime(date);
	    getSession().save(auditHistory);
	}

	public List<PersonContactMethod> getPersonContactMethodList()
	{
		Criteria criteria = getSession().createCriteria(PersonContactMethod.class);
		return criteria.list();
	}

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory){
		PersonLastnameHistory personLastnameHistoryToReturn = null;
		
		Example example = Example.create(personSurnameHistory);
		
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class).add(example);
		if(criteria != null && criteria.list() != null && criteria.list().size() > 0){
			personLastnameHistoryToReturn =  (PersonLastnameHistory)criteria.list().get(0);	
		}
		
		return personLastnameHistoryToReturn;
	}
	
	public void createPersonLastnameHistory(Person person){
		PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory();
		personLastNameHistory.setPerson(person);
		personLastNameHistory.setLastName(person.getLastName());
		
		getSession().save(personLastNameHistory);
	}
	
	public void updatePersonLastnameHistory(Person person){
		PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory();
		personLastnameHistory.setPerson(person);
		personLastnameHistory.setLastName(person.getLastName());
		
		String currentLastName = getCurrentLastname(person);
		
		if(currentLastName == null || (currentLastName != null && !currentLastName.equalsIgnoreCase(person.getLastName())))
			getSession().save(personLastnameHistory);
	}

	public String getPreviousLastname(Person person)
	{
		Criteria criteria =  getSession().createCriteria(PersonLastnameHistory.class);
		
		if(person.getId() != null){
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON,person));	
		}
		criteria.addOrder(Order.desc("id"));
		PersonLastnameHistory personLastameHistory = new PersonLastnameHistory(); 
		if(!criteria.list().isEmpty()){
			if (criteria.list().size() > 1)
				personLastameHistory = (PersonLastnameHistory) criteria.list().get(1);
		}
		
		return personLastameHistory.getLastName();
	}
	
	public String getCurrentLastname(Person person)
	{
		Criteria criteria =  getSession().createCriteria(PersonLastnameHistory.class);
		
		if(person.getId() != null){
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON,person));	
		}
		criteria.addOrder(Order.desc("id"));
		PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory(); 
		if(!criteria.list().isEmpty()){
			personLastnameHistory = (PersonLastnameHistory) criteria.list().get(0);
		}
		
		return personLastnameHistory.getLastName();
	}

	public List<PersonLastnameHistory> getLastnameHistory(Person person)
	{
		Criteria criteria =  getSession().createCriteria(PersonLastnameHistory.class);
		
		if(person.getId() != null){
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON,person));	
		}
		
		return criteria.list();
	}
	
	public LinkSubjectStudy getSubject(Long id)  throws EntityNotFoundException{
		Criteria criteria =  getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("person.id",id));
		LinkSubjectStudy ls = (LinkSubjectStudy)criteria.uniqueResult();
		if(ls == null){
			throw new EntityNotFoundException("The Subject does not exist in the system");
		}
		//log.info(ls.getPerson().getFirstName());
		return ls;
		
	}
}