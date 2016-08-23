package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;

@Entity
@Table(name = "LINK_SUBJECT_TWIN", schema = Constants.STUDY_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class LinkSubjectTwin implements Serializable {

	/**
    *
    */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private LinkSubjectStudy firstSubject;
	private LinkSubjectStudy secondSubject;
	private TwinType twinType;

	public LinkSubjectTwin() {
	}

	public LinkSubjectTwin(Integer id, LinkSubjectStudy firstSubject,
			LinkSubjectStudy secondSubject, TwinType twinType) {
		this.id = id;
		this.firstSubject = firstSubject;
		this.secondSubject = secondSubject;
		this.twinType = twinType;
	}

	@Id
    @SequenceGenerator(name = "link_subject_twin_generator", sequenceName = "LINK_SUBJECT_TWIN_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "link_subject_twin_generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIRST_SUBJECT")
	public LinkSubjectStudy getFirstSubject() {
		return firstSubject;
	}

	public void setFirstSubject(LinkSubjectStudy firstSubject) {
		this.firstSubject = firstSubject;
	}

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECOND_SUBJECT")
	public LinkSubjectStudy getSecondSubject() {
		return secondSubject;
	}

	public void setSecondSubject(LinkSubjectStudy secondSubject) {
		this.secondSubject = secondSubject;
	}

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TWIN_TYPE_ID")
	public TwinType getTwinType() {
		return twinType;
	}

	public void setTwinType(TwinType twinType) {
		this.twinType = twinType;
	}

}
