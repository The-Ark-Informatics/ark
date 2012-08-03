package au.org.theark.worktracking.util;

import au.org.theark.core.model.worktracking.entity.BillableItem;

public final class BillableItemCostCalculator {
	
	public static double calculateItemCost(final BillableItem billableItem){
		double totalCost=0;
		if(billableItem.getItemCost()!=null &&
				billableItem.getItemCost() > 0 &&	
					billableItem.getBillableItemType()!=null &&
						billableItem.getBillableItemType().getQuantityPerUnit() > 0 &&
							billableItem.getQuantity() !=null &&
								billableItem.getQuantity()>0){
			int quantityPerUnit = billableItem.getBillableItemType().getQuantityPerUnit();
			totalCost =  billableItem.getItemCost() * billableItem.getQuantity()/quantityPerUnit;
			totalCost=Math.round(totalCost*100.0)/100.0;
		}
		return totalCost;
	} 

}
