package au.org.theark.core.model.report.entity;

import java.util.Date;
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
	private Set<QueryFilter>  				queryFilters = new HashSet<QueryFilter>();
	private Set<CustomFieldDisplaySearch>  	customFieldsToReturn = new HashSet<CustomFieldDisplaySearch>();
	private Set<PhenoDataSetFieldDisplaySearch> phenoDataSetFieldsToReturn = new HashSet<PhenoDataSetFieldDisplaySearch>();
	private Set<BiospecimenFieldSearch>  	biospecimenFieldsToReturn = new HashSet<BiospecimenFieldSearch>();
	private Set<BiocollectionFieldSearch>  	biocollectionFieldsToReturn = new HashSet<BiocollectionFieldSearch>();
	private Set<DemographicFieldSearch>  	demographicFieldsToReturn = new HashSet<DemographicFieldSearch>();
	private Set<ConsentStatusFieldSearch>	consentStatusFieldsToReturn = new HashSet<ConsentStatusFieldSearch>();
	private QueryGrouping topLevelQueryGrouping;
	private Study study;
	private String status;
	private Date startTime;
	private Date finishTime;
	private Boolean includeGeno;

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
	public Set<ConsentStatusFieldSearch> getConsentStatusFieldsToReturn() {
		return consentStatusFieldsToReturn;
	}
	public void setConsentStatusFieldsToReturn(Set<ConsentStatusFieldSearch> consentStatusFieldsToReturn) {
		this.consentStatusFieldsToReturn = consentStatusFieldsToReturn;
	}

	@Column(name = "INCLUDE_GENO")
	public Boolean getIncludeGeno() {
		return includeGeno;
	}

	public void setIncludeGeno(Boolean includeGeno) {
		this.includeGeno = includeGeno;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "search")
	public Set<PhenoDataSetFieldDisplaySearch> getPhenoDataSetFieldsToReturn() {
		return phenoDataSetFieldsToReturn;
	}
	public void setPhenoDataSetFieldsToReturn(Set<PhenoDataSetFieldDisplaySearch> phenoDataSetFieldsToReturn) {
		this.phenoDataSetFieldsToReturn = phenoDataSetFieldsToReturn;
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


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "search")
	public Set<QueryFilter> getQueryFilters() {
		return queryFilters;
	}

	public void setQueryFilters(Set<QueryFilter> queryFilters) {
		this.queryFilters = queryFilters;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	@Column(name = "STATUS", length = 255)
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	@Column(name = "STARTTIME")
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the finishTime
	 */
	public Date getFinishTime() {
		return finishTime;
	}

	/**
	 * @param finishTime the finishTime to set
	 */
	@Column(name = "FINISHTIME")
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
}
