package au.org.theark.worktracking.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.vo.ArkVo;

public class BillableItemTypeVo implements ArkVo, Serializable {
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
	
	@Override
	public String getArkVoName(){
		return "Billable Item Type";
	}
}
