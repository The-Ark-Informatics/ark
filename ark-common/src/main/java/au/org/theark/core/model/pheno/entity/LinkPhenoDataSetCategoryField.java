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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
@Entity
@Table(name = "LINK_PHENO_DATASET_CATEGORY_FIELD", schema = Constants.PHENO_TABLE_SCHEMA)
public class LinkPhenoDataSetCategoryField implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Study study;
	private ArkFunction arkFunction;
	private ArkUser arkUser;
	private PhenoDataSetCategory phenoDataSetCategory;
	private PhenoDataSetField phenoDataSetField;
	private Long orderNumber;
	

	@Id
	@SequenceGenerator(name = "link_pheno_dataset_generator", sequenceName = "LINK_PHENO_DATASET_CATEGORY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "link_pheno_dataset_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_FUNCTION_ID")
	public ArkFunction getArkFunction() {
		return arkFunction;
	}
	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}
	@ManyToOne(fetch = FetchType.LAZY)
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
	@JoinColumn(name = "PHENO_DATASET_FIELD_ID")
	public PhenoDataSetField getPhenoDataSetField() {
		return phenoDataSetField;
	}
	public void setPhenoDataSetField(PhenoDataSetField phenoDataSetField) {
		this.phenoDataSetField = phenoDataSetField;
	}
	@Column(name = "ORDER_NUMBER", nullable = false )
	public Long getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arkFunction == null) ? 0 : arkFunction.hashCode());
		result = prime * result + ((arkUser == null) ? 0 : arkUser.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((phenoDataSetCategory == null) ? 0 : phenoDataSetCategory
						.hashCode());
		result = prime
				* result
				+ ((phenoDataSetField == null) ? 0 : phenoDataSetField
						.hashCode());
		result = prime * result + ((study == null) ? 0 : study.hashCode());
		return result;
	}*/
	/*@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkPhenoDataSetCategoryField other = (LinkPhenoDataSetCategoryField) obj;
		if (arkFunction == null) {
			if (other.arkFunction != null)
				return false;
		} else if (!arkFunction.equals(other.arkFunction))
			return false;
		if (arkUser == null) {
			if (other.arkUser != null)
				return false;
		} else if (!arkUser.equals(other.arkUser))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (phenoDataSetCategory == null) {
			if (other.phenoDataSetCategory != null)
				return false;
		} else if (!phenoDataSetCategory.equals(other.phenoDataSetCategory))
			return false;
		if (phenoDataSetField == null) {
			if (other.phenoDataSetField != null)
				return false;
		} else if (!phenoDataSetField.equals(other.phenoDataSetField))
			return false;
		if (study == null) {
			if (other.study != null)
				return false;
		} else if (!study.equals(other.study))
			return false;
		return true;
	}
	*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((phenoDataSetField == null) ? 0 : phenoDataSetField
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
		LinkPhenoDataSetCategoryField other = (LinkPhenoDataSetCategoryField) obj;
		if (phenoDataSetField == null) {
			if (other.phenoDataSetField != null)
				return false;
		} else if (!phenoDataSetField.equals(other.phenoDataSetField))
			return false;
		return true;
	}
	
	
}
