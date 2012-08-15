package au.org.theark.worktracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;
import au.org.theark.worktracking.model.dao.IWorkTrackingDao;
import au.org.theark.worktracking.model.vo.BillableItemVo;
import au.org.theark.worktracking.util.Constants;

@Transactional
@Service(Constants.WORK_TRACKING_SERVICE)
public class WorkTrackingServiceImpl implements IWorkTrackingService{

	private IWorkTrackingDao workTrackingDao;

	@Autowired
	public void setWorkTrackingDao(IWorkTrackingDao workTrackingDao) {
		this.workTrackingDao = workTrackingDao;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ResearcherStatus> getResearcherStatuses() {
		// TODO Auto-generated method stub
		return workTrackingDao.getResearcherStatuses();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ResearcherRole> getResearcherRoles() {
		// TODO Auto-generated method stub
		return workTrackingDao.getResearcherRoles();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillingType> getResearcherBillingTypes() {
		// TODO Auto-generated method stub
		return workTrackingDao.getResearcherBillingTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createResearcher(Researcher researcher){
		workTrackingDao.createResearcher(researcher);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateResearcher(Researcher researcher){
		workTrackingDao.updateResearcher(researcher);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteResearcher(Researcher researcher){
		workTrackingDao.deleteResearcher(researcher);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Researcher> searchResearcher(Researcher researcher) {
		return workTrackingDao.searchResearcher(researcher);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createBillableItemType(BillableItemType billableItemType){
		 workTrackingDao.createBillableItemType(billableItemType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBillableItemType(BillableItemType billableItemType){
		workTrackingDao.updateBillableItemType(billableItemType);		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillableItemTypeStatus> getBillableItemTypeStatuses() {
		return workTrackingDao.getBillableItemTypeStatuses();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillableItemType> searchBillableItemType(
			BillableItemType billableItemType) {
		return workTrackingDao.searchBillableItemType(billableItemType);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<WorkRequestStatus> getWorkRequestStatuses() {
		return workTrackingDao.getWorkRequestStatuses();
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createWorkRequest(WorkRequest workRequest){
		workTrackingDao.createWorkRequest(workRequest);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateWorkRequest(WorkRequest workRequest){
		workTrackingDao.updateWorkRequest(workRequest);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteWorkRequest(WorkRequest workRequest){
		workTrackingDao.deleteWorkRequest(workRequest);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<WorkRequest> searchWorkRequest(WorkRequest workRequest) {
		return workTrackingDao.searchWorkRequest(workRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createBillableItem(BillableItem billableItem){
		workTrackingDao.createBillableItem(billableItem);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBillableItem(BillableItem billableItem){
		workTrackingDao.updateBillableItem(billableItem);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateAllBillableItems(List<BillableItem> billableItemList){
		workTrackingDao.updateAllBillableItems(billableItemList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteBillableItem(BillableItem billableItem){
		workTrackingDao.deleteBillableItem(billableItem);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillableItem> searchBillableItem(BillableItem billableItem) {
		return workTrackingDao.searchBillableItem(billableItem);
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public Long getBillableItemCount(BillableItemType itemType){
		return workTrackingDao.getBillableItemCount(itemType);
	}

	/**
	 * {@inheritDoc}
	 */
	public Long getWorkRequestCount(Researcher researcher) {
		return workTrackingDao.getWorkRequestCount(researcher);
	}

	/**
	 * {@inheritDoc}
	 */
	public Long getBillableItemCount(WorkRequest workRequest) {
		return workTrackingDao.getBillableItemCount(workRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillableItem> searchBillableItem(BillableItemVo billableItemVo) {
		// TODO Auto-generated method stub
		return workTrackingDao.searchBillableItem(billableItemVo);
	}
	
	

}
