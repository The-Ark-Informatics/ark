package au.org.theark.genomics.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.model.spark.entity.Computation;
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

	public long saveOrUpdate(Computation computatin) {
		Long id = null;
		if (computatin.getId() == null || computatin.getId() == 0) {
			getSession().save(computatin);
		} else {
			getSession().update(computatin);
		}
		id = computatin.getId();
		return id;
	}

	@Override
	public long saveOrUpdate(Analysis analysis) {
		Long id = null;
		if (analysis.getId() == null || analysis.getId() == 0) {
			getSession().save(analysis);
		} else {
			getSession().update(analysis);
		}
		id=analysis.getId();
		return id;
	}

	public void delete(MicroService microService) {
		getSession().delete(microService);
	}

	public void delete(DataSource dataSource) {
		getSession().delete(dataSource);
	}

	public void delete(Computation computation) {
		getSession().delete(computation);
	}
	
	public void delete(Analysis analysis) {
		getSession().delete(analysis);
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
		
		if (microService.getServiceUrl() != null) {
			criteria.add(Restrictions.ilike(Constants.SERVICE_URL, microService.getServiceUrl(), MatchMode.ANYWHERE));
		}
		
		criteria.addOrder(Order.asc(Constants.ID));
		
		List<MicroService> list = criteria.list();
		return list;
	}

	public List<DataSourceType> listDataSourceTypes() {
		Criteria criteria = getSession().createCriteria(DataSourceType.class);
		return criteria.list();
	}

	public List<Computation> searchComputations(Computation computation, Long studyId) {
		Criteria criteria = getSession().createCriteria(Computation.class, "c");
		criteria.createAlias("microService", "ms", JoinType.INNER_JOIN);
		criteria.setFetchMode("microService", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ms.studyId", studyId));

		if (computation.getId() != null) {
			criteria.add(Restrictions.eq("c.id", computation.getId()));
		}

		if (computation.getName() != null) {
			criteria.add(Restrictions.like("c.name", computation.getName(), MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc("c.id"));

		List<Computation> list = criteria.list();
		return list;
	}

	public List<DataSource> getDataSources(DataSourceVo dataSourceVo) {
		List<DataSource> list = null;

		Criteria criteria = getSession().createCriteria(DataSource.class);
		criteria.setFetchMode("microService", FetchMode.JOIN);
		if (dataSourceVo.getFileName() != null) {
			criteria.add(Restrictions.eq(Constants.NAME, dataSourceVo.getFileName()));
		}
		if (dataSourceVo.getPath() != null) {
			criteria.add(Restrictions.eq(Constants.PATH, dataSourceVo.getPath()));
		}
		if (dataSourceVo.getDataCenter() != null) {
			criteria.add(Restrictions.eq(Constants.DATACENTER, dataSourceVo.getDataCenter()));
		}
		if (dataSourceVo.getMicroService() != null) {
			criteria.add(Restrictions.eq(Constants.MICROSERVICE, dataSourceVo.getMicroService()));
		}
		list = criteria.list();
		
		return list;
	}

	public List<DataSource> searchDataSources(MicroService microService) {
		Criteria criteria = getSession().createCriteria(DataSource.class);
		criteria.add(Restrictions.eq(Constants.MICROSERVICE, microService));
		List<DataSource> list = criteria.list();
		criteria.addOrder(Order.asc("name"));
		return list;
	}

	public List<Computation> searchComputation(MicroService microService) {
		Criteria criteria = getSession().createCriteria(Computation.class);
		criteria.add(Restrictions.eq(Constants.MICROSERVICE, microService));
		criteria.add(Restrictions.eq(Constants.AVAILABLE, Boolean.TRUE));
		criteria.addOrder(Order.asc("name"));
		List<Computation> list = criteria.list();
		return list;
	}
	
	public List<Analysis> searchAnalysis(Analysis analysis, Long studyId){
		Criteria criteria = getSession().createCriteria(Analysis.class,"a");
		criteria.createAlias("microService", "ms", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ms.studyId", studyId));
		if (analysis.getId() != null) {
			criteria.add(Restrictions.eq("a.id", analysis.getId()));
		}

		if (analysis.getName() != null) {
			criteria.add(Restrictions.like("a.name", analysis.getName(), MatchMode.ANYWHERE));
		}
		criteria.setFetchMode("microService", FetchMode.JOIN);
		criteria.setFetchMode("dataSource", FetchMode.JOIN);
		criteria.setFetchMode("computation", FetchMode.JOIN);
		criteria.addOrder(Order.asc("a.id"));
		List<Analysis> list= criteria.list();
		return list;
	}
	
	public int getAnalysisCount(long computationId){
		int count = 0;
		Criteria criteria = getSession().createCriteria(Analysis.class, "a");
		criteria.createAlias("computation", "c", JoinType.INNER_JOIN);
		criteria.setFetchMode("computation", FetchMode.JOIN);
		criteria.add(Restrictions.eq("c.id", computationId));
		List<Analysis> list = criteria.list();
		count =list.size();
		return count;
	}
	
	public int getDataSourceCount(long dataSourceId){
		int count = 0;
		Criteria criteria = getSession().createCriteria(Analysis.class, "a");
		criteria.createAlias("dataSource", "d", JoinType.INNER_JOIN);
		criteria.setFetchMode("dataSource", FetchMode.JOIN);
		criteria.add(Restrictions.eq("d.id", dataSourceId));
		List<Analysis> list = criteria.list();
		count =list.size();
		return count;
	}

	public void refreshDataSource(DataSource dataSource){
		getSession().refresh(dataSource);
	}
}
