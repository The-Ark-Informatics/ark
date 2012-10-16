package au.org.theark.core.model.report.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * 
 * The search object is used to save what criteria, name, etc is used in a search 
 * 
 * @author travis
 *
 */

@Entity
@Table(name = "search", schema = Constants.REPORT_SCHEMA)
public class Search {

	private Long id;
	private String name;
	private Set<CustomFieldDisplaySearch>  customFieldsToReturn;
	private Set<DemographicFieldSearch>  demographicFieldsToReturn;
//	private Set<QueryFilter> queryFilters;
	private QueryGrouping topLevelQueryGrouping;


	@Id
	@SequenceGenerator(name = "search_generator", sequenceName = "SEARCH_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "search_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "demographicField")
	public Set<DemographicFieldSearch> getDemographicFieldsToReturn() {
		return demographicFieldsToReturn;
	}
	public void setDemographicFieldsToReturn(Set<DemographicFieldSearch> demographicFieldsToReturn) {
		this.demographicFieldsToReturn = demographicFieldsToReturn;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<CustomFieldDisplaySearch> getCustomFieldsToReturn() {
		return customFieldsToReturn;
	}
	public void setCustomFieldsToReturn(Set<CustomFieldDisplaySearch> customFieldsToReturn) {
		this.customFieldsToReturn = customFieldsToReturn;
	}
	
/*	public void setQueryFilters(Set<QueryFilter> queryFilters) {
		this.queryFilters = queryFilters;
	}
	public Set<QueryFilter> getQueryFilters() {
		return queryFilters;
	}*/

	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TOP_LEVEL_GROUPING_ID")
	public QueryGrouping getTopLevelQueryGrouping() {
		return topLevelQueryGrouping;
	}
	public void setTopLevelQueryGrouping(QueryGrouping topLevelQueryGrouping) {
		this.topLevelQueryGrouping = topLevelQueryGrouping;
	}
	
	
	
}
