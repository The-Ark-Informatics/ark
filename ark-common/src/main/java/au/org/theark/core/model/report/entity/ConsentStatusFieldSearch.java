package au.org.theark.core.model.report.entity;

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

@Entity
@Table(name = "consent_status_field_search", schema = Constants.REPORT_SCHEMA)
public class ConsentStatusFieldSearch  implements Serializable  {

	private static final long serialVersionUID = 1L;
	private Long id;
	private ConsentStatusField consentStatusField;
	private Search search;

	public ConsentStatusFieldSearch(){
	}

	public ConsentStatusFieldSearch(ConsentStatusField field, Search searchRef){
		consentStatusField = field;
		search = searchRef;
	}

	@Id
	@SequenceGenerator(name = "consent_status_field_search_generator", sequenceName = "CONSENT_STATUS_FIELD_SEARCH_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "consent_status_field_search_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_ID")
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_STATUS_FIELD_ID")
	public ConsentStatusField getConsentStatusField() {
		return consentStatusField;
	}
	public void setConsentStatusField(ConsentStatusField field) {
		this.consentStatusField = field;
	}
	
}
