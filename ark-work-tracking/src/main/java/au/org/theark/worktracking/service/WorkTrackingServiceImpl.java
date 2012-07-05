package au.org.theark.worktracking.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.worktracking.model.dao.IWorkTrackingDao;
import au.org.theark.worktracking.util.Constants;

@Transactional
@Service(Constants.WORK_TRACKING_SERVICE)
public class WorkTrackingServiceImpl implements IWorkTrackingService{

	private static Logger		log	= LoggerFactory.getLogger(WorkTrackingServiceImpl.class);
	
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
	public void create(Researcher researcher) throws ArkSystemException,
			EntityExistsException {
		workTrackingDao.create(researcher);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Researcher researcher) throws ArkSystemException,
			EntityExistsException {
		workTrackingDao.update(researcher);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Researcher researcher) throws ArkSystemException,
			EntityCannotBeRemoved {
		workTrackingDao.delete(researcher);
		
	}

	public List<Researcher> searchResearcher(Researcher researcher) {
		// TODO Auto-generated method stub
		return workTrackingDao.searchResearcher(researcher);
	}	
	
}
