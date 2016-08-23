package au.org.theark.core.model.report.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import org.hibernate.annotations.Cascade;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

@Entity
@Table(name = "query_filter", schema = Constants.REPORT_SCHEMA)
public class QueryFilter  implements Serializable {


	private static final long serialVersionUID = 1L;

	//	QueryFilter leftQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
//	QueryFilter rightQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
//	JoinType filterJoin; //AND OR  
    @Id
    @SequenceGenerator(name = "query_filter_generator", sequenceName = "QUERY_FILTER_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "query_filter_generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private Long id;
	 
//	int numberOfLeftBracketsBeforePrecursor;
//	int numberOfLeftBracketsAfterPrecursor;
//	int numberOfRightBracketsBeforeJoin;
//	int numberOfRightBracketsAfterJoin;

	private BiospecimenField biospecimenField;	
	private BiocollectionField biocollectionField;	
	private DemographicField demographicField;	
	private CustomFieldDisplay customFieldDisplay;
	private PhenoDataSetFieldDisplay phenoDataSetFieldDisplay;
	private ConsentStatusField consentStatusField;
	private String value;
	private String secondValue; // for between and similar operators
	//private String valueForMultiselectComponentLookup;    -- will be compared to demographicfield.fieldForDisplay
	private Operator operator;
	private Prefix prefix; //eg NOT, IN,
	private Search search;//was going to make this reusable...but think that might just be a neusance

	@Id
	@SequenceGenerator(name = "query_filter_generator", sequenceName = "QUERY_FILTER_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "query_filter_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public QueryFilter(){
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEMOGRAPHIC_FIELD_ID")
	public DemographicField getDemographicField() {
		return demographicField;
	}
	public void setDemographicField(DemographicField demographicField) {
		this.demographicField = demographicField;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_DISPLAY_ID")
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_DATASET_FIELD_DISPLAY_ID")
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay() {
		return phenoDataSetFieldDisplay;
	}

	public void setPhenoDataSetFieldDisplay(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay) {
		this.phenoDataSetFieldDisplay = phenoDataSetFieldDisplay;
	}

	@Column(name ="VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Enumerated(EnumType.STRING)
	@Column(name ="OPERATOR")
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Column(name = "SECOND_VALUE")
	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PREFIX")
	public Prefix getPrefix() {
		return prefix;
	}

	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_STATUS_FIELD_ID")
	public ConsentStatusField getConsentStatusField() {
		return consentStatusField;
	}
	
	public void setConsentStatusField(ConsentStatusField consentStatusField) {
		this.consentStatusField = consentStatusField;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_FIELD_ID")
	public BiospecimenField getBiospecimenField() {
		return biospecimenField;
	}

	public void setBiospecimenField(BiospecimenField biospecimenField) {
		this.biospecimenField = biospecimenField;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOCOLLECTION_FIELD_ID")
	public BiocollectionField getBiocollectionField() {
		return biocollectionField;
	}

	public void setBiocollectionField(BiocollectionField biocollectionField) {
		this.biocollectionField = biocollectionField;
	}

//TODO ASAP : Travis Investigate what we might be forcing cascade all here...does it corelate with our DB?  
//What are the effects on updates, deletes, etc
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_ID")
	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

}
