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
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;

/**
 * EmailAccount entity. @author MyEclipse Persistence Tools
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "EMAIL_ACCOUNT", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class EmailAccount implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private EmailAccountType emailAccountType;
	private String name;
	private boolean primaryAccount;
	private Person person;
	private EmailStatus emailStatus;

	public EmailAccount() {
	}

	public EmailAccount(Long id) {
		this.id = id;
	}

	public EmailAccount(Long id, EmailAccountType emailAccountType, String name, boolean primaryAccount, Person person, EmailStatus emailStatus) {
		this.id = id;
		this.emailAccountType = emailAccountType;
		this.name = name;
		this.primaryAccount = primaryAccount;
		this.person = person;
		this.emailStatus = emailStatus;
	}

	@Id
	@SequenceGenerator(name = "email_account_generator", sequenceName = "email_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "email_account_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMAIL_ACCOUNT_TYPE_ID")
	public EmailAccountType getEmailAccountType() {
		return this.emailAccountType;
	}

	public void setEmailAccountType(EmailAccountType emailAccountType) {
		this.emailAccountType = emailAccountType;
	}

	@Column(name = "NAME", unique = true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PRIMARY_ACCOUNT", precision = 1, scale = 0)
	public boolean getPrimaryAccount() {
		return this.primaryAccount;
	}

	public void setPrimaryAccount(boolean primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMAIL_STATUS_ID")
	public EmailStatus getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(EmailStatus emailStatus) {
		this.emailStatus = emailStatus;
	}
}
