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
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.StudySubjectVO;
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
		}
		
		criteria.addOrder(Order.asc("subjectUID"));
		List<LinkSubjectStudy> list = criteria.list();
		
		Collection<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			
			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			//Place the LinkSubjectStudy instance into a SubjectVO and add the SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setSubjectStudy(linkSubjectStudy);
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
		return criteria.list();
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

}
