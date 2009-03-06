/**
 * Participant.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 03/02/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 12/02/2004		Agustian Agustian		Commenting
 *
 * 
 */

package neuragenix.genix.workflow;

/**
 * Participant - Store info of a participant
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class Participant implements java.io.Serializable
{
	// Id
	private String strId = null;

	// Name
	private String strName = null;

	// Type
	private String strType = null;

	// Description
	private String strDescription = null;

	/**
	 * Default constructor
	 */
	public Participant()
	{
	}

	/**
	 * Full constructor
	 */
	public Participant(String strId,
					   String strName,
					   String strType,
					   String strDescription)
	{
		this.strId = strId;
		this.strName = strName;
		this.strType = strType;
		this.strDescription = strDescription;
	}

	/**
	 * get the participant id
	 * 
	 * @return the participant id 
	 */
	public String getId()
	{
		return this.strId;
	}

	/**
	 * set the participant id
	 * 
	 * @param strId		the participant id 
	 */
	public void setId(String strId)
	{
		this.strId= strId;
	}

	/**
	 * get the participant name
	 * 
	 * @return the participant name
	 */
	public String getName()
	{
		return this.strName;
	}

	/**
	 * set the participant name
	 * 
	 * @param strName		the participant name 
	 */
	public void setName(String strName)
	{
		this.strName= strName;
	}

	/**
	 * get the participant type e.g. human/system
	 * 
	 * @return the participant type
	 */
	public String getType()
	{
		return this.strType;
	}

	/**
	 * set the participant type e.g. human/system
	 * 
	 * @param strType		the participant type
	 */
	public void setType(String strType)
	{
		this.strType = strType;
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
	 * get the participant name
	 * 
	 * @return the  participant name
	 */
	public String toString()
	{
		return this.strName;
	}
}
