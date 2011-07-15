package au.org.theark.dao;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;

import au.org.theark.vo.ArkUserVO;

/**
 * A Class that create a user account under ou=arkusers. With a userid, first,last name and password. The intent of this class is to use it as a
 * utility to create ark users who are supposed to be configured in the Ark Database (Ark_User and Ark_User_Role) table as Super Administrators.
 * 
 * @author nivedann
 * 
 */
public class ArkUserDao implements IArkUserDao {

	private LdapTemplate	ldapTemplate;
	private String			basePeopleDn;
	private String			baseDC;

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.genpassword.IArkUserDao#createArkUser(java.lang.String, java.lang.String)
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws InvalidNameException {

		DirContextAdapter dirContextAdapter = new DirContextAdapter();
		// Map the Front end values into LDAP Context
		mapToContext(arkUserVO, dirContextAdapter);
		LdapName ldapName = new LdapName(basePeopleDn);
		ldapName.add(new Rdn("cn", arkUserVO.getArkUserId()));
		Name nameObj = ldapName;
		// Create the person in ArkUsers Group in LDAP
		ldapTemplate.bind(nameObj, dirContextAdapter, null);
	}

	protected void mapToContext(ArkUserVO arkUserVO, DirContextOperations dirCtxOperations) {

		dirCtxOperations.setAttributeValues("objectClass", new String[] { "inetOrgPerson", "organizationalPerson", "person" });
		dirCtxOperations.setAttributeValue("cn", arkUserVO.getArkUserId());
		dirCtxOperations.setAttributeValue("sn", arkUserVO.getLastName());
		dirCtxOperations.setAttributeValue("givenName", arkUserVO.getFirstName());
		dirCtxOperations.setAttributeValue("userPassword", new Sha256Hash(arkUserVO.getPassword()).toHex());// Service will always hash it.
	}

	public String getBasePeopleDn() {
		return basePeopleDn;
	}

	public void setBasePeopleDn(String basePeopleDn) {
		this.basePeopleDn = basePeopleDn;
	}

	public String getBaseDC() {
		return baseDC;
	}

	public void setBaseDC(String baseDC) {
		this.baseDC = baseDC;
	}

	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

}
