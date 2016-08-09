package au.org.theark.core.util;

import java.util.ArrayList;
import java.util.Collection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;


public class QuartzCommomJobListner implements JobListener {

	private static Logger		log	= LoggerFactory.getLogger(QuartzCommomJobListner.class);
	public static final String LISTENER_NAME = "QuartzCommomSearchJobListner";
	private Collection<String> validationMessage=new ArrayList<String>();
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
			
		}
	}

}
