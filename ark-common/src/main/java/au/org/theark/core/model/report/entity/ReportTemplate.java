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
package au.org.theark.core.model.report.entity;

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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "REPORT_TEMPLATE", schema = Constants.REPORT_TABLE_SCHEMA)
public class ReportTemplate implements java.io.Serializable {


	private Long			id;
	private String			name;
	private String			description;
	private String			templatePath;
	private ArkModule		module;
	private ArkFunction	function;


	public ReportTemplate() {
	}

	public ReportTemplate(Long id, String name, String templatePath) {
		this.id = id;
		this.name = name;
		this.templatePath = templatePath;
	}

	/**
	 * full constructor
	 * 
	 * @param decodeMasks
	 */
	public ReportTemplate(Long id, String name, String description, String templatePath) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.templatePath = templatePath;
	}

	@Id
	@SequenceGenerator(name = "ReportTemplate_PK_Seq", sequenceName = "REPORTING.REPORT_TEMPLATE_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ReportTemplate_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
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

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "TEMPLATE_PATH", nullable = false, length = 255)
	public String getTemplatePath() {
		return this.templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MODULE_ID")
	public ArkModule getModule() {
		return module;
	}

	public void setModule(ArkModule module) {
		this.module = module;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FUNCTION_ID")
	public ArkFunction getFunction() {
		return function;
	}

	public void setFunction(ArkFunction function) {
		this.function = function;
	}
}
