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
	private String gender;
	private String deceased;
	private Date dob;
	
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDeceased() {
		return deceased;
	}
	public void setDeceased(String deceased) {
		this.deceased = deceased;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((individualId == null) ? 0 : individualId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelationshipVo other = (RelationshipVo) obj;
		if (individualId == null) {
			if (other.individualId != null)
				return false;
		}
		else if (!individualId.equals(other.individualId))
			return false;
		return true;
	}	
	
}
