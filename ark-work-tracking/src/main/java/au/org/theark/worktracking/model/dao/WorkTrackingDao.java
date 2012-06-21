package au.org.theark.worktracking.model.dao;

import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;

@Repository("workTrackingDao")
public class WorkTrackingDao extends HibernateSessionDao implements
		IWorkTrackingDao {

}
