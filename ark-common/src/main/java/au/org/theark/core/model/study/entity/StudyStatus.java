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

/**
 * StudyStatus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDY_STATUS", schema = "ETA")
public class StudyStatus implements java.io.Serializable {

	// Fields

	private Long studyStatusKey;
	private String name;
	private String description;
	private Set<Study> studies = new HashSet<Study>(0);

	// Constructors

	/** default constructor */
	public StudyStatus() {
	}

	/** minimal constructor */
	public StudyStatus(Long studyStatusKey) {
		this.studyStatusKey = studyStatusKey;
	}

	/** full constructor */
	public StudyStatus(Long studyStatusKey, String name, String description,
			Set<Study> studies) {
		this.studyStatusKey = studyStatusKey;
		this.name = name;
		this.description = description;
		this.studies = studies;
	}
	
	public StudyStatus(Long studyStatusKey, String name){
		this.studyStatusKey = studyStatusKey;
		this.name = name;
	}

	// Property accessors
	@Id
	@Column(name = "STUDY_STATUS_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getStudyStatusKey() {
		return this.studyStatusKey;
	}

	public void setStudyStatusKey(Long studyStatusKey) {
		this.studyStatusKey = studyStatusKey;
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