package au.org.theark.core.model.report.entity;

public class QueryGroupingGrouping {
	private	Long id;
	private QueryGrouping 	parentGrouping;
	private QueryGrouping	leftGrouping;
	private JoinType 		joinToNextFilter;
	private Long			precedence; //or order...but didn't want to confuse with order by
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public QueryGrouping getParentGrouping() {
		return parentGrouping;
	}
	public void setParentGrouping(QueryGrouping parentGrouping) {
		this.parentGrouping = parentGrouping;
	}
	public QueryGrouping getLeftGrouping() {
		return leftGrouping;
	}
	public void setLeftGrouping(QueryGrouping leftGrouping) {
		this.leftGrouping = leftGrouping;
	}
	public JoinType getJoinToNextFilter() {
		return joinToNextFilter;
	}
	public void setJoinToNextFilter(JoinType joinToNextFilter) {
		this.joinToNextFilter = joinToNextFilter;
	}
	public Long getPrecedence() {
		return precedence;
	}
	public void setPrecedence(Long precedence) {
		this.precedence = precedence;
	}

}
