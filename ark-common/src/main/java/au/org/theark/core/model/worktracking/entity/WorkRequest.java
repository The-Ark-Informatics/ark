package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

@Entity
@Table(name = "WORK_REQUEST", schema = Constants.ADMIN_SCHEMA)
public class WorkRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private WorkRequestStatus requestStatus;
	private Researcher researcher;
	private Date requestedDate;
	private Date commencedDate;
	private Date completedDate;
	private Long studyId;
	
	public WorkRequest() {
	}

	public WorkRequest(Long id) {
		this.id = id;
	}

	public WorkRequest(Long id, String name, String description,
			WorkRequestStatus requestStatus, Researcher researcher,
			Date requestedDate, Date commencedDate, Date completedDate,
			Long studyId) {
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.requestStatus = requestStatus;
		this.researcher = researcher;
		this.requestedDate = requestedDate;
		this.commencedDate = commencedDate;
		this.completedDate = completedDate;
		this.studyId = studyId;
	}

	@Id
	@SequenceGenerator(name = "work_request_generator", sequenceName = "WORK_REQUEST_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "work_request_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	public WorkRequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(WorkRequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	@ManyToOne()
	@JoinColumn(name="RESEARCHER_ID", referencedColumnName="ID")	
	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REQUESTED_DATE", length = 7)
	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "COMMENCED_DATE", length = 7)
	public Date getCommencedDate() {
		return commencedDate;
	}

	public void setCommencedDate(Date commencedDate) {
		this.commencedDate = commencedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "COMPLETED_DATE", length = 7)
	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	@Column(name = "STUDY_ID")
	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commencedDate == null) ? 0 : commencedDate.hashCode());
		result = prime * result
				+ ((completedDate == null) ? 0 : completedDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((requestedDate == null) ? 0 : requestedDate.hashCode());
		result = prime * result + ((studyId == null) ? 0 : studyId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkRequest other = (WorkRequest) obj;
		if (commencedDate == null) {
			if (other.commencedDate != null)
				return false;
		} else if (!commencedDate.equals(other.commencedDate))
			return false;
		if (completedDate == null) {
			if (other.completedDate != null)
				return false;
		} else if (!completedDate.equals(other.completedDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (requestedDate == null) {
			if (other.requestedDate != null)
				return false;
		} else if (!requestedDate.equals(other.requestedDate))
			return false;
		if (studyId == null) {
			if (other.studyId != null)
				return false;
		} else if (!studyId.equals(other.studyId))
			return false;
		return true;
	}
	
	

}
