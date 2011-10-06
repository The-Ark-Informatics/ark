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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.pheno.entity.PhenoData;

/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "CUSTOM_FIELD_DISPLAY", schema = Constants.STUDY_SCHEMA)
public class CustomFieldDisplay implements Serializable{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private Long id;
	private CustomField customField;
	private CustomFieldGroup customFieldGroup;
	private Boolean required;
	private String requiredMessage;
	private Long sequence;
	private Set<SubjectCustomFieldData> subjectCustomFieldData = new HashSet<SubjectCustomFieldData>();
	private Set<BioCollectionCustomFieldData> bioCollectionCustomFieldData = new HashSet<BioCollectionCustomFieldData>();
	private Set<BiospecimenCustomFieldData> biospecimenCustomFieldData = new HashSet<BiospecimenCustomFieldData>();
	private Set<PhenoData> phenoData = new HashSet<PhenoData>();


	public CustomFieldDisplay(){
		
	}


	@Id
	@SequenceGenerator(name = "custom_field_display_seq_gen", sequenceName = "CUSTOM_FIELD_DISPLAY_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_display_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_ID", nullable = false)
	public CustomField getCustomField() {
		return customField;
	}


	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_GROUP_ID")
	public CustomFieldGroup getCustomFieldGroup() {
		return customFieldGroup;
	}


	public void setCustomFieldGroup(CustomFieldGroup customFieldGroup) {
		this.customFieldGroup = customFieldGroup;
	}

	@Column(name = "REQUIRED")
	public Boolean getRequired() {
		return required;
	}


	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Column(name = "REQUIRED_MESSAGE")
	public String getRequiredMessage() {
		return requiredMessage;
	}


	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}

	@Column(name = "SEQUENCE",  precision = 22, scale = 0)
	public Long getSequence() {
		return sequence;
	}


	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<SubjectCustomFieldData> getSubjectCustomFieldData() {
		return subjectCustomFieldData;
	}

	public void setSubjectCustomFieldData(Set<SubjectCustomFieldData> subjectCustomFieldData) {
		this.subjectCustomFieldData = subjectCustomFieldData;
	}

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<BioCollectionCustomFieldData> getBioCollectionCustomFieldData() {
		return bioCollectionCustomFieldData;
	}

	public void setBioCollectionCustomFieldData(Set<BioCollectionCustomFieldData> bioCollectionCustomFieldData) {
		this.bioCollectionCustomFieldData = bioCollectionCustomFieldData;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<BiospecimenCustomFieldData> getBiospecimenCustomFieldData() {
		return biospecimenCustomFieldData;
	}

	public void setBiospecimenCustomFieldData(Set<BiospecimenCustomFieldData> biospecimenCustomFieldData) {
		this.biospecimenCustomFieldData = biospecimenCustomFieldData;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<PhenoData> getPhenoData() {
		return phenoData;
	}

	public void setPhenoData(Set<PhenoData> phenoData) {
		this.phenoData = phenoData;
	}

}
