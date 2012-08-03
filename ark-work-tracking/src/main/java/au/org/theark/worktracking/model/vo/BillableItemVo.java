package au.org.theark.worktracking.model.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.WorkRequest;

public class BillableItemVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BillableItem 		billableItem;
	private List<BillableItem> 	billableItemList;
	private int 				mode;
	private String 				fileName;
	
	private String 				researcherFullName;
	private String				workRequestDescription;
	private String				totalCost;
	
	public BillableItemVo() {
		this.billableItem = new BillableItem();
		this.billableItemList = new ArrayList<BillableItem>();
		this.totalCost="0.00";
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

	public String getResearcherFullName() {
		this.researcherFullName=new String("--");
		
		WorkRequest workRequest = this.billableItem.getWorkRequest();
		
		if(workRequest!=null && workRequest.getResearcher()!=null){
			Researcher researcher=workRequest.getResearcher();
			this.researcherFullName= researcher.getFirstName() +" "+researcher.getLastName();
		}
		return researcherFullName;
	}

	public String getWorkRequestDescription() {
		this.workRequestDescription=new String("--");
		
		WorkRequest workRequest = this.billableItem.getWorkRequest();
		if(workRequest!=null ){
			this.workRequestDescription=workRequest.getName();
		}
		return workRequestDescription;
	}

	public void setResearcherFullName(String researcherFullName) {
		this.researcherFullName = researcherFullName;
	}

	public void setWorkRequestDescription(String workRequestDescription) {
		this.workRequestDescription = workRequestDescription;
	}

	public String getTotalCost() {
		
		if(this.billableItem.getTotalCost() !=null){
			NumberFormat format= new DecimalFormat("#0.00");
			totalCost=format.format(this.billableItem.getTotalCost());
			
		}
		return totalCost;
	}	
	
}
