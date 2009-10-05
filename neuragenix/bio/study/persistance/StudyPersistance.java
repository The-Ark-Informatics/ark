package neuragenix.bio.study.persistance;

import java.util.Vector;

import com.hp.hpl.jena.vocabulary.ResultSet;

import neuragenix.bio.study.Study;
import neuragenix.bio.utilities.StudyUtilities;
import neuragenix.dao.DALQuery;
import neuragenix.dao.DatabaseSchema;

public class StudyPersistance {
    
    public StudyPersistance(){
        
    }
    
    
    public void persistStudy(Study study){
        //delegate the call to StudyUtilties
        StudyUtilities.updateStudy(study);
        
    }
    
    
    public Study getStudyByKey(int key){
        DALQuery query = new DALQuery();
        try 
        {
            query.setWhere(null, 0, "STUDY_intStudyID", "=" , key+"", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND",0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return getStudyDetails(query);
    }
    
    
    public Study getStudyDetails(DALQuery query){
        // return value
        Study resultStudy = new Study();
        Vector formfields = DatabaseSchema.getFormFields("cstudy_study_view_study");
        
        try {
            query.setDomain("STUDY", null,null, null );
            query.setFields(formfields, null);
            
            java.sql.ResultSet rs = query.executeSelect();
            
            
            if (rs.first()){
               resultStudy.setStudyName(rs.getString("STUDY_strStudyName"));
               resultStudy.setEndDate(rs.getDate("STUDY_dtStudyEndDate"));
               resultStudy.setStartDate(rs.getDate("STUDY_dtStudyStartDate"));
               resultStudy.setStudyOwner(rs.getString("STUDY_strStudyOwner"));
               resultStudy.setStudyKey(rs.getInt("STUDY_intStudyID"));
               resultStudy.setStudyCode(rs.getString("STUDY_strStudyCode"));
               resultStudy.setStudyDescription(rs.getString("STUDY_strStudyDescription"));
               resultStudy.setTargetPatientNumber(rs.getInt("STUDY_intTargetPatientNumber"));
               rs.close();
               return resultStudy;
            }
            else return null;
            
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
           
        }
        
        
        
    }

}
