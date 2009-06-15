/**

 * WorkflowManager.java

 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.

 * Created Date: 03/02/2004

 *

 * Last Modified: (Date\Author\Comments)

 * 13/02/2004		Agustian Agustian		Commenting

 * 16/02/2004		Agustian Agustian		Add Delete workflow functionality

 * 24/02/2004		Agustian Agustian		Add update workflow functionality

 * 02/03/2004		Agustian Agustian		Add instantiate workflow functionality

 * 10/03/2004		Agustian Agustian		Add finish task functionality (human and system)

 *

 * 

 */



package neuragenix.genix.workflow;



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



// bean shell packages

import bsh.Interpreter;

import bsh.EvalError;

import bsh.ParseException;



// uPortal packages

import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.services.LogService;

import org.jasig.portal.PropertiesManager;



// neuragenix packages

import neuragenix.dao.*;

import neuragenix.common.*;

import neuragenix.genix.workflow.util.XPDLReader;


/**

 * WorkflowManager - Manages all workflow activities

 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>

 */

public class WorkflowManager

{

	// Valid Characters for transition condition

	private static final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_0123456789";



	// Action types

	private static final String INSERT = "INSERT";

	private static final String UPDATE = "UPDATE";

	

	// Saving types

	protected static final String SAVE_NEW = "SAVE_NEW";

	protected static final String SAVE_UPDATE = "SAVE_UPDATE";



	// Error types

	private static final String DUPLICATE = "DUPLICATE";

	private static final String NOT_EXIST = "NOT_EXIST";

	private static final String ACTIVE_TEMPLATE = "ACTIVE_TEMPLATE";

	private static final String ACTIVE_INSTANTIATIONS = "ACTIVE_INSTANTIATIONS";

	private static final String PARENT_ACTIVE_INSTANTIATIONS = "PARENT_ACTIVE_INSTANTIATIONS";

	

	// List of domains in the workflow world

	public static final String WORKFLOW_PACKAGE = "WORKFLOW_PACKAGE";

	public static final String WORKFLOW_PACKAGE_HEADER = "WORKFLOW_PACKAGE_HEADER";

	public static final String WORKFLOW_PROCESS = "WORKFLOW_TEMPLATE";

	public static final String WORKFLOW_PROCESS_HEADER = "WORKFLOW_TEMPLATE_HEADER";

	public static final String WORKFLOW_TRIGGER = "WORKFLOW_TRIGGER";

	public static final String WORKFLOW_ACTIVITY = "WORKFLOW_ACTIVITY";

	public static final String WORKFLOW_ACTIVITY_TRANSITION = "WORKFLOW_ACTIVITY_TRANSITION";

	public static final String WORKFLOW_ACTIVITY_PARAMETER = "WORKFLOW_ACTIVITY_PARAMETER";

	public static final String WORKFLOW_INSTANCE = "WORKFLOW_INSTANCE";

	public static final String WORKFLOW_TASK = "WORKFLOW_TASK";

	public static final String WORKFLOW_TASK_TRANSITION = "WORKFLOW_TASK_TRANSITION";

	public static final String WORKFLOW_FUNCTION = "WORKFLOW_FUNCTION";

    public static final String WORKFLOW_TASK_PARAMETER = "WORKFLOW_TASK_PARAMETER";

    public static final String WORKFLOW_RUNTIMEDATA = "WORKFLOW_RUNTIMEDATA";

    public static final String WORKFLOW_TEMPLATE_PARAMETER = "WORKFLOW_TEMPLATE_PARAMETER";

    public static final String WORKFLOW_TASK_HISTORY = "WORKFLOW_TASK_HISTORY";

	public static final String WORKFLOW_FUNCTION_THREAD = "WORKFLOW_FUNCTION_THREAD";
        
        public static final String WORKFLOW_FILE = "WORKFLOW_FILE";



	// Instantiation method

	protected static final String MANUAL_INSTANTIATION = "MANUAL_INSTANTIATION";

	protected static final String AUTOMATIC_INSTANTIATION = "AUTOMATIC_INSTANTIATION";



	// Task status

	public static final String TASK_STATUS_NOT_ACTIVE = "Not active";

	public static final String TASK_STATUS_ACTIVE = "Active";

	public static final String TASK_STATUS_STARTED = "Started";

	public static final String TASK_STATUS_COMPLETED = "Completed";

	public static final String TASK_STATUS_INCOMPLETE = "Incomplete";

	public static final String TASK_STATUS_FAILED = "Failed";



	// Workflow template status

	protected static final String WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE = "Not active";

	protected static final String WORKFLOW_TEMPLATE_STATUS_ACTIVE = "Active";

	

	// Workflow instance status

	public static final String WORKFLOW_INSTANCE_STATUS_SUSPENDED = "Suspended";

	public static final String WORKFLOW_INSTANCE_STATUS_IN_PROGRESS = "In progress";

	public static final String WORKFLOW_INSTANCE_STATUS_COMPLETED = "Completed";

	public static final String WORKFLOW_INSTANCE_STATUS_FAILED = "Failed";

	public static final String WORKFLOW_INSTANCE_STATUS_NOT_STARTED = "Not started";



	// Workflow task type

	public static final String WORKFLOW_TASK_TYPE_HUMAN = "Human";

	public static final String WORKFLOW_TASK_TYPE_SYSTEM = "System";

	public static final String WORKFLOW_TASK_TYPE_SUBWORKFLOW = "Sub workflow";

	public static final String WORKFLOW_TASK_TYPE_START = "Start";

	public static final String WORKFLOW_TASK_TYPE_STOP = "Stop";



	// Workflow task if fail option

	protected static final String WORKFLOW_TASK_FAIL_CONTINUE = "Continue";

	protected static final String WORKFLOW_TASK_FAIL_STOP = "Stop";

	protected static final String WORKFLOW_TASK_FAIL_RETRY = "Retry";



	// Workflow task retry delay unit option

	private static final String WORKFLOW_TASK_RETRY_DELAY_UNIT_SECOND = "Second";

	private static final String WORKFLOW_TASK_RETRY_DELAY_UNIT_MINUTE = "Minute";

	private static final String WORKFLOW_TASK_RETRY_DELAY_UNIT_HOUR = "Hour";

	private static final String WORKFLOW_TASK_RETRY_DELAY_UNIT_DAY = "Day";

	private static final String WORKFLOW_TASK_RETRY_DELAY_UNIT_MONTH = "Month";

	private static final String WORKFLOW_TASK_RETRY_DELAY_UNIT_YEAR = "Year";



	// Data type

	private static final String DATA_TYPE_STRING = "String";

	private static final String DATA_TYPE_DATE = "Date";

	private static final String DATA_TYPE_INTEGER = "Integer";

	private static final String DATA_TYPE_FLOAT = "Float";

	private static final String DATA_TYPE_DOUBLE = "Double";



	// Data operators

	private static final String DATA_OPERATOR_EQUAL = "=";

	private static final String DATA_OPERATOR_NOT_EQUAL = "!=";

	private static final String DATA_OPERATOR_GREATER_THAN = ">";

	private static final String DATA_OPERATOR_LESS_THAN = "<";

	private static final String DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO = ">=";

	private static final String DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO= "<=";



	// Data connector

	private static final String DATA_CONNECTOR_AND = "AND";

	private static final String DATA_CONNECTOR_OR = "OR";



	// Transition type

	protected static final String TRANSITION_TYPE_NORMAL = "Normal";

	protected static final String TRANSITION_TYPE_REJECT = "Reject";



	// Workflow function thread status

	public static final String WORKFLOW_FUNCTION_THREAD_RUNNING = "Running";

	public static final String WORKFLOW_FUNCTION_THREAD_COMPLETED = "Completed";

	public static final String WORKFLOW_FUNCTION_THREAD_FAILED = "Failed";



	// Workflow function type

	protected static final String WORKFLOW_FUNCTION_TYPE_NORMAL = "Normal";

	protected static final String WORKFLOW_FUNCTION_TYPE_THREAD = "Thread";



	// Performer type

	public static final String PERFORMER_TYPE_USER = "User";

	public static final String PERFORMER_TYPE_GROUP = "Group";



	// Handover status

	public static final String TASK_HANDOVER_YES = "Yes";

	public static final String TASK_HANDOVER_NO = "No";

	



	public static final String XPDL_DIRECTORY = PropertiesManager.getProperty("neuragenix.genix.workflow.SaveWorkflowXPDLLocation");
        
        
        public static final String FUNCTION_DIRECTORY = PropertiesManager.getProperty("neuragenix.genix.workflow.FunctionClassLocation");



	// load Workflow domains & formfields

    static 

    {

		InputStream file = WorkflowManager.class.getResourceAsStream("WorkflowDBSchema.xml");

        DatabaseSchema.loadDomains(file, "WorkflowDBSchema.xml");

    }



	/**

	 * Default constructor

	 */

	public WorkflowManager()

	{

	}



	// SAVE METHODS

	

	/**

	 * save the package to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param workflowPackage the Package

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

							 Package workflowPackage, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(workflowPackage, hashFieldData);



//System.err.println("strDomain = " + strDomain);

//System.err.println("vtDomainDetails = " + vtDomainDetails);

//System.err.println("hashFieldData = " + hashFieldData);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Package - " + e.toString(), e);

        }

	}



	/**

	 * save the package header to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param packageHeader	the package header

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

							 PackageHeader packageHeader, String strWorkflowPackageXPDLId,

							 DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(packageHeader, hashFieldData);



			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowPackageKey", strWorkflowPackageKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Package Header - " + e.toString(), e);

        }

	}



	/**

	 * save the workflow process to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param workflowProcess the workflow process

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

						     WorkflowProcess workflowProcess, String strWorkflowPackageXPDLId,

							 DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(workflowProcess, hashFieldData);



			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);

			

			query.reset();



			query.setDomain(strDomain, null, null, null);

			hashFieldData.put(strDomain + "_intWorkflowPackageKey", strWorkflowPackageKey);

			hashFieldData.put(strDomain + "_strStatus", WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE);

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Workflow Process - " + e.toString(), e);

        }

	}        
        
        /**
	 * save the function to the Database
	 *
	 * @param strFormFieldName the form field name to be saved
	 * @param strDomain the domain name
	 * @param function the function
	 * @param query the query object
	 */
        protected static void save(String strFormFieldName, String strDomain, 
                                   Function function, DALSecurityQuery query)
        {
            try
            {
                Hashtable hashFieldData = new Hashtable();
                
                Vector vtFunctionDetails = DatabaseSchema.getFormFields(strFormFieldName);
                
                WorkflowManager.convertToHashtable(function, hashFieldData);
                
                // since we're inserting, we need to remove the function
                // keey in the hash mapping
                hashFieldData.remove("WORKFLOW_FUNCTION_intWorkflowFunctionKey");
                
                query.reset();
                query.setDomain(strDomain, null, null, null);
                query.setFields(vtFunctionDetails, hashFieldData);
                query.executeInsert();
            }
            catch (Exception e)            
            {
                LogService.instance().log(LogService.ERROR,
                                            "Unknown error in WorkflowManager::save - Function - " + e.toString(), e);
            }
        }
        

	/**

	 * save the process header to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param processHeader the process header

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL id

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

							 ProcessHeader processHeader, String strWorkflowPackageXPDLId,

							 String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(processHeader, hashFieldData);





			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Process Header - " + e.toString(), e);

        }

	}



	/**

	 * save the trigger to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param trigger the trigger

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL id

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

							 Trigger trigger, String strWorkflowPackageXPDLId,

							 String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(trigger, hashFieldData);



			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			query.reset();



			query.setDomain(strDomain, null, null, null);   

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);

			hashFieldData.put(strDomain + "_intWorkflowPackageKey", strWorkflowPackageKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Trigger - " + e.toString(), e);

        }

	}



	/**

	 * save the activity to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param activity the activity

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL id

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

			                 Activity activity, String strWorkflowPackageXPDLId,

							 String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(activity, hashFieldData);



			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			//get the sub workflow package and template key

			String strSubWorkflowTemplateKey = null;

			String strSubWorkflowPackageXPDLId = (String) hashFieldData.get("WORKFLOW_ACTIVITY_strSubWorkflowPackageXPDLId");

			String strSubWorkflowTemplateXPDLId = (String) hashFieldData.get("WORKFLOW_ACTIVITY_strSubWorkflowTemplateXPDLId");

			if(strSubWorkflowPackageXPDLId != null)

			{

				String strSubWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

						strSubWorkflowPackageXPDLId, query);



				if(strSubWorkflowTemplateXPDLId != null)

				{

					strSubWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

									strSubWorkflowTemplateXPDLId, strSubWorkflowPackageKey, query);

				}

			}



			query.reset();

			

			query.setDomain(strDomain, null, null, null);   

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);



			//only set the sub workflow template key if it is not null

			if(strSubWorkflowTemplateKey != null)

			{

				hashFieldData.put(strDomain + "_intSubWorkflowTemplateKey", strSubWorkflowTemplateKey);

			}



			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();



			//save workflow activity parameters 

			String strWorkflowActivityXPDLId = (String) hashFieldData.get(

											"WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId");

			String strWorkflowActivityKey = WorkflowManager.getWorkflowActivityKey(

										   strWorkflowTemplateKey, strWorkflowActivityXPDLId,

									       query);



			Hashtable hashWorkflowActivityParameters =  activity.getParameters();

			Enumeration enumWorkflowActivityParameter = hashWorkflowActivityParameters.elements();



			while(enumWorkflowActivityParameter.hasMoreElements())

			{

				Parameter parameter = (Parameter) enumWorkflowActivityParameter.nextElement();

				WorkflowManager.save("workflow_activity_parameter_details", WORKFLOW_ACTIVITY_PARAMETER,

									 parameter, strWorkflowActivityKey, query);

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Activity - " + e.toString(), e);

        }

	}



	/**

	 * save the activity parameter to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param parameter	the workflow activity parameter

	 * @param strWorkflowActivityKey the workflow activity Key

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

							 Parameter parameter, String strWorkflowActivityKey,

							 DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);



			hashFieldData.put(strDomain + "_strName", parameter.getName());

			hashFieldData.put(strDomain + "_strType", parameter.getType());

			hashFieldData.put(strDomain + "_strDescription", parameter.getDescription());

			hashFieldData.put(strDomain + "_intWorkflowActivityKey", strWorkflowActivityKey);



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Parameter - " + e.toString(), e);

        }

	}



	/**

	 * save the template parameter to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param parameter	the workflow activity parameter

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL id

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

							 Parameter parameter,  String strWorkflowPackageXPDLId,

							 String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

										   strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

									strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);



			hashFieldData.put(strDomain + "_strName", parameter.getName());

			hashFieldData.put(strDomain + "_strType", parameter.getType());

			hashFieldData.put(strDomain + "_strDescription", parameter.getDescription());

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Parameter - " + e.toString(), e);

        }

	}



	/**

	 * save the transition to the Database

	 *

	 * @param strFormFieldName the form field name to be saved

	 * @param strDomain the domain name

	 * @param transition the transition

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL id

	 * @param query the query object

	 */

	protected static void save(String strFormFieldName, String strDomain,

			                 Transition transition, String strWorkflowPackageXPDLId,

							 String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(transition, hashFieldData);



			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			String strWorkflowTransitionFrom = WorkflowManager.getWorkflowActivityKey(

					strWorkflowTemplateKey, transition.getFrom(), query);

			String strWorkflowTransitionTo = WorkflowManager.getWorkflowActivityKey(

					strWorkflowTemplateKey, transition.getTo(), query);



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_strName", transition.getName());

			hashFieldData.put(strDomain + "_intOriginActivityKey", strWorkflowTransitionFrom);

			hashFieldData.put(strDomain + "_intTargetActivityKey", strWorkflowTransitionTo);

			hashFieldData.put(strDomain + "_intOriginActivityWkflTmplKey", strWorkflowTemplateKey);

			hashFieldData.put(strDomain + "_intTargetActivityWkflTmplKey", strWorkflowTemplateKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::save - Transition - " + e.toString(), e);

        }

	}



        /**
         * Save
         */
        protected static void saveFile(String strFormFieldName, String strDomain, 
                                   String strPackageName, String strTemplateName, 
                                   DALSecurityQuery query)
        {            
            
            
            
        }
        
        

	// UPDATE METHODS


        /**
	 * update the function in the Database
	 *
	 * @param strFormFieldName the form field name to be update
	 * @param strDomain the domain name
	 * @param function the Function
	 * @param query the query object
	 */        
        protected static void update(String strFormFieldName, String strDomain,
                                     Function function, DALSecurityQuery query)
        {
            try
            {                
                Hashtable hashFieldData = new Hashtable();
                
                Vector vtFunctionDetails = DatabaseSchema.getFormFields(strFormFieldName);
                
                WorkflowManager.convertToHashtable(function, hashFieldData);
                
                query.reset();
                query.setDomain(strDomain, null, null, null);
                query.setFields(vtFunctionDetails, hashFieldData);
                query.setWhere(null, 0, "WORKFLOW_FUNCTION_intWorkflowFunctionKey", 
                               "=", function.getKey(), 0, 0);
                query.executeUpdate();
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::update - Function - " + e.toString(), e);
            }
        }
        
        
	/**

	 * update the package in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param workflowPackage the Package

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   Package workflowPackage, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(workflowPackage, hashFieldData);

			String strWorkflowPackageXPDLId = workflowPackage.getId();



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_intWorkflowPackageKey", "=", 

							strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Package - " + e.toString(), e);

        }

    }	



	/**

	 * update the package header in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param packageHeader	the package header

	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   PackageHeader packageHeader, String strWorkflowPackageXPDLId, 

							   DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(packageHeader, hashFieldData);



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowPackageKey", strWorkflowPackageKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_HEADER_intWorkflowPackageKey", "=", 

							strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Package - " + e.toString(), e);

        }

    }	



	/**

	 * update the workflow process in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param workflowProcess	the workflow process

	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   WorkflowProcess workflowProcess, String strWorkflowPackageXPDLId, 

							   DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(workflowProcess, hashFieldData);



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);

			//get the workflow atemplate XPDL Id

			String strWorkflowTemplateXPDLId = workflowProcess.getId();



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowPackageKey", strWorkflowPackageKey);

			hashFieldData.put(strDomain + "_strStatus", WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE);

			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowPackageKey", "=", 

							strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId", "=", 

							strWorkflowTemplateXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Workflow Process - " + e.toString(), e);

        }

    }	



	/**

	 * update the workflow process header in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param processHeader the process header 

	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL Id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   ProcessHeader processHeader, String strWorkflowPackageXPDLId,

							   String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(processHeader, hashFieldData);



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);





			//get workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_HEADER_intWorkflowTemplateKey", "=", 

							strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Process Header - " + e.toString(), e);

        }

    }



	/**

	 * update the workflow template parameter in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param parameter the parameter 

	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL Id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   Parameter parameter, String strWorkflowPackageXPDLId,

							   String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(parameter, hashFieldData);



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);





			//get workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);

			

			//get the workflow template parameter name

			String strWorkflowTemplateParameterName = parameter.getName();



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=", 

							strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_strName", "=", 

							strWorkflowTemplateParameterName, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Parameter - " + e.toString(), e);

        }

    }



	/**

	 * update the workflow trigger in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param trigger the trigger

	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL Id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   Trigger trigger, String strWorkflowPackageXPDLId,

							   String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(trigger, hashFieldData);



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);





			//get workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);

			

			//get the workflow trigger XPDL Id

			String strWorkflowTriggerXPDLId = trigger.getId();



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);

			hashFieldData.put(strDomain + "_intWorkflowPackageKey", strWorkflowPackageKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_TRIGGER_intWorkflowTemplateKey", "=", 

							strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intWorkflowPackageKey", "=", 

							strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_strTriggerXPDLId", "=", 

							strWorkflowTriggerXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Trigger - " + e.toString(), e);

        }

    }



	/**

	 * update the workflow activity in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param activity the activity

	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL Id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   Activity activity, String strWorkflowPackageXPDLId,

							   String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(activity, hashFieldData);



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);



			//get workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			//get the sub workflow package and template key

			String strSubWorkflowTemplateKey = null;

			String strSubWorkflowPackageXPDLId = (String) hashFieldData.get("WORKFLOW_ACTIVITY_strSubWorkflowPackageXPDLId");

			String strSubWorkflowTemplateXPDLId = (String) hashFieldData.get("WORKFLOW_ACTIVITY_strSubWorkflowTemplateXPDLId");

			if(strSubWorkflowPackageXPDLId != null)

			{

				String strSubWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

						strSubWorkflowPackageXPDLId, query);



				if(strSubWorkflowTemplateXPDLId != null)

				{

					strSubWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

									strSubWorkflowTemplateXPDLId, strSubWorkflowPackageKey, query);

				}

			}



			//get the workflow activity XPDL Id

			String strWorkflowActivityXPDLId = (String) hashFieldData.get(

											"WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId");	



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_intWorkflowTemplateKey", strWorkflowTemplateKey);

			

			//only set the sub workflow template key if it is not null

			if(strSubWorkflowTemplateKey != null)

			{

				hashFieldData.put(strDomain + "_intSubWorkflowTemplateKey", strSubWorkflowTemplateKey);

			}



//System.err.println("");

//System.err.println("vtDomainDetails = >>> " + vtDomainDetails + "<<<");

//System.err.println("hashFieldData= >>> " + hashFieldData + "<<<");

//System.err.println("");



			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=", 

							strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId", "=", 

						    strWorkflowActivityXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();



			//save/update workflow activity parameters 

			//String strWorkflowActivityXPDLId = (String) hashFieldData.get(

			//								"WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId");

			String strWorkflowActivityKey = WorkflowManager.getWorkflowActivityKey(

										   strWorkflowTemplateKey, strWorkflowActivityXPDLId,

									       query);



			Hashtable hashWorkflowActivityParameters =  activity.getParameters();

			WorkflowManager.update("workflow_activity_parameter_details", WORKFLOW_ACTIVITY_PARAMETER,

								   hashWorkflowActivityParameters, strWorkflowActivityKey, query);

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Activity - " + e.toString(), e);

        }

    }



	/**

	 * update the workflow activity parameters in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param hashNewWorkflowActivityParameters	the new activity parameters

	 * @param strWorkflowActivityKey the workflow activity id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   Hashtable hashNewWorkflowActivityParameters, String strWorkflowActivityKey,

							   DALSecurityQuery query)

	{

		try

		{

			Vector vecExistingWorkflowActivityParameters = new Vector();



			//get existing parameter in the database

			query.reset();

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_strName", "null", "hashtable");

			query.setField(strDomain + "_strType", "null", "hashtable");

			query.setField(strDomain + "_strDescription", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

					       strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				Parameter parameter = new Parameter();	

				parameter.setName(rsResultSet.getString("WORKFLOW_ACTIVITY_PARAMETER_strName"));

				parameter.setType(rsResultSet.getString("WORKFLOW_ACTIVITY_PARAMETER_strType"));

				parameter.setDescription(rsResultSet.getString("WORKFLOW_ACTIVITY_PARAMETER_strDescription"));

				vecExistingWorkflowActivityParameters.add(parameter);

			}



			Parameter currentExistingParameter = null;

			boolean blDeleteParameter;

			boolean blUpdateParameter;

			for(int intIndex = 0; intIndex <  vecExistingWorkflowActivityParameters.size(); intIndex++)

			{

				blDeleteParameter = true;

				blUpdateParameter = true;

				currentExistingParameter = (Parameter) vecExistingWorkflowActivityParameters.elementAt(intIndex);

				Parameter newParameter = null;



				//deciding whether the existing parameter have to be deleted OR updated

				if(hashNewWorkflowActivityParameters.containsKey(currentExistingParameter.getName()))

				{

					blDeleteParameter = false;

					newParameter = (Parameter) hashNewWorkflowActivityParameters.get(

													currentExistingParameter.getName());

					if(newParameter.getType().equals(currentExistingParameter.getType()) &&

					   newParameter.getDescription().equals(currentExistingParameter.getDescription()))

					{

						blUpdateParameter = false;

						break;

					}

				}



				if(blDeleteParameter)

				{

					//update(delete) the parameter

					query.reset();

			

					query.setDomain(strDomain, null, null, null);    

					query.setField(strDomain + "_intDeleted", "-1", "hashtable");

					query.setWhere(null, 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

								   strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

					query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_PARAMETER_strName", "=",

								   currentExistingParameter.getName(), 0, DALQuery.WHERE_HAS_VALUE);

					query.executeUpdate();

				}

				else if(blUpdateParameter)

				{

					//update the parameter

					query.reset();

					query.setDomain(strDomain, null, null, null);    

					query.setField(strDomain + "_strType", newParameter.getType(), "hashtable");

					query.setField(strDomain + "_strDescription", newParameter.getDescription(), "hashtable");

					query.setWhere(null, 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

								   strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

					query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_PARAMETER_strName", "=",

								   currentExistingParameter.getName(), 0, DALQuery.WHERE_HAS_VALUE);

					query.executeUpdate();

				}

			}

			

			boolean blSaveParameter;

			Parameter currentNewParameter = null;

			Enumeration enumNewWorkflowActivityParameterElements = hashNewWorkflowActivityParameters.elements();

			while(enumNewWorkflowActivityParameterElements.hasMoreElements())

			{

				blSaveParameter = true;

				currentNewParameter = (Parameter) enumNewWorkflowActivityParameterElements.nextElement();

				String strCurrentNewParameterName = currentNewParameter.getName();



				//deciding whether the new parameter needs to be added

				for(int intIndex3 = 0; intIndex3 <  vecExistingWorkflowActivityParameters.size(); intIndex3++)

				{

					currentExistingParameter = (Parameter) vecExistingWorkflowActivityParameters.elementAt(intIndex3);

					String strExistingParameterName = currentExistingParameter.getName();



					if(strExistingParameterName.equals(strCurrentNewParameterName))

					{

						blSaveParameter = false;

						break;

					}

				}



				if(blSaveParameter)

				{

					// save the paramater

					WorkflowManager.save(strFormFieldName, strDomain, currentNewParameter,

										 strWorkflowActivityKey, query);

				}

			}

			

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Parameters - " + e.toString(), e);

        }

    }



	/**

	 * update the workflow activity transition in the Database

	 *

	 * @param strFormFieldName the form field name to be update

	 * @param strDomain the domain name

	 * @param transition the transition 

	 * @param strWorkflowPackageXPDLId the workflow package XPDL Id

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL Id

	 * @param query the query object

	 */

	protected static void update(String strFormFieldName, String strDomain,

							   Transition transition, String strWorkflowPackageXPDLId,

							   String strWorkflowTemplateXPDLId, DALSecurityQuery query)

	{

		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields(strFormFieldName);

			WorkflowManager.convertToHashtable(transition, hashFieldData);



			//get workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

											strWorkflowPackageXPDLId, query);



			//get workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

					strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			//get the workflow transition XPDL Id

			String strWorkflowTransitionXPDLId = transition.getId();



			//get the workflow transition from

			String strWorkflowTransitionFrom = WorkflowManager.getWorkflowActivityKey(

					strWorkflowTemplateKey, transition.getFrom(), query);

			

			//get the workflow transition to

			String strWorkflowTransitionTo = WorkflowManager.getWorkflowActivityKey(

					strWorkflowTemplateKey, transition.getTo(), query);



			query.reset();



			query.setDomain(strDomain, null, null, null);    

			hashFieldData.put(strDomain + "_strName", transition.getName());

			hashFieldData.put(strDomain + "_intOriginActivityKey", strWorkflowTransitionFrom);

			hashFieldData.put(strDomain + "_intTargetActivityKey", strWorkflowTransitionTo);

			hashFieldData.put(strDomain + "_intOriginActivityWkflTmplKey", strWorkflowTemplateKey);

			hashFieldData.put(strDomain + "_intTargetActivityWkflTmplKey", strWorkflowTemplateKey);

			query.setFields(vtDomainDetails, hashFieldData);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_TRANSITION_strActivityTransitionXPDLId", "=", 

						   strWorkflowTransitionXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			//query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityKey", "=", 

			//			   strWorkflowTransitionFrom, 0, DALQuery.WHERE_HAS_VALUE);

			//query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityKey", "=", 

			//			   strWorkflowTransitionTo, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::update - Transition - " + e.toString(), e);

        }

    }



	// DELETE METHODS



	/**

	 * delete the workflow template parameter from the Database

	 *

	 * @param strDomain the domain name

	 * @param strParameterName	the parameter name

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteWorkflowTemplateParameter(String strDomain, String strParameterName,

														  String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_strName", "=",

						   strParameterName, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTemplateParameter - " + e.toString(), e);

        }

	}

	

	/**

	 * delete the workflow template parameter from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteWorkflowTemplateParameter(String strDomain, String strWorkflowTemplateKey,

										  DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTemplateParameter - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow trigger from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteTrigger(String strDomain, String strWorkflowTemplateKey,

										DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TRIGGER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteTrigger - " + e.toString(), e);

        }

	}



	

	/**

	 * delete the workflow trigger from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowPackageKey the workflow package key

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteTrigger(String strDomain, String strWorkflowPackageKey, 

									  String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TRIGGER_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteTrigger - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow transition from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowActivityKey the workflow activity key

	 * @param query the query object

	 */

	protected static void deleteTransition(String strDomain, String strWorkflowActivityKey,

									     DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityKey", "=",

						   strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("OR", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityKey", "=",

						   strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteTransition - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow activity parameter from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowActivityKey the workflow activity key

	 * @param query the query object

	 */

	protected static void deleteActivityParameter(String strDomain, String strWorkflowActivityKey,

									     		DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

						   strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteActivityParameter - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow activity from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteActivity(String strDomain, String strWorkflowTemplateKey,

							   		        DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteActivity - " + e.toString(), e);

        }

	}

        /**
	 * delete the function from the Database
	 *
	 * @param strDomain the domain name
	 * @param strWorkflowFunctionKey the function key
	 * @param query the query object
	 */        
        protected static void deleteFunction(String strDomain, String strWorkflowFunctionKey, 
                                             DALSecurityQuery query)
        {
            try
            {
                query.reset();
                query.setDomain(strDomain, null, null, null);
                query.setField(strDomain + "_intDeleted", "-1", "hashtable");
                query.setWhere(null, 0, strDomain + "_intWorkflowFunctionKey", 
                               "=", strWorkflowFunctionKey, 0, DALQuery.WHERE_HAS_VALUE);
                query.executeUpdate();
            }
		catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::deleteFunction - " + e.toString(), e);
            }            
        }


	/**

	 * delete the workflow template header from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteTemplateHeader(String strDomain, String strWorkflowTemplateKey,

							   		         DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_HEADER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteTemplateHeader - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow template from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteTemplate(String strDomain, String strWorkflowTemplateKey,

							   		   DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteTemplate - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow package header from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowPackageKey the workflow package key

	 * @param query the query object

	 */

	protected static void deletePackageHeader(String strDomain, String strWorkflowPackageKey,

							   		        DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_HEADER_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deletePackageHeader - " + e.toString(), e);

        }

	}

	

	/**

	 * delete the workflow package from the Database

	 *

	 * @param strDomain the domain name

	 * @param strWorkflowPackageKey the workflow package key

	 * @param query the query object

	 */

	protected static void deletePackage(String strDomain, String strWorkflowPackageKey,

							   		  String strWorkflowPackageXPDLId, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_strWorkflowPackageXPDLId", strWorkflowPackageXPDLId + "-" + ((new Date()).toString()), "hashtable");

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deletePackage - " + e.toString(), e);

        }

	}



	// INSTANTIATION METHODS



	/**

	 * copy all necessary details from workflow activity transition to workflow task transition

	 *

	 * @param hashActivityTaskKeys		the related activity and task keys mapping

	 * @param strWorkflowTemplateKey	the workflow template key

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 */

	protected static void copyWorkflowActivityTransitionToWorkflowTaskTransition(Hashtable hashActivityTaskKeys,

															String strWorkflowTemplateKey, String strWorkflowInstanceKey,

															DALSecurityQuery query)

	{

		try

		{

			//to indicate this is the first where clause for activity key

			boolean firstActivityKey = true;



			// get the workflow activity transition and workflow task transition field details

            Vector vecWorkflowActivityTransitionFormFields =

								DatabaseSchema.getFormFields("workflow_activity_transition_details");

            Vector vecWorkflowTaskTransitionFormFields = DatabaseSchema.getFormFields("workflow_task_transition_details");

			Enumeration enumWorkflowActivityKeys = hashActivityTaskKeys.keys();



			query.reset();



			query.setDomain("WORKFLOW_ACTIVITY_TRANSITION", null, null, null);

            query.setFields(vecWorkflowActivityTransitionFormFields, null);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityWkflTmplKey",

									"=", strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityWkflTmplKey",

									"=", strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);
                        
			ResultSet rsResultSet = query.executeSelect();

            Vector vecResult = QueryChannel.lookupRecord(rsResultSet, vecWorkflowActivityTransitionFormFields);

          

			for(int intIndex = 0; intIndex < vecResult.size(); intIndex++)

            {

				Hashtable hashWorkflowTaskTransitionData = new Hashtable();

				Hashtable hashMapping = new Hashtable();

				WorkflowManager.buildWorkflowActivityTransitionAndWorkflowTaskTransitionMapping(hashMapping);

				Hashtable hashResult = (Hashtable) vecResult.get(intIndex);

				WorkflowManager.transferData(hashResult, hashWorkflowTaskTransitionData, hashMapping);

			

				//String strWorkflowInstanceKey = QueryChannel.getNewestKeyAsString(

				//									"WORKFLOW_INSTANCE_intWorkflowInstanceKey");



				query.reset();

				query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

				hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intOriginTaskKey",

						hashActivityTaskKeys.get(hashResult.get("WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityKey")));

				hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intTargetTaskKey",

						hashActivityTaskKeys.get(hashResult.get("WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityKey")));

				hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intOriginTaskWorkflowInstanceKey",

						strWorkflowInstanceKey);

				hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intTargetTaskWorkflowInstanceKey",

						strWorkflowInstanceKey);
                                
				query.setFields(vecWorkflowTaskTransitionFormFields, hashWorkflowTaskTransitionData);

				query.executeInsert();

            }

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::copyWorkflowActivityTransitionToWorkflowTaskTransition - " +

					e.toString(), e);

        }

	}



	/**

	 * build the mapping between workflow activity transition and workflow task transition

	 *

	 * @param hashMapping 	the hashtable where the mapping will be inserted

	 */

	private static void buildWorkflowActivityTransitionAndWorkflowTaskTransitionMapping(Hashtable hashMapping)

	{

		try

		{

			hashMapping.put("WORKFLOW_ACTIVITY_TRANSITION_strName", "WORKFLOW_TASK_TRANSITION_strName");

			hashMapping.put("WORKFLOW_ACTIVITY_TRANSITION_strCondition", "WORKFLOW_TASK_TRANSITION_strCondition");

			hashMapping.put("WORKFLOW_ACTIVITY_TRANSITION_strType", "WORKFLOW_TASK_TRANSITION_strType");

			hashMapping.put("WORKFLOW_ACTIVITY_TRANSITION_strXCoordinate", "WORKFLOW_TASK_TRANSITION_strXCoordinate");

			hashMapping.put("WORKFLOW_ACTIVITY_TRANSITION_strYCoordinate", "WORKFLOW_TASK_TRANSITION_strYCoordinate");                        
		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::buildWorkflowActivityTransitionAndWorkflowTaskTransitionMapping - " +

					e.toString(), e);

        }

	}



	/**

	 * copy all necessary details from workflow activity parameter to workflow task parameter

	 *

	 * @param hashActivityTaskKeys		the related activity and task keys mapping

	 * @param query						the query object

	 */

	protected static void copyWorkflowActivityParameterToWorkflowTaskParameter(Hashtable hashActivityTaskKeys,

															DALSecurityQuery query)

	{

		try

		{

			//to indicate this is the first where clause for activity key

			boolean firstActivityKey = true;



			// get the workflow activity parameter and workflow task parameter field details

            Vector vecWorkflowActivityParameterFormFields = DatabaseSchema.getFormFields("workflow_activity_parameter_details");

            Vector vecWorkflowTaskParameterFormFields = DatabaseSchema.getFormFields("workflow_task_parameter_details");

			Enumeration enumWorkflowActivityKeys = hashActivityTaskKeys.keys();



			query.reset();



			query.setDomain("WORKFLOW_ACTIVITY_PARAMETER", null, null, null);

            query.setFields(vecWorkflowActivityParameterFormFields, null);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



			while(enumWorkflowActivityKeys.hasMoreElements())

			{

				String strWorkflowActivityKey = (String) enumWorkflowActivityKeys.nextElement();



				if(enumWorkflowActivityKeys.hasMoreElements())

				{

					// the opening of the where clause and has more than one key

					if(firstActivityKey)

					{

						query.setWhere("AND", 1, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

								  	 	strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

						firstActivityKey = false;

					}

					else // the in between clauses

					{

						query.setWhere("OR", 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

							  	 	strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

					}

				}

				else

				{

					// if only has one key

					if(firstActivityKey)

					{

						query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

								  	 	strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

						firstActivityKey = false;

					}

					else // closing where clause

					{

						query.setWhere("OR", 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

								  	 	strWorkflowActivityKey, 1, DALQuery.WHERE_HAS_VALUE);

					}

				}

			}



			ResultSet rsResultSet = query.executeSelect();

            Vector vecResult = QueryChannel.lookupRecord(rsResultSet, vecWorkflowActivityParameterFormFields);

          

			for(int intIndex = 0; intIndex < vecResult.size(); intIndex++)

            {

				Hashtable hashWorkflowTaskParameterData = new Hashtable();

				Hashtable hashMapping = new Hashtable();

				WorkflowManager.buildWorkflowActivityParameterAndWorkflowTaskParameterMapping(hashMapping);

				Hashtable hashResult = (Hashtable) vecResult.get(intIndex);

				WorkflowManager.transferData(hashResult, hashWorkflowTaskParameterData, hashMapping);



				query.reset();



				query.setDomain("WORKFLOW_TASK_PARAMETER", null, null, null);

				hashWorkflowTaskParameterData.put("WORKFLOW_TASK_PARAMETER_intWorkflowTaskKey",

						hashActivityTaskKeys.get(hashResult.get("WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey")));

				query.setFields(vecWorkflowTaskParameterFormFields, hashWorkflowTaskParameterData);

				query.executeInsert();

            }

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::copyWorkflowActivityParameterToWorkflowTaskParameter - " +

					e.toString(), e);

        }

	}



	/**

	 * build the mapping between workflow activity parameter and workflow task parameter

	 *

	 * @param hashMapping 	the hashtable where the mapping will be inserted

	 */

	private static void buildWorkflowActivityParameterAndWorkflowTaskParameterMapping(Hashtable hashMapping)

	{

		try

		{

			hashMapping.put("WORKFLOW_ACTIVITY_PARAMETER_strName", "WORKFLOW_TASK_PARAMETER_strName");

			hashMapping.put("WORKFLOW_ACTIVITY_PARAMETER_strType", "WORKFLOW_TASK_PARAMETER_strType");

			hashMapping.put("WORKFLOW_ACTIVITY_PARAMETER_strDescription", "WORKFLOW_TASK_PARAMETER_strDescription");

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::buildWorkflowActivityParameterAndWorkflowTaskParameterMapping - " +

					e.toString(), e);

        }

	}



	/**

	 * copy all necessary details from workflow template to workflow instance

	 *

	 * @param strWorkflowTemplateKey	the workflow template key

	 * @param hashTemplateInstanceKeys	the workflow template and instance key mapping

	 * @param query						the query object

	 */

	protected static void copyWorkflowTemplateToWorkflowInstance(String strWorkflowTemplateKey, 

															   Hashtable hashTemplateInstanceKeys,

															   DALSecurityQuery query)

	{

		try

		{

			// get the workflow template and workflow instance field details

            Vector vecWorkflowTemplateFormFields = new Vector(DatabaseSchema.getFormFields("workflow_template_details"));

            Vector vecWorkflowInstanceFormFields = DatabaseSchema.getFormFields("workflow_instance_details");



			query.reset();



			query.setDomain("WORKFLOW_TEMPLATE", null, null, null);

			vecWorkflowTemplateFormFields.add("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");

            query.setFields(vecWorkflowTemplateFormFields, null);

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

            Vector vecResult = QueryChannel.lookupRecord(rsResultSet, vecWorkflowTemplateFormFields);

          

			for(int intIndex = 0; intIndex < vecResult.size(); intIndex++)

            {

				Hashtable hashWorkflowInstanceData = new Hashtable();

				Hashtable hashMapping = new Hashtable();

				WorkflowManager.buildWorkflowTemplateAndWorkflowInstanceMapping(hashMapping);

				Hashtable hashResult = (Hashtable) vecResult.get(intIndex);

				WorkflowManager.transferData(hashResult, hashWorkflowInstanceData, hashMapping);

				Calendar currentTime = Calendar.getInstance();

	

				String strDateCreated = WorkflowManager.getDate(currentTime) + "/" + 

										WorkflowManager.getMonth(currentTime) + "/" + 

										currentTime.get(Calendar.YEAR);



				String strCurrentTime = currentTime.get(Calendar.HOUR_OF_DAY) + ":" +

										currentTime.get(Calendar.MINUTE) + ":" +

										currentTime.get(Calendar.SECOND);



				//System.err.println("strDateCreated = >>>" + strDateCreated + "<<<");

				//System.err.println("strCurrentTime = >>>" + strCurrentTime + "<<<");

				//System.err.println("vecWorkflowTemplateFormFields = " + vecWorkflowTemplateFormFields);

				//System.err.println("hashWorkflowInstanceData = " + hashWorkflowInstanceData);



				query.reset();

				query.setDomain("WORKFLOW_INSTANCE", null, null, null);

				hashWorkflowInstanceData.put("WORKFLOW_INSTANCE_dtDateCreated", strDateCreated);

				hashWorkflowInstanceData.put("WORKFLOW_INSTANCE_strTimeCreated", strCurrentTime);

				hashWorkflowInstanceData.put("WORKFLOW_INSTANCE_strStatus", WORKFLOW_INSTANCE_STATUS_NOT_STARTED);

				query.setFields(vecWorkflowInstanceFormFields, hashWorkflowInstanceData);

				query.executeInsert();



				String strWorkflowInstanceKey = QueryChannel.getNewestKeyAsString(query);

				//									"WORKFLOW_INSTANCE_intWorkflowInstanceKey");

		

				//if the hashtable contains the template key, add the instance to the vector

				if(hashTemplateInstanceKeys.containsKey(strWorkflowTemplateKey))

				{

					Vector vecElement = (Vector) hashTemplateInstanceKeys.get(strWorkflowTemplateKey);

					vecElement.add(strWorkflowInstanceKey);

					hashTemplateInstanceKeys.put(strWorkflowTemplateKey, vecElement);

				}

				else

				//else create a new vector and add the instance key to the vector

				{

					Vector vecElement = new Vector();

					vecElement.add(strWorkflowInstanceKey);

					hashTemplateInstanceKeys.put(strWorkflowTemplateKey, vecElement);

				}

				//fill up the mapping template - instance mapping

				//hashTemplateInstanceKeys.put(strWorkflowTemplateKey, strWorkflowInstanceKey);



				//get the initial activity Id

				//strInitialActivityKey = (String) hashResult.get("WORKFLOW_TEMPLATE_intInitialActivityKey");

            }

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::copyWorkflowTemplateToWorkflowInstance - " + e.toString(), e);

        }

	}



	/**

	 * build the mapping between workflow template and workflow instance

	 *

	 * @param hashMapping 	the hashtable where the mapping will be inserted

	 */

	private static void buildWorkflowTemplateAndWorkflowInstanceMapping(Hashtable hashMapping)

	{

		try

		{

			hashMapping.put("WORKFLOW_TEMPLATE_strName", "WORKFLOW_INSTANCE_strName");

			hashMapping.put("WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "WORKFLOW_INSTANCE_intWorkflowTemplateKey");

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::buildWorkflowTemplateAndWorkflowInstanceMapping - " +

					e.toString(), e);

        }

	}



	/**

	 * copy all necessary details from workflow activity to workflow task

	 *

	 * @param strWorkflowTemplateKey	the workflow template ke

	 * @param hashActivityTaskKeys		the activity and task keys mapping

	 * @param hashActivityTaskTemplateKeys	the activity and task keys mapping for a particular template only

	 * @param vecWorkflowTemplateKey	the workflow template key list (to be instantiated)

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 */

	protected static void copyWorkflowActivityToWorkflowTask(String strWorkflowTemplateKey,

														   Hashtable hashActivityTaskKeys,

														   Hashtable hashActivityTaskTemplateKeys,

														   Vector vecWorkflowTemplateKey,

														   String strWorkflowInstanceKey,

														   DALSecurityQuery query)

	{

		try

		{

			// get the workflow template and workflow instance field details

            Vector vecWorkflowActivityFormFields = new Vector(DatabaseSchema.getFormFields("workflow_activity_details"));

            Vector vecWorkflowTaskFormFields = DatabaseSchema.getFormFields("workflow_task_details");



			query.reset();



			query.setDomain("WORKFLOW_ACTIVITY", null, null, null);

			vecWorkflowActivityFormFields.add("WORKFLOW_ACTIVITY_intActivityKey");

            query.setFields(vecWorkflowActivityFormFields, null);

			//query.setField("WORKFLOW_ACTIVITY_intActivityKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

            Vector vecResult = QueryChannel.lookupRecord(rsResultSet, vecWorkflowActivityFormFields);

          

			for(int intIndex = 0; intIndex < vecResult.size(); intIndex++)

            {

				Hashtable hashWorkflowTaskData = new Hashtable();

				Hashtable hashMapping = new Hashtable();

				WorkflowManager.buildWorkflowActivityAndWorkflowTaskMapping(hashMapping);

				WorkflowManager.transferData((Hashtable) vecResult.get(intIndex), hashWorkflowTaskData, hashMapping);



//System.err.println("Task Name = >>>" + hashWorkflowTaskData.get("WORKFLOW_TASK_strName") + "<<<");

//System.err.println("hashWorkflowTaskData = >>>" + hashWorkflowTaskData + "<<<");



			

				//need to comment this as we only want to instantiate the parent wf not the children/sub wf

				/*

				String strWorkflowActivityType = ((String)

								((Hashtable) vecResult.get(intIndex)).get("WORKFLOW_ACTIVITY_strType"));

				

				//insert the sub workflow template key to the vector if the activity type is "sub workflow"

				if(strWorkflowActivityType.equals(WORKFLOW_TASK_TYPE_SUBWORKFLOW))

				{

					String strSubWorkflowTemplateKey = ((String)

								((Hashtable) vecResult.get(intIndex)).get("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey"));

					vecWorkflowTemplateKey.add(strSubWorkflowTemplateKey);

				}

				*/



				//String strWorkflowInstanceKey = QueryChannel.getNewestKeyAsString(

				//									"WORKFLOW_INSTANCE_intWorkflowInstanceKey");



				query.reset();

				query.setDomain("WORKFLOW_TASK", null, null, null);

				hashWorkflowTaskData.put("WORKFLOW_TASK_strStatus", TASK_STATUS_NOT_ACTIVE);

				hashWorkflowTaskData.put("WORKFLOW_TASK_intWorkflowInstanceKey", strWorkflowInstanceKey);

				query.setFields(vecWorkflowTaskFormFields, hashWorkflowTaskData);

				query.executeInsert();



//System.err.println("Task Name = >>>" + hashWorkflowTaskData.get("WORKFLOW_TASK_strName") + "<<<");





				String strWorkflowActivityKey = ((String)

								((Hashtable) vecResult.get(intIndex)).get("WORKFLOW_ACTIVITY_intActivityKey"));

				String strWorkflowTaskKey = QueryChannel.getNewestKeyAsString(query); //"WORKFLOW_TASK_intTaskKey");



				//Fill in the hashActivityTaskKeys and hashActivityTaskTemplateKeys

				//hashActivityTaskKeys.put(strWorkflowActivityKey, strWorkflowTaskKey);

				hashActivityTaskTemplateKeys.put(strWorkflowActivityKey, strWorkflowTaskKey);



				//if the hashtable contains the activity key, add the task to the vector

				if(hashActivityTaskKeys.containsKey(strWorkflowActivityKey))

				{

					Vector vecElement = (Vector) hashActivityTaskKeys.get(strWorkflowActivityKey);

					vecElement.add(strWorkflowTaskKey);

					hashActivityTaskKeys.put(strWorkflowActivityKey, vecElement);

				}

				else

				//else create a new vector and add the task key to the vector

				{

					Vector vecElement = new Vector();

					vecElement.add(strWorkflowTaskKey);

					hashActivityTaskKeys.put(strWorkflowActivityKey, vecElement);

				}

            }

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::copyWorkflowActivityToWorkflowTask - " + e.toString(), e);

        }

	}



	/**

	 * build the mapping between workflow activity and workflow task 

	 *

	 * @param hashMapping 	the hashtable where the mapping will be inserted

	 */

	private static void buildWorkflowActivityAndWorkflowTaskMapping(Hashtable hashMapping)

	{

		try

		{

			hashMapping.put("WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId", "WORKFLOW_TASK_strWorkflowActivityXPDLId");

			hashMapping.put("WORKFLOW_ACTIVITY_strName", "WORKFLOW_TASK_strName");

			hashMapping.put("WORKFLOW_ACTIVITY_strPriority", "WORKFLOW_TASK_strPriority");

			hashMapping.put("WORKFLOW_ACTIVITY_intPerformer", "WORKFLOW_TASK_intPerformer");

			hashMapping.put("WORKFLOW_ACTIVITY_strPerformerType", "WORKFLOW_TASK_strPerformerType");

			hashMapping.put("WORKFLOW_ACTIVITY_strReassignable", "WORKFLOW_TASK_strReassignable");

			hashMapping.put("WORKFLOW_ACTIVITY_strSignoff", "WORKFLOW_TASK_strSignoff");

			hashMapping.put("WORKFLOW_ACTIVITY_strInstruction", "WORKFLOW_TASK_strInstruction");

			hashMapping.put("WORKFLOW_ACTIVITY_intFunctionKey", "WORKFLOW_TASK_intFunctionKey");

			hashMapping.put("WORKFLOW_ACTIVITY_strIfFail", "WORKFLOW_TASK_strIfFail");

			hashMapping.put("WORKFLOW_ACTIVITY_strRetryDelayUnit", "WORKFLOW_TASK_strRetryDelayUnit");

			hashMapping.put("WORKFLOW_ACTIVITY_flRetryDelayValue", "WORKFLOW_TASK_flRetryDelayValue");

			hashMapping.put("WORKFLOW_ACTIVITY_intRetryCounter", "WORKFLOW_TASK_intRetryCounter");

			hashMapping.put("WORKFLOW_ACTIVITY_strIfFailAfterRetry", "WORKFLOW_TASK_strIfFailAfterRetry");

			hashMapping.put("WORKFLOW_ACTIVITY_intCompletedTask", "WORKFLOW_TASK_intCompletedTask");

			hashMapping.put("WORKFLOW_ACTIVITY_strMultiAction", "WORKFLOW_TASK_strMultiAction");

			hashMapping.put("WORKFLOW_ACTIVITY_strTriggerUnit", "WORKFLOW_TASK_strTriggerUnit");

			hashMapping.put("WORKFLOW_ACTIVITY_flTriggerValue", "WORKFLOW_TASK_flTriggerValue");

			hashMapping.put("WORKFLOW_ACTIVITY_strFinishedUnit", "WORKFLOW_TASK_strFinishedUnit");

			hashMapping.put("WORKFLOW_ACTIVITY_flFinishedValue", "WORKFLOW_TASK_flFinishedValue");

			hashMapping.put("WORKFLOW_ACTIVITY_strIntervalUnit", "WORKFLOW_TASK_strIntervalUnit");

			hashMapping.put("WORKFLOW_ACTIVITY_strIntervalValue", "WORKFLOW_TASK_strIntervalValue");

			hashMapping.put("WORKFLOW_ACTIVITY_strType", "WORKFLOW_TASK_strType");

			hashMapping.put("WORKFLOW_ACTIVITY_intWorkingDays", "WORKFLOW_TASK_intWorkingDays");

			hashMapping.put("WORKFLOW_ACTIVITY_strSendTriggerAlertToPerformer",

							"WORKFLOW_TASK_strSendTriggerAlertToPerformer");

			hashMapping.put("WORKFLOW_ACTIVITY_intSendTriggerAlertToOtherKey",

							"WORKFLOW_TASK_intSendTriggerAlertToOtherKey");

			hashMapping.put("WORKFLOW_ACTIVITY_strSendTriggerAlertToOtherType",

							"WORKFLOW_TASK_strSendTriggerAlertToOtherType");

			hashMapping.put("WORKFLOW_ACTIVITY_strRecurringTriggerAlert",

							"WORKFLOW_TASK_strRecurringTriggerAlert");

			hashMapping.put("WORKFLOW_ACTIVITY_strRecurringTriggerAlertUnit", "WORKFLOW_TASK_strRecurringTriggerAlertUnit");

			hashMapping.put("WORKFLOW_ACTIVITY_flRecurringTriggerAlertValue",

							"WORKFLOW_TASK_flRecurringTriggerAlertValue");

			hashMapping.put("WORKFLOW_ACTIVITY_strSendFinishAlertToPerformer",

							"WORKFLOW_TASK_strSendFinishAlertToPerformer");

			hashMapping.put("WORKFLOW_ACTIVITY_intSendFinishAlertToOtherKey", "WORKFLOW_TASK_intSendFinishAlertToOtherKey");

			hashMapping.put("WORKFLOW_ACTIVITY_strSendFinishAlertToOtherType",

							"WORKFLOW_TASK_strSendFinishAlertToOtherType");

			hashMapping.put("WORKFLOW_ACTIVITY_strRecurringFinishAlert", "WORKFLOW_TASK_strRecurringFinishAlert");

			hashMapping.put("WORKFLOW_ACTIVITY_strRecurringFinishAlertUnit", "WORKFLOW_TASK_strRecurringFinishAlertUnit");

			hashMapping.put("WORKFLOW_ACTIVITY_flRecurringFinishAlertValue", "WORKFLOW_TASK_flRecurringFinishAlertValue");

			hashMapping.put("WORKFLOW_ACTIVITY_intPosX", "WORKFLOW_TASK_intPosX");

			hashMapping.put("WORKFLOW_ACTIVITY_intPosY", "WORKFLOW_TASK_intPosY");

			hashMapping.put("WORKFLOW_ACTIVITY_intWidth", "WORKFLOW_TASK_intWidth");

			hashMapping.put("WORKFLOW_ACTIVITY_intHeight", "WORKFLOW_TASK_intHeight");
                        
			hashMapping.put("WORKFLOW_ACTIVITY_strThread", "WORKFLOW_TASK_strThread");                        

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::buildWorkflowActivityAndWorkflowTaskMapping - " +

					e.toString(), e);

        }

	}



	// TASK RELATED METHODS



	/**

	 * Causes the currently executing thread to sleep (temporarily cease execution) 

	 * for the specified number of milliseconds. The thread does not lose ownership of any monitors.

	 *

	 * @param strRetryDelayUnit	the retry delay unit

	 * @param strRetryDelayValue	the retry delay value

	 */

	protected static void sleep(String strRetryDelayUnit, String strRetryDelayValue)

	{

		try

		{

			if(strRetryDelayUnit.equals(WORKFLOW_TASK_RETRY_DELAY_UNIT_SECOND))

			{ 

				//1 sec = 1000 msec

				long longWaitTime = (Long.valueOf(strRetryDelayValue)).longValue();

				long longWaitTimeinMilliseconds = longWaitTime * Long.parseLong("1000");

				Thread.sleep(longWaitTimeinMilliseconds);

			}

			else if(strRetryDelayUnit.equals(WORKFLOW_TASK_RETRY_DELAY_UNIT_MINUTE))

			{

				//1 minute = 60000 msec

				long longWaitTime = (Long.valueOf(strRetryDelayValue)).longValue();

				long longWaitTimeinMilliseconds = longWaitTime * Long.parseLong("60000");

				Thread.sleep(longWaitTimeinMilliseconds);

			}

			else if(strRetryDelayUnit.equals(WORKFLOW_TASK_RETRY_DELAY_UNIT_HOUR))

			{

				//1 hour = 3600000

				long longWaitTime = (Long.valueOf(strRetryDelayValue)).longValue();

				long longWaitTimeinMilliseconds = longWaitTime * Long.parseLong("3600000");

				Thread.sleep(longWaitTimeinMilliseconds);

			}

			else if(strRetryDelayUnit.equals(WORKFLOW_TASK_RETRY_DELAY_UNIT_DAY))

			{

				//1 day = 86400000 msec

				long longWaitTime = (Long.valueOf(strRetryDelayValue)).longValue();

				long longWaitTimeinMilliseconds = longWaitTime * Long.parseLong("86400000");

				Thread.sleep(longWaitTimeinMilliseconds);

			}

			else if(strRetryDelayUnit.equals(WORKFLOW_TASK_RETRY_DELAY_UNIT_MONTH))

			{

				//1 month = 30 days = 2592000000 msec

				long longWaitTime = (Long.valueOf(strRetryDelayValue)).longValue();

				long longWaitTimeinMilliseconds = longWaitTime * Long.parseLong("2592000000");

				Thread.sleep(longWaitTimeinMilliseconds);

			}

			else if(strRetryDelayUnit.equals(WORKFLOW_TASK_RETRY_DELAY_UNIT_YEAR))

			{

				//1 year = 365 days = 31536000000000 msec

				long longWaitTime = (Long.valueOf(strRetryDelayValue)).longValue();

				long longWaitTimeinMilliseconds = longWaitTime * Long.parseLong("31536000000000");

				Thread.sleep(longWaitTimeinMilliseconds);

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::sleep - " + e.toString(), e);

        }

	}



	/**

	 * to prepare and excute the function

	 *

	 * @param strTaskKey	the task key

	 * @param runtimeData	the runtime data

	 * @param query			the query object

	 *

	 * @return	indicate whether the function was executed succesfully or not

	 */

	protected static Boolean prepareAndExecuteFunction(String strTaskKey, ChannelRuntimeData runtimeData,

													 DALSecurityQuery query)

	{

		Boolean blResultValue = Boolean.valueOf(false);



		try

		{

			//get the function key

			String strFunctionKey = WorkflowManager.getFunctionKey(strTaskKey, query);

			ResultSet rsFunctionDetails = WorkflowManager.getFunctionDetails(strFunctionKey, query);



//System.err.println("strFunctionKey = " + strFunctionKey);

			

			String strClassName = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strClassName"); 

			String strFunctionName = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strFunctionName"); 

			String strFunctionParameterTypes = rsFunctionDetails.getString(

												"WORKFLOW_FUNCTION_strFunctionParameterTypes"); 

			String strFunctionParameterNames = rsFunctionDetails.getString(

												"WORKFLOW_FUNCTION_strFunctionParameterNames"); 



//System.err.println("strClassName = " + strClassName);

//System.err.println("strFunctionName = " + strFunctionName);

//System.err.println("strFunctionParameterTypes = " + strFunctionParameterTypes);

//System.err.println("strFunctionParameterNames = " + strFunctionParameterNames);



			//construct the parameter types - if required!
                        
                        if (strFunctionParameterTypes != null && strFunctionParameterTypes.length()>0)
                        {

                            String[] arrParameterTypes = strFunctionParameterTypes.split(",");

                            Class[] clsParameterTypes = new Class[arrParameterTypes.length];



    //System.err.println("arrParameterTypes.length = " + arrParameterTypes.length);



                            for(int intIndex1 = 0; intIndex1 < arrParameterTypes.length; intIndex1++)

                            {

                                    clsParameterTypes[intIndex1] = Class.forName((String) arrParameterTypes[intIndex1]);

                            }



                            //construct the arguments

                            String[] arrParameterNames = strFunctionParameterNames.split(",");

                            Object[] arguments = new Object[arrParameterNames.length];



    //System.err.println("arrParameterNames.length = " + arrParameterNames.length);



                            //get the parameters type

                            //Hashtable hashNextParameterType = WorkflowManager.getParameterType(strTaskKey, query);

                            String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strTaskKey, query);

                            String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(strWorkflowInstanceKey, query);

                            Hashtable hashParameterType = WorkflowManager.getWorkflowTemplateParameterType(strWorkflowTemplateKey, query);



    //System.err.println("prepareAndExecuteFunction - hashParameterType = " + hashParameterType);



                            for(int intIndex2 = 0; intIndex2 < arrParameterNames.length; intIndex2++)

                            {

                                    String strParameterName = arrParameterNames[intIndex2];

                                    String strParameterType = (String) hashParameterType.get(strParameterName);



    //System.err.println("prepareAndExecuteFunction - strParameterName = " + strParameterName);

    //System.err.println("prepareAndExecuteFunction - strParameterType = " + strParameterType);

    //System.err.println("agustian - start");

    //System.err.println("prepareAndExecuteFunction - runtimeData = " + runtimeData);

    //System.err.println("prepareAndExecuteFunction - runtimeData.getParameter(strParameterName) = " + String.valueOf(runtimeData.get(strParameterName)));

    //System.err.println("agustian - end");





                                    if(strParameterType.equals("String"))

                                    {

                                            arguments[intIndex2] = runtimeData.getParameter(strParameterName);

                                    }

                                    else if(strParameterType.equals("Integer"))

                                    {

                                            arguments[intIndex2] = Integer.valueOf(runtimeData.getParameter(strParameterName));

                                    }

                                    else if(strParameterType.equals("Float"))

                                    {

                                            arguments[intIndex2] = Float.valueOf(runtimeData.getParameter(strParameterName));

                                    }

                                    else if(strParameterType.equals("Double"))

                                    {

                                            arguments[intIndex2] = Double.valueOf(runtimeData.getParameter(strParameterName));

                                    }

                                    else if(strParameterType.equals("Boolean"))

                                    {

                                            arguments[intIndex2] = Boolean.valueOf(runtimeData.getParameter(strParameterName));

                                    }

                            }



                            blResultValue = WorkflowManager.executeFunction(strClassName, strFunctionName,

                                                                                                                            clsParameterTypes, arguments);
                                                                                                                            
                        }
                        else
                        {
                            
                            // no parameter types or values!
                            blResultValue = WorkflowManager.executeFunction(strClassName, strFunctionName, null, null);
                        }
		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::prepareAndExecuteFunction - " + e.toString(), e);

        }



		return blResultValue;

	}

        

	/**

	 * to execute the function

	 *

	 * @param strClassName	the full path class name

	 * @param strFunctionName	the function name

	 * @param parameterTypes	the parameter types

	 * @param arguments			the arguments

	 *

	 * @return indicate whether the function was executed succesfully or not

	 */

	private static Boolean executeFunction(String strClassName, String strFunctionName,

										   Class[] parameterTypes, Object[] arguments)

	{

		Boolean blResultValue = Boolean.valueOf(false);



		try

		{

			Method method = null;

			Class functionClass = Class.forName(strClassName);

			Object functionObject = functionClass.newInstance();



			try

			{

				method = functionClass.getMethod(strFunctionName, parameterTypes);

				blResultValue = (Boolean) method.invoke(functionObject, arguments);

			}

			catch(NoSuchMethodException nsmee)

			{

				LogService.instance().log(LogService.ERROR,

					"NoSuchMethodException occur in WorkflowManager::executeFunction - " + nsmee.toString(), nsmee);

			}

			catch(IllegalAccessException iae)

			{

				LogService.instance().log(LogService.ERROR,

					"IllegalAccessException occur in WorkflowManager::executeFunction - " + iae.toString(), iae);

			}

			catch(InvocationTargetException ite)

			{

				LogService.instance().log(LogService.ERROR,

					"InvocationTargetException occur in WorkflowManager::executeFunction - " + ite.toString(), ite);

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::executeFunction - " + e.toString(), e);

        }



		return blResultValue;

	}



	/**

	 * to check on the condition

	 *

	 * @param strCondition	the condition

	 * @param runtimeData	the runtime data

	 * @param hashParameterType	the list of parameter type

	 *

	 * @return	true if the check condition is successful else return false

	 */

	protected static boolean checkCondition(String strCondition, ChannelRuntimeData runtimeData, Hashtable hashParameterType)

	{

		boolean blReturnValue = false;

		try

		{

			//instantiate the interpreter

			Interpreter bshInterpreter = new Interpreter();



			//transfer all data from runtimeData to the Interpreter object

			//WorkflowManager.transferRuntimeDataToInterpreter(runtimeData, bshInterpreter, hashParameterType);



			if(strCondition == null || strCondition.equals(""))

			{

				//if no condition that means it is always true

				blReturnValue = true;

			}

			else

			{

				//transfer all data from runtimeData to the Interpreter object

				WorkflowManager.transferRuntimeDataToInterpreter(runtimeData, bshInterpreter, hashParameterType);



				//run the evaluation of the script

				blReturnValue = ((Boolean) bshInterpreter.eval(strCondition)).booleanValue();

			}



		}

		catch(ParseException pe) 

		{

			LogService.instance().log(LogService.ERROR, "Parse Exception error in WorkflowManager::checkCondition - " + 

									  pe.toString());

        }

        catch(EvalError er) 

		{

			LogService.instance().log(LogService.ERROR, "Evaluation error in WorkflowManager::checkCondition - " + 

									  er.toString());

        }

        catch(Exception e)

        {

        	LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::checkCondition - " + 

									  e.toString());

        }

		finally

		{

			return blReturnValue;

		}

	}





	/**

	 * transfer all data from runtimeData to the Interpreter object

	 *

	 * @param	runtimeData	the runtime data

	 * @param	bshInterpreter	the bean shell java interpreter

	 * @param 	hashParameterType	the list of parameter type

	 */

	private static void transferRuntimeDataToInterpreter(ChannelRuntimeData runtimeData, Interpreter bshInterpreter,

														 Hashtable hashParameterType)

	{

		try

		{

			Enumeration enumRuntimeDataKeys = runtimeData.keys();

			while(enumRuntimeDataKeys.hasMoreElements())

			{

				String strRuntimeDataKey = (String) enumRuntimeDataKeys.nextElement();

				String strRuntimeDataValue = runtimeData.getParameter(strRuntimeDataKey);

				//String strRuntimeDataValue = (String) runtimeData.get(strRuntimeDataKey);

				

				//remove the leading and trailing whitespaces

				if(strRuntimeDataValue != null)

				{

					strRuntimeDataValue.trim();

				}

				else

				{

					strRuntimeDataValue = "";

				}



				boolean blValidChar = false;

				boolean blValidKey = false;



				//ensure that the Key is valid

				for(int intIndex = 0; intIndex < strRuntimeDataKey.length(); intIndex++)

				{

					String strKeyCharacter = String.valueOf(strRuntimeDataKey.charAt(intIndex));



					for(int intIndex2 = 0; intIndex2 < VALID_CHARACTERS.length(); intIndex2++)

					{

						String strValidCharacter = String.valueOf(VALID_CHARACTERS.charAt(intIndex2));



						if(strKeyCharacter.equals(strValidCharacter))

						{

							blValidChar = true;

							break;

						}

					}



					if(blValidChar)

					{

						blValidKey = true;

						blValidChar = false;

					}

					else

					{

						blValidKey = false;

						break;

					}

				}



				//if it is a valid key

				if(blValidKey)

				{

					String strRuntimeDataKeyType = (String)  hashParameterType.get(strRuntimeDataKey);

			

					//if we have the type of the key then set it else do not set it at all

					if(strRuntimeDataKeyType != null)

					{

						if((strRuntimeDataKeyType.equals("Integer") ||

						    strRuntimeDataKeyType.equals("Boolean") ||			

						    strRuntimeDataKeyType.equals("Date") ||			

						    strRuntimeDataKeyType.equals("Float") ||			

						    strRuntimeDataKeyType.equals("Double")) &&

							strRuntimeDataValue.length() != 0)

						{

							bshInterpreter.eval(strRuntimeDataKey + "=" + strRuntimeDataValue);

						}

						else if(strRuntimeDataKeyType.equals("String"))

						{

							bshInterpreter.eval(strRuntimeDataKey + "=" + "\"" + strRuntimeDataValue + "\"");

						}

					}

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::transferRuntimeDataToInterpreter - " + e.toString(), e);

        }

	}



	/**

	 * to get the parameter types for a given task key

	 *

	 * @param strTaskKey	the task key

	 *

	 * @return the hashtable that contains the parameter name and type pair

	 */

	protected static Hashtable getParameterType(String strTaskKey, DALSecurityQuery query)

	{

		Hashtable hashParameterType = new Hashtable();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_PARAMETER, null, null, null);

			query.setField("WORKFLOW_TASK_PARAMETER_strName", "null", "hashtable");

			query.setField("WORKFLOW_TASK_PARAMETER_strType", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_PARAMETER_intWorkflowTaskKey", "=",

								strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

		

			while(rsResultSet.next())

			{

				String strParameterName = rsResultSet.getString("WORKFLOW_TASK_PARAMETER_strName");

				String strParameterType = rsResultSet.getString("WORKFLOW_TASK_PARAMETER_strType");

				hashParameterType.put(strParameterName, strParameterType);

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getParameterType - " + e.toString(), e);

        }



		return hashParameterType;

	}



	/**

	 *  to check whether the minimum number of task to be completed has been met before moving to the next task

	 *

	 *  @param vecOriginTaskKeys	the task keys

	 *  @param strTargetTaskKey		the target task key

	 *  @param intRequiredCompletedTask	the number of required task that need to be completed

	 *  @param query	the query object

	 *

	 *  @return	the indicator to indicate whether the completed task requirement has been satisfied

	 */

	protected static boolean checkCompletedTask(Vector vecOriginTaskKeys, String strTargetTaskKey,

												int intRequiredCompletedTask, DALSecurityQuery query)

	{

		boolean blCompleted = false;



		try

		{

			int intActualCompletedTask = 0;

			Enumeration enumTaskKeys = vecOriginTaskKeys.elements();



			while(enumTaskKeys.hasMoreElements())

			{

				String strOriginTaskKey = (String) enumTaskKeys.nextElement();

				String strOriginTaskStatus = WorkflowManager.getTaskStatus(strOriginTaskKey, query);

				String strTaskTransitionType = 

						WorkflowManager.getTaskTransitionType(strOriginTaskKey, strTargetTaskKey, query);

				if(strOriginTaskStatus.equals(TASK_STATUS_COMPLETED) &&

				   strTaskTransitionType.equals(TRANSITION_TYPE_NORMAL))

				{

					intActualCompletedTask++;

				}

			}



			if(intActualCompletedTask >= intRequiredCompletedTask)

			{

				blCompleted = true;

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::checkCompletedTask - " + e.toString(), e);

        }



		return blCompleted;

	}



	// DELETE Workflow Instance


	/**
	 * delete workflow instance data in ix_report_sys_task_param
	 *
	 * String strWorkflowInstanceKey 	the workflow instance key
	 * DALSecurityQuery query			the query object
	 */
	public static void deleteWorkflowInstanceDataInReportSysTaskParam(String strWorkflowInstanceKey, DALSecurityQuery query)
	{
		try
		{
			query.reset();

			query.setDomain("RSTP", null, null, null);  
			query.setField("RSTP_intDeleted", "-1");
            query.setWhere(null, 0, "RSTP_strInstanceType", "=", "Workflow", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "RSTP_intInstanceKey", "=", strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);
            query.executeUpdate();
		}
		catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::deleteWorkflowInstanceDataInReportSysTaskParam" + e.toString(), e);
        }
	}

	/**

	 * delete workflow instance association with the domain from the ix_workflow_instance_to_domain

	 *

	 * String strWorkflowInstanceKey 	the workflow instance key

	 * DALSecurityQuery query			the query object

	 */

	public static void deleteWorkflowInstanceInDomainWorkflow(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain("WORKFLOW_INSTANCE_TO_DOMAIN", null, null, null);    

			query.setField("WORKFLOW_INSTANCE_TO_DOMAIN_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_TO_DOMAIN_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::deleteWorkflowInstanceInDomainWorkflow" + e.toString(), e);

        }

	}



	/**

	 * delete the workflow instance

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 */

	protected static void deleteWorkflowInstance(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setField("WORKFLOW_INSTANCE_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowInstance - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow task

	 *

	 * @param strWorkflowTaskKey		the workflow task key

	 * @param query						the query object

	 */

	protected static void deleteWorkflowTask(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTask - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow tasks for a given workflow instance key

	 *

	 * @param strWorkflowInstanceKey 	the workflow instance key

	 * @param query						the query object

	 */

	protected static void deleteWorkflowTasks(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTasks - " + e.toString(), e);

        }

	}



	

	/**

	 * delete the workflow task parameter

	 *

	 * @param strWorkflowTaskKey		the workflow task key

	 * @param query						the query object

	 */

	protected static void deleteWorkflowTaskParameter(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK_PARAMETER, null, null, null);    

			query.setField("WORKFLOW_TASK_PARAMETER_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_PARAMETER_intWorkflowTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTaskParameter - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow task transition 

	 *

	 * @param strWorkflowInstanceKey		the workflow instance key

	 * @param query							the query object

	 */

	protected static void deleteWorkflowTaskTransition(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);    

			query.setField("WORKFLOW_TASK_TRANSITION_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTaskTransition - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow runtimedata

	 *

	 * @param strWorkflowInstanceKey		the workflow instance key

	 * @param query							the query object

	 */

	protected static void deleteWorkflowRuntimedata(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_RUNTIMEDATA, null, null, null);    

			query.setField("WORKFLOW_RUNTIMEDATA_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_RUNTIMEDATA_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowRuntimedata - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow task history

	 *

	 * @param strWorkflowTaskKey			the workflow task key

	 * @param query							the query object

	 */

	protected static void deleteWorkflowTaskHistory(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK_HISTORY, null, null, null);    

			query.setField("WORKFLOW_TASK_HISTORY_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_HISTORY_intWorkflowTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTaskHistory - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow tasks history for a given workflow instance key

	 *

	 * @param strWorkflowInstanceKey 		the workflow instance key

	 * @param query							the query object

	 */

	protected static void deleteWorkflowTasksHistory(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK_HISTORY, null, null, null);    

			query.setField("WORKFLOW_TASK_HISTORY_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_HISTORY_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteWorkflowTasksHistory - " + e.toString(), e);

        }

	}



	// UTILITY METHODS



	/**

	 * get workflow task keys for a given workflow instance key

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 */

	protected static Vector getWorkflowTaskKeys(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		Vector vecWorkflowTaskKeys = new Vector();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

			query.setField("WORKFLOW_TASK_intTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey,

								 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			while(rsResultSet.next())

			{

				vecWorkflowTaskKeys.add(rsResultSet.getString("WORKFLOW_TASK_intTaskKey"));

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTaskKeys - " + e.toString(), e);

        }



		return vecWorkflowTaskKeys;

	}



	/**

	 * get workflow task key for a given workflow activity XPDL id and workflow instance key

	 *

	 * @param strWorkflowActivityXPDLId the workflow activity XPDL ID

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 */

	public static String getWorkflowTaskKey(String strWorkflowActivityXPDLId, 

			                                String strWorkflowInstanceKey,

											DALSecurityQuery query)

	{

		String strWorkflowTaskKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

			query.setField("WORKFLOW_TASK_intTaskKey", null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey,

								 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_strWorkflowActivityXPDLId", "=", strWorkflowActivityXPDLId,

								 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			if(rsResultSet.next())

			{

				strWorkflowTaskKey = rsResultSet.getString("WORKFLOW_TASK_intTaskKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTaskKey - " + e.toString(), e);

        }



		return strWorkflowTaskKey;

	}



	/**

	 * get the task key for a sub workflow task for a given sub workflow instance key

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 *

	 * @return	the task key

	 */

	public static String getSubWorkflowTaskKey(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		String strWorkflowTaskKey = null;

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_intTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intSubWorkflowInstanceKey", "=",

							strWorkflowInstanceKey , 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strWorkflowTaskKey = rsResultSet.getString("WORKFLOW_TASK_intTaskKey");

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getSubWorkflowTaskKey - " + e.toString(), e);

        }

		

		return strWorkflowTaskKey;

	}



	/**

	 * get all target task keys from the task transition table

	 *

	 * @param enumWorkflowInstanceKeys	the list of workflow instance keys 

	 * @param query	the query object

	 *

	 * @return	all the target task keys from the task transition table

	 */

	private static Vector getTargetTaskKeys(Enumeration enumWorkflowInstanceKeys, DALSecurityQuery query)

	{

		Vector vecTargetTaskKeys = new Vector();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

			query.setField("WORKFLOW_TASK_TRANSITION_intTargetTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



			while(enumWorkflowInstanceKeys.hasMoreElements())

			{

				String strWorkflowInstanceKey = (String) enumWorkflowInstanceKeys.nextElement();

				query.setWhere("OR", 1, "WORKFLOW_TASK_TRANSITION_intOriginTaskWorkflowInstanceKey", "=",

										strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskWorkflowInstanceKey", "=",

										strWorkflowInstanceKey, 1, DALQuery.WHERE_HAS_VALUE);

			}



			ResultSet rsResultSet = query.executeSelect();

			

			while(rsResultSet.next())

			{

				vecTargetTaskKeys.add(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_intTargetTaskKey"));

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTargetTaskKeys - " + e.toString(), e);

        }



		return vecTargetTaskKeys;

	}

	

	/**

	 * get the initial task key

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 *

	 * @return	the initial task key

	 */

	protected static String getInitialTaskKey(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		String strInitialTaskKey = null;

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_INSTANCE, null, null, null);

            query.setField("WORKFLOW_INSTANCE_intInitialTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

							strWorkflowInstanceKey , 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strInitialTaskKey = rsResultSet.getString("WORKFLOW_INSTANCE_intInitialTaskKey");

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getInitialTaskKey - " + e.toString(), e);

        }

		

		return strInitialTaskKey;

	}

	

	/**

	 * get the sub workflow instance key

	 *

	 * @param strWorkflowTaskKey	the workflow task key

	 * @param query					the query object

	 *

	 * @return	the sub workflow instance key

	 */

	protected static String getSubWorkflowInstanceKey(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		String strSubWorkflowInstanceKey = null;

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_intSubWorkflowInstanceKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strWorkflowTaskKey , 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strSubWorkflowInstanceKey = rsResultSet.getString("WORKFLOW_TASK_intSubWorkflowInstanceKey");

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getSubWorkflowInstanceKey - " + e.toString(), e);

        }

		

		return strSubWorkflowInstanceKey;

	}



	/**

	 * get the sub workflow instance keys

	 *

	 * @param strWorkflowInstanceKey 	the workflow instance key

	 * @param query						the query object

	 *

	 * @return	the sub workflow instance keys in a hashtable

	 */

	protected static Hashtable getSubWorkflowInstanceKeys(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		Hashtable hashSubWorkflowInstanceKeys = new Hashtable();



		try

		{

			//DALQuery taskKeyQuery = new DALQuery();



			//taskKeyQuery.setDomain(WORKFLOW_TASK, null, null, null);

			//taskKeyQuery.setField("WORKFLOW_TASK_intTaskKey", null);

			//taskKeyQuery.setWhere(null, 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			//taskKeyQuery.setWhere("AND", 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey,

			//					 0, DALQuery.WHERE_HAS_VALUE);



			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_intTaskKey", null);

            query.setField("WORKFLOW_TASK_intSubWorkflowInstanceKey", null);

			//query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "IN", taskKeyQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);

			query.setWhere(null, 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey,

								 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				String strTaskKey = rsResultSet.getString("WORKFLOW_TASK_intTaskKey");

           		String strTaskSubWorkflowInstanceKey = rsResultSet.getString("WORKFLOW_TASK_intSubWorkflowInstanceKey");



				if(strTaskSubWorkflowInstanceKey != null)

				{

					hashSubWorkflowInstanceKeys.put(strTaskKey, strTaskSubWorkflowInstanceKey);

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getSubWorkflowInstanceKeys - " + e.toString(), e);

        }



		return hashSubWorkflowInstanceKeys;

	}



	/**

	 * create a new task transition, to link subworkflow task to human and system task

	 *

	 * @param strOriginWorflowTaskKey	the origin task key

	 * @param strTargetTaskKey			the target task key

	 * @param strOriginWorkflowInstanceKey	the origin workflow instance key

	 * @param strTargetWorkflowInstanceKey	the target workflow instance key

	 * @param query 						the query object

	 */

	private static void createTaskTransition(String strOriginWorflowTaskKey, String strTargetTaskKey,

											 String strOriginWorkflowInstanceKey, String strTargetWorkflowInstanceKey,

											 DALSecurityQuery query)

	{

		try

		{

			Vector vecWorkflowTaskTransitionFormFields = DatabaseSchema.getFormFields("workflow_task_transition_details");

			Hashtable hashWorkflowTaskTransitionData = new Hashtable();



			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

			hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intOriginTaskKey", strOriginWorflowTaskKey);

			hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intTargetTaskKey", strTargetTaskKey);

			hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intOriginTaskWorkflowInstanceKey",

														strOriginWorkflowInstanceKey);

			hashWorkflowTaskTransitionData.put("WORKFLOW_TASK_TRANSITION_intTargetTaskWorkflowInstanceKey",

														strTargetWorkflowInstanceKey);

			query.setFields(vecWorkflowTaskTransitionFormFields, hashWorkflowTaskTransitionData);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::createTaskTransition - " + e.toString(), e);

        }

	}

	

	/**

	 * get the activity Type

	 *

	 * @param strActivityKey	the activity key

	 * @param query 			the query object

	 *

	 * @return	the activity type

	 */

	protected static String getActivityType(String strActivityKey, DALSecurityQuery query)

	{

		String strActivityType = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_strType", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intActivityKey", "=", strActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			if(rsResultSet.next())

			{

				strActivityType = rsResultSet.getString("WORKFLOW_ACTIVITY_strType");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getActivityType - " + e.toString(), e);

        }



		return strActivityType;

	}



	/**

	 * get the sub workflow template key 

	 *

	 * @param strActivityKey	the activity key

	 * @param query 			the query object

	 *

	 * @return	the sub workflow template key 

	 */

	protected static String getSubWorkflowTemplateKey(String strActivityKey, DALSecurityQuery query)

	{

		String strSubWorkflowTemplateKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intActivityKey", "=", strActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			if(rsResultSet.next())

			{

				strSubWorkflowTemplateKey = rsResultSet.getString("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getSubWorkflowTemplateKey - " + e.toString(), e);

        }



		return strSubWorkflowTemplateKey;

	}



	/**

	 * update the subworkflow instance key

	 *

	 * @param strSubWorkflowInstanceKey		the sub workflow instance key

	 * @param strTaskKey		the task key

	 * @param query 			the query object

	 */

	protected static void updateSubWorkflowInstanceKey(String strSubWorkflowInstanceKey, String strTaskKey,

													 DALSecurityQuery query)

	{

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK , null, null, null);

			query.setField("WORKFLOW_TASK_intSubWorkflowInstanceKey", strSubWorkflowInstanceKey, "hashtable");


			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateSubWorkflowInstanceKey - " + e.toString(), e);

        }

	}



	/**

	 * get system task failure related details

	 *

	 * @param strTaskKey	the task key

	 * @param query		the query object

	 *

	 * @return	the system task failure related details

	 */

	protected static ResultSet getSystemTaskFailureDetails(String strTaskKey, DALSecurityQuery query)

	{

		ResultSet rsSystemFailureDetails = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_strIfFail", "null", "hashtable");

            query.setField("WORKFLOW_TASK_strRetryDelayUnit", "null", "hashtable");

            query.setField("WORKFLOW_TASK_flRetryDelayValue", "null", "hashtable");

            query.setField("WORKFLOW_TASK_intRetryCounter", "null", "hashtable");

            query.setField("WORKFLOW_TASK_strIfFailAfterRetry", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            rsSystemFailureDetails = query.executeSelect();



			//move to the first(only) record

			rsSystemFailureDetails.first();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getSystemTaskFailureDetails - " + e.toString(), e);

        }



		return rsSystemFailureDetails;

	}



	/**

	 * get the function details

	 *

	 * @param strFunctionKey	the function key

	 * @param query	the query object

	 *

	 * @return the function details

	 */

	protected static ResultSet getFunctionDetails(String strFunctionKey, DALSecurityQuery query)

	{

		ResultSet rsFunctionDetails = null;



		try

		{

			Vector vecFunctionDetails = DatabaseSchema.getFormFields("workflow_function_details");



			query.reset();



			query.setDomain(WORKFLOW_FUNCTION, null, null, null);

            query.setFields(vecFunctionDetails, null);

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_FUNCTION_intWorkflowFunctionKey", "=",

								strFunctionKey, 0, DALQuery.WHERE_HAS_VALUE);

			rsFunctionDetails = query.executeSelect();



			//move to the first(only) record

			rsFunctionDetails.first();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getFunctionDetails - " + e.toString(), e);

        }



		return rsFunctionDetails;

	}



	/**

	 * get the function key from a system task

	 * 

	 * @param strTaskKey	the task key

	 * @param query		the query object

	 *

	 * @return the function key

	 */

	protected static String getFunctionKey(String strTaskKey, DALSecurityQuery query)

	{

		String strFunctionKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK , null, null, null);

			query.setField("WORKFLOW_TASK_intFunctionKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			if(rsResultSet.next())

			{

				strFunctionKey = rsResultSet.getString("WORKFLOW_TASK_intFunctionKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getFunctionKey - " + e.toString(), e);

        }



		return strFunctionKey;

	}



					

	/**

	 * get the previous task keys 

	 *

	 * @param strTargetTaskKey	the target task key

	 * @param query		the query object

	 *

	 * @return	the previous task keys

	 */

	protected static Vector getPreviousTaskKeys(String strTargetTaskKey, DALSecurityQuery query)

	{

		Vector vecWorkflowTaskKeys = new Vector();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

			query.setField("WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskKey", "=",

					       strTargetTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			while(rsResultSet.next())

			{

				vecWorkflowTaskKeys.add(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_intOriginTaskKey"));

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getPreviousTaskKeys - " + e.toString(), e);

        }



		return vecWorkflowTaskKeys;

	}



	/**

	 * get the next task keys 

	 *

	 * @param strOriginTaskKey	the origin task key

	 * @param query		the query object

	 *

	 * @return	the next task keys

	 */

	protected static Vector getNextTaskKeys(String strOriginTaskKey, DALSecurityQuery query)

	{

		Vector vecWorkflowTaskKeys = new Vector();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

			query.setField("WORKFLOW_TASK_TRANSITION_intTargetTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "=",

					       strOriginTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			while(rsResultSet.next())

			{

				vecWorkflowTaskKeys.add(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_intTargetTaskKey"));

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getNextTaskKeys - " + e.toString(), e);

        }



		return vecWorkflowTaskKeys;

	}


	//agus start
	/**
	 * update the workflow instance previous status
	 *
	 * @param strPreviousStatus	the previous status of the task
	 * @param strWorkflowInstanceKey the workflow instance key
	 * @param query the query object
	 */
	public static void updateWorkflowInstancePreviousStatus(String strPreviousStatus,
										 			 String strWorkflowInstanceKey,
										 			 DALSecurityQuery query)
	{
		try
		{
			query.reset();

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    
			query.setField("WORKFLOW_INSTANCE_strPreviousStatus", strPreviousStatus);
			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",
						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
			query.executeUpdate();
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::updateWorkflowInstancePreviousStatus - " + e.toString(), e);
        }
	}

	/**
	 * get the workflow instance previous status 
	 *
	 * @param strWorkflowInstanceKey	the workflow instance key
	 * @param query					the query object
	 *
	 * @return the workflow instance previous status 
	 */
	public static String getWorkflowInstancePreviousStatus(String strWorkflowInstanceKey, DALSecurityQuery query)
	{
		String strWorkflowInstancePreviousStatus = null;

		try
		{
			query.reset();

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);
            query.setField("WORKFLOW_INSTANCE_strPreviousStatus", null);
			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",
								strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();

			if(rsResultSet.next())
			{
           		strWorkflowInstancePreviousStatus = rsResultSet.getString("WORKFLOW_INSTANCE_strPreviousStatus");
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::getWorkflowInstancePreviousStatus - " + e.toString(), e);
        }

		return strWorkflowInstancePreviousStatus;
	}

	//agus end

	/**

	 * update the workflow instance status

	 *

	 * @param strStatus	the status of the task

	 * @param strWorkflowInstanceKey the workflow instance key

	 * @param query the query object

	 */

	public static void updateWorkflowInstanceStatus(String strStatus,

										 			 String strWorkflowInstanceKey,

										 			 DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setField("WORKFLOW_INSTANCE_strStatus", strStatus, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateWorkflowInstanceStatus - " + e.toString(), e);

        }

	}



	/**

	 * get the workflow instance status 

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query					the query object

	 *

	 * @return the workflow instance status 

	 */

	public static String getWorkflowInstanceStatus(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		String strWorkflowInstanceStatus = null;

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_INSTANCE, null, null, null);

            query.setField("WORKFLOW_INSTANCE_strStatus", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

								strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strWorkflowInstanceStatus = rsResultSet.getString("WORKFLOW_INSTANCE_strStatus");

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowInstanceStatus - " + e.toString(), e);

        }

		

		return strWorkflowInstanceStatus;

	}



	/**

	 * get the workflow instance key

	 *

	 * @param strWorkflowTaskKey	the workflow task key

	 * @param query					the query object

	 *

	 * @return the workflow instance key

	 */

	public static String getWorkflowInstanceKey(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		String strWorkflowInstanceKey = null;

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_intWorkflowInstanceKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strWorkflowTaskKey , 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strWorkflowInstanceKey = rsResultSet.getString("WORKFLOW_TASK_intWorkflowInstanceKey");

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowInstanceKey - " + e.toString(), e);

        }

		

		return strWorkflowInstanceKey;

	}



	

	/**

	 * get task condition 

	 *

	 * @param strOriginTaskKey	the origin task key

	 * @param strTargetTaskKey	the target task key

	 * @param query		the query object

	 *

	 * @return	the task condition

	 */

	protected static String getTaskCondition(String strOriginTaskKey, String strTargetTaskKey, DALSecurityQuery query)

	{

		String strTaskCondition = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

            query.setField("WORKFLOW_TASK_TRANSITION_strCondition", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "=", strOriginTaskKey,

									0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskKey", "=", strTargetTaskKey,

									0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskCondition = rsResultSet.getString("WORKFLOW_TASK_TRANSITION_strCondition");

			}



			/*

			if(strTaskCondition != null && !strTaskCondition.equals(""))

			{

				StringBuffer strBufMessage = new StringBuffer();



				strBufMessage.insert(0, strTaskCondition);   



				Utilities.substrReplace(strBufMessage, "&amp;", "&");

				Utilities.substrReplace(strBufMessage, "&lt;", "<");

				Utilities.substrReplace(strBufMessage, "&gt;", ">");

				Utilities.substrReplace(strBufMessage, "&apos;", "'");

				Utilities.substrReplace(strBufMessage, "&quot;", "\"");



				strTaskCondition = strBufMessage.toString(); 

			}

			*/

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskCondition - " + e.toString(), e);

        }



		return strTaskCondition;

	}



	/**

	 * get task transition name 

	 *

	 * @param strOriginTaskKey	the origin task key

	 * @param strTargetTaskKey	the target task key

	 * @param query		the query object

	 *

	 * @return	the task transition name

	 */

	protected static String getTaskTransitionName(String strOriginTaskKey, String strTargetTaskKey,

													   DALSecurityQuery query)

	{

		String strTaskTransitionName = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

            query.setField("WORKFLOW_TASK_TRANSITION_strName", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "=", strOriginTaskKey,

									0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskKey", "=", strTargetTaskKey,

									0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskTransitionName = rsResultSet.getString("WORKFLOW_TASK_TRANSITION_strName");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskTransitionName - " + e.toString(), e);

        }



		return strTaskTransitionName;

	}



	/**

	 * get the task key

	 *

	 * @param strTaskName				the task name

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query						the query object

	 *

	 * @return the task key

	 */

	public static String getTaskKey(String strTaskName, String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		String strTaskKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_intTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_strName", "=", strTaskName, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey, 0,

							DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskKey = rsResultSet.getString("WORKFLOW_TASK_intTaskKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskKey - " + e.toString(), e);

        }



		return strTaskKey;

	}

	

	/**

	 * get task status 

	 *

	 * @param strTaskKey	the task key

	 * @param query		the query object

	 *

	 * @return	the task status 

	 */

	public static String getTaskStatus(String strTaskKey, DALSecurityQuery query)

	{

		String strTaskStatus = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_strStatus", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskStatus = rsResultSet.getString("WORKFLOW_TASK_strStatus");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskStatus - " + e.toString(), e);

        }



		return strTaskStatus;

	}



	/**

	 * get task type

	 *

	 * @param strTaskKey	the task key

	 * @param query		the query object

	 *

	 * @return	the task type

	 */

	protected static String getTaskType(String strTaskKey, DALSecurityQuery query)

	{

		String strTaskType = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_strType", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskType = rsResultSet.getString("WORKFLOW_TASK_strType");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskType - " + e.toString(), e);

        }



		return strTaskType;

	}



	/**

	 * get task types

	 *

	 * @param strWorkflowInstanceKey 	the workflow instance key

	 * @param query						the query object

	 *

	 * @return	the task types in a hashtable

	 */

	protected static Hashtable getTaskTypes(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		Hashtable hashTaskTypes = new Hashtable();



		try

		{

			//DALQuery taskKeyQuery = new DALQuery();



			//taskKeyQuery.setDomain(WORKFLOW_TASK, null, null, null);

			//taskKeyQuery.setField("WORKFLOW_TASK_intTaskKey", null);

			//taskKeyQuery.setWhere(null, 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			//taskKeyQuery.setWhere("AND", 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey,

			//					 0, DALQuery.WHERE_HAS_VALUE);



			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_intTaskKey", null);

            query.setField("WORKFLOW_TASK_strType", null);

			//query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "IN", taskKeyQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);

			query.setWhere(null, 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey,

								 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				String strTaskKey = rsResultSet.getString("WORKFLOW_TASK_intTaskKey");

           		String strTaskType = rsResultSet.getString("WORKFLOW_TASK_strType");

				hashTaskTypes.put(strTaskKey, strTaskType);

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskTypes - " + e.toString(), e);

        }



		return hashTaskTypes;

	}



	/**

	 * get task action

	 *

	 * @param strTaskKey	the task key

	 * @param query		the query object

	 *

	 * @return	the task action 

	 */

	public static String getTaskAction(String strTaskKey, DALSecurityQuery query)

	{

		String strTaskAction = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_strAction", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskAction = rsResultSet.getString("WORKFLOW_TASK_strAction");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskAction - " + e.toString(), e);

        }



		return strTaskAction;

	}



	/**

	 * get required completed task to continue on to the target task

	 *

	 * @param strTaskKey	the task key

	 * @param query		the query object

	 *

	 * @return	the required completed task

	 */

	protected static int getCompletedTask(String strTaskKey, DALSecurityQuery query)

	{

		int intCompletedTask = 0;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_intCompletedTask", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		intCompletedTask = rsResultSet.getInt("WORKFLOW_TASK_intCompletedTask");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getCompletedTask - " + e.toString(), e);

        }



		return intCompletedTask;

	}



	/**

	 * update the initial activity key in a workflow template 

	 *

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 *

	 * @return	the initial activity key

	 */

	protected static String getInitialActivityKey(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		String strInitialActivityKey = null;



		try

		{

			query.reset();



			query.setDomain("WORKFLOW_TEMPLATE", null, null, null);

            query.setField("WORKFLOW_TEMPLATE_intInitialActivityKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strInitialActivityKey = rsResultSet.getString("WORKFLOW_TEMPLATE_intInitialActivityKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getInitialActivityKey - " + e.toString(), e);

        }



		return strInitialActivityKey;

	}



	/**

	 * update the task status

	 *

	 * @param strStatus	the status of the task

	 * @param strWorkflowTaskKey the workflow task key

	 * @param query the query object

	 */

	public static void updateTaskStatus(String strStatus,

										 String strWorkflowTaskKey,

										 DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_strStatus", strStatus, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateTaskStatus - " + e.toString(), e);

        }

	}



	/**

	 * update the task received date

	 *

	 * @param strDateReceived	the date received

	 * @param strWorkflowTaskKey the workflow task key

	 * @param query the query object

	 */

	public static void updateTaskReceivedDate(String strDateReceived,

											   String strWorkflowTaskKey,

											   DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_dtDateReceived", strDateReceived, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateInitialTaskReceivedDate - " + e.toString(), e);

        }

	}



	/**

	 * update the task completed date

	 *

	 * @param strDateCompleted	the date completed

	 * @param strWorkflowTaskKey the workflow task key

	 * @param query the query object

	 */

	public static void updateTaskCompletedDate(String strDateCompleted,

											   String strWorkflowTaskKey,

											   DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_dtDateCompleted", strDateCompleted, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateTaskCompletedDate - " + e.toString(), e);

        }

	}



	/**

	 * update the task received time 

	 *

	 * @param strTimeReceived	the time received

	 * @param strWorkflowTaskKey the workflow task key

	 * @param query the query object

	 */

	public static void updateTaskReceivedTime(String strTimeReceived,

											   String strWorkflowTaskKey,

											   DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_strTimeReceived", strTimeReceived, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateTaskReceivedTime - " + e.toString(), e);

        }

	}



	/**

	 * update the task completed time 

	 *

	 * @param strTimeCompleted	the time completed

	 * @param strWorkflowTaskKey the workflow task key

	 * @param query the query object

	 */

	public static void updateTaskCompletedTime(String strTimeCompleted,

											    String strWorkflowTaskKey,

											    DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_strTimeCompleted", strTimeCompleted, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateTaskCompletedTime - " + e.toString(), e);

        }

	}



	/**

	 * update the workflow instance completed date

	 *

	 * @param strDateCompleted	the date completed

	 * @param strWorkflowInstanceKey the workflow instance key

	 * @param query the query object

	 */

	protected static void updateWorkflowInstanceCompletedDate(String strDateCompleted,

											   				String strWorkflowInstanceKey,

											   				DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setField("WORKFLOW_INSTANCE_dtDateCompleted", strDateCompleted, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateWorkflowInstanceCompletedDate - " + e.toString(), e);

        }

	}



	/**

	 * update the workflow instance completed time 

	 *

	 * @param strTimeCompleted	the time completed

	 * @param strWorkflowInstanceKey the workflow instance key

	 * @param query the query object

	 */

	protected static void updateWorkflowInstanceCompletedTime(String strTimeCompleted,

											   				String strWorkflowInstanceKey,

											   				DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setField("WORKFLOW_INSTANCE_strTimeCompleted", strTimeCompleted, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateWorkflowInstanceCompletedTime - " + e.toString(), e);

        }

	}

	

	/**

	 * update the initial task id in a workflow instance

	 *

	 * @param strWorkflowTaskKey the workflow task key

	 * @param strWorkflowInstanceKey the workflow instance key 

	 * @param query the query object

	 */

	protected static void updateInitialTaskKey(String strWorkflowTaskKey,

			                                 String strWorkflowInstanceKey,

											 DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setField("WORKFLOW_INSTANCE_intInitialTaskKey", strWorkflowTaskKey, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateInitialTaskKey - " + e.toString(), e);

        }

	}



	/**

	 * transfer data between hashtable based on the mapping

	 *

	 * @param hashOrigin	the origin hashtable

	 * @param hashTarget 	the target hashtable

	 * @param hashMapping 	the mapping hashtable

	 */

	private static void transferData(Hashtable hashOrigin, Hashtable hashTarget, Hashtable hashMapping)

	{

		try

		{

			Enumeration enumMappingKeys = hashMapping.keys();

			while(enumMappingKeys.hasMoreElements())

			{

				String strOriginKey = (String) enumMappingKeys.nextElement();

				String strTargetKey = (String) hashMapping.get(strOriginKey);

				String strTargetValue = (String) hashOrigin.get(strOriginKey);

				if(strTargetValue == null)

				{

					hashTarget.put(strTargetKey, "");

				}

				else

				{

					hashTarget.put(strTargetKey, strTargetValue);

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::transferData - " + e.toString(), e);

        }

	}



	/**

	 * get month in two digit format eg. 01,11

	 *

	 * @param currentTime	the calendar object which indicate the time

	 *

	 * @return the month in string

	 */

	public static String getMonth(Calendar currentTime)

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

	public static String getDate(Calendar currentTime)

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

	 * check whether is there any non active templates, including sub workflow

	 *

	 * @param strWorkflowTemplateKey	the parent workflow template key

	 * @param query						the query object

	 *    

	 * @return true if there is a non active templates else return false

	 */

	public static boolean haveNonActiveTemplates(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		//store the list of subworkflow

		Vector vecWorkflowTemplateKey = new Vector();

		boolean blResultValue = false;



		try

		{

			//add the first key to the vector

			vecWorkflowTemplateKey.add(strWorkflowTemplateKey);



			for(int intIndex = 0; intIndex < vecWorkflowTemplateKey.size(); intIndex++)

			{

				String strTempWorkflowTemplateKey = (String) vecWorkflowTemplateKey.elementAt(intIndex);



				query.reset();



				query.setDomain("WORKFLOW_ACTIVITY", null, null, null);

				query.setField("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "null", "hashtable");

				query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

							   strTempWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



				ResultSet rsResultSet = query.executeSelect();



				String strResult = null;

				while(rsResultSet.next())

				{

					strResult = rsResultSet.getString("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey");

					if(strResult != null && (!strResult.equals("")))

					{

						vecWorkflowTemplateKey.add(strResult);

					//	if(WorkflowManager.getWorkflowTemplateStatus(strResult, query).equals(

					//							WorkflowManager.WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE))

					//	{

					//		blResultValue = true;

					//		return blResultValue;

					//	}

					}

				}

				

				if(WorkflowManager.getWorkflowTemplateStatus(strTempWorkflowTemplateKey, query).equals(

													WorkflowManager.WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE))

				{

					blResultValue = true;

					return blResultValue;

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::haveNonActiveTemplates - " + e.toString(), e);

        }



		return blResultValue;

	}



	/**

	 * check whether is there any active templates

	 *

	 * @param strWorkflowTemplateKey	the parent workflow template key

	 * @param query						the query object

	 *

	 * @return true if there is any active templates else return false

	 */

	public static boolean haveActiveTemplates(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		//store the list of subworkflow

		Vector vecWorkflowTemplateKey = new Vector();

		boolean blResultValue = false;



		try

		{

			//add the first key to the vector

			vecWorkflowTemplateKey.add(strWorkflowTemplateKey);



			for(int intIndex = 0; intIndex < vecWorkflowTemplateKey.size(); intIndex++)

			{

				String strTempWorkflowTemplateKey = (String) vecWorkflowTemplateKey.elementAt(intIndex);



				query.reset();



				query.setDomain("WORKFLOW_ACTIVITY", null, null, null);

				query.setField("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "null", "hashtable");

				query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

							   strTempWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



				ResultSet rsResultSet = query.executeSelect();



				String strResult = null;

				while(rsResultSet.next())

				{

					strResult = rsResultSet.getString("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey");

					if(strResult != null && (!strResult.equals("")))

					{

						vecWorkflowTemplateKey.add(strResult);

						//if(WorkflowManager.getWorkflowTemplateStatus(strResult, query).equals(

						//									WorkflowManager.WORKFLOW_TEMPLATE_STATUS_ACTIVE))

						//{

						//	blResultValue = true;

						//	return blResultValue;

						//}

					}

				}

				

				if(WorkflowManager.getWorkflowTemplateStatus(strTempWorkflowTemplateKey, query).equals(

													WorkflowManager.WORKFLOW_TEMPLATE_STATUS_ACTIVE))

				{

					blResultValue = true;

					return blResultValue;

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::haveActiveTemplates - " + e.toString(), e);

        }



		return blResultValue;

	}



	/**

	 * activate a workflow templates

	 *

	 * @param strWorkflowTemplateKey	the parent workflow template key

	 * @param query	the query object

	 *

	 * @return a list of workflow templates which were activated thourgh this method

	 */

	public static Vector activateWorkflowTemplates(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		//store the list of subworkflow

		Vector vecWorkflowTemplateKey = new Vector();



		try

		{

			//add the first key to the vector

			vecWorkflowTemplateKey.add(strWorkflowTemplateKey);



			for(int intIndex = 0; intIndex < vecWorkflowTemplateKey.size(); intIndex++)

			{

				String strTempWorkflowTemplateKey = (String) vecWorkflowTemplateKey.elementAt(intIndex);



				query.reset();



				query.setDomain("WORKFLOW_ACTIVITY", null, null, null);

				query.setField("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "null", "hashtable");

				query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

							   strTempWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

				//query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "<>", "", 0, DALQuery.WHERE_HAS_VALUE);

				//query.setWhere("OR", 0, "WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "=", "IS NULL", 1, DALQuery.WHERE_HAS_VALUE);



				ResultSet rsResultSet = query.executeSelect();



				while(rsResultSet.next())

				{

					String strResult = rsResultSet.getString("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey");

					if(strResult != null && (!strResult.equals("")))

					{

						vecWorkflowTemplateKey.add(strResult);

					}

				}



				WorkflowManager.activateWorkflowTemplate(strTempWorkflowTemplateKey, query);

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::activateWorkflowTemplates - " + e.toString(), e);

        }



		return vecWorkflowTemplateKey;

	}



	/**

	 * activate a workflow template

	 *

	 * @param strWorkflowTemplateKey	the workflow template key

	 * @param query	the query object

	 */

	public static void activateWorkflowTemplate(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_PROCESS, null, null, null);    

			query.setField("WORKFLOW_TEMPLATE_strStatus", WORKFLOW_TEMPLATE_STATUS_ACTIVE, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::activateWorkflowTemplate - " + e.toString(), e);

        }

	}



	/**

	 * deactivate a workflow template

	 *

	 * @param strWorkflowTemplateKey	the workflow template key

	 * @param query	the query object

	 */

	public static void deactivateWorkflowTemplate(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_PROCESS, null, null, null);    

			query.setField("WORKFLOW_TEMPLATE_strStatus", WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deactivateWorkflowTemplate - " + e.toString(), e);

        }

	}





	/**

	 * check whether if the given workflow template key is a sub workflow in any other workflow templates

	 *

	 * @param strWorkflowTemplateKey	the parent workflow template key

	 * @param query						the query object

	 *

	 * @return true if the given workflow template key is a sub workflow in any other workflow templates, else return false

	 */

	protected static boolean isSubWorkflowTemplate(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			query.reset();



			query.setDomain("WORKFLOW_ACTIVITY", null, null, null);

			query.setField("WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isSubWorkflowTemplate - " + e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * check whether is there any active instantiations of the parent workflow templates

	 *

	 * @param strWorkflowTemplateKey	the parent workflow template key

	 * @param query						the query object

	 *

	 * @return true if any of the parent workflow is either in progress or suspended, else return false

	 */

	public static boolean doParentsHaveActiveInstantiations(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		//store the list of parent workflow

		Vector vecWorkflowTemplateKey = new Vector();

		boolean blResultValue = false;



		try

		{

			//add the first key to the vector

			vecWorkflowTemplateKey.add(strWorkflowTemplateKey);



			for(int intIndex = 0; intIndex < vecWorkflowTemplateKey.size(); intIndex++)

			{

				String strTempWorkflowTemplateKey = (String) vecWorkflowTemplateKey.elementAt(intIndex);



				query.reset();



				query.setDomain("WORKFLOW_ACTIVITY", null, null, null);

				query.setField("WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "null", "hashtable");

				//query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

				//			   strTempWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "=",

							   strTempWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



				ResultSet rsResultSet = query.executeSelect();



				String strResult = null;

				while(rsResultSet.next())

				{

					strResult = rsResultSet.getString("WORKFLOW_ACTIVITY_intWorkflowTemplateKey");

					if(strResult != null && (!strResult.equals("")))

					{

						vecWorkflowTemplateKey.add(strResult);

					}

				}

				

				if(WorkflowManager.haveActiveInstantiation(strTempWorkflowTemplateKey, query))

				{

					blResultValue = true;

					return blResultValue;

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::doParentsHaveActiveInstantiations - " + e.toString(), e);

        }



		return blResultValue;

	}

	

	/**

	 * check whether is there any active instantiations of the main and sub workflow templates

	 *

	 * @param strWorkflowTemplateKey	the parent workflow template key

	 * @param query						the query object

	 *

	 * @return true if any of the sub workflow is either in progress or suspended, else return false

	 */

	public static boolean haveActiveInstantiations(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		//store the list of subworkflow

		Vector vecWorkflowTemplateKey = new Vector();

		boolean blResultValue = false;



		try

		{

			//add the first key to the vector

			vecWorkflowTemplateKey.add(strWorkflowTemplateKey);



			for(int intIndex = 0; intIndex < vecWorkflowTemplateKey.size(); intIndex++)

			{

				String strTempWorkflowTemplateKey = (String) vecWorkflowTemplateKey.elementAt(intIndex);



				query.reset();



				query.setDomain("WORKFLOW_ACTIVITY", null, null, null);

				query.setField("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", "null", "hashtable");

				query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

							   strTempWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



				ResultSet rsResultSet = query.executeSelect();



				String strResult = null;

				while(rsResultSet.next())

				{

					strResult = rsResultSet.getString("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey");

					if(strResult != null && (!strResult.equals("")))

					{

						vecWorkflowTemplateKey.add(strResult);

					}

				}

				

				if(WorkflowManager.haveActiveInstantiation(strTempWorkflowTemplateKey, query))

				{

					blResultValue = true;

					return blResultValue;

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::haveActiveInstantiations - " + e.toString(), e);

        }



		return blResultValue;

	}



	/**

	 * check whether the workflow template has any active instantiation

	 *

	 * @param strWorkflowTemplateKey	the workflow template key

	 * @param query	the query object

	 *

	 * @return	the indicator the indicate whether the template have any active instantiation or not 

	 */

	public static boolean haveActiveInstantiation(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setField("WORKFLOW_INSTANCE_strName", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 1, "WORKFLOW_INSTANCE_strStatus", "=", WORKFLOW_INSTANCE_STATUS_IN_PROGRESS, 0,

						   DALQuery.WHERE_HAS_VALUE);

			query.setWhere("OR", 0, "WORKFLOW_INSTANCE_strStatus", "=", WORKFLOW_INSTANCE_STATUS_SUSPENDED, 1,

						   DALQuery.WHERE_HAS_VALUE);

			//query.setWhere("OR", 0, "WORKFLOW_INSTANCE_strStatus", "=", WORKFLOW_INSTANCE_STATUS_NOT_STARTED, 1,

			//			   DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}

			

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::haveActiveInstantiation - " + e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * delete the workflow activity transition from the Database

	 *

	 * @param strDomain the domain name

	 * @param strTransitionXPDLId the transition XPDL Id

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteTransition(String strDomain, String strTransitionXPDLId,

									   String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_TRANSITION_strActivityTransitionXPDLId", "=",

						   strTransitionXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteTransition - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow activity from the Database

	 *

	 * @param strDomain the domain name

	 * @param strActivityXPDLId the activity XPDL Id

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteActivity(String strDomain, String strActivityXPDLId,

									   String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId", "=",

						   strActivityXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteActivity - " + e.toString(), e);

        }

	}



	/**

	 * delete the workflow trigger from the Database

	 *

	 * @param strDomain the domain name

	 * @param strTriggerXPDLId	the trigger XPDL Id

	 * @param strWorkflowPackageKey the workflow package key

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 */

	protected static void deleteTrigger(String strDomain, String strTriggerXPDLId,

									  String strWorkflowPackageKey, 

									  String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_intDeleted", "-1", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TRIGGER_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_strTriggerXPDLId", "=",

						   strTriggerXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::deleteTrigger - " + e.toString(), e);

        }

	}



	/**

	 * update the initial activity id in a workflow process

	 *

	 * @param strWorkflowActivityKey the workflow activity key

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL id

	 * @param query the query object

	 */

	protected static void updateInitialActivityId(String strWorkflowActivityKey,

			                                    String strWorkflowTemplateKey,

												DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_PROCESS, null, null, null);    

			query.setField("WORKFLOW_TEMPLATE_intInitialActivityKey", strWorkflowActivityKey, "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateInitialActivityId - " + e.toString(), e);

        }

	}



	/**

	 * get the workflow package key for a given workflow package XPDL id

	 *

	 * @param strWorkflowPackageXPDLId the workflow package XPDL id

	 * @param query the query object

	 *

	 * @return the workflow package key

	 */

	protected static String getWorkflowPackageKey(String strWorkflowPackageXPDLId, DALSecurityQuery query)

	{

		String strWorkflowPackageKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_PACKAGE, null, null, null);

			query.setField("WORKFLOW_PACKAGE_intWorkflowPackageKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_strWorkflowPackageXPDLId", "=",

					       strWorkflowPackageXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_PACKAGE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

                  

            if(rsResultSet.next())

			{

            	strWorkflowPackageKey = rsResultSet.getString("WORKFLOW_PACKAGE_intWorkflowPackageKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowPackageKey - " + e.toString(), e);

        }



		return strWorkflowPackageKey;

	}



	/**

	 * get the workflow template key for a given workflow template XPDL id and workflow package key

	 *

	 * @param strWorkflowTemplateXPDLId the workflow template XPDL id

	 * @param strWorkflowPackageKey the workflow package key

	 * @param query the query object

	 *

	 * @return	the workflow template key

	 */

	protected static String getWorkflowTemplateKey(String strWorkflowTemplateXPDLId, String strWorkflowPackageKey,

												 DALSecurityQuery query)

	{

		String strWorkflowTemplateKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_PROCESS, null, null, null);

			query.setField("WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId", "=",

					       strWorkflowTemplateXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intWorkflowPackageKey", "=",

					       strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

				  

			if(rsResultSet.next())

			{

				strWorkflowTemplateKey = rsResultSet.getString("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTemplateKey - " + e.toString(), e);

        }



		return strWorkflowTemplateKey;

	}



	/**

	 * get the workflow activity key for a given workflow template key and workflow activity XPDL id

	 *

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param strWorkflowActivityXPDLId the workflow activity XPDL Id 

	 * @param query the query object

	 *

	 * @return	the workflow activity key

	 */

	protected static String getWorkflowActivityKey(String strWorkflowTemplateKey,

			                                     String strWorkflowActivityXPDLId,

												 DALSecurityQuery query)

	{

		String strWorkflowActivityKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_intActivityKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

					       strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId", "=",

					       strWorkflowActivityXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

		

			if(rsResultSet.next())

			{

				strWorkflowActivityKey = rsResultSet.getString("WORKFLOW_ACTIVITY_intActivityKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowActivityKey - " + e.toString(), e);

        }



		return strWorkflowActivityKey;

	}





    /**

	 * get the workflow template keys for a given workflow package key

	 *

	 * @param strWorkflowPackageKey the workflow package key

	 * @param query the query object

	 *

	 * @return the workflow template keys

	 */

	protected static Vector getWorkflowTemplateKeys(String strWorkflowPackageKey, DALSecurityQuery query)

	{

		Vector vecWorkflowTemplateKey = new Vector();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_PROCESS, null, null, null);

			query.setField("WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowPackageKey", "=",

					       strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

				 

			if(rsResultSet.first())

			{

				vecWorkflowTemplateKey.add(rsResultSet.getString("WORKFLOW_TEMPLATE_intWorkflowTemplateKey"));

				rsResultSet.next();

				while(!rsResultSet.isAfterLast())

				{

					vecWorkflowTemplateKey.add(rsResultSet.getString("WORKFLOW_TEMPLATE_intWorkflowTemplateKey"));

					rsResultSet.next();

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTemplateKeys - " + e.toString(), e);

        }



		return vecWorkflowTemplateKey;

	}



	/**

	 * get the workflow activity keys for a given workflow template key

	 *

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 *

	 * @return the workflow activity keys

	 */

	public static Vector getWorkflowActivityKeys(String strWorkflowTemplateKey,

												  DALSecurityQuery query)

	{

		Vector vecWorkflowActivityKey = new Vector();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_intActivityKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

					       strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

	

			if(rsResultSet.first())

			{

				vecWorkflowActivityKey.add(rsResultSet.getString("WORKFLOW_ACTIVITY_intActivityKey"));

				rsResultSet.next();

				while(!rsResultSet.isAfterLast())

				{

					vecWorkflowActivityKey.add(rsResultSet.getString("WORKFLOW_ACTIVITY_intActivityKey"));

					rsResultSet.next();

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowActivityKeys - " + e.toString(), e);

        }



		return vecWorkflowActivityKey;

	}



	/**

	 * get the workflow activity parameters for a given workflow activity key

	 *

	 * @param strWorkflowActivityKey the workflow activity key

	 * @param query the query object

	 *

	 * @return	the workflow activity parameter

	 */

	private static Vector getWorkflowActivityParameters(String strWorkflowActivityKey,

														DALSecurityQuery query)

	{

		Vector vecWorkflowActivityParameters = new Vector();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY_PARAMETER, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_PARAMETER_strName", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey", "=",

					       strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

	

			if(rsResultSet.first())

			{

				vecWorkflowActivityParameters.add(rsResultSet.getString(

							"WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey"));

				rsResultSet.next();

				while(!rsResultSet.isAfterLast())

				{

					vecWorkflowActivityParameters.add(rsResultSet.getString(

								"WORKFLOW_ACTIVITY_PARAMETER_intWorkflowActivityKey"));

					rsResultSet.next();

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowActivityParameters - " + e.toString(), e);

        }



		return vecWorkflowActivityParameters;

	}



	/**

	 * get the workflow activity performer type for a given workflow activity key

	 *

	 * @param strWorkflowActivityKey the workflow activity key

	 * @param query the query object

	 *

	 * @return	the workflow activity performer type 

	 */

	public static String getWorkflowActivityPerformerType(String strWorkflowActivityKey,

														   DALSecurityQuery query)

	{

		String strPerformerType = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_strPerformerType", null);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intActivityKey", "=",

					       strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

	

			if(rsResultSet.next())

			{

				strPerformerType = rsResultSet.getString("WORKFLOW_ACTIVITY_strPerformerType");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowActivityPerformerType - " + e.toString(), e);

        }



		return strPerformerType;

	}



	/**

	 * get the workflow activity performer for a given workflow activity key

	 *

	 * @param strWorkflowActivityKey the workflow activity key

	 * @param query the query object

	 *

	 * @return	the workflow activity performer type 

	 */

	public static String getWorkflowActivityPerformer(String strWorkflowActivityKey,

													  DALSecurityQuery query)

	{

		String strPerformer = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_intPerformer", null);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intActivityKey", "=",

					       strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

	

			if(rsResultSet.next())

			{

				strPerformer = rsResultSet.getString("WORKFLOW_ACTIVITY_intPerformer");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowActivityPerformer - " + e.toString(), e);

        }



		return strPerformer;

	}



	/**

	 * get the workflow activity XPDL ID for a given workflow activity key

	 *

	 * @param strWorkflowActivityKey    the workflow activity key

	 * @param query 					the query object

	 *

	 * @return	the workflow activity XPDL ID

	 */

	public static String getWorkflowActivityXPDLId(String strWorkflowActivityKey,

												   DALSecurityQuery query)

	{

		String strWorkflowActivityXPDLId = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId", null);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intActivityKey", "=",

					       strWorkflowActivityKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

	

			if(rsResultSet.next())

			{

				strWorkflowActivityXPDLId = rsResultSet.getString("WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowActivityXPDLId - " + e.toString(), e);

        }



		return strWorkflowActivityXPDLId;

	}



	/**

	 * get the workflow activity XPDL ID for a given workflow task key and workflow instance key

	 *

	 * @param strWorkflowTaskKey    		the workflow task key

	 * @param strWorkflowInstanceKey 		the workflow instance key

	 * @param query 						the query object

	 *

	 * @return	the workflow activity XPDL ID

	 */

	public static String getWorkflowActivityXPDLId(String strWorkflowTaskKey, String strWorkflowInstanceKey,

												   DALSecurityQuery query)

	{

		String strWorkflowActivityXPDLId = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

			query.setField("WORKFLOW_TASK_strWorkflowActivityXPDLId", null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

					       strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=",

					       strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

	

			if(rsResultSet.next())

			{

				strWorkflowActivityXPDLId = rsResultSet.getString("WORKFLOW_TASK_strWorkflowActivityXPDLId");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowActivityXPDLId(String strWorkflowTaskKey, String strWorkflowInstanceKey, DALSecurityQuery query)" + e.toString(), e);

        }



		return strWorkflowActivityXPDLId;

	}



	/**

	 * get the workflow template parameter names for a given workflow template key

	 *

	 * @param strDomain	the domain

	 * @param strWorkflowTemplateKey the workflow activity key

	 * @param query the query object

	 *

	 * @return	a list of workflow template parameters name 

	 */

	protected static Vector getWorkflowTemplateParameterNames(String strDomain, String strWorkflowTemplateKey,

											DALSecurityQuery query)

	{

		Vector vecParameterNames = new Vector();



		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_strName", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.first())

			{

				vecParameterNames.add(rsResultSet.getString(

							"WORKFLOW_TEMPLATE_PARAMETER_strName"));

				rsResultSet.next();

				while(!rsResultSet.isAfterLast())

				{

					vecParameterNames.add(rsResultSet.getString(

								"WORKFLOW_TEMPLATE_PARAMETER_strName"));

					rsResultSet.next();

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTemplateParameterNames - " + e.toString(), e);

        }



		return vecParameterNames;

	}



	/**

	 * get the trigger XPDL IDs for a given workflow template and package key

	 *

	 * @param strDomain	the domain

	 * @param strWorkflowTemplateKey the workflow activity key

	 * @param strWorkflowPackageKey the workflow package key

	 * @param query the query object

	 *

	 * @return	a list of trigger XPDL Ids

	 */

	protected static Vector getTriggerXPDLIds(String strDomain, String strWorkflowPackageKey,

											String strWorkflowTemplateKey,

											DALSecurityQuery query)

	{

		Vector vecTriggerXPDLIds = new Vector();



		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_strTriggerXPDLId", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TRIGGER_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.first())

			{

				vecTriggerXPDLIds.add(rsResultSet.getString(

							"WORKFLOW_TRIGGER_strTriggerXPDLId"));

				rsResultSet.next();

				while(!rsResultSet.isAfterLast())

				{

					vecTriggerXPDLIds.add(rsResultSet.getString(

								"WORKFLOW_TRIGGER_strTriggerXPDLId"));

					rsResultSet.next();

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTriggerXPDLIds - " + e.toString(), e);

        }



		return vecTriggerXPDLIds;

	}



	/**

	 * get the activity XPDL IDs for a given workflow template

	 *

	 * @param strDomain	the domain

	 * @param strWorkflowTemplateKey the workflow activity key

	 * @param query the query object

	 *

	 * @return a list of activity XPDL Id

	 */

	protected static Vector getActivityXPDLIds(String strDomain, String strWorkflowTemplateKey,

											 DALSecurityQuery query)

	{

		Vector vecActivityXPDLIds = new Vector();



		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_strWorkflowActivityXPDLId", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.first())

			{

				vecActivityXPDLIds.add(rsResultSet.getString(

							"WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId"));

				rsResultSet.next();

				while(!rsResultSet.isAfterLast())

				{

					vecActivityXPDLIds.add(rsResultSet.getString(

								"WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId"));

					rsResultSet.next();

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getActivityXPDLIds - " + e.toString(), e);

        }



		return vecActivityXPDLIds;

	}



	/**

	 * get the transition XPDL IDs for a given workflow template

	 *

	 * @param strDomain	the domain

	 * @param strWorkflowTemplateKey the workflow activity key

	 * @param query the query object

	 *

	 * @return a list of transition XPDL Ids

	 */

	protected static Vector getTransitionXPDLIds(String strDomain, String strWorkflowTemplateKey,

											 DALSecurityQuery query)

	{

		Vector vecTransitionXPDLIds = new Vector();



		try

		{

			query.reset();

			

			query.setDomain(strDomain, null, null, null);    

			query.setField(strDomain + "_strActivityTransitionXPDLId", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.first())

			{

				vecTransitionXPDLIds.add(rsResultSet.getString(

							"WORKFLOW_ACTIVITY_TRANSITION_strActivityTransitionXPDLId"));

				rsResultSet.next();

				while(!rsResultSet.isAfterLast())

				{

					vecTransitionXPDLIds.add(rsResultSet.getString(

								"WORKFLOW_ACTIVITY_TRANSITION_strActivityTransitionXPDLId"));

					rsResultSet.next();

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTransitionXPDLIds - " + e.toString(), e);

        }



		return vecTransitionXPDLIds;

	}



	/**

	 * convert the Package object to a Hashtable object

	 *

	 * @param workflowPackage the workflow package

	 * @param hashData the hashtable that will contains the workflow package data

	 */

	private static void convertToHashtable(Package workflowPackage, Hashtable hashData)

	{

		if(workflowPackage != null)

		{

			//System.err.println("The workflow package is NOT null!!!!");



			//System.err.println("Package - id = " + workflowPackage.getId());

			//System.err.println("Package - name = " + workflowPackage.getName());

			//System.err.println("");



			if(workflowPackage.getId() != null)

			{

				hashData.put(WORKFLOW_PACKAGE + "_strWorkflowPackageXPDLId" , workflowPackage.getId());

			}

			else

			{

				hashData.put(WORKFLOW_PACKAGE + "_strWorkflowPackageXPDLId" , "");

			}



			if(workflowPackage.getName() != null)

			{

				hashData.put(WORKFLOW_PACKAGE + "_strName" , workflowPackage.getName());

			}

			else

			{

				hashData.put(WORKFLOW_PACKAGE + "_strName" , "");

			}

		}

		else

		{

			System.err.println("The workflow package is null!!!!");

		}

	}



	/**

	 * convert the PackageHeader object to a Hashtable object

	 *

	 * @param packageHeader the package header

	 * @param hashData the hashtable that will contains the package header data

	 */

	private static void convertToHashtable(PackageHeader packageHeader, Hashtable hashData)

	{

		if(packageHeader != null)

		{

			//System.err.println("The workflow package header is NOT null!!!!");



			//System.err.println("PackageHeader - XPDL Version = " + packageHeader.getXPDLVersion());

			//System.err.println("PackageHeader - Vendor = " + packageHeader.getVendor());

			//System.err.println("PackageHeader - Created = " + packageHeader.getCreated());

			//System.err.println("PackageHeader - Description = " + packageHeader.getDescription());

			//System.err.println("");



			if(packageHeader.getXPDLVersion() != null)

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_strVersion" , packageHeader.getXPDLVersion());

			}

			else

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_strVersion" , "");

			}



			if(packageHeader.getVendor() != null)

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_strVendor" , packageHeader.getVendor());

			}

			else

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_strVendor" , "");

			}



			if(packageHeader.getCreated() != null)

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_dtDateCreated" , packageHeader.getCreated());

			}

			else

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_dtDateCreated" , "");

			}



			if(packageHeader.getDescription() != null)

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_strDescription" , packageHeader.getDescription());

			}

			else

			{

				hashData.put(WORKFLOW_PACKAGE_HEADER + "_strDescription" , "");

			}

		}

		else

		{

			System.err.println("The workflow package header is null!!!!");

		}

	}



	/**

	 * convert the WorkflowProcess object to a Hashtable object

	 *

	 * @param workflowProcess the workflow process

	 * @param hashData the hashtable that will contains the workflow process data

	 */

	private static void convertToHashtable(WorkflowProcess workflowProcess, Hashtable hashData)

	{

		if(workflowProcess != null)

		{

			//System.err.println("The workflow process is NOT null!!!!");



			//System.err.println("WorkflowProcess - Id = " + workflowProcess.getId());

			//System.err.println("WorkflowProcess - Name = " + workflowProcess.getName());

			//System.err.println("WorkflowProcess - InitialActivityId = " + workflowProcess.getInitialActivityId());

			//System.err.println("");



			if(workflowProcess.getId() != null)

			{

				hashData.put(WORKFLOW_PROCESS  + "_strWorkflowTemplateXPDLId", workflowProcess.getId());

			}

			else

			{

				hashData.put(WORKFLOW_PROCESS  + "_strWorkflowTemplateXPDLId", "");

			}



			if(workflowProcess.getName() != null)

			{

				hashData.put(WORKFLOW_PROCESS + "_strName", workflowProcess.getName());

			}

			else

			{

				hashData.put(WORKFLOW_PROCESS + "_strName", "");

			}

		}

		else

		{

			System.err.println("The workflow process is null!!!!");

		}

	}



	/**

	 * convert the ProcessHeader object to a Hashtable object

	 *

	 * @param processHeader the process header

	 * @param hashData the hashtable that will contains the process header data

	 */

	private static void convertToHashtable(ProcessHeader processHeader, Hashtable hashData)

	{

		if(processHeader != null)

		{

			//System.err.println("The process header is NOT null!!!!");



			//System.err.println("ProcessHeader - Created = " + processHeader.getCreated());

			//System.err.println("ProcessHeader - Description = " + processHeader.getDescription());

			//System.err.println("");



			if(processHeader.getCreated() != null)

			{

				hashData.put(WORKFLOW_PROCESS_HEADER  + "_dtDateCreated", processHeader.getCreated());

			}

			else

			{

				hashData.put(WORKFLOW_PROCESS_HEADER  + "_dtDateCreated", "");

			}



			if(processHeader.getDescription() != null)

			{

				hashData.put(WORKFLOW_PROCESS_HEADER + "_strDescription", processHeader.getDescription());

			}

			else

			{

				hashData.put(WORKFLOW_PROCESS_HEADER + "_strDescription", "");

			}

			

		}

		else

		{

			System.err.println("The process header is null!!!!");

		}

	}



	/**

	 * convert the Parameter object to a Hashtable object

	 *

	 * @param parameter the parameter 

	 * @param hashData the hashtable that will contains the parameter data

	 */

	private static void convertToHashtable(Parameter parameter, Hashtable hashData)

	{

		if(parameter != null)

		{

			if(parameter.getName() != null)

			{

				hashData.put(WORKFLOW_TEMPLATE_PARAMETER + "_strName", parameter.getName());

			}

			else

			{

				hashData.put(WORKFLOW_TEMPLATE_PARAMETER + "_strName", "");

			}



			if(parameter.getType() != null)

			{

				hashData.put(WORKFLOW_TEMPLATE_PARAMETER + "_strType", parameter.getType());

			}

			else

			{

				hashData.put(WORKFLOW_TEMPLATE_PARAMETER + "_strType", "");

			}



			if(parameter.getDescription() != null)

			{

				hashData.put(WORKFLOW_TEMPLATE_PARAMETER + "_strDescription", parameter.getDescription());

			}

			else

			{

				hashData.put(WORKFLOW_TEMPLATE_PARAMETER + "_strDescription", "");

			}

		}

		else

		{

			System.err.println("The parameter is null!!!!");

		}

	}



	/**

	 * convert the Trigger object to a Hashtable object

	 *

	 * @param trigger the trigger

	 * @param hashData the hashtable that will contains the trigger data

	 */

	private static void convertToHashtable(Trigger trigger, Hashtable hashData)

	{

		if(trigger != null)

		{

			//System.err.println("The trigger is NOT null!!!!");



			//System.err.println("Trigger - Id = " + trigger.getId());

			//System.err.println("Trigger - Name = " + trigger.getName());

			//System.err.println("Trigger - Action = " + trigger.getAction());

			//System.err.println("Trigger - Domain = " + trigger.getDomain());

			//System.err.println("Trigger - Field = " + trigger.getField());

			//System.err.println("Trigger - Type = " + trigger.getType());

			//System.err.println("Trigger - Connection1 = " + trigger.getConnection1());

			//System.err.println("Trigger - Operator1 = " + trigger.getOperator1());

			//System.err.println("Trigger - Value1 = " + trigger.getValue1());

			//System.err.println("Trigger - Connection2 = " + trigger.getConnection2());

			//System.err.println("Trigger - Operator2 = " + trigger.getOperator2());

			//System.err.println("Trigger - Value2 = " + trigger.getValue2());

			//System.err.println("");		



			if(trigger.getId() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strTriggerXPDLId", trigger.getId());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strTriggerXPDLId", "");

			}



			if(trigger.getName() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strName", trigger.getName());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strName", "");

			}



			if(trigger.getAction() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strAction", trigger.getAction());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strAction", "");

			}



			if(trigger.getDomain() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strDomain", trigger.getDomain());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strDomain", "");

			}



			if(trigger.getField() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strField", trigger.getField());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strField", "");

			}



			if(trigger.getType() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strType", trigger.getType());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strType", "");

			}



			if(trigger.getConnection1() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strConnection1", trigger.getConnection1());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strConnection1", "");

			}



			if(trigger.getOperator1() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strOperator1", trigger.getOperator1());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strOperator1", "");

			}



			if(trigger.getValue1() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strValue1", trigger.getValue1());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strValue1", "");

			}



			if(trigger.getConnection2() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strConnection2", trigger.getConnection2());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strConnection2", "");

			}



			if(trigger.getOperator2() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strOperator2", trigger.getOperator2());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strOperator2", "");

			}



			if(trigger.getValue2() != null)

			{

				hashData.put(WORKFLOW_TRIGGER + "_strValue2", trigger.getValue2());

			}

			else

			{

				hashData.put(WORKFLOW_TRIGGER + "_strValue2", "");

			}

			

		}

		else

		{

			System.err.println("The trigger is null!!!!");

		}

	}

        /**
	 * convert the Function object to a Hashtable object
	 *
	 * @param function the Function object
	 * @param hashData the hashtable that will contains the trigger data
	 */
        private static void convertToHashtable(Function function, Hashtable hashData)
        {
            if (function != null)
            {
                hashData.put(WORKFLOW_FUNCTION + "_intWorkflowFunctionKey", 
                    function.getKey() == null ? "" : function.getKey());
                    
                hashData.put(WORKFLOW_FUNCTION + "_strName", 
                    function.getName() == null ? "" : function.getName());
                    
                hashData.put(WORKFLOW_FUNCTION + "_strClassName", 
                    function.getClassName() == null ? "" : function.getClassName());
                    
                hashData.put(WORKFLOW_FUNCTION + "_strFunctionName", 
                    function.getFunctionName() == null ? "" : function.getFunctionName());
                    
                hashData.put(WORKFLOW_FUNCTION + "_strFunctionParameterTypes", 
                    function.getParameterTypes() == null ? "" : function.getParameterTypes());
                    
                hashData.put(WORKFLOW_FUNCTION + "_strFunctionParameterNames", 
                    function.getParameterNames() == null ? "" : function.getParameterNames());
                    
                hashData.put(WORKFLOW_FUNCTION + "_strType", 
                    function.getType() == null ? "" : function.getType());                                    
            }        
        }
       
        

	/**

	 * convert the Activity object to a Hashtable object

	 *

	 * @param activity the activity

	 * @param hashData the hashtable that will contains the activity data

	 */

	private static void convertToHashtable(Activity activity, Hashtable hashData)

	{

		if(activity != null)

		{

			//System.err.println("The activity is NOT null!!!!");



			//System.err.println("Activity - Id  = " + activity.getId());

			//System.err.println("Activity - Name = " + activity.getName());

			//System.err.println("Activity - Description = " + activity.getDescription());

			//System.err.println("Activity - Performer = " + activity.getPerformer());

			//System.err.println("Activity - Performer Type = " + activity.getPerformerType());

			//System.err.println("Activity - Priority = " + activity.getPriority());

			//System.err.println("");



			// Extended Attributes

			Hashtable hashExtendedAttributes = activity.getExtendedAttributes();



			//System.err.println("No. of Extended Attributes = " + hashExtendedAttributes.size());



			//System.err.println("Activity - Extended Attributes = " + hashExtendedAttributes);

			//System.err.println("");

			//System.err.println("");



			if(activity.getId() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strWorkflowActivityXPDLId", activity.getId());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strWorkflowActivityXPDLId", "");

			}



			if(activity.getName() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strName", activity.getName());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strName", "");

			}



			if(activity.getPriority() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strPriority", activity.getPriority());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strPriority", "");

			}



			if(activity.getPerformer() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY + "_intPerformer", activity.getPerformer());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY + "_intPerformer", "");

			}



			if(activity.getPerformerType() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strPerformerType", activity.getPerformerType());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY + "_strPerformerType", "");

			}



			Enumeration enumKeys = hashExtendedAttributes.keys();



			while(enumKeys.hasMoreElements())

			{

				Object objKey = enumKeys.nextElement();

				Object objValue = hashExtendedAttributes.get(objKey);



				hashData.put(WORKFLOW_ACTIVITY + "_" + objKey.toString(), objValue.toString());

			}

		}

		else

		{

			System.err.println("The activity is null!!!!");

		}

	}



	/**

	 * convert the Transition object to a Hashtable object

	 *

	 * @param transition the transition

	 * @param hashData the hashtable that will contains the transition data

	 */

	private static void convertToHashtable(Transition transition, Hashtable hashData)

	{

		if(transition != null)

		{

			//System.err.println("The transition is NOT null!!!!");



			//System.err.println("Transition - Id = " + transition.getId()); 

			//System.err.println("Transition - From = " + transition.getFrom()); 

			//System.err.println("Transition - To = " + transition.getTo()); 

			//System.err.println("Transition - Name = " + transition.getName()); 

			//System.err.println("Transition - Condition = " + transition.getCondition()); 
                    
                        //System.err.println("Transition - XCoord = " + transition.getXCoordinate());
                        
                        //System.err.println("Transition - YCoord = " + transition.getYCoordinate());
                        
                        //System.err.println("Transition - dpCoord = " + transition.getDecisionPointLocation());

			//System.err.println("");



			if(transition.getId() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strActivityTransitionXPDLId", transition.getId());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strActivityTransitionXPDLId", "");

			}

			

			if(transition.getFrom() != null)

			{
                                
				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_intOriginActivityKey", transition.getFrom());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_intOriginActivityKey", "");

			}



			if(transition.getTo() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_intTargetActivityKey", transition.getTo());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_intTargetActivityKey", "");

			}



			if(transition.getCondition() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strCondition", transition.getCondition());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strCondition", "");

			}



			if(transition.getType() != null)

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strType", transition.getType());

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strType", "");

			}



			if(transition.getXCoordinate() != null)

			{

                                // if transition is a decision point, we want to
                                // add the X coordinates of the decision point
                                //if (transition.isDecisionPoint())
                                //{
        			//	hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strXCoordinate", transition.getXCoordinate() + " " + Transition.DECISION_POINT_PREFIX + transition.getDecisionPointLocation().x);
                                //}
                                //else
                                //{
        				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strXCoordinate", transition.getXCoordinate());                                    
                                //}

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strXCoordinate", "");

			}

			if(transition.getYCoordinate() != null)

			{
                                // if transition is a decision point, we want to
                                // add the Y coordinates of the decision point                            
                                //if (transition.isDecisionPoint())
                                //{                                        
                                //       hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strYCoordinate", transition.getYCoordinate() + " " + Transition.DECISION_POINT_PREFIX + transition.getDecisionPointLocation().y);
                                //}
                                //else
                                //{
                                        hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strYCoordinate", transition.getYCoordinate());
                                //}

			}

			else

			{

				hashData.put(WORKFLOW_ACTIVITY_TRANSITION + "_strYCoordinate", "");

			}
		}

		else

		{

			System.err.println("The transition is null!!!!");

		}

	}
        
	/**

	 * validate the package

	 *

	 * @param workflowPackage the workflow package

	 * @param strAction		the action the is going to be performed

	 * @param query			the query object

	 *

	 * @return the error msg if the validation if failed, if pass then return null

	 */

	protected static String validatePackage(Package workflowPackage, String strAction, DALSecurityQuery query)

	{

		String strReturnValue = null;



		if(strAction.equals(SAVE_NEW))

		{

			//check duplicate Package Id

			if(WorkflowManager.getWorkflowPackageKey(workflowPackage.getId(), query) != null)

			{

				strReturnValue = DUPLICATE + "," + WORKFLOW_PACKAGE + "," + workflowPackage.getId();

			}

		}

		else if(strAction.equals(SAVE_UPDATE))

		{

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(workflowPackage.getId(), query);

			Vector vecWorkflowTemplateKeys = WorkflowManager.getWorkflowTemplateKeys(strWorkflowPackageKey, query);



			Enumeration enumWorkflowTemplateKeys = vecWorkflowTemplateKeys.elements();

			boolean blHaveActiveTemplates = false;

			boolean blHaveActiveInstantiations = false;

			boolean blDoParentsHaveActiveInstantiations = false;

			

			while(enumWorkflowTemplateKeys.hasMoreElements())

			{

				String strWorkflowTemplateKey = (String) enumWorkflowTemplateKeys.nextElement();

				blHaveActiveTemplates = WorkflowManager.haveActiveTemplates(strWorkflowTemplateKey, query);



				blHaveActiveInstantiations = WorkflowManager.haveActiveInstantiations(strWorkflowTemplateKey, query);



				blDoParentsHaveActiveInstantiations = WorkflowManager.doParentsHaveActiveInstantiations(

																			strWorkflowTemplateKey, query);



				if(blHaveActiveTemplates || blHaveActiveInstantiations || blDoParentsHaveActiveInstantiations)

				{

					break;

				}

			}



			//check existence of the Package

			if(strWorkflowPackageKey == null)

			{

				strReturnValue = NOT_EXIST + "," + WORKFLOW_PACKAGE + "," + workflowPackage.getId();

			}

			//return not allow to update due to active workflow and subworkflow templates 

			else if(strWorkflowPackageKey != null && blHaveActiveTemplates)

			{

				strReturnValue = ACTIVE_TEMPLATE + "," + WORKFLOW_PACKAGE + "," + workflowPackage.getId();

			}

			//return not allow to update due to active instantiations of the workflow and subworkflow templates 

			else if(strWorkflowPackageKey != null && blHaveActiveInstantiations)

			{

				strReturnValue = ACTIVE_INSTANTIATIONS + "," + WORKFLOW_PACKAGE + "," + workflowPackage.getId();

			}

			//return not allow to update due to active instantiations of the parent workflow templates 

			else if(strWorkflowPackageKey != null && blDoParentsHaveActiveInstantiations)

			{

				strReturnValue = PARENT_ACTIVE_INSTANTIATIONS + "," + WORKFLOW_PACKAGE + "," + workflowPackage.getId();

			}

		}



		return strReturnValue;	

	}



	/**

	 * check the existence of the package header

	 *

	 * @param strDomain			the domain

	 * @param strWorkflowPackageXPDLId 	the workflow package XPDL id

	 * @param query				the query objeca

	 *

	 * @return  the indicator to indicate whether the package header exist or not

	 */

	protected static boolean isPackageHeaderExist(String strDomain,

												String strWorkflowPackageXPDLId,

												DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);

			query.setField(strDomain + "_intWorkflowPackageHeaderKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_HEADER_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_PACKAGE_HEADER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isPackageHeaderExist - "

					+ e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * check the existence of the workflow process/template

	 *

	 * @param strDomain			the domain

	 * @param strWorkflowPackageXPDLId 	the workflow package XPDL id

	 * @param query				the query object

	 *

	 * @return  the indicator to indicate whether the workflow process exist or not

	 */

	protected static boolean isWorkflowProcessExist(String strDomain, String strWorkflowPackageXPDLId,

								            	  String strWorkflowProcessXPDLId, DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);

			query.setField(strDomain + "_intWorkflowTemplateKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId", "=",

						   strWorkflowProcessXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isWorkflowProcessExist - "

					+ e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * check the existence of the workflow process/template header

	 *

	 * @param strDomain			the domain

	 * @param strWorkflowPackageXPDLId 	the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId 	the workflow template XPDL id

	 * @param query				the query object

	 *

	 * @return  the indicator to indicate whether the process header exist or not

	 */

	protected static boolean isProcessHeaderExist(String strDomain, String strWorkflowPackageXPDLId,

												String strWorkflowTemplateXPDLId,

								   				DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

						strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);

			query.setField(strDomain + "_intWorkflowTemplateHeaderKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_HEADER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_HEADER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isProcessHeaderExist - " + e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * check the existence of the workflow template parameter

	 *

	 * @param strDomain			the domain

	 * @param strWorkflowPackageXPDLId 	the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId 	the workflow template XPDL id

	 * @param strWorkflowTemplateParameterName	the workflow template parameter name

	 * @param query				the query object

	 *

	 * @return	 the indicator to indicate whether the workflow template exist or not

	 */

	protected static boolean isWorkflowTemplateParameterExist(String strDomain, String strWorkflowPackageXPDLId,

										  	  String strWorkflowTemplateXPDLId, String strWorkflowTemplateParameterName,

								   		  	  DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

						strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);

			query.setField(strDomain + "_strName", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_strName", "=",

						   strWorkflowTemplateParameterName, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isParameterExist - " + e.toString(), e);

        }



		return blReturnValue;

	}



























	/**

	 * check the existence of the workflow trigger

	 *

	 * @param strDomain			the domain

	 * @param strWorkflowPackageXPDLId 	the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId 	the workflow template XPDL id

	 * @param query				the query object

	 *

	 * @return	 the indicator to indicate whether the trigger exist or not

	 */

	protected static boolean isTriggerExist(String strDomain, String strWorkflowPackageXPDLId,

										  String strWorkflowTemplateXPDLId, String strWorkflowTriggerXPDLId,

								   		  DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

						strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);

			query.setField(strDomain + "_intTriggerKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TRIGGER_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intWorkflowPackageKey", "=",

						   strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_strTriggerXPDLId", "=",

						   strWorkflowTriggerXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isTriggerExist - " + e.toString(), e);

        }



		return blReturnValue;

	}

	/** 
	 * Validate the Workflow Activication 
	 *
	 * @param strTemplateKey Template Key to be activated
	 * @param query the query object
	 * 
	 * @return the validation error message.  Returns null if no errors
	 */        
	protected static String validateWorkflowActivation(String strTemplateKey, DALSecurityQuery query)
	{
		// Workflow can only be activated if all the functions in the 
		// workflow have the file uploaded.  Don't need to check for functions
		// in sub workflows as a sub workflow can only be activated if 
		// it meets this condition anyway

		String strReturnValue = null;

		Vector vtFormFields = DatabaseSchema.getFormFields("workflow_activity_details");

		try
		{
			// get all the function keys in the workflow template key
			query.reset();
			query.setDomain("WORKFLOW_ACTIVITY", null, null, null);
			query.setFields(vtFormFields, null);
			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 
							0, DALQuery.WHERE_HAS_VALUE);                
			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", 
						   "=", strTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			String strClassName   = null;
			String strFunctionKey = null;
			String strType        = null;
			while ( (strReturnValue == null) &&
					rsResultSet.next() )
			{
				strType = rsResultSet.getString("WORKFLOW_ACTIVITY_strType");                    

				// check system tasks only!
				if ( strType.equals(WORKFLOW_TASK_TYPE_SYSTEM) )
				{
					strFunctionKey = rsResultSet.getString("WORKFLOW_ACTIVITY_intFunctionKey");

					//System.err.println("strFunctionKey = >>>" + strFunctionKey + "<<<");

					ResultSet rsFunctionDetails = getFunctionDetails(strFunctionKey, query);

					//if (rsFunctionDetails.getFetchSize() == 0)
					if (rsFunctionDetails == null || 
						rsFunctionDetails.getString("WORKFLOW_FUNCTION_intWorkflowFunctionKey") == null || 
					    rsFunctionDetails.getString("WORKFLOW_FUNCTION_intWorkflowFunctionKey").length() == 0)
					{
						strReturnValue = "Cannot be activated because one of the " +
										 "system tasks in the workflow has an " +
										 "invalid/missing function key.";
					}

					// validate the class is valid
					else if ( ((strClassName = rsFunctionDetails.getString("WORKFLOW_FUNCTION_strClassName")) == null) ||
						 (strClassName.length() == 0) ||
						 (strClassName.replaceFirst("neuragenix.genix.workflow.function.", "").length() == 0) )
					{
						strReturnValue = "Cannot be activated because one of the " +
										 "system tasks in the workflow is " +
										 "missing the class name.";
					}
					else
					{
						String filename = strClassName.replaceFirst("neuragenix.genix.workflow.function.", "").replaceAll("\\.", "/") + 
										  ".class";

						// let's physically look for the file
						String path = FUNCTION_DIRECTORY + "/" + filename;

						//System.err.println("path = >>>" + path + "<<<");

						File file = new File(path);
						if (file.exists() == false)
						{
							strReturnValue = "Cannot be activated because one of the " + 
											 "system tasks in the workflow is " +
											 "missing the class file.";
						}

						//System.err.println("strReturnValue = >>>" + strReturnValue + "<<<");
					}

					// close result set
					rsFunctionDetails.close();
				}
			}
			// close the result set
			rsResultSet.close();                
		}
		catch(Exception e)
		{
			strReturnValue = "Workflow activate validation failed";
			LogService.instance().log(LogService.ERROR,
							  "Unknown error in WorkflowManager::validateWorkflowActivation - " + e.toString(), e);
		}
		return strReturnValue;
	}

        
        /** 
         * Validate the Workflow Activication 
         *
         * @param strTemplateKey Template Key to be activated
         * @param query the query object
         * 
         * @return the validation error message.  Returns null if no errors
         */

		/*
        protected static String validateWorkflowActivation(String strTemplateKey, DALSecurityQuery query)
        {
            // Workflow can only be activated if all the functions in the 
            // workflow have the file uploaded.  Don't need to check for functions
            // in sub workflows as a sub workflow can only be activated if 
            // it meets this condition anyway
            
            String strReturnValue = null;
            
            Vector vtFormFields = new Vector(20);
            vtFormFields.addAll(DatabaseSchema.getFormFields("workflow_function_details"));            
            vtFormFields.addAll(DatabaseSchema.getFormFields("workflow_activity_details"));
            
            try
            {                
                // get all the function keys in the workflow template key
                query.reset();
                query.setDomain("WORKFLOW_ACTIVITY", null, null, null);
                query.setDomain("WORKFLOW_FUNCTION", 
                                "WORKFLOW_ACTIVITY_intFunctionKey", 
                                "WORKFLOW_FUNCTION_intWorkflowFunctionKey", 
                                "LEFT JOIN");
                query.setFields(vtFormFields, null);
                query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", 
                               "=", strTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_strType", "=", 
                               WORKFLOW_TASK_TYPE_SYSTEM, 0, DALQuery.WHERE_HAS_VALUE);
                
                ResultSet rsResultSet = query.executeSelect();
                
                String strClassName   = null;
                String strFunctionKey = null;
                while (rsResultSet.next())
                {
                    strFunctionKey = rsResultSet.getString("WORKFLOW_FUNCTION_intWorkflowFunctionKey");
                    strClassName   = rsResultSet.getString("WORKFLOW_FUNCTION_strClassName");                   
                    
                    // validate that the function key is exists
                    if ( (strFunctionKey == null) ||
                         (strFunctionKey.length() == 0) )
                    {
                        strReturnValue = "Cannot be activated because one of the " +
                                         "system tasks in the workflow has an " +
                                         "invalid/missing function key."; 
                        break;
                    }
                    // validate the class is valid
                    else if ( (strClassName == null) ||
                         (strClassName.length() == 0) ||
                         (strClassName.replaceFirst("neuragenix.genix.workflow.function.", "").length() == 0) )
                    {
                        strReturnValue = "Cannot be activated because one of the " +
                                         "system tasks in the workflow is " +
                                         "missing the class name.";
                        break;
                    }
                    else
                    {
                        String filename = strClassName.replaceFirst("neuragenix.genix.workflow.function.", "") + 
                                          ".class";
                        
                        // let's physically look for the file
                        String path = FUNCTION_DIRECTORY + "/" + filename;
                        
                        File file = new File(path);
                        if (file.exists() == false)
                        {
                            strReturnValue = "Cannot be activated because one of the " + 
                                             "system tasks in the workflow is " +
                                             "missing the class file.";
                            break;
                        }
                    }
                }
                
                // close the result set
                rsResultSet.close();                
            }
            catch(Exception e)
            {
                strReturnValue = "Workflow activate validation failed";
                LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::validateWorkflowActivation - " + e.toString(), e);
            }
            
            return strReturnValue;
        }
        */ 
        
        /**
         * Validate function for updating
         * 
         * @param strFunctionKey the Function key
         * @param strType the Function type
         * @param query the query object
         *  
         * @return the validation error message.  Returns null if no errors
         */
        protected static String validateFunction(String strFunctionKey,
                                                 String strType,
                                                 DALSecurityQuery query)
        {
            String strReturnValue = null;
            
            // store template Keys for non-active
            Vector vtNonActiveTemplateKeys = new Vector(10, 3);
            // store instance Keys for completed
            Vector vtCompletedInstanceKeys = new Vector(10, 3);
            // hash table to store the names and keys of WF instances
            Hashtable htWorkflowInstances  = new Hashtable(10);            
            
            try
            {
                // check if function exists first
                if (strFunctionKey == null || strFunctionKey == "")
                    return "Missing function key";
                
                if (isFunctionExist(strFunctionKey, null, query) == false)
                    return "Function does not exist.";
                
                // check if its in an active WF template                
                Vector vtFormFields = new Vector(10, 3);
                vtFormFields.addAll(DatabaseSchema.getFormFields("workflow_template_details"));
                vtFormFields.addAll(DatabaseSchema.getFormFields("workflow_activity_details"));
                
                query.reset();

                query.setDomain("WORKFLOW_TEMPLATE", null, null, null);
                query.setDomain("WORKFLOW_ACTIVITY", "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", 
                                "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "LEFT JOIN");
                query.setFields(vtFormFields, null);                
                query.setDistinctField("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");
                query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intFunctionKey", "=", strFunctionKey, 0, DALQuery.WHERE_HAS_VALUE);            
                
                ResultSet rsResultSet = query.executeSelect();
                while (rsResultSet.next())
                {
                    String strStatus = rsResultSet.getString("WORKFLOW_TEMPLATE_strStatus");
                    if (strStatus.equals(WORKFLOW_TEMPLATE_STATUS_ACTIVE))
                    {
                        if (strReturnValue == null)                        
                            strReturnValue = "Function is being used in the following active workflow template(s): ";
                        strReturnValue += rsResultSet.getString("WORKFLOW_TEMPLATE_strName") + ", ";
                    }
                    else
                    {
                        String templateKey = rsResultSet.getString("WORKFLOW_ACTIVITY_intWorkflowTemplateKey");                        
                        vtNonActiveTemplateKeys.add(templateKey);
                    }
                }
                
                // close the object reference of result set
                rsResultSet.close();
                
                // further checking if no errors so far
                if (strReturnValue == null)
                {
                    // check if its in a running WF instance
                    for (int i=0; (i < vtNonActiveTemplateKeys.size()); i++)
                    {
                        String strTemplateKey = (String) vtNonActiveTemplateKeys.get(i);                        
                        
                        vtFormFields = new Vector(3);
                        // add the instance key as a field as well!
                        vtFormFields.add("WORKFLOW_INSTANCE_intWorkflowInstanceKey");
                        vtFormFields.add("WORKFLOW_INSTANCE_strName");
                        vtFormFields.add("WORKFLOW_INSTANCE_strStatus");
                        
                        query.reset();
                        query.setDomain("WORKFLOW_INSTANCE", null, null, null);
                        query.setFields(vtFormFields, null);
                        query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowTemplateKey", 
                                       "=", strTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);
                        
                        rsResultSet = query.executeSelect();                                                
                        
                        while (rsResultSet.next())
                        {
                            String strInstanceKey  = rsResultSet.getString("WORKFLOW_INSTANCE_intWorkflowInstanceKey");
                            String strInstanceName = rsResultSet.getString("WORKFLOW_INSTANCE_strName");
                            String strStatus       = rsResultSet.getString("WORKFLOW_INSTANCE_strStatus");
                            if ( strStatus.equals(WORKFLOW_INSTANCE_STATUS_SUSPENDED)   ||
                                 strStatus.equals(WORKFLOW_INSTANCE_STATUS_IN_PROGRESS) ||
                                 strStatus.equals(WORKFLOW_INSTANCE_STATUS_NOT_STARTED) )                                 
                            {
                                if (strReturnValue == null)
                                    strReturnValue = "Function is being used in the following workflow instance(s): ";
                                strReturnValue += strInstanceName + " (instance key: " + strInstanceKey + ")" + ", ";
                            }
                            else
                            {
                                // if completed, it may still be running as a thread, so further checks may be required!                                
                                vtCompletedInstanceKeys.add(strInstanceKey);
                                htWorkflowInstances.put(strInstanceKey, strInstanceName);
                            }
                        }
                        
                        // close result set
                        rsResultSet.close();
                    }                                        
                } // end of instance validation

                // further checking for thread type functions, only if no errors!
                if (strReturnValue == null)
                {                    
                    // if type is undefined, let's find it!
                    if (strType == null)
                    {
                        rsResultSet = getFunctionDetails(strFunctionKey, query);                    
                        strType = rsResultSet.getString("WORKFLOW_FUNCTION_strType");                        
                    }
                    
                    // Only check if type Thread of course!
                    if (strType.equals(WORKFLOW_FUNCTION_TYPE_THREAD))
                    {
                        // check instances to see if it exists in the function thread
                        for (int i=0; (i < vtCompletedInstanceKeys.size()); i++)
                        {
                            String strInstanceKey = (String) vtCompletedInstanceKeys.get(i);

                            vtFormFields = new Vector(3);
                            // add the Instance Key to form fields
                            vtFormFields.add("WORKFLOW_FUNCTION_THREAD_strStatus");
                            vtFormFields.add("WORKFLOW_FUNCTION_THREAD_intExecuted");
                            vtFormFields.add("WORKFLOW_TASK_intRetryCounter");

                            query.reset();
                            query.setDomain("WORKFLOW_FUNCTION_THREAD", null, null, null);
                            query.setDomain("WORKFLOW_TASK", "WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", 
                                            "WORKFLOW_TASK_intTaskKey", "LEFT JOIN");                        
                            query.setFields(vtFormFields, null);                
                            query.setWhere(null,  0, "WORKFLOW_TASK_intFunctionKey", "=", strFunctionKey, 0, DALQuery.WHERE_HAS_VALUE);
                            query.setWhere("AND", 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

                            rsResultSet = query.executeSelect();

                            while (rsResultSet.next())
                            {
                                String strStatus = rsResultSet.getString("WORKFLOW_FUNCTION_THREAD_strStatus");

                                if (strStatus.equals(WORKFLOW_FUNCTION_THREAD_RUNNING))
                                {
                                    // if it's running, then we don't want to allow modifications
                                    if (strReturnValue == null)
                                        strReturnValue = "Function is still running (or will re-run) as a thread in the following workflow instance(s): ";
                                    strReturnValue += htWorkflowInstances.get(strInstanceKey) + " (instance key: " + strInstanceKey + ")";
                                }
                                else if (strStatus.equals(WORKFLOW_FUNCTION_THREAD_FAILED))
                                {
                                    // if it's failed, then we want to make sure that it's not 
                                    // gonna run again by comparing the number of times it's
                                    // been executed to the maximum retries for the task
                                    int executed     = rsResultSet.getInt("WORKFLOW_FUNCTION_THREAD_intExecuted");
                                    int retryCounter = rsResultSet.getInt("WORKFLOW_TASK_intRetryCounter");

                                    // executed and retry must equal to ensure that thread will not run!
                                    if (executed != retryCounter)
                                    {
                                        if (strReturnValue == null)
                                            strReturnValue = "Function is still running (or will re-run) as a thread in the following workflow instance(s): ";
                                        strReturnValue += htWorkflowInstances.get(strInstanceKey) + " (instance key: " + strInstanceKey + ")" + ", ";
                                    }
                                }
                            }

                            // close result set
                            rsResultSet.close();
                        }
                    }
                } // end of thread type validation
                
            }
            catch (Exception e)
            {
                strReturnValue = "Function validation failed.";
                LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::validateFunction - " + e.toString(), e);
            }

            // cleanup error message
            if ( (strReturnValue != null) && strReturnValue.endsWith(", ") )
                strReturnValue = strReturnValue.substring(0, strReturnValue.length()-2);
            
            return strReturnValue;
        }

        /**
         *  Check if function name exists
         *  
         *  @param functionName Function name to check
         *  @param strExcludeFunctionKey function key to exclude from search
         *  @param query The query object
         *
         *  @return whether the function name exists or not
         */        
        protected static boolean isFunctionNameExist(String strName,                                                 
                                                     String strExcludeFunctionKey, 
                                                     DALSecurityQuery query)
        {
            boolean blReturnValue = false;
            
            try
            {
                query.reset();
                query.setDomain("WORKFLOW_FUNCTION", null, null, null);
                query.setField("WORKFLOW_FUNCTION_intWorkflowFunctionKey", "null", "hashtable");
                query.setWhere(null, 0, "WORKFLOW_FUNCTION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "WORKFLOW_FUNCTION_strName", "=", strName, 0, DALQuery.WHERE_HAS_VALUE);
                
                if (strExcludeFunctionKey != null)
                {
                    query.setWhere("AND", 0, "WORKFLOW_FUNCTION_intWorkflowFunctionKey", "<>", strExcludeFunctionKey, 0, 0);
                }

                ResultSet rsResultSet = query.executeSelect();                
                if(rsResultSet.next())
                    blReturnValue = true;

                rsResultSet.close();
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::isFunctionNameExist - " + e.toString(), e);
            }
            return blReturnValue;            
        }

        /**
         *  Check if function exists based on function key and/or name
         *  
         *  @param strDomain Domain of function key
         *  @param functionKey Function key to check for existance
         *  @param functionName Function name
         *  @param query The query object
         *
         *  @return whether the function exists or not
         */        
        protected static boolean isFunctionExist(String strFunctionKey, 
                                                 String strName,                                                 
                                                 DALSecurityQuery query)
        {
            boolean blCondValue   = false;
            boolean blReturnValue = false;
            
            try
            {
                query.reset();
                query.setDomain("WORKFLOW_FUNCTION", null, null, null);
                query.setField("WORKFLOW_FUNCTION_intWorkflowFunctionKey", "null", "hashtable");
                query.setWhere(null, 0, "WORKFLOW_FUNCTION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                
                if (strFunctionKey != null && strFunctionKey.trim() != "")
                {
                    query.setWhere("AND", 0, "WORKFLOW_FUNCTION_intWorkflowFunctionKey", "=", strFunctionKey, 0, 0);
                    blCondValue = true;
                }
                if (strName != null && strName.trim() != "")
                {
                    query.setWhere("AND", 0, "WORKFLOW_FUNCTION_strName", "=", strName, 0, 0);
                    blCondValue = true;
                }
                
                // only process the query if a condition has been specified
                if (blCondValue)
                {
                    ResultSet rsResultSet = query.executeSelect();                
                    if(rsResultSet.next())
                        blReturnValue = true;
                    
                    rsResultSet.close();
                }                
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::isFunctionExist - " + e.toString(), e);
            }
            return blReturnValue;            
        }        
        

	/**

	 * check the existence of the workflow activity 

	 *

	 * @param strDomain			the domain

	 * @param strWorkflowPackageXPDLId 	the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId 	the workflow template XPDL id

	 * @param query				the query object

	 *

	 * @return	 the indicator to indicate whether the activity exist or not

	 */

	protected static boolean isActivityExist(String strDomain, String strWorkflowPackageXPDLId,

										  String strWorkflowTemplateXPDLId, String strWorkflowActivityXPDLId,

								   		  DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

						strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			query.reset();

			

			query.setDomain(strDomain, null, null, null);

			query.setField(strDomain + "_intActivityKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId", "=",

						   strWorkflowActivityXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isActivityExist - " + e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * check the existence of the workflow transition

	 *

	 * @param strDomain			the domain

	 * @param strWorkflowPackageXPDLId 	the workflow package XPDL id

	 * @param strWorkflowTemplateXPDLId 	the workflow template XPDL id

	 * @param strWorkflowTransitionXPDLId	the workflow transition XPDL id

	 * @param query				the query object

	 *

	 * @return the indicator to indicate whether the transition exist or not

	 */

	protected static boolean isTransitionExist(String strDomain, String strWorkflowPackageXPDLId,

										  	 String strWorkflowTemplateXPDLId, String strWorkflowTransitionXPDLId,

								   		  	 String strWorkflowActivityTransitionFromId,

											 String strWorkflowActivityTransitionToId, DALSecurityQuery query)

	{

		boolean blReturnValue = false;



		try

		{

			//get the workflow package key

			String strWorkflowPackageKey = WorkflowManager.getWorkflowPackageKey(

					strWorkflowPackageXPDLId, query);



			//get the workflow template key

			String strWorkflowTemplateKey = WorkflowManager.getWorkflowTemplateKey(

						strWorkflowTemplateXPDLId, strWorkflowPackageKey, query);



			//get the workflow transition from key	

			String strWorkflowTransitionFromKey = WorkflowManager.getWorkflowActivityKey(

					strWorkflowTemplateKey, strWorkflowActivityTransitionFromId, query);



			//get the workflow transition to key

			String strWorkflowTransitionToKey = WorkflowManager.getWorkflowActivityKey(

					strWorkflowTemplateKey, strWorkflowActivityTransitionToId, query);



//System.err.println("strWorkflowTemplateKey = >>>" + strWorkflowTemplateKey + "<<<");

//System.err.println("strWorkflowTransitionXPDLId = >>>" + strWorkflowTransitionXPDLId + "<<<");

//System.err.println("strWorkflowTransitionFromKey = >>>" + strWorkflowTransitionFromKey + "<<<");

//System.err.println("strWorkflowTransitionToKey = >>>" + strWorkflowTransitionToKey + "<<<");

			

			query.reset();

			

			query.setDomain(strDomain, null, null, null);

			query.setField(strDomain + "_intActivityTransitionKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_TRANSITION_strActivityTransitionXPDLId", "=",

						   strWorkflowTransitionXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			//query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityKey", "=",

			//			   strWorkflowTransitionFromKey, 0, DALQuery.WHERE_HAS_VALUE);

			//query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityKey", "=",

			//			   strWorkflowTransitionToKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intOriginActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intTargetActivityWkflTmplKey", "=",

						   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				blReturnValue = true;

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isTransitionExist - " + e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * get the workflow task transition list

	 *

	 * @param strWorkflowInstanceKey the workflow instance key

	 *

	 * @return the workflow task transition list

	 */

	public static Vector getWorkflowTaskTransition(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		Vector vecTaskTransition = new Vector();



		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields("workflow_task_transition_details");



			query.reset();

			

			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);    

			query.setFields(vtDomainDetails, null);

			//query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "=", 

			//				strOriginTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskWorkflowInstanceKey", "=",

						   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				TaskTransition taskTransition = new TaskTransition();	

				taskTransition.setName(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_strName"));

				taskTransition.setFrom(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_intOriginTaskKey"));

				taskTransition.setTo(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_intTargetTaskKey"));

				taskTransition.setType(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_strType"));

				taskTransition.setXCoordinate(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_strXCoordinate"));

				taskTransition.setYCoordinate(rsResultSet.getString("WORKFLOW_TASK_TRANSITION_strYCoordinate"));



				vecTaskTransition.add(taskTransition);

			}



			/*

			for(int intIndex = 0; intIndex < vecTaskTransition.size(); intIndex++)

			{

				TaskTransition tmpTaskTransition = (TaskTransition) vecTaskTransition.get(intIndex);



				System.err.println("TaskTransition - Name = " + tmpTaskTransition.getName());

				System.err.println("TaskTransition - From = " + tmpTaskTransition.getFrom());

				System.err.println("TaskTransition - To = " + tmpTaskTransition.getTo());

				System.err.println("TaskTransition - Type = " + tmpTaskTransition.getType());

				System.err.println("TaskTransition - X Coordinate = " + tmpTaskTransition.getXCoordinate());

				System.err.println("TaskTransition - Y Coordinate = " + tmpTaskTransition.getYCoordinate());

			}

			*/

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTaskTransition - " + e.toString(), e);

        }



		return vecTaskTransition;

	}



	/**

	 * get workflow instance

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 *

	 * @return the workflow instance object

	 */

	public static WorkflowInstance getWorkflowInstance(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		WorkflowInstance workflowInstance = null;



		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields("workflow_instance_details");



			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setFields(vtDomainDetails, null);

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=", 

							strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

		

			//move to the first(only) record

			rsResultSet.first();



			workflowInstance = new WorkflowInstance();



			workflowInstance.setName(rsResultSet.getString("WORKFLOW_INSTANCE_strName"));

			workflowInstance.setStatus(rsResultSet.getString("WORKFLOW_INSTANCE_strStatus"));

			workflowInstance.setInitialTaskKey(rsResultSet.getString("WORKFLOW_INSTANCE_intInitialTaskKey"));



			//collecting all the tasks

			Vector vecWorkflowTask = new Vector();

			Vector vecWorkflowTaskKey = WorkflowManager.getWorkflowTaskKeys(strWorkflowInstanceKey, query);

			for(int intIndex = 0; intIndex < vecWorkflowTaskKey.size(); intIndex++)

			{

				String strTaskKey = (String) vecWorkflowTaskKey.get(intIndex);



				Task task = WorkflowManager.getWorkflowTask(strTaskKey, query);

				vecWorkflowTask.add(task);

			}



			//set all the task under the workflow instance object

			workflowInstance.setTasks(vecWorkflowTask);



			//collecting all the task transitions

			Vector vecWorkflowTaskTransition = WorkflowManager.getWorkflowTaskTransition(strWorkflowInstanceKey, query);



			//set all the task transition under the workflow instance object

			workflowInstance.setTaskTransitions(vecWorkflowTaskTransition);



			/*	

			System.err.println("WorkflowInstance - Name = " + workflowInstance.getName());

			System.err.println("WorkflowInstance - Status = " + workflowInstance.getStatus());

			System.err.println("WorkflowInstance - Initial Task Key = " + workflowInstance.getInitialTaskKey());



			Vector vecTestWorkflowTask = workflowInstance.getTasks();



			for(int intIndex2 = 0; intIndex2 < vecTestWorkflowTask.size(); intIndex2++)

			{

				Task task = (Task) vecTestWorkflowTask.get(intIndex2);



				System.err.println("");

				System.err.println("Task - Key = " + task.getKey());

				System.err.println("Task - Name = " + task.getName());

				System.err.println("Task - Status = " + task.getStatus());

				System.err.println("Task - Type = " + task.getType());

				System.err.println("Task - Performer = " + task.getPerformer());

				System.err.println("Task - Position X = " + task.getPositionX());

				System.err.println("Task - Position Y = " + task.getPositionY());

				System.err.println("Task - Width = " + task.getWidth());

				System.err.println("Task - Height = " + task.getHeight());

				System.err.println("Task - Date Completed = " + task.getDateCompleted());

			}



			Vector vecTestWorkflowTaskTransition = workflowInstance.getTaskTransitions();



			for(int intIndex3 = 0; intIndex3 < vecTestWorkflowTaskTransition.size(); intIndex3++)

			{

				TaskTransition tmpTaskTransition = (TaskTransition) vecTestWorkflowTaskTransition.get(intIndex3);



				System.err.println("");

				System.err.println("TaskTransition - Name = " + tmpTaskTransition.getName());

				System.err.println("TaskTransition - From = " + tmpTaskTransition.getFrom());

				System.err.println("TaskTransition - To = " + tmpTaskTransition.getTo());

				System.err.println("TaskTransition - Type = " + tmpTaskTransition.getType());

				System.err.println("TaskTransition - X Coordinate = " + tmpTaskTransition.getXCoordinate());

				System.err.println("TaskTransition - Y Coordinate = " + tmpTaskTransition.getYCoordinate());

			}

			*/

			

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowInstance - " + e.toString(), e);

        }



		return workflowInstance;

	}

	

	/**

	 * get workflow task object

	 *

	 * @param strWorkflowTaskKey	the workflow task key

	 *

	 * @return the workflow task object

	 */

	public static Task getWorkflowTask(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		Task task = null;



		try

		{

			Hashtable hashFieldData = new Hashtable();

			Vector vtDomainDetails = DatabaseSchema.getFormFields("workflow_task_details");



			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setFields(vtDomainDetails, null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", 

							strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

		

			//move to the first(only) record

			rsResultSet.first();



			task = new Task();



			StringBuffer strBufTaskName = new StringBuffer();

			StringBuffer strBufTaskInstruction = new StringBuffer();

		

			//get the task name and instruction and place them in StringBuffer

			strBufTaskName.insert(0, rsResultSet.getString("WORKFLOW_TASK_strName")); 

			strBufTaskInstruction.insert(0, rsResultSet.getString("WORKFLOW_TASK_strInstruction"));   



			//get all the runtime data from ix_runtimedata that has workflow instance key = strWorkflowInstanceKey

			String strWorkflowInstanceKey = WorkflowManager.getWorkflowInstanceKey(strWorkflowTaskKey, query);

			ResultSet rsRuntimeData = WorkflowManager.getRuntimeData(strWorkflowInstanceKey, query);



			//while loop

			//get the parameter name 

			//construct the parameter name to be like ***Parameter Name***

			//call the Utilities method and place the modified parameter name and its corresponding value, 

			//	do this for both task name and instruction stringBuffer

			//end loop

			while(rsRuntimeData.next())

			{

				//construct the tag like <TAG/>

				String strTag = "<" + rsRuntimeData.getString("WORKFLOW_RUNTIMEDATA_strParameterName") + "/>";

				String strValue = rsRuntimeData.getString("WORKFLOW_RUNTIMEDATA_strParameterValue");



				//ignore if value is null

				if(strValue == null)

				{ 

					continue;

				}



				Utilities.substrReplace(strBufTaskName, strTag, strValue);

				Utilities.substrReplace(strBufTaskInstruction, strTag, strValue);

			}



			task.setKey(strWorkflowTaskKey);

			task.setName(strBufTaskName.toString());

			task.setStatus(rsResultSet.getString("WORKFLOW_TASK_strStatus"));

			task.setType(rsResultSet.getString("WORKFLOW_TASK_strType"));

			task.setPerformer(rsResultSet.getString("WORKFLOW_TASK_intPerformer"));

			task.setPerformerType(rsResultSet.getString("WORKFLOW_TASK_strPerformerType"));

			task.setTimeCompleted(rsResultSet.getString("WORKFLOW_TASK_strTimeCompleted"));

			task.setComment(rsResultSet.getString("WORKFLOW_TASK_strComments"));

			task.setAction(rsResultSet.getString("WORKFLOW_TASK_strAction"));

			task.setInstruction(strBufTaskInstruction.toString());

			task.setPositionX(rsResultSet.getString("WORKFLOW_TASK_intPosX"));

			task.setPositionY(rsResultSet.getString("WORKFLOW_TASK_intPosY"));

			task.setWidth(rsResultSet.getString("WORKFLOW_TASK_intWidth"));

			task.setHeight(rsResultSet.getString("WORKFLOW_TASK_intHeight"));

			task.setWorkflowInstanceKey(rsResultSet.getString("WORKFLOW_TASK_intWorkflowInstanceKey"));
                        
//                        task.setFunctionKey(rsResultSet.getString("WORKFLOW_TASK_intFunctionKey"));
		

			if(rsResultSet.getString("WORKFLOW_TASK_dtDateCompleted") != null)

			{

				//to convert date format eg. 2004-03-12 to 12/03/2004

				String strDateCompleted = Utilities.convertDateForDisplay(

										rsResultSet.getString("WORKFLOW_TASK_dtDateCompleted"));

				task.setDateCompleted(strDateCompleted);

			}
                        /*
                        // find the original sub workflow key from the template
                        String strSubWorkflowTemplateKey = "";
                        if (rsResultSet.getString("WORKFLOW_TASK_strType").equals(WORKFLOW_TASK_TYPE_SUBWORKFLOW))
                        {
                            // get the original template key based on instance key
                            String strWorkflowTemplateKey    = getWorkflowTemplateKey(strWorkflowInstanceKey, query);
                            
                            // get the XPDL Id based on the template key and instance key
                            String strWorkflowActivityXPDLId = getWorkflowActivityXPDLId(strWorkflowTaskKey, rsResultSet.getString("WORKFLOW_TASK_intWorkflowInstanceKey"), query);
                            
                            // get the subworkflow template key based on the XPDL Id
                            strSubWorkflowTemplateKey = getSubWorkflowTemplateKey(strWorkflowTemplateKey, strWorkflowActivityXPDLId, query);       
                            
                            // convert the Id to the actual name of the template
                            strSubWorkflowTemplateKey = getWorkflowTemplateName(strSubWorkflowTemplateKey, query);                            
                        }
                        task.setSubWorkflowTemplateKey(strSubWorkflowTemplateKey);
                        */
			/*

			System.err.println("Task - Key = " + task.getKey());

			System.err.println("Task - Name = " + task.getName());

			System.err.println("Task - Status = " + task.getStatus());

			System.err.println("Task - Type = " + task.getType());

			System.err.println("Task - Performer = " + task.getPerformer());

			System.err.println("Task - Position X = " + task.getPositionX());

			System.err.println("Task - Position Y = " + task.getPositionY());

			System.err.println("Task - Width = " + task.getWidth());

			System.err.println("Task - Height = " + task.getHeight());

			System.err.println("Task - Date Completed = " + task.getDateCompleted());

			*/

			

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTask - " + e.toString(), e);

        }



		return task;

	}



	/**

	 * get all available packages id from the database

	 *

	 * @param query		the query object

	 *

	 * @return all available packages id from the database

	 */

	private static Vector getPackageIds(DALSecurityQuery query)

	{

		Vector vecPackageId = new Vector();



		try

		{

			query.reset();

			query.setDomain(WORKFLOW_PACKAGE, null, null, null);    

			query.setField("WORKFLOW_PACKAGE_strWorkflowPackageXPDLId", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				vecPackageId.add(rsResultSet.getString("WORKFLOW_PACKAGE_strWorkflowPackageXPDLId"));

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getPackageIds - " + e.toString(), e);

        }



		return vecPackageId;

	}



	/**

	 * get all available packages from the database

	 *

	 * @param query		the query object

	 *

	 * @return all available packages from the database

	 */

	public static Vector getPackages(DALSecurityQuery query)

	{

		Vector vecPackage = new Vector();



		try

		{

			Vector vecPackageId = WorkflowManager.getPackageIds(query);



			for(int intIndex = 0; intIndex < vecPackageId.size(); intIndex++)

			{

				String strPackageId = (String) vecPackageId.get(intIndex);

				String strPackageName = strPackageId + ".xml";



				//get the file

				File file = new File(WorkflowManager.XPDL_DIRECTORY + "/" + strPackageName);
                                
                                // check that the file exists first                                
                                if (file.exists() == false)
                                {
                                    // if file does not exist, then skip the parsing of XML - duh!
                                    continue;
                                }   


				//construct the FileInputStream

				FileInputStream fileInputStream = new FileInputStream(file);



				//parse the XPDL file

        		XPDLReader.parseXML((InputStream) fileInputStream);

				Package neuragenixPackage = XPDLReader.getWorkflowPackage();		

				vecPackage.add(neuragenixPackage);

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getPackages - " + e.toString(), e);

        }



		/*

		System.err.println("vecPackage.size() = " + vecPackage.size());

		System.err.println("vecPackage = " + vecPackage);

		System.err.println("");



		for(int intIndex = 0; intIndex < vecPackage.size(); intIndex++)

		{

			Package neuragenixPackage = (Package) vecPackage.get(intIndex);



			System.err.println("Package - Id = " + neuragenixPackage.getId());

			System.err.println("Package - Name = " + neuragenixPackage.getName());

		}

		*/

		



		return vecPackage;

	}



	/**

	 * get all available functions from the database

	 *

	 * @param query		the query object

	 *

	 * @return all available functions from the database

	 */

	public static Vector getFunctions(DALSecurityQuery query)

	{

		Vector vecFunction = new Vector();



		try

		{

			Vector vtDomainDetails = new Vector(DatabaseSchema.getFormFields("workflow_function_details"));



			query.reset();

			query.setDomain(WORKFLOW_FUNCTION, null, null, null);

			vtDomainDetails.add("WORKFLOW_FUNCTION_intWorkflowFunctionKey");

			query.setFields(vtDomainDetails, null);

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				Function function = new Function();	

				function.setKey(rsResultSet.getString("WORKFLOW_FUNCTION_intWorkflowFunctionKey"));

				function.setName(rsResultSet.getString("WORKFLOW_FUNCTION_strName"));

				vecFunction.add(function);

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getFunctions - " + e.toString(), e);

        }



		/*

		for(int intIndex = 0; intIndex < vecFunction.size(); intIndex++)

		{

			Function function = (Function) vecFunction.get(intIndex);



			System.err.println("");

			System.err.println("Function - Key = " + function.getKey());

			System.err.println("Function - Name = " + function.getName());



		}

		*/



		return vecFunction;

	}

	/**

	 * validate trigger data

	 *

	 * @param strAction				the action being performed

	 * @param runtimeData			the runtimeData

	 * @param strWorkflowTemplateKey	the workflow template key

	 * @param query					the query object

	 *

	 * @return a list of workflow template key that has passed the trigger validation

	 */

	public static Vector validateTriggerData(String strAction, ChannelRuntimeData runtimeData, 

									   String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		//Enumeration enumParameterNames = runtimeData.getParameterNames();

		//while(enumParameterNames.hasMoreElements())

		//{

		//	String strParameterName = (String) enumParameterNames.nextElement();

		//	System.err.println("strParameterName = >>>" + strParameterName + "<<<");



		//	String strValue = (String) runtimeData.getParameter(strParameterName);

		//	System.err.println("strValue = >>>" + strValue + "<<<");

		//}

		

		Vector vecValidWorkflowTemplateKey = new Vector();



		try

		{

			Vector vecTrigger = new Vector();

			

			//get the triggers

			if(strAction != null && strAction.length() != 0)

			{

				vecTrigger = WorkflowManager.getTrigger(strAction, null, query);

			}

			else if(strWorkflowTemplateKey != null && strWorkflowTemplateKey.length() !=0)

			{

				vecTrigger = WorkflowManager.getTrigger(null, strWorkflowTemplateKey, query);



				//No Trigger is specified, this is a special case when 

				//the user instantiate the workflow template manually 

				if(vecTrigger.size() == 0)

				{

					vecValidWorkflowTemplateKey.add(strWorkflowTemplateKey);

					return vecValidWorkflowTemplateKey;

				}

			}



//System.err.println("vecTrigger size = >>>" + vecTrigger.size() + "<<<");



			//get the distinct workflow template key from the triggers

			HashSet hashSetWorkflowTemplateKey = new HashSet();

			Enumeration enumTrigger = vecTrigger.elements();

			while(enumTrigger.hasMoreElements())

			{

				Trigger trigger = (Trigger) enumTrigger.nextElement();

				hashSetWorkflowTemplateKey.add(trigger.getWorkflowTemplateKey());

			}



//System.err.println("hashSetWorkflowTemplateKey = >>>" + hashSetWorkflowTemplateKey + "<<<");



			//for each distinct workflow template key, check the validity of the trigger

			Iterator iteWorkflowTemplateKey = hashSetWorkflowTemplateKey.iterator();

			while(iteWorkflowTemplateKey.hasNext())

			{

				String strTempWorkflowTemplateKey = (String) iteWorkflowTemplateKey.next();



//System.err.println("strTempWorkflowTemplateKey = >>>" + strTempWorkflowTemplateKey + "<<<");



				//from the vector trigger get the trigger that relate to the

				//strTempWorkflowTemplateKey only in sorted order!!!

				Vector vecFilteredTrigger = WorkflowManager.filterTrigger(vecTrigger, strTempWorkflowTemplateKey);

				

				boolean blCheckTrigger = true;

				for(int intIndex = 0; intIndex < vecFilteredTrigger.size(); intIndex++)

				{

					Trigger trigger = (Trigger) vecFilteredTrigger.get(intIndex);



//System.err.println("trigger.getWorkflowTemplateKey() = >>>" + trigger.getWorkflowTemplateKey() + "<<<");

					

					//this is when there is parameter to check

					//which is indicated when the field name is not null and the length is not zero (0)

					//if the field is null or (the field is not null and the length is zero (0)) then

					//there is no parameter to check

					if(trigger.getField() != null && trigger.getField().length() != 0)

					{

						String strConnection1 = trigger.getConnection1();



//System.err.println("strConnection1 = >>>" + strConnection1 + "<<<");

						

						if(strConnection1 == null || strConnection1.length() == 0)

						{

							//check the trigger

							blCheckTrigger = WorkflowManager.checkTrigger(trigger, runtimeData);

//System.err.println("strConnection1 is null or empty string");

						}

						else

						{

							if(strConnection1.equals(DATA_CONNECTOR_AND))

							{

								//check the trigger

								blCheckTrigger &= WorkflowManager.checkTrigger(trigger, runtimeData);

//System.err.println("strConnection1 is an AND");

							}

							else if(strConnection1.equals(DATA_CONNECTOR_OR))

							{

								//check the trigger

								blCheckTrigger |= WorkflowManager.checkTrigger(trigger, runtimeData);

//System.err.println("strConnection1 is an OR");

							}

						}

					}

//System.err.println("INTERM - blCheckTrigger = " + blCheckTrigger);

				}



//System.err.println("FINAL - blCheckTrigger = " + blCheckTrigger);



				//if the template is valid add its key to the list

				if(blCheckTrigger)

				{

					vecValidWorkflowTemplateKey.add(strTempWorkflowTemplateKey);

				}

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::validateTriggerData - " + e.toString(), e);

        }



		return vecValidWorkflowTemplateKey;

	}



	

	//from the vector trigger get the trigger that relate to the strTempWorkflowTemplateKey only in sorted order!!!



	/**

	 * get the trigger associated with the given workflow template key

	 *

	 * @param vecTrigger				the vector that contains all triggers

	 * @param strWorkflowTemplateKey	the workflow template key

	 */

	public static Vector filterTrigger(Vector vecTrigger, String strWorkflowTemplateKey)

	{

		Vector vecFilteredTrigger = new Vector();

		

		try

		{

			Enumeration enumTrigger = vecTrigger.elements();

			while(enumTrigger.hasMoreElements())

			{

				Trigger trigger = (Trigger) enumTrigger.nextElement();



				if(trigger.getWorkflowTemplateKey().equals(strWorkflowTemplateKey))

				{

					if(trigger.getConnection1() == null || trigger.getConnection1().length() == 0)

					{

						//always add it to the very beginning if connection1 == null OR connection1 length is 0

						vecFilteredTrigger.add(0,trigger);

					}

					else

					{

						vecFilteredTrigger.add(trigger);

					}

				}

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::filterTrigger - " + e.toString(), e);

        }



		return vecFilteredTrigger;

	}





	/**

	 * check trigger with runtime data

	 *

	 * @param trigger				the trigger

	 * @param runtimeData			the runtimeData

	 *

	 * @return true if the trigger is valid else false 

	 */

	private static boolean checkTrigger(Trigger trigger, ChannelRuntimeData runtimeData)

	{

		boolean blReturnValue = true;



		try

		{

			String strField = trigger.getField();

			String strType = trigger.getType();

			//String strConnection1 = trigger.getConnection1();

			String strOperator1 = trigger.getOperator1();

			String strValue1 = trigger.getValue1();

			String strConnection2 = trigger.getConnection2();

			String strOperator2 = trigger.getOperator2();

			String strValue2 = trigger.getValue2();



//System.err.println("checkTrigger - strField = " + strField);

//System.err.println("checkTrigger - runtimeData = " + runtimeData);



			String strRuntimeValue = runtimeData.getParameter(strField);



//System.err.println("strRuntimeValue = >>>" + strRuntimeValue + "<<<");

			

			if(strRuntimeValue == null || strRuntimeValue.length() == 0)

			{

				blReturnValue = false;

				return blReturnValue;

			}



			if(strType.equals(DATA_TYPE_STRING))

			{

				int intCompareToValue = strValue1.compareToIgnoreCase(strRuntimeValue);

				boolean blResult1 = true;

				boolean blResult2 = true;

			

				//checking all type of operators

				if(strOperator1.equals(DATA_OPERATOR_EQUAL))

				{

					if(intCompareToValue != 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_NOT_EQUAL))

				{

					if(intCompareToValue == 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN))

				{

					if(intCompareToValue <= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN))

				{

					if(intCompareToValue >= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue < 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue > 0)

					{

						blResult1 = false;

					}

				}



				//if connection2 is available the do the checking too

				if(strConnection2 != null && strConnection2.length() != 0)

				{

					intCompareToValue = strValue2.compareToIgnoreCase(strRuntimeValue);



					if(strOperator2.equals(DATA_OPERATOR_EQUAL))

					{

						if(intCompareToValue != 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_NOT_EQUAL))

					{

						if(intCompareToValue == 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN))

					{

						if(intCompareToValue <= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN))

					{

						if(intCompareToValue >= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue < 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue > 0)

						{

							blResult2 = false;

						}

					}



					if(strConnection2.equals(DATA_CONNECTOR_AND))

					{

						if(!(blResult1 && blResult2))

						{

							blReturnValue = false;

						}

					}

					else if(strConnection2.equals(DATA_CONNECTOR_OR))

					{

						if(!(blResult1 || blResult2))

						{

							blReturnValue = false;

						}

					}

				}



				//if connection2 is not available that means the return value is the same with result1

				if(strConnection2 == null || strConnection2.length() == 0)

				{

					blReturnValue = blResult1;

				}

			}

			else if(strType.equals(DATA_TYPE_DATE))

			{

				String strYear = strRuntimeValue.substring(6); 

				String strMonth = strRuntimeValue.substring(3,5);

				String strDay = strRuntimeValue.substring(0,2);

				String strRuntimeDate = strYear + strMonth + strDay;



				strYear = strValue1.substring(6); 

				strMonth = strValue1.substring(3,5);

				strDay = strValue1.substring(0,2);

				String strValue1Date = strYear + strMonth + strDay;

				

				Integer intRuntimeDate = Integer.valueOf(strRuntimeDate);

				Integer intValue1Date = Integer.valueOf(strValue1Date);

				

				int intCompareToValue = intValue1Date.compareTo(intRuntimeDate);

				boolean blResult1 = true;

				boolean blResult2 = true;

		

//System.err.println("strRuntimeDate = " + strRuntimeDate);

//System.err.println("strValue1Date = " + strValue1Date);

//System.err.println("intRuntimeDate = " + intRuntimeDate);

//System.err.println("intValue1Date = " + intValue1Date);

//System.err.println("intCompareToValue = " + intCompareToValue);

//System.err.println("strOperator1 = " + strOperator1);

//System.err.println("strOperator2 = " + strOperator2);

				

				//checking all type of operators

				if(strOperator1.equals(DATA_OPERATOR_EQUAL))

				{

					if(intCompareToValue != 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_NOT_EQUAL))

				{

					if(intCompareToValue == 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN))

				{

					if(intCompareToValue <= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN))

				{

					if(intCompareToValue >= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue < 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue > 0)

					{

						blResult1 = false;

					}

				}



				//if connection2 is available the do the checking too

				if(strConnection2 != null && strConnection2.length() != 0)

				{

					strYear = strValue2.substring(6); 

					strMonth = strValue2.substring(3,5);

					strDay = strValue2.substring(0,2);

					String strValue2Date = strYear + strMonth + strDay;

					

					Integer intValue2Date = Integer.valueOf(strValue2Date);

					

					intCompareToValue = intValue2Date.compareTo(intRuntimeDate);



					if(strOperator2.equals(DATA_OPERATOR_EQUAL))

					{

						if(intCompareToValue != 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_NOT_EQUAL))

					{

						if(intCompareToValue == 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN))

					{

						if(intCompareToValue <= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN))

					{

						if(intCompareToValue >= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue < 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue > 0)

						{

							blResult2 = false;

						}

					}



					if(strConnection2.equals(DATA_CONNECTOR_AND))

					{

						if(!(blResult1 && blResult2))

						{

							blReturnValue = false;

						}

					}

					else if(strConnection2.equals(DATA_CONNECTOR_OR))

					{

						if(!(blResult1 || blResult2))

						{

							blReturnValue = false;

						}

					}

				}



				//if connection2 is not available that means the return value is the same with result1

				if(strConnection2 == null || strConnection2.length() == 0)

				{

					blReturnValue = blResult1;

				}

			}

			else if(strType.equals(DATA_TYPE_INTEGER))

			{

				Integer intRuntimeValue = Integer.valueOf(strRuntimeValue);

				Integer intValue1 = Integer.valueOf(strValue1);

				

				int intCompareToValue = intValue1.compareTo(intRuntimeValue);

				boolean blResult1 = true;

				boolean blResult2 = true;

			

				//checking all type of operators

				if(strOperator1.equals(DATA_OPERATOR_EQUAL))

				{

					if(intCompareToValue != 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_NOT_EQUAL))

				{

					if(intCompareToValue == 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN))

				{

					if(intCompareToValue <= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN))

				{

					if(intCompareToValue >= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue < 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue > 0)

					{

						blResult1 = false;

					}

				}



				//if connection2 is available the do the checking too

				if(strConnection2 != null && strConnection2.length() != 0)

				{

					Integer intValue2 = Integer.valueOf(strValue2);

					

					intCompareToValue = intValue2.compareTo(intRuntimeValue);



					if(strOperator2.equals(DATA_OPERATOR_EQUAL))

					{

						if(intCompareToValue != 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_NOT_EQUAL))

					{

						if(intCompareToValue == 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN))

					{

						if(intCompareToValue <= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN))

					{

						if(intCompareToValue >= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue < 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue > 0)

						{

							blResult2 = false;

						}

					}



					if(strConnection2.equals(DATA_CONNECTOR_AND))

					{

						if(!(blResult1 && blResult2))

						{

							blReturnValue = false;

						}

					}

					else if(strConnection2.equals(DATA_CONNECTOR_OR))

					{

						if(!(blResult1 || blResult2))

						{

							blReturnValue = false;

						}

					}

				}



				//if connection2 is not available that means the return value is the same with result1

				if(strConnection2 == null || strConnection2.length() == 0)

				{

					blReturnValue = blResult1;

				}

			}

			else if(strType.equals(DATA_TYPE_FLOAT))

			{

				Float flRuntimeValue = Float.valueOf(strRuntimeValue);

				Float flValue1 = Float.valueOf(strValue1);

				

				int intCompareToValue = flValue1.compareTo(flRuntimeValue);

				boolean blResult1 = true;

				boolean blResult2 = true;

			

				//checking all type of operators

				if(strOperator1.equals(DATA_OPERATOR_EQUAL))

				{

					if(intCompareToValue != 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_NOT_EQUAL))

				{

					if(intCompareToValue == 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN))

				{

					if(intCompareToValue <= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN))

				{

					if(intCompareToValue >= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue < 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue > 0)

					{

						blResult1 = false;

					}

				}



				//if connection2 is available the do the checking too

				if(strConnection2 != null && strConnection2.length() != 0)

				{

					Float flValue2 = Float.valueOf(strValue2);

					

					intCompareToValue = flValue2.compareTo(flRuntimeValue);



					if(strOperator2.equals(DATA_OPERATOR_EQUAL))

					{

						if(intCompareToValue != 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_NOT_EQUAL))

					{

						if(intCompareToValue == 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN))

					{

						if(intCompareToValue <= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN))

					{

						if(intCompareToValue >= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue < 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue > 0)

						{

							blResult2 = false;

						}

					}



					if(strConnection2.equals(DATA_CONNECTOR_AND))

					{

						if(!(blResult1 && blResult2))

						{

							blReturnValue = false;

						}

					}

					else if(strConnection2.equals(DATA_CONNECTOR_OR))

					{

						if(!(blResult1 || blResult2))

						{

							blReturnValue = false;

						}

					}

				}



				//if connection2 is not available that means the return value is the same with result1

				if(strConnection2 == null || strConnection2.length() == 0)

				{

					blReturnValue = blResult1;

				}

			}

			else if(strType.equals(DATA_TYPE_DOUBLE))

			{

				Double dblRuntimeValue = Double.valueOf(strRuntimeValue);

				Double dblValue1 = Double.valueOf(strValue1);

				

				int intCompareToValue = dblValue1.compareTo(dblRuntimeValue);

				boolean blResult1 = true;

				boolean blResult2 = true;

			

				//checking all type of operators

				if(strOperator1.equals(DATA_OPERATOR_EQUAL))

				{

					if(intCompareToValue != 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_NOT_EQUAL))

				{

					if(intCompareToValue == 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN))

				{

					if(intCompareToValue <= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN))

				{

					if(intCompareToValue >= 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue < 0)

					{

						blResult1 = false;

					}

				}

				else if(strOperator1.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

				{

					if(intCompareToValue > 0)

					{

						blResult1 = false;

					}

				}



				//if connection2 is available the do the checking too

				if(strConnection2 != null && strConnection2.length() != 0)

				{

					Double dblValue2 = Double.valueOf(strValue2);

					

					intCompareToValue = dblValue2.compareTo(dblRuntimeValue);



					if(strOperator2.equals(DATA_OPERATOR_EQUAL))

					{

						if(intCompareToValue != 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_NOT_EQUAL))

					{

						if(intCompareToValue == 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN))

					{

						if(intCompareToValue <= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN))

					{

						if(intCompareToValue >= 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_GREATER_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue < 0)

						{

							blResult2 = false;

						}

					}

					else if(strOperator2.equals(DATA_OPERATOR_LESS_THAN_AND_EQUAL_TO))

					{

						if(intCompareToValue > 0)

						{

							blResult2 = false;

						}

					}



					if(strConnection2.equals(DATA_CONNECTOR_AND))

					{

						if(!(blResult1 && blResult2))

						{

							blReturnValue = false;

						}

					}

					else if(strConnection2.equals(DATA_CONNECTOR_OR))

					{

						if(!(blResult1 || blResult2))

						{

							blReturnValue = false;

						}

					}

				}



				//if connection2 is not available that means the return value is the same with result1

				if(strConnection2 == null || strConnection2.length() == 0)

				{

					blReturnValue = blResult1;

				}

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::checkTrigger - " + e.toString(), e);

        }



		return blReturnValue;

	}



	/**

	 * get all available triggers for a given action and workflow template key

	 *

	 * @param strAction	the action/activity

	 * @param strWorkflowTemplateKey 	the workflow template key

	 * @param query		the query object

	 *

	 * @return all available triggers for a given action and workflow template key

	 */

	private static Vector getTrigger(String strAction, String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		Vector vecTrigger = new Vector();



		try

		{

			Vector vtDomainDetails = new Vector(DatabaseSchema.getFormFields("workflow_trigger_details"));



			query.reset();

			query.setDomain(WORKFLOW_TRIGGER, null, null, null);

			vtDomainDetails.add("WORKFLOW_TRIGGER_intTriggerKey");

			query.setFields(vtDomainDetails, null);

			query.setWhere(null, 0, "WORKFLOW_TRIGGER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);



			if(strAction != null && strAction.length() != 0)

			{

				query.setWhere("AND", 0, "WORKFLOW_TRIGGER_strAction", "=", strAction, 0, DALQuery.WHERE_HAS_VALUE);

			}

			else if(strWorkflowTemplateKey != null && strWorkflowTemplateKey.length() != 0)

			{

				query.setWhere("AND", 0, "WORKFLOW_TRIGGER_intWorkflowTemplateKey", "=",

							   strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			}



			query.setOrderBy("WORKFLOW_TRIGGER_intTriggerKey", "DESC");

			

            ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				Trigger trigger = new Trigger();	

				trigger.setKey(rsResultSet.getString("WORKFLOW_TRIGGER_intTriggerKey"));

				trigger.setId(rsResultSet.getString("WORKFLOW_TRIGGER_strTriggerXPDLId"));

				trigger.setName(rsResultSet.getString("WORKFLOW_TRIGGER_strName"));

				trigger.setDomain(rsResultSet.getString("WORKFLOW_TRIGGER_strDomain"));

				trigger.setAction(rsResultSet.getString("WORKFLOW_TRIGGER_strAction"));

				trigger.setField(rsResultSet.getString("WORKFLOW_TRIGGER_strField"));

				trigger.setType(rsResultSet.getString("WORKFLOW_TRIGGER_strType"));

				trigger.setConnection1(rsResultSet.getString("WORKFLOW_TRIGGER_strConnection1"));

				trigger.setOperator1(rsResultSet.getString("WORKFLOW_TRIGGER_strOperator1"));

				trigger.setValue1(rsResultSet.getString("WORKFLOW_TRIGGER_strValue1"));

				trigger.setConnection2(rsResultSet.getString("WORKFLOW_TRIGGER_strConnection2"));

				trigger.setOperator2(rsResultSet.getString("WORKFLOW_TRIGGER_strOperator2"));

				trigger.setValue2(rsResultSet.getString("WORKFLOW_TRIGGER_strValue2"));

				trigger.setWorkflowTemplateKey(rsResultSet.getString("WORKFLOW_TRIGGER_intWorkflowTemplateKey"));

				trigger.setWorkflowPackageKey(rsResultSet.getString("WORKFLOW_TRIGGER_intWorkflowPackageKey"));

				vecTrigger.add(trigger);

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTrigger - " + e.toString(), e);

        }



		return vecTrigger;

	}

        /**
         * get the workflow domains from the database
         * 
         * @param query the query object
         * 
         * @return Hashtable containing all the workflow domains
         */        
        protected static Hashtable getWorkflowDomains(DALSecurityQuery query)
        {
            Hashtable hashDomains = new Hashtable(10);
            try
            {
                Vector vtDomainDetails = new Vector(DatabaseSchema.getFormFields("workflow_domain_details"));

                query.reset();
                query.setDomain("WORKFLOW_DOMAIN", null, null, null);
                query.setFields(vtDomainDetails, null);
                query.setOrderBy("WORKFLOW_DOMAIN_intWorkflowDomainKey", "ASC");
                ResultSet rs = query.executeSelect();

                while (rs.next())
                {
                    String domainKey  = rs.getString("WORKFLOW_DOMAIN_intWorkflowDomainKey");
                    String domainName = rs.getString("WORKFLOW_DOMAIN_strWorkflowDomainNameId");
                    hashDomains.put(domainKey, domainName);
                }
            }
            catch(Exception e)
            {
                LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::getWorkflowDomains - " + e.toString(), e);
            }
            return hashDomains;
        }
        
        /**
         * Get the trigger types from the database
         *
         * @pram query the query object
         *
         * @return the hashtable containing all the trigger types
         */        
        protected static Hashtable getTriggerTypes(DALSecurityQuery query) 
        {
            // Hashtable of domain <key> and trigger types <value>
            Hashtable hashTriggerTypes = new Hashtable(10);
            try
            {
                Vector vtTriggerTypeDetails = new Vector(DatabaseSchema.getFormFields("workflow_trigger_type_details"));                
                
                // extract all the domains first
                Hashtable hashDomains = getWorkflowDomains(query);          
                Enumeration key_enum = hashDomains.keys();
                
                // get the trigger types based on each domain
                while (key_enum.hasMoreElements())
                {
                    String domainKey  = (String) key_enum.nextElement();
                    String domainName = (String) hashDomains.get(domainKey);
                                        
                    query.reset();
                    query.setDomain("WORKFLOW_TRIGGER_TYPE", null, null, null);
                    query.setFields(vtTriggerTypeDetails, null);
                    query.setWhere(null, 0, "WORKFLOW_TRIGGER_TYPE_intWorkflowDomainKey", "=", domainKey, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setOrderBy("WORKFLOW_TRIGGER_TYPE_intTriggerTypeKey", "ASC");
                    ResultSet rs = query.executeSelect();

                    // Extract the trigger types for each domain
                    Vector vtTriggerTypes = new Vector(10, 5);                
                    while (rs.next())
                    {
                        String triggerName  = rs.getString("WORKFLOW_TRIGGER_TYPE_strName");
                        String triggerField = rs.getString("WORKFLOW_TRIGGER_TYPE_strField");
                        String triggerType  = rs.getString("WORKFLOW_TRIGGER_TYPE_strType");
                        vtTriggerTypes.add(new TriggerField(triggerField, triggerName, triggerType));
                    }

                    // add domain to Hashtable
                    hashTriggerTypes.put(domainName, vtTriggerTypes);
                }
            }
            catch(Exception e)
            {
                LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::getTriggerTypes - " + e.toString(), e);
            }
            return hashTriggerTypes;
        }
        

	/**

	 * persist the runtime data to the database

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param runtimeData				the runtimeData

	 * @param hashParameterType			the list of parameter type

	 * @param query						the query object

	 */

	public static void copyRuntimedata(String strWorkflowInstanceKey, ChannelRuntimeData runtimeData,

								   Hashtable hashParameterType, DALSecurityQuery query)

	{

		try

		{

			Enumeration enumRuntimeDataKeys = runtimeData.keys();

			while(enumRuntimeDataKeys.hasMoreElements())

			{

				String strRuntimeDataKey = (String) enumRuntimeDataKeys.nextElement();

				String strRuntimeDataValue = runtimeData.getParameter(strRuntimeDataKey);



				//System.err.println("strRuntimeDataKey = >>>" + strRuntimeDataKey + "<<<");

				//System.err.println("strRuntimeDataValue = >>>" + strRuntimeDataValue + "<<<");



				//skip runtime data that has a null value

				if(strRuntimeDataValue == null)

				{

					continue;

				}



				//remove the leading and trailing whitespaces

				strRuntimeDataValue.trim();



				String strType = (String) hashParameterType.get(strRuntimeDataKey);



				//this is to ensure that we only copy all the required parameter only NOT all the entries

				//in runtimeData

				if(strType != null)

				{

					Hashtable hashFieldData = new Hashtable();

					Vector vtDomainDetails = DatabaseSchema.getFormFields("workflow_runtimedata_details");



					query.reset();

				

					query.setDomain(WORKFLOW_RUNTIMEDATA, null, null, null);    

					hashFieldData.put("WORKFLOW_RUNTIMEDATA_strParameterName", strRuntimeDataKey);

					hashFieldData.put("WORKFLOW_RUNTIMEDATA_strParameterValue", strRuntimeDataValue);

					hashFieldData.put("WORKFLOW_RUNTIMEDATA_intWorkflowInstanceKey", strWorkflowInstanceKey);

					query.setFields(vtDomainDetails, hashFieldData);

					query.executeInsert();	

				}

			}

		}

		catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::copyRuntimedata - " + e.toString(), e);

        }

	}



	/**

	 * to get the workflow template parameter types for a given template key

	 *

	 * @param strWorkflowTemplateKey	the workflow template key

	 *

	 * @return the hashtable that contains the parameter name and type pair

	 */

	public static Hashtable getWorkflowTemplateParameterType(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		Hashtable hashParameterType = new Hashtable();



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TEMPLATE_PARAMETER, null, null, null);

			query.setField("WORKFLOW_TEMPLATE_PARAMETER_strName", "null", "hashtable");

			query.setField("WORKFLOW_TEMPLATE_PARAMETER_strType", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=",

								strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

		

			while(rsResultSet.next())

			{

				String strParameterName = rsResultSet.getString("WORKFLOW_TEMPLATE_PARAMETER_strName");

				String strParameterType = rsResultSet.getString("WORKFLOW_TEMPLATE_PARAMETER_strType");

				hashParameterType.put(strParameterName, strParameterType);

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTemplateParameterType - " + e.toString(), e);

        }



		return hashParameterType;

	}



	/**

	 * get the workflow template key for a given workflow instance key

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query					the query object

	 *

	 * @return the workflow template key

	 */

	public static String getWorkflowTemplateKey(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		String strWorkflowTemplateKey = null;

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_INSTANCE, null, null, null);

            query.setField("WORKFLOW_INSTANCE_intWorkflowTemplateKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",

								strWorkflowInstanceKey , 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strWorkflowTemplateKey = rsResultSet.getString("WORKFLOW_INSTANCE_intWorkflowTemplateKey");

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTemplateKey - " + e.toString(), e);

        }

		

		return strWorkflowTemplateKey;

	}

	

	/**

	 * get the list of parameter and its value pairs from the ix_workflow_runtimedata for a given workflow instace key

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query					the query object

	 *

	 * @return the list of parameter and its value pairs from the ix_workflow_runtimedata for a given workflow instace key

	 */

	public static ChannelRuntimeData getWorkflowRuntimedata(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		ChannelRuntimeData runtimeData = new ChannelRuntimeData();

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_RUNTIMEDATA, null, null, null);

            query.setField("WORKFLOW_RUNTIMEDATA_strParameterName", "null", "hashtable");

            query.setField("WORKFLOW_RUNTIMEDATA_strParameterValue", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_RUNTIMEDATA_intWorkflowInstanceKey", "=",

								strWorkflowInstanceKey , 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_RUNTIMEDATA_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



//System.err.println("agustian query = " + query.convertSelectQueryToString());

			

			while(rsResultSet.next())

			{

           		String strParameterName = rsResultSet.getString("WORKFLOW_RUNTIMEDATA_strParameterName");

           		String strParameterValue = rsResultSet.getString("WORKFLOW_RUNTIMEDATA_strParameterValue");

//System.err.println("strParameterName = " + strParameterName);

//System.err.println("strParameterValue = " + strParameterValue);

				runtimeData.setParameter(strParameterName, strParameterValue);

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowRuntimedata - " + e.toString(), e);

        }

		

		return runtimeData;

	}



	/**

	 * update workflow runtimedata for a given data and the target workflow instance key

	 *

	 * @param strParameterName				the parameter name

	 * @param strParameterValue				the parameter value

	 * @param strWorkflowInstanceKey		the sub workflow instance key

	 * @param query 			the query object

	 */

	protected static void updateWorkflowRuntimedata(String strParameterName,

													String strParameterValue,

													String strWorkflowInstanceKey,

													DALSecurityQuery query)

	{

		try

		{

			//only update if both the parameter name and value is not equals to null

			if(strParameterName != null && strParameterValue != null)

			{

				query.reset();



				query.setDomain(WORKFLOW_RUNTIMEDATA, null, null, null);

				query.setField("WORKFLOW_RUNTIMEDATA_strParameterValue", strParameterValue, "hashtable");

				query.setWhere(null, 0, "WORKFLOW_RUNTIMEDATA_intWorkflowInstanceKey", "=",

										strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_RUNTIMEDATA_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND", 0, "WORKFLOW_RUNTIMEDATA_strParameterName", "=", 

										strParameterName, 0, DALQuery.WHERE_HAS_VALUE);

				query.executeUpdate();

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateWorkflowRuntimedata - " + e.toString(), e);

        }

	}



	/**

	 * get the task transition type for a given origin task and target key

	 *

	 * @param strOriginTaskKey		the origin task key

	 * @param strTargetTaskKey		the target task key

	 * @param query the query object

	 *

	 * @return the task transition type

	 */

	protected static String getTaskTransitionType(String strOriginTaskKey, String strTargetTaskKey,

											 	  DALSecurityQuery query)

	{

		String strTaskTransitionType = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

            query.setField("WORKFLOW_TASK_TRANSITION_strType", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "=", strOriginTaskKey,

									0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskKey", "=", strTargetTaskKey,

									0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskTransitionType = rsResultSet.getString("WORKFLOW_TASK_TRANSITION_strType");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskTransitionType - " + e.toString(), e);

        }



		return strTaskTransitionType;

	}



	/**

	 * get reversed task transition map

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query					the query object

	 *

	 * @return the reversed task transition map

	 */

	protected static Hashtable getReversedTaskTransition(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		Hashtable hashReversedTaskTransition = new Hashtable();

		

		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK_TRANSITION, null, null, null);

            query.setField("WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "null", "hashtable");

            query.setField("WORKFLOW_TASK_TRANSITION_intTargetTaskKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskWorkflowInstanceKey", "=",

								strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intTargetTaskWorkflowInstanceKey", "=",

								strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

           		String strOriginTaskKey = rsResultSet.getString("WORKFLOW_TASK_TRANSITION_intOriginTaskKey");

           		String strTargetTaskKey = rsResultSet.getString("WORKFLOW_TASK_TRANSITION_intTargetTaskKey");

				Vector vecTargetTaskKey = null;

				if(hashReversedTaskTransition.get(strTargetTaskKey) == null)

				{

					vecTargetTaskKey = new Vector();

					vecTargetTaskKey.add(strOriginTaskKey);

					hashReversedTaskTransition.put(strTargetTaskKey, vecTargetTaskKey);

				}

				else

				{

					vecTargetTaskKey = (Vector) hashReversedTaskTransition.get(strTargetTaskKey);

					vecTargetTaskKey.add(strOriginTaskKey);

					hashReversedTaskTransition.put(strTargetTaskKey, vecTargetTaskKey);

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getReversedTaskTransition - " + e.toString(), e);

        }

		

		return hashReversedTaskTransition;

	}





	/**

	 * copy all task details to workflow task history table

	 *

	 * @param strWorkflowTaskKey	the workflow task key

	 * @param query					the query object

	 */

	protected static void copyWorkflowTaskToWorkflowTaskHistory(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		try

		{

			// get the workflow task and workflow task history field details

            Vector vecWorkflowTaskFormFields = new Vector(DatabaseSchema.getFormFields("workflow_task_details"));

            Vector vecWorkflowTaskHistoryFormFields = DatabaseSchema.getFormFields("workflow_task_history_details");



			query.reset();



			query.setDomain("WORKFLOW_TASK", null, null, null);

			vecWorkflowTaskFormFields.add("WORKFLOW_TASK_intTaskKey");

            query.setFields(vecWorkflowTaskFormFields, null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

            Vector vecResult = QueryChannel.lookupRecord(rsResultSet, vecWorkflowTaskFormFields);

          

			for(int intIndex = 0; intIndex < vecResult.size(); intIndex++)

            {

//System.err.println("vecResult = " + (Hashtable) vecResult.get(intIndex));



				Hashtable hashWorkflowTaskHistoryData = new Hashtable();

				Hashtable hashMapping = new Hashtable();

				WorkflowManager.buildWorkflowTaskAndWorkflowTaskHistoryMapping(hashMapping);

				WorkflowManager.transferData((Hashtable) vecResult.get(intIndex), hashWorkflowTaskHistoryData, hashMapping);



//System.err.println("vecWorkflowTaskHistoryFormFields = " + vecWorkflowTaskHistoryFormFields);

//System.err.println("hashWorkflowTaskHistoryData = " + hashWorkflowTaskHistoryData);



				query.reset();

				query.setDomain("WORKFLOW_TASK_HISTORY", null, null, null);

				query.setFields(vecWorkflowTaskHistoryFormFields, hashWorkflowTaskHistoryData);

				query.executeInsert();

            }

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::copyWorkflowTaskToWorkflowTaskHistory - " + e.toString(), e);

        }

	}



	/**

	 * build the mapping between workflow task and workflow task history 

	 *

	 * @param hashMapping 	the hashtable where the mapping will be inserted

	 */

	private static void buildWorkflowTaskAndWorkflowTaskHistoryMapping(Hashtable hashMapping)

	{

		try

		{

			hashMapping.put("WORKFLOW_TASK_strWorkflowActivityXPDLId", "WORKFLOW_TASK_HISTORY_strWorkflowActivityXPDLId");

			hashMapping.put("WORKFLOW_TASK_intTaskKey", "WORKFLOW_TASK_HISTORY_intWorkflowTaskKey");

			hashMapping.put("WORKFLOW_TASK_strName", "WORKFLOW_TASK_HISTORY_strName");

			hashMapping.put("WORKFLOW_TASK_strPriority", "WORKFLOW_TASK_HISTORY_strPriority");

			hashMapping.put("WORKFLOW_TASK_intPerformer", "WORKFLOW_TASK_HISTORY_intPerformer");

			hashMapping.put("WORKFLOW_TASK_strPerformerType", "WORKFLOW_TASK_HISTORY_strPerformerType");

			hashMapping.put("WORKFLOW_TASK_strReassignable", "WORKFLOW_TASK_HISTORY_strReassignable");

			hashMapping.put("WORKFLOW_TASK_strSignoff", "WORKFLOW_TASK_HISTORY_strSignoff");

			hashMapping.put("WORKFLOW_TASK_strInstruction", "WORKFLOW_TASK_HISTORY_strInstruction");

			hashMapping.put("WORKFLOW_TASK_intFunctionKey", "WORKFLOW_TASK_HISTORY_intFunctionKey");

			hashMapping.put("WORKFLOW_TASK_strIfFail", "WORKFLOW_TASK_HISTORY_strIfFail");

			hashMapping.put("WORKFLOW_TASK_strRetryDelayUnit", "WORKFLOW_TASK_HISTORY_strRetryDelayUnit");

			hashMapping.put("WORKFLOW_TASK_flRetryDelayValue", "WORKFLOW_TASK_HISTORY_flRetryDelayValue");

			hashMapping.put("WORKFLOW_TASK_intRetryCounter", "WORKFLOW_TASK_HISTORY_intRetryCounter");

			hashMapping.put("WORKFLOW_TASK_strIfFailAfterRetry", "WORKFLOW_TASK_HISTORY_strIfFailAfterRetry");

			hashMapping.put("WORKFLOW_TASK_intCompletedTask", "WORKFLOW_TASK_HISTORY_intCompletedTask");

			hashMapping.put("WORKFLOW_TASK_strMultiAction", "WORKFLOW_TASK_HISTORY_strMultiAction");

			hashMapping.put("WORKFLOW_TASK_strTriggerUnit", "WORKFLOW_TASK_HISTORY_strTriggerUnit");

			hashMapping.put("WORKFLOW_TASK_flTriggerValue", "WORKFLOW_TASK_HISTORY_flTriggerValue");

			hashMapping.put("WORKFLOW_TASK_strFinishedUnit", "WORKFLOW_TASK_HISTORY_strFinishedUnit");

			hashMapping.put("WORKFLOW_TASK_flFinishedValue", "WORKFLOW_TASK_HISTORY_flFinishedValue");

			hashMapping.put("WORKFLOW_TASK_strIntervalUnit", "WORKFLOW_TASK_HISTORY_strIntervalUnit");

			hashMapping.put("WORKFLOW_TASK_strIntervalValue", "WORKFLOW_TASK_HISTORY_strIntervalValue");

			hashMapping.put("WORKFLOW_TASK_strType", "WORKFLOW_TASK_HISTORY_strType");

			hashMapping.put("WORKFLOW_TASK_strStatus", "WORKFLOW_TASK_HISTORY_strStatus");

			hashMapping.put("WORKFLOW_TASK_intPosX", "WORKFLOW_TASK_HISTORY_intPosX");

			hashMapping.put("WORKFLOW_TASK_intPosY", "WORKFLOW_TASK_HISTORY_intPosY");

			hashMapping.put("WORKFLOW_TASK_intWidth", "WORKFLOW_TASK_HISTORY_intWidth");

			hashMapping.put("WORKFLOW_TASK_intHeight", "WORKFLOW_TASK_HISTORY_intHeight");

			hashMapping.put("WORKFLOW_TASK_dtDateReceived", "WORKFLOW_TASK_HISTORY_dtDateReceived");

			hashMapping.put("WORKFLOW_TASK_dtDateCompleted", "WORKFLOW_TASK_HISTORY_dtDateCompleted");

			hashMapping.put("WORKFLOW_TASK_strTimeReceived", "WORKFLOW_TASK_HISTORY_strTimeReceived");

			hashMapping.put("WORKFLOW_TASK_strTimeCompleted", "WORKFLOW_TASK_HISTORY_strTimeCompleted");

			hashMapping.put("WORKFLOW_TASK_strComments", "WORKFLOW_TASK_HISTORY_strComments");

			hashMapping.put("WORKFLOW_TASK_strAction", "WORKFLOW_TASK_HISTORY_strAction");

			hashMapping.put("WORKFLOW_TASK_intWorkingDays", "WORKFLOW_TASK_HISTORY_intWorkingDays");

			hashMapping.put("WORKFLOW_TASK_strSendTriggerAlertToPerformer",

							"WORKFLOW_TASK_HISTORY_strSendTriggerAlertToPerformer");

			hashMapping.put("WORKFLOW_TASK_intSendTriggerAlertToOtherKey",

							"WORKFLOW_TASK_HISTORY_intSendTriggerAlertToOtherKey");

			hashMapping.put("WORKFLOW_TASK_strSendTriggerAlertToOtherType",

							"WORKFLOW_TASK_HISTORY_strSendTriggerAlertToOtherType");

			hashMapping.put("WORKFLOW_TASK_strRecurringTriggerAlert",

							"WORKFLOW_TASK_HISTORY_strRecurringTriggerAlert");

			hashMapping.put("WORKFLOW_TASK_strRecurringTriggerAlertUnit",

							"WORKFLOW_TASK_HISTORY_strRecurringTriggerAlertUnit");

			hashMapping.put("WORKFLOW_TASK_flRecurringTriggerAlertValue",

							"WORKFLOW_TASK_HISTORY_flRecurringTriggerAlertValue");

			hashMapping.put("WORKFLOW_TASK_strSendFinishAlertToPerformer",

							"WORKFLOW_TASK_HISTORY_strSendFinishAlertToPerformer");

			hashMapping.put("WORKFLOW_TASK_intSendFinishAlertToOtherKey", 

						    "WORKFLOW_TASK_HISTORY_intSendFinishAlertToOtherKey");

			hashMapping.put("WORKFLOW_TASK_strSendFinishAlertToOtherType",

							"WORKFLOW_TASK_HISTORY_strSendFinishAlertToOtherType");

			hashMapping.put("WORKFLOW_TASK_strRecurringFinishAlert", "WORKFLOW_TASK_HISTORY_strRecurringFinishAlert");

			hashMapping.put("WORKFLOW_TASK_strRecurringFinishAlertUnit",

							"WORKFLOW_TASK_HISTORY_strRecurringFinishAlertUnit");

			hashMapping.put("WORKFLOW_TASK_flRecurringFinishAlertValue",

							"WORKFLOW_TASK_HISTORY_flRecurringFinishAlertValue");

			hashMapping.put("WORKFLOW_TASK_intWorkflowInstanceKey", "WORKFLOW_TASK_HISTORY_intWorkflowInstanceKey");

			hashMapping.put("WORKFLOW_TASK_intSubWorkflowInstanceKey", "WORKFLOW_TASK_HISTORY_intSubWorkflowInstanceKey");

                        hashMapping.put("WORKFLOW_TASK_strThread", "WORKFLOW_TASK_HISTORY_strThread");

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::buildWorkflowTaskAndWorkflowTaskHistoryMapping - " +

					e.toString(), e);

        }

	}



	/**

	 * reset a workflow task 

	 *

	 * @param strWorkflowTaskKey	the workflow task key

	 * @param query					the query object

	 */

	protected static void resetWorkflowTask(String strWorkflowTaskKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField(WORKFLOW_TASK + "_strStatus", TASK_STATUS_NOT_ACTIVE);

			query.setField(WORKFLOW_TASK + "_dtDateReceived", null);

			query.setField(WORKFLOW_TASK + "_dtDateCompleted", null);

			query.setField(WORKFLOW_TASK + "_strTimeReceived", null);

			query.setField(WORKFLOW_TASK + "_strTimeCompleted", null);

			query.setField(WORKFLOW_TASK + "_strComments", null);

			query.setField(WORKFLOW_TASK + "_strAction", null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", 

							strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::resetWorkflowTask - " + e.toString(), e);

        }

	}



	/**

	 * reset a workflow instance

	 *

	 * @param strWorkflowInstanceKey	the workflow instance key

	 * @param query					the query object

	 */

	protected static void resetWorkflowInstance(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_INSTANCE, null, null, null);    

			query.setField(WORKFLOW_INSTANCE + "_dtDateCompleted", null);

			query.setField(WORKFLOW_INSTANCE + "_strStatus", WORKFLOW_INSTANCE_STATUS_NOT_STARTED);

			query.setWhere(null, 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=", 

							strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::resetWorkflowInstance - " + e.toString(), e);

        }

	}



	/**

	 * get the workflow package key for a given workflow template key

	 *

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 *

	 * @return the workflow package key

	 */

	protected static String getWorkflowPackageKeyGivenWorkflowTemplateKey(String strWorkflowTemplateKey,

																		  DALSecurityQuery query)

	{

		String strWorkflowPackageKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_PROCESS, null, null, null);

			query.setField("WORKFLOW_TEMPLATE_intWorkflowPackageKey", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

					       strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

                  

            if(rsResultSet.next())

			{

            	strWorkflowPackageKey = rsResultSet.getString("WORKFLOW_TEMPLATE_intWorkflowPackageKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowPackageKey - " + e.toString(), e);

        }



		return strWorkflowPackageKey;

	}



	/**

	 * get the workflow template name for a given workflow template key

	 *

	 * @param strWorkflowTemplateKey the workflow template key

	 * @param query the query object

	 *

	 * @return the workflow package key

	 */

	public static String getWorkflowTemplateName(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		String strWorkflowTemplateName = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_PROCESS, null, null, null);

			query.setField("WORKFLOW_TEMPLATE_strName", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

					       strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

                  

            if(rsResultSet.next())

			{

            	strWorkflowTemplateName = rsResultSet.getString("WORKFLOW_TEMPLATE_strName");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTemplateName - " + e.toString(), e);

        }



		return strWorkflowTemplateName;

	}



	/**

	 * get the workflow package name for a given workflow package key

	 *

	 * @param strWorkflowPackageKey the workflow package key

	 * @param query the query object

	 *

	 * @return the workflow package key

	 */

	protected static String getWorkflowPackageName(String strWorkflowPackageKey, DALSecurityQuery query)

	{

		String strWorkflowPackageName = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_PACKAGE, null, null, null);

			query.setField("WORKFLOW_PACKAGE_strName", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_PACKAGE_intWorkflowPackageKey", "=",

					       strWorkflowPackageKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_PACKAGE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

                  

            if(rsResultSet.next())

			{

            	strWorkflowPackageName = rsResultSet.getString("WORKFLOW_PACKAGE_strName");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::strWorkflowPackageName - " + e.toString(), e);

        }



		return strWorkflowPackageName;

	}





	/**

	 * get workflow template status 

	 *

	 * @param strWorkflowTemplateKey	the workflow template key

	 * @param query		the query object

	 *

	 * @return	the workflow template status 

	 */

	protected static String getWorkflowTemplateStatus(String strWorkflowTemplateKey, DALSecurityQuery query)

	{

		String strWorkflowTemplateStatus = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_PROCESS, null, null, null);

            query.setField("WORKFLOW_TEMPLATE_strStatus", "null", "hashtable");

			query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=",

									strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strWorkflowTemplateStatus = rsResultSet.getString("WORKFLOW_TEMPLATE_strStatus");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getWorkflowTemplateStatus - " + e.toString(), e);

        }



		return strWorkflowTemplateStatus;

	}





	/**

     * Assemble the message body of the email message

     */

    public static String getMessageBody(String strMessageTemplate,

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

					"Unknown error in WorkflowManager::getMessageBody - " + e.toString(), e);

        }

        

		return strMessageResult;

    }



    /**

     * Retrieve the Email template from Configuration table

	 *

	 * @param strType	the template type

	 *

	 * @retun the email template

     */

    public static String getEmailTemplate(String strType)

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

					"Unknown error in WorkflowManager::getEmailTemplate - " + e.toString(), e);

        }



		return strEmailTemplate;

    }



	/**

	 * to insert an entry to the ix_workflow_instance_to_domain table 

	 *

	 * @param strWorkflowInstanceKey 	the workflow instance key

	 * @param strDomain					the domain name

	 * @param strDomainKey				the domain key

	 * @param query						the query object

	 */

	protected static void insertIntoWorkflowInstanceToDomain(String strWorkflowInstanceKey, String strDomain,

															 String strDomainKey, DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain("WORKFLOW_INSTANCE_TO_DOMAIN", null, null, null);  

			query.setField("WORKFLOW_INSTANCE_TO_DOMAIN_intWorkflowInstanceKey", strWorkflowInstanceKey);

			query.setField("WORKFLOW_INSTANCE_TO_DOMAIN_strDomain", strDomain);

			query.setField("WORKFLOW_INSTANCE_TO_DOMAIN_intDomainKey", strDomainKey);

			query.executeInsert();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::insertIntoWorkflowInstanceToDomain - " + e.toString(), e);

        }

	}



	/**

	 * get runtime data from the database for a given workflow instance key

	 *

	 * @param strWorkflowInstanceKey 	the workflow instance key

	 * @param query						the query object

	 *

	 * @return the ResultSet object that encapsulates the data being queried 

	 */

	public static ResultSet getRuntimeData(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		ResultSet rsRuntimeData = null;



		try

		{

            Vector vecWorkflowRuntimeDataFormFields =

								DatabaseSchema.getFormFields("workflow_runtimedata_details");



			query.reset();



			query.setDomain("WORKFLOW_RUNTIMEDATA", null, null, null);

            query.setFields(vecWorkflowRuntimeDataFormFields, null);

			query.setWhere(null, 0, "WORKFLOW_RUNTIMEDATA_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_RUNTIMEDATA_intWorkflowInstanceKey",

									"=", strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			

			rsRuntimeData = query.executeSelect();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getRuntimeData - " + e.toString(), e);

        }



		return rsRuntimeData;

	}



	/**

	 * sort the task keys, put the stop task at the end of the list

	 *

	 * @param vecTaskKeys	the task keys

	 * @param query			the query object

	 *

	 * @return the sorted task keys

	 */

	protected static Vector sortTaskKeys(Vector vecTaskKeys, DALSecurityQuery query)

	{

		Vector vecSortedTaskKeys = new Vector();



		try

		{

			Enumeration enumSortedTaskKeys = vecTaskKeys.elements();



			String strTaskKey = null;

			String strTaskType = null;

			while(enumSortedTaskKeys.hasMoreElements())

			{

				strTaskKey = (String) enumSortedTaskKeys.nextElement();

				strTaskType = WorkflowManager.getTaskType(strTaskKey, query);

				

				if(strTaskType.equals(WORKFLOW_TASK_TYPE_STOP))

				{

					vecSortedTaskKeys.add(strTaskKey);

				}

				else

				{

					//add the key at the first position of the vector

					vecSortedTaskKeys.add(0,strTaskKey);

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::sortTaskKeys - " + e.toString(), e);

        }



		return vecSortedTaskKeys;

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

	 * @param strSendEmail			to determine whether an email should be send or not

	 */

	public static DALQuery createAlert(String strName, String strDescription, String strTargetUserKey,

									  String strTargetUserType, String strType, String strWorkflowTaskKey, 

									  String strSendEmail)

	{

		DALQuery query = new DALQuery();



		try

		{

			Calendar currentTime = Calendar.getInstance();

			String strDate = getDate(currentTime) + "/" + 

						     getMonth(currentTime) + "/" + 

						     currentTime.get(Calendar.YEAR);



			String strCurrentTime = currentTime.get(Calendar.HOUR_OF_DAY) + ":" +

									currentTime.get(Calendar.MINUTE) + ":" +

									currentTime.get(Calendar.SECOND);



			//DALQuery query = new DALQuery();



			query.setDomain("ALERT", null, null, null);

			query.setField("ALERT_strName", strName);

			query.setField("ALERT_strDescription", strDescription);

			query.setField("ALERT_strOriginUserType", "ALERT_ENGINE");

			query.setField("ALERT_intTargetUserKey", strTargetUserKey);

			query.setField("ALERT_strTargetUserType", strTargetUserType);

			query.setField("ALERT_dtDate", strDate);

			query.setField("ALERT_strTime", strCurrentTime);

			query.setField("ALERT_strStatus", "Sent");



			if(strSendEmail.equals("yes"))

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

					"Unknown error in WorkflowManager::createAlert - " + e.toString(), e);

        }



		return query;

	}





	/**

	 * update the alert email sent 

	 *

	 * @param	strAlertKey		the alert key

	 * @param	strEmailSent 	the alert email sent

	 */

	public static void updateAlertEmailSent(String strAlertKey, String strEmailSent)

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

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::updateAlertEmailSent " + e.toString(), e);

        }

	}





	/**

	 * insert a new record to ix_workflow_function_thread or increment the number of execution time,

	 * as part of the running of a new thread functionality

	 *

	 * @param	strWorkflowTaskKey		the workflow task key

	 * @param	strWorkflowInstanceKey	the workflow instance key

	 * @param	strStatus				the thread status

	 */

	public static void addWorkflowFunctionThread(String strWorkflowTaskKey, String strWorkflowInstanceKey,

													String strStatus)

	{

		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

			query.setField("WORKFLOW_FUNCTION_THREAD_intWorkflowFunctionThreadKey", null);

			query.setField("WORKFLOW_FUNCTION_THREAD_intExecuted", null);

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowInstanceKey", "=", 

							strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", "=", 

							strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsRecordExist = query.executeSelect();



			boolean blRecordExist = false;

			int intNoOfTimesExecuted = 0;

			if(rsRecordExist.next())

			{

				blRecordExist = true;

				intNoOfTimesExecuted = rsRecordExist.getInt("WORKFLOW_FUNCTION_THREAD_intExecuted");

			}



			if(blRecordExist)

			{

				intNoOfTimesExecuted++;



				query.reset();

				query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

				query.setField("WORKFLOW_FUNCTION_THREAD_intExecuted", String.valueOf(intNoOfTimesExecuted));

				query.setField("WORKFLOW_FUNCTION_THREAD_strStatus", strStatus);

				query.setWhere(null, 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowInstanceKey", "=", 

							   strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", "=", 

							   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

				query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

				query.executeUpdate();

			}

			else

			{

				query.reset();

				query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

				query.setField("WORKFLOW_FUNCTION_THREAD_intWorkflowInstanceKey", strWorkflowInstanceKey);

				query.setField("WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", strWorkflowTaskKey);

				query.setField("WORKFLOW_FUNCTION_THREAD_intExecuted", "1");

				query.setField("WORKFLOW_FUNCTION_THREAD_strStatus", strStatus);

				query.executeInsert();

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::addWorkflowFunctionThread " + e.toString(), e);

        }

	}





	/**

	 * update thread status for a given workflow task key and workflow instance key

	 *

	 * @param	strWorkflowTaskKey		the workflow task key

	 * @param	strWorkflowInstanceKey	the workflow instance key

	 * @param	strStatus				the thread status

	 */

	public static void updateWorkflowFunctionThreadStatus(String strWorkflowTaskKey, String strWorkflowInstanceKey,

													String strStatus)

	{

		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

			query.setField("WORKFLOW_FUNCTION_THREAD_strStatus", strStatus);

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowInstanceKey", "=", 

							strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", "=", 

							strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::updateWorkflowFunctionThreadStatus " + e.toString(), e);

        }

	}



	/**

	 * reset thread no of execution for a given workflow task key and workflow instance key

	 *

	 * @param	strWorkflowTaskKey		the workflow task key

	 * @param	strWorkflowInstanceKey	the workflow instance key

	 * @param	strStatus				the thread status

	 */

	public static void resetWorkflowFunctionThreadNoOfExecution(String strWorkflowTaskKey, 

																String strWorkflowInstanceKey,

																String strStatus)

	{

		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

			query.setField("WORKFLOW_FUNCTION_THREAD_intExecuted", "0");

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowInstanceKey", "=", 

							strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", "=", 

							strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::resetWorkflowFunctionThreadNoOfExecution " + e.toString(), e);

        }

	}



	/**

	 * delete function thread for a given workflow task key and workflow instance key

	 *

	 * @param	strWorkflowTaskKey		the workflow task key

	 * @param	strWorkflowInstanceKey	the workflow instance key

	 */

	public static void deleteWorkflowFunctionThread(String strWorkflowTaskKey, String strWorkflowInstanceKey)

	{

		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

			query.setField("WORKFLOW_FUNCTION_THREAD_intDeleted", "-1");

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowInstanceKey", "=", 

							strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);



			if(strWorkflowTaskKey != null)

			{

				query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", "=", 

								strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			}



			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::deleteWorkflowFunctionThread " + e.toString(), e);

        }

	}



	/**

	 * count how many times have the thread function being executed

	 *

	 * @param	strWorkflowTaskKey		the workflow task key

	 * @param	strWorkflowInstanceKey	the workflow instance key

	 *

	 * @return the number of time the thread function being executed

	 */

	public static int countNumberOfWorkflowFunctionThreadExecuted(String strWorkflowTaskKey,

																	 String strWorkflowInstanceKey)

	{

		int intNoOfExecuted = -1;



		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

			query.setField("WORKFLOW_FUNCTION_THREAD_intExecuted", null);

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowInstanceKey", "=", 

							strWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", "=", 

							strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

				intNoOfExecuted = rsResultSet.getInt("WORKFLOW_FUNCTION_THREAD_intExecuted");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::countNumberOfWorkflowFunctionThreadExecuted " + e.toString(), e);

        }



		return intNoOfExecuted;

	}



	/**

	 * get list of threads that needs to be restarted

	 *

	 * @return a Vector of task keys

	 */

	public static Vector getRestartRequiredTaskThread()

	{

		Vector vecTaskKeys = new Vector();



		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_FUNCTION_THREAD, null, null, null);

			query.setField("WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey", null);

			query.setWhere(null, 0, "WORKFLOW_FUNCTION_THREAD_strStatus", "=",

									WorkflowManager.WORKFLOW_FUNCTION_THREAD_RUNNING, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND" , 0, "WORKFLOW_FUNCTION_THREAD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();



			while(rsResultSet.next())

			{

				vecTaskKeys.add(rsResultSet.getString("WORKFLOW_FUNCTION_THREAD_intWorkflowTaskKey"));

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::getRestartRequiredTaskThread " + e.toString(), e);

        }



		return vecTaskKeys;

	}



	/**

	 * get the workflow task start date for a given workflow task key

	 *

	 * @param strWorkflowTaskKey	the workflow task key

	 *

	 * @return the task start date

	 */

	public static String getWorkflowTaskStartDate(String strWorkflowTaskKey)

	{

		String strStartDate = null;



		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_dtDateReceived", null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strStartDate = rsResultSet.getString("WORKFLOW_TASK_dtDateReceived");

			}



		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::getWorkflowTaskStartDate " + e.toString(), e);

        }



		return strStartDate;

	}



	/**

	 * get the workflow task start time for a given workflow task key

	 *

	 * @param strWorkflowTaskKey	the workflow task key

	 *

	 * @return the task start time 

	 */

	public static String getWorkflowTaskStartTime(String strWorkflowTaskKey)

	{

		String strStartTime = null;



		try

		{

			DALQuery query = new DALQuery();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_strTimeReceived", null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strStartTime = rsResultSet.getString("WORKFLOW_TASK_strTimeReceived");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in WorkflowManager::getWorkflowTaskStartTime " + e.toString(), e);

        }



		return strStartTime;

	}



	/**

	 * check whether is there any active tasks for a given workflow instances

	 *

	 * @param strWorkflowInstanceKey 	the workflow instance key

	 * @param query						the query object

	 *

	 * @return true if there is any active tasks else return false

	 */

	public static boolean haveActiveTasks(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

		boolean blResultValue = false;



		try

		{

			Vector vecWorkflowTaskKeys = WorkflowManager.getWorkflowTaskKeys(strWorkflowInstanceKey, query);

			Enumeration enumWorkflowTaskKeys = vecWorkflowTaskKeys.elements();



			while(enumWorkflowTaskKeys.hasMoreElements())

			{

				String strWorkflowTaskKey = (String) enumWorkflowTaskKeys.nextElement();

				String strWorkflowTaskStatus = WorkflowManager.getTaskStatus(strWorkflowTaskKey, query);

				String strWorkflowTaskType = WorkflowManager.getTaskType(strWorkflowTaskKey, query);



				if(!(strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_STOP) ||

					 strWorkflowTaskType.equals(WorkflowManager.WORKFLOW_TASK_TYPE_START)) &&

					 (strWorkflowTaskStatus.equals(WorkflowManager.TASK_STATUS_ACTIVE) ||

					  strWorkflowTaskStatus.equals(TASK_STATUS_STARTED)))

				{

					blResultValue = true;

					break;

				}

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::haveActiveTasks - " + e.toString(), e);

        }



		return blResultValue;

	}



	/**

	 * get org user details for a given org user key

	 *

	 * @param strOrgUserKey 			the org user key

	 *

	 * @return the ResultSet object that encapsulates the data being queried 

	 */

	public static ResultSet getOrgUserDetails(String strOrgUserKey)

	{

		ResultSet rsOrgUserDetails = null;



		try

		{

            Vector vecOrgUserFormFields =

								DatabaseSchema.getFormFields("orguser_details");



			DALQuery query = new DALQuery();



			query.setDomain("ORGUSER", null, null, null);

            query.setFields(vecOrgUserFormFields, null);

			query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "ORGUSER_intOrgUserKey",

									"=", strOrgUserKey, 0, DALQuery.WHERE_HAS_VALUE);

			

			rsOrgUserDetails = query.executeSelect();



			//move to the first(only) record

			rsOrgUserDetails.first();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getOrgUserDetails - " + e.toString(), e);

        }



		return rsOrgUserDetails;

	}



	/**

	 * get org group details for a given org group key

	 *

	 * @param strOrgGroupKey 			the org group key

	 *

	 * @return the ResultSet object that encapsulates the data being queried 

	 */

	public static ResultSet getOrgGroupDetails(String strOrgGroupKey)

	{

		ResultSet rsOrgGroupDetails = null;



		try

		{

            Vector vecOrgGroupFormFields =

								DatabaseSchema.getFormFields("orggroup_details");



			DALQuery query = new DALQuery();



			query.setDomain("ORGGROUP", null, null, null);

            query.setFields(vecOrgGroupFormFields, null);

			query.setWhere(null, 0, "ORGGROUP_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "ORGGROUP_intOrgGroupKey",

									"=", strOrgGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

			

			rsOrgGroupDetails = query.executeSelect();



			//move to the first(only) record

			rsOrgGroupDetails.first();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getOrgGroupDetails - " + e.toString(), e);

        }



		return rsOrgGroupDetails;

	}




	/**

	 * get the sub workflow template key 

	 *

	 * @param strWorkflowTemplateKey 		the workflow template key

	 * @param strWorkflowActivityXPDLId		the workflow activity xpdl id 

	 * @param query 						the query object

	 *

	 * @return	the sub workflow template key 

	 */

	protected static String getSubWorkflowTemplateKey(String strWorkflowTemplateKey, String strWorkflowActivityXPDLId,

													  DALSecurityQuery query)

	{

		String strSubWorkflowTemplateKey = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_ACTIVITY, null, null, null);

			query.setField("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey", null);

			query.setWhere(null, 0, "WORKFLOW_ACTIVITY_intWorkflowTemplateKey", "=", strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_strWorkflowActivityXPDLId", "=", strWorkflowActivityXPDLId, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			ResultSet rsResultSet = query.executeSelect();

			

			if(rsResultSet.next())

			{

				strSubWorkflowTemplateKey = rsResultSet.getString("WORKFLOW_ACTIVITY_intSubWorkflowTemplateKey");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getSubWorkflowTemplateKey(String strWorkflowTemplateKey, String strWorkflowActivityXPDLId, DALSecurityQuery query)" + e.toString(), e);

        }



		return strSubWorkflowTemplateKey;

	}




	/**

	 * update the workflow task handover status

	 *

	 * @param strStatus				the handover status

	 * @param strWorkflowTaskKey 	the workflow task key

	 * @param query 				the query object

	 */

	public static void updateWorkflowTaskHandoverStatus(String strStatus, String strWorkflowTaskKey,

										 			 	DALSecurityQuery query)

	{

		try

		{

			query.reset();

			

			query.setDomain(WORKFLOW_TASK, null, null, null);    

			query.setField("WORKFLOW_TASK_strHandover", strStatus);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=",

						   strWorkflowTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

			query.executeUpdate();

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::updateWorkflowTaskHandoverStatus - " + e.toString(), e);

        }

	}



	/**

	 * get task handover status 

	 *

	 * @param strTaskKey	the task key

	 * @param query			the query object

	 *

	 * @return	the task handover status 

	 */

	public static String getTaskHandoverStatus(String strTaskKey, DALSecurityQuery query)

	{

		String strTaskHandoverStatus = null;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_strHandover", null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			if(rsResultSet.next())

			{

           		strTaskHandoverStatus = rsResultSet.getString("WORKFLOW_TASK_strHandover");

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::getTaskHandoverStatus - " + e.toString(), e);

        }



		return strTaskHandoverStatus;

	}



	/**

	 * check whether is there any handover in the workflow instance that are currently in progress

	 *

	 * @param strWorkflowInstanceKey 	the workflow instance key

	 * @param query						the query object

	 *

	 * @return	true if there is, else return no

	 */

	public static boolean isHandoverExist(String strWorkflowInstanceKey, DALSecurityQuery query)

	{

	    boolean blHandoverExist = false;



		try

		{

			query.reset();



			query.setDomain(WORKFLOW_TASK, null, null, null);

            query.setField("WORKFLOW_TASK_strHandover", null);

			query.setWhere(null, 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", strWorkflowInstanceKey, 0,

						   DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_strHandover", "=", WorkflowManager.TASK_HANDOVER_YES, 0,

						   DALQuery.WHERE_HAS_VALUE);

			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();



			//if there is at least one result in the ResultSet that means there is handovering currently in progress.

			if(rsResultSet.next())

			{

           		blHandoverExist = true;

			}

		}

		catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR,

					"Unknown error in WorkflowManager::isHandoverExist - " + e.toString(), e);

        }



		return blHandoverExist;

	}

	/**
	 * get the task thread status
	 *
	 * @param strTaskKey	the task key
	 * @param query			the query object
	 *
	 * @return	the task thread status
	 */
	public static String getTaskThreadStatus(String strTaskKey, DALSecurityQuery query)
	{
		String strTaskThreadStatus = null;

		try
		{
			query.reset();

			query.setDomain(WORKFLOW_TASK, null, null, null);
            query.setField("WORKFLOW_TASK_strThread", null);
			query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);
			query.setWhere("AND", 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();

			if(rsResultSet.next())
			{
           		strTaskThreadStatus = rsResultSet.getString("WORKFLOW_TASK_strThread");
			}
		}
		catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
					"Unknown error in WorkflowManager::getTaskThreadStatus - " + e.toString(), e);
        }

		return strTaskThreadStatus;
	}        
}

