/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.model.study.entity.SubjectStatus;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings("serial")
public class SubjectVO implements Serializable {

	protected String										subjectFullName;
	protected SubjectStatus								subjectStatus;
	protected LinkSubjectStudy							linkSubjectStudy;
	protected Collection<SubjectVO>					subjectList;
	protected SubjectFile								subjectFile;
	protected ConsentAnswer								consentAnswerSelect;
	protected String										subjectPreviousLastname;

	/** A List of phone numbers linked to this person/subject */
	protected Collection<Phone>						phoneList;
	/** A List of Address linked to this person/subject */
	protected Collection<Address>						addressList;
	/** A List of Email account linked to this person/subject */
	protected Collection<EmailAccount>				emailAccountList;

	protected Collection<StudyConsentQuestion>	consentQuestions;

	/** A List of Files linked to this person/subject */
	protected Collection<SubjectFile>				subjectFileList;

	/**
	 * Constructor
	 */
	public SubjectVO() {
		phoneList = new ArrayList<Phone>();
		addressList = new ArrayList<Address>();
		emailAccountList = new ArrayList<EmailAccount>();
		linkSubjectStudy = new LinkSubjectStudy();
		subjectFile = new SubjectFile();
		subjectFileList = new ArrayList<SubjectFile>();
		subjectPreviousLastname = new String();
	}

	public String getSubjectFullName() {
		return subjectFullName;
	}

	public void setSubjectFullName(String subjectFullName) {
		this.subjectFullName = subjectFullName;
	}

	protected Collection<LinkSubjectStudy>	participants;

	public Collection<LinkSubjectStudy> getParticipants() {
		return participants;
	}

	public void setParticipants(Collection<LinkSubjectStudy> participants) {
		this.participants = participants;
	}

	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
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

	public SubjectFile getSubjectFile() {
		return subjectFile;
	}

	public void setSubjectFile(SubjectFile subjectFile) {
		this.subjectFile = subjectFile;
	}

	public ConsentAnswer getConsentAnswerSelect() {
		return consentAnswerSelect;
	}

	public void setConsentAnswerSelect(ConsentAnswer consentAnswerSelect) {
		this.consentAnswerSelect = consentAnswerSelect;
	}

	/**
	 * @param subjectPreviousLastname
	 *           the subjectPreviousLastname to set
	 */
	public void setSubjectPreviousLastname(String subjectPreviousLastname) {
		this.subjectPreviousLastname = subjectPreviousLastname;
	}

	/**
	 * @return the subjectPreviousLastname
	 */
	public String getSubjectPreviousLastname() {
		return subjectPreviousLastname;
	}

	public Collection<SubjectFile> getSubjectFileList() {
		return subjectFileList;
	}

	public void setSubjectFileList(Collection<SubjectFile> subjectFileList) {
		this.subjectFileList = subjectFileList;
	}
}
