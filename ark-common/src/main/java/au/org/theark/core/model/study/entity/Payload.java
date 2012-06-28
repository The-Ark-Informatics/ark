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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * Person entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "payload", schema = Constants.STUDY_SCHEMA)
public class Payload implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long								id;
	private Set<Upload>			uploads					= new HashSet<Upload>(0);
	private byte[] payload;
	public Payload() {
	}

	public Payload(Long id) {
		this.id = id;
	}

	
	
	public Payload(byte[] payload) {
		this.payload = payload;
	}

	@Id
	@SequenceGenerator(name = "payload_generator", sequenceName = "PAYLOAD_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "payload_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "payload")
	public Set<Upload> getUploads() {
		return this.uploads;
	}

	public void setUploads(Set<Upload> uploads) {
		this.uploads = uploads;
	}

	/*
	 * @return the payload
	 */
	//	@Fetch(FetchMode.SELECT)
	@Lob
	@Column(name = "PAYLOAD")
	public byte[] getPayload() {
		return this.payload;
	}

	/**
	 * @param payload
	 *           the payload to set
	 */
	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	
}
