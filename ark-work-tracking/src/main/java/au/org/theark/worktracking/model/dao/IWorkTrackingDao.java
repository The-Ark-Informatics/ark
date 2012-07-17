package au.org.theark.worktracking.model.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;

public interface IWorkTrackingDao {

	/**
	 * 
	 * @return List of {@link ResearcherStatus} assigned to researchers.
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
	 * Create new {@link Researcher} record
	 * @param researcher
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void createResearcher(Researcher researcher) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * Update selected {@link Researcher} record
	 * @param researcher
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void updateResearcher(Researcher researcher) throws ArkSystemException, EntityExistsException ;

	/**
	 * Delete selected {@link Researcher} record
	 * @param researcher
	 * @throws ArkSystemException
	 * @throws EntityCannotBeRemoved
	 */
	public void deleteResearcher(Researcher researcher) throws ArkSystemException, EntityCannotBeRemoved ;
	
	/**
	 * Search Researcher's for the given criteria
	 * @param researcher
	 * @return Selected Researcher List.
	 */
	public List<Researcher> searchResearcher(Researcher researcher);
	
	/**
	 * Create new {@link BillableItemType} record
	 * @param billableItemType
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void createBillableItemType(BillableItemType billableItemType) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * Update {@link BillableItemType} record
	 * @param billableItemType
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void updateBillableItemType(BillableItemType billableItemType) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * 
	 * @return List of {@link BillableItemTypeStatus} assigned to the BillableItemType.
	 */
	public List<BillableItemTypeStatus> getBillableItemTypeStatuses();
	
	/**
	 * Search Billable Item Types for the given criteria.
	 * @param billableItemType
	 * @return
	 */
	public List<BillableItemType> searchBillableItemType(BillableItemType billableItemType);
	
	/**
	 * @return List of {@link WorkRequestStatus} assigned to work requests.
	 */
	public List<WorkRequestStatus> getWorkRequestStatuses();
	
	/**
	 * Create new {@link WorkRequest} record
	 * @param workRequest
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void createWorkRequest(WorkRequest workRequest) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * Update {@link WorkRequest} record
	 * @param billableItemType
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void updateWorkRequest(WorkRequest workRequest) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * Delete {@link WorkRequest} record
	 * @param billableItemType
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void deleteWorkRequest(WorkRequest workRequest) throws ArkSystemException, EntityCannotBeRemoved ;
	
	/**
	 * Search Work Request's for the given criteria
	 * @param researcher
	 * @return Selected Researcher List.
	 */
	public List<WorkRequest> searchWorkRequest(WorkRequest workRequest);
	
	/**
	 * Create new {@link BillableItem} record
	 * @param workRequest
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void createBillableItem(BillableItem billableItem) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * Update {@link BillableItem} record
	 * @param billableItemType
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void updateBillableItem(BillableItem billableItem) throws ArkSystemException, EntityExistsException ;
	
	/**
	 * Delete {@link BillableItem} record
	 * @param billableItemType
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void deleteBillableItem(BillableItem billableItem) throws ArkSystemException, EntityCannotBeRemoved ;
	
	/**
	 * Search Billable Item's for the given criteria
	 * @param researcher
	 * @return Selected BillableItem List.
	 */
	public List<BillableItem> searchBillableItem(BillableItem billableItem);
	
}
