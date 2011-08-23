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

import javax.persistence.*;

import au.org.theark.core.Constants;

@Entity
@Table(name = "ETA_USER", schema = Constants.STUDY_SCHEMA)
public class EtaUser implements Serializable {

	private Long				id;
	private String				userName;
	private String				userPassword;
	private Set<UserRoles>	userRoles	= new HashSet<UserRoles>();

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	@Column(name = "USER_NAME", length = 100)
	public String getUserName() {
		return userName;
	}

	@Column(name = "USER_PASSWORD", length = 250)
	public String getUserPassword() {
		return userPassword;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "etaUser")
	public Set<UserRoles> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Default constructor
	 */
	public EtaUser() {

	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name ");
		sb.append(this.getUserName());
		sb.append("Id");
		sb.append(this.getId());
		return sb.toString();

	}

}
