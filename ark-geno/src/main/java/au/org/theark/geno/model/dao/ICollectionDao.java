package au.org.theark.geno.model.dao;

import java.util.List;

import au.org.theark.geno.model.entity.Collection;
import au.org.theark.geno.model.entity.CollectionImport;
import au.org.theark.geno.model.entity.MetaData;
import au.org.theark.geno.model.entity.MetaDataField;
import au.org.theark.geno.model.entity.MetaDataType;
import au.org.theark.geno.model.entity.Status;

public interface ICollectionDao {

	public void createCollection(Collection col);
	
	public List<Collection> getCollectionMatches(Collection colExample);
	
	/**
	 * Interface to get a list of Study Status reference data from the backend.
	 * These study status' are no associated with a study as such but can be used for
	 * displaying a list of options for a particular study.
	 * @return
	 */

	public Collection getCollection(Long id);
	
	public void updateCollection(Collection colEntity);
	
	public void createMetaData(MetaData metaData);
	
	public Status getStatusByName(String statusName);
	
	public void createMetaDataField(MetaDataField mdf);

	//public MetaDataField getMetaDataFieldByName(Long studyId, String mdfName);

	public MetaDataField getMetaDataField(Long metaDataFieldId);

	public MetaDataType getMetaDataTypeByName(String typeName);
    //TODO: Patch the Long studyId with a true study object?
    //(it should already be in context)

	public void createCollectionImport(CollectionImport colImport);

}
