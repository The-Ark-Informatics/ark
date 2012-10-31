package au.org.theark.core.model.report.entity;

import java.util.HashSet;
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
import au.org.theark.core.model.study.entity.Study;

/**
 * 
 * The search object is used to save what criteria, name, etc is used in a search 
 * 
 * @author travis
 *
 */

@Entity
@Table(name = "search", schema = Constants.REPORT_SCHEMA)
public class Search  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Set<CustomFieldDisplaySearch>  	customFieldsToReturn = new HashSet<CustomFieldDisplaySearch>();
	private Set<BiospecimenFieldSearch>  	biospecimenFieldsToReturn = new HashSet<BiospecimenFieldSearch>();
	private Set<BiocollectionFieldSearch>  	biocollectionFieldsToReturn = new HashSet<BiocollectionFieldSearch>();
	private Set<DemographicFieldSearch>  	demographicFieldsToReturn = new HashSet<DemographicFieldSearch>();
	private QueryGrouping topLevelQueryGrouping;
	private Study study;


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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "search")
	public Set<DemographicFieldSearch> getDemographicFieldsToReturn() {
		return demographicFieldsToReturn;
	}
	public void setDemographicFieldsToReturn(Set<DemographicFieldSearch> demographicFieldsToReturn) {
		this.demographicFieldsToReturn = demographicFieldsToReturn;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "search")
	public Set<BiocollectionFieldSearch> getBiocollectionFieldsToReturn() {
		return biocollectionFieldsToReturn;
	}
	public void setBiocollectionFieldsToReturn(Set<BiocollectionFieldSearch> biocollectionFieldsToReturn) {
		this.biocollectionFieldsToReturn = biocollectionFieldsToReturn;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "search")
	public Set<BiospecimenFieldSearch> getBiospecimenFieldsToReturn() {
		return biospecimenFieldsToReturn;
	}
	public void setBiospecimenFieldsToReturn(Set<BiospecimenFieldSearch> biospecimenFieldsToReturn) {
		this.biospecimenFieldsToReturn = biospecimenFieldsToReturn;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "search")
	public Set<CustomFieldDisplaySearch> getCustomFieldsToReturn() {
		return customFieldsToReturn;
	}
	public void setCustomFieldsToReturn(Set<CustomFieldDisplaySearch> customFieldsToReturn) {
		this.customFieldsToReturn = customFieldsToReturn;
	}
	

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


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}

	
}
