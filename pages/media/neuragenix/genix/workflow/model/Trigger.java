package media.neuragenix.genix.workflow.model;



public class Trigger

{

	private String strId = null;

	private String strName = null;

	private String strAction = null;

	private String strDomain = null;

	private String strField = null;

	private String strConnection1 = null;

	private String strOperator1 = null;

	private String strValue1 = null;

	private String strConnection2 = null;

	private String strOperator2 = null;

	private String strValue2 = null;



	public Trigger()

	{

	}



	public Trigger(String strId,

				   String strName,

				   String strAction,

				   String strDomain,

				   String strField,

				   String strConnection1,

				   String strOperator1,

				   String strValue1,

				   String strConnection2,

				   String strOperator2,

				   String strValue2)

	{

		this.strId = strId;

		this.strName = strName;

		this.strAction = strAction;

		this.strDomain = strDomain;

		this.strField = strField;

		this.strConnection1 = strConnection1;

		this.strOperator1 = strOperator1;

		this.strValue1 = strValue1;

		this.strConnection2 = strConnection2;

		this.strOperator2 = strOperator2;

		this.strValue2 = strValue2;

		

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



	public String getAction()

	{

		return this.strAction;

	}



	public void setAction(String strAction)

	{

		this.strAction = strAction;

	}



	public String getDomain()

	{

		return this.strDomain;

	}



	public void setDomain(String strDomain)

	{

		this.strDomain = strDomain;

	}



	public String getField()

	{

		return this.strField;

	}



	public void setField(String strField)

	{

		this.strField = strField;

	}



	public String getConnection1()

	{

		return this.strConnection1;

	}



	public void setConnection1(String strConnection1)

	{

		this.strConnection1 = strConnection1;

	}



	public String getOperator1()

	{

		return this.strOperator1;

	}



	public void setOperator1(String strOperator1)

	{

		this.strOperator1 = strOperator1;

	}



	public String getValue1()

	{

		return this.strValue1;

	}



	public void setValue1(String strValue1)

	{

		this.strValue1 = strValue1;

	}



	public String getConnection2()

	{

		return this.strConnection2;

	}



	public void setConnection2(String strConnection2)

	{

		this.strConnection2 = strConnection2;

	}



	public String getOperator2()

	{

		return this.strOperator2;

	}



	public void setOperator2(String strOperator2)

	{

		this.strOperator2 = strOperator2;

	}



	public String getValue2()

	{

		return this.strValue2;

	}



	public void setValue2(String strValue2)

	{

		this.strValue2 = strValue2;

	}

}

