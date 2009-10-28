package org.wager.lims.biodata;

// Generated 20/10/2009 12:25:59 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Group.
 * 
 * @see org.wager.lims.biodata.Group
 * @author Hibernate Tools
 */

public class GroupDAO {

	private static final Log log = LogFactory.getLog(GroupDAO.class);

	private EntityManager entityManager;

	public GroupDAO() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("labPersistence");
		entityManager = emf.createEntityManager();
	}

	public void persist(Group transientInstance) {
		log.debug("persisting Group instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Group persistentInstance) {
		log.debug("removing Group instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Group merge(Group detachedInstance) {
		log.debug("merging Group instance");
		try {
			Group result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Group findById(BigDecimal id) {
		log.debug("getting Group instance with id: " + id);
		try {
			Group instance = entityManager.find(Group.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findFieldsinGroupWithData(int biokey, Group g) {
		Query q = entityManager.createQuery("select f,d from Field f left join f.datas as d with d.biospecimenkey = :biokey where f in (:grouplist)");
		q.setParameter("biokey", new BigDecimal(biokey));
		List<Field> fields= g.getFields();
		log.debug("***** Field size: " +fields.size());
		q.setParameter("grouplist",fields);
	
		List<Object[]> objectList = q.getResultList();
		
		log.debug("Fields with/without data: " + objectList.size());
		return objectList;
		
	}
	
}
