package au.org.theark.worktracking.util;

import au.org.theark.core.model.worktracking.entity.BillableItem;

public final class BillableItemCostCalculator {
	
	/**
	 * Calculate Billable item cost without GST
	 * @param billableItem
	 * @return Gross total cost
	 */
	private static double calculateItemGrossTotalCost(final BillableItem billableItem){
		double grossTotalCost=0;
		if(billableItem.getItemCost()!=null &&
				billableItem.getItemCost() > 0 &&	
					billableItem.getBillableItemType()!=null &&
						billableItem.getBillableItemType().getQuantityPerUnit() > 0 &&
							billableItem.getQuantity() !=null &&
								billableItem.getQuantity()>0){
			int quantityPerUnit = billableItem.getBillableItemType().getQuantityPerUnit();
			grossTotalCost =  billableItem.getItemCost() * billableItem.getQuantity()/quantityPerUnit;
		}	
		return grossTotalCost;
	}
	
	/**
	 * Calculate Billable Item total cost
	 * @param billableItem
	 * @return total cost.
	 */
	public static double calculateItemCost(final BillableItem billableItem){
		double totalCost= BillableItemCostCalculator.calculateItemGrossTotalCost(billableItem);
		if(totalCost > 0
				&& billableItem.getWorkRequest() !=null
				&& billableItem.getWorkRequest().getGstAllow() !=null
				&& billableItem.getWorkRequest().getGstAllow()
				&& billableItem.getWorkRequest().getGst() !=null){
			double gst= billableItem.getWorkRequest().getGst();			
			totalCost =totalCost *(100+gst)/100;
			totalCost=Math.round(totalCost*100.0)/100.0;
		}
		return totalCost;
	} 

	/**
	 * Calculate Billable item total GST
	 * @param billableItem
	 * @return total GST.
	 */
	public static double calculateItemGST(final BillableItem billableItem){
		double grossTotalCost= BillableItemCostCalculator.calculateItemCost(billableItem);
		double totalGST=0;
		if(grossTotalCost > 0
				&& billableItem.getWorkRequest() !=null
				&& billableItem.getWorkRequest().getGstAllow() !=null
				&& billableItem.getWorkRequest().getGstAllow() 
				&& billableItem.getWorkRequest().getGst() !=null
				&& billableItem.getWorkRequest().getGst() > 0 ){			
			double gst= billableItem.getWorkRequest().getGst();
			totalGST =  grossTotalCost*gst/100;
			totalGST=Math.round(totalGST*100.0)/100.0;
		}
		return totalGST;
	}
}
