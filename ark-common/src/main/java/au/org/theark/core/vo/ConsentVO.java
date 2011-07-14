package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;

/**
 * @author nivedann
 *
 */
public class ConsentVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3717906790802004376L;
	protected Consent consent;
	protected Date consentDateEnd; 
	protected ConsentFile consentFile;
	protected Collection<Consent> consentList;
	protected Collection<ConsentFile> consentFileList;
	
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
	public Date getConsentDateEnd() {
		return consentDateEnd;
	}
	public void setConsentDateEnd(Date consentDateEnd) {
		this.consentDateEnd = consentDateEnd;
	}
	public ConsentFile getConsentFile() {
		return consentFile;
	}
	public void setConsentFile(ConsentFile consentFile) {
		this.consentFile = consentFile;
	}
	public Collection<ConsentFile> getConsentFileList() {
		return consentFileList;
	}
	public void setConsentFileList(Collection<ConsentFile> consentFileList) {
		this.consentFileList = consentFileList;
	}
	
}
