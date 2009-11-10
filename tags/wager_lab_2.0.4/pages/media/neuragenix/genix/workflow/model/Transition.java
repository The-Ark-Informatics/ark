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



package media.neuragenix.genix.workflow.model;



/**

 * Transition - Store info of a transition

 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>

 */

public class Transition

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

					  String strCondition)

	{

		this.strId = strId;

		this.strFrom = strFrom;

		this.strTo = strTo;

		this.strName = strName;

		this.strCondition = strCondition;

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


        
        public String toString()
        {
            return getName();
        }

}

