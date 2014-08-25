package au.org.theark.core.model.disease.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 * 
 */
@Entity
@Table(name = "PHONE_STATUS", schema = Constants.STUDY_SCHEMA)
public class AffectionStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;

	public AffectionStatus() {

	}

	@Id
	@SequenceGenerator(name = "affection_status_generator", sequenceName = "AFFECTION_STATUS_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "affection_status_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
