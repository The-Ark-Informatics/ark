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

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;

/**
 * 
 * @author tendersby
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DataExtractionUploadJob implements Job {
	//private static final Logger	log					= LoggerFactory.getLogger(DataExtractionUploadJob.class);

	public static final String		IARKCOMMONSERVICE	= "iArkCommonService";
	public static final String		SEARCH_ID			= "uploadId";
	public static final String		CURRENT_USER		= "currentUser";
	public static final String		STUDY_ID			= "studyId";
	
	private IArkCommonService<Void>	iArkCommonService;

	/**
	 * Empty constructor for job initialization
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 */
	public DataExtractionUploadJob() {
	}

	/**
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * @throws JobExecutionException  if there is an exception while executing the job.
	 */
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {		
		JobDataMap data = context.getJobDetail().getJobDataMap();
		
		iArkCommonService			= (IArkCommonService<Void>) data.get(IARKCOMMONSERVICE);
		Long searchId 				= (Long) data.get(SEARCH_ID);
		Long studyId 				= data.getLongValue(STUDY_ID);
		
		iArkCommonService.runSearch(searchId);

	}

}
