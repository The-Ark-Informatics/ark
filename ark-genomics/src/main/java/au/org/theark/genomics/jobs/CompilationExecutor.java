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

import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.genomics.service.IGenomicService;

public class CompilationExecutor {
	private Computation computation;
	
	private String processUID;
	
	private IGenomicService iGenomicService;

	public CompilationExecutor(Computation computation, String processUID, IGenomicService iGenomicService) {
		this.computation = computation;
		this.processUID = processUID;
		this.iGenomicService = iGenomicService;
	}
	
	public void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		JobDetail compilationJob = newJob(CompilationJob.class).withIdentity("CompilationJob"+processUID, "group1").build();
		// pass initialization parameters into the job
		compilationJob.getJobDataMap().put(CompilationJob.GENOMICSSERVICE, iGenomicService);
		compilationJob.getJobDataMap().put(CompilationJob.COMPUTATION, computation);
		compilationJob.getJobDataMap().put(CompilationJob.PROCESSUID, processUID);
		Date startTime = nextGivenSecondDate(null, 1);
		SimpleTrigger trigger1 = newTrigger().withIdentity("CompilationJobTrigger"+processUID, "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		sched.scheduleJob(compilationJob, trigger1);
		sched.start();
	}
}
