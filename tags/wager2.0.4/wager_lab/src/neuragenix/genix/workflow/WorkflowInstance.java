/**
 * WorkflowInstance.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 14/03/2004
 *
 * Last Modified: (Date\Author\Comments)
 *
 * 
 */

package neuragenix.genix.workflow;

import java.util.Vector;

/**
 * WorkflowInstance - Store info for a workflow instance 
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class WorkflowInstance implements java.io.Serializable
{
	// Name
	private String strName = null;

	// Status
	private String strStatus = null;

	// Initial Task Key
	private String strInitialTaskKey = null;

	// List of task
	private Vector vecTask = null;

	// List of task transition
	private Vector vecTaskTransition = null;

	/**
	 * Default constructor
	 */
	public WorkflowInstance()
	{
	}

	/**
	 * Full constructor
	 */
	public WorkflowInstance(String strName,
							String strStatus,
							String strInitialTaskKey,
							Vector vecTask,
							Vector vecTaskTransition)
	{
		this.strName = strName;
		this.strStatus = strStatus;
		this.strInitialTaskKey = strInitialTaskKey;
		this.vecTask = vecTask;
		this.vecTaskTransition = vecTaskTransition;
	}

	/**
	 * get the task name
	 * 
	 * @return the task name
	 */
	public String getName()
	{
		return this.strName;
	}

	/**
	 * set the task name 
	 * 
	 * @param strName	the task name
	 */
	public void setName(String strName)
	{
		this.strName = strName;
	}

	/**
	 * get the task status 
	 * 
	 * @return the task status 
	 */
	public String getStatus()
	{
		return this.strStatus;
	}

	/**
	 * set the task status  
	 * 
	 * @param strStatus the task status
	 */
	public void setStatus(String strStatus)
	{
		this.strStatus = strStatus;
	}

	/**
	 * get the initial task key
	 * 
	 * @return the initial task key 
	 */
	public String getInitialTaskKey()
	{
		return this.strInitialTaskKey;
	}

	/**
	 * set the initial task key WorkflowInstance
	 * 
	 * @param strInitialTaskKey the initial task key
	 */
	public void setInitialTaskKey(String strInitialTaskKey)
	{
		this.strInitialTaskKey = strInitialTaskKey;
	}

	/**
	 * get the list of task under the workflow instance
	 *
	 * @return list of task under the workflow instance
	 */
	public Vector getTasks()
	{
		return this.vecTask;
	}

	/**
	 * set the list of task under the workflow instance
	 *
	 * @param vecTask the list of task under the workflow instance
	 */
	public void setTasks(Vector vecTask)
	{
		this.vecTask = vecTask;
	}
	
	/**
	 * get the list of task transition under the workflow instance
	 *
	 * @return list of task transition under the workflow instance
	 */
	public Vector getTaskTransitions()
	{
		return this.vecTaskTransition;
	}

	/**
	 * set the list of task transition under the workflow instance
	 *
	 * @param vecTaskTransition the list of task transition under the workflow instance
	 */
	public void setTaskTransitions(Vector vecTaskTransition)
	{
		this.vecTaskTransition = vecTaskTransition;
	}

	/**
	 * get the workflow instance name
	 * 
	 * @return the  workflow instance name
	 */
	public String toString()
	{
		return this.strName;
	}
}
