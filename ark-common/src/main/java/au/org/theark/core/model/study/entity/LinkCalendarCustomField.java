package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;

@Entity
@Table(name = "LINK_CALENDAR_CUSTOM_FIELD", schema = Constants.STUDY_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class LinkCalendarCustomField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private StudyCalendar studyCalendar;
	private CustomField customField;

	@Id
	@SequenceGenerator(name = "link_calendar_customfield_generator", sequenceName = "LINK_CALENDAR_CUSTOMFIELD_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "link_calendar_customfield_generator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CALENDAR_ID")
	public StudyCalendar getStudyCalendar() {
		return studyCalendar;
	}

	public void setStudyCalendar(StudyCalendar studyCalendar) {
		this.studyCalendar = studyCalendar;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_ID")
	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

}
