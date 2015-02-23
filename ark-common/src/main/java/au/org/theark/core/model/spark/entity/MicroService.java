package au.org.theark.core.model.spark.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Study;

@Entity
@Table(name = "MICRO_SERVICE", schema = Constants.SPARK_SCHEMA)
public class MicroService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String serviceUrl;
	
	@Transient
	private String status;
	
	private Long studyId;

	@Id
	@SequenceGenerator(name = "microservice_generator", sequenceName = "MICROSERVICE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "microservice_generator")
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
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

	@Column(name = "SERVICE_URL")
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Column(name = "STUDY_ID")
	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	@Transient
	public String getStatus() {
		return status;
	}

	@Transient
	public void setStatus(String status) {
		this.status = status;
	}
}
