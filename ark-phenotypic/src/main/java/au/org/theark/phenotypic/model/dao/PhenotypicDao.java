package au.org.theark.phenotypic.model.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.Session;
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
import au.org.theark.phenotypic.model.entity.DelimiterType;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldDataLog;
import au.org.theark.phenotypic.model.entity.FieldPhenoCollection;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.PhenoCollectionUpload;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.entity.PhenoUpload;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.util.PhenoUploadReport;

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

	public java.util.Collection<PhenoCollection> getPhenotypicCollectionByStudy(Study study)
	{
		Criteria crit = getSession().createCriteria(PhenoCollection.class);

		if (study != null)
		{
			crit.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_STUDY, study));
		}

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
		
		if (collectionToMatch.getDescription() != null)
		{
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_DESCRIPTION, collectionToMatch.getDescription(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStartDate() != null)
		{
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_START_DATE, collectionToMatch.getStartDate()));
		}
		
		if (collectionToMatch.getExpiryDate() != null)
		{
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_EXPIRY_DATE, collectionToMatch.getExpiryDate()));
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

	public PhenoCollectionVO getPhenoCollectionAndFields(Long id)
	{
		PhenoCollection phenoCollection = getPhenotypicCollection(id);
		PhenoCollectionVO phenoCollVo = new PhenoCollectionVO();
		phenoCollVo.setPhenoCollection(phenoCollection);
		java.util.Collection<FieldPhenoCollection> fieldPhenocCollectionFields = getPhenoCollectionFields(phenoCollection);
		for (FieldPhenoCollection fieldPhenoCollection : fieldPhenocCollectionFields)
		{
			phenoCollVo.getFieldsSelected().add(fieldPhenoCollection.getField());
		}

		return phenoCollVo;
	}

	public java.util.Collection<FieldPhenoCollection> getPhenoCollectionFields(PhenoCollection phenoCollection)
	{
		Criteria collectionCriteria = getSession().createCriteria(FieldPhenoCollection.class);
		if (phenoCollection != null)
		{
			collectionCriteria.add(Restrictions.eq("phenoCollection", phenoCollection));
		}

		java.util.Collection<FieldPhenoCollection> fieldPhenoCollectionFields = collectionCriteria.list();
		return fieldPhenoCollectionFields;
	}

	public void createPhenoCollection(PhenoCollection collection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collection.setInsertTime(dateNow);
		collection.setUserId(currentUser.getPrincipal().toString());

		getSession().save(collection);
	}

	public void createPhenoCollection(PhenoCollectionVO collectionVo)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collectionVo.getPhenoCollection().setInsertTime(dateNow);
		collectionVo.getPhenoCollection().setUserId(currentUser.getPrincipal().toString());

		Session session = getSession();
		session.save(collectionVo.getPhenoCollection());

		Collection<Field> fieldSelection = collectionVo.getFieldsSelected();

		FieldPhenoCollection fieldPhenoCollection = new FieldPhenoCollection();
		for (Field field : fieldSelection)
		{
			fieldPhenoCollection.setStudy(collectionVo.getPhenoCollection().getStudy());
			fieldPhenoCollection.setPhenoCollection(collectionVo.getPhenoCollection());
			fieldPhenoCollection.setField(field);

			session.save(fieldPhenoCollection);
			fieldPhenoCollection = new FieldPhenoCollection();
		}
	}

	public void updatePhenoCollection(PhenoCollection collection)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collection.setUserId(currentUser.getPrincipal().toString());
		collection.setUpdateTime(dateNow);

		getSession().update(collection);
	}

	public void updatePhenoCollection(PhenoCollectionVO collectionVo)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collectionVo.getPhenoCollection().setUpdateTime(dateNow);
		collectionVo.getPhenoCollection().setUserId(currentUser.getPrincipal().toString());

		Session session = getSession();
		session.update(collectionVo.getPhenoCollection());

		Collection<FieldPhenoCollection> fieldPhenoCollections = getPhenoCollectionFields(collectionVo.getPhenoCollection());
		// Delete all previous field_collections
		for (FieldPhenoCollection fieldPhenoCollection : fieldPhenoCollections)
		{
			session.delete(fieldPhenoCollection);
		}
		// Save field_collections
		FieldPhenoCollection fieldPhenoCollection = new FieldPhenoCollection();
		Collection<Field> fieldSelection = collectionVo.getFieldsSelected();
		for (Field field : fieldSelection)
		{
			fieldPhenoCollection = new FieldPhenoCollection();
			fieldPhenoCollection.setStudy(collectionVo.getPhenoCollection().getStudy());
			fieldPhenoCollection.setPhenoCollection(collectionVo.getPhenoCollection());
			fieldPhenoCollection.setField(field);

			session.save(fieldPhenoCollection);
		}
	}

	/**
	 * <p>
	 * This method is a helper for the Update FieldPhenoCollection. It establishes the list of fields that need to be added. 1. Iterate through each
	 * Field that is in the selected list</br> 2. Check if the selected Field exists in the existing list of Fields.</br> 3. If the selected list
	 * contains the Field, then checks if there is a Field that is not currently present and adds it to a list. 4. If the selected Field does NOT exist
	 * (inverse of 3), it adds the Field into a list. .</br>
	 * 
	 * </p>
	 * 
	 * @param selectedFieldList
	 * @param existingFieldList
	 * @param FieldListToAdd
	 * 
	 *           private void processAddList(List<Field> selectedFieldList, List<Field> existingFieldList, List<Field> fieldListToAdd){ for (Field
	 *           selectedField : selectedFieldList) { // Check if the selected Field is already there in the existing fields
	 *           if(existingFieldList.contains(selectedField)) { // Check if the field is present for(Field existingField: existingFieldList) { // If
	 *           the selected Field and the current existingField match then process the fields if(selectedField.equals(existingField)) { // Check if
	 *           the field exists for(Field selectedField1 : selectedFieldList) { // If not in list, add it
	 *           if(!existingFieldList.contains(selectedField1)) { fieldListToAdd.add(selectedField1); } } } } } else {
	 *           fieldListToAdd.add(selectedField); } } }
	 * 
	 * 
	 *           <p>
	 *           This method is a helper for the Update user details. It establishes the list of PhenoCollection and fields that need to be removed. 1.
	 *           Iterate through each PhenoCollection that is currently in the back-end.</br> 2. Check if the PhenoCollection exists in the currently
	 *           selected list of PhenoCollections.</br> 3. If the selected list does not contain the PhenoCollection then it marks the existing
	 *           PhenoCollection and all fields linked to it to be removed.</br> 4. If the existing PhenoCollection does exist(inverse of 3), then
	 *           checks if the existing fields are listed in the selected PhenoCollection's list of fields.</br> 5. If it does not find a field that
	 *           exists then it marks that PhenoCollection and the specific field(s) to be removed.</br>
	 *           </p>
	 * @param selectedFieldList
	 * @param existingFieldList
	 * @param FieldListToRemove
	 * 
	 *           private void processRemoveList(List<Field> selectedFieldList, List<Field> existingFieldList, List<Field> fieldListToRemove) {
	 *           for(Field existingField : existingFieldList) { // If the existing Field was not in the selected list of Fields, then mark it for
	 *           removal if(!selectedFieldList.contains(existingField)) { fieldListToRemove.add(existingField); } else { // Selected List contains an
	 *           existing Field. Determine the fields that need to be removed for(Field selectedField : selectedFieldList) {
	 *           if(selectedField.equals(existingField)) { fieldListToRemove.add(existingField); } } } } }
	 */

	public void deletePhenoCollection(PhenoCollection collection)
	{
		getSession().delete(collection);
	}

	public void deletePhenoCollection(PhenoCollectionVO collectionVo)
	{
		Session session = getSession();

		Collection<FieldPhenoCollection> fieldPhenoCollections = getPhenoCollectionFields(collectionVo.getPhenoCollection());
		// Delete all previous field_collections
		for (FieldPhenoCollection fieldPhenoCollection : fieldPhenoCollections)
		{
			session.delete(fieldPhenoCollection);
		}

		session.delete(collectionVo.getPhenoCollection());
	}

	public void createCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		phenoCollectionUpload.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		phenoCollectionUpload.setInsertTime(dateNow);

		getSession().save(phenoCollectionUpload);
	}
	
	public void updateCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		phenoCollectionUpload.setUserId(currentUser.getPrincipal().toString());
		phenoCollectionUpload.setUpdateTime(dateNow);

		getSession().update(phenoCollectionUpload);
	}

	public void deleteCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		getSession().delete(phenoCollectionUpload);
	}

	public java.util.Collection<FieldPhenoCollection> getFieldPhenoCollection(PhenoCollection phenoCollection)
	{
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (phenoCollection.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION, phenoCollection.getId()));
		}

		java.util.Collection<FieldPhenoCollection> fieldDataCollection = criteria.list();
		return fieldDataCollection;

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

	private String formatFieldName(String fieldName)
	{
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

	public java.util.Collection<FieldData> getFieldDataByCollectionAndField(PhenoCollection phenoCollection, Field field)
	{
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (phenoCollection.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION_ID, phenoCollection.getId()));
		}
		if (field.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD_ID, field.getId()));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public FieldData getFieldData(PhenoCollection phenoCollection, LinkSubjectStudy linkSubjectStudy, Field field, Date dateCollected, String value)
	{
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (phenoCollection != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_PHENO_COLLECTION, phenoCollection));
		}

		if (linkSubjectStudy != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DATA_LINK_SUBJECT_STUDY, linkSubjectStudy));
		}

		if (field != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD, field));
		}

		if (dateCollected != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DATA_COLLECTED, dateCollected));
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
		// FieldDataLog fieldDataLog = new FieldDataLog();
		// fieldDataLog.setFieldData(fieldData);
		// fieldDataLog.setValue(fieldData.getValue());
		// fieldDataLog.setComment("Insert");
		// fieldDataLog.setUserId(currentUser.getPrincipal().toString());
		// fieldDataLog.setInsertTime(dateNow);
		//		
		// fieldData.getFieldDataLogs().add(fieldDataLog);
		getSession().save(fieldData);
	}

	public void updateFieldData(FieldData fieldData)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		fieldData.setUpdateTime(dateNow);
		fieldData.setUpdateUserId(currentUser.getPrincipal().toString());

		// TODO Add fieldDataLog on Update
		// FieldDataLog fieldDataLog = new FieldDataLog();
		// fieldDataLog.setFieldData(fieldData);
		// fieldDataLog.setValue(fieldData.getValue());
		// fieldDataLog.setComment("Update");
		// fieldDataLog.setUserId(currentUser.getPrincipal().toString());
		// fieldDataLog.setInsertTime(dateNow);
		//		
		// fieldData.getFieldDataLogs().add(fieldDataLog);

		getSession().update(fieldData);
	}

	public void deleteFieldData(FieldData fieldData)
	{
		// TODO Add fieldDataLog on Insert
		// FieldDataLog fieldDataLog = new FieldDataLog();
		// fieldDataLog.setFieldData(fieldData);
		// fieldDataLog.setValue(fieldData.getValue());
		// fieldDataLog.setComment("Delete");
		// fieldDataLog.setUserId(currentUser.getPrincipal().toString());
		// fieldDataLog.setInsertTime(dateNow);
		// fieldData.getFieldDataLogs().add(fieldDataLog);

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

	public void createUpload(PhenoUpload phenoUpload)
	{
		Session session = getSession();
		
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		phenoUpload.setInsertTime(dateNow);
		phenoUpload.setUserId(currentUser.getPrincipal().toString());
		phenoUpload.setStartTime(dateNow);

		session.save(phenoUpload);
		
		// Set Upload report
		PhenoUploadReport uploadReport = new PhenoUploadReport();
		PhenoUploadReport.appendDetails(phenoUpload);
		phenoUpload.setUploadReport(uploadReport.getReportAsBlob());
		session.update(phenoUpload);
	}
	
	public void createUpload(UploadVO phenoUploadVo)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		phenoUploadVo.getUpload().setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		phenoUploadVo.getUpload().setInsertTime(dateNow);
		phenoUploadVo.getUpload().setStartTime(dateNow);
		
		phenoUploadVo.getPhenoCollectionUpload().setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		phenoUploadVo.getPhenoCollectionUpload().setInsertTime(dateNow);
		
		Session session = getSession();
		session.save(phenoUploadVo.getUpload());
		session.save(phenoUploadVo.getPhenoCollectionUpload());
		
		// Set Upload report
		PhenoUploadReport uploadReport = new PhenoUploadReport();
		PhenoUploadReport.appendDetails(phenoUploadVo.getUpload());
		phenoUploadVo.getUpload().setUploadReport(uploadReport.getReportAsBlob());
		session.update(phenoUploadVo.getUpload());
	}

	public void updateUpload(PhenoUpload upload)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		upload.setUpdateTime(dateNow);
		upload.setUpdateUserId(currentUser.getPrincipal().toString());

		getSession().update(upload);
	}

	public void deleteUpload(PhenoUpload upload)
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

		if (field.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_ID, field.getId()));
		}

		if (field.getName() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_NAME, field.getName()));
		}

		if (field.getStudy() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_STUDY, field.getStudy()));
		}

		if (field.getFieldType() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_FIELD_TYPE, field.getFieldType()));
		}

		if (field.getDescription() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DESCRIPTION, field.getDescription(), MatchMode.ANYWHERE));
		}

		if (field.getUnits() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_UNITS, field.getUnits(), MatchMode.ANYWHERE));
		}

		if (field.getMinValue() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_MIN_VALUE, field.getMinValue(), MatchMode.ANYWHERE));
		}

		if (field.getMaxValue() != null)
		{
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

	public PhenoCollectionUpload getCollectionUpload(Long id)
	{
		PhenoCollectionUpload phenoCollectionUpload = (PhenoCollectionUpload) getSession().get(PhenoCollectionUpload.class, id);
		return phenoCollectionUpload;
	}

	public java.util.Collection<PhenoCollectionUpload> getCollectionUpload()
	{
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);
		java.util.Collection<PhenoCollectionUpload> phenoCollectionUploadCollection = criteria.list();
		return phenoCollectionUploadCollection;
	}

	public java.util.Collection<PhenoCollectionUpload> searchCollectionUpload(PhenoCollectionUpload phenoCollectionUploadToMatch)
	{
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (phenoCollectionUploadToMatch.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.COLLECTION_IMPORT_ID, phenoCollectionUploadToMatch.getId()));
		}

		/*
		 * TODO Change to FieldFieldGroupUpload if (phenoCollectionUploadToMatch.getCollection() != null) {
		 * criteria.add(Restrictions.ilike(au.org.theark
		 * .phenotypic.web.Constants.COLLECTION_IMPORT_COLLECTION,phenoCollectionUploadToMatch.getCollection())); }
		 */

		java.util.Collection<PhenoCollectionUpload> phenoCollectionUploadCollection = criteria.list();
		return phenoCollectionUploadCollection;
	}

	public FieldDataLog getFieldDataLog(Long id)
	{
		FieldDataLog fieldDataLog = (FieldDataLog) getSession().get(FieldDataLog.class, id);
		return fieldDataLog;
	}

	public java.util.Collection<FieldDataLog> getFieldDataLogByField(Field field)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public java.util.Collection<FieldData> searchFieldDataByField(Field field)
	{
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (field.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD, field));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public java.util.Collection<FieldData> searchFieldData(FieldData fieldData)
	{
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (fieldData.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_ID, fieldData.getId()));
		}

		if (fieldData.getCollection() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_PHENO_COLLECTION, fieldData.getCollection()));
		}

		if (fieldData.getField() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD, fieldData.getField()));
		}

		if (fieldData.getLinkSubjectStudy() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_LINK_SUBJECT_STUDY, fieldData.getLinkSubjectStudy()));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public void createFieldDataLog(FieldDataLog fieldDataLog)
	{
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		fieldDataLog.setInsertTime(dateNow);
		fieldDataLog.setUserId(currentUser.getPrincipal().toString());
		getSession().save(fieldDataLog);
	}

	public PhenoUpload getUpload(Long id)
	{
		PhenoUpload upload = (PhenoUpload) getSession().get(PhenoUpload.class, id);
		return upload;
	}

	public java.util.Collection<PhenoUpload> getUploadByFileName(String fileName)
	{
		Criteria criteria = getSession().createCriteria(PhenoUpload.class);

		if (fileName != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION, fileName));
		}

		java.util.Collection<PhenoUpload> uploadCollection = criteria.list();

		return uploadCollection;
	}

	public PhenoCollectionVO getPhenoCollectionAndUploads(Long id)
	{
		PhenoCollection phenoCollection = getPhenotypicCollection(id);
		PhenoCollectionVO phenoCollVo = new PhenoCollectionVO();
		phenoCollVo.setPhenoCollection(phenoCollection);
		java.util.Collection<PhenoUpload> phenoCollectionUploads = getPhenoCollectionUploads(phenoCollection);
		for (PhenoUpload upload : phenoCollectionUploads)
		{
			phenoCollVo.getUploadCollection().add(upload);
		}

		return phenoCollVo;
	}

	private java.util.Collection<PhenoUpload> getPhenoCollectionUploads(PhenoCollection phenoCollection)
	{
		Criteria criteria = getSession().createCriteria(PhenoUpload.class);

		if (phenoCollection != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION, phenoCollection));
		}

		java.util.Collection<PhenoUpload> uploadCollection = criteria.list();
		return uploadCollection;
	}

	public java.util.Collection<PhenoUpload> searchUpload(PhenoUpload upload)
	{
		Criteria criteria = getSession().createCriteria(PhenoUpload.class);

		if (upload.getId() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.UPLOAD_ID, upload.getId()));
		}
		
		if (upload.getStudy() != null)
		{
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.UPLOAD_STUDY, upload.getStudy()));
		}

		if (upload.getFileFormat() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_FILE_FORMAT, upload.getFileFormat()));
		}
		
		if (upload.getDelimiterType() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_DELIMITER_TYPE, upload.getDelimiterType()));
		}
		
		if (upload.getFilename() != null)
		{
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_FILENAME, upload.getFilename()));
		}

		criteria.addOrder(Order.desc(au.org.theark.phenotypic.web.Constants.UPLOAD_ID));
		java.util.Collection<PhenoUpload> uploadCollection = criteria.list();

		return uploadCollection;
	}

	public java.util.Collection<FileFormat> getFileFormats()
	{
		Criteria crit = getSession().createCriteria(FileFormat.class);
		java.util.Collection<FileFormat> fileFormatCollection = crit.list();
		return fileFormatCollection;
	}

	public Collection<DelimiterType> getDelimiterTypes()
	{
		Criteria crit = getSession().createCriteria(DelimiterType.class);
		java.util.Collection<DelimiterType> delimiterTypeCollection = crit.list();
		return delimiterTypeCollection;
	}
}