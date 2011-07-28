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

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "SUBJECT_CUSTOM_FIELD_DATA", schema = Constants.STUDY_SCHEMA)
public class SubjectCustomFieldData implements Serializable{
	
	private Long id;
	private LinkSubjectStudy linkSubjectStudy;
	private CustomFieldDisplay customFieldDisplay;
	private String customFieldValue;
	
	/**
	 * Default Constructor
	 */
	public SubjectCustomFieldData(){
		
	}

	@Id
	@SequenceGenerator(name = "subject_custom_field_data_generator", sequenceName = "SUBJECT_CUSTOM_FIELD_DATA_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "subject_custom_field_data_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_DISPLAY_ID")
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	@Column(name = "CUSTOM_FIELD_VALUE",length=2000)
	public String getCustomFieldValue() {
		return customFieldValue;
	}

	public void setCustomFieldValue(String customFieldValue) {
		this.customFieldValue = customFieldValue;
	}
	
	
	

}
