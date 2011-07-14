/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;

import javax.persistence.Column;

import au.org.theark.core.model.study.entity.StudyConsentQuestion;

/**
 * @author nivedann
 *
 */
public class StudyConsentQuestionVO implements Serializable{
	
	private StudyConsentQuestion studyConsentQuestion;
	private Boolean consentStatus;
	
	public StudyConsentQuestionVO(){
		
	}

	public StudyConsentQuestion getStudyConsentQuestion() {
		return studyConsentQuestion;
	}

	public void setStudyConsentQuestion(StudyConsentQuestion studyConsentQuestion) {
		this.studyConsentQuestion = studyConsentQuestion;
	}

	public Boolean getConsentStatus() {
		return consentStatus;
	}

	public void setConsentStatus(Boolean consentStatus) {
		this.consentStatus = consentStatus;
	}

}
