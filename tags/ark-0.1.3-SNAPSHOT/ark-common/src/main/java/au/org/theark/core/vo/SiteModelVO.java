package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.StudySite;

public class SiteModelVO implements Serializable{
	
	public SiteModelVO(){
		 siteVo = new SiteVO();
		 siteVoList = new ArrayList<SiteVO>();
	}
	/**
	 * Maps to ldap properties
	 */
	private SiteVO siteVo;
	private List<SiteVO> siteVoList;
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	private int mode;

	public SiteVO getSiteVo() {
		return siteVo;
	}

	public void setSiteVo(SiteVO siteVo) {
		this.siteVo = siteVo;
	}

	public List<SiteVO> getSiteVoList() {
		return siteVoList;
	}

	public void setSiteVoList(List<SiteVO> siteVoList) {
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
