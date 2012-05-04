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
package au.org.theark.core.model.geno.entity;

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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import au.org.theark.core.model.Constants;

/**
 * Upload entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.Upload")
@Table(name = "UPLOAD", schema = Constants.GENO_TABLE_SCHEMA)
public class Upload implements java.io.Serializable {

	// Fields


	private static final long serialVersionUID = 1L;
	private Long							id;
	private FileFormat					fileFormat;
	private DelimiterType				delimiterType;
	private String							filename;
	private Set<UploadMarkerGroup>	uploadMarkerGroups	= new HashSet<UploadMarkerGroup>(0);
	private Set<UploadCollection>		uploadCollections		= new HashSet<UploadCollection>(0);
	private Blob							payload;
	private String							userId;
	private Date							insertTime;
	private String							updateUserId;
	private Date							updateTime;

	// Constructors

	/** default constructor */
	public Upload() {
	}

	/** minimal constructor */
	public Upload(Long id, DelimiterType delimiterType, FileFormat fileFormat, String filename, String userId, Date insertTime) {
		this.id = id;
		this.delimiterType = delimiterType;
		this.fileFormat = fileFormat;
		this.filename = filename;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public Upload(Long id, DelimiterType delimiterType, FileFormat fileFormat, String filename, String userId, Date insertTime, Set<UploadMarkerGroup> uploadMarkerGroups,
			Set<UploadCollection> uploadCollections) {
		this.id = id;
		this.delimiterType = delimiterType;
		this.filename = filename;
		this.fileFormat = fileFormat;
		this.userId = userId;
		this.insertTime = insertTime;
		this.uploadMarkerGroups = uploadMarkerGroups;
		this.uploadCollections = uploadCollections;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "Upload_PK_Seq", sequenceName = Constants.UPLOAD_PK_SEQ)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Upload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID", nullable = false)
	public FileFormat getFileFormat() {
		return this.fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELIMITER_TYPE_ID", nullable = false)
	public DelimiterType getDelimiterType() {
		return this.delimiterType;
	}

	public void setDelimiterType(DelimiterType delimiterType) {
		this.delimiterType = delimiterType;
	}

	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Lob
	@Fetch(FetchMode.SELECT)
	@Column(name = "PAYLOAD") //if this doesnt take affect, articles like this; http://stackoverflow.com/questions/2605477/spring-hibernate-blob-lazy-loading recommend adding this ALSO;  @Type(type="org.hibernate.type.PrimitiveByteArrayBlobType")
	public Blob getPayload() {
		return this.payload;
	}

	public void setPayload(Blob payload) {
		this.payload = payload;
	}

	@Column(name = "USER_ID", nullable = false)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID")
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upload")
	public Set<UploadMarkerGroup> getUploadMarkerGroups() {
		return this.uploadMarkerGroups;
	}

	public void setUploadMarkerGroups(Set<UploadMarkerGroup> uploadMarkerGroups) {
		this.uploadMarkerGroups = uploadMarkerGroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upload")
	public Set<UploadCollection> getUploadCollections() {
		return this.uploadCollections;
	}

	public void setUploadCollections(Set<UploadCollection> uploadCollections) {
		this.uploadCollections = uploadCollections;
	}

}
