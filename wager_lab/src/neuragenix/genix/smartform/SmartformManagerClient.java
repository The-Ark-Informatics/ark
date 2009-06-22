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
public class SmartformManagerClient extends SmartformManager
{
    
    /** Creates a new instance of SmartformManager */
    public SmartformManagerClient() 
    {
    	super();
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
		                qr_result.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=",DE.getID() , 0, DALQuery.WHERE_HAS_VALUE);
		                ResultSet rs_results = qr_result.executeSelect();
		                while(rs_results.next())
		                {
		                    DE.setResult(rs_results.getString("SMARTFORMRESULTS_strDataElementResult"));
//		                  check for diseases if Name is protein
		                    if(DE.getName().equalsIgnoreCase("protein") && DE.getResult() != null)
		                    {
		                    	DALQuery query_protein = new DALQuery();
		                    	query_protein.setDomain("PROTEIN", null, null, null);
		                    	query_protein.setField("PROTEIN_strDisease",null);
		                    	query_protein.setWhere(null, 0, "PROTEIN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
		                    	query_protein.setWhere("AND", 0, "PROTEIN_strName", "=",DE.getResult() , 0, DALQuery.WHERE_HAS_VALUE);
		                        ResultSet rs_protein = query_protein.executeSelect();
		                        while(rs_protein.next())
		                        {
		                        	String strOther = rs_protein.getString("PROTEIN_strDisease");
		                        	if(strOther != null)
		                        	{
		                        		DE.setOther(strOther);
                                                        DE.setAttribute("strDisease",strOther);
		                        	}
		                        	else
		                        	{
		                        		DE.setOther("");
                                                        DE.setAttribute("strDisease","");
		                        	}
		                        }
		                        //***********SECTION: update the gene
                                        //rs_protein.close();
                                        //todo: Gene
                                        query_protein = new DALQuery();
		                    	query_protein.setDomain("PROTEIN", null, null, null);
		                    	query_protein.setField("PROTEIN_strGene",null);
		                    	query_protein.setWhere(null, 0, "PROTEIN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
		                    	query_protein.setWhere("AND", 0, "PROTEIN_strName", "=",DE.getResult() , 0, DALQuery.WHERE_HAS_VALUE);
		                        rs_protein = query_protein.executeSelect();
		                        while(rs_protein.next())
		                        {
		                        	String strOther = rs_protein.getString("PROTEIN_strGene");
		                        	if(strOther != null)
		                        	{
		                        		DE.setAttribute("strGene",strOther);
		                        	}
		                        	else
		                        	{
		                        		DE.setAttribute("strGene","");
		                        	}
		                        }
		                        rs_protein.close();
                                        
		                    }
		                }
		                rs_results.close();
		            }
		            catch(Exception e)
		            {
		                LogService.instance().log(LogService.ERROR, "Unknown error in getting smartformList in Manager - " + e.toString(), e);
		            }
		           SE.replaceDE(vtDE);
		        }
           }
        }
        return vtSmrtfrmEntity;
    }
 }
