package au.org.theark.core.vo;

import java.io.Serializable;

public class ContactVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private AddressVO addressVo;
	private PhoneVO phoneVo;
	
	public ContactVO() {
		addressVo=new AddressVO();
		phoneVo=new PhoneVO();
	}

	public AddressVO getAddressVo() {
		return addressVo;
	}

	public void setAddressVo(AddressVO addressVo) {
		this.addressVo = addressVo;
	}

	public PhoneVO getPhoneVo() {
		return phoneVo;
	}

	public void setPhoneVo(PhoneVO phoneVo) {
		this.phoneVo = phoneVo;
	}
	
	


}
