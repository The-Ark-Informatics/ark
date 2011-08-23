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
package au.org.theark.study.web.component.subjectcustomdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;

public class SubjectCustomDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	protected LinkSubjectStudy linkSubjectStudy;
	protected ArkModule arkModule;
	protected List<SubjectCustomFieldData> subjectCustomFieldDataList;
	
	SubjectCustomDataVO() {
		linkSubjectStudy = new LinkSubjectStudy();
		subjectCustomFieldDataList = new ArrayList<SubjectCustomFieldData>();
	}

	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	public ArkModule getArkModule() {
		return arkModule;
	}

	public void setArkModule(ArkModule arkModule) {
		this.arkModule = arkModule;
	}

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList() {
		return subjectCustomFieldDataList;
	}

	public void setSubjectCustomFieldDataList(List<SubjectCustomFieldData> subjectCustomDataList) {
		this.subjectCustomFieldDataList = subjectCustomDataList;
	}

}
