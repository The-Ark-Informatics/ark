package au.org.theark.phenotypic.model.dao;

import java.util.Date;
import java.util.List;

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
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.entity.Upload;
import au.org.theark.phenotypic.model.entity.UploadCollection;

@SuppressWarnings("unchecked")
@Repository("phenotypicDao")
public class PhenotypicDao extends HibernateSessionDao implements IPhenotypicDao
{
	static Logger	log	= LoggerFactory.getLogger(PhenotypicDao.class);
	private Subject currentUser;
	private Date dateNow;

	public List<Collection> getCollectionMatches(Collection collectionToMatch)
	{
		Criteria collectionCriteria = getSession().createCriteria(Collection.class);

		if (collectionToMatch.getId() != null)
		{
			collectionCriteria.add(Restrictions.eq("id", collectionToMatch.getId()));
		}

		if (collectionToMatch.getName() != null)
		{
			collectionCriteria.add(Restrictions.ilike("name", collectionToMatch.getName(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStudyId() != null)
		{
			collectionCriteria.add(Restrictions.eq("studyId", collectionToMatch.getStudyId()));
		}

		if (collectionToMatch.getInsertTime() != null)
		{
			collectionCriteria.add(Restrictions.eq("insertTime", collectionToMatch.getInsertTime()));
		}

		if (collectionToMatch.getUserId() != null)
		{
			collectionCriteria.add(Restrictions.ilike("userId", collectionToMatch.getUserId(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getUpdateTime() != null)
		{
			collectionCriteria.add(Restrictions.eq("updateTime", collectionToMatch.getUpdateTime()));
		}

		if (collectionToMatch.getUpdateUserId() != null)
		{
			collectionCriteria.add(Restrictions.ilike("updateUserId", collectionToMatch.getUpdateUserId(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStatus() != null)
		{
			collectionCriteria.add(Restrictions.eq("status", collectionToMatch.getStatus()));
		}

		collectionCriteria.addOrder(Order.asc("name"));
		List<Collection> collectionList = collectionCriteria.list();
		return collectionList;
	}

	public Collection getCollection(Long id)
	{
		Collection col = (Collection) getSession().get(Collection.class, id);
		return col;
	}

	public void createCollection(Collection collection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		
		collection.setInsertTime(dateNow);
		collection.setUserId(currentUser.getPrincipal().toString());
		
		getSession().save(collection);
	}

	public void updateCollection(Collection collection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		
		collection.setUserId(currentUser.getPrincipal().toString());
		collection.setUpdateTime(dateNow);
		
		getSession().update(collection);
	}
	
	public void deleteCollection(Collection collection)
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
	
	public Field getFieldByName(Long studyId, String fieldName)
	{
		log.info("PhenotypicDao.getFieldName(studyId, fieldName): " + studyId + "\t" + fieldName);
		
		Field field = new Field();
		field.setStudyId(studyId);
		field.setName(fieldName);
		Example example = Example.create(field);
		Criteria criteria = getSession().createCriteria(Field.class).add(example);
		if(criteria != null && criteria.list() != null && criteria.list().size() > 0){
			return (Field)criteria.list().get(0);
		}
		else{
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
		
		getSession().save(field);
	}

	public void updateField(Field field)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		field.setUpdateUserId(currentUser.getPrincipal().toString());
		field.setUpdateTime(dateNow);
		
		getSession().update(field);
	}
	
	public void deleteField(Field field)
	{
		getSession().delete(field);
	}

	public FieldType getFieldTypeByName(String fieldTypeName)
	{
		FieldType fieldType = new FieldType();
		Criteria criteria = getSession().createCriteria(FieldType.class);
		criteria.add(Restrictions.eq("name", fieldTypeName));
		
		if(criteria != null && criteria.list() != null && criteria.list().size() > 0){
			fieldType = (FieldType)criteria.list().get(0);
		}
		else{
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

	public FieldData getFieldDataByName(String fieldName)
	{
		log.info("PhenotypicDao.setFieldDataByName: " + fieldName);
		return null;
	}

	public void createFieldData(FieldData fieldData)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		fieldData.setUserId(currentUser.getPrincipal().toString());
		fieldData.setInsertTime(dateNow);
		
		getSession().save(fieldData);
	}
	
	public void updateFieldData(FieldData fieldData)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		fieldData.setUpdateTime(dateNow);
		fieldData.setUpdateUserId(currentUser.getPrincipal().toString());
		getSession().update(fieldData);
	}

	public void deleteFieldData(FieldData fieldData)
	{
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

	public void createUploadCollection(UploadCollection uploadCollection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		uploadCollection.setInsertTime(dateNow);
		uploadCollection.setUserId(currentUser.getPrincipal().toString());
		
		getSession().save(uploadCollection);
	}

	public void deleteUpload(Upload upload)
	{
		getSession().delete(upload);
	}

	public void deleteUploadCollection(UploadCollection uploadCollection)
	{
		getSession().delete(uploadCollection);
	}

	public void updateUpload(Upload upload)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		upload.setUpdateTime(dateNow);
		upload.setUpdateUserId(currentUser.getPrincipal().toString());
		
		getSession().update(upload);
	}

	public void updateUploadCollection(UploadCollection uploadCollection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		uploadCollection.setUpdateTime(dateNow);
		uploadCollection.setUpdateUserId(currentUser.getPrincipal().toString());
		
		getSession().update(uploadCollection);
	}
}
