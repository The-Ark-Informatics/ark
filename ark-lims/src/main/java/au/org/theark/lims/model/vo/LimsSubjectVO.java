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
package au.org.theark.lims.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;

/**
 * 
 * @author cellis
 *
 */
@SuppressWarnings("serial")
public class LimsSubjectVO implements Serializable {

	protected Study										study;
	protected SubjectStatus								subjectStatus;
	protected LinkSubjectStudy							linkSubjectStudy;
	protected List<Study>								studyList;
	
	public LimsSubjectVO() {
		this.study = new Study();
		this.subjectStatus = new SubjectStatus();
		this.linkSubjectStudy = new LinkSubjectStudy();
		this.studyList = new ArrayList<Study>(0);
	}
	
	public LimsSubjectVO(Study study, SubjectStatus subjectStatus, LinkSubjectStudy linkSubjectStudy, List<Study> studyList) {
		super();
		this.study = study;
		this.subjectStatus = subjectStatus;
		this.linkSubjectStudy = linkSubjectStudy;
		this.studyList = studyList;
	}
	/**
	 * @return the study
	 */
	public Study getStudy() {
		return study;
	}
	/**
	 * @param study the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}
	/**
	 * @return the subjectStatus
	 */
	public SubjectStatus getSubjectStatus() {
		return subjectStatus;
	}
	/**
	 * @param subjectStatus the subjectStatus to set
	 */
	public void setSubjectStatus(SubjectStatus subjectStatus) {
		this.subjectStatus = subjectStatus;
	}
	/**
	 * @return the linkSubjectStudy
	 */
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}
	/**
	 * @param linkSubjectStudy the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}
	/**
	 * @return the studyList
	 */
	public List<Study> getStudyList() {
		return studyList;
	}
	/**
	 * @param studyList the studyList to set
	 */
	public void setStudyList(List<Study> studyList) {
		this.studyList = studyList;
	}
}
