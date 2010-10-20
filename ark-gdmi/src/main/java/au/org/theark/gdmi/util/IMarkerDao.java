package au.org.theark.gdmi.util;

import au.org.theark.gdmi.model.dao.IMapStorage;
import au.org.theark.gdmi.model.entity.MarkerGroup;

public interface IMarkerDao extends IMapStorage {

	
	/**
	 * Called to setup the MarkerGroup id and user id for the storage 
	 * @param markerGroupId
	 * @param userId
	 */
	void setup (MarkerGroup markerGroup, String userId);
	
}
