/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.job;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
//import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.util.SubjectUploadReport;

public class StudyDataUploadExecutor {
	private static final Logger	log	= LoggerFactory.getLogger(StudyDataUploadExecutor.class);
	
	private IArkCommonService<Void>	iArkCommonService			= null;
	private IStudyService				iStudyService				= null;
	private Long							uploadId;
//	private String							currentUser;
	private Long							studyId;
//	private PhenoCollection				phenoCollection;
//	private File							file;
	private String							fileFormat;
	private InputStream					inputStream;
	private char							delimiter		= Constants.IMPORT_DELIM_CHAR_COMMA;
	private long							size;

	private String							report;
	
	/**
	 * StudyDataUploadExecutor constructor
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
	public StudyDataUploadExecutor(IArkCommonService iArkCommonService,
											IStudyService iStudyService,
											InputStream inputStream,
											Long uploadId,
											Long studyId, 
//											PhenoCollection phenoCollection,
//											File file, 
											String fileFormat,
											char delimiter,
											long size, String report) {
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
		this.inputStream = inputStream;
		this.uploadId = uploadId;
		this.studyId = studyId;
//		this.phenoCollection = phenoCollection;
//		this.file = file;
		this.fileFormat = fileFormat;
		this.delimiter = delimiter;
		this.size = size;
		this.report = report;
	}

	public void run() throws Exception {
		log.info("------- Initializing SchedulerFactory ---------");

		// First we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		log.info("------- Initialization SchedulerFactory Complete --------");

		log.info("------- Scheduling StudyDataUploadJob Jobs ----------------");

		// get a "nice round" time a few seconds in the future....
		Date startTime = nextGivenSecondDate(null, 2);

		// job1 will only run 5 times (at start time, plus 4 repeats), every 10 seconds
		JobDetail job1 = newJob(StudyDataUploadJob.class).withIdentity("StudyDataUploadJob", "group1").build();

		SimpleTrigger trigger1 = newTrigger().withIdentity("StudyDataUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();

		// pass initialization parameters into the job
		job1.getJobDataMap().put(StudyDataUploadJob.IARKCOMMONSERVICE, iArkCommonService);
		job1.getJobDataMap().put(StudyDataUploadJob.ISTUDYSERVICE, iStudyService);
		job1.getJobDataMap().put(StudyDataUploadJob.UPLOADID, uploadId);
		job1.getJobDataMap().put(StudyDataUploadJob.STUDY_ID, studyId);
		job1.getJobDataMap().put(StudyDataUploadJob.REPORT, report);
		job1.getJobDataMap().put(StudyDataUploadJob.FILE_FORMAT, fileFormat);
		job1.getJobDataMap().put(StudyDataUploadJob.INPUT_STREAM, inputStream);
		job1.getJobDataMap().put(StudyDataUploadJob.DELIMITER, delimiter);
		job1.getJobDataMap().put(StudyDataUploadJob.SIZE, size);

		// schedule the job to run
		Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
		log.warn(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");

		// All of the jobs have been added to the scheduler, but none of the jobs will run until the scheduler has been started
		log.warn("------- Starting Scheduler ----------------");
		sched.start();
	}
	
}
