package au.org.theark.lims.model.vo;

import java.io.Serializable;

/**
 * Represents a Biospecimen's complete location details in one VO.
 * @author nivedan
 *
 */
public class BiospecimenLocationVO implements Serializable{
		
	protected String siteName;
	protected Boolean isAllocated;
	protected String tankName;
	protected String trayName;
	protected String boxName;
	protected Long row;//has rows and cols
	protected Long column;
	
	public BiospecimenLocationVO(){
		
	}
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getTankName() {
		return tankName;
	}

	public void setTankName(String tankName) {
		this.tankName = tankName;
	}

	public String getTrayName() {
		return trayName;
	}

	public void setTrayName(String trayName) {
		this.trayName = trayName;
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

	

}
