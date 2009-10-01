/**

 * Trigger.java

 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.

 * Created Date: 03/02/2004

 *

 * Last Modified: (Date\Author\Comments)

 * 13/02/2004		Agustian Agustian		Commenting

 *

 * 

 */



package neuragenix.genix.workflow;



/**

 * Trigger - Store info of a trigger

 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>

 */

public class Trigger implements java.io.Serializable

{

	// Id

	private String strId = null;



	// Name

	private String strName = null;



	// Action

	private String strAction = null;



	// Domain

	private String strDomain = null;



	// Field

	private String strField = null;



	// Type 

	private String strType = null;



	// Connection1

	private String strConnection1 = null;



	// Operator1

	private String strOperator1 = null;



	// Value1

	private String strValue1 = null;



	// Connection2

	private String strConnection2 = null;



	// Operator2

	private String strOperator2 = null;



	// Value2

	private String strValue2 = null;



	// Key

	private String strKey = null;



	// Workflow Template Key

	private String strWorkflowTemplateKey = null;



	// Workflow Package Key

	private String strWorkflowPackageKey = null;



	/**

	 * Default constructor

	 */

	public Trigger()

	{

	}



	/**

	 * Full constructor

	 */

	public Trigger(String strId,

				   String strName,

				   String strAction,

				   String strDomain,

				   String strField,

				   String strType,

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

		this.strType = strType;

		this.strConnection1 = strConnection1;

		this.strOperator1 = strOperator1;

		this.strValue1 = strValue1;

		this.strConnection2 = strConnection2;

		this.strOperator2 = strOperator2;

		this.strValue2 = strValue2;

		

	}



	/**

	 * get the trigger id

	 * 

	 * @return the trigger id 

	 */

	public String getId()

	{

		return this.strId;

	}



	/**

	 * set the trigger id

	 * 

	 * @param strId	the trigger id 

	 */

	public void setId(String strId)

	{

		this.strId = strId;

	}



	/**

	 * get the trigger name

	 * 

	 * @return the trigger name

	 */

	public String getName()

	{

		return this.strName;

	}



	/**

	 * set the trigger name

	 * 

	 * @param strName	the trigger name

	 */

	public void setName(String strName)

	{

		this.strName = strName;

	}



	/**

	 * get the trigger action 

	 * 

	 * @return the trigger action 

	 */

	public String getAction()

	{

		return this.strAction;

	}



	/**

	 * set the trigger action 

	 * 

	 * @param strAction	the trigger action 

	 */

	public void setAction(String strAction)

	{

		this.strAction = strAction;

	}



	/**

	 * get the trigger domain 

	 * 

	 * @return the trigger domain 

	 */

	public String getDomain()

	{

		return this.strDomain;

	}



	/**

	 * set the trigger domain 

	 * 

	 * @param strDomain	the trigger domain 

	 */

	public void setDomain(String strDomain)

	{

		this.strDomain = strDomain;

	}



	/**

	 * get the trigger field 

	 * 

	 * @return the trigger field 

	 */

	public String getField()

	{

		return this.strField;

	}



	/**

	 * set the trigger field 

	 * 

	 * @param strField	the trigger field 

	 */

	public void setField(String strField)

	{

		this.strField = strField;

	}



	/**

	 * get the trigger type

	 * 

	 * @return the trigger type

	 */

	public String getType()

	{

		return this.strType;

	}



	/**

	 * set the trigger type 

	 * 

	 * @param strType the trigger type 

	 */

	public void setType(String strType)

	{

		this.strType = strType;

	}



	/**

	 * get the trigger connection1 

	 * 

	 * @return the trigger connection1

	 */

	public String getConnection1()

	{

		return this.strConnection1;

	}



	/**

	 * set the trigger connection1 

	 * 

	 * @param strConnection1	the trigger connection1

	 */

	public void setConnection1(String strConnection1)

	{

		this.strConnection1 = strConnection1;

	}



	/**

	 * get the trigger operator1 

	 * 

	 * @return the trigger operator1 

	 */

	public String getOperator1()

	{

		return this.strOperator1;

	}



	/**

	 * set the trigger operator1 

	 * 

	 * @param strOperator1	the trigger operator1

	 */

	public void setOperator1(String strOperator1)

	{

		this.strOperator1 = strOperator1;

	}



	/**

	 * get the trigger value1

	 * 

	 * @return the trigger value1 

	 */

	public String getValue1()

	{

		return this.strValue1;

	}



	/**

	 * set the trigger value1

	 * 

	 * @param strValue1	the trigger value1

	 */

	public void setValue1(String strValue1)

	{

		this.strValue1 = strValue1;

	}



	/**

	 * get the trigger connection2 

	 * 

	 * @return the trigger connection2

	 */

	public String getConnection2()

	{

		return this.strConnection2;

	}



	/**

	 * set the trigger connection2 

	 * 

	 * @param strConnection2	the trigger connection2

	 */

	public void setConnection2(String strConnection2)

	{

		this.strConnection2 = strConnection2;

	}



	/**

	 * get the trigger operator2 

	 * 

	 * @return the trigger operator2 

	 */

	public String getOperator2()

	{

		return this.strOperator2;

	}



	/**

	 * set the trigger operator2 

	 * 

	 * @param strOperator2	the trigger operator2

	 */

	public void setOperator2(String strOperator2)

	{

		this.strOperator2 = strOperator2;

	}



	/**

	 * get the trigger value2

	 * 

	 * @return the trigger value2 

	 */

	public String getValue2()

	{

		return this.strValue2;

	}



	/**

	 * set the trigger value2

	 * 

	 * @param strValue2	the trigger value2

	 */

	public void setValue2(String strValue2)

	{

		this.strValue2 = strValue2;

	}



	/**

	 * get the trigger name

	 * 

	 * @return the  trigger name

	 */

	public String toString()

	{
            String strResult = "";
            
            strResult += (strConnection1 == null ? "" : strConnection1);
            for (int i=strResult.length(); i < 4; i++)
                strResult += " ";
            
            strResult += strField;
            for (int i=strResult.length(); i < 44; i++)
                strResult += " ";
            
            strResult += strOperator1 + "     ";
            for (int i=strResult.length(); i < 48; i++)
                strResult += " ";
            
            strResult += strValue1;
            for (int i=strResult.length(); i < 68; i++)
                strResult += " ";
            
            if (strConnection2 != null && !strConnection2.equals(""))
            {
                strResult += strConnection2;
                for (int i=strResult.length(); i < 72; i++)
                    strResult += " ";

                strResult += strField;
                for (int i=strResult.length(); i < 112; i++)
                    strResult += " ";

                strResult += strOperator2 + "     ";
                for (int i=strResult.length(); i < 116; i++)
                    strResult += " ";

                strResult += strValue2;
                
            }
            

            return strResult;

	}



	/**

	 * get the trigger key 

	 *  

	 * @return the trigger key 

	 */

	public String getKey()

	{

		return this.strKey;

	}



	/**

	 * set the trigger key

	 * 

	 * @param strKey	the trigger key 

	 */

	public void setKey(String strKey)

	{

		this.strKey = strKey;

	}



	/**

	 * get the workflow template key 

	 *  

	 * @return the workflow template key 

	 */

	public String getWorkflowTemplateKey()

	{

		return this.strWorkflowTemplateKey;

	}



	/**

	 * set the workflow template key

	 * 

	 * @param strWorkflowTemplateKey	the workflow template key 

	 */

	public void setWorkflowTemplateKey(String strWorkflowTemplateKey)

	{

		this.strWorkflowTemplateKey = strWorkflowTemplateKey;

	}



	/**

	 * get the workflow package key 

	 *  

	 * @return the workflow package key 

	 */

	public String getWorkflowPackageKey()

	{

		return this.strWorkflowPackageKey;

	}



	/**

	 * set the workflow package key

	 * 

	 * @param strWorkflowPackageKey	the workflow package key 

	 */

	public void setWorkflowPackageKey(String strWorkflowPackageKey)

	{

		this.strWorkflowPackageKey = strWorkflowPackageKey;

	}

}