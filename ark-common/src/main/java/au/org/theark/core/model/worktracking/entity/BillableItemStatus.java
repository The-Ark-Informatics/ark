package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "BILLABLE_ITEM__STATUS", schema = Constants.ADMIN_SCHEMA)
public class BillableItemStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long			id;
	private String			name;
	private String			description;
	private Set<BillableItem> billableItems = new HashSet<BillableItem>();
	
	public BillableItemStatus() {
	}

	public BillableItemStatus(Long id) {
		this.id = id;
	}

	public BillableItemStatus(Long id, String name, String description,
			Set<BillableItem> billableItems) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.billableItems = billableItems;
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 25)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemStatus")
	public Set<BillableItem> getBillableItems() {
		return billableItems;
	}

	public void setBillableItems(Set<BillableItem> billableItems) {
		this.billableItems = billableItems;
	}
	
}
