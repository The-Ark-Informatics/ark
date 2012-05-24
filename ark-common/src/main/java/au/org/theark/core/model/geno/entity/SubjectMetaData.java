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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * SubjectMetaData entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.SubjectMetaData")
@Table(name = "SUBJECT_META_DATA", schema = Constants.GENO_TABLE_SCHEMA)
public class SubjectMetaData implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long					id;
	private MetaData				metaData;
	private LinkSubjectStudy	subjectId;
	private String					userId;
	private String					insertTime;
	private String					updateUserId;
	private String					updateTime;


	public SubjectMetaData() {
	}

	public SubjectMetaData(Long id, MetaData metaData, LinkSubjectStudy subjectId, String userId, String insertTime) {
		this.id = id;
		this.metaData = metaData;
		this.subjectId = subjectId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	public SubjectMetaData(Long id, MetaData metaData, LinkSubjectStudy subjectId, String userId, String insertTime, String updateUserId, String updateTime) {
		this.id = id;
		this.metaData = metaData;
		this.subjectId = subjectId;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "META_DATA_ID", nullable = false)
	public MetaData getMetaData() {
		return this.metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_ID", nullable = false)
	public LinkSubjectStudy getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(LinkSubjectStudy subjectId) {
		this.subjectId = subjectId;
	}

	@Column(name = "USER_ID", nullable = false)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "INSERT_TIME", nullable = false)
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
