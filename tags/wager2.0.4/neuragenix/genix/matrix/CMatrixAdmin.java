/* 

 * Copyright (c) 2002 Neuragenix, Inc. All Rights Reserved.

 * CAdmin.java

 * Created on 2 December 2002, 15:24

 * @author  Shendon Ewans

 * 

 * Description: 

 * Channel for administion function. 

 * - Change passwords

 * - Maintain List of Values

 */



package neuragenix.genix.matrix;



// Channel Classes

import org.jasig.portal.IChannel;

import org.jasig.portal.ChannelStaticData;

import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.ChannelRuntimeProperties;

import org.jasig.portal.PortalEvent;

import org.jasig.portal.PortalException;

import org.jasig.portal.utils.XSLT;

import org.xml.sax.ContentHandler;

import org.jasig.portal.services.LogService;





// Additional module imports

import neuragenix.common.Utilities;

import neuragenix.common.BaseChannel;

import neuragenix.dao.DBSchema;

import java.util.Hashtable;

import java.util.Vector;

import java.util.Enumeration;

import neuragenix.utils.Password;



/** 

 *  Channel for administion. 

 *  @author Shendon Ewans

 */

public class CMatrixAdmin implements IChannel {



    private static final int NORMAL_MODE =    42; 

    private static final int ABOUT_MODE  = 31337;

    private static final String ADMIN_PASSWORD = "admin_password";



    private int mode; // whether we're in NORMAL_MODE or ABOUT_MODE

    private AdminFormFields oAdminFormFields;



    private ChannelStaticData staticData;

    private ChannelRuntimeData runtimeData;

    private String strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    private String stylesheet;



    private String strUsername;

    private String strOldPassword;

    private String strNewPassword1;

    private String strNewPassword2;



    private BaseChannel my_base_channel = new BaseChannel();

    private Hashtable hashChangePasswordFormFields;

    private Hashtable hashUpdatePasswordFields;

    private Hashtable hashAdminValidateFields;

    // These are the page names that the value current (current page) can contain

    private static final String MANAGE_CASE = "manage_case";

    private static final String DEFRAUDED_ACCT = "defruaded_account";

    private static final String SEARCH_CASE = "search_case";

    private static final String CREATE_CASE = "create_case";

    private static final String DIARY = "diary";

    private static final String OFFENDING_BRANCH = "offending_branch";



    private String strCurrent; // This is used to know what the current page the user is on



    // Message string

    private String strErr;



      /** 

       *  Construct0r. 

       */ 

      public CMatrixAdmin() { 



            this.mode = NORMAL_MODE; // start in normal mode

            //this.strName = "";     



            // LogService.log(LogService.DEBUG, "Initiating test channel code..");



      }



      //

      //  Implementing the IChannel Interface

      //



      /** 

       *  Returns channel runtime properties.

       *  Satisfies implementation of Channel Interface.

       *

       *  @return handle to runtime properties 

       */ 

      public ChannelRuntimeProperties getRuntimeProperties() { 

        return new ChannelRuntimeProperties();

      }



      /** 

       *  Process layout-level events coming from the portal.

       *  Satisfies implementation of IChannel Interface.

       *

       *  @param <b>PortalEvent</b> ev a portal layout event

       */

      public void receiveEvent(PortalEvent ev) {

          if (ev.getEventNumber() == PortalEvent.ABOUT_BUTTON_EVENT) {

              mode = ABOUT_MODE;

          }

      }



      /** 

       *  Receive static channel data from the portal.

       *  Satisfies implementation of IChannel Interface.

       *

       *  @param <b>ChannelStaticData</b> sd static channel data

       */

      public void setStaticData(ChannelStaticData sd) {

        this.staticData = sd;

      }



      /** 

       *  Receive channel runtime data from the portal.

       *  Satisfies implementation of IChannel Interface.

       *

       *  @param <b>ChannelRuntimeData</b> rd handle to channel runtime data

       */

      public void setRuntimeData(ChannelRuntimeData rd) {

        

        // Set the database tables to be used

        my_base_channel.setDomain(oAdminFormFields.PERSON_DIR);

        

        // Most of the processing is usually done here.

        this.runtimeData = rd;        

        strErr = "";               

        

        // Get the current page the users is on

        strCurrent = runtimeData.getParameter("current");

        if(strCurrent != null)

        {

            if (strCurrent.compareTo(MANAGE_CASE) == 0) { doManageCase();} 

            else if (strCurrent.compareTo(SEARCH_CASE) == 0) { doSearchCase(); }

            else if (strCurrent.compareTo(DEFRAUDED_ACCT) == 0) { doDefraudedAccount(); }

            else if (strCurrent.compareTo(CREATE_CASE) == 0) { doCreateCase(); }

            else if (strCurrent.compareTo(DIARY) == 0) { doDiary(); }

            else if (strCurrent.compareTo(OFFENDING_BRANCH) == 0) { doOffendingBranch(); }

            else { doSearchCase(); }                

 

        }

        else

        {

            //strCurrent = MANAGE_CASE; // The default is the search form.

            //doSearchCase();

            doManageCase();

            //stylesheet = SEARCH_CASE;      

        }

          

        System.err.println("sheet selected: " + strCurrent);

        

        

        /* why can't java have string switches?

        switch (intCurrent) {

        

            case 1: doManageCase();

            case 2: doDefraudedAccount(); 

            case 3: doSearchCase();    

            

            default: doSearchCase();                     

        }

        */

            

      

      } // end runtime data



      /** Output channel content to the portal

       *  @param out a sax document handler

       */

      public void renderXML(ContentHandler out) throws PortalException {

            // Create a new XSLT styling engine

            XSLT xslt = new XSLT(this);

            

            // pass the result XML to the styling engine.

            xslt.setXML(strXML);



            // specify the stylesheet selector

            xslt.setXSL("CAdmin.ssl", stylesheet, runtimeData.getBrowserInfo());



            // set parameters that the stylesheet needs.

            xslt.setStylesheetParameter("baseActionURL",

            runtimeData.getBaseActionURL());



            // set the output Handler for the output.

            xslt.setTarget(out);



            // do the deed

            xslt.transform();

            



      }

      

      

     

    private void doManageCase() {

          



            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<manage_case>" + "Neuragenix Pty Ltd - To be implemented" + "</manage_case>";



        stylesheet = MANAGE_CASE;    

          

        return;

      }

      

      private void doDefraudedAccount() {

          return;

      }

      private void doCreateCase() {

          

        if (runtimeData.getParameter("save") != null) {          

             // Retrive the XML file contents into an XML file structure 

            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<create_case>" + "Neuragenix Pty Ltd - To be implemented" + "</create_case>";            

        } 

        else {            

        

        }

        stylesheet = CREATE_CASE;          

        // Retrive the XML file contents into an XML file structure 

        strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<create_case>" + "Neuragenix Pty Ltd - To be implemented" + "</create_case>";            

        return;

      }

      

       private void doSearchCase() {

          

        if (runtimeData.getParameter("Return") != null) {

            doManageCase();

        } 

        else {   

            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<search_case>" + "Neuragenix Pty Ltd - To be implemented" + "</search_case>";

            stylesheet = SEARCH_CASE; 

        }

           

          

        return;

      }

       

       

      private void doDiary() {

          

        if (runtimeData.getParameter("save") != null) {

            // Retrive the XML file contents into an XML file structure 

            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<diary>" + "Neuragenix Pty Ltd - To be implemented" + "</diary>";      

        } 

        else {   

            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<diary>" + "Neuragenix Pty Ltd - To be implemented" + "</diary>";

      

        }

        stylesheet = DIARY;    

          

        return;

      }





      private void doOffendingBranch() {

          

        if (runtimeData.getParameter("save") != null) {

            // Retrive the XML file contents into an XML file structure 

            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<offending_branch>" + "Neuragenix Pty Ltd - To be implemented" + "</offending_branch>";      

        } 

        else {   

            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

            + "<offending_branch>" + "Neuragenix Pty Ltd - To be implemented" + "</offending_branch>";

      

        }

        stylesheet = OFFENDING_BRANCH;    

          

        return;

      }



}
