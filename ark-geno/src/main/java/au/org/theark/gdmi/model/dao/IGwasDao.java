package au.org.theark.gdmi.model.dao;

import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.Marker;
import au.org.theark.gdmi.model.entity.MarkerType;

public interface IGwasDao {
	
	public void createEncodedData(EncodedData ed);

	public EncodedData getEncodedData(Long encodedDataId);
	
    public MarkerType getMarkerType(String typeName);
    
    public void createMarker(Marker marker);
}
