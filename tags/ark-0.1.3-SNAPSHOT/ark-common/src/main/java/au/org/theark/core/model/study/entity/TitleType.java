/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "TITLE_TYPE", schema = Constants.STUDY_SCHEMA)
public class TitleType implements Serializable{

	private Long id;
	private String name;
	private String description;
	
	public TitleType(){
		
	}
	
	public TitleType(Long id){
		this.id = id;
	}
	
	@Id
	@Column(name="ID",unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	@Column(name = "NAME", length = 20)
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
