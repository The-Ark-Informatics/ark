package au.org.theark.study.model.entity;

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

/**
 * DataType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DATA_TYPE", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class DataType implements java.io.Serializable {

	// Fields

	private Long dataTypeKey;
	private String name;
	private String description;
	private Set<SubjectCustmFld> subjectCustmFlds = new HashSet<SubjectCustmFld>(
			0);

	// Constructors

	/** default constructor */
	public DataType() {
	}

	/** minimal constructor */
	public DataType(Long dataTypeKey, String name) {
		this.dataTypeKey = dataTypeKey;
		this.name = name;
	}

	/** full constructor */
	public DataType(Long dataTypeKey, String name, String description,
			Set<SubjectCustmFld> subjectCustmFlds) {
		this.dataTypeKey = dataTypeKey;
		this.name = name;
		this.description = description;
		this.subjectCustmFlds = subjectCustmFlds;
	}

	// Property accessors
	@Id
	@Column(name = "DATA_TYPE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getDataTypeKey() {
		return this.dataTypeKey;
	}

	public void setDataTypeKey(Long dataTypeKey) {
		this.dataTypeKey = dataTypeKey;
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