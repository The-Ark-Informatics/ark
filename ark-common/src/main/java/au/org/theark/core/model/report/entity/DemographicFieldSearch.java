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
@Table(name = "demographic_field_search", schema = Constants.REPORT_SCHEMA)
public class DemographicFieldSearch  implements Serializable  {

	private static final long serialVersionUID = 1L;
	private Long id;
	private DemographicField demographicField;
	private Search search;

	public DemographicFieldSearch(){
	}

	public DemographicFieldSearch(DemographicField field, Search searchRef){
		demographicField = field;
		search = searchRef;
	}

	@Id
	@SequenceGenerator(name = "demographic_field_search_generator", sequenceName = "DEMOGRAPHIC_FIELD_SEARCH_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "demographic_field_search_generator")
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
	@JoinColumn(name = "DEMOGRAPHIC_FIELD_ID")
	public DemographicField getDemographicField() {
		return demographicField;
	}
	public void setDemographicField(DemographicField demographicField) {
		this.demographicField = demographicField;
	}
	
}
