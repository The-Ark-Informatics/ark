package au.org.theark.worktracking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.worktracking.model.dao.IWorkTrackingDao;

@Transactional
@Service(Constants.WORK_TRACKING_SERVICE)
public class WorkTrackingServiceImpl {

	private static Logger		log	= LoggerFactory.getLogger(WorkTrackingServiceImpl.class);
	
	private IWorkTrackingDao workTrackingDao;

	@Autowired
	public void setWorkTrackingDao(IWorkTrackingDao workTrackingDao) {
		this.workTrackingDao = workTrackingDao;
	}
	
	
}
