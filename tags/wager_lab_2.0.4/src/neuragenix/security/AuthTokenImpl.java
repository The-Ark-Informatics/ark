package neuragenix.security;



import java.util.*;

import neuragenix.security.exception.SecurityException;

import neuragenix.security.exception.SecurityRetrievingAuthTokenException;

public class AuthTokenImpl implements AuthToken

{	

	private String uid = "Null User";

	private String sessionUID="";

	private Hashtable hashActivities = new Hashtable();
        private Hashtable hashDenyActivities = new Hashtable();



	public AuthTokenImpl()

	{

		super();

		
		sessionUID = String.valueOf(this.hashCode());

	}

	

	public String getUserIdentifier()

	{ 

		return uid; 

	}

	

	//public Vector getRolesAndGroups() {return new Vector();}

	public Vector getGroupsForActivity(String activity) throws SecurityException

	{ 

		if(hasActivity(activity))

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

	public Vector getStudyList()
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

	
	public Vector getSiteList() {
		return null;
	}
	public boolean hasRecordAccess(int domain, int intDomainID)  {
		return true;
		
	}
	
	public boolean hasSite( int siteKey ) { return true ; }
	public boolean hasActivity(String activity, int studyKey) throws SecurityException

	{ 

		// System.err.println("hasActivity? " + activity);

		if(activity == null) throw new SecurityRetrievingAuthTokenException("Null Pointer: No Activity requested");

		return hashActivities.containsKey(activity); 

	}
	
	
	public boolean hasActivity(String activity) throws SecurityException

	{ 

		// System.err.println("hasActivity? " + activity);

		if(activity == null) throw new SecurityRetrievingAuthTokenException("Null Pointer: No Activity requested");

		return hashActivities.containsKey(activity); 

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

	

	protected void addActivity(String activity, String group) throws SecurityException

	{


		if(hasActivity(activity))

		{

			((Vector)hashActivities.get(activity)).add(group);

		}

		else

		{

			Vector vecTmp = new Vector();

			vecTmp.add(group);

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

