package org.wager.lims.biodata;

// Generated 22/10/2009 4:41:14 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class IxBdCriteria.
 * @see org.wager.lims.biodata.Criteria
 * @author Hibernate Tools
 */

public class CriteriaDAO {

	private static final Log log = LogFactory.getLog(CriteriaDAO.class);


	private EntityManager entityManager;

	public CriteriaDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("labPersistence");
		entityManager = emf.createEntityManager();

	}
	
	public void persist(Criteria transientInstance) {
		log.debug("persisting IxBdCriteria instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Criteria persistentInstance) {
		log.debug("removing IxBdCriteria instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Criteria merge(Criteria detachedInstance) {
		log.debug("merging IxBdCriteria instance");
		try {
			Criteria result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Criteria findById(BigDecimal id) {
		log.debug("getting IxBdCriteria instance with id: " + id);
		try {
			Criteria instance = entityManager.find(Criteria.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
