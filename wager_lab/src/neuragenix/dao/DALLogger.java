package neuragenix.dao;

 

import java.sql.*;

import java.util.*;

import org.jasig.portal.RDBMServices;

import org.jasig.portal.services.LogService;

import neuragenix.utils.Property;



public class DALLogger

{

	public static String ACTIVITY = "ACTIVITY";

	public static String ALERT = "ALERT";

	public static String INFO = "INFO";
        

	public static int PRIORITY_LOW = 3;
	public static int PRIORITY_NORMAL = 2;
	public static int PRIORITY_HIGH = 1;



	public static int RESULT_SUCCESS = 1;
	public static int RESULT_FAILURE = 0;

	

	private Connection con = null;

	private boolean isEnabled = false;
        private boolean isFull = false;



	private static DALLogger singleton = new DALLogger();

	

	public DALLogger()

	{

		try

		{

			isEnabled = Property.getPropertyAsBoolean("neuragenix.dao.DALLogger.useDalLogger");
                        isFull = Property.getPropertyAsBoolean("neuragenix.dao.DALLogger.LogEverything");
		}

		catch(Exception e)

		{

			 LogService.instance().log(LogService.ERROR, "DALLogger::DALLogger() : Unable to locate property neuragenix.dao.DALLogger.useDalLogger ");

			isEnabled = false;

		}

	}



	public static DALLogger instance()

	{

		return singleton;

	}



	public void log(String strType, int intPriority, int intResult, String strShortDesc, String strLongDesc, String strUsername)

	{
                
		if( (isEnabled) && ( isFull || (intPriority==1) ) )
		{
			try
			{
                                
                                DALQuery qrObject = new DALQuery();
                                qrObject.setDomain("LOG", null, null, null);
                                qrObject.setField("LOG_strType", strType);
                                qrObject.setField("LOG_intPriority", "" + intPriority);
                                qrObject.setField("LOG_intResult", "" + intResult);
                                qrObject.setField("LOG_strShortDesc", strShortDesc);
                                qrObject.setField("LOG_strLongDesc", strLongDesc);
                                qrObject.setField("LOG_strUser", strUsername);
                                qrObject.executeInsert();
				/*String strSQL = "INSERT INTO ix_log (\"TYPE\", \"PRIORITY\", \"RESULT\", \"SHORT_DESCRIPTION\", \"LONG_DESCRIPTION\", \"USER\") VALUES(?,?,?,?,?,?)";

				if(con == null)

				{

					con = RDBMServices.getConnection();

				}

				PreparedStatement ps = con.prepareStatement(strSQL);

				ps.setString(1, strType);

				ps.setInt(2, intPriority);

				ps.setInt(3, intResult);

				ps.setString(4, strShortDesc);

				ps.setString(5, strLongDesc);

				ps.setString(6, strUsername);

				ps.execute();*/

			}

			catch(Exception e)

			{

				LogService.instance().log(LogService.ERROR, "DALLogger::log() : Log entry: ");

				LogService.instance().log(LogService.ERROR, "Type: " + strType + " Priority: " + intPriority + " Result: " + intResult + " User: " + strUsername);

				LogService.instance().log(LogService.ERROR, "Short Description: " + strShortDesc);

				LogService.instance().log(LogService.ERROR, "Long Description: " + strLongDesc);

				LogService.instance().log(LogService.ERROR, "DALLogger::log() : Exception creating log entry: ", e);

			}

		}

	}



}

