/**
 * 
 */
package au.org.theark.phenotypic.model.dao;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.phenotypic.exception.StorageIOException;
import au.org.theark.phenotypic.model.entity.Field;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author cellis
 *
 */
@SuppressWarnings("unused")
@Repository("phenotypicStorage")
public class PhenotypicStorage extends HibernateSessionDao implements IPhenotypicStorage
{
	static Logger log = LoggerFactory.getLogger(PhenotypicStorage.class);
	private Field field;
	private String fieldName;
	private Date dateCollected;
	private String subjectId;
	private String userId;
	private Date dateNow;
	
	public PhenotypicStorage(){
		// Default constructor
	}
	
	/**
	 * Called to setup the field id and user id for the storage 
	 * @param field
	 * @param userId
	 */
	public void setup (Field field, String userId) {
		this.field = field;
		this.fieldName = null;
		this.userId = userId;
		dateNow = new Date(System.currentTimeMillis());
	}
	
	
	/* (non-Javadoc)
	 * @see au.org.theark.phenotypic.util.IPhenotypicStorage#init()
	 */
	public void init() throws StorageIOException
	{
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
		
		log.info("Called init in au.org.theark.phenotypic.model.dao.PhenotypicStorage");
		field = new Field();
		field.setName(fieldName);
		field.setStudyId(studyId);
		field.setUserId(userId);
		field.setInsertTime(dateNow);
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.phenotypic.util.IPhenotypicStorage#setFieldName(java.lang.String)
	 */
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.phenotypic.util.IPhenotypicStorage#setDateCollected(java.lang.String)
	 */
	public void setDateCollected(Date dateCollected)
	{
		this.dateCollected = dateCollected;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.phenotypic.util.IPhenotypicStorage#setSubjectId(java.lang.String)
	 */
	public void setSubjectId(String subjectId)
	{
		this.subjectId = subjectId;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.phenotypic.util.IPhenotypicStorage#commit()
	 */
	public void commit() throws StorageIOException
	{
		try {
			getSession().save(field);
		}
		catch (Exception ex) {
			log.error("commit Exception stacktrace: ", ex);
			throw new StorageIOException("Couldn't commit new field record to database");
		}
	}

}
