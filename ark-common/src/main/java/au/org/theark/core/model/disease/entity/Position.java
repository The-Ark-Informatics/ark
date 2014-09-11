package au.org.theark.core.model.disease.entity;

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

import au.org.theark.core.Constants;

@Entity
@Table(name = "POSITION", schema = Constants.DISEASE_SCHEMA)
public class Position implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Gene gene; //not sure if needed. Maybe just for easier navigation
	
	public Position() {
		
	}
	
	public Position(Long id) {
		this.id = id;
	}
	
	@Id
	@SequenceGenerator(name = "position_generator", sequenceName = "POSITION_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "position_generator")
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GENE_ID")
	public Gene getGene(){
		return this.gene;
	}
	
	public void setGene(Gene gene) {
		this.gene = gene;
	}

	public String toString() {
		return "Position[id=" + id + ", name=" + name + "]";
	}
	
}
