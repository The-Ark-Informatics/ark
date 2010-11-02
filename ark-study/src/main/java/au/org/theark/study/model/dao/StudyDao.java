package au.org.theark.study.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
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
	
	public List<Study> getStudy(Study study)
	{
		
		
		Criteria studyCriteria =  getSession().createCriteria(Study.class);
		
		if(study.getStudyKey() != null){
			studyCriteria.add(Restrictions.eq(Constants.STUDY_KEY,study.getStudyKey()));	
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
			log.error("A hibernate exception occured. Cannot update the study component ID: " + studyComponent.getStudyCompKey() + " Cause " + hibException.getStackTrace());
			throw new ArkSystemException("Cannot update Study component due to system error");
		}
	}
	
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria){
		
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		
		if(studyCompCriteria.getStudyCompKey() != null){
			criteria.add(Restrictions.eq(Constants.STUDY_COMP_KEY,studyCompCriteria.getStudyCompKey()));	
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
	
		Session session = getSession();
		Person person  = subjectVO.getPerson();
		//Add the person
		session.save(person);
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		linkSubjectStudy.setPerson(person);
		linkSubjectStudy.setStudy(subjectVO.getStudy());
		linkSubjectStudy.setSubjectStatus(subjectVO.getSubjectStatus());
		session.save(linkSubjectStudy);
		
	}
	
	public void updateSubject(SubjectVO subjectVO){
		
		try{
			
			Session session = getSession();
			
			//Updat the Person Details
			Person person  = subjectVO.getPerson();
			session.update(person);//Personal Details of Subject updated
			
			//Get the LinkSubjectStudy reference based on the id
			LinkSubjectStudy linkSubjectStudy = getLinkSubjectStudy(subjectVO.getLinkSubjectStudyId());	
			//Update this linkSubjectStudy instance with any details the user may have changed from front end
			linkSubjectStudy.setStudy(subjectVO.getStudy());
			//No need to set Person here since, there would not be a change to the actual person ID primary key
			linkSubjectStudy.setSubjectStatus(subjectVO.getSubjectStatus());
			
			//Update the instance
			session.update(linkSubjectStudy);
			
		}catch(EntityNotFoundException entityNotFound){
			log.error("The LinkSubjectStudy entity does not exist to update this subject " );
			//Throw an appropriate exception to the user
		}
		
	}
	
	public Collection<SubjectStatus> getSubjectStatus(){
		
		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();
	
	}
	
	/**
	 * Look up the Link Subject Study for subjects linked to a study
	 * @param subjectVO
	 * @return
	 */
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO){

		
		StringBuffer hqlString =	new StringBuffer();
		hqlString.append(" select linkSubStudy.person,linkSubStudy.subjectStatus, linkSubStudy.linkSubjectStudyKey, linkSubStudy.study");
		hqlString.append(" from LinkSubjectStudy as linkSubStudy ");
		hqlString.append(" where linkSubStudy.study.studyKey = ");
		hqlString.append( subjectVO.getStudy().getStudyKey());
		
		//TODO This should be the Subject UID or ID, this field will need to be in LinkSubjectStudy table
		if(subjectVO.getPerson().getPersonKey() != null){
			hqlString.append(" and linkSubStudy.person.personKey = ");
			hqlString.append( subjectVO.getPerson().getPersonKey());
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
			hqlString.append(" and linkSubStudy.subjectStatus.subjectStatusKey = ");
			hqlString.append(subjectVO.getSubjectStatus().getSubjectStatusKey());
		}
		
		Query query = getSession().createQuery(hqlString.toString());
		List<Object[]> list  = query.list();
		
		Collection<SubjectVO> subjectList = new ArrayList<SubjectVO>();
		if(list.size() > 0){
			log.info("Number of rows fetched " + list.size());
			
			for (Object[] objects : list) {
				if(objects.length > 0 && objects.length == 4){
					
					SubjectVO subject = new SubjectVO();
					Person person =(Person) objects[0];
					subject.setPerson(person);
					
					SubjectStatus status = (SubjectStatus) objects[1];
					subject.setSubjectStatus(status);
					
					subject.setLinkSubjectStudyId((Long)objects[2]);
					subjectList.add(subject);
					
					subject.setStudy((Study)objects[3]);
				}
				
			}
			log.info("Size : " + subjectList.size());
		}

		return subjectList;
	}
	
	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException{
		
		Criteria linkSubjectStudyCriteria =  getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("linkSubjectStudyKey",id));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if(listOfSubjects != null && listOfSubjects.size() > 0){
			return listOfSubjects.get(0);
		}else{
			throw new EntityNotFoundException("The entity with id" + id.toString() +" cannot be found.");
		}
	}
	
	
}
