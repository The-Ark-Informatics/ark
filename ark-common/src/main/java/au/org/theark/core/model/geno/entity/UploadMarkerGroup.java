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

import au.org.theark.core.model.Constants;

/**
 * UploadMarkerGroup entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.UploadMarkerGroup")
@Table(name = "UPLOAD_MARKER_GROUP", schema = Constants.GENO_TABLE_SCHEMA)
public class UploadMarkerGroup implements java.io.Serializable {

	// Fields


	private static final long serialVersionUID = 1L;
	private Long			id;
	private Upload			upload;
	private MarkerGroup	markerGroup;
	private String			userId;
	private String			insertTime;
	private String			updateUserId;
	private String			updateTime;

	// Constructors

	/** default constructor */
	public UploadMarkerGroup() {
	}

	/** minimal constructor */
	public UploadMarkerGroup(Long id, Upload upload, MarkerGroup markerGroup) {
		this.id = id;
		this.upload = upload;
		this.markerGroup = markerGroup;
	}

	/** full constructor */
	public UploadMarkerGroup(Long id, Upload upload, MarkerGroup markerGroup, String userId, String insertTime, String updateUserId, String updateTime) {
		this.id = id;
		this.upload = upload;
		this.markerGroup = markerGroup;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "Upload_Marker_PK_Seq", sequenceName = Constants.UPLOAD_MARKER_PK_SEQ)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Upload_Marker_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID", nullable = false)
	public Upload getUpload() {
		return this.upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARKER_GROUP_ID", nullable = false)
	public MarkerGroup getMarkerGroup() {
		return this.markerGroup;
	}

	public void setMarkerGroup(MarkerGroup markerGroup) {
		this.markerGroup = markerGroup;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "INSERT_TIME")
	public String getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID")
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "UPDATE_TIME")
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
