package au.org.theark.core.model.pheno.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.Study;

@Entity
@Table(name = "PHENO_DATASET_FIELD_GROUP", schema = Constants.PHENO_TABLE_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PhenoDataSetGroup  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Study study;
	private Boolean published;
	private ArkFunction arkFunction;
	private Set<PhenoCollection> phenoCollection = new HashSet<PhenoCollection>();

	public PhenoDataSetGroup() {
	
	}
	@Id
	@SequenceGenerator(name = "pheno_dataset_field_group_seq_gen", sequenceName = "PHENO_DATASET_FIELD_GROUP_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pheno_dataset_field_group_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Column(name = "PUBLISHED")
	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_FUNCTION_ID")
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	//TODO: Remove NotAudited when I do pheno auditing
	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "questionnaire")
	public Set<PhenoCollection> getPhenoCollection() {
		return phenoCollection;
	}

	public void setPhenoCollection(Set<PhenoCollection> phenoCollection) {
		this.phenoCollection = phenoCollection;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhenoDataSetGroup other = (PhenoDataSetGroup) obj;
		if(other.getId() == this.getId())
			return true;
		return false;
	}

	

}
