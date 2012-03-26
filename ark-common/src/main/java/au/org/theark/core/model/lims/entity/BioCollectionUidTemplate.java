package au.org.theark.core.model.lims.entity;

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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "BIOCOLLECTIONUID_TEMPLATE", schema = Constants.LIMS_TABLE_SCHEMA)
public class BioCollectionUidTemplate implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long						id;
	private Study						study;
	private BioCollectionUidToken		bioCollectionUidToken;
	private BioCollectionUidPadChar		bioCollectionUidPadChar;
	private String						bioCollectionUidPrefix;
	
	public BioCollectionUidTemplate(){
		
	}


	// Property accessors
	@Id
	@SequenceGenerator(name = "biocollectionUid_template", sequenceName = "biocollectionUid_template_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biocollectionUid_template")
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
	@JoinColumn(name = "BIOCOLLECTIONUID_TOKEN_ID")
	public BioCollectionUidToken getBioCollectionUidToken() {
		return bioCollectionUidToken;
	}


	public void setBioCollectionUidToken(BioCollectionUidToken bioCollectionUidToken) {
		this.bioCollectionUidToken = bioCollectionUidToken;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOCOLLECTIONUID_PADCHAR_ID")
	public BioCollectionUidPadChar getBioCollectionUidPadChar() {
		return bioCollectionUidPadChar;
	}


	public void setBioCollectionUidPadChar(
			BioCollectionUidPadChar bioCollectionUidPadChar) {
		this.bioCollectionUidPadChar = bioCollectionUidPadChar;
	}

	@Column(name = "BIOCOLLECTIONUID_PREFIX")
	public String getBioCollectionUidPrefix() {
		return bioCollectionUidPrefix;
	}


	public void setBioCollectionUidPrefix(String bioCollectionUidPrefix) {
		this.bioCollectionUidPrefix = bioCollectionUidPrefix;
	}

}
