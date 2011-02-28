/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.EmailAccount;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.StudyConsentQuestion;
import au.org.theark.core.model.study.entity.SubjectStatus;



/**
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class SubjectVO implements Serializable{
	
	private String subjectFullName;
	public String getSubjectFullName() {
		return subjectFullName;
	}

	public void setSubjectFullName(String subjectFullName) {
		this.subjectFullName = subjectFullName;
	}


	protected Collection<LinkSubjectStudy> participants;
	
	public Collection<LinkSubjectStudy> getParticipants() {
		return participants;
	}

	public void setParticipants(Collection<LinkSubjectStudy> participants) {
		this.participants = participants;
	}


	protected SubjectStatus subjectStatus;
	protected String subjectUID;

	protected LinkSubjectStudy subjectStudy;
	
	public LinkSubjectStudy getSubjectStudy() {
		return subjectStudy;
	}

	public void setSubjectStudy(LinkSubjectStudy subjectStudy) {
		this.subjectStudy = subjectStudy;
	}

	protected Collection<SubjectVO> subjectList;

	/** A List of phone numbers linked to this person/subject*/
	protected Collection<Phone> phoneList;
	/** A List of Address linked to this person/subject*/
	protected Collection<Address> addressList;
	/** A List of Email account linked to this person/subject*/
	protected Collection<EmailAccount> emailAccountList;
	
	protected Collection<StudyConsentQuestion> consentQuestions;
	
	private ConsentAnswer consentAnswerSelect;
	
	/**
	 * Constructor
	 */
	public SubjectVO(){
		phoneList = new ArrayList<Phone>();
		addressList = new ArrayList<Address>();
		emailAccountList = new ArrayList<EmailAccount>();
		subjectUID = new String();
		subjectStudy = new LinkSubjectStudy();
	}

	

	public Collection<Phone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(Collection<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	public Collection<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(Collection<Address> addressList) {
		this.addressList = addressList;
	}

	public Collection<EmailAccount> getEmailAccountList() {
		return emailAccountList;
	}

	public void setEmailAccountList(Collection<EmailAccount> emailAccountList) {
		this.emailAccountList = emailAccountList;
	}

	public SubjectStatus getSubjectStatus() {
		return subjectStatus;
	}

	public void setSubjectStatus(SubjectStatus subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

	public Collection<SubjectVO> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(Collection<SubjectVO> subjectList) {
		this.subjectList = subjectList;
	}

	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

	public ConsentAnswer getConsentAnswerSelect() {
		return consentAnswerSelect;
	}

	public void setConsentAnswerSelect(ConsentAnswer consentAnswerSelect) {
		this.consentAnswerSelect = consentAnswerSelect;
	}


}
