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
package au.org.theark.lims.model.vo;

import java.io.Serializable;

/**
 * Represents a Biospecimen's complete location details in one VO.
 * 
 * @author nivedan
 * @author cellis
 * 
 */
public class BiospecimenLocationVO implements Serializable {


	private static final long	serialVersionUID	= 1L;
	protected Boolean				isAllocated;
	protected String				siteName;
	protected String				freezerName;
	protected String				rackName;
	protected String				boxName;
	protected Long					row;
	protected Long					column;
	protected String				rowLabel;
	protected String				colLabel;

	public BiospecimenLocationVO() {
		isAllocated = false;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getFreezerName() {
		return freezerName;
	}

	public void setFreezerName(String freezerName) {
		this.freezerName = freezerName;
	}

	public String getRackName() {
		return rackName;
	}

	public void setRackName(String rackName) {
		this.rackName = rackName;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public Long getRow() {
		return row;
	}

	public void setRow(Long row) {
		this.row = row;
	}

	public Long getColumn() {
		return column;
	}

	public void setColumn(Long column) {
		this.column = column;
	}

	public Boolean getIsAllocated() {
		return isAllocated;
	}

	public void setIsAllocated(Boolean isAllocated) {
		this.isAllocated = isAllocated;
	}

	/**
	 * @return the rowLabel
	 */
	public String getRowLabel() {
		return rowLabel;
	}

	/**
	 * @param rowLabel
	 *           the rowLabel to set
	 */
	public void setRowLabel(String rowLabel) {
		this.rowLabel = rowLabel;
	}

	/**
	 * @return the colLabel
	 */
	public String getColLabel() {
		return colLabel;
	}

	/**
	 * @param colLabel
	 *           the colLabel to set
	 */
	public void setColLabel(String colLabel) {
		this.colLabel = colLabel;
	}
}
