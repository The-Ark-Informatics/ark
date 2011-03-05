package au.org.theark.study.model.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkStudySubstudy;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.Constants;

@Repository("studyDao")
public class StudyDao extends HibernateSessionDao implements IStudyDao {

	private static Logger log = LoggerFactory.getLogger(StudyDao.class);
	private Subject	currentUser;
	private Date		dateNow;

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
		// session.update and session.flush required as Blob read/writes are used, and InputStream may cause NullPointers when closed incorrectly
		Session session = getSession();
		session.update(studyEntity);
		session.flush();
		session.refresh(studyEntity);
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
		
			criteria.add(Restrictions.ilike(Constants.STUDY_COMP_KEYWORD,studyCompCriteria.getKeyword(),MatchMode.ANYWHERE));
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
			getSession().update(phone);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot update the Phone record. Cause: " + hibException.getStackTrace());
			throw new ArkSystemException("Unable to create a Phone record.");
		}
	}
	
	public void delete(Phone phone) throws ArkSystemException {
		try{
			getSession().delete(phone);			
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to delete this Phone record. Cause: " + someHibernateException.getStackTrace());
			throw new ArkSystemException("Unable to delete a Phone record.");
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
	
	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException{
			//Add Business Validations here as well apart from UI validation
		if(isSubjectUIDUnique(subjectVO.getSubjectStudy().getSubjectUID(),subjectVO.getSubjectStudy().getStudy().getId(), "Insert")){
			Session session = getSession();
			Person person  = subjectVO.getSubjectStudy().getPerson();
			session.save(person); 
			LinkSubjectStudy linkSubjectStudy = subjectVO.getSubjectStudy();
			session.save(linkSubjectStudy);//The hibernate session is the same. This should be automatically bound with Spring's OpenSessionInViewFilter
		}else{
			throw new ArkUniqueException("Subject UID must be unique");
		}
	}
	
	
 
	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException{
	
		if(true){			
			Session session = getSession();
			Person person  = subjectVO.getSubjectStudy().getPerson();
			session.update(person);//Update Person and associated Phones
			LinkSubjectStudy linkSubjectStudy = subjectVO.getSubjectStudy();
			session.update(linkSubjectStudy);
		}else{
			throw new ArkUniqueException("Subject UID must be unique");
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
	
	
	public void create(Address address) throws ArkSystemException{
		Session session = getSession();
		session.save(address);
	}
	
	public void update(Address address) throws ArkSystemException{
		Session session = getSession();
		session.update(address);
	}
	
	public void delete(Address address) throws ArkSystemException{
		
		getSession().delete(address);
	}
	
	
	public void create(Consent consent) throws ArkSystemException{
		try{
			Session session = getSession();
			session.save(consent);	

		}catch(HibernateException hibException){
			log.error("An exception occured while creating a consent " + hibException.getStackTrace());
			throw new ArkSystemException("Could not create the consent.");
		}
	}
	
	public void update(Consent consent) throws ArkSystemException,EntityNotFoundException{
		try{
			Session session = getSession();
			session.update(consent);	
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to update this consent " + someHibernateException.getStackTrace());
		}catch(Exception e){
			log.error("An Exception occured while trying to update this consent " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
		
	}
	
	/**
	 * If a consent is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * @param consent
	 * @throws ArkSystemException
	 */
	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException{
		try{
			Session session = getSession();
			consent = (Consent)session.get(Consent.class,consent.getId());	
			if(consent != null){
				getSession().delete(consent);	
			}else{
				throw new EntityNotFoundException("The Consent record you tried to remove does not exist in the Ark System");
			}
			
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to delete this consent " + someHibernateException.getStackTrace());
		}catch(Exception e){
			log.error("An Exception occured while trying to delete this consent " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}
	
	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException,ArkSystemException{
		
		Criteria criteria =  getSession().createCriteria(Consent.class);
		if(consent != null){
			
			criteria.add(Restrictions.eq("subject.id", consent.getSubject().getId()));
			criteria.add(Restrictions.eq("study.id", consent.getStudy().getId()));
			
			if(consent.getStudyComp() != null){
				criteria.add(Restrictions.eq("studyComp", consent.getStudyComp()));	
			}
			
			if(consent.getStudyComponentStatus() != null){
				criteria.add(Restrictions.eq("studyComponentStatus", consent.getStudyComponentStatus()));
			}
			
			if(consent.getConsentedBy() != null){
				criteria.add(Restrictions.ilike("consentedBy", consent.getConsentedBy(),MatchMode.ANYWHERE));
			}
			
			if(consent.getConsentStatus() != null){
				criteria.add(Restrictions.eq("consentStatus", consent.getConsentStatus()));
			}
		
			if(consent.getConsentDate() != null){
				criteria.add(Restrictions.eq("consentDate", consent.getConsentDate()));
			}
			
			if(consent.getConsentType() != null){
				criteria.add(Restrictions.eq("consentType", consent.getConsentType()));
			}
			
		}
		
		return criteria.list();
	}
	
	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException,ArkSystemException{
		
		Criteria criteria =  getSession().createCriteria(Consent.class);
		if(consentVO != null){
			
			criteria.add(Restrictions.eq("subject.id", consentVO.getConsent().getSubject().getId()));
			criteria.add(Restrictions.eq("study.id",  consentVO.getConsent().getStudy().getId()));
			
			if( consentVO.getConsent().getStudyComp() != null){
				criteria.add(Restrictions.eq("studyComp",  consentVO.getConsent().getStudyComp()));	
			}
			
			if( consentVO.getConsent().getStudyComponentStatus() != null){
				criteria.add(Restrictions.eq("studyComponentStatus",  consentVO.getConsent().getStudyComponentStatus()));
			}
			
			if( consentVO.getConsent().getConsentedBy() != null){
				criteria.add(Restrictions.ilike("consentedBy",  consentVO.getConsent().getConsentedBy(),MatchMode.ANYWHERE));
			}
			
			if( consentVO.getConsent().getConsentStatus() != null){
				criteria.add(Restrictions.eq("consentStatus",  consentVO.getConsent().getConsentStatus()));
			}
		
			if( consentVO.getConsent().getConsentDate() != null){
				criteria.add(Restrictions.between("consentDate",  consentVO.getConsent().getConsentDate(), consentVO.getConsentDateEnd()));
			}
			
			if( consentVO.getConsent().getConsentType() != null){
				criteria.add(Restrictions.eq("consentType",  consentVO.getConsent().getConsentType()));
			}
			
		}
		List<Consent> list = criteria.list();
		return list;
	}
	
	public List<SubjectCustmFld> searchStudyFields(SubjectCustmFld subjectCustmFld){
		
		Criteria criteria = getSession().createCriteria(SubjectCustmFld.class);
		if(subjectCustmFld != null){
			
		}
		List<SubjectCustmFld> list = criteria.list();
		return list;
	}
	
	public Consent getConsent(Long id) throws ArkSystemException {
		Consent consent = (Consent)getSession().get(Consent.class, id);
		return consent;
	}
	
	public void create(ConsentFile consentFile) throws ArkSystemException
	{
		Session session = getSession();
		
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		consentFile.setInsertTime(dateNow);
		consentFile.setUserId(currentUser.getPrincipal().toString());
		
		session.save(consentFile);
	}
	
	public void update(ConsentFile consentFile) throws ArkSystemException,EntityNotFoundException{
		Session session = getSession();
		
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		consentFile.setUserId(currentUser.getPrincipal().toString());
		consentFile.setUpdateTime(dateNow);
		
		if((ConsentFile)session.get(ConsentFile.class,consentFile.getId()) != null){
			session.update(consentFile);	
		}else{
			throw new EntityNotFoundException("The Consent file record you tried to update does not exist in the Ark System");
		}
		
	}
	
	/**
	 * If a consentFile is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * @param consentFile
	 * @throws ArkSystemException
	 */
	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException{
		try{
			Session session = getSession();
			consentFile = (ConsentFile)session.get(ConsentFile.class,consentFile.getId());	
			if(consentFile != null){
				getSession().delete(consentFile);	
			}else{
				throw new EntityNotFoundException("The Consent file record you tried to remove does not exist in the Ark System");
			}
			
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to delete this consent file " + someHibernateException.getStackTrace());
		}catch(Exception e){
			log.error("An Exception occured while trying to delete this consent file " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}
	
	public List<ConsentFile> searchConsentFile(ConsentFile consentFile)
			throws EntityNotFoundException, ArkSystemException {
		Criteria criteria =  getSession().createCriteria(ConsentFile.class);
		if(consentFile != null){
			
			if(consentFile.getId() != null){
				criteria.add(Restrictions.eq("id", consentFile.getId()));
			}
			
			if(consentFile.getConsent() != null){
				criteria.add(Restrictions.eq("consent", consentFile.getConsent()));
			}
			
			if(consentFile.getFilename() != null){
				criteria.add(Restrictions.ilike("fileName", consentFile.getFilename(),MatchMode.ANYWHERE));
			}
		}
		criteria.addOrder(Order.desc("id"));
		
		@SuppressWarnings("unchecked")
		List<ConsentFile> list = criteria.list();
		return list;
	}
	
	
	private boolean isSubjectUIDUnique(String subjectUID, Long studyId,String action){
		boolean isUnique = true;
		Session session = getSession();
		Criteria criteria =  session.createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("subjectUID", subjectUID));
		criteria.add(Restrictions.eq("study.id", studyId));
		if(action.equalsIgnoreCase(au.org.theark.core.Constants.ACTION_INSERT)){
			if (criteria.list().size() > 0){
				isUnique = false;
			}
		}else if(action.equalsIgnoreCase(au.org.theark.core.Constants.ACTION_UPDATE)){
			if (criteria.list().size() > 1 ){
				isUnique = false;
			}
		}
		return isUnique;
	}

}
