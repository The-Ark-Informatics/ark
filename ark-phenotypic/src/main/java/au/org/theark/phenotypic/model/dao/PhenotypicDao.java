package au.org.theark.phenotypic.model.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldDataLog;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.entity.Upload;
import au.org.theark.phenotypic.model.entity.UploadCollection;

@SuppressWarnings("unchecked")
@Repository("phenotypicDao")
public class PhenotypicDao extends HibernateSessionDao implements IPhenotypicDao
{
	static Logger		log	= LoggerFactory.getLogger(PhenotypicDao.class);
	private Subject	currentUser;
	private Date		dateNow;
	
	public java.util.Collection<PhenoCollection> getPhenotypicCollection()
	{
		Criteria crit = getSession().createCriteria(PhenoCollection.class);
		java.util.List<PhenoCollection> collectionList = crit.list();
		return collectionList;
	}

	public java.util.Collection<PhenoCollection> searchPhenotypicCollection(PhenoCollection collectionToMatch)
	{
		Criteria collectionCriteria = getSession().createCriteria(PhenoCollection.class);

		if (collectionToMatch.getId() != null)
		{
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_ID, collectionToMatch.getId()));
		}

		if (collectionToMatch.getName() != null)
		{
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_NAME, collectionToMatch.getName(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStudy() != null)
		{
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_STUDY, collectionToMatch.getStudy()));
		}

		if (collectionToMatch.getInsertTime() != null)
		{
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_INSERT_TIME, collectionToMatch.getInsertTime()));
		}

		if (collectionToMatch.getUserId() != null)
		{
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_USER_ID, collectionToMatch.getUserId(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getUpdateTime() != null)
		{
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_UPDATE_TIME, collectionToMatch.getUpdateTime()));
		}

		if (collectionToMatch.getUpdateUserId() != null)
		{
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_UPDATE_USER_ID, collectionToMatch.getUpdateUserId(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStatus() != null)
		{
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.STATUS, collectionToMatch.getStatus()));
		}

		collectionCriteria.addOrder(Order.asc("name"));
		java.util.Collection<PhenoCollection> phenotypicCollectionCollection = collectionCriteria.list();
		return phenotypicCollectionCollection;
	}

	public PhenoCollection getPhenotypicCollection(Long id)
	{
		PhenoCollection collection = (PhenoCollection) getSession().get(PhenoCollection.class, id);
		return collection;
	}

	public void createPhenoCollection(PhenoCollection collection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collection.setInsertTime(dateNow);
		collection.setUserId(currentUser.getPrincipal().toString());

		getSession().save(collection);
	}

	public void updatePhenoCollection(PhenoCollection collection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collection.setUserId(currentUser.getPrincipal().toString());
		collection.setUpdateTime(dateNow);

		getSession().update(collection);
	}

	public void deletePhenoCollection(PhenoCollection collection)
	{
		getSession().delete(collection);
	}

	public void createCollectionImport(CollectionImport collectionImport)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collectionImport.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		collectionImport.setInsertTime(dateNow);

		getSession().save(collectionImport);
	}

	public void updateCollectionImport(CollectionImport collectionImport)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collectionImport.setUserId(currentUser.getPrincipal().toString());
		collectionImport.setUpdateTime(dateNow);

		getSession().update(collectionImport);
	}

	public void deleteCollectionImport(CollectionImport collectionImport)
	{
		getSession().delete(collectionImport);
	}

	public Field getField(Long id)
	{
		Field field = (Field) getSession().get(Field.class, id);
		return field;
	}

	public Field getFieldByNameAndStudy(String fieldName, Study study)
	{
		Field field = new Field();
		field.setStudy(study);
		field.setName(fieldName);
		Example example = Example.create(field);
		Criteria criteria = getSession().createCriteria(Field.class).add(example);
		if (criteria != null && criteria.list() != null && criteria.list().size() > 0)
		{
			return (Field) criteria.list().get(0);
		}
		else
		{
			log.error("No field returned...");
			return null;
		}
	}

	public void createField(Field field)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		field.setUserId(currentUser.getPrincipal().toString());
		field.setInsertTime(dateNow);
		
		// Format the Field name before creating
		field.setName(formatFieldName(field.getName()));

		getSession().save(field);
	}

	public void updateField(Field field)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		field.setUpdateUserId(currentUser.getPrincipal().toString());
		field.setUpdateTime(dateNow);
		
		// Format the Field name before updating
		field.setName(formatFieldName(field.getName()));

		getSession().update(field);
	}

	public void deleteField(Field field)
	{
		getSession().delete(field);
	}
	
	private String formatFieldName(String fieldName){
		// Uppercase the Fieldname
		fieldName = fieldName.toUpperCase();
		
		// Replace all spaces with underscores
		fieldName = StringUtils.replace(fieldName, " ", "_");
		
		// Replace all spaces with underscores
		fieldName = StringUtils.replace(fieldName, " ", "_");
		
		return fieldName;
	}

	public FieldType getFieldTypeByName(String fieldTypeName)
	{
		FieldType fieldType = new FieldType();
		Criteria criteria = getSession().createCriteria(FieldType.class);
		criteria.add(Restrictions.eq("name", fieldTypeName));

		if (criteria != null && criteria.list() != null && criteria.list().size() > 0)
		{
			fieldType = (FieldType) criteria.list().get(0);
		}
		else
		{
			log.error("Field Type Table maybe out of synch. Please check if it has an entry for " + fieldTypeName + " status");
			log.error("Cannot locate a study status with " + fieldTypeName + " in the database");
		}
		return fieldType;
	}

	public void createFieldType(FieldType fieldType)
	{
		getSession().save(fieldType);
	}

	public void updateFieldType(FieldType fieldType)
	{
		getSession().update(fieldType);
	}

	public FieldData getFieldData(Long id)
	{
		FieldData fieldData = (FieldData) getSession().get(FieldData.class, id);
		return fieldData;
	}
	
	public FieldData getFieldDataByName(String fieldName)
	{
		log.info("PhenotypicDao.getFieldDataByName: " + fieldName);
		return null;
	}

	public Collection<FieldData> getFieldDataByCollectionAndField(PhenoCollection phenoCollection, Field field)
	{
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (phenoCollection.getId() != null){
			 criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION_ID,phenoCollection.getId()));
		}
		if (field.getId() != null){
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD_ID,field.getId()));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}
	
	public FieldData getFieldData(PhenoCollection phenoCollection, LinkSubjectStudy linkSubjectStudy, Field field, Date dateCollected, String value)
	{
		Criteria criteria = getSession().createCriteria(CollectionImport.class);

		if (phenoCollection != null){
			 criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_PHENO_COLLECTION,phenoCollection));
		}

		if (linkSubjectStudy!= null){
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DATA_LINK_SUBJECT_STUDY,linkSubjectStudy));
		}
		
		if (field != null){
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD,field));
		}
		
		if (dateCollected != null){
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DATA_COLLECTED,dateCollected));
		}

		FieldData fieldData = new FieldData();
		if (criteria != null && criteria.list() != null && criteria.list().size() > 0)
		{
			fieldData = (FieldData) criteria.list().get(0);
		}
		else
		{
			log.error("Field Data table maybe out of synch. Please check if it has an entry for a fieldData with id:" + fieldData.getId());
		}
		return fieldData;
	}

	public void createFieldData(FieldData fieldData)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		fieldData.setUserId(currentUser.getPrincipal().toString());
		fieldData.setInsertTime(dateNow);
		
		// TODO Add fieldDataLog on Insert
//		FieldDataLog fieldDataLog = new FieldDataLog();
//		fieldDataLog.setFieldData(fieldData);
//		fieldDataLog.setValue(fieldData.getValue());
//		fieldDataLog.setComment("Insert");
//		fieldDataLog.setUserId(currentUser.getPrincipal().toString());
//		fieldDataLog.setInsertTime(dateNow);
//		
//		fieldData.getFieldDataLogs().add(fieldDataLog);
		getSession().save(fieldData);
	}

	public void updateFieldData(FieldData fieldData)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		fieldData.setUpdateTime(dateNow);
		fieldData.setUpdateUserId(currentUser.getPrincipal().toString());
		
		// TODO Add fieldDataLog on Update
//		FieldDataLog fieldDataLog = new FieldDataLog();
//		fieldDataLog.setFieldData(fieldData);
//		fieldDataLog.setValue(fieldData.getValue());
//		fieldDataLog.setComment("Update");
//		fieldDataLog.setUserId(currentUser.getPrincipal().toString());
//		fieldDataLog.setInsertTime(dateNow);
//		
//		fieldData.getFieldDataLogs().add(fieldDataLog);
		
		getSession().update(fieldData);
	}

	public void deleteFieldData(FieldData fieldData)
	{
		// TODO Add fieldDataLog on Insert
//		FieldDataLog fieldDataLog = new FieldDataLog();
//		fieldDataLog.setFieldData(fieldData);
//		fieldDataLog.setValue(fieldData.getValue());
//		fieldDataLog.setComment("Delete");
//		fieldDataLog.setUserId(currentUser.getPrincipal().toString());
//		fieldDataLog.setInsertTime(dateNow);
//		fieldData.getFieldDataLogs().add(fieldDataLog);
		
		getSession().delete(fieldData);
	}

	public Status getStatus(Long statusId)
	{
		Criteria crit = getSession().createCriteria(Status.class);
		crit.add(Restrictions.eq("id", statusId));

		List<Status> statusList = crit.list();
		if (statusList.size() > 0)
		{
			if (statusList.size() > 1)
			{
				log.error("Backend database has non-unique Status names, returned the first one");
			}
			return (statusList.get(0));
		}
		else
			return null;
	}

	public Status getStatusByName(String statusName)
	{
		Criteria crit = getSession().createCriteria(Status.class);
		crit.add(Restrictions.eq("name", statusName));
		crit.addOrder(Order.asc("id"));
		List<Status> statusList = crit.list();
		if (statusList.size() > 0)
		{
			if (statusList.size() > 1)
			{
				log.error("Backend database has non-unique Status names, returned the first one");
			}
			return (statusList.get(0));
		}
		else
			return null;
	}
	
	public java.util.Collection<Status> getStatus()
	{
		Criteria crit = getSession().createCriteria(Status.class);
		java.util.Collection<Status> statusCollection = crit.list();
		return (statusCollection);
	}

	public void createStatus(Status status)
	{
		getSession().save(status);
	}

	public void updateStatus(Status status)
	{
		getSession().update(status);
	}

	public void createUpload(Upload upload)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		upload.setInsertTime(dateNow);
		upload.setUserId(currentUser.getPrincipal().toString());

		getSession().save(upload);
	}
	
	public void updateUpload(Upload upload)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		upload.setUpdateTime(dateNow);
		upload.setUpdateUserId(currentUser.getPrincipal().toString());

		getSession().update(upload);
	}
	
	public void deleteUpload(Upload upload)
	{
		getSession().delete(upload);
	}

	public java.util.Collection<FieldType> getFieldTypes()
	{
		Criteria crit = getSession().createCriteria(FieldType.class);
		java.util.Collection<FieldType> fieldTypeCollection = crit.list();
		return fieldTypeCollection;
	}

	public FieldType getFieldType(Long id)
	{
		FieldType fieldType = (FieldType) getSession().get(FieldType.class, id);
		return fieldType;
	}

	public java.util.Collection<Field> searchField(Field field)
	{
		java.util.Collection<Field> fieldCollection = null;
		
		Criteria criteria = getSession().createCriteria(Field.class);

		if (field.getId() != null){
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_ID, field.getId()));
		}

		if (field.getName() != null){
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_NAME, field.getName()));
		}
		
		if(field.getStudy() != null){
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_STUDY, field.getStudy()));
		}

		if (field.getFieldType() != null){
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_FIELD_TYPE, field.getFieldType()));
		}

		if (field.getDescription() != null){
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DESCRIPTION, field.getDescription(), MatchMode.ANYWHERE));
		}
		
		if (field.getUnits() != null){
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_UNITS, field.getUnits(), MatchMode.ANYWHERE));
		}
		
		if (field.getMinValue() != null){
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_MIN_VALUE, field.getMinValue(), MatchMode.ANYWHERE));
		}
		
		if (field.getMaxValue() != null){
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_MAX_VALUE, field.getMaxValue(), MatchMode.ANYWHERE));
		}
		
		fieldCollection = criteria.list();
		
		return fieldCollection;
	}

	public java.util.Collection<Field> getField()
	{
		Criteria criteria = getSession().createCriteria(Field.class);
		java.util.Collection<Field> fieldCollection = criteria.list();
		return fieldCollection;
	}
	
	public CollectionImport getCollectionImport(Long id)
	{
		CollectionImport collectionImport = (CollectionImport) getSession().get(CollectionImport.class, id);
		return collectionImport;
	}

	public java.util.Collection<CollectionImport> getCollectionImport()
	{
		Criteria criteria = getSession().createCriteria(CollectionImport.class);
		java.util.Collection<CollectionImport> collectionImportCollection = criteria.list();
		return collectionImportCollection;
	}

	public java.util.Collection<CollectionImport> searchCollectionImport(CollectionImport collectionImportToMatch)
	{
		Criteria criteria = getSession().createCriteria(CollectionImport.class);

		if (collectionImportToMatch.getId() != null)
		{
			 criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.COLLECTION_IMPORT_ID,collectionImportToMatch.getId()));
		}

		if (collectionImportToMatch.getCollection() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.COLLECTION_IMPORT_COLLECTION,collectionImportToMatch.getCollection()));
		}
		
		if (collectionImportToMatch.getImportType() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.COLLECTION_IMPORT_TYPE,collectionImportToMatch.getImportType()));
		}

		java.util.Collection<CollectionImport> collectionImportCollection = criteria.list();
		return collectionImportCollection;
	}

	public FieldDataLog getFieldDataLog(Long id)
	{
		FieldDataLog fieldDataLog = (FieldDataLog) getSession().get(FieldDataLog.class, id);
		return fieldDataLog;
	}

	public Collection<FieldDataLog> getFieldDataLogByField(Field field)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public void createFieldDataLog(FieldDataLog fieldDataLog)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		fieldDataLog.setInsertTime(dateNow);
		fieldDataLog.setUserId(currentUser.getPrincipal().toString());
		getSession().save(fieldDataLog);
	}

	public Upload getUpload(Long id)
	{
		Upload upload = (Upload) getSession().get(Upload.class, id);
		return upload;
 	}

	public Collection<Upload> getUploadByFileName(String fileName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public UploadCollection getUploadCollection(Long id)
	{
		UploadCollection uploadCollection = (UploadCollection) getSession().get(UploadCollection.class, id);
		return uploadCollection;
	}

	public Collection<UploadCollection> getUploadCollectionByCollection(PhenoCollection phenoCollection)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public void createUploadCollection(UploadCollection uploadCollection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		uploadCollection.setInsertTime(dateNow);
		uploadCollection.setUserId(currentUser.getPrincipal().toString());

		getSession().save(uploadCollection);
	}

	public void updateUploadCollection(UploadCollection uploadCollection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		uploadCollection.setUpdateTime(dateNow);
		uploadCollection.setUpdateUserId(currentUser.getPrincipal().toString());

		getSession().update(uploadCollection);
	}
	
	public void deleteUploadCollection(UploadCollection uploadCollection)
	{
		getSession().delete(uploadCollection);
	}
}