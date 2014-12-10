package au.org.theark.core.model.disease.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;

@Entity
@Table(name = "AFFECTION", schema = Constants.DISEASE_SCHEMA)
public class Affection implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Study study;
	private Disease disease;
	private Date recordDate;
	private LinkSubjectStudy linkSubjectStudy;
	private AffectionStatus affectionStatus;
	private Set<AffectionCustomFieldData> affectionCustomFieldDataSet = new HashSet<AffectionCustomFieldData>();
	private Set<Position> positions = new HashSet<Position>();
	
	@Id
	@SequenceGenerator(name = "affection_generator", sequenceName = "AFFECTION_SEQ")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "affection_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "affection")
	public Set<AffectionCustomFieldData> getAffectionCustomFieldDataSets() {
		return this.affectionCustomFieldDataSet;
	}
	
	public void setAffectionCustomFieldDataSets(Set<AffectionCustomFieldData> affectionCustomFieldDataSet) {
		this.affectionCustomFieldDataSet = affectionCustomFieldDataSet;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DISEASE_ID")
	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINKSUBJECTSTUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "RECORD_DATE", length = 10)
	public Date getRecordDate() {
		return recordDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AFFECTION_STATUS_ID")
	public AffectionStatus getAffectionStatus() {
		return affectionStatus;
	}
	
	public void setAffectionStatus(AffectionStatus affectionStatus) {
		this.affectionStatus = affectionStatus;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "affection_position", schema=Constants.DISEASE_SCHEMA, 
	joinColumns = {@JoinColumn(name = "AFFECTION_ID", nullable = false, updatable = false) },
	inverseJoinColumns = { @JoinColumn(name = "POSITION_ID", nullable = false, updatable = false)})
	public Set<Position> getPositions() {
		return this.positions;
	}
	
	public void setPositions(Set<Position> positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "Affection [id=" + id + ", study=" + study + ", recordDate=" + recordDate
				+ ", disease=" + disease +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((affectionCustomFieldDataSet == null) ? 0
						: affectionCustomFieldDataSet.hashCode());
		result = prime * result
				+ ((affectionStatus == null) ? 0 : affectionStatus.hashCode());
		result = prime * result + ((disease == null) ? 0 : disease.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((linkSubjectStudy == null) ? 0 : linkSubjectStudy.hashCode());
		result = prime * result
				+ ((recordDate == null) ? 0 : recordDate.hashCode());
		result = prime * result + ((study == null) ? 0 : study.hashCode());
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
		Affection other = (Affection) obj;
		if (affectionCustomFieldDataSet == null) {
			if (other.affectionCustomFieldDataSet != null)
				return false;
		} else if (!affectionCustomFieldDataSet
				.equals(other.affectionCustomFieldDataSet))
			return false;
		if (affectionStatus == null) {
			if (other.affectionStatus != null)
				return false;
		} else if (!affectionStatus.equals(other.affectionStatus))
			return false;
		if (disease == null) {
			if (other.disease != null)
				return false;
		} else if (!disease.equals(other.disease))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (linkSubjectStudy == null) {
			if (other.linkSubjectStudy != null)
				return false;
		} else if (!linkSubjectStudy.equals(other.linkSubjectStudy))
			return false;
		if (recordDate == null) {
			if (other.recordDate != null)
				return false;
		} else if (!recordDate.equals(other.recordDate))
			return false;
		if (study == null) {
			if (other.study != null)
				return false;
		} else if (!study.equals(other.study))
			return false;
		return true;
	}
	
}
