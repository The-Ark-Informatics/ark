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

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jfree.util.Log;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.service.IPhenotypicService;

public class CustomDataUploadExecutor {
	//private static final Logger	log	= LoggerFactory.getLogger(CustomDataUploadExecutor.class);
	
	private IArkCommonService<Void>		iArkCommonService			= null;
	private IPhenotypicService			iPhenoService				= null;
	private Long						uploadId;
	private Long						studyId;
	private String						fileFormat;
	private InputStream					inputStream;
	private char						delimiter		= Constants.IMPORT_DELIM_CHAR_COMMA;
	private long						size;

	private String						report;
	private List<String>				uidsToUpload;
	private PhenoCollection				phenoCollection;
	private CustomFieldGroup			customFieldGroup;
	
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
	 * @param uidsToUpload 
	 */
	public CustomDataUploadExecutor(IArkCommonService iArkCommonService,
											IPhenotypicService iPhenoService,
											InputStream inputStream,
											Long uploadId,
											Long studyId,
											String fileFormat,
											char delimiter,
											long size, String report, List<String> uidsToUpload,
											PhenoCollection phenoCollection,
											CustomFieldGroup customFieldGroup) {
		this.iArkCommonService = iArkCommonService;
		this.iPhenoService = iPhenoService;
		this.inputStream = inputStream;
		this.uploadId = uploadId;
		this.studyId = studyId;
		this.fileFormat = fileFormat;
		this.delimiter = delimiter;
		this.size = size;
		this.report = report;
		this.uidsToUpload = uidsToUpload;
		this.phenoCollection = phenoCollection;
		this.customFieldGroup = customFieldGroup;
	}

	public void run() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Log.warn("executor " + uidsToUpload.size());
		
		JobDetail customDataUploadJob = newJob(CustomDataUploadJob.class).withIdentity("CustomDataUploadJob", "group2").build();
		// pass initialization parameters into the job
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.IARKCOMMONSERVICE, iArkCommonService);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.IPHENOSERVICE, iPhenoService);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.UPLOADID, uploadId);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.STUDY_ID, studyId);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.REPORT, report);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.FILE_FORMAT, fileFormat);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.INPUT_STREAM, inputStream);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.DELIMITER, delimiter);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.SIZE, size);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.LIST_OF_UIDS_TO_UPDATE, uidsToUpload);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.PHENO_COLLECTION, phenoCollection);
		customDataUploadJob.getJobDataMap().put(CustomDataUploadJob.CUSTOM_FIELD_GROUP, customFieldGroup);
		
		Date startTime = nextGivenSecondDate(null, 1);
		
		SimpleTrigger trigger1 = newTrigger().withIdentity("CustomDataUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
		
		sched.scheduleJob(customDataUploadJob, trigger1);
		//		log.warn(studyUploadJob.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");
		// All of the jobs have been added to the scheduler, but none of the jobs will run until the scheduler has been started
		sched.start();
	}
	
}
