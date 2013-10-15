package au.org.theark.core.model.geno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;


@Entity
@Table(name = "COMMAND", schema = Constants.GENO_SCHEMA)
public class Command implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String serverURL;
	private String location;
	private String inputFileFormat;//TODO: 
	private String outputFileFormat;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
/*
	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
*/

}
