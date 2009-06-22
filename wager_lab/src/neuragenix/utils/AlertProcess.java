/*   

 * Copyright (c) 2003 Neuragenix, All Rights Reserved.

 * AlertProcess.java

 * Created on 16 January 2003, 00:00  

 * @author  Shendon Ewans

 * 

 * Description: 

 * This class executes the reading of appointments, emailing and updates

 * to the appointment table. This class is suppose to run as a thread,

 * running every interval with a sleep cycle until an interrupt is raised.

 *

 */



package neuragenix.utils;





//import neuragenix.utils.TestFrame;



import neuragenix.utils.*;

import neuragenix.utils.exception.*;

import neuragenix.dao.*;

import neuragenix.common.LockRequest;

import neuragenix.common.LockRecord;

import neuragenix.common.Utilities;



//import neuragenix.security.PassAuthToken;

import org.jasig.portal.services.LogService;

import neuragenix.utils.Property;



import java.sql.ResultSet;

import java.util.Date;

import java.text.SimpleDateFormat;

/**

 *

 * @author  Administrator

 */

public class AlertProcess extends Thread {

//public class AlertProcess implements Runnable {

    

    private final static String PATIENT_DOMAIN = "PATIENT";

    private final static String APPOINTMENT_DOMAIN = "APPOINTMENTS";

    private final static String PROCESS_DOMAIN = "PROCESS";

    private final static String CONFIGURATION_DOMAIN = "CONFIGURATION";

    private final static String APPOINTMENT_TIMESTAMP = "APPOINTMENTS_TIMESTAMP";

    private final static String EMAIL_APPOINTMENTS = "EMAIL_APPOINTMENTS";

    private final static String EMAIL_SUBJECT = "EMAIL_SUBJECT";    

    private String ADMINISTRATOR_EMAIL = "";

    private String SMTP_SERVER = "";   

    private int DELAYSECONDS = 10000;  

    

    private static boolean  KEEP_PROCESS = false;   

    private static boolean STARTED = false;   

    //private LockRequest lr;    

    private String strMessageTemplate;

    private String strMessageSubject;

    

    private ProcessRegister oProcessReg = new ProcessRegister();

    private int intProcessKey = 0;

        



    public AlertProcess () {

        //System.err.println("Intialising alert process");   

    }



    public boolean IsStarted() {        

        return this.STARTED;

    }

    public void run ()

    {

        // Wroking variables

        Email clEmail;

        LockRequest AppointmentLock = new LockRequest();

        int intAppPatientID = 0;    

        String strPatientID = "";

        String strMedicareNo= "";

        String strHospitalUR= "";

        String strTitle= "";

        String strSurname= "";

        String strFirstName= "";

                            

                            

        //System.out.println("Starting process");

        try {

            KEEP_PROCESS = true;

            STARTED = true;

            

            // Retrieve settings from neuragenix.properties file

            ADMINISTRATOR_EMAIL = Property.getProperty("neuragenix.utils.AlertProcess.AdministratorEmail");

            SMTP_SERVER = Property.getProperty("neuragenix.utils.AlertProcess.SMTPServer");  

            DELAYSECONDS = Property.getPropertyAsInt("neuragenix.utils.AlertProcess.DelaySeconds"); 

                

            strMessageTemplate = getEmailTemplate(EMAIL_APPOINTMENTS);

            strMessageSubject = getEmailTemplate(EMAIL_SUBJECT);

            //System.err.println("strMessageTemplate="+strMessageTemplate);

            //System.err.println("strMessageSubject="+strMessageSubject);   

       

            try {

                

                oProcessReg.setProcessType(oProcessReg.TYPE_EMAIL_ALERT);

                oProcessReg.setProcessStatus(oProcessReg.STATUS_IDLE);

                intProcessKey = oProcessReg.insertProcessItem("Commenced","none");

            

                while (KEEP_PROCESS) {                       

                    // Update Process Table as Running

                    try {

                        oProcessReg.setProcessType(oProcessReg.TYPE_EMAIL_ALERT);

                        oProcessReg.setProcessStatus(oProcessReg.STATUS_PROCESSING);

                        boolean  blResult = oProcessReg.updateProcessItem(intProcessKey,"Processing","none");

                    } 

                    catch (ProcessRegisterUnabletoUpdate e) { KEEP_PROCESS = false; } 

                    catch (ProcessRegisterNoProcessItem e) { KEEP_PROCESS = false; }

                    catch (Exception e){

                        LogService.log(LogService.ERROR, "Unable to register a Job Process Entry.");                

                    }           



                    //System.err.println ("Wake Thread ID:" + Thread.currentThread() ); 



                    // Retrieve all appointments for mail out    

                    ResultSet rs = getAppointmentRecords();

                    while( rs != null && rs.next() ) {



                        // Clear all working variables for a new email

                        clEmail = new Email();

                        String  strAppointmentID = rs.getString("intAppointmentID");

                        intAppPatientID = 0;    

                        strPatientID = "";

                        strMedicareNo= "";

                        strHospitalUR= "";

                        strTitle= "";

                        strSurname= "";

                        strFirstName= "";



                        try {                               

                            intAppPatientID = Integer.parseInt(rs.getString("intAppPatientID")); 

                        }

                        catch(Exception e){

                            intAppPatientID = 0;

                        }



                        if (intAppPatientID != 0)  {

                            //System.err.println ("Appointment ID: " + strAppointmentID );

                            // Retrieve a single patient record corresponding to the appointment

                            // being processed.

                            ResultSet rsPatients = getPatientRecord(intAppPatientID);

                            if( rsPatients != null && rsPatients.next() ) {



                                strPatientID = rsPatients.getString("strPatientID");

                                strMedicareNo = rsPatients.getString("strMedicareNo");

                                strHospitalUR = rsPatients.getString("strHospitalUR");              

                                strTitle = rsPatients.getString("strTitle");

                                strSurname = rsPatients.getString("strSurname");

                                strFirstName = rsPatients.getString("strFirstName");   

                                // Send Email

                                String[] strAddress = new String[1];

                                //System.err.println ("Building an email");



                                // Debugging mode

                                if (Property.getPropertyAsBoolean("neuragenix.utils.AlertProcess.EmailDebugMode")) { 

                                    strAddress[0] = ADMINISTRATOR_EMAIL;  

                                } //production mode

                                else {

                                    strAddress[0] = rs.getString("strAppNotify");  

                                }                                

                                String strFrom = Property.getProperty("neuragenix.utils.AlertProcess.AppointmentReturnEmail");

                                String strMessage = getMessageBody(strTitle,strFirstName,strSurname,strHospitalUR,rs.getString("dtAppDate"),rs.getString("tmAppTime"),rs.getString("strAppPurpose"));

                                //System.err.println ("Message constructed");

                                // Clear all settings 

                                clEmail.clearEmail();               



                                clEmail.setSMTPServer(SMTP_SERVER);              

                                clEmail.setRecipientsTo(strAddress);

                                clEmail.setFrom(strFrom);

                                clEmail.setSubject(strMessageSubject);

                                clEmail.setMessage(strMessage);    



                                clEmail.setSentDate( new Date() );



                                try {

                                    //System.err.println ("Attempting to unlock appointment");

                                    // lock the appointment record                                     

                                    if (AppointmentLock != null) AppointmentLock.unlock();                                      



                                    AppointmentLock.addLock(APPOINTMENT_DOMAIN, strAppointmentID, LockRecord.READ_WRITE);

                                    //System.err.println("Attempting to lock appointment :" + APPOINTMENT_DOMAIN + " " + strAppointmentID);

                                    if (AppointmentLock.lockDelayWrite()) {                                   

                                        //System.err.println("lock successful");

                                        // Send the thing

                                        clEmail.sendEmail();   

                                        //Update the database for single record

                                        boolean blResults = markAppointmentSent(strAppointmentID, rs.getString(APPOINTMENT_TIMESTAMP) );



                                        // unlock the appointment record

                                        AppointmentLock.unlock();

                                    } else {                                        

                                        // Mark or log appointment for resending   

                                       LogService.log(LogService.ERROR, "Appointment ID :" + strAppointmentID + " was not sent due to record-locking or email service errors.");                     

                                    }                                   

                                }

                                catch (Exception e) 

                                {                   

                                    //System.err.println("MessagingException error. " + e.toString() ); 

                                    LogService.log(LogService.ERROR, "MessagingException error. " + e.toString());                

                                }        

                            }

                        }

                        else {

                            //System.err.println ("Error sending appointment emails. No Patient ID for Appointment :" + strAppointmentID);

                             LogService.log(LogService.ERROR, "Error sending appointment emails. No Patient ID for Appointment :" + strAppointmentID);                                                   

                        }         



                    } // While



                    // Update process table

                    oProcessReg.setProcessType(oProcessReg.TYPE_EMAIL_ALERT);

                    oProcessReg.setProcessStatus(oProcessReg.STATUS_IDLE);

                    Date dtDateTime = new Date();

                    dtDateTime.setTime(dtDateTime.getTime() + DELAYSECONDS);               



                    String strTime = (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")).format(dtDateTime);

                    boolean  blResult = oProcessReg.updateProcessItem(intProcessKey,"Idle. Next scheduled processing at:" + strTime,"none");



                    // Set the next delay

                    Thread.sleep(DELAYSECONDS*1000);



                } // while                 

                

            }                    

            catch (InterruptedException e) {        

               LogService.log(LogService.ERROR, "InterruptedException error. " + e.toString());                

            }

            catch (Exception e){            

                //System.err.println("ProcessRegisterUnabletoInsert");

                LogService.log(LogService.ERROR, "Unable to register a Job Process Entry.");                

            }                               

            

        } catch (Exception e) {

              //System.err.println("Error executing Alert Process");

              e.printStackTrace();              

              LogService.log(LogService.ERROR, "Exception executing Alert Process " + e.toString());                

        } catch (Error er) {

              //System.err.println("Error executing Alert Process");

              er.printStackTrace();

              LogService.log(LogService.ERROR, "Error executing Alert Process " + er.toString());                

        } 

    }

    /*

     * Get a recordset of appointments

     */

    private ResultSet getAppointmentRecords() {

        

        DALMasterQuery myQuery;        

        try {

            myQuery = new DALSelectQuery();



            myQuery.setDomainName(APPOINTMENT_DOMAIN);

            myQuery.setDeletedColumn(true);

            myQuery.setCaseSensitive(false);



            myQuery.setField("intAppointmentID", "");

            myQuery.setField("intAppPatientID", "");

            myQuery.setField("dtAppDate", "");

            myQuery.setField("dtAppAlertDate", "");

            myQuery.setField("intAppointmentID", "");

            myQuery.setField("tmAppTime", "");

            myQuery.setField("strAppNotify", "");

            myQuery.setField("strAppPurpose", "");

            myQuery.setField("strSentStamp", "");

       

            myQuery.setWhere("","dtAppAlertDate", DBSchema.EQUALS_OPERATOR, (new SimpleDateFormat("dd/MM/yyyy")).format(new Date()));

            myQuery.setWhere(DBSchema.AND_CONNECTOR,"strSentStamp", DBSchema.LIKE_OPERATOR, "pending");

            

            if (myQuery.execute()) {   return (myQuery.getResults());      } 

            else {  return (null);  }   



        }catch (Exception e) {

            return null;

        }

    }

    /*

     * Retrieve the Patient record for an appointment id

     */

     private ResultSet getPatientRecord(int intAppPatientID) {

        

        DALMasterQuery myQuery;

        

        try {

            myQuery = new DALSelectQuery();



            myQuery.setDomainName(PATIENT_DOMAIN);

            myQuery.setDeletedColumn(true);

            myQuery.setCaseSensitive(false);



            myQuery.setField("intInternalPatientID", "");

            myQuery.setField("strPatientID", "");

            myQuery.setField("strMedicareNo", "");

            myQuery.setField("strHospitalUR", "");

            myQuery.setField("strTitle", "");

            myQuery.setField("strSurname", "");

            myQuery.setField("strFirstName", "");

            myQuery.setField("dtDob", "");

            myQuery.setField("strAddressLine1", "");

            myQuery.setField("strAddressSuburb", "");

            myQuery.setField("strAddressState", "");

            myQuery.setField("strAddressOtherState", "");

            myQuery.setField("strAddressCountry", "");

            myQuery.setField("strAddressOtherCountry", "");

            myQuery.setField("intAddressPostCode", "");

            myQuery.setField("intPhoneHome", "");

            myQuery.setField("intPhoneWork", "");

            myQuery.setField("intPhoneMobile", "");

            myQuery.setField("strEmail", "");

            myQuery.setField("strStatus", "");

            myQuery.setField("strHospital", "");

      

            myQuery.setWhere("","intInternalPatientID", DBSchema.EQUALS_OPERATOR, Integer.toString(intAppPatientID));

            

            if (myQuery.execute()) {    return (myQuery.getResults());      } 

            else {    return (null);       }   



        }catch (Exception e) {

            return null;

        }

    }

    /*

     * Mark an appointment as sent after the email has been sent.

     */

    private boolean markAppointmentSent(String strAppointmentID, String strTimeStamp) {

        

        DALMasterQuery myQuery;

        

        try {

            myQuery = new DALUpdateQuery();



            myQuery.setDomainName(APPOINTMENT_DOMAIN);

            myQuery.setDeletedColumn(true);

            myQuery.setCaseSensitive(false);



            myQuery.setField("strSentStamp", (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")).format(new Date()) ); 

            myQuery.setWhere("","intAppointmentID", DBSchema.EQUALS_OPERATOR, strAppointmentID);

            myQuery.setTimestamp(strTimeStamp);

            

            boolean blResult =  myQuery.execute();

            

            // commit changes

            myQuery.saveChanges();

            

            if (myQuery.getUpdatedRecordCount() > 0 ) {      return blResult;       }

            else {  return false;       }



        }catch (Exception e) {

            return false;

        }

    }

    /*

     * Stop the process thread. 

     */

    public void stopProcess(){  

        

        try {

            KEEP_PROCESS = false;     

            STARTED = false;

            // Update Process Table as terminated

            oProcessReg.setProcessType(oProcessReg.TYPE_EMAIL_ALERT);

            oProcessReg.setProcessStatus(oProcessReg.STATUS_TERMINATED);

            

            boolean  blResult = oProcessReg.updateProcessItem(intProcessKey,"Process terminated by user.","none");

            



        } catch (ProcessRegisterUnabletoUpdate e) {

             LogService.log(LogService.ERROR, "Exception ProcessRegisterUnabletoUpdate" + e.toString());                

        }

        catch(Exception e){

            LogService.log(LogService.ERROR, "Exception " + e.toString());   

        }        

    }

    /*

     * Assemble the message body for one email message

     */

    private String getMessageBody(String strTitle,String strFirstName,String strSurname,String strHospitalUR,String dtAppDate,String tmAppTime,String strAppPurpose){

        

        StringBuffer strBufMessage = new StringBuffer();

  

        String cstrTitle = "<+strTitle+>";

        String cstrFirstName = "<+strFirstName+>";

        String cstrSurname = "<+strSurname+>";        

        String cstrHospitalUR = "<+strHospitalUR+>";

        String cdtAppDate = "<+dtAppDate+>";

        String ctmAppTime = "<+tmAppTime+>";

        String cstrAppPurpose = "<+strAppPurpose+>";       

        

        strBufMessage.insert(0, strMessageTemplate);   

        Utilities.substrReplace(strBufMessage, cstrTitle, strTitle);

        Utilities.substrReplace(strBufMessage, cstrFirstName, strFirstName);

        Utilities.substrReplace(strBufMessage, cstrSurname, strSurname);

        Utilities.substrReplace(strBufMessage, cstrHospitalUR, strHospitalUR);

        Utilities.substrReplace(strBufMessage, cdtAppDate, dtAppDate);

        Utilities.substrReplace(strBufMessage, ctmAppTime, tmAppTime);

        Utilities.substrReplace(strBufMessage, cstrAppPurpose, strAppPurpose);

  

        String strMessageResult = strBufMessage.toString(); 

        

        return strMessageResult;

    }

    /*

     * Retrieve the Email template from Configuration table

     */

    private String getEmailTemplate(String strType) {

        

        DALMasterQuery myQuery;

        

        try {

            

            myQuery = new DALSelectQuery();

            myQuery.setDomainName(CONFIGURATION_DOMAIN);

            myQuery.setDeletedColumn(true);

            myQuery.setCaseSensitive(false);



            myQuery.setField("intConfigKey", "");

            myQuery.setField("strConfigName", "");

            myQuery.setField("strConfigValue", "");     

            myQuery.setWhere("","strConfigName", DBSchema.LIKE_OPERATOR, strType);

            

            if (myQuery.execute()) {                

                ResultSet rsConfig = myQuery.getResults();

                if ( rsConfig != null ) { 

                    rsConfig.next();

                    return rsConfig.getString("strConfigValue");     

                }                

                else { return null;}                                    

            } else { return null; }   



        }catch (Exception e) {

            return null;

        }

    }

}

