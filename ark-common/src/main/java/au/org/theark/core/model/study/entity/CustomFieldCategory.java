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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.tool.hbm2ddl.Target;

import au.org.theark.core.Constants;

/**
 * Study entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CUSTOM_FIELD_CATEGORY", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CustomFieldCategory implements  java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	//private String labelName;
	private String description;
	private CustomFieldType customFieldType;
	private Study study;
	private ArkFunction arkFunction;
	// Parent study link
	private CustomFieldCategory parentCategory;
	private Long orderNumber;
	private Long displayLevel;
	

	public CustomFieldCategory() {
	}

	public CustomFieldCategory(Long id) {
		this.id = id;
	}

	public CustomFieldCategory(Long id, String name,	String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	@Id
	@SequenceGenerator(name = "custom_field_category_generator", sequenceName = "CUSTOM_FIELD_CATEGORY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_category_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", unique = true, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 255)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_TYPE_ID")
	public CustomFieldType getCustomFieldType() {
		return customFieldType;
	}

	public void setCustomFieldType(CustomFieldType customFieldType) {
		this.customFieldType = customFieldType;
	}

	/**
	 * @param parentCategory
	 *            the parentCategory to set
	 */
	public void setParentCategory(CustomFieldCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	/**
	 * @return the parentCategory
	 */
	@Audited
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	public CustomFieldCategory getParentCategory() {
		return parentCategory;
	}
	/**
	 * 
	 * @return the orderNumber
	 */
	@Audited
	@Column(name = "ORDER_NUMBER", nullable = false )
	public Long getOrderNumber() {
		return orderNumber;
	}

	/**
	 * 
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @param study  the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}

	/**
	 * @return the study
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_FUNCTION_ID")
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}
	@Transient
	public Long getDisplayLevel() {
		return displayLevel;
	}

	public void setDisplayLevel(Long displayLevel) {
		this.displayLevel = displayLevel;
	}
}
