package au.org.theark.geno.model.dao;

import au.org.theark.core.model.geno.entity.EncodedData;
import au.org.theark.core.model.geno.entity.Marker;
import au.org.theark.core.model.geno.entity.MarkerType;

public interface IGwasDao {
	
	public void createEncodedData(EncodedData ed);

	public EncodedData getEncodedData(Long encodedDataId);
	
    public MarkerType getMarkerType(String typeName);
    
    public void createMarker(Marker marker);
}
