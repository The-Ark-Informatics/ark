package au.org.theark.report.model.vo.report;

import java.io.Serializable;
import java.util.Date;

public class ResearcherDetailCostDataRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String description;
	private Date commencedDate;
	private String invoice;
	private String gst;
	private Integer quantity;
	private Double totalAmount;
	private Double totalGST;
	private String itemType;
	private Long typeId;
	
	public ResearcherDetailCostDataRow() {
	}

	public ResearcherDetailCostDataRow(String description, Date commencedDate,
			String invoice, String gst, Integer quantity, Double totalAmount,
			Double totalGST, String itemType, Long typeId) {
		this.description = description;
		this.commencedDate = commencedDate;
		this.invoice = invoice;
		this.gst = gst;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
		this.totalGST = totalGST;
		this.itemType = itemType;
		this.typeId = typeId;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCommencedDate() {
		return commencedDate;
	}
	public void setCommencedDate(Date commencedDate) {
		this.commencedDate = commencedDate;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getGst() {
		return gst;
	}
	public void setGst(String gst) {
		this.gst = gst;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getTotalGST() {
		return totalGST;
	}
	public void setTotalGST(Double totalGST) {
		this.totalGST = totalGST;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
}
