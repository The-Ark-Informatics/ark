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
package au.org.theark.phenotypic.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
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
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.CustomField;
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
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.vo.PhenoDataSetCategoryVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.vo.PhenoDataSetFieldVO;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.util.PhenoDataUploader;

@Transactional
@Service("phenotypicService")
public class PhenotypicServiceImpl implements IPhenotypicService {
	
	
	static final Logger log = LoggerFactory
			.getLogger(PhenotypicServiceImpl.class);

	private IArkCommonService<Void> iArkCommonService;
	private IPhenotypicDao phenotypicDao;
	private IStudyDao studyDao;

	@Autowired
	public void setiArkCommonService(IArkCommonService<Void> iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	@Autowired
	public void setPhenotypicDao(IPhenotypicDao phenotypicDao) {
		this.phenotypicDao = phenotypicDao;
	}
	@Autowired
	public void setStudyoDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	/**
	 * A Phenotypic collection is the data storage or grouping of a particular
	 * set set of data, containing subjects with fields with field data values
	 * for a particular date collected
	 * 
	 * @param col
	 *            the collection object to be created
	 */
	public void createCollection(PhenoDataSetCollection col) {
	

		phenotypicDao.createPhenoCollection(col);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Collection " + col.getDescription()+" was successfully created.");
		ah.setEntityId(col.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}
	public void updateCollection(PhenoDataSetCollection colEntity) {
		phenotypicDao.updatePhenoCollection(colEntity);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Pheno Collection " + colEntity.getDescription()+" was successfully updated.");
		ah.setEntityId(colEntity.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public Collection<PhenoDataSetCollection> getPhenoCollectionByStudy(Study study) {
		return phenotypicDao.getPhenoCollectionByStudy(study);
	}

	public void deleteCollection(PhenoDataSetCollection collection) {
		phenotypicDao.createPhenoCollection(collection);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Pheno Collection " + collection.getDescription()+" was successfully deleted.");
		ah.setEntityId(collection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}


	public void createUpload(Upload upload) {
		phenotypicDao.createUpload(upload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("PhenoUpload for File " + upload.getFilename()+" was successfully created.");
		ah.setEntityId(upload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public Collection<FileFormat> getFileFormats() {
		return phenotypicDao.getFileFormats();
	}


	public Collection<DelimiterType> getDelimiterTypes() {
		return phenotypicDao.getDelimiterTypes();
	}


	public long getCountOfFieldsInStudy(Study study) {
		return phenotypicDao.getCountOfFieldsInStudy(study);
	}

	public long getCountOfFieldsWithDataInStudy(Study study) {
		return phenotypicDao.getCountOfFieldsWithDataInStudy(study);
	}

	public long getCountOfCollectionsInStudy(Study study) {
		return phenotypicDao.getCountOfCollectionsInStudy(study);
	}

	public long getCountOfCollectionsWithDataInStudy(Study study) {
		return phenotypicDao.getCountOfCollectionsWithDataInStudy(study);
	}

	
	public DelimiterType getDelimiterType(Long id) {
		return phenotypicDao.getDelimiterType(id);
	}
	
	public boolean phenoCollectionHasData(PhenoDataSetCollection phenoCollection) {
		return phenoCollectionHasData(phenoCollection);
	}

	public String getDelimiterTypeByDelimiterChar(char phenotypicDelimChr) {
		return phenotypicDao
				.getDelimiterTypeByDelimiterChar(phenotypicDelimChr);
	}

	public FileFormat getFileFormatByName(String name) {
		return phenotypicDao.getFileFormatByName(name);
	}

	/**
	 * During the insert and delete the record of the custom filed we must careful to update the status of the 
	 * customer field "HasData" stage that is why we have additional update statements for 
	 * Please check insert and update here.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<PhenoDataSetData> createOrUpdatePhenoData(List<PhenoDataSetData> phenoDataList) {

		List<PhenoDataSetData> listOfExceptions = new ArrayList<PhenoDataSetData>();
		/* Iterate the list and call DAO to persist each Item */
		for (PhenoDataSetData phenoData : phenoDataList) {

			try {
				/*
				 * Insert the Field if it does not have a ID and has the
				 * required fields
				 */
				if (canInsert(phenoData)) {

					phenotypicDao.createPhenoData(phenoData);
					Long id = phenoData.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getId();
					PhenoDataSetField phenoDataSetField=phenotypicDao.getPhenoDataSetField(id);
					phenoDataSetField.setPhenoFieldHasData(true);
					PhenoDataSetFieldVO phenoDataSetFieldVO=new PhenoDataSetFieldVO();
					phenoDataSetFieldVO.setPhenoDataSetField(phenoDataSetField);
					updatePhenoDataSetField(phenoDataSetFieldVO);
				
				} else if (canUpdate(phenoData)) {

					// If there was bad data uploaded and the user has now
					// corrected it on the front end then set/blank out the
					// error data value and updated the record.
					if (phenoData.getErrorDataValue() != null) {
						phenoData.setErrorDataValue(null);
					}
					phenotypicDao.updatePhenoData(phenoData);

				} else if (canDelete(phenoData)) {
					// Check if the PhenoDataSetField is used by anyone else and if
					// not set the PhenoDataSetFieldHasData to false;
					//This will help to delete the phenodatesetField to delete one day.
					Long count = phenotypicDao.isPhenoDataSetFieldUsed(phenoData);

					phenotypicDao.deletePhenoData(phenoData);
					phenoData.getPhenoDataSetCollection().getPhenoDataSetData().remove(phenoData);
					if (count <= 1) {
						// Then update the PhenoDataSetField hasDataFlag to false.Reload since the session was closed in the front end and the child objects won't be loaded
						Long id = phenoData.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getId();
						PhenoDataSetField phenoDataSetField=phenotypicDao.getPhenoDataSetField(id);
						phenoDataSetField.setPhenoFieldHasData(false);
						PhenoDataSetFieldVO phenoDataSetFieldVO=new PhenoDataSetFieldVO();
						phenoDataSetFieldVO.setPhenoDataSetField(phenoDataSetField);
						updatePhenoDataSetField(phenoDataSetFieldVO);
					}
				}
			} catch (Exception someException) {
				listOfExceptions.add(phenoData);// Continue with rest of the
												// list
			}
		}

		return listOfExceptions;
	}

	/**
	 * In order to delete it must satisfy the following conditions 1. PhenoData
	 * must be a persistent entity(with a valid primary key/ID) AND 2. PhenoData
	 * should have a valid Subject linked to it and must not be null AND 3.
	 * PhenoData.TextDataValue is NULL OR is EMPTY 4. PhenoData.NumberDataValue
	 * is NULL 5. PhenoData.DatewDataValue is NULL When these conditions are
	 * satisfied this method will return Boolean TRUE
	 * 
	 * @param phenoData
	 * @return
	 */
	private Boolean canDelete(PhenoDataSetData phenoData) {
		Boolean flag = false;

		if (phenoData.getId() != null
				&& phenoData.getPhenoDataSetCollection() != null
				&& (phenoData.getTextDataValue() == null
						|| phenoData.getTextDataValue().isEmpty()
						|| phenoData.getNumberDataValue() == null || phenoData
						.getDateDataValue() == null)) {

			flag = true;

		}
		return flag;
	}

	/**
	 * In order to Update a PhenoData instance the following conditions must be
	 * met 1. PhenoData must be a persistent entity(with a valid primary key/ID)
	 * AND 2. PhenoData should have a valid Subject linked to it and must not be
	 * null AND 3. PhenoData.TextDataValue is NOT NULL AND NOT EMPTY OR 4.
	 * PhenoData.NumberDataValue is NOT NULL 5. PhenoData.DateDataValue is NOT
	 * NULL When these conditions are satisfied the method will return Boolean
	 * TRUE
	 * 
	 * @param phenoData
	 * @return
	 */
	private Boolean canUpdate(PhenoDataSetData phenoData) {
		Boolean flag = false;

		if (phenoData.getId() != null
				&& phenoData.getPhenoDataSetCollection() != null
				&& ((phenoData.getTextDataValue() != null && !phenoData
						.getTextDataValue().isEmpty())
						|| phenoData.getDateDataValue() != null || phenoData
						.getNumberDataValue() != null)) {

			flag = true;

		}
		return flag;
	}

	/**
	 * In order to Insert a PhenoData instance the following conditions must be
	 * met. 1. PhenoData must be a transient entity(Not yet associated with an
	 * ID/PK) AND 2. PhenoData should have a valid Subject linked to it and must
	 * not be null AND 3. PhenoData.TextDataValue is NOT NULL OR 4.
	 * PhenoData.NumberDataValue is NOT NULL OR 5. PhenoData.DateDataValue is
	 * NOT NULL When these conditions are satisfied the method will return
	 * Boolean TRUE
	 * 
	 * @param phenoData
	 * @return
	 */
	private Boolean canInsert(PhenoDataSetData phenoData) {
		Boolean flag = false;

		if (phenoData.getId() == null
				&& phenoData.getPhenoDataSetCollection() != null
				&& (phenoData.getNumberDataValue() != null	|| phenoData.getTextDataValue() != null || phenoData.getDateDataValue() != null)) {

			flag = true;

		}
		return flag;
	}

	public long getPhenoDataCount(PhenoDataSetCollection phenoCollection,PhenoDataSetCategory phenoDataSetCategory) {
		return phenotypicDao.getPhenoDataCount(phenoCollection,phenoDataSetCategory);
	}

	public List<PhenoDataSetData> getPhenoDataList(PhenoDataSetCollection phenoCollection,PhenoDataSetCategory phenoDataSetCategory,int first, int count) {
		List<PhenoDataSetData> resultsList = phenotypicDao.getPhenoDataList(phenoCollection,phenoDataSetCategory, first, count);
		return resultsList;
	}

	public PhenoDataSetCollection getPhenoCollection(Long id) {
		return phenotypicDao.getPhenoCollection(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO)
			throws EntityExistsException, ArkSystemException {
		try {
			phenotypicDao.createCustomFieldGroup(customFieldGroupVO);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Custom Field Group "
					+ customFieldGroupVO.getCustomFieldGroup().getName()+" was successfully created.");
			ah.setEntityId(customFieldGroupVO.getCustomFieldGroup().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_GROUP);
			iArkCommonService.createAuditHistory(ah);
		} catch (ConstraintViolationException cvex) {
			log.error("A Questionnaire with this name for the given study  exists.: "
					+ cvex);
			throw new EntityExistsException(
					"A Questionnaire with that name already exists.");
		} catch (Exception ex) {
			log.error("Problem creating Questionnaire: " + ex);
			throw new ArkSystemException("Problem creating Questionnaire: "
					+ ex.getMessage());
		}

	}

	public long getPhenoCollectionCount(PhenoDataCollectionVO criteria) {
		return phenotypicDao.getPhenoCollectionCount(criteria);
	}

	public List<PhenoDataSetCollection> searchPageablePhenoCollections(	PhenoDataCollectionVO criteria, int first, int count) {
		return phenotypicDao.searchPageablePhenoCollection(criteria, first,	count);
	}
	
	public List<PhenoDataSetField> getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(PhenoDataSetGroup phenoDataSetGroup)
	{
		return phenotypicDao.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(phenoDataSetGroup);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
			Exception.class, ConstraintViolationException.class })
	public void updateCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO)
			throws EntityExistsException, ArkSystemException {
		try {
			phenotypicDao.updateCustomFieldGroup(customFieldGroupVO);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("Custom Field Group "
					+ customFieldGroupVO.getCustomFieldGroup().getName()+" was successfully updated.");
			ah.setEntityId(customFieldGroupVO.getCustomFieldGroup().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_GROUP);
			iArkCommonService.createAuditHistory(ah);
		} catch (ConstraintViolationException cvex) {
			log.error("A Questionnaire with this name for the given study  exists.: "
					+ cvex);
			throw new EntityExistsException(
					"A Questionnaire with that name already exists.");
		} catch (Exception ex) {
			log.error("Problem creating Questionnaire: " + ex);
			throw new ArkSystemException("Problem creating Questionnaire: "
					+ ex.getMessage());
		}

	}

	public Collection<PhenoDataSetFieldDisplay> getCFDLinkedToQuestionnaire(PhenoDataSetGroup phenoDataSetGroup, int first, int count) {
		return phenotypicDao.getCFDLinkedToQuestionnaire(phenoDataSetGroup,first, count);
	}

	public long getCFDLinkedToQuestionnaireCount(PhenoDataSetGroup phenoDataSetGroup) {
		return phenotypicDao.getCFDLinkedToQuestionnaireCount(phenoDataSetGroup);
	}

	public List<QuestionnaireStatus> getPhenoCollectionStatusList() {
		return phenotypicDao.getPhenoCollectionStatusList();
	}

	public void createPhenoCollection(PhenoDataSetCollection phenoCollection) {
		phenotypicDao.createPhenoCollection(phenoCollection);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("PhenoCollection "
				+ phenoCollection.getDescription()+" was successfully created.");
		ah.setEntityId(phenoCollection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updatePhenoCollection(PhenoDataSetCollection phenoCollection) {
		phenotypicDao.updatePhenoCollection(phenoCollection);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("PhenoCollection "
				+ phenoCollection.getDescription()+" was successfully updated.");
		ah.setEntityId(phenoCollection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deletePhenoCollection(PhenoDataSetCollection phenoCollection) {
		phenotypicDao.deletePhenoCollection(phenoCollection);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("PhenoCollection "
				+ phenoCollection.getDescription()+" was successfully deleted.");
		ah.setEntityId(phenoCollection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void createFieldUpload(au.org.theark.core.vo.UploadVO uploadVo) {
		// TODO: Actually create the upload
		// iArkCommonService.createCustomFieldUpload(uploadVo);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Upload for File "
				+ uploadVo.getUpload().getFilename()+" was successfully created.");
		ah.setEntityId(uploadVo.getUpload().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deleteFieldUpload(au.org.theark.core.vo.UploadVO uploadVO) {
		// TODO Auto-generated method stub

	}

	public void updateFieldUpload(au.org.theark.core.vo.UploadVO uploadVO) {
		// TODO Auto-generated method stub

	}

	public void deleteCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO) {
		phenotypicDao.deleteCustomFieldGroup(customFieldGroupVO);
	}

	public QuestionnaireStatus getPhenoCollectionStatusByName(String statusName) {
		return phenotypicDao.getPhenoCollectionStatusByName(statusName);
	}

	public QuestionnaireStatus getDefaultPhenoCollectionStatus() {
		return phenotypicDao.getPhenoCollectionStatusByName(au.org.theark.phenotypic.service.Constants.PHENOCOLLECTION_STATUS_IN_PROGRESS);
	}

	public java.util.Collection<Upload> searchUpload(Upload upload) {
		return phenotypicDao.searchUpload(upload);
	}

	public void deleteUpload(Upload studyUpload) {
		phenotypicDao.deleteUpload(studyUpload);
	}

	public Upload getUpload(Long id) {
		return phenotypicDao.getUpload(id);
	}

	/**** TODO IMPLEMENT THIS THING AGAIN! ****/
	public StringBuffer uploadAndReportCustomDataFile(InputStream inputStream,long size, String fileFormat, char delimChar, Long studyId,List<String> listOfUIDsToUpdate, PhenoDataSetGroup phenoDataSetGroup,PhenoDataSetCollection phenoCollection, boolean overwriteExisting) {

		StringBuffer uploadReport = null;
		Study study = iArkCommonService.getStudy(studyId);
		PhenoDataUploader dataUploader = new PhenoDataUploader(study,
				iArkCommonService, this);
		try {
			// log.warn("uploadAndReportCustomDataFile list=" +
			// listOfUIDsToUpdate);
			uploadReport = dataUploader.uploadAndReportCustomDataFile(inputStream, size, fileFormat, delimChar,listOfUIDsToUpdate, phenoDataSetGroup, phenoCollection,overwriteExisting);
		} catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		} catch (ArkSystemException ae) {
			log.error("Exception in handling batch upload of custom data (phenotypic) "
					+ ae);
		}
		return uploadReport;
	}

	/**** TODO IMPLEMENT THIS THING AGAIN! ****/
	public void refreshUpload(Upload upload) {
	}

	public Collection<CustomFieldGroup> getCustomFieldGroupList(Study study) {
		return phenotypicDao.getCustomFieldGroupList(study);
	}

	public void processPhenoCollectionsWithTheirDataToInsertBatch(List<PhenoDataSetCollection> phenoCollectionsWithTheirDataToInsert,Study study) {
		phenotypicDao.processPhenoCollectionsWithTheirDataToInsertBatch(phenoCollectionsWithTheirDataToInsert, study);
	}

	public List<List<String>> getPhenoDataAsMatrix(Study study,List<String> subjectUids, List<PhenoDataSetField> phenoDataSetFields,List<PhenoDataSetGroup> phenoDataSetGroups,PhenoDataSetCategory phenoDataSetCategory) {
		return phenotypicDao.getPhenoDataAsMatrix(study, subjectUids,phenoDataSetFields, phenoDataSetGroups,phenoDataSetCategory);
	}

	public List<PhenoDataSetGroup> getPhenoDataSetGroupsByLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		return phenotypicDao.getPhenoDataSetGroupsByLinkSubjectStudy(linkSubjectStudy);
	}

	public CustomFieldGroup getCustomFieldGroupByNameAndStudy(String name,Study study) {
		return phenotypicDao.getCustomFieldGroupByNameAndStudy(name, study);
	}

	public PhenoDataSetGroup getPhenoFieldGroupById(Long id) {
		return phenotypicDao.getPhenoFieldGroupById(id);
	}

	public List<PhenoDataSetCollection> getSubjectMatchingPhenoCollections(LinkSubjectStudy subject, PhenoDataSetGroup phenoDataSetGroup,Date recordDate) {
		return phenotypicDao.getSubjectMatchingPhenoCollections(subject,phenoDataSetGroup, recordDate);
	}

	@Override
	public PhenoDataSetCategory getPhenoDataSetCategory(Long id)throws EntityNotFoundException {
		return phenotypicDao.getPhenoDataSetCategory(id);
	}

	@Override
	public List<PhenoDataSetCategory> getAvailableAllCategoryList(Study study,ArkFunction arkFunction) throws ArkSystemException {
		return phenotypicDao.getAvailableAllCategoryList(study, arkFunction);
	}

	@Override
	public long getPhenoDataSetCategoryCount(PhenoDataSetCategory phenoDataSetCategoryCriteria) {
		return phenotypicDao.getPhenoDataSetCategoryCount(phenoDataSetCategoryCriteria);
	}

	/*@Override
	public List<PhenoDataSetCategory> getPhenoParentCategoryList(Study study,ArkFunction arkFunction) throws ArkSystemException {
		return phenotypicDao.getPhenoParentCategoryList(study, arkFunction);
	}*/

	@Override
	public List<PhenoDataSetCategory> getAvailableAllCategoryListExceptThis(Study study, ArkFunction arkFunction,PhenoDataSetCategory thisPhenoDataSetCategory)throws ArkSystemException {
		return phenotypicDao.getAvailableAllCategoryListExceptThis(study, arkFunction, thisPhenoDataSetCategory);
	}

	@Override
	public List<PhenoDataSetCategory> searchPageablePhenoDataSetCategories(PhenoDataSetCategory phenoDataSetCategoryCriteria, int first,int count) {
		return phenotypicDao.searchPageablePhenoDataSetCategories(phenoDataSetCategoryCriteria, first, count);
	}

	@Override
	public void createPhenoDataSetCategory(PhenoDataSetCategoryVO phenoDataSetCategoryvo)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException {
try {
			AuditHistory ah = new AuditHistory();
			// Force uppercase and replace erroneous characters
			phenoDataSetCategoryvo.getPhenoDataSetCategory().getName().toUpperCase();
			phenoDataSetCategoryvo.getPhenoDataSetCategory().getName().replaceAll(" ", "_");
			phenotypicDao.createPhenoDataSetCategory(phenoDataSetCategoryvo.getPhenoDataSetCategory());
			// PhenoDataSet Field History
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Pheno Dataset category " + phenoDataSetCategoryvo.getPhenoDataSetCategory().getName()+" was successfully created.");
			ah.setEntityId(phenoDataSetCategoryvo.getPhenoDataSetCategory().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_CATEGORY);
			phenotypicDao.createAuditHistory(ah,null,null);
		} catch (ConstraintViolationException cvex) {
			log.error("Pheno DataSet Category Constrain violation" + cvex);
			if(cvex.getMessage().contains("Duplicate entry")){
				log.error("Pheno DataSet Category Duplicates" + cvex);
				throw new ArkRunTimeUniqueException("A Pheno DataSet Category duplicate value violation error occurred.");
			}else if(cvex.getMessage().contains("cannot be null")){
				log.error("Pheno DataSet Category field cannot be null" + cvex);
				throw new ArkRunTimeException("A Pheno DataSet Category null violation error occurred.");
			}
		} catch (Exception ex) {
			log.error("Problem creating Pheno DataSet Category: " + ex);
			throw new ArkSystemException("Problem creating Pheno DataSet Category: " + ex.getMessage());
		}
	}

	@Override
	public void updatePhenoDataSetCategory(PhenoDataSetCategoryVO phenoDataSetCategoryvo)throws ArkSystemException, ArkUniqueException {
		boolean isUnique = phenotypicDao.isPhenoDataSetCategoryUnique(phenoDataSetCategoryvo.getPhenoDataSetCategory().getName(), phenoDataSetCategoryvo.getPhenoDataSetCategory().getStudy(), phenoDataSetCategoryvo.getPhenoDataSetCategory());
		if (!isUnique) {
			log.error("Pheno DataSet Category of this name Already Exists.: ");
			throw new ArkUniqueException("A Pheno DataSet Category of this name already exists.");
		}
		try {
			phenotypicDao.updatePhenoDataSetCategory(phenoDataSetCategoryvo.getPhenoDataSetCategory());
			// Pheno DataSet History
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("Pheno DataSet Category" + phenoDataSetCategoryvo.getPhenoDataSetCategory().getName()+" was successfully updated.");
			ah.setEntityId(phenoDataSetCategoryvo.getPhenoDataSetCategory().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_CATEGORY);
			phenotypicDao.createAuditHistory(ah,null,null);
		} catch (ConstraintViolationException cvex) {
			log.error("Pheno DataSet Category Already Exists.: " + cvex);
			throw new ArkUniqueException("A Pheno DataSet Category already exists.");
		} catch (Exception ex) {
			log.error("Problem updating Pheno DataSet: " + ex);
			throw new ArkSystemException("Problem updating Pheno DataSet Category: " + ex.getMessage());
		}

	}

	@Override
	public void deletePhenoDataSetCategory(PhenoDataSetCategoryVO phenoDataSetCategoryvo)throws ArkSystemException, EntityCannotBeRemoved, QueryException {
		String fieldName = phenoDataSetCategoryvo.getPhenoDataSetCategory().getName();
		if(phenotypicDao.isPhenoDataSetCategoryAlreadyUsed(phenoDataSetCategoryvo.getPhenoDataSetCategory())){
					throw new EntityCannotBeRemoved("You cannot delete a Pheno DataSet Category which already in use.");
		}else{
			phenotypicDao.deletePhenoDataSetCategory(phenoDataSetCategoryvo.getPhenoDataSetCategory());
					// History for Pheno DataSet Category
					AuditHistory ah = new AuditHistory();
					ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
					ah.setComment("Pheno DataSet Category " + fieldName+" was successfully deleted.");
					ah.setEntityId(phenoDataSetCategoryvo.getPhenoDataSetCategory().getId());
					ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_CATEGORY);
					phenotypicDao.createAuditHistory(ah,null,null);
	}

	}
	/**
	 * 
	 */
	public PhenoDataSetField getPhenoDataSetField(Long id){
		return phenotypicDao.getPhenoDataSetField(id);
	}

	@Override
	public long getPhenoFieldCount(PhenoDataSetField phenoFieldCriteria) {
		return phenotypicDao.getPhenoFieldCount(phenoFieldCriteria);
	}
	/**
	 * 
	 */
	public List<PhenoDataSetField> searchPageablePhenoFields(PhenoDataSetField phenoDataSetCriteria, int first, int count){
		return phenotypicDao.searchPageablePhenoFields(phenoDataSetCriteria,first,count);
	}
	public List<PhenoDataSetCategory> getAvailableAllCategoryListInStudy(Study study, ArkFunction arkFunction)throws ArkSystemException{
		return phenotypicDao.getAvailableAllCategoryListInStudy(study,arkFunction);
	}
	
	/**
	 * Update a Pheno Field if it is not yet any data and update the PhenoDataSet
	 * Field display details.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatePhenoDataSetField(PhenoDataSetFieldVO phenoDataSetFieldVO) throws ArkSystemException, ArkUniqueException {

		boolean isUnique = phenotypicDao.isPhenoDataSetFieldUnqiue(phenoDataSetFieldVO.getPhenoDataSetField().getName(), phenoDataSetFieldVO.getPhenoDataSetField().getStudy(), phenoDataSetFieldVO.getPhenoDataSetField());
		if (!isUnique) {
			log.error("Pheno Field of this name Already Exists.: ");
			throw new ArkUniqueException("A Pheno Field with this name already exists.");
		}
		try {
			// Remove any encoded values if DATE or NUMBER
			if (phenoDataSetFieldVO.getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) || phenoDataSetFieldVO.getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
				phenoDataSetFieldVO.getPhenoDataSetField().setEncodedValues(null);
			}

			phenotypicDao.updatePhenoDataSetField(phenoDataSetFieldVO.getPhenoDataSetField());
			// PhenoDataSet Field History
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("PhenoDataSet Field " + phenoDataSetFieldVO.getPhenoDataSetField().getName()+" was successfully updated.");
			ah.setEntityId(phenoDataSetFieldVO.getPhenoDataSetField().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_FIELD);
			createAuditHistory(ah);


		} catch (ConstraintViolationException cvex) {
			log.error("PhenoDataSet Field Already Exists.: " + cvex);
			throw new ArkUniqueException("A PhenoDataSet Field already exists.");
		} catch (Exception ex) {
			log.error("Problem updating PhenoDataSet Field: " + ex);
			throw new ArkSystemException("Problem updating PhenoDataSet Field: " + ex.getMessage());
		}
	}
	public void createAuditHistory(AuditHistory auditHistory) {
		studyDao.createAuditHistory(auditHistory);
	}

	public void mergePhenoDataSetFieldCategory(PhenoDataSetCategory phenoDataSetCategory)throws ArkSystemException{
		 phenotypicDao.mergePhenoDataSetFieldCategory(phenoDataSetCategory);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createPhenoDataSetField(PhenoDataSetFieldVO phenoDataSetFieldVO) throws ArkSystemException, ArkUniqueException {
		try {
			// Create Both CustomField and CustomFieldDisplay
			AuditHistory ah = new AuditHistory();

			// Force uppercase and replace erroneous characters
			phenoDataSetFieldVO.getPhenoDataSetField().getName().toUpperCase();
			phenoDataSetFieldVO.getPhenoDataSetField().getName().replaceAll(" ", "_");

			// Remove any encoded values if DATE or NUMBER
			if (phenoDataSetFieldVO.getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) || phenoDataSetFieldVO.getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
				phenoDataSetFieldVO.getPhenoDataSetField().setEncodedValues(null);
			}

			// Field can not have data yet (since it's new)
			phenoDataSetFieldVO.getPhenoDataSetField().setPhenoFieldHasData(false);;
			phenotypicDao.createPhenoDataSetField(phenoDataSetFieldVO.getPhenoDataSetField());

			// PhenoDataSet Field History
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("PhenoDataSet " + phenoDataSetFieldVO.getPhenoDataSetField().getName()+" was successfully created.");
			ah.setEntityId(phenoDataSetFieldVO.getPhenoDataSetField().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_FIELD);

			createAuditHistory(ah);
		} catch (ConstraintViolationException cvex) {
			log.error("PhenoDataSet Field Already Exists.: " + cvex);
			throw new ArkUniqueException("A PhenoDataSet Field already exists.");
		} catch (Exception ex) {
			log.error("Problem creating PhenoDataSet Field: " + ex);
			throw new ArkSystemException("Problem creating PhenoDataSet Field: " + ex.getMessage());
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deletePhenoDataSetField(PhenoDataSetFieldVO phenoDataSetFieldVO) throws ArkSystemException, EntityCannotBeRemoved {
		try {
			if (!phenoDataSetFieldVO.getPhenoDataSetField().getPhenoFieldHasData()) {
				String fieldName = phenoDataSetFieldVO.getPhenoDataSetField().getName();
				
				phenotypicDao.deletePhenoDataSetField(phenoDataSetFieldVO.getPhenoDataSetField());

				// History for PhenoDataSet Field
				AuditHistory ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
				ah.setComment("Deleted PhenoDataSet Field " + fieldName);
				ah.setEntityId(phenoDataSetFieldVO.getPhenoDataSetField().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_FIELD_DISPLAY);
				createAuditHistory(ah);
			} else {
				throw new EntityCannotBeRemoved("PhenoDataSet Field cannot be removed, it is used in the system");
			}
		} catch (Exception ex) {
			log.error("Unable to delete PhenoDataSetField. " + ex);
			throw new ArkSystemException("Unable to delete PhenoDataSet Field: " + ex.getMessage());
		}
	}

	@Override
	public void createPhenoFieldDataSetGroup(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO)throws EntityExistsException, ArkSystemException {
		try {
			phenotypicDao.createPhenoDataSetFieldGroup(phenoDataSetFieldGroupVO);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created Pheno Data Field Group "
					+ phenoDataSetFieldGroupVO.getPhenoDataSetGroup().getName());
			ah.setEntityId(phenoDataSetFieldGroupVO.getPhenoDataSetGroup().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_CATEGORY_FIELD_GROUP);
			iArkCommonService.createAuditHistory(ah);
		} catch (ConstraintViolationException cvex) {
			log.error("A Questionnaire with this name for the given study  exists.: "
					+ cvex);
			throw new EntityExistsException(
					"A Questionnaire with that name already exists.");
		} catch (Exception ex) {
			log.error("Problem creating Questionnaire: " + ex);
			throw new ArkSystemException("Problem creating Questionnaire: "
					+ ex.getMessage());
		}
		
	}

	@Override
	public void updatePhenoFieldDataSetGroup(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO)throws EntityExistsException, ArkSystemException {
			
		try {
			phenotypicDao.updatePhenoDataSetFieldGroup(phenoDataSetFieldGroupVO);
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("Updated Pheno Data Field Group "
					+ phenoDataSetFieldGroupVO.getPhenoDataSetGroup().getName());
			ah.setEntityId(phenoDataSetFieldGroupVO.getPhenoDataSetGroup().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_DATASET_CATEGORY_FIELD_GROUP);
			iArkCommonService.createAuditHistory(ah);
		} catch (ConstraintViolationException cvex) {
			log.error("A Questionnaire with this name for the given study  exists.: "
					+ cvex);
			throw new EntityExistsException(
					"A Questionnaire with that name already exists.");
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Problem creating Questionnaire: " + ex);
			throw new ArkSystemException("Problem creating Questionnaire: "
					+ ex.getMessage());
		}
	}

	@Override
	public void deletePhenoFieldDataSetGroup(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO) {
		phenotypicDao.deletePhenoDataSetFieldGroup(phenoDataSetFieldGroupVO);
	}

	@Override
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplayByPhenoDataSetFieldAndGroup(PhenoDataSetField phenoDataSetField,PhenoDataSetGroup phenoDataSetGroup) {
		return phenotypicDao.getPhenoDataSetFieldDisplayByPhenoDataSetFieldAndGroup(phenoDataSetField,phenoDataSetGroup);
	}

	@Override
	public long getPhenoDataSetFieldGroupCount(PhenoDataSetGroup phenoDataSetGroup) {
		
		return phenotypicDao.getPhenoDataSetFieldGroupCount(phenoDataSetGroup);
	}
	
	public List<PhenoDataSetField> getPhenoDataSetFieldList(PhenoDataSetField phenoDataSetFieldCriteria){
		return phenotypicDao.getPhenoDataSetFieldList(phenoDataSetFieldCriteria);
	}

	@Override
	public List<PhenoDataSetGroup> getPhenoDataSetGroups(PhenoDataSetGroup phenoDataSetGroup, int first, int count) {
		
		return phenotypicDao.getPhenoDataSetGroups(phenoDataSetGroup, first, count);
	}

	@Override
	public void createPickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException, EntityExistsException {
		phenotypicDao.createPickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
		
	}

	@Override
	public void deletePickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory)throws ArkSystemException, EntityCannotBeRemoved {
		phenotypicDao.deletePickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
		
	}

	@Override
	public List<PickedPhenoDataSetCategory> getPickedPhenoDataSetCategories(Study study, ArkFunction arkFunction, ArkUser arkUser) {
		return phenotypicDao.getPickedPhenoDataSetCategories(study, arkFunction, arkUser);
	}

	@Override
	public List<PhenoDataSetCategory> getAvailablePhenoCategoryListNotPicked(Study study, ArkFunction arkFunctionPhenoCat,ArkFunction arkFunctionPhenoCollection, ArkUser arkUser)throws ArkSystemException {
		return phenotypicDao.getAvailablePhenoCategoryListNotPicked(study, arkFunctionPhenoCat, arkFunctionPhenoCollection, arkUser);
	}

	@Override
	public PickedPhenoDataSetCategory getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(Study study, ArkFunction arkFunction, ArkUser arkUser,PhenoDataSetCategory phenoDataSetCategory) {
		return phenotypicDao.getPickedPhenoDataSetCategoryFromPhenoDataSetCategory(study, arkFunction, arkUser, phenoDataSetCategory);
	}

	@Override
	public void deleteLinkPhenoDataSetCategoryField(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField)throws ArkSystemException, EntityCannotBeRemoved {
		phenotypicDao.deleteLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
		
	}

	@Override
	public void createLinkPhenoDataSetCategoryField(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException, EntityExistsException {
		phenotypicDao.createLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
		
	}

	@Override
	public List<PhenoDataSetField> getAvailablePhenoFieldListNotInLinked(Study study, ArkFunction arkFunctionPhenoDataDictionary,ArkFunction arkFunctionPhenoCollection, ArkUser arkUser)throws ArkSystemException {
		return phenotypicDao.getAvailablePhenoFieldListNotInLinked(study, arkFunctionPhenoDataDictionary, arkFunctionPhenoCollection, arkUser);
	}

	@Override
	public List<LinkPhenoDataSetCategoryField> getLinkPhenoDataSetCategoryFieldLst(Study study, ArkFunction arkFunction, ArkUser arkUser) {
		return phenotypicDao.getLinkPhenoDataSetCategoryFieldLst(study, arkFunction, arkUser);
	}

	@Override
	public void updatePickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException {
		phenotypicDao.updatePickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
		
	}
	@Override
	public List<PhenoDataSetField> getLinkedPhenoDataSetFieldsForSelectedCategories(Study study, ArkFunction arkFunction, ArkUser arkUser,List<PhenoDataSetCategory> phenoDataSetCategories) {
		return phenotypicDao.getLinkedPhenoDataSetFieldsForSelectedCategories(study, arkFunction, arkUser, phenoDataSetCategories);
	}

	@Override
	public LinkPhenoDataSetCategoryField getLinkPhenoDataSetCategoryField(Study study, ArkFunction arkFunction, ArkUser arkUser,PhenoDataSetCategory phenoDataSetCategory,PhenoDataSetField phenoDataSetField) {
		return phenotypicDao.getLinkPhenoDataSetCategoryField(study, arkFunction, arkUser, phenoDataSetCategory, phenoDataSetField);
	}

	@Override
	public boolean isSelectedCategoriesAlreadyAssignedToFields(Study study,ArkFunction arkFunction, ArkUser arkUser,List<PhenoDataSetCategory> phenoDataSetCategories) {
		return phenotypicDao.isSelectedCategoriesAlreadyAssignedToFields(study, arkFunction, arkUser, phenoDataSetCategories);
	}

	@Override
	public Long getNextAvailbleNumberForPickedCategory(Study study,ArkFunction arkFunction, ArkUser arkUser) {
		return phenotypicDao.getNextAvailbleNumberForPickedCategory(study, arkFunction, arkUser);
	}

	@Override
	public PickedPhenoDataSetCategory getSwapOverPickedPhenoDataSetCategoryForUpButton(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		return phenotypicDao.getSwapOverPickedPhenoDataSetCategoryForUpButton(pickedPhenoDataSetCategory);
	}

	@Override
	public PickedPhenoDataSetCategory getSwapOverPickedPhenoDataSetCategoryForDownButton(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		return phenotypicDao.getSwapOverPickedPhenoDataSetCategoryForDownButton(pickedPhenoDataSetCategory);
	}

	@Override
	public Long getNextAvailbleNumberForAssignedField(Study study,ArkFunction arkFunction, ArkUser arkUser,PhenoDataSetCategory phenoDataSetCategory) {
		return phenotypicDao.getNextAvailbleNumberForAssignedField(study, arkFunction, arkUser, phenoDataSetCategory);
	}

	@Override
	public PhenoDataSetCategory getPhenoDataSetCategoryForAssignedPhenoDataSetField(Study study, ArkFunction arkFunction, ArkUser arkUser,PhenoDataSetField phenoDataSetField) {
		return phenotypicDao.getPhenoDataSetCategoryForAssignedPhenoDataSetField(study, arkFunction, arkUser, phenoDataSetField);
	}

	@Override
	public LinkPhenoDataSetCategoryField getSwapOverPhenoDataSetFieldForUpButton(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField) {
		return phenotypicDao.getSwapOverPhenoDataSetFieldForUpButton(linkPhenoDataSetCategoryField);
	}

	@Override
	public LinkPhenoDataSetCategoryField getSwapOverPhenoDataSetFieldForDownButton(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField) {
		return phenotypicDao.getSwapOverPhenoDataSetFieldForDownButton(linkPhenoDataSetCategoryField);
	}

	@Override
	public void updateLinkPhenoDataSetCategoryField(LinkPhenoDataSetCategoryField linkPhenoDataSetCategoryField)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException {
		 phenotypicDao.updateLinkPhenoDataSetCategoryField(linkPhenoDataSetCategoryField);
	}

	@Override
	public Boolean isPickedPhenoDataSetCategoryIsAParentOfAnotherCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		return phenotypicDao.isPickedPhenoDataSetCategoryIsAParentOfAnotherCategory(pickedPhenoDataSetCategory);
	}

	@Override
	public List<PickedPhenoDataSetCategory> getChildrenOfPickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		return phenotypicDao.getChildrenOfPickedPhenoDataSetCategory( pickedPhenoDataSetCategory);
	}

	@Override
	public List<PickedPhenoDataSetCategory> getAllParentPickedPhenoDataSetCategories(Study study, ArkFunction arkFunction, ArkUser arkUser) {
		return phenotypicDao.getAllParentPickedPhenoDataSetCategories(study, arkFunction, arkUser);
	}

	@Override
	public List<LinkPhenoDataSetCategoryField> getLinkPhenoDataSetCategoryFieldsForPickedPhenoDataSetCategory(PickedPhenoDataSetCategory pickedPhenoDataSetCategory) {
		return phenotypicDao.getLinkPhenoDataSetCategoryFieldsForPickedPhenoDataSetCategory(pickedPhenoDataSetCategory);
	}
	
	@Override
	public PhenoDataSetCategory getPhenoDataFieldCategoryByNameStudyAndArkFunction(String name, Study study, ArkFunction arkFunction) {
		return phenotypicDao.getPhenoDataFieldCategoryByNameStudyAndArkFunction(name,study,arkFunction);
	}
	@Override
	public PhenoDataSetField getPhenoDataSetFieldByNameStudyArkFunction(String name,Study study,ArkFunction arkFunction){
		return phenotypicDao.getPhenoDataSetFieldByNameStudyArkFunction(name, study, arkFunction);
	}
	
	public void createPhenoDataSetFieldCategoryUpload(PhenoDataSetFieldCategoryUpload phenoDataSetFieldCategoryUpload){
		phenotypicDao.createPhenoDataSetFieldCategoryUpload(phenoDataSetFieldCategoryUpload);
	}
	
	public void createPhenoDataSetFieldUpload(PhenoFieldUpload phenoFieldUpload){
		phenotypicDao.createPhenoDataSetFieldUpload(phenoFieldUpload);
	}

	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroup(PhenoDataSetGroup phenoDataSetGroup) {
		return phenotypicDao.getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroup(phenoDataSetGroup);
	}

	@Override
	public void deletePickedCategoriesAndAllTheirChildren(Study study,ArkFunction arkFunction, ArkUser arkUser) {
		phenotypicDao.deletePickedCategoriesAndAllTheirChildren(study, arkFunction, arkUser);
		
	}

	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoFieldDisplaysIn(List<String> fieldNameCollection, Study study,ArkFunction arkFunction, PhenoDataSetGroup phenoDataSetGroup) {
		return phenotypicDao.getPhenoFieldDisplaysIn(fieldNameCollection, study, arkFunction, phenoDataSetGroup);
	}

	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoFieldDisplaysIn(Study study, ArkFunction arkFunction) {
		return phenotypicDao.getPhenoFieldDisplaysIn(study, arkFunction);
	}

	@Override
	public long getPhenoFieldGroupCount(Study study,ArkFunction arkFunction,Boolean status){
		return phenotypicDao.getPhenoFieldGroupCount(study,arkFunction,status);
	}

	@Override
	public PhenoDataSetField getPhenoDataSetFieldByNameStudyPFG(String FieldName, Study study, ArkFunction arkFunction,PhenoDataSetGroup phenoDataSetGroup)throws ArkRunTimeException,ArkSystemException {
		return phenotypicDao.getPhenoDataSetFieldByNameStudyPFG(FieldName, study, arkFunction, phenoDataSetGroup);
	}

	@Override
	public List<PhenoDataSetGroup> getPhenoDataSetFieldGroups(PhenoDataSetGroup phenoDataSetGroup, int first, int count) {
		return phenotypicDao.getPhenoDataSetFieldGroups(phenoDataSetGroup, first, count);
	}

	@Override
	public List<PhenoDataSetFieldDisplay> getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroupOrderByPhenoDataSetCategory(PhenoDataSetGroup phenoDataSetGroup) {
		return phenotypicDao.getPhenoDataSetFieldDisplayForPhenoDataSetFieldGroupOrderByPhenoDataSetCategory(phenoDataSetGroup);
	}

	@Override
	public List<PhenoDataSetField> getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroupAndPhenoDataSetCategory(PhenoDataSetGroup phenoDataSetGroupCriteria,PhenoDataSetCategory phenoDataSetCategory) {
		return phenotypicDao.getPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroupAndPhenoDataSetCategory(phenoDataSetGroupCriteria, phenoDataSetCategory);
	}

	@Override
	public List<Boolean> getPublishedSatusLst(Study study,ArkFunction arkFunction) {
		return phenotypicDao.getPublishedSatusLst(study, arkFunction); 
	}

	@Override
	public PhenoDataSetCategory getPhenoDataSetCategoryById(Long id) {
		return phenotypicDao.getPhenoDataSetCategoryById(id);
	}

	@Override
	public boolean isPhenoDataSetFieldCategoryBeingUsed(PhenoDataSetCategory phenoDataSetCategory) {
		return phenotypicDao.isPhenoDataSetCategoryAlreadyUsed(phenoDataSetCategory);
	}

	@Override
	public List<PhenoDataSetField> getAllPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(PhenoDataSetGroup phenoDataSetGroupCriteria) {
		return phenotypicDao.getAllPhenoDataSetFieldsLinkedToPhenoDataSetFieldGroup(phenoDataSetGroupCriteria);
	}

	@Override
	public boolean isEncodedValue(PhenoDataSetField phenoDataSetField, String value) {
		return phenotypicDao.isInEncodedValues(phenoDataSetField, value);
	}

	@Override
	public boolean isSameNameFieldGroupExsistsForTheStudy(PhenoDataSetFieldGroupVO phenoDataSetFieldGroupVO) {
		return phenotypicDao.isSameNameFieldGroupExsistsForTheStudy(phenoDataSetFieldGroupVO.getPhenoDataSetGroup().getName(),phenoDataSetFieldGroupVO.getPhenoDataSetGroup().getStudy()
				,phenoDataSetFieldGroupVO.getPhenoDataSetGroup().getArkFunction());
	}

	@Override
	public void deletePhenoDatasetData(PhenoDataSetCollection phenoDataSetCollection) {
		phenotypicDao.deletePhenoDatasetData(phenoDataSetCollection);
		
	}
	

	
	

}
