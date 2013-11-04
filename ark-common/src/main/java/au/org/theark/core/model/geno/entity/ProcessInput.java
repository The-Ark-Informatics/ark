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
@Table(name = "PROCESS_INPUT", schema = Constants.GENO_SCHEMA)
public class ProcessInput implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Process process;
	private String inputFileLocation;
	private String inputFileHash;
	private String inputFileType;
	private int inputKept;
	private String inputServer;
	
	@Id
	@SequenceGenerator(name = "process_input_generator", sequenceName = "PROCESS_INPUT_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "process_input_generator")
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
	
	@Column(name = "INPUT_FILE_LOCATION", length = 255)
	public String getinputFileLocation() {
		return inputFileLocation;
	}
	
	public void setinputFileLocation(String inputFileLocation) {
		this.inputFileLocation = inputFileLocation;
	}
	
	@Column(name = "INPUT_FILE_HASH", length = 255)
	public String getInputFileHash() {
		return inputFileHash;
	}
	
	public void setInputFileHash(String InputFileHash) {
		this.inputFileHash = InputFileHash;
	}
	
	@Column(name = "INPUT_FILE_TYPE", length = 255)
	public String getInputFileType() {
		return inputFileType;
	}
	public void setInputFileType(String InputFileType) {
		this.inputFileType = InputFileType;
	}
	
	@Column(name = "INPUT_KEPT")
	public int getInputKept() {
		return inputKept;
	}
	
	public void setInputKept(int inputKept) {
		this.inputKept = inputKept;
	}
	
	@Column(name = "INPUT_SERVER", length = 255)
	public String getInputServer() {
		return inputServer;
	}
	
	public void setInputServer(String inputServer) {
		this.inputServer = inputServer;
	}
}
