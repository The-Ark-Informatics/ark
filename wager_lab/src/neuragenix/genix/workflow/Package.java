/**
 * Package.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 03/02/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 12/02/2004		Agustian Agustian		Commenting
 *
 * 
 */

package neuragenix.genix.workflow;

import java.util.Hashtable;

/**
 * Package - Store info of a package
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class Package implements java.io.Serializable
{
	// Id
	private String strId = null;
	
	// Name
	private String strName = null;

	// PackageHeader
	private PackageHeader packageHeader = null;

	// Participants
	private Hashtable hashParticipants = null;

	// WorkflowProcesses
	private Hashtable hashWorkflowProcesses = null;

	/**
	 * Default constructor
	 */
	public Package()
	{
	}

	/**
	 * Full constructor
	 */
	public Package(String strId,
				   String strName,
				   PackageHeader packageHeader,
				   Hashtable hashParticipants,
				   Hashtable hashWorkfowProcesses)
	{
		this.strId = strId;
		this.strName = strName;
		this.packageHeader = packageHeader;
		this.hashParticipants = hashParticipants;
		this.hashWorkflowProcesses = hashWorkflowProcesses;
	}

	/**
	 * get the package id
	 * 
	 * @return the package id 
	 */
	public String getId()
	{
		return this.strId;
	}

	/**
	 * set the package id
	 * 
	 * @param strId		the package id 
	 */
	public void setId(String strId)
	{
		this.strId = strId;
	}

	/**
	 * get the package name
	 * 
	 * @return the package name 
	 */
	public String getName()
	{
		return this.strName;
	}

	/**
	 * set the package name
	 * 
	 * @param strName		the package name 
	 */
	public void setName(String strName)
	{
		this.strName = strName;
	}

	/**
	 * get the package header 
	 * 
	 * @return the package header 
	 */
	public PackageHeader getPackageHeader()
	{
		return this.packageHeader;
	}

	/**
	 * set the package header
	 * 
	 * @param packageHeader 	the package header
	 */
	public void setPackageHeader(PackageHeader packageHeader)
	{
		this.packageHeader = packageHeader;
	}

	/**
	 * get the participants 
	 * 
	 * @return the participants 
	 */
	public Hashtable getParticipants()
	{
		return this.hashParticipants;
	}

	/**
	 * set the participants
	 * 
	 * @param hashParticipants 	the participants
	 */
	public void setParticipants(Hashtable hashParticipants)
	{
		this.hashParticipants = hashParticipants;
	}

	/**
	 * get the workflow processes 
	 * 
	 * @return the workflow processes 
	 */
	public Hashtable getWorkflowProcesses()
	{
		return this.hashWorkflowProcesses;
	}

	/**
	 * set the workflow processes 
	 * 
	 * @param hashWorkflowProcesses 	the workflow processes
	 */
	public void setWorkflowProcesses(Hashtable hashWorkflowProcesses)
	{
		this.hashWorkflowProcesses = hashWorkflowProcesses;
	}

	/**
	 * get the package name
	 * 
	 * @return the  package name
	 */
	public String toString()
	{
		return this.strName;
	}
}
