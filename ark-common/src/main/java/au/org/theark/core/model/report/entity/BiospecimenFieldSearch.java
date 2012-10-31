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
@Table(name = "biospecimen_field_search", schema = Constants.REPORT_SCHEMA)
public class BiospecimenFieldSearch  implements Serializable  {

	private static final long serialVersionUID = 1L;
	private Long id;
	private BiospecimenField biospecimenField;
	private Search search;

	public BiospecimenFieldSearch(){
	}

	public BiospecimenFieldSearch(BiospecimenField field, Search searchRef){
		biospecimenField = field;
		search = searchRef;
	}

	@Id
	@SequenceGenerator(name = "biospecimen_field_search_generator", sequenceName = "BIOSPECIMEN_FIELD_SEARCH_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biospecimen_field_search_generator")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_FIELD_ID")
	public BiospecimenField getBiospecimenField() {
		return biospecimenField;
	}
	public void setBiospecimenField(BiospecimenField biospecimenField) {
		this.biospecimenField = biospecimenField;
	}
	
}
