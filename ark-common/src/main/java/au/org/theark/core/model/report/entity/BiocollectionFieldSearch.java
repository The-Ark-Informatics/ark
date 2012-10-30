package au.org.theark.core.model.report.entity;

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

@Entity
@Table(name = "biocollection_field_search", schema = Constants.REPORT_SCHEMA)
public class BiocollectionFieldSearch  implements Serializable  {

	private static final long serialVersionUID = 1L;
	private Long id;
	private BiocollectionField biocollectionField;
	private Search search;

	public BiocollectionFieldSearch(){
	}

	public BiocollectionFieldSearch(BiocollectionField field, Search searchRef){
		biocollectionField = field;
		search = searchRef;
	}

	@Id
	@SequenceGenerator(name = "biocollection_field_search_generator", sequenceName = "BIOCOLLECTION_FIELD_SEARCH_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biocollection_field_search_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_ID")
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}

	@JoinColumn(name = "BIOCOLLECTION_FIELD_ID")
	public BiocollectionField getBiocollectionField() {
		return biocollectionField;
	}
	public void setBiocollectionField(BiocollectionField biocollectionField) {
		this.biocollectionField = biocollectionField;
	}
	
}
