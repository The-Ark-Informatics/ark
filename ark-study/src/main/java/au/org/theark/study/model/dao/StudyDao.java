package au.org.theark.study.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
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
		
		studyCritera.add(Restrictions.isNotNull("studyKey"));
		studyCritera.add(Restrictions.eq("studyKey",study.getStudyKey()));
		studyCritera.add(Restrictions.ilike("name", study.getName(), MatchMode.ANYWHERE));
		
		//studyCritera.add(Restrictions.ilike("name",study.getName(),MatchMode.ANYWHERE));
		//studyCritera.addOrder(Order.asc("name"));
		List<Study> studyList  = studyCritera.list();
		return studyList;
	}

	public void create(Study study) {
		getSession().save(study);
	}
	
}
