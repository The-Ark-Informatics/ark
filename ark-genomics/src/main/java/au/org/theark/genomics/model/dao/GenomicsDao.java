package au.org.theark.genomics.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.util.Constants;

@Repository(Constants.GENOMICS_DAO)
public class GenomicsDao extends HibernateSessionDao implements IGenomicsDao {

	private static Logger log = LoggerFactory.getLogger(GenomicsDao.class);

	public void saveOrUpdate(MicroService microService) {
		if (microService.getId() == null || microService.getId() == 0) {
			getSession().save(microService);
		} else {
			getSession().update(microService);
		}
	}

	public void delete(MicroService microService) {
		getSession().delete(microService);
	}

	public List<MicroService> searchMicroService(MicroService microService) {
		Criteria criteria = getSession().createCriteria(MicroService.class);
		criteria.add(Restrictions.eq(Constants.STUDY_ID, microService.getStudyId()));
		if (microService.getId() != null) {
			criteria.add(Restrictions.eq(Constants.ID, microService.getId()));
		}

		if (microService.getName() != null) {
			criteria.add(Restrictions.ilike(Constants.NAME, microService.getName(), MatchMode.ANYWHERE));
		}
		List<MicroService> list = criteria.list();
		return list;
	}

}
