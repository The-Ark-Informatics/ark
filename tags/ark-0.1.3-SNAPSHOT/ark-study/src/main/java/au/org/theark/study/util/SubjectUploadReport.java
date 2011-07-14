package au.org.theark.study.util;

import java.sql.Blob;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.hibernate.Hibernate;

import au.org.theark.core.model.study.entity.StudyUpload;


public class SubjectUploadReport
{
	private StringBuffer report = new StringBuffer();
	private Date		dateNow;

	public SubjectUploadReport(){
		/*
		 * Example report:
		 * 
		 * **********************************************
			the-ark.org.au Upload Report
			2011-01-14 13:52:01
			**********************************************
			UploadID: 15
			Study: ATR-1
			UserID: arksadmin@uwa.edu.au
			Filename: test.csv
			File Format: CSV
			Delimiter Type: COMMA
		 * 
		 */
		
		dateNow = new Date(System.currentTimeMillis());
		this.appendAndNewLine("************************************************************");
		this.appendAndNewLine("the-ark.org.au Upload Report");
		this.appendAndNewLine(dateNow.toString());
		this.appendAndNewLine("************************************************************");
	}
	
	/**
	 * @param report the report to set
	 */
	public void setReport(StringBuffer report)
	{
		this.report = report;
	}

	/**
	 * @return the report
	 */
	public StringBuffer getReport()
	{
		return report;
	}
	
	public void append(String string)
	{
		report.append(string);
	}
	
	public void appendAndNewLine(String string)
	{
		report.append(string);
		report.append("\n");
	}
	
	public void appendDetails(StudyUpload studyUpload)
	{
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
	
	public Blob getReportAsBlob()
	{
		Blob reportAsBlob = Hibernate.createBlob(this.getInputStream());
		return reportAsBlob;
	}

	private byte[] getInputStream()
	{
		return report.toString().getBytes();
	};
}