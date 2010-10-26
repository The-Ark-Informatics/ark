package au.org.theark.phenotypic.model.dao;

import java.util.List;

import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.entity.Upload;
import au.org.theark.phenotypic.model.entity.UploadCollection;

/**
 * Interface for all select/insert/update/delete operations on the backend database.
 * 
 */
public interface IPhenotypicDao {

	// Collection
	public Collection getCollection(Long id);
	public List<Collection> getCollectionMatches(Collection colExample);
	public void createCollection(Collection col);
	public void updateCollection(Collection col);
	public void deleteCollection(Collection col);
	
	// Collection Import
	public void createCollectionImport(CollectionImport collectionImport);
	public void updateCollectionImport(CollectionImport collectionImport);
	public void deleteCollectionImport(CollectionImport collectionImport);
	
	// Status
	public Status getStatus(Long statusId);
	public Status getStatusByName(String statusName);
	public void createStatus(Status status);
	public void updateStatus(Status status);
	public void deleteStatus(Status status);
	
	// Field
	public Field getField(Long fieldId);
	public Field getFieldByName(String fieldName);
	public Field getFieldByName(Long studyId, String fieldName);
	public void createField(Field field);
	public void updateField(Field field);
	public void deleteField(Field field);
	
	// Field Type
	public FieldType getFieldTypeByName(String fieldTypeName);
	public void createFieldType(FieldType fieldType);
	public void updateFieldType(FieldType fieldType);
	public void deleteFieldType(FieldType fieldType);

	// Field Data
	public void createFieldData(FieldData fieldData);
	public void updateFieldData(FieldData fieldData);
	public void deleteFieldData(FieldData fieldData);
	
	// Upload
	public void createUpload(Upload upload);
	public void updateUpload(Upload upload);
	public void deleteUpload(Upload upload);
	
	// Upload
	public void createUploadCollection(UploadCollection uploadCollection);
	public void updateUploadCollection(UploadCollection uploadCollection);
	public void deleteUploadCollection(UploadCollection uploadCollection);
}
