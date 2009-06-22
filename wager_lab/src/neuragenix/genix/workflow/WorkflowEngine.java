/**
 * WorkflowEngine.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 17/03/2004
 *
 * Last Modified: (Date\Author\Comments)
 * 
 */

package neuragenix.genix.workflow;


import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;
import java.util.Date;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;

// neuragenix packages
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.utils.Property;
import neuragenix.utils.Email;

/**
 * WorkflowEngine - Abstract workflow manager in managing all workflow activities
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class WorkflowEngine
{
	private static String ALERT_ENGINE_EMAIL =
							Property.getProperty("neuragenix.utils.WorkflowAlertProcess.AlertEngineEmail");
    private static String ADMINISTRATOR_EMAIL =
							Property.getProperty("neuragenix.utils.WorkflowAlertProcess.AdministratorEmail");
	private static String ADMINISTRATOR_ORG_USER_KEY =
							Property.getProperty("neuragenix.utils.WorkflowAlertProcess.AdministratorOrgUserKey");
	private static String ADMINISTRATOR_ORG_USER_TYPE =
							Property.getProperty("neuragenix.utils.WorkflowAlertProcess.AdministratorOrgUserType");
    private static String SMTP_SERVER = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.SMTPServer");   
    private static String EMAIL_USERNAME = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.EmailUsername");
    private static String EMAIL_PASSWORD = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.EmailPassword");
	private static String SEND_EMAIL = Property.getProperty("neuragenix.utils.WorkflowAlertProcess.SendEmail");  

	/**
	 * Default constructor
	 */
	public WorkflowEngine()
	{
	}


	// SAVE METHOD
	
	/**
	 * Save the package object and its children into the Database
	 *
	 * @param workflowPackage the Package
	 * @param query the query object
	 */
	public static String save(Package workflowPackage, DALSecurityQuery  query)
	{
		String strReturnValue = null;
		
		try
		{
			//validate workflow package
			strReturnValue = WorkflowManager.validatePackage(workflowPackage, WorkflowManager.SAVE_NEW, query);
			
System.err.println("validate package = >>>" + strReturnValue + "<<<");
			
			if(strReturnValue != null)
			{
				return strReturnValue;
			}
			
			//save workflow package
			WorkflowManager.save("workflow_package_details", WorkflowManager.WORKFLOW_PACKAGE, 
								 workflowPackage, query);
			
			//save workflow package header
			PackageHeader packageHeader = workflowPackage.getPackageHeader();
			String strWorkflowPackageXPDLId = workflowPackage.getId();
			WorkflowManager.save("workflow_package_header_details", 
								 WorkflowManager.WORKFLOW_PACKAGE_HEADER, packageHeader,
								 strWorkflowPackageXPDLId, query);

			
			//save workflow processes/templates
			Hashtable hashWorkflowProcesses = workflowPackage.getWorkflowProcesses();
			Enumeration enumWorkflowProcesses = hashWorkflowProcesses.elements();

			while(enumWorkflowProcesses.hasMoreElements())
			{
				WorkflowProcess workflowProcess = (WorkflowProcess) enumWorkflowProcesses.nextElement();
				WorkflowManager.save("workflow_template_details", WorkflowManager.WORKFLOW_PROCESS,
									 workflowProcess, strWorkflowPackageXPDLId, query);
			    	
				//save process/template header
				ProcessHeader processHeader = workflowProcess.getProcessHeader();
				String strWorkflowTemplateXPDLId = workflowProcess.getId();
				WorkflowManager.save("workflow_template_header_details",
									 WorkflowManager.WORKFLOW_PROCESS_HEADER, processHeader,
									 strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
									 query);


				//save parameters
				Hashtable hashWorkflowParameters = workflowProcess.getParameters();
				Enumeration enumWorflowParameters = hashWorkflowParameters.elements();

				while(enumWorflowParameters.hasMoreElements())
				{
					Parameter parameter = (Parameter) enumWorflowParameters.nextElement();
					WorkflowManager.save("workflow_template_parameter_details", 
										 WorkflowManager.WORKFLOW_TEMPLATE_PARAMETER,
										 parameter, strWorkflowPackageXPDLId,
										 strWorkflowTemplateXPDLId, query);
				}
			    	
				//save triggers
				Hashtable hashWorkflowTriggers = workflowProcess.getTriggers();
				Enumeration enumWorkflowTriggers = hashWorkflowTriggers.elements();

				while(enumWorkflowTriggers.hasMoreElements())
				{
					Trigger trigger = (Trigger) enumWorkflowTriggers.nextElement();
					WorkflowManager.save("workflow_trigger_details", WorkflowManager.WORKFLOW_TRIGGER,
										 trigger, strWorkflowPackageXPDLId,
										 strWorkflowTemplateXPDLId, query);
				}

				
				//save activities
				Hashtable hashWorkflowActivities = workflowProcess.getActivities();
				Enumeration enumWorkflowActivities = hashWorkflowActivities.elements();

				while(enumWorkflowActivities.hasMoreElements())
				{
					Activity activity = (Activity) enumWorkflowActivities.nextElement();
					WorkflowManager.save("workflow_activity_details", WorkflowManager.WORKFLOW_ACTIVITY,
										 activity, strWorkflowPackageXPDLId,
										 strWorkflowTemplateXPDLId, query);
				}

				
				//save transitions
				Hashtable hashTransitions = workflowProcess.getTransitions();
				Enumeration enumTransitions = hashTransitions.elements();

				while(enumTransitions.hasMoreElements())
				{
					Transition transition = (Transition) enumTransitions.nextElement();
					WorkflowManager.save("workflow_activity_transition_details",
										 WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION, transition, 
										 strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
										 query);
				}

				//get the workflow package key
				String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(
						strWorkflowPackageXPDLId, query);
				
				//get the workflow template key
				String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(
						strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);

				//get the workflow activity key
				String strInitialActivityXPDLId = workflowProcess.getInitialActivityId();
				String strWorkflowActivityKey = WorkflowManager.getWorkflowActivityKey(
						strWorkflowTemplateKey, strInitialActivityXPDLId, query);
			    	
				// update initialActivityId in WorkflowTemplate
				WorkflowManager.updateInitialActivityId(strWorkflowActivityKey,
						strWorkflowTemplateKey, query);
				
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::save - " + e.toString(), e);
        }

		return strReturnValue;
	}


	// UPDATE METHOD

	/**
	 * update the package object and its children in the Database
	 *
	 * @param workflowPackage the workflow package
	 * @param query the query object
	 */
	public static String update(Package workflowPackage, DALSecurityQuery  query)
	{
		String strReturnValue = null;
		
		try
		{
			//validate workflow package
			strReturnValue = WorkflowManager.validatePackage(workflowPackage, WorkflowManager.SAVE_UPDATE, query);
			
System.err.println("validate package = >>>" + strReturnValue + "<<<");
			
			if(strReturnValue != null)
			{
				return strReturnValue;
			}

			
			//update workflow package
			WorkflowManager.update("workflow_package_details", WorkflowManager.WORKFLOW_PACKAGE, workflowPackage, query); 

			
			PackageHeader packageHeader = workflowPackage.getPackageHeader();
			String strWorkflowPackageXPDLId = workflowPackage.getId();
			//check existence of a package header
			boolean blPackageHeaderExist = WorkflowManager.isPackageHeaderExist(WorkflowManager.WORKFLOW_PACKAGE_HEADER, 
										   strWorkflowPackageXPDLId, query);
			
			if(blPackageHeaderExist)
			{
				//do update the package header
				WorkflowManager.update("workflow_package_header_details", WorkflowManager.WORKFLOW_PACKAGE_HEADER,
							   		   packageHeader, strWorkflowPackageXPDLId, query);
			}
			else
			{
				//do insert the package header
				WorkflowManager.save("workflow_package_header_details", 
								 	 WorkflowManager.WORKFLOW_PACKAGE_HEADER, packageHeader,
								 	 strWorkflowPackageXPDLId, query);
			}

				
			//get all workflow templates/processes
			Hashtable hashWorkflowProcesses = workflowPackage.getWorkflowProcesses();
			Enumeration enumWorkflowProcesses = hashWorkflowProcesses.elements();
			String strWorkflowProcessXPDLId = null;

			while(enumWorkflowProcesses.hasMoreElements())
			{
				WorkflowProcess workflowProcess = (WorkflowProcess) enumWorkflowProcesses.nextElement();
				strWorkflowProcessXPDLId = workflowProcess.getId();

				boolean blWorkflowProcessExist = WorkflowManager.isWorkflowProcessExist(
													WorkflowManager.WORKFLOW_PROCESS, strWorkflowPackageXPDLId,
													strWorkflowProcessXPDLId, query);


				if(blWorkflowProcessExist)
				{
					//do update the workflow template 
					WorkflowManager.update("workflow_template_details", WorkflowManager.WORKFLOW_PROCESS,
							   		   	   workflowProcess, strWorkflowPackageXPDLId, query);
				}
				else
				{
					//do insert the workflow template
					WorkflowManager.save("workflow_template_details", WorkflowManager.WORKFLOW_PROCESS,
					 					 workflowProcess, strWorkflowPackageXPDLId, query);
				}
				
				
				//get process/template header
				ProcessHeader processHeader = workflowProcess.getProcessHeader();
				String strWorkflowTemplateXPDLId = workflowProcess.getId();
				
				boolean blWorkflowProcessHeaderExist = WorkflowManager.isProcessHeaderExist(
													   WorkflowManager.WORKFLOW_PROCESS_HEADER, strWorkflowPackageXPDLId, 
										   			   strWorkflowTemplateXPDLId, query);


				if(blWorkflowProcessHeaderExist)
				{
					//do update the workflow template header
					WorkflowManager.update("workflow_template_header_details", WorkflowManager.WORKFLOW_PROCESS_HEADER,
							   		   	   processHeader, strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
										   query);
				}
				else
				{
					//do insert the workflow template header
					WorkflowManager.save("workflow_template_header_details",
									 	 WorkflowManager.WORKFLOW_PROCESS_HEADER, processHeader,
									 	 strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
									 	 query);
				}

				//get parameters
			    Hashtable hashWorkflowParameters = workflowProcess.getParameters();
				Enumeration enumWorkflowParameters = hashWorkflowParameters.elements();
				String strWorkflowTemplateParameterName = null;

				while(enumWorkflowParameters.hasMoreElements())
				{
					Parameter parameter = (Parameter) enumWorkflowParameters.nextElement();
					strWorkflowTemplateParameterName = parameter.getName();	

					boolean blWorkflowParameterExist = WorkflowManager.isWorkflowTemplateParameterExist(
													   WorkflowManager.WORKFLOW_TEMPLATE_PARAMETER,
													   strWorkflowPackageXPDLId,
													   strWorkflowTemplateXPDLId, 
													   strWorkflowTemplateParameterName, query);


					if(blWorkflowParameterExist)
					{
//System.err.println("");
//System.err.println("the parameter exist so I'm going to update");
//System.err.println("name = " + parameter.getName());
//System.err.println("type = " + parameter.getType());
//System.err.println("description = " + parameter.getDescription());
//System.err.println("");
						//do update the workflow parameter 
						WorkflowManager.update("workflow_template_parameter_details", 
											   WorkflowManager.WORKFLOW_TEMPLATE_PARAMETER,
											   parameter, strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
											   query);
					}
					else
					{
//System.err.println("");
//System.err.println("the parameter doesn't exist so I'm going to save");
//System.err.println("name = " + parameter.getName());
//System.err.println("type = " + parameter.getType());
//System.err.println("description = " + parameter.getDescription());
//System.err.println("");
						//do insert the workflow parameter 
						WorkflowManager.save("workflow_template_parameter_details", 
											 WorkflowManager.WORKFLOW_TEMPLATE_PARAMETER,
											 parameter, strWorkflowPackageXPDLId,
											 strWorkflowTemplateXPDLId, query);
					}
				}
			   
				//get triggers
				Hashtable hashWorkflowTriggers = workflowProcess.getTriggers();
				Enumeration enumWorkflowTriggers = hashWorkflowTriggers.elements();
				String strWorkflowTriggerXPDLId = null;

				while(enumWorkflowTriggers.hasMoreElements())
				{
					Trigger trigger = (Trigger) enumWorkflowTriggers.nextElement();
					strWorkflowTriggerXPDLId = trigger.getId();	

					boolean blWorkflowTriggerExist = WorkflowManager.isTriggerExist(
													 WorkflowManager.WORKFLOW_TRIGGER, strWorkflowPackageXPDLId,
													 strWorkflowTemplateXPDLId, strWorkflowTriggerXPDLId,
													 query);


					if(blWorkflowTriggerExist)
					{
						//do update the workflow trigger
						WorkflowManager.update("workflow_trigger_details", WorkflowManager.WORKFLOW_TRIGGER,
											   trigger, strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
											   query);
					}
					else
					{
						//do insert the workflow trigger 
						WorkflowManager.save("workflow_trigger_details", WorkflowManager.WORKFLOW_TRIGGER,
										 trigger, strWorkflowPackageXPDLId,
										 strWorkflowTemplateXPDLId, query);
					}
				}
				
				//get activities
				Hashtable hashWorkflowActivities = workflowProcess.getActivities();
				Enumeration enumWorkflowActivities = hashWorkflowActivities.elements();
				String strWorkflowActivityXPDLId = null;

				while(enumWorkflowActivities.hasMoreElements())
				{
					Activity activity = (Activity) enumWorkflowActivities.nextElement();
					strWorkflowActivityXPDLId = activity.getId();

					boolean blWorkflowActivityExist = WorkflowManager.isActivityExist(
													  WorkflowManager.WORKFLOW_ACTIVITY, strWorkflowPackageXPDLId,
													  strWorkflowTemplateXPDLId, strWorkflowActivityXPDLId,
													  query);

//System.err.println("blWorkflowActivityExist = >>>" + blWorkflowActivityExist + "<<<");
//System.err.println("extended attributes = >>>" + activity.getExtendedAttributes() + "<<<");


					if(blWorkflowActivityExist)
					{
						//do update the workflow activity 
						WorkflowManager.update("workflow_activity_details", WorkflowManager.WORKFLOW_ACTIVITY,
											   activity, strWorkflowPackageXPDLId,
											   strWorkflowTemplateXPDLId, query);
					}
					else
					{
						//do insert the workflow activity
						WorkflowManager.save("workflow_activity_details", WorkflowManager.WORKFLOW_ACTIVITY,
										 activity, strWorkflowPackageXPDLId,
										 strWorkflowTemplateXPDLId, query);
					}
				}
			
				//get transitions
				Hashtable hashTransitions = workflowProcess.getTransitions();
				Enumeration enumTransitions = hashTransitions.elements();
				String strWorkflowActivityTransitionXPDLId = null;
				String strWorkflowActivityTransitionFromId = null;
				String strWorkflowActivityTransitionToId = null;

				while(enumTransitions.hasMoreElements())
				{
					Transition transition = (Transition) enumTransitions.nextElement();
					strWorkflowActivityTransitionXPDLId = transition.getId();
					strWorkflowActivityTransitionFromId = transition.getFrom();
					strWorkflowActivityTransitionToId = transition.getTo();

//System.err.println("strWorkflowPackageXPDLId = >>>" + strWorkflowPackageXPDLId + "<<<");
//System.err.println("strWorkflowTemplateXPDLId = >>>" + strWorkflowTemplateXPDLId + "<<<");
//System.err.println("strWorkflowActivityTransitionXPDLId = >>>" + strWorkflowActivityTransitionXPDLId + "<<<");
//System.err.println("strWorkflowActivityTransitionFromId = >>>" + strWorkflowActivityTransitionFromId + "<<<");
//System.err.println("strWorkflowActivityTransitionToId = >>>" + strWorkflowActivityTransitionToId + "<<<");
//System.err.println("");

					boolean blWorkflowTransitionExist = WorkflowManager.isTransitionExist(
													 WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION, strWorkflowPackageXPDLId,
													 strWorkflowTemplateXPDLId, strWorkflowActivityTransitionXPDLId,
													 strWorkflowActivityTransitionFromId, strWorkflowActivityTransitionToId,
													 query);

//System.err.println("");
//System.err.println("blWorkflowTransitionExist = >>>" + blWorkflowTransitionExist + "<<<");

					if(blWorkflowTransitionExist)
					{
//System.err.println("I'm about to UPDATE");
						//do update the workflow transition
						WorkflowManager.update("workflow_activity_transition_details",
											   WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION,
											   transition, strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
											   query);
					}
					else
					{
//System.err.println("I'm about to INSERT");
						//do insert the workflow transition
						WorkflowManager.save("workflow_activity_transition_details",
										 WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION, transition, 
										 strWorkflowPackageXPDLId, strWorkflowTemplateXPDLId,
										 query);
					}
				}
				
				
				//get the workflow package key
				String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(
						strWorkflowPackageXPDLId, query);
				
				//get the workflow template key
				String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(
						strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);

				//get the workflow activity key
				String strInitialActivityXPDLId = workflowProcess.getInitialActivityId();
				String strWorkflowActivityKey = WorkflowManager.getWorkflowActivityKey(
						strWorkflowTemplateKey, strInitialActivityXPDLId, query);
			    	
				// update initialActivityId in WorkflowTemplate
				WorkflowManager.updateInitialActivityId(strWorkflowActivityKey,
						strWorkflowTemplateKey, query);
			}

			//Delete all unused workflow process, process header, workflow template parameter,
			//trigger, activity, activity parameter and transition 

			//get the workflow package key from the DB
			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(
						strWorkflowPackageXPDLId, query);

			//get the list of workflow template keys from the DB
			Vector vecWorkflowTemplateKeys = WorkflowManager.getWorkflowTemplateKeys(strWorkflowPackageKey, query);

			String strExistingWorkflowTemplateKey = null;
			String strWorkflowActivityKey = null;
			String strWorkflowTemplateXPDLId = null;
			String strNewWorkflowTemplateKey = null;

//System.err.println("");
//System.err.println("going to delete all unused stuff");
//System.err.println("");
			for(int intIndex = 0; intIndex < vecWorkflowTemplateKeys.size(); intIndex++)
			{
				//get the existing workflow template key
				strExistingWorkflowTemplateKey = (String) vecWorkflowTemplateKeys.get(intIndex);
				
//System.err.println("strExistingWorkflowTemplateKey = " + strExistingWorkflowTemplateKey);

				enumWorkflowProcesses = hashWorkflowProcesses.elements();
				boolean blDeleteExistingWorkflowTemplate = true;
				Hashtable hashNewParameters = null;
				Hashtable hashNewTriggers = null;
				Hashtable hashNewActivities = null;
				Hashtable hashNewTransitions = null;

				while(enumWorkflowProcesses.hasMoreElements())
				{
					WorkflowProcess workflowProcess = (WorkflowProcess) enumWorkflowProcesses.nextElement();
					strWorkflowTemplateXPDLId = workflowProcess.getId();

					//get workflow template key
					strNewWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(
							strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);

//System.err.println("strNewWorkflowTemplateKey = " + strNewWorkflowTemplateKey);

					if(strNewWorkflowTemplateKey.equals(strExistingWorkflowTemplateKey))
					{
						blDeleteExistingWorkflowTemplate = false;
						hashNewParameters = workflowProcess.getParameters();
						hashNewTriggers = workflowProcess.getTriggers();
						hashNewActivities = workflowProcess.getActivities();
						hashNewTransitions = workflowProcess.getTransitions();
						break;
					}
				}

				// the workflow template in the DB is not in the XPDL file so it has to be deleted
				if(blDeleteExistingWorkflowTemplate)
				{
//System.err.println("");
//System.err.println("blDeleteExistingWorkflowTemplate = true");
//System.err.println("");
					//delete the workflow template and all its children

//System.err.println("about to delete workflow template parameter with template key = " + strExistingWorkflowTemplateKey);
					//delete workflow template parameter
					WorkflowManager.deleteWorkflowTemplateParameter(WorkflowManager.WORKFLOW_TEMPLATE_PARAMETER, 
													strExistingWorkflowTemplateKey, query);
					
					//delete workflow trigger
					WorkflowManager.deleteTrigger(WorkflowManager.WORKFLOW_TRIGGER, strWorkflowPackageKey, 
												  strExistingWorkflowTemplateKey, query);

					//get the list of workflow activities keys
					Vector vecWorkflowActivityKeys = 
								WorkflowManager.getWorkflowActivityKeys(strExistingWorkflowTemplateKey, query);

					for(int intIndex1 = 0; intIndex1 < vecWorkflowActivityKeys.size(); intIndex1++)
					{
						strWorkflowActivityKey = (String) vecWorkflowActivityKeys.get(intIndex1);

						//delete workflow transition
						WorkflowManager.deleteTransition(WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION,
														 strWorkflowActivityKey, query);

						//delete workflow activity parameter 
						WorkflowManager.deleteActivityParameter(WorkflowManager.WORKFLOW_ACTIVITY_PARAMETER,
								    							strWorkflowActivityKey, query);
					}

					//delete workflow activity 
					WorkflowManager.deleteActivity(WorkflowManager.WORKFLOW_ACTIVITY, strExistingWorkflowTemplateKey, query);

					//delete workflow template header 
					WorkflowManager.deleteTemplateHeader(WorkflowManager.WORKFLOW_PROCESS_HEADER,
														 strExistingWorkflowTemplateKey, query);
					
					//delete workflow template
					WorkflowManager.deleteTemplate(WorkflowManager.WORKFLOW_PROCESS, strExistingWorkflowTemplateKey, query);
				}
				else // the workflow template in th DB is in the XPDL file so it may need to be updated
				{

//System.err.println("");
//System.err.println("blDeleteExistingWorkflowTemplate = false");
//System.err.println("");
					//get the existing(DB) list of workflow template parameter name
					Vector vecParameterNames = WorkflowManager.getWorkflowTemplateParameterNames(
																 WorkflowManager.WORKFLOW_TEMPLATE_PARAMETER,
																 strExistingWorkflowTemplateKey, query);
					
					//get the new list of parameters - use hashNewParameters 
					//check if any of the parameter name in the existing parameters not in the new list of parameters
					//(contain the parameter name), delete them
					Enumeration enumNewParameters = null;
					for(int intIndex5 = 0; intIndex5 < vecParameterNames.size(); intIndex5++)
					{
						boolean blDeleteExistingParameter = true; 
						enumNewParameters = hashNewParameters.elements();
						String strCurrentParameterName = (String) vecParameterNames.get(intIndex5);
						
						while(enumNewParameters.hasMoreElements())
						{
							Parameter parameter = (Parameter) enumNewParameters.nextElement();
							if(parameter.getName().equals(strCurrentParameterName))
							{
								blDeleteExistingParameter = false;
								break;
							}
						}

						if(blDeleteExistingParameter)
						{
							WorkflowManager.deleteWorkflowTemplateParameter(WorkflowManager.WORKFLOW_TEMPLATE_PARAMETER,
															strCurrentParameterName, strExistingWorkflowTemplateKey,
														    query);
						}
					}

					//get the existing(DB) list of trigger xpdl id
					Vector vecTriggerXPDLIds = WorkflowManager.getTriggerXPDLIds(WorkflowManager.WORKFLOW_TRIGGER,
																 strWorkflowPackageKey, 
																 strExistingWorkflowTemplateKey, query);
					
					//get the new list of triggers - use hashNewTriggers
					//check if any of the xpdl ids in the existing triggers not in the new list of triggers
					//(contain the xpdl id), delete them
					Enumeration enumNewTriggers = null;
					for(int intIndex2 = 0; intIndex2 < vecTriggerXPDLIds.size(); intIndex2++)
					{
						boolean blDeleteExistingTrigger = true; 
						enumNewTriggers = hashNewTriggers.elements();
						String strCurrentTriggerXPDLId = (String) vecTriggerXPDLIds.get(intIndex2);
						
						while(enumNewTriggers.hasMoreElements())
						{
							Trigger trigger = (Trigger) enumNewTriggers.nextElement();
							if(trigger.getId().equals(strCurrentTriggerXPDLId))
							{
								blDeleteExistingTrigger = false;
								break;
							}
						}

						if(blDeleteExistingTrigger)
						{
							WorkflowManager.deleteTrigger(WorkflowManager.WORKFLOW_TRIGGER, strCurrentTriggerXPDLId,
													      strWorkflowPackageKey, strExistingWorkflowTemplateKey,
														  query);
						}
					}
					
					//get the existing(DB) list of activity xpdl id
					Vector vecActivityXPDLIds = WorkflowManager.getActivityXPDLIds(WorkflowManager.WORKFLOW_ACTIVITY,
															strExistingWorkflowTemplateKey, query);

					//get the new list of activities - use hashActivities
					//check if any of the xpdl ids in the existing activity not in the new list of activity
					//(contain the xpdl id), delete them
					Enumeration enumNewActivities = null;
					for(int intIndex3 = 0; intIndex3 < vecActivityXPDLIds.size(); intIndex3++)
					{
						boolean blDeleteExistingActivity = true; 
						enumNewActivities = hashNewActivities.elements();
						String strCurrentActivityXPDLId = (String) vecActivityXPDLIds.get(intIndex3);
						
						while(enumNewActivities.hasMoreElements())
						{
							Activity activity = (Activity) enumNewActivities.nextElement();
							if(activity.getId().equals(strCurrentActivityXPDLId))
							{
								blDeleteExistingActivity = false;
								break;
							}
						}

						if(blDeleteExistingActivity)
						{
							//get the workflow activity key
							String strCurrentWorkflowActivityKey = WorkflowManager.getWorkflowActivityKey(
											strExistingWorkflowTemplateKey, strCurrentActivityXPDLId, query);

							WorkflowManager.deleteActivityParameter(WorkflowManager.WORKFLOW_ACTIVITY_PARAMETER,
																	strCurrentWorkflowActivityKey, query);

							WorkflowManager.deleteActivity(WorkflowManager.WORKFLOW_ACTIVITY, strCurrentActivityXPDLId,
													       strExistingWorkflowTemplateKey, query);
						}
					}
					
					//get the existing(DB) list of transition xpdl id, keep in mind that I need to get 
					//all the transition in this template!!! TEMPLATE LEVEL!!!
					Vector vecTransitionXPDLIds = WorkflowManager.getTransitionXPDLIds(
													WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION, 
													strExistingWorkflowTemplateKey, query);
					
					//get the new list of transitions - use hashTransitions
					//check if any of the xpdl ids in the existing transition not in the new list of transition
					//(contain the xpdl id), delete them
					Enumeration enumNewTransitions = null;
					for(int intIndex4 = 0; intIndex4 < vecTransitionXPDLIds.size(); intIndex4++)
					{
						boolean blDeleteExistingTransition = true; 
						enumNewTransitions = hashNewTransitions.elements();
						String strCurrentTransitionXPDLId = (String) vecTransitionXPDLIds.get(intIndex4);
						
						while(enumNewTransitions.hasMoreElements())
						{
							Transition transition = (Transition) enumNewTransitions.nextElement();
							if(transition.getId().equals(strCurrentTransitionXPDLId))
							{
								blDeleteExistingTransition= false;
								break;
							}
						}

						if(blDeleteExistingTransition)
						{
							WorkflowManager.deleteTransition(WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION,
															 strCurrentTransitionXPDLId,
													         strExistingWorkflowTemplateKey, query);
						}
					}	
				}
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::update - " + e.toString(), e);
        }

		return strReturnValue;
	}


	// DELETE METHODS

	/**
	 * delete the package object and its children from the Database
	 *
	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id 
	 * @param query the query object
	 */
	public static void delete(String strWorkflowPackageXPDLId, DALSecurityQuery query)
	{
		try
		{
			//get the workflow package key
			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(
						strWorkflowPackageXPDLId, query);

			//get the list of workflow template keys
			Vector vecWorkflowTemplateKeys = WorkflowManager.getWorkflowTemplateKeys(strWorkflowPackageKey, query);

			String strWorkflowTemplateKey = null;
			String strWorkflowActivityKey = null;
			for(int intIndex = 0; intIndex < vecWorkflowTemplateKeys.size(); intIndex++)
			{
				//get the workflow template key
				strWorkflowTemplateKey = (String) vecWorkflowTemplateKeys.get(intIndex);
				
				//delete workflow trigger
				WorkflowManager.deleteTrigger(WorkflowManager.WORKFLOW_TRIGGER, strWorkflowPackageKey,
											  strWorkflowTemplateKey, query);

				//get the list of workflow activities keys
				Vector vecWorkflowActivityKeys = WorkflowManager.getWorkflowActivityKeys(strWorkflowTemplateKey, query);

				for(int intIndex1 = 0; intIndex1 < vecWorkflowActivityKeys.size(); intIndex1++)
				{
					strWorkflowActivityKey = (String) vecWorkflowActivityKeys.get(intIndex1);

					//delete workflow transition
					WorkflowManager.deleteTransition(WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION,
													 strWorkflowActivityKey, query);

					//delete workflow activity parameter 
					WorkflowManager.deleteActivityParameter(WorkflowManager.WORKFLOW_ACTIVITY_PARAMETER,
															strWorkflowActivityKey, query);
				}

				//delete workflow activity 
				WorkflowManager.deleteActivity(WorkflowManager.WORKFLOW_ACTIVITY, strWorkflowTemplateKey, query);

				//delete workflow template header 
				WorkflowManager.deleteTemplateHeader(WorkflowManager.WORKFLOW_PROCESS_HEADER, strWorkflowTemplateKey, query);
				
				//delete workflow template
				WorkflowManager.deleteTemplate(WorkflowManager.WORKFLOW_PROCESS, strWorkflowTemplateKey, query);
			}

			//delete workflow package header
			WorkflowManager.deletePackageHeader(WorkflowManager.WORKFLOW_PACKAGE_HEADER, strWorkflowPackageKey, query);

			//delete workflow package
			WorkflowManager.deletePackage(WorkflowManager.WORKFLOW_PACKAGE, strWorkflowPackageKey,
										  strWorkflowPackageXPDLId, query);
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::delete - " + e.toString(), e);
        }
	}

	/**
	 * delete the template object and its children from the Database
	 *
	 * @param strWorkflowTemplateKey the workflow template key
	 * @param query the query object
	 */
	public static void deleteWorkflowTemplateAndChildren(String strWorkflowTemplateKey, DALSecurityQuery query)
	{
		try
		{
			//delete workflow trigger
			WorkflowManager.deleteTrigger(WorkflowManager.WORKFLOW_TRIGGER, strWorkflowTemplateKey, query);

			//get the list of workflow activities keys
			Vector vecWorkflowActivityKeys = WorkflowManager.getWorkflowActivityKeys(strWorkflowTemplateKey, query);

			//workflow activity key
			String strWorkflowActivityKey = null;
			for(int intIndex1 = 0; intIndex1 < vecWorkflowActivityKeys.size(); intIndex1++)
			{
				strWorkflowActivityKey = (String) vecWorkflowActivityKeys.get(intIndex1);

				//delete workflow transition
				WorkflowManager.deleteTransition(WorkflowManager.WORKFLOW_ACTIVITY_TRANSITION,
												 strWorkflowActivityKey, query);

				//delete workflow activity parameter 
				WorkflowManager.deleteActivityParameter(WorkflowManager.WORKFLOW_ACTIVITY_PARAMETER,
														strWorkflowActivityKey, query);
			}

			//delete workflow activity 
			WorkflowManager.deleteActivity(WorkflowManager.WORKFLOW_ACTIVITY, strWorkflowTemplateKey, query);

			//delete workflow template header 
			WorkflowManager.deleteTemplateHeader(WorkflowManager.WORKFLOW_PROCESS_HEADER, strWorkflowTemplateKey, query);
			
			//delete workflow template
			WorkflowManager.deleteTemplate(WorkflowManager.WORKFLOW_PROCESS, strWorkflowTemplateKey, query);
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::deleteWorkflowTemplateAndChildren - " + e.toString(), e);
        }
	}

	// INSTANTIATION METHODS

	/**
	 * instantiate multiple workflow template
	 *
	 * @param strAction					the action/activity
	 * @param runtimeData				the runtime data
	 * @param strWorkflowTemplateKey	the workflow template key
	 * @param query						the query object
	 *
	 * @return the workflow template and instance key mapping (including sub workflows)
	 */
	public static Hashtable instantiateWorkflowTemplates(String strAction, ChannelRuntimeData runtimeData, 
									   					 String strWorkflowTemplateKey, String strDomain, 
														 String strDomainKey, boolean blValidateTrigger,
														 DALSecurityQuery query)
	{
		Hashtable hashTemplateInstancesMap = new Hashtable();

		try
		{
			if(strWorkflowTemplateKey != null || strAction != null)
			{
				Vector vecWorkflowTemplateKey = 
						WorkflowManager.validateTriggerData(strAction, runtimeData, strWorkflowTemplateKey, query);


				//System.err.println("vecWorkflowTemplateKey = >>>" + vecWorkflowTemplateKey + "<<<");
			
				Vector vecActiveWorkflowTemplateKey = new Vector();

				if(blValidateTrigger)
				{
					//get all actives workflow templates only
					for(int intIndex = 0; intIndex < vecWorkflowTemplateKey.size(); intIndex++)
					{
						String strTempWorkflowTemplateKey = (String) vecWorkflowTemplateKey.get(intIndex);
						
						boolean blHaveNonActiveTemplates = WorkflowManager.haveNonActiveTemplates(
															strTempWorkflowTemplateKey, query);

						if(!blHaveNonActiveTemplates)
						{
							vecActiveWorkflowTemplateKey.add(strTempWorkflowTemplateKey); 
						}
					}
				}
				else
				{
					vecActiveWorkflowTemplateKey.addAll(vecWorkflowTemplateKey); 
				}

				//System.err.println("vecActiveWorkflowTemplateKey = >>>" + vecActiveWorkflowTemplateKey + "<<<");

				Enumeration enumWorkflowTemplateKey = vecActiveWorkflowTemplateKey.elements();
				while(enumWorkflowTemplateKey.hasMoreElements())
				{
					String strTempWorkflowTemplateKey = (String) enumWorkflowTemplateKey.nextElement();

					runtimeData.setParameter("strDomain", strDomain);  
                    runtimeData.setParameter("intDomainKey", strDomainKey);                             
					
					Hashtable hashTemplateInstanceMap =
							WorkflowEngine.instantiateWorkflowTemplate(strTempWorkflowTemplateKey, runtimeData, query);

//System.err.println("hashTemplateInstanceMap = >>>" + hashTemplateInstanceMap + "<<<");

					//insert new entry into ix_workflow_instance_to_domain table
					if((strDomain != null && !strDomain.equals("")) &&
					   (strDomainKey != null && !strDomainKey.equals("")))
					{
						
//System.err.println("hashTemplateInstanceMap.size() = >>>" + hashTemplateInstanceMap.size() + "<<<");

						Enumeration enumWorkflowInstanceKeys = hashTemplateInstanceMap.elements();

						while(enumWorkflowInstanceKeys.hasMoreElements())
						{
							Vector vecWorkflowInstanceKeys = (Vector) enumWorkflowInstanceKeys.nextElement();

//System.err.println("vecWorkflowInstanceKeys.size() = >>>" + vecWorkflowInstanceKeys.size() + "<<<");

							for(int intIndex = 0; intIndex < vecWorkflowInstanceKeys.size(); intIndex++)
							{
								
								String strWorkflowInstanceKey = (String) vecWorkflowInstanceKeys.elementAt(intIndex);

//System.err.println("strWorkflowInstanceKey = >>>" + strWorkflowInstanceKey + "<<<");
//System.err.println("strDomain = >>>" + strDomain + "<<<");
//System.err.println("strDomainKey = >>>" + strDomainKey + "<<<");

								WorkflowManager.insertIntoWorkflowInstanceToDomain(strWorkflowInstanceKey, strDomain,
																				   strDomainKey, query);
							}
						}
					}

					hashTemplateInstancesMap.put(strTempWorkflowTemplateKey, hashTemplateInstanceMap);
				}
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::instantiateWorkflowTemplates - " + e.toString(), e);
        }

		return hashTemplateInstancesMap;
	}

	/**
	 * instantiate workflow template
	 *
	 * @param strWorkflowTemplateKey	the workflow template key
	 * @param runtimeData				the runtime data
	 * @param query						the query object
	 *
	 * @return the workflow template and instance key mapping
	 */
	public static Hashtable instantiateWorkflowTemplate(String strWorkflowTemplateKey,
												   ChannelRuntimeData runtimeData,
												   DALSecurityQuery query)
	{
		//store the related template and instance keys mapping
		Hashtable hashTemplateInstanceKeys = new Hashtable();

		try
		{
			//store the related activity and task keys mapping
			Hashtable hashActivityTaskKeys = new Hashtable();

			//store the related activity and task keys mapping for a template
			Hashtable hashActivityTaskTemplateKeys = new Hashtable();

			//store the related template and instance keys mapping
			//Hashtable hashTemplateInstanceKeys = new Hashtable();

			//store the list of subworkflow
			Vector vecWorkflowTemplateKey = new Vector();
			vecWorkflowTemplateKey.add(strWorkflowTemplateKey);

			for(int intIndex = 0; intIndex < vecWorkflowTemplateKey.size(); intIndex++)
			{

				//clear the hashActivityTaskTemplateKeys
				hashActivityTaskTemplateKeys.clear();

				String strTempWorkflowTemplateKey = (String) vecWorkflowTemplateKey.elementAt(intIndex);

				//copy workflow template to workflow instance
				WorkflowManager.copyWorkflowTemplateToWorkflowInstance(strTempWorkflowTemplateKey, 
																	   hashTemplateInstanceKeys, query);

				Vector vecWorkflowInstanceKeys = (Vector) hashTemplateInstanceKeys.get(strTempWorkflowTemplateKey);
				
				//get at position 0 (zero) as there will only be 1 instance
				String strWorkflowInstanceKey = (String) vecWorkflowInstanceKeys.get(0);

				//copy workflow activity to workflow task
				WorkflowManager.copyWorkflowActivityToWorkflowTask(strTempWorkflowTemplateKey, hashActivityTaskKeys,
																   hashActivityTaskTemplateKeys,
																   vecWorkflowTemplateKey, strWorkflowInstanceKey, query);
				
				/* workflow activity parameter is not used
				//copy workflow activity parameter to task parameter
				//WorkflowManager.copyWorkflowActivityParameterToWorkflowTaskParameter(hashActivityTaskTemplateKeys, query);
				*/
				
				//copy workflow activity transition to task transition
				WorkflowManager.copyWorkflowActivityTransitionToWorkflowTaskTransition(hashActivityTaskTemplateKeys,
																				   strTempWorkflowTemplateKey, 
																				   strWorkflowInstanceKey, query);

				String strInitialActivityKey = WorkflowManager.getInitialActivityKey(strTempWorkflowTemplateKey, query);
			
				//String strWorkflowInstanceKey = (String) hashTemplateInstanceKeys.get(strTempWorkflowTemplateKey);
				//the template can be instantiated many times therefore we need vector to store it
				//Vector vecWorkflowInstanceKeys = (Vector) hashTemplateInstanceKeys.get(strTempWorkflowTemplateKey);

				//the latest instance is the last element in the vector
				//String strWorkflowInstanceKey = (String) vecWorkflowInstanceKeys.lastElement();
			
				String strWorkflowTaskKey = (String) hashActivityTaskTemplateKeys.get(strInitialActivityKey);

				//update the initial task key in the instantiated workflow instance
				WorkflowManager.updateInitialTaskKey(strWorkflowTaskKey, strWorkflowInstanceKey, query);

				//only copy the parameter for the parent workflow which is located at index# 0!!!! 
				//the sub workflow will be copied later when it has passed the trigger validation 
				//if(intIndex == 0)
				//{
					//copy the workflow templates parameters to ix_workflow_runtimedata
					Hashtable hashTemplateParameterType = 
							WorkflowManager.getWorkflowTemplateParameterType(strTempWorkflowTemplateKey, query);
					
					//copying the domain and domain key too by adding the type of domain and domain key into the hashTemplateParameterType!!
					hashTemplateParameterType.put("strDomain", "String");
					hashTemplateParameterType.put("intDomainKey", "String");

					WorkflowManager.copyRuntimedata(strWorkflowInstanceKey, runtimeData, hashTemplateParameterType, query);


				//}
			}

			//System.err.println("hashTemplateInstanceKeys = >>>" + hashTemplateInstanceKeys + "<<<");

			//store the template and instance key mapping into 
			Hashtable hashTempTemplateInstanceKeys = new Hashtable(hashTemplateInstanceKeys);
			
			//Enumeration enumAgusActivityKeys = hashActivityTaskKeys.keys();
			//while(enumAgusActivityKeys.hasMoreElements())
			//{
			//	String strActivityKey = (String) enumAgusActivityKeys.nextElement();
			//	System.err.println("strActivityKey = >>>" + strActivityKey + "<<<");
			//}
			//System.err.println("");
			//System.err.println("");
			//System.err.println("hashActivityTaskKeys = >>>" + hashActivityTaskKeys + "<<<");

			//need to comment this as we only want to instantiate the parent wf not the children/sub wf
			/*
			//updating the sub workflow instance key for each sub-workflow type task
			Enumeration enumActivityKeys = hashActivityTaskKeys.keys();
			while(enumActivityKeys.hasMoreElements())
			{
				String strActivityKey = (String) enumActivityKeys.nextElement();

				//get the activity type by querying the ix_workflow_activity_table
				String strActivityType = WorkflowManager.getActivityType(strActivityKey, query);
				
				//if the activity type is a subworkflow
				if(strActivityType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW))
				{
					Vector vecTaskKeys = (Vector) hashActivityTaskKeys.get(strActivityKey);
					Enumeration enumTaskKeys = vecTaskKeys.elements();

					while(enumTaskKeys.hasMoreElements())
					{
						String strTaskKey = (String) enumTaskKeys.nextElement();

						//get the sub_workflow_template_key from the ix_workflow_activity table
						String strSubWorkflowTemplateKey = WorkflowManager.getSubWorkflowTemplateKey(strActivityKey, query);
					
						//get the workflow instance key for the given sub_workflow_template_key from hashTempTemplateInstanceKeys
						//String strSubWorkflowInstanceKey = (String) hashTempTemplateInstanceKeys.get(strSubWorkflowTemplateKey);
						//get the workflow instance keys and allocate to the sub workflow, 
						//starting from the last entry in the vector go backwards to the very first one, 
						//remove the one that has been used.
						Vector vecSubWorkflowInstanceKeys = (Vector) hashTempTemplateInstanceKeys.get(strSubWorkflowTemplateKey);
						String strSubWorkflowInstanceKey = (String) vecSubWorkflowInstanceKeys.lastElement();
						vecSubWorkflowInstanceKeys.remove(strSubWorkflowInstanceKey);
						//add at the very begining of the vector because we still need it as the return value
						//the assignment to hashTempTemplateInstanceKeys is just a reference and not a clone
						//IMPORTANT: need to find out how to clone a hashtable, in the mean time just add the Sub workflow
						//instance key back at the very begining at the vector!!!
					
						vecSubWorkflowInstanceKeys.add(0, strSubWorkflowInstanceKey);	
						
						hashTempTemplateInstanceKeys.put(strSubWorkflowTemplateKey, vecSubWorkflowInstanceKeys);
						
						//get the task key
						//String strTaskKey = (String) hashActivityTaskKeys.get(strActivityKey);

						//set the sub_workflow_instance_key in the ix_workflow_task for a given workflow_task_key
						WorkflowManager.updateSubWorkflowInstanceKey(strSubWorkflowInstanceKey, strTaskKey, query);
					}
				}
			}
			*/

			//KICK START the first task!!!!!!
		
			String strInitialActivityKey = WorkflowManager.getInitialActivityKey(strWorkflowTemplateKey, query);
			Vector vecTaskKeys = (Vector) hashActivityTaskKeys.get(strInitialActivityKey);


			//System.err.println("vecTaskKeys.size() = >>>" + vecTaskKeys.size() + "<<<");

			String strWorkflowTaskKey = (String) vecTaskKeys.get(0);
			//String strWorkflowTaskKey = (String) hashActivityTaskKeys.get(strInitialActivityKey);

			//if(strMethod.equals(WorkflowManager.MANUAL_INSTANTIATION))
			//{
				WorkflowEngine.kickStartTask(strWorkflowTaskKey, runtimeData, query);
			//}
			//else
			//{
				//do nothing yet
			//}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::instantiateWorkflowTemplate - " + e.toString(), e);
        }

		return hashTemplateInstanceKeys;
	}

	/**
	 * kick start the first task in an instance
	 *
	 * @param strWorkflowTaskKey	the workflow task key
	 * @param runtimeData			the runtime data
	 * @param query 				the query object
	 */
	private static void kickStartTask(String strWorkflowTaskKey, ChannelRuntimeData runtimeData, DALSecurityQuery query)
	{
		try
		{
			String strTaskStatus = WorkflowManager.getTaskStatus(strWorkflowTaskKey, query);

			//only kick start the task if its status is not active 
			if(!strTaskStatus.equals(WorkflowManager.TASK_STATUS_NOT_ACTIVE))
			{
				return;
			}

			// get the workflow instance key for a given task and update the status of WF instance
			String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strWorkflowTaskKey, query);
			WorkflowManager.updateWorkflowInstanceStatus(WorkflowManager.WORKFLOW_INSTANCE_STATUS_IN_PROGRESS,
														 strWorkflowInstanceKey, query);

			Calendar currentTime = Calendar.getInstance();
			String strToday = WorkflowManager.getDate(currentTime) + "/" + 
							  WorkflowManager.getMonth(currentTime) + "/" + 
							  currentTime.get(Calendar.YEAR);

			String strCurrentTime = currentTime.get(Calendar.HOUR_OF_DAY) + ":" +
									currentTime.get(Calendar.MINUTE) + ":" +
									currentTime.get(Calendar.SECOND);

			WorkflowManager.updateTaskReceivedTime(strCurrentTime, strWorkflowTaskKey, query);
			WorkflowManager.updateTaskReceivedDate(strToday, strWorkflowTaskKey, query);
			WorkflowManager.updateTaskStatus(WorkflowManager.TASK_STATUS_ACTIVE, strWorkflowTaskKey, query);

			//get the initial task type
			String strType = WorkflowManager.getTaskType(strWorkflowTaskKey, query);

			if(strType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM))
			{
				ChannelRuntimeData tempRuntimeData =
							WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

				//add workflow instance key into the tempRuntimeData, 
				//the workflow instance key will only be provided on runtime, 
				//so we do not need to store the actual value in the ix_workflow_runtimedata
				//the parameter name is still there because there is a type checking, if the parameter name is not in the
				//ix_workflow_runtimedata, the parameter will not be available for the system function
				tempRuntimeData.setParameter("strWorkflowInstanceKey", strWorkflowInstanceKey);

				//prepare and execute the function
				Boolean blExecutionResult = WorkflowManager.prepareAndExecuteFunction(strWorkflowTaskKey, tempRuntimeData,
																					  query);	

				//if the function was successful and do the next task
				if(blExecutionResult.booleanValue())
				{
					String strWorkflowTaskFunctionKey = WorkflowManager.getFunctionKey(strWorkflowTaskKey, query);
					ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strWorkflowTaskFunctionKey, query);

					String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType"); 

					//only run finishTask when the function type is not equals to "Thread"
					if(!strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
					{
						WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
											   Boolean.valueOf(true), tempRuntimeData, query);
					}
					else
					{
//System.err.println("about to add an entry in the workflow function thread table!!!!");
						WorkflowManager.addWorkflowFunctionThread(strWorkflowTaskKey, strWorkflowInstanceKey, 
																	 WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
					}

					//WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
					//					   	  Boolean.valueOf(true), tempRuntimeData, query);
				}
				else
				{
					ResultSet rsSystemTaskFailureDetails =
								WorkflowManager.getSystemTaskFailureDetails(strWorkflowTaskKey, query);

					String strIfFail = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFail"); 
					String strRetryDelayUnit = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strRetryDelayUnit"); 
					String strRetryDelayValue = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_flRetryDelayValue"); 
					String strRetryCounter = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_intRetryCounter"); 
					String strIfFailAfterRetry = 
									rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFailAfterRetry");

					if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
					{
						//send alert to the administrator
						//task failed
						Task task = WorkflowManager.getWorkflowTask(strWorkflowTaskKey, query);
						String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
						WorkflowInstance workflowInstance = 
										WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
						String strWorkflowTaskName = task.getName();
						String strWorkflowInstanceName = workflowInstance.getName();
						String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
											" in the workflow " + strWorkflowInstanceName + " has failed.";
						String strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
						String strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
															   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
															   "", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
						DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody, 
												ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE,
												"Task fail", strWorkflowTaskKey, SEND_EMAIL);
						String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);
						
						//send email
    					Email email = new Email();
						email.setUsername(EMAIL_USERNAME);
						email.setPassword(EMAIL_PASSWORD);
						email.setSMTPServer(SMTP_SERVER);              
						email.setFrom(ALERT_ENGINE_EMAIL);
						String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
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
								LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, continue workflow - kickStartTask" + e.toString());

								// update the email sent of the alert to "fail"
								WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
							}
						}

						WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
											   Boolean.valueOf(true), tempRuntimeData, query);
					}
					else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
					{
						String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
						Task task = WorkflowManager.getWorkflowTask(strWorkflowTaskKey, query);
						String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
						WorkflowInstance workflowInstance = 
										WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
						String strWorkflowTaskName = task.getName();
						String strWorkflowInstanceName = workflowInstance.getName();
						String strEmailTemplate = null;
						String strMessageBody = null;

						
						//send alert to the administrator
						//task failed
						String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
											" in the workflow " + strWorkflowInstanceName + " has failed.";
						strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
						strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
															   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
															   "", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
						DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody, 
												ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE, "Task fail",
												strWorkflowTaskKey, SEND_EMAIL);
						String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);

						
						//send email
						Email email = new Email();
						email.setUsername(EMAIL_USERNAME);
						email.setPassword(EMAIL_PASSWORD);
						email.setSMTPServer(SMTP_SERVER);              
						email.setFrom(ALERT_ENGINE_EMAIL);
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
								LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, stop workflow - kickStartTask" + e.toString());
								
								// update the email sent of the alert to "fail"
								WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
							}
						}

						//workflow instance failed
						strSubject = "WORKFLOW ENGINE ALERT: - The workflow instance " + strWorkflowInstanceName + 
								 	 " has failed.";
						strEmailTemplate = WorkflowManager.getEmailTemplate("WORKFLOW_INSTANCE_FAILED");
						strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, "",
															   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
															   "", "", "", "");
						alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody,
													ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE,
													"Workflow instance fail", "", SEND_EMAIL);
						strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);

						email.clearEmail();
						email.setUsername(EMAIL_USERNAME);
						email.setPassword(EMAIL_PASSWORD);
						email.setSMTPServer(SMTP_SERVER);              
						email.setFrom(ALERT_ENGINE_EMAIL);
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
								LogService.log(LogService.ERROR, "Exception while executing send email for workflow that has failed to continue - kickStartTask" + e.toString());
								
								// update the email sent of the alert to "fail"
								WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
							}
						}

						WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
											   Boolean.valueOf(false), tempRuntimeData, query);
					}
					else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_RETRY))
					{
						for(int intIndex = 0; intIndex < (Integer.valueOf(strRetryCounter)).intValue(); intIndex++)
						{
							WorkflowManager.sleep(strRetryDelayUnit, strRetryDelayValue);

							//prepare and execute the function
							blExecutionResult = WorkflowManager.prepareAndExecuteFunction(strWorkflowTaskKey,
																						  tempRuntimeData, query);
							//the function only need to be successfully executed once 
							if(blExecutionResult.booleanValue())
							{
								break;
							}
						}
					
						//task completed succesfully and continue to the next task
						if(blExecutionResult.booleanValue())
						{
							String strWorkflowTaskFunctionKey = WorkflowManager.getFunctionKey(strWorkflowTaskKey, query);
							ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strWorkflowTaskFunctionKey,
																							 query);

							String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType"); 

							//only run finishTask when the function type is not equals to "Thread"
							if(!strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
							{
								WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
													   Boolean.valueOf(true), tempRuntimeData, query);
							}
							else
							{
								WorkflowManager.addWorkflowFunctionThread(strWorkflowTaskKey, strWorkflowInstanceKey, 
																		 WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
							}

							//WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
							//			   Boolean.valueOf(true), tempRuntimeData, query);
						}
						else
						{
							//task did not completed successfully and continue
							if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
							{
								WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
										   Boolean.valueOf(true), tempRuntimeData, query);
							}
							else if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
							{
								//stop the workflow instance
								WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
										   Boolean.valueOf(false), tempRuntimeData, query);
							}
						}
					} //retry
				} //the first execution of the function was failed
			} //this is a SYSTEM workflow task
			else if(strType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW))
			{
				ChannelRuntimeData tempRuntimeData =
							WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

				//use the strWorkflowTaskKey and get the value for sub_workflow_instance_key
				String strSubWorkflowInstanceKey = WorkflowManager.getSubWorkflowInstanceKey(strWorkflowTaskKey, query);
					

				//System.err.println("strSubWorkflowInstanceKey-1 = >>>" + strSubWorkflowInstanceKey + "<<<");
				
				//if the task is of type sub workflow and the sub workflow has not been instantiated,
				//then instantiated
				if(strSubWorkflowInstanceKey == null ||
				    (strSubWorkflowInstanceKey != null && strSubWorkflowInstanceKey.length() == 0))
				{
					String strDomain = tempRuntimeData.getParameter("strDomain");
					String strDomainKey = tempRuntimeData.getParameter("intDomainKey");

					//get the to be instantiated wf template key using the task key and instance key

					String strWorkflowActivityXPDLId =
								WorkflowManager.getWorkflowActivityXPDLId(strWorkflowTaskKey, strWorkflowInstanceKey, query);
					
					String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(strWorkflowInstanceKey, query);

					String strToBeInstantiatedSubWorkflowTemplateKey = 
						WorkflowManager.getSubWorkflowTemplateKey(strWorkflowTemplateKey, strWorkflowActivityXPDLId, query);

					//unable to specify the instantiation of a wf as thread because thread does not 
					//return anything and I require the wf instantiated keys!!
					//instantiate the wf template for a given template key and runtimedata
					Hashtable hashTemplatesInstancesKeys = WorkflowEngine.instantiateWorkflowTemplates(null, tempRuntimeData,
										strToBeInstantiatedSubWorkflowTemplateKey, strDomain, strDomainKey, false, query);
					
					//update the task with the subworkflow instance key
					//String strInstantiatedSubWorkflowInstanceKey = QueryChannel.getNewestKeyAsString(
					//									"WORKFLOW_INSTANCE_intWorkflowInstanceKey");

					Hashtable hashTemplateInstancesKeys = (Hashtable) hashTemplatesInstancesKeys.get(
																	 strToBeInstantiatedSubWorkflowTemplateKey);

					Vector vecWorkflowInstanceKeys = (Vector) hashTemplateInstancesKeys.get(
																	 strToBeInstantiatedSubWorkflowTemplateKey);
				
					//get at position 0 (zero) as there will only be 1 instance
					String strInstantiatedSubWorkflowInstanceKey = (String) vecWorkflowInstanceKeys.get(0);

					//set the sub_workflow_instance_key in the ix_workflow_task for a given workflow_task_key
					WorkflowManager.updateSubWorkflowInstanceKey(strInstantiatedSubWorkflowInstanceKey,
																 strWorkflowTaskKey, query);
				
					//set the sub workflow instance key
					strSubWorkflowInstanceKey = strInstantiatedSubWorkflowInstanceKey;
				}

				//System.err.println("strSubWorkflowInstanceKey-2 = >>>" + strSubWorkflowInstanceKey + "<<<");
				
				//get the initial task key
				String strInitialTaskKey = WorkflowManager.getInitialTaskKey(strSubWorkflowInstanceKey, query);

				//System.err.println("strInitialTaskKey = >>>" + strInitialTaskKey + "<<<");
				
				//get the workflow template key
				String strSubWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(strSubWorkflowInstanceKey, query);

				//System.err.println("strSubWorkflowTemplateKey = >>>" + strSubWorkflowTemplateKey + "<<<");

				Vector vecSubWorkflowTemplateKey = 
						WorkflowManager.validateTriggerData(null, tempRuntimeData, strSubWorkflowTemplateKey, query);

				//System.err.println("vecSubWorkflowTemplateKey = >>>" + vecSubWorkflowTemplateKey + "<<<");

				Enumeration enumSubWorkflowTemplateKey = vecSubWorkflowTemplateKey.elements();
				while(enumSubWorkflowTemplateKey.hasMoreElements())
				{
					//ChannelRuntimeData tempRuntimeData =
					//		WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

					//move the pointer one ahead in which that we know that this loop should only goes once
					enumSubWorkflowTemplateKey.nextElement();

					Enumeration enumParameterName = tempRuntimeData.keys();
					while(enumParameterName.hasMoreElements())
					{
						String strParameterName = (String) enumParameterName.nextElement();
						//String strParameterValue = (String) tempRuntimeData.get(strParameterName);
						String strParameterValue = tempRuntimeData.getParameter(strParameterName);
						WorkflowManager.updateWorkflowRuntimedata(strParameterName, strParameterValue,
																  strSubWorkflowInstanceKey, query);
					}
		
					//get the runtimedata
					tempRuntimeData = WorkflowManager.getWorkflowRuntimedata(strSubWorkflowInstanceKey, query);

					WorkflowEngine.kickStartTask(strInitialTaskKey, tempRuntimeData, query);
				}

				//WorkflowEngine.kickStartTask(strInitialTaskKey, runtimeData, query);

			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::kickStartTask - " + e.toString(), e);
        }
	}

	/**
	 * kick start a Failed thread task
	 *
	 * @param strWorkflowTaskKey	the workflow task key
	 * @param query 				the query object
	 */
	public static void kickStartFailedTaskThread(String strWorkflowTaskKey, DALSecurityQuery query)
	{
		try
		{
			// get the workflow instance key for a given task and update the status of WF instance
			String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strWorkflowTaskKey, query);

			int intNoOfExecuted = WorkflowManager.countNumberOfWorkflowFunctionThreadExecuted(strWorkflowTaskKey,
																	 							  strWorkflowInstanceKey);

			ChannelRuntimeData tempRuntimeData =
							WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

			//add workflow instance key into the tempRuntimeData, 
			//the workflow instance key will only be provided on runtime, 
			//so we do not need to store the actual value in the ix_workflow_runtimedata
			//the parameter name is still there because there is a type checking, if the parameter name is not in the
			//ix_workflow_runtimedata, the parameter will not be available for the system function
			tempRuntimeData.setParameter("strWorkflowInstanceKey", strWorkflowInstanceKey);	
			
			ResultSet rsSystemTaskFailureDetails =
					WorkflowManager.getSystemTaskFailureDetails(strWorkflowTaskKey, query);

			String strIfFail = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFail"); 
			String strRetryDelayUnit = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strRetryDelayUnit"); 
			String strRetryDelayValue = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_flRetryDelayValue"); 
			String strRetryCounter = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_intRetryCounter"); 
			String strIfFailAfterRetry = 
						rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFailAfterRetry");

			if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
			{
				//send alert to the administrator
				//task failed
				Task task = WorkflowManager.getWorkflowTask(strWorkflowTaskKey, query);
				String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
				WorkflowInstance workflowInstance = 
								WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
				String strWorkflowTaskName = task.getName();
				String strWorkflowInstanceName = workflowInstance.getName();
				String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
									" in the workflow " + strWorkflowInstanceName + " has failed.";
				String strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
				String strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
													   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
													   "", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
				DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody, ADMINISTRATOR_ORG_USER_KEY, 
										ADMINISTRATOR_ORG_USER_TYPE, "Task fail", strWorkflowTaskKey, SEND_EMAIL);
				String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);
				
				//send email
				Email email = new Email();
				email.setUsername(EMAIL_USERNAME);
				email.setPassword(EMAIL_PASSWORD);
				email.setSMTPServer(SMTP_SERVER);              
				email.setFrom(ALERT_ENGINE_EMAIL);
				String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
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
						LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, continue workflow - kickStartFailedTaskThread" + e.toString());

						// update the email sent of the alert to "fail"
						WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
					}
				}

				WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
									   Boolean.valueOf(true), tempRuntimeData, query);
			}
			else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
			{
				String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
				Task task = WorkflowManager.getWorkflowTask(strWorkflowTaskKey, query);
				String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
				WorkflowInstance workflowInstance = 
								WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
				String strWorkflowTaskName = task.getName();
				String strWorkflowInstanceName = workflowInstance.getName();
				String strEmailTemplate = null;
				String strMessageBody = null;

				
				//send alert to the administrator
				//task failed
				String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
									" in the workflow " + strWorkflowInstanceName + " has failed.";
				strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
				strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
													   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
													   "", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
				DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody, ADMINISTRATOR_ORG_USER_KEY , 
										ADMINISTRATOR_ORG_USER_TYPE, "Task fail", strWorkflowTaskKey, SEND_EMAIL);
				String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);

				
				//send email
				Email email = new Email();
				email.setUsername(EMAIL_USERNAME);
				email.setPassword(EMAIL_PASSWORD);
				email.setSMTPServer(SMTP_SERVER);              
				email.setFrom(ALERT_ENGINE_EMAIL);
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
						LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, stop workflow - kickStartFailedTaskThread" + e.toString());
						
						// update the email sent of the alert to "fail"
						WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
					}
				}

				//workflow instance failed
				strSubject = "WORKFLOW ENGINE ALERT: - The workflow instance " + strWorkflowInstanceName + 
							 " has failed.";
				strEmailTemplate = WorkflowManager.getEmailTemplate("WORKFLOW_INSTANCE_FAILED");
				strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, "",
													   strWorkflowInstanceName, strTempWorkflowInstanceKey, "", 
													   "", "", "", "");
				alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody, ADMINISTRATOR_ORG_USER_KEY , 
											ADMINISTRATOR_ORG_USER_TYPE, "Workflow instance fail", "", SEND_EMAIL);
				strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);

				email.clearEmail();
				email.setUsername(EMAIL_USERNAME);
				email.setPassword(EMAIL_PASSWORD);
				email.setSMTPServer(SMTP_SERVER);              
				email.setFrom(ALERT_ENGINE_EMAIL);
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
						LogService.log(LogService.ERROR, "Exception while executing send email for workflow that has failed to continue - kickStartFailedTaskThread" + e.toString());
						
						// update the email sent of the alert to "fail"
						WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
					}
				}

				WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
									   Boolean.valueOf(false), tempRuntimeData, query);
			}
			else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_RETRY))
			{

//System.err.println("retrying!!!");
//System.err.println("Integer.valueOf(strRetryCounter).intValue() = " + Integer.valueOf(strRetryCounter).intValue());
//System.err.println("intNoOfExecuted = " + intNoOfExecuted);
				
				if(Integer.valueOf(strRetryCounter).intValue() > intNoOfExecuted)
				{
//System.err.println("about to sleep for " + strRetryDelayValue + strRetryDelayUnit);
					WorkflowManager.sleep(strRetryDelayUnit, strRetryDelayValue);
//System.err.println("after sleeping for " + strRetryDelayValue + strRetryDelayUnit);
					
					WorkflowManager.addWorkflowFunctionThread(strWorkflowTaskKey, strWorkflowInstanceKey, 
																 WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
					//blExecutionResult = 
					WorkflowManager.prepareAndExecuteFunction(strWorkflowTaskKey,
															  tempRuntimeData,
															  query);
				}
				else if(Integer.valueOf(strRetryCounter).intValue() <= intNoOfExecuted)
				{
					//task did not completed successfully and continue
					if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
					{
						WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
								   Boolean.valueOf(true), tempRuntimeData, query);
					}
					else if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
					{
						//stop the workflow instance
						WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
								   Boolean.valueOf(false), tempRuntimeData, query);
					}
				}
			}//retry
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::kickStartFailedTaskThread - " + e.toString(), e);
        }
	}

	/**
	 * kick start a thread task
	 *
	 * @param strWorkflowTaskKey	the workflow task key
	 * @param query 				the query object
	 */
	public static void kickStartTaskThread(String strWorkflowTaskKey, DALSecurityQuery query)
	{
		try
		{
			// get the workflow instance key for a given task and update the status of WF instance
			String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strWorkflowTaskKey, query);

			ChannelRuntimeData tempRuntimeData =
							WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

			//add workflow instance key into the tempRuntimeData, 
			//the workflow instance key will only be provided on runtime, 
			//so we do not need to store the actual value in the ix_workflow_runtimedata
			//the parameter name is still there because there is a type checking, if the parameter name is not in the
			//ix_workflow_runtimedata, the parameter will not be available for the system function
			tempRuntimeData.setParameter("strWorkflowInstanceKey", strWorkflowInstanceKey);	
	

			//WorkflowManager.addWorkflowFunctionThread(strWorkflowTaskKey, strWorkflowInstanceKey, 
			//													 WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
			
			Boolean blExecutionResult =
						WorkflowManager.prepareAndExecuteFunction(strWorkflowTaskKey, tempRuntimeData, query);

			//if the function was successful and do the next task
			if(blExecutionResult.booleanValue())
			{
				//String strWorkflowTaskFunctionKey = WorkflowManager.getFunctionKey(strWorkflowTaskKey, query);
				//ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strWorkflowTaskFunctionKey, query);

				//String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType"); 

				//only run finishTask when the function type is not equals to "Thread"
				//if(!strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
				//{
					//WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
					//					   Boolean.valueOf(true), tempRuntimeData, query);
				//}
				//else
				//{
//System.err.println("about to add an entry in the workflow function thread table!!!!");
					WorkflowManager.addWorkflowFunctionThread(strWorkflowTaskKey, strWorkflowInstanceKey, 
																 WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
				//}

				//WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
				//					   	  Boolean.valueOf(true), tempRuntimeData, query);
			}
			else
			{
				ResultSet rsSystemTaskFailureDetails =
							WorkflowManager.getSystemTaskFailureDetails(strWorkflowTaskKey, query);

				String strIfFail = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFail"); 
				String strRetryDelayUnit = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strRetryDelayUnit"); 
				String strRetryDelayValue = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_flRetryDelayValue"); 
				String strRetryCounter = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_intRetryCounter"); 
				String strIfFailAfterRetry = 
								rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFailAfterRetry");

				if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
				{
					//send alert to the administrator
					//task failed
					Task task = WorkflowManager.getWorkflowTask(strWorkflowTaskKey, query);
					String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
					WorkflowInstance workflowInstance = 
									WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
					String strWorkflowTaskName = task.getName();
					String strWorkflowInstanceName = workflowInstance.getName();
					String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
										" in the workflow " + strWorkflowInstanceName + " has failed.";
					String strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
					String strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
														   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
														   "", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
					DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody,
											ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE, "Task fail",
											strWorkflowTaskKey, SEND_EMAIL);
					String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);
					
					//send email
					Email email = new Email();
					email.setUsername(EMAIL_USERNAME);
					email.setPassword(EMAIL_PASSWORD);
					email.setSMTPServer(SMTP_SERVER);              
					email.setFrom(ALERT_ENGINE_EMAIL);
					String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
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
							LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, continue workflow - kickStartTaskThread" + e.toString());

							// update the email sent of the alert to "fail"
							WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
						}
					}

					WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
										   Boolean.valueOf(true), tempRuntimeData, query);
				}
				else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
				{
					String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
					Task task = WorkflowManager.getWorkflowTask(strWorkflowTaskKey, query);
					String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
					WorkflowInstance workflowInstance = 
									WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
					String strWorkflowTaskName = task.getName();
					String strWorkflowInstanceName = workflowInstance.getName();
					String strEmailTemplate = null;
					String strMessageBody = null;

					
					//send alert to the administrator
					//task failed
					String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
										" in the workflow " + strWorkflowInstanceName + " has failed.";
					strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
					strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
														   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
														   "", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
					DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody,
											ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE, "Task fail",
											strWorkflowTaskKey, SEND_EMAIL);
					String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);

					
					//send email
					Email email = new Email();
					email.setUsername(EMAIL_USERNAME);
					email.setPassword(EMAIL_PASSWORD);
					email.setSMTPServer(SMTP_SERVER);              
					email.setFrom(ALERT_ENGINE_EMAIL);
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
							LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, stop workflow - kickStartTaskThread" + e.toString());
							
							// update the email sent of the alert to "fail"
							WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
						}
					}

					//workflow instance failed
					strSubject = "WORKFLOW ENGINE ALERT: - The workflow instance " + strWorkflowInstanceName + 
								 " has failed.";
					strEmailTemplate = WorkflowManager.getEmailTemplate("WORKFLOW_INSTANCE_FAILED");
					strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, "",
														   strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
														   "", "", "", "");
					alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody, ADMINISTRATOR_ORG_USER_KEY , 
												ADMINISTRATOR_ORG_USER_TYPE, "Workflow instance fail", "", SEND_EMAIL);
					strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);

					email.clearEmail();
					email.setUsername(EMAIL_USERNAME);
					email.setPassword(EMAIL_PASSWORD);
					email.setSMTPServer(SMTP_SERVER);              
					email.setFrom(ALERT_ENGINE_EMAIL);
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
							LogService.log(LogService.ERROR, "Exception while executing send email for workflow that has failed to continue - kickStartTaskThread" + e.toString());
							
							// update the email sent of the alert to "fail"
							WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
						}
					}

					WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
										   Boolean.valueOf(false), tempRuntimeData, query);
				}
				else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_RETRY))
				{
					for(int intIndex = 0; intIndex < (Integer.valueOf(strRetryCounter)).intValue(); intIndex++)
					{
						WorkflowManager.sleep(strRetryDelayUnit, strRetryDelayValue);

						//prepare and execute the function
						blExecutionResult = WorkflowManager.prepareAndExecuteFunction(strWorkflowTaskKey,
																					  tempRuntimeData, query);
						//the function only need to be successfully executed once 
						if(blExecutionResult.booleanValue())
						{
							break;
						}
					}
				
					//task completed succesfully and continue to the next task
					if(blExecutionResult.booleanValue())
					{
						//String strWorkflowTaskFunctionKey = WorkflowManager.getFunctionKey(strWorkflowTaskKey, query);
						//ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strWorkflowTaskFunctionKey,
						//																 query);

						//String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType"); 

						//only run finishTask when the function type is not equals to "Thread"
						//if(!strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
						//{
						//	WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
						//						   Boolean.valueOf(true), tempRuntimeData, query);
						//}
						//else
						//{
							WorkflowManager.addWorkflowFunctionThread(strWorkflowTaskKey, strWorkflowInstanceKey, 
																	 WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
						//}

						//WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
						//			   Boolean.valueOf(true), tempRuntimeData, query);
					}
					else
					{
						//task did not completed successfully and continue
						if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
						{
							WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
									   Boolean.valueOf(true), tempRuntimeData, query);
						}
						else if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
						{
							//stop the workflow instance
							WorkflowEngine.finishTask(strWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
									   Boolean.valueOf(false), tempRuntimeData, query);
						}
					}
				} //retry
			} //the first execution of the function was failed
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::kickStartTaskThread - " + e.toString(), e);
        }
	}

	// TASK RELATED METHODS

	/**
	 * to indicate that a task has been completed and to move to the next task 
	 *
	 * @param strFinishedWorkflowTaskKey 	the current completed workflow task key
	 * @param strFinishedWorkflowTaskStatus	the current completed workflow task status
	 * @param blContinue					indicate whether the next task should be executed or not
	 * @param runtimeData					the runtime data
	 * @param query							the query object
	 */
	public static void finishTask(String strFinishedWorkflowTaskKey, String strFinishedWorkflowTaskStatus,
								  Boolean blContinue, ChannelRuntimeData runtimeData, DALSecurityQuery query)
	{
		try
		{
			//to indicate whether this is the last task in the workflow or not
			boolean blLastTask = false;

			//to indicate whether this is the last task in the workflow or not
			//boolean blEndTask = false;
			
			//set the task in handovering mode
			WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_YES, strFinishedWorkflowTaskKey,
															 query);

			// update ix_task, status, date complete
			WorkflowManager.updateTaskStatus(strFinishedWorkflowTaskStatus, strFinishedWorkflowTaskKey, query);

			Calendar currentTime = Calendar.getInstance();
			String strToday = WorkflowManager.getDate(currentTime) + "/" + 
							  WorkflowManager.getMonth(currentTime) + "/" + 
							  currentTime.get(Calendar.YEAR); 

			String strCurrentTime = currentTime.get(Calendar.HOUR_OF_DAY) + ":" +
									currentTime.get(Calendar.MINUTE) + ":" +
									currentTime.get(Calendar.SECOND);

			WorkflowManager.updateTaskCompletedDate(strToday, strFinishedWorkflowTaskKey, query);
			WorkflowManager.updateTaskCompletedTime(strCurrentTime, strFinishedWorkflowTaskKey, query);

			//get the next task
			Vector vecNextTaskKeys = WorkflowManager.getNextTaskKeys(strFinishedWorkflowTaskKey, query);

			Vector vecSortedNextTaskKeys = WorkflowManager.sortTaskKeys(vecNextTaskKeys, query);

			/*
			//check whether if any of the next task is an end task
			for(int intIndex = 0; intIndex < vecNextTaskKeys.size(); intIndex++)
			{
				//get the next task key
				String strNextTaskKey = (String) vecNextTaskKeys.get(intIndex);

				//get the next task type
				String strNextTaskType = WorkflowManager.getTaskType(strNextTaskKey, query);

				if(strNextTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_STOP))
				{
					//blContinue = Boolean.valueOf(false);
					blLastTask = true;
				}
				else
				//if(!strNextTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_STOP))
				{
					//blContinue = Boolean.valueOf(false);
					blLastTask = false;
					break;
				}
				
			}
			*/

//System.err.println("blLastTask = " + blLastTask);

			for(int intIndex = 0; intIndex < vecSortedNextTaskKeys.size() && blContinue.booleanValue() &&
								  blLastTask == false; intIndex++)
			{
				//not the last task
				//blLastTask = false;
			
				//get the next task key
				String strNextTaskKey = (String) vecSortedNextTaskKeys.get(intIndex);

//System.err.println("strNextTaskKey = " + strNextTaskKey);

				//get the task transition type
				String strTaskTransitionType = 
						WorkflowManager.getTaskTransitionType(strFinishedWorkflowTaskKey, strNextTaskKey, query);

				String strTaskStatus = WorkflowManager.getTaskStatus(strNextTaskKey, query);

//System.err.println("strTaskStatus = " + strTaskStatus);

				//only kick start the task if the transition is normal and the next task status is not active 
				if(strTaskTransitionType.equals(WorkflowManager.TRANSITION_TYPE_NORMAL) &&
				   !strTaskStatus.equals(WorkflowManager.TASK_STATUS_NOT_ACTIVE))
				{
					continue;
				}

				//get the next task type
				String strNextTaskType = WorkflowManager.getTaskType(strNextTaskKey, query);

//System.err.println("strNextTaskType = " + strNextTaskType);
				
				//get the condition to get to the next task
				String strCondition = WorkflowManager.getTaskCondition(strFinishedWorkflowTaskKey, strNextTaskKey, query);
			
				//System.err.println("strCondition = >>>" + strCondition + "<<<");
//System.err.println("strCondition = " + strCondition);

				boolean blCompletedTask = true;
				if(strTaskTransitionType.equals(WorkflowManager.TRANSITION_TYPE_NORMAL) &&
				   !strNextTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_STOP))
				{
					//get the completed task for the next task key
					int intCompletedTask = WorkflowManager.getCompletedTask(strNextTaskKey, query);

//System.err.println("intCompletedTask = " + intCompletedTask);

					//get all the origin task keys that is going to the next task key
					Vector vecOriginTaskKeys = WorkflowManager.getPreviousTaskKeys(strNextTaskKey, query);

					//check the completed task
					blCompletedTask = WorkflowManager.checkCompletedTask(vecOriginTaskKeys,
																		 strNextTaskKey, intCompletedTask, query);

//System.err.println("blCompletedTask = " + blCompletedTask);
				}

				//get the parameters type
				//Hashtable hashFinishedParameterType = WorkflowManager.getParameterType(strFinishedWorkflowTaskKey, query);
				
				//get the workflow instance key
				String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strFinishedWorkflowTaskKey, query);

				//get the workflow template key
			    String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(strWorkflowInstanceKey, query);

				//get the template parameters type
				Hashtable hashTemplateParameterType = 
							WorkflowManager.getWorkflowTemplateParameterType(strWorkflowTemplateKey, query);
				
				//get the runtime data for a given workflow instance key
				ChannelRuntimeData tempRuntimeData =
							WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

//System.err.println("strWorkflowInstanceKey = " + strWorkflowInstanceKey);
//System.err.println("strWorkflowTemplateKey = " + strWorkflowTemplateKey);
//System.err.println("hashTemplateParameterType = " + hashTemplateParameterType);
//System.err.println("tempRuntimeData = " + tempRuntimeData);

				//check the condition
				//boolean blCondition = WorkflowManager.checkCondition(strCondition, tempRuntimeData, hashFinishedParameterType);
				boolean blCondition = WorkflowManager.checkCondition(strCondition, tempRuntimeData, hashTemplateParameterType);

//System.err.println("blCondition = " + blCondition);

				//get the origin task type
				String strOriginTaskType = WorkflowManager.getTaskType(strFinishedWorkflowTaskKey, query);

//System.err.println("strOriginTaskType = " + strOriginTaskType);
				
				//Used when the origin task is a human task and actions have been pre-selected
				//and no condition need to be satisfied
				boolean blHumanActionCondition = true;

				//if the origin task type is a human
				if(strOriginTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_HUMAN))
				{
					//if no condition
					if(strCondition == null || strCondition.equals(""))
					{
						String strTaskTransitionName = WorkflowManager.getTaskTransitionName(
														strFinishedWorkflowTaskKey, strNextTaskKey, query);	

//System.err.println("strTaskTransitionName = " + strTaskTransitionName);
						
						//if there is no transition name
						if(strTaskTransitionName != null && !(strTaskTransitionName.equals("")))
						{
							String strAction = WorkflowManager.getTaskAction(strFinishedWorkflowTaskKey, query);

//System.err.println("strAction = " + strAction);
					
							//if the transition name is not part of the action set the blHumanActionCondition = false
							if(strAction != null && strAction.indexOf(strTaskTransitionName) == -1)
							{
//System.err.println("setting the blHumanActionCondition = false 1");
								blHumanActionCondition = false;
							}
							else if(strAction == null) //if no action being selected
							{
//System.err.println("setting the blHumanActionCondition = false 2");
								blHumanActionCondition = false;
							}
						}
					}
				}

//System.err.println("blHumanActionCondition = " + blHumanActionCondition);

				if(strTaskTransitionType.equals(WorkflowManager.TRANSITION_TYPE_NORMAL))
				{
					//if human, update ix_task, status, date received
					if(strNextTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_HUMAN) &&
							blCondition && blCompletedTask && blHumanActionCondition)
					{
						WorkflowManager.updateTaskStatus(WorkflowManager.TASK_STATUS_ACTIVE, strNextTaskKey, query);
						WorkflowManager.updateTaskReceivedDate(strToday, strNextTaskKey, query);
						WorkflowManager.updateTaskReceivedTime(strCurrentTime, strNextTaskKey, query);

						//set the task to not in handovering mode
						WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO, 
																		 strFinishedWorkflowTaskKey, query);
					}
					else if(strNextTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM) && 
							blCondition && blCompletedTask && blHumanActionCondition)
					{
						WorkflowManager.updateTaskStatus(WorkflowManager.TASK_STATUS_ACTIVE, strNextTaskKey, query);
						WorkflowManager.updateTaskReceivedDate(strToday, strNextTaskKey, query);
						WorkflowManager.updateTaskReceivedTime(strCurrentTime, strNextTaskKey, query);

						//set the task to not in handovering mode
						WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO, 
																		 strFinishedWorkflowTaskKey, query);
				
						//get the workflow instance key
						strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strNextTaskKey, query);

						//get the runtime data for a given workflow instance key
						tempRuntimeData = WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

						//add workflow instance key into the tempRuntimeData
						tempRuntimeData.setParameter("strWorkflowInstanceKey", strWorkflowInstanceKey);
						
						//prepare and execute the function
						Boolean blExecutionResult = WorkflowManager.prepareAndExecuteFunction(strNextTaskKey, 
																							  tempRuntimeData, query);	

                                                System.err.println("blExecutionResult = >>>" + blExecutionResult.booleanValue() + "<<<");
                                                
						//if the function was successful and do the next task
						if(blExecutionResult.booleanValue())
						{
							String strWorkflowTaskFunctionKey = WorkflowManager.getFunctionKey(strNextTaskKey, query);
							ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strWorkflowTaskFunctionKey,
																							 query);

							String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType"); 

							//only run finishTask when the function type is not equals to "Thread"
							if(!strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
							{
								WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
													   Boolean.valueOf(true), tempRuntimeData, query);
							}
							else
							{
//System.err.println("finishTask - about to add an entry in the workflow function thread table!!!!");
								WorkflowManager.addWorkflowFunctionThread(strNextTaskKey , strWorkflowInstanceKey, 
																		  WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
							}
					
							//WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
							//						   Boolean.valueOf(true), tempRuntimeData, query);
						}
						else
						{
							ResultSet rsSystemTaskFailureDetails =
											WorkflowManager.getSystemTaskFailureDetails(strNextTaskKey, query);

							String strIfFail = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFail"); 
							String strRetryDelayUnit = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strRetryDelayUnit"); 
							String strRetryDelayValue = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_flRetryDelayValue"); 
							String strRetryCounter = rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_intRetryCounter"); 
							String strIfFailAfterRetry =
											rsSystemTaskFailureDetails.getString("WORKFLOW_TASK_strIfFailAfterRetry"); 

							if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
							{
								//send alert to the administrator
								//task failed
								Task task = WorkflowManager.getWorkflowTask(strNextTaskKey, query);
								String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
								WorkflowInstance workflowInstance = 
												WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
								String strWorkflowTaskName = task.getName();
								String strWorkflowInstanceName = workflowInstance.getName();
								String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
													" in the workflow " + strWorkflowInstanceName + " has failed.";
								String strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
								String strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
														strWorkflowInstanceName, strTempWorkflowInstanceKey, "", 
														"", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
								DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody,
														ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE,
														"Task fail", strNextTaskKey, SEND_EMAIL);
								String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);
							

								Email email = new Email();
								email.setUsername(EMAIL_USERNAME);
								email.setPassword(EMAIL_PASSWORD);
								email.setSMTPServer(SMTP_SERVER);              
								email.setFrom(ALERT_ENGINE_EMAIL);
								String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
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
										LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, continue workflow - finishTask" + e.toString());
								
										// update the email sent of the alert to "fail"
										WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
									}
								}

								WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_FAILED,
													   Boolean.valueOf(true), tempRuntimeData, query);
							}
							else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
							{
								String[] arrTriggerEmailRecipients = {ADMINISTRATOR_EMAIL};
								Task task = WorkflowManager.getWorkflowTask(strNextTaskKey, query);
								String strTempWorkflowInstanceKey = task.getWorkflowInstanceKey();
								WorkflowInstance workflowInstance = 
												WorkflowManager.getWorkflowInstance(strTempWorkflowInstanceKey, query);
								String strWorkflowTaskName = task.getName();
								String strWorkflowInstanceName = workflowInstance.getName();
								String strEmailTemplate = null;
								String strMessageBody = null;


								//send alert to the administrator
								//task failed
								String strSubject = "WORKFLOW ENGINE ALERT: - The task " + strWorkflowTaskName + 
													" in the workflow " + strWorkflowInstanceName + " has failed.";
								strEmailTemplate = WorkflowManager.getEmailTemplate("TASK_FAILED");
								strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, strWorkflowTaskName,
															strWorkflowInstanceName, strTempWorkflowInstanceKey, "",
															"", "", "", WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM);
								DALQuery alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody,
															ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE,
															"Task fail", strNextTaskKey, SEND_EMAIL);
								String strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);	
								
								Email email = new Email();
								email.setUsername(EMAIL_USERNAME);
								email.setPassword(EMAIL_PASSWORD);
								email.setSMTPServer(SMTP_SERVER);              
								email.setFrom(ALERT_ENGINE_EMAIL);
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
										LogService.log(LogService.ERROR, "Exception while executing send email for system task that has failed to be executed, stop workflow - finishTask" + e.toString());

										// update the email sent of the alert to "fail"
										WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
									}
								}

								//workflow instance failed
								strSubject = "WORKFLOW ENGINE ALERT: - The workflow instance " + strWorkflowInstanceName + 
											 " has failed.";
								strEmailTemplate = WorkflowManager.getEmailTemplate("WORKFLOW_INSTANCE_FAILED");
								strMessageBody = WorkflowManager.getMessageBody(strEmailTemplate, "",
												 strWorkflowInstanceName, strTempWorkflowInstanceKey, "", "", "", "", "");
								alertQuery = WorkflowManager.createAlert(strSubject, strMessageBody,
													ADMINISTRATOR_ORG_USER_KEY, ADMINISTRATOR_ORG_USER_TYPE,
													"Workflow instance fail", "", SEND_EMAIL);
								strAlertKey = QueryChannel.getNewestKeyAsString(alertQuery);		
								
								email.clearEmail();
								email.setUsername(EMAIL_USERNAME);
								email.setPassword(EMAIL_PASSWORD);
								email.setSMTPServer(SMTP_SERVER);              
								email.setFrom(ALERT_ENGINE_EMAIL);
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
										LogService.log(LogService.ERROR, "Exception while executing send email for a workflow that has failed to continue - finishTask" + e.toString());

										// update the email sent of the alert to "fail"
										WorkflowManager.updateAlertEmailSent(strAlertKey, "fail");
									}
								}

								WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_FAILED,
													   Boolean.valueOf(false), tempRuntimeData, query);
							}
							else if(strIfFail.equals(WorkflowManager.WORKFLOW_TASK_FAIL_RETRY))
							{
								for(int intIndex3 = 0; intIndex3 < (Integer.valueOf(strRetryCounter)).intValue(); intIndex3++)
								{
									WorkflowManager.sleep(strRetryDelayUnit, strRetryDelayValue);

									//prepare and execute the function
									blExecutionResult = WorkflowManager.prepareAndExecuteFunction(strNextTaskKey,
																							tempRuntimeData, query);
									
									//the function only need to be successfully executed once 
									if(blExecutionResult.booleanValue())
									{
										break;
									}
								}
							
								//task completed succesfully and continue to the next task
								if(blExecutionResult.booleanValue())
								{
									String strWorkflowTaskFunctionKey = WorkflowManager.getFunctionKey(strNextTaskKey,
																									   query);
									ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(
																	strWorkflowTaskFunctionKey, query);

									String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType"); 

									//only run finishTask when the function type is not equals to "Thread"
									if(!strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
									{
										WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
															   Boolean.valueOf(true), tempRuntimeData, query);
									}
									else
									{
										WorkflowManager.addWorkflowFunctionThread(strNextTaskKey, strWorkflowInstanceKey, 
																		WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING);
									}

									//WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
									//			   Boolean.valueOf(true), tempRuntimeData, query);
								}
								else
								{
									//task did not completed successfully and continue
									if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_CONTINUE))
									{
										WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_FAILED,
												   Boolean.valueOf(true), tempRuntimeData, query);
									}
									else if(strIfFailAfterRetry.equals(WorkflowManager.WORKFLOW_TASK_FAIL_STOP))
									{
										//stop the workflow instance
										WorkflowEngine.finishTask(strNextTaskKey, WorkflowManager.TASK_STATUS_FAILED,
												   Boolean.valueOf(false), tempRuntimeData, query);
									}
								}
							} //retry
						} //the first execution of the function was failed
					} //this is a SYSTEM workflow task
					else if(strNextTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW) &&
										blCondition && blCompletedTask && blHumanActionCondition)
					{
						//do nothing yet
						WorkflowManager.updateTaskStatus(WorkflowManager.TASK_STATUS_ACTIVE, strNextTaskKey, query);
						WorkflowManager.updateTaskReceivedDate(strToday, strNextTaskKey, query);
						WorkflowManager.updateTaskReceivedTime(strCurrentTime, strNextTaskKey, query);

						//set the task to not in handovering mode
						WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO, 
																		 strFinishedWorkflowTaskKey, query);

						//WorkflowManager.finishTask(strNextTaskKey, TASK_STATUS_COMPLETED,
						//						   Boolean.valueOf(true), runtimeData, query);

						//ChannelRuntimeData tempRuntimeData =
						//	WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);
					
						//define the sub workflow instance key that is going to be instantiated
						//String strSubWorkflowInstanceKey = null;
		
						//use the strNextTaskKey and get the value for sub_workflow_instance_key
						String strNextSubWorkflowInstanceKey = WorkflowManager.getSubWorkflowInstanceKey(strNextTaskKey,
																										 query);
						
						//System.err.println("strNextSubWorkflowInstanceKey-1 = >>>" + strNextSubWorkflowInstanceKey + "<<<");

						//if the task is of type sub workflow and the sub workflow has not been instantiated,
						//then instantiated
						if(strNextSubWorkflowInstanceKey == null ||
						   (strNextSubWorkflowInstanceKey != null && strNextSubWorkflowInstanceKey.length() == 0))
						{
							String strDomain = tempRuntimeData.getParameter("strDomain");
							String strDomainKey = tempRuntimeData.getParameter("intDomainKey");

							//get the to be instantiated wf template key using the task key and instance key

							String strWorkflowActivityXPDLId = WorkflowManager.getWorkflowActivityXPDLId(strNextTaskKey, strWorkflowInstanceKey, query);
							
							//String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(strWorkflowInstanceKey, query);

							String strToBeInstantiatedSubWorkflowTemplateKey = WorkflowManager.getSubWorkflowTemplateKey(strWorkflowTemplateKey, strWorkflowActivityXPDLId, query);

							//unable to specify the instantiation of a wf as thread because thread does not 
							//return anything and I require the wf instantiated keys!!
							//instantiate the wf template for a given template key and runtimedata
							Hashtable hashTemplatesInstancesKeys = WorkflowEngine.instantiateWorkflowTemplates(null,
									tempRuntimeData, strToBeInstantiatedSubWorkflowTemplateKey, strDomain, 
									strDomainKey, false, query);

							//update the task with the subworkflow instance key
							//String strInstantiatedSubWorkflowInstanceKey = QueryChannel.getNewestKeyAsString(
							//									"WORKFLOW_INSTANCE_intWorkflowInstanceKey");

							Hashtable hashTemplateInstancesKeys = (Hashtable) hashTemplatesInstancesKeys.get(
																			 strToBeInstantiatedSubWorkflowTemplateKey);

							Vector vecWorkflowInstanceKeys = (Vector) hashTemplateInstancesKeys.get(
																			 strToBeInstantiatedSubWorkflowTemplateKey);
						
							//get at position 0 (zero) as there will only be 1 instance
							String strInstantiatedSubWorkflowInstanceKey = (String) vecWorkflowInstanceKeys.get(0);

							//set the sub_workflow_instance_key in the ix_workflow_task for a given workflow_task_key
							WorkflowManager.updateSubWorkflowInstanceKey(strInstantiatedSubWorkflowInstanceKey, strNextTaskKey, query);
							strNextSubWorkflowInstanceKey = strInstantiatedSubWorkflowInstanceKey;
						}
						
						//get the sub workflow instance key
						//String strSubWorkflowInstanceKey = WorkflowManager.getSubWorkflowInstanceKey(strNextTaskKey, query);
						
						//System.err.println("strNextSubWorkflowInstanceKey-2 = >>>" + strNextSubWorkflowInstanceKey + "<<<");

						//get the initial task key
						String strInitialTaskKey = WorkflowManager.getInitialTaskKey(strNextSubWorkflowInstanceKey, query);
						
						//System.err.println("strInitialTaskKey = >>>" + strInitialTaskKey + "<<<");

						//get the workflow template key
						String strSubWorkflowTemplateKey = 
									WorkflowManager.getWorkflowTemplateKey(strNextSubWorkflowInstanceKey, query);
						
						//System.err.println("strSubWorkflowTemplateKey = >>>" + strSubWorkflowTemplateKey + "<<<");

						Vector vecSubWorkflowTemplateKey = 
								WorkflowManager.validateTriggerData(null, tempRuntimeData, strSubWorkflowTemplateKey, query);

						//System.err.println("vecSubWorkflowTemplateKey = >>>" + vecSubWorkflowTemplateKey + "<<<");

						Enumeration enumSubWorkflowTemplateKey = vecSubWorkflowTemplateKey.elements();
						while(enumSubWorkflowTemplateKey.hasMoreElements())
						{
							//move the pointer one ahead in which that we know that this loop should only goes once
							enumSubWorkflowTemplateKey.nextElement();
							
							strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strNextTaskKey, query);

							tempRuntimeData = WorkflowManager.getWorkflowRuntimedata(strWorkflowInstanceKey, query);

							Enumeration enumParameterName = tempRuntimeData.keys();
							while(enumParameterName.hasMoreElements())
							{
								String strParameterName = (String) enumParameterName.nextElement();
								//String strParameterValue = (String) tempRuntimeData.get(strParameterName);
								String strParameterValue = tempRuntimeData.getParameter(strParameterName);
								WorkflowManager.updateWorkflowRuntimedata(strParameterName, strParameterValue,
																		  strNextSubWorkflowInstanceKey, query);
							}

							//get the runtimedata
							tempRuntimeData = WorkflowManager.getWorkflowRuntimedata(strNextSubWorkflowInstanceKey, query);
							
							WorkflowEngine.kickStartTask(strInitialTaskKey, tempRuntimeData, query);
						}

						//WorkflowEngine.kickStartTask(strInitialTaskKey, runtimeData, query);
					}
					else if(strNextTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_STOP) &&
										blCondition && blCompletedTask && blHumanActionCondition)
					{
						blLastTask = true;
						
						//set the task to not in handovering mode
						WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO, 
																		 strFinishedWorkflowTaskKey, query);
						break;
					}
				}
				else if(strTaskTransitionType.equals(WorkflowManager.TRANSITION_TYPE_REJECT))
				{
					//transition condition and the human action condition has to be satisfied first.
					if(blCondition && blHumanActionCondition)
					{
						//1. get all the task transition for a given workflow instance key into a hashtable,
						//reverse the allocation the target is to be place in key and origin is to be place in element
						strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strNextTaskKey, query);

//System.err.println("strWorkflowInstanceKey = " + strWorkflowInstanceKey);
						
						Hashtable hashReversedTaskTransition = 
								WorkflowManager.getReversedTaskTransition(strWorkflowInstanceKey, query);
			
//System.err.println("hashReversedTaskTransition = " + hashReversedTaskTransition);
						
						Vector vecClearedTask = new Vector();
						vecClearedTask.add(strFinishedWorkflowTaskKey);

//System.err.println("vecClearedTask = " + vecClearedTask);

						Vector vecCurrentMainTaskKey = new Vector();
						vecCurrentMainTaskKey.add(strFinishedWorkflowTaskKey);

//System.err.println("vecCurrentMainTaskKey = " + vecCurrentMainTaskKey);
						
						boolean blLastTaskToBeDeleted = false;
						
						for(int intIndex1 = 0; intIndex1 < vecClearedTask.size(); intIndex1++)
						{
//System.err.println("");
//System.err.println("");
//System.err.println("intIndex1 = " + intIndex1);

							String strWorkflowTaskKey = (String) vecClearedTask.elementAt(intIndex1);
							String strWorkflowTaskType = WorkflowManager.getTaskType(strWorkflowTaskKey, query);
							String strWorkflowTaskStatus = WorkflowManager.getTaskStatus(strWorkflowTaskKey, query);

//System.err.println("strWorkflowTaskKey = " + strWorkflowTaskKey);
//System.err.println("strWorkflowTaskType = " + strWorkflowTaskType);
//System.err.println("strWorkflowTaskStatus = " + strWorkflowTaskStatus);
							
							if(!strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW))
							{
//System.err.println("");
//System.err.println("this is a normal human/system task only");

								if(!strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_START) &&
								   !strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_STOP) &&	
								   !strWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_NOT_ACTIVE))
								{
//System.err.println("the task is not NOT active therefore i'm going to copy the task to task history AND reset the task");
									WorkflowManager.copyWorkflowTaskToWorkflowTaskHistory(strWorkflowTaskKey, query);
									WorkflowManager.resetWorkflowTask(strWorkflowTaskKey, query);
								}

								if(strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM))
								{
									String strFunctionKey = WorkflowManager.getFunctionKey(strWorkflowTaskKey, query);
									ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strFunctionKey, query);
//System.err.println("");
//System.err.println("strFunctionKey = " + strFunctionKey);
			
									String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType");

//System.err.println("strFunctionType = " + strFunctionType);
//System.err.println("strWorkflowTaskKey = " + strWorkflowTaskKey);
//System.err.println("strWorkflowInstanceKey = " + strWorkflowInstanceKey);
//System.err.println("");
									if(strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
									{
										//delete that thread entries in the 
										WorkflowManager.deleteWorkflowFunctionThread(strWorkflowTaskKey, 
																					 strWorkflowInstanceKey);
									}
								}
							}
							else if(strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW))
							{
//System.err.println("");
//System.err.println("this is a subworkflow task");

								if(!strWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_NOT_ACTIVE))
								{
//System.err.println("the task is not NOT active therefore i'm going to copy the task to task history AND reset the task AND reset the workflow instance");
									//strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(
									//							strWorkflowTaskKey, query);

//System.err.println("strWorkflowInstanceKey = " + strWorkflowInstanceKey);
									String strSubWorkflowInstanceKey = WorkflowManager.getSubWorkflowInstanceKey(strWorkflowTaskKey, query);

//System.err.println("strSubWorkflowInstanceKey = " + strSubWorkflowInstanceKey);

									Vector vecAdditionalClearedTask = 
												WorkflowManager.getWorkflowTaskKeys(strSubWorkflowInstanceKey, query);

//System.err.println("vecAdditionalClearedTask = " + vecAdditionalClearedTask);

									vecClearedTask.addAll(vecAdditionalClearedTask);

//System.err.println("vecClearedTask = " + vecClearedTask);
									
									WorkflowManager.copyWorkflowTaskToWorkflowTaskHistory(strWorkflowTaskKey, query);
									WorkflowManager.resetWorkflowTask(strWorkflowTaskKey, query);
									
									WorkflowManager.resetWorkflowInstance(strSubWorkflowInstanceKey, query);
								}
							}

//System.err.println("");
//System.err.println("intIndex1 + 1 = " + intIndex1 + 1);
//System.err.println("vecClearedTask = " + vecClearedTask.size());
							
							//boolean blLastTaskToBeDeleted = false;
							//check whether if this is the last task to be process in the vecClearedTask
							if(intIndex1+1 == vecClearedTask.size())
							{
//System.err.println("intIndex1+1 == vecClearedTask.size()");
//System.err.println("vecCurrentMainTaskKey.size() = " + vecCurrentMainTaskKey.size());
								
								Vector vecPreviousTaskKey = new Vector();
								for(int intIndex2 = 0; intIndex2 < vecCurrentMainTaskKey.size(); intIndex2++)
								{
//System.err.println("");
//System.err.println("intIndex2 = " + intIndex2);
									String strCurrentMainTaskKey = (String) vecCurrentMainTaskKey.elementAt(intIndex2);

//System.err.println("strCurrentMainTaskKey = " + strCurrentMainTaskKey);

									vecPreviousTaskKey.addAll(
											(Vector) hashReversedTaskTransition.get(strCurrentMainTaskKey));
//System.err.println("(Vector) hashReversedTaskTransition.get(strCurrentMainTaskKey) = " + (Vector) hashReversedTaskTransition.get(strCurrentMainTaskKey));
//System.err.println("vecPreviousTaskKey = " + vecPreviousTaskKey);
								}

								if(vecPreviousTaskKey.contains(strNextTaskKey))
								{
//System.err.println("vecPreviousTaskKey.contains(strNextTaskKey) = true");
//System.err.println("set the blLastTaskToBeDeleted = true and then break");
									blLastTaskToBeDeleted = true;
									break;
								}
								else
								{
//System.err.println("vecPreviousTaskKey.contains(strNextTaskKey) = false");
									//append vecPreviousTaskKey into vecClearedTask
									vecClearedTask.addAll(vecPreviousTaskKey);

//System.err.println("vecClearedTask = " + vecClearedTask);

									//clear the vecPreviousTaskKey and add the new task in vecPreviousTaskKey
									vecCurrentMainTaskKey.clear();
									vecCurrentMainTaskKey.addAll(vecPreviousTaskKey);
									
//System.err.println("vecCurrentMainTaskKey = " + vecCurrentMainTaskKey);
								}
							}
							
							/*
							if(blLastTaskToBeDeleted)
							{
								//not sure whether i should put all the code before the break out side the
								//for vecClearedTask loop or inside here!!!!
								???? WorkflowManager.copyWorkflowTaskToWorkflowTaskHistory(strWorkflowTaskKey, query);
								???? WorkflowManager.resetWorkflowTask(strWorkflowTaskKey, query);
								tempRuntimeData = WorkflowManager.getWorkflowRuntimedata(
															strWorkflowInstanceKey, query);
								WorkflowEngine.kickStartTask(strWorkflowTaskKey, tempRuntimeData, query);
								break;
							}
							*/
						}

						if(blLastTaskToBeDeleted)
						{
							WorkflowManager.copyWorkflowTaskToWorkflowTaskHistory(strNextTaskKey, query);
							WorkflowManager.resetWorkflowTask(strNextTaskKey, query);

							String strWorkflowTaskType = WorkflowManager.getTaskType(strNextTaskKey, query);

							if(strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SYSTEM))
							{
								String strFunctionKey = WorkflowManager.getFunctionKey(strNextTaskKey , query);
								ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strFunctionKey, query);
//System.err.println("");
//System.err.println("strFunctionKey = " + strFunctionKey);
		
								String strFunctionType = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strType");

//System.err.println("strFunctionType = " + strFunctionType);
//System.err.println("strNextTaskKey = " + strNextTaskKey);
//System.err.println("strWorkflowInstanceKey = " + strWorkflowInstanceKey);
//System.err.println("");
								if(strFunctionType.equals(WorkflowManager.WORKFLOW_FUNCTION_TYPE_THREAD))
								{
									//delete that thread entries in the 
									WorkflowManager.deleteWorkflowFunctionThread(strNextTaskKey, strWorkflowInstanceKey);
								}
							}

							strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strNextTaskKey, query);
							tempRuntimeData = WorkflowManager.getWorkflowRuntimedata(
														strWorkflowInstanceKey, query);
							WorkflowEngine.kickStartTask(strNextTaskKey, tempRuntimeData, query);
						}
					} //if(blCondition && blHumanActionCondition)
					
					//set the task to not in handovering mode
					WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO,
																	 strFinishedWorkflowTaskKey, query);
				}// transition type is reject
			}// for loop for all available task

			//set the task to not in handovering mode
			WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO, strFinishedWorkflowTaskKey,
															 query);

			String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strFinishedWorkflowTaskKey, query);
			
			//System.err.println("blLastTask = >>>" + blLastTask + "<<<");
			//System.err.println("haveActiveTask = >>>" + WorkflowManager.haveActiveTasks(strWorkflowInstanceKey, query) + "<<<");
			//System.err.println("isHandoverExist = >>>" + WorkflowManager.isHandoverExist(strWorkflowInstanceKey, query) + "<<<");

			//if this is the last task the update the workflow instance status to completed
			//if blContinue is false, we consider that as the last task means stop the whole workflow (exclude sub workflow)
			if((blLastTask && !WorkflowManager.haveActiveTasks(strWorkflowInstanceKey, query) && 
				!WorkflowManager.isHandoverExist(strWorkflowInstanceKey, query)) || 
					!blContinue.booleanValue())
			{

//System.err.println("strFinishedWorkflowTaskKey = >>>" + strFinishedWorkflowTaskKey + "<<<");

				//String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strFinishedWorkflowTaskKey, query);


				//get all the task under the strWorkflowInstanceKey that status = (not active, active, started) 
				//and change them to incomplete only if blContinue = false
				if(!blContinue.booleanValue())
				{
					Vector vecWorkflowTaskKeys = WorkflowManager.getWorkflowTaskKeys(strWorkflowInstanceKey, query);
					Enumeration enumWorkflowTaskKeys = vecWorkflowTaskKeys.elements();

					//System.err.println("vecWorkflowTaskKeys.size() = " + vecWorkflowTaskKeys.size());
					
					while(enumWorkflowTaskKeys.hasMoreElements())
					{
						String strWorkflowTaskKey = (String) enumWorkflowTaskKeys.nextElement();
						String strWorkflowTaskStatus = WorkflowManager.getTaskStatus(strWorkflowTaskKey, query);
						String strWorkflowTaskType = WorkflowManager.getTaskType(strWorkflowTaskKey, query);

						if(!strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW) &&
						   (strWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_NOT_ACTIVE) || 
							strWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_ACTIVE) ||
							strWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_STARTED)))
						{
							WorkflowManager.updateTaskStatus(WorkflowManager.TASK_STATUS_INCOMPLETE,
															 strWorkflowTaskKey, query);
						}
					}
				}

				//to check if strWorkflowInstanceKey is a sub workflow of any parent workflow or not
				//if strSubWorkflowInstanceKey return null means it is not a sub workflow
				//else it indicates that it is a sub workflow
				String strSubWorkflowTaskKey = WorkflowManager.getSubWorkflowTaskKey(strWorkflowInstanceKey, query);

//System.err.println("strSubWorkflowTaskKey = >>>" + strSubWorkflowTaskKey + "<<<");
//System.err.println("strWorkflowInstanceKey = >>>" + strWorkflowInstanceKey + "<<<");
//System.err.println("task status = >>>" + strFinishedWorkflowTaskStatus + "<<<");
//System.err.println("blContinue = >>>" + blContinue.booleanValue() + "<<<");
				
				if(strSubWorkflowTaskKey != null)
				{
					String strParentWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strSubWorkflowTaskKey,
																								 query);

//System.err.println("strParentWorkflowInstanceKey = >>>" + strParentWorkflowInstanceKey + "<<<");

					ChannelRuntimeData tempRuntimeData =
									WorkflowManager.getWorkflowRuntimedata(strParentWorkflowInstanceKey, query);

					if(strFinishedWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_COMPLETED) ||
					   (strFinishedWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_FAILED) &&
															 blContinue.booleanValue() == true))
					{
						WorkflowManager.updateWorkflowInstanceStatus(WorkflowManager.WORKFLOW_INSTANCE_STATUS_COMPLETED, 
																	 strWorkflowInstanceKey, query);
						WorkflowEngine.finishTask(strSubWorkflowTaskKey, WorkflowManager.TASK_STATUS_COMPLETED,
									Boolean.valueOf(true), tempRuntimeData, query);

						//set the task to not in handovering mode
						//WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO, 
						//												 strSubWorkflowTaskKey, query);
					}
					else if(strFinishedWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_FAILED) &&
																 blContinue.booleanValue() == false)
					{
						WorkflowManager.updateWorkflowInstanceStatus(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED, 
																	 strWorkflowInstanceKey, query);
						WorkflowEngine.finishTask(strSubWorkflowTaskKey, WorkflowManager.TASK_STATUS_FAILED,
									Boolean.valueOf(false), tempRuntimeData, query);
						
						//set the task to not in handovering mode
						//WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_NO,
						//												 strSubWorkflowTaskKey,
						//												 query);
					}

					WorkflowManager.updateWorkflowInstanceCompletedDate(strToday, strWorkflowInstanceKey, query);
					WorkflowManager.updateWorkflowInstanceCompletedTime(strCurrentTime, strWorkflowInstanceKey, query);
				}
			    else
				{
					if(strFinishedWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_COMPLETED) ||
					   (strFinishedWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_FAILED) &&
															 blContinue.booleanValue() == true))
					{
						WorkflowManager.updateWorkflowInstanceStatus(WorkflowManager.WORKFLOW_INSTANCE_STATUS_COMPLETED, 
																	 strWorkflowInstanceKey, query);
					}
					else if(strFinishedWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_FAILED) &&
																 blContinue.booleanValue() == false)
					{
						WorkflowManager.updateWorkflowInstanceStatus(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED, 
																	 strWorkflowInstanceKey, query);
					}

					WorkflowManager.updateWorkflowInstanceCompletedDate(strToday, strWorkflowInstanceKey, query);
					WorkflowManager.updateWorkflowInstanceCompletedTime(strCurrentTime, strWorkflowInstanceKey, query);
				}
			}
		}
		catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::finishTask - " + e.toString(), e);
        }
	}


	// DELETE Workflow Instance

	/**
	 * delete the workflow instance and all its subworkflow
	 *
	 * @param strWorkflowInstanceKey	the workflow instance key
	 * @param query						the query object
	 */
	public static void deleteWorkflowInstances(String strWorkflowInstanceKey, DALSecurityQuery query)
	{
		try
		{
			Vector vecWorkflowInstanceKey = new Vector();
			vecWorkflowInstanceKey.add(strWorkflowInstanceKey);

			// looping to delete all sub workflows
			for(int intIndex = 0; intIndex < vecWorkflowInstanceKey.size(); intIndex++)
			{
				String strTempWorkflowInstanceKey = (String) vecWorkflowInstanceKey.get(intIndex);
				//System.err.println("strTempWorkflowInstanceKey = >>>" + strTempWorkflowInstanceKey + "<<<");
				
				//delete the workflow instance
				WorkflowManager.deleteWorkflowInstance(strTempWorkflowInstanceKey, query);

				//agus start
				//delete the workflow instance data in ix_report_sys_task_param
				WorkflowManager.deleteWorkflowInstanceDataInReportSysTaskParam(strTempWorkflowInstanceKey, query);
				//agus end

				//delete the workflow instance association with any domain
				WorkflowManager.deleteWorkflowInstanceInDomainWorkflow(strTempWorkflowInstanceKey, query);
			
				//delete all workflow function threads related to this workflow instance
				WorkflowManager.deleteWorkflowFunctionThread(null, strTempWorkflowInstanceKey);

				//get the task key and type pair in a hashtable
				Hashtable hashTaskTypes = WorkflowManager.getTaskTypes(strTempWorkflowInstanceKey, query);
				
				//System.err.println("hashTaskTypes = >>>" + hashTaskTypes + "<<<");

				//get the task key and sub wf instanance key pair in a hashtable
				Hashtable hashSubWorkflowInstanceKeys = WorkflowManager.getSubWorkflowInstanceKeys(
														strTempWorkflowInstanceKey, query);

				//System.err.println("hashSubWorkflowInstanceKeys = >>>" + hashSubWorkflowInstanceKeys + "<<<");

				Enumeration enumTaskKeys = hashTaskTypes.keys();

				while(enumTaskKeys.hasMoreElements())
				{
					String strTaskKey = (String) enumTaskKeys.nextElement();
					String strTaskType = (String) hashTaskTypes.get(strTaskKey);

					//System.err.println("strTaskKey = >>>" + strTaskKey + "<<<");
					//System.err.println("strTaskType = >>>" + strTaskType + "<<<");

					//add the sub workflow instance key to the vector (list of workflow instance to be deleted)
					if(strTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW))
					{
						String strSubWorkflowInstanceKey = (String) hashSubWorkflowInstanceKeys.get(strTaskKey);
						//System.err.println("about to add strSubWorkflowInstanceKey = >>>" + strSubWorkflowInstanceKey + "<<<");
						vecWorkflowInstanceKey.add(strSubWorkflowInstanceKey);
					}
				}

				//System.err.println("");

				//delete the workflow tasks
				WorkflowManager.deleteWorkflowTasks(strTempWorkflowInstanceKey, query);

				//delete the workflow tasks history
				WorkflowManager.deleteWorkflowTasksHistory(strTempWorkflowInstanceKey, query);

				/*
				//get all task keys under the given workflow instance key
				Vector vecWorkflowTaskKey = WorkflowManager.getWorkflowTaskKeys(strTempWorkflowInstanceKey, query);

				for(int intIndex2 = 0; intIndex2 < vecWorkflowTaskKey.size(); intIndex2++)
				{
					String strTaskKey = (String) vecWorkflowTaskKey.get(intIndex2);

					String strTaskType = WorkflowManager.getTaskType(strTaskKey, query);

					//add the sub workflow instance key to the vector (list of workflow instance to be deleted)
					if(strTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_SUBWORKFLOW))
					{
						String strSubWorkflowInstanceKey = WorkflowManager.getSubWorkflowInstanceKey(strTaskKey, query);
						vecWorkflowInstanceKey.add(strSubWorkflowInstanceKey);
					}

					//delete the workflow task
					WorkflowManager.deleteWorkflowTask(strTaskKey, query);

					//delete the workflow task parameter
					WorkflowManager.deleteWorkflowTaskParameter(strTaskKey, query);

					//delete the workflow task history
					WorkflowManager.deleteWorkflowTaskHistory(strTaskKey, query);
				}
				*/

				//delete the workflow task transition
				WorkflowManager.deleteWorkflowTaskTransition(strTempWorkflowInstanceKey, query);

				//delete the workflow runtimedata
				WorkflowManager.deleteWorkflowRuntimedata(strTempWorkflowInstanceKey, query);
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowEngine::deleteWorkflowInstances - " + e.toString(), e);
        }
	}
}
