package au.org.theark.vo;

import java.io.Serializable;

/**
 * @author nivedann
 * 
 */
public class ArkUserVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String	arkUserId;
	private String	firstName;
	private String	lastName;
	private String	password;

	/**
	 * Constructor
	 */
	public ArkUserVO() {
		arkUserId = "";
		firstName = "";
		password = "";
	}

	public String getArkUserId() {
		return arkUserId;
	}

	public void setArkUserId(String arkUserId) {
		this.arkUserId = arkUserId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
