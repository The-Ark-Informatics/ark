package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "WORK_REQUEST_STATUS", schema = Constants.ADMIN_SCHEMA)
public class WorkRequestStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long			id;
	private String			name;
	private String			description;
	private Set<WorkRequest>	workRequests	= new HashSet<WorkRequest>(0);


	public WorkRequestStatus() {
	}

	public WorkRequestStatus(Long id) {
		this.id = id;
	}

	public WorkRequestStatus(Long id, String name, String description, Set<WorkRequest> workRequests) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.workRequests = workRequests;
	}

	public WorkRequestStatus(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 25)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requestStatus")
	public Set<WorkRequest> getWorkRequests() {
		return workRequests;
	}

	public void setWorkRequests(Set<WorkRequest> workRequests) {
		this.workRequests = workRequests;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		WorkRequestStatus other = (WorkRequestStatus) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "WorkRequestStatus [id=" + id + ", name=" + name
				+ ", description=" + description + "]";
	}
		
}
