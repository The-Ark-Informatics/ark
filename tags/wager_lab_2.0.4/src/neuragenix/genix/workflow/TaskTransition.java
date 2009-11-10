/**
 * TaskTransition.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 14/03/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 
 */

package neuragenix.genix.workflow;

/**
 * TaskTransition - Store info of a task transition
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class TaskTransition implements java.io.Serializable
{
	// Origin activity
	private String strFrom = null;

	// Target activity
	private String strTo = null;

	// transition name
	private String strName = null;

	// transition type
	private String strType = null;

	// transition X-Coordinate 
	private String strXCoordinate = null;

	// transition Y-Coordinate 
	private String strYCoordinate = null;

	
	/**
	 * Default constructor
	 */
	public TaskTransition()
	{
	}

	/**
	 * Full constructor
	 */
	public TaskTransition(String strFrom,
				      	  String strTo,
				      	  String strName,
						  String strType,
					  	  String strXCoordinate,
					   	  String strYCoordinate)
	{
		this.strFrom = strFrom;
		this.strTo = strTo;
		this.strName = strName;
		this.strType = strType;
		this.strXCoordinate = strXCoordinate;
		this.strYCoordinate = strYCoordinate;
	}

	/**
	 * get the transition origin
	 * 
	 * @return the transition origin 
	 */
	public String getFrom()
	{
		return this.strFrom;
	}

	/**
	 * set the transition origin
	 * 
	 * @param strId		the transition origin 
	 */
	public void setFrom(String strFrom)
	{
		this.strFrom = strFrom;
	}

	/**
	 * get the transition target
	 * 
	 * @return the transition target 
	 */
	public String getTo()
	{
		return this.strTo;
	}

	/**
	 * set the transition target
	 * 
	 * @param strId		the transition target
	 */
	public void setTo(String strTo)
	{
		this.strTo = strTo;
	}

	/**
	 * get the transition name 
	 * 
	 * @return the transition name 
	 */
	public String getName()
	{
		return this.strName;
	}

	/**
	 * set the transition name
	 * 
	 * @param strId		the transition name
	 */
	public void setName(String strName)
	{
		this.strName = strName;
	}

	/**
	 * get the transition type 
	 * 
	 * @return the transition type 
	 */
	public String getType()
	{
		return this.strType;
	}

	/**
	 * set the transition type 
	 * 
	 * @param strType	the transition type
	 */
	public void setType(String strType)
	{
		this.strType= strType;
	}

	/**
	 * get the transition X Coordinate
	 * 
	 * @return the transition X Coordinate
	 */
	public String getXCoordinate()
	{
		return this.strXCoordinate;
	}

	/**
	 * set the transition X Coordinate 
	 * 
	 * @param strXCoordinate 	the transition X Coordinate 
	 */
	public void setXCoordinate(String strXCoordinate)
	{
		this.strXCoordinate = strXCoordinate;
	}

	/**
	 * get the transition Y Coordinate
	 * 
	 * @return the transition Y Coordinate
	 */
	public String getYCoordinate()
	{
		return this.strYCoordinate;
	}

	/**
	 * set the transition Y Coordinate 
	 * 
	 * @param strYCoordinate 	the transition Y Coordinate 
	 */
	public void setYCoordinate(String strYCoordinate)
	{
		this.strYCoordinate = strYCoordinate;
	}

	/**
	 * get the task transition name
	 * 
	 * @return the  task transition name
	 */
	public String toString()
	{
		return this.strName;
	}
}
