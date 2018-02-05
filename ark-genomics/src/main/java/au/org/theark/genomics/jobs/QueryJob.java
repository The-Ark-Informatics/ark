package au.org.theark.genomics.jobs;

import java.util.ArrayList;
import java.util.List;

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
public class QueryJob implements Job {
	
	
	public static final String GENOMICSSERVICE = "iGenomicsService";
	public static final String DATASOURCE = "dataSource";
	public static final String PROCESSUID = "processUID";
	public static final String DATASOURCE_INIT_STATUS="dataSourceInitStatus";

	private DataSource dataSource;
	
	private IGenomicService iGenomicService;
	
	private String status;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException  {
		// TODO Auto-generated method stub
		JobDataMap data = context.getJobDetail().getJobDataMap();
		iGenomicService = (IGenomicService)data.get(GENOMICSSERVICE);
		dataSource = (DataSource) data.get(DATASOURCE);
		List<DataSource> dataSourceList = new ArrayList<DataSource>(0);
		String processUID = data.get(PROCESSUID).toString();
		status= data.getString(DATASOURCE_INIT_STATUS);
		iGenomicService.updateDataSourceStatus(processUID, dataSource, dataSourceList,status);
		
	}
	
	public static void main(String[] args) {
		List<DataSource> dataSourceList =  new ArrayList<DataSource>(0);;
		
		for(DataSource ds : dataSourceList){
			
		}
	}

}
