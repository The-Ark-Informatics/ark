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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "link_correspondence_audit_person", schema = Constants.STUDY_SCHEMA)
public class LinkCorrespondenceAuditPerson implements Serializable {

	private Long id;
	private CorrespondenceAudit correspondenceAudit;
	private Person person;
	
	@Id
    @SequenceGenerator(name="link_correspondence_audit_person_generator", sequenceName="LINK_CORRESPONDENCE_AUDIT_PERSON_SEQUENCE")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "link_correspondence_audit_person_generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORRESPONDENCE_AUDIT_ID")
	public CorrespondenceAudit getCorrespondenceAudit() {
		return correspondenceAudit;
	}
	
	public void setCorrespondenceAudit(CorrespondenceAudit correspondenceAudit) {
		this.correspondenceAudit = correspondenceAudit;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
}
