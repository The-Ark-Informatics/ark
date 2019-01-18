package au.org.spark.jobs;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import au.org.spark.service.CassandraService;
import au.org.spark.service.SshService;
import au.org.spark.web.view.AnalysisVo;
import au.org.spark.web.view.ComputationVo;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;
import au.org.spark.web.view.QueryVo;
import au.org.spark.web.view.Report;

@Service("reportsService")
public class JobServiceImpl implements JobService {

	@Autowired
	private CassandraService cassandraService;

	@Autowired
	private SshService sshService;

	@Async
	public Future<Report> generateReport() {
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);
	}

	@Async
	public Future<Report> uploadDataSource(DataSourceVo dataSource) {
		try {
			cassandraService.createDataSourceMapTable(dataSource);
			sshService.processMapFile(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);
	}

	@Async
	public Future<Report> uploadPlinkDataSource(DataCenterVo dataCenter) throws Exception {
		//TODO need to remove these lines after the new implementation 
//		cassandraService.createPlinkDataCenterTables(dataCenter);
//		sshService.processPlinkDataSource(dataCenter);

		sshService.onlinePlinkDataSource(dataCenter);
		
		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);
	}

	@Async
	public Future<Report> deletePlinkDataSource(DataCenterVo dataCenter) {

		cassandraService.dropPlinkDataCenterTables(dataCenter);

		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);
	}

	@Async
	public Future<Report> deleteDataSource(DataSourceVo dataSource) {
		try {
			cassandraService.dropDataSourceTable(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);
	}
	
	@Async
	public Future<Report> compileProgram(ComputationVo computation) {
		try {
			System.out.println(" ----------- Execute the computation --------------------");
			sshService.compileProgram(computation);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);
	}

	
	@Async
	public Future<Report> executeAnalysis(AnalysisVo analysis) {
		try {
			System.out.println(" ----------- Execute the Analysis --------------------");
			sshService.executeAnalysis(analysis);		
		} catch (Exception e) {
			e.printStackTrace();
		}

		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);
	}
	
	@Async
	public Future<Report> executeQuery(QueryVo queryVo){

		try{
			sshService.executeQuery(queryVo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Report report = new Report();
		report.setName("New Report");
		report.setDescription("New Report Description");

		return new AsyncResult<Report>(report);

	}
	
}
