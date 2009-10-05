/* 

 * Copyright (c) 2003 Neuragenix. All Rights Reserved.

 * Email.java

 * Created on 1 February 2003, 00:00

 * @author  Shendon Ewans

 * 

 * Description: 

 * Class for sending emails

  */

package neuragenix.utils;



import java.beans.*;

import javax.mail.*;

import javax.mail.internet.*;

import java.util.*; 

import java.io.*;



/**

 *

 * @author  Shendon

 */

public class Email extends Object implements java.io.Serializable {

    

    private static final String SMTP_HOST = "mail.smtp.host";        

    private static final String C_NEURAGENIX_HEADER = "Neuragenix Mail Generator";

    

    private String strRecipientsTo[];

    private String strRecipientsCC[] = new String[0];

    private String strRecipientsBCC[] = new String[0];   

    InternetAddress[] addressCC;

    InternetAddress[] addressBCC;

    private String strFrom;

    private String strSubject;

    private String strMessage;      

    private String strSMTPServer;              

    private String strUsername;      

    private String strPassword;   

    private Date dtSentDate;

    

    public void setUsername(String username) {        

        strUsername = username;

    }

    public void setPassword(String password) {        

        strPassword = password;

    }

    public void setRecipientsTo(String RecipientsTo[]) {        

        strRecipientsTo = RecipientsTo;

    }

    public void setRecipientsCC(String RecipientsCC[]) {        

        strRecipientsCC = RecipientsCC;

    }

    

    public void setRecipientsBCC(String RecipientsBCC[]) {        

        strRecipientsBCC = RecipientsBCC;

    }

    

    public void setFrom(String from) {        

        strFrom = from;

    }

    public void setSubject(String subject) {        

        strSubject = subject;

    }

    public void setMessage(String message) {        

        strMessage = message;

    }

    public void setSMTPServer(String SMTPServer) {        

        this.strSMTPServer = SMTPServer;

    }

    public void setSentDate(Date SentDate) {        

        this.dtSentDate = SentDate;

    }

    /** Creates new Email */

    public Email() {

        //propertySupport = new PropertyChangeSupport( this );

         clearEmail();

    }

    /** Clear Email Settings */

    public void clearEmail() {

        

        strRecipientsTo = null;

        strRecipientsCC = null;

        strRecipientsBCC = null;

        strFrom = "";

        strSubject = "";

        strMessage = "";      

        strSMTPServer = "";          

        strUsername = "";          

        strPassword = "";              

        dtSentDate = null;

    }    

    

    // Send emails

    public void sendEmail( ) throws MessagingException

    {

        Session session;

        

        //Set the host smtp address

        Properties props = new Properties();

        props.put(SMTP_HOST, strSMTPServer);

		//to tell the world who am i
		props.put("mail.smtp.localhost", "neuragenix.com");

        

        if ( (strUsername != null) && (strPassword != null) && (strUsername.length() > 0) && (strPassword.length() > 0) ) {


            props.put("mail.smtp.auth","true");               

            extAuthenticator oAuth = new extAuthenticator(strUsername, strPassword);        

        

            session = Session.getInstance(props, oAuth);

        }

        else {        

            session = Session.getDefaultInstance(props, null);

            //session.setDebug(true);            

        }

        

            

        // create a message

        Message msg = new MimeMessage(session);



        // set the from and to address

        InternetAddress addressFrom = new InternetAddress(strFrom);

        msg.setFrom(addressFrom);



        InternetAddress[] addressTo = new InternetAddress[strRecipientsTo.length]; 

        if(strRecipientsCC != null)

        {

            addressCC = new InternetAddress[strRecipientsCC.length]; 

        }

        if(strRecipientsBCC != null)

        {

            addressBCC = new InternetAddress[strRecipientsBCC.length]; 

        }

        

        // Log Sent message

        //System.out.println("Message To:");

            

        for (int i = 0; i < strRecipientsTo.length; i++)

        {

            addressTo[i] = new InternetAddress(strRecipientsTo[i]);

            // Log Sent message

            //System.out.println(addressTo[i]);

        } 

        if(strRecipientsCC != null)

        {

            for (int i = 0; i < strRecipientsCC.length; i++)

            {

                addressCC[i] = new InternetAddress(strRecipientsCC[i]);

            }         

        }

        if(strRecipientsBCC != null)

        {

            for (int i = 0; i < strRecipientsBCC.length; i++)

            {

                addressBCC[i] = new InternetAddress(strRecipientsBCC[i]);

            }  

        }

        msg.setRecipients(Message.RecipientType.TO, addressTo);

        if(strRecipientsCC != null)

        {  

            msg.setRecipients(Message.RecipientType.CC, addressCC);

        }

        if(strRecipientsBCC != null)

        {

            msg.setRecipients(Message.RecipientType.BCC, addressBCC);

        }

        

        // Log Sent message

        //System.out.println("Message From:" + addressFrom);

        //System.out.println("Message Subject:" + strSubject);

        //System.out.println("Message Body:" + strMessage);

        

        //Set standard Neuragenix Header

        msg.addHeader("Header", C_NEURAGENIX_HEADER);



        // Setting the Subject and Content Type

        msg.setSubject(strSubject);

        msg.setContent(strMessage, "text/plain");

        msg.setSentDate(dtSentDate);

        

        

        try {

            Transport.send(msg);

        } catch (Exception e) {

            System.out.println("Unable to send email. " + e.toString());
        }

        

        

    }

    

    class extAuthenticator extends javax.mail.Authenticator{

        private String userName = null;

        private String password = null;



        public extAuthenticator(String userName, String password) {

            this.userName = userName;

            this.password = password;

        }

        public PasswordAuthentication getPasswordAuthentication(){

            return new PasswordAuthentication(userName, password);

        }

    }



}

