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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

import org.hibernate.Hibernate;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.exception.PhenotypicSystemException;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoDataUploader;
import au.org.theark.phenotypic.util.PhenoUploadReport;

/**
 * 
 * @author cellis
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PhenoDataUploadJob implements Job {
	private static final Logger	log					= LoggerFactory.getLogger(PhenoDataUploadJob.class);

	// parameter names specific to this job
	public static final String		IARKCOMMONSERVICE	= "iArkCommonService";
	public static final String		IPHENOSERVICE		= "iPhenoService";
	public static final String		UPLOADID				= "uploadId";
	public static final String		CURRENT_USER		= "currentUser";
	public static final String		STUDY					= "study";
	public static final String		PHENO_COLLECTION	= "phenoCollection";
	public static final String		DATA_FILE			= "dataFile";
	public static final String		DELIMITER			= "delimiter";
	
	/**
	 * <p>
	 * Empty constructor for job initialization
	 * </p>
	 * <p>
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 * </p>
	 */
	public PhenoDataUploadJob() {
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

		// This job simply prints out its job name and the
		// date and time that it is running
		JobKey jobKey = context.getJobDetail().getKey();

		// Grab and print passed parameters
		JobDataMap data = context.getJobDetail().getJobDataMap();
		
		IArkCommonService<Void>		iArkCommonService			= (IArkCommonService<Void>) data.get(IARKCOMMONSERVICE);
		IPhenotypicService			iPhenoService				= (IPhenotypicService) data.get(IPHENOSERVICE);
		Long uploadId = (Long) data.get(UPLOADID);
		String currentUser = (String) data.get(CURRENT_USER);
		PhenoCollection phenoCollection = (PhenoCollection) data.get(PHENO_COLLECTION);
		Study study = (Study) data.get(STUDY);
		File file = (File) data.get(DATA_FILE);
		char delimiter = (Character) data.get(DELIMITER);

		log.info("PhenoUploadJob: " + jobKey + " executing at " + new Date());
		log.info("PhenoUploadJob upliadId to be used in upload: " + file.getAbsolutePath());
		log.info("PhenoUploadJob currentUser to be used: " + uploadId);
		log.info("PhenoUploadJob currentUser to be used: " + currentUser);
		log.info("PhenoUploadJob phenoCollection to be used in upload: " + phenoCollection.getName());
		log.info("PhenoUploadJob study to be used in upload: " + study.getName());
		int phenoTest = iPhenoService.getCountOfCollectionsInStudy(study);
		log.info("PhenoUploadJob collections in study (iPhenoServiceTest): " + phenoTest);
		try {
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser);
			log.info("PhenoUploadJob currentUser (iArkCommonService test): " + arkUser.getLdapUserName());
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());			
		}
		log.info("PhenoUploadJob delimiter: [" + delimiter + "]");
		
		// perform the data upload and report
		StringBuffer uploadReport = uploadAndReportPhenotypicDataFile(iArkCommonService, iPhenoService, study, phenoCollection, file, delimiter);
		
		updateDatabase(iPhenoService, uploadId, uploadReport);
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
	 */
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
	}
	
	/**
	 * Update the database row with the upload job results
	 * @param iPhenoService
	 * @param id
	 * @param uploadReport
	 */
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

}
