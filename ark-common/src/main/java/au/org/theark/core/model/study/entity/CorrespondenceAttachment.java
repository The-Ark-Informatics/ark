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

import java.io.Serializable;
import java.sql.Blob;

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

import au.org.theark.core.Constants;

@Entity
@Table(name = "correspondence_attachment", schema = Constants.STUDY_SCHEMA)
public class CorrespondenceAttachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long				id;
	private Correspondences	correspondence;
	private String				filename;
	private Blob				payload;
	private String				checksum;
	private String				userId;
	private String				comments;

	@Id
	@SequenceGenerator(name = "correspondence_attachment_generator", sequenceName = "CORRESPONDENCE_GENERATOR_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "correspondence_attachment_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CORRESPONDENCE_ID")
	public Correspondences getCorrespondence() {
		return correspondence;
	}

	public void setCorrespondence(Correspondences correspondence) {
		this.correspondence = correspondence;
	}

	@Column(name = "FILENAME", length = 255)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "PAYLOAD")
	public Blob getPayload() {
		return payload;
	}

	public void setPayload(Blob payload) {
		this.payload = payload;
	}

	@Column(name = "CHECKSUM", length = 50)
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name = "USER_ID", length = 100)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "COMMENTS", length = 500)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
