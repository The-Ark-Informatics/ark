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
package au.org.theark.core.model.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;

/**
 * 
 * @author smaddumarach
 *
 */
@Entity
@Table(name = "SEARCH_FILE", schema = Constants.REPORT_SCHEMA)
public class SearchFile implements java.io.Serializable {

	private static final long serialVersionUID = -3611814204230766317L;
	private Long id;
	private Study study;
	private Search search;
	private String filename;
	private byte[] payload;
	private String checksum;
	private String userId;
	private String comments;
	private String fileId;

	public SearchFile() {
	}

	public SearchFile(Long id, LinkSubjectStudy linkSubjectStudy,
			StudyComp studyComp, String filename, String userId) {
		this.id = id;
		this.filename = filename;
		this.userId = userId;
	}

	@Id
	@SequenceGenerator(name = "StudyFile_PK_Seq", sequenceName = "STUDYFILE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "StudyFile_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_ID")
	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Transient
	public byte[] getPayload() {
		return this.payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	@Column(name = "CHECKSUM")
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name = "USER_ID", nullable = false, length = 100)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the comments
	 */
	@Column(name = "COMMENTS", length = 500)
	public String getComments() {
		return comments;
	}

	@Column(name = "FILE_ID", length = 1000)
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
}
