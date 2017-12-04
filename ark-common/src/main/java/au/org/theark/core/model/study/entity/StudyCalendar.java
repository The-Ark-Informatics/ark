package au.org.theark.core.model.study.entity;

import java.io.Serializable;
import java.util.Date;
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

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;

@Entity
@Table(name = "STUDY_CALENDAR", schema = Constants.STUDY_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class StudyCalendar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private Study study;
	private StudyComp studyComp;
	private Boolean allowOverlapping;
	private Set<LinkCalendarCustomField> calendarCustomFields = new HashSet<LinkCalendarCustomField>();
	
	@Id
	@SequenceGenerator(name = "studycalendar_generator", sequenceName = "STUDYCALENDAR_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "studycalendar_generator")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name= "ALLOW_OVERLAPPING")
	public Boolean getAllowOverlapping(){
		return allowOverlapping;
	}
	
	public void setAllowOverlapping(Boolean allowOverlapping){
		this.allowOverlapping = allowOverlapping;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}
	
	public void setStudy(Study study) {
		this.study = study;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMPONENT_ID")
	public StudyComp getStudyComp() {
		return studyComp;
	}
	
	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studyCalendar")
	public Set<LinkCalendarCustomField> getCalendarCustomFields() {
		return calendarCustomFields;
	}

	public void setCalendarCustomFields(Set<LinkCalendarCustomField> calendarCustomFields) {
		this.calendarCustomFields = calendarCustomFields;
	}	
}
