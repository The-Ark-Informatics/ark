package au.org.theark.core.model.pheno.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "QUESTIONNAIRE_STATUS", schema = Constants.PHENO_TABLE_SCHEMA)
public class QuestionnaireStatus implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long	id;
	private String	name;
	private String	description;
	
	/**
	 * Constructor
	 */
	public QuestionnaireStatus(){
		
	}
	
	@Id																			
	@SequenceGenerator(name = "questionnaire_status_generator", sequenceName = "QUESTIONNAIRE_STATUS_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "questionnaire_status_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", nullable = false, length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
