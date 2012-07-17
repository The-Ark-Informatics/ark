package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "LINK_BILLABLE_ITEM_SUBJECT", schema = Constants.ADMIN_SCHEMA)
public class BillableSubject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long Id;
	private Long subjectId;
	private String description;
	private BillableItem billableItem;
	
	public BillableSubject() {
	}

	public BillableSubject(Long id) {
		Id = id;
	}

	public BillableSubject(Long id, Long subjectId, BillableItem billableItem) {
		Id = id;
		this.subjectId = subjectId;
		this.billableItem = billableItem;
	}

	@Id
	@SequenceGenerator(name = "billable_subject_generator", sequenceName = "BILLABLE_SUBJECT_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "billable_subject_generator")
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	@Column(name = "SUBJECT_ID")
	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	
	@Column(name = "DESCRIPTION",length=255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne()
	@JoinColumn(name="BILLABLE_ITEM_ID", nullable = false)
	public BillableItem getBillableItem() {
		return billableItem;
	}

	public void setBillableItem(BillableItem billableItem) {
		this.billableItem = billableItem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result
				+ ((billableItem == null) ? 0 : billableItem.hashCode());
		result = prime * result
				+ ((subjectId == null) ? 0 : subjectId.hashCode());
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
		BillableSubject other = (BillableSubject) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (billableItem == null) {
			if (other.billableItem != null)
				return false;
		} else if (!billableItem.equals(other.billableItem))
			return false;
		if (subjectId == null) {
			if (other.subjectId != null)
				return false;
		} else if (!subjectId.equals(other.subjectId))
			return false;
		return true;
	}
}
