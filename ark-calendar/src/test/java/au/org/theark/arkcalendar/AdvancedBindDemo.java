package au.org.theark.arkcalendar;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class AdvancedBindDemo {
	 public static void main(String[] args) throws NamingException {

	        if (args.length < 2) {
	            System.err.println("Usage: java AdvancedBindDemo <uid> <password>");
	            System.exit(1);
	        }

	        Hashtable env = new Hashtable();
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	        env.put(Context.PROVIDER_URL, "ldap://ark-ldap:389/");
	        env.put(Context.SECURITY_AUTHENTICATION, "simple");

	        String uid = args[0];
	        String password = args[1];

	        DirContext ctx = null;

	        try {            
	            // Step 1: Bind anonymously            
	            ctx = new InitialDirContext(env);

	            // Step 2: Search the directory
	            String base = "o=LDAP Ark";
	            String filter = "(&(objectClass=inetOrgPerson)(uid={0}))";           
	            SearchControls ctls = new SearchControls();
	            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	            ctls.setReturningAttributes(new String[0]);
	            ctls.setReturningObjFlag(true);
	            NamingEnumeration enm = ctx.search(base, filter, new String[] { uid }, ctls);

	            String dn = null;

	            if (enm.hasMore()) {
	                SearchResult result = (SearchResult) enm.next();
	                dn = result.getNameInNamespace();

	                System.out.println("dn: "+dn);
	            }

	            if (dn == null || enm.hasMore()) {
	                // uid not found or not unique
	                throw new NamingException("Authentication failed");
	            }

	            // Step 3: Bind with found DN and given password
	            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
	            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
	            // Perform a lookup in order to force a bind operation with JNDI
	            ctx.lookup(dn);
	            System.out.println("Authentication successful");

	            enm.close();
	        } catch (NamingException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            ctx.close();
	        }
	    }
}
