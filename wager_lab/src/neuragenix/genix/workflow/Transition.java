/**
 * Transition.java
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
 * Transition - Store info of a transition
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class Transition implements java.io.Serializable
{
	// Id
	private String strId = null;

	// Origin activity
	private String strFrom = null;

	// Target activity
	private String strTo = null;

	// transition name
	private String strName = null;

	// transition condition 
	private String strCondition = null;

	// transition type
	private String strType = null;

	// transition X-Coordinate 
	private String strXCoordinate = null;

	// transition Y-Coordinate 
	private String strYCoordinate = null;
	
	/**
	 * Default constructor
	 */
	public Transition()
	{
	}

	/**
	 * Full constructor
	 */
	public Transition(String strId,
				   	  String strFrom,
				      String strTo,
				      String strName,
					  String strCondition,
					  String strType,
					  String strXCoordinate,
					  String strYCoordinate)
	{
		this.strId = strId;
		this.strFrom = strFrom;
		this.strTo = strTo;
		this.strName = strName;
		this.strCondition = strCondition;
		this.strType = strType;
		this.strXCoordinate = strXCoordinate;
		this.strYCoordinate = strYCoordinate;
	}

	/**
	 * get the transition id
	 * 
	 * @return the transition id 
	 */
	public String getId()
	{
		return this.strId;
	}

	/**
	 * set the transition id
	 * 
	 * @param strId		the transition id 
	 */
	public void setId(String strId)
	{
		this.strId = strId;
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
		this.strFrom= strFrom;
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
	 * get the transition condition 
	 * 
	 * @return the transition condition 
	 */
	public String getCondition()
	{
		return this.strCondition;
	}

	/**
	 * set the transition condition 
	 * 
	 * @param strCondition		the transition condition 
	 */
	public void setCondition(String strCondition)
	{
		this.strCondition= strCondition;
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
	 * get the transition name
	 * 
	 * @return the  transition  name
	 */
	public String toString()
	{
		return this.strName;
	}
}
