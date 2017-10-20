package au.org.theark.web.rest.model;

import java.util.Date;

public class MadelineObject {
	
	private String 	familyId;
	private String 	individualId;
	private String 	gender;
	private String 	father;
	private String 	mother;
	private String 	affected;
	//private Boolean dZTwin;
	//private Boolean mZTwin;
	private String 	zygosity;
	private Date 	dOB;
	private String 	proband;
	private String 	deceased;
	
	
	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getAffected() {
		return affected;
	}

	public void setAffected(String affected) {
		this.affected = affected;
	}

	public String getZygosity() {
		return zygosity;
	}

	public void setZygosity(String zygosity) {
		this.zygosity = zygosity;
	}

	public Date getdOB() {
		return dOB;
	}

	public void setdOB(Date dOB) {
		this.dOB = dOB;
	}

	public String getProband() {
		return proband;
	}

	public void setProband(String proband) {
		this.proband = proband;
	}

	public String getDeceased() {
		return deceased;
	}

	public void setDeceased(String deceased) {
		this.deceased = deceased;
	}

}
