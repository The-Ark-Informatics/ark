package au.org.theark.genomics.jobs;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.genomics.service.IGenomicService;

public class AnalysisExecutor {

	private Analysis analysis;
	
	private String processUID;
	
	private IGenomicService iGenomicService;

	public AnalysisExecutor(Analysis analysis, String processUID, IGenomicService iGenomicService) {
		this.analysis = analysis;
		this.processUID = processUID;
		this.iGenomicService = iGenomicService;
	}
	
	public void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		JobDetail analysisJob = newJob(AnalysisJob.class).withIdentity("AnalysisJob"+processUID, "group1").build();
		// pass initialization parameters into the job
		analysisJob.getJobDataMap().put(AnalysisJob.GENOMICSSERVICE, iGenomicService);
		analysisJob.getJobDataMap().put(AnalysisJob.ANALYSIS, analysis);
		analysisJob.getJobDataMap().put(AnalysisJob.PROCESSUID, processUID);
		Date startTime = nextGivenSecondDate(null, 1);
		SimpleTrigger trigger1 = newTrigger().withIdentity("AnalysisJobTrigger"+processUID, "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		sched.scheduleJob(analysisJob, trigger1);
		sched.start();
	}
	
}
