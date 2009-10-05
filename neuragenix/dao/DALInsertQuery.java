/*

 * DALInsertQuery.java

 *

 * Created on October 30, 2002, 3:45 PM

 */

 

package neuragenix.dao;



import java.util.*;

import java.sql.*;

import neuragenix.dao.exception.*;

import neuragenix.dao.DBMSTypes;

import org.jasig.portal.RDBMServices;

import org.jasig.portal.PropertiesManager;

import org.jasig.portal.services.LogService;

import neuragenix.common.Utilities;

/** This object inserts records into the database depending on the dbschema parameters provided

 *

 * @author Hayden Molnar

 */

public class DALInsertQuery extends DALMasterQuery {

    private Connection con = null;

    private PreparedStatement ps = null;

    private int intAddedRecordId;

  
    /** Creates a new instance of DALSelectQuery */

    public DALInsertQuery() throws Exception{

        
        
        
        

    }

    public int getInsertedRecordId()

    {

        return intAddedRecordId;

    }

    public void setDeletedColumn(boolean aDeletedFlag)

    {

         

    }

     public void setCaseSensitive(boolean aTurnOffKey)

    {

    }

    public void setGetMaxRecord(boolean aGetMaxRecord){}

    /** Throws an error as this method should not be called if an insert query is used

     * @return nothing

     * @throws DAOInsertResultSet Thrown if method is called

     *

     */    

    public ResultSet getResults() throws DAOInsertResultSet, DAOUpdateResultSet

    {

        LogService.instance().log(LogService.ERROR, "DAOInsertResultSet - An insert query can not return a ResultSet");

        throw new DAOInsertResultSet("An insert query can not return a ResultSet");

    }

    public void killResultSet(ResultSet rsReturned) throws DAOException

    {

        

    }

    

    /** Executes the insert query

     * @return Returns true or false depending on the success of the insert query

     */    

    public boolean execute() throws DAOQueryInvalidWhereValues, DAOQueryInvalidDomainJoin, DAOException, DAOQueryInvalidDomain, DAOQueryInvalidFieldValue, DAOUpdateInvalidDataType, DAOSQLException, DAONullSerialKey

    {

        boolean blUpdateOk = true;

        try

        {

            // Make sure the fields supplied have an equal number of values

            if(vecFieldNames.size() == vecFieldValue.size())

            {

            

                String strSQLInsert = "INSERT INTO ";

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

                    strSQLInsert += hashDBDomains.get(vecDomainNames.get(0)) ;

                }

         

// ---------------------- ADD THE FIELDS TO BE UPDATED ------------------------------------------------

        

                strSQLInsert += " (";

            String strPrimaryKey = null;
            // for Oracle we need to add primary key field
            if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                strPrimaryKey = DatabaseSchema.getPrimaryKey((String) vecDomainNames.get(0));
                
                if (strPrimaryKey != null) {
                    strSQLInsert += (DBMSTypes.getField(strPrimaryKey) + ", ");
                }
            }
            
                for(int intCounter = 0; intCounter < vecFieldNames.size(); intCounter++)

                {

                     strSQLInsert += "\"" + hashDBFields.get(vecFieldNames.get(intCounter)) + "\"";

                    // Only put a comma in the SQL statement if it's not the last field

                    if(intCounter != (vecFieldNames.size() - 1))

                    {

                        strSQLInsert += ", ";

                    }               

                }

                strSQLInsert += ") VALUES (";

            // for Oracle we need to add primary key field
            if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                if (strPrimaryKey != null) {
                    strSQLInsert += (DatabaseSchema.getSequence((String) vecDomainNames.get(0)) + ".NEXTVAL, ");
                }
            }
                for(int intCounter = 0; intCounter < vecFieldValue.size(); intCounter++)

                {

                    strSQLInsert += "?";

                    if(intCounter != (vecFieldValue.size() - 1))

                    {

                        strSQLInsert += ", ";

                    }

                }

                strSQLInsert += ")";

      //         System.out.println(strSQLInsert);

// ----------------------- Get a connection if we don't have one ----------------------

                if(con == null)

                {

                    con = RDBMServices.getConnection();

                }

                // Turn off auto commit so we can do transaction based updates!

                con.setAutoCommit(false);

                // Create the preparedstatment
                System.err.println(strSQLInsert);

               ps = con.prepareStatement(strSQLInsert);

            

// ------------------------ ADD THE FIELD VALUES -----------------------------------------------------------

                

                    for(int intCounter = 0; intCounter < vecFieldNames.size(); intCounter ++)

                    {

                       

                        // Create the correct preparedstatement type depending on the field type

                        if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.INT_TYPE))

                        {

                               ps.setInt((intCounter + 1), new Integer(vecFieldValue.get(intCounter).toString()).intValue());

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.STR_TYPE))

                        {

                              ps.setString((intCounter + 1), vecFieldValue.get(intCounter).toString());

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.DATE_TYPE))

                        {

                              ps.setDate((intCounter + 1), java.sql.Date.valueOf(Utilities.convertDateForDB(vecFieldValue.get(intCounter).toString())));

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.TIME_TYPE))

                        {

                              ps.setTime((intCounter + 1), java.sql.Time.valueOf(Utilities.convertTimeForDB(vecFieldValue.get(intCounter).toString())));

                        }

                        else if(hashDBFieldTypes.get(vecFieldNames.get(intCounter)).equals(DBSchema.FLOAT_TYPE))

                        {

                              ps.setFloat((intCounter + 1), new Float(vecFieldValue.get(intCounter).toString()).floatValue());

                        }

                        else

                        {

                            ps.setString((intCounter + 1), vecFieldValue.get(intCounter).toString());

                        }

                    }

// ------------------------ Excute the update ------------------------------------------------------------

                  //System.err.println("DALInsertQuery: " + strSQLInsert);

                  ps.executeUpdate();

                  ps = null;

		  ResultSet rsSelectInsertedId = null;

                  if(hashDBSerialKeys.containsKey(vecDomainNames.get(0).toString()) == true)

                  {
                        if (DatabaseSchema.getDBMSType() == DBMSTypes.SQLSERVER)
                        {
                            ps = con.prepareStatement("select @@IDENTITY");
                        }
                        else if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE)
                        {
                            ps = con.prepareStatement("select " + hashDBSerialKeys.get(vecDomainNames.get(0).toString()) + ".currval from dual" );
                        }
                        else

                        {
                            ps  = con.prepareStatement("SELECT currval('" + hashDBSerialKeys.get(vecDomainNames.get(0).toString()) + "')");
                        }

                  	rsSelectInsertedId = ps.executeQuery();

                  }

                  else if (!(DBSchema.getDBNoSerialKeyDomains().containsKey(vecDomainNames.get(0).toString())))

                  {

                      blUpdateOk = false;

                      throw new DAONullSerialKey("No serial key specified for the domain. Please add to the DBSchema");

                  }

                  while((rsSelectInsertedId != null) && rsSelectInsertedId.next())

                  {

                       intAddedRecordId = rsSelectInsertedId.getInt(1);

                  }

                  rsSelectInsertedId = null;

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

                

                blUpdateOk = false;

                ResultSet rsDuplicateTest;

                

                // Test to see if the error was caused by entering a duplicate value when it is not allowed!

                try

                {

                  Hashtable hashDBNonDuplicateFields = DBSchema.getNonDuplicateFields();

                  if(!hashDBNonDuplicateFields.isEmpty())
	          {
                    DALSelectQuery my_select_query = new DALSelectQuery();

                    my_select_query.setDomainName(vecDomainNames.get(0).toString());

                    my_select_query.setDeletedColumn(false); // so it checks all deletes ones as well

                    // Get the non duplicate fields for the domain


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

                    

                }

                catch(DAODuplicateKey dk)

                {

                                               

                    dk.printStackTrace();

                    LogService.instance().log(LogService.ERROR, "DAODuplicateKey - Insert not allowed as a duplicate key already exists");

                    throw new DAODuplicateKey("Insert not allowed as a duplicate key already exists");

                }

                catch(Exception e)

                {

                                      

                    LogService.instance().log(LogService.ERROR, "DAOSQLException - SQL format error. Have the domains and fields been set correctly?");

                    throw new DAOSQLException("SQL format error from update dao query. Have the domains and fields been set correctly? - ", sqle);

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

                LogService.instance().log(LogService.ERROR, e.toString(), e);

                throw new DAOException("Unknown error while trying to execute the SQL commit: " , e);

            }

          

          return blUpdateOk;

    }

    /** Used to save the changes to the database

     * from the execute. The idea is to be able to execute

     * inserts into many domains before saving the changes

     * @throws DAOException Thrown is an unknown error occurs

     *

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

                LogService.instance().log(LogService.ERROR, e.toString(), e);

                throw new DAOException("Unknown error while trying the final commit: " , e);

        }

    }

    /** Used to cancel the execute method

     * @throws DAOException Thrown when an unknown error occurs

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

             LogService.instance().log(LogService.ERROR, e.toString(), e);

             throw new DAOException("Unknown error while trying to undo the final commit: " , e);

        }

    }

    /** Return the number of rows affected by the last update.

     * @throws DAOException Always thrown. You shouldn't be calling this...

     */    

    public void setTimestamp(String strTimeout) throws DAOException

    {

	throw new DAOException("No update count for an insert query.");

    }

    /** Return the number of rows affected by the last update.

     * @throws DAOException Always thrown. You shouldn't be calling this...

     */    

    public int getUpdatedRecordCount() throws DAOException

    {

	throw new DAOException("No update count for an insert query.");

    }

}



