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

import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
/**
 * 
 * @author smaddumarach
 *
 */
@Entity
@Table(name = "PHENO_FIELD_UPLOAD", schema = au.org.theark.core.model.Constants.PHENO_TABLE_SCHEMA)
public class PhenoFieldUpload implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Upload studyUpload;
	private PhenoDataSetField phenoDataSetField;
	public PhenoFieldUpload() {

	}
	@Id
	@SequenceGenerator(name = "PhenoDataSetFieldUpload_PK_Seq", sequenceName = "PHENO.PHENODATASETFIELD_UPLOAD_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PhenoDataSetFieldUpload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID")
	public Upload getUpload() {
		return studyUpload;
	}
	public void setUpload(Upload studyUpload) {
		this.studyUpload = studyUpload;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_FIELD_ID")
	public PhenoDataSetField getPhenoDataSetField() {
		return phenoDataSetField;
	}
	public void setPhenoDataSetField(PhenoDataSetField phenoDataSetField) {
		this.phenoDataSetField = phenoDataSetField;
	}
}
