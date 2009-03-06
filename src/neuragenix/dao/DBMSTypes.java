/**
 * DBMSTypes.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 27/08/2003
 */


package neuragenix.dao;

/**
 * Class to define SQL syntax for popular DBMSs
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

import java.util.Hashtable;

import neuragenix.dao.exception.DBMSInvalidOperator;

public class DBMSTypes 
{
    /** Indicate Postgres DBMS
     */ 
    public static final int POSTGRES = 0;
    
    /** Indicate Oracle DBMS
     */ 
    public static final int ORACLE = 1;
    
    /** Indicate MS SQLSERVER DBMS
     */ 
    public static final int SQLSERVER = 2;
    
    /** DBMS type of the current Schema
     */
    private static final int DBMS_TYPE = DatabaseSchema.getDBMSType();
    
    /** Data type integer
     */
    public static final int INT_TYPE = 1;
    
    /** Data type floating point
     */
    public static final int FLOAT_TYPE = 2;
    
    /** Data type double
     */
    public static final int DOUBLE_TYPE = 3;
    
    /** Data type String
     */
    public static final int STR_TYPE = 4;
    
    /** Data type date
     */
    public static final int DATE_TYPE = 5;
    
    /** Data type time
     */
    public static final int TIME_TYPE = 6;
    
    /** Data type duration 
     */
    public static final int DURATION_TYPE = 7;

    
    /** Data type time
     */
    public static final int CLOB_TYPE = 8;
    
    
    /** Keep DBMS's operators
     */
    private static Hashtable hashOperators;
    
    /** Indicate if the data is loaded
     */
    private static boolean blLoaded = false;
    
    /** Creates a new instance of DBMSAbstract
     */
    public DBMSTypes()
    {
    }
    
    /** Return the actual operator presentation based on the operator name
     */
    public static String getOperator(String strAOperator) throws DBMSInvalidOperator
    {
        if (!blLoaded)
            loadDBMSs();
        
        String[] strArrOperators = (String[]) hashOperators.get(strAOperator);
        return strArrOperators[DBMS_TYPE];
    }
    
    /** Return the actual field presentation based on the field name
     */
    public static String getDomain(String strADomain)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return strADomain;
            case ORACLE  : return strADomain;
            case SQLSERVER: return strADomain;
        }
        
        return strADomain;
    }
    
    /** Return the actual field presentation based on the field name
     */
    public static String getField(String strAField)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return "\"" + strAField + "\"";
            case ORACLE  : return "\"" + strAField + "\"";
            case SQLSERVER: return "\"" + strAField + "\"";
        }
        
        return strAField;
    }
    
     /** Return the alias name to set the Order by
     */
    public static String getAlias(String strAField)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return "\"" + strAField + "\"";
            case ORACLE  : return "\"" + strAField + "\"";
            case SQLSERVER  : return strAField;
        }
        
        return strAField;
    }
    
    /** Convert a string to a correct presentation of a numeric value for SQL
     */
    public static String convertToNumeric(String strAValue)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return strAValue;
            case ORACLE  : return strAValue;
            case SQLSERVER  : return strAValue;
        }
        
        return strAValue;
    }
    
    /** Convert a string to a correct presentation of a string value for SQL
     */
    public static String convertToString(String strAValue)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return "'" + cleanDataForQuery(strAValue) + "'";
            case ORACLE  : return "'" + cleanDataForQuery(strAValue) + "'";
            case SQLSERVER  : return "'" + cleanDataForQuery(strAValue) + "'";
        }
        
        return strAValue;
    }
    
    /** Convert a string to a correct presentation of a time value for SQL
     */
    public static String convertToTime(String strAValue)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return "'" + cleanDataForQuery(strAValue) + "'";
            case ORACLE  : return cleanDataForQuery(strAValue);
            case SQLSERVER  : return "'" + cleanDataForQuery(strAValue) + "'";
        }
        
        return strAValue;
    }
    
    /** Replace special characters like "'" to a proper format for SQL statement
     *  @param: string to clean
     *  @return: cleaned string
     */
    public static String cleanDataForQuery(String strAValue)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return strAValue.replaceAll("'", "''");
            case ORACLE  : return strAValue.replaceAll("'", "''");
            case SQLSERVER  : return strAValue.replaceAll("'", "''");
        }
        
        return strAValue;
    }
    
    /** Return the LIMIT, OFFSET presentationquery.setWhere("AND", 0, "SURVEY QUESTIONS_intQuestionType", "<", "100", 0, DALQuery.WHERE_HAS_VALUE);
     */
    public static String getLimitOffset(int intLimit, int intOffset)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return " LIMIT " + intLimit + " OFFSET " + intOffset + " ";
            case ORACLE  : return "";
            case SQLSERVER  : return "";
        }
        
        return " LIMIT " + intLimit + " OFFSET " + intOffset + " ";
    }
    
    /** Attach lowercase function to a field
     */
    public static String lower(String strAFieldName)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: return "lower(" + strAFieldName + ")";
            case ORACLE  : return "lower(" + strAFieldName + ")";
            case SQLSERVER  : return "lower(" + strAFieldName + ")";
        }
        
        return strAFieldName;
    }
    
    /** Get the query to get inserted record key
     *  @param strDomainName: internal domain name
     *  @return strQuery: the query
     */
    public static String getQueryToGetInsertedRecordKey(String strDomainName)
    {
        switch (DBMS_TYPE)
        {
            case POSTGRES: 
                if (DatabaseSchema.getSequence(strDomainName) != null) {
                    return "SELECT currval('" + DatabaseSchema.getSequence(strDomainName) + "')";
                }
                else {
                    return null;
                }
                
            case ORACLE:
                if (DatabaseSchema.getSequence(strDomainName) != null) {
                    return "SELECT " + DatabaseSchema.getSequence(strDomainName) + ".CURRVAL from dual";
                }
                else {
                    return null;
                }
                
            case SQLSERVER:
                if (DatabaseSchema.getSequence(strDomainName) != null) {
                    return "SELECT @@IDENTITY"; 
                }
                else {
                    return null;
                }
        }
        
        return strDomainName;
    }
    
    /** Load DBMS's syntax
     */
    private static void loadDBMSs()
    {
        hashOperators = new Hashtable(20);
        
        // load AND operator
        String[] strArrAND = {" AND ", " AND ", " AND "};
        hashOperators.put("AND", strArrAND);
        
        // load OR operator
        String[] strArrOR = {" OR ", " OR ", " OR "};
        hashOperators.put("OR", strArrOR);
        
        // load NOT operator
        String[] strArrNOT = {" NOT ", " NOT ", " NOT "};
        hashOperators.put("NOT", strArrNOT);
        
        // load AND NOT operator
        String[] strArrANDNOT = {" AND NOT ", " AND NOT ", " AND NOT "};
        hashOperators.put("AND NOT", strArrANDNOT);

        // load IN operator
        String[] strArrIN = {" IN ", " IN ", " IN "};
        hashOperators.put("IN", strArrIN);
        
        // load NOT IN operator
        String[] strArrNOTIN = {" NOT IN ", " NOT IN ", " NOT IN "};
        hashOperators.put("NOT IN", strArrNOTIN);
        
        // load UNION operator
        String[] strArrUNION = {" UNION ", " UNION ", " UNION "};
        hashOperators.put("UNION", strArrUNION);
        
        // load INTERSECT operator
        String[] strArrINTERSECT = {" INTERSECT ", " INTERSECT ", " EXISTS "};
        hashOperators.put("INTERSECT", strArrINTERSECT);
        
        // load EXCEPT operator
        String[] strArrEXCEPT = {" EXCEPT ", " EXCEPT ", " EXCEPT "};
        hashOperators.put("EXCEPT", strArrEXCEPT);
        
        // load CONTAINS operator
        String[] strArrCONTAINS = {" CONTAINS ", " CONTAINS ", " CONTAINS "};
        hashOperators.put("CONTAINS", strArrCONTAINS);
        
        // load LESS THAN operator
        String[] strArrLESSTHAN = {" < ", " < ", " < "};
        hashOperators.put("<", strArrLESSTHAN);
        
        // load LESS THAN OR EQUAL operator
        String[] strArrLESSTHAN_OR_EQUAL = {" <= ", " <= ", " <= "};
        hashOperators.put("<=", strArrLESSTHAN_OR_EQUAL);
        
        // load GREATER THAN operator
        String[] strArrGREATERTHAN = {" > ", " > ", " > "};
        hashOperators.put(">", strArrGREATERTHAN);
        
        // load GREATER THAN OR EQUAL operator
        String[] strArrGREATERTHAN_OR_EQUAL = {" >= ", " >= ", " >= "};
        hashOperators.put(">=", strArrGREATERTHAN_OR_EQUAL);
        
        // load EQUAL operator
        String[] strArrEQUAL = {" = ", " = ", " = "};
        hashOperators.put("=", strArrEQUAL);
        
        // load NOT EQUAL operator
        String[] strArrNOT_EQUAL = {" <> ", " <> ", " <> "};
        hashOperators.put("<>", strArrNOT_EQUAL);
        
        // load IS NULL operator
        String[] strArrISNULL = {" IS NULL ", " IS NULL ", " IS NULL "};
        hashOperators.put("IS NULL", strArrISNULL);
        
        // load IS NOT NULL operator
        String[] strArrISNOTNULL = {" IS NOT NULL ", " IS NOT NULL ", " IS NOT NULL "};
        hashOperators.put("IS NOT NULL", strArrISNOTNULL);
        
        // load EXISTS operator
        String[] strArrEXISTS = {" EXISTS ", " EXISTS ", " EXISTS "};
        hashOperators.put("EXISTS", strArrEXISTS);
        
        // load NOT EXISTS operator
        String[] strArrNOT_EXISTS = {" NOT EXISTS ", " NOT EXISTS ", " NOT EXISTS "};
        hashOperators.put("NOT EXISTS", strArrNOT_EXISTS);
        
        // load LEFT JOIN operator
        String[] strArrLEFT_JOIN = {" LEFT JOIN ", " LEFT JOIN ", " LEFT JOIN "};
        hashOperators.put("LEFT JOIN", strArrLEFT_JOIN);
        
        // load INNER JOIN operator
        String[] strArrINNER_JOIN = {" INNER JOIN ", " INNER JOIN ", " INNER JOIN "};
        hashOperators.put("INNER JOIN", strArrINNER_JOIN);
        
        blLoaded = true;
    }
}