package au.org.theark.core.vo;

import java.io.Serializable;

public class ContactVO implements ArkVo,Serializable {
	
	private static final long serialVersionUID = 1L;
	private AddressVO addressVo;
	private PhoneVO phoneVo;
	private EmailAccountVo emailAccountVo;
	private String objectId;
	
	public ContactVO() {
		addressVo=new AddressVO();
		phoneVo=new PhoneVO();
		emailAccountVo = new EmailAccountVo();
		this.objectId = "Contact";
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

	public EmailAccountVo getEmailAccountVo() {
		return emailAccountVo;
	}

	public void setEmailAccountVo(EmailAccountVo emailAccountVo) {
		this.emailAccountVo = emailAccountVo;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	@Override
	public String getArkVoName() {
		// TODO Auto-generated method stub
		return this.objectId;
	}

}
