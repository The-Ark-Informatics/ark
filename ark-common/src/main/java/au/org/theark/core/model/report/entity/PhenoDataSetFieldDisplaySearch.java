package au.org.theark.core.model.report.entity;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(name = "pheno_dataset_field_display_search", schema = Constants.REPORT_SCHEMA)
public class PhenoDataSetFieldDisplaySearch implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private PhenoDataSetFieldDisplay phenoDatasetFieldDisplay;
	private Search search;

	public PhenoDataSetFieldDisplaySearch() {
	}

	public PhenoDataSetFieldDisplaySearch(PhenoDataSetFieldDisplay pdfd, Search search2) {
		search = search2;
		this.phenoDatasetFieldDisplay = pdfd;
	}

	@Id
	@SequenceGenerator(name = "pheno_dataset_field_display_search_generator", sequenceName = "PHENO_DATASET_FIELD_DISPLAY_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pheno_dataset_field_display_search_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_DATASET_FIELD_DISPLAY_ID")
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay() {
		return phenoDatasetFieldDisplay;
	}

	public void setPhenoDataSetFieldDisplay(PhenoDataSetFieldDisplay phenoDatasetFieldDisplay) {
		this.phenoDatasetFieldDisplay = phenoDatasetFieldDisplay;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_ID")
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	
}
