package au.org.theark.study.job;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.InputStream;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.service.IStudyService;

public class PedigreeDataUploadExecutor {
	private IArkCommonService<Void>	iArkCommonService			= null;
	private IStudyService				iStudyService				= null;
	private Long							uploadId;
	private Long							studyId;
	private String							fileFormat;
	private InputStream					inputStream;
	private char							delimiter		= Constants.IMPORT_DELIM_CHAR_TAB;
	private long							size;

	private String							report;
	
	/**
	 * PedigreeDataUploadExecutor constructor
	 * @param iArkCommonService
	 * @param iPhenoService
	 * @param uploadId
	 * @param currentUser
	 * @param study
	 * @param file
	 * @param delimiter
	 * @param report 
	 */
	public PedigreeDataUploadExecutor(IArkCommonService iArkCommonService,
											IStudyService iStudyService,
											InputStream inputStream,
											Long uploadId,
											Long studyId,
											String fileFormat,
											char delimiter,
											long size, String report) {
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
		this.inputStream = inputStream;
		this.uploadId = uploadId;
		this.studyId = studyId;
		this.fileFormat = fileFormat;
		this.delimiter = delimiter;
		this.size = size;
		this.report = report;
		
	}

	public void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		JobDetail pedigreeloadJob = newJob(PedigreeDataUploadJob.class).withIdentity("PedigreeDataUploadJob", "group1").build();
		// pass initialization parameters into the job
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.IARKCOMMONSERVICE, iArkCommonService);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.ISTUDYSERVICE, iStudyService);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.UPLOADID, uploadId);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.STUDY_ID, studyId);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.REPORT, report);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.FILE_FORMAT, fileFormat);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.INPUT_STREAM, inputStream);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.DELIMITER, delimiter);
		pedigreeloadJob.getJobDataMap().put(PedigreeDataUploadJob.SIZE, size);
		
		Date startTime = nextGivenSecondDate(null, 1);
		SimpleTrigger trigger1 = newTrigger().withIdentity("StudyDataUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		sched.scheduleJob(pedigreeloadJob, trigger1);
		//		log.warn(studyUploadJob.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");
		// All of the jobs have been added to the scheduler, but none of the jobs will run until the scheduler has been started
		sched.start();
	}
}
