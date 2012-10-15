package au.org.theark.core.model.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

@Entity
@Table(name = "guery_grouping", schema = Constants.REPORT_SCHEMA)
public class QueryGrouping {
	private Long id;
	private String name;

	@Id
	@SequenceGenerator(name = "query_grouping_generator", sequenceName = "QUERY_GROUPING_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "query_grouping_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
