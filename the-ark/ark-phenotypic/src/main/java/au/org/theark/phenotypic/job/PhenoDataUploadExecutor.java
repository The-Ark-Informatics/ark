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
package au.org.theark.phenotypic.job;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.service.IPhenotypicService;

public class PhenoDataUploadExecutor {
	private static final Logger	log	= LoggerFactory.getLogger(PhenoDataUploadExecutor.class);
	
	private IArkCommonService<Void>	iArkCommonService			= null;
	private IPhenotypicService			iPhenoService				= null;
	private Long							uploadId;
	private String							currentUser;
	private Study							study;
	private PhenoCollection				phenoCollection;
	private File							file;
	private char							delimiter		= Constants.IMPORT_DELIM_CHAR_COMMA;
	
	/**
	 * PhenoDataUploadExecutor constructor
	 * @param iArkCommonService
	 * @param iPhenoService
	 * @param uploadId
	 * @param currentUser
	 * @param study
	 * @param phenoCollection
	 * @param file
	 * @param delimiter
	 */
	public PhenoDataUploadExecutor(IArkCommonService<Void> iArkCommonService, 
											IPhenotypicService iPhenoService,
											Long uploadId,
											String currentUser, 
											Study study, 
											PhenoCollection phenoCollection,
											File file, 
											char delimiter) {
		this.iArkCommonService = iArkCommonService;
		this.iPhenoService = iPhenoService;
		this.uploadId = uploadId;
		this.currentUser = currentUser;
		this.study = study;
		this.phenoCollection = phenoCollection;
		this.file = file;
		this.delimiter = delimiter;
	}

	public void run() throws Exception {
		log.info("------- Initializing SchedulerFactory ---------");

		// First we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		log.info("------- Initialization SchedulerFactory Complete --------");

		log.info("------- Scheduling PhenoDataUploadJob Jobs ----------------");

		// get a "nice round" time a few seconds in the future....
		Date startTime = nextGivenSecondDate(null, 2);

		// job1 will only run 5 times (at start time, plus 4 repeats), every 10 seconds
		JobDetail job1 = newJob(PhenoDataUploadJob.class).withIdentity("PhenoDataUploadJob", "group1").build();

		SimpleTrigger trigger1 = newTrigger().withIdentity("PhenoDataUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();

		// pass initialization parameters into the job
		job1.getJobDataMap().put(PhenoDataUploadJob.IARKCOMMONSERVICE, iArkCommonService);
		job1.getJobDataMap().put(PhenoDataUploadJob.IPHENOSERVICE, iPhenoService);
		job1.getJobDataMap().put(PhenoDataUploadJob.UPLOADID, uploadId);
		job1.getJobDataMap().put(PhenoDataUploadJob.CURRENT_USER, currentUser);
		job1.getJobDataMap().put(PhenoDataUploadJob.STUDY, study);
		job1.getJobDataMap().put(PhenoDataUploadJob.PHENO_COLLECTION, phenoCollection);
		job1.getJobDataMap().put(PhenoDataUploadJob.DATA_FILE, file);
		job1.getJobDataMap().put(PhenoDataUploadJob.DELIMITER, delimiter);

		// schedule the job to run
		Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
		log.info(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");

		// All of the jobs have been added to the scheduler, but none of the jobs will run until the scheduler has been started
		log.info("------- Starting Scheduler ----------------");
		sched.start();
	}
}
