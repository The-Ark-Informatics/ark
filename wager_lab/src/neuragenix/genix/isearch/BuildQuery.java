/*
 * BuildQuery.java
 *
 * Created on 7 July 2005
 */

package neuragenix.genix.isearch;

/**
 *
 * @author  sparappat
 */

import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.bio.utilities.*;
import neuragenix.bio.biospecimen.*;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;




public class BuildQuery extends IntelligentSearch{
    
    private Hashtable hashIdentityKey = new Hashtable ();
    private Hashtable hashNoCriteriaSFDomain = new Hashtable ();
    private final int MAX_SIBLING_QUERIES = 5;
    private Vector vtUsedSFCriteria = new Vector ();
    private Hashtable hashStudyDomainSelected = new Hashtable();
    /** Creates a new instance of BuildQuery */
    public BuildQuery() 
    {

    }    
    
    public BuildQuery(IntelligentSearch is) 
    {
        super.vtDomainVal = is.vtDomainVal;
        super.vtParticipantDomain = is.vtParticipantDomain;
        super.vtJoin = is.vtJoin;
        super.vtSelectedDomains = is.vtSelectedDomains;
        super.vtSearchFormFields = is.vtSearchFormFields;
        super.vtSelectedSmartforms = is.vtSelectedSmartforms;
        super.vtSelectedDataelements = is.vtSelectedDataelements;
        super.vtSelectedSFDataelements = is.vtSelectedSFDataelements;
        super.vtSelectedStudys = is.vtSelectedStudys;        
        super.vtField = is.vtField; 
        super.vtOp = is.vtOp; 
        super.vtSearchVal = is.vtSearchVal; 
        super.vtOpenBracket = is.vtOpenBracket;
        super.vtCloseBracket = is.vtCloseBracket;
        super.runtimeData = is.runtimeData;
        super.hashCriteriaDomains = is.hashCriteriaDomains;
        
        hashIdentityKey.put ("PATIENT", "PATIENT_intInternalPatientID");
        hashIdentityKey.put ("BIOSPECIMEN", "BIOSPECIMEN_intBiospecimenID");
        hashIdentityKey.put ("BIOANALYSIS", "BIOSPECIMEN_intBiospecimenID");
        hashIdentityKey.put ("ADMISSIONS", "ADMISSIONS_intGroupKey");
    }
    
    ResultSet executeSFSearch ()    
    {
        try
        {            
            DALQuery query = new DALQuery ();
            DALQuery siblingQuery[] = new DALQuery [MAX_SIBLING_QUERIES];
            boolean blSmartformCriteria = false;
            String strDomain = "";
            boolean blHasResults = true;
            
            // For each smartform selected for display
            // determine which domain in ix_smartform_participants 
            // is to be searched
            Hashtable hashSFDomains = getSmartformDomains ();

            // Clear the search formfields
            vtSearchFormFields.clear();

            // Add the identity keys based on the domains to be searched on
            // to vtSearchFormFields
            Enumeration key_enum = hashSFDomains.keys();

            while (key_enum.hasMoreElements())
            {
                strDomain = key_enum.nextElement().toString();
                if (hashIdentityKey.containsKey(strDomain))
                {    
                    if (!vtSearchFormFields.contains(hashIdentityKey.get(strDomain)))
                    {
                         vtSearchFormFields.add (hashIdentityKey.get(strDomain));
                    }    
                }            
            }   
            
            // Always add the Patient and Biospecimen Keys if these domains have been selected
            if(vtSelectedDomains.contains("PATIENT")){
                if (!vtSearchFormFields.contains(hashIdentityKey.get("PATIENT")))
                {
                     vtSearchFormFields.add (hashIdentityKey.get("PATIENT"));
                }    
            }
            if(vtSelectedDomains.contains("BIOSPECIMEN")){
                if (!vtSearchFormFields.contains(hashIdentityKey.get("BIOSPECIMEN")))
                {
                     vtSearchFormFields.add (hashIdentityKey.get("BIOSPECIMEN"));
                }    
            }
            
            // If the admissions domain is selected also add the admissionkey to the formfields
            if (vtSelectedDomains.contains("ADMISSIONS"))
            {
                vtSearchFormFields.add("ADMISSIONS_intAdmissionkey");
            }

            // If the biospeicmen transactions domain is selected also add the biotransactionskey to the formfields
            if(vtSelectedDomains.contains("BIOSPECIMEN_TRANSACTIONS")){
                 vtSearchFormFields.add ("BIOSPECIMEN_TRANSACTIONS_intBioTransactionID");
            }

            // Get all the formfields selected for display
            getDisplayFormfields();
            Enumeration enumDomain = hashSFDomains.keys();

            strDomain = enumDomain.nextElement().toString();
            
            // Build the query for the first smartform participant domain
            query = buildMainQueryDomains (strDomain, hashIdentityKey.get(strDomain).toString());            
            buildMainQueryConditions (strDomain, query, (Vector) hashSFDomains.get(strDomain));
            int intNumSibQuery=0;
            // union with queries for the other smartform  participant domains
            while (enumDomain.hasMoreElements())
            {
               strDomain = enumDomain.nextElement().toString();
               siblingQuery[intNumSibQuery] = new DALQuery();
               siblingQuery[intNumSibQuery] = buildMainQueryDomains (strDomain, hashIdentityKey.get(strDomain).toString());            
               buildMainQueryConditions (strDomain, siblingQuery[intNumSibQuery], (Vector) hashSFDomains.get(strDomain));
               query.setSiblingQuery("UNION", siblingQuery[intNumSibQuery], 0, 0);
               intNumSibQuery++;
            }    
            
            
            // Determine if smartform criteria has been entered by the user
//            if ( vtSelectedDataelements.isEmpty() == false ){
                for( int h = 0; h < vtSelectedSmartforms.size(); h++){
                     if( vtDomainVal.contains( vtSelectedSmartforms.get(h).toString() ) ){
                         blSmartformCriteria = true;
                         break;
                     }   
                }   
//            }    
            
            // If there is smartform criteria specified, take this into account
            // and modify the query as required
            if (blSmartformCriteria == true)
            {                
                int intTotalSFCriteria = 0;
                // Get the total number of smartform criteria
                // build each of the smartform criteria into the query
                for (int i=0; i<vtDomainVal.size(); i++)
                {
                    
                    // if a smartform criteria
                    if (!vtParticipantDomain.get(i).equals("blank"))
                    {
                        intTotalSFCriteria++;
                    }
                }
                
                Hashtable hashResult;
                String strSFPartList = "";
                int intSFCriteriaNum = 0;
                
                // build each of the smartform criteria into the query
                for (int i=0; i<vtDomainVal.size(); i++)
                {
                    
                    // if a smartform criteria
                    if (!vtParticipantDomain.get(i).equals("blank") && !vtUsedSFCriteria.contains(Integer.toString(i)))
                    {
                        // get a vector of other criteria that can be grouped with this one
                        Vector vtSFCriteria = getCriteriaforSF(i, vtDomainVal.get(i).toString(),vtParticipantDomain.get(i).toString());
                        
                        intSFCriteriaNum++;
                        int firstCriteria = Integer.parseInt(vtSFCriteria.firstElement().toString());                        
                        int lastCriteria = Integer.parseInt(vtSFCriteria.lastElement().toString());                        
                        int OpenBracketNo = getBracketNo(vtOpenBracket.get(firstCriteria).toString());
                        int CloseBracketNo = getBracketNo(vtCloseBracket.get(lastCriteria).toString());
                        strDomain = vtParticipantDomain.get(i).toString();
                        
                        // dummy criteria to enclose the smartform criteria
                        if (intSFCriteriaNum == 1)
                        {
                            query.setWhere("AND", 1, hashIdentityKey.get(strDomain).toString() , ">", "-1", 0, DALQuery.WHERE_HAS_VALUE);                            
                            for (int sibQuery=0; sibQuery<intNumSibQuery; sibQuery++)
                            {    
                                siblingQuery[sibQuery].setWhere("AND", 1, hashIdentityKey.get(strDomain).toString() , ">", "-1", 0, DALQuery.WHERE_HAS_VALUE); 
                            }                        
                        }                                                                        
                        
                        hashResult = getSFCriteriaResults (strDomain, hashIdentityKey.get(strDomain).toString(), vtSFCriteria);
                        String strJoin = "AND";
                        if (vtJoin.get(i) != null && !vtJoin.get(i).toString().equals(""))
                        {
                            strJoin = vtJoin.get(i).toString();
                            if (vtJoin.get(i).toString().equals("EXCLUDING"))
                            {
                                strJoin = "AND NOT";
                            }    
                        }    

                        if (hashResult.get(hashIdentityKey.get(strDomain)) != null && !hashResult.get(hashIdentityKey.get(strDomain)).equals(""))
                        {
                            query.setWhere(strJoin, OpenBracketNo, hashIdentityKey.get(strDomain).toString() , "IN", hashResult.get(hashIdentityKey.get(strDomain)), CloseBracketNo, DALQuery.WHERE_HAS_VALUE);
                            // set the criteria conditions for each sibling query
                            for (int sibQuery=0; sibQuery<intNumSibQuery; sibQuery++)
                            {    
                                siblingQuery[sibQuery].setWhere(strJoin, OpenBracketNo, hashIdentityKey.get(strDomain).toString() , "IN", hashResult.get(hashIdentityKey.get(strDomain)), CloseBracketNo, DALQuery.WHERE_HAS_VALUE);
                            }
                            strSFPartList +=  hashResult.get("SMARTFORMPARTICIPANTS_intSmartformParticipantID").toString();                        
                        }
                        
                        // dummy criteria to enclose the smartform criteria
                        if (vtUsedSFCriteria.size() == intTotalSFCriteria)
                        {
                            query.setWhere("AND", 0, hashIdentityKey.get(strDomain).toString() , ">", "-1", 1, DALQuery.WHERE_HAS_VALUE);                            
                            for (int sibQuery=0; sibQuery<intNumSibQuery; sibQuery++)
                            {    
                                siblingQuery[sibQuery].setWhere("AND", 0, hashIdentityKey.get(strDomain).toString() , ">", "-1", 1, DALQuery.WHERE_HAS_VALUE);                            
                            }                              
                        }
                        
                    }    
                }
                
                // if the smartforms for which criterias were specified returns some results
                // Get the smartform participant keys for the smartforms with no criteria                
                if (!strSFPartList.equals(""))
                {               
                    Enumeration sfEnum = hashNoCriteriaSFDomain.keys();
                    DALQuery sfQuery = new DALQuery (); 
                    Vector vtSF = new Vector();
                    Vector vtSFPartKey = new Vector ();
                    ResultSet rs;
                    while (sfEnum.hasMoreElements())
                    {
                        String strSmartform = sfEnum.nextElement().toString();
                        strDomain = hashNoCriteriaSFDomain.get(strSmartform).toString();
                        sfQuery.reset();
                        sfQuery = buildMainQueryDomains (strDomain, hashIdentityKey.get(strDomain).toString());     
                        vtSF.clear();
                        vtSF.add(strSmartform);
                        buildMainQueryConditions (strDomain, sfQuery, vtSF);
                        
                        //System.out.println ("No Criteria SF " + sfQuery.convertSelectQueryToString());                        
                        rs = sfQuery.executeSelect();
                        vtSFPartKey.clear();
                        while (rs.next())
                        {
                           if (!vtSFPartKey.contains(rs.getString ("SMARTFORMPARTICIPANTS_intSmartformParticipantID")))
                           {
                               vtSFPartKey.add (rs.getString ("SMARTFORMPARTICIPANTS_intSmartformParticipantID"));
                           }              
                        }  
                        
                        // Close the resultset
                        rs.close();

                        // Get the comma delimited list of smartform participant keys
                        // and add to current SPPartKeyList
                        strSFPartList += getCommaDelimitedList (vtSFPartKey);                                    
                    }    
                

                    strSFPartList = strSFPartList.replaceFirst("\\(",  "");
                    strSFPartList = strSFPartList.replaceAll("\\)", "");
                    strSFPartList = strSFPartList.replaceAll("\\(", ",");
                    strSFPartList = "(" + strSFPartList + ")"; 
        
                    query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID" , "IN", strSFPartList, 0, DALQuery.WHERE_HAS_VALUE);                    
                    // set the criteria conditions for each sibling query
                    for (int sibQuery=0; sibQuery<intNumSibQuery; sibQuery++)
                    {    
                        siblingQuery[sibQuery].setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID" , "IN", strSFPartList, 0, DALQuery.WHERE_HAS_VALUE);                    
                    }                
                }   
                else
                {
                    blHasResults = false;
                }    

            }    

            
            if(vtSelectedDomains.contains("PATIENT")){
                query.setOrderBy("PATIENT_intInternalPatientID", "ASC", true);
            }
            
            if(vtSearchFormFields.contains("ADMISSIONS_intAdmissionkey")){
                query.setOrderBy("ADMISSIONS_intAdmissionkey", "ASC", true);
            }
            
            if(vtSelectedDomains.contains("BIOSPECIMEN")){
                query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC", true);
            }
            
            if(vtSelectedDomains.contains("BIOSPECIMEN_TRANSACTIONS")){
                query.setOrderBy("BIOSPECIMEN_TRANSACTIONS_intBioTransactionID", "ASC", true); 
            }
            


            if ((vtSelectedStudys.isEmpty() == false))
            {
                query.setOrderBy("STUDY_intStudyID", "ASC", true);
            }   

            // Set orderby SF participant, only if dataelements are selected
            if( vtSelectedDataelements.isEmpty() == false ){
                query.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "ASC", true);
            }
            
            
            
            //System.out.println ("executeSFSearch: " + query.convertSelectQueryToString());            
 
            if (blHasResults)
                return query.executeSelect();
            else 
            {
                // dummy query to indicate no results
                // done to be compatible with the way the IntelligentSearch module
                // handles the resultset returned from a query
                DALQuery retQuery = new DALQuery();
                retQuery.setDomain("PATIENT", null, null, null);
                retQuery.setField("PATIENT_intInternalPatientID", null);
                retQuery.setWhere(null, 0, "PATIENT_intInternalPatientID" , "<", "-1", 0, DALQuery.WHERE_HAS_VALUE);                            
                
                return retQuery.executeSelect();
            }   
        }
        catch (Exception e)
        {
           LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
           return null;
        }
                
        
    }
    
    /*
     * Build the Main criteria Query domains 
     *
     */
   private DALQuery buildMainQueryDomains(String strParticipantDomain, String strParticipantDomainKey)
   {
       
   try{       
           DALQuery query = new DALQuery();      
           boolean blStudyDomainSelected = false;           
           hashStudyDomainSelected.put (strParticipantDomain, "false");

           // Set the first domain
           // if only 1 study is selected and no domains are selected
           if (vtSelectedDomains.isEmpty() == true && vtSelectedStudys.size() == 1)
           {
                query.setDomain("STUDY", null, null, null);  

           }
           else
           {

               if ( vtSelectedDomains.isEmpty() == true || (vtSelectedDomains.size() == 1 && vtSelectedDomains.get(0).toString().equals("BIOANALYSIS")) )
               {
                    return null;

               }else
               {
                   // if the first domain is not BIOANALYSIS
                   if(!vtSelectedDomains.get(0).toString().equals("BIOANALYSIS"))
                   {
                       query.setDomain(vtSelectedDomains.get(0).toString(), null, null, null);                  
                   }
                   else
                   {
                       // if the first domain is BIOANALYSIS
                       query.setDomain(vtSelectedDomains.get(1).toString(), null, null, null);                  
                   }
                } 
           } 

           // Set the other domains and the join between them
           // Get all the selected domains except first
           for (int i=1; i < vtSelectedDomains.size(); i++){

               String strDomain = (String)vtSelectedDomains.get(i);
               
               // if study already selected, don't join
               // it will processed later
               if (strDomain.equals("STUDY") && (vtSelectedStudys.size()>0))
               {
                  continue;
               }    

               // if the first domain is BIOANALYSIS and if i is 1, skip since it is already set OR if the domain is BIOANALYSIS do not set
               if( (vtSelectedDomains.get(0).toString().equals("BIOANALYSIS") && i == 1) || (strDomain.equals("BIOANALYSIS")) )
               {
                 /* Do Nothing */
               }
               else
               {

                   // For each domain, get its join object               
                   Hashtable hashJoins = (Hashtable)DatabaseSchema.getSearchDomains().get(strDomain);

                   // Get all possible joins for this strDomain, with all the domains in vtSelectedDomains
                   for( int j=0; j < i; j++ )
                   {
                       // Check to see if there is a join between strDomain and vtSelectedDomains(j)
                       if(hashJoins.containsKey((String)vtSelectedDomains.get(j)))
                       { 
                           // Get the join object, which consists of join between strDomain and vtSelectedDomains.get(j)
                            DBJoin joinObj = (DBJoin)hashJoins.get(vtSelectedDomains.get(j));
                            query.setDomain( strDomain, joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                            break; 
                       } 
                   }   
               }

           }

           
           if (strParticipantDomain.equals("PATIENT"))
           {
               query.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
               query.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                                  
               if (vtSelectedStudys.isEmpty() == false)
               {
                   query.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");
                   blStudyDomainSelected = true;
                   hashStudyDomainSelected.put (strParticipantDomain, "true");
               }                       
           }
           else if (strParticipantDomain.equals("BIOSPECIMEN"))
           {
               query.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
               query.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                                      
               
               if (IntelligentSearch.FILTER_BIOSPECIMEN_BY_STUDY == true)
               {    
                   if (vtSelectedStudys.isEmpty() == false)
                   {
                       query.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");   
                       blStudyDomainSelected = true;
                       hashStudyDomainSelected.put (strParticipantDomain, "true");
                   }
               }
              
           }    
           else
           {
               // if bioanalysis domain and searching by study
               if (IntelligentSearch.FILTER_BIOSPECIMEN_BY_STUDY == true)
               {    
                   if ((strParticipantDomain.equals("BIOANALYSIS")) && (vtSelectedStudys.isEmpty() == false))
                   {
                       query.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN"); 
                       blStudyDomainSelected = true;
                       hashStudyDomainSelected.put (strParticipantDomain, "true");
                   }    
               }
               query.setDomain("SMARTFORMPARTICIPANTS", strParticipantDomainKey, "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");                      
           }   
           
           // Set the Consent and Consent Study domains if Study is selected & a domain is also selected
           // only if the study domain has not already been selected
           if( (vtSelectedDomains.isEmpty() == false) && (blStudyDomainSelected == false)  )
           {
               if(vtSelectedDomains.contains("PATIENT"))
               {        
                   // if Consent is selected & Consent Study is not selected, and Study is selected along with Patient
                   if( vtSelectedDomains.contains("CONSENT") && !vtSelectedDomains.contains("CONSENTSTUDY") && vtSelectedStudys.isEmpty() == false )
                   {
                        query.setDomain( "CONSENTSTUDY", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN");
                        query.setDomain( "STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN");                                 
                   }                

                   // if Consent & Consent Study is not selected, and Study is selected along with Patient
                   if( !vtSelectedDomains.contains("CONSENT") && !vtSelectedDomains.contains("CONSENTSTUDY") && vtSelectedStudys.isEmpty() == false )
                   {
                       query.setDomain( "CONSENT", "CONSENT_intInternalPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");
                       query.setDomain( "CONSENTSTUDY", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN");
                       query.setDomain( "STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN");            
                   }   
                   // if Consent is selected & Consent Study is not selected, and Study is selected along with Patient
                   if( vtSelectedDomains.contains("CONSENT") && vtSelectedDomains.contains("CONSENTSTUDY") && vtSelectedStudys.isEmpty() == false )
                   {
                       query.setDomain( "STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN");                                 
                   }                
               }
           }           
           

           query.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");           
           query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
           query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID","INNER JOIN");           
           query.setDomain("SMARTFORMRESULTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "INNER JOIN");
           
           return query;
        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        }      
   } 
   
    /*
     * Build the Main criteria Query conditions 
     *
     */
   private void buildMainQueryConditions(String strParticipantDomain, DALQuery query, Vector vtSFList)
   {
       Vector vtSFDataelements = new Vector ();
       boolean blEncloseCriteria = false;
       try
       {
       
           for (int y=0; y<vtSFList.size(); y++)
           {
               if (hashCriteriaDomains.containsKey((String) vtSFList.get(y)))
               {
                   String strCriteriaDomain = (String) vtSFList.get(y);

                   Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);

                    // if domain is a study, dont add Study Name as the formfield            
                    for( int j=0; j < vtSelectedFields.size(); j++ ){

                        vtSFDataelements.add( (String)vtSelectedFields.get(j) );
                    }            
               }    
           }    
           

           // Check for formfields - if it is not empty proceed
           if( vtSearchFormFields.isEmpty() == false)
           {

               // Set the form fields
               query.setFields(vtSearchFormFields, null);

               // to get the smartform names
               for( int k=0; k < vtSFList.size(); k++ )
               {
                   int last = vtSFList.size() - 1;

                   if( vtSFList.size() == 1)
                   {
                       query.setWhere( null, 0, "SMARTFORM_strSmartformName", "=", (String)vtSFList.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                   }else if(k == 0)
                   {
                       query.setWhere( null, 1, "SMARTFORM_strSmartformName", "=", (String)vtSFList.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                   }else if( k == last)
                   {
                       query.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSFList.get(k), 1, DALQuery.WHERE_HAS_VALUE );                            
                   }else
                   {
                       query.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSFList.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                   }          
               }

               // Search by study
               // if study is selected, add it to the where condition
               
               for( int m=0; m < vtSelectedStudys.size(); m++)
               {
                   int last = vtSelectedStudys.size() - 1;

                   if( vtSelectedStudys.size() == 1){
                       query.setWhere( "AND", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                   }else if(m == 0){
                       query.setWhere( "AND", 1, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                   }else if( m == last){
                       query.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 1, DALQuery.WHERE_HAS_VALUE );                            
                   }else{
                       query.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                   }          
               }


               // to get the dataelement name              
               query.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", "SMARTFORMRESULTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);

               // set the smartforms dataelements selected in where condition
               int last = vtSFDataelements.size() - 1;

               for( int m=0; m < vtSFDataelements.size(); m++ )
               {

                    if( vtSFDataelements.size() == 1){
                        query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSFDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }else if( m == 0 ){
                        query.setWhere("AND", 1, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSFDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }else if( m == last ){
                        query.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSFDataelements.get(m), 1, DALQuery.WHERE_HAS_VALUE);
                    }else{                            
                        // Set the selected field to where condition, to be used in search query                        
                        query.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSFDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }

               }       

               query.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
               query.setWhere( "AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
               query.setWhere( "AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
               query.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
               query.setWhere( "AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );           
               if (strParticipantDomain.equals("PATIENT"))
               {
                   query.setWhere( "AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
               }    
               if (strParticipantDomain.equals("BIOSPECIMEN"))
               {
                   query.setWhere( "AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
               }
               
//  NB Incorrect - the intDeleted is getting set when the domain has not even been added to the query
               if (!vtSelectedStudys.isEmpty() && hashStudyDomainSelected.get(strParticipantDomain).equals("false"))
               {                   
                   if(!vtSelectedDomains.contains("CONSENT"))
                   {
                       query.setWhere( "AND", 0, "CONSENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                   }

                   if( !vtSelectedDomains.contains("CONSENTSTUDY"))
                   {
                       query.setWhere( "AND", 0, "CONSENTSTUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                   }

                   if( !vtSelectedDomains.contains("STUDY"))
                   {
                       query.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                   }
               }    
                          
               // set the deleted fields
               for( int m = 0; m < vtSelectedDomains.size(); m++)
               {
                   String name = (String)vtSelectedDomains.get(m);              

                   if(!name.equals("BIOANALYSIS")){
                       name = name+"_intDeleted";
                       query.setWhere( "AND", 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                   }
               }              

               // Convert the domain to correct case for ix_smartform_participants e.g. BIOSPECIMEN to Biospecimen
               String SFDomainUpper = strParticipantDomain.substring(0, 1);
               String SFDomainLower = strParticipantDomain.substring(1).toLowerCase();

               String SFDomain = SFDomainUpper + SFDomainLower;
               if (strParticipantDomain.equals ("PATIENT"))
               {
                   SFDomain = "Study";
               }       

               query.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", SFDomain, 0, DALQuery.WHERE_HAS_VALUE );                            
               
               // Get the number of non-smartform criteria that was entered
               int intTotalCriteria = 0;
               int intCriteria = 0;
               for(int i=0; i < vtJoin.size(); i++)
               {             
                   if( vtSelectedDomains.contains( vtDomainVal.get(i) ))
                   {  
                       intTotalCriteria++;                   
                   }    
                       
               }    
               
               // join **
               // Set the Where condition for criterias
               for(int i=0; i < vtJoin.size(); i++)
               {             

                   int intOpenBracket = getBracketNo(vtOpenBracket.get(i).toString());
                   int intCloseBracket = getBracketNo(vtCloseBracket.get(i).toString());            

                   if( i == 0){
                       vtJoin.set(0, "AND");
                   }


                    // set the new query object "query1" to be same as object "query"
                    //query1 = setQueryObject();                    

                    String strOp = "";
                    String strSearchVal = "";

                    strOp = dbOperator(vtOp.get(i).toString());
                    if( strOp.equals("IS NULL") ){
                        strOp = "";
                        strSearchVal = "IS NULL";                    
                    }else if( strOp.equals("IS NOT NULL") ){
                        strOp = "";
                        strSearchVal = "IS NOT NULL";
                    }
                    
                    
                   // if the criteria is added for schema domain
                   if( vtSelectedDomains.contains( vtDomainVal.get(i) )){                   
                         
                        intCriteria ++;
                        // Add an extra bracket to enclose the criteria
                        if (blEncloseCriteria == false)
                        {
                            intOpenBracket++;
                            blEncloseCriteria = true;
                        } 
                        
                        if ((blEncloseCriteria == true) && (intCriteria == intTotalCriteria))
                        {
                            intCloseBracket++;
                        }                                                 
                        
                        String strIntlName = vtField.get(i).toString().substring(1, vtField.get(i).toString().length() - 1);

                        if( vtOp.get(i).toString().equals("Is empty") || vtOp.get(i).toString().equals("Is not empty")){

                            // if condition contains EXCLUDING - and also NULL / NOT NULL
                            if( vtJoin.get(i).toString().equals("EXCLUDING")){   

                                query.setWhere( "AND NOT",intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                            }else{
                                 query.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                            }

                        }else if( vtJoin.get(i).toString().equals("EXCLUDING")){ 

                            query.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );                            

                        }else{                            
                            query.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                        }                          


                   }

                   // reset back to blank
                   if( i == 0){
                       vtJoin.set(0, "");
                   }
             }     
           
        }
                            
        // Set the case sensitivity of the dataelement result
        if( runtimeData.getParameter("Sensitivity") != null ){
            query.setCaseSensitive(true);            
        }else{
            query.setCaseSensitive(false);
        }
                              
   }    
   catch (Exception e)
   {
       LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
   }
   
   }
      
   /*
    * Name: getSFCriteriaResults
    * Description: Return the parent keys and the smartform participant key
    * for the database records that match the smartform criteria 
    * Parameters: strParticipantKey - the participant key name e.g. BIOSPECIMEN_intBiospecimenID if a Biospecimen or
    *                                 Bioanalysis smartform
    *             intCriteriaNum - the criteria to obtain results for
    * Returns: a Hashtable containing the participant keys (e.g the Biospecimen keys) and the
               smartform participant keys that matched the criteria
    */   
   private Hashtable getSFCriteriaResults (String strParticipantDomain, String strParticipantKey, Vector vtCriteriaNum)
   {
    Hashtable hashResult = new Hashtable ();
    
    try
    {
       DALQuery[] query = new DALQuery[vtCriteriaNum.size()];              
       
       query[0] =  buildSFConditions (strParticipantDomain, strParticipantKey, Integer.parseInt(vtCriteriaNum.get(0).toString()));
       for (int i=1; i<vtCriteriaNum.size(); i++)
       {
           int intCriteriaNum = Integer.parseInt(vtCriteriaNum.get(i).toString());
           query[i] =  buildSFConditions (strParticipantDomain, strParticipantKey, intCriteriaNum);
           query[i].clearFields();
           query[i].setField("SMARTFORMPARTICIPANTS_intSmartformParticipantID", null);
//           query[0].setSiblingQuery("INTERSECT", query[i], 0, 0);
           query[0].setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID" , "IN", query[i], 0, DALQuery.WHERE_HAS_SUB_QUERY);
       }           

       
       // Set the case sensitivity of the dataelement result
       if( runtimeData.getParameter("Sensitivity") != null ){
           query[0].setCaseSensitive(true);
       }else{
           query[0].setCaseSensitive(false);
       }
       
       // get the resultset consisting of the pariticipant keys and smartform participant keys               
       //System.out.println ("SF Query " + query[0].convertSelectQueryToString());       
       ResultSet rs = query[0].executeSelect();
             
       Vector vtPartKey = new Vector ();
       Vector vtSFPartKey = new Vector ();
       while (rs.next())
       {
          if (!vtPartKey.contains(rs.getString (strParticipantKey)))
          {
              vtPartKey.add (rs.getString (strParticipantKey));
          }    
          
          if (!vtSFPartKey.contains(rs.getString ("SMARTFORMPARTICIPANTS_intSmartformParticipantID")))
          {
              vtSFPartKey.add (rs.getString ("SMARTFORMPARTICIPANTS_intSmartformParticipantID"));
          }              
       }    
       
       // Close the result set
       rs.close();
              
       // Get the comma delimited list of participant keys
       String strPartKeyList = getCommaDelimitedList (vtPartKey);
       
       // Get the comma delimited list of smartform participant keys
       String strSFPartKeyList = getCommaDelimitedList (vtSFPartKey);
       
       hashResult.put(strParticipantKey, strPartKeyList);
       hashResult.put("SMARTFORMPARTICIPANTS_intSmartformParticipantID", strSFPartKeyList);
       
       return hashResult;

    }
    catch(Exception e)
    {  
        LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - BuildQuery" + e.toString(), e);           
    } 
    
    return hashResult;
        
   }  
   
   
    private DALQuery buildSFConditions (String strParticipantDomain, String strParticipantKey, int intCriteria)
    {       
       try
       {            
            DALQuery query1 = new DALQuery();
            boolean blStudyDomainAdded = false;
            
            String  strDomain[] = strParticipantKey.split("_");              
            query1.setDomain(strDomain[0], null, null, null);
            if (strParticipantDomain.equals("PATIENT"))
            {
                query1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                query1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                                  
                if (vtSelectedStudys.isEmpty() == false)
                {
                    query1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                                             
                    blStudyDomainAdded = true;
                }                       
             }
            else if (strParticipantDomain.equals("BIOSPECIMEN"))
            {
                query1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                                      
                if (IntelligentSearch.FILTER_BIOSPECIMEN_BY_STUDY == true)
                {
                    if (vtSelectedStudys.isEmpty() == false)
                    {
                        query1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");                
                        blStudyDomainAdded = true;
                    }    
                }
            }    
            else
            {
                // if bioanalysis domain and searching by study
                if (IntelligentSearch.FILTER_BIOSPECIMEN_BY_STUDY == true)
                {    
                    if ((strParticipantDomain.equals("BIOANALYSIS")) && (vtSelectedStudys.isEmpty() == false))
                    {
                        query1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN"); 
                        blStudyDomainAdded = true;
                    }
                }
                 
                query1.setDomain("SMARTFORMPARTICIPANTS", strParticipantKey, "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");                      
            }    

            query1.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");           
            query1.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
            query1.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID","INNER JOIN");           
            query1.setDomain("SMARTFORMRESULTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "INNER JOIN");

            // Set the form fields
            query1.setField(strParticipantKey, null);               
            query1.setField("SMARTFORMPARTICIPANTS_intSmartformParticipantID", null);
            query1.setWhere( null, 0, "SMARTFORM_strSmartformName", "=", vtDomainVal.get(intCriteria).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
            
            // Convert the domain to correct case for ix_smartform_participants e.g. BIOSPECIMEN to Biospecimen
            String strPartDomain  = vtParticipantDomain.get(intCriteria).toString();
            String strPartDomainUpper = strPartDomain.substring(0, 1);
            String strPartDomainLower = strPartDomain.substring(1).toLowerCase();
            strPartDomain = strPartDomainUpper + strPartDomainLower;
            
            if (strPartDomain.equals ("Patient"))
            {
                strPartDomain = "Study";
            }       

            query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", strPartDomain, 0, DALQuery.WHERE_HAS_VALUE );                            

            // to get the dataelement name              
            query1.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", "SMARTFORMRESULTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
           
            String strOp = "";
            String strSearchVal = vtSearchVal.get(intCriteria).toString();
            int intQueryValue = DALQuery.WHERE_HAS_VALUE;

            strOp = dbOperator(vtOp.get(intCriteria).toString());
            if( strOp.equals("IS NULL") ){
                strOp = "";
                strSearchVal = "IS NULL";    
                intQueryValue = DALQuery.WHERE_HAS_NULL_VALUE;
            }else if( strOp.equals("IS NOT NULL") ){
                strOp = "";
                strSearchVal = "IS NOT NULL";
                intQueryValue = DALQuery.WHERE_HAS_NULL_VALUE;
            }

            String DEId = (String)vtField.get(intCriteria);                    

            query1.setWhere( "AND", 1, "SMARTFORMRESULTS_strDataElementResult", strOp, strSearchVal, 0, intQueryValue);
            query1.setWhere( "AND", 0, "DATAELEMENTS_intDataElementID", "=", DEId, 1, DALQuery.WHERE_HAS_VALUE );                            
            
            // Search by study
            // if study is selected, add it to the where condition
            if (blStudyDomainAdded == true)
            {    
                for( int m=0; m < vtSelectedStudys.size(); m++)
                {
                    int last = vtSelectedStudys.size() - 1;

                    if( vtSelectedStudys.size() == 1){
                        query1.setWhere( "AND", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                    }else if(m == 0){
                        query1.setWhere( "AND", 1, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                    }else if( m == last){
                        query1.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 1, DALQuery.WHERE_HAS_VALUE );                            
                    }else{
                        query1.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                    }          
                }
            }
            
            query1.setWhere ("AND", 0, strDomain[0] + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query1.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query1.setWhere( "AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query1.setWhere( "AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query1.setWhere( "AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            
            if (strParticipantDomain.equals("PATIENT"))
            {
                query1.setWhere( "AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            } 
            
            if (strParticipantDomain.equals("BIOSPECIMEN"))
            {
                query1.setWhere( "AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            }
              
            
            // Set the case sensitivity of the dataelement result
            if( runtimeData.getParameter("Sensitivity") != null ){
                query1.setCaseSensitive(true);
            }else{
                query1.setCaseSensitive(false);
            }
            
            return query1;

       }
       catch (Exception e)
       {
           LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
           return null;
       }
    }  
      
    
    /* Name: getCommaDelimitedList
     * Description: given a vector list, returns a comma delimited list
     *              that can be used in a query
     * Parameters: vtList - the list to be converted
     * Return: String - the comma delimited list enclosed in brackets e.g. (1, 2, 3, etc) 
     */    
    private String getCommaDelimitedList (Vector vtList)
    {        
        String strList = "";
        
        if (!vtList.isEmpty())
        {    
            strList = "(";

            for (int i=0; i<vtList.size(); i++)
            {
                strList += vtList.get(i);

                // Don't put comma for last element in the list
                if (i != (vtList.size()-1))
                {
                    strList += ", ";
                }    
            }    

            strList += ")";
            
        }
        
        return strList;
    }
    
    // for each smartform get the domain
    // to select in smartform participants
    private Hashtable getSmartformDomains ()
    {
        Hashtable hashSFDomains = new Hashtable ();
        String strSFDomain = "";
        Vector vtSF = new Vector ();
        

        try
        {
            for (int i=0; i<vtSelectedSmartforms.size(); i ++)
            {
               // if a criteria has been added for that smartform 
               // set the domain based on the selected participant domain
               if (vtDomainVal.contains( vtSelectedSmartforms.get(i).toString() ))
               {
                   int intCriteria = vtDomainVal.indexOf(vtSelectedSmartforms.get(i).toString());
                   strSFDomain = vtParticipantDomain.get(intCriteria).toString();
               }
               else
               {
                   // if no criteria has been added for the smartform
                   // set the domain based on the domain allocated in ix_smartform
                   // only if this domain has been selected from the dataset
                   // if not default the smartform domain to the PATIENT or BIOSPECIMEN domain
                   DALQuery query = new DALQuery ();
                   query.setDomain("SMARTFORM", null, null, null);  
                   query.setField ("SMARTFORM_strDomain", null);
                   query.setWhere( null, 0, "SMARTFORM_strSmartformName", "=", vtSelectedSmartforms.get(i).toString(), 0, DALQuery.WHERE_HAS_VALUE );
                   query.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                   ResultSet rs = query.executeSelect ();

                   if (rs.next())
                   {
                       if (rs.getString("SMARTFORM_strDomain") != null)
                       {
                           strSFDomain = rs.getString("SMARTFORM_strDomain");
                           strSFDomain = strSFDomain.toUpperCase();
                           if (strSFDomain.equals("STUDY"))
                           {
                              strSFDomain = "PATIENT";
                           }    

                           if (!vtSelectedDomains.contains(strSFDomain))
                           {
                                if (vtSelectedDomains.contains("PATIENT"))
                                    strSFDomain = "PATIENT";

                                else if (vtSelectedDomains.contains("BIOSPECIMEN"))
                                    strSFDomain = "BIOSPECIMEN";
                           }    
                       }    
                   } // end if (rs.next())  
                   
                   //Close the result set
                   rs.close();                   
                   
                   hashNoCriteriaSFDomain.put(vtSelectedSmartforms.get(i), strSFDomain);

               } // end else

               if (!strSFDomain.equals(""))
               {
                   if (hashSFDomains.containsKey(strSFDomain))
                   {                       
                       vtSF = (Vector) hashSFDomains.get(strSFDomain);                      
                       vtSF.add (vtSelectedSmartforms.get(i).toString());
                       hashSFDomains.put (strSFDomain, vtSF);                   
                   }
                   else
                   {
                       Vector vtSF1 = new Vector();
                       vtSF1.add(vtSelectedSmartforms.get(i).toString());
                       hashSFDomains.put (strSFDomain, vtSF1);                   
                   }    
               }    

            } // end for 
            
            //Start Debug            
            /*
            Enumeration key_enum = hashSFDomains.keys();            
            while (key_enum.hasMoreElements())
            {
                String domain = key_enum.nextElement().toString();
                
                if (hashSFDomains.get(domain) != null)
                {
                    Vector vectorSF = (Vector) hashSFDomains.get(domain);
                    for (int k=0; k<vectorSF.size(); k++)
                    {
                        System.out.println("Smartforms" + vectorSF.get(k).toString());
                    }                        
                }                    
            }
            */                     
            // End Debug
        
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);        
        }
         
        
        return hashSFDomains;
    }
    
    // The formfields selected for display are obtained
    private void getDisplayFormfields ()
    {
       // Build the form fields from all the fields selected in criteriadomains - only schema domains
       // And also the dataelements selected for the smartforms           
       vtSelectedDataelements.clear();
       vtSelectedSFDataelements.clear(); 
       
       for (int z=0; z<vtSelectedDomains.size(); z++)
       {
           if (hashCriteriaDomains.containsKey((String) vtSelectedDomains.get(z)))
           {
               String strCriteriaDomain = (String) vtSelectedDomains.get(z);
               Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);
                               
                for( int i=0; i < vtSelectedFields.size(); i++ ){                
                    // Set the selected field to formfield, to be used in search query
                    vtSearchFormFields.add((String)vtSelectedFields.get(i));
                }            
               
           }    
       }    
       
       for (int y=0; y<vtSelectedSmartforms.size(); y++)
       {
           if (hashCriteriaDomains.containsKey((String) vtSelectedSmartforms.get(y)))
           {
               String strCriteriaDomain = (String) vtSelectedSmartforms.get(y);
               Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);
               
                // if domain is a study, dont add Study Name as the formfield            
                for( int j=0; j < vtSelectedFields.size(); j++ ){

                    vtSelectedDataelements.add( (String)vtSelectedFields.get(j) );
                    // Prepend the smartform name to the dataelement, this is to
                    // account for the the same dataelements belonging to different smartforms
                    // This is used when building the XML file for the search results page
                    vtSelectedSFDataelements.add ( (strCriteriaDomain + "__" + (String)vtSelectedFields.get(j)) );                    
                }            
           }    
       }    

       
       // only if datelements are selected show smartform details
       if( vtSelectedDataelements.isEmpty() == false )
       {        
           // it is a smartform so add smartform columns
           // if patient & biospecimen is not selected
           if(!vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN"))
           {
              vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intParticipantID"); 
           }

           if(vtSelectedStudys.isEmpty() == false)
           {               
               vtSearchFormFields.add("STUDY_intStudyID");
               vtSearchFormFields.add("STUDY_strStudyName");
           }    
           vtSearchFormFields.add("SMARTFORM_strSmartformName");
           vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intSmartformParticipantID");       
           vtSearchFormFields.add("DATAELEMENTS_intDataElementID");                  
           vtSearchFormFields.add("DATAELEMENTS_strDataElementName");                     
           vtSearchFormFields.add("SMARTFORMRESULTS_strDataElementResult");
       }    
    }
        
    
    // get the other criteria's for the same smartform that can be grouped with this one
    Vector getCriteriaforSF(int intCriteriaNum, String strSmartform, String strParticipantDomain)
    {
        Vector vtSFCriteria = new Vector ();
        
        // add the current criteria
        vtSFCriteria.add(Integer.toString(intCriteriaNum));
        vtUsedSFCriteria.add(Integer.toString(intCriteriaNum));
        
        for (int i=(intCriteriaNum+1); i<vtDomainVal.size(); i++)
        {
            if (vtJoin.get(i).equals("OR") || (getBracketNo(vtOpenBracket.get(i).toString()) > 0) ) 
            {
                break;
            }    
            else
            {
                // if same smartform and participant and not separated by OR condition or by brackets
                // this criteria may be grouped together
                if (vtDomainVal.get(i).equals(strSmartform) && vtParticipantDomain.get(i).equals(strParticipantDomain))
                {
                    vtSFCriteria.add(Integer.toString(i));    
                    vtUsedSFCriteria.add(Integer.toString(i));
                }
                
                if (getBracketNo(vtCloseBracket.get(i).toString()) > 0)
                {
                    break;
                }    
            }    
        }
        
        
        return vtSFCriteria;
    }
    
}
