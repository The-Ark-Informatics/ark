/**
 * 
 * This is a new file
 *
 *
 */
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
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "STUDY_CONSENT_QUESTION", schema = Constants.STUDY_SCHEMA)
public class StudyConsentQuestion implements Serializable{
	
	private Long id;
	private String question;
	private Study study;
	private DataType dataType;
	private String discreteValues;
	private Long postion;
	
	public DataType getDataType() {
		return dataType;
	}


	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}


	public String getDiscreteValues() {
		return discreteValues;
	}


	public void setDiscreteValues(String discreteValues) {
		this.discreteValues = discreteValues;
	}


	public Long getPostion() {
		return postion;
	}


	public void setPostion(Long postion) {
		this.postion = postion;
	}


	/**
	 * Constrcutor
	 */
	public StudyConsentQuestion(){
		
	}

	
	@Id
	@SequenceGenerator(name="studyconsent_gen", sequenceName="STUDY_CONSENT_GEN_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "studyconsent_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "QUESTION")
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}
	
}
