package org.wager.lims.biodata;

// Generated 20/10/2009 12:25:59 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Field.
 * 
 * @see org.wager.lims.biodata.Field
 * @author Hibernate Tools
 */

public class FieldHome {

	private static final Log log = LogFactory.getLog(FieldHome.class);

	private EntityManager entityManager;

	public FieldHome() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("labPersistence");
		entityManager = emf.createEntityManager();
	}

	public void persist(Field transientInstance) {
		log.debug("persisting Field instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Field persistentInstance) {
		log.debug("removing Field instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Field merge(Field detachedInstance) {
		log.debug("merging Field instance");
		try {
			Field result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Field findById(BigDecimal id) {
		log.debug("getting Field instance with id: " + id);
		try {
			Field instance = entityManager.find(Field.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
