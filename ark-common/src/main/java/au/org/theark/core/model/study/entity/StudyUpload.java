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
package au.org.theark.core.model.study.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

/**
 * Upload entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.core.model.study.entity")
@Table(name = "UPLOAD", schema = Constants.STUDY_SCHEMA)
public class StudyUpload implements java.io.Serializable {

	// Fields
	private Long				id;
	private Study				study;
	private FileFormat		fileFormat;
	private DelimiterType	delimiterType;
	private UploadType		uploadType;
	private String				filename;
	private byte[]				payload;
	private String				checksum;
	private Date				startTime;
	private Date				finishTime;
	private byte[]				uploadReport;
	private String				userId;
	private ArkFunction	arkFunction;

	// Constructors
	/** default constructor */
	public StudyUpload() {
	}

	/** minimal constructor */
	public StudyUpload(Long id, FileFormat fileFormat, DelimiterType delimiterType, String filename, byte[] uploadReport, ArkFunction arkFunction) {
		this.id = id;
		this.fileFormat = fileFormat;
		this.delimiterType = delimiterType;
		this.filename = filename;
		this.uploadReport = uploadReport;
		this.arkFunction = arkFunction;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "SubjectUpload_PK_Seq", sequenceName = "STUDY.UPLOAD_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SubjectUpload_PK_Seq")
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
	//	@Fetch(FetchMode.SELECT)
	@Lob
	@Column(name = "PAYLOAD")
	public byte[] getPayload() {
		return this.payload;
	}

	/**
	 * @param payload
	 *           the payload to set
	 */
	public void setPayload(byte[] payload) {
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
	public void setUploadReport(byte[] uploadReport) {
		this.uploadReport = uploadReport;
	}

	/**
	 * @return the uploadReport
	 */
	@Lob
	@Column(name = "UPLOAD_REPORT")
	public byte[] getUploadReport() {
		return uploadReport;
	}

	@Column(name = "USER_ID", nullable = false, length = 100)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_FUNCTION_ID", nullable = false)
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_TYPE_ID", nullable = false)
	public UploadType getUploadType() {
		return uploadType;
	}
	
	public void setUploadType(UploadType uploadType) {
		this.uploadType = uploadType;
	}

	
}
