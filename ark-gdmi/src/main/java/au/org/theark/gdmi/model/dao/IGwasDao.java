package au.org.theark.gdmi.model.dao;

import au.org.theark.gdmi.model.entity.EncodedData;

public interface IGwasDao {
	
	public void createEncodedData(EncodedData ed);

	public EncodedData getEncodedData(Long encodedDataId);

}
