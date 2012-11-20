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

public class SubjectConsentDataUploadExecutor {
	private IArkCommonService<Void>	iArkCommonService			= null;
	private IStudyService				iStudyService				= null;
	private Long							uploadId;
	private Long							studyId;
	private String							fileFormat;
	private InputStream					inputStream;
	private char							delimiter		= Constants.IMPORT_DELIM_CHAR_COMMA;
	private long							size;

	private String							report;
	
	/**
	 * SubjectConsentDataUploadExecutor constructor
	 * @param iArkCommonService
	 * @param iPhenoService
	 * @param uploadId
	 * @param currentUser
	 * @param study
	 * @param phenoCollection
	 * @param file
	 * @param delimiter
	 * @param report 
	 */
	public SubjectConsentDataUploadExecutor(IArkCommonService iArkCommonService,
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
		JobDetail studyUploadJob = newJob(SubjectConsentDataUploadJob.class).withIdentity("SubjectConsentDataUploadJob", "group1").build();
		// pass initialization parameters into the job
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.IARKCOMMONSERVICE, iArkCommonService);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.ISTUDYSERVICE, iStudyService);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.UPLOADID, uploadId);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.STUDY_ID, studyId);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.REPORT, report);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.FILE_FORMAT, fileFormat);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.INPUT_STREAM, inputStream);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.DELIMITER, delimiter);
		studyUploadJob.getJobDataMap().put(SubjectConsentDataUploadJob.SIZE, size);
		
		Date startTime = nextGivenSecondDate(null, 1);
		SimpleTrigger trigger1 = newTrigger().withIdentity("StudyDataUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		sched.scheduleJob(studyUploadJob, trigger1);
		//		log.warn(studyUploadJob.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");
		// All of the jobs have been added to the scheduler, but none of the jobs will run until the scheduler has been started
		sched.start();
	}
}
