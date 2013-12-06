package au.org.theark.core.model.geno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;


@Entity
@Table(name = "COMMAND", schema = Constants.GENO_SCHEMA)
public class Command implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String serverUrl;
	private String location;
	private String inputFileFormat;//TODO: 
	private String outputFileFormat;

	@Id
	@SequenceGenerator(name = "command_generator", sequenceName = "COMMAND_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "command_generator")
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

	@Column(name = "LOCATION", length = 100)
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "SERVER_URL", length = 100)
	public String getServerUrl() {
		return this.serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	@Column(name = "INPUT_FILE_FORMAT", length = 100)
	public String getInputFileFormat() {
		return this.inputFileFormat;
	}

	public void setInputFileFormat(String inputFileFormat) {
		this.inputFileFormat = inputFileFormat;
	}

	@Column(name = "OUTPUT_FILE_FORMAT", length = 100)
	public String getOutputFileFormat() {
		return this.outputFileFormat;
	}

	public void setOutputFileFormat(String outputFileFormat) {
		this.outputFileFormat = outputFileFormat;
	}
	
	
}
