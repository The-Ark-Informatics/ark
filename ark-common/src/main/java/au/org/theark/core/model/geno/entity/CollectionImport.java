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
 * CollectionImport entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.CollectionImport")
@Table(name = "COLLECTION_IMPORT", schema = Constants.GENO_TABLE_SCHEMA)
public class CollectionImport implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long				id;
	private ImportType		importType;
	// private DelimiterType delimiterType;
	private MarkerGroup		markerGroup;
	private GenoCollection	collection;
	private Date				startTime;
	private Date				finishTime;
	private String				userId;
	private Date				insertTime;
	private String				updateUserId;
	private Date				updateTime;


	public CollectionImport() {
	}

	public CollectionImport(Long id, ImportType importType,
	/* DelimiterType delimiterType, */MarkerGroup markerGroup, GenoCollection collection, String userId, Date insertTime) {
		this.id = id;
		this.importType = importType;
		// this.delimiterType = delimiterType;
		this.markerGroup = markerGroup;
		this.collection = collection;
//		this.startTime = startTime;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	public CollectionImport(Long id, ImportType importType,
	/* DelimiterType delimiterType, */MarkerGroup markerGroup, GenoCollection collection, Date startTime, Date finishTime, String userId, Date insertTime, String updateUserId, Date updateTime) {
		this.id = id;
		this.importType = importType;
		// this.delimiterType = delimiterType;
		this.markerGroup = markerGroup;
		this.collection = collection;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	@Id
	@SequenceGenerator(name = "Collection_Import_PK_Seq", sequenceName = Constants.COLLECTION_IMPORT_PK_SEQ)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Collection_Import_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPORT_TYPE_ID", nullable = false)
	public ImportType getImportType() {
		return this.importType;
	}

	public void setImportType(ImportType importType) {
		this.importType = importType;
	}

	//
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "DELIMITER_TYPE_ID", nullable = false)
	// public DelimiterType getDelimiterType() {
	// return this.delimiterType;
	// }
	//
	// public void setDelimiterType(DelimiterType delimiterType) {
	// this.delimiterType = delimiterType;
	// }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARKER_GROUP_ID", nullable = false)
	public MarkerGroup getMarkerGroup() {
		return this.markerGroup;
	}

	public void setMarkerGroup(MarkerGroup markerGroup) {
		this.markerGroup = markerGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public GenoCollection getCollection() {
		return this.collection;
	}

	public void setCollection(GenoCollection collection) {
		this.collection = collection;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME")
	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
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

}
