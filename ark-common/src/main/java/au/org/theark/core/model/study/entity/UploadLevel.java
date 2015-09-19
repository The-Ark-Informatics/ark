package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;

	@Entity(name = "au.org.theark.common.model.study.entity.UploadLevel")
	@Table(name = "UPLOAD_LEVEL", schema = Constants.STUDY_SCHEMA)
	public class UploadLevel implements Serializable {
		private static final long serialVersionUID = 1L;

		private Long id;

		private String name;
		
		
		public UploadLevel() {
		}

		public UploadLevel(Long id) {
			this.id = id;
		}

		public UploadLevel(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		@Id
		@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		@Column(name = "NAME", length = 20)
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
	

}
