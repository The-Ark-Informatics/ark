package au.org.theark.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
/**
 * A class to get a handle on the hibernate session object. This class must be
 * extended by classes that require access to a hibernate session.
 * @author nivedann
 *
 */
public abstract class HibernateSessionDao {
	
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Return a Hibernate Session instance through the sessionFactory
	 * @return
	 */
	public Session getSession(){
		return SessionFactoryUtils.getSession(this.sessionFactory, true);
	}
	

}
