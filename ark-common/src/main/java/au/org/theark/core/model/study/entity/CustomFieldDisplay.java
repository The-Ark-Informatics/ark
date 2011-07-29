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

import au.org.theark.core.model.Constants;

/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "CUSTOM_FIELD_DISPLAY", schema = Constants.STUDY_SCHEMA)
public class CustomFieldDisplay implements Serializable{
	
	private Long id;
	private CustomField customField;
	private CustomFieldGroup customFieldGroup;
	private Boolean required;
	private String requiredMessage;
	private Long sequence;
	
	
	public CustomFieldDisplay(){
		
	}


	@Id
	@SequenceGenerator(name = "custom_field_display_seq_gen", sequenceName = "CUSTOM_FIELD_DISPLAY_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_display_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_ID", nullable = false)
	public CustomField getCustomField() {
		return customField;
	}


	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_GROUP_ID")
	public CustomFieldGroup getCustomFieldGroup() {
		return customFieldGroup;
	}


	public void setCustomFieldGroup(CustomFieldGroup customFieldGroup) {
		this.customFieldGroup = customFieldGroup;
	}

	@Column(name = "REQUIRED")
	public Boolean getRequired() {
		return required;
	}


	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Column(name = "REQUIRED_MESSAGE")
	public String getRequiredMessage() {
		return requiredMessage;
	}


	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}

	@Column(name = "SEQUENCE",  precision = 22, scale = 0)
	public Long getSequence() {
		return sequence;
	}


	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}


	
}
