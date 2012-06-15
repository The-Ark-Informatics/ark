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
import java.util.Date;

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
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "SUBJECT_CUSTOM_FIELD_DATA", schema = Constants.STUDY_SCHEMA)
public class SubjectCustomFieldData implements Serializable, ICustomFieldData {
	
	private static final long	serialVersionUID	= 1L;
	private Long id;
	private LinkSubjectStudy linkSubjectStudy;
	private CustomFieldDisplay customFieldDisplay;
	private String textDataValue;
	private Date dateDataValue;
	private String errorDataValue;
	private Double numberDataValue;
	
	public SubjectCustomFieldData(){
		
	}
	
	/*
	@Id
	@SequenceGenerator(name = "subject_custom_field_data_generator", sequenceName = "SUBJECT_CUSTOM_FIELD_DATA_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "subject_custom_field_data_generator")
	*/

	@TableGenerator(name = "subject_custom_field_data_generator", 
						table = "study.subjectuid_sequence", 
						pkColumnValue = "NotAStudy.CustomField",
						valueColumnName = "UID_SEQUENCE",
						pkColumnName = "STUDY_NAME_ID",
						initialValue=1000000,
						allocationSize=1000)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "subject_custom_field_data_generator") //@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_DISPLAY_ID")
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_DATA_VALUE")
	public Date getDateDataValue() {
		return dateDataValue;
	}

	public void setDateDataValue(Date dateDataValue) {
		this.dateDataValue = dateDataValue;
	}

	@Column(name = "ERROR_DATA_VALUE")
	public String getErrorDataValue() {
		return errorDataValue;
	}

	public void setErrorDataValue(String errorDataValue) {
		this.errorDataValue = errorDataValue;
	}
	
	@Column(name="NUMBER_DATA_VALUE")
	public Double getNumberDataValue() {
		return numberDataValue;
	}

	public void setNumberDataValue(Double numberDataValue) {
		this.numberDataValue = numberDataValue;
	}

	@Column(name = "TEXT_DATA_VALUE")
	public String getTextDataValue() {
		return textDataValue;
	}

	public void setTextDataValue(String textDataValue) {
		this.textDataValue = textDataValue;
	}
	

}
