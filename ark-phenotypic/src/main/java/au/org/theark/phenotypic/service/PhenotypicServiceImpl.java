package au.org.theark.phenotypic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.dao.IPhenotypicStorage;
import au.org.theark.phenotypic.model.dao.PhenotypicStorage;
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.util.PhenotypicImport;

@Transactional
@Service("phenotypicService")
@SuppressWarnings("unused")
public class PhenotypicServiceImpl implements IPhenotypicService
{

	final Logger					log	= LoggerFactory.getLogger(PhenotypicServiceImpl.class);

	private IPhenotypicDao		phenotypicDao;
	private IPhenotypicStorage	phenotypicStorage;

	@Autowired
	public void setPhenotypicDao(IPhenotypicDao phenotypicDao)
	{
		this.phenotypicDao = phenotypicDao;
	}

	public IPhenotypicDao getPhenotypicDao()
	{
		return phenotypicDao;
	}
	
	@Autowired
	public void setPhenotypicStorage(IPhenotypicStorage phenotypicStorage)
	{
		this.phenotypicStorage = phenotypicStorage;
	}

	public IPhenotypicStorage getPhenotypicStorage()
	{
		return phenotypicStorage;
	}

	public void createCollection(Collection col)
	{
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");

		// Newly created collections must start with a Created status
		Status status = phenotypicDao.getStatusByName(Constants.STATUS_CREATED);
		if (studyId == null)
			studyId = new Long(1);
		col.setName("New test");
		col.setStatus(status);
		col.setStudyId(studyId);
		col.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		col.setInsertTime(dateNow);
		phenotypicDao.createCollection(col);
	}

	public void createCollectionImport(CollectionImport colImport)
	{
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());
		Long studyId = (Long) currentUser.getSession().getAttribute("studyId");

		colImport.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		colImport.setInsertTime(dateNow);
		phenotypicDao.createCollectionImport(colImport);
	}

	public void updateCollection(Collection colEntity)
	{
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

		colEntity.setUpdateUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		colEntity.setUpdateTime(dateNow);
		phenotypicDao.updateCollection(colEntity);
	}

	public void createField(Field f)
	{
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

		f.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		f.setInsertTime(dateNow);
		
		phenotypicDao.createField(f);
	}

	public FieldType getFieldTypeByName(String fieldTypeName)
	{
		return phenotypicDao.getFieldTypeByName(fieldTypeName);
	}

	public Status getStatusByName(String statusName)
	{
		return phenotypicDao.getStatusByName(statusName);
	}

	public Field getField(Long fieldId)
	{
		return phenotypicDao.getField(fieldId);
	}

	public Collection getCollection(Long collectionId)
	{
		return phenotypicDao.getCollection(collectionId);
	}

	public void testPhenotypicImport()
	{
		String userId = "test12345";
		Date dateNow = new Date(System.currentTimeMillis());

		FieldType fieldType = phenotypicDao.getFieldTypeByName("NUMBER");
		Field field = new Field();
		field.setStudyId(new Long(1));
		field.setFieldType(fieldType);
		field.setUserId(userId);
		field.setInsertTime(dateNow);

		// if whichever is the correct IPhenotypicStorage
		// then pass this to PhenotypicImport
		// assuming Database is the target...
		PhenotypicImport pi = null;
		pi = new PhenotypicImport(phenotypicStorage);

		try
		{
			File file = new File("/home/ark/TestData/first100.map");
			InputStream is = new FileInputStream(file);
			pi.processFile(is, file.length());
		}
		catch (IOException ioe)
		{
			log.error("IOException: Well something didn't go right. " + ioe);
		}
		catch (FileFormatException ffe)
		{
			log.error("FileFormatException: Well something didn't go right. " + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error("PhenotypicSystemException: Well something didn't go right. " + pse);
		}
	}

	public void createFieldData(FieldData fieldData)
	{
		Subject currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());

		fieldData.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		fieldData.setInsertTime(dateNow);
		phenotypicDao.createFieldData(fieldData);
	}
}
