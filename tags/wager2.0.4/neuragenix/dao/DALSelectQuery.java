/*
 * DALSelectQuery.java
 *
 * Created on October 28, 2002, 2:59 PM
 */

package neuragenix.dao;

import java.util.*;
import java.sql.*;
import neuragenix.dao.exception.*;
import org.jasig.portal.RDBMServices;
import org.jasig.portal.services.LogService;
import neuragenix.common.Utilities;
/** This object queries the database based on the parameters provided (E.g., Domain, fields, etc)
 * and returns the results in the form of a ResultSet
 *
 *
 * @author Hayden Molnar
 * 
 */ 
public class DALSelectQuery extends DALMasterQuery {
    Connection con = null;
    private ResultSet rsSelect = null;
    private boolean blDeletedColumn = true;
    private boolean blGetMaxRecord = false;
    private boolean blTurnLowerCaseOff = false;
    
    
    /** Creates a new instance of DALSelectQuery */
    public DALSelectQuery() throws Exception{
        
    }
     
    /** Returns a ResultSet based on the query excuted.
     * @return Returns a ResultSet base on the query provided
     */    
    public ResultSet getResults()
    { 
        return rsSelect;
    }
     public int getInsertedRecordId()
    {
        return 0;
    } 
     public void setGetMaxRecord(boolean aGetMaxRecord)
    {
        blGetMaxRecord = aGetMaxRecord;
     }
     
     public void setCaseSensitive(boolean aTurnOffKey)
    {
        blTurnLowerCaseOff = aTurnOffKey;
     }
     /** Allows the user to get the deleted records
      * @param aDeletedFlag true or false
      */     
     public void setDeletedColumn(boolean aDeletedFlag) 
    {
        blDeletedColumn = aDeletedFlag;
     }
    /** excutes the select query based on the parameters provided.
     * @return Returns true or false depending on the success of the select query
     * @throws DAOQueryInvalidWhereValues Thrown if where values supplied do not match the where field types
     * @throws DAOQueryInvalidDomainJoin Thrown when there is no relationship define for the domina specified
     * @throws DAOQueryInvalidDomain Thrown when a domain is supplied that is not in the dbschema
     * @throws DAOSQLException Thrown when the database can not execute the sql statement provided
     * @throws DAOException Thrown when an unknown error is found
     */    
    /*public boolean execute() throws DAOQueryInvalidWhereValues, DAOQueryInvalidDomainJoin, DAOException, DAOQueryInvalidDomain, DAOSQLException
    {
        boolean blSelectOk = true;
        try
        {
            String strSQLSelect = "SELECT ";
           
            
// ---------------------------- ADD THE FIELDS!! --------------------------------------------------------------
            
            for(int intCounter = 0; intCounter < vecFieldNames.size(); intCounter++)
            {
                 // Only put a comma in the SQL statement if it's not the first field
                 if(intCounter != (0))
                {
                    strSQLSelect += ", ";
                 }               
                 // ----------- ADD THE MAX IF REQUIRED ----------------------
                
                if(vecDomainNames.size() > 1 && hashDBJoinKeys.containsKey(vecFieldNames.get(intCounter).toString()))
                {
                    if(blGetMaxRecord == true)
                    {
                        strSQLSelect += "MAX(";
                    }
                    strSQLSelect += hashDBJoinKeys.get(vecFieldNames.get(intCounter));
                    if(blGetMaxRecord == true)
                    {
                        strSQLSelect += strSQLSelect += ")";
                    }
                    strSQLSelect += " AS " + vecFieldNames.get(intCounter);
                }
                else
                {      
                    if(blGetMaxRecord == true)
                    {
                        strSQLSelect += "MAX(";
                    }
                    strSQLSelect += "\"" + hashDBFields.get(vecFieldNames.get(intCounter)) + "\"";
                    if(blGetMaxRecord == true)
                    {
                        strSQLSelect += ")";
                    }
                    strSQLSelect += " AS " + vecFieldNames.get(intCounter);
                }
                
                 // Only put a comma in the SQL statement if it's not the last field
//                 if(intCounter != (vecFieldNames.size() - 1))
  //              {
    //                strSQLSelect += ", ";
      //           }               
            }
            if(blGetMaxRecord == false) // Don't add the timestamp for MAX!
            {
                for(int intCounter = 0; intCounter < vecDomainNames.size(); intCounter++)
                {
                    strSQLSelect += ", " + hashDBDomains.get(vecDomainNames.get(intCounter)) + ".\"TIMESTAMP\" AS " + vecDomainNames.get(intCounter) + "_Timestamp" ;
		

                }
            }
// -------------------------- ADD THE FROM !! ------------------------------------------------------------------
            
            // We must have more than one domain name to do a select!
            if (vecDomainNames.size() < 1)
            {
                blSelectOk = false;
                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - No domain specified");
                throw new DAOQueryInvalidDomain ("No domain specified");
            }
            else  // With one domain name we don't need to do a join!
            {
                String strFirstDomainName = hashDBDomains.get(vecDomainNames.get(0)).toString(); 
                strSQLSelect += " FROM " + strFirstDomainName;
                // if using default joins
                if (blUseDefaultJoin)
                {  
                    for(int intCounter = 1; intCounter < vecDomainNames.size(); intCounter ++)
                    {
                        if(hashDBJoinTypes.containsKey(vecDomainNames.get(intCounter -1).toString() + "-" + vecDomainNames.get(intCounter).toString()) && hashDBJoinFields.containsKey(vecDomainNames.get(intCounter -1).toString() + "-" + vecDomainNames.get(intCounter).toString()))
                        {
                            strSQLSelect += " " + hashDBJoinTypes.get(vecDomainNames.get(intCounter -1).toString() + "-" + vecDomainNames.get(intCounter).toString());
                            strSQLSelect += " " + hashDBDomains.get(vecDomainNames.get(intCounter));
                            strSQLSelect += " " + hashDBJoinFields.get(vecDomainNames.get(intCounter -1 ).toString() + "-" + vecDomainNames.get(intCounter).toString());

                        }
                        else // If the relationship between the two domains doesn't exist tell them!
                        {
                            blSelectOk = false;

                            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomainJoin - There is no relationship defined in the schema for the domains specified");
                            throw new DAOQueryInvalidDomainJoin("There is no relationship defined in the schema for the domains specified");
                        }
                    }
                }
                // not using default joins
                else
                {
                    for (int intCounter=0; intCounter < vecJoinDomainNames.size(); intCounter++)
                    {                 
                        strSQLSelect += " " + hashDBConnectors.get(vecJoinTypes.get(intCounter));
                        strSQLSelect += " " + hashDBDomains.get(vecJoinDomainNames.get(intCounter));
                        strSQLSelect += " ON " + strFirstDomainName + ".\"";
                        strSQLSelect += hashDBFields.get(vecFirstJoinFieldNames.get(intCounter)) + "\"";
                        strSQLSelect += " = " + hashDBDomains.get(vecJoinDomainNames.get(intCounter)) + ".\"";
                        strSQLSelect += hashDBFields.get(vecSecondJoinFieldNames.get(intCounter)) + "\"";
                    }
                }
            }
            
// ----------------------------- ADD THE WHERE IF WE HAVE ANY!! -------------------------------------
            int intWhereCounter = 0;
            String strTempWhereBracketConnector = "";
            if(vecWhereFields.size() > 0)
            {
                
                // Check to see if we have the right number of where fields for where values
                if (vecWhereFields.size() == vecWhereFieldValue.size())
                {
                    strSQLSelect += " WHERE ";
                    for(intWhereCounter = 0; intWhereCounter < vecWhereOperator.size(); intWhereCounter ++)
                    {
                        // Add brackets if required
                        if(vecOpenWhereBracket.contains(new Integer(intWhereCounter)))
                        {
                            strSQLSelect += "(";
                        }
                        
                        if(vecDomainNames.size() > 1 && hashDBJoinKeys.containsKey(vecWhereFields.get(intWhereCounter).toString()))
                        {
                            // If the where is the first one we don't need a connector
                            if(hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) == null)
                            {
                                // You must add the lower function to string where's
                                if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                                {
                                    if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    {
                                        strSQLSelect += "lower(" + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + ") " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ? ";
                                    }else{strSQLSelect += hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ? ";}
                                }else{strSQLSelect += hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ? ";}
                            }
                            else{
                                // You must add the lower function to string where's
                                if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                                {
                                    if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    {
                                        
                                        strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " lower(" + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + ") " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ?";
                                    }else{strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ?";}
                                }else{strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ?";}

                            }

                        }
                        else
                        {   
                            // If the where is the first one we don't need a connector
                            if(hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) == null)
                            {
                                // You must add the lower function to string where's
                                if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                                {
                                    if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    {
                                        strSQLSelect += "lower(\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\")" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ? ";
                                    }else{strSQLSelect += "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ? ";}
                                }else{strSQLSelect += "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ? ";}
                            }
                            else
                            {
                                // You must add the lower function to string where's
                                if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                                {
                                    if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    {
                                        strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + "lower(\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\")" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ?";
                                    }else{strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ?";}
                                }else{strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " ?";}
                            }

                        }
                        
                        // Close the bracket if required
                        // Add brackets if required
                        if(vecCloseWhereBracket.contains(new Integer(intWhereCounter)))
                        {
                            strTempWhereBracketConnector = vecWhereBracketConnector.get(vecCloseWhereBracket.indexOf(new Integer(intWhereCounter))).toString();
                           // if(hashDBConnectors.get(vecWhereBracketConnector.get(intWhereCounter)) == null)
                      //      {
                        //        strSQLSelect += ") ";
                          //  }else{ strSQLSelect += ") " + hashDBConnectors.get(vecWhereBracketConnector.get(intWhereCounter)) + " ";}
                            
                            
                            if(hashDBConnectors.get(strTempWhereBracketConnector) == null)
                            {
                                strSQLSelect += ") ";
                            }else{ strSQLSelect += ") " + hashDBConnectors.get(strTempWhereBracketConnector) + " ";}
                        }
                    }


                }
                else
                {
                    blSelectOk = false;
                    LogService.instance().log(LogService.ERROR, "DAOQueryInvalidWhereValues - The number of where fields do not match the number of where values.");
                    throw new DAOQueryInvalidWhereValues("The number of where fields do not match the number of where values.");
                }
            }
         //     System.err.println(strSQLSelect);
            // ADD THE DEFAULT OF ONLY GETTING THE NON DELETED ITEMS, UNLESS OTHER WISE SPECIFIED
            if(blDeletedColumn == true)
            {
                for(int intCounter = 0; intCounter < vecDomainNames.size(); intCounter ++)
                {
                    if(intWhereCounter == 0) // We need the WHERE STATEMENT
                    {
                        strSQLSelect += " WHERE \"" + hashDBDomains.get(vecDomainNames.get(intCounter)) + "\".\"" + DBSchema.DELETED_DB_FIELD_NAME + "\" = 0";
			intWhereCounter += 1;
                    }
                    else if(intWhereCounter > 0)
                    {
                        strSQLSelect += " AND \"" + hashDBDomains.get(vecDomainNames.get(intCounter)) + "\".\"" + DBSchema.DELETED_DB_FIELD_NAME + "\" = 0";
                    }
                }
            }
// ------------------------ ADD AN ORDERBY IF REQUIRED ------------------------------------------------
            if(vecOrderByFields.size() > 0)
            {
                
                strSQLSelect += " ORDER BY ";
                for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)
            {
                 if(vecDomainNames.size() > 1 && hashDBJoinKeys.containsKey(vecOrderByFields.get(intCounter).toString()))
                {
                    
                    strSQLSelect += hashDBJoinKeys.get(vecOrderByFields.get(intCounter)) + " " + vecOrderByFieldDirection.get(intCounter);
                }
                 else{
                     strSQLSelect += "\"" + hashDBFields.get(vecOrderByFields.get(intCounter)) + "\"" + " " + vecOrderByFieldDirection.get(intCounter);
                 }
                 // Only put a comma in the SQL statement if it's not the last field
                 if(intCounter != (vecOrderByFields.size() - 1))
                {
                    strSQLSelect += ", ";
                 }               
            }
            }
            
            
            // paging
            if (intStartRecord >= 0 && intRecordPerPage > 0)
                strSQLSelect += " LIMIT " + intRecordPerPage + " OFFSET " + intStartRecord;
            
            
     //       System.err.println(strSQLSelect);
            // Get a connection if we don't already have one!
            if(con == null)
            {
                 con = RDBMServices.getConnection();
            }
             
             // Create the preparedstatment
             PreparedStatement ps = con.prepareStatement(strSQLSelect);
            
// -------------------- ADD THE WHERE VALUES IF WE HAVE ANY!! -----------------------------------------------------------------------------------------
            if(vecWhereFields.size() > 0)
            {
                String strTempForLike = ""; // Used for like operators
                for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter ++)
                {
                    // If the operator is a "LIKE" cause put the wildcard "%" in the where cause
                    if(hashDBOperators.get(vecWhereOperator.get(intCounter)).equals("LIKE"))
                    {
                        strTempForLike = "%";
                    }else{ strTempForLike = "";}
                    
                    // Create the correct preparedstatement type depending on the field type
                     if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.INT_TYPE))
                     {
                            ps.setInt((intCounter + 1), new Integer(vecWhereFieldValue.get(intCounter).toString()).intValue());
                     }
                     else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.STR_TYPE))
                     {
                         
                         if(blTurnLowerCaseOff == false) // Turn the lower case off?
                         {
                            ps.setString((intCounter + 1), vecWhereFieldValue.get(intCounter).toString().toLowerCase() + strTempForLike);
                         }else{ps.setString((intCounter + 1), vecWhereFieldValue.get(intCounter).toString() + strTempForLike);}
                     }
                     else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.DATE_TYPE))
                     {
                            ps.setString((intCounter + 1), Utilities.convertDateForDB(vecWhereFieldValue.get(intCounter).toString()));
                     }
                     else
                     {
                            ps.setString((intCounter + 1), vecFieldValue.get(intCounter).toString());
                     }
                } 
            }


            //System.err.println("DALSelect: " + ps.toString());

             // Excute the query
            rsSelect = ps.executeQuery();
            
        }
        catch(SQLException sqle)
        {
            blSelectOk = false;
            LogService.instance().log(LogService.ERROR, "DAOSQLException - SQL format error. Have the domains, and fields been set correctly?");
            throw new DAOSQLException("SQL format error. Have the domains, and fields been set correctly?");
        }
        catch(NumberFormatException nfe)
        {
            blSelectOk = false;
            LogService.instance().log(LogService.ERROR, "DAOSelectInvalidDataType - Are you trying to select an integer fields with a string value?");
            throw new DAOSelectInvalidDataType("Are you trying to select an integer fields with a string value?");
        }
        catch (Exception e) 
        {
            blSelectOk = false;
            LogService.instance().log(LogService.ERROR, e);
            throw new DAOException("Unknown error. Is the database running? - " + e.toString());
       }
          return blSelectOk;
    }
    */
     
    public boolean execute()
    {
        try
        {
            String strSQLSelect = toString();
            
            if (vecQueries.size() > 0)
            {
                strSQLSelect = "(" + strSQLSelect + ")";
                for (int i=0; i < vecQueries.size(); i++)
                    strSQLSelect += " " + vecQueryConnectors.get(i) + " (" + vecQueries.get(i) + ")";
            }
            
            if(vecOrderByFields.size() > 0)
            {
                
                //strSQLSelect += " ORDER BY LOWER(";
                strSQLSelect += " ORDER BY (";
                for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)
                {
                    if((vecDomainNames.size() > 1 || vecJoinDomainNames.size() > 0) && hashDBJoinKeys.containsKey(vecOrderByFields.get(intCounter)))
                        strSQLSelect += hashDBJoinKeys.get(vecOrderByFields.get(intCounter)) + ") " + vecOrderByFieldDirection.get(intCounter);
                    else
                        strSQLSelect += "\"" + hashDBFields.get(vecOrderByFields.get(intCounter)) + "\")" + " " + vecOrderByFieldDirection.get(intCounter);
                    
                    // Only put a comma in the SQL statement if it's not the last field
                    if(intCounter != (vecOrderByFields.size() - 1))
                        strSQLSelect += ", ";
                }
            }
            
            // paging
            if (intStartRecord >= 0 && intRecordPerPage > 0)
                strSQLSelect += " LIMIT " + intRecordPerPage + " OFFSET " + intStartRecord;
            
                     
            if (con == null)
                con = RDBMServices.getConnection();
             
           //System.err.println("SQL SELECT: " + strSQLSelect);
            // Create the preparedstatment
            PreparedStatement ps = con.prepareStatement(strSQLSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsSelect = ps.executeQuery();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /** Closes off the ResultSet provided through getResults() and closes the database connection
     * @param rsReturned The same ResultSet provided from the getResults method
     * @throws DAOException Thrown when an unknown error occurs
     */    
    public void killResultSet(ResultSet rsReturned) throws DAOException
    {
        try
        {
            // Kill the ResultSet
            if (rsReturned != null)
            {
                rsReturned.close();
                rsReturned = null;
            }
            // Return the connection to the UPortal DB manager
            if(con != null)
            {
                RDBMServices.releaseConnection(con);
                con = null;
            }
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, e);
            throw new DAOException("Unknown problem occured while trying to close the resultset and the database connection");
        }
    }
    public void cancelChanges() throws DAOException
    {
    }
    public void saveChanges() throws DAOException
    {
    }
    /** Return the number of rows affected by the last update.
     * @throws DAOException Always thrown. You shouldn't be calling this...
     */    
    public void setTimestamp(String strTimeout) throws DAOException
    {
	throw new DAOException("No update count for a select query.");
    }
    
    /** Return the number of rows affected by the last update.
     * @throws DAOException Always thrown. You shouldn't be calling this...
     */    
    public int getUpdatedRecordCount() throws DAOException
    {
	throw new DAOException("No update count for a select query.");
    }
    
    public String toString()
    {
        String strSQLSelect = "SELECT ";
           
            
// ---------------------------- ADD THE FIELDS!! --------------------------------------------------------------
            
        for (int intCounter = 0; intCounter < vecFieldNames.size(); intCounter++)
        {
            // Only put a comma in the SQL statement if it's not the first field
            if(intCounter != (0))
                strSQLSelect += ", ";
             
                 // ----------- ADD THE MAX IF REQUIRED ----------------------
                
            if ((vecDomainNames.size() > 1 || vecJoinDomainNames.size() > 0) && hashDBJoinKeys.containsKey(vecFieldNames.get(intCounter)))
            {
                if (blGetMaxRecord == true)
                    strSQLSelect += "MAX(";
                
                //System.err.println("FIELD NAME =" + vecFieldNames.get(intCounter));
                strSQLSelect += hashDBJoinKeys.get(vecFieldNames.get(intCounter));
                if (blGetMaxRecord == true)
                    strSQLSelect += strSQLSelect += ")";
                
                strSQLSelect += " AS " + vecFieldNames.get(intCounter);
            }
            else
            {      
                if (blGetMaxRecord == true)
                    strSQLSelect += "MAX(";
                
                strSQLSelect += "\"" + hashDBFields.get(vecFieldNames.get(intCounter)) + "\"";
                if (blGetMaxRecord == true)
                    strSQLSelect += ")";
                
                strSQLSelect += " AS " + vecFieldNames.get(intCounter);
            }        
        }
        
        if(blGetMaxRecord == false) // Don't add the timestamp for MAX!
            for(int intCounter = 0; intCounter < vecDomainNames.size(); intCounter++)
                strSQLSelect += ", " + hashDBDomains.get(vecDomainNames.get(intCounter)) + ".\"TIMESTAMP\" AS " + vecDomainNames.get(intCounter) + "_Timestamp" ;
        
// -------------------------- ADD THE FROM !! ------------------------------------------------------------------
            
        // We must have more than one domain name to do a select!
        if (vecDomainNames.size() < 1)
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - No domain specified");
        else  
        {
            String strFirstDomainName = hashDBDomains.get(vecDomainNames.get(0)).toString(); 
            strSQLSelect += " FROM " + strFirstDomainName;
            
            // if using default joins
            if (blUseDefaultJoin)
            {  
                for(int intCounter = 1; intCounter < vecDomainNames.size(); intCounter ++)
                {
                    if(hashDBJoinTypes.containsKey(vecDomainNames.get(intCounter -1) + "-" + vecDomainNames.get(intCounter)) && hashDBJoinFields.containsKey(vecDomainNames.get(intCounter -1) + "-" + vecDomainNames.get(intCounter)))
                    {
                        strSQLSelect += " " + hashDBJoinTypes.get(vecDomainNames.get(intCounter -1) + "-" + vecDomainNames.get(intCounter));
                        strSQLSelect += " " + hashDBDomains.get(vecDomainNames.get(intCounter));
                        strSQLSelect += " " + hashDBJoinFields.get(vecDomainNames.get(intCounter -1 ) + "-" + vecDomainNames.get(intCounter));
                    }
                    else // If the relationship between the two domains doesn't exist tell them!
                        LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomainJoin - There is no relationship defined in the schema for the domains specified");
                }
            }
            // not using default joins
            else
            {
                for (int intCounter=0; intCounter < vecJoinDomainNames.size(); intCounter++)
                {    
                    if (intCounter < 1)
                    {
                        strSQLSelect += " " + hashDBConnectors.get(vecJoinTypes.get(intCounter));
                        strSQLSelect += " " + hashDBDomains.get(vecJoinDomainNames.get(intCounter));
                        strSQLSelect += " ON " + strFirstDomainName + ".\"";
                        strSQLSelect += hashDBFields.get(vecFirstJoinFieldNames.get(intCounter)) + "\"";
                        strSQLSelect += " = " + hashDBDomains.get(vecJoinDomainNames.get(intCounter)) + ".\"";
                        strSQLSelect += hashDBFields.get(vecSecondJoinFieldNames.get(intCounter)) + "\"";
                    }
                    else
                    {
                        strSQLSelect += " " + hashDBConnectors.get(vecJoinTypes.get(intCounter));
                        strSQLSelect += " " + hashDBDomains.get(vecJoinDomainNames.get(intCounter));
                        strSQLSelect += " ON " + hashDBDomains.get(vecJoinDomainNames.get(intCounter - 1)) + ".\"";
                        strSQLSelect += hashDBFields.get(vecFirstJoinFieldNames.get(intCounter)) + "\"";
                        strSQLSelect += " = " + hashDBDomains.get(vecJoinDomainNames.get(intCounter)) + ".\"";
                        strSQLSelect += hashDBFields.get(vecSecondJoinFieldNames.get(intCounter)) + "\"";
                    }
                }
            }

        }
        
// ----------------------------- ADD THE WHERE IF WE HAVE ANY!! -------------------------------------
        int intWhereCounter = 0;
        String strTempWhereBracketConnector = "";
        if(vecWhereFields.size() > 0)
        {
            // Check to see if we have the right number of where fields for where values
            if (vecWhereFields.size() == vecWhereFieldValue.size())
            {
                strSQLSelect += " WHERE ";
                for (intWhereCounter = 0; intWhereCounter < vecWhereOperator.size(); intWhereCounter ++)
                {
                    String strTemp = (String) vecWhereFieldValue.get(intWhereCounter);
                    
                    
                    // Add brackets if required
                    if(vecOpenWhereBracket.contains(new Integer(intWhereCounter)))
                        strSQLSelect += "(";

                    if (strTemp != null)
                    {
                    if((vecDomainNames.size() > 1 || vecJoinDomainNames.size() > 0) && hashDBJoinKeys.containsKey(vecWhereFields.get(intWhereCounter)))
                    {
                        // If the where is the first one we don't need a connector
                        if(hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) == null)
                        {
                            // You must add the lower function to string where's
                            if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                            {
                                if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    strSQLSelect += "lower(" + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + ") " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                                else
                                    strSQLSelect += hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                            }
                            else
                                strSQLSelect += hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                        }
                        else
                        {
                            // You must add the lower function to string where's
                            if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                            {
                                if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " lower(" + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + ") " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                                else
                                    strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                            }
                            else
                                strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + hashDBJoinKeys.get(vecWhereFields.get(intWhereCounter)) + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                        }
                    }
                    else
                    {   
                        // If the where is the first one we don't need a connector
                        if(hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) == null)
                        {
                            // You must add the lower function to string where's
                            if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                            {
                                if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    strSQLSelect += "lower(\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\")" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                                else
                                    strSQLSelect += "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                            }
                            else
                                strSQLSelect += "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                        }
                        else
                        {
                            // You must add the lower function to string where's
                            if(hashDBFieldTypes.get(vecWhereFields.get(intWhereCounter)).equals(DBSchema.STR_TYPE))
                            {
                                if(blTurnLowerCaseOff == false) // Turn the lower case off?
                                    strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + "lower(\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\")" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                                else
                                    strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                            }
                            else
                                strSQLSelect += hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) + " " + "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intWhereCounter)) + " " + convertValueForWhere(intWhereCounter) + " ";
                        }
                    }
                    }
                    else
                    {
                        // If the where is the first one we don't need a connector
                        if(hashDBConnectors.get(vecWhereConnector.get(intWhereCounter)) == null)
                        {
                            if(vecWhereOperator.get(intWhereCounter).equals(DBSchema.NOT_EQUALS_OPERATOR))
                            {
                                strSQLSelect += "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " IS NOT NULL ";
                                
                            }else{ strSQLSelect += "\"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " IS NULL ";}
                        }
                        else{
                            if(vecWhereOperator.get(intWhereCounter).equals(DBSchema.NOT_EQUALS_OPERATOR))
                            {
                                strSQLSelect += "AND \"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " IS NOT NULL ";
                            }else{ strSQLSelect += " AND \"" + hashDBFields.get(vecWhereFields.get(intWhereCounter)) + "\"" + " IS NULL ";}
                        }
                    }

                    // Close the bracket if required
                    // Add brackets if required
                    if(vecCloseWhereBracket.contains(new Integer(intWhereCounter)))
                    {
                        strTempWhereBracketConnector = vecWhereBracketConnector.get(vecCloseWhereBracket.indexOf(new Integer(intWhereCounter))).toString();

                        if(hashDBConnectors.get(strTempWhereBracketConnector) == null)
                            strSQLSelect += ") ";
                        else
                            strSQLSelect += ") " + hashDBConnectors.get(strTempWhereBracketConnector) + " ";
                    }
                }
                
                
            }
            else
            {
                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidWhereValues - The number of where fields do not match the number of where values.");
            }
        }
     
        if (blDeletedColumn == true)
        {
            for (int intCounter = 0; intCounter < vecDomainNames.size(); intCounter ++)
            {
                if(intWhereCounter == 0) // We need the WHERE STATEMENT
                {
                    strSQLSelect += " WHERE " + hashDBDomains.get(vecDomainNames.get(intCounter)) + ".\"" + DBSchema.DELETED_DB_FIELD_NAME + "\" = 0";
                    intWhereCounter += 1;
                }
                else if(intWhereCounter > 0)
                {
                    strSQLSelect += " AND " + hashDBDomains.get(vecDomainNames.get(intCounter)) + ".\"" + DBSchema.DELETED_DB_FIELD_NAME + "\" = 0";
                }
            }
        }
        
        for (int i=0; i < vecWhereStatements.size(); i++)
        {
            if (intWhereCounter == 0)
            {
                strSQLSelect += " WHERE " + vecWhereStatements.get(i);
                intWhereCounter++;
            }
            else
                strSQLSelect += " " + vecWhereStatConnectors.get(i) + " " + vecWhereStatements.get(i);
        }
        
        for (int i=0; i < vecWhereQueries.size(); i++)
        {
            if (intWhereCounter == 0)
            {
                strSQLSelect += " WHERE " + vecWhereQueryConnectors.get(i) + vecWhereQueryFields.get(i) + vecWhereQueryOperators.get(i) + " (" + vecWhereQueries.get(i) + ")";
                intWhereCounter++;
            }
            else
                strSQLSelect += " " + vecWhereQueryConnectors.get(i) + vecWhereQueryFields.get(i) + vecWhereQueryOperators.get(i) + " (" + vecWhereQueries.get(i) + ")";
        }
        
        return strSQLSelect;
    }
    
    public String convertValueForWhere(int intCounter)
    {
        
        String strTempForLike = "";

        // If the operator is a "LIKE" cause put the wildcard "%" in the where cause
        if(hashDBOperators.get(vecWhereOperator.get(intCounter)).equals("LIKE"))
            strTempForLike = "%";

        if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.INT_TYPE))
            return (String) vecWhereFieldValue.get(intCounter);
        else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.STR_TYPE))
        {
            if(blTurnLowerCaseOff == false) // Turn the lower case off?
                return "'" + vecWhereFieldValue.get(intCounter).toString().toLowerCase() + strTempForLike + "'";
            else
                return "'" + (String) vecWhereFieldValue.get(intCounter) + strTempForLike + "'";
        }
        else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.DATE_TYPE))
        {
            return "'" + Utilities.convertDateForQuery((String) vecWhereFieldValue.get(intCounter)) + "'";
        }
        else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.TIME_TYPE))
        {
            return "'" + Utilities.convertTimeForQuery((String) vecWhereFieldValue.get(intCounter)) + "'";
        }
        else
        {
            return (String) vecFieldValue.get(intCounter);
        }
        
    }
    
    /*public static void main(String[] args)
    {
        try
        {
            DALSelectQuery query = new DALSelectQuery();
            
            query.setUseDefaultJoin(false);
            query.setDomainName("SURVEY_RESULTS");
            query.setDomainName("PATIENT");
            query.setDomainName("SURVEY");
            query.setJoinDomain("LEFT_JOIN", "PATIENT", "intParticipantID", "intInternalPatientID");
            query.setJoinDomain("LEFT_JOIN", "SURVEY", "intSurveyID", "intSurveyID");
            query.setField("strFirstName", "");
            query.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
}
