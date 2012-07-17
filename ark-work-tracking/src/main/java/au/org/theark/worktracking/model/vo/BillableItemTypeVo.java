package au.org.theark.worktracking.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.worktracking.entity.BillableItemType;

public class BillableItemTypeVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BillableItemType 			billableItemType;
	
	private List<BillableItemType> 		billableItemTypeList;
	
	private int							mode;
	
	public BillableItemTypeVo() {
		billableItemType=new BillableItemType();
		billableItemTypeList= new ArrayList<BillableItemType>();
	}

	public BillableItemType getBillableItemType() {
		return billableItemType;
	}

	public void setBillableItemType(BillableItemType billableItemType) {
		this.billableItemType = billableItemType;
	}

	public List<BillableItemType> getBillableItemTypeList() {
		return billableItemTypeList;
	}

	public void setBillableItemTypeList(List<BillableItemType> billableItemTypeList) {
		this.billableItemTypeList = billableItemTypeList;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}
