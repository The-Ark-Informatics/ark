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
package au.org.theark.core.model.lims.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.ICustomFieldData;

/**
 * @author elam
 *
 */

@Entity
@Table(name = "biocollection_custom_field_data", schema = Constants.LIMS_TABLE_SCHEMA)
public class BioCollectionCustomFieldData implements Serializable,  ICustomFieldData {
	

	private static final long	serialVersionUID	= 1L;
	
	private Long id;
	private BioCollection bioCollection;
	private CustomFieldDisplay customFieldDisplay;
	private String textDataValue;
	private Date dateDataValue;
	private Double numberDataValue;
	private String errorDataValue;
	
	/**
	 * Default Constructor
	 */
	public BioCollectionCustomFieldData(){
		
	}

	@Id
	@SequenceGenerator(name = "biocollection_custom_field_data_generator", sequenceName = "BIOCOLLECTION_CUSTOM_FIELD_DATA_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biocollection_custom_field_data_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * NOTE: A Cascade.DELETE on THIS BioCollectionCustomFieldData will occur when a BioCollection is deleted (by database Cascade.DELETE)
	 * @return the parent bioCollection
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIO_COLLECTION_ID")
	public BioCollection getBioCollection() {
		return bioCollection;
	}
	
	public void setBioCollection(BioCollection bioCollection) {
		this.bioCollection = bioCollection;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_DISPLAY_ID")
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	@Column(name = "TEXT_DATA_VALUE")
	public String getTextDataValue() {
		return textDataValue;
	}

	public void setTextDataValue(String textDataValue) {
		this.textDataValue = textDataValue;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_DATA_VALUE")
	public Date getDateDataValue() {
		return dateDataValue;
	}

	public void setDateDataValue(Date dateDataValue) {
		this.dateDataValue = dateDataValue;
	}

	@Column(name="NUMBER_DATA_VALUE")
	public Double getNumberDataValue() {
		return numberDataValue;
	}

	public void setNumberDataValue(Double numberDataValue) {
		this.numberDataValue = numberDataValue;
	}

	@Column(name = "ERROR_DATA_VALUE")
	public String getErrorDataValue() {
		return errorDataValue;
	}

	public void setErrorDataValue(String errorDataValue) {
		this.errorDataValue = errorDataValue;
	}
	
}
