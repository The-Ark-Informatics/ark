package au.org.theark.core.model.study.entity;

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

/**
 * StudyStatus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDY_STATUS", schema = Constants.STUDY_SCHEMA)
public class StudyStatus implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Set<Study> studies = new HashSet<Study>(0);

	// Constructors

	/** default constructor */
	public StudyStatus() {
	}

	/** minimal constructor */
	public StudyStatus(Long id) {
		this.id = id;
	}

	/** full constructor */
	public StudyStatus(Long id, String name, String description,
			Set<Study> studies) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.studies = studies;
	}
	
	public StudyStatus(Long id, String name){
		this.id = id;
		this.name = name;
	}

	// Property accessors
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studyStatus")
	public Set<Study> getStudies() {
		return this.studies;
	}

	public void setStudies(Set<Study> studies) {
		this.studies = studies;
	}

}