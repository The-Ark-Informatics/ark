/**
 * WorkflowTaskCompletionThread.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 05/10/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 
 */

package neuragenix.utils;

// java packages

// uPortal packages
import org.jasig.portal.services.LogService;
import org.jasig.portal.ChannelRuntimeData;

// neuragenix packages
import neuragenix.dao.*;
import neuragenix.genix.workflow.*;

/**
 * WorkflowTaskCompletionThread - completing a task in a workflow in a separate thread 
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class WorkflowTaskCompletionThread extends Thread
{
	//the finished workflow task key
	private String strFinishedWorkflowTaskKey = null;

	//the finished workflow task status
	private String strFinishedWorkflowTaskStatus = null;

	//indicate whether the next task should be executed or not
	private Boolean blContinue = null;

	//the runtime data
	private ChannelRuntimeData runtimeData = null;

	//the DALSecurityQuery object
	private DALSecurityQuery query = null;
	
	/**
	 * Default constructor
	 */
	public WorkflowTaskCompletionThread(String strFinishedWorkflowTaskKey,
										String strFinishedWorkflowTaskStatus,
								  		Boolean blContinue, 
										ChannelRuntimeData runtimeData, 
										DALSecurityQuery query)
	{
		this.strFinishedWorkflowTaskKey = strFinishedWorkflowTaskKey;
		this.strFinishedWorkflowTaskStatus = strFinishedWorkflowTaskStatus;
		this.blContinue = blContinue;
		this.runtimeData = runtimeData;
		this.query = query;
	}

	/**
	 * to indicate that a task has been completed and to move to the next task
	 *
	 * @param strFinishedWorkflowTaskKey 	the current completed workflow task key
	 * @param strFinishedWorkflowTaskStatus	the current completed workflow task status
	 * @param blContinue					indicate whether the next task should be executed or not
	 * @param runtimeData					the runtime data
	 * @param query							the query object
	 */
	public void run ()
	{
		try
		{
			WorkflowEngine.finishTask(strFinishedWorkflowTaskKey, strFinishedWorkflowTaskStatus,
					                  blContinue, runtimeData, query);
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowTaskCompletionThread::run - " + e.toString(), e);
        }
	}
}
