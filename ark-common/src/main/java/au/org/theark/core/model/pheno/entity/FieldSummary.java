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
package au.org.theark.core.model.pheno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * fieldField entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FIELD_SUMMARY", schema = Constants.PHENO_TABLE_SCHEMA)
public class FieldSummary implements java.io.Serializable {


	private Long	id;
	private int		fields;
	private int		fieldsWithData;

	// Constructors
	public FieldSummary() {
	}

	// Note: db "table" is actually a view, and id actually = study.id
	@Id
	@SequenceGenerator(name = "FieldSummary_PK_Seq", sequenceName = "PHENOTYPIC.FIELD_SUMMARY_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "FieldSummary_PK_Seq")
	@Column(name = "STUDY_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FIELDS", nullable = false)
	public int getFields() {
		return this.fields;
	}

	public void setFields(int fields) {
		this.fields = fields;
	}

	@Column(name = "FIELDS_WITH_DATA", nullable = false)
	public int getFieldsWithData() {
		return this.fieldsWithData;
	}

	public void setFieldsWithData(int fieldsWithData) {
		this.fieldsWithData = fieldsWithData;
	}
}
