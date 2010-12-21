package au.org.theark.phenotypic.service;

import java.util.Collection;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;

public interface IPhenotypicService {
	
	// PhenoCollection
	public PhenoCollection getPhenoCollection(Long id);
	public java.util.Collection<PhenoCollection> getPhenoCollectionByStudy(Study study);
	public java.util.Collection<PhenoCollection> searchPhenotypicCollection(PhenoCollection phenotypicCollection);
	public PhenoCollectionVO getPhenoCollectionAndFields(Long id);
	public void createCollection(PhenoCollection col);
	public void createCollection(PhenoCollectionVO colVo);
	public void updateCollection(PhenoCollection col);
	public void updateCollection(PhenoCollectionVO colVo);
	public void deleteCollection(PhenoCollection col);
	public void deleteCollection(PhenoCollectionVO colVo);
	
	// Collection Import
	public CollectionImport getCollectionImport(Long id);
	public void createCollectionImport(CollectionImport collectionImport);
	public void updateCollectionImport(CollectionImport collectionImport);
	public void deleteCollectionImport(CollectionImport collectionImport);

	// Field
	public Field getField(Long fieldId);
	public java.util.Collection<Field> searchField(Field field);
	public Field getFieldByNameAndStudy(String fieldName, Study study);
	public void createField(Field field);
	public void updateField(Field field);
	public void deleteField(Field field);
	
	// FieldType
	public FieldType getFieldType(Long id);
	public FieldType getFieldTypeByName(String fieldTypeName);
	public java.util.Collection<FieldType> getFieldTypes();
	
	// FieldData
	public Collection<FieldData> searchFieldDataByField(Field field);
	public Collection<FieldData> searchFieldData(FieldData fieldData);
	public void createFieldData(FieldData fieldData);
	public void updateFieldData(FieldData fieldData);
	public void deleteFieldData(FieldData fieldData);
	
	// Status
	public Status getStatusByName(String statusName);
	public java.util.Collection<Status> getStatus();
	
	// Validate phenotypic data file
	public java.util.Collection<String> validatePhenotypicDataFile();
	
	// Import phenotypic data file
	public void importPhenotypicDataFile();
}
