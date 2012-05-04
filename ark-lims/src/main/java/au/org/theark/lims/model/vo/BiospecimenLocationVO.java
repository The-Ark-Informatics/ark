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
