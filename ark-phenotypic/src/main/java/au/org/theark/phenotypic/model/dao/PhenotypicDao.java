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
package au.org.theark.phenotypic.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;

@SuppressWarnings("unchecked")
@Repository("phenotypicDao")
public class PhenotypicDao extends HibernateSessionDao implements IPhenotypicDao {
	static Logger		log	= LoggerFactory.getLogger(PhenotypicDao.class);

	private IArkCommonService<Void>	iArkCommonService;
	@Autowired
	public void setiArkCommonService(IArkCommonService<Void> iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	public java.util.Collection<PhenoCollection> getPhenoCollectionByStudy(Study study) {
		Criteria criteria = getSession().createCriteria(PhenoCollection.class);

		if (study != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_STUDY, study));
		}
		criteria.addOrder(Order.asc("name"));

		java.util.List<PhenoCollection> collectionList = criteria.list();
		return collectionList;
	}
/*
	public java.util.Collection<PhenoCollection> searchPhenoCollection(PhenoCollection collectionToMatch) {
		Criteria collectionCriteria = getSession().createCriteria(PhenoCollection.class);

		if (collectionToMatch.getId() != null) {
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_ID, collectionToMatch.getId()));
		}

		if (collectionToMatch.getName() != null) {
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_NAME, collectionToMatch.getName(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStudy() != null) {
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_STUDY, collectionToMatch.getStudy()));
		}

		if (collectionToMatch.getDescription() != null) {
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_DESCRIPTION, collectionToMatch.getDescription(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStartDate() != null) {
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_START_DATE, collectionToMatch.getStartDate()));
		}

		if (collectionToMatch.getEndDate() != null) {
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_END_DATE, collectionToMatch.getEndDate()));
		}

		if (collectionToMatch.getInsertTime() != null) {
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_INSERT_TIME, collectionToMatch.getInsertTime()));
		}

		if (collectionToMatch.getUserId() != null) {
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_USER_ID, collectionToMatch.getUserId(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getUpdateTime() != null) {
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_UPDATE_TIME, collectionToMatch.getUpdateTime()));
		}

		if (collectionToMatch.getUpdateUserId() != null) {
			collectionCriteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_UPDATE_USER_ID, collectionToMatch.getUpdateUserId(), MatchMode.ANYWHERE));
		}

		if (collectionToMatch.getStatus() != null) {
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.STATUS, collectionToMatch.getStatus()));
		}

		collectionCriteria.addOrder(Order.asc("name"));
		java.util.Collection<PhenoCollection> phenoCollectionCollection = collectionCriteria.list();
		return phenoCollectionCollection;
	}

	public PhenoCollectionVO getPhenoCollectionAndFields(Long id) {
		PhenoCollection phenoCollection = getPhenoCollection(id);
		PhenoCollectionVO phenoCollVo = new PhenoCollectionVO();
		phenoCollVo.setPhenoCollection(phenoCollection);
		java.util.Collection<FieldPhenoCollection> fieldPhenocCollectionFields = getPhenoCollectionFields(phenoCollection);
		for (FieldPhenoCollection fieldPhenoCollection : fieldPhenocCollectionFields) {
			phenoCollVo.getFieldsSelected().add(fieldPhenoCollection.getField());
		}

		return phenoCollVo;
	}

	public java.util.Collection<FieldPhenoCollection> getPhenoCollectionFields(PhenoCollection phenoCollection) {
		Criteria collectionCriteria = getSession().createCriteria(FieldPhenoCollection.class);
		if (phenoCollection != null) {
			collectionCriteria.add(Restrictions.eq("phenoCollection", phenoCollection));
		}

		java.util.Collection<FieldPhenoCollection> fieldPhenoCollectionFields = collectionCriteria.list();
		return fieldPhenoCollectionFields;
	}

	public void createPhenoCollection(PhenoCollectionVO collectionVo) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collectionVo.getPhenoCollection().setInsertTime(dateNow);
		collectionVo.getPhenoCollection().setUserId(currentUser.getPrincipal().toString());

		Session session = getSession();
		session.save(collectionVo.getPhenoCollection());

		Collection<Field> fieldSelection = collectionVo.getFieldsSelected();

		FieldPhenoCollection fieldPhenoCollection = new FieldPhenoCollection();
		for (Field field : fieldSelection) {
			fieldPhenoCollection.setStudy(collectionVo.getPhenoCollection().getStudy());
			fieldPhenoCollection.setPhenoCollection(collectionVo.getPhenoCollection());
			fieldPhenoCollection.setField(field);

			session.save(fieldPhenoCollection);
			fieldPhenoCollection = new FieldPhenoCollection();
		}
	}
	public void updatePhenoCollection(PhenoCollectionVO collectionVo) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		collectionVo.getPhenoCollection().setUpdateTime(dateNow);
		collectionVo.getPhenoCollection().setUserId(currentUser.getPrincipal().toString());

		Session session = getSession();
		session.update(collectionVo.getPhenoCollection());

		Collection<FieldPhenoCollection> fieldPhenoCollections = getPhenoCollectionFields(collectionVo.getPhenoCollection());
		// Delete all previous field_collections
		for (FieldPhenoCollection fieldPhenoCollection : fieldPhenoCollections) {
			session.delete(fieldPhenoCollection);
		}
		// Save field_collections
		FieldPhenoCollection fieldPhenoCollection = new FieldPhenoCollection();
		Collection<Field> fieldSelection = collectionVo.getFieldsSelected();
		for (Field field : fieldSelection) {
			fieldPhenoCollection = new FieldPhenoCollection();
			fieldPhenoCollection.setStudy(collectionVo.getPhenoCollection().getStudy());
			fieldPhenoCollection.setPhenoCollection(collectionVo.getPhenoCollection());
			fieldPhenoCollection.setField(field);

			session.save(fieldPhenoCollection);
		}
	}

*/

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

	public void deletePhenoCollection(PhenoCollection collection) throws ArkSystemException, EntityCannotBeRemoved {
		getSession().delete(collection);

		if (!phenoCollectionHasData(collection)) {
			getSession().delete(collection);
		}
		else {
			throw new EntityCannotBeRemoved("The Collection: " + collection.getName() + " has data associated and cannot be removed.");
		}
	}
/*
	public void deletePhenoCollection(PhenoCollectionVO collectionVo) {
		Session session = getSession();

		Collection<FieldPhenoCollection> fieldPhenoCollections = getPhenoCollectionFields(collectionVo.getPhenoCollection());
		// Delete all previous field_collections
		for (FieldPhenoCollection fieldPhenoCollection : fieldPhenoCollections) {
			session.delete(fieldPhenoCollection);
		}

		session.delete(collectionVo.getPhenoCollection());
	}

	public void createCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		phenoCollectionUpload.setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		phenoCollectionUpload.setInsertTime(dateNow);

		getSession().save(phenoCollectionUpload);
	}

	public void updateCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		phenoCollectionUpload.setUserId(currentUser.getPrincipal().toString());
		phenoCollectionUpload.setUpdateTime(dateNow);

		getSession().update(phenoCollectionUpload);
	}

	public void deleteCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		getSession().delete(phenoCollectionUpload);
	}

	public java.util.Collection<FieldPhenoCollection> getFieldPhenoCollection(PhenoCollection phenoCollection) {
		Criteria criteria = getSession().createCriteria(FieldPhenoCollection.class);

		if (phenoCollection.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION, phenoCollection));
		}

		java.util.Collection<FieldPhenoCollection> fieldPhenoCollection = criteria.list();
		return fieldPhenoCollection;

	}

	public Field getField(Long id) {
		Field field = (Field) getSession().get(Field.class, id);
		return field;
	}

	public Field getFieldByNameAndStudy(String fieldName, Study study) throws EntityNotFoundException {
		Field field = new Field();
		Criteria criteria = getSession().createCriteria(Field.class);
		if (fieldName != null && study != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_NAME, fieldName));
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_STUDY, study));
		}

		List<Field> results = criteria.list();
		if (results.size() > 0) {
			field = (Field)results.get(0);
		}
		else {
			throw new EntityNotFoundException();
		}

		return field;
	}

	public void createField(Field field) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		field.setUserId(currentUser.getPrincipal().toString());
		field.setInsertTime(dateNow);

		// Format the Field name before creating
		field.setName(formatFieldName(field.getName()));

		// Default quality control boolean
		if (field.getQualityControlStatus() == null)
			field.setQualityControlStatus(true);

		getSession().save(field);
	}

	public void updateField(Field field) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		field.setUpdateUserId(currentUser.getPrincipal().toString());
		field.setUpdateTime(dateNow);

		// Format the Field name before updating
		field.setName(formatFieldName(field.getName()));

		getSession().update(field);
	}

	public void deleteField(Field field) throws ArkSystemException, EntityCannotBeRemoved {
		if (!fieldHasData(field)) {
			getSession().delete(field);
		}
		else {
			throw new EntityCannotBeRemoved("The Field: " + field.getName() + " has data associated and cannot be removed.");
		}
	}

	private String formatFieldName(String fieldName) {
		// Uppercase the Fieldname
		fieldName = fieldName.toUpperCase();

		// Replace all spaces with underscores
		fieldName = StringUtils.replace(fieldName, " ", "_");

		// Replace all spaces with underscores
		fieldName = StringUtils.replace(fieldName, " ", "_");

		return fieldName;
	}

	public FieldType getFieldTypeByName(String fieldTypeName) {
		FieldType fieldType = new FieldType();
		Criteria criteria = getSession().createCriteria(FieldType.class);
		criteria.add(Restrictions.eq("name", fieldTypeName));

		if (criteria != null){ 
			List<FieldType> results = criteria.list();
			if(results != null && !results.isEmpty()) {
				fieldType = (FieldType) results.get(0);
			}
		}
		else {
			log.error("Field Type Table maybe out of synch. Please check if it has an entry for " + fieldTypeName + " status");
			log.error("Cannot locate a field type with " + fieldTypeName + " in the database");
		}
		return fieldType;
	}
	public void createFieldType(FieldType fieldType) {
		getSession().save(fieldType);
	}

	public void updateFieldType(FieldType fieldType) {
		getSession().update(fieldType);
	}

	public FieldData getFieldData(Long id) {
		FieldData fieldData = (FieldData) getSession().get(FieldData.class, id);
		return fieldData;
	}

	public FieldData getFieldData(FieldData fieldData) {
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (fieldData.getCollection() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_PHENO_COLLECTION, fieldData.getCollection()));
		}

		if (fieldData.getLinkSubjectStudy() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_LINK_SUBJECT_STUDY, fieldData.getLinkSubjectStudy()));
		}

		if (fieldData.getField() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD, fieldData.getField()));
		}

		if (fieldData.getDateCollected() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_DATE_COLLECTED, fieldData.getDateCollected()));
		}

		FieldData returnFieldData = new FieldData();
		if (criteria != null){
			List<FieldData> results = criteria.list();
			if(results != null && results.size() > 0) {
				returnFieldData = (FieldData) results.get(0);
			}
		}
		else {
			log.error("Field Data table maybe out of synch. Please check if it has an entry for a fieldData with id:" + fieldData.getId());
		}
		return returnFieldData;
	}

	public FieldData getFieldDataByName(String fieldName) {
		log.info("PhenotypicDao.getFieldDataByName: " + fieldName);
		return null;
	}

	public java.util.Collection<FieldData> getFieldDataByCollectionAndField(PhenoCollection phenoCollection, Field field) {
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (phenoCollection.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION_ID, phenoCollection.getId()));
		}
		if (field.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD_ID, field.getId()));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public FieldData getFieldData(PhenoCollection phenoCollection, LinkSubjectStudy linkSubjectStudy, Field field, Date dateCollected, String value) {
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (phenoCollection != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_PHENO_COLLECTION, phenoCollection));
		}

		if (linkSubjectStudy != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_LINK_SUBJECT_STUDY, linkSubjectStudy));
		}

		if (field != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD, field));
		}

		if (dateCollected != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_DATE_COLLECTED, dateCollected));
		}

		FieldData fieldData = new FieldData();
		if (criteria != null){
			List<FieldData> results = criteria.list();
			if(results != null && !results.isEmpty()) {
				fieldData = (FieldData) results.get(0);
			}
		}
		else {
			log.error("Field Data table maybe out of synch. Please check if it has an entry for a fieldData with id:" + fieldData.getId());
		}
		return fieldData;
	}

	public void createFieldData(FieldData fieldData) {
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

	public void updateFieldData(FieldData fieldData) {
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

	public void deleteFieldData(FieldData fieldData) {
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

	public Status getStatus(Long statusId) {
		Criteria criteria = getSession().createCriteria(Status.class);
		criteria.add(Restrictions.eq("id", statusId));

		List<Status> statusList = criteria.list();
		if (statusList.size() > 0) {
			if (statusList.size() > 1) {
				log.error("Backend database has non-unique Status names, returned the first one");
			}
			return (statusList.get(0));
		}
		else
			return null;
	}

	public Status getStatusByName(String statusName) {
		Criteria criteria = getSession().createCriteria(Status.class);
		criteria.add(Restrictions.eq("name", statusName));
		criteria.addOrder(Order.asc("id"));
		List<Status> statusList = criteria.list();
		if (statusList.size() > 0) {
			if (statusList.size() > 1) {
				log.error("Backend database has non-unique Status names, returned the first one");
			}
			return (statusList.get(0));
		}
		else
			return null;
	}

	public java.util.Collection<Status> getStatus() {
		Criteria criteria = getSession().createCriteria(Status.class);
		java.util.Collection<Status> statusCollection = criteria.list();
		return (statusCollection);
	}

	public void createStatus(Status status) {
		getSession().save(status);
	}

	public void updateStatus(Status status) {
		getSession().update(status);
	}*/

	public void createUpload(Upload phenoUpload) {
		Session session = getSession();

		//currentUser = SecurityUtils.getSubject();
		Date dateNow = new Date(System.currentTimeMillis());
		//phenoUpload.setInsertTime(dateNow);
		//phenoUpload.setUserId(currentUser.getPrincipal().toString());

		if (phenoUpload.getStartTime() == null)
			phenoUpload.setStartTime(dateNow);

		session.save(phenoUpload);
	}
/*
	public void createUpload(UploadVO phenoUploadVo) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		phenoUploadVo.getUpload().setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
		phenoUploadVo.getUpload().setInsertTime(dateNow);

		if (phenoUploadVo.getUpload().getStartTime() == null)
			phenoUploadVo.getUpload().setStartTime(dateNow);

		FileFormat fileFormat = new FileFormat();
		String fileFormatName = phenoUploadVo.getUpload().getFileFormat().getName();
		fileFormat = getFileFormatByName(fileFormatName);
		phenoUploadVo.getUpload().setFileFormat(fileFormat);

		Session session = getSession();
		session.save(phenoUploadVo.getUpload());

		// Save PhenoCollectionUpload entity
		if (phenoUploadVo.getPhenoCollectionUpload() != null) {
			phenoUploadVo.getPhenoCollectionUpload().setUserId(currentUser.getPrincipal().toString()); // use Shiro to get username
			phenoUploadVo.getPhenoCollectionUpload().setInsertTime(dateNow);
			session.save(phenoUploadVo.getPhenoCollectionUpload());
		}

		// Loop and save any FieldUpload entities
		for (FieldUpload fieldUpload : phenoUploadVo.getFieldUploadCollection()) {
			fieldUpload.setUserId(currentUser.getPrincipal().toString());
			fieldUpload.setUpload(phenoUploadVo.getUpload());
			fieldUpload.setInsertTime(dateNow);
			session.save(fieldUpload);
		}
	}
*/
	public void updateUpload(Upload upload) {
		//currentUser = SecurityUtils.getSubject();
		//dateNow = new Date(System.currentTimeMillis());
		//upload.setUpdateTime(dateNow);
		//upload.setUpdateUserId(currentUser.getPrincipal().toString());

		getSession().update(upload);
	}

	/*public void deleteUpload(Upload upload) throws ArkSystemException, EntityCannotBeRemoved {
		if (!uploadHasData(upload)) {
			getSession().delete(upload);
		}
		else {
			throw new EntityCannotBeRemoved("The File: " + upload.getFilename() + " has data associated and cannot be removed.");
		}
	}

	public java.util.Collection<FieldType> getFieldTypes() {
		Criteria criteria = getSession().createCriteria(FieldType.class);
		java.util.Collection<FieldType> fieldTypeCollection = criteria.list();
		return fieldTypeCollection;
	}

	public FieldType getFieldType(Long id) {
		FieldType fieldType = (FieldType) getSession().get(FieldType.class, id);
		return fieldType;
	}

	public java.util.Collection<Field> searchField(Field field) {
		java.util.Collection<Field> fieldCollection = null;

		Criteria criteria = getSession().createCriteria(Field.class);

		if (field.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_ID, field.getId()));
		}

		if (field.getName() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_NAME, field.getName(), MatchMode.ANYWHERE));
		}

		if (field.getStudy() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_STUDY, field.getStudy()));
		}

		if (field.getFieldType() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_FIELD_TYPE, field.getFieldType()));
		}

		if (field.getDescription() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_DESCRIPTION, field.getDescription(), MatchMode.ANYWHERE));
		}

		if (field.getUnits() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_UNITS, field.getUnits(), MatchMode.ANYWHERE));
		}

		if (field.getMinValue() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_MIN_VALUE, field.getMinValue(), MatchMode.ANYWHERE));
		}

		if (field.getMaxValue() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.FIELD_MAX_VALUE, field.getMaxValue(), MatchMode.ANYWHERE));
		}

		// Return fields ordered alphabetically
		criteria.addOrder(Order.asc(au.org.theark.phenotypic.web.Constants.FIELD_NAME));
		fieldCollection = criteria.list();

		return fieldCollection;
	}

	public java.util.Collection<Field> getField() {
		Criteria criteria = getSession().createCriteria(Field.class);
		java.util.Collection<Field> fieldCollection = criteria.list();
		return fieldCollection;
	}

	public PhenoCollectionUpload getCollectionUpload(Long id) {
		PhenoCollectionUpload phenoCollectionUpload = (PhenoCollectionUpload) getSession().get(PhenoCollectionUpload.class, id);
		return phenoCollectionUpload;
	}

	public java.util.Collection<PhenoCollectionUpload> getCollectionUpload() {
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);
		java.util.Collection<PhenoCollectionUpload> phenoCollectionUploadCollection = criteria.list();
		return phenoCollectionUploadCollection;
	}

	public java.util.Collection<PhenoCollectionUpload> searchCollectionUpload(PhenoCollectionUpload phenoCollectionUploadToMatch) {
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (phenoCollectionUploadToMatch.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_UPLOAD_ID, phenoCollectionUploadToMatch.getId()));
		}

		/*
		 * TODO Change to FieldFieldGroupUpload if (phenoCollectionUploadToMatch.getCollection() != null) {
		 * criteria.add(Restrictions.ilike(au.org.theark
		 * .phenotypic.web.Constants.COLLECTION_IMPORT_COLLECTION,phenoCollectionUploadToMatch.getCollection())); }
		 *

		java.util.Collection<PhenoCollectionUpload> phenoCollectionUploadCollection = criteria.list();
		return phenoCollectionUploadCollection;
	}

	public FieldDataLog getFieldDataLog(Long id) {
		FieldDataLog fieldDataLog = (FieldDataLog) getSession().get(FieldDataLog.class, id);
		return fieldDataLog;
	}

	public java.util.Collection<FieldDataLog> getFieldDataLogByField(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	public java.util.Collection<FieldData> searchFieldDataByField(Field field) {
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (field.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD, field));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public java.util.Collection<FieldData> searchFieldData(FieldData fieldData) {
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (fieldData.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_ID, fieldData.getId()));
		}

		if (fieldData.getCollection() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_PHENO_COLLECTION, fieldData.getCollection()));
		}

		if (fieldData.getField() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_FIELD, fieldData.getField()));
		}

		if (fieldData.getLinkSubjectStudy() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_LINK_SUBJECT_STUDY, fieldData.getLinkSubjectStudy()));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public java.util.Collection<FieldData> searchFieldDataByStudy(Study study) {
		Criteria criteria = getSession().createCriteria(FieldData.class);
		// TODO: Implement
		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public void createFieldDataLog(FieldDataLog fieldDataLog) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		fieldDataLog.setInsertTime(dateNow);
		fieldDataLog.setUserId(currentUser.getPrincipal().toString());
		getSession().save(fieldDataLog);
	}

	public PhenoUpload getUpload(Long id) {
		PhenoUpload upload = (PhenoUpload) getSession().get(PhenoUpload.class, id);
		return upload;
	}

	public java.util.Collection<PhenoUpload> getUploadByFileName(String fileName) {
		Criteria criteria = getSession().createCriteria(PhenoUpload.class);

		if (fileName != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION, fileName));
		}

		java.util.Collection<PhenoUpload> uploadCollection = criteria.list();

		return uploadCollection;
	}

	public PhenoCollectionVO getPhenoCollectionAndUploads(Long id) {
		PhenoCollection phenoCollection = getPhenoCollection(id);
		PhenoCollectionVO phenoCollVo = new PhenoCollectionVO();
		phenoCollVo.setPhenoCollection(phenoCollection);
		java.util.Collection<PhenoUpload> phenoCollectionUploads = getPhenoCollectionUploads(phenoCollection);
		for (PhenoUpload upload : phenoCollectionUploads) {
			phenoCollVo.getUploadCollection().add(upload);
		}

		return phenoCollVo;
	}

	public PhenoCollectionVO getPhenoCollectionAndUploads(PhenoCollection phenoCollection) {
		PhenoCollectionVO phenoCollVo = new PhenoCollectionVO();
		phenoCollVo.setPhenoCollection(phenoCollection);
		java.util.Collection<PhenoUpload> phenoCollectionUploads = getPhenoCollectionUploads(phenoCollection);
		for (PhenoUpload upload : phenoCollectionUploads) {
			phenoCollVo.getUploadCollection().add(upload);
		}

		return phenoCollVo;
	}

	private java.util.Collection<PhenoUpload> getPhenoCollectionUploads(PhenoCollection phenoCollection) {
		Criteria criteria = getSession().createCriteria(PhenoUpload.class);

		if (phenoCollection != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION, phenoCollection));
		}

		java.util.Collection<PhenoUpload> uploadCollection = criteria.list();
		return uploadCollection;
	}

	public java.util.Collection<PhenoUpload> searchUpload(PhenoUpload upload) {
		Criteria criteria = getSession().createCriteria(PhenoUpload.class);

		if (upload.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.UPLOAD_ID, upload.getId()));
		}

		if (upload.getStudy() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.UPLOAD_STUDY, upload.getStudy()));
		}

		if (upload.getFileFormat() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_FILE_FORMAT, upload.getFileFormat()));
		}

		if (upload.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_DELIMITER_TYPE, upload.getDelimiterType()));
		}

		if (upload.getFilename() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_FILENAME, upload.getFilename()));
		}

		if (upload.getUploadType() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_UPLOAD_TYPE, upload.getUploadType()));
		}

		criteria.addOrder(Order.desc(au.org.theark.phenotypic.web.Constants.UPLOAD_ID));
		java.util.Collection<PhenoUpload> uploadCollection = criteria.list();

		return uploadCollection;
	}

*/
	public java.util.Collection<FileFormat> getFileFormats() {
		Criteria criteria = getSession().createCriteria(FileFormat.class);
		java.util.Collection<FileFormat> fileFormatCollection = criteria.list();
		return fileFormatCollection;
	}

	public Collection<DelimiterType> getDelimiterTypes() {
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		java.util.Collection<DelimiterType> delimiterTypeCollection = criteria.list();
		return delimiterTypeCollection;
	}
/*
	public Collection<PhenoUpload> searchUploadByCollection(PhenoCollection phenoCollection) {
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (phenoCollection.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_UPLOAD_COLLECTION, phenoCollection));
		}
		criteria.addOrder(Order.desc(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_ID));
		java.util.Collection<PhenoCollectionUpload> phenoCollectionUploadCollection = criteria.list();
		java.util.Collection<PhenoUpload> phenoUploadCollection = new java.util.ArrayList<PhenoUpload>();

		for (Iterator iterator = phenoCollectionUploadCollection.iterator(); iterator.hasNext();) {
			PhenoCollectionUpload phenoCollectionUpload = (PhenoCollectionUpload) iterator.next();
			PhenoUpload phenoUpload = phenoCollectionUpload.getUpload();
			
			phenoUploadCollection.add(phenoUpload);
		}

		return phenoUploadCollection;
	}

	public void createPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());
		phenoCollectionUpload.setInsertTime(dateNow);
		phenoCollectionUpload.setUserId(currentUser.getPrincipal().toString());
		getSession().save(phenoCollectionUpload);

	}

	public void deletePhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		getSession().delete(phenoCollectionUpload);
	}

	public PhenoCollectionUpload getPhenoCollectionUpload(Long id) {
		PhenoCollectionUpload phenoCollectionUpload = (PhenoCollectionUpload) getSession().get(PhenoCollectionUpload.class, id);
		return phenoCollectionUpload;
	}

	public Collection<PhenoCollectionUpload> searchPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (phenoCollectionUpload.getCollection().getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_UPLOAD_COLLECTION, phenoCollectionUpload.getCollection()));
		}
		criteria.addOrder(Order.desc(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_ID));
		java.util.Collection<PhenoCollectionUpload> phenoCollectionUploadCollection = criteria.list();
		return phenoCollectionUploadCollection;
	}

	public void updatePhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		getSession().update(phenoCollectionUpload);
	}
*/
	public long getCountOfFieldsInStudy(Study study) {
		//TODO implement
		return -1L;
	}

	public long getCountOfFieldsWithDataInStudy(Study study) {
		//TODO implement
		return -1L;
		
	}
/*
	public Collection<FieldData> searchFieldDataBySubjectAndDateCollected(LinkSubjectStudy linkSubjectStudy, java.util.Date dateCollected) {
		Criteria criteria = getSession().createCriteria(FieldData.class);
//		String dateStr = dateCollected.toString();

		if (linkSubjectStudy.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_LINK_SUBJECT_STUDY, linkSubjectStudy));
		}

		if (dateCollected != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_DATA_DATE_COLLECTED, dateCollected));
		}

		java.util.Collection<FieldData> fieldDataCollection = criteria.list();
		return fieldDataCollection;
	}

	public Collection<PhenoUpload> searchFieldUpload(PhenoUpload phenoUpload) {
		Criteria criteria = getSession().createCriteria(PhenoUpload.class);
		criteria.add(Restrictions.eq("uploadType", "field"));

		if (phenoUpload.getStudy() != null) {
			criteria.add(Restrictions.eq("study", phenoUpload.getStudy()));
		}

		java.util.Collection<PhenoUpload> phenoUploadCollection = criteria.list();
		return phenoUploadCollection;
	}
*/
	public long getCountOfCollectionsInStudy(Study study) {
		int count = 0;

		if (study.getId() != null) {
			Criteria criteria = getSession().createCriteria(PhenoCollection.class);
			criteria.add(Restrictions.eq("study", study));

			java.util.Collection<PhenoCollection> phenoCollection = criteria.list();
			count = phenoCollection.size();
		}

		return count;
	}

	public long getCountOfCollectionsWithDataInStudy(Study study) {
		long count = 0;

		if (study.getId() != null) {
			Collection<PhenoCollection> phenoCollectionColn = getPhenoCollectionByStudy(study);

			for (Iterator iterator = phenoCollectionColn.iterator(); iterator.hasNext();) {
				PhenoCollection phenoCollection = (PhenoCollection) iterator.next();

				Criteria criteria = getSession().createCriteria(PhenoData.class);
				criteria.add(Restrictions.eq("collection", phenoCollection));
				ProjectionList projList = Projections.projectionList();
				projList.add(Projections.countDistinct("collection"));
				criteria.setProjection(projList);
				List list = criteria.list();
				count = count + ((Long) list.get(0));
			}
		}

		return count;
	}
/*
	public FieldPhenoCollection getFieldPhenoCollection(FieldPhenoCollection fieldPhenoCollection) {
		Criteria criteria = getSession().createCriteria(FieldPhenoCollection.class);

		if (fieldPhenoCollection.getField() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_PHENO_COLLECTION_FIELD, fieldPhenoCollection.getField()));
		}
		if (fieldPhenoCollection.getStudy() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_PHENO_COLLECTION_STUDY, fieldPhenoCollection.getStudy()));
		}
		if (fieldPhenoCollection.getPhenoCollection() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.FIELD_PHENO_COLLECTION_PHENO_COLLECTION, fieldPhenoCollection.getPhenoCollection()));
		}

		FieldPhenoCollection aFieldPhenoCollection = null;

		List<FieldPhenoCollection> results = criteria.list();
		if (results.size() > 0)
			aFieldPhenoCollection = (FieldPhenoCollection) results.get(0);

		return aFieldPhenoCollection;
	}

	public void createFieldPhenoCollection(FieldPhenoCollection fieldPhenoCollection) {
		getSession().save(fieldPhenoCollection);
	}

	public void updateFieldPhenoCollection(FieldPhenoCollection fieldPhenoCollection) {
		getSession().update(fieldPhenoCollection);
	}

	public long getStudyFieldDataCount(PhenoCollectionVO phenoCollectionVoCriteria) {
		// Handle for study not in context
		if (phenoCollectionVoCriteria.getStudy() == null) {
			return 0;
		}

		Criteria criteria = buildGeneralCriteria(phenoCollectionVoCriteria);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}

	public List<PhenoCollectionVO> searchPageableFieldData(PhenoCollectionVO phenoCollectionVoCriteria, int first, int count) {
		Criteria criteria = buildGeneralCriteria(phenoCollectionVoCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<FieldData> list = criteria.list();
		List<PhenoCollectionVO> phenoCollectionVOList = new ArrayList<PhenoCollectionVO>();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			PhenoCollectionVO phenoCollectionVO = new PhenoCollectionVO();
			FieldData fieldData = (FieldData) iterator.next();
			phenoCollectionVO.setFieldData(fieldData);
			phenoCollectionVOList.add(phenoCollectionVO);
		}
		return phenoCollectionVOList;
	}

	private Criteria buildGeneralCriteria(PhenoCollectionVO phenoCollectionVo) {
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (phenoCollectionVo.getStudy() != null) {
			criteria.createAlias("field", "f");
			criteria.add(Restrictions.eq("f.study", phenoCollectionVo.getStudy()));
		}

		if (phenoCollectionVo.getFieldData().getId() != null) {
			criteria.add(Restrictions.eq("id", phenoCollectionVo.getFieldData().getId()));
		}

		if (phenoCollectionVo.getFieldData().getCollection() != null) {
			criteria.add(Restrictions.eq("collection", phenoCollectionVo.getFieldData().getCollection()));
		}

		if (phenoCollectionVo.getFieldData().getLinkSubjectStudy() != null && phenoCollectionVo.getFieldData().getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.createAlias("linkSubjectStudy", "lss");
			criteria.add(Restrictions.eq("lss.subjectUID", phenoCollectionVo.getFieldData().getLinkSubjectStudy().getSubjectUID()));
		}

		if (phenoCollectionVo.getFieldData().getField() != null) {
			criteria.add(Restrictions.eq("field", phenoCollectionVo.getFieldData().getField()));
		}

		criteria.addOrder(Order.asc("id"));

		return criteria;
	}
*/
	public DelimiterType getDelimiterType(Long id) {
		DelimiterType delimiterType = (DelimiterType) getSession().get(DelimiterType.class, id);
		return delimiterType;
	}
/*
	public int clearPhenoCollection(PhenoCollection phenoCollection) {
		int rowsDeleted = 0;
		if (phenoCollection != null) {
			try {
				String hqlString = "DELETE FROM FieldData WHERE collection= :phenoCollection";
				Query query = getSession().createQuery(hqlString);
				query.setParameter("phenoCollection", phenoCollection);
				rowsDeleted = query.executeUpdate();

				log.info("Cleared PhenoCollection: " + phenoCollection.getName() + " " + rowsDeleted + " field data rows deleted successfully");

				// Delete PhenoCollectionUploads and FK Upload as well
				List<PhenoCollectionUpload> phenoCollectionUploadList = getPhenoCollectionUploadList(phenoCollection);
				for (Iterator iterator = phenoCollectionUploadList.iterator(); iterator.hasNext();) {
					PhenoCollectionUpload phenoCollectionUpload = (PhenoCollectionUpload) iterator.next();
					deleteCollectionUpload(phenoCollectionUpload);
					deleteUpload(phenoCollectionUpload.getUpload());
				}
			}
			catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return rowsDeleted;
	}
	
	private java.util.List<PhenoCollectionUpload> getPhenoCollectionUploadList(PhenoCollection phenoCollection) {
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (phenoCollection != null) {
			criteria.add(Restrictions.eq("collection", phenoCollection));
		}

		java.util.List<PhenoCollectionUpload> uploadCollection = criteria.list();
		return uploadCollection;
	}

	public java.util.List<BarChartResult> getFieldsWithDataResults(Study study) {
		java.util.List<BarChartResult> results = new ArrayList<BarChartResult>();

		String hql = "SELECT COUNT(DISTINCT fd.field) AS value, 'Collection' AS rowKey, c.name AS columnKey\n" + "FROM FieldData fd \n" + "INNER JOIN fd.collection AS c\n" + "WHERE c.study = :study\n"
				+ " GROUP BY fd.collection";
		Query query = getSession().createQuery(hql);
		query.setParameter("study", study);
		List resultList = query.list();
		if ((resultList != null) && (resultList.size() > 0)) {
			for (Object r : resultList) {
				Object[] obj = (Object[]) r;
				double value = ((Long) obj[0]).doubleValue();
				Comparable rowKey = (Comparable) obj[1];
				Comparable columnKey = (Comparable) obj[2];

				results.add(new BarChartResult(value, rowKey, columnKey));
			}
		}
		return results;
	}

	public boolean fieldHasData(Field field) {
		Criteria criteria = getSession().createCriteria(FieldData.class);

		if (field.getStudy() != null) {
			criteria.createAlias("field", "f");
			criteria.add(Restrictions.eq("f.study", field.getStudy()));
		}

		if (field != null) {
			criteria.add(Restrictions.eq("field", field));
		}

		return criteria.list().size() > 0;
	}
*/
	public boolean phenoCollectionHasData(PhenoCollection phenoCollection) {
		Criteria criteria = getSession().createCriteria(PhenoData.class);

		if (phenoCollection != null) {
			criteria.add(Restrictions.eq("collection", phenoCollection));
		}

		return criteria.list().size() > 0;
	}
/*
	public boolean uploadHasData(PhenoUpload upload) {
		boolean hasData = false;

		// Data dictionary upload
		Criteria criteria = getSession().createCriteria(FieldUpload.class);

		if (upload != null) {
			criteria.add(Restrictions.eq("upload", upload));
			if (criteria.list().size() > 0){
				return true;
			}
				//hasData = true; save execution of big data below
		}

		// field data upload
		criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (upload != null) {
			criteria.add(Restrictions.eq("upload", upload));
			if (criteria.list().size() > 0){
				hasData = true;
			}
		}

		return hasData;
	}

	public PhenoCollection getPhenoCollectionByUpload(PhenoUpload upload) {
		PhenoCollectionUpload phenoCollectionUpload = null;
		Criteria criteria = getSession().createCriteria(PhenoCollectionUpload.class);

		if (upload != null) {
			criteria.add(Restrictions.eq("upload", upload));
			if (criteria.list().size() > 0)
				phenoCollectionUpload = (PhenoCollectionUpload) criteria.list().get(0);
		}

		return phenoCollectionUpload.getCollection();
	}
*/
	public String getDelimiterTypeByDelimiterChar(char delimiterCharacter) {
		String delimiterTypeName = null;
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		criteria.add(Restrictions.eq("delimiterCharacter", delimiterCharacter));

		if (criteria.list().size() > 0) {
			DelimiterType delimiterType = (DelimiterType) criteria.list().get(0);
			delimiterTypeName = delimiterType.getName();
		}
		return delimiterTypeName;
	}

	public FileFormat getFileFormatByName(String name) {
		FileFormat fileFormat = null;
		Criteria criteria = getSession().createCriteria(FileFormat.class);
		criteria.add(Restrictions.eq("name", name));

		if (criteria.list().size() > 0) {
			fileFormat = (FileFormat) criteria.list().get(0);
		}
		return fileFormat;
	}

	public Long isCustomFieldUsed(PhenoData phenoData) {
		Long count = new Long("0");
		CustomField customField = phenoData.getCustomFieldDisplay().getCustomField();

		Study study = customField.getStudy();
		ArkFunction arkFunction = customField.getArkFunction();

		StringBuffer stringBuffer = new StringBuffer();
		
		stringBuffer.append(" SELECT COUNT(*) FROM " + PhenoData.class.getName() + " AS phenoData WHERE EXISTS ");
		stringBuffer.append(" ( ");               
		stringBuffer.append(" SELECT cfd.id FROM " + CustomFieldDisplay.class.getName() + " AS cfd  WHERE cfd.customField.study.id = :studyId");
		stringBuffer.append(" AND cfd.customField.arkFunction.id = :functionId AND phenoData.customFieldDisplay.id = :customFieldDisplayId");
		stringBuffer.append(" )");
		
		String theHQLQuery = stringBuffer.toString();
		
		Query query = getSession().createQuery(theHQLQuery);
		query.setParameter("studyId", study.getId());
		query.setParameter("functionId", arkFunction.getId());
		query.setParameter("customFieldDisplayId", phenoData.getCustomFieldDisplay().getId());
		count = (Long) query.uniqueResult();
			
		return count;
	}

	/**
	 * Create Pheno data
	 */
	public void createPhenoData(PhenoData phenoData) {
		getSession().save(phenoData);
	}

	/**
	 * Delete Pheno data
	 */
	public void deletePhenoData(PhenoData phenoData) {
		getSession().delete(phenoData);
	}

	/**
	 * Update Pheno data
	 */
	public void updatePhenoData(PhenoData phenoData) {
		getSession().update(phenoData);
	}

	public PhenoCollection getPhenoCollection(Long id) {
		return (PhenoCollection) getSession().get(PhenoCollection.class, id);
	}

	public long getPhenoDataCount(PhenoCollection phenoCollection) {
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.createAlias("customFieldGroup", "qnaire");
		criteria.add(Restrictions.eq("qnaire.id", phenoCollection.getQuestionnaire().getId()));
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}

	public List<PhenoData> getPhenoDataList(PhenoCollection phenoCollection, int first, int count) {
		
		List<PhenoData> phenoDataList = new ArrayList<PhenoData>();

		// The following HQL is based on this SQL, except that we don't need the CustomField
//		SELECT *
//		  FROM study.custom_field cf
//		 INNER JOIN study.custom_field_display cfd
//		    ON cfd.custom_field_id = cf.id
//		 INNER JOIN study.custom_field_group cfg
//		    ON cfd.custom_field_group_id = cfg.id
//		 INNER JOIN pheno.pheno_collection pc
//		    ON pc.custom_field_group_id = cfg.id
//		  LEFT JOIN pheno.pheno_data pd
//		    ON pd.custom_field_display_id = cfd.id
//		   AND pd.pheno_collection_id = pc.id
//		 WHERE pc.id = 1;

		/*		
		 * WARNING: Do no try to do a HQL WITH clause between "pd.phenoCollection.id" and another table's column!
		 * Even though it looks like it should work in SQL (as above), for an unknown reason HQL parsing will fail - like this:
		 * ----
		 * Caused by: org.hibernate.hql.ast.QuerySyntaxException: with-clause referenced two different from-clause elements 
		 * [  FROM au.org.theark.core.model.study.entity.CustomFieldDisplay AS cfd  
		 * INNER JOIN cfd.customFieldGroup cfg  
		 * INNER JOIN cfg.phenoCollection pc   
		 * LEFT JOIN cfd.phenoData AS pd   
		 * WITH pd.phenoCollection.id = pc.id 
		 * WHERE pc.id = :pcId ORDER BY cfd.sequence ]
		 * ----
		 * Thus the present work-around is to use an argument "pcId" for the WITH criteria.
		 */
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cfd, pd ");
		sb.append("  FROM " + CustomFieldDisplay.class.getName() + " AS cfd ");
		sb.append(" INNER JOIN cfd.customFieldGroup qnaire ");
		sb.append(" INNER JOIN qnaire.phenoCollection pc ");
		sb.append("  LEFT JOIN cfd.phenoData AS pd ");
		sb.append("  WITH pd.phenoCollection.id = :pcId ");
		sb.append(" WHERE pc.id = :pcId ");
		sb.append(" ORDER BY cfd.sequence ");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("pcId", phenoCollection.getId());
		query.setFirstResult(first);
		query.setMaxResults(count);
		
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			CustomFieldDisplay cfd = new CustomFieldDisplay();
			PhenoData phenoData = new PhenoData();
			if (objects.length > 0 && objects.length >= 1) {
				
					cfd = (CustomFieldDisplay)objects[0];
					if (objects[1] != null) {
						phenoData = (PhenoData)objects[1];
					} 
					else {
						phenoData.setCustomFieldDisplay(cfd);
					}
					phenoDataList.add(phenoData);	
			}
		}
		return phenoDataList;
	}
	
	/**
	 * Create  a CustomFieldGroup and then link the selected custom fields into the Group via
	 * the CustomFieldDisplay. For each Custom Field create a new CustomFieldDisplay
	 * @param customFieldGroupVO
	 */
	public void createCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO) throws ArkSystemException{
		
		CustomFieldGroup customFieldGroup = customFieldGroupVO.getCustomFieldGroup();
		Session session = getSession();
		if(customFieldGroup.getPublished() == null){
			customFieldGroup.setPublished( new Boolean("false"));
		}
		session.save(customFieldGroup);
		ArrayList<CustomField> customFieldList = customFieldGroupVO.getSelectedCustomFields();
		
		int fieldposition = 0;
		for (CustomField customField : customFieldList) {
			++fieldposition;
			CustomFieldDisplay customFieldDisplay = new CustomFieldDisplay();
			customFieldDisplay.setCustomFieldGroup(customFieldGroup);
			customFieldDisplay.setCustomField(customField);
			customFieldDisplay.setSequence( new Long(fieldposition));
			session.save(customFieldDisplay);
			log.debug("Saved CustomFieldDisplay for Custom Field Group");
		}
	}

	public long getPhenoCollectionCount(PhenoDataCollectionVO collectionCriteria) {
		Criteria criteria = getSession().createCriteria(PhenoCollection.class);
		criteria.createAlias("questionnaire", "qnaire");
		criteria.add(Restrictions.eq("linkSubjectStudy", collectionCriteria.getPhenoCollection().getLinkSubjectStudy()));
		// Just a precaution (PhenoCollection to should always map to a CustomFieldGroup where the ArkFunction will correspond to Pheno) 
		//criteria.add(Restrictions.eq("qnaire.arkFunction", collectionCriteria.getArkFunction()));	
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count;
	}

	public List<PhenoCollection> searchPageablePhenoCollection(PhenoDataCollectionVO collectionCriteria, int first, int count) {
		
		List<PhenoCollection> resultList = new ArrayList<PhenoCollection>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT qnaire, pc ");
		sb.append("  FROM " + CustomFieldGroup.class.getName() + " AS qnaire ");
		sb.append("  LEFT JOIN qnaire.phenoCollection as pc ");
		sb.append("  WITH pc.linkSubjectStudy.id = :subjectId ");
		sb.append(" WHERE qnaire.study.id = :studyId " );
		//sb.append("   AND qnaire.arkFunction.id = :functionId ");
		sb.append("   AND qnaire.published = true ");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("subjectId", collectionCriteria.getPhenoCollection().getLinkSubjectStudy().getId());
		query.setParameter("studyId", collectionCriteria.getCustomFieldGroup().getStudy().getId());
		//log.info("colcrit ark=" + collectionCriteria.getArkFunction());
		//long id = collectionCriteria.getArkFunction().getId();
		//log.info("id=" + id);
		//query.setParameter("functionId",id);
		query.setFirstResult(first);
		query.setMaxResults(count);
		
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			CustomFieldGroup questionnaire = new CustomFieldGroup();
			PhenoCollection pc = new PhenoCollection();
			if (objects.length > 0 && objects.length >= 1) {
				questionnaire = (CustomFieldGroup)objects[0];
				if (objects[1] != null){
					pc = (PhenoCollection)objects[1];
				} else {
					pc.setQuestionnaire(questionnaire);
				}
				resultList.add(pc);	
			}
		}
		Criteria criteria = getSession().createCriteria(PhenoCollection.class);
		criteria.createAlias("questionnaire", "qnaire");
		criteria.add(Restrictions.eq("linkSubjectStudy", collectionCriteria.getPhenoCollection().getLinkSubjectStudy()));
		// Just a precaution (PhenoCollection to should always map to a CustomFieldGroup where the ArkFunction will correspond to Pheno) 
		criteria.add(Restrictions.eq("qnaire.arkFunction", collectionCriteria.getArkFunction()));	
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		resultList = criteria.list();
		return resultList;
	}
	
	public List<CustomField> getCustomFieldsLinkedToCustomFieldGroup(CustomFieldGroup customFieldCriteria){
		
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.add(Restrictions.eq("customFieldGroup",customFieldCriteria));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("customField"));
		criteria.setProjection(projectionList);
		criteria.addOrder(Order.asc("sequence"));
		List<CustomField> fieldsList = criteria.list();
		//log.warn("______________customFieldsList = " + fieldsList.size());
		return fieldsList;
		
	}

	public List<QuestionnaireStatus> getPhenoCollectionStatusList() {
		List<QuestionnaireStatus> resultList = new ArrayList<QuestionnaireStatus>(0);
		Criteria criteria = getSession().createCriteria(QuestionnaireStatus.class);
		resultList = criteria.list();
		return resultList;
	}
	
	private List<CustomFieldDisplay> getCustomFieldDisplayForCustomFieldGroup(CustomFieldGroup customFieldGroup){
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.add(Restrictions.eq("customFieldGroup",customFieldGroup));
		return criteria.list();
	}
	
	/**
	 * Update CustomFieldGroup and its related CustomFields(Add or remove)
	 */
	public void updateCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO) throws EntityExistsException,ArkSystemException{
		
		CustomFieldGroup customFieldGroup = customFieldGroupVO.getCustomFieldGroup();
		Session session = getSession();
		session.update(customFieldGroup);//Update
		
		if(!customFieldGroup.getPublished()){//Allow Removal only if the form is not published
			Collection<CustomFieldDisplay> customFieldDisplayToRemove = getCustomFieldDisplayToRemove(customFieldGroupVO.getSelectedCustomFields(), customFieldGroup);	
			for (CustomFieldDisplay cfd : customFieldDisplayToRemove) {
				session.delete(cfd);
			}
		}
	
		ArrayList<CustomFieldDisplay> customFieldsToAdd = getCustomFieldsToAdd(customFieldGroupVO.getSelectedCustomFields(), customFieldGroup);
		for (CustomFieldDisplay fieldToAdd : customFieldsToAdd) {
			session.saveOrUpdate(fieldToAdd);//Add a new CustomFieldDisplay field that is linked to the CustomField	
		}
		
		ArrayList<CustomField> list = customFieldGroupVO.getSelectedCustomFields();
		int position = 0;
		
		for (CustomField customField : list) {
			++position;
			CustomFieldDisplay cfd = iArkCommonService.getCustomFieldDisplayByCustomField(customField,customFieldGroupVO.getCustomFieldGroup());
			cfd.setSequence(new Long(position));
			session.update(cfd);
		}
	
		
	}
	
	/**
	 * Creates Collection that will contain the list of new CustomFields that must be added/linked to the CustomFieldGroup
	 * @param selectedCustomFields
	 * @param customFieldGroup
	 * @return Collection<CustomField>
	 */
	private ArrayList<CustomFieldDisplay> getCustomFieldsToAdd(Collection<CustomField> selectedCustomFields, CustomFieldGroup customFieldGroup){
		
		ArrayList<CustomFieldDisplay> cfdisplayList = new ArrayList<CustomFieldDisplay>();
		List<CustomField> existingCustomFieldList = getCustomFieldsLinkedToCustomFieldGroup(customFieldGroup);// Existing List of CustomFieldsthat were linked to this CustomFieldGroup
		ArrayList<CustomField> nonProxyCustomFieldList = new ArrayList<CustomField>();
		
		/**
		 * Note:
		 * getCustomFieldsLinkedToCustomFieldGroup() returns a projected List representing CustomField from CustomFieldDisplay. Since CustomField was a lazily loaded object, it is represented as a proxy object.
		 * For us to do a comparison using contains the equals() will fail when the class is compared. To be able to do that we convert to an underlying object before we do the final comparison.
		 * Since Hibernate returns proxy objects for LazyInitialisation when the equals() is invoked the class comparison will fail. 
		 */
		
		for (Object obj : existingCustomFieldList) {
			if(obj instanceof HibernateProxy){
				CustomField  cf = (CustomField)((HibernateProxy)obj).getHibernateLazyInitializer().getImplementation();
				nonProxyCustomFieldList.add(cf);
			}
		}

		for (CustomField customField : selectedCustomFields) {
			if((!nonProxyCustomFieldList.contains(customField))){
				
				CustomFieldDisplay customFieldDisplay = new CustomFieldDisplay();
				customFieldDisplay.setCustomFieldGroup(customFieldGroup);
				customFieldDisplay.setCustomField(customField);
				cfdisplayList.add(customFieldDisplay);
			}else{
				//Retrieve the customField for the sequence could have changed
				//String name = customField.getName();
				CustomFieldDisplay cfd = iArkCommonService.getCustomFieldDisplayByCustomField(customField);
				cfdisplayList.add(cfd);
			}
		}
		return cfdisplayList;
	}
	
	/**
	 * Determine the list of CustomField that was linked to this CustomFieldGroup and is not used by anyone and then if this is true add it to a list that will be processed later
	 * for removal.
	 * @param selectedCustomFields
	 * @param customFieldGroup
	 * @return
	 */
	private Collection<CustomFieldDisplay> getCustomFieldDisplayToRemove(Collection<CustomField> selectedCustomFields, CustomFieldGroup customFieldGroup){
		
		Collection<CustomFieldDisplay> customFieldDisplayList = getCustomFieldDisplayForCustomFieldGroup(customFieldGroup);
		Collection<CustomFieldDisplay> customFieldDisplayToRemove = new ArrayList<CustomFieldDisplay>();
		for (CustomFieldDisplay existingCustomFieldDisplay : customFieldDisplayList) {
			
			if(existingCustomFieldDisplay.getCustomField() instanceof HibernateProxy){
				CustomField  cf = (CustomField)((HibernateProxy)existingCustomFieldDisplay.getCustomField()).getHibernateLazyInitializer().getImplementation();
				if(!selectedCustomFields.contains(cf)){
					customFieldDisplayToRemove.add(existingCustomFieldDisplay);	
				}
			}
		}
		return customFieldDisplayToRemove;
	}
	
	public Collection<CustomFieldDisplay> getCFDLinkedToQuestionnaire(CustomFieldGroup customFieldGroup, int first, int count){
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.add(Restrictions.eq("customFieldGroup",customFieldGroup));
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		criteria.addOrder(Order.asc("sequence"));
		return criteria.list();
		
	}
	
	public long getCFDLinkedToQuestionnaireCount(CustomFieldGroup customFieldGroup){
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.add(Restrictions.eq("customFieldGroup",customFieldGroup));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}

	public void createPhenoCollection(PhenoCollection phenoCollection) {
		getSession().save(phenoCollection);
	}

	public void updatePhenoCollection(PhenoCollection phenoCollection) {
		getSession().update(phenoCollection);
	}
	
	public void deletePhenoCollectionRegardlessOfData(PhenoCollection phenoCollection) {
		// This relies on CASCADE ON DELETE on the database [pheno].[pheno_data] table
		getSession().delete(phenoCollection);
	}
	
	public void deleteCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO){
		//Delete all the CustomFieldDisplay Items linked to the Group
		Session session = getSession();
		Collection<CustomFieldDisplay> customFieldDisplayList = getCustomFieldDisplayForCustomFieldGroup(customFieldGroupVO.getCustomFieldGroup());
		for (CustomFieldDisplay customFieldDisplay : customFieldDisplayList) {
			session.delete(customFieldDisplay);
		}
		session.delete(customFieldGroupVO.getCustomFieldGroup());
	}
	
	/**
	 * The method checks if the given questionnaire's fields have data linked to it.
	 * 
	 * @param customFieldGroup
	 */
	public void isDataAvailableForQuestionnaire(CustomFieldGroup customFieldGroup){
		
		Criteria criteria = getSession().createCriteria(CustomField.class, "cf");
		criteria.createAlias("customFieldDisplay", "cfd", JoinType.LEFT_OUTER_JOIN);	// Left join to CustomFieldDisplay
		criteria.createAlias("cfd.customFieldGroup", "cfg", JoinType.LEFT_OUTER_JOIN); // Left join to CustomFieldGroup
		criteria.add(Restrictions.eq("cf.study", customFieldGroup.getStudy()));
		
		ArkFunction function = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		criteria.add(Restrictions.eq("cf.arkFunction", function));
		criteria.add(Restrictions.eq("cfg.id", customFieldGroup.getId()));
		
		DetachedCriteria fieldDataCriteria = DetachedCriteria.forClass(PhenoData.class, "pd");
		// Join CustomFieldDisplay and PhenoData on ID FK
		fieldDataCriteria.add(Property.forName("cfd.id").eqProperty("pd." + "customFieldDisplay.id"));
		criteria.add(Subqueries.exists(fieldDataCriteria.setProjection(Projections.property("pd.customFieldDisplay"))));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("cfg.name"), "questionnaire");
		projectionList.add(Projections.property("cf.name"), "fieldName");
		projectionList.add(Projections.property("cf.description"), "description");
		
		
	}

	public QuestionnaireStatus getPhenoCollectionStatusByName(String statusName) {
		Criteria criteria = getSession().createCriteria(QuestionnaireStatus.class);
		criteria.add(Restrictions.eq("name", statusName).ignoreCase());
		QuestionnaireStatus result = (QuestionnaireStatus)criteria.uniqueResult();
		return result;
	}
	
	public java.util.Collection<Upload> searchUpload(Upload upload) {
		Criteria criteria = getSession().createCriteria(Upload.class);

		if (upload.getId() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.UPLOAD_ID, upload.getId()));
		}

		if (upload.getStudy() != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.UPLOAD_STUDY, upload.getStudy()));
		}
		
		if(upload.getArkFunction() != null){
			criteria.add(Restrictions.eq("arkFunction",upload.getArkFunction()));
		}

		if (upload.getFileFormat() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_FILE_FORMAT, upload.getFileFormat()));
		}

		if (upload.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_DELIMITER_TYPE, upload.getDelimiterType()));
		}

		if (upload.getFilename() != null) {
			criteria.add(Restrictions.ilike(au.org.theark.phenotypic.web.Constants.UPLOAD_FILENAME, upload.getFilename()));
		}
		
		criteria.addOrder(Order.desc(au.org.theark.phenotypic.web.Constants.UPLOAD_ID));
		java.util.Collection<Upload> uploadCollection = criteria.list();

		return uploadCollection;
	}
	
	public void deleteUpload(Upload studyUpload){
		//TODO if the upload was successful it must stop the user from removing an uploaded file
		getSession().delete(studyUpload);
	}
	
	public Upload getUpload(Long id){
		return (Upload)getSession().get(Upload.class, id);
	}	

	public Collection<CustomFieldGroup> getCustomFieldGroupList(Study study){
		Criteria criteria = getSession().createCriteria(CustomFieldGroup.class);
		criteria.add(Restrictions.eq("study", study));
		Collection<CustomFieldGroup>  result = criteria.list();
		return result;
	}
	
	public void processPhenoCollectionsWithTheirDataToInsertBatch(List<PhenoCollection> phenoCollectionsWithTheirDataToInsert, Study study){
		Session session = getSession();
//		int count = 0;
		for(PhenoCollection collectionToInsert : phenoCollectionsWithTheirDataToInsert){
			//TODO : investigate more efficient way to deal with null parent entity
			Set<PhenoData> dataToSave = collectionToInsert.getPhenoData();
			collectionToInsert.setPhenoData(new HashSet<PhenoData>());
			
			session.save(collectionToInsert);
			session.refresh(collectionToInsert);
			for(PhenoData data : dataToSave){
				data.setPhenoCollection(collectionToInsert);
				session.save(data);
			}
		}
		session.flush();
		session.clear();
	}

}
