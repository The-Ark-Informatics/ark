/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.geno.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
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

import au.org.theark.core.model.geno.entity.CollectionImport;
import au.org.theark.core.model.geno.entity.DelimiterType;
import au.org.theark.core.model.geno.entity.EncodedData;
import au.org.theark.core.model.geno.entity.FileFormat;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.Marker;
import au.org.theark.core.model.geno.entity.MarkerGroup;
import au.org.theark.core.model.geno.entity.MetaData;
import au.org.theark.core.model.geno.entity.MetaDataField;
import au.org.theark.core.model.geno.entity.MetaDataType;
import au.org.theark.core.model.geno.entity.Status;
import au.org.theark.core.model.geno.entity.UploadCollection;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.geno.exception.DataAcceptorIOException;
import au.org.theark.geno.model.dao.ICollectionDao;
import au.org.theark.geno.model.dao.IGwasDao;
import au.org.theark.geno.util.IMapDataAcceptor;
import au.org.theark.geno.util.IPedDataAcceptor;

@Transactional
@Service(Constants.GENO_SERVICE)
public class GenoService implements IGenoService {

	static final Logger log = LoggerFactory.getLogger(GenoService.class);

	private ICollectionDao collectionDao;
	private IGwasDao gwasDao;
	private IArkCommonService arkCommonService;

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

	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService)
	{
		this.arkCommonService = arkCommonService;
	}
	
	public IArkCommonService getArkCommonService()
	{
		return arkCommonService;
	}

	// Create
	public void createCollection(GenoCollection col) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");

		// Newly created collections must start with a Created status       
		Status status = collectionDao.getStatusByName(Constants.STATUS_CREATED);
		if (studyId == null)
			studyId = new Long(Constants.TEST_STUDY_ID);
		col.setStudy(arkCommonService.getStudy(studyId));
		col.setStatus(status);
		col.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		col.setInsertTime(dateNow);
        collectionDao.createCollection(col);
        
        AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Geno Collection " + col.getName());
		ah.setEntityId(col.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_COLLECTION);
		arkCommonService.createAuditHistory(ah);
	}

	public void createCollectionImport(CollectionImport colImport) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
		
		colImport.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		colImport.setInsertTime(dateNow);
		collectionDao.createCollectionImport(colImport);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Geno Collection Import " + colImport.getId());
		ah.setEntityId(colImport.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_COLLECTION_IMPORT);
		arkCommonService.createAuditHistory(ah);
	}
	
	public void createMetaData(MetaData metaData) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

		metaData.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
        metaData.setInsertTime(dateNow);
		collectionDao.createMetaData(metaData);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Geno MetaData " + metaData.getMetaDataField().getName());
		ah.setEntityId(metaData.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_METADATA);
		arkCommonService.createAuditHistory(ah);
	}

	public void createEncodedData(EncodedData ed) {
		gwasDao.createEncodedData(ed);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Geno EncodedData " + ed.getId());
		ah.setEntityId(ed.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_ENCODED_DATA);
		arkCommonService.createAuditHistory(ah);
	}

	public void createUploadCollection(UploadCollection uploadCollection) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

		uploadCollection.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		uploadCollection.setInsertTime(dateNow);
		uploadCollection.getUpload().setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		uploadCollection.getUpload().setInsertTime(dateNow);
		collectionDao.createUploadCollection(uploadCollection);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Geno Upload Collection " + uploadCollection.getId());
		ah.setEntityId(uploadCollection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_UPLOAD_COLLECTION);
		arkCommonService.createAuditHistory(ah);
	}

	
	// Read
	public Collection<GenoCollection> searchGenoCollection(
			GenoCollection genoCollectionCriteria) {
		return collectionDao.getCollectionMatches(genoCollectionCriteria);
	}

	public Collection<UploadCollection> searchUploadCollection(
			UploadCollection uploadCollectionCriteria) {
		return collectionDao.getFileUploadMatches(uploadCollectionCriteria);
	}

	public EncodedData getEncodedData(Long encodedDataId) {
		return gwasDao.getEncodedData(encodedDataId);
	}
		
	public void getEncodedBit(Long encodedDataId) {
		EncodedData ed = getEncodedData(encodedDataId);
    	Blob aBlob = ed.getEncodedBit1();
    	try {
    		String outFilePath = Constants.TEST_BLOB_OUTFILE;
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

	public void createMetaDataField(MetaDataField mdf) {
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

        mdf.setUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
        mdf.setInsertTime(dateNow);
		collectionDao.createMetaDataField(mdf);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Geno MetaDataField " + mdf.getName());
		ah.setEntityId(mdf.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_METADATA_FIELD);
		arkCommonService.createAuditHistory(ah);
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
	
	public GenoCollection getCollection(Long collectionId) {
		return collectionDao.getCollection(collectionId);
	}

	public Collection<Status> getStatusCollection() {
		return collectionDao.getStatusCollection();
	}

	public Collection<FileFormat> getFileFormatCollection() {
		return collectionDao.getFileFormatCollection();
	}
	
	public Collection<DelimiterType> getDelimiterTypeCollection() {
		return collectionDao.getDelimiterTypeCollection();
	}
	

	// Update
	public void updateCollection(GenoCollection colEntity) {
		Subject currentUser = SecurityUtils.getSubject();
        Date dateNow = new Date(System.currentTimeMillis());

        colEntity.setUpdateUserId(currentUser.getPrincipal().toString());	//use Shiro to get username
		colEntity.setUpdateTime(dateNow);		
		collectionDao.updateCollection(colEntity);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Geno Collection " + colEntity.getName());
		ah.setEntityId(colEntity.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_COLLECTION);
		arkCommonService.createAuditHistory(ah);
	}
	
	
	// Delete 
	public void deleteCollection(GenoCollection col) {
		collectionDao.deleteCollection(col);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Geno Collection " + col.getName());
		ah.setEntityId(col.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_COLLECTION);
		arkCommonService.createAuditHistory(ah);
	}
	
	public void deleteUploadCollection(UploadCollection uploadCollection) {
		collectionDao.deleteUploadCollection(uploadCollection);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Geno Upload Collection " + uploadCollection.getId());
		ah.setEntityId(uploadCollection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_GENO_UPLOAD_COLLECTION);
		arkCommonService.createAuditHistory(ah);
	}
	
	// Test
	public Long newEncodedData(GenoCollection col) {
    	EncodedData ed = new EncodedData();
    	try {
   		Subject currentUser = SecurityUtils.getSubject();
   		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");
   		Study study = arkCommonService.getStudy(studyId);
    		ed.setSubject(arkCommonService.getSubjectByUID(Constants.TEST_SUBJECT_UID, study));
    		ed.setCollection(col);
    		String filePath = Constants.TEST_BLOB_INFILE;
    		FileInputStream fis = new FileInputStream(filePath);
        	Blob blobbo = Hibernate.createBlob(fis);
	    	FileInputStream fis2 = new FileInputStream(filePath);
        	Blob blobbo2 = Hibernate.createBlob(fis2);
	    	ed.setEncodedBit1(blobbo);
	    	ed.setEncodedBit2(blobbo2);
	    	createEncodedData(ed);
	    	fis.close();
	    	fis2.close();
    	} catch (au.org.theark.core.exception.EntityNotFoundException enfe) {
			log.error("Error acquiring SubjectUID: " + enfe.getMessage());
    	} catch (Exception ex) {
    		log.error("Something went horribly wrong with the file storage...\n" + ex);
    	}
    	return ed.getId();
	}
	
	public void testGWASImport() {
		// This will only test that the database integration is working correctly
		// All transactions will be rolled back
		Subject currentUser = SecurityUtils.getSubject();
		String userId = currentUser.getPrincipal().toString();	//use Shiro to get username
		log.info("GWAS Import test started for user: " + userId);
		Date dateNow = new Date(System.currentTimeMillis());
		Long studyId = new Long(Constants.TEST_STUDY_ID); 
		
		/*
		MarkerType markerType = gwasDao.getMarkerType("SNP");
		MarkerGroup markerGroup = new MarkerGroup();
		markerGroup.setStudy(arkCommonService.getStudy(studyId));
		//markerGroup.setUploadId(new Long(1));
		markerGroup.setMarkerType(markerType);
		markerGroup.setUserId(userId);
		markerGroup.setInsertTime(dateNow);
		
		// if whichever is the correct IMapDataAcceptor
		// then pass this to GWASImport
		// assuming Database is the target...
		MapPipeToMarker mapPipeToMarker = this.new MapPipeToMarker();
		PedPipeToEncodedData pedPipeToED = this.new PedPipeToEncodedData();

		mapPipeToMarker.setup(markerGroup, userId);
		pedPipeToED.setup(markerGroup, userId);
		PedMapImport gi = new PedMapImport(mapPipeToMarker, pedPipeToED);

		try {
			File mapFile = new File(Constants.TEST_MAP_INFILE);
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
		*/
	}
	
	/** 
	 * This is an inner class that will accept the data from 
	 * PedMapImport.processMap 
	 */
	private class MapPipeToMarker implements IMapDataAcceptor {

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
			//getSession().save(markerGroup);	// not required when Hibernate cascading annotations work correctly
			
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
	
	/** 
	 * This is an inner class that will accept the data from 
	 * PedMapImport.processMap 
	 */
	private class PedPipeToEncodedData implements IPedDataAcceptor {
		
		private MarkerGroup markerGroup;
		private EncodedData encData;
		private String userId;
		private Date dateNow;
		
		public void setup(MarkerGroup markerGroup, String userId) {
			this.markerGroup = markerGroup;
			this.encData = null;
			this.userId = userId;
			dateNow = new Date(System.currentTimeMillis());
		}

		public void init() throws DataAcceptorIOException {
			// TODO Auto-generated method stub
			
		}

		public void setFamilyId(String familyId) {
			// TODO Auto-generated method stub
			
		}

		public void setFatherId(String fatherId) {
			// TODO Auto-generated method stub
			
		}

		public void setMotherId(String motherId) {
			// TODO Auto-generated method stub
			
		}

		public void setIndivId(String indivId) {
			// TODO Auto-generated method stub
			
		}

		public void setGender(String gender) {
			// TODO Auto-generated method stub
			
		}

		public void setPhenotype(String phenotype) {
			// TODO Auto-generated method stub
			
		}

		public void setMarkerName(String markerName) {
			// TODO Auto-generated method stub
			
		}

		public void setAllele1(String allele1) {
			// TODO Auto-generated method stub
			
		}

		public void setAllele2(String allele2) {
			// TODO Auto-generated method stub
			
		}

		public void sync() throws DataAcceptorIOException {
			// TODO Auto-generated method stub
			
		}

	}


}
