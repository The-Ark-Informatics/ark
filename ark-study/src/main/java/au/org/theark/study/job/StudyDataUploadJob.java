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

import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.util.SubjectUploadReport;


/**
 * 
 * @author cellis
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StudyDataUploadJob implements Job {
	private static final Logger	log					= LoggerFactory.getLogger(StudyDataUploadJob.class);

/* not in spring context	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService		iStudyService;*/
	
	// parameter names specific to this job
	public static final String		IARKCOMMONSERVICE	= "iArkCommonService";
	public static final String		ISTUDYSERVICE		= "iStudyService";
	public static final String		UPLOADID				= "uploadId";
	public static final String		CURRENT_USER		= "currentUser";
	public static final String		STUDY					= "study";
	public static final String		PHENO_COLLECTION	= "phenoCollection";
	public static final String		DATA_FILE			= "dataFile";
	public static final String		FILE_FORMAT			= "fileFormat";
	public static final String		DELIMITER			= "delimiter";
	public static final String		INPUT_STREAM		= "inputStream";
	public static final String		SIZE					= "size";

	public static final String		REPORT				= "report";
	private IStudyService	iStudyService;
	private IArkCommonService<Void>	iArkCommonService;
	/**
	 * <p>
	 * Empty constructor for job initialization
	 * </p>
	 * <p>
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 * </p>
	 */
	public StudyDataUploadJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *            if there is an exception while executing the job.
	 */
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap data = context.getJobDetail().getJobDataMap();

		iArkCommonService		= (IArkCommonService<Void>) data.get(IARKCOMMONSERVICE);
		iStudyService			= (IStudyService) data.get(ISTUDYSERVICE);
		Long uploadId 			= (Long) data.get(UPLOADID);
		char delimiter 		= (Character) data.get(DELIMITER);
		String fileFormat 		= (String) data.get(FILE_FORMAT);
		InputStream inputStream = (InputStream) data.get(INPUT_STREAM);
		long size 				= data.getLongValue(SIZE);
		SubjectUploadReport originalReport  = (SubjectUploadReport) data.get(REPORT);
		
		//String currentUser = (String) data.get(CURRENT_USER); //PhenoCollection phenoCollection = (PhenoCollection) data.get(PHENO_COLLECTION);
		//Study study = (Study) data.get(STUDY); //TODO get back to file and make sure saved beforehand   //File file = (File) data.get(DATA_FILE);

		log.warn("UploadJob delimiter: [" + delimiter + "]");
		log.warn("UploadJob format: [" + fileFormat + "]");
		log.warn("UploadJob uopload ID: [" + uploadId + "]");
		log.warn("UploadJob is iArkCommonService null? [" + (iArkCommonService == null) + "]");
		log.warn("UploadJob is iStudyService null? [" + (iStudyService == null) + "]");
		log.warn("UploadJob is input stream null? [" + (inputStream == null) + "]");
		log.warn("UploadJob size from cricket: [" + size + "]");

		//SubjectUploader uploader = new SubjectUploader(study, iArkCommonService, iStudyService);
		
		try {
			//TRAV CODE TO START USING 
				//iStudyService.countNumberOfUniqueSubjects(study, uploader.getListOfUidsFromInputStream(fileIS, file.length(), fileFormat, delimiter));
			//InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			StringBuffer uploadReport = iStudyService.uploadAndReportMatrixSubjectFile(inputStream, size, fileFormat, delimiter);
			StudyUpload upload = iStudyService.getUpload(uploadId);
			//updateUploadReport(originalReport, uploadReport.toString(), upload);
			save(upload);
		}
		/*catch (FileFormatException e) {	}catch (ArkSystemException e) {	}*/
		catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<CorrespondenceStatusType> statusTypesAsATestOfService =  (iStudyService.getCorrespondenceStatusTypes());
		for(CorrespondenceStatusType status : statusTypesAsATestOfService){

			log.warn("atatus types as a test: " + status.getName());
		}
		/*try {
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser);
			log.info("PhenoUploadJob currentUser (iArkCommonService test): " + arkUser.getLdapUserName());
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());			
		}*/
		
		// perform the data upload and report
	//	StringBuffer uploadReport = uploadAndReportPhenotypicDataFile(iArkCommonService, iPhenoService, study, phenoCollection, file, delimiter);		
		//updateDatabase(iPhenoService, uploadId, uploadReport);
	}

	public SubjectUploadReport updateUploadReport(SubjectUploadReport originalreport, String importReport, StudyUpload upload) {
		SubjectUploadReport subjectUploadReport = originalreport; //new SubjectUploadReport();
		//subjectUploadReport.appendDetails(upload, upload.getStudy().getName());
		//subjectUploadReport.append(importReport);
		byte[] bytes = subjectUploadReport.getReport().toString().getBytes();
		Blob uploadReportBlob = Hibernate.createBlob(bytes);
		upload.setUploadReport(uploadReportBlob);
		return subjectUploadReport;
	}

	private void save(StudyUpload upload) {
		iStudyService.refreshUpload(upload);
		upload.setFinishTime(new Date(System.currentTimeMillis()));
		upload.setArkFunction(iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_UPLOAD));
		iArkCommonService.updateUpload(upload);
	}
	/**
	 * Upload and report on the data upload process
	 * @param iArkCommonService
	 * @param iPhenoService
	 * @param study
	 * @param phenoCollection
	 * @param file
	 * @param delimiterChar
	 * @return
	 *
	public StringBuffer uploadAndReportPhenotypicDataFile(IArkCommonService<Void> iArkCommonService, IPhenotypicService iPhenoService, Study study, PhenoCollection phenoCollection, File file, char delimiterChar) {
		log.info("running uploadAndReportPhenotypicDataFile");
		StringBuffer importReport = null;
		String fileFormat = "CSV";
		PhenoDataUploader pi = new PhenoDataUploader(iPhenoService, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);

		try {
			InputStream inputStream = new FileInputStream(file);
			importReport = pi.uploadAndReportMatrixFieldDataFile(inputStream, file.length());
		}
		catch (IOException ioe) {
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse) {
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return importReport;
	}*/
	
	/**
	 * Update the database row with the upload job results
	 * @param iPhenoService
	 * @param id
	 * @param uploadReport
	 *
	private void updateDatabase(IPhenotypicService iPhenoService, Long id, StringBuffer uploadReport) {
		PhenoUpload upload = iPhenoService.getUpload(id);
		
		// Set Upload report
		PhenoUploadReport phenoUploadReport = new PhenoUploadReport();
		phenoUploadReport.append(uploadReport.toString());
		byte[] bytes = phenoUploadReport.getReport().toString().getBytes();
		Blob uploadReportBlob = Hibernate.createBlob(bytes);
		upload.setUploadReport(uploadReportBlob);
		iPhenoService.updateUpload(upload);
	}
*/
	
}
