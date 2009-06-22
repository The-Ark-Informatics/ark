/**
 * DALQuery.java
 * Copyright ? 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 27/08/2003
 */

package neuragenix.dao;

/**
 * Class to model a database query
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.RDBMServices;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;


// Third parties packages
import oracle.sql.*;

// neuragenix packages
import neuragenix.common.Utilities;
import neuragenix.dao.exception.*;
import neuragenix.security.AuthToken;

public class DALQuery
{    
    /** Indicate that there is a value in WHERE clause
     */
    public static final int WHERE_HAS_VALUE = 0;
    
    
    /** Indicate that both sides of the WHERE clause are fields
     */
    public static final int WHERE_BOTH_FIELDS = 1;
    
    /** Indicate that there is a sub-query in WHERE clause
     */
    public static final int WHERE_HAS_SUB_QUERY = 2;
    
    /** Indicate that the WHERE clause compares with NULL value
     */
    public static final int WHERE_HAS_NULL_VALUE = 3;
    
    /** Keep all domains that needs for the query
     */
    protected Vector vtDomains = new Vector(3, 2);
    
    /** Keep first fields of the joins
     */
    protected Vector vtFirstJoinFields = new Vector(3, 2);
    
    /** Keep second fields of the joins
     */
    protected Vector vtSecondJoinFields = new Vector(3, 2);
    
    /** Keep join types
     */
    protected Vector vtJoinTypes = new Vector(3, 2);
    
    /** Keep form fields
     */
    protected Vector vtFields = null;
    
    /** Keep field's data
     */
    protected ChannelRuntimeData rdFieldData = null;

    /** Keep field's data in a hashtable
     */
    protected Hashtable hashFieldData = null;
    
    /** Keep data for multi-insert
     */
    protected ChannelRuntimeData[] rdMultiData = null;
    
    /** Fields to apply MAX
     */
    protected Vector vtMaxFields = new Vector();
    
    /** Fields to apply MIN
     */
    protected Vector vtMinFields = new Vector();
    
    /** Fields to apply SUM
     */
    protected Vector vtSumFields = new Vector();
    
    /** Fields to apply AVERAGE
     */
    protected Vector vtAverageFields = new Vector();
    
    /** Field to apply COUNT
     */
    protected String strCountField = null;
    
    /** COUNT distinct record only
     */
    protected boolean blCountDistinct = false;
    
    /** Is this a distinct query (ie whole record ) **/
    protected boolean blDistinct = false;
    /** Fields to apply Order By
     */
    protected Vector vtOrderByFields = new Vector(3, 2);
    
    /** Fields to apply Order By using Alias
     */
    private Vector vtOrderByFieldsAlias = new Vector(3, 2);
    
    /** Fields to apply Order By sql functions
     */
    private Vector vtOrderBySqlFunctions = new Vector(3, 2);
    
    /** Order By directions of the function. eg. Ascending or Descending
     */
    private Vector vtOrderByDirectionFunction = new Vector(3, 2);
    
    
    /** Order By directions. eg. Ascending or Descending
     */
    protected Vector vtOrderByDirections = new Vector(3, 2);
    
    /** Fields to apply Group By
     */
    protected Vector vtGroupByFields = new Vector();
    
    /** Specify HAVING condition
     */
    protected String strHavingCondition = null;
    
    /** Keep the LIMIT value
     */
    protected int intLimit = 0;
    
    /** Keep the OFFSET value
     */
    protected int intOffset = 0;
    
    /** Keep the where connectors
     */
    protected Vector vtWhereConnectors = new Vector(5, 5);
    
    /** Keep the where no of open brackets
     */
    protected Vector vtWhereOpenBrackets = new Vector(5, 5);
    
    /** Keep the where fields
     */
    protected Vector vtWhereFields = new Vector(5, 5);
    
    /** Keep the where operator
     */
    protected Vector vtWhereOperators = new Vector(5, 5);
    
    /** Keep the where objects
     */
    protected Vector vtWhereObjects = new Vector(5, 5);
    
    /** Keep the where no of close brackets
     */
    protected Vector vtWhereCloseBrackets = new Vector(5, 5);
    
    /** Keep the where types
     */
    protected Vector vtWhereTypes = new Vector(5, 5);
    
    /** Keep sibling queries
     */
    protected Vector vtSiblingQueries = new Vector(3, 2);
    
    /** Keep sibling queries connectors
     */
    protected Vector vtSiblingQueryConnectors = new Vector(3, 2);
    
    /** Keep sibling queries open brackets
     */
    protected Vector vtSiblingQueryOpenBrackets = new Vector(3, 2);
    
    /** Keep sibling queries close brackets
     */
    protected Vector vtSiblingQueryCloseBrackets = new Vector(3, 2);
    
    /** Keep distinct field
     */
    protected String strDistinctField = null;
    
    /** Flag to indicate if the case sensitive is turn on
     */
    protected boolean blCaseSensitiveOn = true;
    
    /** JDBC connection
     */
    private Connection con = null;
    
    /** JDBC prepared statement
     */
    private PreparedStatement ps = null;
    
    /** Inserted record key
     */
    protected int intInsertedRecordKey = -1;
    
    /** Update record key
     */
    protected String strUpdateRecordKey = "-1";
    
    /** Set the auto commit mode
     */
    protected boolean blManualCommit = false;
    
    /** Keep the authToken
    */
    protected AuthToken authToken = null;
    
    /** Keep resultsets that are created by the DAL
     */
    protected Vector vtResultSets = new Vector(10,10);
    
    /** Keep the sub-queries
     */
    private Vector vtSubQueries = new Vector(3, 2);
    
    /** Keep the sub-queries alias
     */
    private Vector vtSubQueryAlias = new Vector(3, 2);
    
    /** Keep first fields of the joins
     */
    private Vector vtSubQueryFirstJoinFields = new Vector(3, 2);
    
    /** Keep second fields of the joins
     */
    private Vector vtSubQuerySecondJoinFields = new Vector(3, 2);
    
    /** Keep join types
     */
    private Vector vtSubQueryJoinTypes = new Vector(3, 2);
    
    private static boolean DISPLAY_DAL_DEBUG = false;
    
    /** Creates a new instance of DALQuery 
     */
    public DALQuery()
    {
    }
    
    public DALQuery(AuthToken authToken) {
        this.authToken = authToken;
    }
    
    static 
    {
        try
        {
            DISPLAY_DAL_DEBUG = PropertiesManager.getPropertyAsBoolean("neuragenix.dao.displayDebug");
        }
        catch(Exception e)
        {
            System.out.println ("[DALQuery] Property neuragenix.dao.displayDebug not present default to false.");
        }        
    }
    
    public DALQuery (DALQuery query)
    {
        this.blCaseSensitiveOn = query.blCaseSensitiveOn;
        this.blCountDistinct = query.blCountDistinct;
        this.blManualCommit = query.blManualCommit;
        this.intInsertedRecordKey = query.intInsertedRecordKey;
        this.intLimit = query.intLimit;
        this.intOffset = query.intOffset;
        this.strCountField = query.strCountField;
        this.strDistinctField = query.strDistinctField;
        this.strHavingCondition = query.strHavingCondition;
        this.strUpdateRecordKey = query.strUpdateRecordKey;
        
        if (query.hashFieldData != null)
            this.hashFieldData = (Hashtable) query.hashFieldData.clone();
        if (query.vtAverageFields != null)
            this.vtAverageFields = (Vector) query.vtAverageFields.clone();
        
        if (query.vtDomains != null)
            this.vtDomains = (Vector) query.vtDomains.clone();
        
        if (query.vtFields != null)
            this.vtFields = (Vector) query.vtFields.clone();
        
        if (query.vtFirstJoinFields != null) {
            this.vtFirstJoinFields = (Vector) query.vtFirstJoinFields.clone();
            for (int i = 0; i< vtFirstJoinFields.size(); i++)
            	this.vtFirstJoinFields.set(i,((Vector) query.vtFirstJoinFields.get(i)).clone());
        }
            
        if (query.vtGroupByFields != null)
            this.vtGroupByFields = (Vector) query.vtGroupByFields.clone();
        
        if (query.vtJoinTypes != null)
            this.vtJoinTypes = (Vector) query.vtJoinTypes.clone();
        
        if (query.vtMaxFields != null)
            this.vtMaxFields = (Vector) query.vtMaxFields.clone();
        
        if (query.vtMinFields != null)
            this.vtMinFields = (Vector) query.vtMinFields.clone();
        
        if (query.vtOrderByDirections != null)
            this.vtOrderByDirections = (Vector) query.vtOrderByDirections.clone();
        
        if (query.vtOrderByFields != null)
            this.vtOrderByFields = (Vector) query.vtOrderByFields.clone();
        
        if (query.vtOrderByFieldsAlias != null)
            this.vtOrderByFieldsAlias = (Vector) query.vtOrderByFieldsAlias.clone();
        
        if (query.vtSecondJoinFields != null) {
            this.vtSecondJoinFields = (Vector) query.vtSecondJoinFields.clone();
            for (int i = 0; i< vtSecondJoinFields.size(); i++)
            	this.vtSecondJoinFields.set(i,((Vector) query.vtSecondJoinFields.get(i)).clone());
        }
        
        if (query.vtSiblingQueries != null)
            this.vtSiblingQueries = (Vector) query.vtSiblingQueries.clone();
        
        if (query.vtSiblingQueries != null)
            this.vtSiblingQueryCloseBrackets = (Vector) query.vtSiblingQueryCloseBrackets.clone();
        
        if (query.vtSiblingQueryConnectors != null)
            this.vtSiblingQueryConnectors = (Vector) query.vtSiblingQueryConnectors.clone();
        
        if (query.vtSiblingQueryOpenBrackets != null)
            this.vtSiblingQueryOpenBrackets = (Vector) query.vtSiblingQueryOpenBrackets.clone();
        
        if (query.vtSumFields != null)
            this.vtSumFields = (Vector) query.vtSumFields.clone();
        
        if (query.vtWhereCloseBrackets != null)
            this.vtWhereCloseBrackets = (Vector) query.vtWhereCloseBrackets.clone();
        
        if (query.vtWhereConnectors != null)
            this.vtWhereConnectors = (Vector) query.vtWhereConnectors.clone();
        
        if (query.vtWhereFields != null)
            this.vtWhereFields = (Vector) query.vtWhereFields.clone();
        
        if (query.vtWhereObjects != null)
            this.vtWhereObjects = (Vector) query.vtWhereObjects.clone();
        
        if (query.vtWhereOpenBrackets != null)
            this.vtWhereOpenBrackets = (Vector) query.vtWhereOpenBrackets.clone();
        
        if (query.vtWhereOperators != null)
            this.vtWhereOperators = (Vector) query.vtWhereOperators.clone();
        
        if (query.vtWhereTypes != null)
            this.vtWhereTypes = (Vector) query.vtWhereTypes.clone();
        
        if (query.vtSubQueries != null)
            this.vtSubQueries = (Vector) query.vtSubQueries.clone();
        
        if (query.vtSubQueryAlias != null)
            this.vtSubQueryAlias = (Vector) query.vtSubQueryAlias.clone();
        
        if (query.vtSubQueryFirstJoinFields != null)
            this.vtSubQueryFirstJoinFields = (Vector) query.vtSubQueryFirstJoinFields.clone();

        if (query.vtSubQuerySecondJoinFields != null)
            this.vtSubQuerySecondJoinFields = (Vector) query.vtSubQuerySecondJoinFields.clone();
        
        if (query.vtSubQueryJoinTypes != null)
            this.vtSubQueryJoinTypes = (Vector) query.vtSubQueryJoinTypes.clone();
        
        if (query.authToken != null)
            this.authToken = query.authToken;
        
        this.rdFieldData = query.rdFieldData;
        this.rdMultiData = query.rdMultiData;
        
    }
    
    
    
    /** Set case sensitive
     */
    public void setCaseSensitive(boolean blCaseSensitiveOn)
    {
        this.blCaseSensitiveOn = blCaseSensitiveOn;
    }
    
    /** Set domain for the query
     */
//    public void setDomain(String strADomain,
//                          String strAField1,
//                          String strAField2,
//                          String strAJoinType) throws DAOQueryInvalidDomain
//    {
//        if (domainExists(strADomain))
//        {
//            vtDomains.add(strADomain);
//            vtFirstJoinFields.add(strAField1);
//            vtSecondJoinFields.add(strAField2);
//            vtJoinTypes.add(strAJoinType);
//        }
//        else
//        {
//            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - The domain " + strADomain + " is invalid");
//            throw new DAOQueryInvalidDomain("The domain supplied is invalid");
//        }
//    }
    public void setDomain(String strADomain,
    		String strAField1,
    		String strAField2,
    		String strAJoinType) throws DAOQueryInvalidDomain
    		{
    	if (domainExists(strADomain))
    	{
    		if (vtDomains.contains(strADomain)) {
    		int vindex = vtDomains.indexOf(strADomain);
    		Vector v = (Vector) vtFirstJoinFields.get(vindex);
    		v.add(strAField1);
    		Vector v2 = (Vector) vtSecondJoinFields.get(vindex);
    		v2.add(strAField2);
    			
    		} else {
    		vtDomains.add(strADomain);
    		Vector field1 = new Vector();
    		field1.add(strAField1);
    		vtFirstJoinFields.add(field1);
    		Vector field2 = new Vector();
    		field2.add(strAField2);
    		vtSecondJoinFields.add(field2);
    		vtJoinTypes.add(strAJoinType);
    		}
    	}
    	else
    	{
    		LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - The domain " + strADomain + " is invalid");
    		throw new DAOQueryInvalidDomain("The domain supplied is invalid");
    	}
    		}
    
    
    
    /** Set domain for the query
     */
    public void setSubQuery(DALQuery qrObject,
                          String strAAlias,
                          String strAField1,
                          String strAField2,
                          String strAJoinType) throws DAOQueryInvalidDomain
    {
        if (DatabaseSchema.getDBMSType() != DBMSTypes.ORACLE)
        {
            vtSubQueries.add(qrObject);
            vtSubQueryAlias.add(strAAlias);
            vtSubQueryFirstJoinFields.add(strAField1);
            vtSubQuerySecondJoinFields.add(strAField2);
            vtSubQueryJoinTypes.add(strAJoinType);
        }
        else
        {
            LogService.instance().log(LogService.ERROR, "This function has not been implemented yet for this DBMS");
            throw new DAOQueryInvalidDomain("This function has not been implemented yet for this DBMS");
        }
    }
    
    
    /** Clear domains of the query
     */
    public void clearDomains()
    {
        vtDomains.clear();
        vtFirstJoinFields.clear();
        vtSecondJoinFields.clear();
        vtJoinTypes.clear();
    }
    
    /** Set fields that needs to get Maximum value
     */
    public void setMaxField(String strAField) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        vtMaxFields.add(strAField);
    }
    
    /** Clear all max fields
     */
    public void clearMaxFields()
    {
        vtMaxFields.clear();
    }
    
    /** Set fields that needs to get Minimum value
     */
    public void setMinField(String strAField) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        vtMinFields.add(strAField);
    }
    
    /** Clear all min fields
     */
    public void clearMinFields()
    {
        vtMinFields.clear();
    }
    
    /** Set fields that needs to get Average value
     */
    public void setAverageField(String strAField) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        vtAverageFields.add(strAField);
    }
    
    /** Clear all average fields
     */
    public void clearAverageFields()
    {
        vtAverageFields.clear();
    }
    
    /** Set fields that needs to get Sum value
     */
    public void setSumField(String strAField) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        vtSumFields.add(strAField);
    }
    
    /** Clear all sum fields
     */
    public void clearSumFields()
    {
        vtSumFields.clear();
    }
    
    /** Set Count function
     */
    public void setCountField(String strAField, boolean blADistinct) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        strCountField = strAField;
        blCountDistinct = blADistinct;
    }
    
    /** Clear Count function
     */
    public void clearCountField()
    {
        strCountField = null;
        blCountDistinct = false;
    }
    
    /** Set Order By condition
     */
    public void setOrderBy(String strAField, String strADirection) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        vtOrderByFields.add(strAField);
        vtOrderByDirections.add(strADirection);
    }
    
    /** Set Order By condition using Alias
     */
    public void setOrderBy(String strAField, String strADirection, boolean blAlias) throws DAOQueryInvalidField
    {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strAField);
        
       if (fieldTemp != null) {
           String strShortAFieldName = fieldTemp.getShortInternalName();
        
           if(blAlias == true){            
               vtOrderByFieldsAlias.add(strShortAFieldName);
               vtOrderByDirections.add(strADirection);
           }
           else{
               setOrderBy(strAField, strADirection);
           }
       }
       else{
           setOrderBy(strAField, strADirection);
        }        
     
    }
    public void setOrderByFunction(String sqlFunction, String strADirection)
    {
    	//LogService.instance().log(LogService.INFO,"setOrderByFunction IS CALLED");
         
    	vtOrderBySqlFunctions.add(sqlFunction);
    	vtOrderByDirectionFunction.add(strADirection);
    }
     
    
    /** Clear all Order By conditions
     */
    public void clearOrderBy()
    {
        vtOrderByFields.clear();
        vtOrderByDirections.clear();
        vtOrderByFieldsAlias.clear();
        vtOrderBySqlFunctions.clear();
        vtOrderByDirectionFunction.clear();
    }

    /** Set Group By
     */
    public void setGroupBy(String strAField) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        vtGroupByFields.add(strAField);
    }
    
    /** Clear all Group By conditions
     */
    public void clearGroupBy()
    {
        vtGroupByFields.clear();
    }
    
    /** Set Having condition
     */
    public void setHavingCondition(String strACondition)
    {
        strHavingCondition = strACondition;
    }
    
    /** Clear Having condition
     */
    public void clearHavingCondition()
    {
        strHavingCondition = null;
    }
    
    /** Set LIMIT and OFFSET
     */
    public void setLimitOffset(int intALimit, int intAOffset)
    {
        intLimit = intALimit;
        intOffset = intAOffset;
    }
    
    /** Clear LIMIT and OFFSET
     */
    public void clearLimitOffset()
    {
        intLimit = 0;
        intOffset = 0;
    }
    
    /** Set sibling query
     */
    public void setSiblingQuery(String strAConnector, DALQuery aQuery)
    {
        vtSiblingQueryConnectors.add(strAConnector);
        vtSiblingQueries.add(aQuery);
        vtSiblingQueryOpenBrackets.add(new Integer(0));
        vtSiblingQueryCloseBrackets.add(new Integer(0));
    }
    
    /** Set sibling query
     */
    public void setSiblingQuery(String strAConnector, DALQuery aQuery, int intOpenBrackets, int intCloseBrackets)
    {
        vtSiblingQueryConnectors.add(strAConnector);
        vtSiblingQueries.add(aQuery);
        vtSiblingQueryOpenBrackets.add(new Integer(intOpenBrackets));
        vtSiblingQueryCloseBrackets.add(new Integer(intCloseBrackets));
    }
    
    /** Clear sub-queries
     */
    public void clearSubQueries()
    {
        vtSubQueries.clear();
        vtSubQueryAlias.clear();
        vtSubQueryFirstJoinFields.clear();
        vtSubQuerySecondJoinFields.clear();
        vtSubQueryJoinTypes.clear();        
    }
    

    /** Clear sibling queries
     */
    public void clearSiblingQueries()
    {
        vtSiblingQueryConnectors.clear();
        vtSiblingQueries.clear();
        vtSiblingQueryOpenBrackets.clear();
        vtSiblingQueryCloseBrackets.clear();
    }
    
    
    /** Clear case sensitive
     */
    public void clearCaseSensitive()
    {
        blCaseSensitiveOn = true;
    }
    
    /** Set fields
     */
    public void setFields(Vector vtAFieldList, ChannelRuntimeData rdData) throws DAOQueryInvalidField
    {
        if (vtAFieldList == null)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No field supplied");
            throw new DAOQueryInvalidField("No field supplied");
        }
            
        for (int index=0; index < vtAFieldList.size(); index++)
        {
            if (!DatabaseSchema.getFields().containsKey(vtAFieldList.get(index)))
            {
                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + vtAFieldList.get(index));
                throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + vtAFieldList.get(index));
            }
        }
        
        vtFields = new Vector(vtAFieldList);
        rdFieldData = rdData;
    }

	/** Set fields in a hashtable
     */
    public void setFields(Vector vtAFieldList, Hashtable hashData) throws DAOQueryInvalidField
    {
        if (vtAFieldList == null)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No field supplied");
            throw new DAOQueryInvalidField("No field supplied");
        }
            
        for (int index=0; index < vtAFieldList.size(); index++)
        {
            if (!DatabaseSchema.getFields().containsKey(vtAFieldList.get(index)))
            {
                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + vtAFieldList.get(index));
                throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + vtAFieldList.get(index));
            }
        }
        
        vtFields = new Vector(vtAFieldList);
        hashFieldData = hashData;
    }

    /** Clear fields
     */
    public void clearFields() 
    {
        vtFields = null;
        rdFieldData = null;
		hashFieldData = null;
    }
    
    /** Set fields
     */
    public void setMultiInsert(Vector vtAFieldList, ChannelRuntimeData[] rdMultiData) throws DAOQueryInvalidField
    {
        if (vtAFieldList == null)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No field supplied");
            throw new DAOQueryInvalidField("No field supplied");
        }
            
        for (int index=0; index < vtAFieldList.size(); index++)
        {
            if (!DatabaseSchema.getFields().containsKey(vtAFieldList.get(index)))
            {
                LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid");
                throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + vtAFieldList.get(index));
            }
        }
        
        vtFields = new Vector(vtAFieldList);
        this.rdMultiData = rdMultiData;
    }
    
    /** Clear fields
     */
    public void clearMultiInsert() 
    {
        vtFields = null;
        rdMultiData = null;
    }
    
    /** Set  a field
     */
    public void setField(String strAField, String strAValue) throws DAOQueryInvalidField
    {       
        if (!DatabaseSchema.getFields().containsKey(strAField))
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }
        
        if (vtFields == null)
            vtFields = new Vector(10, 5);
        vtFields.add(strAField);
        
        if (rdFieldData == null)
            rdFieldData = new ChannelRuntimeData();
        rdFieldData.setParameter(strAField, strAValue);
    }

	/** Set a field
     */
    public void setField(String strAField, String strAValue, String strTarget) throws DAOQueryInvalidField
    {       
        if (!DatabaseSchema.getFields().containsKey(strAField))
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }

        if(strTarget.equals("hashtable"))
       	{ 
            if(vtFields == null)
            {
            	vtFields = new Vector(10, 5);
            }

            vtFields.add(strAField);

            if(hashFieldData == null)
            {
               hashFieldData = new Hashtable();
            }
            hashFieldData.put(strAField, strAValue);
	}
    }
    
    /** Set WHERE clause
     */
    public void setWhere(String strAConnector,
                         int intOpenBrackets,
                         String strAField,
                         String strAOperator,
                         Object objAObject,
                         int intCloseBrackets,
                         int intWhereType) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strAField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid. Field = " + strAField);
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strAField);
        }

        vtWhereConnectors.add(strAConnector);
        vtWhereOpenBrackets.add(new Integer(intOpenBrackets));
        vtWhereFields.add(strAField);
        vtWhereOperators.add(strAOperator);
        vtWhereObjects.add(objAObject);
        vtWhereCloseBrackets.add(new Integer(intCloseBrackets));
        vtWhereTypes.add(new Integer(intWhereType));
    }
    
    /**
     *   Sets the where clause for multiple fields
     *   
     * @param strInitialConnector Connector for the first field in the set
     * @param strGroupConnector Connector for field_2 to field_n
     * @param intOpenBrackets Open brackets will start for the first field only
     * @param intCloseBrackets Close brackets are applied to the last field
     * @param intWhereType Where type is constant for all fields
     * @param vtFields Form fields that the function should be interested in
     * @param runtimeData Channel runtime data for the fields
     *
     */
    
    
    public void setWhereMultiField(String strInitialConnector, String strGroupConnector, 
                                     int intOpenBrackets,
                                     String strAOperator,
                                     int intCloseBrackets,
                                     int intWhereType,
                                     Vector vtFields,
                                     ChannelRuntimeData runtimeData)  throws DAOQueryInvalidField
    {
        int intFieldCount = 0;
        
        if (vtFields != null && vtFields.size() > 0)
        {
            for (int i = 0; i < vtFields.size(); i++)
            {
            
                String strConnector = "";
                int openBracket = 0;
                int closeBracket = 0;
                String strFieldName = (String) vtFields.get(i);
                String strComparisonValue = runtimeData.getParameter(strFieldName);
                
                if (strComparisonValue != null)
                {
                    if (intFieldCount == 0 && vtFields.size() == 1)
                    {
                        strConnector = strInitialConnector;
                        openBracket = intOpenBrackets;
                        closeBracket = intCloseBrackets;
                    }
                    else if (intFieldCount == 0) // plugging the first field in
                    {
                        strConnector = strInitialConnector;
                        openBracket = intOpenBrackets;
                    }
                    else if (intFieldCount == vtFields.size())
                    {
                        strConnector = strGroupConnector;
                        closeBracket = intCloseBrackets;
                    }
                    else // just a normal middle field
                    {
                        strConnector = strGroupConnector;
                    }

                    this.setWhere(strConnector, openBracket, strFieldName, strAOperator, strComparisonValue, closeBracket, intWhereType);
                    intFieldCount++;    
                }
            }
            
        }
        
        
        
    }
    
    
    /**
     *   Sets the where clause for multiple fields, uses data from hashtable
     *   instead of Runtime data
     *   
     * @param strInitialConnector Connector for the first field in the set
     * @param strGroupConnector Connector for field_2 to field_n
     * @param strAOperator Operator that will be applied to all fields
     * @param intOpenBrackets Open brackets will start for the first field only
     * @param intCloseBrackets Close brackets are applied to the last field
     * @param intWhereType Where type is constant for all fields
     * @param htFields Hashtable of fields and data
     *
     */
    
    
    public void setWhereMultiField(String strInitialConnector, String strGroupConnector, 
                                     int intOpenBrackets,
                                     String strAOperator,
                                     int intCloseBrackets,
                                     int intWhereType,
                                     Hashtable htFields)  throws DAOQueryInvalidField
    {
        int intFieldCount = 0;
        
        if (htFields != null && htFields.size() > 0)
        {
            Enumeration enumFields = htFields.keys();
            
            while (enumFields.hasMoreElements())
            {
                String strConnector = "";
                int openBracket = 0;
                int closeBracket = 0;
                String strFieldName = (String) enumFields.nextElement();
                String strComparisonValue = (String) htFields.get(strFieldName);
                
                if (intFieldCount == 0 && htFields.size() == 1)
                {
                    strConnector = strInitialConnector;
                    openBracket = intOpenBrackets;
                    closeBracket = intCloseBrackets;
                }
                else if (intFieldCount == 0) // plugging the first field in
                {
                    strConnector = strInitialConnector;
                    openBracket = intOpenBrackets;
                }
                else if (intFieldCount == htFields.size())
                {
                    strConnector = strGroupConnector;
                    closeBracket = intCloseBrackets;
                }
                else // just a normal middle field
                {
                    strConnector = strGroupConnector;
                }
                
                this.setWhere(strConnector, openBracket, strFieldName, strAOperator, strComparisonValue, closeBracket, intWhereType);
                intFieldCount++;
            }
            
        }
        
    }
    
    
    
    /** Clear Where conditions
     */
    public void clearWhere()
    {
        vtWhereConnectors.clear();
        vtWhereOpenBrackets.clear();
        vtWhereFields.clear();
        vtWhereOperators.clear();
        vtWhereObjects.clear();
        vtWhereCloseBrackets.clear();
        vtWhereTypes.clear();
    }
    
    /** Checks to see if the supplied domain a valid domain
     */
    protected boolean domainExists(String strADomainToCheck)
    {
        return DatabaseSchema.getDomains().containsKey(strADomainToCheck);
    }
    
    /** Convert a SELECT query to a string
     */
    public String convertSelectQueryToString() throws DAOQueryInvalidDomain, DAOQueryInvalidField, DBMSInvalidOperator
    {      
        // must have at least one domain for the query
        if (vtDomains.size() < 1)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - No domain supplied");
            throw new DAOQueryInvalidDomain ("No domain supplied");
        }
        
        
        StringBuffer strResult = new StringBuffer("SELECT ");
        
        if (blDistinct) {
        	strResult.append(" DISTINCT ");
        }
        
        // add MAX fields
        for (int index=0; index < vtMaxFields.size(); index++)
        {
            String strFieldName = (String) vtMaxFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("MAX(" + fieldTemp.getExternalName() + ") AS " + DBMSTypes.getField("MAX_" + fieldTemp.getShortInternalName()) + ", ");
        }
        
        // add MIN fields
        for (int index=0; index < vtMinFields.size(); index++)
        {
            String strFieldName = (String) vtMinFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("MIN(" + fieldTemp.getExternalName() + ") AS " + DBMSTypes.getField("MIN_" + fieldTemp.getShortInternalName()) + ", ");
        }
        
        // add AVERAGE fields
        for (int index=0; index < vtAverageFields.size(); index++)
        {
            String strFieldName = (String) vtAverageFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("AVG(" + fieldTemp.getExternalName() + ") AS " + DBMSTypes.getField("AVG_" + fieldTemp.getShortInternalName()) + ", ");
        }
        
        // add SUM fields
        for (int index=0; index < vtSumFields.size(); index++)
        {
            String strFieldName = (String) vtSumFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("SUM(" + fieldTemp.getExternalName() + ") AS " + DBMSTypes.getField("SUM_" + fieldTemp.getShortInternalName()) + ", ");
        }
        
        // add COUNT
        if (strCountField != null)
        {
            if (strCountField.equals("*"))
            {
                strResult.append("COUNT(*) AS COUNT_ALL, ");
            }
            else
            {
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strCountField);
                
                if (blCountDistinct)
                    if (DatabaseSchema.getDBMSType() == DBMSTypes.SQLSERVER)
                       strResult.append("COUNT(DISTINCT (" + fieldTemp.getExternalName() + ")) AS " + DBMSTypes.getField("COUNT_" + fieldTemp.getShortInternalName()) + ", ");
                    else
                       strResult.append("COUNT(DISTINCT " + fieldTemp.getExternalName() + ") AS " + DBMSTypes.getField("COUNT_" + fieldTemp.getShortInternalName()) + ", ");
                else
                    strResult.append("COUNT(" + fieldTemp.getExternalName() + ") AS " + DBMSTypes.getField("COUNT_" + fieldTemp.getShortInternalName()) + ", ");
            }
        }
        
        // add fields
        if (vtFields != null)
        {
            for (int index=0; index < vtFields.size(); index++)
            {
                String strFieldName = (String) vtFields.get(index);
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
                
                if (strDistinctField != null && strDistinctField.equals(strFieldName))
                    if (DatabaseSchema.getDBMSType() == DBMSTypes.SQLSERVER)
                       strResult.append("DISTINCT (" + fieldTemp.getExternalName() + ") AS " + fieldTemp.getShortInternalName() + ", ");
                    else
                       strResult.append("DISTINCT " + fieldTemp.getExternalName() + " AS " + fieldTemp.getShortInternalName() + ", ");
                else
                    strResult.append(fieldTemp.getExternalName() + " AS " + DBMSTypes.getField(fieldTemp.getShortInternalName()) + ", ");
            }
            strResult = new StringBuffer(strResult.substring(0, strResult.length() - 2) + " FROM ");
        }
        else
            strResult = new StringBuffer(strResult.substring(0, strResult.length() - 2) + " FROM ");
        
        // add domains
        Vector<String> vtLeftovers = new Vector<String>();
        String strFirstDomainName = (String) DatabaseSchema.getDomains().get(vtDomains.get(0));
        strResult.append(strFirstDomainName);
        boolean setFirstDomainClause = false;
        for (int index=1; index < vtDomains.size(); index++)
        {
        		setFirstDomainClause = false;
            String strPrevDomainName = (String) DatabaseSchema.getDomains().get(vtDomains.get(index-1));
            
            String strDomainName = (String) DatabaseSchema.getDomains().get(vtDomains.get(index));
            String strJoinType = (String) vtJoinTypes.get(index);
            
            // there is no join for this domain
            if (strJoinType == null)
                strResult.append(", " + strDomainName);
            else
            	
            {
            	Vector joinFields1 = (Vector) vtFirstJoinFields.get(index);
            	Vector joinFields2 = (Vector) vtSecondJoinFields.get(index);
            
            	for (int i=0; i < joinFields1.size(); i++) {
            			DBField firstJoinField =(DBField) DatabaseSchema.getFields().get(joinFields1.get(i));
            			 DBField secondJoinField = (DBField) DatabaseSchema.getFields().get(joinFields2.get(i));
            		// if ( firstJoinField == null)
            				// System.err.println("1st join field is null "+i);
            			// if ( secondJoinField == null)
            				// System.err.println("2nd join field is null "+i);
            			 //-System.err.println("'"+DatabaseSchema.secondJoinField.getDomain() + "'\t'" +firstJoinField.getDomain() + "'=" + strPrevDomainName  );
            			 String firstJoinFieldDomain = (String) DatabaseSchema.getDomains().get(firstJoinField.getDomain());
            			 String secondJoinFieldDomain = (String) DatabaseSchema.getDomains().get(secondJoinField.getDomain());
            			 List subList =  vtDomains.subList(0, index);
            			 boolean previouslyCovered = subList.contains(firstJoinField.getDomain()) || subList.contains(secondJoinField.getDomain());
            			 if (firstJoinFieldDomain.equals(strPrevDomainName) || secondJoinFieldDomain.equals(strPrevDomainName) || previouslyCovered) {
            			 
            				 if (setFirstDomainClause==false) {
            					 strResult.append(DBMSTypes.getOperator(strJoinType) + strDomainName + " ON " + firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
            					 setFirstDomainClause=true;
            				 }
            				 else
            					 strResult.append(" AND "+ firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
            				
            			 }
            			 else {
            				 vtLeftovers.add(firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
            				
            				 
            			 }
            		
            	}
            			
              //  DBField firstJoinField = (DBField) DatabaseSchema.getFields().get(vtFirstJoinFields.get(index));
             //   DBField secondJoinField = (DBField) DatabaseSchema.getFields().get(vtSecondJoinFields.get(index));
                
              //  strResult.append(DBMSTypes.getOperator(strJoinType) + strDomainName + " ON " + firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
            }
        }
        
        // add sub-queries
        for (int index=0; index < vtSubQueries.size(); index++)
        {
            DALQuery qrObject = (DALQuery) vtSubQueries.get(index);
            String strAlias = (String) vtSubQueryAlias.get(index);
            String strJoinType = (String) vtSubQueryJoinTypes.get(index);
            
            // there is no join for this sub query
            if (strJoinType == null) {
                strResult.append(", (" + qrObject.convertSelectQueryToString() + ") AS " + strAlias);
            }
            else {
                DBField firstJoinField = (DBField) DatabaseSchema.getFields().get(vtSubQueryFirstJoinFields.get(index));
                DBField secondJoinField = (DBField) DatabaseSchema.getFields().get(vtSubQuerySecondJoinFields.get(index));
                
                strResult.append(DBMSTypes.getOperator(strJoinType) + "(" + qrObject.convertSubSelectQueryToString() + ") AS " +  strAlias + " ON " + firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
            }
        }

        
        // add WHERE clauses
        for (int index=0; index < vtWhereTypes.size(); index++)
        {
            // if we have WHERE clauses
            if (index < 1)
                strResult.append(" WHERE ");
         
            int intOpenBrackets;
            int intCloseBrackets;
            String strWhereConnector;
            String strWhereOperator;
            DBField firstWhereField;
            DBField secondWhereField;
 
            int intWhereType = ((Integer) vtWhereTypes.get(index)).intValue();
            switch (intWhereType)
            {
                case WHERE_HAS_VALUE:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    strWhereOperator = (String) vtWhereOperators.get(index);
                    firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                    String data = (String) vtWhereObjects.get(index);
                    int intMaxDataLength = firstWhereField.getLength();
                    int intDataType = firstWhereField.getDataType();
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add fields and operator
                    if (strWhereOperator.equals("LIKE"))
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '%" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "%'");
                            
                        else
                            strResult.append(firstWhereField.getExternalName() + " LIKE '%" + DBMSTypes.cleanDataForQuery(data) + "%'");
                    }
                    else if (strWhereOperator.equals("LEFT LIKE"))
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "%'"); 
                        else
                            strResult.append(firstWhereField.getExternalName() + " LIKE '" + DBMSTypes.cleanDataForQuery(data) + "%'"); 
                    }
                    else if (strWhereOperator.equals("RIGHT LIKE"))
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '%" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "'"); 
                        else
                            strResult.append(firstWhereField.getExternalName() + " LIKE '%" + DBMSTypes.cleanDataForQuery(data) + "'"); 
                    }
                    else if (strWhereOperator.equals("NOT LIKE"))
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " NOT LIKE '" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "%'"); 
                        else
                            strResult.append(firstWhereField.getExternalName() + " NOT LIKE '" + DBMSTypes.cleanDataForQuery(data) + "%'"); 
                    }
                    else
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + DBMSTypes.getOperator(strWhereOperator));
                        else
                            strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));

                        switch (intDataType)
                        {
                            // Data is integer
                            case DBMSTypes.INT_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                            // Data is floating point
                            case DBMSTypes.FLOAT_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                            // Data is double
                            case DBMSTypes.DOUBLE_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                            // Data is date
                            case DBMSTypes.DATE_TYPE:
                                strResult.append(DBMSTypes.convertToString(Utilities.convertDateForQuery(data)));
                                break;
                                
                            // Data is time
                            case DBMSTypes.TIME_TYPE:
                                strResult.append(DBMSTypes.convertToTime(Utilities.convertTimeForQuery(data)));
                                break;

                            // Data is string
                            case DBMSTypes.STR_TYPE:
                                // if data is longer than the max allowable length, need truncation
                                if (data.length() > intMaxDataLength)
                                    data = data.substring(0, intMaxDataLength);
                                    
                                if (!blCaseSensitiveOn)
                                    data = data.toLowerCase();
                                
                                strResult.append(DBMSTypes.convertToString(data));
                                break;
                                
                            // Data is duration    
                            case DBMSTypes.DURATION_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                        }
                    }
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    break;
                    
                case WHERE_BOTH_FIELDS:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    strWhereOperator = (String) vtWhereOperators.get(index);
                    firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                    secondWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereObjects.get(index));
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add fields and operator
                    strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator) +secondWhereField.getExternalName());
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    
                    break;
                    
                case WHERE_HAS_NULL_VALUE:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    String strWhereValue = (String) vtWhereObjects.get(index);
                    firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add field and operator
                    strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereValue));
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    break;
                    
                case WHERE_HAS_SUB_QUERY:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    strWhereOperator = (String) vtWhereOperators.get(index);
                    DALQuery sub_query = (DALQuery) vtWhereObjects.get(index);
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add field and operator
                    if (vtWhereFields.get(index) == null)
                        strResult.append(DBMSTypes.getOperator(strWhereOperator));
                    else
                    {
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                        strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));
                    }
                    
                    // add sub-query recursively
                    strResult.append("(" + sub_query.convertSelectQueryToString() + ")");
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    break;
            }
        }
        
        
        // Add leftover where clauses (from domain joins)
        if (vtLeftovers.size() > 0) {
       
        	 if (vtWhereFields.size() == 0 ) {

        		 strResult.append(" WHERE ");
        	 }
        	 else
        		 strResult.append(" AND ");
        	 
        	 for (int index=0; index < vtLeftovers.size(); index++) {
        		 
        		 	
        		 if (index > 0 ) {
        			 strResult.append(" AND ");
        		 }
        		
        			 strResult.append(vtLeftovers.get(index));
      
        	 }
        }
        	 
         // add sibling queries
        for (int index=0; index < vtSiblingQueries.size(); index++) {
            int intOpenBrackets = ((Integer) vtSiblingQueryOpenBrackets.get(index)).intValue();
            int intCloseBrackets = ((Integer) vtSiblingQueryCloseBrackets.get(index)).intValue();
            
            
            if (DatabaseSchema.getDBMSType() == DBMSTypes.SQLSERVER)
            {
                if (!vtSiblingQueryConnectors.get(index).equals("UNION"))
                {    
                    if (vtWhereFields.size() > 0 || vtLeftovers.size() > 0)
                    {
                        strResult.append(" AND " + DBMSTypes.getOperator((String) vtSiblingQueryConnectors.get(index)) + " (");
                    }
                    else
                    {
                        strResult.append(" WHERE " + DBMSTypes.getOperator((String) vtSiblingQueryConnectors.get(index)) + " (");
                    }
                }
                else 
                {
                    strResult.append(DBMSTypes.getOperator((String) vtSiblingQueryConnectors.get(index)) + " (");
                }    
            }
            else
            {
                strResult.append(DBMSTypes.getOperator((String) vtSiblingQueryConnectors.get(index)));    
            }            
            
            
            // add open brackets
            for (int i=0; i < intOpenBrackets; i++) {
                strResult.append("(");
            }
            
            strResult.append(((DALQuery) vtSiblingQueries.get(index)).convertSelectQueryToString());
            
            // add close brackets
            for (int i=0; i < intCloseBrackets; i++) {
                strResult.append(")");
            }
            
            if (DatabaseSchema.getDBMSType() == DBMSTypes.SQLSERVER)
            {
                strResult.append(" )");
            }
            
        }
        
        
   
        // add GROUP BY
        for (int index=0; index < vtGroupByFields.size(); index++)
        {
            DBField groupbyField = (DBField) DatabaseSchema.getFields().get(vtGroupByFields.get(index));
            if (index < 1)
                strResult.append(" GROUP BY " + groupbyField.getExternalName());
            else
                strResult.append(", " + groupbyField.getExternalName());
        }
        
                
        // add HAVING
        if (strHavingCondition != null)
            strResult.append(" HAVING " + strHavingCondition); 
        
        // add ORDER BY        
        // Check if both vtOrderByFieldsAlias & vtOrderByFields is defined, if so throw error
        if( vtOrderByFieldsAlias.isEmpty() == false && vtOrderByFields.isEmpty() == false){
            
            System.err.println("Unknown error while trying to execute the SELECT: [DALQuery Error] - Invalid orderby defined.");   
            LogService.instance().log(LogService.ERROR, "Unknown error while trying to execute the SELECT: [DALQuery Error] - Invalid orderby defined.");           
         
        }
        
        // if vtOrderByFieldsAlias is not defined proceed as below
        if(vtOrderByFieldsAlias.isEmpty() == true){
            
            for (int index=0; index < vtOrderByFields.size(); index++)
            {
                DBField orderbyField = (DBField) DatabaseSchema.getFields().get(vtOrderByFields.get(index));
                if (index < 1)
                    strResult.append(" ORDER BY " + orderbyField.getExternalName() + " " + vtOrderByDirections.get(index));
                else
                    strResult.append(", " + orderbyField.getExternalName() + " " + vtOrderByDirections.get(index));
            }
            
        }else{ //else if vtOrderByFieldsAlias is defined proceed as below
            
            for (int index=0; index < vtOrderByFieldsAlias.size(); index++)
            {      
                String strAlias = vtOrderByFieldsAlias.get(index).toString();
                
                if (index < 1)                   
                   strResult.append(" ORDER BY " + DBMSTypes.getAlias(strAlias) + " " + vtOrderByDirections.get(index));
                else
                    strResult.append(", "+ DBMSTypes.getAlias(strAlias) + " " + vtOrderByDirections.get(index));
            }
             
            
        }        
        //LogService.instance().log(LogService.INFO, "ORDER BY FUNCTION HERE");
        if(!vtOrderBySqlFunctions.isEmpty())
        {
        	
        	//LogService.instance().log(LogService.INFO, "ORDER BY FUNCTION INSIDE");
        	 for (int index=0; index < vtOrderBySqlFunctions.size(); index++)
             { 
             	
                 if (index < 1)                   
                    strResult.append(" ORDER BY " + vtOrderBySqlFunctions.get(index).toString() + " " + vtOrderByDirectionFunction.get(index));
                 else
                    strResult.append(", " + vtOrderBySqlFunctions.get(index).toString() + " " + vtOrderByDirectionFunction.get(index));
             }
        }
        // add LIMIT, OFFSET
        //if (intLimit != 0)
        //    strResult += DBMSTypes.getLimitOffset(intLimit, intOffset);
         
        return strResult.toString();       
            
    }
    
    /** Convert a Sub-SELECT query to a string
     */
    public String convertSubSelectQueryToString() throws DAOQueryInvalidDomain, DAOQueryInvalidField, DBMSInvalidOperator
    {      
        // must have at least one domain for the query
        if (vtDomains.size() < 1)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - No domain supplied");
            throw new DAOQueryInvalidDomain ("No domain supplied");
        }
        
        
        StringBuffer strResult = new StringBuffer("SELECT ");
        
        // add MAX fields
        for (int index=0; index < vtMaxFields.size(); index++)
        {
            String strFieldName = (String) vtMaxFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("MAX(" + fieldTemp.getExternalName() + "), ");
        }
        
        // add MIN fields
        for (int index=0; index < vtMinFields.size(); index++)
        {
            String strFieldName = (String) vtMinFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("MIN(" + fieldTemp.getExternalName() + "), ");
        }
        
        // add AVERAGE fields
        for (int index=0; index < vtAverageFields.size(); index++)
        {
            String strFieldName = (String) vtAverageFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("AVG(" + fieldTemp.getExternalName() + "), ");
        }
        
        // add SUM fields
        for (int index=0; index < vtSumFields.size(); index++)
        {
            String strFieldName = (String) vtSumFields.get(index);
            DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strResult.append("SUM(" + fieldTemp.getExternalName() + "), ");
        }
        
        // add COUNT
        if (strCountField != null)
        {
            if (strCountField.equals("*"))
            {
                strResult.append("COUNT(*) AS COUNT_ALL, ");
            }
            else
            {
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strCountField);
                
                if (blCountDistinct)
                    strResult.append("COUNT(DISTINCT " + fieldTemp.getExternalName() + ") , ");
                else
                    strResult.append("COUNT(" + fieldTemp.getExternalName() + ") , ");
            }
        }
        
        // add fields
        if (vtFields != null)
        {
            for (int index=0; index < vtFields.size(); index++)
            {
                String strFieldName = (String) vtFields.get(index);
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(strFieldName);
                
                if (strDistinctField != null && strDistinctField.equals(strFieldName))
                    strResult.append("DISTINCT " + fieldTemp.getExternalName() + ", ");
                else
                    strResult.append(fieldTemp.getExternalName() + ", ");
            }
            strResult = new StringBuffer(strResult.substring(0, strResult.length() - 2) + " FROM ");
        }
        else
            strResult = new StringBuffer(strResult.substring(0, strResult.length() - 2) + " FROM ");
        
        // add domains
        String strFirstDomainName = (String) DatabaseSchema.getDomains().get(vtDomains.get(0));
        strResult.append(strFirstDomainName);
        for (int index=1; index < vtDomains.size(); index++)
        {
            String strPrevDomainName = (String) DatabaseSchema.getDomains().get(vtDomains.get(index-1));
            String strDomainName = (String) DatabaseSchema.getDomains().get(vtDomains.get(index));
            String strJoinType = (String) vtJoinTypes.get(index);
            
            // there is no join for this domain
            if (strJoinType == null)
                strResult.append(", " + strDomainName);
            else
            {
            	  	Vector joinFields1 = (Vector) vtFirstJoinFields.get(index);
                	Vector joinFields2 = (Vector) vtSecondJoinFields.get(index);
                	for (int i=0; i < joinFields1.size(); i++) {
                			DBField firstJoinField =(DBField) DatabaseSchema.getFields().get(joinFields1.get(i));
                			 DBField secondJoinField = (DBField) DatabaseSchema.getFields().get(joinFields2.get(i));
                			 
                			 if (i==0) {
                				 strResult.append(DBMSTypes.getOperator(strJoinType) + strDomainName + " ON " + firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
                			 }
                			 else
                				 strResult.append(" AND "+ firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
                		
                		
                	}
                			
                             	
            	
               // DBField firstJoinField = (DBField) DatabaseSchema.getFields().get(vtFirstJoinFields.get(index));
              //  DBField secondJoinField = (DBField) DatabaseSchema.getFields().get(vtSecondJoinFields.get(index));
                
               // strResult.append(DBMSTypes.getOperator(strJoinType) + strDomainName + " ON " + firstJoinField.getExternalName() + " = " + secondJoinField.getExternalName());
            }
        }
        
        // add WHERE clauses
        for (int index=0; index < vtWhereTypes.size(); index++)
        {
            // if we have WHERE clauses
            if (index < 1)
                strResult.append(" WHERE ");
         
            int intOpenBrackets;
            int intCloseBrackets;
            String strWhereConnector;
            String strWhereOperator;
            DBField firstWhereField;
            DBField secondWhereField;
 
            int intWhereType = ((Integer) vtWhereTypes.get(index)).intValue();
            switch (intWhereType)
            {
                case WHERE_HAS_VALUE:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    strWhereOperator = (String) vtWhereOperators.get(index);
                    firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                    String data = (String) vtWhereObjects.get(index);
                    int intMaxDataLength = firstWhereField.getLength();
                    int intDataType = firstWhereField.getDataType();
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add fields and operator
                    if (strWhereOperator.equals("LIKE"))
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '%" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "%'");
                        else
                            strResult.append(firstWhereField.getExternalName() + " LIKE '%" + DBMSTypes.cleanDataForQuery(data) + "%'");
                    }
                    else if (strWhereOperator.equals("LEFT LIKE"))
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "%'"); 
                        else
                            strResult.append(firstWhereField.getExternalName() + " LIKE '" + DBMSTypes.cleanDataForQuery(data) + "%'"); 
                    }
                    else if (strWhereOperator.equals("RIGHT LIKE"))
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '%" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "'"); 
                        else
                            strResult.append(firstWhereField.getExternalName() + " LIKE '%" + DBMSTypes.cleanDataForQuery(data) + "'"); 
                    }
                    else
                    {
                        if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                            strResult.append(DBMSTypes.lower(firstWhereField.getExternalName()) + DBMSTypes.getOperator(strWhereOperator));
                        else
                            strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));

                        switch (intDataType)
                        {
                            // Data is integer
                            case DBMSTypes.INT_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                            // Data is floating point
                            case DBMSTypes.FLOAT_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                            // Data is double
                            case DBMSTypes.DOUBLE_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                            // Data is date
                            case DBMSTypes.DATE_TYPE:
                                strResult.append(DBMSTypes.convertToString(Utilities.convertDateForQuery(data)));
                                break;
                                
                            // Data is time
                            case DBMSTypes.TIME_TYPE:
                                strResult.append(DBMSTypes.convertToTime(Utilities.convertTimeForQuery(data)));
                                break;

                            // Data is string
                            case DBMSTypes.STR_TYPE:
                                // if data is longer than the max allowable length, need truncation
                                if (data.length() > intMaxDataLength)
                                    data = data.substring(0, intMaxDataLength);
                                    
                                if (!blCaseSensitiveOn)
                                    data = data.toLowerCase();
                                
                                strResult.append(DBMSTypes.convertToString(data));
                                break;
                                
                            // Data is duration    
                            case DBMSTypes.DURATION_TYPE:
                                strResult.append(DBMSTypes.convertToNumeric(data));
                                break;

                        }
                    }
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    break;
                    
                case WHERE_BOTH_FIELDS:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    strWhereOperator = (String) vtWhereOperators.get(index);
                    firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                    secondWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereObjects.get(index));
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add fields and operator
                    strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator) +secondWhereField.getExternalName());
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    
                    break;
                    
                case WHERE_HAS_NULL_VALUE:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    String strWhereValue = (String) vtWhereObjects.get(index);
                    firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add field and operator
                    strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereValue));
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    break;
                    
                case WHERE_HAS_SUB_QUERY:
                    intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                    intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                    strWhereConnector = (String) vtWhereConnectors.get(index);
                    strWhereOperator = (String) vtWhereOperators.get(index);
                    DALQuery sub_query = (DALQuery) vtWhereObjects.get(index);
                    
                    // add the connector
                    if (strWhereConnector != null)
                        strResult.append(DBMSTypes.getOperator(strWhereConnector));
                    
                    // add open brackets
                    for (int i=0; i < intOpenBrackets; i++)
                        strResult.append("(");
                    
                    // add field and operator
                    if (vtWhereFields.get(index) == null)
                        strResult.append(DBMSTypes.getOperator(strWhereOperator));
                    else
                    {
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                        strResult.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));
                    }
                    
                    // add sub-query recursively
                    strResult.append("(" + sub_query.convertSelectQueryToString() + ")");
                    
                    // add close brackets
                    for (int i=0; i < intCloseBrackets; i++)
                        strResult.append(")");
                    break;
            }
        }

        // add sibling queries
        for (int index=0; index < vtSiblingQueries.size(); index++) {
            int intOpenBrackets = ((Integer) vtSiblingQueryOpenBrackets.get(index)).intValue();
            int intCloseBrackets = ((Integer) vtSiblingQueryCloseBrackets.get(index)).intValue();
            
            strResult.append(DBMSTypes.getOperator((String) vtSiblingQueryConnectors.get(index)));
            // add open brackets
            for (int i=0; i < intOpenBrackets; i++) {
                strResult.append("(");
            }
            
            strResult.append(((DALQuery) vtSiblingQueries.get(index)).convertSelectQueryToString());
            // add close brackets
            for (int i=0; i < intCloseBrackets; i++) {
                strResult.append(")");
            }
        }
        
   
        // add GROUP BY
        for (int index=0; index < vtGroupByFields.size(); index++)
        {
            DBField groupbyField = (DBField) DatabaseSchema.getFields().get(vtGroupByFields.get(index));
            if (index < 1)
                strResult.append(" GROUP BY " + groupbyField.getExternalName());
            else
                strResult.append(", " + groupbyField.getExternalName());
        }
        
                
        // add HAVING
        if (strHavingCondition != null)
            strResult.append(" HAVING " + strHavingCondition); 
        
        // add ORDER BY        
        // Check if both vtOrderByFieldsAlias & vtOrderByFields is defined, if so throw error
        if( vtOrderByFieldsAlias.isEmpty() == false && vtOrderByFields.isEmpty() == false){
            
            System.err.println("Unknown error while trying to execute the SELECT: [DALQuery Error] - Invalid orderby defined.");   
            LogService.instance().log(LogService.ERROR, "Unknown error while trying to execute the SELECT: [DALQuery Error] - Invalid orderby defined.");           
         
        }
        
        // if vtOrderByFieldsAlias is not defined proceed as below
        // LogService.instance().log(LogService.INFO, "ORDER BY NORMAL ORDER BY");
        if(vtOrderByFieldsAlias.isEmpty() == true){
            
            for (int index=0; index < vtOrderByFields.size(); index++)
            {
                DBField orderbyField = (DBField) DatabaseSchema.getFields().get(vtOrderByFields.get(index));
                if (index < 1)
                    strResult.append(" ORDER BY " + orderbyField.getExternalName() + " " + vtOrderByDirections.get(index));
                else
                    strResult.append(", " + orderbyField.getExternalName() + " " + vtOrderByDirections.get(index));
            }
            
        }else{ //else if vtOrderByFieldsAlias is defined proceed as below
            
            for (int index=0; index < vtOrderByFieldsAlias.size(); index++)
            {      
                String strAlias = vtOrderByFieldsAlias.get(index).toString();
                
                if (index < 1)                   
                   strResult.append(" ORDER BY " + DBMSTypes.getAlias(strAlias) + " " + vtOrderByDirections.get(index));
                else
                    strResult.append(", "+ DBMSTypes.getAlias(strAlias) + " " + vtOrderByDirections.get(index));
            }
             
            
        }
        
        
        // add LIMIT, OFFSET
        //if (intLimit != 0)
        //    strResult += DBMSTypes.getLimitOffset(intLimit, intOffset);
         
        return strResult.toString();       
            
    }    
    
    /** Execute SELECT query
     */
    public ResultSet executeSelect() throws DAOException
    {
        try
        {
            String strSQLSelect = convertSelectQueryToString();
            
            // request a log if authToken exists
            if (authToken != null) {
                DALLoggerRepository.addLog("SELECT", strSQLSelect, vtFields, null, authToken);
            }
            
            // Create a connection if we don't have
            if (con == null)
            {
                con = RDBMServices.getConnection();
            //    conCount.addcon(this.hashCode());
            }
                
            // Create the preparedstatment
            ps = con.prepareStatement(strSQLSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            ResultSet rsResultSet = ps.executeQuery();
            NGXResultSet ngxResultSet = new NGXResultSet(rsResultSet, this);
            
            vtResultSets.add(ngxResultSet);
            
            if (intLimit != 0) {
                ngxResultSet.setOffset(intOffset);
                ngxResultSet.setLimit(intLimit);
            }
            ngxResultSet.beforeFirst();
            
            return ngxResultSet;
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, e.toString());
            throw new DAOException("Unknown error while trying to execute the SELECT: " + e.toString());
        }
    }
    
   
    
    public void finalize()
    {
       try
       {
            if (vtResultSets != null)
            {
                for (int i = 0; i < vtResultSets.size(); i++)
                {
                    ResultSet rsClear = (ResultSet) vtResultSets.get(i);
                    
                    if (rsClear != null)
                    {
                        try
                        {
                            rsClear.close();
                        }
                        catch (Exception e)
                        {
                            if (DISPLAY_DAL_DEBUG)
                            {
                                LogService.instance().log(LogService.ERROR, "[DALQuery] Warning - Unable to release database connection", e);
                                System.err.println ("[DALQuery] Warning - Unable to close ResultSet");
                                e.printStackTrace(System.err);
                            }
                        }

                        rsClear = null;
                    }
                
                }
                vtResultSets.clear();
                vtResultSets = null;
            }
        }
        catch (Exception e)
        {
            if (DISPLAY_DAL_DEBUG)
            {
                LogService.instance().log(LogService.ERROR, "[DALQuery] - Unexpected error when destroying connections", e);
                System.err.println("[DALQuery] - Unexpected error when destroying connections");
                e.printStackTrace(System.err);
            }
        }
        finally
        {
            try
            {
                releaseConnection();
            }
            catch (Exception rc)
            {
                if (DISPLAY_DAL_DEBUG)
                {
                    LogService.instance().log(LogService.ERROR, "[DALQuery] Warning - Unable to release database connection", rc);
                    System.err.println ("[DALQuery] Warning - Unable to release database connection");
                    rc.printStackTrace(System.err);
                }
            }
            // clean out all the other objects that we're holding
            reset();
        }
        
    }
    
    
   /** Execute UPDATE query
     */
    public boolean executeUpdate() throws DAOException, DAOUpdateInvalidDomain, DBMSInvalidOperator
    {
        boolean blUpdateOK = true;
        
        // must have only one domain to do the UPDATE
        if (vtDomains.size() < 1 || vtDomains.size() > 1)
        {
            blUpdateOK = false;
            LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidDomain - The number of domains specified is invalid. Only one allowed!");
            throw new DAOUpdateInvalidDomain ("The number of domains specified is invalid. Only one allowed!");
        }
        
        // must have at least one field
        if (vtFields == null || vtFields.size() < 1)
        {
            LogService.instance().log(LogService.ERROR, "DAOException - No field supplied");
            throw new DAOException ("No field supplied");
        }
       
		// must have data
        if (rdFieldData == null && hashFieldData == null)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No runtime/hash data supplied");
            throw new DAOQueryInvalidField ("No runtime/hash data supplied");
        }

        try
        {
            StringBuffer strSQLUpdate = new StringBuffer("UPDATE " + DatabaseSchema.getDomains().get(vtDomains.get(0)) + " SET ");
            
            String strPrimaryKey = null;
            // for Oracle we need to add primary key field
            if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                strPrimaryKey = DatabaseSchema.getPrimaryKey((String) vtDomains.get(0));
            }
            
            // transfer all data from hashtable to runtimedata
            if(hashFieldData != null)
            {
                transferHashtableToRuntimeData();
            }

            Vector vtCLOB = new Vector();
            Vector vtCLOBFieldname = new Vector();
            for (int index=0; index < vtFields.size(); index++)
            {
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                if (fieldTemp.getDataType() == DBMSTypes.CLOB_TYPE && DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                    vtCLOB.add(fieldTemp.getNameForUpdate());
                    vtCLOBFieldname.add(vtFields.get(index));
                    strSQLUpdate.append(fieldTemp.getNameForUpdate() + " = EMPTY_CLOB(), ");
                }
                else {
                    strSQLUpdate.append(fieldTemp.getNameForUpdate() + " = ?, ");
                }
            }
            strSQLUpdate = new StringBuffer(strSQLUpdate.substring(0, strSQLUpdate.length() - 2));
            
            // add WHERE clauses
            
            for (int index=0; index < vtWhereTypes.size(); index++)
            {
                // if we have WHERE clauses
                if (index < 1){
                    strSQLUpdate.append(" WHERE ");

                }

                int intOpenBrackets;
                int intCloseBrackets;
                String strWhereConnector;
                String strWhereOperator;
                DBField firstWhereField;
                DBField secondWhereField;

                int intWhereType = ((Integer) vtWhereTypes.get(index)).intValue();
                switch (intWhereType)
                {
                    case WHERE_HAS_VALUE:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        strWhereOperator = (String) vtWhereOperators.get(index);
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                        String data = (String) vtWhereObjects.get(index);
                        int intDataType = firstWhereField.getDataType();

                        // add the connector
                        if (strWhereConnector != null){

                            strSQLUpdate.append(DBMSTypes.getOperator(strWhereConnector));
                        }

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLUpdate.append("(");

                        // add fields and operator
                        if (strWhereOperator.equals("LIKE"))
                        {
                            if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE){
                                strSQLUpdate.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '%" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "%'");

                            }
                            else {
                                strSQLUpdate.append(firstWhereField.getExternalName() + " LIKE '%" + DBMSTypes.cleanDataForQuery(data) + "%'");

                            }
                            //strSQLUpdate.append(firstWhereField.getExternalName() + " LIKE '%" + data + "%'"); 
                        }
                        else if (strWhereOperator.equals("LEFT LIKE"))
                        {   
                            if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE){
                                strSQLUpdate.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "%'"); 

                            }
                            else {
                                strSQLUpdate.append(firstWhereField.getExternalName() + " LIKE '" + DBMSTypes.cleanDataForQuery(data) + "%'"); 

                            }
                            //strSQLUpdate.append(firstWhereField.getExternalName() + " LIKE '" + data + "%'"); 
                        }
                        else if (strWhereOperator.equals("RIGHT LIKE"))
                        {
                            if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                                strSQLUpdate.append(DBMSTypes.lower(firstWhereField.getExternalName()) + " LIKE '%" + DBMSTypes.cleanDataForQuery(data.toLowerCase()) + "'"); 
                            else
                                strSQLUpdate.append(firstWhereField.getExternalName() + " LIKE '%" + DBMSTypes.cleanDataForQuery(data) + "'"); 
                            //strSQLUpdate.append(firstWhereField.getExternalName() + " LIKE '%" + data + "'"); 
                        }
                        else
                        {
                            if (!blCaseSensitiveOn && intDataType == DBMSTypes.STR_TYPE)
                                strSQLUpdate.append(DBMSTypes.lower(firstWhereField.getExternalName()) + DBMSTypes.getOperator(strWhereOperator));
                             else
                                strSQLUpdate.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));
                            //strSQLUpdate.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));

                            int intMaxDataLength = firstWhereField.getLength();
                            
                            switch (intDataType)
                            {
                                // Data is integer
                                case DBMSTypes.INT_TYPE:
                                    strSQLUpdate.append(DBMSTypes.convertToNumeric(data));
                                    break;

                                // Data is floating point
                                case DBMSTypes.FLOAT_TYPE:
                                    strSQLUpdate.append(DBMSTypes.convertToNumeric(data));
                                    break;

                                // Data is double
                                case DBMSTypes.DOUBLE_TYPE:
                                    strSQLUpdate.append(DBMSTypes.convertToNumeric(data));
                                    break;

                                // Data is date
                                case DBMSTypes.DATE_TYPE:
                                    strSQLUpdate.append(DBMSTypes.convertToString(Utilities.convertDateForQuery(data)));
                                    break;

                                // Data is time
                                case DBMSTypes.TIME_TYPE:
                                    strSQLUpdate.append(DBMSTypes.convertToTime(Utilities.convertTimeForQuery(data)));
                                    break;
                                    
                                // Data is string
                                case DBMSTypes.STR_TYPE:
                                    // if data is longer than the max allowable length, need truncation
                                    if (data.length() > intMaxDataLength)
                                        data = data.substring(0, intMaxDataLength);

                                    if (!blCaseSensitiveOn)
                                        data = data.toLowerCase();
                                    
                                    strSQLUpdate.append(DBMSTypes.convertToString(data));
                                    break;
                                  
                                // Data is duration
                                case DBMSTypes.DURATION_TYPE:
                                    strSQLUpdate.append(DBMSTypes.convertToNumeric(Utilities.convertDurationForDB(data)));
                                    break;

                            }
                        }

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLUpdate.append(")");
                        break;

                    case WHERE_BOTH_FIELDS:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        strWhereOperator = (String) vtWhereOperators.get(index);
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                        secondWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereObjects.get(index));

                        // add the connector
                        if (strWhereConnector != null)
                            strSQLUpdate.append(DBMSTypes.getOperator(strWhereConnector));

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLUpdate.append("(");

                        // add fields and operator
                        strSQLUpdate.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator) +secondWhereField.getExternalName());

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLUpdate.append(")");

                        break;

                    case WHERE_HAS_NULL_VALUE:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        String strWhereValue = (String) vtWhereObjects.get(index);
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));

                        // add the connector
                        if (strWhereConnector != null)
                            strSQLUpdate.append(DBMSTypes.getOperator(strWhereConnector));

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLUpdate.append("(");

                        // add field and operator
                        strSQLUpdate.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereValue));

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLUpdate.append(")");
                        break;

                    case WHERE_HAS_SUB_QUERY:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        strWhereOperator = (String) vtWhereOperators.get(index);
                        DALQuery sub_query = (DALQuery) vtWhereObjects.get(index);

                        // add the connector
                        if (strWhereConnector != null)
                            strSQLUpdate.append(DBMSTypes.getOperator(strWhereConnector));

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLUpdate.append("(");

                        // add field and operator
                        if (vtWhereFields.get(index) == null)
                            strSQLUpdate.append(DBMSTypes.getOperator(strWhereOperator));
                        else
                        {
                            firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                            strSQLUpdate.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));
                        }

                        // add sub-query recursively
                        strSQLUpdate.append("(" + sub_query.convertSelectQueryToString() + ")");

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLUpdate.append(")");
                        break;
                }
            }
            
           
            
            // Create a connection if we don't have
            if (con == null)
                con = RDBMServices.getConnection();
            
            // Turn off auto commit so we can do transaction based updates!
            con.setAutoCommit(false);
            
            // Create the preparedstatment
            ps = con.prepareStatement(strSQLUpdate.toString());
            int psIndex = 0;
            
            for (int index=0; index < vtFields.size(); index++)
            {
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                int intDataType = fieldTemp.getDataType();
                int intMaxDataLength = fieldTemp.getLength();
                String data = null;
               	data = rdFieldData.getParameter((String) vtFields.get(index));
                
                // data is null or not specified
                if (data == null || data.trim().length() == 0)
                {
                    switch (intDataType)
                    {
                        // Data is integer
                        case DBMSTypes.INT_TYPE:
                            ps.setNull(psIndex + 1, Types.INTEGER);
                            psIndex++;
                            break;

                        // Data is floating point
                        case DBMSTypes.FLOAT_TYPE:
                            ps.setNull(psIndex + 1, Types.FLOAT);
                            psIndex++;
                            break;

                        // Data is double
                        case DBMSTypes.DOUBLE_TYPE:
                            ps.setNull(psIndex + 1, Types.DOUBLE);
                            psIndex++;
                            break;

                        // Data is date
                        case DBMSTypes.DATE_TYPE:
                            ps.setNull(psIndex + 1, Types.DATE);
                            psIndex++;
                            break;
                            
                        // Data is time
                        case DBMSTypes.TIME_TYPE:
                            ps.setNull(psIndex + 1, Types.TIME);
                            psIndex++;
                            break;

                        // Data is string
                        case DBMSTypes.STR_TYPE:
                            ps.setNull(psIndex + 1, Types.VARCHAR);
                            psIndex++;
                            break;
                            
                        // Data is duration
                        case DBMSTypes.DURATION_TYPE:
                            ps.setNull(psIndex + 1, Types.INTEGER);
                            psIndex++;
                            break;
                            
                        // Data is duration
                        case DBMSTypes.CLOB_TYPE:
                            if (DatabaseSchema.getDBMSType() != DBMSTypes.ORACLE) {
                                ps.setNull(psIndex + 1, Types.VARCHAR);
                                psIndex++;
                            }
                           
                            break;

                    }
                }
                else
                {
                    switch (intDataType)
                    {
                        // Data is integer
                        case DBMSTypes.INT_TYPE:
                            ps.setInt(psIndex + 1, Integer.parseInt(data));
                            psIndex++;
                            break;

                        // Data is floating point
                        case DBMSTypes.FLOAT_TYPE:
                            ps.setFloat(psIndex + 1, Float.parseFloat(data));
                            psIndex++;
                            break;

                        // Data is double
                        case DBMSTypes.DOUBLE_TYPE:
                            ps.setDouble(psIndex + 1, Double.parseDouble(data));
                            psIndex++;
                            break;

                        // Data is date
                        case DBMSTypes.DATE_TYPE:
                            ps.setDate(psIndex + 1, java.sql.Date.valueOf(Utilities.convertDateForDB(data)));
                            psIndex++;
                            break;
                            
                        // Data is time
                        case DBMSTypes.TIME_TYPE:
                            ps.setTime(psIndex + 1, java.sql.Time.valueOf(Utilities.convertTimeForDB(data)));
                            psIndex++;
                            break;

                        // Data is string
                        case DBMSTypes.STR_TYPE:
                            // if data is longer than the max allowable length, need truncation
                            if (data.length() > intMaxDataLength)
                                data = data.substring(0, intMaxDataLength);

                            if (ps instanceof oracle.jdbc.OraclePreparedStatement) {
                                ((oracle.jdbc.OraclePreparedStatement) ps).setFormOfUse(index + 1, oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);
                            }
                            else {
                                if (ps instanceof com.codestudio.sql.PoolManPreparedStatement) {
                                    PreparedStatement psTemp = ((com.codestudio.sql.PoolManPreparedStatement) ps).getNativePreparedStatement();
                                    if (psTemp instanceof oracle.jdbc.OraclePreparedStatement) {
                                        ((oracle.jdbc.OraclePreparedStatement) psTemp).setFormOfUse(index + 1, oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);
                                    }
                                }
                            }
                            ps.setString(psIndex + 1, data);
                            psIndex++;
                            break;
                            
                        // Data is duration
                        case DBMSTypes.DURATION_TYPE:
                            ps.setInt(psIndex + 1, Integer.parseInt(Utilities.convertDurationForDB(data)));
                            psIndex++;
                            break;

                        // Data is CLOB
                        case DBMSTypes.CLOB_TYPE:
                            if (DatabaseSchema.getDBMSType() != DBMSTypes.ORACLE) {
                                ps.setString(psIndex + 1, data);
                                psIndex++;
                            }
                            
                            break;
                    }
                }
            }
            
            // request a log if authToken exists
            if (authToken != null) {
                DALLoggerRepository.addLog("UPDATE", strSQLUpdate.toString(), vtFields, rdFieldData, authToken);
            }
             String strCopy = strSQLUpdate.toString();
            String strWhereString = strCopy.substring(strCopy.indexOf("WHERE"));

            
            
            // execute the UPDATE
            int intUpdateResult = ps.executeUpdate();
            
            // if insert fail, cancel the transaction
            if (intUpdateResult == 0)
            {   

                cancelTransaction();
                blUpdateOK = false;
            }
            else {
                // close the prepared statement
                ps.close();
                ps = null;

                // if we have CLOB objects
                if (vtCLOB.size() > 0) {

                    StringBuffer strUpdateClob = new StringBuffer("SELECT ");
                    for (int i=0; i < vtCLOB.size(); i++) {

                        strUpdateClob.append((String) vtCLOB.get(i) + ", ");
                    }
                    strUpdateClob = new StringBuffer(strUpdateClob.substring(0, strUpdateClob.length() - 2));
                    strUpdateClob.append(" FROM " + DatabaseSchema.getDomains().get(vtDomains.get(0)));
                    strUpdateClob.append(" " + strWhereString +" FOR UPDATE");

                    PreparedStatement psUpdateClob = con.prepareStatement(strUpdateClob.toString());
                    ResultSet rsUpdateClob = psUpdateClob.executeQuery();
              
                    if (rsUpdateClob.next()) {

                        for (int i=0; i < vtCLOB.size(); i++) {
                            //String strColumnName = (String) vtCLOB.get(i);
                            if (rdFieldData.getParameter((String) vtCLOBFieldname.get(i)) != null && rdFieldData.getParameter((String) vtCLOBFieldname.get(i)).trim().length() > 0) {
                                oracle.sql.CLOB clob = (oracle.sql.CLOB) rsUpdateClob.getClob(i + 1);
                                Writer wtClob = clob.getCharacterOutputStream();
                                wtClob.write(rdFieldData.getParameter((String) vtCLOBFieldname.get(i)));
                                wtClob.flush();
                                wtClob.close();
                                wtClob = null;
                            }
                        }
                    }
                    
                    rsUpdateClob.close();
                    rsUpdateClob = null;
                    psUpdateClob.close();
                    psUpdateClob = null;
                }
                
                if (!blManualCommit) {
                    commitTransaction();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            cancelTransaction();
            blUpdateOK = false;
            LogService.instance().log(LogService.ERROR, e.toString());
            throw new DAOException("Unknown error while trying to execute the UPDATE: " + e.toString());
        }
        return blUpdateOK;
    }
    
    /** Execute INSERT query
     */
    public boolean executeInsert() throws DAOException, DAOUpdateInvalidDomain, DBMSInvalidOperator
    {
        boolean blInsertOK = true;
        
        // must have only one domain to do the INSERT
        if (vtDomains.size() < 1 || vtDomains.size() > 1)
        {
            blInsertOK = false;
            LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidDomain - The number of domains specified is invalid. Only one allowed!");
            throw new DAOUpdateInvalidDomain ("The number of domains specified is invalid. Only one allowed!");
        }
        
        // must have at least one field
        if (vtFields == null || vtFields.size() < 1)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No field supplied");
            throw new DAOQueryInvalidField ("No field supplied");
        }
        
        // must have data
        if (rdFieldData == null && hashFieldData == null)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No runtime/hash data supplied");
            throw new DAOQueryInvalidField ("No runtime/hash data supplied");
        }
        
        try
        {             
            StringBuffer strSQLInsert = new StringBuffer("INSERT INTO " + DatabaseSchema.getDomains().get(vtDomains.get(0)) + " (");
           
            // transfer hashtable data to runtimedata
            if(hashFieldData != null)
            {
                transferHashtableToRuntimeData();
            }
		
            String strPrimaryKey = null;
            // for Oracle we need to add primary key field
            if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                strPrimaryKey = DatabaseSchema.getPrimaryKey((String) vtDomains.get(0));
                
                if (strPrimaryKey != null) {
                    strSQLInsert.append(DBMSTypes.getField(strPrimaryKey) + ", ");
                }
            }
            
            // Add fields into SQL statement
            for (int index=0; index < vtFields.size(); index++)
            {
                // Long change to obmit 'null' value in runtimeData to be inserted
                if( (rdFieldData.getParameter((String) vtFields.get(index)) != null) && (!rdFieldData.getParameter((String) vtFields.get(index)).trim().equals("")) ){

                    DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
    	            strSQLInsert.append(fieldTemp.getNameForUpdate() + ", ");
                }
                else {
                   DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                   if (fieldTemp.getDataType() == DBMSTypes.CLOB_TYPE) {
                      strSQLInsert.append(fieldTemp.getNameForUpdate() + ", ");
                   }
                }

                
            }            
            // cut the last comma
            strSQLInsert = new StringBuffer(strSQLInsert.substring(0, strSQLInsert.length() - 2) + ") VALUES (");
            
            // for Oracle we need to add primary key field
            if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                if (strPrimaryKey != null) {
                    strSQLInsert.append(DatabaseSchema.getSequence((String) vtDomains.get(0)) + ".NEXTVAL, ");
                }
            }
            
            Vector vtCLOB = new Vector();
            Vector vtCLOBFieldname = new Vector();
            
            // Add ? for field values
            for (int index=0; index < vtFields.size(); index++) {
                if( (rdFieldData.getParameter((String) vtFields.get(index)) != null)  && (!rdFieldData.getParameter((String) vtFields.get(index)).trim().equals("")) ) {
                    DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                    if (fieldTemp.getDataType() == DBMSTypes.CLOB_TYPE && DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                        vtCLOB.add(fieldTemp.getNameForUpdate());
                        vtCLOBFieldname.add(vtFields.get(index));
                        strSQLInsert.append("EMPTY_CLOB(), ");
                    }
                    else { 
                        strSQLInsert.append("?, ");
                    }
                }
                else {
                    DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                    if (fieldTemp.getDataType() == DBMSTypes.CLOB_TYPE && DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                        vtCLOB.add(fieldTemp.getNameForUpdate());
                        vtCLOBFieldname.add(vtFields.get(index));
                        strSQLInsert.append("EMPTY_CLOB(), ");
                    }
                }
            }
            // cut the last ?
            strSQLInsert = new StringBuffer(strSQLInsert.substring(0, strSQLInsert.length() - 2) + ")");
            
            //System.err.println(strSQLInsert);
            // Create a connection if we don't have
            if (con == null)
            {
                con = RDBMServices.getConnection();
                // conCount.addcon(this.hashCode());
            }
            
            // Turn off auto commit so we can do transaction based updates!
            con.setAutoCommit(false);
            
           // System.err.println(strSQLInsert);
            // Create the prepared statment
            ps = con.prepareStatement(strSQLInsert.toString());
            int psIndex = 0;
            
            // Set field's values
            for (int index=0; index < vtFields.size(); index++)
            {
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                int intDataType = fieldTemp.getDataType();
                int intMaxDataLength = fieldTemp.getLength();
                String data = null;
               	data = rdFieldData.getParameter((String) vtFields.get(index));
                
                // data is null or not specified
                if (data == null || data.trim().equals(""))
                {
					/*
                    switch (intDataType)
                    {
                        // Data is integer
                        case DBMSTypes.INT_TYPE:
                            ps.setNull(index + 1, Types.INTEGER);
                            break;

                        // Data is floating point
                        case DBMSTypes.FLOAT_TYPE:
                            ps.setNull(index + 1, Types.FLOAT);
                            break;

                        // Data is double
                        case DBMSTypes.DOUBLE_TYPE:
                            ps.setNull(index + 1, Types.DOUBLE);
                            break;
                            
                        // Data is date
                        case DBMSTypes.DATE_TYPE:
                            ps.setNull(index + 1, Types.DATE);
                            break;
                            
                        // Data is string
                        case DBMSTypes.STR_TYPE:
                            ps.setNull(index + 1, Types.VARCHAR);
                            break;
					}
					*/
                }
                else
                {
                    switch (intDataType)
                    {
                        // Data is integer
                        case DBMSTypes.INT_TYPE:
                            ps.setInt(psIndex + 1, Integer.parseInt(data));
			    psIndex++;
                            break;

                        // Data is floating point
                        case DBMSTypes.FLOAT_TYPE:
                            ps.setFloat(psIndex + 1, Float.parseFloat(data));
			    psIndex++;
                            break;

                        // Data is double
                        case DBMSTypes.DOUBLE_TYPE:
                            ps.setDouble(psIndex + 1, Double.parseDouble(data));
			    psIndex++;
                            break;
                            
                        // Data is date
                        case DBMSTypes.DATE_TYPE:
                            ps.setDate(psIndex + 1, java.sql.Date.valueOf(Utilities.convertDateForDB(data)));
			    psIndex++;
                            break;
                            
                        // Data is time
                        case DBMSTypes.TIME_TYPE:
                            
		            ps.setTime(psIndex + 1, java.sql.Time.valueOf(Utilities.convertTimeForDB(data)));
			    psIndex++;
                            break;
                            
                        // Data is string
                        case DBMSTypes.STR_TYPE:
                            data = data.trim();
                            // if data is longer than the max allowable length, need truncation
                            if (data.length() > intMaxDataLength)
                                data = data.substring(0, intMaxDataLength);
                        
                            ps.setString(psIndex + 1, data);
			    psIndex++;
                            break;
                            
                        // Data is CLOB
                        case DBMSTypes.CLOB_TYPE:
                            if (DatabaseSchema.getDBMSType() != DBMSTypes.ORACLE) {
                                ps.setString(psIndex + 1, data);
			        psIndex++;
                            }
                            break;
                    }
                }
            }
            
            //System.err.println(vtFormFieldTemp);
            // request a log if authToken exists
            if (authToken != null) {
                DALLoggerRepository.addLog("INSERT", strSQLInsert.toString(), vtFields, rdFieldData, authToken);
            }
            
            // execute the INSERT
          
            int intInsertResult = ps.executeUpdate();
            
            // if insert fail, cancel the transaction
            if (intInsertResult == 0)
            {
                cancelTransaction();
                blInsertOK = false;
            }
            else {
                // get the inserted record key
                String strSelectInsertedKey = DBMSTypes.getQueryToGetInsertedRecordKey((String) vtDomains.get(0));
                if (strSelectInsertedKey != null) {
                    PreparedStatement psSelect = con.prepareStatement(strSelectInsertedKey);
                    ResultSet rsSelectInsertedKey = psSelect.executeQuery();
                    while (rsSelectInsertedKey.next()) {
                        intInsertedRecordKey = rsSelectInsertedKey.getInt(1);
                    }
                    rsSelectInsertedKey.close();
                    rsSelectInsertedKey = null;
                    psSelect.close();
                    psSelect = null;
                }
                
                // if we have CLOB objects
                if (vtCLOB.size() > 0) {
                    StringBuffer strUpdateClob = new StringBuffer("SELECT ");
                    for (int i=0; i < vtCLOB.size(); i++) {
                        strUpdateClob.append((String) vtCLOB.get(i) + ", ");
                    }
                    strUpdateClob = new StringBuffer(strUpdateClob.substring(0, strUpdateClob.length() - 2));
                    strUpdateClob.append(" FROM " + DatabaseSchema.getDomains().get(vtDomains.get(0)));
                    strUpdateClob.append(" WHERE " + strPrimaryKey + " = " + intInsertedRecordKey + " FOR UPDATE");
                    
                    PreparedStatement psUpdateClob = con.prepareStatement(strUpdateClob.toString());
                    ResultSet rsUpdateClob = psUpdateClob.executeQuery();
                    if (rsUpdateClob.next()) {
                        for (int i=0; i < vtCLOB.size(); i++) {
                            //String strColumnName = (String) vtCLOB.get(i);
                            if (rdFieldData.getParameter((String) vtCLOBFieldname.get(i)) != null && rdFieldData.getParameter((String) vtCLOBFieldname.get(i)).trim().length() > 0) {
                                oracle.sql.CLOB clob = (oracle.sql.CLOB) rsUpdateClob.getClob(i + 1);
                                if (clob != null){
                                Writer wtClob = clob.getCharacterOutputStream();
                                wtClob.write(rdFieldData.getParameter((String) vtCLOBFieldname.get(i)));
                                wtClob.flush();
                                wtClob.close();
                                wtClob = null;
                                }
                            }
                        }
                    }
                    
                    rsUpdateClob.close();
                    rsUpdateClob = null;
                    psUpdateClob.close();
                    psUpdateClob = null;
                }
                
                if (!blManualCommit) {
                    commitTransaction();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            cancelTransaction();
            blInsertOK = false;
            LogService.instance().log(LogService.ERROR, e.toString(), e);
            throw new DAOException("Unknown error while trying to execute the INSERT: " + e.toString(), e);
        }
        
        return blInsertOK;
    }
    
    /** Execute MULTI-INSERT query
     */
    public boolean executeMultiInsert() throws DAOException, DAOUpdateInvalidDomain, DBMSInvalidOperator
    {
        boolean blInsertOK = true;
        
        // must have only one domain to do the INSERT
        if (vtDomains.size() < 1 || vtDomains.size() > 1)
        {
            blInsertOK = false;
            LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidDomain - The number of domains specified is invalid. Only one allowed!");
            throw new DAOUpdateInvalidDomain ("The number of domains specified is invalid. Only one allowed!");
        }
        
        // must have at least one field
        if (vtFields == null || vtFields.size() < 1)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No field supplied");
            throw new DAOQueryInvalidField ("No field supplied");
        }
        
        // must have data
        if (rdMultiData == null)
        {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - No runtime data supplied");
            throw new DAOQueryInvalidField ("No runtime data supplied");
        }
        
        try
        {             
            StringBuffer strSQLInsert = new StringBuffer("INSERT INTO " + DatabaseSchema.getDomains().get(vtDomains.get(0)) + " (");
            
            String strPrimaryKey = null;
            // for Oracle we need to add primary key field
            if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                strPrimaryKey = DatabaseSchema.getPrimaryKey((String) vtDomains.get(0));
                
                if (strPrimaryKey != null) {
                    strSQLInsert.append(DBMSTypes.getField(strPrimaryKey) + ", ");
                }
            }
            
            // Add fields into SQL statement
            for (int index=0; index < vtFields.size(); index++)
            {
                DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                strSQLInsert.append(fieldTemp.getNameForUpdate() + ", ");
            }            
            // cut the last comma
            strSQLInsert = new StringBuffer(strSQLInsert.substring(0, strSQLInsert.length() - 2) + ") VALUES (");

            // for Oracle we need to add primary key field
            if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                if (strPrimaryKey != null) {
                    strSQLInsert.append(DatabaseSchema.getSequence((String) vtDomains.get(0)) + ".NEXTVAL, ");
                }
            }
            
            
            // Add ? for field values
            for (int index=0; index < vtFields.size(); index++)
                strSQLInsert.append("?, ");
            // cut the last ?
            strSQLInsert = new StringBuffer(strSQLInsert.substring(0, strSQLInsert.length() - 2) + ")");
            
            //System.err.println(strSQLInsert);
            // Create a connection if we don't have
            if (con == null)
            {
                con = RDBMServices.getConnection();
                // conCount.addcon(this.hashCode());
            }
            
            // Turn off auto commit so we can do transaction based updates!
            con.setAutoCommit(false);
            
            
            for (int i=0; i < rdMultiData.length; i++)
            {
                // Create the prepared statment
                ps = con.prepareStatement(strSQLInsert.toString());
                
                // Set field's values
                for (int index=0; index < vtFields.size(); index++)
                {
                    DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(vtFields.get(index));
                    int intDataType = fieldTemp.getDataType();
                    int intMaxDataLength = fieldTemp.getLength();
                    String data = rdMultiData[i].getParameter((String) vtFields.get(index));

                    // data is null or not specified
                    if (data == null)
                    {
                        switch (intDataType)
                        {
                            // Data is integer
                            case DBMSTypes.INT_TYPE:
                                ps.setNull(index + 1, Types.INTEGER);
                                break;

                            // Data is floating point
                            case DBMSTypes.FLOAT_TYPE:
                                ps.setNull(index + 1, Types.FLOAT);
                                break;

                            // Data is double
                            case DBMSTypes.DOUBLE_TYPE:
                                ps.setNull(index + 1, Types.DOUBLE);
                                break;

                            // Data is date
                            case DBMSTypes.DATE_TYPE:
                                ps.setNull(index + 1, Types.DATE);
                                break;
                                
                            // Data is time
                            case DBMSTypes.TIME_TYPE:
                                ps.setNull(index + 1, Types.TIME);
                                break;

                            // Data is string
                            case DBMSTypes.STR_TYPE:
                                ps.setNull(index + 1, Types.VARCHAR);
                                break;
                        }
                    }
                    else
                    {
                        switch (intDataType)
                        {
                            // Data is integer
                            case DBMSTypes.INT_TYPE:
                                ps.setInt(index + 1, Integer.parseInt(data));
                                break;

                            // Data is floating point
                            case DBMSTypes.FLOAT_TYPE:
                                ps.setFloat(index + 1, Float.parseFloat(data));
                                break;

                            // Data is double
                            case DBMSTypes.DOUBLE_TYPE:
                                ps.setDouble(index + 1, Double.parseDouble(data));
                                break;

                            // Data is date
                            case DBMSTypes.DATE_TYPE:
                                ps.setDate(index + 1, java.sql.Date.valueOf(Utilities.convertDateForDB(data)));
                                break;

                            // Data is time
                            case DBMSTypes.TIME_TYPE:
                                ps.setTime(index + 1, java.sql.Time.valueOf(Utilities.convertTimeForDB(data)));
                                break;
                            
                            // Data is string
                            case DBMSTypes.STR_TYPE:
                                // if data is longer than the max allowable length, need truncation
                                if (data.length() > intMaxDataLength)
                                    data = data.substring(0, intMaxDataLength);

                                ps.setString(index + 1, data);
                                break;
                        }
                    }
                }

                //System.err.println(ps.toString());
                // execute the INSERT
                int intInsertResult = ps.executeUpdate();

                // if insert fail, cancel the transaction
                if (intInsertResult == 0)
                {
                    cancelTransaction();
                    blInsertOK = false;
                }
                ps.close();
            }
            
            if (!blManualCommit) {
                commitTransaction();
            }
          
        }
        catch (Exception e)
        {
            cancelTransaction();
            blInsertOK = false;
            LogService.instance().log(LogService.ERROR, e.toString());
            throw new DAOException("Unknown error while trying to execute the MULTI INSERT: " + e.toString());
        }
        
        return blInsertOK;
    }
    
    /** Execute DELETE query
     */
    public boolean executeDelete() throws DAOException, DAOUpdateInvalidDomain, DBMSInvalidOperator
    {
        boolean blDeleteOK = true;
        
        // must have only one domain to do the DELETE
        if (vtDomains.size() < 1 || vtDomains.size() > 1)
        {
            blDeleteOK = false;
            LogService.instance().log(LogService.ERROR, "DAOUpdateInvalidDomain - The number of domains specified is invalid. Only one allowed!");
            throw new DAOUpdateInvalidDomain ("The number of domains specified is invalid. Only one allowed!");
        }
        
        try
        {
            StringBuffer strSQLDelete = new StringBuffer("DELETE FROM " + DatabaseSchema.getDomains().get(vtDomains.get(0)));
            
            // add WHERE clauses
            for (int index=0; index < vtWhereTypes.size(); index++)
            {
                // if we have WHERE clauses
                if (index < 1)
                    strSQLDelete.append(" WHERE ");

                int intOpenBrackets;
                int intCloseBrackets;
                String strWhereConnector;
                String strWhereOperator;
                DBField firstWhereField;
                DBField secondWhereField;

                int intWhereType = ((Integer) vtWhereTypes.get(index)).intValue();
                switch (intWhereType)
                {
                    case WHERE_HAS_VALUE:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        strWhereOperator = (String) vtWhereOperators.get(index);
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                        String data = (String) vtWhereObjects.get(index);

                        // add the connector
                        if (strWhereConnector != null)
                            strSQLDelete.append(DBMSTypes.getOperator(strWhereConnector));

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLDelete.append("(");

                        // add fields and operator
                        if (strWhereOperator.equals("LIKE"))
                        {
                            strSQLDelete.append(firstWhereField.getExternalName() + " LIKE '%" + data + "%'"); 
                        }
                        else if (strWhereOperator.equals("LEFT LIKE"))
                        {
                            strSQLDelete.append(firstWhereField.getExternalName() + " LIKE '" + data + "%'"); 
                        }
                        else if (strWhereOperator.equals("RIGHT LIKE"))
                        {
                            strSQLDelete.append(firstWhereField.getExternalName() + " LIKE '%" + data + "'"); 
                        }
                        else
                        {
                            strSQLDelete.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));

                            int intMaxDataLength = firstWhereField.getLength();
                            int intDataType = firstWhereField.getDataType();

                            switch (intDataType)
                            {
                                // Data is integer
                                case DBMSTypes.INT_TYPE:
                                    strSQLDelete.append(DBMSTypes.convertToNumeric(data));
                                    break;

                                // Data is floating point
                                case DBMSTypes.FLOAT_TYPE:
                                    strSQLDelete.append(DBMSTypes.convertToNumeric(data));
                                    break;

                                // Data is double
                                case DBMSTypes.DOUBLE_TYPE:
                                    strSQLDelete.append(DBMSTypes.convertToNumeric(data));
                                    break;

                                // Data is date
                                case DBMSTypes.DATE_TYPE:
                                    strSQLDelete.append(DBMSTypes.convertToString(Utilities.convertDateForQuery(data)));
                                    break;
                                    
                                // Data is time
                                case DBMSTypes.TIME_TYPE:
                                    strSQLDelete.append(DBMSTypes.convertToTime(Utilities.convertTimeForQuery(data)));
                                    break;

                                // Data is string
                                case DBMSTypes.STR_TYPE:
                                    // if data is longer than the max allowable length, need truncation
                                    if (data.length() > intMaxDataLength)
                                        data = data.substring(0, intMaxDataLength);

                                    strSQLDelete.append(DBMSTypes.convertToString(data));
                                    break;
                            }
                        }

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLDelete.append(")");
                        break;

                    case WHERE_BOTH_FIELDS:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        strWhereOperator = (String) vtWhereOperators.get(index);
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                        secondWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereObjects.get(index));

                        // add the connector
                        if (strWhereConnector != null)
                            strSQLDelete.append(DBMSTypes.getOperator(strWhereConnector));

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLDelete.append("(");

                        // add fields and operator
                        strSQLDelete.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator) +secondWhereField.getExternalName());

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLDelete.append(")");

                        break;

                    case WHERE_HAS_NULL_VALUE:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        String strWhereValue = (String) vtWhereObjects.get(index);
                        firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));

                        // add the connector
                        if (strWhereConnector != null)
                            strSQLDelete.append(DBMSTypes.getOperator(strWhereConnector));

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLDelete.append("(");

                        // add field and operator
                        strSQLDelete.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereValue));

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLDelete.append(")");
                        break;

                    case WHERE_HAS_SUB_QUERY:
                        intOpenBrackets = ((Integer) vtWhereOpenBrackets.get(index)).intValue();
                        intCloseBrackets = ((Integer) vtWhereCloseBrackets.get(index)).intValue();
                        strWhereConnector = (String) vtWhereConnectors.get(index);
                        strWhereOperator = (String) vtWhereOperators.get(index);
                        DALQuery sub_query = (DALQuery) vtWhereObjects.get(index);

                        // add the connector
                        if (strWhereConnector != null)
                            strSQLDelete.append(DBMSTypes.getOperator(strWhereConnector));

                        // add open brackets
                        for (int i=0; i < intOpenBrackets; i++)
                            strSQLDelete.append("(");


                        // add field and operator
                        if (vtWhereFields.get(index) == null)
                            strSQLDelete.append(DBMSTypes.getOperator(strWhereOperator));
                        else
                        {
                            firstWhereField = (DBField) DatabaseSchema.getFields().get(vtWhereFields.get(index));
                            strSQLDelete.append(firstWhereField.getExternalName() + DBMSTypes.getOperator(strWhereOperator));
                        }

                        // add sub-query recursively
                        strSQLDelete.append("(" + sub_query.convertSelectQueryToString() + ")");

                        // add close brackets
                        for (int i=0; i < intCloseBrackets; i++)
                            strSQLDelete.append(")");
                        break;
                }
            }
            
            // Create a connection if we don't have
            if (con == null)
            {
                con = RDBMServices.getConnection();
                // conCount.addcon(this.hashCode());
            }
            
            // Turn off auto commit so we can do transaction based updates!
            con.setAutoCommit(false);
            
            // Create the preparedstatment
            ps = con.prepareStatement(strSQLDelete.toString());
            
            
            //System.err.println(ps);
            // execute the Delete
            int intDeleteResult = ps.executeUpdate();
            
            // if insert fail, cancel the transaction
            if (intDeleteResult == 0)
            {
                cancelTransaction();
                blDeleteOK = false;
            }
            else
                commitTransaction();
        }
        catch (Exception e)
        {
            cancelTransaction();
            blDeleteOK = false;
            LogService.instance().log(LogService.ERROR, e.toString());
            throw new DAOException("Unknown error while trying to execute the DELETE: " + e.toString());
        }
        return blDeleteOK;
    }
    
    /** Commit database transaction
     */
    public void commitTransaction() throws DAOException
    {
        try
        {
            if (ps != null) {
                ps.close();
                ps = null;
            }
            
            if(con != null)
            {
                con.commit();
                RDBMServices.releaseConnection(con);
                // conCount.decreasecon(this.hashCode());
                con = null;
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, e.toString());
            throw new DAOException("Unknown problem occured while trying to commit the database transactions.");
        }
    }
    
    /** Cancel database transaction
     */
    public void cancelTransaction() throws DAOException
    {
        try
        {
            if (ps != null) {
                ps.close();
                ps = null;
            }
            
            if(con != null)
            {
                con.rollback();
                RDBMServices.releaseConnection(con);
                con = null;
            }
        }
        catch (Exception e) 
        {       
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, e.toString());
            throw new DAOException("Unknown problem occured while trying to cancel the database transactions.");
        }
    }
    
    /** Release the connection
     */
    public void releaseConnection() throws DAOException
    {
        try
        {
            if (ps != null) {
                ps.close();
                ps = null;
            }
            
            if(con != null)
            {
             //   System.out.println ("releasing connection");
                RDBMServices.releaseConnection(con);
                con = null;
                // conCount.decreasecon(this.hashCode());
            }
        }
        catch (Exception e) 
        {
            if (DISPLAY_DAL_DEBUG)
            {
                LogService.instance().log(LogService.ERROR, e.toString());
                throw new DAOException("Unknown problem occured while trying to cancel the database transactions.");
            }
        }
    }
    
    /** Reset the query object
     */
    public void reset()
    {
        clearDomains();
        clearMaxFields();
        clearMinFields();
        clearAverageFields();
        clearSumFields();
        clearCountField();
        clearHavingCondition();
        clearOrderBy();
        clearGroupBy();
        clearSiblingQueries();
        clearWhere();
        clearFields();
        clearMultiInsert();
        clearLimitOffset();
        clearDistinctField();
        clearCaseSensitive();
        clearSubQueries();
    }

	/**
	 * transfer all data from hashtable to runtimedata
	 */
	private void transferHashtableToRuntimeData()
	{
		Enumeration enumFieldData = hashFieldData.keys();
		rdFieldData = new ChannelRuntimeData();
		while(enumFieldData.hasMoreElements())
		{
			String strFieldDataKey = (String) enumFieldData.nextElement();
                        //System.err.println(strFieldDataKey);
			String strFieldDataValue = (String) hashFieldData.get(strFieldDataKey);
                        DBField dbField = (DBField) DatabaseSchema.getFields().get(strFieldDataKey);
                        
                        if (dbField != null && dbField.getDataType() == DBMSTypes.TIME_TYPE) {

                            // check if value is already in the format of HH:MM AM
                            if (strFieldDataValue != null && strFieldDataValue.matches("^[01]?[0-9]:[0-9][0-9] [aApP][mM]$"))
                            {
                                rdFieldData.setParameter(strFieldDataKey, strFieldDataValue);
                            }
                            // if not, convert it
                            else
                            {
                                rdFieldData.setParameter(strFieldDataKey, Utilities.convertTimeForDisplay(strFieldDataValue));                                                                
                            } 
                           
                        }
                        else {
                            rdFieldData.setParameter(strFieldDataKey, strFieldDataValue);
                        }
		}
	}
        
	public void setDistinctQuery() {
		this.blDistinct = true;
		
	}
	
	public void clearDistinctQuery() {
		this.blDistinct = false;
	}
	
    /**
     * Set distinct field
     */
    public void setDistinctField(String strDistinctField) throws DAOQueryInvalidField
    {
        if (!DatabaseSchema.getFields().containsKey(strDistinctField)) {
            LogService.instance().log(LogService.ERROR, "DAOQueryInvalidField - The field supplied is invalid");
            throw new DAOQueryInvalidField("The field supplied is invalid. Field = " + strDistinctField);
        }
        
        this.strDistinctField = strDistinctField;
    }
    
    public void clearDistinctField()
    {
        strDistinctField = null;
    }
    
    /**
     * Get inserted record key
     * @return inserted record key
     */
    public int getInsertedRecordKey() {
        return intInsertedRecordKey;
    }
    
    /** Set the record key for update query in case it has some CLOB fields
     *  @param record key
     */
    public void setUpdateRecordKey(String strUpdateKey) {
        strUpdateRecordKey = strUpdateKey;
    }
    
    /** Set the manual commit mode
     *  @param blManualCommit
     */
    public void setManualCommit(boolean blManualCommit) {
        this.blManualCommit = blManualCommit;
    }
    
    /** Get the manual commit mode
     *  @return the manual commit mode
     */
    public boolean isManualCommit() {
        return blManualCommit;
    }

    
    /** Close connection
     */
    public void closeConnection() throws java.sql.SQLException {
        if (!blManualCommit) {
            try {
                releaseConnection();
            }
            catch (Exception e) {
                throw new java.sql.SQLException("Unknown error while trying to release the connection: " + e.toString());
            }
        }
    }
}
