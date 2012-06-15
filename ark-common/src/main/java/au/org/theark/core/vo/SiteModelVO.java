/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.StudySite;

public class SiteModelVO implements Serializable {


	private static final long serialVersionUID = 1L;

	public SiteModelVO() {
		siteVo = new SiteVO();
		siteVoList = new ArrayList<SiteVO>();
	}

	/**
	 * Maps to ldap properties
	 */
	private SiteVO			siteVo;
	private List<SiteVO>	siteVoList;

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	private int	mode;

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

	/*
	 * Access this instance to get to the site details in database specifically for locating and storing address information. This is done so we don't
	 * duplicate this in ldap at each study level. Having access to the study site name from ldap and a study in context we can get the study site and
	 * access the address. Yes we do duplicate the site name an description in the database
	 */
	private StudySite	studySite;

}
