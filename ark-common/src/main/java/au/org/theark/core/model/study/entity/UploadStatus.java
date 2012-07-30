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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 * 
 */
@Entity
@Table(name = "UPLOAD_STATUS", schema = Constants.STUDY_SCHEMA)
public class UploadStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;
	private String shortMessage;
	private String longMessage;
	private Set<Upload> uploads = new HashSet<Upload>();

	public UploadStatus() {

	}

	public UploadStatus(Long id) {
		this.id = id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	@Column(name = "NAME", length = 25)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SHORT_MESSAGE")
	public String getShortMessage() {
		return this.shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	@Column(name = "LONG_MESSAGE")
	public String getLongMessage() {
		return this.longMessage;
	}

	public void setLongMessage(String longMessage) {
		this.longMessage = longMessage;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "uploadStatus")
	public Set<Upload> getUploads() {
		return this.uploads;
	}

	public void setUploads(Set<Upload> uploads) {
		this.uploads = uploads;
	}

}
