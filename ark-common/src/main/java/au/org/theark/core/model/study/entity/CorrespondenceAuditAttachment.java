package au.org.theark.core.model.study.entity;

import java.io.Serializable;
import java.sql.Blob;

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
@Table(name = "correspondence_audit_attachment", schema = Constants.STUDY_SCHEMA)
public class CorrespondenceAuditAttachment implements Serializable {

	private Long id;
	private CorrespondenceAudit correspondenceAudit;
	private String filename;
	private Long size;
	private Blob contents;
	
	@Id
    @SequenceGenerator(name="correspondence_audit_attachment_generator", sequenceName="CORRESPONDENCE_AUDIT_ATTACHMENT_SEQUENCE")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "correspondence_audit_attachment_generator")
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
	
	@Column(name = "FILENAME", length = 255)
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name = "SIZE", precision = 22, scale = 0)
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	
	@Column(name = "CONTENTS")
	public Blob getContents() {
		return contents;
	}
	
	public void setContents(Blob contents) {
		this.contents = contents;
	}
	
	
}
