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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Study;

@Entity
@Table(name = "AFFECTION", schema = Constants.DISEASE_SCHEMA)
public class Affection implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Study study;
	private String name;
	private Set<AffectionCustomFieldData> affectionCustomFieldDataSet = new HashSet<AffectionCustomFieldData>();
	
	@Id
	@SequenceGenerator(name = "affection_generator", sequenceName = "AFFECTION_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "affection_generator")
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
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}
	
	@Column(name = "NAME", length=100)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "affection")
	public Set<AffectionCustomFieldData> getAffectionCustomFieldDataSets() {
		return this.affectionCustomFieldDataSet;
	}
	
	public void setAffectionCustomFieldDataSets(Set<AffectionCustomFieldData> affectionCustomFieldDataSet) {
		this.affectionCustomFieldDataSet = affectionCustomFieldDataSet;
	}
	 
}
