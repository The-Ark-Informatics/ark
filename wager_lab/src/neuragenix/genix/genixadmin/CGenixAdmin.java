/* 

 * Copyright (c) 2003 Neuragenix. All Rights Reserved.

 * Alert.java

 * Created on 1 February 2003, 00:00

 * @author  Shendon Ewans and Allwyn Fernandes

 * 

 * Description: 

 * Class for managing common portal functions

 *

 */

package neuragenix.genix.genixadmin;



import org.jasig.portal.IChannel;

import org.jasig.portal.UPFileSpec;

import org.jasig.portal.ChannelStaticData;

import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.ChannelRuntimeProperties;

import org.jasig.portal.PortalEvent;

import org.jasig.portal.PortalException;

import org.jasig.portal.utils.XSLT;

import org.xml.sax.ContentHandler;

import org.jasig.portal.security.*;

import org.jasig.portal.services.LogService;



import neuragenix.common.Utilities;

import neuragenix.common.BaseChannel;

import neuragenix.common.LockRequest;

import neuragenix.common.LockRecord;

import neuragenix.dao.DBSchema;

import neuragenix.dao.ChannelIDs;

import neuragenix.utils.Password;



import java.util.Hashtable;

import java.util.Vector;

import java.util.Enumeration;

import org.jasig.portal.PropertiesManager;

import neuragenix.security.AuthToken;

import neuragenix.services.AlertService;

import neuragenix.utils.ProcessRegister;



/** This channel deals with the administration functions

 *

 */



public class CGenixAdmin implements IChannel

{



	private static String USER_PASSWORD = "user_password";

	private static String ADMIN_PASSWORD = "admin_password";

	private static String PROFILE_VIEW = "profile_view";

	private static String PROFILE_ADD = "profile_add";

	private static String PROFILE_RESULTS = "profile_results";

	private static String PROFILE_SEARCH = "profile_search";

        private static final String PROCESS_LIST = "process_list";      

        private static final String SECURITY_MESSAGE = "security";   

        

	private AuthToken authToken;

	private static ChannelStaticData staticData;

	private ChannelRuntimeData runtimeData;

	private String strErr ;

	private String strXML ;

	private String strStylesheet;

	private String strExtra1;

	private String strExtra2;

        private LockRequest lr;

        

        private BaseChannel my_base_channel;

        

	public CGenixAdmin()

	{

            strXML = "";

            strStylesheet="";

            //lr = new LockRequest();

        }

	

      /*

       *  Returns channel runtime properties.

       *  Satisfies implementation of Channel Interface.

       *

       *  @return handle to runtime properties

       */

	public ChannelRuntimeProperties getRuntimeProperties()

	{

		return new ChannelRuntimeProperties();

	}

	

	/**

	*  Process layout-level events coming from the portal.

	*  Satisfies implementation of IChannel Interface.

	*

	*  @param PortalEvent ev a portal layout event

	*/

	public void receiveEvent(PortalEvent ev)

	{

            // locking

              if (ev.getEventNumber() == PortalEvent.SESSION_DONE)

                  if (lr != null) {

                      try {

                        lr.unlock();

                      }

                      catch (Exception e) {

                          e.printStackTrace();

                      }

                      finally {

                          lr = null;

                      }

                  } else return;

      

	}

	

	/**

	 *  Receive static channel data from the portal.

	 *  Satisfies implementation of IChannel Interface.

	 *

	 *  @param ChannelStaticData sd static channel data

	 */

	public void setStaticData(ChannelStaticData sd)

	{

		this.staticData = sd;

		this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");

	}

	

	/**

	 *  Receive channel runtime data from the portal.

	 *  Satisfies implementation of IChannel Interface.

	 *

	 *  @param ChannelRuntimeData rd handle to channel runtime data

	 */

	public void setRuntimeData(ChannelRuntimeData rd)

	{

		runtimeData = rd;

		my_base_channel = new BaseChannel(authToken);

		my_base_channel.setActivity("LIST_OF_VALUES_EDIT");

		my_base_channel.setNewXMLFile();

		String strCurrent = (rd.getParameter("current")!=null) ? rd.getParameter("current") : "showlists" ;

		//System.err.println("current: " + strCurrent + ", rd: " + rd.getParameter("current"));

		String strTemp;

		

		try

		{

                    // Alert Service Code                        

                     if(strCurrent.equals(PROCESS_LIST)) {

                       //Check Sercurity permission for Alert Service

                       if (!authToken.hasActivity(PROCESS_LIST) )  {

                            strXML  = "<?xml version=\"1.0\" encoding=\"utf-8\"?><" + SECURITY_MESSAGE + ">"

                                    + "<errorstring>No Permission to perform " + PROCESS_LIST + "</errorstring>"

                                    + "<errortext>The " + staticData.getPerson().getFullName() 

                                    + " is not authorised.</errortext>"

                                    + "<errordata></errordata>"

                                    + "</" + SECURITY_MESSAGE + ">";

                            strStylesheet = SECURITY_MESSAGE;              

                            //System.err.println("Security fail.");

                       }

                       else {

                               my_base_channel.setNewXMLFile();

                               ProcessRegister oProcessRegister = new ProcessRegister();

                                // locking

                                if (lr != null ) lr.unlock();

                                lr = new LockRequest(authToken);

                                

                               if (runtimeData.getParameter("start") != null) {

                                   if (!AlertService.IsStarted()) { 

                                       if (AlertService.startProcess()) my_base_channel.setXMLFile("<strErr>Email alert process started.</strErr>");

                                       else my_base_channel.setXMLFile("<strErr>Unable to start email alert process.</strErr>");   

                                   }

                                   else {

                                        my_base_channel.setXMLFile("<strErr>One email alert process has already been started.</strErr>");   

                                   }

                                   

                               } else if (runtimeData.getParameter("target") != null) {

                                    String strTarget = runtimeData.getParameter("target");                                    

                                    

                                    //System.err.println( "strProcessType=" + runtimeData.getParameter("strProcessType") );

                                    // If its an Alert Process request, process it

                                    if ( runtimeData.getParameter("strProcessType").equals(oProcessRegister.TYPE_EMAIL_ALERT) )  {

                                        

                                       if (strTarget.equals("stop_process")) {   

                                           if ( AlertService.IsStarted() ) {

                                               if ( AlertService.stopProcess() ) { 

                                                   my_base_channel.setXMLFile("<strErr>Process has been stopped.</strErr>");   

                                               }else my_base_channel.setXMLFile("<strErr>Process has already been stopped.</strErr>");  

                                           } else my_base_channel.setXMLFile("<strErr>Process has already been stopped. </strErr>");   

                                       } else if (strTarget.equals("del_process")) {                                              

                                           // Stop the process first before deleting it

                                           if ( AlertService.IsStarted() ) {

                                               if ( !AlertService.stopProcess() ) { 

                                                   my_base_channel.setXMLFile("<strErr>Unable to stop alert process.</strErr>");   

                                               } else {

                                                    // Delete the process 

                                                    deleteProcess(runtimeData.getParameter(oProcessRegister.INTERNAL_PROCESS_KEY));                                                                                                      

                                               }

                                           } else {

                                               // Delete the process 

                                               deleteProcess(runtimeData.getParameter(oProcessRegister.INTERNAL_PROCESS_KEY));                                                     

                                           }                                           

                                       }                                     

                                    // generic delete process                                          

                                    }else if (strTarget.equals("del_process")) {

                                         // Delete the process - ** Same code as below

                                         deleteProcess(runtimeData.getParameter(oProcessRegister.INTERNAL_PROCESS_KEY));

                                                     

                                            //String strProcessKey = runtimeData.getParameter(oProcessRegister.INTERNAL_PROCESS_KEY);

                                            // locks the template

                                            //lr.addLock(oProcessRegister.PROCESS_DOMAIN, strProcessKey, LockRecord.READ_WRITE);

                                            //System.err.println("Lock=" + oProcessRegister.PROCESS_DOMAIN + strProcessKey + LockRecord.READ_WRITE);    

                                            

                                           //System.err.println("Lock obtained="+lr.lockWrites());

                                           

                                           //if (!lr.lock()) {

                                               //my_base_channel.setXMLFile("<strErr>Error locking record for delete. Please stop the process before deleting or try again later.</strErr>");   

                                          /* if (oProcessRegister.deleteProcessItem( Integer.parseInt(strProcessKey),"")) {

                                               my_base_channel.setXMLFile("<strErr>Deleted.</strErr>");                                                    

                                           } else {

                                               my_base_channel.setXMLFile("<strErr>Error locking record for delete. Please stop the process before deleting or try again later.</strErr>");   

                                           }

                                            */   

                                               

                                               

                                           //} else {    

                                            

                                           /*     my_base_channel.setFormFields(new Hashtable());



                                                my_base_channel.setNewXMLFile();

                                                my_base_channel.clearWhere();

                                                my_base_channel.clearDomains();               



                                                my_base_channel.setRunTimeData(runtimeData);



                                                my_base_channel.setDomain(oProcessRegister.PROCESS_DOMAIN);

                                                my_base_channel.deleteRecord();

                                                //my_base_channel.setWhere("", "intProcessKey", DBSchema.EQUALS_OPERATOR, "17");

                                                my_base_channel.setWhere("", oProcessRegister.INTERNAL_PROCESS_KEY, DBSchema.EQUALS_OPERATOR, strProcessKey );

                                                my_base_channel.updateRecord();   



                                            */

                                                  

                                                // Perform the delete.



                                                // Free locks

                                              //   lr.unlock();

                                                 

                                           //}

                                            

                                           //lr = null; 

                                         

                                    }else {                                        

                                        my_base_channel.setXMLFile("<strErr>The function for this type of process has not been implemented.</strErr>");      

                                    }

                                    

                               }              

                              // else if (runtimeData.getParameter("refresh") != null) {

                                   

                              // }            

                                my_base_channel.setActivity(PROCESS_LIST);    

                                strStylesheet = PROCESS_LIST;        



                                try {

 

                                    my_base_channel.clearWhere();

                                    my_base_channel.setDeletedColumn(true);

                                    my_base_channel.clearDomains();



                                    my_base_channel.setDomain(AdminFormFields.PROCESS);

                                    runtimeData.setParameter("intProcessKey", "");                

                                    runtimeData.setParameter("strProcessType", "");

                                    runtimeData.setParameter("strProcessStatus", "");

                                    runtimeData.setParameter("strProcessDescription", "");



                                    my_base_channel.setRunTimeData(runtimeData);

                                    // Set the where conditions from the search form

                                    my_base_channel.setSearchFieldOperators(AdminFormFields.getProcessListFormOperators());

                                    my_base_channel.setSearchWhere();

                                    my_base_channel.setFormFields(AdminFormFields.getProcessServiceFormFields());

                                    //my_base_channel.setFormFieldDropDown(hashFormDropDowns);

                                    // Build the results XML file

                                    my_base_channel.buildSearchResultsXMLFile();





                                } catch (Exception e){

                                    LogService.log(LogService.ERROR, "Could not build XML file for channel.");   

                                }

                                // Retrive the XML file contents into an XML file structure 

                                strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

                                + "<process_list>" + my_base_channel.getXMLFile() + "</process_list>";



                       }       

                     }

                           else if(authToken.hasActivity("LIST_OF_VALUES_EDIT")) {

				if(strCurrent.equals("showlists"))

				{

					//my_base_channel.setDomain("LISTOFVALUES")

					my_base_channel.setFormFields(ListOfValuesFields.getLOVSelectorFields());

					my_base_channel.buildDefaultXMLFile();

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><lovselect>" + my_base_channel.getXMLFile() + "</lovselect>" ;

					strStylesheet = "lovselect";

				}

				

				else if(strCurrent.equals("editlist"))

				{

					if((strTemp = runtimeData.getParameter("editlist")) != null )

					{

						my_base_channel.setDomain("LIST_OF_VALUES");

						my_base_channel.setFormFields(ListOfValuesFields.getLOVEditFields());

						my_base_channel.setWhere("", "strType", DBSchema.EQUALS_OPERATOR, strTemp);

						my_base_channel.setOrderBy("intSortOrder", "ASC");

						my_base_channel.buildSearchResultsXMLFile();

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><lovedit>" + my_base_channel.getXMLFile() + "</lovedit>" ;

						strStylesheet = "lovedit";

						strExtra1 = ListOfValuesFields.getLOVSelectorFields().get(strTemp).toString();

						strExtra2 = strTemp;

					}

					

					else

					{

						//my_base_channel.setDomain("LISTOFVALUES")

						my_base_channel.setFormFields(ListOfValuesFields.getLOVSelectorFields());

						my_base_channel.buildDefaultXMLFile();

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><lovselect>" + my_base_channel.getXMLFile() + "</lovselect>" ;

						strStylesheet = "lovselect";

					}

					

				}

				

				else if(strCurrent.equals("insertval"))

				{

					my_base_channel.setDomain("LIST_OF_VALUES");

					Hashtable hashTmp = new Hashtable(ListOfValuesFields.getLOVEditFields());

					hashTmp.remove("intLovKey");

					my_base_channel.setFormFields(hashTmp);

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.buildAddModifyXMLFile();

					if(my_base_channel.getRequiredStatus())

					{

						my_base_channel.addRecord();

					}

					my_base_channel.clearWhere();

					my_base_channel.setWhere("", "strType", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("strType"));

					my_base_channel.setOrderBy("intSortOrder", "ASC");

					my_base_channel.setNewXMLFile();

					my_base_channel.buildSearchResultsXMLFile();

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><lovedit>" + my_base_channel.getXMLFile() + "</lovedit>" ;

					strStylesheet = "lovedit";

					strExtra1 = ListOfValuesFields.getLOVSelectorFields().get(runtimeData.getParameter("strType")).toString();

					strExtra2 = runtimeData.getParameter("strType");

					

				}

				else if(strCurrent.equals("editval"))

				{

					my_base_channel.setDomain("LIST_OF_VALUES");

					my_base_channel.setFormFields(ListOfValuesFields.getLOVEditFields());

					my_base_channel.setWhere("", "intLovKey" , DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("intLovKey"));

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.buildAddModifyXMLFile();

					if(my_base_channel.getRequiredStatus())

					{

						my_base_channel.updateRecord();

					}

					my_base_channel.clearWhere();

					my_base_channel.setWhere("", "strType", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("strType"));

					my_base_channel.setOrderBy("intSortOrder", "ASC");

					my_base_channel.setNewXMLFile();

					my_base_channel.buildSearchResultsXMLFile();

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><lovedit>" + my_base_channel.getXMLFile() + "</lovedit>" ;

					strStylesheet = "lovedit";

					strExtra1 = ListOfValuesFields.getLOVSelectorFields().get(runtimeData.getParameter("strType")).toString();

					strExtra2 = runtimeData.getParameter("strType");

					

				}

				else if(strCurrent.equals("showusers"))

				{

					my_base_channel.setNewXMLFile();

					my_base_channel.setDomain("PERSON_DIR");

					my_base_channel.setDeletedColumn(false);

					my_base_channel.setFormFields(AdminFormFields.getUserEditFields());

					//my_base_channel.setDomain("PERSON_GROUP");

					//my_base_channel.setWhere("", "intGroupID", DBSchema.EQUALS_OPERATOR, ((AuthToken)staticData.getPerson().getAttribute("AuthToken")).getGroup());

					my_base_channel.setOrderBy("strUsername", "ASC");

					my_base_channel.buildSearchResultsXMLFile();

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><useredit>" + my_base_channel.getXMLFile() + "</useredit>" ;

					strStylesheet = "useredit";

				}

				

				else if(strCurrent.equals("newuser"))

				{

					my_base_channel.setDomain("PERSON_DIR");

	//				my_base_channel.setDomain("PERSON_LAYOUT");

	//				my_base_channel.setDomain("PERSON_GROUP");

					Hashtable hashTmp = new Hashtable(AdminFormFields.getUserEditFields());

	//				hashTmp.remove("intUserKey");

					hashTmp.put("strPassword", "");

	//				hashTmp.put("intGenixGroupID", "");

					my_base_channel.setFormFields(hashTmp);

					runtimeData.setParameter("strPassword","(MD5)LbZOgrnaITgoC5in4cjrGkDU+21TK8+M");

	//				runtimeData.setParameter("intGenixGroupID",authToken.getGroupForRole());

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.buildAddModifyXMLFile();

					if(my_base_channel.getRequiredStatus())

					{

						my_base_channel.addRecord();

						

	//DO OTHER STUFF HERE TO MAKE USER USEFUL

						strStylesheet = ADMIN_PASSWORD;

						my_base_channel.setFormFields(AdminFormFields.getChangePasswordFormFields());

						//my_base_channel.setFormFieldDropDown(new Hashtable());

						

						// Write out the XML file contents of variables

						

						try

						{

							my_base_channel.buildDefaultXMLFile();

						}

						catch (Exception e)

						{

							LogService.log(LogService.ERROR, "Could not build XML file for channel.");

						}

						

						// Retrive the XML file contents into an XML file structure

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

							 + "<admin_password>" + my_base_channel.getXMLFile() + "<strUsername>" + runtimeData.getParameter("strUsername") + "</strUsername></admin_password>";

					}

					else 

					{

						my_base_channel.setNewXMLFile();

						my_base_channel.setDomain("PERSON_DIR");

						my_base_channel.setDeletedColumn(false);

						my_base_channel.setFormFields(AdminFormFields.getUserEditFields());

						//my_base_channel.setDomain("PERSON_GROUP");

						//my_base_channel.setWhere("", "intGroupID", DBSchema.EQUALS_OPERATOR, ((AuthToken)staticData.getPerson().getAttribute("AuthToken")).getGroup());

						my_base_channel.setOrderBy("strUsername", "ASC");

						my_base_channel.buildSearchResultsXMLFile();

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><useredit><errorstring>All fields are required</errorstring>" + my_base_channel.getXMLFile() + "</useredit>" ;

						strStylesheet = "useredit";

					}	

					

				}

				else if(strCurrent.equals("edituser"))

				{

					my_base_channel.setDomain("PERSON_DIR");

					my_base_channel.setFormFields(AdminFormFields.getUserEditFields());

					my_base_channel.setWhere("", "strUsername" , DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("strUsername"));

					my_base_channel.setCaseSensitive(true);

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.buildAddModifyXMLFile();

					if(my_base_channel.getRequiredStatus())

					{

						my_base_channel.updateRecord();

					}

					my_base_channel.clearWhere();

					//my_base_channel.setDomain("PERSON_GROUP");

					//my_base_channel.setWhere("", "intGroupID", DBSchema.EQUALS_OPERATOR, ((AuthToken)staticData.getPerson().getAttribute("AuthToken")).getGroup());

					my_base_channel.setOrderBy("strUsername", "ASC");

					my_base_channel.setNewXMLFile();

					my_base_channel.setDeletedColumn(false);

					my_base_channel.buildSearchResultsXMLFile();

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><useredit>" + my_base_channel.getXMLFile() + "</useredit>" ;

					strStylesheet = "useredit";

				





				}

				

				else if(strCurrent.equals("showprofiles"))

				{

					// Set the stylesheet to results

					my_base_channel.clearDomains();

					my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

					

					my_base_channel.setNewXMLFile();

					my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

					my_base_channel.setDeletedColumn(false);

					

					if (runtimeData.getParameter("search") != null) // We have data from the form

					{

						my_base_channel.setRunTimeData(runtimeData);

						my_base_channel.setSearchFieldOperators(AdminFormFields.getProfileSearchFormOperators());

						my_base_channel.setSearchWhere();

						//my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

					} 

					// Build the results XML file

					//my_base_channel.setSearchFieldOperators(AdminFormFields.getProfileSearchFormOperators());

					my_base_channel.buildSearchResultsXMLFile();

					strStylesheet = PROFILE_RESULTS;

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><profile>" + my_base_channel.getXMLFile() + "</profile>";

				}

				else if(strCurrent.equals("insertprofile"))

				{	

					strStylesheet = PROFILE_ADD; 

					my_base_channel.clearDomains();

					my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

					

					my_base_channel.setNewXMLFile();

					my_base_channel.setFormFields(AdminFormFields.getProfileAddFormFields());

					my_base_channel.setDeletedColumn(false);

					

					if (runtimeData.getParameter("save") != null) // They have clicked the save button!

					{

						my_base_channel.setRunTimeData(runtimeData);

						my_base_channel.buildAddModifyXMLFile();

						// Check to see if all the required fields have been filled and the correct data is in the fields

						if(my_base_channel.getRequiredStatus() == false || my_base_channel.getValidFieldsStatus() == false)

						{

							strStylesheet = PROFILE_ADD;

						}

						

						else // If all data ok add the record

						{

							my_base_channel.addRecord();

							if(my_base_channel.getAddRecordStatus() == true)

							{

								my_base_channel.setXMLFile("<" + AdminFormFields.INTERNAL_PROFILE_ID +">"

											   + my_base_channel.getInsertedRecordId()

											   + "</" + AdminFormFields.INTERNAL_PROFILE_ID + ">");

							}

							strStylesheet = PROFILE_VIEW;

							my_base_channel.setNewXMLFile();

							my_base_channel.clearDomains();

							my_base_channel.clearWhere();

							my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

							my_base_channel.setDeletedColumn(false);

							my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

							my_base_channel.setWhere("", AdminFormFields.INTERNAL_PROFILE_ID, DBSchema.EQUALS_OPERATOR, (new Integer(my_base_channel.getInsertedRecordId())).toString());

							my_base_channel.buildViewXMLFile();

							my_base_channel.setXMLFile("<profileactivities>");

							my_base_channel.setDeletedColumn(true);

							my_base_channel.clearDomains();

							my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

							my_base_channel.setFormFields(AdminFormFields.getProfileActivityLOVFormFields());

							my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

							my_base_channel.clearWhere();

							my_base_channel.buildTableLOVXMLFile();

							my_base_channel.setXMLFile("</profileactivities>");

							

						}

						

					}

					else if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true")) // They have clicked the clear button

					{

						my_base_channel.buildDefaultXMLFile();

					} 

					else // first time to the page!

					{

						my_base_channel.buildDefaultXMLFile();

					}

					

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><profile>" + my_base_channel.getXMLFile() + "</profile>";

				}

				else if(strCurrent.equals("viewprofile"))

				{	

					strStylesheet = PROFILE_VIEW; 

					my_base_channel.clearDomains();

					my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

					

					my_base_channel.setNewXMLFile();

					my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

					my_base_channel.setDeletedColumn(false);

					my_base_channel.setWhere("", AdminFormFields.INTERNAL_PROFILE_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

					

					if (runtimeData.getParameter("save") != null) // They have clicked the save button!

					{

						my_base_channel.setRunTimeData(runtimeData);

						my_base_channel.buildAddModifyXMLFile();

						// Check to see if all the required fields have been filled and the correct data is in the fields

						if(my_base_channel.getRequiredStatus() == true ) //&& my_base_channel.getValidFieldsStatus() == true)

						{

							my_base_channel.updateRecord();

						}







						my_base_channel.setNewXMLFile();

						my_base_channel.clearDomains();

						my_base_channel.clearWhere();

						my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

						my_base_channel.setDeletedColumn(false);

						my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

						my_base_channel.setWhere("", AdminFormFields.INTERNAL_PROFILE_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

						my_base_channel.buildViewXMLFile();

						my_base_channel.setXMLFile("<profileactivities>");

						my_base_channel.clearDomains();

						my_base_channel.setDomain(AdminFormFields.PROFILE_AUTHORIZATION_DOMAIN);

						my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

						my_base_channel.setDeletedColumn(true);

						my_base_channel.clearWhere();

						my_base_channel.setWhere("", "intForeignProfileID", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

						my_base_channel.setFormFields(AdminFormFields.getProfileActivityFormFields());

						my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

						my_base_channel.buildSearchResultsXMLFile();

						my_base_channel.clearDomains();

						my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

						my_base_channel.setFormFields(AdminFormFields.getProfileActivityLOVFormFields());

						my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

						my_base_channel.clearWhere();

						my_base_channel.buildTableLOVXMLFile();

						my_base_channel.setXMLFile("</profileactivities>");





					}

					else if (runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID) != null)

					{

						my_base_channel.setNewXMLFile();

						my_base_channel.clearDomains();

						my_base_channel.clearWhere();

						my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

						my_base_channel.setDeletedColumn(false);

						my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

						my_base_channel.setWhere("", AdminFormFields.INTERNAL_PROFILE_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

						my_base_channel.buildViewXMLFile();

						my_base_channel.setXMLFile("<profileactivities>");

						my_base_channel.clearDomains();

						my_base_channel.setDomain(AdminFormFields.PROFILE_AUTHORIZATION_DOMAIN);

						my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

						my_base_channel.setDeletedColumn(true);

						my_base_channel.clearWhere();

						my_base_channel.setWhere("", "intForeignProfileID", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

						my_base_channel.setFormFields(AdminFormFields.getProfileActivityFormFields());

						my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

						my_base_channel.buildSearchResultsXMLFile();

						my_base_channel.clearDomains();

						my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

						my_base_channel.setFormFields(AdminFormFields.getProfileActivityLOVFormFields());

						my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

						my_base_channel.clearWhere();

						my_base_channel.buildTableLOVXMLFile();

						my_base_channel.setXMLFile("</profileactivities>");

					}

					

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><profile>" + my_base_channel.getXMLFile() + "</profile>";

				}

				else if(strCurrent.equals("insertactivity"))

				{	

					strStylesheet = PROFILE_VIEW;

					/*Enumeration en;

					String s;

					for( boolean b = ( (en = runtimeData.getParameterNames()) != null && (s = en.nextElement().toString()) != null ) ; en.hasMoreElements() ; s = en.nextElement().toString() )

					{

						System.err.println(s + ": " + runtimeData.getParameter(s) );

					}*/

		

					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setDomain(AdminFormFields.PROFILE_AUTHORIZATION_DOMAIN);

					my_base_channel.setDeletedColumn(true);

					my_base_channel.setFormFields(AdminFormFields.getAddActivityFormFields());

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.buildAddModifyXMLFile();

					// Check to see if all the required fields have been filled and the correct data is in the fields

					if(my_base_channel.getRequiredStatus() == true ) //&& my_base_channel.getValidFieldsStatus() == true)

					{

						my_base_channel.addRecord();

					}



					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

					my_base_channel.setDeletedColumn(false);

					my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

					my_base_channel.setWhere("", AdminFormFields.INTERNAL_PROFILE_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

					my_base_channel.buildViewXMLFile();

					my_base_channel.setXMLFile("<profileactivities>");

					my_base_channel.clearDomains();

					my_base_channel.setDomain(AdminFormFields.PROFILE_AUTHORIZATION_DOMAIN);

					my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

					my_base_channel.setDeletedColumn(true);

					my_base_channel.clearWhere();

					my_base_channel.setWhere("", "intForeignProfileID", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

					my_base_channel.setFormFields(AdminFormFields.getProfileActivityFormFields());

					my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

					my_base_channel.buildSearchResultsXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

					my_base_channel.setFormFields(AdminFormFields.getProfileActivityLOVFormFields());

					my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

					my_base_channel.clearWhere();

					my_base_channel.buildTableLOVXMLFile();

					my_base_channel.setXMLFile("</profileactivities>");

					

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><profile>" + my_base_channel.getXMLFile() + "</profile>";

				}

				else if(strCurrent.equals("deleteactivity"))

				{	

					strStylesheet = PROFILE_VIEW; 

		

					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setFormFields(new Hashtable());

					my_base_channel.setDomain(AdminFormFields.PROFILE_AUTHORIZATION_DOMAIN);

					my_base_channel.setDeletedColumn(true);

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.deleteRecord();

					my_base_channel.setWhere("", AdminFormFields.INTERNAL_PROFILE_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

					my_base_channel.setWhere(DBSchema.AND_CONNECTOR, AdminFormFields.INTERNAL_ACTIVITY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_ACTIVITY_ID));

					my_base_channel.updateRecord();



					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setDomain(AdminFormFields.PROFILE_DOMAIN);

					my_base_channel.setDeletedColumn(false);

					my_base_channel.setFormFields(AdminFormFields.getProfileFormFields());

					my_base_channel.setWhere("", AdminFormFields.INTERNAL_PROFILE_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

					my_base_channel.buildViewXMLFile();

					my_base_channel.setXMLFile("<profileactivities>");

					my_base_channel.clearDomains();

					my_base_channel.setDomain(AdminFormFields.PROFILE_AUTHORIZATION_DOMAIN);

					my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

					my_base_channel.setDeletedColumn(true);

					my_base_channel.clearWhere();

					my_base_channel.setWhere("", "intForeignProfileID", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_PROFILE_ID));

					my_base_channel.setFormFields(AdminFormFields.getProfileActivityFormFields());

					my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

					my_base_channel.buildSearchResultsXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.setDomain(AdminFormFields.ACTIVITY_DOMAIN);

					my_base_channel.setFormFields(AdminFormFields.getProfileActivityLOVFormFields());

					my_base_channel.setOrderBy(AdminFormFields.INTERNAL_ACTIVITY_ID, "ASC");

					my_base_channel.clearWhere();

					my_base_channel.buildTableLOVXMLFile();

					my_base_channel.setXMLFile("</profileactivities>");

					

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><profile>" + my_base_channel.getXMLFile() + "</profile>";

				}

				else if(strCurrent.equals("userprofile"))

				{

					my_base_channel.setNewXMLFile();

					my_base_channel.setDomain("PERSON_DIR");

					my_base_channel.setDeletedColumn(false);

					my_base_channel.setFormFields(AdminFormFields.getUserEditFields());

					//my_base_channel.setDomain("PERSON_GROUP");

					//my_base_channel.setWhere("", "intGroupID", DBSchema.EQUALS_OPERATOR, ((AuthToken)staticData.getPerson().getAttribute("AuthToken")).getGroup());

					my_base_channel.setOrderBy("strUsername", "ASC");

					my_base_channel.buildSearchResultsXMLFile();

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><userselect>" + my_base_channel.getXMLFile() + "</userselect>" ;

					strStylesheet = "user_select";

				}

				else if(strCurrent.equals("profileassign"))

				{

					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setCaseSensitive(true);

					my_base_channel.setWhere("", "strUsername", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("strUsername"));

					my_base_channel.setDomain("USERPROFILE");

					my_base_channel.setDomain("PROFILE");

					my_base_channel.setFormFields(AdminFormFields.getUserProfileFormFields());

					my_base_channel.setOrderBy("strProfileName", "ASC");

					my_base_channel.buildSearchResultsXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.setDomain("PROFILE");

					my_base_channel.setFormFields(AdminFormFields.getProfileLOVFormFields());

					my_base_channel.setOrderBy("strProfileName", "ASC");

					my_base_channel.clearWhere();

					my_base_channel.buildTableLOVXMLFile();

					

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><userprofile>" + my_base_channel.getXMLFile() + "</userprofile>" ;

					strStylesheet = "user_profile";

					strExtra1 = runtimeData.getParameter("strUsername");

				}

				else if(strCurrent.equals("insertuserprofile"))

				{

					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setDomain("USERPROFILE");

					my_base_channel.setDeletedColumn(true);

					my_base_channel.setFormFields(AdminFormFields.getUserProfileAddFormFields());

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.buildAddModifyXMLFile();

					// Check to see if all the required fields have been filled and the correct data is in the fields

					if(my_base_channel.getRequiredStatus() == true ) //&& my_base_channel.getValidFieldsStatus() == true)

					{

						my_base_channel.addRecord();

					}

					

					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setCaseSensitive(true);

					my_base_channel.setWhere("", "strUsername", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("strUsername"));

					my_base_channel.setDomain("USERPROFILE");

					my_base_channel.setDomain("PROFILE");

					my_base_channel.setFormFields(AdminFormFields.getUserProfileFormFields());

					my_base_channel.setOrderBy("strProfileName", "ASC");

					my_base_channel.buildSearchResultsXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.setDomain("PROFILE");

					my_base_channel.setFormFields(AdminFormFields.getProfileLOVFormFields());

					my_base_channel.setOrderBy("strProfileName", "ASC");

					my_base_channel.clearWhere();

					my_base_channel.buildTableLOVXMLFile();

					

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><userprofile>" + my_base_channel.getXMLFile() + "</userprofile>" ;

					strStylesheet = "user_profile";

					strExtra1 = runtimeData.getParameter("strUsername");

				}

				else if(strCurrent.equals("deleteuserprofile"))

				{

					

					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setFormFields(new Hashtable());

					my_base_channel.setDomain("USERPROFILE");

					my_base_channel.setDeletedColumn(true);

					my_base_channel.setRunTimeData(runtimeData);

					my_base_channel.deleteRecord();

					my_base_channel.setWhere("", "intUserProfileID", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("intUserProfileID"));

					my_base_channel.updateRecord();

					

					my_base_channel.setNewXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.clearWhere();

					my_base_channel.setCaseSensitive(true);

					my_base_channel.setWhere("", "strUsername", DBSchema.EQUALS_OPERATOR, runtimeData.getParameter("strUsername"));

					my_base_channel.setDomain("USERPROFILE");

					my_base_channel.setDomain("PROFILE");

					my_base_channel.setFormFields(AdminFormFields.getUserProfileFormFields());

					my_base_channel.setOrderBy("strProfileName", "ASC");

					my_base_channel.buildSearchResultsXMLFile();

					my_base_channel.clearDomains();

					my_base_channel.setDomain("PROFILE");

					my_base_channel.setFormFields(AdminFormFields.getProfileLOVFormFields());

					my_base_channel.setOrderBy("strProfileName", "ASC");

					my_base_channel.clearWhere();

					my_base_channel.buildTableLOVXMLFile();

					

					strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><userprofile>" + my_base_channel.getXMLFile() + "</userprofile>" ;

					strStylesheet = "user_profile";

					strExtra1 = runtimeData.getParameter("strUsername");

				}

				

				

				else if(strCurrent.equals("changepass"))

				{

					my_base_channel.setDomain(AdminFormFields.PERSON_DIR);

					

					if (runtimeData.getParameter("save") != null)

					{

					

						strStylesheet = ADMIN_PASSWORD;



						

						// rebuid the return screen

						// Reset the XML file

						my_base_channel.setNewXMLFile();

						

						// Give the base channel a list of fields for the update

						my_base_channel.setFormFields(AdminFormFields.getChangePasswordFormFields());

						

						

						my_base_channel.setRunTimeData(runtimeData);

						// Write out the XML file contents of variables

						my_base_channel.buildAddModifyXMLFile();

						

						// Protect code from executing if required fields are not set

						

						if(my_base_channel.getRequiredStatus() == false)

						{

							strStylesheet = ADMIN_PASSWORD;

						}

						else

						{

							String strUsername = runtimeData.getParameter("strUsername");

							String strNewPassword1 = runtimeData.getParameter("strNewPassword1");

							String strNewPassword2 = runtimeData.getParameter("strNewPassword2");

							

							try

							{

								if (strNewPassword1.equals(strNewPassword2) )

								{

								

									my_base_channel.setFormFields(AdminFormFields.getUpdatePasswordFields());

									

									my_base_channel.setDeletedColumn(false);

									my_base_channel.setWhere("", AdminFormFields.INTERNAL_USER_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_USER_ID));

									Vector vecTimestamp = my_base_channel.lookupRecord();

									if(vecTimestamp != null && vecTimestamp.size() == 1)

									{

										runtimeData.setParameter("PERSON_DIR_Timestamp", ((Hashtable)vecTimestamp.get(0)).get("PERSON_DIR_Timestamp").toString());

									}

									

									runtimeData.setParameter("strPassword",  Password.getEncrypt(strNewPassword1));

									

									my_base_channel.setRunTimeData(runtimeData);

									my_base_channel.updateRecord();

									

									

									// Verified

									

									if (my_base_channel.getUpdateRecordStatus() == true)

									{

										strErr = "Password updated for user " + strUsername;

									}

									

									else

									{

										strErr = "Unable to update password for user " + strUsername;

									}

									

									LogService.log(LogService.INFO, strErr);

								}

								

							}

							

							catch (Exception e)

							{

								// Log the exception

								strErr = "Unknown error: " + LogService.ERROR + " " + e;

								LogService.log(LogService.ERROR, strErr);

							}

						}

						



						// Retrive the XML file contents into an XML file structure

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +

							 "<admin_password>" + "<strErr>" + strErr + "</strErr>" +

							 my_base_channel.getXMLFile() + "</admin_password>";

							 

					}					

					else

					{

                                            strStylesheet = ADMIN_PASSWORD;

                                            my_base_channel.setFormFields(AdminFormFields.getChangePasswordFormFields());

						

						try

						{

							my_base_channel.buildDefaultXMLFile();

						}

						

						catch (Exception e)

						{

							LogService.log(LogService.ERROR, "Could not build XML file for channel.");

						}

						

						// Retrive the XML file contents into an XML file structure

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

							 + "<admin_password>" + my_base_channel.getXMLFile() + "</admin_password>";

							 

					}

				}

				

			}

			else

			{

					my_base_channel.setDomain(AdminFormFields.PERSON_DIR);

					

					if (runtimeData.getParameter("save") != null)

					{

					

						strStylesheet = USER_PASSWORD;



						

						// rebuid the return screen

						// Reset the XML file

						my_base_channel.setNewXMLFile();

						

						// Give the base channel a list of fields for the update

						my_base_channel.setFormFields(AdminFormFields.getUserChangePasswordFormFields());

						

						

						my_base_channel.setRunTimeData(runtimeData);

						// Write out the XML file contents of variables

						my_base_channel.buildAddModifyXMLFile();

						

						// Protect code from executing if required fields are not set

						

						if(my_base_channel.getRequiredStatus() == false)

						{

							strStylesheet = USER_PASSWORD;

						}

						else

						{

							String strUsername = authToken.getUserIdentifier();

							String strOldPassword = runtimeData.getParameter("strOldPassword");

							String strNewPassword1 = runtimeData.getParameter("strNewPassword1");

							String strNewPassword2 = runtimeData.getParameter("strNewPassword2");

							

							try

							{

								if (!Password.isVerified(strUsername, strOldPassword))

								{

									strErr = "Old Password incorrect";

								}

								else if (!strNewPassword1.equals(strNewPassword2))

								{

									strErr = "Password and confirmation do not match";

								}

								else

								{

								

									my_base_channel.setFormFields(AdminFormFields.getUpdatePasswordFields());

									

									runtimeData.setParameter("strUsername",  strUsername);

									runtimeData.setParameter("strPassword",  Password.getEncrypt(strNewPassword1));

									

									my_base_channel.setDeletedColumn(false);

									my_base_channel.setWhere("", AdminFormFields.INTERNAL_USER_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(AdminFormFields.INTERNAL_USER_ID));

									Vector vecTimestamp = my_base_channel.lookupRecord();

									if(vecTimestamp != null && vecTimestamp.size() == 1)

									{

										runtimeData.setParameter("PERSON_DIR_Timestamp", ((Hashtable)vecTimestamp.get(0)).get("PERSON_DIR_Timestamp").toString());

									}

									

									

									my_base_channel.setRunTimeData(runtimeData);

									my_base_channel.updateRecord();

									

									

									// Verified

									

									if (my_base_channel.getUpdateRecordStatus() == true)

									{

										strErr = "Password updated for user " + strUsername;

									}

									

									else

									{

										strErr = "Unable to update password for user " + strUsername;

									}

									

									LogService.log(LogService.INFO, strErr);

									my_base_channel.setNewXMLFile();

								}

								

							}

							

							catch (Exception e)

							{

								// Log the exception

								strErr = "Unknown error: " + LogService.ERROR + " " + e;

								LogService.log(LogService.ERROR, strErr);

							}

						}

						



						// Retrive the XML file contents into an XML file structure

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +

							 "<user_password>" + "<strErr>" + strErr + "</strErr>" +

							 my_base_channel.getXMLFile() + "</user_password>";

							 

					}					

					else

					{				

						

						strStylesheet = USER_PASSWORD;

						my_base_channel.setFormFields(AdminFormFields.getUserChangePasswordFormFields());

						// Write out the XML file contents of variables

						try

						{

							my_base_channel.buildDefaultXMLFile();

						}

						

						catch (Exception e)

						{

							LogService.log(LogService.ERROR, "Could not build XML file for channel.");

						}

						

						// Retrive the XML file contents into an XML file structure

						strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

							 + "<user_password>" + my_base_channel.getXMLFile() + "</user_password>";

							 

					}







			}

				

		}

		

		catch (Exception e)

		{

			//System.err.println("CGenixAdmin: Unknown error: " + e.toString());

			e.printStackTrace(System.err);

			LogService.instance().log(LogService.ERROR, "CGenixAdmin Channel - Unknown error - " + e.toString());

		}













			

	}

	

	

	/** Output channel content to the portal

	 *  @param out a sax document handler

	 */

	public void renderXML(ContentHandler out) throws PortalException

	{

	

		// Create a new XSLT styling engine

		XSLT xslt = new XSLT(this);

		

		// pass the result xml to the styling engine.

		//System.err.println("XML: " + strXML);

		xslt.setXML(strXML);

		

		// specify the stylesheet selector

		xslt.setXSL("CGenixAdmin.ssl", strStylesheet, runtimeData.getBrowserInfo());

		

		// set parameters that the stylesheet needs.

		xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());

		

		if(strStylesheet.equals("lovedit"))

		{

			xslt.setStylesheetParameter("strTypeDef", strExtra1);

			xslt.setStylesheetParameter("strTypeNew", strExtra2);

		}

		

		if(strStylesheet.equals("user_profile"))

		{

			xslt.setStylesheetParameter("strUsername", strExtra1);

		}

		

		// set the output Handler for the output.

		xslt.setTarget(out);

		

		// do the deed

		xslt.transform();

	}

	

        private void deleteProcess( String strProcessKey) {

            try {

                // Delete the process

                if ( (new ProcessRegister() ).deleteProcessItem( Integer.parseInt(strProcessKey),"")) {

                    my_base_channel.setXMLFile("<strErr>Deleted.</strErr>");                                                    

                } else {

                    my_base_channel.setXMLFile("<strErr>Error locking record for delete. Please stop the process before deleting or try again later.</strErr>");   

                }

            } catch(Exception e){

                LogService.log(LogService.ERROR, "Unable to delete process:"+strProcessKey);						

            }

        }

}



