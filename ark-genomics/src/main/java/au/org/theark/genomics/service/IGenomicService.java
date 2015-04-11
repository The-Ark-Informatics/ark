package au.org.theark.genomics.service;

import java.util.List;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;

public interface IGenomicService {

	public void saveOrUpdate(MicroService microService);
	
	public void saveOrUpdate(DataSource dataSource);

	public void delete(MicroService microService);
	
	public void delete(DataSource dataSource);

	public List<MicroService> searchMicroService(final MicroService microService);
	
	public List<String> searchDataCenters(MicroService microService);

	public List<DataSourceVo> searchDataSources(DataCenterVo datacenter);
	
	public List<DataSourceType> listDataSourceTypes();
	
	public DataSource getDataSource(DataSourceVo dataSourceVo);
	
}
