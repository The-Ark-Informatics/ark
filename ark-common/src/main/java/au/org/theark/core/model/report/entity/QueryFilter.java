package au.org.theark.core.model.report.entity;

public class QueryFilter {
//	QueryFilter leftQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
//	QueryFilter rightQueryFilter;	//potentially link to another query to combine rules on...its more explicit than relying on brackets.
	JoinType filterJoin; //AND OR  
	
	// which contraints field, operator, value, nullableSecondValue (eg in between),
	Preprocessor precursor; //eg NOT, IN, 
//	int numberOfLeftBracketsBeforePrecursor;
//	int numberOfLeftBracketsAfterPrecursor;
//	int numberOfRightBracketsBeforeJoin;
//	int numberOfRightBracketsAfterJoin;
	String value;
	Operator operator;
	String secondValue; // for between and similar operators

	
	public QueryFilter(){
//		join.
	}
}
