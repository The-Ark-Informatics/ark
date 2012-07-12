package au.org.theark.worktracking.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.BillableItemTypeStatus;
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
	public List<BillingType> getResearcherBillingTypes() {
		Example researcherBillingType = Example.create(new BillingType());
		Criteria criteria = getSession().createCriteria(BillingType.class)
				.add(researcherBillingType);
		return criteria.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void createResearcher(Researcher researcher) throws ArkSystemException, EntityExistsException {
		getSession().save(researcher);

	}

	/**
	 * {@inheritDoc}
	 */
	public void updateResearcher(Researcher researcher) throws ArkSystemException, EntityExistsException {
		getSession().update(researcher);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteResearcher(Researcher researcher) throws ArkSystemException, EntityCannotBeRemoved {
		
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
		
		if(researcherCriteria.getInstitute() != null ){
			criteria.add(Restrictions.like(Constants.INSTITUTE, researcherCriteria.getInstitute()+"%"));
		}
		if(researcherCriteria.getLastActiveDate() != null ){
			criteria.add(Restrictions.eq(Constants.LAST_ACTIVE_DATE , researcherCriteria.getLastActiveDate()));
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
	public void createBillableItemType(BillableItemType billableItemType)
			throws ArkSystemException, EntityExistsException {
		getSession().save(billableItemType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBillableItemType(BillableItemType billableItemType)
			throws ArkSystemException, EntityExistsException {
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
			criteria.add(Restrictions.eq(Constants.BIT_ID, billableItemTypeCriteria.getId()));
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
	public void createWorkRequest(WorkRequest workRequest)
			throws ArkSystemException, EntityExistsException {
		getSession().save(workRequest);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateWorkRequest(WorkRequest workRequest)
			throws ArkSystemException, EntityExistsException {
		getSession().update(workRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteWorkRequest(WorkRequest workRequest)
			throws ArkSystemException, EntityCannotBeRemoved {
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
			
		List<WorkRequest> list = criteria.list();
		return list;
	}	
	
}
