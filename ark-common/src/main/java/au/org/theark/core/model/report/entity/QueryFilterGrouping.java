package au.org.theark.core.model.report.entity;

public class QueryFilterGrouping {
	QueryGrouping 	parent;
	QueryFilter 	leftFilter;
	FilterJoin 		relationshipToNextFilter;
	Long			filterPrecedence; //or order...but didnt want to confuse with order by

}
