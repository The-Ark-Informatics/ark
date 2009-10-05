/*   
 * Copyright (c) 2004 Neuragenix, All Rights Reserved.
 * WorkflowAlertProcess.java
 * Created on 27 April 2004
 * @author  Agustian Agustian
 * 
 * Description: 
 * This class will send email as an alert by reading the following tables:
 * - ix_workflow_task
 * - ix_alert
 * This class is suppose to run as a thread, running every interval with 
 * a sleep cycle until an interrupt is raised.
 */

package neuragenix.utils;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.lang.reflect.*;

// uPortal packages
import org.jasig.portal.services.LogService;

// neuragenix packages
import neuragenix.utils.Property;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.common.Utilities;
import neuragenix.utils.exception.*;
import neuragenix.genix.workflow.WorkflowManager;

public class WorkflowAlertProcess extends Thread 
{

	// Workflow task retry delay unit option
	private static final String UNIT_SECOND = "Second";
	private static final String UNIT_MINUTE = "Minute";
	private static final String UNIT_HOUR = "Hour";
	private static final String UNIT_DAY = "Day";
	private static final String UNIT_MONTH = "Month";
	private static final String UNIT_YEAR = "Year";
	
    private static final String CONFIGURATION_DOMAIN = "CONFIGURATION";
    private String ADMINISTRATOR_EMAIL = "";
	private String ALERT_ENGINE_EMAIL = "";
    private String SMTP_SERVER = "";   
    private String EMAIL_USERNAME = "";   
    private String EMAIL_PASSWORD = "";
	private String SEND_EMAIL = "";
    private int DELAYSECONDS = 10000;  
    
    private static boolean KEEP_PROCESS = false;   
    private static boolean STARTED = false;   

    private String strMessageTemplate;
    private String strMessageSubject;

    private ProcessRegister oProcessReg = new ProcessRegister();
    private int intProcessKey = 0;

    public WorkflowAlertProcess()
	{
        //System.err.println("Intialising workflow alert process");   
		// create domains & formfields for this channel
		InputStream file = WorkflowAlertProcess.class.getResourceAsStream("AlertDBSchema.xml");
		DatabaseSchema.loadDomains(file, "AlertDBSchema.xml");
    }

    public boolean isStarted()
	{        
        return this.STARTED;
    }

    public void run()
    {
        // Email object 
        Email email = new Email();
		SMTP_SERVER = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.SMTPServer");  
		EMAIL_USERNAME = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.EmailUsername");  
		EMAIL_PASSWORD = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.EmailPassword");  
		SEND_EMAIL = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.SendEmail");  
		ADMINISTRATOR_EMAIL = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.AdministratorEmail"); 
		ALERT_ENGINE_EMAIL = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.AlertEngineEmail");
		DELAYSECONDS = Property.getPropertyAsInt("neuragenix.utils.WorkflowAlertProcess.DelaySeconds"); 

        try 
		{
            KEEP_PROCESS = true;
            STARTED = true;

            oProcessReg.setProcessType(oProcessReg.TYPE_WORKFLOW_EMAIL_ALERT);
            oProcessReg.setProcessStatus(oProcessReg.STATUS_IDLE);
            intProcessKey = oProcessReg.insertProcessItem("Commenced","none");

            while(KEEP_PROCESS)
			{                       
				// Update Process Table as Running
				try 
				{
					oProcessReg.setProcessType(oProcessReg.TYPE_WORKFLOW_EMAIL_ALERT);
					oProcessReg.setProcessStatus(oProcessReg.STATUS_PROCESSING);
					//current time
					Calendar calendar = Calendar.getInstance();
					long longDelayTime = getMilliSeconds("Second", String.valueOf(DELAYSECONDS)); 
					long longNextAlertCheck = calendar.getTimeInMillis() + longDelayTime;
					calendar.setTimeInMillis(longNextAlertCheck);
					Date date = calendar.getTime();
					boolean  blResult = oProcessReg.updateProcessItem(intProcessKey,
									    "Next scheduled processing at:" + date.toString() , "none");
					//System.err.println("WorkflowAlertProcess - updateProcessItem = >>>" + blResult + "<<<");
				} 
				catch(ProcessRegisterUnabletoUpdate e)
				{ 
					KEEP_PROCESS = false; 
				} 
				catch(ProcessRegisterNoProcessItem e)
				{ 
					KEEP_PROCESS = false;
				}
				catch(Exception e)
				{
					LogService.log(LogService.ERROR, "Unable to register a Job Process Entry.");                
				}

				//System.err.println("");
				//System.err.println("wait for " + DELAYSECONDS + " sec before the next debug message appear!!!!"); 
				
				String strEmailOriginator = null;
				String[] arrEmailRecipients = null;
				String[] arrTriggerEmailRecipients = null;
				String[] arrFinishEmailRecipients = null;

				ResultSet rsValidTaskAlertResultSet = getValidTaskAlerts();
				

				while(rsValidTaskAlertResultSet.next())
				{
					//reset arrTriggerEmailRecipients and arrFinishEmailRecipients to null
					arrTriggerEmailRecipients = null;
					arrFinishEmailRecipients = null;
					
					//get all the data
					String strWorkflowTaskKey = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_intTaskKey");
					String strWorkflowTaskName = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strName");


					//start: replace the tag in workflow task name
					StringBuffer strBufTaskName = new StringBuffer();
		
					//get the task name
					strBufTaskName.insert(0, strWorkflowTaskName); 

					//get all the runtime data from ix_runtimedata that has workflow instance key = strWorkflowInstanceKey
					DALSecurityQuery query = new DALSecurityQuery();
					//String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strWorkflowTaskKey, query);
					String strWorkflowInstanceKey = rsValidTaskAlertResultSet.getString("WORKFLOW_INSTANCE_intWorkflowInstanceKey");
					ResultSet rsRuntimeData = WorkflowManager.getRuntimeData(strWorkflowInstanceKey, query);

					//while loop
					//get the parameter name 
					//construct the parameter name to be like ***Parameter Name***
					//call the Utilities method and place the modified parameter name and its corresponding value, 
					//do this for task name
					//end loop
					while(rsRuntimeData.next())
					{
						//construct the tag like <TAG/>
						String strTag = "<" + rsRuntimeData.getString("WORKFLOW_RUNTIMEDATA_strParameterName") + "/>";
						String strValue = rsRuntimeData.getString("WORKFLOW_RUNTIMEDATA_strParameterValue");

						Utilities.substrReplace(strBufTaskName, strTag, strValue);
					}
					//end: replace the tag in workflow task name

					strWorkflowTaskName = strBufTaskName.toString();

					String strWorkflowTaskPerformer = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_intPerformer");
					String strWorkflowTaskPerformerType = 
											rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strPerformerType");
					String strWorkflowTaskTriggerUnit = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strTriggerUnit");
					String strWorkflowTaskTriggerValue = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_flTriggerValue");
					String strWorkflowTaskFinishedUnit = 
											rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strFinishedUnit");
					String strWorkflowTaskFinishedValue = 
											rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_flFinishedValue");
					String strWorkflowTaskType = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strType");
					String strWorkflowTaskStatus = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strStatus");
					String strWorkflowTaskDateReceived = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_dtDateReceived");
					String strWorkflowTaskDateCompleted = 
											rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_dtDateCompleted");
					String strWorkflowTaskTimeReceived = 
											rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strTimeReceived");
					String strWorkflowTaskTimeCompleted = 
											rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strTimeCompleted");
					String strWorkflowTaskWorkingDays = rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_intWorkingDays");

					String strWorkflowTaskSendTriggerAlertToPerformer = 
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strSendTriggerAlertToPerformer");
					String strWorkflowTaskSendTriggerAlertToOtherKey = 
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_intSendTriggerAlertToOtherKey");
					String strWorkflowTaskSendTriggerAlertToOtherType = 
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strSendTriggerAlertToOtherType");
					String strWorkflowTaskRecurringTriggerAlert =
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strRecurringTriggerAlert");
					String strWorkflowTaskRecurringTriggerAlertUnit =
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strRecurringTriggerAlertUnit");
					String strWorkflowTaskRecurringTriggerAlertValue =
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_flRecurringTriggerAlertValue");

					if(strWorkflowTaskRecurringTriggerAlertValue == null)
					{
						strWorkflowTaskRecurringTriggerAlertValue = "0";
					}

					String strWorkflowTaskSendFinishAlertToPerformer = 
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strSendFinishAlertToPerformer");
					String strWorkflowTaskSendFinishAlertToOtherKey = 
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_intSendFinishAlertToOtherKey");
					String strWorkflowTaskSendFinishAlertToOtherType = 
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strSendFinishAlertToOtherType");
					String strWorkflowTaskRecurringFinishAlert =
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strRecurringFinishAlert");
					String strWorkflowTaskRecurringFinishAlertUnit =
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_strRecurringFinishAlertUnit");
					String strWorkflowTaskRecurringFinishAlertValue =
										rsValidTaskAlertResultSet.getString("WORKFLOW_TASK_flRecurringFinishAlertValue");

					if(strWorkflowTaskRecurringFinishAlertValue == null)
					{
						strWorkflowTaskRecurringFinishAlertValue = "0";
					}

					//String strWorkflowInstanceKey = rsValidTaskAlertResultSet.getString("WORKFLOW_INSTANCE_intWorkflowInstanceKey");
					String strWorkflowInstanceName = rsValidTaskAlertResultSet.getString("WORKFLOW_INSTANCE_strName");
					String strWorkflowInstanceTimeCreated = rsValidTaskAlertResultSet.getString("WORKFLOW_INSTANCE_strTimeCreated");
					String strWorkflowInstanceDateCreated = rsValidTaskAlertResultSet.getString("WORKFLOW_INSTANCE_dtDateCreated");

					//getting the performer title,first name,last name, and type
					String strPerformerTitle = "";
				    String strPerformerFirstName = "";
				    String strPerformerLastName = "";
				    String strPerformerType	= "";

					if(strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_HUMAN))
					{
						if(strWorkflowTaskPerformerType.equals(WorkflowManager.PERFORMER_TYPE_USER))
						{
							//System.err.println("strWorkflowTaskPerformer = >>>" + strWorkflowTaskPerformer + "<<<");

							ResultSet rsUserDetails = WorkflowManager.getOrgUserDetails(strWorkflowTaskPerformer);
							if(rsUserDetails.first())
							{
								strPerformerTitle = rsUserDetails.getString("ORGUSER_strTitle");
								strPerformerFirstName = rsUserDetails.getString("ORGUSER_strFirstName");
								strPerformerLastName = rsUserDetails.getString("ORGUSER_strLastName");
							}
							strPerformerType = WorkflowManager.PERFORMER_TYPE_USER;
						}
						else if(strWorkflowTaskPerformerType.equals(WorkflowManager.PERFORMER_TYPE_GROUP))
						{
							ResultSet rsGroupDetails = WorkflowManager.getOrgGroupDetails(strWorkflowTaskPerformer);
							if(rsGroupDetails.first())
							{
								strPerformerTitle = rsGroupDetails.getString("ORGGROUP_strOrgGroupName");
							}
							strPerformerType = WorkflowManager.PERFORMER_TYPE_GROUP;
						}
					}
					else if(strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM))
					{
						strPerformerType = WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM;
					}
					else if(strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW))
					{
						strPerformerType = WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW;
					}
					
					/*
					System.err.println("strWorkflowInstanceDateCreated = " + strWorkflowInstanceDateCreated);
					System.err.println("strWorkflowInstanceTimeCreated = " + strWorkflowInstanceTimeCreated);

					System.err.println("strWorkflowTaskDateReceived = " + strWorkflowTaskDateReceived);
					System.err.println("strWorkflowTaskTimeReceived = " + strWorkflowTaskTimeReceived);
					*/


					strEmailOriginator = ALERT_ENGINE_EMAIL;
					String strPerformerEmailAddress = null;
					String[] arrPerformerEmailAddresses = null;
					String strOtherTriggerEmailAddress = null;
					String[] arrOtherTriggerEmailRecipients = null;
					String strOtherFinishEmailAddress = null;
					String[] arrOtherFinishEmailRecipients = null;

					//get the performer email and the other emails
					if((strWorkflowTaskSendTriggerAlertToPerformer != null &&
						strWorkflowTaskSendTriggerAlertToPerformer.equals("yes")) ||
					   (strWorkflowTaskSendFinishAlertToPerformer != null &&
						strWorkflowTaskSendFinishAlertToPerformer.equals("yes")))
					{
						if(strWorkflowTaskPerformerType.equals("User"))
						{
							strPerformerEmailAddress = getUserEmailAddress(strWorkflowTaskPerformer);
						}
						else if(strWorkflowTaskPerformerType.equals("Group"))
						{
							arrPerformerEmailAddresses = getUsersEmailAddresses(strWorkflowTaskPerformer);
						}
					}
				
					//check the other trigger user is of type user or group
					if(strWorkflowTaskSendTriggerAlertToOtherType != null &&
					   strWorkflowTaskSendTriggerAlertToOtherType.equals("User"))
					{
						strOtherTriggerEmailAddress = getUserEmailAddress(strWorkflowTaskSendTriggerAlertToOtherKey);
					}
					else if(strWorkflowTaskSendTriggerAlertToOtherType != null &&
							strWorkflowTaskSendTriggerAlertToOtherType.equals("Group"))
					{
						arrOtherTriggerEmailRecipients = getUsersEmailAddresses(strWorkflowTaskSendTriggerAlertToOtherKey);
					}

					//check the other finish user is of type user or group
					if(strWorkflowTaskSendFinishAlertToOtherType != null &&
					   strWorkflowTaskSendFinishAlertToOtherType.equals("User"))
					{
						strOtherFinishEmailAddress = getUserEmailAddress(strWorkflowTaskSendFinishAlertToOtherKey);
					}
					else if(strWorkflowTaskSendFinishAlertToOtherType != null &&
							strWorkflowTaskSendFinishAlertToOtherType.equals("Group"))
					{
						arrOtherFinishEmailRecipients = getUsersEmailAddresses(strWorkflowTaskSendFinishAlertToOtherKey);
					}

					//TRIGGER
					//performer need to be notified
					if(strWorkflowTaskSendTriggerAlertToPerformer != null &&
					   strWorkflowTaskSendTriggerAlertToPerformer.equals("yes"))
					{
						//the other user is of type user
						if(strWorkflowTaskSendTriggerAlertToOtherType != null &&
						   strWorkflowTaskSendTriggerAlertToOtherType.equals("User"))
						{
							if(strWorkflowTaskPerformerType.equals("User"))
							{
								arrTriggerEmailRecipients = new String[2];
								arrTriggerEmailRecipients[0] = strPerformerEmailAddress;
								arrTriggerEmailRecipients[1] = strOtherTriggerEmailAddress;
							}
							else if(strWorkflowTaskPerformerType.equals("Group"))
							{
								arrTriggerEmailRecipients = new String[arrPerformerEmailAddresses.length + 1];
								for(int intIndex = 0; intIndex < arrPerformerEmailAddresses.length; intIndex++)
								{
									arrTriggerEmailRecipients[intIndex] = arrPerformerEmailAddresses[intIndex];
								}
								arrTriggerEmailRecipients[arrPerformerEmailAddresses.length] = strOtherTriggerEmailAddress;
							}
						}
						//the other user is of type group 
						else if(strWorkflowTaskSendTriggerAlertToOtherType != null &&
								strWorkflowTaskSendTriggerAlertToOtherType.equals("Group"))
						{
							if(strWorkflowTaskPerformerType.equals("User"))
							{
								arrTriggerEmailRecipients = new String[arrOtherTriggerEmailRecipients.length + 1];
								for(int intIndex = 0; intIndex < arrOtherTriggerEmailRecipients.length; intIndex++)
								{
									arrTriggerEmailRecipients[intIndex] = arrOtherTriggerEmailRecipients[intIndex];
								}
								arrTriggerEmailRecipients[arrOtherTriggerEmailRecipients.length] = strPerformerEmailAddress;
							}
							else if(strWorkflowTaskPerformerType.equals("Group"))
							{
								arrTriggerEmailRecipients = new String[arrPerformerEmailAddresses.length +
																	   arrOtherTriggerEmailRecipients.length];
								int intIndex = 0;
								for(intIndex = 0; intIndex < arrPerformerEmailAddresses.length; intIndex++)
								{
									arrTriggerEmailRecipients[intIndex] = arrPerformerEmailAddresses[intIndex];
								}
								for(int intIndex1 = 0; intIndex1 < arrOtherTriggerEmailRecipients.length;
										intIndex1++, intIndex++)
								{
									arrTriggerEmailRecipients[intIndex] = arrOtherTriggerEmailRecipients[intIndex1];
								}
							}
						}
						else
						{
							if(strWorkflowTaskPerformerType.equals("User"))
							{
								arrTriggerEmailRecipients = new String[1];
								arrTriggerEmailRecipients[0] = strPerformerEmailAddress;
							}
							else if(strWorkflowTaskPerformerType.equals("Group"))
							{
								arrTriggerEmailRecipients = arrPerformerEmailAddresses;
							}
						}
					}
					//performer doesn't need to be notified
					else if(strWorkflowTaskSendTriggerAlertToPerformer != null &&
							strWorkflowTaskSendTriggerAlertToPerformer.equals("no"))
					{
						//the other user is of type user
						if(strWorkflowTaskSendTriggerAlertToOtherType != null &&
						   strWorkflowTaskSendTriggerAlertToOtherType.equals("User"))
						{
							arrTriggerEmailRecipients = new String[1];
							arrTriggerEmailRecipients[0] = strOtherTriggerEmailAddress;
						}
						//the other user is of type group
						else if(strWorkflowTaskSendTriggerAlertToOtherType != null &&
								strWorkflowTaskSendTriggerAlertToOtherType.equals("Group"))
						{
							arrTriggerEmailRecipients = arrOtherTriggerEmailRecipients;
						}
					}


					//FINISH
					//performer need to be notified
					if(strWorkflowTaskSendFinishAlertToPerformer != null &&
					   strWorkflowTaskSendFinishAlertToPerformer.equals("yes"))
					{
						//the other user is of type user
						if(strWorkflowTaskSendFinishAlertToOtherType != null &&
						   strWorkflowTaskSendFinishAlertToOtherType.equals("User"))
						{
							if(strWorkflowTaskPerformerType.equals("User"))
							{
								arrFinishEmailRecipients = new String[2];
								arrFinishEmailRecipients[0] = strPerformerEmailAddress;
								arrFinishEmailRecipients[1] = strOtherFinishEmailAddress;
							}
							else if(strWorkflowTaskPerformerType.equals("Group"))
							{
								arrFinishEmailRecipients = new String[arrPerformerEmailAddresses.length + 1];
								for(int intIndex = 0; intIndex < arrPerformerEmailAddresses.length; intIndex++)
								{
									arrFinishEmailRecipients[intIndex] = arrPerformerEmailAddresses[intIndex];
								}
								arrFinishEmailRecipients[arrPerformerEmailAddresses.length] = strOtherFinishEmailAddress;
							}
						}
						//the other user is of type group 
						else if(strWorkflowTaskSendFinishAlertToOtherType != null &&
								strWorkflowTaskSendFinishAlertToOtherType.equals("Group"))
						{
							if(strWorkflowTaskPerformerType.equals("User"))
							{
								arrFinishEmailRecipients = new String[arrOtherFinishEmailRecipients.length + 1];
								for(int intIndex = 0; intIndex < arrOtherFinishEmailRecipients.length; intIndex++)
								{
									arrFinishEmailRecipients[intIndex] = arrOtherFinishEmailRecipients[intIndex];
								}
								arrFinishEmailRecipients[arrOtherFinishEmailRecipients.length] = strPerformerEmailAddress;
							}
							else if(strWorkflowTaskPerformerType.equals("Group"))
							{
								arrFinishEmailRecipients = new String[arrPerformerEmailAddresses.length +
																	   arrOtherFinishEmailRecipients.length];
								int intIndex = 0;
								for(intIndex = 0; intIndex < arrPerformerEmailAddresses.length; intIndex++)
								{
									arrFinishEmailRecipients[intIndex] = arrPerformerEmailAddresses[intIndex];
								}
								for(int intIndex1 = 0; intIndex1 < arrOtherFinishEmailRecipients.length;
										intIndex1++, intIndex++)
								{
									arrFinishEmailRecipients[intIndex] = arrOtherFinishEmailRecipients[intIndex1];
								}
							}
						}
						else
						{
							if(strWorkflowTaskPerformerType.equals("User"))
							{
								arrFinishEmailRecipients = new String[1];
								arrFinishEmailRecipients[0] = strPerformerEmailAddress;
							}
							else if(strWorkflowTaskPerformerType.equals("Group"))
							{
								arrFinishEmailRecipients = arrPerformerEmailAddresses;
							}
						}
					}
					//performer doesn't need to be notified
					else if(strWorkflowTaskSendFinishAlertToPerformer != null &&
							strWorkflowTaskSendFinishAlertToPerformer.equals("no"))
					{
						//the other user is of type user
						if(strWorkflowTaskSendFinishAlertToOtherType != null &&
						   strWorkflowTaskSendFinishAlertToOtherType.equals("User"))
						{
							arrFinishEmailRecipients = new String[1];
							arrFinishEmailRecipients[0] = strOtherFinishEmailAddress;
						}
						//the other user is of type group
						else if(strWorkflowTaskSendFinishAlertToOtherType != null &&
								strWorkflowTaskSendFinishAlertToOtherType.equals("Group"))
						{
							arrFinishEmailRecipients = arrOtherFinishEmailRecipients;
						}
					}

					if(strWorkflowTaskStatus.equals("Not active") || strWorkflowTaskStatus.equals("Active"))
					{

//System.err.println("strWorkflowTaskStatus = " + strWorkflowTaskStatus + "<<<");

						//check for trigger alert 
						if(passDueDateAndTime(strWorkflowTaskWorkingDays,
											  strWorkflowTaskTriggerUnit,
											  strWorkflowTaskTriggerValue,
											  strWorkflowInstanceDateCreated,
											  strWorkflowInstanceTimeCreated))
						{
//System.err.println("trigger");
							if(hasAlertBeenSent("Trigger", strWorkflowTaskKey))
							{
								if(strWorkflowTaskRecurringTriggerAlert.equals("yes"))
								{
									//get the time and date for the last alert sent for this task
									long longTimestampOfLatestAlert = getTimestampOfLatestAlert(strWorkflowTaskKey,
																								"Trigger");
									
									//add the above time with the interval time
									long longIntervalInMilliseconds = 
											getMilliSeconds(strWorkflowTaskRecurringTriggerAlertUnit, 
															strWorkflowTaskRecurringTriggerAlertValue);

									long longDueTime = longTimestampOfLatestAlert + longIntervalInMilliseconds;

									//current time
									Calendar calendar = Calendar.getInstance();
									
									//if the current time is > than the newly added time 
									//then create the alert and send the email
									if(calendar.getTimeInMillis() > longDueTime)
									{
										//create an entry in ix_alert table for this task
										String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName +
															" in the workflow " + strWorkflowInstanceName + 
															" has not been started.";
										String strEmailTemplate = getEmailTemplate("TASK_NOT_STARTED");
										String strReminderTime = strWorkflowTaskRecurringTriggerAlertValue + " " + 
																 strWorkflowTaskRecurringTriggerAlertUnit + "(s)";
										String strMessageBody = getMessageBody(strEmailTemplate, strWorkflowTaskName,
																			   strWorkflowInstanceName, 
																			   strWorkflowInstanceKey, strReminderTime,
																			   strPerformerTitle, strPerformerFirstName,
																			   strPerformerLastName, strPerformerType);
				
										String strPerformerAlertKey = null;
										if(strWorkflowTaskSendTriggerAlertToPerformer.equals("yes"))
										{
											createAlert(strSubject, strMessageBody, strWorkflowTaskPerformer, 
														strWorkflowTaskPerformerType, "Trigger", strWorkflowTaskKey);
											strPerformerAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
										}
										
										String strOtherAlertKey = null; 
										if(strWorkflowTaskSendTriggerAlertToOtherKey != null &&
										   !(strWorkflowTaskSendTriggerAlertToOtherKey.equals("")))
										{
											createAlert(strSubject, strMessageBody, 
														strWorkflowTaskSendTriggerAlertToOtherKey, 
														strWorkflowTaskSendTriggerAlertToOtherType, 
														"Trigger", strWorkflowTaskKey);
											strOtherAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
										}

										//send email
										if(arrTriggerEmailRecipients != null)
										{
											email.clearEmail(); 
											email.setUsername(EMAIL_USERNAME);
											email.setPassword(EMAIL_PASSWORD);
											email.setSMTPServer(SMTP_SERVER);              
											email.setFrom(strEmailOriginator);
											email.setRecipientsTo(arrTriggerEmailRecipients);
											email.setSubject(strSubject); 
											email.setMessage(strMessageBody); 
											email.setSentDate(new Date());
											
					//System.err.println("strEmailOriginator = >>>" + strEmailOriginator + "<<<");
					//System.err.println("arrTriggerEmailRecipients[0] = >>>" + arrTriggerEmailRecipients[0] + "<<<");
											
											if(SEND_EMAIL.equals("yes"))
											{
												try
												{
													email.sendEmail();
												}
												catch(Exception e)
												{
													e.printStackTrace();
													LogService.log(LogService.ERROR, "Exception while executing send email for task that has not been started, status is active/not active (recurring alert) - run " + e.toString());

													// update the email sent of the alert to "fail"
													updateAlertEmailSent(strPerformerAlertKey, "fail");
													updateAlertEmailSent(strOtherAlertKey, "fail");
												}
											}
										}
									}
								}
							}
							else
							{
								//create an entry in ix_alert table for this task
								String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName +
													" in the workflow " + strWorkflowInstanceName + 
													" has not been started.";
								String strEmailTemplate = getEmailTemplate("TASK_NOT_STARTED");
								String strReminderTime = strWorkflowTaskRecurringTriggerAlertValue + " " + 
														 strWorkflowTaskRecurringTriggerAlertUnit + "(s)";
								String strMessageBody = getMessageBody(strEmailTemplate, strWorkflowTaskName,
																	   strWorkflowInstanceName, 
																	   strWorkflowInstanceKey, strReminderTime, 
																	   strPerformerTitle, strPerformerFirstName, 
																	   strPerformerLastName, strPerformerType);
								
								String strPerformerAlertKey = null;
								if(strWorkflowTaskSendTriggerAlertToPerformer.equals("yes"))
								{
									createAlert(strSubject, strMessageBody, strWorkflowTaskPerformer, 
												strWorkflowTaskPerformerType, "Trigger", strWorkflowTaskKey);
									strPerformerAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
								}

								String strOtherAlertKey = null; 
								if(strWorkflowTaskSendTriggerAlertToOtherKey != null &&
								   !(strWorkflowTaskSendTriggerAlertToOtherKey.equals("")))
								{
									createAlert(strSubject, strMessageBody, 
												strWorkflowTaskSendTriggerAlertToOtherKey, 
												strWorkflowTaskSendTriggerAlertToOtherType, 
												"Trigger", strWorkflowTaskKey);
									strOtherAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
								}

								//send email
								if(arrTriggerEmailRecipients != null)
								{
									email.clearEmail(); 
									email.setUsername(EMAIL_USERNAME);
									email.setPassword(EMAIL_PASSWORD);
									email.setSMTPServer(SMTP_SERVER);              
									email.setFrom(strEmailOriginator);
									email.setRecipientsTo(arrTriggerEmailRecipients);
									email.setSubject(strSubject); 
									email.setMessage(strMessageBody); 
									email.setSentDate(new Date());
									if(SEND_EMAIL.equals("yes"))
									{
										try
										{
											email.sendEmail();
										}
										catch(Exception e)
										{
											e.printStackTrace();
											LogService.log(LogService.ERROR, "Exception while executing send email for task that has not been started, status is active/not active (1st time alert) - run " + e.toString());

											// update the email sent of the alert to "fail"
											updateAlertEmailSent(strPerformerAlertKey, "fail");
											updateAlertEmailSent(strOtherAlertKey, "fail");
										}
									}
								}
							}
						}

						//check for finish alert 
						if(passDueDateAndTime(strWorkflowTaskWorkingDays,
											  strWorkflowTaskFinishedUnit,
											  strWorkflowTaskFinishedValue,
											  strWorkflowTaskDateReceived,
											  strWorkflowTaskTimeReceived))
						{
//System.err.println("finish");
							if(hasAlertBeenSent("Finish", strWorkflowTaskKey))
							{
								if(strWorkflowTaskRecurringFinishAlert.equals("yes"))
								{
									//get the time and date for the last alert sent for this task
									long longTimestampOfLatestAlert = getTimestampOfLatestAlert(strWorkflowTaskKey,
																								"Finish");
									
									//add the above time with the interval time
									long longIntervalInMilliseconds = 
											getMilliSeconds(strWorkflowTaskRecurringFinishAlertUnit, 
															strWorkflowTaskRecurringFinishAlertValue);

									long longDueTime = longTimestampOfLatestAlert + longIntervalInMilliseconds;

									//current time
									Calendar calendar = Calendar.getInstance();
									
									//if the current time is > than the newly added time 
									//then create the alert and send the email
									if(calendar.getTimeInMillis() > longDueTime)
									{
										//create an entry in ix_alert table for this task
										String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName +
															" in the workflow " + strWorkflowInstanceName + 
															" has not been completed.";
										String strEmailTemplate = getEmailTemplate("TASK_NOT_COMPLETED");
										String strReminderTime = strWorkflowTaskRecurringFinishAlertValue + " " + 
																 strWorkflowTaskRecurringFinishAlertUnit + "(s)";
										String strMessageBody = getMessageBody(strEmailTemplate, strWorkflowTaskName,
																			   strWorkflowInstanceName, 
																			   strWorkflowInstanceKey, strReminderTime, 
																			   strPerformerTitle, strPerformerFirstName,
																			   strPerformerLastName, strPerformerType);
				
										String strPerformerAlertKey = null;
										if(strWorkflowTaskSendFinishAlertToPerformer.equals("yes"))
										{
											createAlert(strSubject, strMessageBody, strWorkflowTaskPerformer, 
														strWorkflowTaskPerformerType, "Finish", strWorkflowTaskKey);
											strPerformerAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
										}

										String strOtherAlertKey = null; 
										if(strWorkflowTaskSendFinishAlertToOtherKey != null &&
										   !(strWorkflowTaskSendFinishAlertToOtherKey.equals("")))
										{
											createAlert(strSubject, strMessageBody, 
														strWorkflowTaskSendFinishAlertToOtherKey, 
														strWorkflowTaskSendFinishAlertToOtherType, 
														"Finish", strWorkflowTaskKey);
											strOtherAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
										}

										//send email
										if(arrFinishEmailRecipients != null)
										{
											email.clearEmail(); 
											email.setUsername(EMAIL_USERNAME);
											email.setPassword(EMAIL_PASSWORD);
											email.setSMTPServer(SMTP_SERVER);              
											email.setFrom(strEmailOriginator);
											email.setRecipientsTo(arrFinishEmailRecipients);
											email.setSubject(strSubject);
											email.setMessage(strMessageBody); 
											email.setSentDate(new Date());

											if(SEND_EMAIL.equals("yes"))
											{
												try
												{
													email.sendEmail();
												}
												catch(Exception e)
												{
													e.printStackTrace();
													LogService.log(LogService.ERROR, "Exception while executing send email for task that has not completed, status is active/not active (recurring alert) - run " + e.toString());

													// update the email sent of the alert to "fail"
													updateAlertEmailSent(strPerformerAlertKey, "fail");
													updateAlertEmailSent(strOtherAlertKey, "fail");
												}
											}
										}
									}
								}
							}
							else
							{
//System.err.println("has not been sent");
								//create an entry in ix_alert table for this task
								String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName +
													" in the workflow " + strWorkflowInstanceName + 
													" has not been completed.";
								String strEmailTemplate = getEmailTemplate("TASK_NOT_COMPLETED");
								String strReminderTime = strWorkflowTaskRecurringFinishAlertValue + " " + 
														 strWorkflowTaskRecurringFinishAlertUnit + "(s)";
								String strMessageBody = getMessageBody(strEmailTemplate, strWorkflowTaskName,
																	   strWorkflowInstanceName, 
																	   strWorkflowInstanceKey, strReminderTime, 
																	   strPerformerTitle, strPerformerFirstName,
																	   strPerformerLastName, strPerformerType);
								
								String strPerformerAlertKey = null;
								if(strWorkflowTaskSendFinishAlertToPerformer.equals("yes"))
								{
									createAlert(strSubject, strMessageBody, strWorkflowTaskPerformer, 
												strWorkflowTaskPerformerType, "Finish", strWorkflowTaskKey);
									strPerformerAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
								}

								String strOtherAlertKey = null;
								if(strWorkflowTaskSendFinishAlertToOtherKey != null &&
								   !(strWorkflowTaskSendFinishAlertToOtherKey.equals("")))
								{
									createAlert(strSubject, strMessageBody, 
												strWorkflowTaskSendFinishAlertToOtherKey, 
												strWorkflowTaskSendFinishAlertToOtherType, 
												"Finish", strWorkflowTaskKey);
									strOtherAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
								}

//System.err.println("arrFinishEmailRecipients = >>>" + arrFinishEmailRecipients + "<<<");

								//send email
								if(arrFinishEmailRecipients != null)
								{
									email.clearEmail(); 
									email.setUsername(EMAIL_USERNAME);
									email.setPassword(EMAIL_PASSWORD);
									email.setSMTPServer(SMTP_SERVER);              
									email.setFrom(strEmailOriginator);
									email.setRecipientsTo(arrFinishEmailRecipients);
									email.setSubject(strSubject);
									email.setMessage(strMessageBody); 
									email.setSentDate(new Date());

//System.err.println("SEND_EMAIL = >>>" + SEND_EMAIL + "<<<");

									if(SEND_EMAIL.equals("yes"))
									{
										try
										{
//System.err.println("about to send email");
											email.sendEmail();
										}
										catch(Exception e)
										{
											e.printStackTrace();
											LogService.log(LogService.ERROR, "Exception while executing send email for task that has not completed, status is active/not active (1st time alert) - run " + e.toString());

											// update the email sent of the alert to "fail"
											updateAlertEmailSent(strPerformerAlertKey, "fail");
											updateAlertEmailSent(strOtherAlertKey, "fail");
										}
									}
								}
							}
						}
					}
					else if(strWorkflowTaskStatus.equals("Started"))
					{
						//check for finish alert 
						if(passDueDateAndTime(strWorkflowTaskWorkingDays,
											  strWorkflowTaskFinishedUnit,
											  strWorkflowTaskFinishedValue,
											  strWorkflowTaskDateReceived,
											  strWorkflowTaskTimeReceived))
						{
							if(hasAlertBeenSent("Finish", strWorkflowTaskKey))
							{
								if(strWorkflowTaskRecurringFinishAlert.equals("yes"))
								{
									//get the time and date for the last alert sent for this task
									long longTimestampOfLatestAlert = getTimestampOfLatestAlert(strWorkflowTaskKey,
																								"Finish");
									
									//add the above time with the interval time
									long longIntervalInMilliseconds = 
											getMilliSeconds(strWorkflowTaskRecurringFinishAlertUnit, 
															strWorkflowTaskRecurringFinishAlertValue);

									long longDueTime = longTimestampOfLatestAlert + longIntervalInMilliseconds;

									//current time
									Calendar calendar = Calendar.getInstance();
									
									//if the current time is > than the newly added time 
									//then create the alert and send the email
									if(calendar.getTimeInMillis() > longDueTime)
									{
										//create an entry in ix_alert table for this task
										String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName +
															" in the workflow " + strWorkflowInstanceName + 
															" has not been completed.";
										String strEmailTemplate = getEmailTemplate("TASK_NOT_COMPLETED");
										String strReminderTime = strWorkflowTaskRecurringFinishAlertValue + " " + 
																 strWorkflowTaskRecurringFinishAlertUnit + "(s)";
										String strMessageBody = getMessageBody(strEmailTemplate, strWorkflowTaskName,
																			   strWorkflowInstanceName, 
																			   strWorkflowInstanceKey, strReminderTime, 
																			   strPerformerTitle, strPerformerFirstName, 
																			   strPerformerLastName, strPerformerType);
				
										String strPerformerAlertKey = null;
										if(strWorkflowTaskSendFinishAlertToPerformer.equals("yes"))
										{
											createAlert(strSubject, strMessageBody, strWorkflowTaskPerformer, 
														strWorkflowTaskPerformerType, "Finish", strWorkflowTaskKey);
											strPerformerAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
										}

										String strOtherAlertKey = null; 
										if(strWorkflowTaskSendFinishAlertToOtherKey != null &&
										   !(strWorkflowTaskSendFinishAlertToOtherKey.equals("")))
										{
											createAlert(strSubject, strMessageBody, 
														strWorkflowTaskSendFinishAlertToOtherKey, 
														strWorkflowTaskSendFinishAlertToOtherType, 
														"Finish", strWorkflowTaskKey);
											strOtherAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
										}

										//send email
										if(arrFinishEmailRecipients != null)
										{
											email.clearEmail(); 
											email.setUsername(EMAIL_USERNAME);
											email.setPassword(EMAIL_PASSWORD);
											email.setSMTPServer(SMTP_SERVER);              
											email.setFrom(strEmailOriginator);
											email.setRecipientsTo(arrFinishEmailRecipients);
											email.setSubject(strSubject);
											email.setMessage(strMessageBody); 
											email.setSentDate(new Date());
											if(SEND_EMAIL.equals("yes"))
											{
												try
												{
													email.sendEmail();
												}
												catch(Exception e)
												{
													e.printStackTrace();
													LogService.log(LogService.ERROR, "Exception while executing send email for task that has not completed, status is started (recurring alert) - run " + e.toString());

													// update the email sent of the alert to "fail"
													updateAlertEmailSent(strPerformerAlertKey, "fail");
													updateAlertEmailSent(strOtherAlertKey, "fail");
												}
											}
										}
									}
								}
							}
							else
							{
								//create an entry in ix_alert table for this task
								String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName +
													" in the workflow " + strWorkflowInstanceName + 
													" has not been completed.";
								String strEmailTemplate = getEmailTemplate("TASK_NOT_COMPLETED");
								String strReminderTime = strWorkflowTaskRecurringFinishAlertValue + " " + 
														 strWorkflowTaskRecurringFinishAlertUnit + "(s)";
								String strMessageBody = getMessageBody(strEmailTemplate, strWorkflowTaskName,
																	   strWorkflowInstanceName, 
																	   strWorkflowInstanceKey, strReminderTime, 
																	   strPerformerTitle, strPerformerFirstName, 
																	   strPerformerLastName, strPerformerType);
								
								String strPerformerAlertKey = null;
								if(strWorkflowTaskSendFinishAlertToPerformer.equals("yes"))
								{
									createAlert(strSubject, strMessageBody, strWorkflowTaskPerformer, 
												strWorkflowTaskPerformerType, "Finish", strWorkflowTaskKey);
									strPerformerAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
								}

								String strOtherAlertKey = null; 
								if(strWorkflowTaskSendFinishAlertToOtherKey != null &&
								   !(strWorkflowTaskSendFinishAlertToOtherKey.equals("")))
								{
									createAlert(strSubject, strMessageBody, 
												strWorkflowTaskSendFinishAlertToOtherKey, 
												strWorkflowTaskSendFinishAlertToOtherType, 
												"Finish", strWorkflowTaskKey);
									strOtherAlertKey = QueryChannel.getNewestKeyAsString("ALERT_intAlertKey");
								}	
								
								//send email
								if(arrFinishEmailRecipients != null)
								{
									email.clearEmail(); 
									email.setUsername(EMAIL_USERNAME);
									email.setPassword(EMAIL_PASSWORD);
									email.setSMTPServer(SMTP_SERVER);              
									email.setFrom(strEmailOriginator);
									email.setRecipientsTo(arrFinishEmailRecipients);
									email.setSubject(strSubject);
									email.setMessage(strMessageBody); 
									email.setSentDate(new Date());
									if(SEND_EMAIL.equals("yes"))
									{
										try
										{
											email.sendEmail();
										}
										catch(Exception e)
										{
											e.printStackTrace();
											LogService.log(LogService.ERROR, "Exception while executing send email for task that has not completed, status is started (1st time alert) - run " + e.toString());

											// update the email sent of the alert to "fail"
											updateAlertEmailSent(strPerformerAlertKey, "fail");
											updateAlertEmailSent(strOtherAlertKey, "fail");
										}
									}
								}
							}
						}
					}
				}

				ResultSet rsValidAlertResultSet = getValidAlerts();
		
				while(rsValidAlertResultSet.next())
				{
					String strAlertKey = rsValidAlertResultSet.getString("ALERT_intAlertKey"); 
					String strOriginUserKey = rsValidAlertResultSet.getString("ALERT_intOriginUserKey"); 
					String strTargetUserKey = rsValidAlertResultSet.getString("ALERT_intTargetUserKey"); 
					String strTargetUserType = rsValidAlertResultSet.getString("ALERT_strTargetUserType");
					String strTaskKey = rsValidAlertResultSet.getString("ALERT_intTaskKey");

					String strName = rsValidAlertResultSet.getString("ALERT_strName");
					String strMessage = rsValidAlertResultSet.getString("ALERT_strDescription");

					strEmailOriginator = getUserEmailAddress(strOriginUserKey);

					if(strTargetUserType.equals("Group"))
					{
						arrEmailRecipients = getUsersEmailAddresses(strTargetUserKey);
					}
					else if(strTargetUserType.equals("User"))
					{
						//since a user can only be assign to 1 email address
						arrEmailRecipients = new String[1];
						arrEmailRecipients[0] = getUserEmailAddress(strTargetUserKey);
					}

					String strSubject = "ALERT - " + strName;

					/*
					System.err.println("strEmailOriginator = >>>" + strEmailOriginator + "<<<");

					for(int i = 0; i < 2; i++)
					{
						System.err.println("arrEmailRecipients[" + i + "] = >>>" + arrEmailRecipients[i] + "<<<");
					}
					*/

					//System.err.println("SMTP Server = >>>" + SMTP_SERVER + "<<<");
					//String[] strAddress = {"aagustian@neuragenix.com"};
					//String strFrom = "aagustian@neuragenix.com";
					
					//do the checking whether to send email
					//table to be check are: ix_workflow_task, ix_alert
					
					//send email
					email.clearEmail(); 
					email.setUsername(EMAIL_USERNAME);
					email.setPassword(EMAIL_PASSWORD);
					email.setSMTPServer(SMTP_SERVER);              
					email.setFrom(strEmailOriginator);
					email.setRecipientsTo(arrEmailRecipients);
					email.setSubject(strSubject);
					email.setMessage(strMessage);    
					email.setSentDate(new Date());
					if(SEND_EMAIL.equals("yes"))
					{
						try
						{
							email.sendEmail();
						}
						catch(Exception e)
						{
							e.printStackTrace();
							LogService.log(LogService.ERROR, "Exception while executing send email for manual task:: run - " + e.toString());

							// update the email sent of the alert to "fail"
							updateAlertEmailSent(strAlertKey, "fail");
						}

						updateAlertStatus(strAlertKey, "Sent");
						updateAlertEmailSent(strAlertKey, "yes");
					}
				}

				// Update Process Table as Running
				try 
				{
					oProcessReg.setProcessType(oProcessReg.TYPE_WORKFLOW_EMAIL_ALERT);
					oProcessReg.setProcessStatus(oProcessReg.STATUS_IDLE);
					//current time
					Calendar calendar = Calendar.getInstance();
					long longDelayTime = getMilliSeconds("Second", String.valueOf(DELAYSECONDS)); 
					long longNextAlertCheck = calendar.getTimeInMillis() + longDelayTime;
					calendar.setTimeInMillis(longNextAlertCheck);
					Date date = calendar.getTime();
					boolean  blResult = oProcessReg.updateProcessItem(intProcessKey,
									    "Idle. Next scheduled processing at:" + date.toString() , "none");
					//System.err.println("WorkflowAlertProcess - updateProcessItem = >>>" + blResult + "<<<");
				} 
				catch(ProcessRegisterUnabletoUpdate e)
				{ 
					KEEP_PROCESS = false; 
				} 
				catch(ProcessRegisterNoProcessItem e)
				{ 
					KEEP_PROCESS = false;
				}
				catch(Exception e)
				{
					LogService.log(LogService.ERROR, "Unable to register a Job Process Entry.");                
				}


				// Set the delay time
				//DELAYSECONDS = Property.getPropertyAsInt("neuragenix.utils.WorkflowAlertProcess.DelaySeconds"); 
				//System.err.println("DELAYSECONDS = >>>" + DELAYSECONDS + "<<<");
				Thread.sleep(DELAYSECONDS*1000);
        	} // while KEEP_PROCESS
        }                    
        catch(InterruptedException e)
		{
			e.printStackTrace();
        	LogService.log(LogService.ERROR, "InterruptedException error::run - " + e.toString());                
        }
		catch(Error er)
		{
        	er.printStackTrace();
        	LogService.log(LogService.ERROR, "Error executing Workflow Alert Process::run - " + er.toString());
        } 
		catch(Exception e)
		{
        	e.printStackTrace();              
        	LogService.log(LogService.ERROR, "Unknown error in WorkflowAlertProcess::run -" + e.toString());                
        }
    }


    /*
     * Stop the process thread. 
     */
    public void stopProcess()
	{  
        try 
		{
            KEEP_PROCESS = false;     
            STARTED = false;
            
			// Update Process Table as terminated
            oProcessReg.setProcessType(oProcessReg.TYPE_WORKFLOW_EMAIL_ALERT);
            oProcessReg.setProcessStatus(oProcessReg.STATUS_TERMINATED);
            boolean  blResult = oProcessReg.updateProcessItem(intProcessKey,"Process terminated by user.","none");
        }
		catch(ProcessRegisterUnabletoUpdate e) 
		{
             LogService.log(LogService.ERROR, "Exception ProcessRegisterUnabletoUpdate" + e.toString());                
        }
        catch(Exception e)
		{
            LogService.log(LogService.ERROR, "Exception " + e.toString());   
        }        
    }

	/*
	 * get Valid Task Alerts
	 *
	 * @return the valid task alerts
	 */
	private ResultSet getValidTaskAlerts()
	{
		ResultSet rsResultSet = null;

		try
		{
			Vector vecWorkflowTaskFields = DatabaseSchema.getFormFields("workflow_task_details");
			vecWorkflowTaskFields.add("WORKFLOW_TASK_intTaskKey");
			Vector vecWorkflowInstanceFields = DatabaseSchema.getFormFields("workflow_instance_details");
			vecWorkflowInstanceFields.add("WORKFLOW_INSTANCE_intWorkflowInstanceKey");
			Vector vecWorkflowInstanceAndTaskFields = new Vector();
			vecWorkflowInstanceAndTaskFields.addAll(vecWorkflowTaskFields);
			vecWorkflowInstanceAndTaskFields.addAll(vecWorkflowInstanceFields);
			DALQuery query = new DALQuery();

			query.setDomain("WORKFLOW_INSTANCE", null, null, null);
			query.setDomain("WORKFLOW_TASK", "WORKFLOW_INSTANCE_intWorkflowInstanceKey",
							"WORKFLOW_TASK_intWorkflowInstanceKey", "LEFT JOIN");
            query.setFields(vecWorkflowInstanceAndTaskFields, null);
			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_strStatus", "=", "In progress", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 1, "WORKFLOW_TASK_strStatus", "=", "Not active", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("OR", 0, "WORKFLOW_TASK_strStatus", "=", "Active", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("OR", 0, "WORKFLOW_TASK_strStatus", "=", "Started", 1, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 1, "WORKFLOW_TASK_strType", "=", "Human", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("OR", 0, "WORKFLOW_TASK_strType", "=", "System", 1, DALQuery.WHERE_HAS_VALUE);
			//query.setWhere("AND", 1, "WORKFLOW_TASK_strTriggerUnit", "IS NOT NULL", "", 0, DALQuery.WHERE_HAS_NULL_VALUE);
			query.setWhere("AND", 1, "WORKFLOW_TASK_flTriggerValue", "IS NOT NULL", "IS NOT NULL", 0, DALQuery.WHERE_HAS_NULL_VALUE);
			//query.setWhere("AND", 0, "WORKFLOW_TASK_strFinishedUnit", "IS NOT NULL", "", 0, DALQuery.WHERE_HAS_NULL_VALUE);
			query.setWhere("OR", 0, "WORKFLOW_TASK_flFinishedValue", "IS NOT NULL", "IS NOT NULL", 1, DALQuery.WHERE_HAS_NULL_VALUE);
			//System.err.println("agustian query = >>>" + query.convertSelectQueryToString() + "<<<");
			rsResultSet = query.executeSelect();
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowAlertProcess::getValidTaskAlerts - " + e.toString(), e);
        }

		return rsResultSet;
	}

	/*
	 * get Valid Alerts
	 *
	 * @return the valid alerts
	 */
	private ResultSet getValidAlerts()
	{
		ResultSet rsResultSet = null;

		try
		{
			Vector vecAlertFields = DatabaseSchema.getFormFields("alert_details");
			DALQuery query = new DALQuery();

			query.setDomain("ALERT", null, null, null);
			query.setFields(vecAlertFields, null);
			query.setWhere(null, 0, "ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ALERT_strStatus", "=", "Not sent", 0, DALQuery.WHERE_HAS_VALUE);

			Calendar currentTime = Calendar.getInstance();
			String strToday = getDate(currentTime) + "/" + 
							  getMonth(currentTime) + "/" + 
							  currentTime.get(Calendar.YEAR);

			String strCurrentTime = currentTime.get(Calendar.HOUR_OF_DAY) + ":" +
									currentTime.get(Calendar.MINUTE) + ":" +
									currentTime.get(Calendar.SECOND);

//System.err.println("strToday = " + strToday);
//System.err.println("strCurrentTime = " + strCurrentTime);
			
			query.setWhere("AND", 0, "ALERT_dtDate", "<=", strToday, 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ALERT_strTime", "<=", strCurrentTime, 0, DALQuery.WHERE_HAS_VALUE);

			rsResultSet = query.executeSelect();
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowAlertProcess::getValidAlerts - " + e.toString(), e);
        }

		return rsResultSet;
	}
	
	/*
	 * get the user email address
	 *
	 * @param strUserKey	the alert sender
	 *
	 * @return the sender email address
	 */
	private String getUserEmailAddress(String strUserKey)
	{
		String strEmailAddress = null;

		try
		{
			DALQuery query = new DALQuery();

			query.setDomain("ORGUSER", null, null, null);
			query.setField("ORGUSER_strEmail", null);
			query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ORGUSER_intOrgUserKey", "=", strUserKey, 0, DALQuery.WHERE_HAS_VALUE);
			ResultSet rsResultSet = query.executeSelect();

			if(rsResultSet.next())
			{
				strEmailAddress = rsResultSet.getString("ORGUSER_strEmail");
			}

		}
		catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowAlertProcess::getUserEmailAddress - " + e.toString(), e);
        }

		return strEmailAddress;
	}

	/*
	 * get the users email addresses
	 *
	 * @param strGroupKey 	the group key that the alert sender belongs to
	 *
	 * @return the sender email address
	 */
	private String[] getUsersEmailAddresses(String strGroupKey)
	{
		String[] arrEmailAddresses =  null;

		try
		{
			DALQuery query = new DALQuery();

			query.setDomain("ORGUSER", null, null, null);
			query.setDomain("ORGUSERGROUP", "ORGUSERGROUP_intOrgUserKey",
							"ORGUSER_intOrgUserKey", "INNER JOIN");
			query.setField("ORGUSER_strEmail", null);
			query.setWhere(null, 0, "ORGUSERGROUP_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ORGUSERGROUP_intOrgGroupKey", "=", strGroupKey, 0, DALQuery.WHERE_HAS_VALUE);
			ResultSet rsResultSet = query.executeSelect();

			Vector vecResultSet = new Vector();
			while(rsResultSet.next())
			{
				vecResultSet.add(rsResultSet.getString("ORGUSER_strEmail"));
			}

			arrEmailAddresses = new String[vecResultSet.size()];
			for(int intIndex = 0; intIndex < vecResultSet.size(); intIndex++)
			{
				arrEmailAddresses[intIndex] = (String) vecResultSet.get(intIndex);
			}
		}
		catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowAlertProcess::getUsersEmailAddresses - " + e.toString(), e);
        }

		return arrEmailAddresses;
	}


	/**
	 * get month in two digit format eg. 01,11
	 *
	 * @param currentTime	the calendar object which indicate the time
	 *
	 * @return the month in string
	 */
	private String getMonth(Calendar currentTime)
	{
		String strMonth = null;

		try
		{
			//need to increase the month by 1 because JANUARY start from 0 and DECEMBER ends at 11
			if((currentTime.get(Calendar.MONTH) + 1) < 10)
			{
				strMonth = "0" + (currentTime.get(Calendar.MONTH) + 1);
			}
			else
			{
				strMonth = Integer.toString(currentTime.get(Calendar.MONTH) + 1);
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::getMonth - " + e.toString(), e);
        }

		return strMonth;
	}

	/**
	 * get date in two digit format eg. 01,15,23
	 *
	 * @param currentTime	the calendar object which indicate the time
	 *
	 * @return	the date in string
	 */
	private String getDate(Calendar currentTime)
	{
		String strDate = null;

		try
		{
			//check double digit
			if(currentTime.get(Calendar.DATE) < 10)
			{
				strDate = "0" + currentTime.get(Calendar.DATE);
			}
			else
			{
				strDate = Integer.toString(currentTime.get(Calendar.DATE));
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::getDate - " + e.toString(), e);
        }

		return strDate;
	}

	/**
	 * update the alert status
	 *
	 * @param	strAlertKey		the alert key
	 * @param	strStatus		the alert status
	 */
	private void updateAlertStatus(String strAlertKey, String strStatus)
	{
		try
		{
			DALQuery query = new DALQuery();

			query.setDomain("ALERT", null, null, null);
			query.setField("ALERT_strStatus", strStatus);
			query.setWhere(null, 0, "ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ALERT_intAlertKey", "=", strAlertKey, 0, DALQuery.WHERE_HAS_VALUE);
			query.executeUpdate();
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowAlertProcess - updateAlertStatus" + e.toString(), e);
        }
	}

	/**
	 * update the alert email sent 
	 *
	 * @param	strAlertKey		the alert key
	 * @param	strEmailSent 	the alert email sent
	 */
	private void updateAlertEmailSent(String strAlertKey, String strEmailSent)
	{
		try
		{
			DALQuery query = new DALQuery();

			query.setDomain("ALERT", null, null, null);
			query.setField("ALERT_strEmailSent", strEmailSent);
			query.setWhere(null, 0, "ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ALERT_intAlertKey", "=", strAlertKey, 0, DALQuery.WHERE_HAS_VALUE);
			query.executeUpdate();
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowAlertProcess - updateAlertEmailSent" + e.toString(), e);
        }
	}

	/**
	 * check the existence of the alert associated with a task
	 *
	 * @param strWorkflowTaskKey 	the workflow task key
	 *
	 * @return  the indicator to indicate whether the alert exist or not
	 */
	private boolean isTaskAlertExist(String strWorkflowTaskKey)
	{
		boolean blReturnValue = false;

		try
		{
			DALQuery query = new DALQuery(); 

			query.setDomain("ALERT", null, null, null);
			query.setField("ALERT_intAlertKey", null);
			query.setWhere(null, 0, "ALERT_intTaskKey", "=", strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			ResultSet rsResultSet = query.executeSelect();

			if(rsResultSet.next())
			{
				blReturnValue = true;
			}

		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::isTaskAlertExist - " + e.toString(), e);
        }

		return blReturnValue;
	}

	/*
	 * check whether a the alert has pass its due date
	 */
	private boolean passDueDateAndTime(String strWorkflowTaskWorkingDays,
									   String strWorkflowTaskUnit,
									   String strWorkflowTaskValue,
									   String strDateCreated,
									   String strTimeCreated)
	{

		boolean blReturnValue = false;

		try
		{
			if(strWorkflowTaskWorkingDays == null || strWorkflowTaskWorkingDays.length() == 0 ||
			   strWorkflowTaskUnit == null || strWorkflowTaskUnit.length() == 0 ||
			   strWorkflowTaskValue == null || strWorkflowTaskValue.length() == 0 ||
			   strDateCreated == null || strDateCreated.length() == 0 ||
			   strTimeCreated == null || strTimeCreated.length() == 0)
			{
				return blReturnValue;
			}
			
			//long longValue = 0;
			long longValueinMilliseconds = 0;

			String[] arrDate = strDateCreated.split("-");
			int intYear = (Integer.valueOf(arrDate[0])).intValue();
			//the month is starting from 0 (January) - 11 (December)
			int intMonth = (Integer.valueOf(arrDate[1])).intValue() - 1;
			int intDate = (Integer.valueOf(arrDate[2])).intValue();		
			
			String[] arrTime = strTimeCreated.split(":");
			int intHour = (Integer.valueOf(arrTime[0])).intValue();
			int intMinute = (Integer.valueOf(arrTime[1])).intValue();
			int intSecond = (Integer.valueOf(arrTime[2])).intValue();		

			Calendar creationCalendar = Calendar.getInstance();
			creationCalendar.set(intYear, intMonth, intDate, intHour, intMinute, intSecond);

			longValueinMilliseconds = getMilliSeconds(strWorkflowTaskUnit, strWorkflowTaskValue);

			//5 working days
			if(strWorkflowTaskWorkingDays.equals("5"))
			{
				int intDayOfWeek = creationCalendar.get(Calendar.DAY_OF_WEEK);

				//Day of week
				//Sunday - 1 
				//Monday - 2
				//Tuesday - 3
				//Wednesday - 4
				//Thursday - 5
				//Friday - 6
				//Saturday - 7

				int intDaysToFriday = Calendar.FRIDAY - intDayOfWeek;

				long longAdditionalMilliSeconds = 0;
				
				//the start day falls between Monday and Friday
				if(intDaysToFriday >= 0 && intDaysToFriday <= 4)
				{
					//1 day = 86400000 msec
					long longDaysToFridayInMilliSeconds = ((new Integer(intDaysToFriday)).longValue() *
														   Long.parseLong("86400000"));

					//this is to count the time left on the day 
					long longHourToMilliSeconds = (((new Integer(23)).longValue() - (new Integer(intHour)).longValue()) *
														Long.parseLong("3600000"));
					long longMinuteToMilliSeconds = (((new Integer(59)).longValue() - (new Integer(intMinute)).longValue()) *
														Long.parseLong("60000"));
					long longSecondToMilliSeconds = (((new Integer(59)).longValue() - (new Integer(intSecond)).longValue()) *
														Long.parseLong("1000"));

					long longInitialDaysInMilliSeconds = longDaysToFridayInMilliSeconds +
														 longHourToMilliSeconds + longMinuteToMilliSeconds + 
														 longSecondToMilliSeconds;

					if(longValueinMilliseconds > longInitialDaysInMilliSeconds)
					{
						//this is to count the Saturday and Sunday
						long longDayToMilliSeconds = ((new Integer(2)).longValue() * Long.parseLong("86400000"));
						
						long longUpdateValueinMilliseconds = 
									longValueinMilliseconds - longInitialDaysInMilliSeconds;

						//1 day = 86400000 msec
						long longFiveDaysInMilliSeconds = Long.parseLong("5") * Long.parseLong("86400000");
						long longAdditionalDaysInMilliSeconds = 0;
						if(longUpdateValueinMilliseconds > longFiveDaysInMilliSeconds)
						{
							//weekend is 2 days
							long longAdditionalDays = (longUpdateValueinMilliseconds / longFiveDaysInMilliSeconds) * 2;

							if((longUpdateValueinMilliseconds % longFiveDaysInMilliSeconds) > 0)
							{
								//add 2 more days if we pass the weekend again
								longAdditionalDays = longAdditionalDays + 2; 
							}

							//1 day = 86400000 msec
							longAdditionalDaysInMilliSeconds = longAdditionalDays * Long.parseLong("86400000");
						}

						longAdditionalDaysInMilliSeconds = longAdditionalDaysInMilliSeconds + longDayToMilliSeconds;
					}
				}
				//the start day is Saturday
				else if(intDaysToFriday == -1)
				{
					//this is to count the Sunday
					long longDayToMilliSeconds = ((new Integer(1)).longValue() * Long.parseLong("86400000"));

					//this is to count the time left on Saturday
					long longHourToMilliSeconds = (((new Integer(23)).longValue() - (new Integer(intHour)).longValue()) *
														Long.parseLong("3600000"));
					long longMinuteToMilliSeconds = (((new Integer(59)).longValue() - (new Integer(intMinute)).longValue()) *
														Long.parseLong("60000"));
					long longSecondToMilliSeconds = (((new Integer(59)).longValue() - (new Integer(intSecond)).longValue()) *
														Long.parseLong("1000"));

					//the counting of the start day is change to Monday
					//1 day = 86400000 msec
					long longFiveDaysInMilliSeconds = Long.parseLong("5") * Long.parseLong("86400000");
					long longAdditionalDaysInMilliSeconds = 0;
					if(longValueinMilliseconds > longFiveDaysInMilliSeconds)
					{
						//weekend is 2 days
						long longAdditionalDays = (longValueinMilliseconds / longFiveDaysInMilliSeconds) * 2;

						if((longValueinMilliseconds % longFiveDaysInMilliSeconds) > 0)
						{
							//add 2 more days if we pass the weekend again
							longAdditionalDays = longAdditionalDays + 2; 
						}

						//1 day = 86400000 msec
						longAdditionalDaysInMilliSeconds = longAdditionalDays * Long.parseLong("86400000");
					}


					longAdditionalMilliSeconds = longDayToMilliSeconds + longHourToMilliSeconds +
												 longMinuteToMilliSeconds + longSecondToMilliSeconds +
												 longAdditionalDaysInMilliSeconds;
				}
				//the start day is Sunday
				else if(intDaysToFriday == 5)
				{
					//this is to count the time left on Sunday 
					long longHourToMilliSeconds = (((new Integer(23)).longValue() - (new Integer(intHour)).longValue()) *
													Long.parseLong("3600000"));
					long longMinuteToMilliSeconds = (((new Integer(59)).longValue() - (new Integer(intMinute)).longValue()) *
														Long.parseLong("60000"));
					long longSecondToMilliSeconds = (((new Integer(59)).longValue() - (new Integer(intSecond)).longValue()) *
														Long.parseLong("1000"));

					//the counting of the start day is change to Monday
					//1 day = 86400000 msec
					long longFiveDaysInMilliSeconds = Long.parseLong("5") * Long.parseLong("86400000");
					long longAdditionalDaysInMilliSeconds = 0;
					if(longValueinMilliseconds > longFiveDaysInMilliSeconds)
					{
						//weekend is 2 days
						long longAdditionalDays = (longValueinMilliseconds / longFiveDaysInMilliSeconds) * 2;

						if((longValueinMilliseconds % longFiveDaysInMilliSeconds) > 0)
						{
							//add 2 more days if we pass the weekend again
							longAdditionalDays = longAdditionalDays + 2; 
						}

						//1 day = 86400000 msec
						longAdditionalDaysInMilliSeconds = longAdditionalDays * Long.parseLong("86400000");
					}

					longAdditionalMilliSeconds = longHourToMilliSeconds + longMinuteToMilliSeconds +
												 longSecondToMilliSeconds + longAdditionalDaysInMilliSeconds;
				}


				//reset the time
				longValueinMilliseconds = longValueinMilliseconds + longAdditionalMilliSeconds;

				Calendar currentCalendar = Calendar.getInstance();

				if((currentCalendar.getTimeInMillis() - creationCalendar.getTimeInMillis()) >= longValueinMilliseconds)
				{
					blReturnValue = true;
				}
			}
			//7 working days
			else if(strWorkflowTaskWorkingDays.equals("7"))
			{
				Calendar currentCalendar = Calendar.getInstance();

				if((currentCalendar.getTimeInMillis() - creationCalendar.getTimeInMillis()) >= longValueinMilliseconds)
				{
					blReturnValue = true;
				}
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::passDueDateAndTime - " + e.toString(), e);
        }

		return blReturnValue;
	}

	/**
	 * create a new alert record into ix_alert
	 *
	 * @param strName	the alert name
	 * @param strDescription	the alert description
	 * @param strTargetUserKey	the target user key
	 * @param strTargetUserType	the target user type
	 * @param strType	the alert type
	 * @param strWorkflowTaskKey	the workflow task key
	 */
	private void createAlert(String strName, String strDescription, String strTargetUserKey, String strTargetUserType,
							 String strType, String strWorkflowTaskKey)
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			String strDate = getDate(currentTime) + "/" + 
						     getMonth(currentTime) + "/" + 
						     currentTime.get(Calendar.YEAR);

			String strCurrentTime = currentTime.get(Calendar.HOUR_OF_DAY) + ":" +
									currentTime.get(Calendar.MINUTE) + ":" +
									currentTime.get(Calendar.SECOND);

			DALQuery query = new DALQuery();

			query.setDomain("ALERT", null, null, null);
			query.setField("ALERT_strName", strName);
			query.setField("ALERT_strDescription", strDescription);
			query.setField("ALERT_strOriginUserType", "ALERT_ENGINE");
			query.setField("ALERT_intTargetUserKey", strTargetUserKey);
			query.setField("ALERT_strTargetUserType", strTargetUserType);
			query.setField("ALERT_dtDate", strDate);
			query.setField("ALERT_strTime", strCurrentTime);
			query.setField("ALERT_strStatus", "Sent");

			if(SEND_EMAIL.equals("yes"))
			{
				query.setField("ALERT_strEmailSent", "yes");
			}
			else
			{
				query.setField("ALERT_strEmailSent", "no");
			}
												
			query.setField("ALERT_strType", strType);
			query.setField("ALERT_intTaskKey", strWorkflowTaskKey);
			query.executeInsert();
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::createAlert - " + e.toString(), e);
        }
	}

	/**
	 * to check whether an alert has been sent before
	 *
	 * @param strAlertType	the alert type
	 * @param strWorkflowTaskKey	the workflow task key
	 *
	 * @return true if the alert has been sent before otherwise return false
	 */
	private boolean hasAlertBeenSent(String strAlertType, String strWorkflowTaskKey)
	{
		boolean blReturnValue = false;

		try
		{
			DALQuery query = new DALQuery();

			query.setDomain("ALERT", null, null, null);
			query.setField("ALERT_intAlertKey", null);
			query.setWhere(null, 0, "ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 1, "ALERT_strStatus", "=", "Sent", 0, DALQuery.WHERE_HAS_VALUE); 
			query.setWhere("OR", 0, "ALERT_strStatus", "=", "Read", 1, DALQuery.WHERE_HAS_VALUE); 
			query.setWhere("AND", 0, "ALERT_strType", "=", strAlertType, 0, DALQuery.WHERE_HAS_VALUE); 
			query.setWhere("AND", 0, "ALERT_intTaskKey", "=", strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE); 
			ResultSet rsResultSet = query.executeSelect();

			if(rsResultSet.next())
			{
				blReturnValue = true;
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::hasAlertBeenSent - " + e.toString(), e);
        }

		return blReturnValue;
	}

	/**
	 * get the milliseconds
	 *
	 * @param strUnit	the unit
	 * @param strValue	the value
	 *
	 * @return the value in milliseconds
	 */
	private long getMilliSeconds(String strUnit, String strValue)
	{
		long longValue = 0;
		long longValueinMilliseconds = 0;

		try
		{
			if(strUnit.equals(UNIT_SECOND))
			{
				//1 sec = 1000 msec
				longValue = (Float.valueOf(strValue)).longValue();
				longValueinMilliseconds = longValue * Long.parseLong("1000");
			}
			else if(strUnit.equals(UNIT_MINUTE))
			{
				//1 minute = 60000 msec
				longValue = (Float.valueOf(strValue)).longValue();
				longValueinMilliseconds = longValue * Long.parseLong("60000");
			}
			else if(strUnit.equals(UNIT_HOUR))
			{
				//1 hour = 3600000 msec
				longValue = (Float.valueOf(strValue)).longValue();
				longValueinMilliseconds = longValue * Long.parseLong("3600000");
			}
			else if(strUnit.equals(UNIT_DAY))
			{
				//1 day = 86400000 msec
				longValue = (Float.valueOf(strValue)).longValue();
				longValueinMilliseconds = longValue * Long.parseLong("86400000");
			}
			else if(strUnit.equals(UNIT_MONTH))
			{
				//1 month = 30 days = 2592000000 msec
				longValue = (Float.valueOf(strValue)).longValue();
				longValueinMilliseconds = longValue * Long.parseLong("2592000000");
			}
			else if(strUnit.equals(UNIT_YEAR))
			{
				//1 year = 365 days = 31536000000000 msec
				longValue = (Float.valueOf(strValue)).longValue();
				longValueinMilliseconds = longValue * Long.parseLong("31536000000000");
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::getMilliSeconds - " + e.toString(), e);
        }

		return longValueinMilliseconds;
	}

	/**
	 * get the time stamp in milliseconds of the latest alert being sent for a particular task and alert type
	 *
	 * @param strWorkflowTaskKey	the workflow task key
	 * @param strAlertType			the alert type 
	 *
	 * @return the time stamp in milliseconds
	 */
	private long getTimestampOfLatestAlert(String strWorkflowTaskKey, String strAlertType)
	{
		long longValueinMilliseconds = 0;

		try
		{
			DALQuery query = new DALQuery();

            query.setDomain("ALERT", null, null, null);
            query.setMaxField("ALERT_dtDate");
            query.setMaxField("ALERT_strTime");
			query.setWhere(null, 0, "ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 1, "ALERT_strStatus", "=", "Sent", 0, DALQuery.WHERE_HAS_VALUE); 
			query.setWhere("OR", 0, "ALERT_strStatus", "=", "Read", 1, DALQuery.WHERE_HAS_VALUE); 
			query.setWhere("AND", 0, "ALERT_strType", "=", strAlertType, 0, DALQuery.WHERE_HAS_VALUE); 
			query.setWhere("AND", 0, "ALERT_intTaskKey", "=", strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE); 
            
            ResultSet rsResultSet = query.executeSelect();

			//System.err.println("agustian query = " + query.convertSelectQueryToString());
           
			String strDate = null;
			String strTime = null;
            if(rsResultSet.next())
			{
                strDate = rsResultSet.getString("MAX_ALERT_dtDate");
				strTime = rsResultSet.getString("MAX_ALERT_strTime");
			}

			String[] arrDate = strDate.split("-");
			int intYear = (Integer.valueOf(arrDate[0])).intValue();
			//the month is starting from 0 (January) - 11 (December)
			int intMonth = (Integer.valueOf(arrDate[1])).intValue() - 1;
			int intDate = (Integer.valueOf(arrDate[2])).intValue();		
			
			String[] arrTime = strTime.split(":");
			int intHour = (Integer.valueOf(arrTime[0])).intValue();
			int intMinute = (Integer.valueOf(arrTime[1])).intValue();
			int intSecond = (Integer.valueOf(arrTime[2])).intValue();

			/*
			System.err.println("intYear = " + intYear);
			System.err.println("intMonth = " + intMonth);
			System.err.println("intDate = " + intDate);

			System.err.println("intHour = " + intHour);
			System.err.println("intMinute = " + intMinute);
			System.err.println("intSecond = " + intSecond);
			*/

			Calendar calendar = Calendar.getInstance();
			calendar.set(intYear, intMonth, intDate, intHour, intMinute, intSecond);

			longValueinMilliseconds = calendar.getTimeInMillis();
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::getTimestampOfLatestAlert - " + e.toString(), e);
        }

		return longValueinMilliseconds;
	}


    /*
     * Assemble the message body of the email message
     */
    private String getMessageBody(String strMessageTemplate,
								  String strWorkflowTaskName,
								  String strWorkflowInstanceName,
								  String strWorkflowInstanceKey,
								  String strReminderTime,
								  String strPerformerTitle,
								  String strPerformerFirstName,
								  String strPerformerLastName,
								  String strPerformerType)
	{
		String strMessageResult = null;

		try
		{
			StringBuffer strBufMessage = new StringBuffer();

			String strWorkflowTaskNameTag = "<task_name/>";
			String strWorkflowInstanceNameTag = "<workflow_name/>";
			String strWorkflowInstanceKeyTag = "<workflow_key/>";
			String strReminderTimeTag = "<reminder_time/>";

			String strPerformerTitleTag = "<performer_title/>";
			String strPerformerFirstNameTag = "<performer_first_name/>";
			String strPerformerLastNameTag = "<performer_last_name/>";
			String strPerformerTypeTag = "<performer_type/>";

			strBufMessage.insert(0, strMessageTemplate);   

			Utilities.substrReplace(strBufMessage, strWorkflowTaskNameTag, strWorkflowTaskName);
			Utilities.substrReplace(strBufMessage, strWorkflowInstanceNameTag, strWorkflowInstanceName);
			Utilities.substrReplace(strBufMessage, strWorkflowInstanceKeyTag, strWorkflowInstanceKey);
			Utilities.substrReplace(strBufMessage, strReminderTimeTag, strReminderTime);

			Utilities.substrReplace(strBufMessage, strPerformerTitleTag, strPerformerTitle);
			Utilities.substrReplace(strBufMessage, strPerformerFirstNameTag, strPerformerFirstName);
			Utilities.substrReplace(strBufMessage, strPerformerLastNameTag, strPerformerLastName);
			Utilities.substrReplace(strBufMessage, strPerformerTypeTag, strPerformerType);

			strMessageResult = strBufMessage.toString(); 
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::getMessageBody - " + e.toString(), e);
        }
        
		return strMessageResult;
    }

    /*
     * Retrieve the Email template from Configuration table
	 *
	 * @param strType	the template type
	 *
	 * @retun the email template
     */
    private String getEmailTemplate(String strType)
	{
        String strEmailTemplate = null;

        try
		{
			DALQuery query = new DALQuery();

			query.setDomain("CONFIGURATION", null, null, null);
			query.setField("CONFIGURATION_strValue", null);
			query.setWhere(null, 0, "CONFIGURATION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "CONFIGURATION_strName", "=", strType, 0, DALQuery.WHERE_HAS_VALUE);
			ResultSet rsResultSet = query.executeSelect();

			if(rsResultSet.next())
			{
				strEmailTemplate = rsResultSet.getString("CONFIGURATION_strValue");
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowAlertProcess::getEmailTemplate - " + e.toString(), e);
        }

		return strEmailTemplate;
    }

}

