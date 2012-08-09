package au.org.theark.report.model.vo.report;

import java.io.Serializable;

public class ResearcherCostDataRow implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String costType;
	private double totalCost;
	private double totalGST;
	
	public ResearcherCostDataRow() {
	}
	public ResearcherCostDataRow(String costType, double totalCost,
			double totalGST) {
		this.costType = costType;
		this.totalCost = totalCost;
		this.totalGST = totalGST;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	public double getTotalGST() {
		return totalGST;
	}
	public void setTotalGST(double totalGST) {
		this.totalGST = totalGST;
	}
	
	
	
	
}
