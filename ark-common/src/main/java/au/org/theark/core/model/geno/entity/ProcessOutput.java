package au.org.theark.core.model.geno.entity;

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

import au.org.theark.core.Constants;


@Entity
@Table(name = "PROCESS_OUTPUT", schema = Constants.GENO_SCHEMA)
public class ProcessOutput implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Process process;
	private String outputFileLocation;
	private String outputFileHash;
	private String outputFileType;
	private int outputKept;
	private String outputServer;
	
	@Id
	@SequenceGenerator(name = "process_output_generator", sequenceName = "PROCESS_OUTPUT_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "process_output_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESS_ID")
	public Process getProcess() {
		return process;
	}
	public void setProcess(Process process) {
		this.process = process;
	}
	
	@Column(name = "OUTPUT_FILE_LOCATION", length = 255)
	public String getOutputFileLocation() {
		return outputFileLocation;
	}
	
	public void setOutputFileLocation(String outputFileLocation) {
		this.outputFileLocation = outputFileLocation;
	}
	
	@Column(name = "OUTPUT_FILE_HASH", length = 255)
	public String getOutputFileHash() {
		return outputFileHash;
	}
	
	public void setOutputFileHash(String outputFileHash) {
		this.outputFileHash = outputFileHash;
	}
	
	@Column(name = "OUTPUT_FILE_TYPE", length = 255)
	public String getOutputFileType() {
		return outputFileType;
	}
	public void setOutputFileType(String outputFileType) {
		this.outputFileType = outputFileType;
	}
	
	@Column(name = "OUTPUT_KEPT")
	public int getOutputKept() {
		return outputKept;
	}
	
	public void setOutputKept(int outputKept) {
		this.outputKept = outputKept;
	}
	
	@Column(name = "OUTPUT_SERVER", length = 255)
	public String getOutputServer() {
		return outputServer;
	}
	
	public void setOutputServer(String outputServer) {
		this.outputServer = outputServer;
	}
}
