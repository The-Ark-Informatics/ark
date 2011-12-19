package au.org.theark.core.dao;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class ArkLdapContextSource extends LdapContextSource {
	private String			basePeopleDn;
	private LdapTemplate ldapTemplate = null;

	public void setBasePeopleDn(String basePeopleDn) {
		this.basePeopleDn = basePeopleDn;
	}

	public String getBasePeopleDn() {
		return basePeopleDn;
	}

	public LdapTemplate getLdapTemplate() {
		if (ldapTemplate == null) {
			ldapTemplate = new LdapTemplate(this); 
		}
		return ldapTemplate;
	}
	
}
