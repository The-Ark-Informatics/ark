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

/**
 * MetaData entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.MetaData")
@Table(name = "META_DATA", schema = Constants.GENO_TABLE_SCHEMA)
public class MetaData implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long								id;
	private MetaDataField					metaDataField;
	private GenoCollection					collection;
	private String								value;
	private String								userId;
	private Date								insertTime;
	private String								updateUserId;
	private Date								updateTime;
	private Set<SubjectMarkerMetaData>	subjectMarkerMetaDatas	= new HashSet<SubjectMarkerMetaData>(0);
	private Set<MarkerMetaData>			markerMetaDatas			= new HashSet<MarkerMetaData>(0);
	private Set<SubjectMetaData>			subjectMetaDatas			= new HashSet<SubjectMetaData>(0);


	public MetaData() {
	}

	public MetaData(Long id, MetaDataField metaDataField, GenoCollection collection, String userId, Date insertTime) {
		this.id = id;
		this.metaDataField = metaDataField;
		this.collection = collection;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	public MetaData(Long id, MetaDataField metaDataField, GenoCollection collection, String value, String userId, Date insertTime, String updateUserId, Date updateTime,
			Set<SubjectMarkerMetaData> subjectMarkerMetaDatas, Set<MarkerMetaData> markerMetaDatas, Set<SubjectMetaData> subjectMetaDatas) {
		this.id = id;
		this.metaDataField = metaDataField;
		this.collection = collection;
		this.value = value;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.subjectMarkerMetaDatas = subjectMarkerMetaDatas;
		this.markerMetaDatas = markerMetaDatas;
		this.subjectMetaDatas = subjectMetaDatas;
	}

	@Id
	@SequenceGenerator(name = "MetaData_PK_Seq", sequenceName = Constants.META_DATA_PK_SEQ)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MetaData_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "META_DATA_FIELD_ID", nullable = false)
	public MetaDataField getMetaDataField() {
		return this.metaDataField;
	}

	public void setMetaDataField(MetaDataField metaDataField) {
		this.metaDataField = metaDataField;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public GenoCollection getCollection() {
		return this.collection;
	}

	public void setCollection(GenoCollection collection) {
		this.collection = collection;
	}

	@Column(name = "VALUE", length = 2000)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metaData")
	public Set<SubjectMarkerMetaData> getSubjectMarkerMetaDatas() {
		return this.subjectMarkerMetaDatas;
	}

	public void setSubjectMarkerMetaDatas(Set<SubjectMarkerMetaData> subjectMarkerMetaDatas) {
		this.subjectMarkerMetaDatas = subjectMarkerMetaDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metaData")
	public Set<MarkerMetaData> getMarkerMetaDatas() {
		return this.markerMetaDatas;
	}

	public void setMarkerMetaDatas(Set<MarkerMetaData> markerMetaDatas) {
		this.markerMetaDatas = markerMetaDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metaData")
	public Set<SubjectMetaData> getSubjectMetaDatas() {
		return this.subjectMetaDatas;
	}

	public void setSubjectMetaDatas(Set<SubjectMetaData> subjectMetaDatas) {
		this.subjectMetaDatas = subjectMetaDatas;
	}

}
