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

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.Transient;

import au.org.theark.core.model.Constants;

/**
 * Entity that represents a box/plate/tray for biospecimen cells Note the hierarchy: site -< tank -< tray -< box -< cell
 * 
 * @author cellis
 */
@Entity
@Table(name = "inv_box", schema = Constants.LIMS_TABLE_SCHEMA)
public class InvBox implements java.io.Serializable, InvTreeNode<InvCell> {

	private Long				id;
	private String				timestamp;
	private InvRack			invRack;
	private Integer			deleted;
	private int					noofcol;
	private InvColRowType	colnotype;
	private Integer			capacity;
	private String				name;
	private Integer			available;
	private int					noofrow;
	private InvColRowType	rownotype;
	private Integer			transferId;
	private int					type;
	private List<InvCell>	invCells	= new ArrayList<InvCell>(0);

	public InvBox() {
	}

	public InvBox(Long id, InvRack invRack, int noofcol, InvColRowType colnotype, int noofrow, InvColRowType rownotype, int type) {
		this.id = id;
		this.invRack = invRack;
		this.noofcol = noofcol;
		this.colnotype = colnotype;
		this.noofrow = noofrow;
		this.rownotype = rownotype;
		this.type = type;
	}

	public InvBox(Long id, InvRack invRack, Integer deleted, int noofcol, InvColRowType colnotype, Integer capacity, String name, Integer available, int noofrow, InvColRowType rownotype,
			Integer transferId, int type, List<InvCell> invCells) {
		this.id = id;
		this.invRack = invRack;
		this.deleted = deleted;
		this.noofcol = noofcol;
		this.colnotype = colnotype;
		this.capacity = capacity;
		this.name = name;
		this.available = available;
		this.noofrow = noofrow;
		this.rownotype = rownotype;
		this.transferId = transferId;
		this.type = type;
		this.invCells = invCells;
	}

	@Id
	@SequenceGenerator(name = "invtray_generator", sequenceName = "INVTRAY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "invtray_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TIMESTAMP", length = 55)
	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRAY_ID", nullable = false)
	public InvRack getInvRack() {
		return this.invRack;
	}

	public void setInvRack(InvRack invRack) {
		this.invRack = invRack;
	}

	@Column(name = "DELETED")
	public Integer getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Column(name = "NOOFCOL", nullable = false)
	public int getNoofcol() {
		return this.noofcol;
	}

	public void setNoofcol(int noofcol) {
		this.noofcol = noofcol;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COLNOTYPE_ID", nullable = false)
	public InvColRowType getColnotype() {
		return this.colnotype;
	}

	public void setColnotype(InvColRowType colnotype) {
		this.colnotype = colnotype;
	}

	@Column(name = "CAPACITY")
	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AVAILABLE")
	public Integer getAvailable() {
		return this.available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	@Column(name = "NOOFROW", nullable = false)
	public int getNoofrow() {
		return this.noofrow;
	}

	public void setNoofrow(int noofrow) {
		this.noofrow = noofrow;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROWNOTYPE_ID", nullable = false)
	public InvColRowType getRownotype() {
		return this.rownotype;
	}

	public void setRownotype(InvColRowType rownotype) {
		this.rownotype = rownotype;
	}

	@Column(name = "TRANSFER_ID")
	public Integer getTransferId() {
		return this.transferId;
	}

	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}

	@Column(name = "TYPE", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invBox")
	public List<InvCell> getInvCells() {
		return this.invCells;
	}

	public void setInvCells(List<InvCell> invCells) {
		this.invCells = invCells;
	}

	@Transient
	public List<InvCell> getChildren() {
		return null;
	}

	@Transient
	public String getNodeName() {
		return getName();
	}

	@Transient
	public String getNodeType() {
		return this.getClass().getCanonicalName();
	}
}
