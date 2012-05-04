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
@Table(name = "ARK_MODULE_ROLE", schema = Constants.STUDY_SCHEMA)
public class ArkModuleRole implements Serializable {


	private static final long serialVersionUID = 1L;
	private Long		id;
	private ArkModule	arkModule;
	private ArkRole	arkRole;

	public ArkModuleRole() {

	}

	@Id
	@SequenceGenerator(name = "ark_module_role_generator", sequenceName = "ARK_MODULE_ROLE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ark_module_role_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_MODULE_ID")
	public ArkModule getArkModule() {
		return arkModule;
	}

	public void setArkModule(ArkModule arkModule) {
		this.arkModule = arkModule;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_ROLE_ID")
	public ArkRole getArkRole() {
		return arkRole;
	}

	public void setArkRole(ArkRole arkRole) {
		this.arkRole = arkRole;
	}

}
