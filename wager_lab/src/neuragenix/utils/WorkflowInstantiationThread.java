/**
 * WorkflowInstantiationThread.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 03/06/2004
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
 * WorkflowInstantiationThread - to instantiate workflow on a separate thread
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class WorkflowInstantiationThread extends Thread
{
	//the activity
	private String strActivity = null;

	//the runtime data
	private ChannelRuntimeData runtimeData = null;

	//the workflow template key
	private String strWorkflowTemplateKey = null;

	//the domain
	private String strDomain = null;

	//the domain key
	private String strDomainKey = null;

	//the trigger validation
	private boolean blValidateTrigger = true;

	//the DALSecurityQuery
	private DALSecurityQuery query = null;
	
	/**
	 * Default constructor
	 */
	public WorkflowInstantiationThread(String strActivity, ChannelRuntimeData runtimeData, 
									   String strWorkflowTemplateKey, String strDomain, 
									   String strDomainKey, boolean blValidateTrigger, DALSecurityQuery query)
	{
		this.strActivity = strActivity;
		this.runtimeData = runtimeData;
		this.strWorkflowTemplateKey = strWorkflowTemplateKey;
		this.strDomain = strDomain;
		this.strDomainKey = strDomainKey;
		this.blValidateTrigger = blValidateTrigger;
		this.query = query;
	}

	/**
	 * instantiate the workflow
	 *
	 * @param strActivity				the activity
	 * @param runtimeData				the runtimeData
	 * @param strWorkflowTemplateKey 	the workflow template key
	 * @param strDomain					the domain name
	 * @param strDomainKey				the domain key
	 * @param blValidateTrigger			the trigger validation
	 * @param query						the query object
	 */
	public void run ()
	{
		try
		{
			WorkflowEngine.instantiateWorkflowTemplates(strActivity, runtimeData, strWorkflowTemplateKey,
														strDomain, strDomainKey, blValidateTrigger, query);
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowInstantiationThread::run - " + e.toString(), e);
        }
	}
}
