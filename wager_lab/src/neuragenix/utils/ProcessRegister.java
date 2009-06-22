/*   

 * Copyright (c) 2003 Neuragenix, All Rights Reserved.

 * ProcessRegister.java

 * Created on 4 March 2003, 00:00  

 * @author  Shendon Ewans

 * 

 * Description: 

 * This class executes 

 *

 */



package neuragenix.utils;



import neuragenix.utils.*;

import neuragenix.utils.exception.*;

import neuragenix.dao.*;



import java.sql.ResultSet;

import java.util.Date;

import java.text.SimpleDateFormat;



import neuragenix.common.LockRequest;

import neuragenix.common.LockRecord;

import neuragenix.security.AuthToken;



/**

 *

 * @author  Shendon

 */

public class ProcessRegister extends Object  {

    

    public final static String PROCESS_DOMAIN = "PROCESS";  

    public final static String TYPE_EMAIL_ALERT = "TYPE_EMAIL_ALERT"; 

    public final static String TYPE_INTEGRATION = "TYPE_INTEGRATION"; 
   
	public final static String TYPE_WORKFLOW_EMAIL_ALERT = "TYPE_WORKFLOW_EMAIL_ALERT"; 

    

    public final static String STATUS_IDLE = "idle"; 

    public final static String STATUS_PROCESSING = "processing.."; 

    public final static String STATUS_TERMINATED = "terminated";   

    public final static String STATUS_COMPLETED = "completed";        

    public final static String STATUS_ERROR = "error";    

    public final static String STATUS_DELETED = "deleted";     

    private LockRequest lr;    

    

    // Primary key for Process Table

    public static final String INTERNAL_PROCESS_KEY = "intProcessKey";

    

    private String PROCESS_TYPE = ""; 

    private String PROCESS_STATUS = "";    

    private AuthToken authToken = null;

    

    

    /** Creates new ProcessRegister */

    public ProcessRegister() {

    }

    

    public ProcessRegister(AuthToken a) {

        authToken = a;

    }

    

    public void setProcessType(String strType) {

        PROCESS_TYPE = strType;        

    }

     public void setProcessStatus(String strStatus) {

        PROCESS_STATUS = strStatus;        

    }

       

    // returns the process key    

    public int insertProcessItem(String strDescription, String strUser) throws ProcessRegisterUnabletoInsert {

        

        DALMasterQuery myQuery;

        

        try {

            myQuery = new DALInsertQuery();



            myQuery.setDomainName(PROCESS_DOMAIN);

            myQuery.setDeletedColumn(true);

            myQuery.setCaseSensitive(false);



            myQuery.setField("strProcessType", PROCESS_TYPE);

            myQuery.setField("strProcessStatus", PROCESS_STATUS);

            myQuery.setField("strProcessDescription", strDescription);        

             

            if (myQuery.execute()) {

                // commit changes

                myQuery.saveChanges();

                return myQuery.getInsertedRecordId();

            } else{                

                throw new ProcessRegisterUnabletoInsert("Unable to insert new record");

            }

        }catch (Exception e) {

            throw new ProcessRegisterUnabletoInsert(e.toString());

        }

    }

	public boolean updateProcessItem(int intProcessKey, 
			 						 String strDescription,
									 String strUser) 
	throws ProcessRegisterUnabletoUpdate, ProcessRegisterNoProcessItem 
	{
        DALMasterQuery myQuery;

        //lr = new LockRequest(authToken);

        try 
		{
             //if(lr != null)
			 //{
			 //	 lr.unlock();
			 //}

             //System.err.println("Locking Process ID :" + intProcessKey);

             //lr.addLock(PROCESS_DOMAIN, Integer.toString(intProcessKey), LockRecord.READ_WRITE);

             //if(lr.lockDelayWrite()) 
			 //{
             	// System.err.println("Locked Process ID :" + intProcessKey);    
                myQuery = new DALUpdateQuery();
                myQuery.setDomainName(PROCESS_DOMAIN);
                myQuery.setDeletedColumn(true);
                myQuery.setCaseSensitive(false);

                try 
				{
                    String strTimeStamp = getProcessTimestamp(intProcessKey);

                    if(!strTimeStamp.equals("")) 
					{
                        myQuery.setTimestamp(strTimeStamp);
                    }

                    //myQuery.setField("intProcessKey", Integer.toString(intProcessKey));

                    myQuery.setField("strProcessType", PROCESS_TYPE);
                    myQuery.setField("strProcessStatus", PROCESS_STATUS);
                    myQuery.setField("strProcessDescription", strDescription);   
                    myQuery.setWhere(DBSchema.AND_CONNECTOR,"intProcessKey", DBSchema.EQUALS_OPERATOR, Integer.toString(intProcessKey));

                    if(myQuery.execute()) 
					{
                        // commit changes
                        myQuery.saveChanges(); 

                        //System.err.println("Unlock ");
                        //lr.unlock();
                        if(myQuery.getUpdatedRecordCount() > 0 ) 
						{ 
							return true;
						}
                        else
						{  
							return false; 
						}   
                    }
                    else 
					{  
                        //System.err.println("error Unlock ");
                        //lr.unlock();
                        return false;  
                    }  
                }
				catch (ProcessRegisterNoProcessItem e) 
				{
                    //lr.unlock();
                    throw new ProcessRegisterNoProcessItem(e.toString());      
                }
				catch (Exception e)
				{
                    //lr.unlock();
                    throw new ProcessRegisterUnabletoUpdate(e.toString());      
                }
             //}
             //else
			 //{
                 //lr.unlock();
                 //System.err.println("error Unlock ");
                 //throw new ProcessRegisterUnabletoUpdate("Unable to lock process table");  
             //}
        }
		catch (Exception e) 
		{
            throw new ProcessRegisterUnabletoUpdate(e.toString() );
        }
    }   

     

    public boolean updateProcessItem(int intProcessKey, String strDescription, String strUser, String strReport) throws ProcessRegisterUnabletoUpdate, ProcessRegisterNoProcessItem {

        

        DALMasterQuery myQuery;

        lr = new LockRequest(authToken);

        

        try {

            

             if (lr != null) lr.unlock();    

             //System.err.println("Locking Process ID :" + intProcessKey);

             lr.addLock(PROCESS_DOMAIN, Integer.toString(intProcessKey), LockRecord.READ_WRITE);



             if (lr.lockDelayWrite()) {

               // System.err.println("Locked Process ID :" + intProcessKey);    

                myQuery = new DALUpdateQuery();



                myQuery.setDomainName(PROCESS_DOMAIN);

                myQuery.setDeletedColumn(true);

                myQuery.setCaseSensitive(false);



                try {

                    String strTimeStamp = getProcessTimestamp(intProcessKey);



                    if (!strTimeStamp.equals("")) {

                        myQuery.setTimestamp(strTimeStamp);

                    }

                    //myQuery.setField("intProcessKey", Integer.toString(intProcessKey));

                    myQuery.setField("strProcessType", PROCESS_TYPE);

                    myQuery.setField("strProcessStatus", PROCESS_STATUS);

                    myQuery.setField("strProcessDescription", strDescription);   

                    myQuery.setField("strProcessReport", strReport);   

                    

                    myQuery.setWhere(DBSchema.AND_CONNECTOR,"intProcessKey", DBSchema.EQUALS_OPERATOR, Integer.toString(intProcessKey));



                    if (myQuery.execute()) {

                        // commit changes

                        myQuery.saveChanges(); 

                        //System.err.println("Unlock ");

                        lr.unlock();

                        if (myQuery.getUpdatedRecordCount() > 0 ) { return true;}

                        else {  return false; }   

                    }

                    else {  

                         //System.err.println("error Unlock ");

                        lr.unlock();

                        return false;  

                    }  

                }catch (ProcessRegisterNoProcessItem e) {

                    lr.unlock();

                    throw new ProcessRegisterNoProcessItem(e.toString());      

                }catch (Exception e){

                    lr.unlock();

                    throw new ProcessRegisterUnabletoUpdate(e.toString());      

                }

             }

             else {

                 lr.unlock();

                 //System.err.println("error Unlock ");

                 throw new ProcessRegisterUnabletoUpdate("Unable to lock process table");  

             }



        }catch (Exception e) {

            

            throw new ProcessRegisterUnabletoUpdate(e.toString() );

        }

  

    }   

    public boolean deleteProcessItem(int intProcessKey, 
									 String strUser)
	throws ProcessRegisterUnabletoUpdate, ProcessRegisterNoProcessItem
	{
        DALMasterQuery myQuery;

        //lr = new LockRequest(authToken);
        try 
		{
             //if (lr != null) lr.unlock();    
             //System.err.println("Locking Process ID :" + intProcessKey);
             //lr.addLock(PROCESS_DOMAIN, Integer.toString(intProcessKey), LockRecord.READ_WRITE);
             //if(lr.lockDelayWrite())
			 //{
               // System.err.println("Locked Process ID :" + intProcessKey);    

             myQuery = new DALUpdateQuery();
             myQuery.setDomainName(PROCESS_DOMAIN);
             myQuery.setDeletedColumn(true);
             myQuery.setCaseSensitive(false);

             try 
			 {
             	String strTimeStamp = getProcessTimestamp(intProcessKey);

                if(!strTimeStamp.equals("")) 
				{
                	myQuery.setTimestamp(strTimeStamp);
                }

                // Set deleted column to -1 for deleted.

                myQuery.setField(DBSchema.DELETED_FIELD, "-1"); 
                myQuery.setWhere(DBSchema.AND_CONNECTOR,"intProcessKey", 
								 DBSchema.EQUALS_OPERATOR, Integer.toString(intProcessKey));

                if(myQuery.execute())
				{
                	// commit changes
                    myQuery.saveChanges(); 

                    //System.err.println("Unlock ");
                    //lr.unlock();

                    if(myQuery.getUpdatedRecordCount() > 0 )
					{ 
						return true;
					}
                    else
					{  
						return false; 
					}   
                }
                else
				{  
                	//System.err.println("error Unlock ");
                    //lr.unlock();
                    return false;  
                }  
            }
			catch(ProcessRegisterNoProcessItem e) 
			{
            	//lr.unlock();
                throw new ProcessRegisterNoProcessItem(e.toString());      
            }
			catch(Exception e)
			{
                //lr.unlock();
                throw new ProcessRegisterUnabletoUpdate(e.toString());      
            }
            //}

            //else
			//{
            //    lr.unlock();
            //    System.err.println("error Unlock ");
            //    throw new ProcessRegisterUnabletoUpdate("Unable to lock process table");  
            //}
        }
		catch(Exception e)
		{
            throw new ProcessRegisterUnabletoUpdate(e.toString() );
        }
    }   
	
    private String getProcessTimestamp(int intProcessKey) throws ProcessRegisterNoProcessItem {

        

        DALMasterQuery mySelectQuery;   

        try {

            mySelectQuery = new DALSelectQuery();



            mySelectQuery.clearAllQuery();

            mySelectQuery.setDomainName(PROCESS_DOMAIN);

            mySelectQuery.setDeletedColumn(true);

            mySelectQuery.setCaseSensitive(false);



            mySelectQuery.setField("intProcessKey", "");        

            mySelectQuery.setField("strProcessType", "");            

            mySelectQuery.setField("strProcessStatus", "");

            mySelectQuery.setField("strProcessDescription", "");            

            mySelectQuery.setWhere("","intProcessKey", DBSchema.EQUALS_OPERATOR, Integer.toString(intProcessKey) );

                      

            if (mySelectQuery.execute()) {

                ResultSet rs = mySelectQuery.getResults();                

                if ((rs != null) && (rs.next())) {

                    String strTimeStamp = rs.getString("PROCESS_TIMESTAMP");

                    return(strTimeStamp);

                }else { throw new ProcessRegisterNoProcessItem("No process item");}

            }

            else { throw new ProcessRegisterNoProcessItem("No process item"); }

        }

        catch (Exception e){

            throw new ProcessRegisterNoProcessItem(e.toString());

        }

    }
}
