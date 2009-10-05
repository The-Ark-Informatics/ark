package neuragenix.security;



import java.util.*;

import neuragenix.security.exception.SecurityException;

public class NullAuthToken implements AuthToken

{	

	

	public String getUserIdentifier(){ return "Null User"; }

	

	//public Vector getRolesAndGroups() {return new Vector();}

	public Vector getGroupsForActivity(String activity){ return new Vector(); }

	public String getGroupForActivity(String activity){ return "-1"; }

	public boolean hasActivity(String activity ) { return true ; }
	
	public boolean hasRecordAccess(int domain, int intDomainID) {
		return true;
		
	}

	public boolean hasActivity(String activity, int studyKey ) { return true ; }
	public boolean hasSite( int siteKey ) { return true ; }
	
	public String getGroup() { return "-1"; }

	public Vector getSiteList() {
		return null;
	}
	public Vector getStudyList() {
		return null;
	}
	public String getSessionUniqueID() { return "-1"; }

	public boolean hasDenyActivity(String activity) throws SecurityException
        {
            return false;
        }

}	

