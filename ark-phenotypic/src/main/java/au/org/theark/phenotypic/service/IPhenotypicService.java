package au.org.theark.phenotypic.service;

import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.Status;

public interface IPhenotypicService {
	
	// Collection
	public Collection getCollection(Long id);
	public void createCollection(Collection col);
	public void createCollectionImport(CollectionImport collectionImport);

	// Field
	public Field getField(Long fieldId);
	public void createField(Field field);
	
	// FieldType
	public FieldType getFieldTypeByName(String fieldTypeName);
	
	// FieldData
	public void createFieldData(FieldData fieldData);
	
	// Status
	public Status getStatusByName(String statusName);
	
	// Test stub
	public void testPhenotypicImport();
	
	// Import phenotypic data file
	public void importPhenotypicDataFile();
}
