package au.org.theark.phenotypic.model.dao;

import java.util.Collection;
import java.util.Date;

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
import au.org.theark.phenotypic.model.entity.PhenoUpload;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.model.vo.UploadVO;

/**
 * Interface for all select/insert/update/delete operations on the backend database.
 * 
 */
public interface IPhenotypicDao {

	// Collection
	public PhenoCollection getPhenotypicCollection(Long id);
	public java.util.Collection<PhenoCollection> getPhenotypicCollection();
	public java.util.Collection<PhenoCollection> getPhenotypicCollectionByStudy(Study study);
	public PhenoCollectionVO getPhenoCollectionAndFields(Long id);
	public java.util.Collection<PhenoCollection> searchPhenotypicCollection(PhenoCollection collectionToMatch);
	public void createPhenoCollection(PhenoCollection collection);
	public void createPhenoCollection(PhenoCollectionVO collectionVo);
	public void updatePhenoCollection(PhenoCollection collection);
	public void updatePhenoCollection(PhenoCollectionVO collectionVo);
	public void deletePhenoCollection(PhenoCollection collection);
	public void deletePhenoCollection(PhenoCollectionVO collectionVo);
	
	// FieldCollection
	public java.util.Collection<FieldPhenoCollection> getFieldPhenoCollection(PhenoCollection phenoCollection);
	
	// Collection Upload
	public PhenoCollectionUpload getCollectionUpload(Long id);
	public java.util.Collection<PhenoCollectionUpload> getCollectionUpload();
	public java.util.Collection<PhenoCollectionUpload> searchCollectionUpload(PhenoCollectionUpload phenoCollectionUploadToMatch);
	public void createCollectionUpload(PhenoCollectionUpload phenoCollectionUpload);
	public void updateCollectionUpload(PhenoCollectionUpload phenoCollectionUpload);
	public void deleteCollectionUpload(PhenoCollectionUpload phenoCollectionUpload);
	
	// Status
	public Status getStatus(Long statusId);
	public Status getStatusByName(String statusName);
	public java.util.Collection<Status> getStatus();
	public void createStatus(Status status);
	public void updateStatus(Status status);
	
	// Field
	public Field getField(Long fieldId);
	public java.util.Collection<Field> getField();
	public Field getFieldByNameAndStudy(String fieldName, Study study);
	public java.util.Collection<Field> searchField(Field field);
	public void createField(Field field);
	public void updateField(Field field);
	public void deleteField(Field field);
	
	// Field Type
	public FieldType getFieldType(Long id);
	public FieldType getFieldTypeByName(String fieldTypeName);
	public java.util.Collection<FieldType> getFieldTypes();
	public void createFieldType(FieldType fieldType);
	public void updateFieldType(FieldType fieldType);

	// Field Data
	public FieldData getFieldData(Long id);
	public java.util.Collection<FieldData> getFieldDataByCollectionAndField(PhenoCollection phenoCollection, Field field);
	public FieldData getFieldData(PhenoCollection phenoCollection, LinkSubjectStudy linkSubjectStudy, Field field, Date dateCollected, String value);
	public Collection<FieldData> searchFieldDataByField(Field field);
	public Collection<FieldData> searchFieldData(FieldData fieldData);
	public void createFieldData(FieldData fieldData);
	public void updateFieldData(FieldData fieldData);
	public void deleteFieldData(FieldData fieldData);
	
	// Field Data Log
	public FieldDataLog getFieldDataLog(Long id);
	public java.util.Collection<FieldDataLog> getFieldDataLogByField(Field field);
	public void createFieldDataLog(FieldDataLog fieldDataLog);
	
	// Upload
	public java.util.Collection<PhenoUpload> searchUpload(PhenoUpload upload);
	public PhenoCollectionVO getPhenoCollectionAndUploads(Long id);
	public PhenoUpload getUpload(Long id);
	public void createUpload(PhenoUpload upload);
	public void createUpload(UploadVO uploadVo);
	public void updateUpload(PhenoUpload upload);
	public void deleteUpload(PhenoUpload upload);
	
	// File Formats
	public java.util.Collection<FileFormat> getFileFormats();
	
	// Delimiter Types
	public java.util.Collection<DelimiterType> getDelimiterTypes();
}
