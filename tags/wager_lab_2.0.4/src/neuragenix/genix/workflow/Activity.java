/**

 * Activity.java

 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.

 * Created Date: 03/02/2004

 *

 * Last Modified: (Date\Author\Comments)

 * 12/02/2004		Agustian Agustian		Commenting

 * 19/02/2004		Agustian Agustian		Add parameters

 * 24/02/2004		Agustian Agustian		Change parameters from a vector to a hashtable

 *

 * 

 */



package neuragenix.genix.workflow;



import java.util.Vector;

import java.util.Hashtable;

import java.util.StringTokenizer;



/**

 * Activity - Store info for an activity

 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>

 */

public class Activity implements java.io.Serializable

{
        // Length of a line
    
        private static final int LINE_LENGTH = 12;

	// Id

	private String strId = null;



	// Name

	private String strName = null;



	// Description

	private String strDescription = null;



	// Performer

	private String strPerformer = null;



	// Performer Type

	private String strPerformerType = null;



	// Priority

	private String strPriority = null;



	// Parameters

 	private Hashtable hashParameters = null;	



	// Extended Attributes

	private Hashtable hashExtendedAttributes = null;



	/**

	 * Default constructor

	 */

	public Activity()

	{

	}



	/**

	 * Full constructor

	 */

	public Activity(String strId,

				    String strName,

				    String strDescription,

				    String strPerformer,

				    String strPerformerType,

				    String strPriority,

					Hashtable hashParameters,

				    Hashtable hashExtendedAttributes)

	{

		this.strId = strId;

		this.strName = strName;

		this.strDescription = strDescription;

		this.strPerformer = strPerformer;

		this.strPerformerType = strPerformerType;

		this.strPriority = strPriority;

		this.hashParameters = hashParameters;

		this.hashExtendedAttributes = hashExtendedAttributes;

	}



	/**

	 * get the activity id

	 * 

	 * @return the activity id

	 */

	public String getId()

	{

		return this.strId;

	}



	/**

	 * set the activity id

	 * 

	 * @param strId		the activity id

	 */

	public void setId(String strId)

	{

		this.strId = strId;

	}



	/**

	 * get the activity name

	 * 

	 * @return the activity name

	 */

	public String getName()

	{

		return this.strName;

	}



	/**

	 * set the activity name 

	 * 

	 * @param strName	the activity name

	 */

	public void setName(String strName)

	{

		this.strName = strName;

	}



	/**

	 * get the activity description 

	 * 

	 * @return the activity description

	 */

	public String getDescription()

	{

		return this.strDescription;

	}



	/**

	 * set the activity description 

	 * 

	 * @param strDescription	the activity description

	 */

	public void setDescription(String strDescription)

	{

		this.strDescription = strDescription;

	}

	

	/**

	 * get the activity performer 

	 * 

	 * @return the activity performer

	 */

	public String getPerformer()

	{

		return this.strPerformer;

	}



	/**

	 * set the activity performer 

	 * 

	 * @param strPerformer	 the activity performer 

	 */

	public void setPerformer(String strPerformer)

	{

		this.strPerformer = strPerformer;

	}



	/**

	 * get the activity performer type 

	 * 

	 * @return the activity performer type

	 */

	public String getPerformerType()

	{

		return this.strPerformerType;

	}



	/**

	 * set the activity performer type

	 * 

	 * @param strPerformerType	 the activity performer type 

	 */

	public void setPerformerType(String strPerformerType)

	{

		this.strPerformerType = strPerformerType;

	}



	/**

	 * get the activity priority

	 * 

	 * @return the activity priority

	 */

	public String getPriority()

	{

		return this.strPriority;

	}



	/**

	 * set the activity priority 

	 * 

	 * @param strPriority	 the activity priority

	 */

	public void setPriority(String strPriority)

	{

		this.strPriority = strPriority;

	}



	/**

	 * get the activity parameters 

	 * 

	 * @return the activity parameters

	 */

	public Hashtable getParameters()

	{

		return this.hashParameters;

	}



	/**

	 * set the activity parameters 

	 * 

	 * @param hashParameters the activity parameters

	 */

	public void setParameters(Hashtable hashParameters)

	{

		this.hashParameters = hashParameters;

	}

	

	/**

	 * get the activity extended attribute 

	 * 

	 * @return the activity extended attribute 

	 */

	public Hashtable getExtendedAttributes()

	{

		return this.hashExtendedAttributes;

	}



	/**

	 * set the activity extended attributes 

	 * 

	 * @param hashExtendedAttributes	the extended attributes

	 */

	public void setExtendedAttributes(Hashtable hashExtendedAttributes)

	{

		this.hashExtendedAttributes = hashExtendedAttributes;

	}



	/**

	 * get the activity name

	 * 

	 * @return the activity name

	 */

	public String toString()
        {
            String strResult = "";
            String strLine = "";
            StringTokenizer token = new StringTokenizer(strName);
            
            while (token.hasMoreElements())
            {
                strLine += token.nextToken() + " ";
                if (strLine.length() > LINE_LENGTH)
                {
                    if (token.hasMoreElements())
                        strResult += strLine + "<br>";
                    else
                        strResult += strLine;
                    
                    strLine = "";
                }
                else
                {
                    if (!token.hasMoreElements())
                        strResult += strLine;
                }
            }
            
            
            return strResult = "<html><p style=\"text-align:center\">" + strResult + "</p></html>";
        }

}

