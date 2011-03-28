package au.org.theark.core.model.study.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * StudyStatus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECTUID_TOKEN", schema = Constants.STUDY_SCHEMA)
public class SubjectUidToken implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;

	// Constructors

	/** default constructor */
	public SubjectUidToken() {
	}

	/** minimal constructor */
	public SubjectUidToken(Long id) {
		this.id = id;
	}

	public SubjectUidToken(Long id, String name){
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

	@Column(name = "NAME", length = 3)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
