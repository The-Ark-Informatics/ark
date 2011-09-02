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

import javax.persistence.Column;

import au.org.theark.core.model.study.entity.StudyConsentQuestion;

/**
 * @author nivedann
 * 
 */
public class StudyConsentQuestionVO implements Serializable {

	private StudyConsentQuestion	studyConsentQuestion;
	private Boolean					consentStatus;

	public StudyConsentQuestionVO() {

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
