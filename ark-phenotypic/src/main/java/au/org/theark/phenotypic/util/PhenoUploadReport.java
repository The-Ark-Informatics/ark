package au.org.theark.phenotypic.util;

import java.sql.Blob;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.hibernate.Hibernate;

import au.org.theark.phenotypic.model.entity.PhenoUpload;

public class PhenoUploadReport
{
	private static StringBuffer report = new StringBuffer();
	private Date		dateNow;

	public PhenoUploadReport(){
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
		PhenoUploadReport.appendAndNewLine("************************************************************");
		PhenoUploadReport.appendAndNewLine("the-ark.org.au Upload Report");
		PhenoUploadReport.appendAndNewLine(dateNow.toString());
		PhenoUploadReport.appendAndNewLine("************************************************************");
	}
	
	/**
	 * @param report the report to set
	 */
	public static void setReport(StringBuffer report)
	{
		PhenoUploadReport.report = report;
	}

	/**
	 * @return the report
	 */
	public static StringBuffer getReport()
	{
		return report;
	}
	
	public static void append(String string)
	{
		report.append(string);
	}
	
	public static void appendAndNewLine(String string)
	{
		report.append(string);
		report.append("\n");
	}
	
	public static void appendDetails(PhenoUpload phenoUpload)
	{
		append("UploadID: ");
		appendAndNewLine(phenoUpload.getId().toString());
		append("Study: ");
		appendAndNewLine(phenoUpload.getStudy().getName());
		append("UserID: ");
		appendAndNewLine(SecurityUtils.getSubject().getPrincipal().toString());
		append("Filename: ");
		appendAndNewLine(phenoUpload.getFilename());
		append("File Format: ");
		appendAndNewLine(phenoUpload.getFileFormat().getName());
		append("Delimiter Type ");
		appendAndNewLine(phenoUpload.getDelimiterType().getName());
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

