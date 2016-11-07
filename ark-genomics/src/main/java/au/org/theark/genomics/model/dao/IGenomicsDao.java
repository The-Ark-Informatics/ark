package au.org.theark.genomics.model.dao;

import java.util.List;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.model.vo.DataSourceVo;

public interface IGenomicsDao {
	public void saveOrUpdate(MicroService microService);
	
	public void saveOrUpdate(DataSource dataSource);
	
	public long saveOrUpdate(Computation computatin);

	public long saveOrUpdate(Analysis analysis);
	
	public void delete(MicroService microService);
	
	public void delete(DataSource dataSource);

	public void delete(Computation dataSource);
	
	public void delete(Analysis analysis);
	
	public List<MicroService> searchMicroService(final MicroService microService);
	
	public List<Computation> searchComputations(Computation computation, Long studyId);
	
	public List<DataSourceType> listDataSourceTypes();
	
	public DataSource getDataSource(DataSourceVo dataSourceVo);	
	
	public List<DataSource> searchDataSources(MicroService microService);
	
	public List<Computation> searchComputation(MicroService microService);
	
	public List<Analysis> searchAnalysis(Analysis analysis, Long studyId);
	
	public int getAnalysisCount(long computationId);
}
