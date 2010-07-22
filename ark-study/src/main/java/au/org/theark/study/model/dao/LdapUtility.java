package au.org.theark.study.model.dao;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.springframework.ldap.core.DirContextOperations;

public class LdapUtility {
	
	public static String buildPersonDN(String userName, String baseDC) throws InvalidNameException {
		LdapName ldapName = new LdapName(baseDC);
		ldapName.add( new Rdn("ou","people"));
		ldapName.add(new Rdn("cn", userName));
		return ldapName.toString();
	}
	
	
	public static void mapToGroupContext(DirContextOperations dirContext,String groupOrRoleName,String[] members){
		dirContext.setAttributeValues("objectClass", new String[]{"groupOfNames","top"});
		dirContext.setAttributeValue("cn", groupOrRoleName);
		dirContext.setAttributeValues("member",members);
	}
	

}
