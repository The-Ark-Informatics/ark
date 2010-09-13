package au.org.theark.study.web.component.site;

import java.io.Serializable;

import au.org.theark.study.model.entity.StudySite;

public class SiteModel implements Serializable{
	
	public SiteModel(){
		
	}
	/**
	 * Maps to ldap properties
	 */
	private String siteName;
	private String siteDescription;
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteDescription() {
		return siteDescription;
	}

	public void setSiteDescription(String siteDescription) {
		this.siteDescription = siteDescription;
	}

	public StudySite getStudySite() {
		return studySite;
	}

	public void setStudySite(StudySite studySite) {
		this.studySite = studySite;
	}
	

	/* Access this instance to get to the site details in database specifically for locating and storing address information. This
	 * is done so we don't duplicate this in ldap at each study level. Having access to the study site name from ldap and a study in context we can 
	 * get the study site and access the address. Yes we do duplicate the site name an description in the database*/
	private StudySite studySite;
	
	

}
