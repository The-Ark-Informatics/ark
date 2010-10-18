/**
 * 
 */
package au.org.theark.gdmi.model.dao;

import java.util.Date;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.Log4jConfigListener;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.gdmi.exception.StorageIOException;
import au.org.theark.gdmi.model.entity.*;
import au.org.theark.gdmi.util.GWASImport;
import au.org.theark.gdmi.util.IMapStorage;
import au.org.theark.gdmi.util.IMarkerDao;

/**
 * @author elam
 *
 */
@Repository("markerDao")
public class MarkerDao extends HibernateSessionDao implements IMapStorage {

	private MarkerGroup markerGroup;
	private Marker marker;
	private String userId;
	private Date dateNow;
	
	static Logger log = LoggerFactory.getLogger(MarkerDao.class);
	
	public MarkerDao() {
		//Must exist for the @Autowired annotation
	}
	
	/**
	 * Called to setup the MarkerGroup id and user id for the storage 
	 * @param markerGroupId
	 * @param userId
	 */
	public void setup (MarkerGroup markerGroup, String userId) {
		this.markerGroup = markerGroup;
		this.marker = null;
		this.userId = userId;
		dateNow = new Date(System.currentTimeMillis());
		
	}
/*	
	public MarkerDao(MarkerGroup markerGroup, String userId) {
		this.markerGroup = markerGroup;
		this.marker = null;
		this.userId = userId;
		dateNow = new Date(System.currentTimeMillis());
	}
	*/
	
	/* (non-Javadoc)
	 * @see au.org.theark.gdmi.util.IMapStorage#init()
	 */
	public void init() throws StorageIOException {
		marker = new Marker();
		//getSession().save(markerGroup);
		marker.setMarkerGroup(markerGroup);
		marker.setUserId(userId);
		marker.setInsertTime(dateNow);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.gdmi.util.IMapStorage#setChromoNum(long)
	 */
	public void setChromosome(String chromosome) {
		marker.setChromosome(chromosome);

	}

	/* (non-Javadoc)
	 * @see au.org.theark.gdmi.util.IMapStorage#setMarkerName(java.lang.String)
	 */
	public void setMarkerName(String mkrName) {
		marker.setName(mkrName);

	}

	/* (non-Javadoc)
	 * @see au.org.theark.gdmi.util.IMapStorage#setGeneDist(long)
	 */
	public void setGeneDist(long geneDist) {
		/* ignored genetic distance because...
		* rlawrence does not believe it is used much, except in the
		* case of microsatellites (morgans will not be unique for   
		* SNPs that are close - i.e. they overlap).  Also, for this
		* reason it is not uncommon for this column to be zero.
		*/
	}

	/* (non-Javadoc)
	 * @see au.org.theark.gdmi.util.IMapStorage#setBpPos(long)
	 */
	public void setBpPos(long bpPos) {
		marker.setPosition(bpPos);

	}

	/* (non-Javadoc)
	 * @see au.org.theark.gdmi.util.IMapStorage#commit()
	 */
	public void commit() throws StorageIOException {
		try {
			getSession().save(marker);
		}
		catch (Exception ex) {
			log.error("commit Exception stacktrace: ", ex);
			throw new StorageIOException("Couldn't commit new marker record to database");
		}
		
	}

	public void flush() throws StorageIOException {
		try {
			getSession().flush();
		}
		catch (Exception ex) {
			log.error("flush Exception stacktrace: ", ex);
			throw new StorageIOException("Couldn't flush cached data to database");
		}
	}
}
