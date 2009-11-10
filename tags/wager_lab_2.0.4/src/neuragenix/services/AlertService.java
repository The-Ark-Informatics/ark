/*   

 * Copyright (c) 2003 Neuragenix, All Rights Reserved.

 * AlertService.java

 * Created on 16 January 2003, 00:00  

 * @author  Shendon Ewans

 * 

 * Description: 

 * This class binds with the application server and listens 

 * for requests to start or stop an AlertProcess and WorkflowAlertProcess.

 * An AlertProcess emails appointments out.

 */



package neuragenix.services;



import org.jasig.portal.utils.ResourceLoader;

import org.jasig.portal.services.LogService;



//import neuragenix.security.AuthToken;

import neuragenix.dao.*;

import neuragenix.utils.*;



import java.util.Date;





/**

 * AlertService

 */

public final class AlertService {



  private static boolean bInitialized = false;

  private static AlertProcess oAlert;

  private static boolean blWorkflowAlertProcessInitialized = false;
  private static WorkflowAlertProcess workflowAlertProcess = null;

            
  public   AlertService () {

   //System.err.println("AlertService - Constructing the alert service object BEFORE initalize!!!!");

   //initialize();    

   //System.err.println("AlertService - Constructing the alert service object AFTER initialize!!!!");

  }

  public static boolean startProcess () {        

    try {

        if (!bInitialized){

//System.err.println("AlertService - About to instantiate AlertProcess");

            oAlert = new AlertProcess();

            oAlert.start();

            bInitialized = true;

        }



    } catch (Exception e) {

            String strLogError = "Error initialising Alert Service - Exception " + e.toString();

            LogService.log(LogService.ERROR, strLogError);      

    } catch (Error er) {

            String strLogError = "Error initialising Alert Service - Error " + er.toString();

            LogService.log(LogService.ERROR, strLogError);      

    }

    return bInitialized;    

  }

  public static boolean startWorkflowAlertProcess()
  {        
    try
	{
        if(!blWorkflowAlertProcessInitialized)
		{
			//System.err.println("AlertService - About to instantiate WorkflowAlertProcess");
            workflowAlertProcess = new WorkflowAlertProcess();
            workflowAlertProcess.start();
            blWorkflowAlertProcessInitialized = true;
        }
    }
	catch(Exception e)
	{
		String strLogError = "Error initialising Workflow Alert Service - Exception " + e.toString();
        LogService.log(LogService.ERROR, strLogError);      
    }
	catch(Error er)
	{
    	String strLogError = "Error initialising Workflow Alert Service - Error " + er.toString();
        LogService.log(LogService.ERROR, strLogError);      
    }

    return blWorkflowAlertProcessInitialized;    
  }


  public static boolean stopProcess () {

     if (bInitialized) {         

//System.err.println("AlertService - About to stop AlertProcess");

        oAlert.stopProcess();        

        bInitialized = false;

        return true; 

     }

     else {

        return false;   

     }

  } 

  public static boolean stopWorkflowAlertProcess()
  {
     if(blWorkflowAlertProcessInitialized)
	 {         
		//System.err.println("AlertService - About to stop WorkflowAlertProcess");
        workflowAlertProcess.stopProcess();        
        blWorkflowAlertProcessInitialized = false;
        return true; 
     }
     else
	 {
        return false;   
     }
  }

  public static boolean IsStarted () {

     if (bInitialized) {         

        return oAlert.IsStarted();        

     }

     else {

        return false;   

     }

  }

  public static boolean isWorkflowAlertProcessStarted()
  {
     if(blWorkflowAlertProcessInitialized)
	 {
		//System.err.println("AlertService - the Workflow Alert Process is started/initialized");
        return workflowAlertProcess.isStarted();
     }
     else
	 {
        return false;
     }
  }

  private final void initialize() 
  {
	/*
  	//System.err.println("AlertService - initialize Alert Process!!!!");

	try
	{
		if(!bInitialized)
		{
			oAlert = new AlertProcess();
			oAlert.start();
			bInitialized = true;
		}
	}
	catch(Exception e)
	{
		String strLogError = "Error initialising Alert Service - Exception " + e.toString();
		LogService.log(LogService.ERROR, strLogError);      

	}
	catch(Error er)
	{
		String strLogError = "Error initialising Alert Service - Error " + er.toString();
		LogService.log(LogService.ERROR, strLogError);      
	}

	//System.err.println("AlertService - initialize Workflow Alert Process!!!!");

	try
	{
        if(!blWorkflowAlertProcessInitialized)
		{
            workflowAlertProcess = new WorkflowAlertProcess();
            workflowAlertProcess.start();
            blWorkflowAlertProcessInitialized = true;
        }
    }
	catch(Exception e)
	{
		String strLogError = "Error initialising Workflow Alert Service - Exception " + e.toString();
        LogService.log(LogService.ERROR, strLogError);      
    }
	catch(Error er)
	{
    	String strLogError = "Error initialising Workflow Alert Service - Error " + er.toString();
        LogService.log(LogService.ERROR, strLogError);      
    }

    return;
	*/
  }

}

 





