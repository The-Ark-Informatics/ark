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

import javax.persistence.CascadeType;
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

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;

@Entity
@Table(name = "OtherID", schema = Constants.STUDY_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class OtherID implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Person person;
	private String OtherID_Source;
	private String otherID;

	public OtherID() {
	}

	public OtherID(Long id) {
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "otherID_generator", sequenceName = "OTHERID_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "otherID_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "OtherID_Source")
	public String getOtherID_Source() {
		return this.OtherID_Source;
	}

	public void setOtherID_Source(String OtherID_Source) {
		this.OtherID_Source = OtherID_Source;
	}

	@Column(name = "OtherID", length = 30)
	public String getOtherID() {
		return this.otherID;
	}

	public void setOtherID(String otherID) {
		this.otherID = otherID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PersonID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((OtherID_Source == null) ? 0 : OtherID_Source.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + ((otherID == null) ? 0 : otherID.hashCode());
//		result = prime * result + ((person == null) ? 0 : person.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		OtherID other = (OtherID) obj;
//		if (OtherID_Source == null) {
//			if (other.OtherID_Source != null)
//				return false;
//		} else if (!OtherID_Source.equals(other.OtherID_Source))
//			return false;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		if (otherID == null) {
//			if (other.otherID != null)
//				return false;
//		} else if (!otherID.equals(other.otherID))
//			return false;
//		if (person == null) {
//			if (other.person != null)
//				return false;
//		} else if (!person.equals(other.person))
//			return false;
//		return true;
//	}

	@Override
	public String toString() {
		return "{" + OtherID_Source + " : " + otherID  +"}";
	}
}
