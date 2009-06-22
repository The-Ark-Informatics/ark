package neuragenix.utils;



import HTTPClient.HTTPConnection;

import HTTPClient.HTTPResponse;

import org.jasig.portal.services.LogService;

import java.net.URL;



public class WebServiceAccessor

{

	private boolean lastStatus;

	private String strReturnedData = null;

	

	public boolean get(String server, String path, String query)

	{

		lastStatus = false;



		try

		{

			HTTPConnection hconn = new HTTPConnection(new URL(server)); 

                        hconn.setAllowUserInteraction(false);

			HTTPResponse hres = hconn.Get(path, query);

                        



			int status = hres.getStatusCode();

			if((status >= 200) && (status < 300))

			{

				strReturnedData = hres.getText();

				lastStatus = true;

                                

                                System.err.println("Unable to access location " + path + " on server " + server + query);            

                                System.err.println("strReturnedData=" + strReturnedData);

                                

			}

		}

		catch(Exception e)

		{

                    

			LogService.instance().log(LogService.WARN, "Unable to access location " + path + " on server " + server);

		}



		return lastStatus;

		

	}



	public String getString()

	{

		if (lastStatus == false)

		{

			return "";

		}

		return strReturnedData;

	}



	public String getDocument()

	{

		return "";

	}





}

