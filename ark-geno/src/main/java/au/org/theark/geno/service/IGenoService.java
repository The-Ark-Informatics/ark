package au.org.theark.geno.service;

import java.util.Collection;

import au.org.theark.geno.model.entity.GenoCollection;
import au.org.theark.geno.model.entity.CollectionImport;
import au.org.theark.geno.model.entity.EncodedData;
import au.org.theark.geno.model.entity.MetaData;
import au.org.theark.geno.model.entity.MetaDataField;
import au.org.theark.geno.model.entity.MetaDataType;
import au.org.theark.geno.model.entity.Status;

public interface IGenoService {
	// Create
	public void createCollection(GenoCollection col);
	public void createCollectionImport(CollectionImport colImport);
	public void createMetaData(MetaData metaData);
	public void createEncodedData(EncodedData ed);
	public void createMetaDataField(MetaDataField mdf);

	// Read
	public MetaDataType getMetaDataTypeByName(String typeName);
	public Status getStatusByName(String statusName);
	public MetaDataField getMetaDataField(Long metaDataFieldId);

	public GenoCollection getCollection(Long collectionId);
	public Collection<Status> getStatus();
	public Collection<GenoCollection> searchGenoCollection(
			GenoCollection genoCollection);

	public EncodedData getEncodedData(Long encodedDataId);
	public void getEncodedBit(Long encodedDataId);

	// Update
	public void updateCollection(GenoCollection col);
	
	// Delete
	public void deleteCollection(GenoCollection col);

	// Test
	public Long newEncodedData(GenoCollection col);
	public void testGWASImport();

}
