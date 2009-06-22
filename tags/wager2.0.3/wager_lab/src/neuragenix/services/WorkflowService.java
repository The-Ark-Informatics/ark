/*   
 * Copyright (c) 2003 Neuragenix, All Rights Reserved.
 * WorkflowService.java
 * Created on 30 June 2004 , 15:28  
 * @author  Agustian Agustian
 * 
 * Description: 
 * This class binds with the application server and initialise all workflow initialisation procedure 
 */



package neuragenix.services;


//portal libraries
import org.jasig.portal.services.LogService;


//neuragenix libraries
import neuragenix.dao.*;
import neuragenix.genix.workflow.*;
import neuragenix.utils.*;


//java libraries
import java.util.Enumeration;
import java.util.Vector;



/**
 * WorkflowService
 */

public final class WorkflowService
{

	public WorkflowService()
	{
		initialize();
	}
            
	private final void initialize()
	{
		try
		{
			WorkflowSystemFunctionThreadRestartProcess workflowSystemFunctionThreadRestartProcess = 
															new WorkflowSystemFunctionThreadRestartProcess();

			workflowSystemFunctionThreadRestartProcess.start();

			/*
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
			*/
		}
		catch(Exception e)
		{
			LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowService::initialize - " + e.toString(), e);
		}
	}
}
