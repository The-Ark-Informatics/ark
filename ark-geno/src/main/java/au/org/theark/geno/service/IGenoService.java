package au.org.theark.geno.service;

import au.org.theark.geno.model.entity.Collection;
import au.org.theark.geno.model.entity.CollectionImport;
import au.org.theark.geno.model.entity.EncodedData;
import au.org.theark.geno.model.entity.MetaData;
import au.org.theark.geno.model.entity.MetaDataField;
import au.org.theark.geno.model.entity.MetaDataType;
import au.org.theark.geno.model.entity.Status;

public interface IGenoService {
	
	public void createCollection(Collection col);
	public void createCollectionImport(CollectionImport colImport);
	public void createMetaData(MetaData metaData);
	public void createEncodedData(EncodedData ed);
	public void createMetaDataField(MetaDataField mdf);

	public MetaDataType getMetaDataTypeByName(String typeName);
	public Status getStatusByName(String statusName);
	public MetaDataField getMetaDataField(Long metaDataFieldId);
	public Collection getCollection(Long collectionId);
	public EncodedData getEncodedData(Long encodedDataId);
	public void getEncodedBit(Long encodedDataId);
	public Long newEncodedData(Collection col);
	public void testGWASImport();
}
