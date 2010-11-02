package au.org.theark.study.web.component.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.StudySite;

public class SiteModel implements Serializable{
	
	public SiteModel(){
		 siteVo = new SiteVo();
		 siteVoList = new ArrayList<SiteVo>();
	}
	/**
	 * Maps to ldap properties
	 */
	private SiteVo siteVo;
	private List<SiteVo> siteVoList;
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	private int mode;

	public SiteVo getSiteVo() {
		return siteVo;
	}

	public void setSiteVo(SiteVo siteVo) {
		this.siteVo = siteVo;
	}

	public List<SiteVo> getSiteVoList() {
		return siteVoList;
	}

	public void setSiteVoList(List<SiteVo> siteVoList) {
		this.siteVoList = siteVoList;
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
