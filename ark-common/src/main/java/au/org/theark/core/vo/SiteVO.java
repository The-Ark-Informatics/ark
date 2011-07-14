package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.List;

public class SiteVO implements Serializable{

	public SiteVO(){
		
	}
	
	private String siteName;
	private String siteDescription;
	private List<String> siteMembers;

	
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
	public List<String> getSiteMembers() {
		return siteMembers;
	}
	public void setSiteMembers(List<String> siteMembers) {
		this.siteMembers = siteMembers;
	}
	
	
}
