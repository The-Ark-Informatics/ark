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

import java.sql.Blob;
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

import au.org.theark.core.Constants;

/**
 * ConsentFile entity.
 * 
 * @author cellis
 */
@Entity
@Table(name = "CONSENT_FILE", schema = Constants.STUDY_SCHEMA)
public class ConsentFile implements java.io.Serializable {

	private static final long serialVersionUID = -3611814204230766317L;
	private Long id;
	private Consent consent;
	private String filename;
	private Blob payload;
	private String checksum;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;

	public ConsentFile() {
	}

	public ConsentFile(Long id, Consent consent, String filename,
			String userId, Date insertTime) {
		this.id = id;
		this.consent = consent;
		this.filename = filename;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	@Id
	@SequenceGenerator(name = "ConsentFile_PK_Seq", sequenceName = "CONSENTFILE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ConsentFile_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setConsent(Consent consent) {
		this.consent = consent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_ID", nullable = false)
	public Consent getConsent() {
		return consent;
	}

	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	// TODO may want to consider byte[]
	@Column(name = "PAYLOAD")
	public Blob getPayload() {
		return this.payload;
	}

	public void setPayload(Blob payload) {
		this.payload = payload;
	}

	@Column(name = "CHECKSUM")
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name = "USER_ID", nullable = false, length = 100)
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

	@Column(name = "UPDATE_USER_ID", length = 100)
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
