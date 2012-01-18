package au.org.theark.core.model.lims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;



/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "BIOCOLLECTIONUID_TOKEN", schema =Constants.LIMS_TABLE_SCHEMA)
public class BioCollectionUidToken implements java.io.Serializable{

	
	private Long	id;
	private String	name;
	
	public BioCollectionUidToken(){
		
	}
	
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length =25)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
