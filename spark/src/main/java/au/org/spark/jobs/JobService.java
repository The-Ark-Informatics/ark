package au.org.spark.jobs;

import java.util.concurrent.Future;

import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;
import au.org.spark.web.view.Report;

public interface JobService {
	
	public Future<Report> generateReport();
	
	public Future<Report> uploadDataSource(DataSourceVo dataSource);
	
	public Future<Report> deleteDataSource(DataSourceVo dataSource);
	
	public Future<Report> uploadPlinkDataSource(DataCenterVo dataCenter) throws Exception;
	
	public Future<Report> deletePlinkDataSource(DataCenterVo dataCenter);

}
