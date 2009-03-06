package neuragenix.security;



import java.sql.ResultSet;
import java.util.*;

import neuragenix.bio.utilities.PatientUtilities;
import neuragenix.dao.DALQuery;
import neuragenix.dao.exception.DAOQueryInvalidField;
import neuragenix.security.exception.SecurityException;

import neuragenix.security.exception.SecurityRetrievingAuthTokenException;







public class WAGERAuthTokenImpl implements AuthToken

{	
	
	public static final int DOMAIN_BIOSPECIMEN = 0;
	public static final int DOMAIN_PATIENT = 1;
	private String uid = "Null User";

	private String sessionUID="";
    private Hashtable studyHash = new Hashtable();
	private Hashtable hashActivities = new Hashtable();
	private Hashtable hashSites = new Hashtable();
    private Hashtable hashDenyActivities = new Hashtable();



	public WAGERAuthTokenImpl()

	{

		super();

		sessionUID = String.valueOf(this.hashCode());

	}

	

	public String getUserIdentifier()

	{ 

		return uid; 

	}

	protected void addSite(String site, Integer siteKey) throws SecurityException

	{


		if(hasActivity(site))

		{

			
		}

		else

		{
			hashSites.put(site, siteKey);

		}

		return;

	}
	

	//public Vector getRolesAndGroups() {return new Vector();}

	public Vector getGroupsForActivity(String activity) throws SecurityException

	{ 

		if(hashActivities.containsKey(activity))

		{

			return (Vector) hashActivities.get(activity); 

		}

		else

		{

			Vector vecTmp = new Vector();

			vecTmp.add("-1");

			return vecTmp;

		}

	}

	

	public String getGroupForActivity(String activity) throws SecurityException

	{

		Vector vecTmp =  getGroupsForActivity(activity);

		if(vecTmp != null && vecTmp.size() > 0)

		{

			return vecTmp.get(0).toString();

		}

		return "-1";

		

	}
	/** getStudyList
	 * Returns: a distinct list of study keys (as Integers) that this authToken permits access (of some kind to)
	 */
	public Vector getStudyList()
	{
		TreeSet hashStudies = new TreeSet();
		Iterator it = hashActivities.values().iterator();

	    while(it.hasNext()){
	    	
	    	Vector v = (Vector) it.next();
	   //	System.err.println("Adding vector of size: " + v.size() );
	       hashStudies.addAll(v);
	    }
		
		return new Vector(hashStudies);		
	}
	
	
	public Vector getStudyList2()
	{
		int i;
		int j=0;
		Vector studylist = new Vector();
		//String []  activities = (String []) hashActivities.keySet.toArray();
		Set keyset = hashActivities.keySet();
		Object []  activities =  keyset.toArray();
		
		for (i=0; i<activities.length; i++) {
			if (((String) activities[i]).indexOf("study_security_") == 0) {
				studylist.add(((String) activities[i]).substring(15));
				System.err.println(((String) activities[i]).substring(15));
				j++;
			}
			
		}
		
		return studylist;
				
	}

	public boolean hasRecordAccess(int domain, int intDomainID)
	{
		int intStudyKey = -1;
		int intInternalPatientID = -1;
		switch (domain)
        {
           case DOMAIN_PATIENT:
              intInternalPatientID = intDomainID;
              break;
           case DOMAIN_BIOSPECIMEN:
              // get the patient key for this biospecimen
              intInternalPatientID = PatientUtilities.getPatientKey(intDomainID);
             
              break;
        }
		try{
		 DALQuery studyQuery = new DALQuery();
		 studyQuery.setField("STUDY_intStudyID", null);
		 
		if (intInternalPatientID != -1)
        {
			
			System.err.println("Internal Pat ID: " + intInternalPatientID);
           studyQuery.setDomain("PATIENT", null, null, null);
           studyQuery.setDomain("CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
           studyQuery.setDomain("CONSENTSTUDY", "CONSENT_intConsentKey", "CONSENTSTUDY_intConsentKey", "INNER JOIN");
           studyQuery.setDomain("STUDY", "CONSENTSTUDY_intStudyID", "STUDY_intStudyID", "INNER JOIN");
           studyQuery.setWhere(null,0, "CONSENTSTUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
           studyQuery.setWhere("AND", 0, "PATIENT_intInternalPatientID", "=", intInternalPatientID + "", 0, DALQuery.WHERE_HAS_VALUE);
           ResultSet rsStudyQuery = studyQuery.executeSelect();
           while (rsStudyQuery.next()) {
        	   intStudyKey = rsStudyQuery.getInt("STUDY_intStudyID");
           }
           rsStudyQuery.close();
           Integer IntStudyKey = new Integer(intStudyKey);
           if (intStudyKey == -1) {
        	   if (domain == DOMAIN_BIOSPECIMEN)  {
   				System.err.println("OK we missed first round tried second.... ");
   			Integer isk = new Integer(neuragenix.bio.utilities.BiospecimenUtilities.getBiospecimenStudy(intDomainID));
   				System.err.println("OK we missed first round tried second.... " + isk);
   				
   			Vector studylist = getStudyList();
   			System.err.print("*** ");
   			for (int i=0; i< studylist.size(); i++) {
   				System.err.print(studylist.get(i)+ " ");
   			}
   			System.err.println("");
   	           return studylist.contains(isk);
   			}
        	   
           }
           System.err.println("Study Key is: " + IntStudyKey);
           Vector studylist = this.getStudyList();
           return studylist.contains(IntStudyKey);
        }
		else {
			if (domain == DOMAIN_BIOSPECIMEN)  {
				System.err.println("OK we missed first round tried second.... ");
			Integer isk = new Integer(neuragenix.bio.utilities.BiospecimenUtilities.getBiospecimenStudy(intDomainID));
				System.err.println("OK we missed first round tried second.... " + isk);
			Vector studylist = this.getStudyList();
	           return studylist.contains(isk);
			}
			
		}
			
		}catch( Exception de) {
			de.printStackTrace(System.err);
			return false;
		}
		
		
		
		return false;
		
	}
	
	
	public boolean hasActivity(String activity) throws SecurityException

	{ 

		// System.err.println("hasActivity? " + activity);

		if(activity == null) throw new SecurityRetrievingAuthTokenException("Null Pointer: No Activity requested");

		return (hashActivities.containsKey(activity));
	}
	
	public boolean hasSite(int intSiteKey)

	{ 

		// System.err.println("hasActivity? " + activity);

		return getSiteList().contains(new Integer(intSiteKey));
	}
	
	public Vector getSiteList() { //Returns list of sitekeys.
		TreeSet hashSiteKeys = new TreeSet();
		Iterator it = hashSites.values().iterator();

	    while(it.hasNext()){
	    	
	    	Integer  i= (Integer)  it.next();

	       hashSiteKeys.add(i);
	    }
		
		return new Vector(hashSiteKeys);		
	}
	
	public boolean hasActivity(String activity, int intStudyKey) throws SecurityException

	{ 

		// System.err.println("hasActivity? " + activity);

		if(activity == null) throw new SecurityRetrievingAuthTokenException("Null Pointer: No Activity requested");

		if (!hashActivities.containsKey(activity)) return false;
		Vector v = getGroupsForActivity(activity);
		return (v.contains(new Integer(intStudyKey)));

	}

        public boolean hasDenyActivity(String activity) throws SecurityException

	{ 

		// System.err.println("hasActivity? " + activity);

		if( activity == null) throw new SecurityRetrievingAuthTokenException("Null Pointer: No Activity requested");

		return hashDenyActivities.containsKey(activity); 

	}
	

	public String getGroup() 

	{

		return (String)null;

	}

	

	public String getSessionUniqueID() 

	{

		return sessionUID;

	}

	

	protected void setUserIdentifier(String strUserID)

	{ 

		uid = strUserID; 

	}

	

	protected void addActivity(String activity, int intStudyKey) throws SecurityException

	{
		Integer intValue = new Integer(intStudyKey);

		if(hasActivity(activity))

		{
			((Vector)hashActivities.get(activity)).add(intValue);
		}

		else

		{
			Vector vecTmp = new Vector();
			vecTmp.add(intValue);
			hashActivities.put(activity, vecTmp);
		}

		return;

	}
        
        protected void addDenyActivity(String activity, String group) throws SecurityException

	{


		if(hasDenyActivity(activity))

		{

			((Vector)hashDenyActivities.get(activity)).add(group);

		}

		else

		{

			Vector vecTmp = new Vector();

			vecTmp.add(group);

			hashDenyActivities.put(activity, vecTmp);

		}

		return;

	}

	

}	

