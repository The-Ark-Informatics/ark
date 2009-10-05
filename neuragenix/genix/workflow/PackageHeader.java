/**
 * PackageHeader.java
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
 * PackageHeader - Store info of a package header
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class PackageHeader implements java.io.Serializable
{
	// XPDL Version
	private String strXPDLVersion = null;

	// Vendor
	private String strVendor = null;

	// Created Date
	private String strCreated = null;

	// Description
	private String strDescription = null;

	/**
	 * Default constructor
	 */
	public PackageHeader()
	{
	}

	/**
	 * Full constructor
	 */
	public PackageHeader(String strXPDLVersion,
						 String strVendor,
						 String strCreated,
						 String strDescription)
	{
		this.strXPDLVersion = strXPDLVersion;
		this.strVendor = strVendor;
		this.strCreated = strCreated;
		this.strDescription = strDescription;
	}

	/**
	 * get the XPDL version
	 * 
	 * @return the XPDL version
	 */
	public String getXPDLVersion()
	{
		return this.strXPDLVersion;
	}

	/**
	 * set the XPDL version
	 * 
	 * @param strXPDLVersion 	the XPDL version
	 */
	public void setXPDLVersion(String strXPDLVersion)
	{
		this.strXPDLVersion = strXPDLVersion;
	}

	/**
	 * get the vendor
	 * 
	 * @return the vendor
	 */
	public String getVendor()
	{
		return this.strVendor;
	}

	/**
	 * set the vendor 
	 * 
	 * @param strVendor 	the vendor
	 */
	public void setVendor(String strVendor)
	{
		this.strVendor = strVendor;
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
	 * set the created date 
	 * 
	 * @param strCreated 	the created date
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
	 * @param strDescription	the description
	 */
	public void setDescription(String strDescription)
	{
		this.strDescription = strDescription;
	}

	/**
	 * get the package header name
	 * 
	 * @return the  package header name
	 */
	public String toString()
	{
		String strName = "";
		return strName;
	}
}
