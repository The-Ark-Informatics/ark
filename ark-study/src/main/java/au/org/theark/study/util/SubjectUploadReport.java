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
package au.org.theark.study.util;

import java.sql.Blob;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.dao.LobUtil;
import au.org.theark.core.model.study.entity.StudyUpload;

public class SubjectUploadReport {
	private StringBuffer	report	= new StringBuffer();
	private Date			dateNow;

	@SpringBean(name = "lobUtil")
	private LobUtil			util;
	
	public SubjectUploadReport() {
		/*
		 * Example report:
		 * 
		 * ********************************************** the-ark.org.au Upload Report 2011-01-14 13:52:01*********************************************
		 * UploadID: 15 Study: ATR-1 UserID: arksadmin@uwa.edu.au Filename: test.csv File Format: CSV Delimiter Type: COMMA
		 */

		dateNow = new Date(System.currentTimeMillis());
		this.appendAndNewLine("************************************************************");
		this.appendAndNewLine("the-ark.org.au Upload Report");
		this.appendAndNewLine(dateNow.toString());
		this.appendAndNewLine("************************************************************");
	}

	/**
	 * @param report
	 *           the report to set
	 */
	public void setReport(StringBuffer report) {
		this.report = report;
	}

	/**
	 * @return the report
	 */
	public StringBuffer getReport() {
		return report;
	}

	public void append(String string) {
		report.append(string);
	}

	public void appendAndNewLine(String string) {
		report.append(string);
		report.append("\n");
	}

	public void appendDetails(StudyUpload studyUpload) {
		append("Study: ");
		appendAndNewLine(studyUpload.getStudy().getName());
		append("UserID: ");
		appendAndNewLine(SecurityUtils.getSubject().getPrincipal().toString());
		append("Filename: ");
		appendAndNewLine(studyUpload.getFilename());
		append("File Format: ");
		appendAndNewLine(studyUpload.getFileFormat().getName());
		append("Delimiter Type ");
		appendAndNewLine(studyUpload.getDelimiterType().getName());
	}

	public void appendDetails(StudyUpload studyUpload, String studyName) {
		append("Study: ");
		appendAndNewLine(studyName);
		append("UserID: ");
		appendAndNewLine(SecurityUtils.getSubject().getPrincipal().toString());
		append("Filename: ");
		appendAndNewLine(studyUpload.getFilename());
		append("File Format: ");
		appendAndNewLine(studyUpload.getFileFormat().getName());
		append("Delimiter Type ");
		appendAndNewLine(studyUpload.getDelimiterType().getName());
	}

	public Blob getReportAsBlob() {
		Blob reportAsBlob = util.createBlob(this.getInputStream());
		return reportAsBlob;
	}

	private byte[] getInputStream() {
		return report.toString().getBytes();
	};
}
