/**
 *
 * Copyright (c) 2003 Neuragenix. All Rights Reserved.
 *
 * CCase.java
 *
 * Created on 11 February 2004, 00:00
 *
 * @author  Long Tran, email: ltran@neuragenix.com
 *
 *
 * Description:
 *
 * Displaying data from bb
 *
 *
 * Copyright (c) 2005 Neuragenix Ltd, Pty.
 * 356 Collins St, Melbourne, VIC 3000, AUS
 * All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Neuragenix ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Neuragenix.
 *
 *
 **/
package neuragenix.genix.presentation;

// portal classes
import org.jasig.portal.services.LogService;
        
// java classes
import java.util.*;
import java.sql.ResultSet;

// neuragenix classes
import neuragenix.dao.*;
import neuragenix.common.*;



/** 
 * Class for displaying details from BB table
 *
 * @author Long Tran {@link mailto:ltran@neuragenix.com}
 * @version 1.0
 *
 */

public class Presentation{
    public Presentation(){
    }
    
    /**
     * Method to return a list of Building block for a case
     *
     * @param strCaseKey The case key
     * @return XML String listing the BBs
     *
     **/ 
    public static String buildBBListForCase( String strCaseKey ){
        StringBuffer strXML = new StringBuffer();
        
        try{
            
            // build the query instance
            DALQuery query = new DALQuery();
            query.setDomain( "BB", null, null, null );
            Vector formfields = DatabaseSchema.getFormFields("presentation_view_building_block");
            query.setFields( formfields, null );
            
            // set wheres
            query.setWhere( null, 0, "BB_intCaseKey", "=", strCaseKey, 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "BB_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            
            ResultSet rs = query.executeSelect();
            strXML.append( QueryChannel.buildSearchXMLFile("building_block", rs, formfields ));
            
            // close rs, a must
            rs.close();
            
        }catch( Exception e){
            LogService.instance().log( LogService.ERROR, "Error in Presentation::buildBBForCase: ", e );
        }
        //return "<bb_list>" + strXML.toString() + "</bb_list>";
        return strXML.toString();
    }
    
    /**
     * Method to return a all Building block for a case
     *
     * @param strCaseKey The case key
     * @return XML String the BB details
     *
     **/ 
    public static String buildAllBBDetails( String strCaseKey ){
        StringBuffer strXML = new StringBuffer();
        
        try{
            
            // build the query instance
            DALQuery query = new DALQuery();
            query.setDomain( "BB", null, null, null );
            Vector formfields = DatabaseSchema.getFormFields("presentation_view_building_block");
            query.setFields( formfields, null );
            
            // set wheres
            query.setWhere( null, 0, "BB_intCaseKey", "=", strCaseKey, 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "BB_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            
            ResultSet rs = query.executeSelect();
            
            while( rs.next() ){
                strXML.append( buildBBDetails( rs.getString( "BB_intBBKey" )));
            }
            // close rs, a must
            rs.close();
            
        }catch( Exception e){
            LogService.instance().log( LogService.ERROR, "Error in Presentation::buildBBForCase: ", e );
        }
        //return "<bb_list>" + strXML.toString() + "</bb_list>";
        return strXML.toString();
    }
    
    /**
     * Method to return the building block details
     *
     * @param strBBKey They building block key
     * @return XML String of the building block details
     * 
     **/
    public static String buildBBDetails( String strBBKey ){
        StringBuffer strXML = new StringBuffer();
        
        try{
            
            strXML.append( QueryChannel.buildViewFromKeyXMLFile( 
                    DatabaseSchema.getFormFields("presentation_view_building_block"), "BB_intBBKey",
                    strBBKey));
            
            // build the query instance
            DALQuery query = new DALQuery();
            query.setDomain( "BBDT", null, null, null );
            Vector formfields = DatabaseSchema.getFormFields("presentation_view_bb_data");
            query.setFields( formfields, null );
            
            // set wheres
            query.setWhere( null, 0, "BBDT_intBBKey", "=", strBBKey, 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "BBDT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query.setOrderBy( "BBDT_intBBDTKey", "ASC" );
            
            ResultSet rs = query.executeSelect();
            strXML.append( buildSearchXMLFile("bb_data", "row", rs, formfields ));
            
            // close rs, a must
            rs.close();
            
        }catch( Exception e){
            LogService.instance().log( LogService.ERROR, "Error in Presentation::buildBBForCase: " + e.toString(), e );
        }
        return "<block>" + strXML.toString() + "</block>";
    }
    
    /** 
     * Build XML string for search BB result
     *
     * @param strSearchTage Tag to be wrapped by
     * @param rsResultSet Resultset
     * @param vtFields Vector formfields
     *
     * @return Result XML String
     */
    public static String buildSearchXMLFile(String strSearchTag, String strRowTag, ResultSet rsResultSet, Vector vtFields) throws Exception {
        StringBuffer strXML = new StringBuffer();
        int intNoOfFields = vtFields.size();
        
        try {
            boolean firstCell = true;
            while (rsResultSet.next()) {
                
                // start row tag
                if( firstCell ){
                    strXML.append("<" + strRowTag + ">");
                    firstCell = false;
                }
                
                // build the values here, I don't care, just copy from QueryChannel
                strXML.append("<" + strSearchTag + ">");
                for (int i=0; i < intNoOfFields; i++) {
                    String strFieldName = (String) vtFields.get(i);
                    strXML.append("<" + strFieldName + ">");
                    
                    if (rsResultSet.getString(strFieldName) != null) {
                        DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                        if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                            strXML.append(Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName)));
                        else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                            strXML.append(Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName)));
                        else
                            strXML.append(Utilities.cleanForXSL(rsResultSet.getString(strFieldName)));
                    }
                    strXML.append("</" + strFieldName + ">");
                }
                
                strXML.append("</" + strSearchTag + ">");
                
                // end row tag
                if( rsResultSet.getInt( "BBDT_intIsLast" ) > 0){
                    strXML.append("</" + strRowTag + ">");
                    firstCell = true;
                }
            }
        }
        catch (Exception e) {
            throw new Exception("Unknown error in Presentation::buildSearchXMLFile: ", e);
        }
        
        return strXML.toString();
    }
    
}