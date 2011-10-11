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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.DelimiterType;
import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.model.pheno.entity.FieldPhenoCollection;
import au.org.theark.core.model.pheno.entity.FieldType;
import au.org.theark.core.model.pheno.entity.FileFormat;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.PhenoCollectionUpload;
import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.model.pheno.entity.Status;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.BarChartResult;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.util.PhenoDataUploader;

@Transactional
@Service("phenotypicService")
public class PhenotypicServiceImpl implements IPhenotypicService {
	static final Logger							log	= LoggerFactory.getLogger(PhenotypicServiceImpl.class);

	private IArkCommonService<Void>	iArkCommonService;
	private IPhenotypicDao				phenotypicDao;
	private IStudyDao						studyDao;
	private Long							studyId;
	private Study							study;

	@Autowired
	public void setiArkCommonService(IArkCommonService<Void> iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	@Autowired
	public void setPhenotypicDao(IPhenotypicDao phenotypicDao) {
		this.phenotypicDao = phenotypicDao;
	}
	
	/* To access Hibernate Study Dao */
	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	/**
	 * A Phenotypic collection is the data storage or grouping of a particular set set of data, containing subjects with fields with field data values
	 * for a particular date collected
	 * 
	 * @param col
	 *           the collection object to be created
	 */
	public void createCollection(PhenoCollection col) {
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		// Newly created collections must start with a Created status
		Status status = phenotypicDao.getStatusByName(Constants.STATUS_CREATED);

		col.setStatus(status);
		col.setStudy(iArkCommonService.getStudy(studyId));

		phenotypicDao.createPhenoCollection(col);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Pheno Collection " + col.getName());
		ah.setEntityId(col.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	/**
	 * A Phenotypic collection is the data storage or grouping of a particular set set of data, containing subjects with fields with field data values
	 * for a particular date collected
	 * 
	 * @param phenoVo
	 *           the collectionVo object to be created
	 */
	public void createCollection(PhenoCollectionVO phenoVo) {
		phenotypicDao.createPhenoCollection(phenoVo);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Pheno Collection " + phenoVo.getPhenoCollection().getName());
		ah.setEntityId(phenoVo.getPhenoCollection().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deleteCollection(PhenoCollectionVO phenoVo) {
		phenotypicDao.deletePhenoCollection(phenoVo);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Pheno Collection " + phenoVo.getPhenoCollection().getName());
		ah.setEntityId(phenoVo.getPhenoCollection().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateCollection(PhenoCollectionVO phenoVo) {
		phenotypicDao.updatePhenoCollection(phenoVo);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Pheno Collection " + phenoVo.getPhenoCollection().getName());
		ah.setEntityId(phenoVo.getPhenoCollection().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	/**
	 * A Phenotypic collection import is the job that runs to import the data into the database. It contains relevant metadata about the import, such
	 * as start time and finish time
	 * 
	 * @param colImport
	 *           the collection import object to be created
	 */
	public void createCollectionImport(PhenoCollectionUpload colImport) {
		phenotypicDao.createCollectionUpload(colImport);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Pheno Collection Upload " + colImport.getId());
		ah.setEntityId(colImport.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateCollection(PhenoCollection colEntity) {
		phenotypicDao.updatePhenoCollection(colEntity);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Pheno Collection " + colEntity.getName());
		ah.setEntityId(colEntity.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void createField(Field field) {
		phenotypicDao.createField(field);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Field " + field.getName());
		ah.setEntityId(field.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD);
		iArkCommonService.createAuditHistory(ah);
	}

	public FieldType getFieldTypeByName(String fieldTypeName) {
		return phenotypicDao.getFieldTypeByName(fieldTypeName);
	}

	public Status getStatusByName(String statusName) {
		return phenotypicDao.getStatusByName(statusName);
	}

	public java.util.Collection<Status> getStatus() {
		return phenotypicDao.getStatus();
	}

	public Field getField(Long fieldId) {
		return phenotypicDao.getField(fieldId);
	}

	public PhenoCollection getPhenoCollection(Long id) {
		return phenotypicDao.getPhenoCollection(id);
	}

	public PhenoCollectionVO getPhenoCollectionAndFields(Long id) {
		return phenotypicDao.getPhenoCollectionAndFields(id);
	}

	public Collection<PhenoCollection> getPhenoCollectionByStudy(Study study) {
		return phenotypicDao.getPhenoCollectionByStudy(study);
	}

	public void createFieldData(FieldData fieldData) {
		phenotypicDao.createFieldData(fieldData);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Field Data For Field " + fieldData.getField().getName());
		ah.setEntityId(fieldData.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD_DATA);
		iArkCommonService.createAuditHistory(ah);
	}

	public java.util.Collection<FieldType> getFieldTypes() {
		return phenotypicDao.getFieldTypes();
	}

	public void deleteCollection(PhenoCollection collection) throws ArkSystemException, EntityCannotBeRemoved {
		phenotypicDao.createPhenoCollection(collection);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Pheno Collection " + collection.getName());
		ah.setEntityId(collection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deleteField(Field field) throws ArkSystemException, EntityCannotBeRemoved {
		phenotypicDao.deleteField(field);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Field " + field.getName());
		ah.setEntityId(field.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deleteFieldData(FieldData fieldData) {
		phenotypicDao.deleteFieldData(fieldData);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Field Data for Field " + fieldData.getField().getName());
		ah.setEntityId(fieldData.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD_DATA);
		iArkCommonService.createAuditHistory(ah);
	}

	public PhenoCollectionUpload getCollectionImport(Long id) {
		return phenotypicDao.getCollectionUpload(id);
	}

	public FieldType getFieldType(Long id) {
		return phenotypicDao.getFieldType(id);
	}

	public java.util.Collection<Field> searchField(Field field) {
		return phenotypicDao.searchField(field);
	}

	public Field getFieldByNameAndStudy(String fieldName, Study study) throws EntityNotFoundException {
		return phenotypicDao.getFieldByNameAndStudy(fieldName, study);
	}

	public void updateField(Field field) {
		phenotypicDao.updateField(field);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Field " + field.getName());
		ah.setEntityId(field.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateFieldData(FieldData fieldData) {
		phenotypicDao.updateFieldData(fieldData);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Field Data for Field " + fieldData.getField().getName());
		ah.setEntityId(fieldData.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD_DATA);
		iArkCommonService.createAuditHistory(ah);
	}

	public java.util.Collection<PhenoCollection> searchPhenotypicCollection(PhenoCollection phenotypicCollection) {
		return phenotypicDao.searchPhenotypicCollection(phenotypicCollection);
	}

	public Collection<FieldData> searchFieldDataByField(Field field) {
		return phenotypicDao.searchFieldDataByField(field);
	}

	public Collection<FieldData> searchFieldData(FieldData fieldData) {
		return phenotypicDao.searchFieldData(fieldData);
	}

	public FieldData getFieldData(FieldData fieldData) {
		return phenotypicDao.getFieldData(fieldData);
	}

	public void createUpload(PhenoUpload upload) {
		phenotypicDao.createUpload(upload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PhenoUpload for File " + upload.getFilename());
		ah.setEntityId(upload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void createUpload(UploadVO uploadVo) {
		phenotypicDao.createUpload(uploadVo);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PhenoUpload for File " + uploadVo.getUpload().getFilename());
		ah.setEntityId(uploadVo.getUpload().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateUpload(PhenoUpload upload) {
		phenotypicDao.updateUpload(upload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Created PhenoUpload for File " + upload.getFilename());
		ah.setEntityId(upload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deleteUpload(PhenoUpload upload) throws ArkSystemException, EntityCannotBeRemoved {
		phenotypicDao.deleteUpload(upload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted PhenoUpload " + upload.getFilename());
		ah.setEntityId(upload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public Collection<FileFormat> getFileFormats() {
		return phenotypicDao.getFileFormats();
	}

	public PhenoCollectionVO getPhenoCollectionAndUploads(Long id) {
		return phenotypicDao.getPhenoCollectionAndUploads(id);
	}

	public PhenoCollectionVO getPhenoCollectionAndUploads(PhenoCollection phenoCollection) {
		return phenotypicDao.getPhenoCollectionAndUploads(phenoCollection);
	}

	public PhenoUpload getUpload(Long id) {
		return phenotypicDao.getUpload(id);
	}

	public Collection<PhenoUpload> searchUpload(PhenoUpload upload) {
		return phenotypicDao.searchUpload(upload);
	}

	public Collection<DelimiterType> getDelimiterTypes() {
		return phenotypicDao.getDelimiterTypes();
	}

	public Collection<PhenoUpload> searchUploadByCollection(PhenoCollection phenoCollection) {
		return phenotypicDao.searchUploadByCollection(phenoCollection);
	}

	public void createPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		phenotypicDao.createCollectionUpload(phenoCollectionUpload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PhenoCollectionUpload " + phenoCollectionUpload.getId());
		ah.setEntityId(phenoCollectionUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deletePhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		phenotypicDao.deleteCollectionUpload(phenoCollectionUpload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted PhenoCollectionUpload " + phenoCollectionUpload.getId());
		ah.setEntityId(phenoCollectionUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public PhenoCollectionUpload getPhenoCollectionUpload(Long id) {
		return phenotypicDao.getPhenoCollectionUpload(id);
	}

	public Collection<PhenoCollectionUpload> searchPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		return phenotypicDao.searchPhenoCollectionUpload(phenoCollectionUpload);
	}

	public void updatePhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		phenotypicDao.updateCollectionUpload(phenoCollectionUpload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated PhenoCollectionUpload " + phenoCollectionUpload.getId());
		ah.setEntityId(phenoCollectionUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public int getCountOfFieldsInStudy(Study study) {
		return phenotypicDao.getCountOfFieldsInStudy(study);
	}

	public int getCountOfFieldsWithDataInStudy(Study study) {
		return phenotypicDao.getCountOfFieldsWithDataInStudy(study);
	}

	public void uploadPhenotypicDataFile(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar) {
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);

		Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenoCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);
		;

		try {
			InputStream is = new FileInputStream(file);

			log.debug("Importing file");
			pi.uploadMatrixFieldDataFile(is, file.length());
		}
		catch (IOException ioe) {
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse) {
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
	}

	public StringBuffer uploadAndReportPhenotypicDataFile(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar) {
		StringBuffer importReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);

		Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenoCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);

		try {
			InputStream inputStream = new FileInputStream(file);

			log.debug("Importing file");
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = pi.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			importReport = pi.uploadAndReportMatrixFieldDataFile(inputStream, file.length());
		}
		catch (IOException ioe) {
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse) {
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return importReport;
	}

	public void uploadPhenotypicDataFile(InputStream inputStream, String fileFormat, char delimiterChar) {
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);

		Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenoCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);

		try {
			log.debug("Importing pheno data file");
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = pi.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			pi.uploadMatrixFieldDataFile(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse) {
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
	}

	public StringBuffer uploadAndReportPhenotypicDataFile(InputStream inputStream, String fileFormat, char delimiterChar) {
		StringBuffer uploadReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);

		Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenoCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);

		try {
			log.info("Importing pheno file");
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = pi.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			uploadReport = pi.uploadAndReportMatrixFieldDataFile(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse) {
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return uploadReport;
	}

	public StringBuffer uploadAndReportPhenotypicDataFile(UploadVO uploadVo) {
		StringBuffer uploadReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		String filename = uploadVo.getFileUpload().getClientFileName();
		String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		FileFormat fileFormatObj = new FileFormat();
		fileFormatObj.setName(fileFormat);
		uploadVo.getUpload().setFileFormat(fileFormatObj);

		Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = null;

		if (sessionCollectionId == null) {
			phenoCollection = uploadVo.getPhenoCollection();
		}
		else {
			phenoCollection = phenotypicDao.getPhenoCollection(sessionCollectionId);
		}

		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, uploadVo.getUpload().getDelimiterType().getDelimiterCharacter());

		try {
			log.info("Importing pheno file");
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			if (uploadVo.getUpload().getFileFormat().getName().equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = pi.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}

			uploadReport = pi.uploadAndReportMatrixFieldDataFile(inputStream, uploadVo.getFileUpload().getSize());
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse) {
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		catch (IOException e) {
			log.error(Constants.IO_EXCEPTION + e);
		}
		return uploadReport;
	}

	public Collection<FieldData> searchFieldDataBySubjectAndDateCollected(LinkSubjectStudy linkSubjectStudy, java.util.Date dateCollected) {
		return phenotypicDao.searchFieldDataBySubjectAndDateCollected(linkSubjectStudy, dateCollected);
	}

	public Collection<PhenoUpload> searchFieldUpload(PhenoUpload phenoUpload) {
		return phenotypicDao.searchFieldUpload(phenoUpload);
	}

	public Collection<FieldPhenoCollection> getFieldPhenoCollection(PhenoCollection phenoCollection) {
		return phenotypicDao.getFieldPhenoCollection(phenoCollection);
	}

	public int getCountOfCollectionsInStudy(Study study) {
		return phenotypicDao.getCountOfCollectionsInStudy(study);
	}

	public int getCountOfCollectionsWithDataInStudy(Study study) {
		return phenotypicDao.getCountOfCollectionsWithDataInStudy(study);
	}

	public void createFieldPhenoCollection(FieldPhenoCollection fieldPhenoCollection) {
		phenotypicDao.createFieldPhenoCollection(fieldPhenoCollection);
	}

	public void updateFieldPhenoCollection(FieldPhenoCollection fieldPhenoCollection) {
		phenotypicDao.updateFieldPhenoCollection(fieldPhenoCollection);
	}

	public FieldPhenoCollection getFieldPhenoCollection(FieldPhenoCollection fieldPhenoCollection) {
		return phenotypicDao.getFieldPhenoCollection(fieldPhenoCollection);
	}

	public int getStudyFieldDataCount(PhenoCollectionVO phenoCollectionVoCriteria) {
		return phenotypicDao.getStudyFieldDataCount(phenoCollectionVoCriteria);
	}

	public List<PhenoCollectionVO> searchPageableFieldData(PhenoCollectionVO phenoCollectionVoCriteria, int first, int count) {
		return phenotypicDao.searchPageableFieldData(phenoCollectionVoCriteria, first, count);
	}

	public DelimiterType getDelimiterType(Long id) {
		return phenotypicDao.getDelimiterType(id);
	}

	public int clearPhenoCollection(PhenoCollection phenoCollection) {
		int rowsDeleted = phenotypicDao.clearPhenoCollection(phenoCollection);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Cleared PhenoCollection " + phenoCollection.getId() + " " + rowsDeleted + " field data rows deleted");
		ah.setEntityId(phenoCollection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
		return rowsDeleted;
	}

	public List<BarChartResult> getFieldsWithDataResults(Study study) {
		return phenotypicDao.getFieldsWithDataResults(study);
	}

	public boolean fieldHasData(Field field) {
		return phenotypicDao.fieldHasData(field);
	}

	public boolean phenoCollectionHasData(PhenoCollection phenoCollection) {
		return phenoCollectionHasData(phenoCollection);
	}

	public PhenoCollection getPhenoCollectionByUpload(PhenoUpload upload) {
		return phenotypicDao.getPhenoCollectionByUpload(upload);
	}

	public String getDelimiterTypeByDelimiterChar(char phenotypicDelimChr) {
		return phenotypicDao.getDelimiterTypeByDelimiterChar(phenotypicDelimChr);
	}

	public FileFormat getFileFormatByName(String name) {
		return phenotypicDao.getFileFormatByName(name);
	}

	public List<PhenoData> createOrUpdatePhenoData(List<PhenoData> phenoDataList) {

		List<PhenoData> listOfExceptions = new ArrayList<PhenoData>();
		/* Iterate the list and call DAO to persist each Item */
		for (PhenoData phenoData : phenoDataList) {
			
			
			try{
			/* Insert the Field if it does not have a  ID and has the required fields */
				if( canInsert(phenoData)) {

					phenotypicDao.createPhenoData(phenoData);
					Long id = phenoData.getCustomFieldDisplay().getCustomField().getId();
					
					CustomField customField = iArkCommonService.getCustomField(id);
					customField.setCustomFieldHasData(true);
					CustomFieldVO customFieldVO = new CustomFieldVO();
					customFieldVO.setCustomField(customField);
					
					iArkCommonService.updateCustomField(customFieldVO);

				}else if(canUpdate(phenoData)){
					
					//If there was bad data uploaded and the user has now corrected it on the front end then set/blank out the error data value and updated the record.
					if(phenoData.getErrorDataValue() != null){
						phenoData.setErrorDataValue(null);
					} 
					phenotypicDao.updatePhenoData(phenoData);
				
				}else if(canDelete(phenoData)){
					//Check if the CustomField is used by anyone else and if not set the customFieldHasData to false;
					Long count  = phenotypicDao.isCustomFieldUsed(phenoData);
					
					phenotypicDao.deletePhenoData(phenoData);
					if(count <= 1){
						//Then update the CustomField's hasDataFlag to false;
						Long id = phenoData.getCustomFieldDisplay().getCustomField().getId();//Reload since the session was closed in the front end and the child objects won't be lazy loaded
						CustomField customField = iArkCommonService.getCustomField(id);
						customField.setCustomFieldHasData(false);
						CustomFieldVO customFieldVO = new CustomFieldVO();
						customFieldVO.setCustomField(customField);
						iArkCommonService.updateCustomField(customFieldVO); //Update it
						
					}
				}
			}catch(Exception someException){
				listOfExceptions.add(phenoData);//Continue with rest of the list
			}
		}
		
		return listOfExceptions;
	}
	
	/**
	 * In order to delete it must satisfy the following conditions
	 * 1. PhenoData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. PhenoData should have a valid Subject linked to it and must not be null AND
	 * 3. PhenoData.TextDataValue is NULL OR is EMPTY
	 * 4. PhenoData.NumberDataValue is NULL
	 * 5. PhenoData.DatewDataValue is NULL
	 * When these conditiosn are satisfied this method will return Boolean TRUE
	 * @param phenoData
	 * @return
	 */
	private Boolean canDelete(PhenoData phenoData){
		Boolean flag = false;
		
		if(phenoData.getId() != null &&  phenoData.getPhenotypicCollection() != null && 
				( phenoData.getTextDataValue() == null  	||		
				  phenoData.getTextDataValue().isEmpty()  	|| 
				  phenoData.getNumberDataValue() == null 	||
				  phenoData.getDateDataValue() == null ) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Update a PhenoData instance the following conditions must be met
	 * 1. PhenoData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. PhenoData should have a valid Subject linked to it and must not be null AND
	 * 3. PhenoData.TextDataValue is NOT NULL AND NOT EMPTY OR
	 * 4. PhenoData.NumberDataValue is NOT NULL
	 * 5. PhenoData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param phenoData
	 * @return
	 */
	private Boolean canUpdate(PhenoData phenoData){
		Boolean flag = false;
		
		if(phenoData.getId() != null && phenoData.getPhenotypicCollection() != null && 
				(( phenoData.getTextDataValue() != null 	&& 
				   !phenoData.getTextDataValue().isEmpty()) || 
				   phenoData.getDateDataValue() != null  	|| 
				   phenoData.getNumberDataValue() != null) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Insert a PhenoData instance the following conditions must be met.
	 * 1. PhenoData must be a transient entity(Not yet associated with an ID/PK) AND
	 * 2. PhenoData should have a valid Subject linked to it and must not be null AND
	 * 3. PhenoData.TextDataValue is NOT NULL  OR
	 * 4. PhenoData.NumberDataValue is NOT NULL OR
	 * 5. PhenoData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param phenoData
	 * @return
	 */
	private Boolean canInsert(PhenoData phenoData){
		Boolean flag = false;
		
		if(phenoData.getId() == null &&  phenoData.getPhenotypicCollection() != null && 
				(		phenoData.getNumberDataValue() != null || 
						phenoData.getTextDataValue() != null 	|| 
						phenoData.getDateDataValue() != null )){
			
			flag=true;
			
		}
		return flag;
	}

	public int getPhenoDataCount(PhenotypicCollection phenoCollection) {
		return phenotypicDao.getPhenoDataCount(phenoCollection);
	}

	public List<PhenoData> getPhenoDataList(PhenotypicCollection phenoCollection, int first, int count) {
		List<PhenoData> resultsList = phenotypicDao.getPhenoDataList(phenoCollection, first, count);
		return resultsList;
	}

	public PhenotypicCollection getPhenotypicCollection(Long id) {
		return phenotypicDao.getPhenotypicCollection(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO) throws EntityExistsException, ArkSystemException{
		try{
			phenotypicDao.createCustomFieldGroup(customFieldGroupVO);	
		}catch (ConstraintViolationException cvex) {
			log.error("A Questionnaire with this name for the given study  exists.: " + cvex);
			throw new EntityExistsException("A Study Component already exits.");
		}
		catch (Exception ex) {
			log.error("Problem creating Questionnaire: " + ex);
			throw new ArkSystemException("Problem creating Questionnaire: " + ex.getMessage());
		}
		
	}

	public int getPhenotypicCollectionCount(PhenoDataCollectionVO criteria) {
		return phenotypicDao.getPhenotypicCollectionCount(criteria);
	}

	public List<PhenotypicCollection> searchPageablePhenotypicCollections(PhenoDataCollectionVO criteria, int first, int count) {
		return phenotypicDao.searchPageablePhenotypicCollection(criteria, first, count);
	}
	
	public List<CustomField> getCustomFieldsLinkedToCustomFieldGroup(CustomFieldGroup customFieldCriteria){
		return phenotypicDao.getCustomFieldsLinkedToCustomFieldGroup(customFieldCriteria);
	}
	
	public void updateCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO) throws EntityExistsException,ArkSystemException{
		 phenotypicDao.updateCustomFieldGroup(customFieldGroupVO);
	}

}
