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

public class QueueExecutor {

	private Analysis analysis;
	
	private IGenomicService iGenomicService;

	public QueueExecutor(Analysis analysis, IGenomicService iGenomicService) {
		this.analysis = analysis;
		this.iGenomicService = iGenomicService;
	}
	
	public void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		JobDetail queueJob = newJob(QueueJob.class).withIdentity("QueueJob"+analysis.getJobId(), "group1").build();
		// pass initialization parameters into the job
		queueJob.getJobDataMap().put(AnalysisJob.GENOMICSSERVICE, iGenomicService);
		queueJob.getJobDataMap().put(AnalysisJob.ANALYSIS, analysis);
		Date startTime = nextGivenSecondDate(null, 1);
		SimpleTrigger trigger1 = newTrigger().withIdentity("AnalysisQueueTrigger"+analysis.getJobId(), "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		sched.scheduleJob(queueJob, trigger1);
		sched.start();
	}

}
