/*

 * DALMasterQuery.java

 * 

 * Created on October 28, 2002, 2:59 PM

 */

 

package neuragenix.dao;

import java.util.*;

import java.sql.*;

import neuragenix.dao.exception.*;

import org.jasig.portal.RDBMServices;

import org.jasig.portal.services.LogService;





/**

 * This object is the parent class for the DALInsertQuery,

 * DALUpdateQuery and DALSelectQuery. It sets the domains,

 * fields and where causes for it's children objects.

 * @author Hayden Molnar

 * 

 */

public abstract class DALMasterQuery {

    /** Contains the domain names used in the children objects.

     */    

    protected Vector vecDomainNames = new Vector(5, 2);

    /** Contains the field names used in the children objects.

     */    

    protected Vector vecFieldNames = new Vector(10, 2);

    /** Contains the domain values used in the children objects.

     */    

    protected Vector vecFieldValue = new Vector(10, 2);

    /** Contains the where fields used in the children objects.

     */    

    protected Vector vecWhereFields = new Vector(5, 2); 

    /** Contains the where field values used in the children objects.

     */    

    protected Vector vecWhereFieldValue = new Vector(5, 2); 

    /** Contains the where operators used in the children objects.

     */    

    protected Vector vecWhereOperator = new Vector(5, 2);

    /** Contains the where connectors (e.g., AND, OR) used in the children objects.

     */    

    protected Vector vecWhereConnector = new Vector(5, 2);

    

    /** Contains the order by fields used in the children objects.

     */    

    protected Vector vecOrderByFields = new Vector(5, 2); 

    

    /** Contains the order by direction.

     */    

    protected Vector vecOrderByFieldDirection = new Vector(5, 2); 

    

    /** Holds a open bracket to group where conditions

     */    

    protected Vector vecOpenWhereBracket = new Vector(5,2);

    

    /** Holds a close bracket to group where conditions

     */    

    protected Vector vecCloseWhereBracket = new Vector(5,2);

    

    /** Holds the bracket connectors

     */    

    protected Vector vecWhereBracketConnector = new Vector(5,2);



    // Get the hash tables

    /** Contains the allowable field names for the database.

     */    

    protected Hashtable hashDBFields;

   /** Contains the field types for the field names for the database.

    */   

    protected Hashtable hashDBFieldTypes;

   /** Contains the allowable domains for the database.

    */   

    protected Hashtable hashDBDomains;

   /** Contains the allowable table joins for the database.

    */   

    protected Hashtable hashDBJoinTypes;

   /** Contains the allowable table join fields for the database.

    */   

    protected Hashtable hashDBJoinFields;

   /** Contains the allowable operators (e.g., =, >) for the database.

    */   

    protected Hashtable hashDBOperators;

   /** Contains the allowable connectors(e.g., AND, OR) for the database.

    */   

    protected Hashtable hashDBConnectors;

    

    protected Hashtable hashDBJoinKeys;

    

    protected Hashtable hashDBSerialKeys;

    

    protected boolean blDuplicateKey = false;

    

    

    // paging

    protected int intStartRecord;

    

    protected int intRecordPerPage;

    

    // enhanced DAL

    protected boolean blUseDefaultJoin = true;

    protected Vector vecJoinDomainNames = new Vector(5,2);

    protected Vector vecJoinTypes = new Vector(5,2);

    protected Vector vecFirstJoinFieldNames = new Vector(5,2);

    protected Vector vecSecondJoinFieldNames = new Vector(5,2);

    

    protected Vector vecQueries = new Vector(3,2);

    protected Vector vecQueryConnectors = new Vector(3,2);

    

    protected Vector vecWhereQueries = new Vector(3,2);

    protected Vector vecWhereQueryConnectors = new Vector(3,2);

    protected Vector vecWhereQueryOperators = new Vector(3,2);

    protected Vector vecWhereQueryFields = new Vector(3,2);

    

    protected Vector vecWhereStatements = new Vector(3,2);

    protected Vector vecWhereStatConnectors = new Vector(3,2);

    

    /** Creates a new instance of DALSelectQuery

     * @throws Exception Throws an exception if one is produced

     * from the DBSchema loading.

     */ 

    public  DALMasterQuery() throws Exception{

        try

        {

            hashDBFields = DBSchema.getDBFields();

            hashDBFieldTypes = DBSchema.getDBFieldTypes();

            hashDBDomains = DBSchema.getDBDomains();

            hashDBJoinTypes = DBSchema.getDBJoinTypes();

            hashDBJoinFields = DBSchema.getDBJoinFields();

            hashDBOperators = DBSchema.getDBOperators();

            hashDBConnectors = DBSchema.getDBConnectors();

            hashDBJoinKeys = DBSchema.getDBJoinKeys();

            hashDBSerialKeys = DBSchema.getDBSerialKeys();

        }

        catch(Exception e) // throw any errors from the DBSchema

        {

            LogService.instance().log(LogService.ERROR, e);

            throw new Exception(e.toString());

        }

                
    }

    /** Excutes either a select, insert or update query

     * @return

     */    

    public abstract boolean execute() throws DAOQueryInvalidWhereValues, DAOQueryInvalidDomainJoin, DAOException, DAOQueryInvalidDomain, DAOQueryInvalidFieldValue, DAOUpdateInvalidDataType, DAOSQLException, DAONullSerialKey;

    

    public abstract void saveChanges() throws DAOException;

    public abstract int getInsertedRecordId();

    public abstract void cancelChanges() throws DAOException;

    public abstract void setDeletedColumn(boolean aDeletedFlag); 

    public abstract void setGetMaxRecord(boolean aGetMaxRecord);

    public abstract void setCaseSensitive(boolean aTurnOffKey);

    

    /** Returns a ResultSet from the select query

     * @return Returns a ResultSet containing the results

     * from the select query.

     * @throws DAOInsertResultSet If the insert query was selected and a ResultSet was trying to be obtained

     * @throws DAOUpdateResultSet If the update query was selected and a ResultSet was trying to be obtained

     */    

    public abstract ResultSet getResults() throws DAOInsertResultSet, DAOUpdateResultSet;

    public abstract void killResultSet(ResultSet rsReturned) throws DAOException;

    /** Enables the domain to be set for the children query objects

     * @param strADomainName Takes a valid domain name as defined in the schema

     * @throws DAOQueryInvalidDomain Thrown if the domain supplied is not valid according to the schema

     */    

    public void setDomainName(String strADomainName)  throws DAOQueryInvalidDomain

    {

        if(domainExists(strADomainName))

        {

                vecDomainNames.add(strADomainName);

        }

        else

        {

            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - The domain supplied is not valid");

            throw new DAOQueryInvalidDomain("The domain supplied is not valid");

        }

  

    }

    /** Enables the fields to be set for the children query objects

     * @param strAFieldName The field name in the dbschema

     * @param strAValue The field value. Must be the same type as the field. I.e., you cannot

     * set a string value for a integer field

     * @throws DAOQueryInvalidField Thrown is a field is set that is not in the dbschema

     * @throws Exception Thrown is an unknown error is caught.

     */    

    public void setField(String strAFieldName, String strAValue) throws DAOQueryInvalidField, Exception

    {

        try

        {

            if(fieldExists(strAFieldName))

            {           

                vecFieldNames.add(strAFieldName);

            }

            else

            {

                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is not valid");

                throw new DAOQueryInvalidField("The field supplied is not valid. Field = " + strAFieldName);

            }

            if(strAValue != null && !strAValue.equals(""))

            {

                vecFieldValue.add(strAValue);

            }

            else

            {   

                vecFieldValue.add("");

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, e);

            throw new Exception(e.toString());

        }

    }

    /** Enables the where fields to be set for the children query objects

     * @param strWhereConnector Requires a valid where connector to be

     * supplied according to the schema

     * @param strAFieldName Requires a valid field name to be set according to the schema

     * @param strOperator Requires a valid operator to be set according to the

     * schema.

     * @param strAValue Requires a field value for the field set. It must be the same type as the field

     * @throws DAOQueryInvalidField Thrown if a field is supplied that is not in the dbschema.

     * @throws DAOQueryInvalidFieldValue Thorwn if the wrong type of fielad value is supplied for a field.

     * E.g., A string is supplied for a integer field

     * @throws DAOQueryInvalidWhereOperator Thrown if an operater is supplied that is not in the dbschema

     * @throws DAOQueryInvalidWhereConnector Thrown if a connector is supplied that is not defined in the dbschema

     * @throws Exception Thrown if an unknown error occurs

     *

     */    

    public void setWhere(String strWhereConnector, String strAFieldName, String strOperator, String strAValue) throws DAOQueryInvalidField, DAOQueryInvalidFieldValue, DAOQueryInvalidWhereOperator, DAOQueryInvalidWhereConnector, Exception

    {

        try

        {

            if(fieldExists(strAFieldName))

            {           

                vecWhereFields.add(strAFieldName);

            }

            else

            {

                throw new DAOQueryInvalidField("The field supplied is not valid. Field = " + strAFieldName);

            }

            if(operatorExists(strOperator))

            {

                vecWhereOperator.add(strOperator);

            }

            else

            {

                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidWhereOperator - The operator supplied is not valid");

                throw new DAOQueryInvalidWhereOperator("The operator supplied is not valid");

            }

      //      if(strAValue != null && !strAValue.equals(""))

        //    {

                vecWhereFieldValue.add(strAValue);

          //  }

         //   else

          //  {

           //     LogService.instance().log(LogService.ERROR, "DAOQueryInvalidFieldValue - The field value supplied is not valid");

             //   throw new DAOQueryInvalidFieldValue("The field value supplied is not valid. Value = " + strAValue + " Field = " + strAFieldName);

           // }

            if(connectorExists(strWhereConnector))

            {

                vecWhereConnector.add(strWhereConnector);

            }

            else if(strWhereConnector == null || strWhereConnector.equals(""))

            {

                vecWhereConnector.add("");

            }

            else

            {

                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidWhereConnector - The connector supplied is not valid");

                throw new DAOQueryInvalidWhereConnector("The connector supplied is not valid");

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, e);

            throw new Exception(e.toString());

        }

        

    }

    /** Enables the order by fields to be set for the children query objects

     * @param strOrderByField The field to order by

     * @param strOrderDirection The direction of the order by ASC or DESC

     */    

    public void setOrderBy(String strOrderByField, String strOrderDirection) throws Exception, DAOQueryInvalidField, DAOQueryInvalidOrderByDirection

    {

        try

        {

            if(fieldExists(strOrderByField))

            {           

                vecOrderByFields.add(strOrderByField);

            }

            else

            {

                throw new DAOQueryInvalidField("The OrderBy field supplied is not valid");

            }   

            if(strOrderDirection == "ASC" || strOrderDirection == "DESC")

            {

                vecOrderByFieldDirection.add(strOrderDirection);

            }

            else

            {

                throw new DAOQueryInvalidOrderByDirection(" OrderBy direction is not valid");

            }  

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, e);

            throw new Exception(e.toString());

        }

    }

    

    /** Allows an object to open a bracket to group where conditions

     */    

    public void setOpenWhereBracket()

    {

  

        vecOpenWhereBracket.add(new Integer(vecWhereFields.size()));

   

    }

    /** Allows an object to close a bracket to group where conditions

     * @param strAWhereConnector Takes the connector to join bracket coniditions

     * @throws DAOQueryInvalidWhereConnector Thrown if a invalid connector is supplied

     */    

    public void setCloseWhereBracket(String strAWhereConnector) throws DAOQueryInvalidWhereConnector

    {

      

        vecCloseWhereBracket.add(new Integer(vecWhereFields.size() -1));



            if(connectorExists(strAWhereConnector))

            {

                vecWhereBracketConnector.add(strAWhereConnector);

            }

            else if(strAWhereConnector == null || strAWhereConnector.equals(""))

            {

                vecWhereBracketConnector.add("");

            }

            else

            {

                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidWhereConnector - The connector supplied is not valid");

                throw new DAOQueryInvalidWhereConnector("The connector supplied is not valid");

            }

    }

    

    /** Clears the domains, fields and where values used by the children query objects

     */    

    public void clearAllQuery()

    {

        vecOpenWhereBracket.clear();

        vecCloseWhereBracket.clear();

        vecWhereBracketConnector.clear();

        vecDomainNames.clear();

        vecFieldNames.clear();

        vecFieldValue.clear();

        vecWhereFields.clear(); 

        vecWhereFieldValue.clear(); 

        vecWhereOperator.clear();

        vecWhereConnector.clear();

    }

    /** Clears the domains values used by the children query objects

     */    

    public void clearDomains()

    {

        vecDomainNames.clear();

    }

    /** Clears the domains, fields and where values used by the children query objects

     */    

    public void clearFields()

    {

        vecFieldNames.clear();

        vecFieldValue.clear();

    }

    /** Clears the  where fields and values used by the children query objects

     */    

    public void clearWhere()

    {

        vecOpenWhereBracket.clear();

        vecCloseWhereBracket.clear();

        vecWhereBracketConnector.clear();

        vecWhereFields.clear(); 

        vecWhereFieldValue.clear(); 

        vecWhereOperator.clear();

        vecWhereConnector.clear();

    }

   

    private boolean domainExists(String strDomainToCheck)

    {

        // Checks to see if the supplied domain a allowable domain     

        return hashDBDomains.containsKey(strDomainToCheck);

    }

    

    private boolean fieldExists(String strFieldToCheck)

    {

       // Checks to see if the supplied field is a allowable field

        return hashDBFields.containsKey(strFieldToCheck);

    }

    private boolean operatorExists(String strOperatorToCheck)

    {

         // Checks to see if the supplied operator a allowable operator

        return hashDBOperators.containsKey(strOperatorToCheck);

    }

    private boolean connectorExists(String strConnectorToCheck)

    {

         // Checks to see if the supplied connector a allowable connector

        return hashDBConnectors.containsKey(strConnectorToCheck);

    }

    /** Return the number of rows affected by the last update.

     * @throws DAOException If called on any other query type.

     */    

    public abstract void setTimestamp(String strTimeout) throws DAOException;

    /** Return the number of rows affected by the last update.

     * @throws DAOException If called on any other query type.

     */    

    public abstract int getUpdatedRecordCount() throws DAOException;

    

    // paging

    public void setStartRecord(int startRecord) {

        intStartRecord = startRecord;

    }

    

    public void setRecordPerPage(int recordPerPage) {

        intRecordPerPage = recordPerPage;

    }

    

    public void setUseDefaultJoin(boolean blTemp) {

        blUseDefaultJoin = blTemp;

    }

    

    public void setJoinDomain(Object objJoinType, Object objDomain, 

                    Object objField1, Object objField2) throws DAOQueryInvalidDomain,

                    DAOQueryInvalidField, Exception

    {

        if (domainExists(objDomain.toString()))

        {

            vecJoinDomainNames.add(objDomain);

        }

        else

        {

            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - The domain supplied is not valid");

            throw new DAOQueryInvalidDomain("The domain supplied is not valid");

        }

        

        if (fieldExists(objField1.toString()) && fieldExists(objField2.toString()))

        {

            vecFirstJoinFieldNames.add(objField1);

            vecSecondJoinFieldNames.add(objField2);

        }

        else

        {

            throw new DAOQueryInvalidField("The field supplied is not valid. Field = " + 

                    objField1.toString() + " or Field = " + objField2.toString());

        }

        

        if (connectorExists(objJoinType.toString()))

            vecJoinTypes.add(objJoinType);

        else

        {

            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidConnector - The connector supplied is not valid");

            throw new DAOQueryInvalidWhereConnector("The connector supplied is not valid");

        }

        

    }

    

    public void setQuery(Object query, Object connector)

    {

        vecQueries.add(query);

        vecQueryConnectors.add(connector);

    }

    

    public void setWhereQuery(Object connector, Object fieldName, Object operator, Object query)

    {

        vecWhereQueryConnectors.add(connector);

        vecWhereQueryFields.add(fieldName);

        vecWhereQueryOperators.add(operator);

        vecWhereQueries.add(query);

    }

    

    public void setWhereStatement(Object connector, Object statement) 

    {

        vecWhereStatConnectors.add(connector);

        vecWhereStatements.add(statement);

    }
    public String getSQLString() {
        
        String strSQL;
        
        strSQL = vecDomainNames.toString() + vecFieldNames.toString() + vecFieldValue.toString();
        strSQL = strSQL + vecWhereFields.toString() +   vecWhereFieldValue.toString() +   vecWhereOperator.toString();
        strSQL = strSQL + vecWhereConnector.toString();   
        
         //LogService.instance().log(LogService.INFO, "Security Info:" + strSQL);
         return strSQL;
    }

} 
