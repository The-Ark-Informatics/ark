package au.org.theark.core.model.geno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;


@Entity
@Table(name = "LSS_PIPELINE", schema = Constants.GENO_SCHEMA)
public class LinkSubjectStudyPipeline implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Pipeline pipeline;
	private LinkSubjectStudy linkSubjectStudy;
	
	@Id
	@SequenceGenerator(name = "lss_pipeline_generator", sequenceName = "LSS_PIPELINE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "lss_pipeline_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PIPELINE_ID")
	public Pipeline getPipeline() {
		return this.pipeline;
	}

	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LSS_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return this.linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}
}
