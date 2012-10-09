package au.org.theark.core.model.report.entity;

public class QueryGroupingGrouping {
	QueryGrouping 	parent;
	QueryGrouping	leftGrouping;
		
	FilterJoin 		relationshipToNextFilter;
	Long			filterPrecedence; //or order...but didnt want to confuse with order by

}
