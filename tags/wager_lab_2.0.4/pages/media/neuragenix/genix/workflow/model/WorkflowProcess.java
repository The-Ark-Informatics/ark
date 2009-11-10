package media.neuragenix.genix.workflow.model;



import java.util.Hashtable;


public class WorkflowProcess 

{



	private String strId = null;

	private String strName = null;

	private String strInitialActivityId = null;

	private ProcessHeader processHeader = null;

	private Hashtable hashTriggers = null;

	private Hashtable hashActivities = null;

	private Hashtable hashTransitions = null;



	public WorkflowProcess()

	{

	}



	public WorkflowProcess(String strId,

				   		   String strName,

						   String strInitialActivityId,

				   		   ProcessHeader processHeader,

				   		   Hashtable hashTriggers,

				   		   Hashtable hashActivities,

				   		   Hashtable hashTransitions)

	{

		this.strId = strId;

		this.strName = strName;

		this.strInitialActivityId = strInitialActivityId;

		this.processHeader = processHeader;

		this.hashTriggers = hashTriggers;

		this.hashActivities = hashActivities;

		this.hashTransitions = hashTransitions;

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



	public void setInitialActivityId(String strInitialActivityId)

	{

		this.strInitialActivityId = strInitialActivityId;

	}



	public String getInitialActivityId()

	{

		return this.strInitialActivityId;

	}



	public void setName(String strName)

	{

		this.strName = strName;

	}





	public ProcessHeader getProcessHeader()

	{

		return this.processHeader;

	}



	public void setProcessHeader(ProcessHeader processHeader)

	{

		this.processHeader = processHeader;

	}



	public Hashtable getTriggers()

	{

		return this.hashTriggers;

	}



	public void setTriggers(Hashtable hashTriggers)

	{

		this.hashTriggers = hashTriggers;

	}



	public Hashtable getActivities()

	{

		return this.hashActivities;

	}



	public void setActivities(Hashtable hashActivities)

	{

		this.hashActivities = hashActivities;

	}



	public Hashtable getTransitions()

	{

		return this.hashTransitions;

	}



	public void setTransitions(Hashtable hashTransitions)

	{

		this.hashTransitions = hashTransitions;

	}

}

