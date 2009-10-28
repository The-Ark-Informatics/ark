package org.wager.lims.biodata;

// Generated 20/10/2009 12:25:59 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;

import javax.naming.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transaction;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Data.
 * @see org.wager.lims.biodata.Data
 * @author Hibernate Tools
 */

public class DataDAO {

	private static final Log log = LogFactory.getLog(DataDAO.class);

	
	private EntityManager entityManager;

	public DataDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("labPersistence");
		entityManager = emf.createEntityManager();

	}
	

	public void persist(Data transientInstance) {
		log.debug("persisting Data instance");
		try {
			EntityTransaction tx = entityManager.getTransaction();
			tx.begin();
			entityManager.persist(transientInstance);
			tx.commit();
			log.debug("persist successful");
			
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Data persistentInstance) {
		log.debug("removing Data instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Data merge(Data detachedInstance) {
		log.debug("merging Data instance");
		try {
			Data result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Data findById(BigDecimal id) {
		log.debug("getting Data instance with id: " + id);
		try {
			Data instance = entityManager.find(Data.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Data> findAllData(int biospecimenkey) {
		log.debug("getting Data instance with biokey: " + biospecimenkey);
		try {
			Query q = entityManager.createQuery("select d from Data d where d.biospecimenkey = :biokey"  );
			BigDecimal b = new BigDecimal(biospecimenkey);
			q.setParameter("biokey", b);
			List<Data> results = (List<Data>) q.getResultList();
			log.debug("get successful");
			return results;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
			
		}
		
	}
	
	public void findFieldsAndData() {
		
	}
	
}
