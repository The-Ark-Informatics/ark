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

package neuragenix.genix.LOVManager;



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

import neuragenix.genix.genixadmin.ListOfValuesFields;



/** This channel deals with the administration functions

 *

 */



public class CLOVManager implements IChannel

{



     

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

        

	public CLOVManager()

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

                   if(authToken.hasActivity("LIST_OF_VALUES_EDIT")) {

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

				

		// set the output Handler for the output.

		xslt.setTarget(out);

		

		// do the deed

		xslt.transform();

	}

	



}



