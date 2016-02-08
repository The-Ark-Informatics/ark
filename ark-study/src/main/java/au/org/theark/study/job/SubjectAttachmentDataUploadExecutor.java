package au.org.theark.study.job;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import org.apache.shiro.SecurityUtils;
import au.org.theark.core.Constants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.service.IStudyService;

public class SubjectAttachmentDataUploadExecutor {
	private IArkCommonService<Void>	iArkCommonService			= null;
	private IStudyService				iStudyService				= null;
	private Long							uploadId;
	private Long							studyId;
	private String							fileFormat;
	private InputStream					inputStream;
	private char							delimiter		= Constants.IMPORT_DELIM_CHAR_COMMA;
	private long							size;

	private String							report;
	
	public SubjectAttachmentDataUploadExecutor(IArkCommonService<Void> iArkCommonService, IStudyService iStudyService, Long uploadId, Long studyId, String fileFormat, InputStream inputStream,
			char delimiter, long size, String report) {
		super();
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
		this.uploadId = uploadId;
		this.studyId = studyId;
		this.fileFormat = fileFormat;
		this.inputStream = inputStream;
		this.delimiter = delimiter;
		this.size = size;
		this.report = report;
	}
	
	public void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		JobDetail subjectAttachmentUploadJob = newJob(SubjectAttachmentDataUploadJob.class).withIdentity("SubjectAttachmentDataUploadJob", "group1").build();
		// pass initialization parameters into the job
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.IARKCOMMONSERVICE, iArkCommonService);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.ISTUDYSERVICE, iStudyService);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.UPLOADID, uploadId);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.STUDY_ID, studyId);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.REPORT, report);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.FILE_FORMAT, fileFormat);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.INPUT_STREAM, inputStream);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.DELIMITER, delimiter);
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.SIZE, size);
		String userId = SecurityUtils.getSubject().getPrincipal().toString();
		subjectAttachmentUploadJob.getJobDataMap().put(StudyDataUploadJob.CURRENT_USER, userId);
		Date startTime = nextGivenSecondDate(null, 1);
		SimpleTrigger trigger1 = newTrigger().withIdentity("subjectAttachmentDataUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		sched.scheduleJob(subjectAttachmentUploadJob, trigger1);
		//		log.warn(studyUploadJob.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");
		// All of the jobs have been added to the scheduler, but none of the jobs will run until the scheduler has been started
		sched.start();
	}

}
