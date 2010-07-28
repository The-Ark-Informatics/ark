package au.org.theark.gdmi.service;

import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.MetaData;

public interface ServiceInterface {
	
	public void create(MetaData metaData);
	public void createEncodedData(EncodedData ed);

}
