/*
 * SmartformManager.java
 *
 * Created on August 24, 2005, 1:56 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.smartform;

import java.util.Enumeration;
import java.util.List;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.ResultSet;

import neuragenix.dao.DALQuery;
import neuragenix.dao.DBSchema;

import neuragenix.dao.DatabaseSchema;
import neuragenix.common.QueryChannel;
import neuragenix.common.Utilities;
import neuragenix.security.AuthToken;
import org.jasig.portal.services.LogService;
/**
 *
 * @author renny
 */
public class SmartformManager implements ISmartformManager
{
    
    /** Creates a new instance of SmartformManager */
    public SmartformManager() 
    {

    }
    public List getSmartformList(String strdomain)
    {
        List lst = null;
        return lst;
    }
    
    // Generates a smartform Entity template based on the input smartform key
    public ISmartformEntity getSmartformEntity(int intSmartformKey)
    {
        ISmartformEntity SE = SmartformFactory.getSmartformEntityInstance() ;
        ISmartformDE dataElement;
        Vector vtDataElementDetails = DatabaseSchema.getFormFields("csmartform_dataelement_details"); 
        SE.setSmartformKey(Integer.toString(intSmartformKey));
        
        try
        {
            // Contruct the smartform entity - get all the dataelements, their IDs, types and default values
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setField("DATAELEMENTS_strDataElementName", null);
            query.setField("DATAELEMENTS_intDataElementType", null);
            query.setField("DATAELEMENTS_intDataElementID", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementNo", null);
            query.setField("DATAELEMENTS_intMandatory", null);
            query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            // Don't get the dataelements that indicate a page break
            query.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "<>", "PAGEBREAK", 0, DALQuery.WHERE_HAS_VALUE);        
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", Integer.toString(intSmartformKey), 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");
            ResultSet rsResult = query.executeSelect();

            while (rsResult.next())
            {
                dataElement = SmartformFactory.getSmartformDEInstance();
                dataElement.setID(rsResult.getString("DATAELEMENTS_intDataElementID"));
                dataElement.setName(rsResult.getString("DATAELEMENTS_strDataElementName"));
                dataElement.setType(rsResult.getString("DATAELEMENTS_intDataElementType"));
                dataElement.setOrder(rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder"));
                dataElement.setDENo(rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo"));
                int intmandatory = rsResult.getInt("DATAELEMENTS_intMandatory");
                if(intmandatory == 0)
                {
                    dataElement.setMandatory(false);
                }
                else
                {
                    dataElement.setMandatory(true);
                }
                
                // Add the data element to the list of dataelements for this SmartformEntity
                SE.addDE(dataElement);
            } 

            rsResult.close();
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);            
        }
        
        return SE;

    }    
    //returns a list of smartformEntities
    public List getSmartformList(String strdomain,String strpartcptkey) 
    {
        Vector vtSmrtfrmEntity = new Vector();
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORM",null , null, null);
            query.setDomain("SMARTFORMPARTICIPANTS", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");
            query.setField("SMARTFORMPARTICIPANTS_intSmartformParticipantID", null);
            query.setField("SMARTFORMPARTICIPANTS_intParticipantID", null);
            query.setField("SMARTFORMPARTICIPANTS_intSmartformID", null);
            query.setField("SMARTFORM_strSmartformName", null);
            query.setField("SMARTFORM_strDomain", null);
            query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intParticipantID", "=", strpartcptkey, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORM_strDomain", "=", strdomain, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strSmartformStatus", "<>", "Not started", 0, DALQuery.WHERE_HAS_VALUE);
            //System.err.println("results: "+query.convertSelectQueryToString());
            ResultSet rsResult = query.executeSelect();

            while (rsResult.next())
            {
                ISmartformEntity smrtEObj = SmartformFactory.getSmartformEntityInstance();
                smrtEObj.setSmartformParticipantKey(rsResult.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID"));
                smrtEObj.setDomain(rsResult.getString("SMARTFORM_strDomain"));
                smrtEObj.setSmartformName(rsResult.getString("SMARTFORM_strSmartformName"));
                smrtEObj.setSmartformKey(rsResult.getString("SMARTFORMPARTICIPANTS_intSmartformID"));
                smrtEObj.setParticipantKey(rsResult.getString("SMARTFORMPARTICIPANTS_intParticipantID"));
                vtSmrtfrmEntity.add(smrtEObj);
            }
            rsResult.close();
            if(vtSmrtfrmEntity != null && vtSmrtfrmEntity.size() > 0)
            {
            	for(Enumeration eSE = vtSmrtfrmEntity.elements();eSE.hasMoreElements();)
            	{
            		ISmartformEntity iSE = (ISmartformEntity)eSE.nextElement();
            		Vector vtDE =  new Vector(iSE.getDE());
            		try
                    {
                        DALQuery qr_questions = new DALQuery();
                        qr_questions.setDomain("DATAELEMENTS", null,null,null);
                        qr_questions.setDomain("SMARTFORMTODATAELEMENTS", "DATAELEMENTS_intDataElementID", "SMARTFORMTODATAELEMENTS_intDataElementID", "INNER JOIN");
                        qr_questions.setField("DATAELEMENTS_strDataElementName", null);
                        qr_questions.setField("DATAELEMENTS_intDataElementType", null);
                        qr_questions.setField("DATAELEMENTS_intDataElementID", null);
                        qr_questions.setField("SMARTFORMTODATAELEMENTS_intDataElementNo", null);
                        qr_questions.setField("DATAELEMENTS_intMandatory", null);
                        qr_questions.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", iSE.getSmartformKey(), 0, DALQuery.WHERE_HAS_VALUE);
                        qr_questions.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        qr_questions.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        qr_questions.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementNo","ASC");
                        ResultSet rs_questions = qr_questions.executeSelect();
                        while(rs_questions.next())
                        {
                            ISmartformDE DE = SmartformFactory.getSmartformDEInstance();
                            DE.setName(rs_questions.getString("DATAELEMENTS_strDataElementName"));
                            DE.setType(rs_questions.getString("DATAELEMENTS_intDataElementType"));
                            DE.setID(rs_questions.getString("DATAELEMENTS_intDataElementID"));
                            DE.setDENo(rs_questions.getString("SMARTFORMTODATAELEMENTS_intDataElementNo"));
                            int intmandatory = rs_questions.getInt("DATAELEMENTS_intMandatory");
                            if(intmandatory == 0)
                            {
                                DE.setMandatory(false);
                            }
                            else
                            {
                                DE.setMandatory(true);
                            }
                            vtDE.add(DE);
                        }
                        rs_questions.close();
            	}
		        catch (Exception e)
		        {
		            LogService.instance().log(LogService.ERROR, "Unknown error in getting smartformList in Manager - " + e.toString(), e);
		        }
            	iSE.replaceDE(vtDE);
              }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in getting smartformList in Manager - " + e.toString(), e);
        }
        return vtSmrtfrmEntity;
    }
    public List addSmartformEntities(List lstsmrtfrmEntity)
    {
        List lst = null;
        return lst;
    }
    
    // saves the DE Results to the database
    public boolean saveSmartformResults(List lstsmrtfrmEntity, AuthToken authToken)
    {
        Vector vtSmrtfrmEntity = new Vector(lstsmrtfrmEntity);
        Vector vtAddSmartformParticipant = DatabaseSchema.getFormFields("csmartform_smartform_participant_add");
        
        try
        {
            DALQuery query = new DALQuery();

            for(int i=0; i<vtSmrtfrmEntity.size(); i++)
            {
                ISmartformEntity SE = (ISmartformEntity) vtSmrtfrmEntity.get(i);
                // Need to add entry to ix_smartform_participants
                if (SE.getSmartformParticipantKey() == null)
                {
                    Hashtable htSFPartDetails = new Hashtable();
                    htSFPartDetails.put("SMARTFORMPARTICIPANTS_strAddedBy", authToken.getUserIdentifier());
                    htSFPartDetails.put("SMARTFORMPARTICIPANTS_dtDateAdded", Utilities.getDateTimeStampAsString("yyyy-MM-dd"));
                    htSFPartDetails.put("SMARTFORMPARTICIPANTS_strSmartformStatus", "Incomplete");
                    htSFPartDetails.put("SMARTFORMPARTICIPANTS_intParticipantID", SE.getParticipantKey());
                    htSFPartDetails.put("SMARTFORMPARTICIPANTS_intSmartformID", SE.getSmartformKey());
                    htSFPartDetails.put("SMARTFORMPARTICIPANTS_strDomain", SE.getDomain());

                    // insert new smartform participant
                    query.reset();
                    query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                    query.setFields(vtAddSmartformParticipant, htSFPartDetails);
                    query.setField("SMARTFORMPARTICIPANTS_strLastUpdatedBy",authToken.getUserIdentifier());
                    query.executeInsert();   

                    // Set the new smartform particpant key
                    SE.setSmartformParticipantKey(QueryChannel.getNewestKeyAsString(query));
                }    

                // Get the dataelements belonging to the smartform
                Vector vtDE = new Vector(SE.getDE());
                for (int j=0; j<vtDE.size(); j++)
                {    
                    ISmartformDE DE = (ISmartformDE) vtDE.get(j);
                    String strDataelementKey = DE.getID();
                    query.reset();
                    query.setDomain("SMARTFORMRESULTS", null, null, null);
                    query.setField ("SMARTFORMRESULTS_intSmartformResultID", null);
                    query.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", SE.getSmartformParticipantKey(), 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataelementKey, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rs = query.executeSelect();


                    DALQuery queryUpdate = new DALQuery();
                    queryUpdate.setDomain("SMARTFORMRESULTS", null, null, null);
                    queryUpdate.setField ("SMARTFORMRESULTS_strDataElementResult", DE.getResult());                

                    // If record exists update, otherwise insert a new entry to ix_smartform_results
                    if (rs.next())
                    {
                        queryUpdate.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", SE.getSmartformParticipantKey(), 0, DALQuery.WHERE_HAS_VALUE);
                        queryUpdate.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataelementKey, 0, DALQuery.WHERE_HAS_VALUE);
                        queryUpdate.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        queryUpdate.executeUpdate();

                    }
                    else
                    {
                        // The new smartform results are inserted
                        queryUpdate.setField ("SMARTFORMRESULTS_intSmartformParticipantID", SE.getSmartformParticipantKey());
                        queryUpdate.setField ("SMARTFORMRESULTS_intDataElementID", strDataelementKey);                
                        queryUpdate.executeInsert();
                    }    

                }

            }

        }
        catch (Exception e)
        {
            return false;
        } 
        
        return true;
    }
    
    public boolean deleteSmartformResults(ISmartformEntity smrtentity)
    {
        return false;
    }
    public List getSmartformResults(String strdomain,String strpk)
    {
        List lst = this.getSmartformList(strdomain, strpk);
        lst = this.getSmartformResults(lst);
        return lst;
    }
    public List getSmartformResults(List lstsmrtfrmEntty)
    {
        Vector vtSmrtfrmEntity = new Vector(lstsmrtfrmEntty);
        for(java.util.Enumeration eSE = vtSmrtfrmEntity.elements();eSE.hasMoreElements();)
        {
            ISmartformEntity SE = (ISmartformEntity)eSE.nextElement();
           Vector vtDE = new Vector(SE.getDE()); 
           if(vtDE != null && vtDE.size() > 0)
           {
	           for(Enumeration eDE = vtDE.elements();eDE.hasMoreElements();)
	           {
	        	   ISmartformDE DE = (ISmartformDE)eDE.nextElement();
		            try
		            {
		                DALQuery qr_result = new DALQuery();
		                qr_result.setDomain("SMARTFORMRESULTS",null , null, null);
		                qr_result.setField("SMARTFORMRESULTS_strDataElementResult", null);
		                qr_result.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", SE.getSmartformParticipantKey(), 0, DALQuery.WHERE_HAS_VALUE);
		                qr_result.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
		                qr_result.setWhere("AND", 0, "SMARTFORMRESULTS_strDataElementID", "=",DE.getID() , 0, DALQuery.WHERE_HAS_VALUE);
		                ResultSet rs_results = qr_result.executeSelect();
		                while(rs_results.next())
		                {
		                    DE.setResult(rs_results.getString("SMARTFORMRESULTS_strDataElementResult"));
		                }
		                rs_results.close();
		            }
		            catch(Exception e)
		            {
		            
		            }
		           SE.replaceDE(vtDE);
		        }
           }
        }
        return vtSmrtfrmEntity;
    }
    public ISmartformEntity getSmartformResults(ISmartformEntity smrtfrmEntity)
    {
        return smrtfrmEntity;
    }
}
