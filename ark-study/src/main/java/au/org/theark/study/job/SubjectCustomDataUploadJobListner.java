package au.org.theark.study.job;

import java.util.ArrayList;
import java.util.Collection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;

public class SubjectCustomDataUploadJobListner implements JobListener {
	
	private static Logger		log	= LoggerFactory.getLogger(SubjectCustomDataUploadJobListner.class);
	public static final String LISTENER_NAME = "SubjectCustomDataUploadListner";
	private UploadVO uploadVO;
	private Collection<String> validationMessage=new ArrayList<String>();
	private IArkCommonService iArkCommonService;

	
	public SubjectCustomDataUploadJobListner(UploadVO uploadVO,IArkCommonService iArkCommonService){
		this.uploadVO=uploadVO;
		this.iArkCommonService=iArkCommonService;
	}
	@Override
	public String getName() {
		return LISTENER_NAME;
	}
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().toString();
		log.info("jobToBeExecuted");
		log.info("Job : " + jobName + " is going to start...");
	}
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		log.info("jobExecutionVetoed");
	}
	@Override
	public void jobWasExecuted(JobExecutionContext context,JobExecutionException jobException) {
		log.info("jobWasExecuted");
		String jobName = context.getJobDetail().getKey().toString();
		log.info("Job : " + jobName + " is finished...");
		if (jobException!=null && (!jobException.getMessage().equals("")|| !jobException.getMessage().isEmpty())) {
			log.info("Exception thrown by: " + jobName+ " Exception: " + jobException.getMessage());
			validationMessage.add("Exception thrown by: " + jobName+ " Exception: " + jobException.getMessage());
			uploadVO.setValidationMessages(validationMessage);
			uploadVO.getUpload().setFilename(uploadVO.getFileUpload().getClientFileName());//have to reset this because the container has the file name...luckily it never changes 
			uploadVO.getUpload().setUploadStatus(iArkCommonService.getUploadStatusFor(au.org.theark.study.web.Constants.UPLOAD_STATUS_OF_ERROR_ON_DATA_IMPORT));
			iArkCommonService.updateUpload(uploadVO.getUpload());
		}
	}

	

}
