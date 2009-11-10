package media.neuragenix.genix.workflow.model;



public class ProcessHeader

{

	private String strCreated = null;

	private String strDescription = null;



	public ProcessHeader()

	{

	}



	public ProcessHeader(String strCreated,

						 String strDescription)

	{

		this.strCreated = strCreated;

		this.strDescription = strDescription;

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

