package au.org.theark.core.model.geno.entity;

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
@Table(name = "PIPELINE", schema = Constants.GENO_SCHEMA)
public class Pipeline implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Study study;
	
	private Set<Process> pipelineProcesses = new HashSet<Process>(0);
	private Set<LinkSubjectStudyPipeline> linkSubjectStudyPipelines = new HashSet<LinkSubjectStudyPipeline>(0);

	@Id
	@SequenceGenerator(name = "pipeline_generator", sequenceName = "PIPELINE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pipeline_generator")
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
	
	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pipeline")
	public Set<Process> getPipelineProcesses() {
		return pipelineProcesses;
	}

	public void setPipelineProcesses(Set<Process> pipelineProcesses) {
		this.pipelineProcesses = pipelineProcesses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pipeline")
	public Set<LinkSubjectStudyPipeline> getLinkSubjectStudyPipelines() {
		return this.linkSubjectStudyPipelines;
	}

	public void setLinkSubjectStudyPipelines(Set<LinkSubjectStudyPipeline> linkSubjectStudyPipelines) {
		this.linkSubjectStudyPipelines = linkSubjectStudyPipelines;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}
	

}
