package au.org.theark.genomics.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.genomics.service.IGenomicService;

public class CompilationJob implements Job {
	
	public static final String GENOMICSSERVICE = "iGenomicsService";
	public static final String COMPUTATION = "computation";
	public static final String PROCESSUID = "processUID";

	private Computation computation;
	
	private IGenomicService iGenomicService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		iGenomicService = (IGenomicService)data.get(GENOMICSSERVICE);
		computation = (Computation) data.get(COMPUTATION);
		String processUID = data.get(PROCESSUID).toString();
		iGenomicService.updateCompilationStatus(processUID,computation);
	}

}
