package au.org.theark.worktracking.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.BillableItemStatus;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
import au.org.theark.core.model.worktracking.entity.BillableSubject;
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;
import au.org.theark.worktracking.util.Constants;

@Repository(Constants.WORK_TRACKING_DAO)
public class WorkTrackingDao extends HibernateSessionDao implements
		IWorkTrackingDao {
	
	private static Logger		log	= LoggerFactory.getLogger(WorkTrackingDao.class);
	
	/**
	 * {@inheritDoc}
	 */
	public List<ResearcherStatus> getResearcherStatuses() {
		Example researcherStatus = Example.create(new ResearcherStatus());
		Criteria criteria = getSession().createCriteria(ResearcherStatus.class)
				.add(researcherStatus);
		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ResearcherRole> getResearcherRoles() {
		Example researcherRole = Example.create(new ResearcherRole());
		Criteria criteria = getSession().createCriteria(ResearcherRole.class)
				.add(researcherRole);
		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillingType> getResearcherBillingTypes(){
		Example researcherBillingType = Example.create(new BillingType());
		Criteria criteria = getSession().createCriteria(BillingType.class)
				.add(researcherBillingType);
		return criteria.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void createResearcher(Researcher researcher){
		getSession().save(researcher);

	}

	/**
	 * {@inheritDoc}
	 */
	public void updateResearcher(Researcher researcher){
		getSession().update(researcher);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteResearcher(Researcher researcher){
		
		getSession().delete(researcher);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Researcher> searchResearcher(Researcher researcherCriteria) {
		Criteria criteria = getSession().createCriteria(Researcher.class);
		criteria.add(Restrictions.eq(Constants.STUDY_ID , researcherCriteria.getStudyId()));
		if(researcherCriteria.getId() != null ){
			criteria.add(Restrictions.eq(Constants.ID, researcherCriteria.getId()));
		}

		if(researcherCriteria.getFirstName() != null ){
			criteria.add(Restrictions.like(Constants.FIRST_NAME, researcherCriteria.getFirstName()+"%"));
		}
		
		if(researcherCriteria.getLastName() != null ){
			criteria.add(Restrictions.like(Constants.LAST_NAME, researcherCriteria.getLastName()+"%"));
		}
		
		if(researcherCriteria.getOrganization() != null ){
			criteria.add(Restrictions.like(Constants.ORGANIZATION, researcherCriteria.getOrganization()+"%"));
		}
		if(researcherCriteria.getCreatedDate() != null ){
			criteria.add(Restrictions.eq(Constants.CREATED_DATE , researcherCriteria.getCreatedDate()));
		}
		
		if(researcherCriteria.getResearcherRole() != null ){
			criteria.add(Restrictions.eq(Constants.ROLE , researcherCriteria.getResearcherRole()));
		}
		
		if(researcherCriteria.getResearcherStatus() != null ){
			criteria.add(Restrictions.eq(Constants.STATUS , researcherCriteria.getResearcherStatus()));
		}
		
		List<Researcher> list = criteria.list();
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createBillableItemType(BillableItemType billableItemType){
		getSession().save(billableItemType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBillableItemType(BillableItemType billableItemType){
		getSession().update(billableItemType);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillableItemTypeStatus> getBillableItemTypeStatuses() {
		Example billableItemTypeStatus = Example.create(new BillableItemTypeStatus());
		Criteria criteria = getSession().createCriteria(BillableItemTypeStatus.class)
				.add(billableItemTypeStatus);
		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillableItemType> searchBillableItemType(
			BillableItemType billableItemTypeCriteria) {
		Criteria criteria = getSession().createCriteria(BillableItemType.class);
		criteria.add(Restrictions.eq(Constants.STUDY_ID , billableItemTypeCriteria.getStudyId()));
		
		if(billableItemTypeCriteria.getId() != null ){
			criteria.add(Restrictions.eq(Constants.ID, billableItemTypeCriteria.getId()));
		}

		if(billableItemTypeCriteria.getItemName() != null ){
			criteria.add(Restrictions.like(Constants.BIT_ITEM_NAME, billableItemTypeCriteria.getItemName()+"%"));
		}
		
		if(billableItemTypeCriteria.getQuantityPerUnit() != null ){
			criteria.add(Restrictions.eq(Constants.BIT_QUANTITY_PER_UNIT, billableItemTypeCriteria.getQuantityPerUnit()));
		}
		
		if(billableItemTypeCriteria.getUnitPrice() != null ){
			criteria.add(Restrictions.eq(Constants.BIT_UNIT_PRICE, billableItemTypeCriteria.getUnitPrice()));
		}
		
		if(billableItemTypeCriteria.getGst() != null ){
			criteria.add(Restrictions.eq(Constants.BIT_GST , billableItemTypeCriteria.getGst()));
		}
		
		if(billableItemTypeCriteria.getBillableItemTypeStatus() != null ){
			criteria.add(Restrictions.eq(Constants.BIT_STATUS , billableItemTypeCriteria.getBillableItemTypeStatus()));
		}
		
		List<BillableItemType> list = criteria.list();
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<WorkRequestStatus> getWorkRequestStatuses() {
		Example workRequestStatus = Example.create(new WorkRequestStatus());
		Criteria criteria = getSession().createCriteria(WorkRequestStatus.class)
				.add(workRequestStatus);
		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public void createWorkRequest(WorkRequest workRequest){
		getSession().save(workRequest);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateWorkRequest(WorkRequest workRequest){
		getSession().update(workRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteWorkRequest(WorkRequest workRequest){
		getSession().delete(workRequest);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<WorkRequest> searchWorkRequest(WorkRequest workRequestCriteria) {
		Criteria criteria = getSession().createCriteria(WorkRequest.class);
		criteria.add(Restrictions.eq(Constants.STUDY_ID , workRequestCriteria.getStudyId()));
		if(workRequestCriteria.getId() != null ){
			criteria.add(Restrictions.eq(Constants.ID, workRequestCriteria.getId()));
		}	
		
		if(workRequestCriteria.getName() != null ){
			criteria.add(Restrictions.like(Constants.NAME, workRequestCriteria.getName()+"%"));
		}
		
		if(workRequestCriteria.getRequestedDate() != null ){
			criteria.add(Restrictions.eq(Constants.WR_REQUESTED_DATE, workRequestCriteria.getRequestedDate()));
		}
		
		if(workRequestCriteria.getCommencedDate() != null ){
			criteria.add(Restrictions.eq(Constants.WR_COMMENCED_DATE, workRequestCriteria.getCommencedDate()));
		}
		
		if(workRequestCriteria.getCompletedDate() != null ){
			criteria.add(Restrictions.eq(Constants.WR_COMPLETED_DATE, workRequestCriteria.getCompletedDate()));
		}
		
		if(workRequestCriteria.getRequestStatus() != null ){
			criteria.add(Restrictions.eq(Constants.WR_STATUS, workRequestCriteria.getRequestStatus()));
		}
		
		if(workRequestCriteria.getResearcher() != null ){
			criteria.add(Restrictions.eq(Constants.WR_RESEARCHER, workRequestCriteria.getResearcher()));
		}
			
		List<WorkRequest> list = criteria.list();
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createBillableItem(BillableItem billableItem){
		getSession().save(billableItem);
		if(billableItem.getBillableSubjects() !=null ){
			saveBillableSubject(billableItem);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBillableItem(BillableItem billableItem){
		getSession().update(billableItem);
		if(billableItem.getBillableSubjects() !=null ){
			saveBillableSubject(billableItem);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateAllBillableItems(List<BillableItem> billableItemList){
		for(BillableItem billableItem:billableItemList){
			getSession().update(billableItem);
		}
	}
	
	/**
	 * Save or Update the {@link BillableSubject}s attached to a {@link BillableItem}
	 * @param billableItem
	 */
	private void saveBillableSubject(BillableItem billableItem){
		for(BillableSubject billableSubject:billableItem.getBillableSubjects()){
			billableSubject.setBillableItem(billableItem);
			if(billableSubject.getId() == null){
				getSession().save(billableSubject);
			}else{
				getSession().update(billableSubject);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteBillableItem(BillableItem billableItem){
		getSession().delete(billableItem);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<BillableItem> searchBillableItem(BillableItem billableItemCriteria) {
		Criteria criteria = getSession().createCriteria(BillableItem.class);
		criteria.add(Restrictions.eq(Constants.STUDY_ID , billableItemCriteria.getStudyId()));
		if(billableItemCriteria.getId() != null ){
			criteria.add(Restrictions.eq(Constants.ID, billableItemCriteria.getId()));
		}	
		
		if(billableItemCriteria.getDescription() != null ){
			criteria.add(Restrictions.like(Constants.BI_DESCRIPTION, billableItemCriteria.getDescription()+"%"));
		}
		
		if(billableItemCriteria.getQuantity() != null ){
			criteria.add(Restrictions.eq(Constants.BI_QUANTITY, billableItemCriteria.getQuantity()));
		}
		
		if(billableItemCriteria.getWorkRequest() != null ){
			criteria.add(Restrictions.eq(Constants.BI_WORK_REQUEST, billableItemCriteria.getWorkRequest()));
		}
		
		if(billableItemCriteria.getInvoice() != null ){
			criteria.add(Restrictions.eq(Constants.BI_INVOICE, billableItemCriteria.getInvoice()));
		}
//		if(billableItemCriteria.getItemStatus() != null ){
//			criteria.add(Restrictions.eq(Constants.BI_ITEM_STATUS, billableItemCriteria.getItemStatus()));
//		}
			
		List<BillableItem> list = criteria.list();
		return list;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public Long getBillableItemCount(BillableItemType itemType){
		Long count = new Long(0);
		Criteria criteria = getSession().createCriteria(BillableItem.class);
		criteria.add(Restrictions.eq("billableItemType", itemType));
		criteria.setProjection(Projections.rowCount());
		count= (Long)criteria.uniqueResult();
		return count;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Long getWorkRequestCount(Researcher researcher){
		Long count = new Long(0);
		Criteria criteria = getSession().createCriteria(WorkRequest.class);
		criteria.add(Restrictions.eq("researcher", researcher));
		criteria.setProjection(Projections.rowCount());
		count= (Long)criteria.uniqueResult();
		return count;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Long getBillableItemCount(WorkRequest workRequest){
		Long count = new Long(0);
		Criteria criteria = getSession().createCriteria(BillableItem.class);
		criteria.add(Restrictions.eq("workRequest", workRequest));
		criteria.setProjection(Projections.rowCount());
		count= (Long)criteria.uniqueResult();
		return count;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Long getBillableSubjectCount(BillableItem billableItem){
		Long count = new Long(0);
		Criteria criteria = getSession().createCriteria(BillableSubject.class);
		criteria.add(Restrictions.eq("billableItem", billableItem));
		criteria.setProjection(Projections.rowCount());
		count= (Long)criteria.uniqueResult();
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BillableItemStatus> getBillableItemStatusses() {
		Example billableItemStatus = Example.create(new BillableItemStatus());
		Criteria criteria = getSession().createCriteria(BillableItemStatus.class)
				.add(billableItemStatus);
		return criteria.list();
	}
	
	

}
