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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * EmailAccountType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EMAIL_ACCOUNT_TYPE", schema = Constants.STUDY_SCHEMA)
public class EmailAccountType implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long					id;
	private String					name;
	private String					description;
	private Set<EmailAccount>	emailAccounts	= new HashSet<EmailAccount>(0);

	// Constructors

	/** default constructor */
	public EmailAccountType() {
	}

	/** minimal constructor */
	public EmailAccountType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EmailAccountType(Long id, String name, String description, Set<EmailAccount> emailAccounts) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.emailAccounts = emailAccounts;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emailAccountType")
	public Set<EmailAccount> getEmailAccounts() {
		return this.emailAccounts;
	}

	public void setEmailAccounts(Set<EmailAccount> emailAccounts) {
		this.emailAccounts = emailAccounts;
	}

}
