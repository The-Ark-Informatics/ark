package au.org.theark.worktracking.service;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;

public interface IWorkTrackingService {

	/**
	 * 
	 * @return List of {@link ResearcherStatus} assigned to researcher
	 */
	public List<ResearcherStatus> getResearcherStatuses();

	/**
	 * 
	 * @return List of {@link ResearcherRole} assigned to the researchers.
	 */
	public List<ResearcherRole> getResearcherRoles();
	
	/**
	 * 
	 * @return List of {@link BillingType} assigned to the researchers.
	 */
	public List<BillingType> getResearcherBillingTypes();
	
	/**
	 * Create new {@link Researcher} object
	 * @param researcher
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void create(Researcher researcher) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * Update selected {@link Researcher} object
	 * @param researcher
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void update(Researcher researcher) throws ArkSystemException, EntityExistsException ;

	/**
	 * Delete selected {@link Researcher} object
	 * @param researcher
	 * @throws ArkSystemException
	 * @throws EntityCannotBeRemoved
	 */
	public void delete(Researcher researcher) throws ArkSystemException, EntityCannotBeRemoved ;

	/**
	 * Search Researcher's for the given properties
	 * @param researcher
	 * @return Selected Researcher List.
	 */
	public List<Researcher> searchResearcher(Researcher researcher) ;
}
