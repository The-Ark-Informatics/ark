package au.org.theark.study.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.web.Constants;

@Repository(Constants.STUDY_DAO)
public class StudyDao extends HibernateSessionDao implements IStudyDao {

	public List<Study> getStudy(Study study){
		
		Example example = Example.create(study);
		example.ignoreCase().enableLike();
		Criteria studyCritera =  getSession().createCriteria(Study.class).
												addOrder(Order.asc("studyKey")).
												addOrder(Order.asc("name" )).
												add(example);
		List<Study> studyList  = studyCritera.list();
		return studyList;
	}

	public void create(Study study) {
		getSession().save(study);
	}
	
}
