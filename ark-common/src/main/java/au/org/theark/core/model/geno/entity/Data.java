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
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;


@Entity
@Table(name = "DATA", schema = Constants.GENO_SCHEMA)
public class Data implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Study study;
	private Person person;
	private Row row;
	private Beam beam;
	private String value;
	
	public Data() {
		super();
	}
	
	public Data(Study study, Person person, Row row, Beam beam) {
		super();
		this.study = study;
		this.person = person;
		this.row = row;
		this.beam = beam;
		this.value = new String();
	}

	@Id
	@SequenceGenerator(name = "row_generator", sequenceName = "ROW_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "row_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}
	
	public void setStudy(Study study) {
		this.study = study;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROW_ID")
	public Row getRow() {
		return row;
	}
	
	public void setRow(Row row) {
		this.row = row;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BEAM_ID")
	public Beam getBeam() {
		return beam;
	}
	
	public void setBeam(Beam column) {
		this.beam = column;
	}
	
	@JoinColumn(name = "VALUE")
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Data [id=" + id + ", study=" + study + ", person=" + person
				+ ", row=" + row + ", column=" + beam + ", value=" + value
				+ "]";
	}
	
}