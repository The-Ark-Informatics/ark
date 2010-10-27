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
import au.org.theark.phenotypic.exception.FileTypeNotAvailableException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
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
	final Logger				log	= LoggerFactory.getLogger(PhenotypicServiceImpl.class);

	private IPhenotypicDao	phenotypicDao;
	private Subject currentUser;
	private Date dateNow;
	private Long studyId; 

	@Autowired
	public void setPhenotypicDao(IPhenotypicDao phenotypicDao)
	{
		this.phenotypicDao = phenotypicDao;
	}

	public IPhenotypicDao getPhenotypicDao()
	{
		return phenotypicDao;
	}

	/**
    * A Phenotypic collection is the data storage or grouping of a particular set set of data,
    * containing subjects with fields with field data values for a particular date collected 
    *
    * @param col  the collection object to be created
    */
	public void createCollection(Collection col)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		studyId = (Long) currentUser.getSession().getAttribute(Constants.STUDY_ID);

		// Newly created collections must start with a Created status
		Status status = phenotypicDao.getStatusByName(Constants.STATUS_CREATED);
		
		log.info("StudyId was NULL, using 1 as default...");
		if (studyId == null)
			studyId = new Long(1);
		
		col.setName("New test");
		col.setStatus(status);
		col.setStudyId(studyId);
		col.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		col.setInsertTime(dateNow);
		phenotypicDao.createCollection(col);
	}

	/**
    * A Phenotypic collection import is the job that runs to import the data into the database.
    * It contains relevant metadata about the import, such as start time and finish time
    *
    * @param colImport  the collection import object to be created
    */
	public void createCollectionImport(CollectionImport colImport)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		studyId = (Long) currentUser.getSession().getAttribute(Constants.STUDY_ID);

		colImport.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		colImport.setInsertTime(dateNow);
		phenotypicDao.createCollectionImport(colImport);
	}

	public void updateCollection(Collection colEntity)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		colEntity.setUpdateUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		colEntity.setUpdateTime(dateNow);
		phenotypicDao.updateCollection(colEntity);
	}

	public void createField(Field field)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		studyId = (Long) currentUser.getSession().getAttribute(Constants.STUDY_ID);

		field.setStudyId(studyId);
		field.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		field.setInsertTime(dateNow);

		phenotypicDao.createField(field);
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

	public Collection getCollection(Long id)
	{
		return phenotypicDao.getCollection(id);
	}

	public void testPhenotypicImport()
	{
		log.info("testPhenotypicImport called");
	}

	public void importPhenotypicDataFile()
	{
		Subject currentUser = SecurityUtils.getSubject();
		Long studyId = (Long) currentUser.getSession().getAttribute(Constants.STUDY_ID);
		Long collectionId = (Long) currentUser.getSession().getAttribute(Constants.COLLECTION_ID);
		// TODO: use collectionId from session
		log.info("Using default collectionId of 1...need to utilise selected collectionId");
		Collection collection = phenotypicDao.getCollection(new Long(1));

		PhenotypicImport pi = null;
		pi = new PhenotypicImport(phenotypicDao, studyId, collection);

		try
		{
			File file = new File(Constants.TEST_FILE);
			InputStream is = new FileInputStream(file);
			pi.processMatrixPhenoFile(is, file.length());
		}
		catch (IOException ioe)
		{
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
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
