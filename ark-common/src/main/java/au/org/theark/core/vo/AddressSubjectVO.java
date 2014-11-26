package au.org.theark.core.vo;

import java.util.Date;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.State;


public class AddressSubjectVO {
	
	private Address address;
	private String subjectUID;

	public AddressSubjectVO(Address address,String subjectUID){
		this.address=address;
		this.subjectUID=subjectUID;
	}
	public String getStreetAddress() {
		return address.getStreetAddress();
	}
	public String getPostCode() {
		return address.getPostCode();
	}
	public String getCity() {
		return address.getCity();
	}
	public State getState() {
		return address.getState();
	}
	public AddressType getAddressType() {
		return address.getAddressType();
	}
	public Date getDateReceived() {
		return address.getDateReceived();
	}
	public Boolean getPreferredMailingAddress() {
		return address.getPreferredMailingAddress();
	}
	public String getOtherState() {
		return address.getOtherState();
	}
	public Country getCountry() {
		return address.getCountry();
	}
	public String getSubjectUID() {
		return subjectUID;
	}
}
