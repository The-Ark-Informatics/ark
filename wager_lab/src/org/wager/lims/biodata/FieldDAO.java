package org.wager.lims.biodata;

// Generated 20/10/2009 12:25:59 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Vector;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import neuragenix.common.Utilities;
import neuragenix.dao.DALQuery;
import neuragenix.dao.DBField;
import neuragenix.dao.DatabaseSchema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ecs.xml.XML;
import org.jasig.portal.services.LogService;

/**
 * Home object for domain model class Field.
 * 
 * @see org.wager.lims.biodata.Field
 * @author Hibernate Tools
 */

public class FieldDAO {

	private static final Log log = LogFactory.getLog(FieldDAO.class);

	private EntityManager entityManager;

	public FieldDAO() {
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
	
	 public XML buildLOVXML(Field f, String strValue, int intStudyKey) throws Exception
	   {
		 XML result = new XML("values");
	      boolean blIsInList = false;
	      
	      try
	      {
	         
	         String strLOVType = f.getLovtype();
	         if (strLOVType != null)
	         {
	            DALQuery query = new DALQuery();
	            query.setDomain("LOV", null, null, null);
	            Vector vtFields = DatabaseSchema.getFormFields("view_listofvalues");
	            query.setFields(vtFields, null);
	            query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0, DALQuery.WHERE_HAS_VALUE);
	            if (intStudyKey != -1){
	            	query.setWhere("AND", 1, "LOV_intStudyID", "=", ""+intStudyKey, 0, DALQuery.WHERE_HAS_VALUE);
	            	query.setWhere("OR", 0, "LOV_intStudyID", "=", "0", 1, DALQuery.WHERE_HAS_VALUE);
	            }
	            query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
	            query.setOrderBy("LOV_intLOVSortOrder", "ASC");
	            
	            ResultSet rsResultSet = query.executeSelect();
	            while (rsResultSet.next())
	            {
	               String strTempValue = rsResultSet.getString("LOV_strLOVValue");
	               
	               if (strTempValue == null)
	               {
	               }
	               XML value = new XML("value");
	               
	               
	               
	             if(strValue.equals(strTempValue))
	               {
	                 value.addXMLAttribute("selected", "1");
	                  blIsInList = true;
	               }
	              
	               
	               value.addElement(Utilities.cleanForXSL(strTempValue));
	           
	               result.addElement(value);
	            }
	            
	            
	            rsResultSet.close();
	            rsResultSet = null;
	            
	            if (blIsInList == false && strValue != null)
	            {
	               XML extraValue = new XML("value");
	               extraValue.addXMLAttribute("selected", "1");
	               extraValue.addElement(Utilities.cleanForXSL(strValue));
	               result.addElement(extraValue);
	             
	            }
	            
	         
	         }
	      }
	      catch (Exception e)
	      {
	         LogService.instance().log(LogService.ERROR, "Unknown error in FieldDAO - " + e.toString(), e);
	         throw new Exception("Unknown error in FieldDAO - " + e.toString());
	      }
	      
	      return result;
	   }
	
}
