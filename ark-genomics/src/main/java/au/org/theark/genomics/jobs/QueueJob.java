package au.org.theark.genomics.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.genomics.service.IGenomicService;

public class QueueJob implements Job {
	
	public static final String GENOMICSSERVICE = "iGenomicsService";
	public static final String ANALYSIS = "analysis";

	private Analysis analysis;
	
	private IGenomicService iGenomicService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		iGenomicService = (IGenomicService)data.get(GENOMICSSERVICE);
		analysis = (Analysis) data.get(ANALYSIS);
		iGenomicService.updateQueueStatus(analysis);
	}

}
