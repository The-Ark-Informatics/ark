package au.org.theark.core.model.geno.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;


@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "PROCESS", schema = Constants.GENO_SCHEMA)
public class Process implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Command command;
	private Date startTime;//TODO: 
	private Date endTime;
	private Pipeline pipeline;

	private Set<ProcessInput> processInputs = new HashSet<ProcessInput>(0);
	private Set<ProcessOutput> processOutputs = new HashSet<ProcessOutput>(0);
	

	@Id
	@SequenceGenerator(name = "process_generator", sequenceName = "PROCESS_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "process_generator")
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
	

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COMMAND_ID")
	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", nullable = false, length = 19)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", nullable = false, length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/* TODO: confirm the need for eager */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PIPELINE_ID")
	public Pipeline getPipeline() {
		return pipeline;
	}

	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "process")
	public Set<ProcessOutput> getProcessOutputs() {
		return this.processOutputs;
	}

	public void setProcessOutputs(Set<ProcessOutput> processOutputs) {
		this.processOutputs = processOutputs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "process")
	public Set<ProcessInput> getProcessInputs() {
		return this.processInputs;
	}

	public void setProcessInputs(Set<ProcessInput> processInputs) {
		this.processInputs = processInputs;
	}

	
}
