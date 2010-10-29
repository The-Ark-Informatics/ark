package au.org.theark.geno.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.io.IOUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.geno.exception.DataAcceptorIOException;
import au.org.theark.geno.exception.FileFormatException;
import au.org.theark.geno.exception.GenoSystemException;
import au.org.theark.geno.model.dao.ICollectionDao;
import au.org.theark.geno.model.dao.IGwasDao;
import au.org.theark.geno.model.entity.Collection;
import au.org.theark.geno.model.entity.CollectionImport;
import au.org.theark.geno.model.entity.EncodedData;
import au.org.theark.geno.model.entity.Marker;
import au.org.theark.geno.model.entity.MarkerGroup;
import au.org.theark.geno.model.entity.MarkerType;
import au.org.theark.geno.model.entity.MetaData;
import au.org.theark.geno.model.entity.MetaDataField;
import au.org.theark.geno.model.entity.MetaDataType;
import au.org.theark.geno.model.entity.Status;
import au.org.theark.geno.util.IMapDataAcceptor;
import au.org.theark.geno.util.PedMapImport;

@Transactional
@Service("genoService")
public class GenoService implements IGenoService {

	final Logger log = LoggerFactory.getLogger(GenoService.class);

	private ICollectionDao collectionDao;
	private IGwasDao gwasDao;

	@Autowired
	public void setCollectionDao(ICollectionDao collectionDao) {
		this.collectionDao = collectionDao;
	}

	public ICollectionDao getCollectionDao() {
		return collectionDao;
	}
	
	/*To access Hibernate Study Dao */
	@Autowired
	public void setGwasDao(IGwasDao gwasDao) {
		this.gwasDao = gwasDao;
	}

	public IGwasDao getGwasDao() {
		return gwasDao;
	}

	public void createCollection(Collection col) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");

		// Newly created collections must start with a Created status       
		Status status = collectionDao.getStatusByName(Constants.STATUS_CREATED);
		if (studyId == null)
			studyId = new Long(1);
		col.setName("New test");
		col.setStatus(status);
		col.setStudyId(studyId);
		col.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		col.setInsertTime(dateNow);
        collectionDao.createCollection(col);
	}

	public void createCollectionImport(CollectionImport colImport) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
		
		colImport.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		colImport.setInsertTime(dateNow);
		collectionDao.createCollectionImport(colImport);
	}
	
	public void createMetaData(MetaData metaData) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

		metaData.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
        metaData.setInsertTime(dateNow);
		collectionDao.createMetaData(metaData);
	}

	public void createEncodedData(EncodedData ed) {
		gwasDao.createEncodedData(ed);
	}
	
	public EncodedData getEncodedData(Long encodedDataId) {
		return gwasDao.getEncodedData(encodedDataId);
	}
	
	public Long newEncodedData(Collection col) {
    	EncodedData ed = new EncodedData();
    	ed.setSubjectId(new Long(0));
    	ed.setCollection(col);
    	try {
    		String filePath = "/home/ark/TestData/testException.txt";
    		FileInputStream fis = new FileInputStream(filePath);
        	Blob blobbo = Hibernate.createBlob(fis);
	    	FileInputStream fis2 = new FileInputStream(filePath);
        	Blob blobbo2 = Hibernate.createBlob(fis2);
	    	ed.setEncodedBit1(blobbo);
	    	ed.setEncodedBit2(blobbo2);
	    	createEncodedData(ed);
	    	fis.close();
	    	fis2.close();
    	} catch (Exception ex) {
    		log.error("Something went horribly wrong with the file storage...\n" + ex);
    	}
    	return ed.getId();
	}
			
	
	public void getEncodedBit(Long encodedDataId) {
		EncodedData ed = getEncodedData(encodedDataId);
    	Blob aBlob = ed.getEncodedBit1();
    	try {
    		String outFilePath = "/home/ark/TestData/blahOut.txt";
    		FileOutputStream fos = new FileOutputStream(outFilePath);
    		
    		InputStream blobis = aBlob.getBinaryStream();
    		IOUtils.copy(blobis, fos);
    		fos.flush();
    		blobis.close();
    		fos.close();
    	} catch (SQLException sqlex) {
    		log.error("sqlex.State = " + sqlex.getSQLState() + "::" + sqlex.getErrorCode());    		
    	} catch (Exception ex) {
    		log.error("Something went horribly wrong with the file retrieval...\n" + ex);
    	}

//		EncodedData data = getEncodedData(encodedDataId);
//		Blob encodedDatabit = data.getEncodedBit1();
//		InputStream inputStream;
//		byte[] dataOfBytes;
//		try {
//			dataOfBytes = encodedDatabit.getBytes(1,5);
//			if(dataOfBytes != null){
//				log.debug("Length = "  + dataOfBytes.length + " " + dataOfBytes.toString()); 
//				inputStream = encodedDatabit.getBinaryStream();
//				
//				log.debug("input Stream= "  + inputStream.available());
//			}else{
//				log.debug("Object is null" );
//			}
//		} catch (SQLException e) {
//			log.error("\n-- Exception occured" + e);// TODO Auto-generated catch block
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	}
	
	
	public void updateCollection(Collection colEntity) {
		Subject currentUser = SecurityUtils.getSubject();
        Date dateNow = new Date(System.currentTimeMillis());

        colEntity.setUpdateUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		colEntity.setUpdateTime(dateNow);		
		collectionDao.updateCollection(colEntity);
	}

	public void createMetaDataField(MetaDataField mdf) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

        mdf.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
        mdf.setInsertTime(dateNow);
		collectionDao.createMetaDataField(mdf);
	}

	public MetaDataType getMetaDataTypeByName(String typeName) {
		return collectionDao.getMetaDataTypeByName(typeName);
	}

	public Status getStatusByName(String statusName) {
		return collectionDao.getStatusByName(statusName);
	}

	public MetaDataField getMetaDataField(Long metaDataFieldId) {
		return collectionDao.getMetaDataField(metaDataFieldId);
	}
	
	public Collection getCollection(Long collectionId) {
		return collectionDao.getCollection(collectionId);
	}

	public void testGWASImport() {
		// This will only test that the database integration is working correctly
		// All transactions will be rolled back
		String userId = "test12345";
		Date dateNow = new Date(System.currentTimeMillis());

		MarkerType markerType = gwasDao.getMarkerType("SNP");
		MarkerGroup markerGroup = new MarkerGroup();
		markerGroup.setStudyId(new Long(1));
		markerGroup.setUploadId(new Long(1));
		markerGroup.setMarkerType(markerType);
		markerGroup.setUserId(userId);
		markerGroup.setInsertTime(dateNow);
		
		// if whichever is the correct IMapDataAcceptor
		// then pass this to GWASImport
		// assuming Database is the target...
		MapPipeToMaker mapPipeToMarker = this.new MapPipeToMaker();

		mapPipeToMarker.setup(markerGroup, userId);
		PedMapImport gi = new PedMapImport(mapPipeToMarker, null);

		try {
			File mapFile = new File("/home/ark/TestData/first100.map");
			InputStream is = new FileInputStream(mapFile);
			gi.processMap(is, mapFile.length());
			log.info("Succeessfully executed processMap.");
		}
		catch (IOException ioe) {
			log.error("Well something didn't go right. " + ioe);
		}
		catch (FileFormatException ffe) {
			log.error("Well something didn't go right. " + ffe);
		}
		catch (GenoSystemException gse) {
			log.error("Well something didn't go right. " + gse);
		}
		
	}
	
	/** 
	 * This is an inner class that will accept the data from 
	 * PedMapImport.processMap 
	 */
	private class MapPipeToMaker implements IMapDataAcceptor {

		private MarkerGroup markerGroup;
		private Marker marker;
		private String userId;
		private Date dateNow;
		
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
		
		/* (non-Javadoc)
		 * @see au.org.theark.geno.util.IMapDataAcceptor#init()
		 */
		public void init() throws DataAcceptorIOException {
			marker = new Marker();
			//getSession().save(markerGroup);
			
			marker.setMarkerGroup(markerGroup);
			marker.setUserId(userId);
			marker.setInsertTime(dateNow);
		}

		/* (non-Javadoc)
		 * @see au.org.theark.geno.util.IMapDataAcceptor#setChromoNum(long)
		 */
		public void setChromosome(String chromosome) {
			marker.setChromosome(chromosome);

		}

		/* (non-Javadoc)
		 * @see au.org.theark.geno.util.IMapDataAcceptor#setMarkerName(java.lang.String)
		 */
		public void setMarkerName(String mkrName) {
			marker.setName(mkrName);

		}

		/* (non-Javadoc)
		 * @see au.org.theark.geno.util.IMapDataAcceptor#setGeneDist(long)
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
		 * @see au.org.theark.geno.util.IMapDataAcceptor#setBpPos(long)
		 */
		public void setBpPos(long bpPos) {
			marker.setPosition(bpPos);
		}

		/* (non-Javadoc)
		 * @see au.org.theark.geno.util.IMapDataAcceptor#commit()
		 */
		public void sync() throws DataAcceptorIOException {
			try {
				gwasDao.createMarker(marker);
			}
			catch (Exception ex) {
				log.error("commit Exception stacktrace: ", ex);
				throw new DataAcceptorIOException("Couldn't commit new marker record to database");
			}
			
		}
	}
		
}
