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
package au.org.theark.report.job;

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

import au.org.theark.core.Constants;
import au.org.theark.core.service.IArkCommonService;

public class DataExtractionUploadExecutor {
	
	private IArkCommonService<Void>	iArkCommonService			= null;
	private Long							searchId;
	private Long							studyId;
	/**
	 * DataExtractionUploadExecutor constructor
	 * @param iArkCommonService
	 * @param searchId
	 * @param studyId 
	 */
	public DataExtractionUploadExecutor(IArkCommonService iArkCommonService,
											Long searchId,
											Long studyId
											) {
		this.iArkCommonService = iArkCommonService;
		this.studyId = studyId;
		this.searchId = searchId;
	}

	public void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		JobDetail studyUploadJob = newJob(DataExtractionUploadJob.class).withIdentity("DataExtractionUploadJob", "group1").build();
		// pass initialization parameters into the job
		studyUploadJob.getJobDataMap().put(DataExtractionUploadJob.IARKCOMMONSERVICE, iArkCommonService);
		studyUploadJob.getJobDataMap().put(DataExtractionUploadJob.SEARCH_ID, searchId);
		studyUploadJob.getJobDataMap().put(DataExtractionUploadJob.STUDY_ID, studyId);
		Date startTime = nextGivenSecondDate(null, 1);
		SimpleTrigger trigger1 = newTrigger().withIdentity("DataExtractionUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		sched.scheduleJob(studyUploadJob, trigger1);
		//		log.warn(studyUploadJob.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");
		// All of the jobs have been added to the scheduler, but none of the jobs will run until the scheduler has been started
		sched.start();
	}
	
}
