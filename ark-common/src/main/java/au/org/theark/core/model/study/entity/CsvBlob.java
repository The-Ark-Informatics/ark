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
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author cellis
 * 
 */
@Entity
@Table(name = "CSV_BLOB", schema = Constants.STUDY_SCHEMA)
public class CsvBlob implements Serializable {
	private Long		id;
	private Blob		csvBlob;
	
	/** default constructor */
	public CsvBlob() {
		
	}
	
	@Id
	@SequenceGenerator(name = "CSV_BLOB_PK_SEQ", sequenceName = "STUDY.CSV_BLOB_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CSV_BLOB_PK_SEQ")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the csvBlob
	 */
	@Column(name = "CSV_BLOB")
	public Blob getCsvBlob() {
		return csvBlob;
	}
	
	/**
	 * @param csvBlob
	 *           the csvBlob to set
	 */
	public void setCsvBlob(Blob csvBlob) {
		this.csvBlob = csvBlob;
	}	
}
