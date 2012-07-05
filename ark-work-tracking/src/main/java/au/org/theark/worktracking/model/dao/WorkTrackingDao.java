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
import au.org.theark.core.model.worktracking.entity.BillingType;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
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
	public void create(Researcher researcher) throws ArkSystemException, EntityExistsException {
		getSession().save(researcher);

	}

	/**
	 * {@inheritDoc}
	 */
	public void update(Researcher researcher) throws ArkSystemException, EntityExistsException {
		getSession().update(researcher);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Researcher researcher) throws ArkSystemException, EntityCannotBeRemoved {
		
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

}
