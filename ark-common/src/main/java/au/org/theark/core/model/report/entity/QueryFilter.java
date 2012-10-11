package au.org.theark.core.model.report.entity;

import au.org.theark.core.model.study.entity.CustomFieldDisplay;

public class QueryFilter {
//	QueryFilter leftQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
//	QueryFilter rightQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
//	JoinType filterJoin; //AND OR  
	private Long id;
	// which contraints field, operator, value, nullableSecondValue (eg in between),
	private Prefix prefix; //eg NOT, IN, 
//	int numberOfLeftBracketsBeforePrecursor;
//	int numberOfLeftBracketsAfterPrecursor;
//	int numberOfRightBracketsBeforeJoin;
//	int numberOfRightBracketsAfterJoin;
	
	private DemographicField demographicField;	
	private CustomFieldDisplay customFieldDisplay;
	private String value;
	private Operator operator;
	private String secondValue; // for between and similar operators

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public QueryFilter(){
//		join.
	}


	public DemographicField getDemographicField() {
		return demographicField;
	}


	public void setDemographicField(DemographicField demographicField) {
		this.demographicField = demographicField;
	}

	public Prefix getPrefix() {
		return prefix;
	}

	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}

	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}


}
