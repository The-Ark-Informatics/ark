/**

 * Task.java

 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.

 * Created Date: 14/03/2004

 *

 * Last Modified: (Date\Author\Comments)

 *

 * 

 */



package neuragenix.genix.workflow;


import java.util.StringTokenizer;

/**

 * Task - Store info for a task

 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>

 */

public class Task implements java.io.Serializable

{
	// the max length for task label in 1 line
	private final int LINE_LENGTH = 12;

	// Task key

	private String strKey = null;



	// Name

	private String strName = null;



	// Status

	private String strStatus = null;



	// Type 

	private String strType = null;



	// Performer

	private String strPerformer = null;



	// Performer Type

	private String strPerformerType = null;



	// Date completed 

	private String strDateCompleted = null;



	// Time completed 

	private String strTimeCompleted = null;



    // Comment

	private String strComment = null;



	// Action

	private String strAction = null;

	
	// Instruction

	private String strInstruction = null;


	// Position X

	private String strPosX = null;



	// Position Y

	private String strPosY = null;



	// Width

	private String strWidth =  null;



	// Height

	private String strHeight = null;



	// Workflow Instance Key

	private String strWorkflowInstanceKey = null;



	/**

	 * Default constructor

	 */

	public Task()

	{

	}



	/**

	 * Full constructor

	 */

	public Task(String strKey,

				String strName,

				String strStatus,

				String strType,

				String strPerformer,

				String strPerformerType,

				String strDateCompleted,

				String strTimeCompleted,

				String strComment,

				String strAction,

				String strInstruction,

				String strPosX,

				String strPosY,

				String strWidth,

				String strHeight,

				String strWorkflowInstanceKey)

	{

		this.strKey = strKey;

		this.strName = strName;

		this.strStatus = strStatus;

		this.strType = strType;

		this.strPerformer = strPerformer;

		this.strPerformerType = strPerformerType;

		this.strDateCompleted = strDateCompleted;

		this.strTimeCompleted = strTimeCompleted;

		this.strComment = strComment;

		this.strAction = strAction;

		this.strInstruction = strInstruction;

		this.strPosX = strPosX;

		this.strPosY = strPosY;

		this.strWidth = strWidth;

		this.strHeight = strHeight;

		this.strWorkflowInstanceKey = strWorkflowInstanceKey;

	}



	/**

	 * get the task key 

	 * 

	 * @return the task key 

	 */

	public String getKey()

	{

		return this.strKey;

	}



	/**

	 * set the task key 

	 * 

	 * @param strKey	the task key 

	 */

	public void setKey(String strKey)

	{

		this.strKey = strKey;

	}



	/**

	 * get the task name

	 * 

	 * @return the task name

	 */

	public String getName()

	{

		return this.strName;

	}



	/**

	 * set the task name 

	 * 

	 * @param strName	the task name

	 */

	public void setName(String strName)

	{

		this.strName = strName;

	}



	/**

	 * get the task status 

	 * 

	 * @return the task status 

	 */

	public String getStatus()

	{

		return this.strStatus;

	}



	/**

	 * set the task status  

	 * 

	 * @param strStatus the task status

	 */

	public void setStatus(String strStatus)

	{

		this.strStatus = strStatus;

	}



	/**

	 * get the task type

	 * 

	 * @return the task type

	 */

	public String getType()

	{

		return this.strType;

	}



	/**

	 * set the task type 

	 * 

	 * @param strType the task type 

	 */

	public void setType(String strType)

	{

		this.strType = strType;

	}



	/**

	 * get the task performer 

	 * 

	 * @return the task performer

	 */

	public String getPerformer()

	{

		return this.strPerformer;

	}



	/**

	 * set the task performer 

	 * 

	 * @param strPerformer	 the task performer 

	 */

	public void setPerformer(String strPerformer)

	{

		this.strPerformer = strPerformer;

	}



	/**

	 * get the task performer type 

	 * 

	 * @return the task performer type

	 */

	public String getPerformerType()

	{

		return this.strPerformerType;

	}



	/**

	 * set the task performer type

	 * 

	 * @param strPerformerType	 the task performer type 

	 */

	public void setPerformerType(String strPerformerType)

	{

		this.strPerformerType = strPerformerType;

	}



	/**

	 * get the date completed

	 * 

	 * @return the date completed 

	 */

	public String getDateCompleted()

	{

		return this.strDateCompleted;

	}



	/**

	 * set the date completed 

	 * 

	 * @param strDateCompleted the date completed 

	 */

	public void setDateCompleted(String strDateCompleted)

	{

		this.strDateCompleted = strDateCompleted;

	}



	/**

	 * get the time completed

	 * 

	 * @return the time completed 

	 */

	public String getTimeCompleted()

	{

		return this.strTimeCompleted;

	}



	/**

	 * set the time completed 

	 * 

	 * @param strTimeCompleted the time completed 

	 */

	public void setTimeCompleted(String strTimeCompleted)

	{

		this.strTimeCompleted = strTimeCompleted;

	}



	/**

	 * get the comment

	 * 

	 * @return the comment 

	 */

	public String getComment()

	{

		return this.strComment;

	}



	/**

	 * set the comment 

	 * 

	 * @param strComment the comment 

	 */

	public void setComment(String strComment)

	{

		this.strComment = strComment;

	}



	/**

	 * get the action

	 * 

	 * @return the action

	 */

	public String getAction()

	{

		return this.strAction;

	}



	/**

	 * set the action 

	 * 

	 * @param strAction the action 

	 */

	public void setAction(String strAction)

	{

		this.strAction = strAction;

	}



	/**

	 * get the instruction 

	 * 

	 * @return the instruction 

	 */

	public String getInstruction()

	{

		return this.strInstruction;

	}



	/**

	 * set the instruction

	 * 

	 * @param strInstruction the instruction 

	 */

	public void setInstruction(String strInstruction)

	{

		this.strInstruction = strInstruction;

	}



	/**

	 * get the position X

	 * 

	 * @return the position X 

	 */

	public String getPositionX()

	{

		return this.strPosX;

	}



	/**

	 * set the position X 

	 * 

	 * @param strPosX the position X

	 */

	public void setPositionX(String strPosX)

	{

		this.strPosX = strPosX;

	}



	/**

	 * get the position Y

	 * 

	 * @return the position Y

	 */

	public String getPositionY()

	{

		return this.strPosY;

	}



	/**

	 * set the position Y 

	 * 

	 * @param strPosY the position Y

	 */

	public void setPositionY(String strPosY)

	{

		this.strPosY = strPosY;

	}



	/**

	 * get the width

	 * 

	 * @return the width 

	 */

	public String getWidth()

	{

		return this.strWidth;

	}



	/**

	 * set the width

	 * 

	 * @param strWidth	the width 

	 */

	public void setWidth(String strWidth)

	{

		this.strWidth = strWidth;

	}



	/**

	 * get the height

	 * 

	 * @return the height 

	 */

	public String getHeight()

	{

		return this.strHeight;

	}



	/**

	 * set the height 

	 * 

	 * @param strHeight	the height

	 */

	public void setHeight(String strHeight)

	{

		this.strHeight = strHeight;

	}



	/**

	 * get the workflow instance key

	 * 

	 * @return the workflow instance key 

	 */

	public String getWorkflowInstanceKey()

	{

		return this.strWorkflowInstanceKey;

	}



	/**

	 * set the workflow instance key 

	 * 

	 * @param strWorkflowInstanceKey	the workflow instance key

	 */

	public void setWorkflowInstanceKey(String strWorkflowInstanceKey)

	{

		this.strWorkflowInstanceKey = strWorkflowInstanceKey;

	}



	/**

	 * get the task name

	 * 

	 * @return the  task name

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
