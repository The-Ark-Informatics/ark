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
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.service.*;

/**
 * @author tendersby
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CustomDataUploadJob implements Job {
	//private static final Logger	log					= LoggerFactory.getLogger(CustomDataUploadJob.class);

	public static final String		IARKCOMMONSERVICE	= "iArkCommonService";
	public static final String		IPHENOSERVICE		= "iPhenoService";
	public static final String		UPLOADID			= "uploadId";
	public static final String		CURRENT_USER		= "currentUser";
	public static final String		STUDY_ID			= "study";
	public static final String		PHENO_COLLECTION	= "phenoCollection";
	public static final String		DATA_FILE			= "dataFile";
	public static final String		FILE_FORMAT			= "fileFormat";
	public static final String		DELIMITER			= "delimiter";
	public static final String		INPUT_STREAM		= "inputStream";
	public static final String		SIZE				= "size";
	public static final String		REPORT				= "report";
	public static final String		LIST_OF_UIDS_TO_UPDATE	= "listOfUidsToUpdate";
	public static final String		CUSTOM_FIELD_GROUP = "customFieldGroupDdc";
	
	private 	IPhenotypicService	iPhenoService;
	private 	IArkCommonService<Void>	iArkCommonService;

	/**
	 * Empty constructor for job initialization- Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 */
	public CustomDataUploadJob() {
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
		iPhenoService				= (IPhenotypicService) data.get(IPHENOSERVICE);
		Long uploadId 				= (Long) data.get(UPLOADID);
		char delimiter 			= (Character) data.get(DELIMITER);
		String fileFormat 		= (String) data.get(FILE_FORMAT);
		InputStream inputStream = (InputStream) data.get(INPUT_STREAM);
		long size 					= data.getLongValue(SIZE);
		String originalReport 	= data.getString(REPORT);
		Long studyId 				= data.getLongValue(STUDY_ID);
		List<String> uidsToUpdate=(List<String>)data.get(LIST_OF_UIDS_TO_UPDATE);
		CustomFieldGroup customFieldGroup = (CustomFieldGroup) data.get(CUSTOM_FIELD_GROUP);
		PhenoCollection phenoCollection = (PhenoCollection) data.get(PHENO_COLLECTION);
		
		try {
			Date startTime = new Date(System.currentTimeMillis());
			StringBuffer uploadReport = iPhenoService.uploadAndReportCustomDataFile(inputStream, size, fileFormat, delimiter, studyId, uidsToUpdate, customFieldGroup, phenoCollection);
			Upload upload = iPhenoService.getUpload(uploadId);
			save(upload, uploadReport.toString(), originalReport, startTime);
		}
		/*catch (FileFormatException e) {	}catch (ArkSystemException e) {	}*/
		catch(Exception e){
			// TODO Auto-generated catch block ...fix this throughout the application
			e.printStackTrace();
		}
	}

	private void save(Upload upload, String report, String originalReport, Date startTime) {
		iPhenoService.refreshUpload(upload);
		byte[] bytes = (originalReport + report).getBytes();
		upload.setUploadReport(bytes);
		upload.setStartTime(startTime);
		upload.setFinishTime(new Date(System.currentTimeMillis()));
		upload.setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD));
		iArkCommonService.updateUpload(upload);
	}
}
