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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.pheno.entity.LinkPhenoDataSetCategoryField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.PhenoDataSetFieldCategoryUpload;
import au.org.theark.core.model.study.entity.PhenoFieldUpload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;

@SuppressWarnings("unchecked")
@Repository("phenotypicDao")
public class PhenotypicDao extends HibernateSessionDao implements IPhenotypicDao {
	static Logger		log	= LoggerFactory.getLogger(PhenotypicDao.class);

	private IArkCommonService<Void>	iArkCommonService;
	@Autowired
	public void setiArkCommonService(IArkCommonService<Void> iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	public java.util.Collection<PhenoDataSetCollection> getPhenoCollectionByStudy(Study study) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class);

		if (study != null) {
			criteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_STUDY, study));
		}
		criteria.addOrder(Order.asc("name"));

		java.util.List<PhenoDataSetCollection> collectionList = criteria.list();
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
			collectionCriteria.add(Restrictions.eq(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_STUDY, collectionTPickedPhenoDataSetCategoryoMatch.getStudy()));
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

	public void deletePhenoCollection(PhenoDataSetCollection collection) {
		getSession().delete(collection);
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
			Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class);
			criteria.add(Restrictions.eq("study", study));

			java.util.Collection<PhenoDataSetCollection> phenoCollection = criteria.list();
			count = phenoCollection.size();
		}

		return count;
	}

	public long getCountOfCollectionsWithDataInStudy(Study study) {
		long count = 0;

		if (study.getId() != null) {
			Collection<PhenoDataSetCollection> phenoCollectionColn = getPhenoCollectionByStudy(study);

			for (Iterator iterator = phenoCollectionColn.iterator(); iterator.hasNext();) {
				PhenoDataSetCollection phenoCollection = (PhenoDataSetCollection) iterator.next();

				Criteria criteria = getSession().createCriteria(PhenoDataSetData.class);
				criteria.add(Restrictions.eq("phenCollection", phenoCollection));
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
			criteria.add(Restrictions.eq("phenoCollection", phenoCollectionVo.getFieldData().getCollection()));
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
			criteria.add(Restrictions.eq("phenoCollection", phenoCollection));
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
	public boolean phenoCollectionHasData(PhenoDataSetCollection phenoCollection) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetData.class);

		if (phenoCollection != null) {
			criteria.add(Restrictions.eq("phenoCollection", phenoCollection));
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

	public Long isCustomFieldUsed(PhenoDataSetData phenoData) {
		Long count = new Long("0");
		PhenoDataSetField phenoDataSetField = phenoData.getPhenoDataSetFieldDisplay().getPhenoDataSetField();
		
		Study study = phenoDataSetField.getStudy();
		ArkFunction arkFunction = phenoDataSetField.getArkFunction();
		
		Criteria criteria = getSession().createCriteria(PhenoDataSetData.class, "pd");
		criteria.createAlias("pd.customFieldDisplay", "cfd");
		criteria.createAlias("cfd.customField", "cf");
		criteria.createAlias("cf.arkFunction", "aF");
		criteria.createAlias("cf.study", "s");
		criteria.add(Restrictions.eq("aF.id", arkFunction.getId()));
		criteria.add(Restrictions.eq("cfd.id", phenoData.getPhenoDataSetFieldDisplay().getId()));
		criteria.add(Restrictions.eq("s.id", study.getId()));
		
		count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
				
		return count;
	}

	/**
	 * Create Pheno data
	 */
	public void createPhenoData(PhenoDataSetData phenoData) {
		getSession().save(phenoData);
	}

	/**
	 * Delete Pheno data
	 */
	public void deletePhenoData(PhenoDataSetData phenoData) {
		getSession().delete(phenoData);
	}

	/**
	 * Update Pheno data
	 */
	public void updatePhenoData(PhenoDataSetData phenoData) {
		getSession().update(phenoData);
	}

	public PhenoDataSetCollection getPhenoCollection(Long id) {
		return (PhenoDataSetCollection) getSession().get(PhenoDataSetCollection.class, id);
	}

	public long getPhenoDataCount(PhenoDataSetCollection phenoCollection,PhenoDataSetCategory phenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.createAlias("phenoDataSetGroup", "qnaire");
		if(phenoCollection.getQuestionnaire()!=null){
			criteria.add(Restrictions.eq("qnaire.id", phenoCollection.getQuestionnaire().getId()));
		}
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}

	public List<PhenoDataSetData> getPhenoDataList(PhenoDataSetCollection phenoCollection,PhenoDataSetCategory phenoDataSetCategory, int first, int count) {
		
		List<PhenoDataSetData> phenoDataList = new ArrayList<PhenoDataSetData>();

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
		sb.append("SELECT pdsfd, pdsd ");
		sb.append("  FROM PhenoDataSetFieldDisplay AS pdsfd ");
		sb.append(" INNER JOIN pdsfd.phenoDataSetGroup AS pdsg ");
		sb.append(" INNER JOIN pdsg.phenoDataSetCollections pdsc ");
		sb.append("  LEFT JOIN pdsfd.phenoDataSetData AS pdsd ");
		sb.append("  WITH pdsd.phenoDataSetCollection.id = :pcId ");
		sb.append(" WHERE pdsc.id = :pcId ");
		sb.append(" and pdsfd.phenoDataSetCategory = :phenoDataSetCategory ");
		sb.append(" and pdsfd.phenoDataSetField is not null ");
		sb.append(" ORDER BY pdsfd.phenoDataSetFiledOrderNumber ");
		

		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("pcId", phenoCollection.getId());
		query.setParameter("phenoDataSetCategory", phenoDataSetCategory);
		query.setFirstResult(first);
		query.setMaxResults(count);
		
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			PhenoDataSetFieldDisplay pfd = new PhenoDataSetFieldDisplay();
			PhenoDataSetData phenoData = new PhenoDataSetData();
			if (objects.length > 0 && objects.length >= 1) {
				pfd = (PhenoDataSetFieldDisplay)objects[0];
					if (objects[1] != null) {
						phenoData = (PhenoDataSetData)objects[1];
					} 
					else {
						phenoData.setPhenoDataSetFieldDisplay(pfd);
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
		Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class);
		criteria.createAlias("questionnaire", "qnaire");
		criteria.add(Restrictions.eq("linkSubjectStudy", collectionCriteria.getPhenoDataSetCollection().getLinkSubjectStudy()));
		// Just a precaution (PhenoCollection to should always map to a CustomFieldGroup where the ArkFunction will correspond to Pheno) 
		//criteria.add(Restrictions.eq("qnaire.arkFunction", collectionCriteria.getArkFunction()));	
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count;
	}

	public List<PhenoDataSetCollection> searchPageablePhenoCollection(PhenoDataCollectionVO collectionCriteria, int first, int count) {
		
		List<PhenoDataSetCollection> resultList = new ArrayList<PhenoDataSetCollection>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT qnaire, pc ");
		sb.append("  FROM " + PhenoDataSetGroup.class.getName() + " AS qnaire ");
		sb.append("  LEFT JOIN qnaire.phenoDataSetCollections as pc ");
		sb.append("  WITH pc.linkSubjectStudy.id = :subjectId ");
		sb.append(" WHERE qnaire.study.id = :studyId " );
		//sb.append("   AND qnaire.arkFunction.id = :functionId ");
		sb.append("   AND qnaire.published = true ");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("subjectId", collectionCriteria.getPhenoDataSetCollection().getLinkSubjectStudy().getId());
		query.setParameter("studyId", collectionCriteria.getPhenoDataSetGroup().getStudy().getId());
		//log.info("colcrit ark=" + collectionCriteria.getArkFunction());
		//long id = collectionCriteria.getArkFunction().getId();
		//log.info("id=" + id);
		//query.setParameter("functionId",id);
		query.setFirstResult(first);
		query.setMaxResults(count);
		
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			//CustomFieldGroup questionnaire = new CustomFieldGroup();
			PhenoDataSetGroup questionnaire = new PhenoDataSetGroup();
			PhenoDataSetCollection pc = new PhenoDataSetCollection();
			if (objects.length > 0 && objects.length >= 1) {
				questionnaire = (PhenoDataSetGroup)objects[0];
				if (objects[1] != null){
					pc = (PhenoDataSetCollection)objects[1];
				} else {
					pc.setQuestionnaire(questionnaire);
				}
				resultList.add(pc);	
			}
		}
		Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class);
		criteria.createAlias("questionnaire", "qnaire");
		criteria.add(Restrictions.eq("linkSubjectStudy", collectionCriteria.getPhenoDataSetCollection().getLinkSubjectStudy()));
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
		criteria.addOrder(Order.asc("sequence"));
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
	
	public Collection<PhenoDataSetFieldDisplay> getCFDLinkedToQuestionnaire(PhenoDataSetGroup phenoDataSetGroup, int first, int count){
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroup));
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		criteria.addOrder(Order.asc("phenoDataSetFiledOrderNumber"));
		return criteria.list();
		
	}
	
	public long getCFDLinkedToQuestionnaireCount(PhenoDataSetGroup phenoDataSetGroup){
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroup));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}

	public void createPhenoCollection(PhenoDataSetCollection phenoCollection) {
		getSession().save(phenoCollection);
	}

	public void updatePhenoCollection(PhenoDataSetCollection phenoCollection) {
		getSession().update(phenoCollection);
	}
	
	public void deletePhenoCollectionRegardlessOfData(PhenoDataSetCollection phenoCollection) {
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
		
		DetachedCriteria fieldDataCriteria = DetachedCriteria.forClass(PhenoDataSetData.class, "pd");
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
	
	public void processPhenoCollectionsWithTheirDataToInsertBatch(List<PhenoDataSetCollection> phenoCollectionsWithTheirDataToInsert, Study study){
		Session session = getSession();
//		int count = 0;
		for(PhenoDataSetCollection collectionToInsert : phenoCollectionsWithTheirDataToInsert){
			//TODO : investigate more efficient way to deal with null parent entity
			Set<PhenoDataSetData> dataToSave = collectionToInsert.getPhenoDataSetData();
			collectionToInsert.setPhenoDataSetData(new HashSet<PhenoDataSetData>());
			
			session.save(collectionToInsert);
			session.refresh(collectionToInsert);
			for(PhenoDataSetData data : dataToSave){
				data.setPhenoDataSetCollection(collectionToInsert);
				session.save(data);
			}
		}
		session.flush();
		session.clear();
	}

	/**
	 * 
	 */
	public List<List<String>>  getPhenoDataAsMatrix (Study study, List<String> subjectUids, List<PhenoDataSetField> phenoDataSetFields, List<PhenoDataSetGroup> phenoDataSetGroups,PhenoDataSetCategory phenoDataSetCategory) {
		List<List<String>>  dataSet = new ArrayList<List<String>>();
		StringBuffer dataHQLquery = new StringBuffer();
		StringBuffer noDataHQLquery = new StringBuffer();
		StringBuffer phenoFieldColumnSQL = new StringBuffer();
		List<String> header = new ArrayList<String>(0);
		
		//stringBuffer.append("SELECT data.* FROM (\n");
		//ARK-799
//		dataHQLquery.append("SELECT lss.subjectUID, pc.recordDate, pc.description, \n"); 
		dataHQLquery.append("SELECT lss.subjectUID, pdsc.recordDate, \n"); 
		noDataHQLquery.append("SELECT lss.subjectUID, cast(null as char) AS recordDate, cast(null as char) AS name, \n"); 
		header.add("SUBJECTUID");
		header.add("RECORD_DATE");
		//ARK-799
//		header.add("COLLECTION");
		
		// Loop for all custom goups
		for(PhenoDataSetGroup pdsg : phenoDataSetGroups) {
			// Get all custom fields for the group and create pivot SQL to create column
			//for(PhenoDataSetFieldDisplay pdsfd : getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroup(pdsg)) {
			for(PhenoDataSetField pdsfd : getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroupAndPhenoDataSetCategory(pdsg,phenoDataSetCategory)) {
			
				//MAX(IF(custom_field_display_id = 14, pd.number_data_value, NULL)) AS cfd14,
				phenoFieldColumnSQL.append("(MAX(CASE WHEN pdsd.phenoDataSetFieldDisplay.id = ");
				phenoFieldColumnSQL.append(getPhenoDataSetFieldDisplayByPhenoDataSetFieldAndGroup(pdsfd,pdsg).getId());
				
				// Determine field type and append SQL accordingly
				if(pdsfd.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					phenoFieldColumnSQL.append(" THEN pdsd.dateDataValue ELSE NULL END) ");
				}
				if(pdsfd.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					phenoFieldColumnSQL.append(" THEN pdsd.numberDataValue ELSE NULL END) ");
				}
				if (pdsfd.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					phenoFieldColumnSQL.append(" THEN pdsd.textDataValue ELSE NULL END) ");
				}
				
				phenoFieldColumnSQL.append(") ");
				phenoFieldColumnSQL.append(",");
				
				noDataHQLquery.append("cast(null as char) ");
				noDataHQLquery.append(",");
				
				header.add(pdsfd.getName().toUpperCase());
			}
		}
		// Remove erroneous ',' char from end of strings
		if(phenoFieldColumnSQL.length() > 0) {
			phenoFieldColumnSQL.setLength(phenoFieldColumnSQL.length()-1);
			noDataHQLquery.setLength(noDataHQLquery.length()-1);
			dataHQLquery.append(phenoFieldColumnSQL);
			
			dataHQLquery.append("\nFROM \n");
			dataHQLquery.append(" PhenoDataSetData pdsd, ");
			dataHQLquery.append(" PhenoDataSetCollection pdsc, ");
			dataHQLquery.append(" LinkSubjectStudy lss, ");
			dataHQLquery.append(" PhenoDataSetFieldDisplay pdsfd \n");
			dataHQLquery.append(" WHERE pdsd.phenoDataSetCollection.id = pdsc.id \n");
			dataHQLquery.append(" AND pdsc.linkSubjectStudy.id = lss.id \n");
			dataHQLquery.append(" AND lss.study = :study \n");
			dataHQLquery.append(" AND lss.subjectUID IN (:subjectUids) \n");
			dataHQLquery.append(" AND pdsfd.phenoDataSetGroup in (:phenoDataSetGroups) \n");
			dataHQLquery.append(" AND pdsd.phenoDataSetFieldDisplay.id = pdsfd.id \n");
			dataHQLquery.append("GROUP BY lss.subjectUID, pdsd.phenoDataSetCollection");
			
			noDataHQLquery.append("\nFROM LinkSubjectStudy lss\n");
			noDataHQLquery.append("WHERE lss.study = :study \n");
			noDataHQLquery.append("AND lss.id NOT IN (SELECT pdsc.linkSubjectStudy.id FROM PhenoDataSetCollection pdsc WHERE pdsc.questionnaire IN (:phenoDataSetGroups))\n");
			
			String hqlQuery = dataHQLquery.toString();
			
			Session session = getSession();
			
			Query dataQuery = session.createQuery(hqlQuery);
			dataQuery.setParameter("study", study);
			dataQuery.setParameterList("subjectUids", subjectUids);
			dataQuery.setParameterList("phenoDataSetGroups", phenoDataSetGroups);
			
			// Add header as first list item
			dataSet.add(header);
			// Add data
			//ArrayList<List<String>> dataList = new ArrayList<List<String>>();
			//dataList = (ArrayList<List<String>>) dataQuery.list();
			
			//This result set contains a List of Object arrays���each array represents one set of properties
	      Iterator it=dataQuery.iterate();
	      while (it.hasNext()) {
	          Object[] val = (Object[]) it.next();
	          List<String> stringList = new ArrayList<String>();
	          for(Object o : val) {
	         	 stringList.add(o !=null ? o.toString() : new String());
	          }
	          dataSet.add(stringList);
	      }
			
			
			
			hqlQuery = noDataHQLquery.toString();
			
			Query noDataQuery = session.createQuery(hqlQuery);
			noDataQuery.setParameter("study", study);
			noDataQuery.setParameterList("phenoDataSetGroups", phenoDataSetGroups);
			//noDataQuery.list();
			//dataSet.addAll(noDataQuery.list());
		}
		return dataSet;
	}
	
	public List<PhenoDataSetGroup> getPhenoDataSetGroupsByLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class);
		criteria.add(Restrictions.eq("linkSubjectStudy", linkSubjectStudy));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("questionnaire"), "questionnaire");
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(PhenoDataSetCollection.class));
		List<PhenoDataSetCollection>  phenoDataSetCollections = (List<PhenoDataSetCollection>)criteria.list();
		List<PhenoDataSetGroup> phenoDataSetGroups=new ArrayList<PhenoDataSetGroup>();
		for (PhenoDataSetCollection phenoDataSetCollection : phenoDataSetCollections) {
			phenoDataSetGroups.add(phenoDataSetCollection.getQuestionnaire());
		}
		return phenoDataSetGroups;
	}

	public CustomFieldGroup getCustomFieldGroupByNameAndStudy(String name, Study study) {
		Criteria criteria = getSession().createCriteria(CustomFieldGroup.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("study", study));
		
		CustomFieldGroup  result = null; 
		result = (CustomFieldGroup) criteria.uniqueResult();
		return result;
	}

	public PhenoDataSetGroup getPhenoFieldGroupById(Long id) {
		PhenoDataSetGroup phenoDataSetGroup = (PhenoDataSetGroup) getSession().get(PhenoDataSetGroup.class, id);
		return phenoDataSetGroup;
	}

	public List<PhenoDataSetCollection> getSubjectMatchingPhenoCollections(LinkSubjectStudy subject, PhenoDataSetGroup phenoDataSetGroup,Date recordDate) {
		log.info("subject " + subject.getSubjectUID());
		log.info("phenoDataSetGroup " + phenoDataSetGroup.getName());
		log.info("date: " + recordDate);
		Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class);
		criteria.add(Restrictions.eq("linkSubjectStudy", subject));
		criteria.add(Restrictions.eq("questionnaire", phenoDataSetGroup));
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(recordDate);
		
		//Removing the "Time" section of the Dates as that's not important in this context
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date low = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date high = cal.getTime();
		criteria.add(Restrictions.lt("recordDate", high));
		criteria.add(Restrictions.ge("recordDate", low));
		
		return criteria.list();
	}

	@Override
	public PhenoDataSetCategory getPhenoDataSetCategory(Long id) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.setMaxResults(1);
		return (PhenoDataSetCategory)criteria.uniqueResult();
	}

	@Override
	public List<PhenoDataSetCategory> getAvailableAllCategoryList(Study study,ArkFunction arkFunction) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		List<PhenoDataSetCategory> phenoDataSetCategoryList = (List<PhenoDataSetCategory>) criteria.list();
		return phenoDataSetCategoryList;
	}

	@Override
	public long getPhenoDataSetCategoryCount(PhenoDataSetCategory phenoDataSetCategoryCriteria) {
		// Handle for study or function not in context
		if (phenoDataSetCategoryCriteria.getStudy() == null || phenoDataSetCategoryCriteria.getArkFunction() == null) {
			return 0;
		}
		Criteria criteria = buildGeneralPhenoDataSetCategoryCritera(phenoDataSetCategoryCriteria);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}
	
	protected Criteria buildGeneralPhenoDataSetCategoryCritera(PhenoDataSetCategory phenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		
		// Must be constrained on study and function
		criteria.add(Restrictions.eq("study", phenoDataSetCategory.getStudy()));
		
		criteria.add(Restrictions.eq("arkFunction", phenoDataSetCategory.getArkFunction()));
		
		if (phenoDataSetCategory.getId() != null) {
			criteria.add(Restrictions.eq("id", phenoDataSetCategory.getId()));
		}
	
		if (phenoDataSetCategory.getName() != null) {
			criteria.add(Restrictions.ilike("name", phenoDataSetCategory.getName(), MatchMode.ANYWHERE));
		}
		if (phenoDataSetCategory.getDescription() != null) {
			criteria.add(Restrictions.ilike("description", phenoDataSetCategory.getDescription(), MatchMode.ANYWHERE));
		}
		/*if (phenoDataSetCategory.getParentCategory() != null) {
			criteria.add(Restrictions.eq("parentCategory", phenoDataSetCategory.getParentCategory()));
		}
		if (phenoDataSetCategory.getOrderNumber() != null) {
			criteria.add(Restrictions.eq("orderNumber", phenoDataSetCategory.getOrderNumber()));
		}*/
		return criteria;
	}

	/*@Override
	public List<PhenoDataSetCategory> getPhenoParentCategoryList(Study study,ArkFunction arkFunction) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.isNotNull("parentCategory"));
		//All categories which having the parent categories
		//So we have to filter the parent categories after that 
		List<PhenoDataSetCategory> phenoDataSetCategoryList = (List<PhenoDataSetCategory>) criteria.list();
		List<PhenoDataSetCategory> parentCategoryList=new ArrayList<PhenoDataSetCategory>();
		for (PhenoDataSetCategory phenoDataSetCategory : phenoDataSetCategoryList) {
			parentCategoryList.add(phenoDataSetCategory.getParentCategory());
		}
		//Remove the duplicates.
		Set<PhenoDataSetCategory> phenoDataSetCategoriesSet=new HashSet<PhenoDataSetCategory>();
		phenoDataSetCategoriesSet.addAll(parentCategoryList);
		parentCategoryList.clear();
		parentCategoryList.addAll(phenoDataSetCategoriesSet);
		return parentCategoryList;
	}*/

	@Override
	public List<PhenoDataSetCategory> getAvailableAllCategoryListExceptThis(Study study, ArkFunction arkFunction,PhenoDataSetCategory thisPhenoDataSetCategory)throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.ne("id", thisPhenoDataSetCategory.getId()));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		List<PhenoDataSetCategory> phenoDataSetCategoryList = (List<PhenoDataSetCategory>) criteria.list();
		return phenoDataSetCategoryList;
	}

	@Override
	public List<PhenoDataSetCategory> searchPageablePhenoDataSetCategories(PhenoDataSetCategory phenoDataSetCategoryCriteria, int first,int count) {
		Criteria criteria = buildGeneralPhenoDataSetCategoryCritera(phenoDataSetCategoryCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		criteria.addOrder(Order.asc("name"));
		List<PhenoDataSetCategory> phenoDataSetCategoryList = (List<PhenoDataSetCategory>) criteria.list();
		return phenoDataSetCategoryList;
	}

	@Override
	public void createPhenoDataSetCategory(PhenoDataSetCategory phenoDataSetCategory)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException {
		getSession().save(phenoDataSetCategory);
		
	}

	@Override
	public void updatePhenoDataSetCategory(PhenoDataSetCategory phenoDataSetCategory)throws ArkSystemException, ArkUniqueException {
		getSession().update(phenoDataSetCategory);
		
	}

	@Override
	public void deletePhenoDataSetCategory(PhenoDataSetCategory phenoDataSetCategory)throws ArkSystemException, EntityCannotBeRemoved {
		getSession().delete(phenoDataSetCategory);
		
	}
	public void createAuditHistory(AuditHistory auditHistory, String userId, Study study) {
		Date date = new Date(System.currentTimeMillis());

		if (userId == null) {// if not forcing a userID manually, get
			// currentuser
			Subject currentUser = SecurityUtils.getSubject();
			auditHistory.setArkUserId((String) currentUser.getPrincipal());
		}
		else {
			auditHistory.setArkUserId(userId);
		}
		if (study == null) {
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			if (sessionStudyId != null && auditHistory.getStudyStatus() == null) {
				auditHistory.setStudyStatus(getStudy(sessionStudyId).getStudyStatus());
			}
			else {

				if (auditHistory.getEntityType().equalsIgnoreCase(au.org.theark.core.Constants.ENTITY_TYPE_STUDY)) {
					Study studyFromDB = getStudy(auditHistory.getEntityId());
					if (studyFromDB != null) {
						auditHistory.setStudyStatus(studyFromDB.getStudyStatus());
					}
				}
			}
		}
		else {
			auditHistory.setStudyStatus(study.getStudyStatus());
		}
		auditHistory.setDateTime(date);
		getSession().save(auditHistory);
	}
	private Study getStudy(Long id) {
		Study study = (Study) getSession().get(Study.class, id);
		return study;
	}
	@Override
	public boolean isPhenoDataSetCategoryUnique(String phenoDataSetCategoryName,Study study, PhenoDataSetCategory phenoDataSetCategoryToUpdate){
		boolean isUnique = true;
		StatelessSession stateLessSession = getStatelessSession();
		Criteria criteria = stateLessSession.createCriteria(CustomFieldCategory.class);
		criteria.add(Restrictions.eq("name", phenoDataSetCategoryName));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", phenoDataSetCategoryToUpdate.getArkFunction()));
		criteria.setMaxResults(1);
		
		PhenoDataSetCategory existingPhenoDataSetCategory = (PhenoDataSetCategory) criteria.uniqueResult();
		
		if( (phenoDataSetCategoryToUpdate.getId() != null && phenoDataSetCategoryToUpdate.getId() > 0)){
			
			if(existingPhenoDataSetCategory != null && !phenoDataSetCategoryToUpdate.getId().equals(existingPhenoDataSetCategory.getId())){
				isUnique = false;
			}
		}else{
			if(existingPhenoDataSetCategory != null){
				isUnique = false;
			}
		}
		stateLessSession.close();
		return isUnique;
	}
	/**
	 * check the Custom field category for the data intergrity.
	 */
	@Override
	public boolean isPhenoDataSetCategoryAlreadyUsed(PhenoDataSetCategory phenoDataSetCategory) {
	/**
	 * if a phenoDatasetCategory been used by the system it should be at least one or more of this table.
	 * PickedPhenoDataSetCategory
	 * LinkPhenoDataSetCategoryField
	 * PhenoDataSetFieldDisplay
	 *  
	 */
		Boolean status1=false,status2=false,status3=false;
		
		StatelessSession stateLessSessionOne = getStatelessSession();
		Criteria criteria = stateLessSessionOne.createCriteria(PickedPhenoDataSetCategory.class);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		criteria.add(Restrictions.eq("arkFunction",arkFunction ));
		criteria.add(Restrictions.eq("study",phenoDataSetCategory.getStudy() ));
		criteria.add(Restrictions.eq("phenoDataSetCategory", phenoDataSetCategory));
		List<PickedPhenoDataSetCategory> phenoDataSetCategories= (List<PickedPhenoDataSetCategory>) criteria.list();
		if (phenoDataSetCategories.size() > 0){
			status1= true;
		}else{
			status1= false;
		}
		StatelessSession stateLessSessionTwo = getStatelessSession();
		Criteria criteriaTwo = stateLessSessionTwo.createCriteria(LinkPhenoDataSetCategoryField.class);
		criteriaTwo.add(Restrictions.eq("arkFunction", arkFunction));
		criteriaTwo.add(Restrictions.eq("study", phenoDataSetCategory.getStudy()));
		criteriaTwo.add(Restrictions.eq("phenoDataSetCategory", phenoDataSetCategory));
		List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields= (List<LinkPhenoDataSetCategoryField>) criteriaTwo.list();
		if (linkPhenoDataSetCategoryFields.size() > 0){
			status2= true;
		}else{
			status2= false;
		}
		StatelessSession stateLessSessionThree = getStatelessSession();
		Criteria criteriaThree = stateLessSessionThree.createCriteria(PhenoDataSetFieldDisplay.class);
		criteriaThree.createAlias("phenoDataSetGroup", "phenoDSG");
		criteriaThree.add(Restrictions.eq("phenoDSG.arkFunction",arkFunction ));
		criteriaThree.add(Restrictions.eq("phenoDSG.study", phenoDataSetCategory.getStudy()));
		criteriaThree.add(Restrictions.eq("phenoDataSetCategory", phenoDataSetCategory));
		List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplays= (List<PhenoDataSetFieldDisplay>) criteriaThree.list();
		if (phenoDataSetFieldDisplays.size() > 0){
			status3= true;
		}else{
			status3= false;
		}
		return status1 || status2 || status3;
	}
	public PhenoDataSetField getPhenoDataSetField(Long id){
		Criteria criteria = getSession().createCriteria(PhenoDataSetField.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.setMaxResults(1);
		return (PhenoDataSetField)criteria.uniqueResult();	
	}
	public long getPhenoFieldCount(PhenoDataSetField phenofieldcriteria) {
		// Handle for study or function not in context
		if (phenofieldcriteria.getStudy() == null || phenofieldcriteria.getArkFunction() == null) {
				return 0;
		}
			Criteria criteria = buildGeneralPhenoFieldCritera(phenofieldcriteria);
			criteria.setProjection(Projections.rowCount());
			Long totalCount = (Long) criteria.uniqueResult();
			return totalCount;
	}
	/**
	 * Search method to the  fileds.
	 * @param phenoDataSet
	 * @return
	 */
	protected Criteria buildGeneralPhenoFieldCritera(PhenoDataSetField phenoDataSetField) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetField.class);
		
		criteria.add(Restrictions.eq("study", phenoDataSetField.getStudy()));
		criteria.add(Restrictions.eq("arkFunction", phenoDataSetField.getArkFunction()));
		
		
		if(phenoDataSetField.getFieldType()!=null){
			criteria.add(Restrictions.eq("fieldType", phenoDataSetField.getFieldType()));
		}
		if (phenoDataSetField.getId() != null) {
			criteria.add(Restrictions.eq("id", phenoDataSetField.getId()));
		}
		if (phenoDataSetField.getName() != null) {
			criteria.add(Restrictions.ilike("name", phenoDataSetField.getName(), MatchMode.ANYWHERE));
		}
		if (phenoDataSetField.getDescription() != null) {
			criteria.add(Restrictions.ilike("description", phenoDataSetField.getDescription(), MatchMode.ANYWHERE));
		}
		if (phenoDataSetField.getUnitType() != null && phenoDataSetField.getUnitType().getName() != null && phenoDataSetField.getUnitTypeInText() !=null) {
			criteria.createAlias("unitType", "ut");
			criteria.add(Restrictions.ilike("ut.name", phenoDataSetField.getUnitType().getName(), MatchMode.ANYWHERE));
		}
		if(phenoDataSetField.getUnitTypeInText() !=null){
			criteria.add(Restrictions.ilike("unitTypeInText", phenoDataSetField.getUnitTypeInText(),MatchMode.ANYWHERE));
		}
		if (phenoDataSetField.getMinValue() != null) {
			criteria.add(Restrictions.ilike("minValue", phenoDataSetField.getMinValue(), MatchMode.ANYWHERE));
		}
		if (phenoDataSetField.getMaxValue() != null) {
			criteria.add(Restrictions.ilike("maxValue", phenoDataSetField.getMaxValue(), MatchMode.ANYWHERE));
		}
		return criteria;
	}
	
	@Override
	public List<PhenoDataSetCategory> getCategoriesListInPhenoDataSetField(Study study, ArkFunction arkFunction) throws ArkSystemException {
		List<PhenoDataSetCategory> phenoDataSetCategories= new ArrayList<PhenoDataSetCategory>();
		
		return phenoDataSetCategories;
	}
	@SuppressWarnings("unchecked")
	public List<PhenoDataSetField> searchPageablePhenoFields(PhenoDataSetField phenoDataSetCriteria, int first, int count) {
		Criteria criteria = buildGeneralPhenoFieldCritera(phenoDataSetCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		criteria.addOrder(Order.asc("name"));
		List<PhenoDataSetField> phenoDataSetList = (List<PhenoDataSetField>) criteria.list();
		return phenoDataSetList;
	}
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplayByPhenoDataSet(PhenoDataSetField pheDataSetFieldCriteria){
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetField.id", pheDataSetFieldCriteria.getId()));
		criteria.setMaxResults(1);
		return (PhenoDataSetFieldDisplay)criteria.uniqueResult();
	}
	
	@Override
	public List<PhenoDataSetCategory> getAvailableAllCategoryListInStudy(Study study, ArkFunction arkFunction)throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		List<PhenoDataSetCategory> phenoFiedCategoryList = (List<PhenoDataSetCategory>) criteria.list();
		return phenoFiedCategoryList;
		
	}
	public boolean isPhenoDataSetFieldUnqiue(String phenoFieldName, Study study, PhenoDataSetField phenoFieldToUpdate){
		boolean isUnique = true;
		StatelessSession stateLessSession = getStatelessSession();
		Criteria criteria = stateLessSession.createCriteria(PhenoDataSetField.class);
		criteria.add(Restrictions.eq("name", phenoFieldName));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", phenoFieldToUpdate.getArkFunction()));
		criteria.setMaxResults(1);
		
		PhenoDataSetField existingField = (PhenoDataSetField) criteria.uniqueResult();
		
		if( (phenoFieldToUpdate.getId() != null && phenoFieldToUpdate.getId() > 0)){
			
			if(existingField != null && !phenoFieldToUpdate.getId().equals(existingField.getId())){
				isUnique = false;
			}
		}else{
			if(existingField != null){
				isUnique = false;
			}
		}
		stateLessSession.close();
		return isUnique;
	}
	public void updatePhenoDataSetField(PhenoDataSetField phenoDataSetField) throws  ArkSystemException{
		if(phenoDataSetField.getFieldType().getName().equals(Constants.FIELD_TYPE_NUMBER)) {
			if(phenoDataSetField.getMinValue()!=null){
				phenoDataSetField.setMinValue(phenoDataSetField.getMinValue().replace(",",""));				
			}
			if(phenoDataSetField.getMaxValue()!=null){
				phenoDataSetField.setMaxValue(phenoDataSetField.getMaxValue().replace(",",""));
			}
		}
		getSession().update(phenoDataSetField);
	}
	
	public void updatePhenoDataSetDisplay(PhenoDataSetFieldDisplay phenDataSetFieldDisplay) throws  ArkSystemException{
		if(!phenDataSetFieldDisplay.getRequired()){
			phenDataSetFieldDisplay.setRequiredMessage(null);
		}
		getSession().update(phenDataSetFieldDisplay);
	}
	
	/*@Override
	public List<PhenoDataSetCategory> getSiblingList(Study study,ArkFunction arkFunction,PhenoDataSetCategory phenoDataSetCategory){
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		if(phenoDataSetCategory.getParentCategory()!=null){
			criteria.add(Restrictions.eq("study", study));
			criteria.add(Restrictions.eq("arkFunction", arkFunction));
			criteria.add(Restrictions.eq("parentCategory", phenoDataSetCategory.getParentCategory()));
			return (List<PhenoDataSetCategory>) criteria.list();
		}
		return null;
	}*/
	@Override
	public void mergePhenoDataSetFieldCategory(PhenoDataSetCategory phenoDataSetCategory)throws ArkSystemException {
		getSession().merge(phenoDataSetCategory);
		
	}
	public List<PhenoDataSetCategory> getAllSubCategoriesOfThisCategory(Study study,ArkFunction arkFunction,PhenoDataSetCategory parentphenoDataSetFieldCategory){
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("parentCategory", parentphenoDataSetFieldCategory));
		return  (List<PhenoDataSetCategory>) criteria.list();
		
	}
	/**
	 * 
	 */
	public void deletePhenoDataSetField(PhenoDataSetField phenoDataSetField) throws ArkSystemException{
		getSession().delete(phenoDataSetField);
	}
	/**
	 * 
	 */
	public void deletePhenoDataSetFieldDisplay(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay) throws ArkSystemException{
		getSession().delete(phenoDataSetFieldDisplay);
	}
	/**
	 * 
	 */
	public void createPhenoDataSetField(PhenoDataSetField phenoDataSetField)throws ArkSystemException{
		getSession().save(phenoDataSetField);
	}
	/**
	 * 
	 */
	public void createPhenoDataSetFieldDisplay(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay)throws ArkSystemException{
		getSession().save(phenoDataSetFieldDisplay);
	}
	/**
	 * 
	 * @param phenoDataSetFieldDisplay
	 * @throws ArkSystemException
	 */
	public void updatePhenoDataSetFieldDisplay(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay)throws ArkSystemException{
		getSession().update(phenoDataSetFieldDisplay);
		
	}

	@Override
	public List<PhenoDataSetField> getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(PhenoDataSetGroup phenoDataSetGroupCriteria) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroupCriteria));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("phenoDataSetField"));
		criteria.setProjection(projectionList);
		criteria.addOrder(Order.asc("phenoDataSetFiledOrderNumber"));
		List<PhenoDataSetField> fieldsList = criteria.list();
		return fieldsList;
	}

	@Override
	public void createPhenoDataSetFieldGroup(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO)throws EntityExistsException, ArkSystemException {
		PhenoDataSetGroup phenoDataSetGroup = phenoDataSetFieldGroupVO.getPhenoDataSetGroup();
		Session session = getSession();
		session.save(phenoDataSetGroup);
		session.flush();
		insertToDispalyAndDeleteFromLinkAndPicked(phenoDataSetFieldGroupVO,phenoDataSetGroup, session);	
		log.debug("Saved All PhenoDataSetDisplays for PhenoDataSet Group");	
	}

	@Override
	public void updatePhenoDataSetFieldGroup(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO)throws EntityExistsException, ArkSystemException {
		PhenoDataSetGroup phenoDataSetGroup = phenoDataSetFieldGroupVO.getPhenoDataSetGroup();
		Session session = getSession();
		session.saveOrUpdate(phenoDataSetGroup);//Update phenoDataSetGroup
			Collection<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplayToRemove = getPhenoFieldDisplayToRemove(phenoDataSetGroup);	
			for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplayToRemove) {
				session.delete(phenoDataSetFieldDisplay);
				session.flush();
			}
			insertToDispalyAndDeleteFromLinkAndPicked(phenoDataSetFieldGroupVO, phenoDataSetGroup, session);	
			log.debug("Update PhenoDataSetFieldDisplay for PhenoDataSet Group");
	
	}
	private void insertToDispalyAndDeleteFromLinkAndPicked(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO,PhenoDataSetGroup phenoDataSetGroup, Session session) {
		//Get the Picked Pheno Dataset categories.
		List<PickedPhenoDataSetCategory> phenoDataSetCategories=phenoDataSetFieldGroupVO.getPickedAvailableCategories();
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : phenoDataSetCategories) {
				//Get the Linked Pheno Dataset fields for  PickedPhenoDataSetCategory
				List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields=getLinkPhenoDataSetCategoryFieldsForPickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
				if(!linkPhenoDataSetCategoryFields.isEmpty()){
					for (LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField : linkPhenoDataSetCategoryFields) {
						PhenoDataSetFieldDisplay phenoDataSetFieldDisplay = new PhenoDataSetFieldDisplay();
						phenoDataSetFieldDisplay.setPhenoDataSetGroup(phenoDataSetGroup);
						phenoDataSetFieldDisplay.setPhenoDataSetCategory(pickedPhenoDataSetCategory.getPhenoDataSetCategory());
						if(pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory()!=null){
							phenoDataSetFieldDisplay.setParentPhenoDataSetCategory(pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory().getPhenoDataSetCategory());
						}
						phenoDataSetFieldDisplay.setPhenoDataSetCategoryOrderNumber(pickedPhenoDataSetCategory.getOrderNumber());
						phenoDataSetFieldDisplay.setPhenoDataSetField(linkPhenoDataSetCategoryField.getPhenoDataSetField());
						phenoDataSetFieldDisplay.setPhenoDataSetFiledOrderNumber(linkPhenoDataSetCategoryField.getOrderNumber());
						session.save(phenoDataSetFieldDisplay);
					}
				}else{
					PhenoDataSetFieldDisplay phenoDataSetFieldDisplay = new PhenoDataSetFieldDisplay();
					phenoDataSetFieldDisplay.setPhenoDataSetGroup(phenoDataSetGroup);
					phenoDataSetFieldDisplay.setPhenoDataSetCategory(pickedPhenoDataSetCategory.getPhenoDataSetCategory());
					if(pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory()!=null){
						phenoDataSetFieldDisplay.setParentPhenoDataSetCategory(pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory().getPhenoDataSetCategory());
					}
					phenoDataSetFieldDisplay.setPhenoDataSetCategoryOrderNumber(pickedPhenoDataSetCategory.getOrderNumber());
					session.save(phenoDataSetFieldDisplay);
				}
		}
	}
	/**
	 * Creates Collection that will contain the list of new PhenoDataSetField that must be added/linked to the PhenoFieldGroup
	 * @param selectedCustomFields
	 * @param customFieldGroup
	 * @return
	 */
	private ArrayList<PhenoDataSetFieldDisplay> getPhenoDataSetFieldsToAdd(Collection<PhenoDataSetField> selectedPhenoDataSetFields, PhenoDataSetGroup phenoDataSetGroup){
		
		ArrayList<PhenoDataSetFieldDisplay> phenodatasetdisplayList = new ArrayList<PhenoDataSetFieldDisplay>();
		List<PhenoDataSetField> existingPhenoFieldList = getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(phenoDataSetGroup);// Existing List of CustomFieldsthat were linked to this CustomFieldGroup
		ArrayList<PhenoDataSetField> nonProxyPhenoFieldList = new ArrayList<PhenoDataSetField>();
		
		/**
		 * Note:
		 * getCustomFieldsLinkedToCustomFieldGroup() returns a projected List representing CustomField from CustomFieldDisplay. Since CustomField was a lazily loaded object, it is represented as a proxy object.
		 * For us to do a comparison using contains the equals() will fail when the class is compared. To be able to do that we convert to an underlying object before we do the final comparison.
		 * Since Hibernate returns proxy objects for LazyInitialisation when the equals() is invoked the class comparison will fail. 
		 */
		
		for (Object obj : existingPhenoFieldList) {
			if(obj instanceof HibernateProxy){
				PhenoDataSetField psf = (PhenoDataSetField)((HibernateProxy)obj).getHibernateLazyInitializer().getImplementation();
				nonProxyPhenoFieldList.add(psf);
			}
		}

		for (PhenoDataSetField phenoDataSetField : selectedPhenoDataSetFields) {
			if((!nonProxyPhenoFieldList.contains(phenoDataSetField))){
				
				PhenoDataSetFieldDisplay phenoDataSetFieldDisplay = new PhenoDataSetFieldDisplay();
				phenoDataSetFieldDisplay.setPhenoDataSetGroup(phenoDataSetGroup);
				phenoDataSetFieldDisplay.setPhenoDataSetField(phenoDataSetField);
				phenodatasetdisplayList.add(phenoDataSetFieldDisplay);
			}else{
				//Retrieve the customField for the sequence could have changed
				//String name = customField.getName();
				//PhenoDataSetFieldDisplay cfd = iArkCommonService.getCustomFieldDisplayByCustomField(customField);
				PhenoDataSetFieldDisplay pdsfd=getPhenoDataSetFieldDisplayByPhenoDataSetFieldAndGroup(phenoDataSetField, phenoDataSetGroup);
				phenodatasetdisplayList.add(pdsfd);
			}
		}
		return phenodatasetdisplayList;
	}
	
	/**
	 * Determine the list of PhenoFields that was linked to this PhenoDataSetFieldGroup and is not used by anyone and then if this is true add it to a list that will be processed later
	 * for removal.
	 * @param selectedCustomFields
	 * @param customFieldGroup
	 * @return
	 */
	private Collection<PhenoDataSetFieldDisplay> getPhenoFieldDisplayToRemove(PhenoDataSetGroup phenoDataSetGroup){
		
		Collection<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplaysList = getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroup(phenoDataSetGroup);
		Collection<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplayToRemove = new ArrayList<PhenoDataSetFieldDisplay>();
		//To do discard all the used pheno fileds 
		for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplaysList) {
				phenoDataSetFieldDisplayToRemove.add(phenoDataSetFieldDisplay);
		}
		return phenoDataSetFieldDisplayToRemove;
	}
	@Override
	public void deletePhenoDataSetFieldGroup(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO) {
		//Delete all the PhenoFieldDisplay Items linked to the Group
		Session session = getSession();
		//Delete Display
		PhenoDataSetGroup phenoDataSetGroup=phenoDataSetFieldGroupVO.getPhenoDataSetGroup();
		Collection<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplayList = getPhenoDataSetFieldDisplayForPhenoDataSetFieldGrroup(phenoDataSetGroup);
		for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplayList) {
			session.delete(phenoDataSetFieldDisplay);
		}
		//Delete picked and linked
		deletePickedCategoriesAndAllTheirChildren(phenoDataSetGroup.getStudy(),phenoDataSetGroup.getArkFunction() , phenoDataSetFieldGroupVO.getArkUser());
		//Delete group.
		session.delete(phenoDataSetFieldGroupVO.getPhenoDataSetGroup());
	}
	private List<PhenoDataSetFieldDisplay> getPhenoDataSetFieldDisplayForPhenoDataSetFieldGrroup(PhenoDataSetGroup phenoDataSetGroup){
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroup));
		criteria.addOrder(Order.asc("phenoDataSetFiledOrderNumber"));
		return criteria.list();
	}
	@Override
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplayByPhenoDataSetFieldAndGroup(PhenoDataSetField phenoDataSetField,PhenoDataSetGroup phenoDataSetGroup){
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetField", phenoDataSetField));
		criteria.add(Restrictions.eq("phenoDataSetGroup", phenoDataSetGroup));
		criteria.setMaxResults(1);
		return (PhenoDataSetFieldDisplay)criteria.uniqueResult();
	}
	@Override
	public long getPhenoDataSetFieldGroupCount(PhenoDataSetGroup phenoDataSetGroup) {
		// Handle for study or function not in context
				if (phenoDataSetGroup.getStudy() == null || phenoDataSetGroup.getArkFunction() == null) {
					return 0L;
				}
				Criteria criteria = buildGenericPhenoDataSetFieldGroupCriteria(phenoDataSetGroup);
				criteria.setProjection(Projections.rowCount());
				Long totalCount = (Long) criteria.uniqueResult();
				return totalCount;
	}
	private Criteria buildGenericPhenoDataSetFieldGroupCriteria(PhenoDataSetGroup phenoDataSetGroup){
		
		Criteria criteria = getSession().createCriteria(PhenoDataSetGroup.class);
		
		criteria.add(Restrictions.eq("study", phenoDataSetGroup.getStudy()));
		criteria.add(Restrictions.eq("arkFunction", phenoDataSetGroup.getArkFunction()));
		
		if (phenoDataSetGroup.getName() != null) {
			criteria.add(Restrictions.ilike("name", phenoDataSetGroup.getName(), MatchMode.ANYWHERE));
		}
		
		if(phenoDataSetGroup.getPublished() != null){
			criteria.add(Restrictions.eq("published", phenoDataSetGroup.getPublished()));
		}
		return criteria;
		
	}

	@Override
	public List<PhenoDataSetField> getPhenoDataSetFieldList(PhenoDataSetField phenoDataSetFieldCriteria) {
		Criteria criteria = buildGeneralPhenoFieldCritera(phenoDataSetFieldCriteria);
		// Return fields ordered alphabetically
		criteria.addOrder(Order.asc("name"));
		List<PhenoDataSetField> phenoDataSetFieldList = (List<PhenoDataSetField>) criteria.list();
		//log.warn("custom field criteria (just using name got a list of size " + customFieldList.size());
		return phenoDataSetFieldList;
	}

	@Override
	public List<PhenoDataSetGroup> getPhenoDataSetGroups(PhenoDataSetGroup phenoDataSetGroup, int first, int count) {
		Criteria criteria = buildGenericPhenoDataSetFieldGroupCriteria(phenoDataSetGroup);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<PhenoDataSetGroup> list = (List<PhenoDataSetGroup>)criteria.list();
		return list;	
	}
	public List<PickedPhenoDataSetCategory> getPickedPhenoDataSetCategories(Study study,ArkFunction arkFunction,ArkUser arkUser){
		Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.addOrder(Order.asc("orderNumber"));
		return (List<PickedPhenoDataSetCategory>)criteria.list();
	}

	@Override
	public List<PhenoDataSetCategory> getAvailablePhenoCategoryListNotPicked(Study study, ArkFunction arkFunctionPhenoCat,ArkFunction arkFunctionPhenoCollection,ArkUser arkUser) throws ArkSystemException {
		
		List<PickedPhenoDataSetCategory> pickedPhenoSetCatLst=getPickedPhenoDataSetCategories(study, arkFunctionPhenoCollection,arkUser);
		List<Long> pickedPhenoDataIdLst=new ArrayList<Long>();
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedPhenoSetCatLst) {
			pickedPhenoDataIdLst.add(pickedPhenoDataSetCategory.getPhenoDataSetCategory().getId());
		}
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunctionPhenoCat));
		criteria.add(Restrictions.eq("study", study));
		if(!pickedPhenoDataIdLst.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("id", pickedPhenoDataIdLst)));
		}
		return criteria.list();
	}

	@Override
	public void createPickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException, EntityExistsException {
		getSession().save(pickedPhenoDataSetCategory);
	}
	@Override
	public void deletePickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory)throws ArkSystemException, EntityCannotBeRemoved {
		getSession().delete(pickedPhenoDataSetCategory);
		
	}
	@Override
	public PickedPhenoDataSetCategory getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(Study study, ArkFunction arkFunction,ArkUser arkUser,PhenoDataSetCategory phenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("phenoDataSetCategory", phenoDataSetCategory));
		return (PickedPhenoDataSetCategory)criteria.uniqueResult();
	}
	@Override
	public void deleteLinkPhenoDataSetCategoryField(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField)throws ArkSystemException, EntityCannotBeRemoved {
		getSession().delete(linkPhenoDataSetCategoryField);
	}
	@Override
	public void createLinkPhenoDataSetCategoryField(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException, EntityExistsException {
		getSession().save(linkPhenoDataSetCategoryField);
	}
	/**
	 * Shows all the available pheno fields which not linked with categories.
	 */
	@Override
	public List<PhenoDataSetField> getAvailablePhenoFieldListNotInLinked(Study study, ArkFunction arkFunctionPhenoField,ArkFunction arkFunctionPhenoCollection,ArkUser arkUser) throws ArkSystemException {
		List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields=getLinkPhenoDataSetCategoryFieldLst(study, arkFunctionPhenoCollection,arkUser);
		List<Long> linkedPhenoDataIdLst=new ArrayList<Long>();
		for (LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField : linkPhenoDataSetCategoryFields) {
			linkedPhenoDataIdLst.add(linkPhenoDataSetCategoryField.getPhenoDataSetField().getId());
		}
		Criteria criteria = getSession().createCriteria(PhenoDataSetField.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunctionPhenoField));
		criteria.add(Restrictions.eq("study", study));
		if(!linkedPhenoDataIdLst.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("id", linkedPhenoDataIdLst)));
		}
		return criteria.list();
	}
	
	/**
	 * Shows all the Linked Pheno data categories.
	 */
	@Override
	public List<LinkPhenoDataSetCategoryField> getLinkPhenoDataSetCategoryFieldLst(Study study, ArkFunction arkFunction,ArkUser arkUser) {
		Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		return criteria.list();
	}

	@Override
	public void updatePickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException {
		getSession().update(pickedPhenoDataSetCategory);
		
	}

	@Override
	public List<PhenoDataSetField> getLinkedPhenoDataSetFieldsForSelectedCategories(Study study, ArkFunction arkFunction,ArkUser arkUser,List<PhenoDataSetCategory> phenoDataSetCategories) {
		List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields=new ArrayList<LinkPhenoDataSetCategoryField>();
		List<PhenoDataSetField> sumofPhenoDataSetFields=new ArrayList<PhenoDataSetField>();
			for (PhenoDataSetCategory phenoDataSetCategory : phenoDataSetCategories) {
				Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
				criteria.add(Restrictions.eq("study", study));
				criteria.add(Restrictions.eq("arkFunction", arkFunction));
				criteria.add(Restrictions.eq("arkUser", arkUser));
				criteria.add(Restrictions.eq("phenoDataSetCategory",phenoDataSetCategory));
				criteria.addOrder(Order.asc("orderNumber"));
				linkPhenoDataSetCategoryFields=(List<LinkPhenoDataSetCategoryField>)criteria.list();
				for (LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField : linkPhenoDataSetCategoryFields) {
					sumofPhenoDataSetFields.add(linkPhenoDataSetCategoryField.getPhenoDataSetField());
				}
				linkPhenoDataSetCategoryFields.clear();
			}
		return sumofPhenoDataSetFields;
		
	}

	@Override
	public LinkPhenoDataSetCategoryField getLinkPhenoDataSetCategoryField(Study study, ArkFunction arkFunction,ArkUser arkUser,PhenoDataSetCategory phenoDataSetCategory,PhenoDataSetField phenoDataSetField) {
		Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("phenoDataSetCategory",phenoDataSetCategory));
		criteria.add(Restrictions.eq("phenoDataSetField",phenoDataSetField));
		return (LinkPhenoDataSetCategoryField)criteria.uniqueResult();
		
	}

	@Override
	public boolean isSelectedCategoriesAlreadyAssignedToFields(Study study, ArkFunction arkFunction,ArkUser arkUser,List<PhenoDataSetCategory> phenoDataSetCategories) {
		return !getLinkedPhenoDataSetFieldsForSelectedCategories(study, arkFunction,arkUser, phenoDataSetCategories).isEmpty(); 
	}

	@Override
	public Long getNextAvailbleNumberForPickedCategory(Study study,ArkFunction arkFunction, ArkUser arkUser) {
		Long maxNumber;
		Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		//criteria.add(Restrictions.isNull("parentPickedPhenoDataSetCategory"));
	    criteria.setProjection(Projections.max("orderNumber"));
	    maxNumber= (Long)criteria.uniqueResult();
	    if(maxNumber!=null){
	    	return ++maxNumber;
	    }else{
	    	return new Long(1);
	    }
		
	}

	@Override
	public PickedPhenoDataSetCategory getSwapOverPickedPhenoDataSetCategoryForUpButton(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", pickedPhenoDataSetCategory.getArkFunction()));
		criteria.add(Restrictions.eq("study", pickedPhenoDataSetCategory.getStudy()));
		criteria.add(Restrictions.eq("arkUser", pickedPhenoDataSetCategory.getArkUser()));
		if(pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory()!=null){
			criteria.add(Restrictions.eq("parentPickedPhenoDataSetCategory", pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory()));
		}else{
			criteria.add(Restrictions.isNull("parentPickedPhenoDataSetCategory"));
		}	
		criteria.add(Restrictions.lt("orderNumber",pickedPhenoDataSetCategory.getOrderNumber()));
		criteria.addOrder(Order.desc("orderNumber"));
		criteria.setFirstResult(0);
		criteria.setMaxResults(1);
		List<PickedPhenoDataSetCategory>  pickedPhenoDataSetCategories=(List<PickedPhenoDataSetCategory>)criteria.list();
		if(pickedPhenoDataSetCategories.size() > 0){
			return pickedPhenoDataSetCategories.get(0);
		}else{
			return null;
		}
	}
	@Override
	public PickedPhenoDataSetCategory getSwapOverPickedPhenoDataSetCategoryForDownButton(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
			Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
			criteria.add(Restrictions.eq("arkFunction", pickedPhenoDataSetCategory.getArkFunction()));
			criteria.add(Restrictions.eq("study", pickedPhenoDataSetCategory.getStudy()));
			criteria.add(Restrictions.eq("arkUser", pickedPhenoDataSetCategory.getArkUser()));
			if(pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory()!=null){
				criteria.add(Restrictions.eq("parentPickedPhenoDataSetCategory", pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory()));
			}else{
				criteria.add(Restrictions.isNull("parentPickedPhenoDataSetCategory"));
			}
			criteria.add(Restrictions.gt("orderNumber",pickedPhenoDataSetCategory.getOrderNumber()));
			criteria.addOrder(Order.asc("orderNumber"));
			criteria.setFirstResult(0);
			criteria.setMaxResults(1);
			List<PickedPhenoDataSetCategory>  pickedPhenoDataSetCategories=(List<PickedPhenoDataSetCategory>)criteria.list();
			if(pickedPhenoDataSetCategories.size() > 0){
				return pickedPhenoDataSetCategories.get(0);
			}else{
				return null;
			}
	}
	@Override
	public Long getNextAvailbleNumberForAssignedField(Study study,ArkFunction arkFunction, ArkUser arkUser,PhenoDataSetCategory phenoDataSetCategory) {
		Long maxNumber;
		Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("phenoDataSetCategory", phenoDataSetCategory));
	    criteria.setProjection(Projections.max("orderNumber"));
	    maxNumber= (Long)criteria.uniqueResult();
	    if(maxNumber!=null){
	    	return ++maxNumber;
	    }else{
	    	return new Long(1);
	    }
	}

	@Override
	public LinkPhenoDataSetCategoryField getSwapOverPhenoDataSetFieldForUpButton(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField) {
		Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteria.add(Restrictions.eq("arkFunction", linkPhenoDataSetCategoryField.getArkFunction()));
		criteria.add(Restrictions.eq("study", linkPhenoDataSetCategoryField.getStudy()));
		criteria.add(Restrictions.eq("arkUser", linkPhenoDataSetCategoryField.getArkUser()));
		criteria.add(Restrictions.eq("phenoDataSetCategory", linkPhenoDataSetCategoryField.getPhenoDataSetCategory()));
		criteria.add(Restrictions.lt("orderNumber",linkPhenoDataSetCategoryField.getOrderNumber()));
		criteria.addOrder(Order.desc("orderNumber"));
		criteria.setFirstResult(0);
		criteria.setMaxResults(1);
		List<LinkPhenoDataSetCategoryField>  linkPhenoDataSetCategoryFields=(List<LinkPhenoDataSetCategoryField>)criteria.list();
		if(linkPhenoDataSetCategoryFields.size() > 0){
			return linkPhenoDataSetCategoryFields.get(0);
		}else{
			return null;
		}
	}

	@Override
	public LinkPhenoDataSetCategoryField getSwapOverPhenoDataSetFieldForDownButton(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField) {
		Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteria.add(Restrictions.eq("arkFunction", linkPhenoDataSetCategoryField.getArkFunction()));
		criteria.add(Restrictions.eq("study", linkPhenoDataSetCategoryField.getStudy()));
		criteria.add(Restrictions.eq("arkUser", linkPhenoDataSetCategoryField.getArkUser()));
		criteria.add(Restrictions.eq("phenoDataSetCategory", linkPhenoDataSetCategoryField.getPhenoDataSetCategory()));
		criteria.add(Restrictions.gt("orderNumber",linkPhenoDataSetCategoryField.getOrderNumber()));
		criteria.addOrder(Order.asc("orderNumber"));
		criteria.setFirstResult(0);
		criteria.setMaxResults(1);
		List<LinkPhenoDataSetCategoryField>  linkPhenoDataSetCategoryFields=(List<LinkPhenoDataSetCategoryField>)criteria.list();
		if(linkPhenoDataSetCategoryFields.size() > 0){
			return linkPhenoDataSetCategoryFields.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void updateLinkPhenoDataSetCategoryField(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException {
		getSession().update(linkPhenoDataSetCategoryField);
		
	}

	@Override
	public PhenoDataSetCategory getPhenoDataSetCategoryForAssignedPhenoDataSetField(Study study, ArkFunction arkFunction, ArkUser arkUser,PhenoDataSetField phenoDataSetField) {
		Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("phenoDataSetField", phenoDataSetField));
		LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField=(LinkPhenoDataSetCategoryField)criteria.uniqueResult();
		return linkPhenoDataSetCategoryField.getPhenoDataSetCategory();
	}

	@Override
	public Boolean isPickedPhenoDataSetCategoryIsAParentOfAnotherCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", pickedPhenoDataSetCategory.getArkFunction()));
		criteria.add(Restrictions.eq("study", pickedPhenoDataSetCategory.getStudy()));
		criteria.add(Restrictions.eq("arkUser", pickedPhenoDataSetCategory.getArkUser()));
		criteria.add(Restrictions.eq("parentPickedPhenoDataSetCategory", pickedPhenoDataSetCategory));
		return !((List<PickedPhenoDataSetCategory>)criteria.list()).isEmpty();
	}

	@Override
	public List<PickedPhenoDataSetCategory> getChildrenOfPickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", pickedPhenoDataSetCategory.getArkFunction()));
		criteria.add(Restrictions.eq("study", pickedPhenoDataSetCategory.getStudy()));
		criteria.add(Restrictions.eq("arkUser", pickedPhenoDataSetCategory.getArkUser()));
		criteria.add(Restrictions.eq("parentPickedPhenoDataSetCategory", pickedPhenoDataSetCategory));
		criteria.addOrder(Order.asc("orderNumber"));
		return (List<PickedPhenoDataSetCategory>)criteria.list();
	}

	@Override
	public List<PickedPhenoDataSetCategory> getAllParentPickedPhenoDataSetCategories(Study study, ArkFunction arkFunction, ArkUser arkUser) {
		Criteria criteria = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.isNull("parentPickedPhenoDataSetCategory"));
		criteria.addOrder(Order.asc("orderNumber"));
		return (List<PickedPhenoDataSetCategory>)criteria.list();
	}

	@Override
	public List<LinkPhenoDataSetCategoryField> getLinkPhenoDataSetCategoryFieldsForPickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteria.add(Restrictions.eq("study", pickedPhenoDataSetCategory.getStudy()));
		criteria.add(Restrictions.eq("arkFunction", pickedPhenoDataSetCategory.getArkFunction()));
		criteria.add(Restrictions.eq("arkUser", pickedPhenoDataSetCategory.getArkUser()));
		criteria.add(Restrictions.eq("phenoDataSetCategory",pickedPhenoDataSetCategory.getPhenoDataSetCategory()));
		criteria.addOrder(Order.asc("orderNumber"));
		return(List<LinkPhenoDataSetCategoryField>)criteria.list();
	}
	@Override
	public PhenoDataSetCategory getPhenoDataFieldCategoryByNameStudyAndArkFunction(String name, Study study, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetCategory.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		return (PhenoDataSetCategory) criteria.uniqueResult();
	}

	@Override
	public PhenoDataSetField getPhenoDataSetFieldByNameStudyArkFunction(String name, Study study, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetField.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		return (PhenoDataSetField) criteria.uniqueResult();
	}

	@Override
	public void createPhenoDataSetFieldCategoryUpload(PhenoDataSetFieldCategoryUpload phenoDataSetFieldCategoryUpload) {
		getSession().save(phenoDataSetFieldCategoryUpload);
	}

	@Override
	public void createPhenoDataSetFieldUpload(PhenoFieldUpload phenoFieldUpload) {
		getSession().save(phenoFieldUpload);
	}

	/**
	 * This return both categories and the fields of phenodatasetfieldsDisplay tables.
	 * Note:Not only the fields.          
	 */
	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroup(PhenoDataSetGroup phenoDataSetGroup) {
			Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
			criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroup));
			//Ordering first from the category and then from the field
			criteria.addOrder(Order.asc("phenoDataSetCategoryOrderNumber")).addOrder(Order.asc("phenoDataSetFiledOrderNumber"));
			return criteria.list();
	}

	@Override
	public void deletePickedCategoriesAndAllTheirChildren(Study study,ArkFunction arkFunction, ArkUser arkUser) {
		//Delete all fields
		Criteria criteriaField = getSession().createCriteria(LinkPhenoDataSetCategoryField.class);
		criteriaField.add(Restrictions.eq("study", study));
		criteriaField.add(Restrictions.eq("arkFunction",arkFunction ));
		criteriaField.add(Restrictions.eq("arkUser", arkUser));
		List<LinkPhenoDataSetCategoryField> linkPhenoDataSetCategoryFields=(List<LinkPhenoDataSetCategoryField>)criteriaField.list();
		for (LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField : linkPhenoDataSetCategoryFields) {
			getSession().delete(linkPhenoDataSetCategoryField);
		}
		//Delete all categories.
		Criteria criteriaCategory = getSession().createCriteria(PickedPhenoDataSetCategory.class);
		criteriaCategory.add(Restrictions.eq("study", study));
		criteriaCategory.add(Restrictions.eq("arkFunction",arkFunction ));
		criteriaCategory.add(Restrictions.eq("arkUser", arkUser));
		List<PickedPhenoDataSetCategory> pickedPhenoDataSetCategories=(List<PickedPhenoDataSetCategory>)criteriaCategory.list();
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : pickedPhenoDataSetCategories) {
			getSession().delete(pickedPhenoDataSetCategory);
		}
		getSession().flush();
	}

	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoFieldDisplaysIn(List<String> fieldNameCollection, Study study,ArkFunction arkFunction, PhenoDataSetGroup phenoDataSetGroup) {
			if (fieldNameCollection == null || fieldNameCollection.isEmpty()) {
				return new ArrayList<PhenoDataSetFieldDisplay>();
			}
			else {
				List<String> lowerCaseNames = new ArrayList<String>();
				for (String name : fieldNameCollection) {
					lowerCaseNames.add(name.toLowerCase());
				}
				/*String queryString = "select cfd from PhenoDataSetFieldDisplay cfd " + 
						" where cfd.customFieldGroup =:customFieldGroup and customField.id in ( " + 
						" SELECT id from CustomField cf " + 
						" where cf.study =:study " + " and lower(cf.name) in (:names) " + " and cf.arkFunction =:arkFunction )";
				*/String queryString = "select pdsfd from PhenoDataSetFieldDisplay pdsfd " + 
						" where pdsfd.phenoDataSetGroup =:phenoDataSetGroup and phenoDataSetField.id in ( " + 
						" SELECT id from PhenoDataSetField pdsf " + 
						" where pdsf.study =:study " + " and lower(pdsf.name) in (:names) " + " and pdsf.arkFunction =:arkFunction )";
				Query query = getSession().createQuery(queryString); 
				query.setParameter("study", study);
				// query.setParameterList("names", fieldNameCollection);
				query.setParameterList("names", lowerCaseNames);
				query.setParameter("arkFunction", arkFunction);
				query.setParameter("phenoDataSetGroup", phenoDataSetGroup);
				return query.list();
			}
	}

	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoFieldDisplaysIn(Study study,ArkFunction arkFunction) {
		String queryString = "select pdsfd from PhenoDataSetFieldDisplay pdsfd " +
				" where phenoDataSetField.id in ( " +
				" SELECT id from PhenoDataSetField pdsf " +
				" where pdsf.study =:study " + " and pdsf.arkFunction =:arkFunction )";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameter("arkFunction", arkFunction);
		return query.list();
	}

	@Override
	public long getPhenoFieldGroupCount(Study study,ArkFunction arkFunction,Boolean status) {
		// Handle for study or function not in context
		/*if (phenoDataSetGroup.getStudy() == null || phenoDataSetGroup.getArkFunction() == null) {
			return 0L;
		}*/
		//Criteria criteria = buildGenericPhenoFieldGroupCriteria(phenoDataSetGroup);
		Criteria criteria = getSession().createCriteria(PhenoDataSetGroup.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		criteria.add(Restrictions.eq("published", true));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}
	private Criteria buildGenericPhenoFieldGroupCriteria(PhenoDataSetGroup phenoDataSetGroup){
		
		Criteria criteria = getSession().createCriteria(PhenoDataSetGroup.class);
		
		criteria.add(Restrictions.eq("study", phenoDataSetGroup.getStudy()));
		criteria.add(Restrictions.eq("arkFunction", phenoDataSetGroup.getArkFunction()));
		
		if (phenoDataSetGroup.getName() != null) {
			criteria.add(Restrictions.ilike("name", phenoDataSetGroup.getName(), MatchMode.ANYWHERE));
		}
		
		if(phenoDataSetGroup.getPublished() != null){
			criteria.add(Restrictions.eq("published", phenoDataSetGroup.getPublished()));
		}
		return criteria;
		
	}
	public PhenoDataSetField getPhenoDataSetFieldByNameStudyPFG(String phenoFieldName, Study study, ArkFunction arkFunction, PhenoDataSetGroup phenoDataSetGroup)throws ArkRunTimeException,ArkSystemException{

		/*Query q = getSession().createQuery("Select customField from CustomField customField " +
											" where customField.name =:customFieldName " +
											" and lower(customField.study) =lower(:study) " +
											" and customField.arkFunction =:arkFunction " +
											" and exists (" +
											"				from CustomFieldDisplay as customFieldDisplay " +
											"				where customFieldDisplay.customField = customField " +
											"				and customFieldDisplay.customFieldGroup =:customFieldGroup ) ");
		q.setParameter("customFieldName", phenoFieldName);
		q.setParameter("study", study);
		q.setParameter("arkFunction", arkFunction);
		q.setParameter("customFieldGroup", phenoDataSetGroup);*/
		
		Query q = getSession().createQuery("Select phenoDataSetField from PhenoDataSetField phenoDataSetField " +
		" where phenoDataSetField.name =:phenoDataSetField " +
		" and lower(phenoDataSetField.study) =lower(:study) " +
		" and phenoDataSetField.arkFunction =:arkFunction " +
		" and exists (" +
		"				from PhenoDataSetFieldDisplay as phenoDataSetFieldDisplay " +
		"				where phenoDataSetFieldDisplay.phenoDataSetField = phenoDataSetField " +
		"				and phenoDataSetFieldDisplay.phenoDataSetGroup =:phenoDataSetGroup ) ");
		q.setParameter("phenoDataSetField", phenoFieldName);
		q.setParameter("study", study);
		q.setParameter("arkFunction", arkFunction);
		q.setParameter("phenoDataSetGroup", phenoDataSetGroup);
		List<PhenoDataSetField> results =null;
		try{
			 results = q.list();
		}catch(HibernateException hiberEx){ 
			throw new ArkRunTimeException("Problem finding the phono data set fields.");
		}
		if(results.size()>0){
			return (PhenoDataSetField)results.get(0);
		}
		return null;
	}
	public List<PhenoDataSetGroup> getPhenoDataSetFieldGroups(PhenoDataSetGroup phenoDataSetGroup, int first, int count){
		
		Criteria criteria = buildGenericPhenoFieldGroupCriteria(phenoDataSetGroup);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<PhenoDataSetGroup> list = (List<PhenoDataSetGroup>)criteria.list();
		return list;
	}
	
	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroupOrderByPhenoDataSetCategory(PhenoDataSetGroup phenoDataSetGroup) {
			Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
			criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroup));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("phenoDataSetGroup"), "phenoDataSetGroup");
			projectionList.add(Projections.groupProperty("phenoDataSetCategory"), "phenoDataSetCategory");
			projectionList.add(Projections.groupProperty("parentPhenoDataSetCategory"), "parentPhenoDataSetCategory");
			projectionList.add(Projections.groupProperty("phenoDataSetCategoryOrderNumber"), "phenoDataSetCategoryOrderNumber");
			criteria.setProjection(projectionList);
			criteria.addOrder(Order.asc("phenoDataSetCategoryOrderNumber"));
			criteria.setResultTransformer(Transformers.aliasToBean(PhenoDataSetFieldDisplay.class));
			return (List<PhenoDataSetFieldDisplay>)criteria.list();
	}
	@Override
	public List<PhenoDataSetField> getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroupAndPhenoDataSetCategory(PhenoDataSetGroup phenoDataSetGroupCriteria,PhenoDataSetCategory phenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroupCriteria));
		criteria.add(Restrictions.eq("phenoDataSetCategory",phenoDataSetCategory));
		criteria.add(Restrictions.isNotNull("phenoDataSetField"));
		criteria.addOrder(Order.asc("phenoDataSetFiledOrderNumber"));
		List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplays = (List<PhenoDataSetFieldDisplay>)criteria.list();
		List<PhenoDataSetField>  phenoDataSetFields=new ArrayList<PhenoDataSetField>();
		for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplays) {
			phenoDataSetFields.add(phenoDataSetFieldDisplay.getPhenoDataSetField());
		}
		return phenoDataSetFields;
	}
	@Override
	public List<Boolean> getPublishedSatusLst(Study study,ArkFunction arkFunction){
		Criteria criteria = getSession().createCriteria(PhenoDataSetGroup.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkFunction", arkFunction));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("published"), "published");
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(PhenoDataSetGroup.class));
		List<PhenoDataSetGroup> phenoDataSetGroups=(List<PhenoDataSetGroup>)criteria.list();
		List<Boolean> pubishStatusLst=new ArrayList<Boolean>();
		for (PhenoDataSetGroup phenoDataSetGroup : phenoDataSetGroups) {
			pubishStatusLst.add(phenoDataSetGroup.getPublished());
		}
		return pubishStatusLst;
	}
	@Override
	public PhenoDataSetCategory getPhenoDataSetCategoryById(Long id) {
		PhenoDataSetCategory phenoDataSetCategory = (PhenoDataSetCategory) getSession().get(PhenoDataSetCategory.class, id);
		return phenoDataSetCategory;
	}

	@Override
	public boolean isPhenoDataSetFieldCategoryBeingUsed(PhenoDataSetCategory phenoDataSetCategory) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetCategory",phenoDataSetCategory));
		return ((List<PhenoDataSetFieldDisplay>)criteria.list()).size()>0;
	}

	@Override
	public List<PhenoDataSetField> getAllPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(PhenoDataSetGroup phenoDataSetGroupCriteria) {
		Criteria criteria = getSession().createCriteria(PhenoDataSetFieldDisplay.class);
		criteria.add(Restrictions.eq("phenoDataSetGroup",phenoDataSetGroupCriteria));
		criteria.add(Restrictions.isNotNull("phenoDataSetField"));
		criteria.addOrder(Order.asc("phenoDataSetCategoryOrderNumber")).addOrder(Order.asc("phenoDataSetFiledOrderNumber"));
		List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplays = (List<PhenoDataSetFieldDisplay>)criteria.list();
		List<PhenoDataSetField>  phenoDataSetFields=new ArrayList<PhenoDataSetField>();
		for (PhenoDataSetFieldDisplay phenoDataSetFieldDisplay : phenoDataSetFieldDisplays) {
			phenoDataSetFields.add(phenoDataSetFieldDisplay.getPhenoDataSetField());
		}
		return phenoDataSetFields;
	}

}
