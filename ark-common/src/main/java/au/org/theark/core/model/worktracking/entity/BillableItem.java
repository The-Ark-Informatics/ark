package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

@Entity
@Table(name = "BILABLE_ITEM", schema = Constants.ADMIN_SCHEMA)
public class BillableItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private Long id;
	private String description;
	private Double quantity;
	private Date commenceDate;
	private String type;
	private WorkRequest workRequest;
	private String invoice;
	private Long studyId;
	private BillableItemType billableItemType;
	private Double totalCost;
	private Double itemCost;
	private Double totalGST;

	private String	attachmentFilename;
	private byte[]	attachmentPayload;
		
	public BillableItem() {
	}

	public BillableItem(Long id) {
		this.id = id;
	}

	public BillableItem(Long id, String description, Double quantity,
			Date commenceDate, String type,
			WorkRequest workRequest, String invoice, Long studyId,
			BillableItemType billableItemType, Double totalCost,
			Double itemCost, Double totalGST, 
			String attachmentFilename, byte[] attachmentPayload) {
		super();
		this.id = id;
		this.description = description;
		this.quantity = quantity;
		this.commenceDate = commenceDate;
		this.type = type;
		this.workRequest = workRequest;
		this.invoice = invoice;
		this.studyId = studyId;
		this.billableItemType = billableItemType;
		this.totalCost = totalCost;
		this.itemCost = itemCost;
		this.totalGST = totalGST;
		this.attachmentFilename = attachmentFilename;
		this.attachmentPayload = attachmentPayload;
	}

	@Id
	@SequenceGenerator(name = "billable_item_generator", sequenceName = "BILLABLE_ITEM_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "billable_item_generator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DESCRIPTION", length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "QUANTITY")
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "COMMENCE_DATE", length = 7)
	public Date getCommenceDate() {
		return commenceDate;
	}

	public void setCommenceDate(Date commenceDate) {
		this.commenceDate = commenceDate;
	}

	@Column(name = "TYPE", length = 10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name="REQUEST_ID")
	public WorkRequest getWorkRequest() {
		return workRequest;
	}

	public void setWorkRequest(WorkRequest workRequest) {
		this.workRequest = workRequest;
	}

	@Column(name = "INVOICE", length = 1)
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	@Column(name = "STUDY_ID")
	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	@ManyToOne
	@JoinColumn(name="BILLABLE_TYPE")
	public BillableItemType getBillableItemType() {
		return billableItemType;
	}

	public void setBillableItemType(BillableItemType billableItemType) {
		this.billableItemType = billableItemType;
	}

	@Column(name ="TOTAL_COST")
	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	@Column(name ="ITEM_COST")
	public Double getItemCost() {
		return itemCost;
	}

	public void setItemCost(Double itemCost) {
		this.itemCost = itemCost;
	}
	
	@Column(name = "ATTACHMENT_FILENAME", length = 255)
	public String getAttachmentFilename() {
		return attachmentFilename;
	}

	public void setAttachmentFilename(String attachmentFilename) {
		this.attachmentFilename = attachmentFilename;
	}

	@Lob
	@Column(name = "ATTACHMENT_PAYLOAD")
	public byte[] getAttachmentPayload() {
		return attachmentPayload;
	}

	public void setAttachmentPayload(byte[] attachmentPayload) {
		this.attachmentPayload = attachmentPayload;
	}

	@Column(name = "TOTAL_GST")
	public Double getTotalGST() {
		return totalGST;
	}

	public void setTotalGST(Double totalGST) {
		this.totalGST = totalGST;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((attachmentFilename == null) ? 0 : attachmentFilename
						.hashCode());
		result = prime * result + Arrays.hashCode(attachmentPayload);
		result = prime
				* result
				+ ((billableItemType == null) ? 0 : billableItemType.hashCode());
		result = prime * result
				+ ((commenceDate == null) ? 0 : commenceDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((invoice == null) ? 0 : invoice.hashCode());
		result = prime * result
				+ ((itemCost == null) ? 0 : itemCost.hashCode());
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((studyId == null) ? 0 : studyId.hashCode());
		result = prime * result
				+ ((totalCost == null) ? 0 : totalCost.hashCode());
		result = prime * result
				+ ((totalGST == null) ? 0 : totalGST.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((workRequest == null) ? 0 : workRequest.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BillableItem other = (BillableItem) obj;
		if (attachmentFilename == null) {
			if (other.attachmentFilename != null)
				return false;
		} else if (!attachmentFilename.equals(other.attachmentFilename))
			return false;
		if (!Arrays.equals(attachmentPayload, other.attachmentPayload))
			return false;
		if (billableItemType == null) {
			if (other.billableItemType != null)
				return false;
		} else if (!billableItemType.equals(other.billableItemType))
			return false;
		if (commenceDate == null) {
			if (other.commenceDate != null)
				return false;
		} else if (!commenceDate.equals(other.commenceDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (invoice == null) {
			if (other.invoice != null)
				return false;
		} else if (!invoice.equals(other.invoice))
			return false;
		if (itemCost == null) {
			if (other.itemCost != null)
				return false;
		} else if (!itemCost.equals(other.itemCost))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (studyId == null) {
			if (other.studyId != null)
				return false;
		} else if (!studyId.equals(other.studyId))
			return false;
		if (totalCost == null) {
			if (other.totalCost != null)
				return false;
		} else if (!totalCost.equals(other.totalCost))
			return false;
		if (totalGST == null) {
			if (other.totalGST != null)
				return false;
		} else if (!totalGST.equals(other.totalGST))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (workRequest == null) {
			if (other.workRequest != null)
				return false;
		} else if (!workRequest.equals(other.workRequest))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BillableItem [id=" + id + ", description=" + description
				+ ", quantity=" + quantity + ", commenceDate=" + commenceDate
				+ ", type=" + type + ", workRequest=" + workRequest
				+ ", invoice=" + invoice + ", studyId=" + studyId
				+ ", billableItemType=" + billableItemType + ", totalCost="
				+ totalCost + ", itemCost=" + itemCost + ", totalGST="
				+ totalGST + ", attachmentFilename=" + attachmentFilename
				+ ", attachmentPayload=" + Arrays.toString(attachmentPayload)
				+ "]";
	}
	
}
