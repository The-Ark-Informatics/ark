package au.org.theark.gdmi.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import au.org.theark.gdmi.model.dao.ICollectionDao;
import au.org.theark.gdmi.model.dao.IGwasDao;
import au.org.theark.gdmi.model.entity.Collection;
import au.org.theark.gdmi.model.entity.CollectionImport;
import au.org.theark.gdmi.model.entity.DecodeMask;
import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.Marker;
import au.org.theark.gdmi.model.entity.MarkerGroup;
import au.org.theark.gdmi.model.entity.MarkerType;
import au.org.theark.gdmi.model.entity.MetaData;
import au.org.theark.gdmi.model.entity.MetaDataField;
import au.org.theark.gdmi.model.entity.MetaDataType;
import au.org.theark.gdmi.model.entity.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.io.IOUtils;
import org.hibernate.Hibernate;
import org.mortbay.io.EndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.batch.core.*;

@Transactional
@Service("gwasService")
public class GDMIService implements IGDMIService {

	final Logger log = LoggerFactory.getLogger(GDMIService.class);

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
    		String filePath = "/home/elam/testException.txt";
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
    		System.out.println("Something went horribly wrong with the file storage...\n" + ex);
    	}
    	return ed.getId();
	}
			
	
	public void getEncodedBit(Long encodedDataId) {
		EncodedData ed = getEncodedData(encodedDataId);
    	Blob aBlob = ed.getEncodedBit1();
    	try {
    		String outFilePath = "/home/elam/blahOut.txt";
    		FileOutputStream fos = new FileOutputStream(outFilePath);
    		
    		InputStream blobis = aBlob.getBinaryStream();
    		IOUtils.copy(blobis, fos);
    		fos.flush();
    		blobis.close();
    		fos.close();
    	} catch (SQLException sqlex) {
    		System.out.println("sqlex.State = " + sqlex.getSQLState() + "::" + sqlex.getErrorCode());    		
    	} catch (Exception ex) {
    		System.out.println("Something went horribly wrong with the file retrieval...\n" + ex);
    	}

//		EncodedData data = getEncodedData(encodedDataId);
//		Blob encodedDatabit = data.getEncodedBit1();
//		InputStream inputStream;
//		byte[] dataOfBytes;
//		try {
//			dataOfBytes = encodedDatabit.getBytes(1,5);
//			if(dataOfBytes != null){
//				System.out.println("Length = "  + dataOfBytes.length + " " + dataOfBytes.toString()); 
//				inputStream = encodedDatabit.getBinaryStream();
//				
//				System.out.println("input Stream= "  + inputStream.available());
//			}else{
//				System.out.println("Object is null" );
//			}
//		} catch (SQLException e) {
//			System.out.println("\n-- Exception occured" + e);// TODO Auto-generated catch block
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
	
}
