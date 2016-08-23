package au.org.theark.arkcalendar;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * Example code for retrieving a Users Primary Group
 * from Microsoft Active Directory via. its LDAP API
 * 
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class LDAPTest {
	
	/**
	 * 
	 * 	URL=ldap://10.10.10.10:389 
	   	LDAP BASE:DC=lab2,DC=ins 
		LDAP Bind Account: CN=Ldap Bind,OU=Service Accounts,OU=TECH,DC=lab2,DC=ins 
		LDAP Bind Account Pw: secret 
	 * 
	 * @param userName
	 * @param userPassword
	 * @return
	 */
	
	private static String LDAP_SERVER="ark-ldap";
	private static String LDAP_SERVER_PORT="389";
	private static String LDAP_BASE_DN="dc=the-ark,dc=org,dc=au";
	private static String LDAP_BIND_DN="cn=admin,dc=the-ark,dc=org,dc=au";
	private static String LDAP_BIND_PASSWORD="password";
	
	
	
	public static Boolean validateLogin(String userName, String userPassword) {
	    Hashtable<String, String> env = new Hashtable<String, String>();


	    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, "ldap://" + LDAP_SERVER + ":" + LDAP_SERVER_PORT + "/" + LDAP_BASE_DN);

	    // To get rid of the PartialResultException when using Active Directory
	    env.put(Context.REFERRAL, "follow");

	    // Needed for the Bind (User Authorized to Query the LDAP server) 
	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env.put(Context.SECURITY_PRINCIPAL, LDAP_BIND_DN);
	    env.put(Context.SECURITY_CREDENTIALS, LDAP_BIND_PASSWORD);

	    DirContext ctx;
	    try {
	       ctx = new InitialDirContext(env);
	    } catch (NamingException e) {
	    	e.printStackTrace();
	       throw new RuntimeException(e);
	    }

	    NamingEnumeration<SearchResult> results = null;

	    try {
	       SearchControls controls = new SearchControls();
	       controls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Search Entire Subtree
	       controls.setCountLimit(1);   //Sets the maximum number of entries to be returned as a result of the search
	       controls.setTimeLimit(5000); // Sets the time limit of these SearchControls in milliseconds

//	       String searchString = "(&(objectCategory=user)(sAMAccountName=" + userName + "))";
	       String searchString = "(&(objectCategory=person)(sAMAccountName=" + userName + "))";

	       results = ctx.search("", searchString, controls);

	       if (results.hasMore()) {

	           SearchResult result = (SearchResult) results.next();
	           Attributes attrs = result.getAttributes();
	           Attribute dnAttr = attrs.get("distinguishedName");
	           String dn = (String) dnAttr.get();

	           // User Exists, Validate the Password

	           env.put(Context.SECURITY_PRINCIPAL, dn);
	           env.put(Context.SECURITY_CREDENTIALS, userPassword);

	           new InitialDirContext(env); // Exception will be thrown on Invalid case
	           return true;
	       } 
	       else 
	           return false;

	    } catch (AuthenticationException e) { // Invalid Login
	    	e.printStackTrace();
	        return false;
	    } catch (NameNotFoundException e) { // The base context was not found.
	    	e.printStackTrace();
	        return false;
	    } catch (SizeLimitExceededException e) {
	    	e.printStackTrace();
	        throw new RuntimeException("LDAP Query Limit Exceeded, adjust the query to bring back less records", e);
	    } catch (NamingException e) {
	    	e.printStackTrace();
	       throw new RuntimeException(e);
	    } finally {

	       if (results != null) {
	          try { results.close(); } catch (Exception e) { /* Do Nothing */ }
	       }

	       if (ctx != null) {
	          try { ctx.close(); } catch (Exception e) { /* Do Nothing */ }
	       }
	    }
	}
	
	
	public static void main2(String[] args) {
		boolean result=LDAPTest.validateLogin("arksuperuser@ark.org.au", "password");
//		boolean result=LDAPTest.validateLogin("tranaweera@unimelb.edu.au", "Sunimal_3");
		System.out.println(result);
	}
	
	
	
	public static void main3(String[] args) {
		Hashtable env = new Hashtable(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//		env.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com");
		env.put(Context.PROVIDER_URL, "ldap://" + LDAP_SERVER + ":" + LDAP_SERVER_PORT + "/" + LDAP_BASE_DN);
//		env.put(Context.SECURITY_AUTHENTICATION, "none");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, LDAP_BIND_DN);
		env.put(Context.SECURITY_CREDENTIALS, LDAP_BIND_PASSWORD);
		
		try {
	        LdapContext ctx = new InitialLdapContext(env, null);
	        ctx.setRequestControls(null);
//	        NamingEnumeration<?> namingEnum = ctx.search("ou=people,dc=example,dc=com", "(objectclass=user)", getSimpleSearchControls());
	        NamingEnumeration<?> namingEnum = ctx.search(LDAP_BASE_DN, "(objectclass=person)", getSimpleSearchControls());
	        while (namingEnum.hasMore ()) {
	            SearchResult result = (SearchResult) namingEnum.next ();    
	            Attributes attrs = result.getAttributes ();
	            System.out.println(attrs.get("cn"));

	        } 
	        namingEnum.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		
	}
	
	
	//This code is working
	public static void main(String[] args) throws Exception {
		String url = "ldap://ark-ldap:389";
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=the-ark,dc=org,dc=au");
		env.put(Context.SECURITY_CREDENTIALS, "password");
		 
		String user= "arksuperuser@ark.org.au";
		String password= "password";
		
		try {
		    DirContext ctx = new InitialDirContext(env);
		    System.out.println("connected");
		    System.out.println(ctx.getEnvironment());
		    
		    NamingEnumeration<?> namingEnum = ctx.search("dc=the-ark,dc=org,dc=au", "(objectclass=person)", getSimpleSearchControls());
		    
		    
	        while (namingEnum.hasMore ()) {
	        	 
	        	
	            SearchResult result = (SearchResult) namingEnum.next ();    
	            Attributes attrs = result.getAttributes ();
	            String cn = attrs.get("cn").toString().split(":")[1].trim();
//	        	String cn=namingEnum.nextElement().toString();
	        	System.out.println(cn);

	        	if(user.equalsIgnoreCase(cn)){
	            	System.out.println(user + " is exist in the system");
//	            	Hashtable authEnv = new Hashtable(11);
//	            	Hashtable authEnv=(Hashtable)env.clone();
//	            	authEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
//	            	authEnv.put(Context.PROVIDER_URL, url);
//	            	authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
	            	env.put(Context.SECURITY_PRINCIPAL, "cn="+user+",ou=arkUsers,dc=the-ark,dc=org,dc=au");
	            	env.put(Context.SECURITY_CREDENTIALS, new Sha256Hash(password).toHex());
	        		new InitialDirContext(env);
	        		System.out.println(user + "has successfully authenticated");
	            }
	        } 
	        namingEnum.close();
		    ctx.close();
		 
		} catch (AuthenticationNotSupportedException ex) {
		    System.out.println("The authentication is not supported by the server");
		} catch (AuthenticationException ex) {
		    System.out.println("incorrect password or username");
		} catch (NamingException ex) {
		    System.out.println("error when trying to create the context");
		} 
		
		System.out.println(new Sha256Hash("DemoUser_1").toHex());
	}
	
	public static void main4(String[] args) {
		String userName = "tranaweera@unimelb.edu.au";
		String passWord = "Sunimal_3";
		String base = "ou=arkUsers,dc=the-ark,dc=org,dc=au";
		Hashtable srchEnv = new Hashtable(11);
		srchEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		srchEnv.put(Context.PROVIDER_URL, "ldap://ark-ldap:389");
		srchEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		srchEnv.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=the-ark,dc=org,dc=au");
		srchEnv.put(Context.SECURITY_CREDENTIALS, "password");
		String[] returnAttribute = {"dn"};
		SearchControls srchControls = new SearchControls();
		srchControls.setReturningAttributes(returnAttribute);
		srchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchFilter = "(cn=" + userName + ")";
		
		try {
		    DirContext srchContext = new InitialDirContext(srchEnv);
		    NamingEnumeration srchResponse = srchContext.search(base, searchFilter, srchControls);
		    // Probably want to test for nulls here
		    String distName = srchResponse.nextElement().toString();
		    System.out.println("DN : " + distName.toString());
		    
		    
		} catch (NamingException namEx) {
		    namEx.printStackTrace();
		}
		
		
		/**
		The second bind actually binds as the user with the password given, so you
		don't need to compare anything.  The directory simply returns 
		authentication success or failure (the latter throws an
		AuthenticationException).  Something like this:
		**/
		
		
		Hashtable authEnv = new Hashtable(11);
		authEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		authEnv.put(Context.PROVIDER_URL, "ldap://ark-ldap:389");
		authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
//		authEnv.put(Context.SECURITY_PRINCIPAL, "cn=arksuperuser@ark.org.au,ou=arkUsers,dc=the-ark,dc=org,dc=au");
		authEnv.put(Context.SECURITY_PRINCIPAL, "cn=tranaweera@unimelb.edu.au,ou=arkUsers,dc=the-ark,dc=org,dc=au");
		authEnv.put(Context.SECURITY_CREDENTIALS, new Sha256Hash("Sunimal_3").toHex());
		try {
		    DirContext authContext = new InitialDirContext(authEnv);
		    System.out.println("Successfully authenticated");
		} catch (AuthenticationException authEx) {
		    System.out.println("Authentication failed!");
		} catch (NamingException namEx) {
		    System.out.println("Something went wrong!");
		    namEx.printStackTrace();
		} 
	}
	
	
	private static SearchControls getSimpleSearchControls() {
	    SearchControls searchControls = new SearchControls();
	    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    searchControls.setTimeLimit(30000);
	    //String[] attrIDs = {"objectGUID"};
	    //searchControls.setReturningAttributes(attrIDs);
	    return searchControls;
	}
	
	
	

    /**
     * @param args the command line arguments
     */
    public static void main1(String[] args) throws NamingException {
        
//        final String ldapAdServer = "ldap://ad.your-server.com:389";
        final String ldapAdServer = "ldap://ark-ldap:389";
//        final String ldapSearchBase = "dc=ad,dc=my-domain,dc=com";
        final String ldapSearchBase = "dc=the-ark,dc=org,dc=au";
        
//        final String ldapUsername = "myLdapUsername";
        final String ldapUsername = "arksuperuser@ark.org.au";
//        final String ldapPassword = "myLdapPassword";
        final String ldapPassword = "password";
        
//        final String ldapAccountToLookup = "myOtherLdapUsername";
        final String ldapAccountToLookup = "tranaweera@unimelb.edu.au";
        
        
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        if(ldapUsername != null) {
            env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
        }
        if(ldapPassword != null) {
            env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        }
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapAdServer);

        //ensures that objectSID attribute values
        //will be returned as a byte[] instead of a String
        env.put("java.naming.ldap.attributes.binary", "objectSID");
        
        // the following is helpful in debugging errors
        //env.put("com.sun.jndi.ldap.trace.ber", System.err);
        
        LdapContext ctx = new InitialLdapContext();
        
        LDAPTest ldap = new LDAPTest();
        
        //1) lookup the ldap account
        SearchResult srLdapUser = ldap.findAccountByAccountName(ctx, ldapSearchBase, ldapAccountToLookup);
        
        //2) get the SID of the users primary group
        String primaryGroupSID = ldap.getPrimaryGroupSID(srLdapUser);
        
        //3) get the users Primary Group
        String primaryGroupName = ldap.findGroupBySID(ctx, ldapSearchBase, primaryGroupSID);
    }
    
    public SearchResult findAccountByAccountName(DirContext ctx, String ldapSearchBase, String accountName) throws NamingException {

        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + accountName + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);

        SearchResult searchResult = null;
        if(results.hasMoreElements()) {
             searchResult = (SearchResult) results.nextElement();

            //make sure there is not another item available, there should be only 1 match
            if(results.hasMoreElements()) {
                System.err.println("Matched multiple users for the accountName: " + accountName);
                return null;
            }
        }
        
        return searchResult;
    }
    
    public String findGroupBySID(DirContext ctx, String ldapSearchBase, String sid) throws NamingException {
        
        String searchFilter = "(&(objectClass=group)(objectSid=" + sid + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);

        if(results.hasMoreElements()) {
            SearchResult searchResult = (SearchResult) results.nextElement();

            //make sure there is not another item available, there should be only 1 match
            if(results.hasMoreElements()) {
                System.err.println("Matched multiple groups for the group with SID: " + sid);
                return null;
            } else {
                return (String)searchResult.getAttributes().get("sAMAccountName").get();
            }
        }
        return null;
    }
    
    public String getPrimaryGroupSID(SearchResult srLdapUser) throws NamingException {
        byte[] objectSID = (byte[])srLdapUser.getAttributes().get("objectSid").get();
        String strPrimaryGroupID = (String)srLdapUser.getAttributes().get("primaryGroupID").get();
        
        String strObjectSid = decodeSID(objectSID);
        
        return strObjectSid.substring(0, strObjectSid.lastIndexOf('-') + 1) + strPrimaryGroupID;
    }
    
    /**
     * The binary data is in the form:
     * byte[0] - revision level
     * byte[1] - count of sub-authorities
     * byte[2-7] - 48 bit authority (big-endian)
     * and then count x 32 bit sub authorities (little-endian)
     * 
     * The String value is: S-Revision-Authority-SubAuthority[n]...
     * 
     * Based on code from here - http://forums.oracle.com/forums/thread.jspa?threadID=1155740&tstart=0
     */
    public static String decodeSID(byte[] sid) {
        
        final StringBuilder strSid = new StringBuilder("S-");

        // get version
        final int revision = sid[0];
        strSid.append(Integer.toString(revision));
        
        //next byte is the count of sub-authorities
        final int countSubAuths = sid[1] & 0xFF;
        
        //get the authority
        long authority = 0;
        //String rid = "";
        for(int i = 2; i <= 7; i++) {
           authority |= ((long)sid[i]) << (8 * (5 - (i - 2)));
        }
        strSid.append("-");
        strSid.append(Long.toHexString(authority));
        
        //iterate all the sub-auths
        int offset = 8;
        int size = 4; //4 bytes for each sub auth
        for(int j = 0; j < countSubAuths; j++) {
            long subAuthority = 0;
            for(int k = 0; k < size; k++) {
                subAuthority |= (long)(sid[offset + k] & 0xFF) << (8 * k);
            }
            
            strSid.append("-");
            strSid.append(subAuthority);
            
            offset += size;
        }
        
        return strSid.toString();    
    }
}