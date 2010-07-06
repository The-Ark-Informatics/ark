package au.org.theark.study.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.web.Constants;

@Repository(Constants.STUDY_DAO)
public class StudyDao extends HibernateSessionDao implements IStudyDao {

	public List<Study> getStudy(Study study)
	{
		
		Criteria studyCriteria =  getSession().createCriteria(Study.class);
		
		if(study.getStudyKey() != null){
			studyCriteria.add(Restrictions.eq("studyKey",study.getStudyKey()));	
		}
		
		if(study.getName() != null){
			studyCriteria.add(Restrictions.ilike("name", study.getName(), MatchMode.ANYWHERE));	
		}
		
		if(study.getDateOfApplication() != null){
			studyCriteria.add(Restrictions.eq("dateOfApplication", study.getDateOfApplication()));
		}
		
		if(study.getEstimatedYearOfCompletion() != null){
			studyCriteria.add(Restrictions.eq("estimatedYearOfCompletion", study.getEstimatedYearOfCompletion()));
		}

		if(study.getChiefInvestigator() != null){
			studyCriteria.add(Restrictions.ilike("chiefInvestigator", study.getChiefInvestigator(),MatchMode.ANYWHERE));
		}

		if(study.getContactPerson() != null){
			studyCriteria.add(Restrictions.ilike("contactPerson", study.getContactPerson(), MatchMode.ANYWHERE));
		}
		
		if(study.getStudyStatus() != null){
			studyCriteria.add(Restrictions.eq("studyStatus", study.getStudyStatus()));
		}

		studyCriteria.addOrder(Order.asc("name"));
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
	
	
}
