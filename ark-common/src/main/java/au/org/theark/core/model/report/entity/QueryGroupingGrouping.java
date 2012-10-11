package au.org.theark.core.model.report.entity;

public class QueryGroupingGrouping {
	private	Long id;
	private QueryGrouping 	parent;
	private QueryGrouping	leftGrouping;
	private JoinType 		relationshipToNextFilter;
	private Long			filterPrecedence; //or order...but didn't want to confuse with order by
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public QueryGrouping getParent() {
		return parent;
	}
	public void setParent(QueryGrouping parent) {
		this.parent = parent;
	}
	public QueryGrouping getLeftGrouping() {
		return leftGrouping;
	}
	public void setLeftGrouping(QueryGrouping leftGrouping) {
		this.leftGrouping = leftGrouping;
	}
	public JoinType getRelationshipToNextFilter() {
		return relationshipToNextFilter;
	}
	public void setRelationshipToNextFilter(JoinType relationshipToNextFilter) {
		this.relationshipToNextFilter = relationshipToNextFilter;
	}
	public Long getFilterPrecedence() {
		return filterPrecedence;
	}
	public void setFilterPrecedence(Long filterPrecedence) {
		this.filterPrecedence = filterPrecedence;
	}

}
