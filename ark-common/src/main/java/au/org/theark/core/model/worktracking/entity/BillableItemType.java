package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "BILLABLE_ITEM_TYPE", schema = Constants.ADMIN_SCHEMA)
public class BillableItemType implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long					id;
	private String 					itemName;
	private String					description;
	private Integer					quantityPerUnit;
	private Double 					unitPrice;
	private BillableItemTypeStatus 	billableItemTypeStatus;
	private Long 					studyId;
	private String					type;
	private String					quantityType;
	
	public BillableItemType() {
	}
	
	public BillableItemType(Long id) {
		super();
		this.id = id;
	}

	public BillableItemType(Long id, String itemName, String description,
			Integer quantityPerUnit, Double unitPrice,
			BillableItemTypeStatus billableItemTypeStatus, Long studyId, String type, String quantityType) {
		super();
		this.id = id;
		this.itemName = itemName;
		this.description = description;
		this.quantityPerUnit = quantityPerUnit;
		this.unitPrice = unitPrice;
		this.billableItemTypeStatus = billableItemTypeStatus;
		this.studyId = studyId;
		this.type = type;
		this.quantityType=quantityType;
	}

	@Id
	@SequenceGenerator(name = "billable_item_type_generator", sequenceName = "BILLABLE_ITEM_TYPE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "billable_item_type_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ITEM_NAME", length = 45)
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@Column(name = "DESCRIPTION", length = 255)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "QUANTITY_PER_UNIT")
	public Integer getQuantityPerUnit() {
		return quantityPerUnit;
	}
	
	public void setQuantityPerUnit(Integer quantityPerUnit) {
		this.quantityPerUnit = quantityPerUnit;
	}
	
	@Column(name = "UNIT_PRICE")
	public Double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	public BillableItemTypeStatus getBillableItemTypeStatus() {
		return billableItemTypeStatus;
	}
	
	public void setBillableItemTypeStatus(
			BillableItemTypeStatus billableItemTypeStatus) {
		this.billableItemTypeStatus = billableItemTypeStatus;
	}
	
	@Column(name = "STUDY_ID")
	public Long getStudyId() {
		return studyId;
	}
	
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "QUANTITY_TYPE", length = 45)
	public String getQuantityType() {
		return quantityType;
	}

	public void setQuantityType(String quantityType) {
		this.quantityType = quantityType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((itemName == null) ? 0 : itemName.hashCode());
		result = prime * result
				+ ((quantityPerUnit == null) ? 0 : quantityPerUnit.hashCode());
		result = prime * result
				+ ((quantityType == null) ? 0 : quantityType.hashCode());
		result = prime * result + ((studyId == null) ? 0 : studyId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((unitPrice == null) ? 0 : unitPrice.hashCode());
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
		BillableItemType other = (BillableItemType) obj;
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
		if (itemName == null) {
			if (other.itemName != null)
				return false;
		} else if (!itemName.equals(other.itemName))
			return false;
		if (quantityPerUnit == null) {
			if (other.quantityPerUnit != null)
				return false;
		} else if (!quantityPerUnit.equals(other.quantityPerUnit))
			return false;
		if (quantityType == null) {
			if (other.quantityType != null)
				return false;
		} else if (!quantityType.equals(other.quantityType))
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
		if (unitPrice == null) {
			if (other.unitPrice != null)
				return false;
		} else if (!unitPrice.equals(other.unitPrice))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BillableItemType [id=" + id + ", itemName=" + itemName
				+ ", description=" + description + ", quantityPerUnit="
				+ quantityPerUnit + ", unitPrice=" + unitPrice 
				+ ", studyId=" + studyId + ", type=" + type + ", quantityType="
				+ quantityType + "]";
	}
	
}
