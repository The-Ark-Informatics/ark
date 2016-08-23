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
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;

	@Entity
	@Table(name = "PHENO_FIELD_CATEGORY_UPLOAD", schema =  au.org.theark.core.model.Constants.PHENO_TABLE_SCHEMA)
	public class PhenoDataSetFieldCategoryUpload implements java.io.Serializable {

		private static final long serialVersionUID = 1L;

		private Long id;
		private Upload studyUpload;
		private PhenoDataSetCategory phenoDataSetCategory;
		public PhenoDataSetFieldCategoryUpload() {

		}
		@Id
		@SequenceGenerator(name = "PhenofieldCategoryUpload_PK_Seq", sequenceName = "PHENO.PHENO_FIELD_CATEGORY_UPLOAD_PK_SEQ")
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "PhenofieldCategoryUpload_PK_Seq")
		@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "UPLOAD_ID")
		public Upload getStudyUpload() {
			return studyUpload;
		}
		public void setStudyUpload(Upload studyUpload) {
			this.studyUpload = studyUpload;
		}
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "PHENO_FIELD_CATEGORY_ID")
		public PhenoDataSetCategory getPhenoDataSetCategory() {
			return phenoDataSetCategory;
		}
		public void setPhenoDataSetCategory(PhenoDataSetCategory phenoDataSetCategory) {
			this.phenoDataSetCategory = phenoDataSetCategory;
		}
		
}
