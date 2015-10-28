package au.org.theark.core.vo;

import java.util.Date;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneStatus;
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
	public String getSubjectUID() {
		return subjectUID;
	}
	public String getAreaCode() {
		return phone.getAreaCode();
	}
	public String getPhoneNumber() {
		return phone.getPhoneNumber();
	}
	public PhoneType getPhoneType(){
		return phone.getPhoneType();
	}
	public PhoneStatus getPhoneStatus(){
		return phone.getPhoneStatus();
	}
	public Date getValidFrom(){
		return phone.getValidFrom();
	}
	public Date getValidTo(){
		return phone.getValidTo();
	}
}
