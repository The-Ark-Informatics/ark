package au.org.theark.study.util;

import java.util.Comparator;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * 
 * Check any changes made to Consent History properties in {@link LinkSubjectStudy} object.
 *
 */
public class LinkSubjectStudyConsentHistoryComparator implements Comparator<LinkSubjectStudy> {
	
	
	/**
	 * Check any changes made to subject's consent history properties.
	 * 
	 */
	public final int compare(LinkSubjectStudy oldSubject, LinkSubjectStudy newSubject) {
		// TODO Auto-generated method stub
		int result=0;
		if(oldSubject != null &&
				newSubject!=null){			
			if((oldSubject.getConsentDate() ==null && newSubject.getConsentDate()!=null) ||
					(oldSubject.getConsentDate()!=null && !oldSubject.getConsentDate().equals(newSubject.getConsentDate())) 
					){
				return ++result;
			}
			else if((oldSubject.getConsentStatus() ==null && newSubject.getConsentStatus()!=null) ||
							(oldSubject.getConsentStatus()!=null && !oldSubject.getConsentStatus().getId().equals(newSubject.getConsentStatus().getId()))){
				return ++result;
			}else if((oldSubject.getConsentType() ==null && newSubject.getConsentType()!=null)||
							(oldSubject.getConsentType()!=null && !oldSubject.getConsentType().getId().equals(newSubject.getConsentType().getId()))){
				return ++result;
			}
			else if((oldSubject.getConsentDownloaded() ==null && newSubject.getConsentDownloaded()!=null) ||	
					(oldSubject.getConsentDownloaded()!=null && !oldSubject.getConsentDownloaded().getId().equals(newSubject.getConsentDownloaded().getId()))){
				return ++result;
			}
			else if((oldSubject.getConsentToPassiveDataGathering() ==null && newSubject.getConsentToPassiveDataGathering()!=null)||
					(oldSubject.getConsentToPassiveDataGathering()!=null && !oldSubject.getConsentToPassiveDataGathering().getId().equals(newSubject.getConsentToPassiveDataGathering().getId()))){
				return ++result;
			}
			else if((oldSubject.getConsentToActiveContact() ==null && newSubject.getConsentToActiveContact()!=null) ||
					(oldSubject.getConsentToActiveContact()!=null && !oldSubject.getConsentToActiveContact().getId().equals(newSubject.getConsentToActiveContact().getId()))){
				return ++result;
			}
			else if((oldSubject.getConsentToUseData() ==null && newSubject.getConsentToUseData()!=null) ||
					(oldSubject.getConsentToUseData()!=null && !oldSubject.getConsentToUseData().getId().equals(newSubject.getConsentToUseData().getId()))){
				return ++result;
			}
		}
		return result;
	}
	 
}
