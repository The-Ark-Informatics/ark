package au.org.theark.phenotypic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
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
	private Long studyId;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

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
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		// Newly created collections must start with a Created status
		Status status = phenotypicDao.getStatusByName(Constants.STATUS_CREATED);
		
		if (studyId == null)
		{
			log.error("StudyId was NULL, using 1 as default...");
			studyId = new Long(1);
		}
		
		col.setStatus(status);
		col.setStudyId(studyId);

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
		phenotypicDao.createCollectionImport(colImport);
	}

	public void updateCollection(Collection colEntity)
	{
		phenotypicDao.updateCollection(colEntity);
	}

	public void createField(Field field)
	{
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
	
	public java.util.Collection<Status> getStatus(){
		return phenotypicDao.getStatus();
	}

	public Field getField(Long fieldId)
	{
		return phenotypicDao.getField(fieldId);
	}

	public Collection getCollection(Long id)
	{
		return phenotypicDao.getPhenotypicCollection(id);
	}
	
	public void createFieldData(FieldData fieldData)
	{
		phenotypicDao.createFieldData(fieldData);
	}
	
	public java.util.Collection<String> validatePhenotypicDataFile()
	{
		java.util.Collection<String> validationMessages = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		Long collectionId = (Long) currentUser.getSession().getAttribute(Constants.COLLECTION_ID);
		Collection collection = null;
		
		if (collectionId == null){
			log.info("Using default collectionId of 1");
			collection = phenotypicDao.getPhenotypicCollection(new Long(1));
		}
		else{
			log.info("Using collectionId in context");
			collection = phenotypicDao.getPhenotypicCollection(collectionId);
		}
		
		try {
			log.info("phenotypicImport.collection: " + collection.getName());
		}
		catch (NullPointerException npe){
			log.error("Error with Collection...no object instatiated...");
		}
		
		PhenotypicImport pi = new PhenotypicImport(phenotypicDao, studyId, collection);
	
		try
		{
			File file = new File(Constants.TEST_FILE);
			InputStream is = new FileInputStream(file);
			
			log.info("Importing file");
			validationMessages = pi.validateMatrixPhenoFile(is, file.length());
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
		return validationMessages;
	}

	public void importPhenotypicDataFile()
	{
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		Long collectionId = (Long) currentUser.getSession().getAttribute(Constants.COLLECTION_ID);
		Collection collection = null;
		
		if (collectionId == null){
			log.info("Using default collectionId of 1");
			collection = phenotypicDao.getPhenotypicCollection(new Long(1));
		}
		else{
			log.info("Using collectionId in context");
			collection = phenotypicDao.getPhenotypicCollection(collectionId);
		}
		
		try {
			log.info("phenotypicImport.collection: " + collection.getName());
		}
		catch (NullPointerException npe){
			log.error("Error with Collection...no object instatiated...");
		}
		
		PhenotypicImport pi = new PhenotypicImport(phenotypicDao, studyId, collection);
	
		try
		{
			File file = new File(Constants.TEST_FILE);
			InputStream is = new FileInputStream(file);
			
			log.info("Importing file");
			pi.importMatrixPhenoFile(is, file.length());
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

	public java.util.Collection<FieldType> getFieldTypes()
	{
		return phenotypicDao.getFieldTypes();
	}

	public void deleteCollection(Collection collection)
	{
		phenotypicDao.createCollection(collection);
	}

	public void deleteCollectionImport(CollectionImport collectionImport)
	{
		phenotypicDao.deleteCollectionImport(collectionImport);
	}

	public void deleteField(Field field)
	{
		phenotypicDao.deleteField(field);
	}

	public void deleteFieldData(FieldData fieldData)
	{
		phenotypicDao.deleteFieldData(fieldData);		
	}

	public CollectionImport getCollectionImport(Long id)
	{
		return phenotypicDao.getCollectionImport(id);
	}

	public FieldType getFieldType(Long id)
	{
		return phenotypicDao.getFieldType(id);
	}

	public java.util.Collection<Field> searchField(Field field)
	{
		return phenotypicDao.searchField(field);
	}
	
	public java.util.Collection<Field> getFieldByStudyId(Long studyId)
	{
		return phenotypicDao.getFieldByStudyId(studyId);
	}

	public void updateCollectionImport(CollectionImport collectionImport)
	{
		phenotypicDao.updateCollectionImport(collectionImport);
	}

	public void updateField(Field field)
	{
		phenotypicDao.updateField(field);
	}

	public void updateFieldData(FieldData fieldData)
	{
		phenotypicDao.updateFieldData(fieldData);
	}

	public java.util.Collection<Collection> searchPhenotypicCollection(Collection phenotypicCollection)
	{
		return phenotypicDao.searchPhenotypicCollection(phenotypicCollection);
	}
}
