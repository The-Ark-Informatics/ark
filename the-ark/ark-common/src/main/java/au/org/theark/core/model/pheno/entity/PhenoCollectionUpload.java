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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;

/**
 * CollectionUpload entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.core.model.pheno.entity.PhenoCollectionUpload")
@Table(name = "COLLECTION_UPLOAD", schema = Constants.PHENO_TABLE_SCHEMA)
public class PhenoCollectionUpload implements java.io.Serializable {

	// Fields
	private Long				id;
	private PhenoUpload		upload;
	private PhenoCollection	phenoCollection;
	private String				userId;
	private Date				insertTime;
	private String				updateUserId;
	private Date				updateTime;

	// Constructors

	/** default constructor */
	public PhenoCollectionUpload() {
	}

	/** minimal constructor */
	public PhenoCollectionUpload(Long id, PhenoUpload upload, PhenoCollection phenoCollection, String userId, Date insertTime) {
		this.id = id;
		this.upload = upload;
		this.phenoCollection = phenoCollection;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public PhenoCollectionUpload(Long id, PhenoUpload upload, PhenoCollection phenoCollection, String userId, Date insertTime, String updateUserId, Date updateTime) {
		this.id = id;
		this.upload = upload;
		this.phenoCollection = phenoCollection;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "Collection_Upload_PK_Seq", sequenceName = "PHENOTYPIC.COLLECTION_UPLOAD_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Collection_Upload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the upload
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID", nullable = false)
	public PhenoUpload getUpload() {
		return upload;
	}

	/**
	 * @param upload
	 *           the upload to set
	 */
	public void setUpload(PhenoUpload upload) {
		this.upload = upload;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public PhenoCollection getCollection() {
		return this.phenoCollection;
	}

	public void setCollection(PhenoCollection collection) {
		this.phenoCollection = collection;
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
}
