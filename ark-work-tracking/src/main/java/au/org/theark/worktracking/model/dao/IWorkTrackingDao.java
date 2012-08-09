package au.org.theark.worktracking.model.dao;

import java.util.List;

import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemStatus;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;
import au.org.theark.worktracking.model.vo.BillableItemVo;

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
	 */
	public void createResearcher(Researcher researcher);
	
	/**
	 * Update selected {@link Researcher} record
	 * @param researcher
	 */
	public void updateResearcher(Researcher researcher);

	/**
	 * Delete selected {@link Researcher} record
	 * @param researcher
	 */
	public void deleteResearcher(Researcher researcher);
	
	/**
	 * Search Researcher's for the given criteria
	 * @param researcher
	 * @return Selected Researcher List.
	 */
	public List<Researcher> searchResearcher(Researcher researcher);
	
	/**
	 * Create new {@link BillableItemType} record
	 * @param billableItemType
	 */
	public void createBillableItemType(BillableItemType billableItemType);
	
	/**
	 * Update {@link BillableItemType} record
	 * @param billableItemType
	 */
	public void updateBillableItemType(BillableItemType billableItemType);
	
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
	 */
	public void createWorkRequest(WorkRequest workRequest);
	
	/**
	 * Update {@link WorkRequest} record
	 * @param billableItemType
	 */
	public void updateWorkRequest(WorkRequest workRequest);
	
	/**
	 * Delete {@link WorkRequest} record
	 * @param billableItemType
	 */
	public void deleteWorkRequest(WorkRequest workRequest);
	
	/**
	 * Search Work Request's for the given criteria
	 * @param researcher
	 * @return Selected Researcher List.
	 */
	public List<WorkRequest> searchWorkRequest(WorkRequest workRequest);
	
	/**
	 * Create new {@link BillableItem} record
	 * @param workRequest
	 */
	public void createBillableItem(BillableItem billableItem);
	
	/**
	 * Update {@link BillableItem} record
	 * @param billableItemType
	 */
	public void updateBillableItem(BillableItem billableItem);
	
	/**
	 * Update All {@link BillableItem} s  
	 * @param billableItemList
	 */
	public void updateAllBillableItems(List<BillableItem> billableItemList);
	
	/**
	 * Delete {@link BillableItem} record
	 * @param billableItemType
	 */
	public void deleteBillableItem(BillableItem billableItem);
	
	/**
	 * Search Billable Item's for the given criteria
	 * @param billableItem
	 * @return Selected BillableItem List.
	 */
	public List<BillableItem> searchBillableItem(BillableItem billableItem);
	
	/**
	 * Search Billable Item's for the given Value object criteria
	 * @param billableItemVo
	 * @return Selected BillableItem List.
	 */
	public List<BillableItem> searchBillableItem(BillableItemVo billableItemVo);
	
	/**
	 * Get the billable item count associate with the given Billable item type 
	 * @param itemType
	 * @return billable item count
	 */
	public Long getBillableItemCount(BillableItemType itemType);
	
	/**
	 * Get the work request count associate with the given Researcher
	 * @param researcher
	 * @return work request count
	 */
	public Long getWorkRequestCount(Researcher researcher);
	
	/**
	 * Get the work billable item count associate with the given work request
	 * @param workRequest
	 * @return billable item count
	 */
	public Long getBillableItemCount(WorkRequest workRequest);
	
	/**
	 * Get the billable subject count associate with the given billable item.
	 * @param billableItem
	 * @return billable subject count
	 */
	public Long getBillableSubjectCount(BillableItem billableItem);
	
	/**
	 * 
	 * @return Billable Item Status List
	 */
	public List<BillableItemStatus> getBillableItemStatusses();
}
