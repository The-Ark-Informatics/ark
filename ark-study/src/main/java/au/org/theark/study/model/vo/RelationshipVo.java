package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.Date;

public class RelationshipVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private Integer familyId;
	private String individualId;
	private String relativeId;
	private String gender;
	private String relativeGender;
	private String deceased;
	private String relativeDeceased;
	private Date dob;
	private Date relativeDob;
	
	public Integer getFamilyId() {
		return familyId;
	}
	public void setFamilyId(Integer familyId) {
		this.familyId = familyId;
	}
	public String getIndividualId() {
		return individualId;
	}
	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}
	public String getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(String relativeId) {
		this.relativeId = relativeId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRelativeGender() {
		return relativeGender;
	}
	public void setRelativeGender(String relativeGender) {
		this.relativeGender = relativeGender;
	}
	public String getDeceased() {
		return deceased;
	}
	public void setDeceased(String deceased) {
		this.deceased = deceased;
	}
	public String getRelativeDeceased() {
		return relativeDeceased;
	}
	public void setRelativeDeceased(String relativeDeceased) {
		this.relativeDeceased = relativeDeceased;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Date getRelativeDob() {
		return relativeDob;
	}
	public void setRelativeDob(Date relativeDob) {
		this.relativeDob = relativeDob;
	}
	
	
	
}
