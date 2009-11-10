/*

 * DALUpdateQuery.java

 *

 * Created on October 30, 2002, 3:45 PM

 */



package neuragenix.dao;



import java.util.*;

import java.sql.*;

import neuragenix.dao.exception.*;

import org.jasig.portal.RDBMServices;

import org.jasig.portal.PropertiesManager;

import org.jasig.portal.services.LogService;

import neuragenix.common.Utilities;

/** This object updates the database based on the

 * dbschema parameters provided

 *

 * @author Hayden Molnar

 * @description This class is the child of DALMasterQuery and returns a resultset based on the parameters set

 * in the DALMasterQuery class

 */

public class DALUpdateQuery extends DALMasterQuery {

    private Connection con = null;

    private PreparedStatement ps = null;

    private int intUpdatedRecordCount = 0;

    private String strUpdateTimeout = null;

    private final String DBMSTYPE;

    /** Creates a new instance of DALSelectQuery */

    public DALUpdateQuery() throws Exception{

        String tempDBMSTYPE = PropertiesManager.getProperty("neuragenix.dao.DBMSType");
        
        if (tempDBMSTYPE == null)
            DBMSTYPE = "POSTGRES";
        else
            DBMSTYPE = tempDBMSTYPE;
        
        

    }

    public int getInsertedRecordId()

    {

        return 0;

    }

    public void setDeletedColumn(boolean aDeletedFlag)

    {

         

    }

    public void setCaseSensitive(boolean aTurnOffKey)

    {

    }

    public void setGetMaxRecord(boolean aGetMaxRecord){}

    /** If this method is called an error is thrown as

     * the update query should not provide a ResultSet back!

     * @return Returns nothing

     * @throws DAOUpdateResultSet Thrown if this method is called as it should never

     * be called for an update!

     */    

    public ResultSet getResults() throws DAOInsertResultSet, DAOUpdateResultSet

    {

        LogService.instance().log(LogService.ERROR, "DAOInsertResultSet - An update query can not return a ResultSet");

        throw new DAOInsertResultSet("An update query can not return a ResultSet");

    }

    public void killResultSet(ResultSet rsReturned) throws DAOException

    {

        

    }

    /** Executes the update query

     * @return Returns true or false based on the success of the update query

     * @throws DAOQueryInvalidWhereValues Thrown if an incorrect value is supplied for the where field provided.

     */    

    public boolean execute() throws DAOQueryInvalidWhereValues, DAOQueryInvalidDomainJoin, DAOException, DAOQueryInvalidDomain, DAOQueryInvalidFieldValue, DAOUpdateInvalidDataType, DAOSQLException, DAODuplicateKey

    {

        int intPSCounter = 0; // This is used for adding fields to the preparedstatement

        boolean blUpdateOk = true;

        try

        {

            // Make sure the fields supplied have an equal number of values

            if(vecFieldNames.size() == vecFieldValue.size())

            {

            

                String strSQLUpdate = "UPDATE ";

// ------------------  ADD THE TABLES !! ----------------------------------------

                // We must have more than one domain name to do a select!

                if (vecDomainNames.size() < 1)

                {

                    blUpdateOk = false;

                    LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidDomain - No domain specified");

                    throw new DAOUpdateInvalidDomain ("No domain specified");

                }

                else if(vecDomainNames.size() > 1)

                {

                    blUpdateOk = false;

                    LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidDomain - Too many domains specified. Only one allowed!");

                    throw new DAOUpdateInvalidDomain ("Too many domains specified. Only one allowed!");

                }

                else

                {

                    strSQLUpdate += " " + hashDBDomains.get(vecDomainNames.get(0));

                }

         

// ---------------------- ADD THE FIELDS TO BE UPDATED ------------------------------------------------

        

                strSQLUpdate += " SET ";

                for(int intCounter = 0; intCounter < vecFieldNames.size(); intCounter++)

                {

                     strSQLUpdate += "\"" + hashDBFields.get(vecFieldNames.get(intCounter)) + "\" = ? ";

                    // Only put a comma in the SQL statement if it's not the last field

                    if(intCounter != (vecFieldNames.size() - 1))

                    {

                        strSQLUpdate += ", ";

                    }               

                }
                if (DBMSTYPE.equalsIgnoreCase("sqlserver"))
                {
                    strSQLUpdate += ", \"TIMESTAMP\" = getdate() ";
                }
                else if (DBMSTYPE.equalsIgnoreCase("oracle"))
                {
                    strSQLUpdate += ", \"TIMESTAMP\" = CURRENT_TIMESTAMP ";
                }
                else

                {
                    strSQLUpdate += ", \"TIMESTAMP\" = TIMEOFDAY() ";
                }

// ------------------------ ADD THE WHERE IF WE HAVE ANY!! ------------------------------------------

                if(vecWhereFields.size() > 0)

                {

                    // Check to see if we have the right number of where fields for where values

                    if (vecWhereFields.size() == vecWhereFieldValue.size())

                    {

                        strSQLUpdate += " WHERE ";

                        for(int intCounter = 0; intCounter < vecWhereOperator.size(); intCounter ++)

                        {

                        // If the where is the first one we don't need a connector

                            if(intCounter == 0)

                            {

                                strSQLUpdate += "\"" + hashDBFields.get(vecWhereFields.get(intCounter)) + "\" " + hashDBOperators.get(vecWhereOperator.get(intCounter)) + " ? ";

                            }

                            else

                            {

                                strSQLUpdate +=  hashDBConnectors.get(vecWhereConnector.get(intCounter)) + " \"" + hashDBFields.get(vecWhereFields.get(intCounter)) + "\"" + " " + hashDBOperators.get(vecWhereOperator.get(intCounter)) + " ? ";

                            }

                        }

			if(null != strUpdateTimeout)

			{

			//	strSQLUpdate +=  hashDBConnectors.get(DBSchema.AND_CONNECTOR) + " \"TIMESTAMP\"" + " " + hashDBOperators.get(DBSchema.EQUALS_OPERATOR) + " ?";

			}



			

                    }

                    else

                    {

                        blUpdateOk = false;

                        LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidWhereValues - The number of where fields do not match the number of where values.");

                        throw new DAOUpdateInvalidWhereValues("The number of where fields do not match the number of where values.");

                    }

              //System.err.println("Query:" + strSQLUpdate);

                }

		else

		{

			if(null != strUpdateTimeout)

			{
                        
				// strSQLUpdate += "WHERE \"TIMESTAMP\"" + " " + hashDBOperators.get(DBSchema.EQUALS_OPERATOR) + " ?";

			}

		}

                // Get a connection if we don't have one

                if(con == null)

                {

                    con = RDBMServices.getConnection();

                }

                // Turn off auto commit so we can do transaction based updates!

                con.setAutoCommit(false);

                // Create the preparedstatment

               ps = con.prepareStatement(strSQLUpdate);

            

// ------------------------ ADD THE FIELD VALUES -----------------------------------------------------------

                

                    for(int intCounter = 0; intCounter < vecFieldNames.size(); intCounter ++)

                    {

                        // Create the correct preparedstatement type depending on the field type

                        if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.INT_TYPE))

                        {

                            if(vecFieldValue.get(intCounter).toString().length() > 0)

                            {

                               ps.setInt((intPSCounter + 1), new Integer(vecFieldValue.get(intCounter).toString()).intValue());

                            }

                            else

                            {

                                ps.setNull((intPSCounter + 1), java.sql.Types.INTEGER);

                            }

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.STR_TYPE))

                        {

                              ps.setString((intPSCounter + 1), vecFieldValue.get(intCounter).toString());

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.DATE_TYPE))

                        {

                              if(vecFieldValue.get(intCounter).toString().length() > 0)

                              {

                                  ps.setDate((intPSCounter + 1), java.sql.Date.valueOf(Utilities.convertDateForDB(vecFieldValue.get(intCounter).toString())));

                              }

                              else

                              {

                                  ps.setNull((intPSCounter + 1), java.sql.Types.DATE);

                              }

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.TIME_TYPE))

                        {

                              if(vecFieldValue.get(intCounter).toString().length() > 0)

                              {

                                  ps.setTime((intPSCounter + 1), java.sql.Time.valueOf(Utilities.convertTimeForDB(vecFieldValue.get(intCounter).toString())));

                              }

                              else

                              {

                                  ps.setNull((intPSCounter + 1), java.sql.Types.TIME);

                              }

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.FLOAT_TYPE))

                        {

                            if(vecFieldValue.get(intCounter).toString().length() > 0)

                            {

                               ps.setFloat((intPSCounter + 1), new Float(vecFieldValue.get(intCounter).toString()).floatValue());

                            }

                            else

                            {

                                ps.setNull((intPSCounter + 1), java.sql.Types.INTEGER);

                            }

                            

                        }

                        else

                        {

                            ps.setString((intPSCounter + 1), vecFieldValue.get(intCounter).toString());

                        }

                            intPSCounter ++;

                    }

 // ----------------------- ADD THE WHERE VALUES IF WE HAVE ANY!! -----------------------------------------

                

                if(vecWhereFields.size() > 0)

                {

                    for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter ++)

                    {

                      // Create the correct preparedstatement type depending on the field type

                         if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.INT_TYPE))

                         {

                                ps.setInt((intPSCounter + 1), new Integer(vecWhereFieldValue.get(intCounter).toString()).intValue());

                         }

                         else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.STR_TYPE))

                         {

                              ps.setString((intPSCounter + 1), vecWhereFieldValue.get(intCounter).toString());

                         }

                         else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.DATE_TYPE))

                         {

                              ps.setDate((intPSCounter + 1), java.sql.Date.valueOf(vecWhereFieldValue.get(intCounter).toString()));

                         }

                         else if(hashDBFieldTypes.get(vecWhereFields.get(intCounter)).equals(DBSchema.TIME_TYPE))

                         {

                              ps.setTime((intPSCounter + 1), java.sql.Time.valueOf(vecWhereFieldValue.get(intCounter).toString()));

                         }

                         else

                         {

                            ps.setString((intPSCounter + 1), vecFieldValue.get(intCounter).toString());

                         }

                         intPSCounter ++;

                    }

                        

                }

		// Add the Timeout Where clause

		

		if(null != strUpdateTimeout)

		{
                    
			// ps.setString((intPSCounter + 1), strUpdateTimeout);

		}

		intPSCounter ++;

		

// ------------------------ Excute the update ------------------------------------------------------------



                System.err.println("DALUpdate: " + ps.toString());


			
                  intUpdatedRecordCount = ps.executeUpdate();

                  // Clean up

                    if(ps != null)

                    {

                        ps.close();

                        ps = null;

                    }

                               

                }

                else // If the field name do not match the field values throw an exception

                {

                    LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidFieldValues - The number of field names do not match the number of field values");

                    throw new DAOUpdateInvalidFieldValues("The number of field names do not match the number of field values");

                }

            }

            catch(SQLException sqle)

            {
                
                sqle.printStackTrace();
                
                
                blUpdateOk = false;

                ResultSet rsDuplicateTest;

                              

                // Test to see if the error was caused by entering a duplicate value when it is not allowed!

                try

                {

                    DALSelectQuery my_select_query = new DALSelectQuery();

                    my_select_query.setDomainName(vecDomainNames.get(0).toString());

                    my_select_query.setDeletedColumn(false); // so it checks all deletes ones as well

                    // Get the non duplicate fields for the domain

                    Hashtable hashDBNonDuplicateFields = DBSchema.getNonDuplicateFields();

                    int intCounter2 = 0;

                 

                    for(int intCounter = 0; intCounter < vecFieldNames.size(); intCounter++)

                    {

                        if(hashDBNonDuplicateFields.containsKey(vecFieldNames.get(intCounter).toString()) && hashDBNonDuplicateFields.get(vecFieldNames.get(intCounter).toString()).equals(vecDomainNames.get(0)))

                        {           

                            my_select_query.setField(vecFieldNames.get(intCounter).toString(), "");

                            if(intCounter2 == 0)

                            {

                                my_select_query.setWhere("", vecFieldNames.get(intCounter).toString(), DBSchema.EQUALS_OPERATOR, vecFieldValue.get(intCounter).toString());

                            }else{my_select_query.setWhere(DBSchema.OR_CONNECTOR, vecFieldNames.get(intCounter).toString(), DBSchema.EQUALS_OPERATOR, vecFieldValue.get(intCounter).toString());}

                           intCounter2 ++; 

                        }

                    }

                            my_select_query.execute();

                            rsDuplicateTest = my_select_query.getResults();

                     

                            

                            if(rsDuplicateTest.isBeforeFirst() == true) // We have a duplicate field!

                            {

                                throw new DAODuplicateKey("Insert not allowed as a duplicate key already exists");

                            }

                            else

                            {

                            my_select_query.killResultSet(rsDuplicateTest);

                            my_select_query = null;

                            

                        }

                    

                    

                }

                catch(DAODuplicateKey dk)

                {

                                               
                    dk.printStackTrace();
                        

                    LogService.instance().log(LogService.ERROR, "DAODuplicateKey - Insert not allowed as a duplicate key already exists");

                    throw new DAODuplicateKey("Insert not allowed as a duplicate key already exists");

                }

                catch(Exception e)

                {

                    e.printStackTrace();                   

                    LogService.instance().log(LogService.ERROR, "DAOSQLException - SQL format error. Have the domains and fields been set correctly?");

                    throw new DAOSQLException("SQL format error from update dao query. Have the domains and fields been set correctly? - " + sqle.toString() + " OTHER ERROR = " + e.toString());

                }

            }

            catch(NumberFormatException nfe)

            {

                

                blUpdateOk = false;

                LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidDataType - Are you trying to update an integer fields with a string value?");

                throw new DAOUpdateInvalidDataType("Are you trying to update an integer fields with a string value?");

            }

            catch (Exception e) 

            {

                blUpdateOk = false;

                LogService.instance().log(LogService.ERROR, e.toString());

                throw new DAOException("Unknown error while trying to execute the SQL commit: " + e.toString());

            }

          

          return blUpdateOk;

    }

    /** Saves all the executes to the database if

     * not called the changes made are not saved

     * @throws DAOException Thrown if an unknown error occurs

     */    

    public void saveChanges() throws DAOException

    {

        try

        {

            con.commit();

            

            if(con != null)

            {

               RDBMServices.releaseConnection(con);

               con = null;

            }

        }

        catch (Exception e) 

        {       

            LogService.instance().log(LogService.ERROR, e.toString());

                throw new DAOException("Unknown error while trying the final commit: " + e.toString());

        }

    }

    /** Rollback the executes.

     * @throws DAOException Thrown if an unknown error occurs

     */    

    public void cancelChanges() throws DAOException

    {

        try

        {

            con.rollback();

            if(con != null)

            {

               RDBMServices.releaseConnection(con);

               con = null;

            }

        }

        catch (Exception e) 

        {    

            LogService.instance().log(LogService.ERROR, e.toString());

             throw new DAOException("Unknown error while trying to undo the final commit: " + e.toString());

        }

    }



    /** Return the number of rows affected by the last update.

     * @throws DAOException Never thrown, only here for compatibility with other DAL queries.

     */    

    public void setTimestamp(String strTimeout) throws DAOException

    {

	strUpdateTimeout = strTimeout;

    }

    /** Return the number of rows affected by the last update.

     * @throws DAOException Never thrown, only here for compatibility with other DAL queries.

     */    

    public int getUpdatedRecordCount() throws DAOException

    {

	return intUpdatedRecordCount;

    }

}



