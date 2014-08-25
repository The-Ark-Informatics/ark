package au.org.theark.core.model.disease.entity;

import java.io.Serializable;

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
@Table(name = "MUTATION", schema = Constants.DISEASE_SCHEMA)
public class Mutation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private LinkSubjectStudy linkSubjectStudy;
	private Disease disease;
	private Boolean screened;
	private Boolean tested;
	
	public Mutation() {
		
	}
	
	public Mutation(Long id) {
		this.id = id;
	}
	
	@Id
	@SequenceGenerator(name = "mutation_generator", sequenceName = "MUTATION_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "mutation_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISEASE_ID")
	public Disease getDisease() {
		return this.disease;
	}
	
	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	
	@Column(name = "screened")
	public Boolean getScreened() {
		return this.screened;
	}
	
	public void setScreened(Boolean screened) {
		this.screened = screened;
	}
	
	@Column(name = "tested")
	public Boolean getTested() {
		return this.tested;
	}
	
	public void setTested(Boolean tested) {
		this.tested = tested;
	}

}
