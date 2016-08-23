package au.org.theark.core.model.pheno.entity;


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
import javax.persistence.Transient;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
@Entity
@Table(name = "PICKED_PHENO_DATASET_CATEGORY", schema = Constants.PHENO_TABLE_SCHEMA)
public class PickedPhenoDataSetCategory implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Study study;
	private ArkFunction arkFunction;
	private ArkUser arkUser;
	private PhenoDataSetCategory phenoDataSetCategory;
	private PickedPhenoDataSetCategory parentPickedPhenoDataSetCategory;
	private Boolean selected;
	private Long orderNumber;
	private Long displayLevel;
	

	@Id
	@SequenceGenerator(name = "picked_pheno_dataset_generator", sequenceName = "PICKED_PHENO_DATASET_CATEGORY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "picked_pheno_dataset_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_FUNCTION_ID")
	public ArkFunction getArkFunction() {
		return arkFunction;
	}
	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_USER_ID")
	public ArkUser getArkUser() {
		return arkUser;
	}
	public void setArkUser(ArkUser arkUser) {
		this.arkUser = arkUser;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PHENO_DATASET_CATEGORY_ID")
	public PhenoDataSetCategory getPhenoDataSetCategory() {
		return phenoDataSetCategory;
	}
	public void setPhenoDataSetCategory(PhenoDataSetCategory phenoDataSetCategory) {
		this.phenoDataSetCategory = phenoDataSetCategory;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_PHENO_DATASET_CATEGORY_ID")
	public PickedPhenoDataSetCategory getParentPickedPhenoDataSetCategory() {
		return parentPickedPhenoDataSetCategory;
	}
	public void setParentPickedPhenoDataSetCategory(
			PickedPhenoDataSetCategory parentPickedPhenoDataSetCategory) {
		this.parentPickedPhenoDataSetCategory = parentPickedPhenoDataSetCategory;
	}
	@Column(name = "SELECTED", precision = 1, scale = 0)
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	@Column(name = "ORDER_NUMBER", nullable = false )
	public Long getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	@Transient
	public Long getDisplayLevel() {
		return displayLevel;
	}

	public void setDisplayLevel(Long displayLevel) {
		this.displayLevel = displayLevel;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((phenoDataSetCategory == null) ? 0 : phenoDataSetCategory
						.hashCode());
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
		PickedPhenoDataSetCategory other = (PickedPhenoDataSetCategory) obj;
		if (phenoDataSetCategory == null) {
			if (other.phenoDataSetCategory != null)
				return false;
		} else if (!phenoDataSetCategory.equals(other.phenoDataSetCategory))
			return false;
		return true;
	}
	
	

	
}
