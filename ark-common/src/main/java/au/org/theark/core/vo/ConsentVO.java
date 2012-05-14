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
import java.util.Collection;
import java.util.Date;

import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.SubjectFile;

/**
 * @author nivedann
 * 
 */
public class ConsentVO implements Serializable {

	private static final long				serialVersionUID	= -3717906790802004376L;
	protected Consent							consent;
	protected Date								consentDateEnd;
	protected ConsentFile					consentFile;
	protected Collection<Consent>			consentList;
	protected Collection<ConsentFile>	consentFileList;
	protected SubjectFile					subjectFile;

	/**
	 * We will need upload information here
	 */

	public ConsentVO() {
		consent = new Consent();
		subjectFile = new SubjectFile();
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
	
	public SubjectFile getSubjectFile() {
		return subjectFile;
	}

	public void setSubjectFile(SubjectFile subjectFile) {
		this.subjectFile = subjectFile;
	}
}
