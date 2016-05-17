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
import au.org.theark.core.audit.AuditThreadLocalHelper;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author tendersby
 */
//@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PhenoDataUploadJob implements Job {
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
	public static final String		PHENO_FIELD_GROUP = "phenoFieldGroup";
	public static final String		OVERWRITE_EXISTING = "overwriteExisting";
	public static final String		USERNAME 			= "username";
	
	private 	IPhenotypicService	iPhenoService;
	private 	IArkCommonService<Void>	iArkCommonService;

	/**
	 * Empty constructor for job initialization- Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 */
	public PhenoDataUploadJob() {
	}

	/**
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * @throws JobExecutionException  if there is an exception while executing the job.
	 */
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {		
		JobDataMap data = context.getJobDetail().getJobDataMap();
		Long uploadId=null; 			
		
		try {
			iArkCommonService			= (IArkCommonService<Void>) data.get(IARKCOMMONSERVICE);
			iPhenoService				= (IPhenotypicService) data.get(IPHENOSERVICE);
			uploadId	 				= (Long) data.get(UPLOADID);
			char delimiter 				= (Character) data.get(DELIMITER);
			String fileFormat 			= (String) data.get(FILE_FORMAT);
			InputStream inputStream 	= (InputStream) data.get(INPUT_STREAM);
			long size 					= data.getLongValue(SIZE);
			String originalReport 		= data.getString(REPORT);
			Long studyId 				= data.getLongValue(STUDY_ID);
			List<String> uidsToUpdate=(List<String>)data.get(LIST_OF_UIDS_TO_UPDATE);
			PhenoDataSetGroup phenoDataSetGroup = (PhenoDataSetGroup) data.get(PHENO_FIELD_GROUP);
			PhenoDataSetCollection phenoCollection = (PhenoDataSetCollection) data.get(PHENO_COLLECTION);
			boolean overwriteExisting = data.getBoolean(OVERWRITE_EXISTING);
			String username 			= data.getString(USERNAME);
			AuditThreadLocalHelper.USERNAME.set(username);
			Date startTime = new Date(System.currentTimeMillis());
			StringBuffer uploadReport = iPhenoService.uploadAndReportCustomDataFile(inputStream, size, fileFormat, delimiter, studyId, uidsToUpdate, phenoDataSetGroup, phenoCollection, overwriteExisting);
			Upload upload = iPhenoService.getUpload(uploadId);
			save(upload, uploadReport.toString(), originalReport, startTime);
		}
		/*catch (FileFormatException e) {	}catch (ArkSystemException e) {	}*/
		catch(Exception e){
			// TODO Auto-generated catch block ...fix this throughout the application
			Upload upload = iPhenoService.getUpload(uploadId);
			upload.setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.phenotypic.web.Constants.UPLOAD_STATUS_OF_ERROR_ON_DATA_IMPORT));
			upload.setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.phenotypic.web.Constants.UPLOAD_STATUS_OF_COMPLETED));
			JobExecutionException e2 =new JobExecutionException(e);
	        	// this job will refire immediately
	        	throw e2;
		}
	}

	private void save(Upload upload, String report, String originalReport, Date startTime) {
		iPhenoService.refreshUpload(upload);
		byte[] bytes = (originalReport + report).getBytes();
		upload.setUploadReport(bytes);
		upload.setStartTime(startTime);
		upload.setFinishTime(new Date(System.currentTimeMillis()));
		upload.setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_FIELD_DATA_UPLOAD));
		upload.setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.phenotypic.web.Constants.UPLOAD_STATUS_OF_COMPLETED));
		iArkCommonService.updateUpload(upload);
	}
}
