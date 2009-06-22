/**
 * WorkflowProcess.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 03/02/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 13/02/2004		Agustian Agustian		Commenting
 *
 * 
 */

package neuragenix.genix.workflow;

import java.util.Hashtable;

/**
 * Trigger - Store info of a workflow process
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class WorkflowProcess implements java.io.Serializable 
{
	// Id
	private String strId = null;

	// Name
	private String strName = null;

	// Initial Activity Id
	private String strInitialActivityId = null;

	// Process Header
	private ProcessHeader processHeader = null;

	// Parameters
	private Hashtable hashParameters = null;

	// Triggers
	private Hashtable hashTriggers = null;

	// Activities
	private Hashtable hashActivities = null;

	// Transitions
	private Hashtable hashTransitions = null;

	/**
	 * Default constructor
	 */
	public WorkflowProcess()
	{
	}

	/**
	 * Full constructor
	 */
	public WorkflowProcess(String strId,
				   		   String strName,
						   String strInitialActivityId,
				   		   ProcessHeader processHeader,
				   		   Hashtable hashParameters,
				   		   Hashtable hashTriggers,
				   		   Hashtable hashActivities,
				   		   Hashtable hashTransitions)
	{
		this.strId = strId;
		this.strName = strName;
		this.strInitialActivityId = strInitialActivityId;
		this.processHeader = processHeader;
		this.hashParameters = hashParameters;
		this.hashTriggers = hashTriggers;
		this.hashActivities = hashActivities;
		this.hashTransitions = hashTransitions;
	}

	/**
	 * get the workflow process id
	 * 
	 * @return the workflow process id 
	 */
	public String getId()
	{
		return this.strId;
	}

	/**
	 * set the workflow process id
	 * 
	 * @param strId the workflow process id 
	 */
	public void setId(String strId)
	{
		this.strId = strId;
	}

	/**
	 * get the workflow process name
	 * 
	 * @return the workflow process name 
	 */
	public String getName()
	{
		return this.strName;
	}

	/**
	 * set the workflow process name
	 * 
	 * @param strName the workflow process name 
	 */
	public void setName(String strName)
	{
		this.strName = strName;
	}

	/**
	 * get the initial activity id
	 * 
	 * @return the initial activity id 
	 */
	public String getInitialActivityId()
	{
		return this.strInitialActivityId;
	}

	/**
	 * set the initial activity id
	 * 
	 * @param strInitialActivityId the initial activity id 
	 */
	public void setInitialActivityId(String strInitialActivityId)
	{
		this.strInitialActivityId = strInitialActivityId;
	}

	/**
	 * get the process header
	 * 
	 * @return the process header
	 */
	public ProcessHeader getProcessHeader()
	{
		return this.processHeader;
	}

	/**
	 * set the process header
	 * 
	 * @param processHeader the process header
	 */
	public void setProcessHeader(ProcessHeader processHeader)
	{
		this.processHeader = processHeader;
	}

	/**
	 * get the parameters 
	 * 
	 * @return the parameters 
	 */
	public Hashtable getParameters()
	{
		return this.hashParameters;
	}

	/**
	 * set the parameters 
	 * 
	 * @param hashParameters the parameters
	 */
	public void setParameters(Hashtable hashParameters)
	{
		this.hashParameters = hashParameters;
	}

	/**
	 * get the triggers
	 * 
	 * @return the triggers
	 */
	public Hashtable getTriggers()
	{
		return this.hashTriggers;
	}

	/**
	 * set the triggers
	 * 
	 * @param hashTriggers the triggers
	 */
	public void setTriggers(Hashtable hashTriggers)
	{
		this.hashTriggers = hashTriggers;
	}

	/**
	 * get the activities
	 * 
	 * @return the activities
	 */
	public Hashtable getActivities()
	{
		return this.hashActivities;
	}

	/**
	 * set the activities
	 * 
	 * @param hashActivities the activities
	 */
	public void setActivities(Hashtable hashActivities)
	{
		this.hashActivities = hashActivities;
	}

	/**
	 * get the transitions
	 * 
	 * @return the transitions
	 */
	public Hashtable getTransitions()
	{
		return this.hashTransitions;
	}

	/**
	 * set the transitions
	 * 
	 * @param hashTransitions the transitions
	 */
	public void setTransitions(Hashtable hashTransitions)
	{
		this.hashTransitions = hashTransitions;
	}

	/**
	 * get the workflow process name
	 * 
	 * @return the  workflow process name
	 */
	public String toString()
	{
		return this.strName;
	}
}
