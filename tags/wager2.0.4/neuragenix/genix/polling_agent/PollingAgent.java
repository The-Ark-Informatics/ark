/*
 * PollingAgent.java
 *
 * Created on 10 January 2005, 15:45
 */

package neuragenix.genix.polling_agent;

// import apache packages for Logging and HTTP client
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

// import java xml parsing and email packages 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.mail.*;
import javax.mail.internet.*;

// import java packages for I/O
import java.util.Properties;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Neuragenix Polling Agent is a small piece of software that polls a particular 
 * directory for new XML files exported by Mantas, then transform to Neuragenix's 
 * Casegenix XML format for importing to Casegenix Application server. All the
 * configurations are located in a propertie file specified in the command line.
 *
 * Run without parameter for more details
 *
 * @author Long Tran, ltran@neuragenix.com
 *
 */
class PollingAgent {
    
    private static PropertiesManager pm;
    static Logger logger = Logger.getLogger( PollingAgent.class.getClass() );
	private static boolean isPolling = false;
	private static Timer timer;
    
	static class PollingTask extends TimerTask {
			public void run() {

					// check if the task is running. This is important as it will make sure the polling agent
					// finish the current job
					if( !isPolling ){
						if( PollingAgent.isStopped() == null ){
								cancel();
								PollingAgent.exit();
						}
						isPolling = true;

						// check if user has triggered a "Stop"	
							File dataDirectory = new File( pm.getProperty( "neuragenix.genix.polling_agent.dataDirectory" ) );
							if( dataDirectory.isDirectory() ){
								File[] fileList = dataDirectory.listFiles();
								for( int i=0; i < fileList.length ; i++ ){
									File file = fileList[i];

									// look for files in the folder
									if( file.getName().endsWith( ".xml" )){

										if( PollingAgent.isStopped() != null ){
										// post this file
											postFile( file.getAbsolutePath() );
										}else{
											cancel();
											PollingAgent.exit();
										}
									}
								}
							}
							
							// log into the log file anyway.
							logger.debug( "Neuragenix Polling Agent has started instance and finished." );
							isPolling = false;
					}
							
			}
			
	}

    /** Creates a new instance of PollingAgent */
    public static void main(String args[] ) throws Exception{
        
        if( args.length < 1 ){
            System.out.println("Usage: java PollingAgent /path/to/polling/agent/configuration/polling.properties start|stop");
            return;
        }else{
			if( (args.length > 1 ) && (args[1].indexOf( "stop" ) > -1 )){
				// delete pid file
				File pFile = new File( "PollingAgent.pid" );
				pFile.delete();
				//logger = Logger.getLogger( PollingAgent.class.getClass() );
				//logger.info( "Stopping Polling Agent... ");

				System.out.println("Please wait...");
				Thread.sleep(10000);
				System.out.println("Polling Agent stopped.");

			}else{	
				String testRunning = isStopped();
				if( testRunning != null ){

						System.out.println( "ERROR: " + testRunning );
						return;	

				}
				
				FileOutputStream pFile = new FileOutputStream("PollingAgent.pid");
				pFile.write( (byte) 1);
				pFile.close();
				pFile = null;

            	// get properties from the conf file.
            	pm = new PropertiesManager( args[0]);
            	// BasicConfigurator replaced with PropertyConfigurator.
            	PropertyConfigurator.configure( args[0] );
				timer = new Timer();
				timer.schedule(new PollingTask(), 0, pm.getPropertyAsInt("neuragenix.genix.polling_agent.PollingFrequency") * 1000 );
			}
		}
        
    }

	// Method to exit Polling Agent
	private static void exit(){
			timer.cancel();
	}

	// Method to check is the polling agent is running, by checking PollingAgent.pid file
	private static String isStopped(){
		String strResult = null;

		File pFile = new File( "PollingAgent.pid" );
			
		if( pFile.exists() ){
				strResult = "Polling Agent may be running. Or please delete PollingAgent.pid and start Polling Agent again."; 
		}
		pFile = null;

		return strResult;
	}
    
    /**
     *
     *  Method to transform Mantas XML file to Casegenix XML and then post to 
     * a Casegenix application server for importing
     *
     * @param strFile File name to be processed
     *
     **/
    
    private static void postFile( String strFile ){
        
        String strError = null;
        MultipartPostMethod filePost = null;
        
        try{
			logger.info( "Processing: " + strFile );
        
            // Instantiate the TransformerFactory, and use it along with a StreamSource
            // XSL stylesheet to create a Transformer.
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource( PollingAgent.class.getResourceAsStream( 
                    pm.getProperty( "neuragenix.genix.polling_agent.mappingXSLFile" ) ) ));
            
            // Perform the transformation from a StreamSource to a StreamResult;
			transformer.setParameter("currentTimeMillis", "" + System.currentTimeMillis() );
            transformer.transform(new StreamSource( strFile ),
                              new StreamResult(new FileOutputStream( strFile + ".xmlc")));
            
            // prepare to post the file
            filePost = new MultipartPostMethod( pm.getProperty( "neuragenix.genix.polling_agent.dataCollectorURL" ) + "?username=" + 
							pm.getProperty("neuragenix.genix.polling_agent.username") + "&password=" + pm.getProperty("neuragenix.genix.polling_agent.password"));

            // construct the parameter in the HTTP client.
            filePost.addParameter("alertFile", new File( strFile + ".xmlc" ) );
			
            HttpClient client = new HttpClient();
            client.setConnectionTimeout(120000); // 2 minutes
			
			int postCount = 1;
			boolean notPosted = true;
            
            // post now
			while( postCount < 6 && notPosted ){

					int status = 1; 

					try{
							status = client.executeMethod(filePost);
					}catch(Exception e){ 
						logger.info( "Exception when posting file: " + e.toString() );	
					}

					if (status == HttpStatus.SC_OK) {
						notPosted = false;
						BufferedReader dis = new BufferedReader( new java.io.InputStreamReader(filePost.getResponseBodyAsStream()));
						StringBuffer strResults = new StringBuffer();
						String s;

						// get the response
						while (( s = dis.readLine()) != null) {
							strResults.append( s );
							
						}
						String newFileName = "";
						// check if there is error
						if( strResults.indexOf( "<error>" ) > -1 || strResults.indexOf ("status=\"processed\">") == -1 ){
							logger.info( "Fail to process: " + strFile );
							strError = strResults.toString();
							newFileName = strFile + ".error";
						}else{
							
							// if it's ok, rename the file so it doesn't get reposted
							newFileName = strFile + ".processed";
						}

							File newFile = new File( newFileName );
							if( newFile.exists() ){
								newFile = new File( newFileName );
								newFile.delete();
							}
							newFile = null;
							newFile = new File( strFile );
							newFile.renameTo( new File(newFileName) );
                
						
						// log the response as info
						logger.info( "Polling Agent has successfully negotiated with Casegenix application server (error on" + 
								" the server may occur), response: " + strResults.toString() );

					} else {
						// log the response as error
						strError = "Connect to Casegenix application server failed. Suspect that the server is down. Response: " + HttpStatus.getStatusText(status);
						logger.info( "Server is down, attemp " + postCount++  ); 
						try{
							Thread.sleep(30000);
						}catch( Exception uwe){}
					}
			}
            
        // Catching all the exception here
        }catch( javax.xml.transform.TransformerConfigurationException tce){
            strError = "Cannot configure transformer parameters." + tce.toString();
			tce.printStackTrace();
        }catch( java.io.FileNotFoundException fnf){
            strError = "Cannot create Casegenix-Ready document. Probably due to permission settings of the directory containing alert data.";
			fnf.printStackTrace();
        }catch( javax.xml.transform.TransformerException te){
            strError = "Cannot transform Alert document.";
			te.printStackTrace();
        }catch( java.io.IOException ioe){
            strError = "Cannot connect Casegenix application server at " + pm.getProperty( "neuragenix.genix.polling_agent.dataCollectorURL" );
			ioe.printStackTrace();
        }finally{
            // release the connection
            if( filePost != null ) filePost.releaseConnection();
            if( strError != null ){
                sendmail( strError );
                
                // Log the error
                logger.error( strError );
            } 
        }
    }
    
    /**
     * Method to get information for an email such as mailserver, recipents, etc.
     * then send an email to the administrator when anything goes wrong
     * with the import of data.
     *
     * @param msg Email body message
     * @return Status of sending mail
     *
     **/
    private static boolean sendmail(String msg){
        
        // get the information dynamically from a property file
        String strSMTPServer = pm.getProperty( "neuragenix.genix.polling_agent.smtpServer" );
        String strUsername = pm.getProperty( "neuragenix.genix.polling_agent.smtpUserName" );
        String strPassword = pm.getProperty( "neuragenix.genix.polling_agent.smtpPassword" );
        String strTo = pm.getProperty( "neuragenix.genix.polling_agent.smtpTo" );
        String strFrom = pm.getProperty( "neuragenix.genix.polling_agent.smtpFrom" );
        String strSubject = pm.getProperty( "neuragenix.genix.polling_agent.smtpSubject" );
        
        // check for basic information
        if( (strSMTPServer == null) && (strTo == null) && (strFrom == null) && (strSubject == null)){
            System.err.println( "Could not get email information from properties file. " );
            return false;
        }
        
        String[] strTos = new String[1];
        strTos[0] = strTo;
        
        // send the mail with the information from the property file
        return sendmail( strSMTPServer, strUsername, strPassword, strFrom, strTos,
                strSubject, msg );
    }
    
    /**
     * This method is the actual method that send the email using javax.mail package
     *
     * @param strSMTPServer The SMTP server used for sending mails
     * @param strUsername Username for authenticating to the server, if required.
     * @param strPassword Passord for authenticating to the server, if required.
     * @param strFormField The sender's email.
     * @param strToField The recipents.
     * @param strSubject The subject of the email.
     * @param strMsg The body of the email
     *
     * @return Status of sending the email.
     *
     **/
    
    private static boolean sendmail(String strSMTPServer, String strUsername, String strPassword, String strFromField, 
        String[] strToField, String strSubject, String strMsg){
        
        try{
            
            Session session;

            //Set the host smtp address
            Properties props = new Properties();
            props.put("mail.smtp.host", strSMTPServer);

            //to tell the world who am i
            props.put("mail.smtp.localhost", "neuragenix.com");

            if ( (strUsername != null) && (strPassword != null) && (strUsername.length() > 0) && ( strPassword.length() > 0 )) {

					System.err.println("Auth:" );
                props.put("mail.smtp.auth","true"); 
                extAuthenticator oAuth = new extAuthenticator(strUsername, strPassword);    
                session = Session.getDefaultInstance(props, oAuth);
            }else {        
                props.put("mail.smtp.auth","false"); 
                extAuthenticator oAuth = new extAuthenticator("", "");    
                session = Session.getDefaultInstance(props, oAuth);
            }

            // create a message
            Message msg = new MimeMessage(session);

            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(strFromField);
            msg.setFrom(addressFrom);

            // building the recipents list.
            InternetAddress[] addressTo = new InternetAddress[strToField.length]; 
            for (int i = 0; i < addressTo.length; i++)
            {
                addressTo[i] = new InternetAddress(strToField[i]);
            } 
            msg.setRecipients(Message.RecipientType.TO, addressTo);


            // Setting the Subject and Content Type
            msg.setSubject(strSubject);
            msg.setContent(strMsg, "text/plain");

            // Sent now.
            Transport.send(msg);
            //System.err.println("Sent");
            return true;
        } catch (Exception e) {
            logger.error("Unable to send email. \n" + e.toString());
            return false;
        }

        
    }
    
    /**
     * Extended class used for authenticating with SMTP server when password is
     * required.
     *
     **/
    
    static class extAuthenticator extends javax.mail.Authenticator{

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
