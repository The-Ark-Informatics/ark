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
	private Double quantity;
	private Double totalAmount;
	private Double totalGST;
	private String itemType;
	private Long typeId;
	private String quantityType;
	private Boolean gstAllowed;
	
	public ResearcherDetailCostDataRow() {
	}

	public ResearcherDetailCostDataRow(String description, Date commencedDate,
			String invoice, String gst, Double quantity, Double totalAmount,
			Double totalGST, String itemType, Long typeId,String quantityType, Boolean gstAllowed) {
		this.description = description;
		this.commencedDate = commencedDate;
		this.invoice = invoice;
		this.gst = gst;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
		this.totalGST = totalGST;
		this.itemType = itemType;
		this.typeId = typeId;
		this.quantityType=quantityType;
		this.gstAllowed=gstAllowed;
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
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
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
	public String getQuantityType() {
		return quantityType;
	}
	public void setQuantityType(String quantityType) {
		this.quantityType = quantityType;
	}
	public Boolean getGstAllowed() {
		return gstAllowed;
	}
	public void setGstAllowed(Boolean gstAllowed) {
		this.gstAllowed = gstAllowed;
	}
}
