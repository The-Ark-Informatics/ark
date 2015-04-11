package au.org.theark.genomics.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.model.vo.DataSourceVo;
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
	
	
	public void saveOrUpdate(DataSource dataSource) {
		if (dataSource.getId() == null || dataSource.getId() == 0) {
			getSession().save(dataSource);
		} else {
			getSession().update(dataSource);
		}
	}

	public void delete(MicroService microService) {
		getSession().delete(microService);
	}
	
	public void delete(DataSource dataSource) {
		getSession().delete(dataSource);
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
	
	public List<DataSourceType> listDataSourceTypes(){
		Criteria criteria = getSession().createCriteria(DataSourceType.class);
		return criteria.list();
	}
	
	public DataSource getDataSource(DataSourceVo dataSourceVo){
		List<DataSource> list=null;
		
		Criteria criteria = getSession().createCriteria(DataSource.class);
		
		criteria.add(Restrictions.eq(Constants.NAME, dataSourceVo.getFileName()));
		criteria.add(Restrictions.eq(Constants.PATH, dataSourceVo.getPath()));
		criteria.add(Restrictions.eq(Constants.DATACENTER, dataSourceVo.getDataCenter()));
		criteria.add(Restrictions.eq(Constants.MICROSERVICE, dataSourceVo.getMicroService()));
		
		list= criteria.list();
		
		return list.size()>0?list.get(0):null;
	}

}
