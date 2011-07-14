/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.Phone;

/**
 * @author nivedann
 *
 */
public class PhoneVO implements Serializable{
	
	private Phone phone;
	
	private Collection<Phone> phoneList;
	
	public PhoneVO(){
		phone = new Phone();
		phoneList = new ArrayList<Phone>();
	}
	public Phone getPhone() {
		return phone;
	}
	public void setPhone(Phone phone) {
		this.phone = phone;
	}
	public Collection<Phone> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(Collection<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	
}
