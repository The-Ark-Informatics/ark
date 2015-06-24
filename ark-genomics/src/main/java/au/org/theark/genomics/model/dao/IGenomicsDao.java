package au.org.theark.genomics.model.dao;

import java.util.List;

import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.model.vo.DataSourceVo;

public interface IGenomicsDao {
	public void saveOrUpdate(MicroService microService);
	
	public void saveOrUpdate(DataSource dataSource);
	
	public long saveOrUpdate(Computation computatin);
	
	public void delete(MicroService microService);
	
	public void delete(DataSource dataSource);

	public void delete(Computation dataSource);
	
	public List<MicroService> searchMicroService(final MicroService microService);
	
	public List<Computation> searchComputations(Computation computation, Long studyId);
	
	public List<DataSourceType> listDataSourceTypes();
	
	public DataSource getDataSource(DataSourceVo dataSourceVo);	
}
