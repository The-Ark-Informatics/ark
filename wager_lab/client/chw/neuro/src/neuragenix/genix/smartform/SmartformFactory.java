/*
 * SmartformFactory.java
 *
 * Created on August 24, 2005, 12:17 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.smartform;

import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;

/**
 *
 * @author renny
 */
public class SmartformFactory 
{
	static String strClient="Default";
	static
   {
       try
       {
    	   strClient = PropertiesManager.getProperty("neuragenix.bio.Client");
       }
       catch(Exception e)
       {
    	   strClient = "Default";
            System.out.println ("[CReportFactory] Property neuragenix.bio.Client not present default to DEFAULT.");
       }
   }
    /** Creates a new instance of SmartformFactory */
    public SmartformFactory() 
    {
    }
    
    public static ISmartformManager getSmartformManagerInstance()
    {
    	ISmartformManager ismManager = new SmartformManager();
    	if(!strClient.equalsIgnoreCase("Default"))
		{
			try
			{
				 Class clsObj = Class.forName("neuragenix.genix.smartform.SmartformManagerClient");
				 ismManager = (ISmartformManager)clsObj.newInstance();
			}
			catch(ClassNotFoundException cnfe)
			{
				LogService.instance().log(LogService.INFO, "SmartformManager:No client customisation found hence using the default implementation");
			}
			catch(Exception e)
			{
				LogService.instance().log(LogService.ERROR, e.getMessage(),e);
			}
		}
        return ismManager;
    }
    public static ISmartformEntity getSmartformEntityInstance()
    {
    	ISmartformEntity iSE = new SmartformEntity();
    	if(!strClient.equalsIgnoreCase("Default"))
		{
			try
			{
				 Class clsObj = Class.forName("neuragenix.genix.smartform.SmartformEntityClient");
				 iSE = (ISmartformEntity)clsObj.newInstance();
			}
			catch(ClassNotFoundException cnfe)
			{
				LogService.instance().log(LogService.INFO, "SmartformEntity:No client customisation found hence using the default implementation");
			}
			catch(Exception e)
			{
				LogService.instance().log(LogService.ERROR, e.getMessage(),e);
			}
		}
        return iSE;
    }
    public static ISmartformDE getSmartformDEInstance()
    {
    	ISmartformDE iSDE = null;
    	if(!strClient.equalsIgnoreCase("Default"))
		{
			try
			{
				 Class clsObj = Class.forName("neuragenix.genix.smartform.SmartformDEClient");
				 iSDE = (ISmartformDE)clsObj.newInstance();
			}
			catch(ClassNotFoundException cnfe)
			{
				iSDE = new SmartformDE();
				LogService.instance().log(LogService.INFO, "SmartformDE:No client customisation found hence using the default implementation");
			}
			catch(Exception e)
			{
				iSDE = new SmartformDE();
				LogService.instance().log(LogService.ERROR, e.getMessage(),e);
			}
		}
        return iSDE;
    }
}
