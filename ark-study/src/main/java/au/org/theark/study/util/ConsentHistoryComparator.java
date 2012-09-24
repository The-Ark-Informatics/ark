package au.org.theark.study.util;

import java.util.Comparator;

import au.org.theark.core.model.study.entity.Consent;

/**
 * 
 * Check any changes made to  {@link Consent} object.
 *
 */
public class ConsentHistoryComparator implements Comparator<Consent> {

	/**
	 * Check any changes made to existing  {@link Consent} object.
	 */
	public final int compare(Consent prevConsent, Consent newConsent) {
		int result=0;
		if(prevConsent != null &&
				newConsent!=null){			
			if((prevConsent.getConsentDate() ==null && newConsent.getConsentDate()!=null) ||
					(prevConsent.getConsentDate()!=null && !prevConsent.getConsentDate().equals(newConsent.getConsentDate()))){
				return ++result;
			}
			else if((prevConsent.getConsentStatus() ==null && newConsent.getConsentStatus()!=null) ||
							(prevConsent.getConsentStatus()!=null && !prevConsent.getConsentStatus().getId().equals(newConsent.getConsentStatus().getId()))){
				return ++result;
			}
			else if((prevConsent.getConsentType() ==null && newConsent.getConsentType()!=null)||
							(prevConsent.getConsentType()!=null && !prevConsent.getConsentType().getId().equals(newConsent.getConsentType().getId()))){
				return ++result;
			}
			else if((prevConsent.getConsentDownloaded() ==null && newConsent.getConsentDownloaded()!=null) ||	
					(prevConsent.getConsentDownloaded()!=null && !prevConsent.getConsentDownloaded().getId().equals(newConsent.getConsentDownloaded().getId()))){
				return ++result;
			}
			else if((prevConsent.getStudyComponentStatus() ==null && newConsent.getStudyComponentStatus()!=null)||
					(prevConsent.getStudyComponentStatus()!=null && !prevConsent.getStudyComponentStatus().getId().equals(newConsent.getStudyComponentStatus().getId()))){
				return ++result;
			}
			else if((prevConsent.getCompletedDate() ==null && newConsent.getCompletedDate()!=null) ||
					(prevConsent.getCompletedDate()!=null && !prevConsent.getCompletedDate().equals(newConsent.getCompletedDate()))){
				return ++result;
			}
			else if((prevConsent.getConsentedBy() ==null && newConsent.getConsentedBy()!=null) ||
					(prevConsent.getConsentedBy()!=null && !prevConsent.getConsentedBy().equals(newConsent.getConsentedBy()))){
				return ++result;
			}
			else if((prevConsent.getComments() ==null && newConsent.getComments()!=null) ||
					(prevConsent.getComments()!=null && !prevConsent.getComments().equals(newConsent.getComments()))){
				return ++result;
			}
		}
		return result;
	}

}
