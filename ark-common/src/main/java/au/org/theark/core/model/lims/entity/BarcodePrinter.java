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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.Study;

/**
 * 
 * @author cellis
 *
 */
@Entity
@Table(name = "barcode_printer", schema = Constants.LIMS_TABLE_SCHEMA)
public class BarcodePrinter implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Long		id;
	private Study		study;
	private String		name;
	private String		description;
	private String		location;
	private String		host;
	private String		port;
//	private	String	uniqueName;

	public BarcodePrinter(){
	}
	
	public BarcodePrinter(Long id){
		this.id = id;
	}

	public BarcodePrinter(Long id, Study study, String name, String description, String location, String host, String port) {
		this.id = id;
		this.study = study;
		this.name = name;
		this.description = description;
		this.location = location;
		this.host = host;
		this.port = port;
	}

	@Id
	@SequenceGenerator(name = "barcodeprinter_generator", sequenceName = "BARCODEPRINTER_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "barcodeprinter_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Column(name = "NAME", length = 100, nullable = false)
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

	@Column(name = "LOCATION", length = 100, nullable = false)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "HOST", nullable = false)
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Column(name = "PORT", length = 4)
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	@Transient
	public String getUniqueName() {
		StringBuilder displayExpression = new StringBuilder();
		displayExpression.append(" (");
		displayExpression.append(study.getName());
		displayExpression.append(") ");
		displayExpression.append(name);
		return displayExpression.toString();
	}
}