package au.org.theark.core.dao;

import au.org.theark.core.exception.EntityNotFoundException;

/**
 * @author nivedann
 *
 */
public interface IArkAuthorisation {
	
	/**
	 * This interface checks if the user is a Super Administrator 
	 */
	public boolean isSuperAdministrator(String userName) throws EntityNotFoundException;
	
	/**
	 * This interface checks if the user is a Administrator in the Ark System.
	 * @param userName
	 * @return
	 */
	public boolean isAdministator(String userName) throws EntityNotFoundException;

}
