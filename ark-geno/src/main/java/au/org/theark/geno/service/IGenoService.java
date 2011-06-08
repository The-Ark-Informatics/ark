package au.org.theark.geno.service;

import java.util.Collection;

import au.org.theark.core.model.geno.entity.CollectionImport;
import au.org.theark.core.model.geno.entity.DelimiterType;
import au.org.theark.core.model.geno.entity.EncodedData;
import au.org.theark.core.model.geno.entity.FileFormat;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.MetaData;
import au.org.theark.core.model.geno.entity.MetaDataField;
import au.org.theark.core.model.geno.entity.MetaDataType;
import au.org.theark.core.model.geno.entity.Status;
import au.org.theark.core.model.geno.entity.Upload;
import au.org.theark.core.model.geno.entity.UploadCollection;

public interface IGenoService {
	// Create
	public void createCollection(GenoCollection col);
	public void createCollectionImport(CollectionImport colImport);
	public void createMetaData(MetaData metaData);
	public void createEncodedData(EncodedData ed);
	public void createMetaDataField(MetaDataField mdf);
	public void createUploadCollection(UploadCollection uploadCol);

	// Read
	public Collection<GenoCollection> searchGenoCollection(
			GenoCollection genoCollectionCriteria);
	public Collection<UploadCollection> searchUploadCollection(
			UploadCollection uploadCollectionCriteria);

	public MetaDataType getMetaDataTypeByName(String typeName);
	public Status getStatusByName(String statusName);
	public MetaDataField getMetaDataField(Long metaDataFieldId);
	public GenoCollection getCollection(Long collectionId);
	public EncodedData getEncodedData(Long encodedDataId);
	public void getEncodedBit(Long encodedDataId);

	public Collection<Status> getStatusCollection();
	public Collection<FileFormat> getFileFormatCollection();
	public Collection<DelimiterType> getDelimiterTypeCollection();
	
	// Update
	public void updateCollection(GenoCollection col);
	
	// Delete
	public void deleteCollection(GenoCollection col);
	public void deleteUploadCollection(UploadCollection upload);

	// Test
	public Long newEncodedData(GenoCollection col);
	public void testGWASImport();

}
