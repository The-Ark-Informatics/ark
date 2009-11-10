/**
 * ProcessHeader.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 03/02/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 13/02/2004		Agustian Agustian		Commenting
 *
 * 
 */

package neuragenix.genix.workflow;

/**
 * Participant - Store info of a process header
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class ProcessHeader implements java.io.Serializable
{
	// Date created
	private String strCreated = null;

	// Description
	private String strDescription = null;

	/**
	 * Default constructor
	 */
	public ProcessHeader()
	{
	}

	/**
	 * Full constructor
	 */
	public ProcessHeader(String strCreated,
						 String strDescription)
	{
		this.strCreated = strCreated;
		this.strDescription = strDescription;
	}

	/**
	 * get the date created 
	 * 
	 * @return the date created 
	 */
	public String getCreated()
	{
		return this.strCreated;
	}

	/**
	 * set the date created
	 * 
	 * @param strCreated	the date created
	 */
	public void setCreated(String strCreated)
	{
		this.strCreated = strCreated;
	}

	/**
	 * get the description
	 * 
	 * @return the description 
	 */
	public String getDescription()
	{
		return this.strDescription;
	}

	/**
	 * set the description 
	 * 
	 * @param strDescription		the description 
	 */
	public void setDescription(String strDescription)
	{
		this.strDescription = strDescription;
	}

	/**
	 * get the process header name
	 * 
	 * @return the  process header name
	 */
	public String toString()
	{
		String strName = "";
		return strName;
	}
}
