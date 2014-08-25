package au.org.theark.core.model.disease.entity;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.Study;

@Entity
@Table(name="DISEASE", schema=Constants.DISEASE_SCHEMA)
public class Disease implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Study study;
	private CustomFieldGroup customFieldGroup;
	private Set<Gene> genes = new HashSet<Gene>();
	
	public Disease() {
		
	}
	
	public Disease(Long id) {
		this.id = id;
	}
	
	@Id
	@SequenceGenerator(name = "disease_generator", sequenceName = "DISEASE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "disease_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CUSTOM_FIELD_GROUP_ID")
	public CustomFieldGroup getCustomFieldGroup() {
		return customFieldGroup;
	}

	public void setCustomFieldGroup(CustomFieldGroup customFieldGroup) {
		this.customFieldGroup = customFieldGroup;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "gene_disease", schema=Constants.DISEASE_SCHEMA, 
		joinColumns = {@JoinColumn(name = "DISEASE_ID", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "GENE_ID", nullable = false, updatable = false)}) 
	public Set<Gene> getGenes() {
		return genes;
	}
	
	public void setGenes(Set<Gene> genes) {
		this.genes = genes;
	}

	@Override
	public String toString() {
		return "Disease [id=" + id + ", name=" + name + ", study=" + study
				+ ", customFieldGroup=" + customFieldGroup + ", genes=" 
				+ "]";
	}
	
}
