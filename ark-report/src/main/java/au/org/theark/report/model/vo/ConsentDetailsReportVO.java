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
package au.org.theark.report.model.vo;

import java.util.Date;

import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.StudyComp;

public class ConsentDetailsReportVO extends GenericReportViewVO {


	private static final long	serialVersionUID	= 1L;

	protected LinkSubjectStudy	linkSubjectStudy;
	protected ConsentStatus		consentStatus;
	protected Date					consentDate;
	protected StudyComp			studyComp;

	public ConsentDetailsReportVO() {
		this.linkSubjectStudy = new LinkSubjectStudy();
	}

	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	public ConsentStatus getConsentStatus() {
		return consentStatus;
	}

	public void setConsentStatus(ConsentStatus consentStatus) {
		this.consentStatus = consentStatus;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}

	public StudyComp getStudyComp() {
		return studyComp;
	}

	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
	}

}
