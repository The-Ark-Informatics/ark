/*
 * NGXResultSet.java 
 * 
 * Version 1.0 25/01/2005
 *
 * Copyright (c) 2005 Neuragenix Ltd, Pty.
 * 356 Collins St, Melbourne, VIC 3000, AUS
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Neuragenix ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Neuragenix.
 */

package neuragenix.dao;

/**
 * Neuragenix wrapper class for ResultSet
 *
 * @version 1.0 25/01/2005
 * @author Huy Hoang
 */

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.Map;

import neuragenix.dao.*;

public class NGXResultSet implements ResultSet {
    private ResultSet rsResultSet;
    private int intOffset = -1;
    private int intLimit = -1;
    private int intMaxRow = 0;
    private DALQuery query = null;
    
    /** Creates a new instance of NGXResulSet */
    public NGXResultSet(ResultSet rsResultSet, DALQuery query) {
        this.rsResultSet = rsResultSet;
        this.query = query;
    }
    
    public boolean absolute(int param) throws java.sql.SQLException {
        return rsResultSet.absolute(param);
    }
    
    public void afterLast() throws java.sql.SQLException {
        rsResultSet.afterLast();
    }
    
    public void beforeFirst() throws java.sql.SQLException {
        if (intOffset > 0) {
            rsResultSet.absolute(intOffset);
        }
        else {
            rsResultSet.beforeFirst();
        }
    }
    
    public void cancelRowUpdates() throws java.sql.SQLException {
        rsResultSet.cancelRowUpdates();
    }
    
    public void clearWarnings() throws java.sql.SQLException {
        rsResultSet.clearWarnings();
    }
    
    public void close() throws java.sql.SQLException {
        rsResultSet.close();
        query.closeConnection();
    }
    
    public void deleteRow() throws java.sql.SQLException {
        rsResultSet.deleteRow();
    }
    
    public int findColumn(String str) throws java.sql.SQLException {
        return rsResultSet.findColumn(str);
    }
    
    public boolean first() throws java.sql.SQLException {
        if (intOffset > -1) {
            return rsResultSet.absolute(intOffset + 1);
        }
        else {
            rsResultSet.beforeFirst();
            return rsResultSet.next();
        }
    }
    
    public java.sql.Array getArray(int param) throws java.sql.SQLException {
        return rsResultSet.getArray(param);
    }
    
    public java.sql.Array getArray(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getArray(strShortName);
        }
        return rsResultSet.getArray(str);
    }
    
    public java.io.InputStream getAsciiStream(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getAsciiStream(strShortName);
        }
        return rsResultSet.getAsciiStream(str);
    }
    
    public java.io.InputStream getAsciiStream(int param) throws java.sql.SQLException {
        return rsResultSet.getAsciiStream(param);
    }
    
    public java.math.BigDecimal getBigDecimal(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getBigDecimal(strShortName);
        }
        return rsResultSet.getBigDecimal(str);
    }
    
    public java.math.BigDecimal getBigDecimal(int param) throws java.sql.SQLException {
        return rsResultSet.getBigDecimal(param);
    }
    
    public java.math.BigDecimal getBigDecimal(int param, int param1) throws java.sql.SQLException {
        return rsResultSet.getBigDecimal(param, param1);
    }
    
    public java.math.BigDecimal getBigDecimal(String str, int param) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getBigDecimal(strShortName, param);
        }
        return rsResultSet.getBigDecimal(str, param);
    }
    
    public java.io.InputStream getBinaryStream(int param) throws java.sql.SQLException {
        return rsResultSet.getBinaryStream(param);
    }
    
    public java.io.InputStream getBinaryStream(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getBinaryStream(strShortName);
        }
        return rsResultSet.getBinaryStream(str);
    }
    
    public java.sql.Blob getBlob(int param) throws java.sql.SQLException {
        return rsResultSet.getBlob(param);
    }
    
    public java.sql.Blob getBlob(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getBlob(strShortName);
        }
        return rsResultSet.getBlob(str);
    }
    
    public boolean getBoolean(int param) throws java.sql.SQLException {
        return rsResultSet.getBoolean(param);
    }
    
    public boolean getBoolean(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getBoolean(strShortName);
        }
        return rsResultSet.getBoolean(str);
    }
    
    public byte getByte(int param) throws java.sql.SQLException {
        return rsResultSet.getByte(param);
    }
    
    public byte getByte(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getByte(strShortName);
        }
        return rsResultSet.getByte(str);
    }
    
    public byte[] getBytes(int param) throws java.sql.SQLException {
        return rsResultSet.getBytes(param);
    }
    
    public byte[] getBytes(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getBytes(strShortName);
        }
        return rsResultSet.getBytes(str);
    }
    
    public java.io.Reader getCharacterStream(int param) throws java.sql.SQLException {
        return rsResultSet.getCharacterStream(param);
    }
    
    public java.io.Reader getCharacterStream(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getCharacterStream(strShortName);
        }
        return rsResultSet.getCharacterStream(str);
    }
    
    public java.sql.Clob getClob(int param) throws java.sql.SQLException {
        return rsResultSet.getClob(param);
    }
    
    public java.sql.Clob getClob(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getClob(strShortName);
        }
        
        return rsResultSet.getClob(str);
    }
    
    public int getConcurrency() throws java.sql.SQLException {
        return rsResultSet.getConcurrency();
    }
    
    public String getCursorName() throws java.sql.SQLException {
        return rsResultSet.getCursorName();
    }
    
    public java.sql.Date getDate(int param) throws java.sql.SQLException {
        return rsResultSet.getDate(param);
    }
    
    public java.sql.Date getDate(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getDate(strShortName);
        }
        
        return rsResultSet.getDate(str);
    }
    
    public java.sql.Date getDate(int param, java.util.Calendar calendar) throws java.sql.SQLException {
        return rsResultSet.getDate(param, calendar);
    }
    
    public java.sql.Date getDate(String str, java.util.Calendar calendar) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getDate(strShortName, calendar);
        }
        return rsResultSet.getDate(str, calendar);
    }
    
    public double getDouble(int param) throws java.sql.SQLException {
        return rsResultSet.getDouble(param);
    }
    
    public double getDouble(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getDouble(strShortName);
        }
        return rsResultSet.getDouble(str);
    }
    
    public int getFetchDirection() throws java.sql.SQLException {
        return rsResultSet.getFetchDirection();
    }
    
    public int getFetchSize() throws java.sql.SQLException {
        return rsResultSet.getFetchSize();
    }
    
    public float getFloat(int param) throws java.sql.SQLException {
        return rsResultSet.getFloat(param);
    }
    
    public float getFloat(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getFloat(strShortName);
        }
        return rsResultSet.getFloat(str);
    }
    
    public int getInt(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getInt(strShortName);
        }
        return rsResultSet.getInt(str);
    }
    
    public int getInt(int param) throws java.sql.SQLException {
        return rsResultSet.getInt(param);
    }
    
    public long getLong(int param) throws java.sql.SQLException {
        return rsResultSet.getLong(param);
    }
    
    public long getLong(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getLong(strShortName);
        }
        return rsResultSet.getLong(str);
    }
    
    public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
        return rsResultSet.getMetaData();
    }
    
    public Object getObject(int param) throws java.sql.SQLException {
        return rsResultSet.getObject(param);
    }
    
    public Object getObject(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getObject(strShortName);
        }
        return rsResultSet.getObject(str);
    }
    
    public Object getObject(int param, java.util.Map map) throws java.sql.SQLException {
        return rsResultSet.getObject(param, map);
    }
    
    public Object getObject(String str, java.util.Map map) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getObject(strShortName, map);
        }
        return rsResultSet.getObject(str, map);
    }
    
    public java.sql.Ref getRef(int param) throws java.sql.SQLException {
        return rsResultSet.getRef(param);
    }
    
    public java.sql.Ref getRef(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getRef(strShortName);
        }
        return rsResultSet.getRef(str);
    }
    
    public int getRow() throws java.sql.SQLException {
        return rsResultSet.getRow();
    }
    
    public short getShort(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getShort(strShortName);
        }
        return rsResultSet.getShort(str);
    }
    
    public short getShort(int param) throws java.sql.SQLException {
        return rsResultSet.getShort(param);
    }
    
    public java.sql.Statement getStatement() throws java.sql.SQLException {
        return rsResultSet.getStatement();
    }
    
    public String getString(String str) throws java.sql.SQLException {
        /*DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getString(strShortName);
        }
        
        return rsResultSet.getString(str);*/
        
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            
            if (fieldTemp.getDataType() == DBMSTypes.CLOB_TYPE) {
                if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
                    oracle.sql.CLOB clObject = (oracle.sql.CLOB) rsResultSet.getClob(strShortName);
                    String strClobContent = null;
                    if (clObject != null) {
                        strClobContent = clObject.getSubString(1, DatabaseSchema.getMaxClobSize());
                    }
                    return strClobContent;
                }
                else {
                    throw new java.sql.SQLException("Clob has not been implemented yet!");
                }
            }
            
            return rsResultSet.getString(strShortName);
        }
        
        return rsResultSet.getString(str);
    }
    
    public String getString(int param) throws java.sql.SQLException {
        return rsResultSet.getString(param);
    }
    
    public java.sql.Time getTime(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getTime(strShortName);
        }
        return rsResultSet.getTime(str);
    }
    
    public java.sql.Time getTime(int param) throws java.sql.SQLException {
        return rsResultSet.getTime(param);
    }
    
    public java.sql.Time getTime(String str, java.util.Calendar calendar) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getTime(strShortName, calendar);
        }
        return rsResultSet.getTime(str, calendar);
    }
    
    public java.sql.Time getTime(int param, java.util.Calendar calendar) throws java.sql.SQLException {
        return rsResultSet.getTime(param, calendar);
    }
    
    public java.sql.Timestamp getTimestamp(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getTimestamp(strShortName);
        }
        return rsResultSet.getTimestamp(str);
    }
    
    public java.sql.Timestamp getTimestamp(int param) throws java.sql.SQLException {
        return rsResultSet.getTimestamp(param);
    }
    
    public java.sql.Timestamp getTimestamp(String str, java.util.Calendar calendar) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getTimestamp(strShortName, calendar);
        }
        return rsResultSet.getTimestamp(str, calendar);
    }
    
    public java.sql.Timestamp getTimestamp(int param, java.util.Calendar calendar) throws java.sql.SQLException {
        return rsResultSet.getTimestamp(param, calendar);
    }
    
    public int getType() throws java.sql.SQLException {
        return rsResultSet.getType();
    }
    
    public java.net.URL getURL(int param) throws java.sql.SQLException {
        return rsResultSet.getURL(param);
    }
    
    public java.net.URL getURL(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getURL(strShortName);
        }
        return rsResultSet.getURL(str);
    }
    
    public java.io.InputStream getUnicodeStream(String str) throws java.sql.SQLException {
        DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(str);
        
        if (fieldTemp != null) {
            String strShortName = fieldTemp.getShortInternalName();
            return rsResultSet.getUnicodeStream(strShortName);
        }
        return rsResultSet.getUnicodeStream(str);
    }
    
    public java.io.InputStream getUnicodeStream(int param) throws java.sql.SQLException {
        return rsResultSet.getUnicodeStream(param);
    }
    
    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        return rsResultSet.getWarnings();
    }
    
    public void insertRow() throws java.sql.SQLException {
        rsResultSet.insertRow();
    }
    
    public boolean isAfterLast() throws java.sql.SQLException {
        return rsResultSet.isAfterLast();
    }
    
    public boolean isBeforeFirst() throws java.sql.SQLException {
        if (intOffset > 0) {
            if (intOffset == rsResultSet.getRow()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return rsResultSet.isBeforeFirst(); 
        }
    }
    
    public boolean isFirst() throws java.sql.SQLException {
        if (intOffset > -1) {
            if ((intOffset + 1) == rsResultSet.getRow()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return rsResultSet.isFirst();
        }
    }
    
    public boolean isLast() throws java.sql.SQLException {
        if (intOffset > -1) {
            if (rsResultSet.getRow() == intMaxRow) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return rsResultSet.isLast();
        }
    }
    
    public boolean last() throws java.sql.SQLException {
        rsResultSet.last();
        if (intOffset > -1) {
            if (rsResultSet.getRow() > intMaxRow) {
                return rsResultSet.absolute(intMaxRow);
            }
        }
        
        return rsResultSet.last();
    }
    
    public void moveToCurrentRow() throws java.sql.SQLException {
        rsResultSet.moveToCurrentRow();
    }
    
    public void moveToInsertRow() throws java.sql.SQLException {
        rsResultSet.moveToInsertRow();
    }
    
    public boolean next() throws java.sql.SQLException {
        if (intOffset > -1) {
            if (rsResultSet.getRow() < intMaxRow) {
                return rsResultSet.next();
            }
            else {
                return false;
            }
        }
        
        return rsResultSet.next();
    }
    
    public boolean previous() throws java.sql.SQLException {
        return rsResultSet.previous();
    }
    
    public void refreshRow() throws java.sql.SQLException {
        rsResultSet.refreshRow();
    }
    
    public boolean relative(int param) throws java.sql.SQLException {
        return rsResultSet.relative(param);
    }
    
    public boolean rowDeleted() throws java.sql.SQLException {
        return rsResultSet.rowDeleted();
    }
    
    public boolean rowInserted() throws java.sql.SQLException {
        return rsResultSet.rowInserted();
    }
    
    public boolean rowUpdated() throws java.sql.SQLException {
        return rsResultSet.rowUpdated();
    }
    
    public void setFetchDirection(int param) throws java.sql.SQLException {
        rsResultSet.setFetchDirection(param);
    }
    
    public void setFetchSize(int param) throws java.sql.SQLException {
        rsResultSet.setFetchSize(param);
    }
    
    public void updateArray(String str, java.sql.Array array) throws java.sql.SQLException {
        rsResultSet.updateArray(str, array);
    }
    
    public void updateArray(int param, java.sql.Array array) throws java.sql.SQLException {
        rsResultSet.updateArray(param, array);
    }
    
    public void updateAsciiStream(String str, java.io.InputStream inputStream, int param) throws java.sql.SQLException {
        rsResultSet.updateAsciiStream(str, inputStream, param);
    }
    
    public void updateAsciiStream(int param, java.io.InputStream inputStream, int param2) throws java.sql.SQLException {
        rsResultSet.updateAsciiStream(param, inputStream, param2);
    }
    
    public void updateBigDecimal(String str, java.math.BigDecimal bigDecimal) throws java.sql.SQLException {
        rsResultSet.updateBigDecimal(str, bigDecimal);
    }
    
    public void updateBigDecimal(int param, java.math.BigDecimal bigDecimal) throws java.sql.SQLException {
        rsResultSet.updateBigDecimal(param, bigDecimal);
    }
    
    public void updateBinaryStream(int param, java.io.InputStream inputStream, int param2) throws java.sql.SQLException {
        rsResultSet.updateBinaryStream(param, inputStream, param2);
    }
    
    public void updateBinaryStream(String str, java.io.InputStream inputStream, int param) throws java.sql.SQLException {
        rsResultSet.updateBinaryStream(str, inputStream, param);
    }
    
    public void updateBlob(int param, java.sql.Blob blob) throws java.sql.SQLException {
        rsResultSet.updateBlob(param, blob);
    }
    
    public void updateBlob(String str, java.sql.Blob blob) throws java.sql.SQLException {
        rsResultSet.updateBlob(str, blob);
    }
    
    public void updateBoolean(int param, boolean param1) throws java.sql.SQLException {
        rsResultSet.updateBoolean(param, param1);
    }
    
    public void updateBoolean(String str, boolean param) throws java.sql.SQLException {
        rsResultSet.updateBoolean(str, param);
    }
    
    public void updateByte(int param, byte param1) throws java.sql.SQLException {
        rsResultSet.updateByte(param, param1);
    }
    
    public void updateByte(String str, byte param) throws java.sql.SQLException {
        rsResultSet.updateByte(str, param);
    }
    
    public void updateBytes(int param, byte[] values) throws java.sql.SQLException {
        rsResultSet.updateBytes(param, values);
    }
    
    public void updateBytes(String str, byte[] values) throws java.sql.SQLException {
        rsResultSet.updateBytes(str, values);
    }
    
    public void updateCharacterStream(int param, java.io.Reader reader, int param2) throws java.sql.SQLException {
        rsResultSet.updateCharacterStream(param, reader, param2);
    }
    
    public void updateCharacterStream(String str, java.io.Reader reader, int param) throws java.sql.SQLException {
        rsResultSet.updateCharacterStream(str, reader, param);
    }
    
    public void updateClob(String str, java.sql.Clob clob) throws java.sql.SQLException {
        rsResultSet.updateClob(str, clob);
    }
    
    public void updateClob(int param, java.sql.Clob clob) throws java.sql.SQLException {
        rsResultSet.updateClob(param, clob);
    }
    
    public void updateDate(int param, java.sql.Date date) throws java.sql.SQLException {
        rsResultSet.updateDate(param, date);
    }
    
    public void updateDate(String str, java.sql.Date date) throws java.sql.SQLException {
        rsResultSet.updateDate(str, date);
    }
    
    public void updateDouble(int param, double param1) throws java.sql.SQLException {
        rsResultSet.updateDouble(param, param1);
    }
    
    public void updateDouble(String str, double param) throws java.sql.SQLException {
        rsResultSet.updateDouble(str, param);
    }
    
    public void updateFloat(String str, float param) throws java.sql.SQLException {
        rsResultSet.updateFloat(str, param);
    }
    
    public void updateFloat(int param, float param1) throws java.sql.SQLException {
        rsResultSet.updateFloat(param, param1);
    }
    
    public void updateInt(String str, int param) throws java.sql.SQLException {
        rsResultSet.updateInt(str, param);
    }
    
    public void updateInt(int param, int param1) throws java.sql.SQLException {
        rsResultSet.updateInt(param, param1);
    }
    
    public void updateLong(int param, long param1) throws java.sql.SQLException {
        rsResultSet.updateLong(param, param1);
    }
    
    public void updateLong(String str, long param) throws java.sql.SQLException {
        rsResultSet.updateLong(str, param);
    }
    
    public void updateNull(String str) throws java.sql.SQLException {
        rsResultSet.updateNull(str);
    }
    
    public void updateNull(int param) throws java.sql.SQLException {
        rsResultSet.updateNull(param);
    }
    
    public void updateObject(String str, Object obj) throws java.sql.SQLException {
        rsResultSet.updateObject(str, obj);
    }
    
    public void updateObject(int param, Object obj) throws java.sql.SQLException {
        rsResultSet.updateObject(param, obj);
    }
    
    public void updateObject(int param, Object obj, int param2) throws java.sql.SQLException {
        rsResultSet.updateObject(param, obj, param2);
    }
    
    public void updateObject(String str, Object obj, int param) throws java.sql.SQLException {
        rsResultSet.updateObject(str, obj, param);
    }
    
    public void updateRef(int param, java.sql.Ref ref) throws java.sql.SQLException {
        rsResultSet.updateRef(param, ref);
    }
    
    public void updateRef(String str, java.sql.Ref ref) throws java.sql.SQLException {
        rsResultSet.updateRef(str, ref);
    }
    
    public void updateRow() throws java.sql.SQLException {
        rsResultSet.updateRow();
    }
    
    public void updateShort(int param, short param1) throws java.sql.SQLException {
        rsResultSet.updateShort(param, param1);
    }
    
    public void updateShort(String str, short param) throws java.sql.SQLException {
        rsResultSet.updateShort(str, param);
    }
    
    public void updateString(int param, String str) throws java.sql.SQLException {
        rsResultSet.updateString(param, str);
    }
    
    public void updateString(String str, String str1) throws java.sql.SQLException {
        rsResultSet.updateString(str, str1);
    }
    
    public void updateTime(String str, java.sql.Time time) throws java.sql.SQLException {
        rsResultSet.updateTime(str, time);
    }
    
    public void updateTime(int param, java.sql.Time time) throws java.sql.SQLException {
        rsResultSet.updateTime(param, time);
    }
    
    public void updateTimestamp(String str, java.sql.Timestamp timestamp) throws java.sql.SQLException {
        rsResultSet.updateTimestamp(str, timestamp);
    }
    
    public void updateTimestamp(int param, java.sql.Timestamp timestamp) throws java.sql.SQLException {
        rsResultSet.updateTimestamp(param, timestamp);
    }
    
    public boolean wasNull() throws java.sql.SQLException {
        return rsResultSet.wasNull();
    }
    
    public void setOffset(int intOffset) throws java.sql.SQLException {
        //System.err.println(intOffset);
        this.intOffset = intOffset;
        
    }
    
    public void setLimit(int intLimit) {
        this.intLimit = intLimit;
        intMaxRow = intOffset + intLimit;
    }

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RowId getRowId(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBlob(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBlob(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBlob(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(String arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRowId(String arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
    
}
