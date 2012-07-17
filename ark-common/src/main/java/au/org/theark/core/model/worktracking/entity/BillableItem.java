package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	private Integer quantity;
	private Date commenceDate;
	private String type;
	private BillableItemStatus itemStatus;
	private WorkRequest workRequest;
	private String invoice;
	private Long studyId;
	private BillableItemType billableItemType;
	private Set<BillableSubject> billableSubjects =new HashSet<BillableSubject>(0);
	
	public BillableItem() {
	}

	public BillableItem(Long id) {
		this.id = id;
	}

	public BillableItem(Long id, String description, Integer quantity,
			Date commenceDate, String type, BillableItemStatus itemStatus,
			WorkRequest workRequest, String invoice, Long studyId,
			BillableItemType billableItemType) {
		this.id = id;
		this.description = description;
		this.quantity = quantity;
		this.commenceDate = commenceDate;
		this.type = type;
		this.itemStatus = itemStatus;
		this.workRequest = workRequest;
		this.invoice = invoice;
		this.studyId = studyId;
		this.billableItemType = billableItemType;
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
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	public BillableItemStatus getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(BillableItemStatus itemStatus) {
		this.itemStatus = itemStatus;
	}
	
	@ManyToOne()
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

	@ManyToOne()
	@JoinColumn(name="BILLABLE_TYPE")
	public BillableItemType getBillableItemType() {
		return billableItemType;
	}

	public void setBillableItemType(BillableItemType billableItemType) {
		this.billableItemType = billableItemType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billableItem")
	public Set<BillableSubject> getBillableSubjects() {
		return billableSubjects;
	}

	public void setBillableSubjects(Set<BillableSubject> billableSubjects) {
		this.billableSubjects = billableSubjects;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commenceDate == null) ? 0 : commenceDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((invoice == null) ? 0 : invoice.hashCode());
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((studyId == null) ? 0 : studyId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BillableItem [id=" + id + ", description=" + description
				+ ", quantity=" + quantity + ", commenceDate=" + commenceDate
				+ ", type=" + type + ", invoice=" + invoice + ", studyId="
				+ studyId + "]";
	}
	
	
	
	
		
}
