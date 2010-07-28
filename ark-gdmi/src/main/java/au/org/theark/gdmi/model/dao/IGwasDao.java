package au.org.theark.gdmi.model.dao;

import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.MetaData;
import au.org.theark.gdmi.model.entity.MetaDataField;
import au.org.theark.gdmi.model.entity.MetaDataType;



public interface IGwasDao {
	
	public void create(MetaData metaData);
	public MetaDataType getMetaDataType(String dataType);
	public void createMetaDataField(String dataType, String name, String description);
	public MetaDataField getMetaDataField(String name);
	public void createMetaData(MetaDataField mdf, String value);
	public void createEncodedData(EncodedData ed);

}
