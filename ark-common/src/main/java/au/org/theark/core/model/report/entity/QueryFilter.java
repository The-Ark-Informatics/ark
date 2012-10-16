package au.org.theark.core.model.report.entity;

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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

@Entity
@Table(name = "guery_filter", schema = Constants.REPORT_SCHEMA)
public class QueryFilter {
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
	
	private DemographicField demographicField;	
	private CustomFieldDisplay customFieldDisplay;
	private String value;
	private String secondValue; // for between and similar operators
	@Enumerated(EnumType.STRING)
	private Operator operator;
// which contraints field, operator, value, nullableSecondValue (eg in between),
   @Enumerated(EnumType.STRING)
	private Prefix prefix; //eg NOT, IN,

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
//		join.
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

	@Column(name ="VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

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

	@Column(name = "PREFIX")
	public Prefix getPrefix() {
		return prefix;
	}

	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}

}
