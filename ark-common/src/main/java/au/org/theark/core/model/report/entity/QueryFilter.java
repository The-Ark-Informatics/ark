package au.org.theark.core.model.report.entity;

import au.org.theark.core.model.study.entity.CustomFieldDisplay;

public class QueryFilter {
//	QueryFilter leftQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
//	QueryFilter rightQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
//	JoinType filterJoin; //AND OR  
	
	// which contraints field, operator, value, nullableSecondValue (eg in between),
	Prefix prefix; //eg NOT, IN, 
//	int numberOfLeftBracketsBeforePrecursor;
//	int numberOfLeftBracketsAfterPrecursor;
//	int numberOfRightBracketsBeforeJoin;
//	int numberOfRightBracketsAfterJoin;
	
	DemographicField demographicField;	
	CustomFieldDisplay customFieldDisplay;
	String value;
	Operator operator;
	String secondValue; // for between and similar operators

	
	public QueryFilter(){
//		join.
	}
}
