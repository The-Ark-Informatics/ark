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

/**
 * StudyCompStatus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDY_COMP_STATUS", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class StudyCompStatus implements java.io.Serializable {

	// Fields

	private Long studyCompStatusKey;
	private String name;
	private String description;
	private Set<LinkStudyStudycomp> linkStudyStudycomps = new HashSet<LinkStudyStudycomp>(
			0);

	// Constructors

	/** default constructor */
	public StudyCompStatus() {
	}

	/** minimal constructor */
	public StudyCompStatus(Long studyCompStatusKey) {
		this.studyCompStatusKey = studyCompStatusKey;
	}

	/** full constructor */
	public StudyCompStatus(Long studyCompStatusKey, String name,
			String description, Set<LinkStudyStudycomp> linkStudyStudycomps) {
		this.studyCompStatusKey = studyCompStatusKey;
		this.name = name;
		this.description = description;
		this.linkStudyStudycomps = linkStudyStudycomps;
	}

	// Property accessors
	@Id
	@Column(name = "STUDY_COMP_STATUS_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getStudyCompStatusKey() {
		return this.studyCompStatusKey;
	}

	public void setStudyCompStatusKey(Long studyCompStatusKey) {
		this.studyCompStatusKey = studyCompStatusKey;
	}

	@Column(name = "NAME", unique = true, length = 100)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studyCompStatus")
	public Set<LinkStudyStudycomp> getLinkStudyStudycomps() {
		return this.linkStudyStudycomps;
	}

	public void setLinkStudyStudycomps(
			Set<LinkStudyStudycomp> linkStudyStudycomps) {
		this.linkStudyStudycomps = linkStudyStudycomps;
	}

}