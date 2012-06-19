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

// Generated 15/06/2011 1:22:58 PM by Hibernate Tools 3.3.0.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * Entity that represents the type of column/row in the InvBox entity (Numeric or Alphabet)
 * @author cellis
 *
 */
@Entity
@Table(name = "inv_col_row_type", schema = Constants.LIMS_TABLE_SCHEMA)
public class InvColRowType implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Long	id;
	private String	name;

	public InvColRowType() {
	}

	public InvColRowType(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@SequenceGenerator(name = "colrowtype_generator", sequenceName = "COLROWTYPE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "colrowtype_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}