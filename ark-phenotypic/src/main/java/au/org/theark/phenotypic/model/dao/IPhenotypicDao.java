package au.org.theark.phenotypic.model.dao;

import java.util.List;

import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldType;


public interface IPhenotypicDao {

	public void createCollection(Collection col);
	
	public List<Collection> getCollectionMatches(Collection colExample);
	
	/**
	 * Interface to get a list of Study Status reference data from the backend.
	 * These study status' are no associated with a study as such but can be used for
	 * displaying a list of options for a particular study.
	 * @return
	 */

	// Collection
	public Collection getCollection(Long id);
	public void createCollectionImport(CollectionImport colImport);
	public void updateCollection(Collection colEntity);
	
	// Status
	public Status getStatusByName(String statusName);
	
	// Field
	public Field getField(Long fieldId);
	public Field getFieldByName(String fieldName);
	public void createField(Field f);
	public void updateField(Field f);
	
	// Field Type
	public FieldType getFieldTypeByName(String fieldTypeName);
	public void createFieldType(FieldType fieldType);
	public void updateFieldType(FieldType fieldType);

	// FieldData
	public void createFieldData(FieldData fieldData);
	public void updateFieldData(FieldData fieldData);
}
