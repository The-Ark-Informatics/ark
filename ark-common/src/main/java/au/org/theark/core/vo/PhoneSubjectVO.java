package au.org.theark.core.vo;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;

public class PhoneSubjectVO {
	
	private Phone phone;
	private String subjectUID;
	
	
	public PhoneSubjectVO(Phone phone,String subjectUID){
		this.phone=phone;
		this.subjectUID=subjectUID;
		
	}
	public Long getId() {
		return phone.getId();
	}

	public PhoneType getPhoneType() {
		return phone.getPhoneType();
	}

	public String getPhoneNumber() {
		return phone.getPhoneNumber();
	}
	public String getAreaCode() {
		return phone.getAreaCode();
	}
	public String getSubjectUID() {
		return subjectUID;
	}

}
