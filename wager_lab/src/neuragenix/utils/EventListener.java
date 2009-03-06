/*   

 * Copyright (c) 2004 Neuragenix, All Rights Reserved.

 * EventListener.java

 * Created on 10 May 2004

 * @author  Agustian Agustian

 * 

 * Description: 

 * 

 * This class will become the single point of control of channelling of what to listen to and what to invoke 

 */



package neuragenix.utils;



// uPortal packages

import org.jasig.portal.services.LogService;

import org.jasig.portal.ChannelRuntimeData;



// java libraries

import java.util.Enumeration;

import java.util.Hashtable;

import java.util.Vector;



// neuragenix libraries

import neuragenix.dao.DALSecurityQuery;

import neuragenix.security.AuthToken;

import neuragenix.genix.workflow.WorkflowEngine;



public class EventListener

{

	// type of notifications

	public static final String SYNC = "Sync";

	public static final String NOT_SYNC = "Not sync";



	// activities and target objects mapping

	private static Hashtable hashActivities = new Hashtable();

	private static Hashtable hashActivityDomainMap = new Hashtable();

	private static Hashtable hashDomainDomainKeyNameMap = new Hashtable();



	static

	{

		//for now we are going to hard code it first

		//e.g.

		//hashtable ---- keys   <>  elements

		//				 activity name  <> name of the object (use meaningful ones)

		

		Vector vecMappedObject = new Vector();

		vecMappedObject.add("workflow_manager");



		hashActivities.put("add_nurse", vecMappedObject);

		hashActivities.put("add_case", vecMappedObject);

		hashActivities.put("edit_case", vecMappedObject);

		hashActivities.put("view_case", vecMappedObject);

		hashActivities.put("edit_case_diary", vecMappedObject);

		hashActivities.put("add_case_diary", vecMappedObject);

		hashActivities.put("view_case_diary", vecMappedObject);

		//agus start
		
		//case re-assignment
		hashActivities.put("case_assignment_edit", vecMappedObject);
	
		//smartform completion
		hashActivities.put("smartform_complete", vecMappedObject);
		//agus end


		hashActivityDomainMap.put("add_case", "case");

		hashActivityDomainMap.put("edit_case", "case");

		hashActivityDomainMap.put("view_case", "case");

		hashActivityDomainMap.put("edit_case_diary", "case");

		hashActivityDomainMap.put("add_case_diary", "case");

		hashActivityDomainMap.put("view_case_diary", "case");

		//agus start
	
		//case re-assignment
		//hashActivityDomainMap.put("case_assignment_edit", "case");
		
		//agus end



		hashDomainDomainKeyNameMap.put("case", "CORECASE_intCoreCaseKey");

	}



	// constructor

	public EventListener()

	{

	}



	/**

	 * notify the world about an event

	 *

	 * @param strActivity		the activity

	 * @param strType			notification type (sync or not sync)
	 
	 * @param hashParameters	list of parameter value pairs

	 * @param runtimeData		the runtimedata

	 *

	 * @return a hastable that contains the target object as the key and the return object as the element 

	 */

	public static Hashtable notify(String strActivity, String strType, Hashtable hashParameters,
								   ChannelRuntimeData runtimeData)

	{

		Hashtable hashReturnValue = null;



		try

		{

			// sync

			if(strType.equals(SYNC))

			{

				if(hashActivities.containsKey(strActivity))

				{

					hashReturnValue = new Hashtable();



//System.err.println("strActivity = >>>" + strActivity + "<<<");

		

					Vector vecTargetObject = (Vector) hashActivities.get(strActivity);



					// the target object

					Enumeration enumTargetObject = vecTargetObject.elements();



					while(enumTargetObject.hasMoreElements())

					{

						String strTargetObject = (String) enumTargetObject.nextElement();



//System.err.println("strTargetObject = >>>" + strTargetObject + "<<<");



						Object objReturned = EventListener.executeObject(strTargetObject, runtimeData, strActivity,
																		 strType, hashParameters);



						if(objReturned != null)

						{

							hashReturnValue.put(strTargetObject, (Hashtable) objReturned);

						}

					}

				}

			}

			// not sync

			else if(strType.equals(NOT_SYNC))

			{

				if(hashActivities.containsKey(strActivity))

				{

					Vector vecTargetObject = (Vector) hashActivities.get(strActivity);



					Enumeration enumTargetObject = vecTargetObject.elements();



					while(enumTargetObject.hasMoreElements())

					{

						String strTargetObject = (String) enumTargetObject.nextElement();



						EventListener.executeObject(strTargetObject,runtimeData, strActivity, strType, hashParameters);

					}

				}

			}

		}

		catch(Exception e)

		{

        	e.printStackTrace();              

        	LogService.log(LogService.ERROR, "Unknown error in EventListener - publish" + e.toString());                

        }



		return hashReturnValue;



	}



	/**

	 * subscribe

	 */

	public static void subscribe()

	{

	}



	/**

	 * execute the target object

	 *

	 * @param strObject			the target object

	 * @param runtimeData		the runtime data

	 * @param strActivity		the activity

	 * @param strType			the type

	 * @param hashParameters	list of parameter value pairs
	 
	 *

	 * @return an object based on the target object

	 */

	private static Object executeObject(String strObject, ChannelRuntimeData runtimeData, String strActivity,
										String strType, Hashtable hashParameters)

	{

		Object objReturnValue = null;



		try

		{

			DALSecurityQuery query = new DALSecurityQuery("", (AuthToken) null);



			if(strObject.equals("workflow_manager"))

			{

				String strDomain = (String) hashActivityDomainMap.get(strActivity);

				String strDomainKey = null;

				if(strDomain != null)
				{
					strDomainKey = runtimeData.getParameter((String) hashDomainDomainKeyNameMap.get(strDomain));
				}

				boolean blValidateTrigger = ((Boolean) hashParameters.get("blValidateTrigger")).booleanValue();

				//System.err.println("strDomain = >>>" + strDomain + "<<<");

				//System.err.println("strDomainKey = >>>" + strDomainKey + "<<<");
				
				
				//System.err.println("strActivity = >>>" + strActivity + "<<<");



				if(strType.equals(SYNC))

				{

					objReturnValue = WorkflowEngine.instantiateWorkflowTemplates(strActivity, runtimeData, null, 

																				 strDomain, strDomainKey, 
																				 blValidateTrigger, query);

				}

				else if(strType.equals(NOT_SYNC))

				{

					WorkflowInstantiationThread workflowInstantiationThread = 

												new WorkflowInstantiationThread(strActivity, runtimeData, null, 

																			    strDomain, strDomainKey, 
																				blValidateTrigger, query);

					workflowInstantiationThread.start();

				}

			}



		}

		catch(Exception e)

		{

        	e.printStackTrace();              

        	LogService.log(LogService.ERROR, "Unknown error in EventListener - executeObject" + e.toString());                

        }



		return objReturnValue;

	}

}

