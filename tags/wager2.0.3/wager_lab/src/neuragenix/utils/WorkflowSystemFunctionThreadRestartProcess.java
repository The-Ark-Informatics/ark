/*   
 * Copyright (c) 2005 Neuragenix, All Rights Reserved.
 * WorkflowSystemFunctionThreadRestartProcess.java
 * Created on 30 May 2005 , 15:28  
 * @author  Agustian Agustian
 * 
 * Description: 
 * Restart all system functions of type thread read from ix_workflow_function_thread
 */



package neuragenix.utils;


//portal libraries
import org.jasig.portal.services.LogService;


//neuragenix libraries
import neuragenix.dao.*;
import neuragenix.genix.workflow.*;


//java libraries
import java.util.Enumeration;
import java.util.Vector;



/**
 * WorkflowSystemFunctionThreadRestartProcess
 */

public class WorkflowSystemFunctionThreadRestartProcess extends Thread
{

	public WorkflowSystemFunctionThreadRestartProcess()
	{
	}
            
	public void run()
	{
		try
		{
			DALSecurityQuery query = new DALSecurityQuery();

			//get all the task keys that need to be restarted during tomcat re-start
			Vector vecTaskKeys = WorkflowManager.getRestartRequiredTaskThread();

			Enumeration enumTaskKeys = vecTaskKeys.elements();
			while(enumTaskKeys.hasMoreElements())
			{
				String strTaskKey = (String) enumTaskKeys.nextElement();
//System.err.println("restarting task key >>>" + strTaskKey + "<<<");
				String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strTaskKey, query);

				//delete the thread first then kick it again
				WorkflowManager.deleteWorkflowFunctionThread(strTaskKey, strWorkflowInstanceKey);
				WorkflowEngine.kickStartTaskThread(strTaskKey, query);
			}
		}
		catch(Exception e)
		{
			LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowSystemFunctionThreadRestartProcess::run - " + e.toString(), e);
		}
	}
}
