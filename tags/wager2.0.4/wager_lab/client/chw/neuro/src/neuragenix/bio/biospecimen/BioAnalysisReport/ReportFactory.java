
package neuragenix.bio.biospecimen.BioAnalysisReport;
/**
*
* @author renny
*/
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;

public class ReportFactory 
{
	static String strClient = "Default";
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
	public ReportFactory()
	{
		
	}
	public static IDocPreProcessor getDocPreProcessorInstance()
	{
		IDocPreProcessor idocprep= new DocPreProcessor();
		if(!strClient.equalsIgnoreCase("Default"))
		{
			try
			{
				 Class clsObj = Class.forName("neuragenix.bio.biospecimen.BioAnalysisReport.DocPreProcessorClient");
				 idocprep = (IDocPreProcessor)clsObj.newInstance();
			}
			catch(ClassNotFoundException cnfe)
			{
				LogService.instance().log(LogService.INFO, "No client customisation found hence using the default implementation");
			}
			catch(Exception e)
			{
				LogService.instance().log(LogService.ERROR, e.getMessage(),e);
			}
		}
		return idocprep;
	}
	public static DocPPFactory getDocPPFactoryInstance()
	{
		DocPPFactory idppFactory= new DocPPFactory();
		if(!strClient.equalsIgnoreCase("Default"))
		{
			try
			{
				 Class clsObj = Class.forName("neuragenix.bio.biospecimen.BioAnalysisReport.DocPPFactoryClient");
				 idppFactory = (DocPPFactory)clsObj.newInstance();
			}
			catch(ClassNotFoundException cnfe)
			{
				LogService.instance().log(LogService.INFO, "No client customisation found hence using the default implementation");
			}
			catch(Exception e)
			{
				LogService.instance().log(LogService.ERROR, e.getMessage(),e);
			}
		}
		return idppFactory;
	}
}
