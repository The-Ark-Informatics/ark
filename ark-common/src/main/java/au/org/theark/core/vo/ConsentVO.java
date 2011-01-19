package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.Collection;

import au.org.theark.core.model.study.entity.Consent;

/**
 * @author nivedann
 *
 */
public class ConsentVO implements Serializable{
	
	
	protected Consent consent;
	protected Collection<Consent> consentList;
	/**
	 * We will need upload information here
	 */
	
	public ConsentVO(){
		consent = new Consent();
	}
	public Consent getConsent() {
		return consent;
	}
	public void setConsent(Consent consent) {
		this.consent = consent;
	}
	public Collection<Consent> getConsentList() {
		return consentList;
	}
	public void setConsentList(Collection<Consent> consentList) {
		this.consentList = consentList;
	}
	
}
