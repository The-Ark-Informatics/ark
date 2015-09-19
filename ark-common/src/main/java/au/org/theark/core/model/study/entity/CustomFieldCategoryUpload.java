package au.org.theark.core.model.study.entity;

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

	@Entity
	@Table(name = "CUSTOM_FIELD_CATEGORY_UPLOAD", schema = Constants.STUDY_SCHEMA)
	public class CustomFieldCategoryUpload implements java.io.Serializable {

		private static final long serialVersionUID = 1L;

		private Long id;
		private Upload studyUpload;
		private CustomFieldCategory customFieldCategoty;

		public CustomFieldCategoryUpload() {

		}
		@Id
		@SequenceGenerator(name = "CustomFieldCategoryUpload_PK_Seq", sequenceName = "STUDY.CUSTOM_FIELD_CATEGORY_UPLOAD_PK_SEQ")
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "CustomFieldCategoryUpload_PK_Seq")
		@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "UPLOAD_ID")
		public Upload getUpload() {
			return studyUpload;
		}

		public void setUpload(Upload studyUpload) {
			this.studyUpload = studyUpload;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "CUSTOM_FIELD_CATEGORY_ID")
		public CustomFieldCategory getCustomFieldCategoty() {
			return customFieldCategoty;
		}

		public void setCustomFieldCategoty(CustomFieldCategory customFieldCategoty) {
			this.customFieldCategoty = customFieldCategoty;
		}
	
}
