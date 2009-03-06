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

	public boolean hasActivity(String activity) throws SecurityException;
        public boolean hasDenyActivity(String activity) throws SecurityException;

	public String getGroup();

	

	public String getSessionUniqueID();

	

}	

