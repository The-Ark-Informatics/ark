package media.neuragenix.genix.workflow.model;



public class PackageHeader

{

	private String strXPDLVersion = null;

	private String strVendor = null;

	private String strCreated = null;

	private String strDescription = null;



	public PackageHeader()

	{

	}



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



	public String getXPDLVersion()

	{

		return this.strXPDLVersion;

	}



	public void setXPDLVersion(String strXPDLVersion)

	{

		this.strXPDLVersion = strXPDLVersion;

	}



	public String getVendor()

	{

		return this.strVendor;

	}



	public void setVendor(String strVendor)

	{

		this.strVendor = strVendor;

	}



	public String getCreated()

	{

		return this.strCreated;

	}



	public void setCreated(String strCreated)

	{

		this.strCreated = strCreated;

	}



	public String getDescription()

	{

		return this.strDescription;

	}



	public void setDescription(String strDescription)

	{

		this.strDescription = strDescription;

	}

}

