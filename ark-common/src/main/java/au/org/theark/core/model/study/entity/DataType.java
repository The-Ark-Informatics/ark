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
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * DataType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DATA_TYPE", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class DataType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Set<SubjectCustmFld> subjectCustmFlds = new HashSet<SubjectCustmFld>(0);

	// Constructors

	/** default constructor */
	public DataType() {
	}

	/** minimal constructor */
	public DataType(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public DataType(Long id, String name, String description,	Set<SubjectCustmFld> subjectCustmFlds) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.subjectCustmFlds = subjectCustmFlds;
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

	@Column(name = "NAME", unique = true, nullable = false)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dataType")
	public Set<SubjectCustmFld> getSubjectCustmFlds() {
		return this.subjectCustmFlds;
	}

	public void setSubjectCustmFlds(Set<SubjectCustmFld> subjectCustmFlds) {
		this.subjectCustmFlds = subjectCustmFlds;
	}

}