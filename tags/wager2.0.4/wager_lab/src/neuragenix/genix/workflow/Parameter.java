/**
 * Parameter.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 23/02/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 
 */

package neuragenix.genix.workflow;

/**
 * Parameter - Store info of an activity parameter
 * @author <a href="mailto:aagustian@neuragenix.com">Agustian Agustian</a>
 */

public class Parameter implements java.io.Serializable
{
	// Id
    private String strName = null;
   
	// Type
    private String strType = null;
   
	// Description
    private String strDescription = null;
    
    /**
	 * Default constructor 
     */
    public Parameter() 
    {
    }
    
    /** 
	 * Full Constructor
     */
    public Parameter(String strName, String strType, String strDescription)
    {
        this.strName = strName;
        this.strType = strType;
        this.strDescription = strDescription;
    }
   
	/**
	 * get the parameter name
	 * 
	 * @return the parameter name
	 */
    public String getName()
    {
        return this.strName;
    }

	/**
	 * set the parameter name
	 * 
	 * @param strName	the parameter name
	 */
    public void setName(String strName)
    {
        this.strName = strName;
    }
   
	/**
	 * get the parameter type 
	 * 
	 * @return the parameter type
	 */
    public String getType()
    {
        return this.strType;
    }
      
	/**
	 * set the parameter type
	 * 
	 * @param strType	the parameter type
	 */
    public void setType(String strType)
    {
        this.strType = strType;
    }

	/**
	 * get the parameter description
	 * 
	 * @return the parameter description 
	 */
    public String getDescription()
    {
        return this.strDescription;
    }
   
	/**
	 * set the parameter description 
	 * 
	 * @param strDescription	the parameter description
	 */
    public void setDescription(String strDescription)
    {
        this.strDescription = strDescription;
    }

	/**
	 * get the parameter name
	 * 
	 * @return the  parameter name
	 */
	public String toString()
	{
		return this.strName;
	}
}
