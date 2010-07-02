package au.org.theark.study.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.web.Constants;

@Repository(Constants.STUDY_DAO)
public class StudyDao extends HibernateSessionDao implements IStudyDao {

	public List<Study> getStudy(Study study)
	{
		
		Criteria studyCritera =  getSession().createCriteria(Study.class);
		
		if(study.getStudyKey() != null){
			studyCritera.add(Restrictions.eq("studyKey",study.getStudyKey()));	
		}
		
		if(study.getName() != null){
			studyCritera.add(Restrictions.ilike("name", study.getName(), MatchMode.ANYWHERE));	
		}
		
		if(study.getDateOfApplication() != null){
			studyCritera.add(Restrictions.eq("dateOfApplication", study.getDateOfApplication()));
		}
		
		if(study.getEstimatedYearOfCompletion() != null){
			studyCritera.add(Restrictions.eq("estimatedYearOfCompletion", study.getEstimatedYearOfCompletion()));
		}

		if(study.getChiefInvestigator() != null){
			studyCritera.add(Restrictions.ilike("chiefInvestigator", study.getChiefInvestigator(),MatchMode.ANYWHERE));
		}

		if(study.getContactPerson() != null){
			studyCritera.add(Restrictions.ilike("contactPerson", study.getContactPerson(), MatchMode.ANYWHERE));
		}

		studyCritera.addOrder(Order.asc("name"));
		List<Study> studyList  = studyCritera.list();
		return studyList;
	}

	public void create(Study study) {
		getSession().save(study);
	}
	
}
