package au.org.theark.core.model.report.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

/**
 * 
 * Dummy ResearcherBillableItemTypeCostDataRow table has defined to map the researcher cost report named native query.
 *
 */

@NamedNativeQueries({
	@NamedNativeQuery(
	name = "findTotalBillableItemTypeCostsPerResearcher",
	query = "select bit.id as billableItemTypeId, bit.item_name as costType, IFNULL(x.total_cost,0) as totalCost, IFNULL(x.total_gst,0) as totalGST\n" +
            "from admin.billable_item_type bit\n" +
            "        left outer join \n" +
            "(select bi.billable_type, sum(bi.total_cost) as total_cost, sum(bi.total_gst) as total_gst \n" +
            "from admin.bilable_item bi\n" +
            "        inner join admin.work_request wr on wr.id = bi.request_id\n" +
            "        inner join admin.researcher re on re.id = wr.researcher_id\n" +
            "where bi.invoice = :invoice\n" +
            "         and (bi.commence_date between :fromDate and :toDate) \n" +
            "         and re.id = :researcherId\n" +
            "         and re.study_id = :studyId\n" +
            "group by bi.billable_type) x on bit.id = x.billable_type where bit.study_id =:studyId \n",
        resultClass = ResearcherBillableItemTypeCostDataRow.class
	)
})
@Entity
@Table(name = "ResearcherBillableItemTypeCostDataRow")
public class ResearcherBillableItemTypeCostDataRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long billableItemTypeId;
	private String costType;
	private Double totalCost;
	private Double totalGST;
	
	@Id
	public Long getBillableItemTypeId() {
		return billableItemTypeId;
	}
	public void setBillableItemTypeId(Long billableItemTypeId) {
		this.billableItemTypeId = billableItemTypeId;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Double getTotalGST() {
		return totalGST;
	}
	public void setTotalGST(Double totalGST) {
		this.totalGST = totalGST;
	} 

	
	
}
