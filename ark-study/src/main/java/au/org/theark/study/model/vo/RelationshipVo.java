package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.Date;

public class RelationshipVo implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private Integer id;
	
	private Integer familyId;
	private String individualId;
	private String gender;
	private String deceased;
	private Date dob;
	
	private String firstName;
	private String lastName;
	private String relationship;
	private String twin;
	
	private String fatherId;
	private String motherId;
	private int relativeIndex;
	
	public RelationshipVo() {
		relationship = "--";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	public String getTwin() {
		return twin;
	}
	
	public void setTwin(String twin) {
		this.twin = twin;
	}
	
	public String getFatherId() {
		return fatherId;
	}
	
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	
	public String getMotherId() {
		return motherId;
	}
	
	public void setMotherId(String motherId) {
		this.motherId = motherId;
	}
	
	public int getRelativeIndex() {
		return relativeIndex;
	}
	public void setRelativeIndex(int relativeIndex) {
		this.relativeIndex = relativeIndex;
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
