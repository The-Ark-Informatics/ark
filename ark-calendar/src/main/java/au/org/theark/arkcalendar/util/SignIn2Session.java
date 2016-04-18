package au.org.theark.arkcalendar.util;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import au.org.theark.arkcalendar.dao.ArkCalendarDao;

public class SignIn2Session extends AuthenticatedWebSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Trivial user representation */
	private String user;

	/**
	 * Constructor
	 * 
	 * @param request
	 *            The current request object
	 */
	public SignIn2Session(Request request) {
		super(request);

	}

	/**
	 * Checks the given username and password, returning a User object if if the
	 * username and password identify a valid user.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was authenticated
	 */
	@Override
	public final boolean authenticate(final String username, final String password) {
		
		final String WICKET = "wicket";

		if (user == null) {
			// Trivial password "db"
			// if (WICKET.equalsIgnoreCase(username) &&
			// WICKET.equalsIgnoreCase(password))
			// {
			// user = username;
			// }

			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, System.getProperty("ldap.url"));
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, System.getProperty("ldap.userDn"));
			env.put(Context.SECURITY_CREDENTIALS, System.getProperty("ldap.password"));

			// String user= "arksuperuser@ark.org.au";
			// String password= "password";

			try {
				DirContext ctx = new InitialDirContext(env);

				SearchControls searchControls = new SearchControls();
				searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				searchControls.setTimeLimit(30000);

				NamingEnumeration<?> namingEnum = ctx.search(System.getProperty("ldap.base"), "(objectclass=person)", searchControls);

				while (namingEnum.hasMore()) {

					SearchResult result = (SearchResult) namingEnum.next();
					Attributes attrs = result.getAttributes();
					String cn = attrs.get("cn").toString().split(":")[1].trim();
					// String cn=namingEnum.nextElement().toString();

					if (username.equalsIgnoreCase(cn)) {
						env.put(Context.SECURITY_PRINCIPAL, "cn=" + username + ",ou=" + System.getProperty("ldap.basePeopleDn") + "," + System.getProperty("ldap.base"));
						env.put(Context.SECURITY_CREDENTIALS, new Sha256Hash(password).toHex());
						new InitialDirContext(env);
						user = username;
						signIn(true);
						break;
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
		}

		return user != null;
	}

	/**
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
	 */
	@Override
	public Roles getRoles() {
		if (isSignedIn()) {
			// If the user is signed in, they have these roles
			// return new Roles(Roles.ADMIN);

			return new Roles(ArkCalendarDao.getUserRoles(user));

		}
		return null;
	}

	/**
	 * @return User
	 */
	public String getUser() {
		return user;
	}

	
	

}
