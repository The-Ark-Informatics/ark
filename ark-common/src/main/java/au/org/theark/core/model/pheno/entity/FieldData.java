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
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * MetaData entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FIELD_DATA", schema = Constants.PHENO_TABLE_SCHEMA)
public class FieldData implements java.io.Serializable {

	private Long					id;
	private PhenoCollection		collection;
	private LinkSubjectStudy	linkSubjectStudy;
	private Date					dateCollected;
	private Field					field;
	private String					value;
	private String					userId;
	private Date					insertTime;
	private String					updateUserId;
	private Date					updateTime;
	private Boolean				passedQualityControl;
	private Set<FieldDataLog>	fieldDataLogs	= new HashSet<FieldDataLog>(0);

	// Constructors
	/** default constructor */
	public FieldData() {
	}

	public FieldData(Long id, Field field, PhenoCollection collection, Date dateCollected, LinkSubjectStudy linkSubjectStudy, String userId, Date insertTime) {
		this.id = id;
		this.field = field;
		this.collection = collection;
		this.dateCollected = dateCollected;
		this.linkSubjectStudy = linkSubjectStudy;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	public FieldData(Long id, Field field, PhenoCollection collection, Date dateCollected, LinkSubjectStudy linkSubjectStudy, String value, String userId, Date insertTime, String updateUserId,
			Date updateTime) {
		this.id = id;
		this.field = field;
		this.collection = collection;
		this.dateCollected = dateCollected;
		this.linkSubjectStudy = linkSubjectStudy;
		this.value = value;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "FieldData_PK_Seq", sequenceName = "PHENOTYPIC.FIELD_DATA_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "FieldData_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FIELD_ID", nullable = false)
	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public PhenoCollection getCollection() {
		return this.collection;
	}

	public void setCollection(PhenoCollection collection) {
		this.collection = collection;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_COLLECTED", nullable = false)
	public Date getDateCollected() {
		return this.dateCollected;
	}

	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}

	@Column(name = "VALUE", length = 2000)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
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

	@Column(name = "UPDATE_USER_ID", length = 50)
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

	/**
	 * @return the linkSubjectStudy
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	/**
	 * @param linkSubjectStudy
	 *           the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fieldData")
	public Set<FieldDataLog> getFieldDataLogs() {
		return fieldDataLogs;
	}

	/**
	 * @param fieldDataLogs
	 *           the fieldDataLogs to set
	 */
	public void setFieldDataLogs(Set<FieldDataLog> fieldDataLogs) {
		this.fieldDataLogs = fieldDataLogs;
	}

	/**
	 * @param passedQualityControl
	 *           the passedQualityControl to set
	 */
	public void setPassedQualityControl(Boolean passedQualityControl) {
		this.passedQualityControl = passedQualityControl;
	}

	/**
	 * @return the passedQualityControl
	 */
	@Column(name = "PASSED_QUALITY_CONTROL")
	public Boolean getPassedQualityControl() {
		return passedQualityControl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collection == null) ? 0 : collection.hashCode());
		result = prime * result + ((dateCollected == null) ? 0 : dateCollected.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((linkSubjectStudy == null) ? 0 : linkSubjectStudy.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldData other = (FieldData) obj;
		if (collection == null) {
			if (other.collection != null)
				return false;
		}
		else if (!collection.equals(other.collection))
			return false;
		if (dateCollected == null) {
			if (other.dateCollected != null)
				return false;
		}
		else if (!dateCollected.equals(other.dateCollected))
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		}
		else if (!field.equals(other.field))
			return false;
		if (linkSubjectStudy == null) {
			if (other.linkSubjectStudy != null)
				return false;
		}
		else if (!linkSubjectStudy.equals(other.linkSubjectStudy))
			return false;
		return true;
	}
}
