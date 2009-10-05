/*
 * updateProtein.java
 *
 * Created on 18 November 2005, 13:34
 * Maps the protein names containing greek characters to the unicode equivalent
 */


/**
 * 
 */
import java.sql.*;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.*;


/**
 * @author S Parappat
 * @date 17 November 2005
 */
public class updateProtein 
{
    /**
     * Private Variables
     */
    private final String SYSTEM_TIMEZONE = "Australia/Melbourne";
    private final String SYSTEM_LANGUAGE = "en";
    private final String SYSTEM_COUNTRY = "AU";
    private String databaseName = "chw-neuro4.6";
    private String host = "localhost:5432";
    private Hashtable hashProteinNameMapping = new Hashtable ();

    private java.sql.Connection con = null;

    private java.sql.PreparedStatement psUpdateProtein = null;
    
    /**
     * Constructor
     */
    public updateProtein()
    {
            super();
            this.init("org.postgresql.Driver",
            "jdbc:postgresql://"+host+"/"+databaseName,
            "postgres", "postgres");
    }

    /**
     * Constructor
     * @param driver
     * @param url
     * @param username
     * @param passwd
     */
    public updateProtein(String driver, String url, String username, String passwd)
    {
            super();
            this.init(driver, url, username, passwd);
    }

    /**
     * Function - finalize
     */
    public void finalize() throws java.lang.Throwable
    {
        super.finalize();
        con.close();
    }
    

    /**
     * Function - init
     * @param driver
     * @param url
     * @param username
     * @param passwd
     */
    private void init(String driver, String url, String username, String passwd)
    {
        try
        {
            Class.forName(driver);
            con = java.sql.DriverManager.getConnection(url,username,passwd);
            
            // define update statements
            psUpdateProtein = con.prepareStatement("update ix_protein set \"DISPLAYNAME\"=? where \"NAME\"=?");
            
            this.resetDebug();
            
            // update the protein name mappings
            hashProteinNameMapping.put("a 1 syntrophin", "#a" + " 1 syntrophin");
            hashProteinNameMapping.put("a 7 integrin", "#a" + " 7 integrin");
            hashProteinNameMapping.put("a 7 integrinA", "#a" + " 7 integrinA");
            hashProteinNameMapping.put("a 7 integrinB", "#a" + " 7 integrinB");
            hashProteinNameMapping.put("a actinin 1", "#a" + " actinin 1");
            hashProteinNameMapping.put("a actinin 2", "#a" + " actinin 2");
            hashProteinNameMapping.put("a actinin 3", "#a" + " actinin 3");
            hashProteinNameMapping.put("a actinin 4", "#a" + " actinin 4");
            hashProteinNameMapping.put("a actinin, sarcomeric", "#a" + " actinin, sarcomeric");
            hashProteinNameMapping.put("a dystrobrevin 1", "#a" + " dystrobrevin 1");
            hashProteinNameMapping.put("a dystrobrevin 2", "#a" + " dystrobrevin 2");
            hashProteinNameMapping.put("a dystroglycan", "#a" + " dystroglycan");
            hashProteinNameMapping.put("a sarcoglycan (adhalin)", "#a" + " sarcoglycan (adhalin)");
            
            hashProteinNameMapping.put("Acetylcholine receptor subunit-a", "Acetylcholine receptor subunit-" + "#a");
            hashProteinNameMapping.put("Acetylcholine receptor subunit-g", "Acetylcholine receptor subunit-" + "#g");
            hashProteinNameMapping.put("Actin, a-cardiac", "Actin, " + "#a" + "-cardiac");
            hashProteinNameMapping.put("Actin, a-sarcomeric", "Actin, " + "#a" + "-sarcomeric");
            hashProteinNameMapping.put("Actin, a-skeletal", "Actin, " + "#a" + "-skeletal");
            
             
            hashProteinNameMapping.put("b 1 syntrophin", "#b" + " 1 syntrophin");
            hashProteinNameMapping.put("b 2 syntrophin", "#b" + " 2 syntrophin");
            hashProteinNameMapping.put("b dystrobrevin", "#b" + " dystrobrevin");
            hashProteinNameMapping.put("b dystroglycan", "#b" + " dystroglycan");
            hashProteinNameMapping.put("b sarcoglycan", "#b" + " sarcoglycan");
            
            hashProteinNameMapping.put("Calcium channel a1C subunit (DHPR)", "Calcium channel " + "#a" + "1C subunit (DHPR)");
            hashProteinNameMapping.put("Collagen VI, a3 chain", "Collagen VI, " + "#a" + "3 chain");
            hashProteinNameMapping.put("d sarcoglycan", "#d" + " sarcoglycan");


            hashProteinNameMapping.put("g actin", "#g" + " actin");
            hashProteinNameMapping.put("g sarcoglycan", "#g" + " sarcoglycan");
            hashProteinNameMapping.put("Laminin a 2 (Merosin)", "Laminin " + "#a" + " 2 (Merosin)");
            hashProteinNameMapping.put("Laminin b 1", "Laminin " + "#b" + " 1 (Laminin)");
            
        }
        catch (Exception e)
        {
            System.out.println ("\t[updateProtein:init] *** Exception Caught");
            e.printStackTrace();
        }
    }
    
    /**
     * Function - doProtein()
     *
     */
    public void doProtein()
    {
        
        try
        {
            
            Enumeration enum = hashProteinNameMapping.keys();
            
            while (enum.hasMoreElements())
            {
                String proteinName = enum.nextElement().toString();
                psUpdateProtein.setString(1, hashProteinNameMapping.get(proteinName).toString());
                psUpdateProtein.setString(2, proteinName);
                psUpdateProtein.executeUpdate();
            }    

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
   
    
    /**
     * Function gets current time
     * @return
     */
    private String getTime()
    {
        String time = new String();
        try
        {
            java.util.TimeZone tz = java.util.TimeZone.getTimeZone(SYSTEM_TIMEZONE);
            java.util.Locale loc = new java.util.Locale(SYSTEM_LANGUAGE, SYSTEM_COUNTRY);
            java.util.Calendar rightNow = java.util.Calendar.getInstance(tz,loc);
            time = rightNow.get(java.util.Calendar.HOUR_OF_DAY) + ":" 
                    + rightNow.get(java.util.Calendar.MINUTE) 
                    + " (" + rightNow.get(java.util.Calendar.SECOND) + ":" 
                    + rightNow.get(java.util.Calendar.MILLISECOND) + ") ";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return time;        
    }
    

    /**
     * Function - updateOutputToFile
     * Sets the flag: outputToFile
     */
    public void dumpOutputToFile(String pathFileName)
    {
    	try
    	{
	        PrintWriter out = new PrintWriter(new FileOutputStream(pathFileName));
	        out.println(getDebug());
	        out.close();
    	}
    	catch (Exception e)
    	{
    		System.out.println ("[updateInventory::dumpOutputToFile] *** Caught Exception");
    		e.printStackTrace();
    	}
    }

    /**
     * Function - updateOutputToScreen
     * Sets the flag: outputToScreen
     */
    public void dumpOutputToScreen()
    {
    	System.out.println(getDebug());
    }

 
    
    /**
     * Function - main
     * @param args
     */
    public static void main(String[] args) 
    {
        try
        {
            // Create a new updateInventory object
            updateProtein upPrt = new updateProtein();
        	
            System.out.println("[main] Connected to database at " + upPrt.getTime());
            
            // Call the main function to do the protein updates
            upPrt.doProtein();
            
//            // Specify if the output of the doProtein call should be sent to file
            upPrt.dumpOutputToFile("E:\\temp\\debug.txt");
            
            
            System.out.println("[main] End at " + upPrt.getTime());
        }
        catch(Exception ex)
        {
            System.out.println ("[main] *** Exception Caught");
            ex.printStackTrace();
        }
    }
}

