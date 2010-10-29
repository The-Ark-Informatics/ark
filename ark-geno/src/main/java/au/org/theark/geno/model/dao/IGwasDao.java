package au.org.theark.geno.model.dao;

import au.org.theark.geno.model.entity.EncodedData;
import au.org.theark.geno.model.entity.Marker;
import au.org.theark.geno.model.entity.MarkerType;

public interface IGwasDao {
	
	public void createEncodedData(EncodedData ed);

	public EncodedData getEncodedData(Long encodedDataId);
	
    public MarkerType getMarkerType(String typeName);
    
    public void createMarker(Marker marker);
}
