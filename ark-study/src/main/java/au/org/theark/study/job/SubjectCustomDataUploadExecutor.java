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

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jfree.util.Log;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import au.org.theark.core.Constants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.study.service.IStudyService;

public class SubjectCustomDataUploadExecutor {
	//private static final Logger	log	= LoggerFactory.getLogger(CustomDataUploadExecutor.class);
	
	private IArkCommonService<Void>	iArkCommonService			= null;
	private IStudyService				iStudyService				= null;
	private Long							uploadId;
	private Long							studyId;
	private String							fileFormat;
	private InputStream						inputStream;
	private char							delimiter		= Constants.IMPORT_DELIM_CHAR_COMMA;
	private long							size;
	private String							report;
	private List<String>					uidsToUpload;
	private String 							customFeildType;
	private UploadVO 						uploadVO;
	
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
	public SubjectCustomDataUploadExecutor(IArkCommonService iArkCommonService,
											IStudyService iStudyService,
											InputStream inputStream,
											Long uploadId,
											Long studyId,
											String fileFormat,
											char delimiter,
											long size, String report, List<String> uidsToUpload,String customFeildType,UploadVO uploadVO) {
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
		this.inputStream = inputStream;
		this.uploadId = uploadId;
		this.studyId = studyId;
		this.fileFormat = fileFormat;
		this.delimiter = delimiter;
		this.size = size;
		this.report = report;
		this.uidsToUpload = uidsToUpload;
		this.customFeildType=customFeildType;
		this.uploadVO=uploadVO;
	}

	public void run() throws SchedulerException{

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched;
		try {
			sched = sf.getScheduler();
			JobKey jobKey = new JobKey("SubjectCustomDataUploadJob", "group2");
			//Listener attached to jobKey
			sched.getListenerManager().addJobListener(new SubjectCustomDataUploadJobListner(uploadVO,iArkCommonService), KeyMatcher.keyEquals(jobKey));
			Log.warn("executor " + uidsToUpload.size());
			JobDetail customDataUploadJob = newJob(SubjectCustomDataUploadJob.class).withIdentity(jobKey).build();
			// pass initialization parameters into the job
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.IARKCOMMONSERVICE, iArkCommonService);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.ISTUDYSERVICE, iStudyService);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.UPLOADID, uploadId);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.STUDY_ID, studyId);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.REPORT, report);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.FILE_FORMAT, fileFormat);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.INPUT_STREAM, inputStream);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.DELIMITER, delimiter);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.SIZE, size);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.LIST_OF_UIDS_TO_UPDATE, uidsToUpload);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.CUSTOM_FIELD_TYPE, customFeildType);
			customDataUploadJob.getJobDataMap().put(SubjectCustomDataUploadJob.MODEL_OBJECT, uploadVO);
			Date startTime = nextGivenSecondDate(null, 1);
			SimpleTrigger trigger1 = newTrigger().withIdentity("SubjectCustomDataUploadJobTrigger", "group1").startAt(startTime).withSchedule(simpleSchedule()).build();
			sched.start();
			sched.scheduleJob(customDataUploadJob, trigger1);
		}catch(SchedulerException schedE){
			throw new SchedulerException(schedE.getMessage());
		}
		
	}
	
}
