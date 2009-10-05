package neuragenix.security;



import java.util.Vector;

import neuragenix.security.exception.SecurityException;

public interface AuthToken

{	

	

	
	
	public String getUserIdentifier();

	

	//public Vector getRolesAndGroups();

	public Vector getGroupsForActivity(String activity) throws SecurityException;

	public String getGroupForActivity(String activity) throws SecurityException;

	public Vector getStudyList();
	public boolean hasRecordAccess(int domain, int intDomainID);
	public boolean hasActivity(String activity) throws SecurityException;
	public boolean hasActivity(String activity, int studyKey) throws SecurityException;
    public boolean hasDenyActivity(String activity) throws SecurityException;
    public Vector<Integer> getSiteList();
    public boolean hasSite(int intSiteKey);
	public String getGroup();

	

	public String getSessionUniqueID();

	

}	

