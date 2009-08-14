/*
 * TransactionManager.java
 *
 * Created on 30 June 2005, 19:40
 */

package neuragenix.bio.biospecimen;

// Libraries
    // Org
    import org.jasig.portal.PropertiesManager;
    import org.jasig.portal.services.LogService;
    import org.jasig.portal.ChannelRuntimeData;

    // Java
    import java.sql.ResultSet;
    import java.lang.StringBuffer;
    import java.util.*;

    // NGX
    import neuragenix.dao.*;
    import neuragenix.security.AuthToken;
    import neuragenix.common.*;

/**
 *
 * @author  navin
 */
public class TransactionManager
{
    private AuthToken authToken = null;
    private int intBiospecimenKey;
    private int intParentKey;
    private Map<String,Object> quantities;
    public static final String CLIENT = PropertiesManager.getProperty("neuragenix.bio.Client");

    /** Creates a new instance of TransactionManager */
    public TransactionManager(int intBiospecimenKey, AuthToken authToken)
    {
        try
        {
            this.reset(intBiospecimenKey, authToken);
        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager] Exception - " + e.toString());
            e.printStackTrace();
        }
    }

    /** Creates a new instance of TransactionManager */
    public TransactionManager(int intBiospecimenKey, AuthToken authToken, boolean isSave, double dbQty, double dbCollected, double dbRemoved) 
    {
        try
        {
            this.reset(intBiospecimenKey, authToken);
            this.setTransactionAction(isSave);
            this.setTransactionQuantity(dbQty);

            // define collected, removed in the Map
            quantities.put("BIOSPECIMEN_flNumberCollected", new Double(dbCollected));
            quantities.put("BIOSPECIMEN_flNumberRemoved", new Double(dbRemoved));

        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager] Exception - " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     *  Function resets the TransactionManager (simillar to defining a new transaction manager)
     *  Function assumes the transactionAction is a save. (Change via call to setTransactionAction())
     *  Function assumes the transaction quantity is 1. (Change via call to setTransactionQuantity())
     */
    public void reset (int intBiospecimenKey, AuthToken authToken)
    {
        try
        {
            if (authToken.hasActivity("biospecimen_update"))
            {
                this.intBiospecimenKey = intBiospecimenKey;
                this.authToken = authToken;
                quantities = Collections.synchronizedMap(new HashMap<String,Object>());

                // set intParentKey
                intParentKey = getParentBiospecimenKey (intBiospecimenKey);

                // assumes saving
                quantities.put("transactionAction", "save");

                // assumes adding only 1 unit of quantity
                quantities.put("flNewQuantity", new Double(1.0));
                
                 // specify collected, removed, remaining
                getQuantity(this.intBiospecimenKey,  this.quantities);
            }
            else
            {
                StringBuffer sbError = new StringBuffer ("[TransactionManager::reset] User ");
                sbError.append(authToken.getUserIdentifier());
                sbError.append(" attempted to update a biospecimen (due to a cascaded biospecimen quantity update).");
                
                LogService.instance();
				LogService.log(LogService.ERROR, sbError.toString());
                System.out.println (sbError.toString());
            }
        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager::reset] Exception - " + e.toString());
            e.printStackTrace();
        }
        
   }
    
    /**
     *  Function allows an external class to specify the quantity involved in the biospecimen transaction update.
     *  The default constructur sets this to 1.
     */
    public void setTransactionQuantity(double dbQty)
    {
        quantities.put("flNewQuantity", new Double(dbQty));
    }
    
    
    /**
     *  Function allows an external class to specify if the biospecimen transaction is a save (true) or delete (false)
     *  The default constructur sets this to true (ie. a save).
     */
    public void setTransactionAction(boolean isSave)
    {
        if (isSave)
            quantities.put("transactionAction", "save");
        else
            quantities.put("transactionAction", "delete");
    }
    
   /**
    *   Recursive function will update the transaction history for all of its parents
    *   If the biospecimen intBiospecimenKey has a parent.
    *   Function expects that the provided Map contains key/value pairs for
    *   - type of transaction
    *   - qty added/removed
    *   - amount collected
    *   - amount removed
    *   - amount remaining
    */
    public void cascadedUpdateTransactions ()
    {
        try
        {
            recursiveTransactionUpdate (this.intBiospecimenKey, this.quantities);
        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager::cascadedUpdateTransactions] Exception - " + e.toString());
            e.printStackTrace();
        }
    }
    
    private void recursiveTransactionUpdate (int intBiospecimenKey, Map<String,Object> quantity)
    {
        try
        {
            int intParKey = this.getParentBiospecimenKey(intBiospecimenKey);
            
            if (intParKey != -1)
            {
               // update the relevant biospecimen transactions
               updateQuantity (intParKey, quantities);

               // recurse higher up the tree
               recursiveTransactionUpdate (intParKey, quantities);
            }
            
        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager::recursiveTransactionUpdate] Exception - " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     *  Function attempts to get the intParentKey for the intBiospecimenKey provided
     */
    private int getParentBiospecimenKey (int intBiospecimenKey) throws java.sql.SQLException
    {
        int intParentKey = -1;
        ResultSet rs = null;
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_intParentID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", String.valueOf(intBiospecimenKey) , 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();

            if (rs.first())
            {
               intParentKey = rs.getInt("BIOSPECIMEN_intParentID");
            }
        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager::getParentBiospecimenKey] Exception - " + e.toString());
            e.printStackTrace();
        }
        finally
        {
            if (rs != null)
                rs.close();
        }
        return intParentKey;
    }
   
    /**
     *      Function updates the quantity for the intBiospecimen (no changes to ix_bio_transactions only ix_biospecimen)
     *      Function stores the transactionHistory for for the specimen (intBiospecimenKey) in myQuantity
     *      Function then defines double values for collected, removed and remaining based on quantity available
     *      Function updates ix_biospecimen to reflect the new double values
     */
    private void updateQuantity (int intBiospecimenKey, Map<String,Object> quantities) throws java.sql.SQLException
    {
        ResultSet rs = null;
        
        try
        {
            Map<String,Object> myQuantity = Collections.synchronizedMap(new HashMap<String,Object>());
            
            // if this specimen has its own bio-transactions take that into account
            getQuantity (intBiospecimenKey, myQuantity);
            
            String strAction = (String)quantities.get("transactionAction");
            double dbQty = ((Double)quantities.get("flNewQuantity")).doubleValue();
            double dbQtyCollect = ((Double)myQuantity.get("BIOSPECIMEN_flNumberCollected")).doubleValue();
            double dbQtyRemoved = ((Double)myQuantity.get("BIOSPECIMEN_flNumberRemoved")).doubleValue();
            double dbQtyRemain = ((Double)myQuantity.get("BIOSPECIMEN_flNumberRemaining")).doubleValue();
            
            //System.out.println ("\t" + intBiospecimenKey + "] collectd = " + dbQtyCollect + "\tremoved" + dbQtyRemoved + "\tremain" + dbQtyRemain);

            // If deleting define either qtyCollected or qtyRemoved
            if (strAction.equalsIgnoreCase("delete"))
                if (dbQty >0)
                    dbQtyCollect = dbQtyCollect - dbQty;
                else
                    dbQtyRemoved = dbQtyRemoved - dbQty;
            
            // If saving define either qtyCollected or qtyRemoved        
            if (strAction.equalsIgnoreCase("save"))
                if (dbQty >0)
                    dbQtyCollect = dbQtyCollect + dbQty;
                else
                    dbQtyRemoved = dbQtyRemoved + dbQty;

            // Set the amount remaining to the difference between collected and removed
            dbQtyRemain = dbQtyCollect + dbQtyRemoved;
            
            //System.out.println ("\t" + intBiospecimenKey + "] collectd = " + dbQtyCollect + "\tremoved" + dbQtyRemoved + "\tremain" + dbQtyRemain);           
            
            // Perform a query to update qtyCollected, qtyRemoved and optionally qtyRemaining
            DALQuery query = new DALQuery();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_flNumberCollected", String.valueOf(dbQtyCollect));
            query.setField("BIOSPECIMEN_flNumberRemoved", String.valueOf(dbQtyRemoved));
            
            if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA")))
            {
                query.setField("BIOSPECIMEN_flNumberRemaining", String.valueOf(dbQtyRemain));
            }
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", String.valueOf(intBiospecimenKey), 0, DALQuery.WHERE_HAS_VALUE);

            if (!query.executeUpdate())
            {
                System.out.println("\t[BiospecimenCore::updateQuantity] Update failure for sample " + intBiospecimenKey);
            }
        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager::getParentBiospecimenKey] Exception - " + e.toString());
            e.printStackTrace();
        }
        finally
        {
            if (rs!=null)
                rs.close();
        }
    }
    
    /**
     *      Function determines the amount of qtyCollected, qtyRemoved and optionally qtyRemianing
     *      for the biospecimen as specified by intBiospecimenKey
     */
    private void getQuantity (int intBiospecimenKey, Map<String,Object> quantities) throws java.sql.SQLException
    {
        ResultSet rs = null;
        try
        {
            DALQuery query = new DALQuery();
            
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_flNumberCollected", null);
            query.setField("BIOSPECIMEN_flNumberRemoved", null);
            
            if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA")))
            {
                query.setField("BIOSPECIMEN_flNumberRemaining", null);
            }
            
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", String.valueOf(intBiospecimenKey) , 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();

            if (rs.first())
            {
                quantities.put("BIOSPECIMEN_flNumberCollected", new Double(rs.getDouble("BIOSPECIMEN_flNumberCollected")));
                quantities.put("BIOSPECIMEN_flNumberRemoved", new Double(rs.getDouble("BIOSPECIMEN_flNumberRemoved")));
                if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA")))
                {
                    quantities.put("BIOSPECIMEN_flNumberRemaining", new Double(rs.getDouble("BIOSPECIMEN_flNumberRemaining")));
                }
                else
                {
                    quantities.put("BIOSPECIMEN_flNumberRemaining", new Double(0));
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance();
			LogService.log(LogService.ERROR, "[TransactionManager::getParentBiospecimenKey] Exception - " + e.toString());
            e.printStackTrace();
        }
        finally
        {
            if (rs!=null)
                rs.close();
        }
    }    
    
    private StringBuffer displayQuantity()
    {
        StringBuffer sbDebug = new StringBuffer("");
        sbDebug.append(this.intBiospecimenKey).append("] ");
        Iterator j = this.quantities.values().iterator();
        Iterator k = this.quantities.keySet().iterator();
        while (k.hasNext() && j.hasNext())
            sbDebug.append ("\t\t" + k.next() + " = " + j.next());
        
        return sbDebug;
    }
}
