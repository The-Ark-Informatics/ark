package au.org.theark.core.model.report.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import au.org.theark.core.model.Constants;

@Entity
@Table(name = "guery_filter_grouping", schema = Constants.REPORT_SCHEMA)
public class QueryFilterGrouping  implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private QueryGrouping 	parentGrouping;
	private QueryFilter 	leftFilter;
	private JoinType 		joinToNextFilter;
	private Long			precedence; //or order...but didn't want to confuse with order by


	@Id
	@SequenceGenerator(name = "query_filter_grouping_generator", sequenceName = "QUERY_FILTER_GROUPING_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "query_filter_grouping_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_GROUPING_ID")
	public QueryGrouping getParentGrouping() {
		return parentGrouping;
	}
	public void setParentGrouping(QueryGrouping parentGrouping) {
		this.parentGrouping = parentGrouping;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LEFT_FILTER_ID")
	public QueryFilter getLeftFilter() {
		return leftFilter;
	}
	public void setLeftFilter(QueryFilter leftFilter) {
		this.leftFilter = leftFilter;
	}
	
	@Enumerated(EnumType.STRING)
	@JoinColumn(name = "JOIN_TO_NEXT_FILTER")
	public JoinType getJoinToNextFilter() {
		return joinToNextFilter;
	}
	public void setJoinToNextFilter(JoinType joinToNextFilter) {
		this.joinToNextFilter = joinToNextFilter;
	}

	@JoinColumn(name = "PRECEDENCE")
	public Long getPrecedence() {
		return precedence;
	}
	public void setPrecedence(Long precedence) {
		this.precedence = precedence;
	}

}
