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
package au.org.theark.core.model.lims.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;

@Entity
@Table(name = "access_request", schema = Constants.LIMS_TABLE_SCHEMA)
public class AccessRequest implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private Long		id;
	private String		name;
	private Date		requestDate;
	private Date		requiredDate;
	private String		comments;

	public AccessRequest() {
	}

	public AccessRequest(Long id, String name, Date requestDate) {
		this.id = id;
		this.name = name;
		this.requestDate = requestDate;
	}

	@Id
	@SequenceGenerator(name = "access_request_generator", sequenceName = "ACCESS_REQUEST_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "access_request_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE", nullable = false, length = 19)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUIRED_DATE", length = 19)
	public Date getRequiredDate() {
		return requiredDate;
	}

	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}

	@Column(name = "COMMENTS", length = 100)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
