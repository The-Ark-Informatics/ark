package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
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

@SuppressWarnings("serial")

@Table(name = "CUSTOM_FIELD_UPLOAD", schema = Constants.STUDY_SCHEMA)
public class CustomFieldUpload  implements java.io.Serializable{
	
	private Long id;
	private StudyUpload studyUpload;
	private CustomField customField;
	
	
	public CustomFieldUpload(){
		
	}

	@Id
	@SequenceGenerator(name = "CustomFieldUpload_PK_Seq", sequenceName = "STUDY.CUSTOM_FIELD_UPLOAD_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CustomFieldUpload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID")
	public StudyUpload getStudyUpload() {
		return studyUpload;
	}

	public void setStudyUpload(StudyUpload studyUpload) {
		this.studyUpload = studyUpload;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_ID")
	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}
	

}
