package au.org.theark.geno.model.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.geno.model.entity.DelimiterType;
import au.org.theark.geno.model.entity.FileFormat;
import au.org.theark.geno.model.entity.GenoCollection;
import au.org.theark.geno.model.entity.CollectionImport;
import au.org.theark.geno.model.entity.MetaData;
import au.org.theark.geno.model.entity.MetaDataField;
import au.org.theark.geno.model.entity.MetaDataType;
import au.org.theark.geno.model.entity.Status;
import au.org.theark.geno.model.entity.Upload;
import au.org.theark.geno.model.entity.UploadCollection;

public interface ICollectionDao {

	// Create
	public void createCollection(GenoCollection col);
	public void createMetaData(MetaData metaData);
	public void createCollectionImport(CollectionImport colImport);
	public void createMetaDataField(MetaDataField mdf);
	public void createUploadCollection(UploadCollection upload);

	// Read
	public List<GenoCollection> getCollectionMatches(GenoCollection colExample);
	public Collection<Status> getStatusCollection();
	public Collection<FileFormat> getFileFormatCollection();
	public Collection<DelimiterType> getDelimiterTypeCollection();
	public GenoCollection getCollection(Long id);
	public Status getStatusByName(String statusName);
	//public MetaDataField getMetaDataFieldByName(Long studyId, String mdfName);
	public MetaDataField getMetaDataField(Long metaDataFieldId);
	public MetaDataType getMetaDataTypeByName(String typeName);
    //TODO: Patch the Long studyId with a true study object?
    //(it should already be in context)
	public Collection<UploadCollection> getFileUploadMatches(
			UploadCollection uploadCollectionCriteria);

	// Update
	public void updateCollection(GenoCollection colEntity);
	public void updateMetaData(MetaData mdEntity);
	public void updateMetaDataField(MetaDataField mdf);
	
	// Delete
	public void deleteCollection(GenoCollection col);
	public void deleteUploadCollection(UploadCollection uploadCollection);

}
