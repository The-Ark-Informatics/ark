/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.Collection;
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
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 *
 */
@Repository("commonStudyDao")
public class StudyDao  extends HibernateSessionDao implements IStudyDao{

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

		StringBuffer hqlString =	new StringBuffer();
		hqlString.append(" select linkSubStudy.person,linkSubStudy.subjectStatus, linkSubStudy.id, linkSubStudy.study, linkSubStudy.subjectUID");
		hqlString.append(" from LinkSubjectStudy as linkSubStudy ");
		hqlString.append(" where linkSubStudy.study.id = ");
		hqlString.append( subjectVO.getStudy().getId());
		
		if(subjectVO.getPerson().getId() != null){
			hqlString.append(" and linkSubStudy.person.id = ");
			hqlString.append( subjectVO.getPerson().getId());
		}
		
		if(subjectVO.getSubjectUID() != null  && subjectVO.getSubjectUID().length() > 0){
			hqlString.append(" and linkSubStudy.subjectUID = ");
			hqlString.append("\'");
			hqlString.append( subjectVO.getSubjectUID());
			hqlString.append("\'");
		}
		
		if(subjectVO.getPerson().getFirstName() != null){
			hqlString.append(" and linkSubStudy.person.firstName = ");
			hqlString.append("\'");
			hqlString.append(subjectVO.getPerson().getFirstName().trim());
			hqlString.append("\'");
		}
		
		if(subjectVO.getPerson().getMiddleName() != null){
			hqlString.append(" and linkSubStudy.person.middleName = ");
			hqlString.append("\'");
			hqlString.append(subjectVO.getPerson().getMiddleName().trim());
			hqlString.append("\'");
		}
							
		if(subjectVO.getPerson().getLastName() != null){
			hqlString.append(" and linkSubStudy.person.lastName = ");
			hqlString.append("\'");
			hqlString.append(subjectVO.getPerson().getLastName().trim());
			hqlString.append("\'");
		}
		
		if(subjectVO.getPerson().getGenderType() != null){
			hqlString.append(" and linkSubStudy.person.genderType.id = ");
			hqlString.append(subjectVO.getPerson().getGenderType().getId());
		}
		
		if(subjectVO.getPerson().getVitalStatus() != null){
			hqlString.append(" and linkSubStudy.person.vitalStatus.id = ");
			hqlString.append(subjectVO.getPerson().getVitalStatus().getId());
		}
		
		if(subjectVO.getSubjectStatus() != null){
			hqlString.append(" and linkSubStudy.subjectStatus.id = ");
			hqlString.append(subjectVO.getSubjectStatus().getId());
		}
		
		Query query = getSession().createQuery(hqlString.toString());
		List<Object[]> list  = query.list();
		
		Collection<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();
		
		if(list.size() > 0){
			
			SubjectVO subject = null;
			
			log.info("Number of rows fetched " + list.size());
			//The Length is determined by the number of select columns specified in the hqlString above
			for (Object[] objects : list) {
				if(objects.length > 0 && objects.length == 5){
					
					subject = new SubjectVO();
					subject.setPerson((Person) objects[0]);
					subject.setSubjectStatus((SubjectStatus) objects[1]);
					subject.setLinkSubjectStudyId((Long)objects[2]);
					subject.setStudy((Study)objects[3]);
					subject.setSubjectUID((String)objects[4]);
					
//					for (Phone phone : subject.getPerson().getPhones()) {
//						PhoneType phoneType = phone.getPhoneType();
//						phoneType.setName(phoneType.getName());
//						phoneType.setDescription(phoneType.getDescription());
//						
//						phone.setPhoneType(phoneType);
//						subject.getPhoneList().add(phone);
//					}
					subjectVOList.add(subject);
				}
			}
			log.info("Size : " + subjectVOList.size());
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
	
	public List<CountryState>  getStates(Country country){
		//Testing this
		//TODO Remove this
		Country testCountry = getCountry(new Long("1"));
		StringBuffer hqlString =	new StringBuffer();
		hqlString.append(" from CountryState as cs ");
		hqlString.append(" where cs.country.id = ");
		hqlString.append( testCountry.getId());
		
		Query query = getSession().createQuery(hqlString.toString());
		List<CountryState> list =  (List<CountryState>) query.list();
		return list;
		
	}

}
