package au.org.theark.study.model.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.Constants;

@Repository("studyDao")
public class StudyDao extends HibernateSessionDao implements IStudyDao {

	private static Logger log = LoggerFactory.getLogger(StudyDao.class);

	public void create(Study study) {
		getSession().save(study);
	}

	public List<StudyStatus> getListOfStudyStatus() {

		Example studyStatus = Example.create(new StudyStatus());
		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatus);
		return   studyStatusCriteria.list();
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
	
	public Study getStudy(Long id){
		Study study =  (Study)getSession().get(Study.class, id);
		return study;
	}
	
	public void updateStudy(Study studyEntity){
		getSession().update(studyEntity);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.study.model.dao.IStudyDao#create(au.org.theark.study.model.entity.StudyComp)
	 */
	public void create(StudyComp studyComponent) throws ArkSystemException {
		try{
			getSession().save(studyComponent);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot create the study component ID: " + studyComponent.getName() + " Cause " + hibException.getStackTrace());
			throw new ArkSystemException("Cannot create Study component");
		}
		
		
	}
	
	public void update(StudyComp studyComponent) throws ArkSystemException{
		try{
			getSession().update(studyComponent);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot update the study component ID: " + studyComponent.getId() + " Cause " + hibException.getStackTrace());
			throw new ArkSystemException("Cannot update Study component due to system error");
		}
	}
	
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria){
		
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		
		if(studyCompCriteria.getId() != null){
			criteria.add(Restrictions.eq(Constants.ID,studyCompCriteria.getId()));	
		}
		
		if(studyCompCriteria.getName() != null){
			criteria.add(Restrictions.eq(Constants.STUDY_COMP_NAME,studyCompCriteria.getName()));
		}
		
		if(studyCompCriteria.getKeyword() != null){
		
			criteria.add(Restrictions.ilike(Constants.STUDY_COMP_KEYWORD,studyCompCriteria.getKeyword()));
		}
		List<StudyComp> list =  criteria.list();
		return list;
	}
	
	public List<PhoneType> getListOfPhoneType() {
		Example phoneTypeExample = Example.create(new PhoneType());
		Criteria criteria = getSession().createCriteria(PhoneType.class).add(phoneTypeExample);
		return   criteria.list();
	}
	
	public void create(Phone phone) throws ArkSystemException{
		try{
			getSession().save(phone);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot create the Phone record. Cause: " + hibException.getStackTrace());
			throw new ArkSystemException("Unable to create a Phone record.");
		}
	}
	
	
	public void update(Phone phone) throws ArkSystemException{
		try{
			getSession().save(phone);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot update the Phone record. Cause: " + hibException.getStackTrace());
			throw new ArkSystemException("Unable to create a Phone record.");
		}
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
	
	public void createSubject(SubjectVO subjectVO){
		
		//Add Business Validations here as well apart from UI validation
		Session session = getSession();
		Person person  = subjectVO.getPerson();
		session.save(person); //Save the Person and associated Phones
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		linkSubjectStudy.setPerson(person);
		linkSubjectStudy.setStudy(subjectVO.getStudy());
		linkSubjectStudy.setSubjectStatus(subjectVO.getSubjectStatus());
		linkSubjectStudy.setSubjectUID(subjectVO.getSubjectUID());
		
		session.save(linkSubjectStudy);//The hibernate session is the same. This should be automatically bound with Spring's OpenSessionInViewFilter
		
	}
	
	
 
	public void updateSubject(SubjectVO subjectVO){
		
		try{
			Session session = getSession();
			Person person  = subjectVO.getPerson();
			session.update(person);//Update Person and associated Phones
			//Get the LinkSubjectStudy reference based on the id
			LinkSubjectStudy linkSubjectStudy = getLinkSubjectStudy(subjectVO.getLinkSubjectStudyId());	
			//Update this linkSubjectStudy instance with any details the user may have changed from front end
			linkSubjectStudy.setStudy(subjectVO.getStudy());
			//No need to set Person here since, there would not be a change to the actual person ID primary key
			linkSubjectStudy.setSubjectStatus(subjectVO.getSubjectStatus());
			linkSubjectStudy.setSubjectUID(subjectVO.getSubjectUID());
			//Update the instance
			session.update(linkSubjectStudy);
			
		}catch(EntityNotFoundException entityNotFound){
			log.error("The LinkSubjectStudy entity does not exist to update this subject " );
			//Throw an appropriate exception to the user
		}catch(Exception  ae){
			log.error("A System Exception occured while update of Subject " + ae.getStackTrace());
			//TODO throw ArkSystemException back to caller
		}
		
	}
	
	public Collection<SubjectStatus> getSubjectStatus(){
		
		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();
	
	}
	
	
	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException{
		
		Criteria linkSubjectStudyCriteria =  getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq(Constants.ID,id));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if(listOfSubjects != null && listOfSubjects.size() > 0){
			return listOfSubjects.get(0);
		}else{
			throw new EntityNotFoundException("The entity with id" + id.toString() +" cannot be found.");
		}
	}

	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can represent
	 * a subject or contact.
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException{
		
		Criteria personCriteria =  getSession().createCriteria(Person.class);
		personCriteria.add(Restrictions.eq(Constants.ID,personId));
		List<Person> listOfPerson = personCriteria.list();
		if(listOfPerson != null && listOfPerson.size() > 0){
			return listOfPerson.get(0);
		}else{
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");
		}
	}
	
	public List<Phone> getPersonPhoneList(Long personId) throws EntityNotFoundException, ArkSystemException{
		Criteria phoneCriteria =  getSession().createCriteria(Phone.class);
		phoneCriteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		List<Phone> personPhoneList = phoneCriteria.list();
		log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());
		if(personPhoneList == null && personPhoneList.size() == 0){
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");
		}
		log.info("Number of phone items retrieved for person Id " + personId + " " + personPhoneList.size());
		return personPhoneList;
	}
	
	public List<Phone> getPersonPhoneList(Long personId,Phone phone) throws EntityNotFoundException,ArkSystemException{
		
		Criteria phoneCriteria =  getSession().createCriteria(Phone.class);
	
		
		if(personId != null){
			phoneCriteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}
		
		if(phone != null){
			
			if( phone.getPhoneNumber() != null){
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_NUMBER, phone.getPhoneNumber()));
			}

			if( phone.getPhoneType() != null){
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_TYPE, phone.getPhoneType()));
			}
			
			if( phone.getAreaCode() != null){
				phoneCriteria.add(Restrictions.eq(Constants.AREA_CODE, phone.getAreaCode()));
			}
			
		}
		
		List<Phone> personPhoneList = phoneCriteria.list();
		log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());
		if(personPhoneList == null && personPhoneList.size() == 0){
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");
		}
		return personPhoneList;
	}
	
	/**
	 * Looks up all the addresses for a person.
	 * @param personId
	 * @param address
	 * @return List<Address>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Address> getPersonAddressList(Long personId, Address address) throws EntityNotFoundException,ArkSystemException{
		
		Criteria criteria =  getSession().createCriteria(Address.class);
		
		if(personId != null){
			criteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}
		
		if(address != null){
			//Add criteria for address
			if(address.getStreetAddress() != null){
				criteria.add(Restrictions.ilike(Constants.STREET_ADDRESS, address.getStreetAddress(),MatchMode.ANYWHERE));
			}
			
			if(address.getCountry() != null){
				criteria.add(Restrictions.eq(Constants.COUNTRY_NAME, address.getCountry()));
			}
			
			if(address.getPostCode() != null){
				criteria.add(Restrictions.eq(Constants.POST_CODE, address.getPostCode()));
			}
			
			if(address.getCity() != null){
				criteria.add(Restrictions.ilike(Constants.CITY, address.getCity()));
			}
			
			if(address.getCountryState() != null ){
				criteria.add(Restrictions.eq(Constants.COUNTRY_STATE_NAME, address.getCountryState()));
			}
			
			if(address.getAddressType() != null){
				criteria.add(Restrictions.eq(Constants.ADDRESS_TYPE, address.getAddressType()));
			}
		}
		
		List<Address> personAddressList = criteria.list();
		if(personAddressList == null && personAddressList.size() == 0){
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");		
		}
		return personAddressList;
	}
	
	
	
}
