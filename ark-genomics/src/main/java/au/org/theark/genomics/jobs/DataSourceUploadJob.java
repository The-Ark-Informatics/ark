package au.org.theark.genomics.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.genomics.service.IGenomicService;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DataSourceUploadJob implements Job {

	public static final String GENOMICSSERVICE = "iGenomicsService";
	public static final String DATASOURCE = "dataSource";
	public static final String PROCESSUID = "processUID";

	private DataSource dataSource;
	
	private IGenomicService iGenomicService;

	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		iGenomicService = (IGenomicService)data.get(GENOMICSSERVICE);
		dataSource = (DataSource) data.get(DATASOURCE);
		String processUID = data.get(PROCESSUID).toString();
		iGenomicService.updateDataSourceStatus(processUID, dataSource);
	}

}