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

import javax.persistence.Column;
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
 * UploadPhenotypicGroup entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Table(name = "FIELD_GROUP_UPLOAD", schema = Constants.PHENO_TABLE_SCHEMA)
public class FieldGroupUpload implements java.io.Serializable {



	private Long			id;
	private PhenoUpload	upload;
	private FieldGroup	fieldGroup;
	private String			userId;
	private String			insertTime;
	private String			updateUserId;
	private String			updateTime;


	public FieldGroupUpload() {
	}

	public FieldGroupUpload(Long id, PhenoUpload upload) {
		this.id = id;
		this.upload = upload;
	}

	public FieldGroupUpload(Long id, PhenoUpload upload, String userId, String insertTime, String updateUserId, String updateTime) {
		this.id = id;
		this.upload = upload;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	@Id
	@SequenceGenerator(name = "Field_Group_Upload_PK_Seq", sequenceName = "PHENOTYPIC.FIELD_GROUP_UPLOAD_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Field_Group_Upload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID", nullable = false)
	public PhenoUpload getUpload() {
		return this.upload;
	}

	public void setUpload(PhenoUpload upload) {
		this.upload = upload;
	}

	/**
	 * @return the fieldGroup
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_GROUP_ID", nullable = false)
	public FieldGroup getFieldGroup() {
		return fieldGroup;
	}

	/**
	 * @param fieldGroup
	 *           the fieldGroup to set
	 */
	public void setFieldGroup(FieldGroup fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	@Column(name = "USER_ID", length = 50)
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

	@Column(name = "UPDATE_USER_ID", length = 50)
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
