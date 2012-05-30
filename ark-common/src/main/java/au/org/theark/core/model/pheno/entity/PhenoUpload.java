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
package au.org.theark.core.model.pheno.entity;

import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.Study;

/**
 * Upload entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.core.model.pheno.entity.Upload")
@Table(name = "UPLOAD", schema = Constants.PHENO_TABLE_SCHEMA)
public class PhenoUpload implements java.io.Serializable {


	private Long								id;
	private Study								study;
	private FileFormat						fileFormat;
	private DelimiterType					delimiterType;
	private String								filename;
	private Blob								payload;
	private String								checksum;
	private Date								startTime;
	private Date								finishTime;
	private Blob								uploadReport;
	private String								userId;
	private Date								insertTime;
	private String								updateUserId;
	private Date								updateTime;
	private String								uploadType;

	private Set<PhenoCollectionUpload>	phenoCollectionUploads	= new HashSet<PhenoCollectionUpload>(0);


	public PhenoUpload() {
	}

	public PhenoUpload(Long id, FileFormat fileFormat, DelimiterType delimiterType, String filename, Blob uploadReport) {
		this.id = id;
		this.fileFormat = fileFormat;
		this.delimiterType = delimiterType;
		this.filename = filename;
		this.uploadReport = uploadReport;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "Upload_PK_Seq", sequenceName = "PHENOTYPIC.UPLOAD_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Upload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the study
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	/**
	 * @param study
	 *           the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}

	/**
	 * @return the fileFormat
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID", nullable = false)
	public FileFormat getFileFormat() {
		return this.fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * @param delimiterType
	 *           the delimiterType to set
	 */
	public void setDelimiterType(DelimiterType delimiterType) {
		this.delimiterType = delimiterType;
	}

	/**
	 * @return the delimiterType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELIMITER_TYPE_ID", nullable = false)
	public DelimiterType getDelimiterType() {
		return delimiterType;
	}

	/**
	 * @return the filename
	 */
	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	/**
	 * @param filename
	 *           the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the payload
	 */
	@Column(name = "PAYLOAD")
	public Blob getPayload() {
		return this.payload;
	}

	/**
	 * @param payload
	 *           the payload to set
	 */
	public void setPayload(Blob payload) {
		this.payload = payload;
	}

	/**
	 * @return the checksum
	 */
	@Column(name = "CHECKSUM", nullable = false, length = 50)
	public String getChecksum() {
		return checksum;
	}

	/**
	 * @param checksum
	 *           the checksum to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	/**
	 * @return the startTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", nullable = false)
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *           the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the finishTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME")
	public Date getFinishTime() {
		return finishTime;
	}

	/**
	 * @param finishTime
	 *           the finishTime to set
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	/**
	 * @param uploadReport
	 *           the uploadReport to set
	 */
	public void setUploadReport(Blob uploadReport) {
		this.uploadReport = uploadReport;
	}

	/**
	 * @return the uploadReport
	 */
	@Column(name = "UPLOAD_REPORT")
	public Blob getUploadReport() {
		return uploadReport;
	}

	/**
	 * @return the userId
	 */
	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	/**
	 * @param userId
	 *           the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the insertTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime() {
		return this.insertTime;
	}

	/**
	 * @param insertTime
	 *           the insertTime to set
	 */
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	/**
	 * @return the updateUserId
	 */
	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	/**
	 * @param updateUserId
	 *           the updateUserId to set
	 */
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * @return the updateTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * @param updateTime
	 *           the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @param phenoCollectionUploads
	 *           the phenoCollectionUploads to set
	 */
	public void setPhenoCollectionUploads(Set<PhenoCollectionUpload> phenoCollectionUploads) {
		this.phenoCollectionUploads = phenoCollectionUploads;
	}

	/**
	 * @return the phenoCollectionUploads
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upload")
	public Set<PhenoCollectionUpload> getPhenoCollectionUploads() {
		return phenoCollectionUploads;
	}

	/**
	 * @param uploadType
	 *           the uploadType to set
	 */
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	/**
	 * @return the uploadType
	 */
	@Column(name = "UPLOAD_TYPE")
	public String getUploadType() {
		return uploadType;
	}
}
