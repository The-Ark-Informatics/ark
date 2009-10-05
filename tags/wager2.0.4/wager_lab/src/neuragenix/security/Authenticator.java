package neuragenix.security;



import neuragenix.security.exception.*;



public interface Authenticator

{



	
	

	public boolean login(String username, String password);

	

	

	public AuthToken getAuthToken() throws SecurityNotLoggedInException;



	/**

	 * Equivalent to login(username, password); getAuthToken();

	 */

	public AuthToken getAuthToken(String username, String password) throws SecurityNotLoggedInException;

}

