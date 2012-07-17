package au.org.theark.worktracking.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.worktracking.entity.BillableItem;

public class BillableItemVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BillableItem 		billableItem;
	private List<BillableItem> 	billableItemList;
	private int 				mode;
	private String 				fileName;
	
	public BillableItemVo() {
		billableItem = new BillableItem();
		billableItemList=new ArrayList<BillableItem>();
	}
	
	public BillableItem getBillableItem() {
		return billableItem;
	}
	
	public void setBillableItem(BillableItem billableItem) {
		this.billableItem = billableItem;
	}
	
	public List<BillableItem> getBillableItemList() {
		return billableItemList;
	}
	
	public void setBillableItemList(List<BillableItem> billableItemList) {
		this.billableItemList = billableItemList;
	}
	
	public int getMode() {
		return mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
