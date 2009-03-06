package media.neuragenix.genix.workflow.model;



import java.util.Hashtable;


public class Package

{



	private String strId = null;

	private String strName = null;

	private PackageHeader packageHeader = null;

	private Hashtable hashParticipants = null;

	private Hashtable hashWorkflowProcesses = null;



	public Package()

	{

	}



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



	public String getId()

	{

		return this.strId;

	}



	public void setId(String strId)

	{

		this.strId = strId;

	}



	public String getName()

	{

		return this.strName;

	}



	public void setName(String strName)

	{

		this.strName = strName;

	}



	public PackageHeader getPackageHeader()

	{

		return this.packageHeader;

	}



	public void setPackageHeader(PackageHeader packageHeader)

	{

		this.packageHeader = packageHeader;

	}



	public Hashtable getParticipants()

	{

		return this.hashParticipants;

	}



	public void setParticipants(Hashtable hashParticipants)

	{

		this.hashParticipants = hashParticipants;

	}



	public Hashtable getWorkflowProcesses()

	{

		return this.hashWorkflowProcesses;

	}



	public void setWorkflowProcesses(Hashtable hashWorkflowProcesses)

	{

		this.hashWorkflowProcesses = hashWorkflowProcesses;

	}



}

