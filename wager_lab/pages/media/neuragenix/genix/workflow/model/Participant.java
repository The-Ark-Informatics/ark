package media.neuragenix.genix.workflow.model;



public class Participant

{

	private String strId = null;

	private String strName = null;

	private String strType = null;

	private String strDescription = null;



	public Participant()

	{

	}



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



	public String getId()

	{

		return this.strId;

	}



	public void setId(String strId)

	{

		this.strId= strId;

	}



	public String getName()

	{

		return this.strName;

	}



	public void setName(String strName)

	{

		this.strName= strName;

	}



	public String getType()

	{

		return this.strType;

	}



	public void setType(String strType)

	{

		this.strType = strType;

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

