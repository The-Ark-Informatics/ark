/*

 * DALMasterQuery.java

 * 

 * Created on October 28, 2002, 2:59 PM

 */



package neuragenix.dao;

import neuragenix.security.*;

import java.sql.ResultSet;



import neuragenix.dao.exception.*;





/** 

 * This object is the parent class for the DALInsertQuery,

 * DALUpdateQuery and DALSelectQuery. It sets the domains,

 * fields and where causes for it's children objects.

 * @author Hayden Molnar

 * 

 */

public class Query {

	

	

	public static final int SELECT_QUERY = 1;

	public static final int INSERT_QUERY = 2;

	public static final int UPDATE_QUERY = 3;

        public int intLogPriority = 0;



	private DALMasterQuery query_obj;

	private int query_type = 0;

	private String strActivityRequested ;

    	private AuthToken authToken ;

	private boolean blHasWhere = false ;

    

    /** Creates a new instance of Query

     * 

     */
	  public  Query(int querytype, String activity, AuthToken auth ) throws Exception

	    {

		authToken = auth ;

		strActivityRequested = activity;

		if(!authToken.hasActivity(strActivityRequested))

		{

			DALLogger.instance().log(DALLogger.ALERT, DALLogger.PRIORITY_NORMAL, DALLogger.RESULT_FAILURE, getQueryTypeName(), authToken.getUserIdentifier() + " attempted a " + getQueryTypeName() + " for activity " + strActivityRequested, authToken.getUserIdentifier());

			throw new DAONotAuthorisedException("Security Violation: Attempting to use a disallowed activity.") ;

		}

		

		query_type = querytype ;

		if (query_type == Query.SELECT_QUERY)

		{
	                intLogPriority = 3;
			query_obj = new DALSelectQuery();

		}

		if (query_type == Query.INSERT_QUERY)

		{
	                intLogPriority = 2;
			query_obj = new DALInsertQuery();

		}

		if (query_type == Query.UPDATE_QUERY)

		{
	                intLogPriority = 1;
			query_obj = new DALUpdateQuery();

		}

		

	    }
	
	
	
    public  Query(int querytype, String activity, int intStudyKey, AuthToken auth ) throws Exception

    {

	authToken = auth ;

	strActivityRequested = activity;

	if(!authToken.hasActivity(strActivityRequested,intStudyKey))

	{

		DALLogger.instance().log(DALLogger.ALERT, DALLogger.PRIORITY_NORMAL, DALLogger.RESULT_FAILURE, getQueryTypeName(), authToken.getUserIdentifier() + " attempted a " + getQueryTypeName() + " for activity " + strActivityRequested, authToken.getUserIdentifier());

		throw new DAONotAuthorisedException("Security Violation: Attempting to use a disallowed activity.") ;

	}

	

	query_type = querytype ;

	if (query_type == Query.SELECT_QUERY)

	{
                intLogPriority = 3;
		query_obj = new DALSelectQuery();

	}

	if (query_type == Query.INSERT_QUERY)

	{
                intLogPriority = 2;
		query_obj = new DALInsertQuery();

	}

	if (query_type == Query.UPDATE_QUERY)

	{
                intLogPriority = 1;
		query_obj = new DALUpdateQuery();

	}

	

    }





    /** Excutes either a select, insert or update query

     * @return

     */    

    public boolean execute() throws DAOQueryInvalidWhereValues, DAOQueryInvalidDomainJoin, DAOException, DAOQueryInvalidDomain, DAOQueryInvalidFieldValue, DAOUpdateInvalidDataType, DAOSQLException, DAONotAuthorisedException, DAONullSerialKey, DAODBSchema, Exception

    {

/*	if(!authToken.hasActivity(strActivityRequested))

	{

		throw new DAONotAuthorisedException("Security Violation: Attempting to use a disallowed activity.") ;

	}

	else if(strActivityRequested != ACTIVITY_LOV_ACCESS)

	{

		Vector vecGroups = authToken.getRestrictions(strActivityRequested)

		for(int i = 0 ; i < vecRestrictions.size() ; i++ )

		{

			query_obj.setWhere("" , "intGenixSecurityGroups" , DBSchema.EQUALS_OPERATOR, ((Integer)vecRestrictions.get(i)).intValue() );

		}

	} */

	

	//DALLogger.instance().log(DALLogger.ACTIVITY, DALLogger.PRIORITY_NORMAL, DALLogger.RESULT_SUCCESS, getQueryTypeName(), authToken.getUserIdentifier() + " performed a " + getQueryTypeName() + " for activity " + strActivityRequested, authToken.getUserIdentifier());

	DALLogger.instance().log(DALLogger.ACTIVITY, intLogPriority, DALLogger.RESULT_SUCCESS, getQueryTypeName(), query_obj.getSQLString() , authToken.getUserIdentifier());

	if (domainRequiresGroupSecurity())

	{

		if (query_type == Query.SELECT_QUERY || query_type == Query.UPDATE_QUERY)

		{

			if(blHasWhere)

			{

				setWhere(DBSchema.AND_CONNECTOR,"intGenixGroupID", DBSchema.EQUALS_OPERATOR, authToken.getGroupForActivity(strActivityRequested));

			}

			else

			{

				setWhere("","intGenixGroupID", DBSchema.EQUALS_OPERATOR, authToken.getGroupForActivity(strActivityRequested));

			}

		}

		if (query_type == Query.INSERT_QUERY)

		{

			setField( "intGenixGroupID", authToken.getGroupForActivity(strActivityRequested));

		}

	}

	return query_obj.execute();


    }	

     public void saveChanges() throws DAOException

    {

        query_obj.saveChanges();

     }

     public void cancelChanges() throws DAOException

    {  

        query_obj.cancelChanges();

     }







    /** Returns a ResultSet from the select queryV

     * @return Returns a ResultSet containing the results

     * from the select query.

     * @throws DAOInsertResultSet If the insert query was selected and a ResultSet was trying to be obtained

     * @throws DAOUpdateResultSet If the update query was selected and a ResultSet was trying to be obtained

     */    

    public ResultSet getResults() throws DAOInsertResultSet, DAOUpdateResultSet, DAONotAuthorisedException

    {

	//verify role, etc...

	return query_obj.getResults();

    }

    public int getInsertedRecordId()

    {

        return query_obj.getInsertedRecordId();

    }





    /** Enables the domain to be set for the children query objects

     * @param strADomainName Takes a valid domain name as defined in the schema

     * @throws DAOQueryInvalidDomain Thrown if the domain supplied is not valid according to the schema

     */    

    public void setDomainName(String strADomainName)  throws DAOQueryInvalidDomain

    {

	query_obj.setDomainName(strADomainName);

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

	query_obj.setField(strAFieldName, strAValue);

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

	query_obj.setWhere(strWhereConnector, strAFieldName, strOperator, strAValue);

	blHasWhere = true;

    }

    public void setOpenWhereBracket()

    {

        query_obj.setOpenWhereBracket();

    }

     public void setCloseWhereBracket(String strAConnector) throws DAOQueryInvalidWhereConnector

    {

        query_obj.setCloseWhereBracket(strAConnector);

    }

    

    public void setOrderBy(String strOrderByField, String strOrderDirection) throws Exception, DAOQueryInvalidField, DAOQueryInvalidOrderByDirection

    {

        query_obj.setOrderBy(strOrderByField, strOrderDirection);

    }

    /** Clears the domains, fields and where values used by the children query objects

     */    

    public void clearAllQuery()

    {

	query_obj.clearAllQuery();

    }





    /** Clears the domains values used by the children query objects

     */    

    public void clearDomains()

    {

	query_obj.clearDomains();

    }





    /** Clears the domains, fields and where values used by the children query objects

     */    

    public void clearFields()

    {

	query_obj.clearFields();

    }





    /** Clears the  where fields and values used by the children query objects

     */    

    public void clearWhere()

    {

	blHasWhere = false;

	query_obj.clearWhere();

    }

   

    public void killResultSet(ResultSet rsReturned) throws DAOException

    {

        query_obj.killResultSet(rsReturned);

    }

    

    public void setDeletedColumn(boolean aDeletedFlag)

    {

        query_obj.setDeletedColumn(aDeletedFlag);

    } 

    public void setCaseSensitive(boolean aTurnOffKey)

    {

        query_obj.setCaseSensitive(aTurnOffKey);

    }

    public void setGetMaxRecord(boolean aGetMaxRecord)

    {

        query_obj.setGetMaxRecord(aGetMaxRecord);

    } 

    public void setTimestamp(String strTimeout) throws DAOException

    {

        query_obj.setTimestamp(strTimeout);

    }

    public int getUpdatedRecordCount() throws DAOException

    {

        return query_obj.getUpdatedRecordCount();

    } 



    private boolean domainRequiresGroupSecurity() throws DAODBSchema

    {

	try

	{

		java.util.Hashtable hashTmp = DBSchema.getDBGroupSecurity();

		for(int i = 0 ; i < query_obj.vecDomainNames.size() ; i++)

		{

			if(hashTmp.containsKey(query_obj.vecDomainNames.get(i)))

			{

				return true;

			}

		}

		return false;

	}

	catch(Exception e)

	{

		throw new DAODBSchema(e);

	}

    }



    public String getQueryTypeName()

    {

	if (query_type == Query.SELECT_QUERY)

	{

		return "Select Query";

	}

	if (query_type == Query.INSERT_QUERY)

	{

		return "Insert Query";

	}

	if (query_type == Query.UPDATE_QUERY)

	{

		return "Update Query";

	}

	return "Unknown query type";

    }



    // paging

    public void setStartRecord(int startRecord) {

        query_obj.setStartRecord(startRecord);

    }

    

    public void setRecordPerPage(int recordPerPage) {

        query_obj.setRecordPerPage(recordPerPage);

    }

    

    public void setUseDefaultJoin(boolean blTemp) {

        query_obj.setUseDefaultJoin(blTemp);

    }

    

    public void setJoinDomain(Object objJoinType, Object objDomain, 

                    Object objField1, Object objField2) throws DAODBSchema

    {

        try

        {

            query_obj.setJoinDomain(objJoinType, objDomain, objField1, objField2);

        }

        catch (Exception e)

        {

            throw new DAODBSchema(e);

        }

    }

    

    public Object getQueryObject()

    {

        return query_obj;

    }

    

    public void setQuery(Query query, Object connector) {

        query_obj.setQuery(query.getQueryObject(), connector);

    }

    

    public void setWhereQuery(Object connector, Object fieldName, Object operator, Query query) {

        query_obj.setWhereQuery(connector, fieldName, operator, query.getQueryObject());

    }

    

    public void setWhereStatement(Object connector, Object statement) {

        query_obj.setWhereStatement(connector, statement);

    }
    
    
 
    

}

